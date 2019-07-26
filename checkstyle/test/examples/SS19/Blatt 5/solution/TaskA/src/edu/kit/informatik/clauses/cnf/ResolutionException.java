/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.clauses.cnf;

/**
 * Encapsulates an exception which is thrown in case of problems during the
 * resolution.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class ResolutionException extends Exception {

    /**
     * The constructor of the ResolutionException that is thrown in case of
     * problems during the resolution.
     *
     * @param message The error message to display to the user.
     */
    public ResolutionException(String message) {
        super(message);
    }
}
