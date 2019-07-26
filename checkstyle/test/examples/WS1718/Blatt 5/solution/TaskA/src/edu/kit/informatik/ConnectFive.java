package edu.kit.informatik;

import edu.kit.informatik.wincheck.WinCheck;
import edu.kit.informatik.wincheck.WinCheckVertical;
import edu.kit.informatik.wincheck.WinCheckHorizontal;
import edu.kit.informatik.wincheck.WinCheckDiagonal;
import edu.kit.informatik.wincheck.WinCheckAntidiagonal;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ConnectFive {
    private static final int BOARD_DIMENSIONS = 15;
    private static final int CELL_COUNT = BOARD_DIMENSIONS * BOARD_DIMENSIONS;
    
    private Board board;
    private Queue<Player> players;
    private List<WinCheck> winChecks;
    private int moveCount;

    /**
     * Constructs a new instance of the game with two players.
     */
    public ConnectFive() {
        this.board = new Board(BOARD_DIMENSIONS, BOARD_DIMENSIONS);
        this.players = new LinkedList<>();
        this.players.add(new Player(1, '1'));
        this.players.add(new Player(2, '2'));
        
        this.winChecks = new LinkedList<>();
        this.winChecks.add(new WinCheckHorizontal());
        this.winChecks.add(new WinCheckVertical());
        this.winChecks.add(new WinCheckDiagonal());
        this.winChecks.add(new WinCheckAntidiagonal());
    }

    /**
     * Places a token of the player whose turn it is on the cell with the given coordinates.
     * 
     * @param i The row coordinate of the cell to place the token on.
     * @param j The column coordinate of the cell to place the token on.
     * @return A string containing a state change message to the user.
     * @throws InputException if the call isn't allowed with that input. Contains an error message.
     */
    public String place(int i, int j) throws InputException {
        Player currentPlayer = players.peek();
        
        if (moveCount == CELL_COUNT) {
            throw new InputException("the game is already over.");
        } else if (board.occupy(i, j, currentPlayer)) {
            //Check if the player won
            for (WinCheck current : winChecks) {
                if (current.check(board, currentPlayer, i, j)) {
                    moveCount = CELL_COUNT;
                    return "P" + currentPlayer.getNumber() + " wins";
                }
            }
            
            //Put current player at the end of the list
            players.add(players.remove());
            
            if (++moveCount != CELL_COUNT) {
                return "OK";
            } else {
                return "draw";
            }
        } else {
            throw new InputException("you can't place your token here, try again (already occupied or out of bounds).");
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

    /**
     * Returns a string containing the state (player occupying or empty) of the specified cell.
     * 
     * @param i The row coordinate of the cell to get the state of.
     * @param j The column coordinate of the cell to get the state of.
     * @return A string representation of the cells state.
     * @throws InputException if the call isn't allowed with that input. Contains an error message.
     */
    public String state(int i, int j) throws InputException {
        Cell cell = board.getCell(i, j);
        
        if (cell == null) {
            throw new InputException("the requested cell is out of bounds.");
        }
        
        return cell.toString();
    }
}
