/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.dawn.data;

import edu.kit.informatik.dawn.InputException;
import edu.kit.informatik.dawn.ui.InteractionStrings;

import java.util.StringJoiner;

/**
 * @author Thomas Weber
 * @version 1.0
 */
public class Board {

    public static int NUMBER_COLUMNS = 14;

    public static int NUMBER_ROWS = 10;

    public static int NUMBER_ROWS_MIN = 0;

    public static int NUMBER_COLS_MIN = 0;

    public static int MAX_COL_DAWN = 20;

    public static int MIN_COL_DAWN = -6;

    public static int MAX_ROW_DAWN = 16;

    public static int MIN_ROW_DAWN = -6;

    private BoardState[][] board;

    public Board() {
        this.board = new BoardState[NUMBER_ROWS + 1][NUMBER_COLUMNS + 1];
        for (int row = 0; row < NUMBER_ROWS + 1; row++) {
            for (int col = 0; col < NUMBER_COLUMNS + 1; col++) {
                board[row][col] = BoardState.EMPTY;
            }
        }
    }

    public BoardState getCellState(Coordinates coordinates) {
        return board[coordinates.getRowIndex().getIndex()][coordinates
                .getColIndex().getIndex()];
    }

    public String placeCell(Coordinates coordinates, BoardState boardState)
            throws InputException {
        if (coordinates.getRowIndex().getIndex() > NUMBER_ROWS
            || coordinates.getColIndex().getIndex() > NUMBER_COLUMNS
            || coordinates.getRowIndex().getIndex() < NUMBER_ROWS_MIN
            || coordinates.getColIndex().getIndex() < NUMBER_COLS_MIN) {
            return InteractionStrings.OK.toString();
        }
        if (board[coordinates.getRowIndex().getIndex()][coordinates
                .getColIndex().getIndex()] != BoardState.EMPTY) {
            throw new InputException("cell already occupied");
        }
        this.board[coordinates.getRowIndex().getIndex()][coordinates
                .getColIndex().getIndex()] = boardState;
        return InteractionStrings.OK.toString();
    }

    public String placeCellIgnoring(Coordinates coordinates,
            BoardState boardState) {
        this.board[coordinates.getRowIndex().getIndex()][coordinates
                .getColIndex().getIndex()] = boardState;
        return InteractionStrings.OK.toString();
    }

    public String print() {
        StringJoiner ret = new StringJoiner(
                InteractionStrings.OUTPUT_SEPARATOR_OUTER.toString());
        for (BoardState[] row : board) {
            StringJoiner rowString = new StringJoiner("");
            for (BoardState cell : row) {
                rowString.add(cell.toString());
            }
            ret.add(rowString.toString());
        }
        return ret.toString();
    }

    public String print(RowIndex rowIndex) {
        StringJoiner rowString = new StringJoiner(
                InteractionStrings.OUTPUT_SEPARATOR_INNER.toString());
        for (BoardState cell : board[rowIndex.getIndex()]) {
            rowString.add(cell.toString());
        }
        return rowString.toString();
    }

    public String print(ColIndex colIndex) {
        StringJoiner colString = new StringJoiner(
                InteractionStrings.OUTPUT_SEPARATOR_INNER.toString());
        for (int row = 0; row < Board.NUMBER_ROWS; row++) {
            colString.add(board[row][colIndex.getIndex()].toString());
        }
        return colString.toString();
    }

    public Coordinates getPlanet(final BoardState boardState)
            throws InputException {
        for (int row = 0; row <= NUMBER_ROWS; row++) {
            for (int col = 0; col <= NUMBER_COLUMNS; col++) {
                if (board[row][col] == boardState) {
                    return new Coordinates(new RowIndex(row),
                            new ColIndex(col));
                }
            }
        }
        throw new InputException("Something went wrong");
    }
}
