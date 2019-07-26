/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.duelofinstitute.data;

import edu.kit.informatik.duelofinstitute.exception.InputException;

import java.util.Arrays;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * @author Thomas Weber
 * @version 1.0
 */
public enum Direction {
    ZERO("0", new Position(0, 0, 0, true, 1)), ONE("1",
            new Position(0, 1, -1, true, 0)),
    TWO("2", new Position(1, 0, -1, true, 0)), THREE("3",
            new Position(1, -1, 0, true, 0)),
    FOUR("4", new Position(0, -1, 1, true, 0)), FIVE("5",
            new Position(-1, 0, 1, true, 0)),
    SIX("6", new Position(-1, 1, 0, true, 0));

    private final String representation;
    private final Position position;

    Direction(final String representation, final Position position) {
        this.representation = representation;
        this.position = position;
    }

    public static Direction[] getMovingDirections() {
        return new Direction[] {ONE, TWO, THREE, FOUR, FIVE, SIX};
    }

    public static Direction fromString(final String representation)
            throws InputException {
        final Optional<Direction> direction = Arrays.stream(Direction.values())
                .filter(it -> it.representation.equals(representation)).findAny();
        if (!direction.isPresent()) {
            throw new InputException("Invalid direction");
        }
        return direction.get();
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return  representation;
    }

    public static String getAllDirectionsRegex() {
        StringJoiner joiner = new StringJoiner("|");
        Arrays.stream(Direction.values()).forEach(it -> joiner.add(it.representation));
        return "(" + joiner.toString() + ")";
    }

    public boolean printHighestTile() {
        if (this == ZERO) {
            return false;
        }
        return true;
    }
}
