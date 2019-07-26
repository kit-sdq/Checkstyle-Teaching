/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.duelofinstitute.data;

import edu.kit.informatik.duelofinstitute.exception.InvalidParameterException;

/**
 * @author Thomas Weber
 * @version 1.0
 */
public class Position {
    private final int x_coordinate;

    private final int y_coordinate;

    private final int z_coordinate;

    private final int index;

    public Position(final int x_coordinate, final int y_coordinate,
            final int z_coordinate, final int index)
            throws InvalidParameterException {
        this.index = index;
        if (!(x_coordinate + y_coordinate + z_coordinate == 0)) {
            throw new InvalidParameterException(
                    "invalid position for the hex" + " grid");
        }

        this.x_coordinate = x_coordinate;
        this.y_coordinate = y_coordinate;
        this.z_coordinate = z_coordinate;
    }

    public Position(final int x_coordinate, final int y_coordinate,
            final int z_coordinate, boolean unchecked, final int index) {
        this.x_coordinate = x_coordinate;
        this.y_coordinate = y_coordinate;
        this.z_coordinate = z_coordinate;
        this.index = index;
    }

    public int getY_coordinate() {
        return y_coordinate;
    }

    public int getZ_coordinate() {
        return z_coordinate;
    }

    public int getX_coordinate() {
        return x_coordinate;
    }

    public int getIndex() {
        return index;
    }

    public Position getNeighbour(Direction direction) {
        return new Position(x_coordinate + direction.getPosition().x_coordinate,
                y_coordinate + direction.getPosition().y_coordinate,
                z_coordinate + direction.getPosition().z_coordinate, true,
                index + direction.getPosition().index);
    }

    public Position getGroundNeighbour(Direction direction) {
        return new Position(x_coordinate + direction.getPosition().x_coordinate,
                y_coordinate + direction.getPosition().y_coordinate,
                z_coordinate + direction.getPosition().z_coordinate, true, 0);
    }

    public Position getGroundTile() {
        return new Position(x_coordinate, y_coordinate, z_coordinate, true, 0);
    }

    public boolean equalsWithIndex(final Position position) {
        if (x_coordinate != position.x_coordinate) {
            return false;
        }
        if (y_coordinate != position.y_coordinate) {
            return false;
        }
        if (index != position.index) {
            return false;
        }
        return z_coordinate == position.z_coordinate;

    }

    public boolean isNeigbour(Position position) {
        for (Direction direction : Direction.values()) {
            if (this.getNeighbour(direction).equals(position)) {
                return true;
            }
        }
        return false;
    }

    public Position placeOnTop() {
        return new Position(x_coordinate, y_coordinate, z_coordinate, true,
                (index + 1));
    }

    @Override
    public int hashCode() {
        int result = x_coordinate;
        result = 31 * result + y_coordinate;
        result = 31 * result + z_coordinate;
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

        if (x_coordinate != position.x_coordinate) {
            return false;
        }
        if (y_coordinate != position.y_coordinate) {
            return false;
        }
        return z_coordinate == position.z_coordinate;

    }

    @Override
    public String toString() {
        return x_coordinate + ", " + y_coordinate + ", " + ", " + z_coordinate
               + ", " + index;
    }
}
