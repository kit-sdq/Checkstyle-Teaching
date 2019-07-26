package edu.kit.informatik;

import java.util.*;

public class Board {
    private Token[][] board = new Token[8][8];

    public Board() {
        for (int a = 0; a < board.length; a++) {
            for (int b = 0; b < board[0].length; b++) {
                board[a][b] = new Token(Players.None);
            }
        }
    }

    public String print() {
        String ret = "";
        for (Token[] row : board) {
            for (Token token : row) {
                ret =  ret + token.getLiteral() + " ";
            }
            ret = removeLastChar(ret);
            ret =  ret + "\n";
        }
        return removeLastChar(ret);
    }

    public String state(int x, int y) {
        return board[x][y].getLiteral();
    }

    private String removeLastChar(String str) {
        String newString = "";

        if (str != null && str.length() > 0) {
            newString = str.substring(0, str.length() - 1);
        }
        return newString;
    }

    public String throwin(int column, Token token) throws InputException {
        final int x = getRow(board, column, true);
        if (x == -1) throw new InputException("column is full");
        board[x][column] = token;
        String winner = getWinner(checkWin(x, column), false);
        if (winner != null) return winner;
        return "OK";
    }

    /// checkwin generically
    private Set<WinningRow> checkWin(int x, int y) {
        int[][][] operators = {
                {{0,1},{0,-1}},
                {{1,0},{-1,0}},
                {{1,1},{-1,-1}},
                {{-1,1},{1,-1}},
        };
        Set<WinningRow> wins = new HashSet<WinningRow>();
        final Players token = board[x][y].getPlayer();
        if (token == Players.None) return wins;
        for (int[][] operator : operators) {
            int win = 1;
            WinningRow perhapsWin = new WinningRow();
            perhapsWin.addPoint(new Point(x,y));
            sideFor: for (int[] side : operator){
                for (int i = 1 ; i < 8 ; i++) {
                    if (x + i*side[0] <8 && x + i*side[0] >=0
                            && y + i*side[1] <8 && y + i*side[1] >=0 ) {
                        if (token == board[x+ i*side[0]][y+ i*side[1]].getPlayer()) {
                            win++;
                            perhapsWin.addPoint(new Point(x+ i*side[0],y+ i*side[1]));
                        } else continue sideFor;
                    }
                }
            }
            if (win > 3) {
                wins.add(perhapsWin);
            }
        }
        return wins;
    }

    private int getRow(Token[][] board, int column, boolean throwin) throws InputException {
        for (int a = board[0].length - 1; a >= 0; a--) {
            if (board[a][column].getLiteral().equals(Players.None.getLiteral())) {
                return a;
            }
        }
        if (!throwin) {
            return 0;
        } else {
            return -1;
        }
    }

    private Set<WinningRow> checkWinWholeBoard() {
        Set<WinningRow> wins = new HashSet<WinningRow>();
        for (int a = 0; a<board.length; a++) {
            for (int b = 0; b<board[0].length; b++) {
                wins.addAll(checkWin(a, b));
            }
        }
        return wins;
    }

    private String getWinner(Set<WinningRow> wins, boolean count) {
        int counterP1 = 0;
        int counterP2 = 0;
        for (WinningRow win : wins) {
            Point coordinates = win.iterator().next();
            if (board[coordinates.getRow()][coordinates.getColumn()].getPlayer() == Players.P1)
                counterP1++;
            else counterP2++;
        }
        if (count) {
            if (counterP1 > counterP2)  return Players.P1.getLiteral() + " wins";
            else if (counterP1 == counterP2 && counterP1 != 0) return "draw";
            else if (counterP1 < counterP2) return Players.P2.getLiteral() + " wins";
            else if (!containsNoneTokens()) {
                return "draw";
            } return null;
        } else {
            if (counterP1 > 0 && counterP2 > 0) return "draw";
            else if (counterP1 > 0) return Players.P1.getLiteral() + " wins";
            else if (counterP2 > 0) return Players.P2.getLiteral() + " wins";
            else if (!containsNoneTokens()) {
                return "draw";
            } else return null;
        }

    }

    private boolean containsNoneTokens() {
        for (Token[] tokenLine : board) {
            for (Token token : tokenLine) {
                if (token.getPlayer().equals(Players.None)) return true;
            }
        }
        return false;
    }

    public String flip() throws InputException {
        Token[][] newBoard = new Token[board.length][board[0].length];

        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                newBoard[x][y] = board[(board.length - 1)  - x][ y];
            }
        }
        // let tokens fall down
        for (int x = board[0].length-1; x >= 0; x--) {
            for (int y = board[x].length-1; y >=0; y--) {
                if (!newBoard[x][y].getPlayer().equals(Players.None)) {
                    if (getRow(newBoard, y, true) != -1){
                        changeTokens(newBoard, x, getRow(newBoard, y, true), y);
                    }
                }
            }
        }

        board = newBoard;
        String winner = getWinner(checkWinWholeBoard(), true);
        if (winner != null) return winner;
        return "OK";
    }

    private void changeTokens(Token[][] tokens, int oldRow,  int newRow, int column) {
        Token tempToken = tokens[oldRow][column];
        if (newRow<tokens.length) {
            tokens[oldRow][column] = tokens[newRow][column];
            tokens[newRow][column] = tempToken;
        }
    }

    public String remove(int column, Player player) throws InputException {
        if (board[board.length - 1][column].getPlayer().equals(player.getPlayer())) {
            for (int a = board.length - 1; a >= 1; a--) {
                board[a][column] = board[a - 1][column];
            }
            board[0][column] = new Token(Players.None);
        } else {
            throw new InputException("you do not have a token at the bottom of this column !");
        }
        String winner = getWinner(checkWinWholeBoard(), false);
        if (winner != null) return winner;
        return "OK";
    }
}
