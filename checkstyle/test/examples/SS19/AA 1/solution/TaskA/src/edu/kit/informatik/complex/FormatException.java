/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.complex;

/**
 * Represents a format exception. It is thrown when an invalid format is detected.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public class FormatException extends Exception {
    private static final long serialVersionUID = 7477819371159667786L;

    /**
     * Creates a new format exception with the given error message.
     * 
     * @param message - the given error message
     */
    public FormatException(final String message) {
        super(message);
    }
}
