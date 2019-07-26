/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.complex;

/**
 * Represents an abstract expression.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public interface Expression {
    
    /**
     * Evaluates the expression to a complex number result.
     * 
     * @return the result of the expression evaluation
     * @throws FormatException - if an invalid operation (like / by 0) occurs during evaluation
     */
    ComplexNumber evaluate() throws FormatException;
}
