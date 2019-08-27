/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.searchformisterx.data;

/**
 * Encapsulates a number for a token.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class Number {
    /**
     * The number.
     */
    private final int id;

    /**
     * @param id the id of this number
     */
    Number(final int id) {
        this.id = id;
    }

    /**
     * @return the id of this number
     */
    int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Number)) {
            return false;
        }

        final Number number = (Number) o;

        return id == number.id;

    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
