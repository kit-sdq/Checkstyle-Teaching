/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.praktomat.users;

/**
 * Encapsulates a user of the praktomat system.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class User {

    /**
     * The name of the user.
     */
    private final String userName;

    /**
     * Instantiates a new user with the given name.
     *
     * @param name the name of the user
     * @throws NullPointerException occurs if given name is null
     */
    public User(String name) throws NullPointerException {
        if (name == null) {
            throw new NullPointerException("Given name is null!");
        }
        this.userName = name;
    }

    /**
     * @return the name of the user
     */
    public String getName() {
        return userName;
    }

    @Override
    public String toString() {
        return userName;
    }
}
