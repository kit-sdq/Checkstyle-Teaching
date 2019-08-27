/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.searchformisterx.exception;

/**
 * This exception is thrown in case some of the given parameters to a method
 * are invalid.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class InvalidParameterException extends Exception {

    /**
     * The constructor of the InvalidParameterException.
     *
     * @param message the message of the exception
     */
    public InvalidParameterException(final String message) {
        super(message);
    }
}
