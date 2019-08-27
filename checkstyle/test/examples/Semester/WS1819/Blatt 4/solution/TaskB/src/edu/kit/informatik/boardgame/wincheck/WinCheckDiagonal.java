/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.boardgame.wincheck;

/**
 * Check for diagonal win lines.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class WinCheckDiagonal implements WinCheck {
    @Override
    public int alterI(int i, int offset, Direction direction) {
        return alter(i, offset, direction);
    }

    @Override
    public int alterJ(int j, int offset, Direction direction) {
        return alter(j, offset, direction);
    }

    private int alter(int coordinate, int offset, Direction direction) {
        switch (direction) {
            case DIRECTION_ONE:
                //Top Left
                return coordinate - offset;
            case DIRECTION_TWO:
                //Bot Right
                return coordinate + offset;
            default:
                throw new IllegalArgumentException("You called the method with an impossible direction!");
        }
    }
}
