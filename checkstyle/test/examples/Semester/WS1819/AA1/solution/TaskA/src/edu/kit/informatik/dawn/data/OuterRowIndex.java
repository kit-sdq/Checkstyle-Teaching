/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.dawn.data;

import edu.kit.informatik.dawn.InputException;

/**
 * @author Thomas Weber
 * @version 1.0
 */
public class OuterRowIndex {
    private int index;

    public OuterRowIndex(final int index) throws InputException {
        if (index < Board.MIN_ROW_DAWN || index > Board.MAX_ROW_DAWN) {
            throw new InputException("invalid row index");
        }
        this.index = index;
    }

    public OuterRowIndex(final String group) throws InputException {
        this(Integer.parseInt(group));
    }

    public static boolean isOnBoard(int index) {
        return index >= 0 && index <= Board.NUMBER_ROWS;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OuterRowIndex)) {
            return false;
        }

        final OuterRowIndex that = (OuterRowIndex) o;

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
