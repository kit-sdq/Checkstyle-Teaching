/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.addressbook.data;

/**
 * Encapsulates an e-mail.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class Email {
    /**
     * Simply a string to store the e-mail, can be extended easily if demanded
     * later.
     */
    private String email;

    /**
     * Instantiates a new email object with the given data. No checks or
     * calculations are performed. Access is package-private to restrict the
     * creation of objects of this class.
     *
     * @param email the e-mail to store
     */
    Email(final String email) {
        this.email = email;
    }

    /**
     * Returns the e-mail as string.
     *
     * @return the e-mail as simple string
     */
    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return this.email;
    }
}
