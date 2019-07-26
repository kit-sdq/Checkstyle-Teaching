/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.duelofinstitute.data;

/**
 * @author Thomas Weber
 * @version 1.0
 */
public class Hop {
    private Direction direction;
    private Tile tile;

    public Hop(final Direction direction, final Tile tile) {
        this.direction = direction;
        this.tile = tile;
    }

    public boolean targetTileEmpty() {
        return getTargetTile() == null;
    }

    public Tile getTargetTile() {
        return Tile.getTileFromPosition(tile.getPosition().getNeighbour(direction), false);
    }

    public Position getTargetPosition() {
        Position pos = tile.getPosition().getNeighbour(direction);
        if (Tile.getTileFromPosition(pos, true) != null) {
            // the tile is already occupied, get the tile above
            return Tile.getTileFromPosition(pos, true).getPosition().placeOnTop();
        }
        return pos;
    }

    public Direction getDirection() {
        return direction;
    }

    public Tile getTile() {
        return tile;
    }

    @Override
    public String toString() {
        return direction+ " " + tile;
    }
}
