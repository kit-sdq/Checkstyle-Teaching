/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.addressbook;

/**
 * Encapsulates an exception which is thrown in case of invalid user input.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class InputException extends Exception {
    /**
     * The constructor of the InputException that is thrown if a user input is
     * invalid.
     *
     * @param message The error message to display to the user.
     */
    public InputException(String message) {
        super(message);
    }
}
