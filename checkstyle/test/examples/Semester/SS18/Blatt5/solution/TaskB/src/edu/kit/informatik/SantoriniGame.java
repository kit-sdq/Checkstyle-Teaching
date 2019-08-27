package edu.kit.informatik;

import edu.kit.informatik.exceptions.IllegalMoveException;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;

import static java.lang.String.format;

/**
 * A UI of santorini. Contains the playing board and performs moves on it.
 * <p>
 * Note that all methods taking a row and column number expect the number to be at least 0 and lower than the board's
 * size. The method's behavior is undefined otherwise.
 * 
 * @author Peter Oettig
 * @version 1.0
 */
public class SantoriniGame {
    private GameState gameState;

    private final List<Pawn> pawns;
    private final Queue<Player> players;
    private Player movingPlayer;
    private Pawn lastMovedPawn;

    private final Cell[][] board;
    private Player winner = null;

    /**
     * Creates a UI of santorini with a board sized {@code boardSize x boardSize}.
     *
     * @param players
     *            The queue of players that are participating.
     * @param pawns
     *            The list of pawns that are on the board.
     * @param boardSize
     *            The size of the quadratic UI board.
     */
    public SantoriniGame(Queue<Player> players, List<Pawn> pawns, int boardSize) {
        this.gameState = GameState.BEFORE_MOVE;

        this.pawns = pawns;
        this.players = players;
        this.movingPlayer = players.remove();

        board = new Cell[boardSize][boardSize];
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                board[row][column] = new Cell(row, column);
            }
        }
    }

    /**
     * Performs a move of {@code pawn} to the cell at {@code row:column}. Checks whether the move itself is valid, but
     * no whether {@code pawn} is allowed to perform the move at the moment.
     * 
     * @param pawnName
     *            The pawn identifier of the pawn to move.
     * @param row
     *            The destination cell's row.
     * @param column
     *            The destination cell's column.
     * @throws IllegalMoveException
     *             If the move is not allowed. This is for example the case if:
     *             <ul>
     *             <li>the cell at {@code row:column} is not a neighbor cell of {@code pawn}'s momentary visited cell.
     *             <li>the cell at {@code row:column} cannot be entered by {@code pawn} because it
     *             <ul>
     *             <li>is to high
     *             <li>is already visited
     *             <li>has a dome
     *             </ul>
     *             </ul>
     */
    public void move(String pawnName, int row, int column) throws IllegalMoveException {
        Pawn pawn = convertStringToPawn(pawnName);
        Cell cell = getCell(row, column);
        pawn.moveTo(cell);
        revalidateWinningState(cell);
        gameState = GameState.IN_MOVE;
        lastMovedPawn = pawn;
    }

    /**
     * Whether the UI is won. This is the case if a pawn is on a cell that has a tower height of
     * {@link Cell#MAX_TOWER_HEIGHT}.
     * 
     * @return {@code true} only if the UI is one.
     */
    public boolean isWon() {
        return winner != null;
    }

    /**
     * Returns the UI's winner.
     * 
     * @return If {@code #isWon()} returns {@code true}, the UI's winner. {@code null} otherwise.
     */
    public Player getWinner() {
        return winner;
    }

    /**
     * Internal method: Checks {@code cell} if it contains a winning pawn. Sets the UI's winning state accordingly. To
     * be called when a cell is entered.
     * 
     * @param cell
     *            The cell that was entered.
     */
    private void revalidateWinningState(Cell cell) {
        if (winner == null && cell.isVisited() && cell.getTowerHeight() == Cell.MAX_TOWER_HEIGHT) {
            winner = cell.getVisitor().getOwner();
        }
    }

    /**
     * Performs the building of a dome by the last moved {@code pawn}.
     * Checks whether the build itself is valid, but not whether
     * {@code pawn} is allowed to perform the build at the moment.
     * 
     * @param row
     *            The row of the cell the last moved {@code pawn} wants to build on.
     * @param column
     *            The column of the cell the last moved {@code pawn} wants to build on.
     * @throws IllegalMoveException
     *             If the move is not allowed. This is for example the case if:
     *             <ul>
     *             <li>the cell at {@code row:column} is not a neighbor cell of {@code pawn}'s momentary visited cell.
     *             <li>no dome can be built on the cell at {@code row:column} because it
     *             <ul>
     *             <li>has not a tower that's not {@link Cell#MAX_TOWER_HEIGHT} high.
     *             <li>is visited
     *             <li>already has a dome
     *             </ul>
     *             </ul>
     *             <li>
     */
    public void buildDome(int row, int column) throws IllegalMoveException {
        lastMovedPawn.buildDome(getCell(row, column));
        gameState = GameState.AFTER_MOVE;
    }

    /**
     * Performs the building of a cuboid by the last moved {@code pawn}.
     * Checks whether the build itself is valid, but not whether
     * {@code pawn} is allowed to perform the build at the moment.
     * 
     * @param row
     *            The row of the cell the last moved {@code pawn} wants to build on.
     * @param column
     *            The column of the cell the last moved {@code pawn} wants to build on.
     * @throws IllegalMoveException
     *             If the move is not allowed. This is for example the case if:
     *             <ul>
     *             <li>the cell at {@code row:column} is not a neighbor cell of {@code pawn}'s momentary visited cell.
     *             <li>no cuboid can be built on the cell at {@code row:column} because it
     *             <ul>
     *             <li>already has a tower that's {@link Cell#MAX_TOWER_HEIGHT} high.
     *             <li>is visited
     *             <li>has a dome
     *             </ul>
     *             </ul>
     *             <li>
     */
    public void buildCuboid(int row, int column) throws IllegalMoveException {
        lastMovedPawn.buildCuboid(getCell(row, column));
        gameState = GameState.AFTER_MOVE;
    }

    /**
     * Returns a string representation of the cell at {@code row:column}.
     * 
     * @param row
     *            The requested cell's row.
     * @param column
     *            The requested cell's column.
     * @return A String representation of the cell at {@code row:column}. It contains all elements on the cell from
     *         bottom to top. {@code "C"} is printed for a cuboid, {@code "D"} is printed for a dome.
     * @throws IllegalMoveException if row or column is invalid.
     */
    public String getCellPrint(int row, int column) throws IllegalMoveException {
        return getCell(row, column).toString();
    }

    /**
     * Places {@code pawn} – which was previously not in the UI – on the board at {@code row:column}.
     * This method check's whether placing {@code pawn} is a valid move, but not whether the move
     * is allowed at this moment. The UI's behaviour becomes undefined if a pawn that's already part
     * of the UI is re-added through this method.
     * 
     * @param pawn
     *            The pawn to add.
     * @param row
     *            The row of the cell the pawn is to be added on.
     * @param column
     *            The column of the cell the pawn is to be added on.
     * @throws IllegalMoveException
     *             If adding {@code pawn} at {@code row:column} is not allowed. This if for example the case if
     *             <ul>
     *             <li>the cell at {@code row:column} is already visited.
     *             </ul>
     */
    public void placePawn(Pawn pawn, int row, int column) throws IllegalMoveException {
        if (!pawns.contains(pawn)) {
            throw new IllegalMoveException("this pawn doesn't exist in the game.");
        }

        Cell cell = getCell(row, column);
        if (cell.isVisited()) {
            throw new IllegalMoveException(format("%s is already visited by '%s'", cell.getDescription(),
                cell.getVisitor()));
        }
        pawn.startOnCell(cell);
        cell.setVisitor(pawn);
    }

    /**
     * Switches to the next player.
     *
     * @return The string representation of the player thats turn it is now.
     */
    public String nextPlayer() {
        players.add(movingPlayer);
        movingPlayer = players.remove();
        gameState = GameState.BEFORE_MOVE;

        return movingPlayer.toString();
    }

    /**
     * Asserts that the state of the game is one of a list of allowed states.
     *
     * @param allowedStates The list of allowed states as individual parameters.
     * @throws IllegalMoveException if the state of the game is not one of the allowed ones.
     */
    public void assertState(GameState... allowedStates) throws IllegalMoveException {
        if (!Arrays.asList(allowedStates).contains(gameState)) {
            throw new IllegalMoveException(gameState.getStateRequirement(movingPlayer));
        }
    }

    private Cell getCell(int row, int column) throws IllegalMoveException {
        if (row < 0 || row >= board.length || column < 0 || column >= board[row].length) {
            throw new IllegalMoveException("the coordinates aren't valid");
        } else {
            return board[row][column];
        }
    }

    private Pawn convertStringToPawn(String pawnName) throws IllegalMoveException {
        Pawn pawn = null;
        for (Pawn currentPawn : pawns) {
            if (currentPawn.getName().equals(pawnName) && currentPawn.getOwner().equals(movingPlayer)) {
                pawn = currentPawn;
            }
        }

        if (pawn == null) {
            throw new IllegalMoveException(
                    format("there is no pawn '%s' for the current player in this game.", pawnName)
            );
        }

        return pawn;
    }
}
