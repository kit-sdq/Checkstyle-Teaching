/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.searchformisterx.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulates a game player with a number of tiles to place.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class GamePlayer {
    /**
     * The {@link Player} of this gamePlayer.
     */
    private final Player player;

    /**
     * Map of all available tiles for this gamePlayer.
     */
    private Map<String, Tile> availableTiles;

    /**
     * Instantiates a new GamePlayer for the given player by creating its
     * tiles (which are also stored statically in the {@link Tile} class for
     * accessing reasons).
     *
     * @param player {@link Player} for this gamePlayer
     */
    public GamePlayer(Player player) {
        this.player = player;
        availableTiles = new HashMap<>();
        for (Role role : Role.values()) {
            role.getAvailableNumbers().forEach(it -> {
                Tile tile = new Tile(player,
                        new Token(role, new Number(it), true));
                availableTiles.put(tile.toString(), tile);
            });
        }
    }

    /**
     * @return the special tile for this player, in the version this class is
     *         used {@link Role#MISTER_X}
     */
    public Tile getSpecialTile() {
        return availableTiles.values().stream()
                .filter(it -> it.getRole().equals(Role.MISTER_X)).findAny().get();
    }

    /**
     * @return the player of this gamePlayer
     */
    public Player getPlayer() {
        return player;
    }

}
