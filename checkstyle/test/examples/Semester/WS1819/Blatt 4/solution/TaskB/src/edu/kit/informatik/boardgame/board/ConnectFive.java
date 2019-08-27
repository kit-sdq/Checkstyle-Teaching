/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.boardgame.board;

import edu.kit.informatik.boardgame.InputException;
import edu.kit.informatik.boardgame.wincheck.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Encapsulates the game logic for the board game.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class ConnectFive {
    /**
     * Dimensions of the board.
     */
    private static final int BOARD_DIMENSIONS = 6;

    /**
     * Number of cells on the board.
     */
    private static final int CELL_COUNT = BOARD_DIMENSIONS * BOARD_DIMENSIONS;

    /**
     * The board to play the game with.
     */
    private Board board;

    /**
     * Queue of players to swap between them.
     */
    private Queue<Player> players;

    /**
     * Available win checks for further externsion.
     */
    private List<WinCheck> winChecks;

    /**
     * Available moves.
     */
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
     * Places a token of the player whose turn it is on the cell with the given
     * coordinates.
     *
     * @param i The row coordinate of the cell to place the token on.
     * @param j The column coordinate of the cell to place the token on.
     * @return A string containing a state change message to the user.
     * @throws InputException if the call isn't allowed with that input.
     *         Contains an error message.
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
                    if (currentPlayer != null) {
                        return "P" + currentPlayer.getNumber() + " wins";
                    }
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
            throw new InputException(
                    "you can't place your token here, try again (already "
                    + "occupied or out of bounds).");
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
     * Turns the quadrant with the given number 90 degrees.
     *
     * @param numberOfQuadrant the number of the quadrant
     * @return string to display to the user
     * @throws InputException occurs if the quadrant is invalid or the
     *         game is already over
     */
    public String turnRight(int numberOfQuadrant) throws InputException {
        if (moveCount == CELL_COUNT) {
            throw new InputException("the game is already over.");
        } else if (board.turnRight(numberOfQuadrant)) {
            //Check if the player won
            return checkBoardQuarter(numberOfQuadrant);
        } else {
            throw new InputException(
                    "you can't place your token here, try again (already "
                    + "occupied or out of bounds).");
        }
    }

    /**
     * Performs win checks for a whole quarter after it was turned.
     *
     * @param numberOfQuadrant the number of the quadrant
     * @return string to display to the user
     * @throws InputException occurs if the data is invalid
     */
    private String checkBoardQuarter(int numberOfQuadrant)
            throws InputException {
        int[][] affectedCellIndexes = board.getCellIndexes(numberOfQuadrant);
        // check for the current player
        String outputActivePlayer = checkBoardQuarterWithPlayer(
                affectedCellIndexes, players.peek());

        //Put current player at the end of the list
        players.add(players.remove());

        // players are switched so we can check for the second player
        String outputNewPlayer = checkBoardQuarterWithPlayer(
                affectedCellIndexes, players.peek());

        if (outputActivePlayer != null && outputNewPlayer != null) {
            // both players have a winning row (not requested in the task)
            // placing or turning is now not allowed anymore
            moveCount = CELL_COUNT;
            // return draw
            return "draw";
        } else if (outputActivePlayer != null) {
            return outputActivePlayer;
        } else if (outputNewPlayer != null) {
            return outputNewPlayer;
        }

        if (++moveCount != CELL_COUNT) {
            return "OK";
        } else {
            return "draw";
        }
    }

    private String checkBoardQuarterWithPlayer(
            final int[][] affectedCellIndexes, final Player currentPlayer) {
        for (WinCheck current : winChecks) {
            for (int[] cellIndex : affectedCellIndexes) {
                if (current.check(board, currentPlayer, cellIndex[0],
                        cellIndex[1])) {
                    moveCount = CELL_COUNT;
                    return "P" + currentPlayer.getNumber() + " wins";
                }
            }
        }
        return null;
    }

    /**
     * Turns the quadrant with the given number -90 degrees.
     *
     * @param numberOfQuadrant the number of the quadrant
     * @return string to display to the user
     * @throws InputException occurs if the quadrant is invalid or the
     *         game is already over
     */
    public String turnLeft(int numberOfQuadrant) throws InputException {
        if (moveCount == CELL_COUNT) {
            throw new InputException("the game is already over.");
        } else if (board.turnLeft(numberOfQuadrant)) {
            //Check if the player won
            return checkBoardQuarter(numberOfQuadrant);
        } else {
            throw new InputException(
                    "you can't place your token here, try again (already "
                    + "occupied or out of bounds).");
        }
    }

    /**
     * Returns the placed tokens of the given player in a readable format.
     *
     * @param playerId the id of the player
     * @return textual representation of the placed tokens of this player
     */
    public String token(int playerId) {
        return board.token(playerId);
    }

    /**
     * Returns a string containing the state (player occupying or empty) of the
     * specified cell.
     *
     * @param i The row coordinate of the cell to get the state of.
     * @param j The column coordinate of the cell to get the state of.
     * @return A string representation of the cells state.
     * @throws InputException if the call isn't allowed with that input.
     *         Contains an error message.
     */
    public String state(int i, int j) throws InputException {
        Cell cell = board.getCell(i, j);

        if (cell == null) {
            throw new InputException("the requested cell is out of bounds.");
        }

        return cell.toString();
    }
}
