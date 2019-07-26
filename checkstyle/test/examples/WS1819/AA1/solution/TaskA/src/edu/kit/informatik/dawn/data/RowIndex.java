/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.dawn.data;

import edu.kit.informatik.dawn.InputException;

/**
 * @author Thomas Weber
 * @version 1.0
 */
public class RowIndex extends OuterRowIndex{

    public RowIndex(final int index) throws InputException {
        super(index);
        if (index < Board.NUMBER_ROWS_MIN || index > Board.NUMBER_ROWS) {
            throw new InputException("invalid row index");
        }
    }

    public RowIndex(final String group) throws InputException {
        super(group);
        int index = Integer.parseInt(group);
        if (index < Board.NUMBER_ROWS_MIN || index > Board.NUMBER_ROWS) {
            throw new InputException("invalid row index");
        }
    }
}
