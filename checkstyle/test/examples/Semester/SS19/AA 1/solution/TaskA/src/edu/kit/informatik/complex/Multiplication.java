/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.complex;

/**
 * Represents a multiplication.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public class Multiplication implements Expression {
    private final Expression factorOne;
    private final Expression factorTwo;
    
    /**
     * Creates a new multiplication expression with the two factors.
     * 
     * @param factorOne - first factor
     * @param factorTwo - second factor
     */
    public Multiplication(final Expression factorOne, final Expression factorTwo) {
        this.factorOne = factorOne;
        this.factorTwo = factorTwo;
    }

    @Override
    public ComplexNumber evaluate() throws FormatException {
        return this.factorOne.evaluate().multiply(this.factorTwo.evaluate());
    }
}
