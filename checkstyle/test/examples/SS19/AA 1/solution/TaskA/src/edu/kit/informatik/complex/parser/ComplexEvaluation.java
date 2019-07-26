/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.complex.parser;

import java.util.HashMap;
import java.util.Map;

import edu.kit.informatik.complex.ComplexNumber;
import edu.kit.informatik.complex.FormatException;
import edu.kit.informatik.complex.Variable;

/**
 * Represents a complex evaluation system for implementing the assignment.
 * It manages the assignment and the evaluation of variables.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public class ComplexEvaluation {
    private static final String VARIABLE_FORMAT = "[\\$_a-zA-Z]+[\\$_\\w]*";
    private final Map<String, Variable> variables;
    
    /**
     * Creates a new complex evaluation.
     */
    public ComplexEvaluation() {
        this.variables = new HashMap<>();
    }
    
    /**
     * Assigns the variable with the given name to the evaluation of the given expression string.
     * If the variable does not exist, it is created. 
     * The result of the evaluation is also always stored in the last variable until the next assignment occurs.
     * 
     * @param name - the given name
     * @param expressionString - the given expression string without any spaces
     * @return the complex number which is result of the expression evaluation
     * @throws FormatException - if the name or the expressionString is not formatted correctly
     */
    public ComplexNumber assign(final String name, final String expressionString) throws FormatException {
        if (!name.matches(VARIABLE_FORMAT)) {
            throw new FormatException("variable has an invalid format");
        }
        final ComplexNumber result = evaluate(expressionString);
        if (!this.variables.containsKey(name)) {
            this.variables.put(name, new Variable(name, result));
        } else {
            this.variables.get(name).setValue(result);
        }
        return result;
    }
    
    private ComplexNumber evaluate(final String expressionString) throws FormatException {
        final ComplexExpressionParser parser 
            = new ComplexExpressionParser(expressionString, new HashMap<>(this.variables));
        final ComplexNumber result = parser.parse().evaluate();
        
        return result;
    }
    
    /**
     * Evaluates a variable.
     * 
     * @param variableName - the name of the variable
     * @return the value of the variable specified by the given name
     * @throws FormatException - if the variable name is not valid, i.e. it is not set or has a wrong format 
     */
    public ComplexNumber evaluateVariable(final String variableName) throws FormatException {
        // expression can only be a variable
        if (this.variables.containsKey(variableName) && this.variables.get(variableName) != null) {
            final Variable variable = this.variables.get(variableName);
            return variable.evaluate();
        } else {
            throw new FormatException("variable named \"" + variableName + "\" is not set");
        }
        
        // general evaluation of expression would be:
        // return evaluate(expressionString, true);
    }
}
