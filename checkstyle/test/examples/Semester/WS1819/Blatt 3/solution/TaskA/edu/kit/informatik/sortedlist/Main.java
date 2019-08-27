/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.sortedlist;

/**
 * The Main class. Contains the main-method.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public final class Main {

    /**
     * The main-method which starts the sorted list program's command line interface.
     * 
     * @param args - ignored
     */
    public static void main(String[] args) {
        CommandLineInterface cli = new CommandLineInterface();
        cli.start();
    }

}
