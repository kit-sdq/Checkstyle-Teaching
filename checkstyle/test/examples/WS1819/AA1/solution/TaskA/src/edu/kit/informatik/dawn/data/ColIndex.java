/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.dawn.data;

import edu.kit.informatik.dawn.InputException;

/**
 * @author Thomas Weber
 * @version 1.0
 */
public class ColIndex extends OuterColIndex{


    public ColIndex(final int index) throws InputException {
        super(index);
        if (index < Board.NUMBER_COLS_MIN || index > Board.NUMBER_COLUMNS) {
            throw new InputException("invalid column index");
        }
    }

    public ColIndex(final String group) throws InputException {
        super(group);
        int index = Integer.parseInt(group);
        if (index < Board.NUMBER_COLS_MIN || index > Board.NUMBER_COLUMNS) {
            throw new InputException("invalid column index");
        }
    }


}
