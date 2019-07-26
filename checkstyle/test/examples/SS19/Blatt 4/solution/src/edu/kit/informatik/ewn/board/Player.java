/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.ewn.board;

/**
 * Represents a player.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public enum Player {
    /**
     * Player 1.
     */
    ONE("P1"), 
    
    /**
     * Player 2.
     */
    TWO("P2"), 
    
    /**
     * Player "None" (represents empty tiles).
     */
    NONE("*");

    private final String representation;

    /**
     * Player's contructor.
     * 
     * @param representation - the string representation of this player
     */
    Player(final String representation) {
        this.representation = representation;
    }
    
    @Override
    public String toString() {
        return this.representation;
    }
}
