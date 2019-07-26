/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.duelofinstitute.data;

import edu.kit.informatik.duelofinstitute.exception.InvalidParameterException;

/**
 * @author Thomas Weber
 * @version 1.0
 */
public class Number {
    private final int id;

    public Number(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
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
    public int hashCode() {
        return id;
    }
}
