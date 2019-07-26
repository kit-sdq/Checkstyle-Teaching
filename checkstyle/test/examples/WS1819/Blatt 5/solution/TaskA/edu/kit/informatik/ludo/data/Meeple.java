/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.ludo.data;

import java.util.Objects;

import edu.kit.informatik.ludo.PlayerColor;

/**
 * Represents a meeple, i.e. a playing stone of a player.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public class Meeple {
    private final PlayerColor color;
    private final int id;
    
    /**
     * Creates a new Meeple of the given PlayerColor and an id.
     * 
     * @param color - the given PlayerColor
     * @param id - the meeple's id for distinguishing the different meeples of one player
     */
    public Meeple(final PlayerColor color, final int id) {
        this.color = color;
        this.id = id;
    }
    
    /**
     * Gets the meeple's color.
     * 
     * @return the meeple's color 
     */
    public PlayerColor getColor() {
        return this.color;
    }
    
    /**
     * Gets the id.
     * 
     * @return the id
     */
    public int getID() {
        return this.id;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.getClass(), this.color, this.id);
    }
    
    @Override
    public boolean equals(final Object other) {
        if (getClass().equals(other.getClass())) {
            final Meeple otherMeeple = (Meeple) other;
            return (this.color == otherMeeple.color && this.id == otherMeeple.id);
        }
        return false;
    }
}
