/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.addressbook.data;

/**
 * Encapsulates a name.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class NameOfContact {
    /**
     * Simply a string to store the name, can be extended easily if demanded
     * later.
     */
    private String name;

    /**
     * Instantiates a new NameOfContact object with the given data. No checks or
     * calculations are performed. Access is package-private to restrict the
     * creation of objects of this class.
     *
     * @param name the name of the contact as string
     */
    NameOfContact(final String name) {
        this.name = name;
    }

    /**
     * Returns the name as string.
     *
     * @return the name as simple string
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
