/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.praktomat.main;

import edu.kit.informatik.praktomat.users.Tutor;

/**
 * Encapsulates a state for the command line interface with a praktomat, a
 * tutor and whether or not it is running.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class State {
    /**
     * The praktomat of this state.
     */
    private Praktomat praktomat;

    /**
     * Whether or not this state is running, therefore accepts commands.
     */
    private boolean quit;

    /**
     * The currently active tutor.
     */
    private Tutor tutor;

    /**
     * Instantiates a new State for a praktomat with a praktomat, a running
     * state and an active tutor.
     *
     * @param praktomat a praktomat
     * @param quit whether or not this state will not accept commands
     * @param tutor the active tutor
     */
    public State(final Praktomat praktomat, final boolean quit,
            final Tutor tutor) {
        this.praktomat = praktomat;
        this.quit = quit;
        this.tutor = tutor;
    }

    /**
     * resets this state, therefore creates a new praktomat and removes the
     * active tutor.
     */
    public void reset() {
        this.praktomat = new Praktomat();
        this.tutor = null;
    }

    /**
     * quits the state, therefore it will not accept input anymore
     */
    public void quit() {
        this.quit = true;
    }

    /**
     * @return the praktomat of this state
     */
    public Praktomat getPraktomat() {
        return praktomat;
    }

    /**
     * @return whether or not the state accepts input
     */
    public boolean isQuit() {
        return quit;
    }

    /**
     * @return the active tutor, might be null if no active tutor is set
     */
    public Tutor getTutor() {
        return tutor;
    }

    /**
     * Sets the active tutor for this state, therefore the
     * tutor that can submit reviews in the praktomat this state
     * contains.
     *
     * @param tutor a tutor
     */
    public void setTutor(final Tutor tutor) {
        this.tutor = tutor;
    }
}