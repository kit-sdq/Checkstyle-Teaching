/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.ewn.board;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.StringJoiner;
import java.util.TreeSet;

/**
 * Represents the game board.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public abstract class Board {
    private final int boardSize;
    private Tile[][] board;
    private Map<Player, SortedSet<Token>> tokenMap;

    /**
     * Creates a new board initialized without any players' tokens.
     * 
     * @param size
     *            - the size of the board
     */
    public Board(final int size) {
        this.boardSize = size;
        this.board = new Tile[size][size];
        for (int row = 0; row < this.boardSize; row++) {
            for (int column = 0; column < this.boardSize; column++) {
                this.board[row][column] = new Tile(new Token(Player.NONE, 0));
            }
        }
        this.tokenMap = new HashMap<>();
        for (final Player player : Player.values()) {
            this.tokenMap.put(player, new TreeSet<>());
        }
    }

    /**
     * Gets the token at the given position.
     * 
     * @param row
     *            - the row of the position
     * @param column
     *            - the column of the position
     * @return the token on the given cell
     * @throws IllegalArgumentException
     *             - if coordinates are invalid
     */
    public Token getCell(final int row, final int column) throws IllegalArgumentException {
        checkCoordinates(row, column);
        return this.board[calculateRowIndex(row)][calculateColumnIndex(column)].getToken();
    }

    /**
     * Sets the given cell to the given token and removes the token which was
     * formerly placed on this cell.
     * 
     * @param token
     *            - the given token
     * @param row
     *            - the row of the cell
     * @param column
     *            - the column of the cell
     * @throws IllegalArgumentException
     *             - if coordinates are invalid
     */
    public void setCell(final Token token, final int row, final int column) throws IllegalArgumentException {
        checkCoordinates(row, column);
        final Token beforeSet = this.board[calculateRowIndex(row)][calculateRowIndex(column)].getToken();
        this.tokenMap.get(beforeSet.getPlayer()).remove(beforeSet);
        this.tokenMap.get(token.getPlayer()).add(token);
        this.board[calculateRowIndex(row)][calculateColumnIndex(column)].setToken(token);
    }

    /**
     * Moves the given token to the given coordinates if and only if the move is
     * allowed. The token must exist and be a player's token.
     * 
     * @param token
     *            - the token to be moved
     * @param row
     *            - the row to which the token should be moved
     * @param column
     *            - the column to which the token should be moved
     * @throws IllegalArgumentException
     *             - if coordinates are invalid for this move
     */
    public void move(final Token token, final int row, final int column) throws IllegalArgumentException {
        checkCoordinates(row, column);

        int rowOld = -1;
        int columnOld = -1;
        outer: for (int x = 0; x < this.boardSize; x++) {
            for (int y = 0; y < this.boardSize; y++) {
                if (this.board[x][y].getToken().equals(token)) {
                    rowOld = x;
                    columnOld = y;
                    break outer;
                }
            }
        }
        assert rowOld >= 0;

        final Player player = token.getPlayer();
        checkMove(player, rowOld, columnOld, row, column);
        clearCell(rowOld, columnOld);
        setCell(token, row, column);
    }

    private void checkMove(final Player player, final int rowOld, final int columnOld, final int rowNew,
            final int columnNew) throws IllegalArgumentException {
        final boolean[] okArray;
        if (player == Player.ONE) {
            okArray = new boolean[] {
                    rowEquals(rowOld, rowNew) && columnEquals(columnOld + 1, columnNew),
                    rowEquals(rowOld + 1, rowNew) && columnEquals(columnOld, columnNew),
                    rowEquals(rowOld + 1, rowNew) && columnEquals(columnOld + 1, columnNew) };
        } else {
            okArray = new boolean[] {
                    rowEquals(rowOld, rowNew) && columnEquals(columnOld - 1, columnNew),
                    rowEquals(rowOld - 1, rowNew) && columnEquals(columnOld, columnNew),
                    rowEquals(rowOld - 1, rowNew) && columnEquals(columnOld - 1, columnNew) };
        }
        boolean okComplete = false;
        for (final boolean ok : okArray) {
            if (ok) {
                okComplete = true;
                break;
            }
        }
        if (!okComplete) {
            throw new IllegalArgumentException("move not possible");
        }
    }

    private boolean rowEquals(final int rowOne, final int rowTwo) {
        return calculateRowIndex(rowOne) == calculateRowIndex(rowTwo);
    }

    private boolean columnEquals(final int columnOne, final int columnTwo) {
        return calculateColumnIndex(columnOne) == calculateColumnIndex(columnTwo);
    }

    private void clearCell(final int row, final int column) throws IllegalArgumentException {
        checkCoordinates(row, column);
        final Token beforeClear = this.board[row][column].getToken();
        this.tokenMap.get(beforeClear.getPlayer()).remove(beforeClear);
        this.board[calculateRowIndex(row)][calculateColumnIndex(column)].clearTile();
    }

    /**
     * Gets a sorted set of a player's tokens.
     * 
     * @param player
     *            - the player
     * @return a sorted set of the player's tokens
     */
    public SortedSet<Token> getTokens(final Player player) {
        return new TreeSet<Token>(this.tokenMap.get(player));
    }

    /**
     * Gets the board size.
     * 
     * @return the board size as an int
     */
    protected int getBoardSize() {
        return this.boardSize;
    }

    /**
     * Calculates the valid row index from the given index.
     * 
     * @param rowIndex
     *            - the given row index
     * @return the valid row index
     */
    public abstract int calculateRowIndex(int rowIndex);

    /**
     * Calculates the valid column index from the given index.
     * 
     * @param columnIndex
     *            - the given column index
     * @return the valid column index
     */
    public abstract int calculateColumnIndex(int columnIndex);

    @Override
    public String toString() {
        StringJoiner completeBoardRepresentation = new StringJoiner("\n");
        for (int rowIndex = 0; rowIndex < this.board.length; rowIndex++) {
            completeBoardRepresentation.add(rowToString(rowIndex).toString());
        }
        return completeBoardRepresentation.toString();
    }

    private String rowToString(final int rowIndex) {
        StringJoiner rowRepresentation = new StringJoiner(" ");
        for (Tile tile : board[rowIndex]) {
            rowRepresentation.add(tile.toString());
        }
        return rowRepresentation.toString();
    }

    private void checkCoordinates(final int row, final int column) throws IllegalArgumentException {
        final String rowChecked = checkRow(row);
        final String columnChecked = checkColumn(column);
        final String message = (rowChecked != null) ? rowChecked : columnChecked;
        if (message != null) {
            throw new IllegalArgumentException(message);
        }
    }

    private String checkRow(final int row) {
        final int calculatedRow = calculateRowIndex(row);
        if (calculatedRow < 0 || calculatedRow >= this.boardSize) {
            return "invalid row index: " + calculatedRow;
        }
        return null;
    }

    private String checkColumn(final int column) {
        final int calculateColumn = calculateColumnIndex(column);
        if (calculateColumn < 0 || calculateColumn >= this.boardSize) {
            return "invalid column index: " + calculateColumn;
        }
        return null;
    }
}
