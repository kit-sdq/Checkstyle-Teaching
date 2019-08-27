/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.searchformisterx.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * Encapsulates a tile with a player, a token and a position.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class Tile implements Comparable<Tile> {
    /**
     * Map to save all available tiles instead of using a board with
     * adjacency matrices.
     */
    private static final Map<String, Tile> AVAILABLE_TILES = new HashMap<>();

    /**
     * The player this tile belongs to.
     */
    private final Player player;

    /**
     * The token of this tile.
     */
    private final Token token;

    /**
     * Position of this tile, null until its placed.
     */
    private Position position;

    /**
     * Instantiates a new tile with the given data. The tile is accessible
     * through {@link #getAvailableTiles()} or {@link #getTile(String)}. The
     * {@link #getPosition()} is initially set to null.
     *
     * @param player the player of this tile
     * @param token the token of this tile
     */
    Tile(final Player player, final Token token) {
        this.player = player;
        this.token = token;
        this.position = null;
        Tile.AVAILABLE_TILES.put(this.toString(), this);
    }

    /**
     * If a tile with the given string representation exists, it will be
     * returned, null otherwise.
     *
     * @param tile string representation of the tile {@link #toString()}
     * @return the tile if it exists, null otherwise
     */
    public static Tile getTile(String tile) {
        return AVAILABLE_TILES.get(tile);
    }

    /**
     * @return list of all tiles that are available
     */
    public static List<Tile> getAvailableTiles() {
        return Collections
                .unmodifiableList(new ArrayList<>(AVAILABLE_TILES.values()));
    }

    /**
     * Resets all positions of all tiles.
     */
    public static void clearPositions() {
        AVAILABLE_TILES.values().forEach(Tile::resetPosition);
    }

    /**
     * Returns a tile with the given position if it exists.
     *
     * @param position the position the tile may be placed on
     * @param highestTile boolean whether or not we want the highest
     *         tile
     * @return tile with the given position or the highest placed tile on the
     *         given position, depending on highestTile, null if no tile is
     *         placed on the given position
     */
    public static Tile getTileFromPosition(final Position position,
            boolean highestTile) {
        Tile tile = AVAILABLE_TILES.values().stream()
                // all placed tiles
                .filter(it -> it.position != null)
                // that are placed on the given position
                .filter(it -> it.position.equalsWithIndex(position)).findFirst()
                // or null
                .orElse(null);
        if (highestTile) {
            return tile == null ? null : tile.getHighestTile();
        } else {
            return tile;
        }
    }

    /**
     * Returns whether or not the given position has adjacent tiles from the
     * other players and therefore, if a new tile can be placed there.
     *
     * @param player the player
     * @param position the position that may has neighbouring tiles from
     *         other players
     * @return whether or not the position is adjacent to tiles from another
     *         player
     */
    public static boolean hasOtherPlayerNeighbour(Player player,
            Position position) {
        return getAllDirectNeighbours(position).stream()
                .anyMatch(it -> !it.getPlayer().equals(player));
    }

    /**
     * Returns a list of all neighbours of the given position. Uses
     * {@link Position#getNeighbour(Direction)} for this task.
     *
     * @param position position we want to know the neighbours
     * @return list of all neighbours of this position
     */
    public static List<Tile> getAllDirectNeighbours(Position position) {
        List<Tile> neighbourTiles = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            Position positionNeighbour = position.getNeighbour(direction);
            Tile neighbourTile = getTileFromPosition(positionNeighbour,
                    direction.printHighestTile());
            if (neighbourTile != null) {
                neighbourTiles.add(neighbourTile);
            }
        }
        return neighbourTiles;
    }

    /**
     * Returns the Tile with the position that results from the given
     * position and direction.
     *
     * @param direction direction the searched tile is regarding to the
     *         given position
     * @param position position the searched tile is adjacent to
     * @return the tile if it exists, null if no tile exists
     */
    public static Tile getTile(Direction direction, Position position) {
        return direction == Direction.ZERO ? Tile
                .getTileFromPosition(position.getNeighbour(direction), false)
                : Tile.getTileFromPosition(
                        position.getGroundNeighbour(direction), true);
    }

    /**
     * Returns a set of all tiles with the given position, ignores height.
     *
     * @param position the position of the tiles
     * @return set of tiles with the given position, ignoring height
     */
    public static Set<Tile> allTilesWithThisPosition(Position position) {
        Set<Tile> tilesOnThisPosition = new HashSet<>();
        Tile tileAbove = getTileFromPosition(position.getGroundPosition(),
                false);
        while (tileAbove != null) {
            tilesOnThisPosition.add(tileAbove);
            tileAbove = getTileFromPosition(
                    tileAbove.position.getNeighbour(Direction.ZERO), false);
        }
        return tilesOnThisPosition;
    }

    /**
     * @return a set of all direct ground neighbours of this tile, may be
     *         empty but not null
     */
    public Set<Tile> getAllDirectGroundNeighbours() {
        Set<Tile> neighbourTiles = new HashSet<>();
        for (Direction direction : Direction.values()) {
            Position positionNeighbour = position.getNeighbour(direction)
                    .getGroundPosition();
            Tile neighbourTile = getTileFromPosition(positionNeighbour, false);
            if (neighbourTile != null) {
                neighbourTiles.add(neighbourTile);
            }
        }
        return neighbourTiles;
    }

    /**
     * Returns a set of all tiles that can be reached by this one, useful to
     * determine whether or not a move splits the board.
     *
     * @param ignoredTiles set of ignored tiles, initially the start
     *         tile itself, fills with further iterations, should not be null
     * @return a set of all neighbours of this tile that can be reached by a
     *         hop onto another tile, may be empty
     */
    public Set<Tile> getAllNeighboursRecursively(Set<Tile> ignoredTiles) {
        Set<Tile> neighbourTiles = new HashSet<>();
        neighbourTiles.addAll(this.allTilesWithThisPosition());
        for (Tile tile : getAllDirectGroundNeighbours()) {
            if (!ignoredTiles.contains(tile) && !neighbourTiles
                    .contains(tile)) {
                ignoredTiles.addAll(tile.allTilesWithThisPosition());
                neighbourTiles
                        .addAll(tile.getAllNeighboursRecursively(ignoredTiles));
            }
        }
        return neighbourTiles;
    }

    /**
     * @return all tiles with the position of this one but with different
     *         heights
     */
    public Set<Tile> allTilesWithThisPosition() {
        Set<Tile> tilesOnThisPosition = new HashSet<>();
        tilesOnThisPosition.add(this);
        Tile tileAbove = getTileFromPosition(this.position.getGroundPosition(),
                false);
        while (tileAbove != null) {
            tilesOnThisPosition.add(tileAbove);
            tileAbove = getTileFromPosition(
                    tileAbove.position.getNeighbour(Direction.ZERO), false);
        }
        return tilesOnThisPosition;
    }

    /**
     * A tile is allowed to move if its movement does not split the board.
     *
     * @return whether or not this tile is allowed to move
     */
    public boolean allowedToMove() {
        // get a list of all placed tile except this one
        Set<Tile> institute = AVAILABLE_TILES.values().stream()
                // remove this tile from the list
                .filter(it -> !it.equals(this))
                // add them if they are placed
                .filter(it -> it.position != null)
                // and get a list of it
                .collect(Collectors.toSet());
        // if there are less than 3 tiles all tiles are movable
        if (institute.size() < 3) {
            return true;
        }
        // create a new empty institute
        // and add all tiles that can be reached recursively
        Set<Tile> ignoredTiles = new HashSet<>();
        ignoredTiles.add(this);
        Set<Tile> newInstitute = new HashSet<>(institute.iterator().next()
                .getAllNeighboursRecursively(ignoredTiles));
        newInstitute.remove(this);
        for (Tile tile : institute) {
            // a tile in the institute was not found in the new one
            // so they are not equal
            if (!newInstitute.remove(tile)) {
                return false;
            }
        }
        return newInstitute.isEmpty();

    }

    /**
     * @return number of neighbours, 6 is equal to not movable
     */
    private int numberOfNeighbours() {
        if (this.position == null) {
            return 0;
        } else if (getTile(Direction.ZERO, this.position) != null) {
            // if there is a tile above this one it wont count for the
            // enclosed count
            return getAllDirectNeighbours(this.position).size() - 1;
        } else {
            return getAllDirectNeighbours(this.position).size();
        }
    }

    /**
     * A tile can move if the number of its neighbours does not exceed 5.
     *
     * @return whether or not this tile can move (does not necessarily result
     *         in any valid moves available!)
     */
    public boolean canMove() {
        return numberOfNeighbours() <= 5;
    }

    /**
     * @return the role of this tile
     */
    public Role getRole() {
        return token.getRole();
    }

    /**
     * @return whether or not this tile is placed
     */
    public boolean isPlaced() {
        return position != null;
    }

    /**
     * @return the highest tile that has the same position as this one
     */
    public Tile getHighestTile() {
        return AVAILABLE_TILES.values().stream().filter(Tile::isPlaced)
                .filter(it -> it.position
                        .equals(this.position.getGroundPosition()))
                .max(Comparator.comparingInt(o -> o.getPosition().getHeight()))
                .get();
    }

    /**
     * @return if this tile is the highest one on its position
     */
    public boolean isHighestTile() {
        return this.equals(this.getHighestTile());
    }

    /**
     * @return the player who owns this tile
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return the position of this tile
     */
    public Position getPosition() {
        return position;
    }

    /**
     * @param position new position for this tile
     */
    public void setPosition(final Position position) {
        this.position = position;
    }

    /**
     * @return a String representation of the game board like stated in the
     *         assignment.
     */
    public String print() {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add(this.toString());
        for (Direction direction : Direction.values()) {
            Tile tile;
            if (direction == Direction.ZERO) {
                tile = getTile(direction, this.position);
            } else {
                tile = getTile(direction, this.position.getGroundPosition());
                if (tile != null) {
                    tile = tile.getHighestTile();
                }
            }

            // tiles above this tile have another index
            if (tile != null && !tile.equals(this)) {
                joiner.add(direction.toString());
                joiner.add(tile.toString());
            }
        }
        return joiner.toString();
    }

    /**
     * Resets the position of this tile.
     */
    public void resetPosition() {
        this.position = null;
    }

    @Override
    public int compareTo(final Tile o) {
        if (this.player != o.player) {
            return this.player.compareTo(o.player);
        }
        return this.token.compareTo(o.token);
    }

    @Override
    public int hashCode() {
        int result = player != null ? player.hashCode() : 0;
        result = 31 * result + (token != null ? token.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tile)) {
            return false;
        }

        final Tile tile = (Tile) o;

        if (player != tile.player) {
            return false;
        }
        if (!Objects.equals(token, tile.token)) {
            return false;
        }
        return Objects.equals(position, tile.position);

    }

    @Override
    public String toString() {
        return token.getRole().toString() + token.getNumber().toString()
               + player.toString();
    }
}
