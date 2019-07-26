package edu.kit.informatik;

public class TicTacToe {
    private char[][] board;

    public TicTacToe() {
        this.board = new char[3][3];
    }

    public String simulateGame(int[] turns) {
        int currentTurn = 1;
        Player playerOne = new Player(1, 'X');
        Player playerTwo = new Player(2, 'O');

        for (int turn : turns) {
            Player currentPlayer = (currentTurn % 2 == 1) ? playerOne : playerTwo; 
            int winner = doTurn(turn, currentPlayer);
            if (winner != 0) {
                return "P" + currentPlayer.getNumber() + " wins " + currentTurn;
            }
            currentTurn++;
        }
        
        return "draw";
    }

    private int doTurn(int index, Player player) {
        int row = index / 3;
        int col = index % 3;
        board[row][col] = player.getToken();
        
        //Check winner horizontal
        if (board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
            return player.getNumber();
        }
        
        //Check winner vertical
        if (board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
            return player.getNumber();
        }
        
        //Check winner diagonal 1 if on the diagonal
        if (row == col && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return player.getNumber();
        }
        
        //Check winner diagonal 2 if on the diagonal
        if (row + col == 2 && board[2][0] == board[1][1] && board[1][1] == board[0][2]) {
            return player.getNumber();
        }
        
        return 0;
    }
}
