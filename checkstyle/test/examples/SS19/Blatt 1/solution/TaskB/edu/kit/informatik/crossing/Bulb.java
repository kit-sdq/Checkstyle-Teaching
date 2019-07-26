/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.crossing;

/**
 * This class encapsulates the functionality for a bulb as described in the
 * assignment.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class Bulb {

    /**
     * Boolean status variable to indicate the state of the bulb.
     */
    private boolean isOn;

    /**
     * Instantiates a new Bulb object with the given bulb state.
     *
     * @param isOn the state of the bulb
     */
    public Bulb(boolean isOn) {
        this.isOn = isOn;
    }

    /**
     * Returns the state of the bulb.
     *
     * @return returns whether or not the bulb is on
     */
    public boolean isLightOn() {
        return isOn;
    }

    /**
     * Changes the state of the bulb.
     */
    public void toggle() {
        isOn = !isOn;
    }
}
