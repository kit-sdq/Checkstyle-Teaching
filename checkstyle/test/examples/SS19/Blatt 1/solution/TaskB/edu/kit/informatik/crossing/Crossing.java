/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.crossing;

/**
 * This class encapsulates the functionality for a crossing as described in the
 * assignment.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class Crossing {

    /**
     * Array of the traffic lights.
     */
    private TrafficLight[] trafficLights;

    /**
     * Instantiates a new crossing with traffic lights.
     */
    public Crossing() {
        // Initialize the array and the traffic lights
        trafficLights = new TrafficLight[4];
        trafficLights[0] = new TrafficLight();
        trafficLights[1] = new TrafficLight();
        trafficLights[2] = new TrafficLight();
        trafficLights[3] = new TrafficLight();

        // all traffic lights are initially red
        // and we switch some to green
        trafficLights[0].toggle();
        trafficLights[1].toggle();

    }

    /**
     * Turns the current green lights red and the green ones red.
     */
    public void toggleTrafficLights() {
        // toggle all lights once
        trafficLights[0].toggle();
        trafficLights[1].toggle();
        trafficLights[2].toggle();
        trafficLights[3].toggle();
    }

    /**
     * Returns if you can cross the road at the given traffic light.
     *
     * @param trafficLightIndex the index of the traffic light
     * @return returns whether or not you can cross the road
     */
    public boolean isAllowedToCross(int trafficLightIndex) {
        return trafficLights[trafficLightIndex].isGreen();
    }

}
