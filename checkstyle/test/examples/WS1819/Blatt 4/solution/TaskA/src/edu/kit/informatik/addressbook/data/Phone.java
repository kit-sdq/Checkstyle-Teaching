/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.addressbook.data;

/**
 * Encapsulates a phone number.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class Phone {
    /**
     * Simply a string to store the phone number, can be extended easily if
     * demanded later. String is used to support different representations
     * (not requested).
     */
    private String phone;

    /**
     * Instantiates a new Phone object with the given data. No checks or
     * calculations are performed. Access is package-private to restrict the
     * creation of objects of this class.
     *
     * @param phone the phone number as string
     */
    Phone(final String phone) {
        this.phone = phone;
    }

    /**
     * Returns the phone number as string. Parsing to an Integer may not be
     * possible to certain format specifications.
     *
     * @return the phone number as simple string
     */
    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return this.phone;
    }
}
