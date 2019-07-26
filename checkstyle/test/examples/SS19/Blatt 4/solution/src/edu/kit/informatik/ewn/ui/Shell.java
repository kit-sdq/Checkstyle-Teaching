/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.ewn.ui;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.ewn.game.BoardSize;
import edu.kit.informatik.ewn.game.Game;
import edu.kit.informatik.ewn.game.Gamemode;

/**
 * This is the entry point class for the program containing the main method and
 * the command line interactions.
 *
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public final class Shell {
    /**
     * Static main class, no instances should be created.
     */
    private Shell() {
        assert false;
    }

    /**
     * Main method of the program, parses input from {@link Terminal#readLine()} and
     * executes the contained commands if possible.
     *
     * @param args
     *            - command line arguments
     */
    public static void main(String[] args) {
        final String check = analyseCommandLineParameters(args);
        if (check != null) {
            error(check);
            return;
        }

        final Gamemode mode = Gamemode.of(args[0]);
        final BoardSize size = BoardSize.of(args[1]);
        final State state = new State(new Game(mode, size), false);

        while (!state.isQuit()) {
            evaluateLine(state, size, mode);
        }
    }

    private static void evaluateLine(final State state, final BoardSize size, final Gamemode mode) {
        final String cmdline = Terminal.readLine();
        String[] cmdsplit = cmdline.split(" ");

        boolean cmdFound = false;
        Command[] availableCommands = Command.getAvailableCommands();
        for (int i = 0; i < availableCommands.length && !cmdFound; i++) {
            final Command cmd = availableCommands[i];

            if (cmdsplit.length > 0 && cmd.isCmd(cmdsplit[0])) {
                cmdFound = true;

                final Format tokenValue = size == BoardSize.FIVE ? Format.TOKEN_VALUE_RANGE_ONE_TO_SIX
                        : Format.TOKEN_VALUE_RANGE_ONE_TO_TEN;
                final Format boardIndex;
                if (mode == Gamemode.TORUS) {
                    boardIndex = Format.BOARD_INDEX_INT;
                } else if (size == BoardSize.FIVE) {
                    boardIndex = Format.BOARD_INDEX_RANGE_ZERO_TO_FOUR;
                } else {
                    boardIndex = Format.BOARD_INDEX_RANGE_ZERO_TO_SIX;
                }

                try {
                    cmdsplit = cmd.split(cmdline);
                    if (cmd.formatOk(cmdsplit, tokenValue, boardIndex, size) 
                            && !cmdline.endsWith(InteractionStrings.SEPERATOR_WHITESPACE)
                            && !cmdline.endsWith(InteractionStrings.SEPARATOR_SEMICOLON) 
                            && !cmdline.endsWith(InteractionStrings.SEPARATOR_COMMA)) {
                        executeCommand(cmd, state, cmdsplit);
                    } else {
                        error(InteractionStrings.ILLEGAL_PARAMETER_FORMAT);
                    }
                } catch (IllegalArgumentException e) {
                    error(e.getMessage());
                }
            }
        }

        if (!cmdFound) {
            error("unknown command.");
        }
    }

    private static void executeCommand(final Command cmd, final State state, final String[] cmdsplit) {
        try {
            cmd.execute(state, cmdsplit);
        } catch (IllegalArgumentException e) {
            error(e.getMessage());
        } catch (IllegalStateException e) {
            error(e.getMessage());
        }
    }

    private static String analyseCommandLineParameters(String[] args) {
        if (args.length != 2) {
            return "wrong number of arguments: " + args.length;
        }
        if (!args[0].equals(Gamemode.STANDARD.getShellRepresentation())
                && !args[0].equals(Gamemode.TORUS.getShellRepresentation())) {
            return "invalid game mode: " + args[0];
        }
        if (!args[1].equals(String.valueOf(BoardSize.FIVE.getBoardSize()))
                && !args[1].equals(String.valueOf(BoardSize.SEVEN.getBoardSize()))) {
            return "invalid board size: " + args[1];
        }
        return null;
    }

    /**
     * @param message
     *            - an error message to be printed
     */
    public static void error(final String message) {
        Terminal.printError(message);
    }

    /**
     * @param object
     *            - an object to be printed
     */
    public static void println(final Object object) {
        Terminal.printLine(object);
    }
}
