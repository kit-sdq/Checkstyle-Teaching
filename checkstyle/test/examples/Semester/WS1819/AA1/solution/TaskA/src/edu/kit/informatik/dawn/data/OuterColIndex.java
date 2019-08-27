/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.dawn.data;

import edu.kit.informatik.dawn.InputException;

/**
 * @author Thomas Weber
 * @version 1.0
 */
public class OuterColIndex {

    private int index;

    public OuterColIndex(final int index) throws InputException {
        if (index < Board.MIN_COL_DAWN || index > Board.MAX_COL_DAWN) {
            throw new InputException("invalid col index");
        }
        this.index = index;
    }

    public OuterColIndex(final String group) throws InputException {
        this(Integer.parseInt(group));
    }

    public static boolean isOnBoard(int index) {
        return index >= 0 && index <= Board.NUMBER_COLUMNS;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OuterColIndex)) {
            return false;
        }

        final OuterColIndex that = (OuterColIndex) o;

        return index == that.index;
    }

    @Override
    public int hashCode() {
        return index;
    }

    @Override
    public String toString() {
        return String.valueOf(index);
    }
}
