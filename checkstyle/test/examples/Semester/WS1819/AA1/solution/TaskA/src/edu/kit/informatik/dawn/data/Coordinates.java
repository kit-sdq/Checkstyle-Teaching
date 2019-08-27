/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.dawn.data;

/**
 * @author Thomas Weber
 * @version 1.0
 */
public class Coordinates {
    private final OuterRowIndex rowIndex;

    private final OuterColIndex colIndex;

    public Coordinates(final OuterRowIndex rowIndex,
            final OuterColIndex colIndex) {
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
    }

    public static Coordinates min(final Coordinates start,
            final Coordinates end) {
        return start.getColIndex().equals(end.getColIndex()) ? (
                start.getRowIndex().getIndex() > end.getRowIndex().getIndex()
                        ? end : start)
                : (start.getColIndex().getIndex() > end.getColIndex().getIndex()
                           ? end : start);
    }

    public static Coordinates max(final Coordinates start,
            final Coordinates end) {
        return start.getColIndex().equals(end.getColIndex()) ? (
                start.getRowIndex().getIndex() > end.getRowIndex().getIndex()
                        ? start : end)
                : (start.getColIndex().getIndex() > end.getColIndex().getIndex()
                           ? start : end);
    }

    public OuterRowIndex getRowIndex() {
        return rowIndex;
    }

    public OuterColIndex getColIndex() {
        return colIndex;
    }

    @Override
    public int hashCode() {
        int result = rowIndex != null ? rowIndex.hashCode() : 0;
        result = 31 * result + (colIndex != null ? colIndex.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Coordinates)) {
            return false;
        }

        final Coordinates that = (Coordinates) o;

        if (rowIndex != null ? !rowIndex.equals(that.rowIndex)
                : that.rowIndex != null) {
            return false;
        }
        return colIndex != null ? colIndex.equals(that.colIndex)
                : that.colIndex == null;
    }

    @Override
    public String toString() {
        return rowIndex + ", " + colIndex;
    }
}
