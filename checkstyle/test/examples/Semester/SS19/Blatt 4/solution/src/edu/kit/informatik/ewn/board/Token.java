/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.ewn.board;

import java.util.Objects;

/**
 * Represents a token.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public class Token implements Comparable<Token> {
    private final Player player;
    private final int value;

    /**
     * Creates a new token.
     * 
     * @param player
     *            - the player who owns this token
     * @param value
     *            - the value of this token
     */
    public Token(final Player player, final int value) {
        this.player = player;
        this.value = value;
    }

    /**
     * Gets the value of this token.
     * 
     * @return the value of this token
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Gets the player who owns this token.
     * 
     * @return the player who owns this token
     */
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public String toString() {
        return this.player == Player.NONE ? "" + this.player : this.player + ":" + this.value;
    }

    @Override
    public int compareTo(final Token other) {
        return this.value - other.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.player, this.value);
    }

    @Override
    public boolean equals(final Object other) {
        if (other != null && other.getClass().equals(this.getClass())) {
            final Token otherToken = (Token) other;
            return this.player == otherToken.player && this.value == otherToken.value;
        }
        return false;
    }
}
