/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.praktomat.main;

/**
 * Describes available input parameters for the commands.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public enum Format {
    /**
     * string without whitespaces, use underscores instead
     */
    DESCRIPTION(new String[]{"\n", "\t", ".*\\s.*"}, ".*"),
    /**
     * int within 1-6
     */
    GRADE(null, "[123456]"),
    /**
     * unique int
     */
    ID(null, "[0-9]+"),
    /**
     * int with 6 digits, no leading zeroes
     */
    MATRICULATION_NUMBER(new String[]{"000000"},
            "[1-9][0-9][0-9][0-9][0-9][0-9]"),
    /**
     * lower case without whitspace strings
     */
    NAME(null, "[a-z]+");

    private final String[] forbiddenRegex;

    private final String commandRegex;

    /**
     * Instantiates a new format for input parameters.
     *
     * @param forbiddenRegex string array of forbidden regex, may be
     *         null
     * @param commandRegex regex that represents the command, has to be
     *         not null
     */
    Format(final String[] forbiddenRegex, final String commandRegex) {
        this.forbiddenRegex = forbiddenRegex;
        this.commandRegex = commandRegex;
    }

    /**
     * Checks whether or not the given string contains some of the forbidden
     * sequences for this command. Returns true if the input conforms the
     * command rules, false if not.
     *
     * @param input the string to test against the forbidden sequences
     * @return true if the input conforms the rules, therefore does not
     *         contain any of the forbidden sequences
     */
    public boolean checkForbiddenSequences(final String input) {
        if (forbiddenRegex == null) {
            return true;
        } else {
            for (final String regex : forbiddenRegex) {
                if (input.contains(regex)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Matches the given string against the regex of this command.
     *
     * @param input the string to match
     * @return boolen if match
     */
    public boolean matchCommandRegex(final String input) {
        return input.matches(this.commandRegex);
    }

    /**
     * Checks whether or not the given string conforms all rules of this
     * command, therefore if it conforms the restrictions applied in
     * {@link #checkForbiddenSequences(String)} and if it conforms the regex
     * matched in {@link #checkCommandRegex(String)}. Returns true if the
     * given string is a valid input for this command.
     *
     * @param input the string to check
     * @return whether or not it is a valid input for the command
     */
    public boolean checkCommandRegex(final String input) {
        return checkForbiddenSequences(input) && matchCommandRegex(input);
    }
}