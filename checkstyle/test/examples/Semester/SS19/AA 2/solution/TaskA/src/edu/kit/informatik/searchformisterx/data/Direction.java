/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.searchformisterx.data;

import edu.kit.informatik.searchformisterx.exception.InputException;

import java.util.Arrays;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * Encapsulates the available directions for a hexagon board.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public enum Direction {
    /**
     * Directly above (only available index change)
     */
    ZERO("0", new Position(0, 0, 0, true, 1)),

    /**
     * North
     */
    ONE("1", new Position(0, 1, -1, true, 0)),

    /**
     * North-East
     */
    TWO("2", new Position(1, 0, -1, true, 0)),

    /**
     * South-East
     */
    THREE("3", new Position(1, -1, 0, true, 0)),

    /**
     * South
     */
    FOUR("4", new Position(0, -1, 1, true, 0)),

    /**
     * South-West
     */
    FIVE("5", new Position(-1, 0, 1, true, 0)),

    /**
     * North-West
     */
    SIX("6", new Position(-1, 1, 0, true, 0));

    /**
     * The representation for the direction for printing purposes.
     */
    private final String representation;

    /**
     * The position relative to the position (0,0,0,true,0)
     */
    private final Position position;

    /**
     * Instantiates a new Direction.
     *
     * @param representation the representation of this direction
     * @param position the position relative to 0,0,0
     */
    Direction(final String representation, final Position position) {
        this.representation = representation;
        this.position = position;
    }

    /**
     * @return an array of all moving directions (all directions except zero)
     */
    public static Direction[] getMovingDirections() {
        return new Direction[]{ONE, TWO, THREE, FOUR, FIVE, SIX};
    }

    /**
     * Parses the given string to a direction if possible, otherwise throws
     * an exception.
     *
     * @param representation The String representation of the direction
     * @return the direction if possible
     * @throws InputException if the direction does not exist
     */
    public static Direction fromString(final String representation)
            throws InputException {
        final Optional<Direction> direction = Arrays.stream(Direction.values())
                .filter(it -> it.representation.equals(representation))
                .findAny();
        if (!direction.isPresent()) {
            throw new InputException("Invalid direction");
        }
        return direction.get();
    }

    /**
     * @return a regular expression covering all available directions
     */
    public static String getAllDirectionsRegex() {
        StringJoiner joiner = new StringJoiner("|");
        Arrays.stream(Direction.values())
                .forEach(it -> joiner.add(it.representation));
        return "(" + joiner.toString() + ")";
    }

    /**
     * @return the position of the direction relative to 0,0,0
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Returns the previous direction for this one.
     *
     * @return the previous direction for checking obstructed paths (move in
     *         direction 2, direction 1 is {@link #getPrevious()}, 3 is {@link
     *         #getNext()})
     * @throws InputException if the method is called for {@link #ZERO}
     *         which has no previous or next direction
     */
    public Direction getPrevious() throws InputException {
        if (this == Direction.ZERO) {
            throw new InputException("illeagal neighbour check");
        }
        int index = this.ordinal() == 1 ? 6 : (this.ordinal() - 1);
        return Direction.values()[index];
    }

    /**
     * Javadoc at {@link #getPrevious()}.
     *
     * @return next direction
     * @throws InputException called with {@link #ZERO}
     */
    public Direction getNext() throws InputException {
        if (this == Direction.ZERO) {
            throw new InputException("illeagal neighbour check");
        }
        int index = this.ordinal() == 6 ? 1 : (this.ordinal() + 1);
        return Direction.values()[index];
    }

    @Override
    public String toString() {
        return representation;
    }

    /**
     * @return what tile we should print (highest tile for {@link #ONE} to
     *         {@link #SIX}, tile directly above for {@link #ZERO})
     */
    public boolean printHighestTile() {
        if (this == ZERO) {
            return false;
        }
        return true;
    }
}
