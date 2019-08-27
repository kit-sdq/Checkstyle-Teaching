/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.praktomat.main;

import edu.kit.informatik.Terminal;

/**
 * This is the entry point class for the program containing the main method
 * and the command line interactions.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public final class Shell {

    /**
     * Static main class, no instances should be created.
     */
    private Shell() {
    }

    /**
     * Main method of the program, parses input from
     * {@link Terminal#readLine()} and executes the contained commands if
     * possible.
     *
     * @param argv ignored
     */
    public static void main(String[] argv) {
        final State state = new State(new Praktomat(), false, null);

        while (!state.isQuit()) {
            final String cmdline = Terminal.readLine();
            final String[] cmdsplit = cmdline.split(" ");

            boolean cmdFound = false;
            Command[] availableCommands = Command.getAvailableCommands();
            for (int i = 0; i < availableCommands.length && !cmdFound; i++) {
                final Command cmd = availableCommands[i];

                if (cmdsplit.length > 0 && cmd.isCmd(cmdsplit[0])) {
                    cmdFound = true;

                    if (cmd.isFormatOk(cmdsplit) && !cmdline.endsWith(" ")) {
                        cmd.execute(state, cmdsplit);
                    } else {
                        error("illegal parameter format.");
                    }
                }
            }

            if (!cmdFound) {
                error("unknown command.");
            }
        }
    }

    /**
     * @param message an error message to be printed
     */
    public static void error(final String message) {
        Terminal.printError(message);
    }

    /**
     * @param object an object to be printed
     */
    public static void println(final Object object) {
        Terminal.printLine(object);
    }
}
