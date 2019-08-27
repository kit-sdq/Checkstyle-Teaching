/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.boardgame.wincheck;

/**
 * Check for vertical win lines.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class WinCheckVertical implements WinCheck {
    @Override
    public int alterI(int i, int offset, Direction direction) throws IllegalArgumentException {
        switch (direction) {
            case DIRECTION_ONE:
                //Up
                return i - offset;
            case DIRECTION_TWO:
                //Down
                return i + offset;
            default:
                throw new IllegalArgumentException("You called the method with an impossible direction!");
        }
    }

    @Override
    public int alterJ(int j, int offset, Direction direction) {
        return j;
    }
}
