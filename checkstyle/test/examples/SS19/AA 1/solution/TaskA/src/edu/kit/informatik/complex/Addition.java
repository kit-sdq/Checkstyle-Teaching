/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.complex;

/**
 * Represents an addition.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public class Addition implements Expression {
    private final Expression summandOne;
    private final Expression summandTwo;
    
    /**
     * Creates a new addition expression with the two summands.
     * 
     * @param summandOne - first summand
     * @param summandTwo - second summand
     */
    public Addition(final Expression summandOne, final Expression summandTwo) {
        this.summandOne = summandOne;
        this.summandTwo = summandTwo;
    }

    @Override
    public ComplexNumber evaluate() throws FormatException {
        return this.summandOne.evaluate().add(this.summandTwo.evaluate());
    }
}
