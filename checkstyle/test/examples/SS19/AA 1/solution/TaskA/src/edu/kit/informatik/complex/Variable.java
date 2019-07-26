/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.complex;

/**
 * Represents a variable.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public class Variable implements Expression {
    private final String name;
    private ComplexNumber value; 
    
    /**
     * Creates a new variable expression with a given name and an expression which is evaluated and
     * then the value of the variable is set to the evaluation result of the given expression.
     * 
     * @param name - the name of the variable
     * @param expression - the given expression
     * @throws FormatException - if evaluation fails
     */
    public Variable(final String name, final Expression expression) throws FormatException {
        this.name = name;
        this.value = expression.evaluate();
    }

    /**
     * Gets the name of this variable.
     * 
     * @return the name of the variable
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Sets the value of this variable to the evaluation result of the given expression.
     * 
     * @param expression - the given expression
     * @throws FormatException - if evaluation fails
     */
    public void setValue(final Expression expression) throws FormatException {
        this.value = expression.evaluate();
    }
    
    @Override
    public ComplexNumber evaluate() {
        return this.value;
    }
}
