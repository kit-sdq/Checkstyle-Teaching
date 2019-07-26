/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.dawn.data;

import edu.kit.informatik.dawn.InputException;

/**
 * @author Thomas Weber
 * @version 1.0
 */
public class MissionControlTokens {
    private DiceSides length;

    public MissionControlTokens(final DiceSides length) {
        this.length = length;
    }

    public static DiceSides getDiceSides(Coordinates start, Coordinates end)
            throws InputException {
        if (!start.getColIndex().equals(end.getColIndex()) && !start
                .getRowIndex().equals(end.getRowIndex())) {
            throw new InputException(
                    "Invalid placement, you cannot place " + "diagonal!");
        }
        if (start.getColIndex().getIndex() == end.getColIndex().getIndex()) {
            return DiceSides.fromData(Math.abs(
                    end.getRowIndex().getIndex() - start.getRowIndex()
                            .getIndex()) + 1);
        } else {
            return DiceSides.fromData(Math.abs(
                    end.getColIndex().getIndex() - start.getColIndex()
                            .getIndex()) + 1);
        }
    }

    public static Direction getDirection(Coordinates start, Coordinates end)
            throws InputException {
        if (!start.getColIndex().equals(end.getColIndex()) && !start
                .getRowIndex().equals(end.getRowIndex())) {
            throw new InputException(
                    "Invalid placement, you cannot place " + "diagonal!");
        }
        if (start.getColIndex().getIndex() != end.getColIndex().getIndex()) {
            return Direction.HORIZONTAL;
        } else {
            return Direction.VERTICAL;
        }
    }

    public DiceSides getLength() {
        return length;
    }

    public boolean isPlaceable(Coordinates start, Coordinates end)
            throws InputException {
        if (!start.getColIndex().equals(end.getColIndex()) && !start
                .getRowIndex().equals(end.getRowIndex())) {
            throw new InputException(
                    "Invalid placement, you cannot place " + "diagonal!");
        }
        if (start.getColIndex().getIndex() != end.getColIndex().getIndex()) {
            if ((end.getRowIndex().getIndex() - start.getRowIndex().getIndex())
                != length.getLength()) {
                throw new InputException("invalid length for placeable stone!");
            } else {
                return true;
            }
        } else {
            if ((end.getColIndex().getIndex() - start.getColIndex().getIndex())
                != length.getLength()) {
                throw new InputException("invalid length for placeable stone!");
            } else {
                return true;
            }
        }
    }
}
