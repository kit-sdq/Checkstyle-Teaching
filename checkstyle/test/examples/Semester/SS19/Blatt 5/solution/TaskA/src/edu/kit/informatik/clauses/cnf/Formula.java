/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.clauses.cnf;

import java.util.ArrayList;
import java.util.List;


/**
 * Encapsulates a formula, consisting of a number of {@link Variable} and a
 * number
 * of {@link Clause}. This class is primarily used to read and process the
 * given cnf format files and save its information.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class Formula {

    /**
     * String to start a p line.
     */
    public static final String P_CHARACTER = "p";

    /**
     * String to start a comment line.
     */
    public static final String C_CHARACTER = "c";

    /**
     * String of the format specification.
     */
    public static final String CNF_CHARACTERS = "cnf";

    /**
     * String to end a clause definition.
     */
    public static final String END_CLAUSE = "0";

    /**
     * Input separator.
     */
    public static final String SEPARATOR = " ";

    /**
     * List of all variables in the clauses.
     */
    private List<Variable> variables = new ArrayList<>();

    /**
     * List of clauses.
     */
    private List<Clause> clauses = new ArrayList<>();

    /**
     * Number of variables.
     */
    private int variableCount;

    /**
     * Number of clauses.
     */
    private int clauseCount;

    /**
     * Reads the data from the given String array and saves it.
     *
     * @param fileContentInCnf String array containing the data from the
     *         file in cnf
     *         format
     * @throws InputFormatException in case the data is malformed
     */
    public Formula(final String[] fileContentInCnf) throws InputFormatException {
        readFormulaFromFile(fileContentInCnf);
    }

    /**
     * Returns a list of all variables of this formula.
     *
     * @return a {@link Variable} list
     */
    public List<Variable> getVariables() {
        return variables;
    }

    /**
     * Returns a list of all clauses of this formula.
     *
     * @return a {@link Clause} list
     */
    public List<Clause> getClauses() {
        return clauses;
    }

    /**
     * Reads the data from the given string array and throws an exception if
     * the format is invalid. For the format definition refer to the assignment.
     *
     * @param fileContentInCnf String array containing the data from the
     *         file in cnf format
     * @throws InputFormatException in case the data is malformed
     */
    private void readFormulaFromFile(final String[] fileContentInCnf)
            throws InputFormatException {

        if (fileContentInCnf.length < 1) {
            throw new InputFormatException("empty files are not allowed");
        }

        int index = 0;
        boolean pLineRead = false;

        while (index < fileContentInCnf.length) {

            String line = fileContentInCnf[index++];

            String[] tokens = line.split(SEPARATOR);

            if (tokens[0].equals(P_CHARACTER)) {
                pLineCheck(tokens[1], pLineRead);
                pLineRead = true;
                if (tokens.length != 4) {
                    throw new InputFormatException("invalid number of "
                                                   + "parameters for p line");
                }
                try {
                    variableCount = Integer.parseInt(tokens[2]);
                    clauseCount = Integer.parseInt(tokens[3]);
                } catch (NumberFormatException e) {
                    throw new InputFormatException("illegal clause or "
                                                   + "variable number");
                }

            } else if (!tokens[0].equals(C_CHARACTER)) {
                clauseLineCheck(tokens[tokens.length - 1], pLineRead);
                addClause(tokens);
            }
        }

        if (!pLineRead) {
            throw new InputFormatException(
                    "Missing p-Line at start of document");
        }
    }

    /**
     * Adds a new clause to this formula based on the data given in the
     * String array.
     *
     * @param tokens String array containing the literal informations
     * @throws InputFormatException occurs if the given tokens do not
     *         conform the format
     */
    private void addClause(final String[] tokens) throws InputFormatException {
        Clause clause = new Clause();

        for (int i = 0; i < tokens.length - 1; i++) {
            if (tokens[i].isEmpty()) {
                continue;
            }
            int var;
            try {
                var = Integer.parseInt(tokens[i]);
                if (var == 0) {
                    throw new InputFormatException(
                            "Expected Variable Id but got " + tokens[i]);
                }
            } catch (NumberFormatException ne) {
                throw new InputFormatException(
                        "Expected Variable Id but got " + tokens[i]);
            }
            Variable variable = Variable.get(Math.abs(var), this);
            Literal literal = Literal.get(variable, var < 0);
            if (!variables.contains(variable)) {
                variables.add(variable);
            }
            clause.add(literal);
        }

        clauses.add(clause);

    }

    /**
     * Checks constraints for the given parameters.
     *
     * @param token String containing "cnf" or an exception is thrown
     * @param pLineRead if there is already a p line, which would lead
     *         to an exception
     * @throws InputFormatException occurs if the given parameters are
     *         invalid for this state
     */
    private void pLineCheck(final String token, final boolean pLineRead)
            throws InputFormatException {
        if (!token.equals(CNF_CHARACTERS)) {
            throw new InputFormatException("Expected cnf");
        }
        if (pLineRead) {
            throw new InputFormatException("Duplicate p-Line");
        }
    }

    /**
     * Checks constraints for the given parameters.
     *
     * @param token String containing "0" or an exception is thrown
     * @param pLineRead if there is no p line, which would lead
     *         to an exception
     * @throws InputFormatException occurs if the given parameters are
     *         invalid for this state
     */
    private void clauseLineCheck(final String token, final boolean pLineRead)
            throws InputFormatException {
        if (!token.equals(END_CLAUSE)) {
            throw new InputFormatException("Expected 0");
        }
        if (!pLineRead) {
            throw new InputFormatException(
                    "Missing p-Line at start of document");
        }
    }

    /**
     * @return the biggest available variable id
     */
    public int getVariableCount() {
        return variableCount;
    }
}
