/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.clauses.cnf;

/**
 * Encapsulates an exception which is thrown in case of invalid user input.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class InputFormatException extends Exception {

    /**
     * The constructor of the InputFormatException that is thrown if a user input is
     * invalid.
     *
     * @param message The error message to display to the user.
     */
    public InputFormatException(String message) {
        super(message);
    }

}
