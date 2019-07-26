/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.duelofinstitute.data;

import edu.kit.informatik.duelofinstitute.exception.InvalidParameterException;

import java.util.Objects;

/**
 * @author Thomas Weber
 * @version 1.0
 */
public class Token implements Comparable<Token>{
    private final Role role;
    private final Number number;

    public Token(final Role role, final Number number)
            throws InvalidParameterException {
        if (!role.validNumber(number)) {
            throw new InvalidParameterException("this number is not allowed "
                                                + "for this role");
        }
        this.role = role;
        this.number = number;
    }

    public Token(final Role role, final Number number, boolean unchecked) {
        this.role = role;
        this.number = number;
    }

    public Number getNumber() {
        return number;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public int compareTo(final Token o) {
        if (this.role.equals(o.role)) {
            return this.getNumber().getId()-o.getNumber().getId();
        }
        return this.role.compareTo(o.role);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Token)) {
            return false;
        }

        final Token token = (Token) o;

        if (role != token.role) {
            return false;
        }
        return Objects.equals(number, token.number);

    }

    @Override
    public int hashCode() {
        int result = role != null ? role.hashCode() : 0;
        result = 31 * result + (number != null ? number.hashCode() : 0);
        return result;
    }
}
