package edu.kit.informatik;

import edu.kit.informatik.wincheck.WinCheck;
import edu.kit.informatik.wincheck.WinCheckVertical;
import edu.kit.informatik.wincheck.WinCheckHorizontal;
import edu.kit.informatik.wincheck.WinCheckDiagonal;
import edu.kit.informatik.wincheck.WinCheckAntidiagonal;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ConnectSix {
    private Board board;
    private Queue<Player> players;
    private List<WinCheck> winChecks;
    private boolean gameOver;
    private BoardType boardType;

    /**
     * Constructs a new instance of the game with two players.
     */
    public ConnectSix(int playerCount, int boardSize, BoardType boardType) {
        this.boardType = boardType;
        init(playerCount, boardSize);
    }
    
    public void checkPlacement(int i, int j) throws InputException {
        if (gameOver || board.isFull()) {
            throw new InputException("the game is already over.");
        } 
        
        try {
            if (board.getOccupyingPlayer(i, j) != null) {
                throw new InputException("the cell (" + i + "," + j + ") is already occupied.");
            }
        } catch (CellOutOfBoundsException e) {
            throw new InputException("the cell (" + i + "," + j + ") is out of bounds.");
        }
    }

    public String place(int i1, int j1, int i2, int j2) {
        Player currentPlayer = players.peek();
        try {
            board.occupy(i1, j1, currentPlayer);
            board.occupy(i2, j2, currentPlayer);
        } catch (CellOutOfBoundsException e) {
            throw new IllegalStateException("Never call place without first calling checkPlacement!");
        }
        
        //Check if the player won
        for (WinCheck current : winChecks) {
            if (current.check(board, currentPlayer, i1, j1) || current.check(board, currentPlayer, i2, j2)) {
                gameOver = true;
                return "P" + currentPlayer.getNumber() + " wins";
            }
        }

        //Put current player at the end of the list
        players.add(players.remove());
        
        if (!board.isFull()) {
            return "OK";
        } else {
            return "draw";
        }
    }
    
    /**
     * Returns a string with a representation of the current board state.
     * 
     * @return The string representation of the board state.
     */
    public String print() {
        return board.toString();
    }
    
    public String rowprint(int row) throws InputException {
        String result = board.rowToString(row);
        if (result == null) {
            throw new InputException("row is out of bounds.");
        }
        
        return result;
    }

    public String colprint(int col) throws InputException {
        String result = board.colToString(col);
        if (result == null) {
            throw new InputException("row is out of bounds.");
        }
        
        return result;
    }

    /**
     * Returns a string containing the state (player occupying or empty) of the specified cell.
     * 
     * @param i The row coordinate of the cell to get the state of.
     * @param j The column coordinate of the cell to get the state of.
     * @return A string representation of the cells state.
     * @throws InputException if the call isn't allowed with that input. Contains an error message.
     */
    public String state(int i, int j) throws InputException {
        Cell cell;
        try {
            cell = board.getCell(i, j);
        } catch (CellOutOfBoundsException e) {
            throw new InputException("the requested cell is out of bounds.");
        }
        
        return cell.toString();
    }
    
    public void reset() {
        init(players.size(), board.getBoardSize());
    }
    
    private void init(int playerCount, int boardSize) {
        switch (boardType) {
            case STANDARD:
                this.board = new Board(boardSize);
                break;
            case TORUS:
                this.board = new TorusBoard(boardSize);
                break;
            default:
                throw new IllegalArgumentException("Unknown board type!");
        }

        this.players = new LinkedList<>();
        for (int i = 1; i <= playerCount; i++) {
            this.players.add(new Player(i, (char) ('0' + i)));
        }

        this.winChecks = new LinkedList<>();
        this.winChecks.add(new WinCheckHorizontal());
        this.winChecks.add(new WinCheckVertical());
        this.winChecks.add(new WinCheckDiagonal());
        this.winChecks.add(new WinCheckAntidiagonal());

        this.gameOver = false;
    }
}
