/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.duelofinstitute.data;

import edu.kit.informatik.duelofinstitute.exception.InputException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Thomas Weber
 * @version 1.0
 */
public class Path {
    private List<Hop> hopList;
    private Tile moveTile;

    public Path(Tile moveTile, String path) throws InputException {
        this.moveTile = moveTile;
        hopList = new ArrayList<>();
        String[] hops = path.split(" ");
        if (hops.length % 2 != 0) {
            throw new InputException("hops have a direction and a target tile");
        }
        for (int index = 0; index < hops.length; index += 2) {
            Direction direction = Direction.fromString(hops[index]);
            Tile targetTile = Tile.getTile(hops[index + 1]);
            if (direction == null) {
                throw new InputException("direction of hop " + hops[index] +
                                         " " + hops[index+1] + " not valid");
            } else if (targetTile == null) {
                throw new InputException("target tile of hop " + hops[index] +
                                         " " + hops[index+1] + " not valid");
            } else if (!targetTile.isPlaced()) {
                throw new InputException("target tile " + hops[index] + " " + hops[index+1] + " not placed");
            }
            hopList.add(new Hop(direction, targetTile));
        }
    }

    public Position getTargetPosition() {
        return hopList.get(hopList.size()-1).getTargetPosition();
    }

    public boolean isValidPath() {
        return moveTile.getRole().validMove(this);
    }

    public List<Hop> getHopList() {
        return hopList;
    }

    public Tile getMoveTile() {
        return moveTile;
    }

    @Override
    public String toString() {
        return moveTile.toString() +  hopList.toString();
    }
}
