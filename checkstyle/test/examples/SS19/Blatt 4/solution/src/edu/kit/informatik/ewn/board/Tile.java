/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.ewn.board;

/**
 * Represents a tile on the board.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public class Tile {
    private Token token;

    /**
     * Creates a new tile.
     * 
     * @param token - the token on this tile
     */
    public Tile(final Token token) {
        this.token = token;
    }
    
    /**
     * Gets the token on this tile.
     * 
     * @return the token on this tile
     */
    public Token getToken() {
        return this.token;
    }

    /**
     * Replaces the token of this tile with the given token.
     * 
     * @param token - the given token
     */
    public void setToken(final Token token) {
        this.token = token;
    }
    
    /**
     * Clears this tile, i.e. replaces the token of this tile with a "None" token.
     */
    public void clearTile() {
        this.token = new Token(Player.NONE, 0);
    }
    
    @Override
    public String toString() {
        return this.token.toString();
    }
}
