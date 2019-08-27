/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.ludo.ui;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.ludo.InputException;
import edu.kit.informatik.ludo.data.LudoGame;

/**
 * Entry point for the program.
 *
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public class Main {
    /**
     * The main method that is the entry point to the program.
     *
     * @param args An array of command line arguments.
     */
    public static void main(final String[] args) {
        final LudoGame ludo = new LudoGame(); 
        Command command = null;
        do {
            try {
                command = Command
                        .executeMatching(Terminal.readLine(), ludo);
                final String output = command.getOutput();
                if (command.isRunning() && output != null) {
                    Terminal.printLine(output);
                }
            } catch (InputException e) {
                Terminal.printError(e.getMessage());
            }
        } while (command == null || command.isRunning());
    }
}