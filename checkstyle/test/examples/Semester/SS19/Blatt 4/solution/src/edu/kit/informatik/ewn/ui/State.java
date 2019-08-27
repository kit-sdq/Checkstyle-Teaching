/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.ewn.ui;

import edu.kit.informatik.ewn.game.Game;

/**
 * Encapsulates a state for the command line interface with a game and whether or not it is running.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class State {
    /**
     * The game of this state.
     */
    private final Game game;

    /**
     * Whether or not this state is running, therefore accepts commands.
     */
    private boolean quit;

    /**
     * Instantiates a new State for a game with a running state.
     *
     * @param game - a game
     * @param quit - whether or not this state will not accept commands
     */
    public State(final Game game, final boolean quit) {
        this.game = game;
        this.quit = quit;
    }

    /**
     * quits the state, therefore it will not accept input anymore
     */
    public void quit() {
        this.quit = true;
    }

    /**
     * @return whether or not the state accepts input
     */
    public boolean isQuit() {
        return this.quit;
    }

    /**
     * Gets the game.
     * 
     * @return the game
     */
    public Game getGame() {
        return this.game;
    }
}