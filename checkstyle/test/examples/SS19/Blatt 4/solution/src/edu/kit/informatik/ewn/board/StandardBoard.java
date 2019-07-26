/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.ewn.board;

/**
 * Represents a standard board with hard borders.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public class StandardBoard extends Board {
    
    /**
     * Creates a new standard board.
     * @param size - the size of the board
     */
    public StandardBoard(final int size) {
        super(size);
    }

    @Override
    public int calculateRowIndex(final int rowIndex) {
        return rowIndex;
    }

    @Override
    public int calculateColumnIndex(final int columnIndex) {
        return columnIndex;
    }
}
