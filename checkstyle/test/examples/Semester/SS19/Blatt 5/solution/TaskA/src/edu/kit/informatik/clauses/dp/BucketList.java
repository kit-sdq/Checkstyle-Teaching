/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */
package edu.kit.informatik.clauses.dp;

import edu.kit.informatik.clauses.cnf.Clause;
import edu.kit.informatik.clauses.cnf.Variable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulates a list of all available buckets as stated in the assignment.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public final class BucketList {

    /**
     * Map containing all available buckets.
     */
    private Map<Variable, Bucket> buckets = new HashMap<>();

    /**
     * Adds the given clause to the correct bucket in this bucket list.
     *
     * @param clause the {@link Clause} to add to this bucket list
     * @throws BucketException occurs if the clause cannot be added
     */
    public void addClause(Clause clause) throws BucketException {
        Variable variable = Collections.min(clause).getVariable();
        if (!buckets.containsKey(variable)) {
            buckets.put(variable, new Bucket(variable));
        }
        buckets.get(variable).addClause(clause);
    }

    /**
     * Return the bucket for the given variable.
     *
     * @param variable a {@link Variable}
     * @return the bucket of the variable
     */
    public Bucket get(Variable variable) {
        return buckets.get(variable);
    }
}
