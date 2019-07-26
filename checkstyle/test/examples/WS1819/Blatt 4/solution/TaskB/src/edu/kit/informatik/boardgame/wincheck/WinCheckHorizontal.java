/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.boardgame.wincheck;

/**
 * Check for horizontal win lines.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class WinCheckHorizontal implements WinCheck {
    @Override
    public int alterI(int i, int offset, Direction direction) {
        return i;
    }

    @Override
    public int alterJ(int j, int offset, Direction direction) {
        switch (direction) {
            case DIRECTION_ONE:
                //Left
                return j - offset;
            case DIRECTION_TWO:
                //Right
                return j + offset;
            default:
                throw new IllegalArgumentException("You called the method with an impossible direction!");
        }
    }
}
