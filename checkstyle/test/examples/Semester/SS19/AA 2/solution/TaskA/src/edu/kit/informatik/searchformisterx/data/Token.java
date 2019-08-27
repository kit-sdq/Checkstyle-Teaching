/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.searchformisterx.data;

import edu.kit.informatik.searchformisterx.exception.InvalidParameterException;

import java.util.Objects;

/**
 * Encapsulates a token with a role and a number as stated in the assignment.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class Token implements Comparable<Token> {
    /**
     * The role of this token
     */
    private final Role role;

    /**
     * The number of this token
     */
    private final Number number;

    /**
     * Instantiates a new token with the given parameters, throws and
     * exception if the number is not valid for the given role. Use
     * {@link #Token(Role, Number, boolean)} if you check this yourself.
     *
     * @param role the role of this token
     * @param number the number of this token
     * @throws InvalidParameterException occurs, if the given number is
     *         not
     *         valid for the given role
     */
    public Token(final Role role, final Number number)
            throws InvalidParameterException {
        if (!role.validNumber(number)) {
            throw new InvalidParameterException(
                    "this number is not allowed for this role");
        }
        this.role = role;
        this.number = number;
    }

    /**
     * Unchecked version of {@link #Token(Role, Number)}
     *
     * @param role the role of this token
     * @param number the number of this token
     * @param unchecked ignored value to make sure you know you use an
     *         unchecked constructor and resulting tokens may not be valid
     */
    public Token(final Role role, final Number number, boolean unchecked) {
        this.role = role;
        this.number = number;
    }

    /**
     * @return the number of this token
     */
    public Number getNumber() {
        return number;
    }

    /**
     * @return the role of this token
     */
    public Role getRole() {
        return role;
    }

    @Override
    public int compareTo(final Token o) {
        if (this.role.equals(o.role)) {
            return this.getNumber().getId() - o.getNumber().getId();
        }
        return this.role.compareTo(o.role);
    }

    @Override
    public int hashCode() {
        int result = role != null ? role.hashCode() : 0;
        result = 31 * result + (number != null ? number.hashCode() : 0);
        return result;
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
}
