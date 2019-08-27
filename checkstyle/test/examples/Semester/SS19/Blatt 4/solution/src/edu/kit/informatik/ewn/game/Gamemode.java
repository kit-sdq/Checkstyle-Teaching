/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.ewn.game;

/**
 * The Gamemode enum represents the different gamemodes.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public enum Gamemode {
    /**
     * The torus gamemode.
     */
    TORUS("torus"),
    
    /**
     * The statndard gamemode.
     */
    STANDARD("standard");
    
    private final String shellRepresentation;

    /**
     * Gamemode's constuctor.
     * 
     * @param shellRepresentation - the string representation of the gamemode
     */
    Gamemode(final String shellRepresentation) {
        this.shellRepresentation = shellRepresentation;
    }
    
    /**
     * Gets the string representation of this gamemode.
     * 
     * @return the string representation of this gamemode
     */
    public String getShellRepresentation() {
        return this.shellRepresentation;
    }
    
    @Override
    public String toString() {
        return getShellRepresentation();
    }
    
    /**
     * Gets the gamemode to the given mode string.
     * 
     * @param modeStr - the given mode string
     * @return the gamemode representing the given mode string
     */
    public static Gamemode of(final String modeStr) {
        for (final Gamemode mode : values()) {
            if (mode.getShellRepresentation().equals(modeStr)) {
                return mode;
            }
        }
        
        return null;
    }
}
