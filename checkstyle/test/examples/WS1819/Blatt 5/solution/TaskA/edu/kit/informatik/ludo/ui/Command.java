/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.ludo.ui;

import edu.kit.informatik.util.StringUtil;
import edu.kit.informatik.ludo.BoardState;
import edu.kit.informatik.ludo.InputException;
import edu.kit.informatik.ludo.PlayerColor;
import edu.kit.informatik.ludo.data.Field;
import edu.kit.informatik.ludo.data.LudoGame;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Encapsulates the available commands with description.
 *
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public enum Command {

    /**
     * Represents the start command. Starts the game with the given board state if
     * existing.
     */
    START("start" + StringUtil
            .parenthesize(StringUtil.parenthesize("\\s" + InteractionStrings.STATE_PATTERN.toString()) + "{0,1}")) {
        @Override
        public void execute(final MatchResult matcher, final LudoGame ludo) throws InputException {
            if (!ludo.isGameActive()) {
                final String argument = matcher.group(1);
                if (argument.equals("")) {
                    ludo.start();
                } else {
                    ludo.start(new BoardState(argument.substring(1))); // removing space
                }
                this.output = InteractionStrings.OK.toString();
            } else {
                throwGameActive();
            }
        }
    },

    /**
     * Represents the roll command. Rolls the D6 dice with the given number.
     */
    ROLL("roll " + StringUtil.parenthesize(InteractionStrings.DICE_PATTERN.toString())) {
        @Override
        public void execute(final MatchResult matcher, final LudoGame ludo) throws InputException {
            if (ludo.isGameActive()) {
                final int number = Integer.parseInt(matcher.group(1));
                this.output = StringUtil.toString(ludo.roll(number),
                        "" + InteractionStrings.OUTPUT_SEPARATOR_ROLL_OUTER);
                this.output += this.output.equals("") ? "" : InteractionStrings.OUTPUT_SEPARATOR_ROLL_OUTER;
                this.output += ludo.getActivePlayer().getColor();
            } else {
                throwGameNotActive();
            }
        }
    },

    /**
     * Represents the move command. Moves the meeple on the given field if it is
     * possible after the former dice roll.
     */
    MOVE("move " + StringUtil.parenthesize(InteractionStrings.FIELD_PATTERN.toString())) {
        @Override
        public void execute(final MatchResult matcher, final LudoGame ludo) throws InputException {
            if (ludo.isGameActive()) {
                final String fieldStr = matcher.group(1);
                final Field movedTo = ludo.move(ludo.getField(fieldStr));
                this.output = "" + movedTo + InteractionStrings.OUTPUT_SEPARATOR_OUTER;
                final PlayerColor winner = ludo.checkWin();
                if (winner == null) {
                    this.output += ludo.getActivePlayer().getColor();
                } else {
                    this.output += winner.getColor() + InteractionStrings.SPACE + InteractionStrings.WINNER;
                    ludo.abort();
                }
            } else {
                throwGameNotActive();
            }
        }
    },

    /**
     * Represents the print command. Prints the game board if the game is active.
     */
    PRINT("print") {
        @Override
        public void execute(final MatchResult matcher, final LudoGame ludo) throws InputException {
            if (ludo.isGameActive()) {
                this.output = ludo.toString();
            } else {
                throwGameNotActive();
            }
        }
    },

    /**
     * Represents the abort command. Aborts the active game.
     */
    ABORT("abort") {
        @Override
        public void execute(final MatchResult matcher, final LudoGame ludo) throws InputException {
            if (ludo.isGameActive()) {
                ludo.abort();
                this.output = null;
            } else {
                throwGameNotActive();
            }
        }
    },

    /**
     * Finishes the interaction.
     */
    QUIT("quit") {
        @Override
        public void execute(final MatchResult matcher, final LudoGame ludo) {
            quit();
        }
    };

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
     * @param pattern
     *            The regex pattern to use for command validation and processing.
     */
    Command(final String pattern) {
        this.isRunning = true;
        this.pattern = Pattern.compile(pattern);
    }

    /**
     * Checks an input against all available commands and calls the command if one
     * is found.
     *
     * @param input
     *            The user input.
     * @param ludo
     *            The instance of the Ludo object.
     * @return The command that got executed.
     * @throws InputException
     *             if no matching command is found. Contains an error message.
     */
    public static Command executeMatching(final String input, final LudoGame ludo) throws InputException {
        for (final Command command : Command.values()) {
            final Matcher matcher = command.pattern.matcher(input);
            if (matcher.matches()) {
                command.execute(matcher, ludo);
                return command;
            }
        }

        throw new InputException("not a valid command!");
    }

    /**
     * Executes a command.
     *
     * @param matcher
     *            The regex matcher that contains the groups of input of the
     *            command.
     * @param ludo
     *            The instance of the Ludo object.
     * @throws InputException
     *             if the command contains syntactical or semantic errors.
     */
    public abstract void execute(MatchResult matcher, LudoGame ludo) throws InputException;

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

    private static void throwGameNotActive() throws InputException {
        throw new InputException("game is not active!");
    }

    private static void throwGameActive() throws InputException {
        throw new InputException("game is already active!");
    }

}
