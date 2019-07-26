/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.ludo.ui;

import edu.kit.informatik.ludo.PlayerColor;

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
     * Separator for the output separating the outputs for the player's different
     * meeples.
     */
    OUTPUT_SEPARATOR_INNER(","),

    /**
     * Separator for the output separating the outputs for the different players.
     */
    OUTPUT_SEPARATOR_OUTER("\n"),

    /**
     * Separator for the output separating the outputs for the different possible
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
    INPUT_SEPARATOR_INNER(","),

    /**
     * Separator for the input separating the inputs for the different players.
     */
    INPUT_SEPARATOR_OUTER(";"),

    /**
     * Separator for the output separating the outputs for the two fields.
     */
    OUTPUT_SEPARATOR_ROLL_INNER("-"),

    /**
     * Represents the prefix of the start fields.
     */
    START_FIELD_PREFIX("S"),
    
    /**
     * Represents the final fields prefix pattern.
     */
    FINAL_FIELD_PREFIX("(A|B|C|D)"),
    
    /**
     * Represents the pattern for a field.
     */
    FIELD_PATTERN("([1-3]{0,1}[0-9])|(" + START_FIELD_PREFIX
            + PlayerColor.getPatterns() 
            + ")|(" 
            + FINAL_FIELD_PREFIX 
            + PlayerColor.getPatterns() 
            + ")"),

    /**
     * Represents the pattern for the fields of a player (4 fields).
     */
    FOUR_FIELD_PATTERN("((" + FIELD_PATTERN + ")" + INPUT_SEPARATOR_INNER + "){3}(" + FIELD_PATTERN + ")"),

    /**
     * Represents the pattern for the fields of all players, i.e. the state of the game.
     */
    STATE_PATTERN("((" + FOUR_FIELD_PATTERN + ")" + INPUT_SEPARATOR_OUTER + "){3}(" + FOUR_FIELD_PATTERN + ")"),

    /**
     * Represents the pattern for the D6 dice roll.
     */
    DICE_PATTERN("[1-6]");

    /**
     * String representation of the output.
     */
    private String content;

    /**
     * @param content
     *            the string representation for output
     */
    InteractionStrings(final String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return content;
    }
}
