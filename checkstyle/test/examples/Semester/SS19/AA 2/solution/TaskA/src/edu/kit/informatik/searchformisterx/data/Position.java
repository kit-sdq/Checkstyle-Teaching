/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.searchformisterx.data;

import edu.kit.informatik.searchformisterx.exception.InvalidParameterException;

/**
 * @author Thomas Weber
 * @version 1.0
 */
public class Position {
    /**
     * the x coordinate of this position
     */
    private final int xCoordinate;

    /**
     * the y coordinate of this position
     */
    private final int yCoordinate;

    /**
     * the z coordinate of this position
     */
    private final int zCoordinate;

    /**
     * the height of this position
     */
    private final int height;

    /**
     * Instantiates a new position on a hexagonal board if the given
     * coordinates form a valid position, throws an exception otherwise. Use
     * {@link Position#Position(int, int, int, boolean, int)} if you verify the
     * positions yourself.
     *
     * @param xCoordinate x coordinate
     * @param yCoordinate y coordinate
     * @param zCoordinate z coordinate
     * @param height height
     * @throws InvalidParameterException if the coordinates are not
     *         valid for a hexagonal board
     */
    public Position(final int xCoordinate, final int yCoordinate,
            final int zCoordinate, final int height)
            throws InvalidParameterException {
        this.height = height;
        if (!(xCoordinate + yCoordinate + zCoordinate == 0)) {
            throw new InvalidParameterException(
                    "invalid position for the hex" + " grid");
        }

        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.zCoordinate = zCoordinate;
    }

    /**
     * Unchecked version of {@link Position#Position(int, int, int, int)}
     *
     * @param xCoordinate x coordinate
     * @param yCoordinate y coordinate
     * @param zCoordinate z coordinate
     * @param unchecked ignored variable, just to make sure you know
     *         you work with unchecked positions which may be invalid
     *         and produce wrong results.
     * @param height height
     */
    public Position(final int xCoordinate, final int yCoordinate,
            final int zCoordinate, boolean unchecked, final int height) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.zCoordinate = zCoordinate;
        this.height = height;
    }

    /**
     * Instantiates a new position with the given x,y,z coordinates from the
     * given position and the given height.
     *
     * @param position position which x,y,z coordinates are used
     * @param height height for the new position
     */
    Position(final Position position, final int height) {
        this.xCoordinate = position.xCoordinate;
        this.yCoordinate = position.yCoordinate;
        this.zCoordinate = position.zCoordinate;
        this.height = height;
    }

    /**
     * @return the height of this position
     */
    int getHeight() {
        return height;
    }

    /**
     * @param direction the direction between this position and the
     *         neighbour we want to get.
     * @return the position in the given direction
     */
    public Position getNeighbour(Direction direction) {
        return new Position(xCoordinate + direction.getPosition().xCoordinate,
                yCoordinate + direction.getPosition().yCoordinate,
                zCoordinate + direction.getPosition().zCoordinate, true,
                height + direction.getPosition().height);
    }

    /**
     * @param direction the direction between this position and the
     *         neighbour we want to get.
     * @return the position in the given direction with height 0
     */
    public Position getGroundNeighbour(Direction direction) {
        return new Position(xCoordinate + direction.getPosition().xCoordinate,
                yCoordinate + direction.getPosition().yCoordinate,
                zCoordinate + direction.getPosition().zCoordinate, true, 0);
    }

    /**
     * @return Position for this one but with height 0 (may be the same as
     *         this one)
     */
    Position getGroundPosition() {
        return new Position(xCoordinate, yCoordinate, zCoordinate, true, 0);
    }

    /**
     * Compares the given position to this one based on x,y,z and height.
     *
     * @param position other position
     * @return whether or not this position is equal to the given one,
     *         regarding the height of both
     */
    boolean equalsWithIndex(final Position position) {
        return this.equals(position) && this.height == position.height;

    }

    /**
     * Checks if the given position and this one are neighbours.
     *
     * @param position position which may be a neighbour of this one
     * @return whether or not the given position is a neighbour of this one
     */
    boolean isNeighbour(Position position) {
        for (Direction direction : Direction.values()) {
            if (this.getNeighbour(direction).equals(position)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tries to find the direction between this position and the given one.
     *
     * @param neighbourPosition position of the neighbour
     * @return direction between this and the given position if possible,
     *         null otherwise
     */
    Direction getDirectionOfNeighbour(Position neighbourPosition) {
        for (Direction direction : Direction.values()) {
            if (this.getNeighbour(direction).equals(neighbourPosition)) {
                return direction;
            }
        }
        return null;
    }

    /**
     * @return new Position on top of this one (height + 1)
     */
    Position placeOnTop() {
        return new Position(xCoordinate, yCoordinate, zCoordinate, true,
                (height + 1));
    }

    @Override
    public int hashCode() {
        int result = xCoordinate;
        result = 31 * result + yCoordinate;
        result = 31 * result + zCoordinate;
        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Position)) {
            return false;
        }

        final Position position = (Position) o;

        if (xCoordinate != position.xCoordinate) {
            return false;
        }
        if (yCoordinate != position.yCoordinate) {
            return false;
        }
        return zCoordinate == position.zCoordinate;

    }

    @Override
    public String toString() {
        return xCoordinate + ", " + yCoordinate + ", " + zCoordinate + ", "
               + height;
    }
}
