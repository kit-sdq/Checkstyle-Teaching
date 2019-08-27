/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.ludo.data;

import java.util.Objects;

import edu.kit.informatik.ludo.ui.InteractionStrings;

/**
 * Represents a move on the board.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public class Move implements Comparable<Move> {
    private final Field from;
    private final Field to;
    
    /**
     * Creates a new Move.
     * 
     * @param from - the from field
     * @param to - the to field
     */
    public Move(final Field from, final Field to) {
        this.from = from;
        this.to = to;
    }
    
    /**
     * Gets the from field.
     * 
     * @return the from field
     */
    public Field getFrom() {
        return this.from;
    }
    
    /**
     * Gets the to field.
     * 
     * @return the to field
     */
    public Field getTo() {
        return this.to;
    }
    
    /**
     * Gets the reverse move, i.e. a move with from and to fields swapped.
     * 
     * @return the reverse move
     */
    public Move reverseMove() {
        return new Move(this.to, this.from);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getClass(), this.from, this.to);
    }
    
    @Override
    public boolean equals(final Object other) {
        if (getClass().equals(other.getClass())) {
            final Move otherMove = (Move) other;
            return this.from.equals(otherMove.from) && this.to.equals(otherMove.to);
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "" + this.from + InteractionStrings.OUTPUT_SEPARATOR_ROLL_INNER + this.to;
    }

    @Override
    public int compareTo(final Move other) {
        return this.from.compareTo(other.from);
    }
}
