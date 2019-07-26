/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.ewn.ui;

import java.util.Arrays;

import edu.kit.informatik.ewn.game.BoardSize;

/**
 * Encapsulates all available commands for the command line interface as stated
 * in the assignment.
 *
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
abstract class Command {
    /**
     * Array which contains all available commands, for a detailed description of
     * the commands, refer to the assignment.
     */
    private static final Command[] CMDS = new Command[] {
        new Command("start", new Format[] {Format.TOKEN_VALUE_LIST, Format.TOKEN_VALUE_LIST }) {
                @Override
                public void execute(State state, String[] cmdArguments)
                        throws IllegalArgumentException, IllegalStateException {
                    final int[] stonesPlayerOne = extractStones(cmdArguments[1]);
                    final int[] stonesPlayerTwo = extractStones(cmdArguments[2]);
                    state.getGame().start(stonesPlayerOne, stonesPlayerTwo);
                }

                private int[] extractStones(final String tokenValues) {
                    final String[] split = tokenValues.split(InteractionStrings.SEPARATOR_COMMA);
                    // converting the token values to int
                    return Arrays.stream(split).mapToInt(s -> Integer.parseInt(s)).toArray();
                }

                @Override
                public String[] split(final String cmdline) {
                    final String[] splitAtWhiteSpace = whiteSpaceSplit(cmdline);
                    final String[] splitAtSemicolon = splitAtWhiteSpace[1]
                            .split(InteractionStrings.SEPARATOR_SEMICOLON);
                    throwIllegalLength(splitAtSemicolon, 2);
                    return addCommandNameAtIndexZero(splitAtSemicolon);
                }
            }, new Command("roll", new Format[] {Format.TOKEN_VALUE }) {
                @Override
                public void execute(State state, String[] cmdArguments)
                        throws IllegalArgumentException, IllegalStateException {
                    final int number = Integer.parseInt(cmdArguments[1]);
                    state.getGame().roll(number);
                }
            }, new Command("move", new Format[] {Format.TOKEN_VALUE, Format.BOARD_INDEX, Format.BOARD_INDEX }) {
                @Override
                public void execute(State state, String[] cmdArguments)
                        throws IllegalArgumentException, IllegalStateException {
                    final int tokenNumber = Integer.parseInt(cmdArguments[1]);
                    final int row = Integer.parseInt(cmdArguments[2]);
                    final int column = Integer.parseInt(cmdArguments[3]);
                    final boolean win = state.getGame().moveToken(tokenNumber, row, column);
                    if (win) {
                        Shell.println("" + state.getGame().getWinner() + InteractionStrings.WIN);
                    } else {
                        Shell.println(InteractionStrings.OK);
                    }
                }

                @Override
                public String[] split(final String cmdline) {
                    final String[] splitAtWhiteSpace = whiteSpaceSplit(cmdline);
                    final String[] splitAtComma = splitAtWhiteSpace[1].split(InteractionStrings.SEPARATOR_COMMA);
                    throwIllegalLength(splitAtComma, 2);
                    final String[] splitAtSemicolon = splitAtComma[1].split(InteractionStrings.SEPARATOR_SEMICOLON);
                    throwIllegalLength(splitAtSemicolon, 2);
                    return addCommandNameAtIndexZero(
                            new String[] {splitAtComma[0], splitAtSemicolon[0], splitAtSemicolon[1] });
                }
            }, new Command("print", new Format[] {}) {
                @Override
                public void execute(State state, String[] cmdArguments)
                        throws IllegalArgumentException, IllegalStateException {
                    Shell.println(state.getGame());
                }
            }, new Command("print-cell", new Format[] {Format.BOARD_INDEX, Format.BOARD_INDEX }) {
                @Override
                public void execute(State state, String[] cmdArguments)
                        throws IllegalArgumentException, IllegalStateException {
                    final int row = Integer.parseInt(cmdArguments[1]);
                    final int column = Integer.parseInt(cmdArguments[2]);
                    Shell.println(state.getGame().getCell(row, column));
                }

                @Override
                public String[] split(final String cmdline) {
                    final String[] splitAtWhiteSpace = whiteSpaceSplit(cmdline);
                    return addCommandNameAtIndexZero(
                            splitAtWhiteSpace[1].split(InteractionStrings.SEPARATOR_SEMICOLON));
                }
            }, new Command("token", new Format[] {}) {
                @Override
                public void execute(State state, String[] cmdArguments) {
                    Shell.println(state.getGame().getTokenCountOfActivePlayer());
                }
            }, new Command("quit", new Format[] {}) {
                @Override
                public void execute(State state, String[] cmdArguments) {
                    state.quit();
                }
            } //
    };

    /**
     * Command without parameters.
     */
    private final String commandString;

    /**
     * {@link Format} array containing the expected parameters for format checks.
     */
    private final Format[] params;

    /**
     * Instantiates a new command with given command string and an array of formats
     * for the parameters.
     *
     * @param commandString
     *            command without parameters
     * @param params
     *            {@link Format} array
     */
    private Command(String commandString, Format[] params) {
        this.commandString = commandString;
        this.params = params;
    }

    /**
     * @return an array of available commands
     */
    public static Command[] getAvailableCommands() {
        return CMDS;
    }

    /**
     * @return the command string without parameters
     */
    public String getCommandString() {
        return commandString;
    }

    /**
     * Checks whether or not the format of the given string array equals the format
     * the parameters should have (the format array of each command).
     *
     * @param cmdline
     *            String array of the command line input, split by the command's
     *            split method
     * @param tokenValue
     *            - the allowed token values
     * @param boardIndex
     *            - the allowed board indices
     * @param boardSize
     *            - the board size
     * @return boolean - if format is ok
     */
    public boolean formatOk(String[] cmdline, Format tokenValue, Format boardIndex, BoardSize boardSize) {
        if (cmdline.length != this.params.length + 1 || !isCmd(cmdline[0])) {
            return false;
        }

        boolean formatOk = true;

        for (int i = 0; i < this.params.length; i++) {
            final String cur = cmdline[i + 1];

            switch (this.params[i]) {
                case TOKEN_VALUE:
                    formatOk = tokenValue.matchCommandRegex(cur);
                    break;
                case BOARD_INDEX:
                    formatOk = boardIndex.matchCommandRegex(cur);
                    if (boardIndex == Format.BOARD_INDEX_INT) {
                        try {
                            Integer.parseInt(cur);
                        } catch (NumberFormatException e) {
                            formatOk = false;
                        }
                    }
                    break;
                case TOKEN_VALUE_LIST:
                    formatOk = Format.checkTokenValueList(cur, tokenValue, boardSize);
                    break;
                default:
                    // should not happen
                    throw new IllegalStateException("Unhandled parameter type: " + params[i]);
            }
        }

        return formatOk;

    }

    /**
     * Checks whether or not the given string contains a command string from the
     * CDMS array.
     *
     * @param command
     *            string representation of the command
     * @return if the given string contains a valid command, therefore if it can be
     *         found in CMDS.
     */
    public boolean isCmd(String command) {
        return command != null && this.commandString.equals(command.trim().toLowerCase());
    }

    /**
     * Specifies the actions for every available command.
     *
     * @param state
     *            - the state of the command line interface
     * @param cmdArguments
     *            - the arguments from the command line as string array
     * @throws IllegalArgumentException
     *             - if an argument is not valid
     * @throws IllegalStateException
     *             - if the game is in an invalid state for the command
     */
    public abstract void execute(State state, String[] cmdArguments)
            throws IllegalArgumentException, IllegalStateException;

    /**
     * Splits the cmdline String according to the format od the command. Default
     * implementation is splitting at whitespace.
     * 
     * @param cmdline
     *            - the command line
     * @return the splitted command line
     * @throws IllegalArgumentException
     *             - if the cmdline cannot be split
     */
    public String[] split(String cmdline) throws IllegalArgumentException {
        return cmdline.split(InteractionStrings.SEPERATOR_WHITESPACE);
    }

    private static String[] whiteSpaceSplit(String cmdline) throws IllegalArgumentException {
        String[] splitAtWhiteSpace = cmdline.split(InteractionStrings.SEPERATOR_WHITESPACE);
        throwIllegalLength(splitAtWhiteSpace, 2);
        return splitAtWhiteSpace;
    }

    private static void throwIllegalLength(String[] array, int length) throws IllegalArgumentException {
        if (array.length != length) {
            throw new IllegalArgumentException(InteractionStrings.ILLEGAL_PARAMETER_FORMAT);
        }
    }

    /**
     * Adds the command name at the beginning of the splitted command line.
     * 
     * @param commandLine
     *            - the splitted command line
     * @return the correct command line for format checking
     */
    protected String[] addCommandNameAtIndexZero(final String[] commandLine) {
        final String[] result = new String[commandLine.length + 1];
        result[0] = this.commandString;
        for (int i = 1; i < result.length; i++) {
            result[i] = commandLine[i - 1];
        }
        return result;
    }
}
