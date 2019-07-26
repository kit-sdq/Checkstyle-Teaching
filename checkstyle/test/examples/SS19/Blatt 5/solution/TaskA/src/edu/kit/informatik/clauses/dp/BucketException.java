/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.clauses.dp;

/**
 * Encapsulates an exception which is thrown in case of invalid bucket usage.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class BucketException extends Exception {

    /**
     * The constructor of the BucketException that is thrown for invalid
     * bucket usage.
     *
     * @param message The error message to display to the user.
     */
    public BucketException(String message) {
        super(message);
    }

}
