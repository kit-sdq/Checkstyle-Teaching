/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.dawn.data;

import edu.kit.informatik.dawn.InputException;

/**
 * @author Thomas Weber
 * @version 1.0
 */
public enum DiceSides {
    TWO("2", 2), THREE("3", 3), FOUR("4", 4), FIVE("5", 5), SIX("6", 6),
    DAWN("DAWN", 7);

    private final String rep;

    private final int length;

    DiceSides(final String rep, final int length) {
        this.rep = rep;
        this.length = length;
    }

    public static DiceSides fromData(String diceSide) throws InputException {
        for (DiceSides sides : DiceSides.values()) {
            if (sides.toString().equals(diceSide)) {
                return sides;
            }
        }
        throw new InputException("invalid dice side from String " + diceSide);
    }

    public static DiceSides fromData(int length) throws InputException {
        for (DiceSides sides : DiceSides.values()) {
            if (sides.getLength() == length) {
                return sides;
            }
        }
        throw new InputException("invalid dice side from int " + length);
    }

    @Override
    public String toString() {
        return rep;
    }

    public int getLength() {
        return length;
    }

    public int getIndex() {
        return length - 2;
    }
}
