/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.clauses.cnf;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Extends a Variable with a sign. Used as kind of a runtime
 * enum for all literals used.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public final class Literal implements Comparable<Literal> {

    /**
     * All positive literals.
     */
    private static Map<Variable, Literal> positive = new HashMap<>();

    /**
     * All negative literals.
     */
    private static Map<Variable, Literal> negative = new HashMap<>();

    /**
     * Variable of this literal.
     */
    private final Variable variable;

    /**
     * Sign of this literal.
     */
    private final boolean sign;

    /**
     * Instantiates a new Literal with the given variable and sign.
     *
     * @param variable a Variable
     * @param sign boolean sign for the variable
     */
    private Literal(final Variable variable, final boolean sign) {
        this.variable = variable;
        this.sign = sign;
    }

    /**
     * Returns the unique literal for the given variable with the given sign.
     *
     * @param variable a variable
     * @param sign a sign for the variable
     * @return the literal for the given variable with the given sign
     */
    public static Literal get(final Variable variable, final boolean sign) {
        return sign ? getNegative(variable) : getPositive(variable);
    }

    /**
     * Returns the literal for the given negative variable.
     *
     * @param variable a variable with implicit negative sign
     * @return literal for the given variable
     */
    private static Literal getNegative(final Variable variable) {
        if (!negative.containsKey(variable)) {
            negative.put(variable, new Literal(variable, true));
        }
        return negative.get(variable);
    }

    /**
     * Returns the literal for the given positive variable.
     *
     * @param variable a variable with implicit positive sign
     * @return literal for the given variable
     */
    private static Literal getPositive(final Variable variable) {
        if (!positive.containsKey(variable)) {
            positive.put(variable, new Literal(variable, false));
        }
        return positive.get(variable);
    }

    /**
     * @return the sign of this literal
     */
    public boolean isSign() {
        return sign;
    }

    /**
     * @return the encapsulated variable for this literal
     */
    public Variable getVariable() {
        return variable;
    }

    /**
     * @return the id of the encapsulated variable
     */
    private int getVariableId() {
        return variable.getId();
    }

    @Override
    public int compareTo(final Literal lit) {
        return 2 * (this.getVariableId() - lit.getVariableId()) - (
                (sign != lit.sign) ? 1 : 0);
    }

    @Override
    public int hashCode() {
        int result = variable != null ? variable.hashCode() : 0;
        result = 31 * result + (sign ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Literal)) {
            return false;
        }

        final Literal literal = (Literal) o;

        if (sign != literal.sign) {
            return false;
        }
        return Objects.equals(variable, literal.variable);
    }

    @Override
    public String toString() {
        return (sign ? "-" : "") + variable.getId();
    }
}
