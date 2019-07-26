/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.boardgame.board;

import edu.kit.informatik.boardgame.InputException;

import java.util.StringJoiner;

/**
 * Encapsulates a board like stated in task B on the assignment.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class Board {
    /**
     * Array of board quarters.
     */
    private BoardQuarter[] board;

    /**
     * width of the board.
     */
    private int width;

    /**
     * height of the board.
     */
    private int height;

    /**
     * The constructor for a new playing board consisting of cells.
     *
     * @param width The width of the board.
     * @param height The height of the board.
     */
    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.board = new BoardQuarter[4];
        initiateBoardQuarter(0, 0, 0);
        initiateBoardQuarter(1, 0, height / 2);
        initiateBoardQuarter(2, width / 2, 0);
        initiateBoardQuarter(3, width / 2, height / 2);
    }

    /**
     * Initiates the board quarter with the given index and the given offsets.
     *
     * @param index the index of the board quarter
     * @param offsetX offset on the x axis
     * @param offsetY offset on the y axis
     */
    private void initiateBoardQuarter(int index, int offsetX, int offsetY) {
        this.board[index] = new BoardQuarter(width / 2, height / 2, offsetX,
                offsetY);
    }

    /**
     * Gets a cell of the board with specific coordinates.
     *
     * @param i The row coordinate of the cell to get.
     * @param j The column coordinate of the cell to get.
     * @return The cell.
     */
    Cell getCell(int i, int j) {
        return board[getQuarter(i, j)].getCell(i, j);
    }

    /**
     * Gets the player that occupies a cell with specified coordinates.
     *
     * @param i The row coordinate of the cell to get the occupying
     *         player from.
     * @param j The column coordinate of the cell to get the occupying
     *         player from.
     * @return The player occupying the cell or <code>null</code> if the cell is
     *         empty.
     */
    public Player getOccupyingPlayer(int i, int j) {
        return board[getQuarter(i, j)].getOccupyingPlayer(i, j);
    }

    /**
     * Occupies the cell with the specified coordinates with a given player.
     *
     * @param i The row coordinate of the cell to get occupied.
     * @param j The column coordinate of the cell to get occupied.
     * @param player The player to occupy the cell.
     * @return true if the player occupies the cell now, false if the cell isn't
     *         empty.
     */
    boolean occupy(int i, int j, Player player) {
        return this.board[getQuarter(i, j)].occupy(i, j, player);
    }

    /**
     * Turns the quadrant with the given number 90 degrees.
     *
     * @param numberOfQuadrant the number of the quadrant
     * @return string to display to the user
     * @throws InputException occurs if the quadrant number is invalid
     */
    boolean turnRight(int numberOfQuadrant) throws InputException {
        if (isNoQuadrant(numberOfQuadrant)) {
            throw new InputException("illegal quadrant number");
        }
        board[getIndexFromQuarter(numberOfQuadrant)].turnRight();
        return true;
    }

    /**
     * Turns the quadrant with the given number -90 degrees.
     *
     * @param numberOfQuadrant the number of the quadrant
     * @return string to display to the user
     * @throws InputException occurs if the quadrant number is invalid
     */
    boolean turnLeft(int numberOfQuadrant) throws InputException {
        if (isNoQuadrant(numberOfQuadrant)) {
            throw new InputException("illegal quadrant number");
        }
        board[getIndexFromQuarter(numberOfQuadrant)].turnLeft();
        return true;
    }

    /**
     * Returns all cell coordinates the given player has tokens placed on in
     * a readable format.
     *
     * @param playerId the id of the player to show the tokens
     * @return list of the tokens the player has placed
     */
    public String token(int playerId) {
        Cell[][] completeBoard = getCompleteBoard();
        StringJoiner result = new StringJoiner(",");
        for (int i = 0; i < completeBoard.length; i++) {
            for (int j = 0; j < completeBoard[0].length; j++) {
                if (completeBoard[i][j].getPlayer() != null
                    && completeBoard[i][j].getPlayer().getNumber()
                       == playerId) {
                    result.add("(" + i + ";" + j + ")");
                }
            }
        }
        return result.toString();
    }

    /**
     * Returns all cell indexes of the given quadrant.
     *
     * @param numberOfQuadrant the number of the quadrant
     * @return int array with all cell indexes in the specified quadrant
     * @throws InputException occurs if the quadrant number is invalid
     */
    int[][] getCellIndexes(int numberOfQuadrant) throws InputException {
        if (isNoQuadrant(numberOfQuadrant)) {
            throw new InputException("illegal quadrant number");
        }
        return board[getIndexFromQuarter(numberOfQuadrant)].getCellIndexes();
    }

    @Override
    public String toString() {
        Cell[][] completeBoard = getCompleteBoard();
        StringJoiner result = new StringJoiner("\n");
        for (Cell[] row : completeBoard) {
            StringJoiner rowJoiner = new StringJoiner(" ");
            for (Cell cell : row) {
                rowJoiner.add(cell.toString());
            }
            result.add(rowJoiner.toString());
        }

        return result.toString();
    }

    /**
     * @return the complete board as {@link Cell} array
     */
    private Cell[][] getCompleteBoard() {
        Cell[][] completeBoard = new Cell[width][height];
        for (int i = 0; i < 4; i++) {
            for (int x = 0; x < width / 2; x++) {
                for (int y = 0; y < height / 2; y++) {
                    completeBoard[x + board[i].getOffsetX()][y + board[i]
                            .getOffsetY()] = board[i]
                            .getCell(x + board[i].getOffsetX(),
                                    y + board[i].getOffsetY());
                }
            }
        }
        return completeBoard;
    }

    /**
     * Returns the quarter index in the board array of the given cell
     * coordinates. NO bound checks are performed, thus the coordinates may
     * be outside the board quarter.
     *
     * @param x x coordinate of the cell
     * @param y y coordinate of the cell
     * @return the quarter the cell could be in, no bound checks are used
     */
    private int getQuarter(int x, int y) {
        if (x >= this.width / 2) {
            if (y >= this.height / 2) {
                return getIndexFromQuarter(4);
            } else {
                return getIndexFromQuarter(3);
            }
        } else {
            if (y >= this.height / 2) {
                return getIndexFromQuarter(2);
            } else {
                return getIndexFromQuarter(1);
            }
        }
    }

    /**
     * @param numberOfQuadrant the number of the quadrant
     * @return the index corresponding to the quarter number
     */
    private int getIndexFromQuarter(int numberOfQuadrant) {
        return numberOfQuadrant - 1;
    }

    /**
     * @param numberOfQuadrant the number of the quadrant
     * @return if the number is valid (between 1 and 4, both included)
     */
    private boolean isNoQuadrant(int numberOfQuadrant) {
        return numberOfQuadrant <= 0 || numberOfQuadrant >= 5;
    }
}
