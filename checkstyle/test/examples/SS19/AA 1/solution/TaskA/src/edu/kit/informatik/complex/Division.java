/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.complex;

/**
 * Represents a division.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public class Division implements Expression {
    private final Expression divident;
    private final Expression divisor;
    
    /**
     * Creates a new division expression.
     * 
     * @param divident - the divident of the division
     * @param divisor - the divisor of the division
     */
    public Division(final Expression divident, final Expression divisor) {
        this.divident = divident;
        this.divisor = divisor;
    }

    @Override
    public ComplexNumber evaluate() throws FormatException {
        return this.divident.evaluate().divide(this.divisor.evaluate());
    }
}
