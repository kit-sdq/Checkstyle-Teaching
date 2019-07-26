/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.boardgame.board;

import java.util.StringJoiner;

/**
 * Encapsulates a board quarter.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class BoardQuarter {
    /**
     * Array to save the encapsulated cells.
     */
    private Cell[][] board;

    /**
     * Offset to determine the position in the whole board at the x axis.
     */
    private int offsetX;

    /**
     * Offset to determine the position in the whole board at the x axis.
     */
    private int offsetY;

    /**
     * The width of this board quarter.
     */
    private int width;

    /**
     * The height of this board quarter.
     */
    private int height;

    /**
     * The constructor for a new playing board quarter consisting of cells.
     *
     * @param width The width of the board.
     * @param height The height of the board.
     * @param offsetX x axis offset
     * @param offsetY y axis offset
     */
    public BoardQuarter(int width, int height, int offsetX, int offsetY) {
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.board = new Cell[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                board[i][j] = new Cell();
            }
        }
    }

    /**
     * Gets a cell of the board with specific coordinates.
     *
     * @param i The row coordinate of the cell to get in the whole
     *         board.
     * @param j The column coordinate of the cell to get in the whole
     *         board.
     * @return The cell.
     */
    Cell getCell(int i, int j) {
        if (checkBounds(i, j)) {
            return null;
        }
        return board[i % width][j % height];
    }

    /**
     * @return the x axis offset of this quarter
     */
    int getOffsetX() {
        return offsetX;
    }

    /**
     * @return the y axis offset of this quarter
     */
    int getOffsetY() {
        return offsetY;
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
    Player getOccupyingPlayer(int i, int j) {
        if (checkBounds(i, j)) {
            return null;
        }
        Cell cell = getCell(i, j);

        if (cell == null) {
            return null;
        }

        return getCell(i, j).getPlayer();
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
        if (checkBounds(i, j)) {
            return false;
        }
        Cell cell = getCell(i, j);

        if (cell == null || cell.getPlayer() != null) {
            return false;
        }

        cell.setPlayer(player);
        return true;
    }

    /**
     * Turns this quarter 90 degrees.
     */
    void turnRight() {
        Cell[][] newBoard = new Cell[board.length][board[0].length];
        for (int i = 0; i < newBoard.length; i++) {
            for (int j = 0; j < newBoard[i].length; j++) {
                newBoard[i][j] = board[(newBoard[i].length - 1) - j][i];
            }
        }
        this.board = newBoard;
    }

    /**
     * Turns this quarter -90 degrees.
     */
    void turnLeft() {
        Cell[][] newBoard = new Cell[board.length][board[0].length];
        for (int i = 0; i < newBoard.length; i++) {
            for (int j = 0; j < newBoard[i].length; j++) {
                newBoard[i][j] = board[j][(newBoard[i].length - 1) - i];
            }
        }
        this.board = newBoard;
    }

    /**
     * @return int array with all cell indexes of this quarter in the whole
     *         board, with offsets, may be used for a win check after a turn.
     */
    int[][] getCellIndexes() {
        int[][] cellIndexes = new int[width * height][2];
        int index = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cellIndexes[index][0] = offsetX + x;
                cellIndexes[index][1] = offsetY + y;
                index++;
            }
        }
        return cellIndexes;
    }

    @Override
    public String toString() {
        StringJoiner result = new StringJoiner("\n");
        for (Cell[] row : board) {
            StringJoiner rowJoiner = new StringJoiner(" ");
            for (Cell cell : row) {
                rowJoiner.add(cell.toString());
            }
            result.add(rowJoiner.toString());
        }

        return result.toString();
    }

    /**
     * Checks if the given cell coordinates are inside this quarter.
     *
     * @param x x coordinate of the cell
     * @param y y coordinate of the cell
     * @return whether or not the cell is inside this quarter
     */
    private boolean checkBounds(int x, int y) {
        return x < offsetX || y < offsetY || x >= (offsetX + width) || y >= (
                offsetY + height);
    }
}
