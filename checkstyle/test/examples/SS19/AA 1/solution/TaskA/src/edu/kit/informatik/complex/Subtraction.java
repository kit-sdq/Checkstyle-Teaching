/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.complex;

/**
 * Represents a subtraction.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public class Subtraction implements Expression {
    private final Expression minuend;
    private final Expression subtrahend;
    
    /**
     * Creates a new subtraction expression.
     * 
     * @param minuend - the minuend of the subtraction
     * @param subtrahend -  the subtrahend of the subtraction
     */
    public Subtraction(final Expression minuend, final Expression subtrahend) {
        this.minuend = minuend;
        this.subtrahend = subtrahend;
    }

    @Override
    public ComplexNumber evaluate() throws FormatException {
        return this.minuend.evaluate().subtract(this.subtrahend.evaluate());
    }
}
