/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.boardgame.board;

/**
 * Encapsulates a cell for the board with a player and a textual representation.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class Cell {
    /**
     * The player that occupies this cell or null.
     */
    private Player player;

    /**
     * Constructs a new empty cell.
     */
    public Cell() {
        this.player = null;
    }

    /**
     * Constructs a new cell occupied by a player.
     *
     * @param player The player that occupies that cell.
     */
    public Cell(Player player) {
        this.player = player;
    }

    /**
     * Gets the player that occupies the cell.
     *
     * @return the occupying player or <code>null</code> if the cell is empty.
     */
    Player getPlayer() {
        return player;
    }

    /**
     * Sets the player that occupies the cell.
     *
     * @param newPlayer The player to occupy this cell.
     */
    void setPlayer(Player newPlayer) {
        this.player = newPlayer;
    }

    @Override
    public String toString() {
        return player != null ? String.valueOf(player.getToken()) : "-";
    }
}
