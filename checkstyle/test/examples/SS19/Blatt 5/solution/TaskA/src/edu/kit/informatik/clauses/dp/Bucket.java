/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.clauses.dp;

import edu.kit.informatik.clauses.cnf.Clause;
import edu.kit.informatik.clauses.cnf.Literal;
import edu.kit.informatik.clauses.cnf.Variable;

import java.util.Set;
import java.util.TreeSet;

/**
 * Encapsulates a bucket as stated in the assignment.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class Bucket implements Comparable<Bucket> {

    /**
     * Variable of this bucket (you could say its index).
     */
    private Variable variable;

    /**
     * Positivie literal of the variable.
     */
    private Literal positiveLiteral;

    /**
     * Negative literal of the variable.
     */
    private Literal negativeLiteral;

    /**
     * Clauses with the positive literal.
     */
    private Set<Clause> positivieClauses = new TreeSet<>();

    /**
     * Clauses with the negative literal.
     */
    private Set<Clause> negativeClauses = new TreeSet<>();

    /**
     * Instantiates a new Bucket for the given variable.
     *
     * @param variable the variable of this bucket
     */
    public Bucket(final Variable variable) {
        this.variable = variable;
        positiveLiteral = Literal.get(this.variable, false);
        negativeLiteral = Literal.get(this.variable, true);
    }

    /**
     * Adds the given clause to this bucket if possible.
     *
     * @param clause a {@link Clause}
     * @throws BucketException occurs if this bucket is wrong for the
     *         given clause
     */
    public void addClause(final Clause clause) throws BucketException {

        Set<Clause> clauses;

        if (clause.contains(negativeLiteral)) {
            clauses = negativeClauses;
        } else if (clause.contains(positiveLiteral)) {
            clauses = positivieClauses;
        } else {
            throw new BucketException("Attempt to add clause to wrong Bucket");
        }

        TreeSet<Clause> subsumed = new TreeSet<>();
        for (Clause clause1 : clauses) {
            if (clause.isSubsumedBy(clause1)) {
                subsumed.add(clause1);
            } else if (clause.subsumes(clause1)) {
                return;
            }
        }

        clauses.removeAll(subsumed);
        clauses.add(clause);
    }

    /**
     * @return the variable of this bucket
     */
    public Variable getVariable() {
        return variable;
    }

    /**
     * @return set of the positive clauses for this bucket
     */
    public Set<Clause> getPositiveClauses() {
        return positivieClauses;
    }

    /**
     * @return set of the negative clauses for this bucket
     */
    public Set<Clause> getNegativeClauses() {
        return negativeClauses;
    }

    /**
     * @return whether or not this bucket is empty regarding the resolution
     *         algorithm, therefore whether or not one of the clause sets is
     *         empty ({@link #getPositiveClauses()} or {@link #getNegativeClauses()})
     */
    public boolean isEmpty() {
        return positivieClauses.isEmpty() || negativeClauses.isEmpty();
    }

    @Override
    public int compareTo(Bucket bucket) {
        return variable.compareTo(bucket.variable);
    }

}