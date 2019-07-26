/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.duelofinstitute.data;

/**
 * @author Thomas Weber
 * @version 1.0
 */
public enum Player {
    INFRARED("R", "INFRARED"), ULTRAVIOLET("V", "ULTRAVIOLET");

    private final String identifier;

    private final String color;

    Player(final String identifier, final String color) {
        this.identifier = identifier;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return identifier;
    }
}
