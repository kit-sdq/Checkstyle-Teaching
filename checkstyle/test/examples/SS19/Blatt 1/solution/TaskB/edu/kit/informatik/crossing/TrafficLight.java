/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.crossing;

/**
 * This class encapsulates the functionality for a traffic light as described in
 * the assignment.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class TrafficLight {

    /**
     * The red bulb.
     */
    private Bulb red;

    /**
     * The green bulb.
     */
    private Bulb green;

    /**
     * Instantiates a new traffic light.
     */
    public TrafficLight() {
        red = new Bulb(true);
        green = new Bulb(false);
    }

    /**
     * Changes the state of the traffic light.
     */
    public void toggle() {
        green.toggle();
        red.toggle();
    }

    /**
     * @return whether or not the traffic light is green
     */
    public boolean isGreen() {
        return green.isLightOn();
    }

}
