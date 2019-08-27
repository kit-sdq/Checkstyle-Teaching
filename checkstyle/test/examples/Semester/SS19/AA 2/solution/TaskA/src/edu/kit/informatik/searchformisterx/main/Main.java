/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.searchformisterx.main;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.searchformisterx.exception.InputException;
import edu.kit.informatik.searchformisterx.exception.InvalidParameterException;
import edu.kit.informatik.searchformisterx.game.Game;

/**
 * This is the entry point class for the program containing the main method.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class Main {

    /**
     * String describing a regex for a valid token
     */
    public static final String TOKEN_REGEX = "(([A-Z])(\\d)([A-Z]))";

    /**
     * Input and output Separator
     */
    public static final String SEPARATOR = " ";

    /**
     * The main method of the program.
     *
     * @param args The arguments that are passed to the program at
     *         launch as array - IGNORED
     */
    public static void main(String[] args) {

        Game game = new Game();

        Command command = null;
        do {
            try {
                command = Command.executeMatching(Terminal.readLine(), game);
            } catch (InputException | InvalidParameterException e) {
                Terminal.printError(e.getMessage());
            }
        } while (command == null || command.isRunning());
    }
}
