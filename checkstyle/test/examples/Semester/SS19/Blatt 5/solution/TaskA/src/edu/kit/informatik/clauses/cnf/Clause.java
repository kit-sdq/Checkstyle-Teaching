/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.clauses.cnf;


import java.util.TreeSet;

/**
 * Represents a clause as a set of {@link Literal}.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class Clause extends TreeSet<Literal> implements Comparable<Clause> {

    /**
     * Checks whether or not the given clause isSubsumedBy this one.
     *
     * @param clause a {@link Clause}
     * @return boolean if given clause subsumes this
     */
    public boolean isSubsumedBy(Clause clause) {
        return !(this.size() > clause.size()) && clause.containsAll(this);
    }

    /**
     * Checks whether or not this clause subsumes the given one.
     *
     * @param clause a {@link Clause}
     * @return boolean if this isSubsumedBy the given clause
     */
    public boolean subsumes(Clause clause) {
        return clause.isSubsumedBy(this);
    }

    /**
     * Checks whether or not this clause is tautological, therefore if it
     * contains the same literal negated and not negated.
     *
     * @return boolean if this is tautological
     */
    public boolean isTautological() {
        for (Literal lit : this) {
            Literal lit2 = Literal.get(lit.getVariable(), !lit.isSign());
            if (this.contains(lit2)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Resolves the given clause with this clause for the given variable.
     *
     * @param clause a {@link Clause}
     * @param variable a {@link Variable}
     * @return the resolved Clause
     * @throws ResolutionException occurs if the literals do not conform
     *         the algorithm
     */
    public Clause resolve(final Clause clause, final Variable variable)
            throws ResolutionException {
        Clause resolvent = new Clause();

        Literal lit1 = null;
        Literal lit2 = null;

        for (Literal lit : clause) {
            if (lit.getVariable() != variable) {
                resolvent.add(lit);
            } else {
                lit1 = lit;
            }
        }

        for (Literal lit : this) {
            if (lit.getVariable() != variable) {
                resolvent.add(lit);
            } else {
                lit2 = lit;
            }
        }

        if (lit1 == null || lit2 == null) {
            throw new ResolutionException("Resolution literal not found");
        }

        if (lit1.isSign() == lit2.isSign()) {
            throw new ResolutionException(
                    "Resolution literals have same polarity");
        }

        return resolvent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        int i = 0;
        for (Literal lit : this) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append(lit);
            ++i;
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(final Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public int compareTo(Clause o) {
        if (o.equals(this)) {
            return 0;
        } else if (this.size() - o.size() != 0) {
            return this.size() - o.size();
        } else {
            return -1;
        }
    }

}