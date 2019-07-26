/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.dawn.ui;

import edu.kit.informatik.util.StringUtil;

/**
 * Contains strings or characters used for input or output.
 *
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public enum InteractionStrings {
    /**
     * String for a successfully executed command.
     */
    OK("OK"),

    /**
     * Separator for the output separating the outputs for the player's
     * different
     * meeples.
     */
    OUTPUT_SEPARATOR_INNER(";"),

    /**
     * Separator for the output separating the outputs for the different
     * players.
     */
    OUTPUT_SEPARATOR_OUTER("\n"),

    /**
     * Separator for the output separating the outputs for the different
     * possible
     * moves.
     */
    OUTPUT_SEPARATOR_ROLL_OUTER("\n"),

    /**
     * A Whitespace.
     */
    SPACE(" "),

    /**
     * The winner string.
     */
    WINNER("winner"),

    /**
     * Separator for the input separating the inputs for the player's different
     * meeples.
     */
    INPUT_SEPARATOR_INNER(":"),

    /**
     * Separator for the input separating the inputs for the different players.
     */
    INPUT_SEPARATOR_OUTER(";"),

    /**
     * Separator for the output separating the outputs for the two fields.
     */
    OUTPUT_SEPARATOR_ROLL_INNER("-"),

    ROW("[1]?[0-6]|[7-9]|-[1-6]"),

    COL("[1]?[0-9]|20|-[1-6]"),

    COORDINATES(StringUtil.parenthesize(ROW.toString()) + INPUT_SEPARATOR_OUTER
            .toString() + StringUtil.parenthesize(COL.toString())),


    /**
     * Represents the pattern for the D6 dice roll.
     */
    DICE_PATTERN("[2-6]|DAWN");

    /**
     * String representation of the output.
     */
    private String content;

    /**
     * @param content the string representation for output
     */
    InteractionStrings(final String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return content;
    }
}
