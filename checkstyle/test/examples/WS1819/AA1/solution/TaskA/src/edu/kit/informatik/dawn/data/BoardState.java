/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.dawn.data;

import edu.kit.informatik.dawn.InputException;

/**
 * @author Thomas Weber
 * @version 1.0
 */
public enum BoardState {
    EMPTY("-"), CERES("C"), VESTA("V"), MISSIONCONTROL("+");

    private String rep;

    BoardState(final String rep) {
        this.rep = rep;
    }

    public static DiceSides fromData(String diceSide) throws InputException {
        for (DiceSides sides : DiceSides.values()) {
            if (sides.toString().equals(diceSide)) {
                return sides;
            }
        }
        throw new InputException("invalid dice side");
    }

    @Override
    public String toString() {
        return rep;
    }
}
