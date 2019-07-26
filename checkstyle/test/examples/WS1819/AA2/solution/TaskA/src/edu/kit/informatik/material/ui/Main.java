/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.material.ui;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.material.InputException;
import edu.kit.informatik.material.data.AssemblyManager;

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
        final AssemblyManager manager = new AssemblyManager();
        Command command = null;
        do {
            try {
                command = Command
                        .executeMatching(Terminal.readLine(), manager);
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