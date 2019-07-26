/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.ui;

/**
 * The main class. It contains the main method which is the starting point of the program.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public class Main {
    
    /**
     * The main method which is the starting point of the program.
     * 
     * @param args - ignored
     */
    public static void main(String[] args) {
        final UserInterface ui = new UserInterface();
        while (!ui.isQuit()) {
            ui.interact();
        } 
    }
}
