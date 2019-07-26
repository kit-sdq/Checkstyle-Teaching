/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.duelofinstitute.data;

import edu.kit.informatik.duelofinstitute.game.Game;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Thomas Weber
 * @version 1.0
 */
public class Tile implements Comparable<Tile> {
    private static final Map<String, Tile> availableTiles = new HashMap<>();

    private final Player player;

    private final Token token;

    private Position position;

    Tile(final Player player, final Token token) {
        this.player = player;
        this.token = token;
        this.position = null;
        Tile.availableTiles.put(this.toString(), this);
    }

    public static void clearTiles() {
        availableTiles.clear();
    }

    public static Tile getTile(String tile) {
        return availableTiles.get(tile);
    }

    public static boolean isEmpty() {
        return availableTiles.isEmpty();
    }

    public static List<Tile> getAvailableTiles() {
        return Collections
                .unmodifiableList(new ArrayList<>(availableTiles.values()));
    }

    public static void clearPositions() {
        availableTiles.values().forEach(Tile::resetPosition);
    }

    public static Tile getTileFromPosition(final Position position,
            boolean highestTile) {
        Tile tile = availableTiles.values().stream()
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

    public static boolean hasOtherPlayerNeighbour(Player player,
            Position position) {
        return getAllNeighbours(position).stream()
                .anyMatch(it -> !it.getPlayer().equals(player));
    }

    public static List<Tile> getAllNeighbours(Position position) {
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

    public boolean allowedToMove() {
        // TODO add breadth first search and compare the sets
        Set<Tile> institue = availableTiles.values().stream()
                // remove this tile from the list
                .filter(it -> !it.equals(this))
                .filter(it -> it.position != null)
                .collect(Collectors.toSet());
        Set<Tile> newInstitute = new HashSet<>();
        // add the first tile of the list
        newInstitute.add(institue.iterator().next());
         for (Tile tile : institue) {
             newInstitute.add(tile);
             newInstitute.addAll(getAllNeighbours(tile.position));
         }
         return true;

    }

    /**
     * @return number of neighbours, 6 is equal to not movable, 7  to another
     * tile is placed on this one
     */
    private int numberOfNeighbours() {
        if (this.position == null) {
            return 0;
        } else if (getTile(Direction.ZERO, this.position) != null) {
            // if there is a tile above this one it wont count for the
            // enclosed count
            return getAllNeighbours(this.position).size()-1;
        } else {
            return getAllNeighbours(this.position).size();
        }
    }

    public boolean canMove() {
        return numberOfNeighbours() <= 5;
    }

    public Role getRole() {
        return token.getRole();
    }

    public boolean isPlaced() {
        return position != null;
    }

    public Tile getHighestTile() {
        return availableTiles.values().stream().filter(Tile::isPlaced)
                .filter(it -> it.position.equals(this.position))
                .max(Comparator.comparingInt(o -> o.getPosition().getIndex())).get();
    }

    public boolean onTop() {
        return getHighestTile().equals(this);
    }


    public Player getPlayer() {
        return player;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(final Position position) {
        this.position = position;
    }

    public String print(Game game) {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add(this.toString());
        for (Direction direction : Direction.values()) {
            Tile tile = getTile(direction, this.position);
            // tiles above this tile have another index
            if (tile != null && !tile.equals(this)) {
                joiner.add(direction.toString());
                joiner.add(tile.toString());
            }
        }
        return joiner.toString();
    }

    public static Tile getTile(Direction direction, Position position) {
        return direction == Direction.ZERO ?
                Tile.getTileFromPosition(position.getNeighbour(direction),
                false) : Tile.getTileFromPosition(position.getGroundNeighbour(direction),
                true);
    }

    public void resetPosition() {
        this.position = null;
    }

    public Token getToken() {
        return token;
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
