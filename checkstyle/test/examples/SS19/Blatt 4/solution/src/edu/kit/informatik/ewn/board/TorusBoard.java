/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.ewn.board;

/**
 * Represents a torus board with soft borders.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public class TorusBoard extends Board {
    
    /**
     * Creates a new torus board.
     * @param size - the size of the board
     */
    public TorusBoard(final int size) {
        super(size);
    }

    @Override
    public int calculateRowIndex(final int rowIndex) {
        return Math.floorMod(rowIndex, getBoardSize());
    }

    @Override
    public int calculateColumnIndex(final int columnIndex) {
        return Math.floorMod(columnIndex, getBoardSize());
    }
}
