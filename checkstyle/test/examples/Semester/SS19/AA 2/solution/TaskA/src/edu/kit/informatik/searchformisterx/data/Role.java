/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.searchformisterx.data;

import edu.kit.informatik.searchformisterx.exception.InputException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Encapsulates all available roles in the game.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public enum Role implements Comparable<Role> {
    /**
     *
     */
    MISTER_X("X", new int[]{1}) {
        @Override
        public boolean validMove(Tile moveTile, Path path) {
            // professor is allowed to move exactly one position
            if (path.getHopList().size() != 1) {
                return false;
            }
            // and is not allowed to move onto another part
            if (Tile.getTileFromPosition(
                    path.getTargetPosition().getGroundPosition(), false)
                != null) {
                return false;
            }
            if (!path.getMoveTile().getPosition()
                    .isNeighbour(path.getTargetPosition())) {
                return false;
            }
            return moveNotObstructed(moveTile, path) && moveKeepsNeighbour(
                    moveTile, path);
        }
    },
    /**
     *
     */
    AGENT("A", new int[]{1, 2}) {
        @Override
        public boolean validMove(Tile moveTile, Path path) {
            if (path.getHopList().size() != 1) {
                return false;
            }
            if (!path.getMoveTile().getPosition()
                    .isNeighbour(path.getTargetPosition())) {
                return false;
            }
            return moveNotObstructed(moveTile, path) && moveKeepsNeighbour(
                    moveTile, path);
        }
    },
    /**
     *
     */
    SPY("S", new int[]{1, 2}) {
        @Override
        public boolean validMove(Tile moveTile, Path path) {
            boolean validMove = false;
            for (Direction direction : Direction.getMovingDirections()) {
                Position movePosition = path.getMoveTile().getPosition()
                        .getGroundPosition();
                while (Tile.getTileFromPosition(movePosition, false) != null) {
                    movePosition = movePosition.getGroundNeighbour(direction);
                }
                if (movePosition.equals(path.getTargetPosition())) {
                    validMove = true;
                }
            }
            return validMove;
        }
    },
    /**
     *
     */
    INVESTIGATOR("E", new int[]{1, 2, 3}) {
        @Override
        public boolean validMove(Tile moveTile, Path path) {
            // professor is allowed to move exactly one position
            if (path.getHopList().size() != 3) {
                return false;
            }
            // and is not allowed to move onto another part
            if (Tile.getTileFromPosition(
                    path.getTargetPosition().getGroundPosition(), false)
                != null) {
                return false;
            }
            return moveNotObstructed(moveTile, path) && moveKeepsNeighbour(
                    moveTile, path);
        }
    },
    /**
     *
     */
    WHISTLEBLOWER("I", new int[]{1, 2, 3}) {
        @Override
        public boolean validMove(Tile moveTile, Path path) {
            // and is not allowed to move onto another part
            if (Tile.getTileFromPosition(
                    path.getTargetPosition().getGroundPosition(), false)
                != null) {
                return false;
            }
            return moveNotObstructed(moveTile, path) && moveKeepsNeighbour(
                    moveTile, path);
        }
    };

    /**
     * identifier of this role for the output.
     */
    private final String identifier;

    /**
     * array of available numbers for the tokens with this role.
     */
    private final int[] availableNumbers;

    /**
     * Instantiates a new Role with the given parameters.
     *
     * @param identifier identifier of this role for the output
     * @param availableNumbers array of available numbers for the tokens
     *         with this role
     */
    Role(final String identifier, final int[] availableNumbers) {
        this.identifier = identifier;
        this.availableNumbers = availableNumbers;
    }

    /**
     * Checks if the given path for the given tile is not obstructed as
     * stated in the assignment.
     *
     * @param moveTile the tile that will be moved
     * @param path the path the tile will be moved on
     * @return if the move is not obstructed by tiles on adjacent positions
     */
    private static boolean moveNotObstructed(Tile moveTile, Path path) {
        Position movePosition = moveTile.getPosition();
        if (moveTile.getRole() == Role.AGENT) {
            // the agent can jump in and out of obstructed situations,
            // so we can finish the check here
            if (movePosition.getHeight() != path.getTargetPosition()
                    .getHeight()) {
                return true;
            }
            movePosition = new Position(movePosition,
                    path.getTargetPosition().getHeight());
        }
        // check for obstructed paths
        for (Hop hop : path.getHopList()) {
            Direction moveDirection = movePosition
                    .getDirectionOfNeighbour(hop.getTargetPosition());
            if (moveDirection == null) {
                return false;
            }
            try {
                // both adjacent directions have a placed token
                if (Tile.getTileFromPosition(
                        movePosition.getNeighbour(moveDirection.getPrevious()),
                        false) != null && Tile.getTileFromPosition(
                        movePosition.getNeighbour(moveDirection.getNext()),
                        false) != null) {
                    // and that token is not the token that will be moved
                    if (!moveTile.equals(Tile.getTileFromPosition(movePosition
                            .getNeighbour(moveDirection.getPrevious()), false))
                        && !moveTile.equals(Tile.getTileFromPosition(
                            movePosition.getNeighbour(moveDirection.getNext()),
                            false))) {
                        return false;
                    }
                }
            } catch (InputException e) {
                return false;
            }

            movePosition = hop.getTargetPosition();

        }
        return true;
    }

    /**
     * Checks if the given path for the given tile will not split the board.
     *
     * @param moveTile the tile that will be moved
     * @param path the path the tile will be moved on
     * @return if the move of the given tile will not split the board
     */
    private static boolean moveKeepsNeighbour(Tile moveTile, Path path) {
        Position movePosition = moveTile.getPosition();
        for (Hop hop : path.getHopList()) {
            List<Tile> allNeighbours = Tile
                    .getAllDirectNeighbours(movePosition.getGroundPosition());
            allNeighbours.addAll(moveTile.allTilesWithThisPosition());
            List<Tile> allNeighboursTarget = Tile.getAllDirectNeighbours(
                    hop.getTargetPosition().getGroundPosition());
            allNeighboursTarget.addAll(Objects.requireNonNull(
                    Tile.allTilesWithThisPosition(hop.getTargetPosition())));
            allNeighbours.retainAll(allNeighboursTarget);
            allNeighbours.remove(moveTile);
            if (!(allNeighbours.size() > 0)) {
                return false;
            }
            movePosition = hop.getTargetPosition();

        }
        return true;
    }

    /**
     * Checks if the given number is a valid one for this role.
     *
     * @param number a number
     * @return if the given number is a valid number for this role
     */
    public boolean validNumber(Number number) {
        return Arrays.stream(availableNumbers)
                .anyMatch(it -> it == number.getId());
    }

    /**
     * @return IntStream of all available numbers for this role
     */
    public IntStream getAvailableNumbers() {
        return IntStream.of(availableNumbers);
    }

    /**
     * Checks if the given path for the given tile is valid regarding role
     * specific rules.
     *
     * @param moveTile the tile that will be moved
     * @param path the path the tile will be moved on
     * @return if the path is valid for this role
     */
    public abstract boolean validMove(Tile moveTile, Path path);

    @Override
    public String toString() {
        return identifier;
    }
}
