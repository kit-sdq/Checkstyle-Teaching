/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.addressbook.ui;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.addressbook.data.AddressBooks;
import edu.kit.informatik.addressbook.InputException;

/**
 * Entry point for the program.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class Main {
    /**
     * The main method that is the entry point to the program.
     *
     * @param args An array of command line arguments.
     */
    public static void main(String[] args) {
        AddressBooks addressBooks = new AddressBooks();
        Command command = null;
        do {
            try {
                command = Command
                        .executeMatching(Terminal.readLine(), addressBooks);
                if (command.isRunning()) {
                    Terminal.printLine(command.getOutput());
                }
            } catch (InputException e) {
                Terminal.printError(e.getMessage());
            }
        } while (command == null || command.isRunning());
    }
}