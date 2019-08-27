/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.searchformisterx.data;

/**
 * Encapsulates a player as stated in the assignment.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public enum Player {
    /**
     * Player infrared as stated in the assignment.
     */
    INFRARED("R", "INFRARED"),

    /**
     * Player ultraviolet as stated in the assignment.
     */
    ULTRAVIOLET("V", "ULTRAVIOLET");

    /**
     * Identifier for the output of single tiles.
     */
    private final String identifier;

    /**
     * Identifier for the output of a winner as stated in the assignment.
     */
    private final String winningIdentifier;

    /**
     * Instantiates a new Player with the given parameters.
     *
     * @param identifier output of single tiles
     * @param winningIdentifier output of a winner
     */
    Player(final String identifier, final String winningIdentifier) {
        this.identifier = identifier;
        this.winningIdentifier = winningIdentifier;
    }

    /**
     * @return the identifier for winner
     */
    public String getWinningIdentifier() {
        return winningIdentifier;
    }

    @Override
    public String toString() {
        return identifier;
    }
}
