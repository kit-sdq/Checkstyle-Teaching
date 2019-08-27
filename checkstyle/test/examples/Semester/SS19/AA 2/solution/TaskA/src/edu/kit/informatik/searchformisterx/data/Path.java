/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.searchformisterx.data;

import edu.kit.informatik.searchformisterx.exception.InputException;
import edu.kit.informatik.searchformisterx.main.Main;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates a path as a series of hops and a tile that should be moved.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class Path {
    /**
     * List of hops of this path.
     */
    private List<Hop> hopList;

    /**
     * Tile that is moved along this path.
     */
    private Tile moveTile;

    /**
     * Instantiates a new Path for the given moveTile with the given String
     * containing the series of hops. Throws an InpuException if anything is
     * invalid about the path, so a created path is valid (as long as the
     * board does not change)
     *
     * @param moveTile the tile that should be moved along this path
     * @param path String containing the path as series of hops
     * @throws InputException occurs if the tile is not placed or the
     *         given
     *         path is malformed
     */
    public Path(Tile moveTile, String path) throws InputException {
        this.moveTile = moveTile;
        if (!this.moveTile.isPlaced()) {
            throw new InputException(
                    "The tile you want to move has to be " + "placed");
        }
        hopList = new ArrayList<>();
        String[] hops = path.split(Main.SEPARATOR);
        if (hops.length % 2 != 0) {
            throw new InputException("hops have a direction and a target tile");
        }
        for (int index = 0; index < hops.length; index += 2) {
            Direction direction = Direction.fromString(hops[index]);
            Tile targetTile = Tile.getTile(hops[index + 1]);
            if (targetTile == null) {
                throw new InputException(
                        "target tile " + hops[index] + " " + hops[index + 1]
                        + " not valid");
            }
            if (!targetTile.isPlaced()) {
                throw new InputException(
                        "target tile " + hops[index] + " " + hops[index + 1]
                        + " not placed");
            }
            if (hopList.contains(new Hop(direction, targetTile))) {
                throw new InputException("path already contains that hop");
            }
            if (hopList.stream().anyMatch(it -> it.getTargetPosition()
                    .equals(targetTile.getPosition()
                            .getNeighbour(direction)))) {
                throw new InputException(
                        "that position is already part of " + "the path");
            }
            hopList.add(new Hop(direction, targetTile));
        }
    }

    /**
     * @return the target position of this path
     */
    public Position getTargetPosition() {
        return hopList.get(hopList.size() - 1).getTargetPosition();
    }

    /**
     * @return if this path is valid regarding restrictions of the role of
     *         the moveTile
     */
    public boolean isValidPath() {
        return moveTile.getRole().validMove(moveTile, this);
    }

    /**
     * @return the list of hops of this path
     */
    List<Hop> getHopList() {
        return hopList;
    }

    /**
     * @return the tile that should be moved along this path
     */
    Tile getMoveTile() {
        return moveTile;
    }

    @Override
    public String toString() {
        return moveTile.toString() + hopList.toString();
    }
}
