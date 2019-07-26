/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.boardgame.ui;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.boardgame.InputException;
import edu.kit.informatik.boardgame.board.ConnectFive;

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
     * @param args An array of command line arguments, everything passed
     *         is ignored.
     */
    public static void main(String[] args) {
        ConnectFive connectFive = new ConnectFive();
        Command command = null;
        do {
            try {
                command = Command
                        .executeMatching(Terminal.readLine(), connectFive);
            } catch (InputException e) {
                Terminal.printError(e.getMessage());
            }
        } while (command == null || command.isRunning());
    }
}