/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.clauses.cnf;


import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulates a variable as part of a clause. Used as kind of a runtime
 * enum for all variables used.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public final class Variable implements Comparable<Variable> {

    /**
     * Stores all variables with their id.
     */
    private static Map<Integer, Variable> variableMap = new HashMap<>();

    /**
     * Id of a variable.
     */
    private final int id;

    /**
     * Instantiates a new Variable with the given id. Checks if the given id
     * is smaller than {@link Formula#getVariableCount()}, if not throws an
     * Exception.
     *
     * @param id the id of the variable
     * @param formula formula to determine the biggest possible id
     * @throws InputFormatException occurs if the given id is too big
     *         for this formula
     */
    private Variable(final int id, Formula formula)
            throws InputFormatException {
        if (id > formula.getVariableCount()) {
            throw new InputFormatException("illeagal variable");
        }
        this.id = id;
    }

    /**
     * Returns a unique variable with the given id.
     *
     * @param id the id of the variable
     * @param formula formula to determine the biggest possible id
     * @return the variable with the given id
     * @throws InputFormatException occurs if the given id is too big
     *         for this formula
     */
    public static Variable get(final int id, final Formula formula)
            throws InputFormatException {
        if (!variableMap.containsKey(id)) {
            variableMap.put(id, new Variable(id, formula));
        }
        return variableMap.get(id);
    }

    /**
     * Returns the id of this variable. With this id the variable can be
     * retrieved using {@link #get(int, Formula)}.
     *
     * @return the id of the variable
     */
    public int getId() {
        return id;
    }

    @Override
    public int compareTo(final Variable v) {
        return this.id - v.getId();
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
        if (!(o instanceof Variable)) {
            return false;
        }

        final Variable variable = (Variable) o;

        return id == variable.id;

    }

    @Override
    public String toString() {
        return String.valueOf(this.id);
    }
}
