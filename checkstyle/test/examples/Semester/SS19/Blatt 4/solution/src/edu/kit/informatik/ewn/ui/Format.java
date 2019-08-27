/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.ewn.ui;

import edu.kit.informatik.ewn.game.BoardSize;

/**
 * Describes available input parameters for the commands.
 *
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public enum Format {
    /**
     * Format representing a token value within 1-6.
     */
    TOKEN_VALUE_RANGE_ONE_TO_SIX("[1-6]"),

    /**
     * Format representing a token value within 1-10.
     */
    TOKEN_VALUE_RANGE_ONE_TO_TEN("([1-9])|(10)"),

    /**
     * Format representing a board index of torus game.
     */
    BOARD_INDEX_INT("-?(\\d+)"),
    
    /**
     * Format representing a board index within 0-4.
     */
    BOARD_INDEX_RANGE_ZERO_TO_FOUR("[0-4]"),

    /**
     * Format representing a board index within 0-6.
     */
    BOARD_INDEX_RANGE_ZERO_TO_SIX("[0-6]"),

    /**
     * Format representing a board index.
     */
    BOARD_INDEX("BI"),

    /**
     * Format representing a token value.
     */
    TOKEN_VALUE("TV"),

    /**
     * Format representing a token value list.
     */
    TOKEN_VALUE_LIST("TVL");

    private final String formatDescription;

    /**
     * Instantiates a new format for input parameters.
     *
     * @param formatDescription
     *            description of the format or regex that represents the command
     */
    Format(final String formatDescription) {
        this.formatDescription = formatDescription;
    }

    /**
     * Matches the given string against the regex of this command.
     *
     * @param input
     *            the string to match
     * @return boolean if match
     */
    public boolean matchCommandRegex(final String input) {
        return input.matches(this.formatDescription);
    }

    /**
     * Checks the given token value list for formal correctness.
     * 
     * @param list
     *            - the token value list
     * @param tokenValueFormat
     *            - the token value format
     * @param boardSize
     *            - the board size
     * @return whether the given list has a correct format
     */
    static boolean checkTokenValueList(final String list, final Format tokenValueFormat,
            final BoardSize boardSize) {
        final String[] tokenValueStrs = list.split(InteractionStrings.SEPARATOR_COMMA);
        final int countTokens = boardSize.getStoneCount();
        if (tokenValueStrs.length != countTokens) {
            return false;
        }

        for (int i = 0; i < countTokens; i++) {
            if (!tokenValueFormat.matchCommandRegex(tokenValueStrs[i])) {
                return false;
            }
        }

        return true;
    }
}