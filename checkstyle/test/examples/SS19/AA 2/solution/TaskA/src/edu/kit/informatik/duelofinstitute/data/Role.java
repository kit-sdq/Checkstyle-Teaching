/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.duelofinstitute.data;

import edu.kit.informatik.Terminal;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * @author Thomas Weber
 * @version 1.0
 */
public enum Role implements Comparable<Role> {
    MISTER_X("X", new int[]{1}) {
        @Override
        public boolean validMove(Path path) {
            // professor is allowed to move exactly one position
            if (path.getHopList().size() != 1) {
                return false;
            }
            // and is not allowed to move onto another part
            if (Tile.getTileFromPosition(path.getTargetPosition(), false)
                != null) {
                return false;
            }
            if (!path.getMoveTile().getPosition()
                    .isNeigbour(path.getTargetPosition())) {
                return false;
            }
            // TODO
            return true;
        }
    }, AGENT("A", new int[]{1, 2}) {
        @Override
        public boolean validMove(Path path) {
            if (path.getHopList().size() != 1) {
                return false;
            }
            if (!path.getMoveTile().getPosition()
                    .isNeigbour(path.getTargetPosition())) {
                return false;
            }
            // TODO
            return true;
        }
    }, SPY("S", new int[]{1, 2}) {
        @Override
        public boolean validMove(Path path) {
            boolean validMove = false;
            for (Direction direction : Direction.getMovingDirections()) {
                Position movePosition = path.getMoveTile().getPosition()
                        .getGroundTile();
                while (Tile.getTileFromPosition(movePosition, false) != null) {
                    movePosition = movePosition.getGroundNeighbour(direction);
                }
                if (movePosition.equals(path.getTargetPosition())) {
                    validMove = true;
                }
            }
            return validMove;
            // TODO
        }
    }, INVESTIGATOR("E", new int[]{1, 2, 3}) {
        @Override
        public boolean validMove(Path path) {
            // professor is allowed to move exactly one position
            if (path.getHopList().size() != 3) {
                return false;
            }
            // and is not allowed to move onto another part
            if (Tile.getTileFromPosition(
                    path.getTargetPosition().getGroundTile(), false) != null) {
                return false;
            }
            // TODO
            return true;
        }
    }, WHISTLEBLOWER("I", new int[]{1, 2, 3}) {
        @Override
        public boolean validMove(Path path) {
            // TODO
            return true;
        }
    };

    private final String identifier;

    private final int[] availableNumbers;

    Role(final String identifier, final int[] availableNumbers) {
        this.identifier = identifier;
        this.availableNumbers = availableNumbers;
    }

    public boolean validNumber(Number number) {
        return Arrays.stream(availableNumbers)
                .anyMatch(it -> it == number.getId());
    }

    public IntStream getAvailableNumbers() {
        return IntStream.of(availableNumbers);
    }

    public abstract boolean validMove(Path path);

    @Override
    public String toString() {
        return identifier;
    }
}
