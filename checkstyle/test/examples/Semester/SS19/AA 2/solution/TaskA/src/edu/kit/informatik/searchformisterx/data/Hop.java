/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.searchformisterx.data;

import java.util.Objects;

/**
 * Encapsulates a hop, therefore a move of a single tile in a single direction.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class Hop {
    /**
     * parameter description {@link #Hop(Direction, Tile)}
     */
    private Direction direction;

    /**
     * parameter description {@link #Hop(Direction, Tile)}
     */
    private Tile tile;

    /**
     * Instantiates a new hop with the given parameters.
     *
     * @param direction direction of the tile relative to the tile we
     *         want to move on
     * @param tile tile we want to move adjacent to
     */
    public Hop(final Direction direction, final Tile tile) {
        this.direction = direction;
        this.tile = tile;
    }

    /**
     * Calculates the position this hop points to based on the given
     * adjacent tile and the direction to look at. Gets the first free
     * Position regarding the {@link Position#getHeight()} of the Position.
     *
     * @return the position this hop points to
     */
    public Position getTargetPosition() {
        Position pos = tile.getPosition().getGroundNeighbour(direction);
        if (Tile.getTileFromPosition(pos, true) != null) {
            // the tile is already occupied, get the tile above
            return Tile.getTileFromPosition(pos, true).getPosition()
                    .placeOnTop();
        }
        return pos;
    }

    /**
     * @return parameter description {@link #Hop(Direction, Tile)}
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * @return parameter description {@link #Hop(Direction, Tile)}
     */
    public Tile getTile() {
        return tile;
    }

    @Override
    public int hashCode() {
        int result = direction != null ? direction.hashCode() : 0;
        result = 31 * result + (tile != null ? tile.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Hop)) {
            return false;
        }

        final Hop hop = (Hop) o;

        if (direction != hop.direction) {
            return false;
        }
        return Objects.equals(tile, hop.tile);

    }

    @Override
    public String toString() {
        return direction + " " + tile;
    }
}
