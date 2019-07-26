/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.duelofinstitute.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author Thomas Weber
 * @version 1.0
 */
public class GamePlayer {
    private Map<String, Tile> availableTiles;
    private final Player player;

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

    public Tile get(String tile) {
        return availableTiles.get(tile);
    }

    public Tile getSpecialTile() {
        // there has to be a special token or the game rules changed drastically
        return availableTiles.values().stream().filter(it -> it.getRole().equals(Role.MISTER_X)).findAny().orElse(null);
    }

    public String print() {
        StringJoiner joiner = new StringJoiner(System.lineSeparator());
        List<Tile> availableTile = new ArrayList<>(availableTiles.values());
        availableTile.removeIf(it -> it.getPosition()==null);
        Collections.sort(availableTile);
        availableTile.forEach(it -> joiner.add(it.toString()));
        return joiner.toString();
    }

    public Player getPlayer() {
        return player;
    }

    public Map<String, Tile> getAvailableTiles() {
        return availableTiles;
    }
    public void resetPositions() {
        availableTiles.values().forEach(it -> it.setPosition(null));
    }
}
