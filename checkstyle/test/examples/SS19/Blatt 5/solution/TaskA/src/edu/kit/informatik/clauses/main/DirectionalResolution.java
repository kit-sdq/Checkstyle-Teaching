/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.clauses.main;


import edu.kit.informatik.Terminal;
import edu.kit.informatik.clauses.cnf.Clause;
import edu.kit.informatik.clauses.cnf.Formula;
import edu.kit.informatik.clauses.cnf.InputFormatException;
import edu.kit.informatik.clauses.cnf.ResolutionException;
import edu.kit.informatik.clauses.cnf.Variable;
import edu.kit.informatik.clauses.dp.Bucket;
import edu.kit.informatik.clauses.dp.BucketException;
import edu.kit.informatik.clauses.dp.BucketList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Main class for the assignment. Encapsulates the user interaction and the
 * resolution result processing.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class DirectionalResolution {

    /**
     * Output for the result sat.
     */
    public static final String SAT = "SAT";

    /**
     * Output for the result unsat.
     */
    public static final String UNSAT = "UNSAT";


    /**
     * List to keep track of the adding order for the output.
     */
    private static List<Clause> allClauses;

    /**
     * Main methode of the program. Reads file content from the given path
     * and tries to resolve the content of the file.
     *
     * @param args takes exactly one path of a file as paramater, others
     *         will
     *         result in an error
     */
    public static void main(final String[] args) {
        allClauses = new ArrayList<>();

        if (args.length != 1) {
            Terminal.printError("file path missing.");
            return;
        }

        String[] input = Terminal.readFile(args[0]);

        Formula formula;
        try {
            formula = new Formula(input);
        } catch (InputFormatException e) {
            Terminal.printError(
                    "InputFormatException while reading File '" + args[0]
                    + "'");
            return;
        }

        boolean sat;
        try {
            sat = directionalResolution(formula);
        } catch (ResolutionException | BucketException e) {
            Terminal.printError(e.getLocalizedMessage());
            return;
        }

        Terminal.printLine(sat ? SAT : UNSAT);
    }

    /**
     * Implementation of the algorithm described on the assignment to resolve
     * the given formula.
     *
     * @param formula formula with all clauses for the resolution
     * @return whether or not the given formula can be satisfied
     * @throws BucketException occurs if problems with the buckets occur
     * @throws ResolutionException occurs if some problems occur during
     *         the resolution process
     */
    private static boolean directionalResolution(final Formula formula)
            throws BucketException, ResolutionException {
        // put clauses into buckets
        BucketList buckets = new BucketList();

        for (Clause clause : formula.getClauses()) {
            if (clause.isEmpty()) {
                return false;
            }
            buckets.addClause(clause);
            allClauses.add(clause);
        }

        // traverse buckets by variable order
        List<Variable> variables = formula.getVariables();
        Collections.sort(variables);

        for (Variable variable : variables) {
            Terminal.printLine("Processing Bucket " + variable.getId());

            Bucket bucket = buckets.get(variable);

            // resolve bucket
            List<Clause> resolvents = resolveAll(bucket);

            // add resolvents to bucketlist
            for (Clause clause : resolvents) {
                if (clause.isEmpty()) {
                    return false;
                }
                buckets.addClause(clause);
                allClauses.add(clause);
            }
        }

        return true;
    }

    /**
     * Resolves the given bucket.
     *
     * @param bucket a {@link Bucket} with clauses
     * @return list of clauses after the bucket has been resolved
     * @throws ResolutionException occurs if problems during the
     *         resolution arise
     */
    private static ArrayList<Clause> resolveAll(final Bucket bucket)
            throws ResolutionException {
        ArrayList<Clause> resolvents = new ArrayList<>();

        if (bucket == null || bucket.isEmpty()) {
            return new ArrayList<>();
        }

        Set<Clause> positiveClauses = bucket.getPositiveClauses();
        Set<Clause> negativeClauses = bucket.getNegativeClauses();

        for (Clause positiveClause : positiveClauses) {
            for (Clause negativeClause : negativeClauses) {
                Clause resolvent = positiveClause
                        .resolve(negativeClause, bucket.getVariable());
                if (allClauses.indexOf(negativeClause) > allClauses.indexOf(positiveClause)) {
                    Terminal.printLine(
                            positiveClause + "*" + negativeClause + "=" + resolvent);
                } else {
                    Terminal.printLine(
                            negativeClause + "*" + positiveClause + "=" + resolvent);
                }
                if (!resolvent.isTautological()) {
                    resolvents.add(resolvent);
                }
            }
        }

        return resolvents;
    }
}
