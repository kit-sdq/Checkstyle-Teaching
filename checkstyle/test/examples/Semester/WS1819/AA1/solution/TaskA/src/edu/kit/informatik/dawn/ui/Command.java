/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.dawn.ui;

import edu.kit.informatik.dawn.DawnGame;
import edu.kit.informatik.dawn.InputException;
import edu.kit.informatik.dawn.data.ColIndex;
import edu.kit.informatik.dawn.data.Coordinates;
import edu.kit.informatik.dawn.data.DiceSides;
import edu.kit.informatik.dawn.data.RowIndex;
import edu.kit.informatik.util.StringUtil;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Encapsulates the available commands with description.
 *
 * @author original Jonathan Schenkenberger, adapted by Thomas Weber
 * @version 1.2
 */
public enum Command {

    SETVC("set-vc " + InteractionStrings.COORDINATES.toString()) {
        @Override
        public void execute(final MatchResult matcher, final DawnGame dawnGame)
                throws InputException {
            Coordinates coordinates = new Coordinates(
                    new RowIndex(matcher.group(1)),
                    new ColIndex(matcher.group(2)));
            output = dawnGame.setVC(coordinates,
                    dawnGame.getGlobalState().getBoardState());
        }
    },

    ROLL("roll " + StringUtil
            .parenthesize(InteractionStrings.DICE_PATTERN.toString())) {
        @Override
        public void execute(final MatchResult matcher, final DawnGame dawnGame)
                throws InputException {
            output = dawnGame.roll(DiceSides.fromData(matcher.group(1)));
        }
    },

    PLACE("place " + InteractionStrings.COORDINATES.toString()
          + InteractionStrings.INPUT_SEPARATOR_INNER
          + InteractionStrings.COORDINATES.toString()) {
        @Override
        public void execute(final MatchResult matcher, final DawnGame dawnGame)
                throws InputException {
            output = dawnGame
                    .place(matcher.group(1), matcher.group(2), matcher.group(3),
                            matcher.group(4));
        }
    },

    MOVE("move " + InteractionStrings.COORDINATES.toString() + "[:"
         + InteractionStrings.COORDINATES.toString() + "]*") {
        @Override
        public void execute(final MatchResult matcher, final DawnGame dawnGame)
                throws InputException {
            String coordinatesString = input.substring(5);
            String[] coords = coordinatesString.split(":");
            Coordinates[] coordinates = new Coordinates[coords.length];
            int index = 0;
            for (String coordinate : coords) {
                String[] coord = coordinate.split(";");
                coordinates[index] = new Coordinates(new RowIndex(coord[0]),
                        new ColIndex(coord[1]));
                index++;
            }
            output = dawnGame.move(coordinates);
        }
    },

    LIST("list") {
        @Override
        public void execute(final MatchResult matcher, final DawnGame dawnGame)
                throws InputException {
            throw new InputException("no valid command!");
        }
    },

    SHOWRESULT("show-result") {
        @Override
        public void execute(final MatchResult matcher, final DawnGame dawnGame)
                throws InputException {
            output = dawnGame.showResult();
        }
    },

    STATE("state " + InteractionStrings.COORDINATES.toString()) {
        @Override
        public void execute(final MatchResult matcher, final DawnGame dawnGame)
                throws InputException {
            output = dawnGame
                    .state(new Coordinates(new RowIndex(matcher.group(1)),
                            new ColIndex(matcher.group(2))));
        }
    },

    PRINT("print") {
        @Override
        public void execute(final MatchResult matcher, final DawnGame dawnGame)
                throws InputException {
            output = dawnGame.print();
        }
    },

    ROWPRINT("rowprint " + StringUtil
            .parenthesize(InteractionStrings.ROW.toString())) {
        @Override
        public void execute(final MatchResult matcher, final DawnGame dawnGame)
                throws InputException {
            output = dawnGame.printRow(new RowIndex(matcher.group(1)));
        }
    },

    COLPRINT("colprint " + StringUtil
            .parenthesize(InteractionStrings.COL.toString())) {
        @Override
        public void execute(final MatchResult matcher, final DawnGame dawnGame)
                throws InputException {
            output = dawnGame.printCol(new ColIndex(matcher.group(1)));
        }
    },

    RESET("reset") {
        @Override
        public void execute(final MatchResult matcher, final DawnGame dawnGame)
                throws InputException {
            output = dawnGame.reset();
        }
    },


    /**
     * Finishes the interaction.
     */
    QUIT("quit") {
        @Override
        public void execute(final MatchResult matcher,
                final DawnGame dawnGame) {
            quit();
        }
    };

    private static String input;

    /**
     * Contains the output of the command if there is any.
     */
    protected String output;

    /**
     * Contains the game state.
     */
    private boolean isRunning;

    /**
     * Contains this commands input format.
     */
    private Pattern pattern;

    /**
     * Constructs a new command.
     *
     * @param pattern The regex pattern to use for command validation
     *         and processing.
     */
    Command(final String pattern) {
        this.isRunning = true;
        this.pattern = Pattern.compile(pattern);
    }

    /**
     * Checks an input against all available commands and calls the command if
     * one
     * is found.
     *
     * @param input The user input.
     * @param dawnGame The instance of the Ludo object.
     * @return The command that got executed.
     * @throws InputException if no matching command is found. Contains
     *         an error message.
     */
    public static Command executeMatching(final String input,
            final DawnGame dawnGame) throws InputException {
        for (final Command command : dawnGame.getAvailableCommands()) {
            Command.input = input;
            final Matcher matcher = command.pattern.matcher(input);
            if (matcher.matches()) {
                command.execute(matcher, dawnGame);
                return command;
            }
        }

        throw new InputException("not a valid command!");
    }

    private static void throwGameNotActive() throws InputException {
        throw new InputException("game is not active!");
    }

    private static void throwGameActive() throws InputException {
        throw new InputException("game is already active!");
    }

    /**
     * Executes a command.
     *
     * @param matcher The regex matcher that contains the groups of
     *         input of the
     *         command.
     * @param dawnGame The instance of the Ludo object.
     * @throws InputException if the command contains syntactical or
     *         semantic errors.
     */
    public abstract void execute(MatchResult matcher, DawnGame dawnGame)
            throws InputException;

    /**
     * Checks if the program is still running or was exited.
     *
     * @return true if the program is still running, false otherwise.
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Returns the string passed by the last active command.
     *
     * @return string to display to the user
     */
    public String getOutput() {
        return output;
    }

    /**
     * Exits the program gracefully.
     */
    protected void quit() {
        isRunning = false;
    }

}
