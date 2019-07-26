/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.complex.parser;

import edu.kit.informatik.complex.ComplexNumber;
import edu.kit.informatik.complex.FormatException;

/**
 * Represents a token, i.e. an operation or a complex number definition.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public class Token {
    
    /**
     * The opening parenthesis of the complex number definition.
     */
    public static final char COMPLEX_OPEN = '(';
    
    /**
     * The closing parenthesis of the complex number definition.
     */
    public static final char COMPLEX_CLOSE = ')';
    
    private static final String COMPLEX_FORMAT = "\\" + COMPLEX_OPEN + "-?\\d+\\+-?\\d+i" + "\\"
            + COMPLEX_CLOSE;

    private final Operation operation;
    private final ComplexNumber number;
    private final boolean isOperation;

    /**
     * Creates a new token from an operation.
     * 
     * @param operation - the operation
     */
    public Token(final Operation operation) {
        this.operation = operation;
        this.number = null;
        this.isOperation = true;
    }
    
    /**
     * Creates a new token from a complex number definition string.
     * 
     * @param complexNumberString - the complex number definition string
     * @throws FormatException - if the format of the complex number definition is invalid
     */
    public Token(final String complexNumberString) throws FormatException {
        throwIfInvalidFormat(complexNumberString);
        this.operation = null;
        final String withoutParenthesesAndI = complexNumberString.substring(1, complexNumberString.length() - 2);
        final String[] splitted = withoutParenthesesAndI.split("\\+");
        try {
            final int real = Integer.parseInt(splitted[0]);
            final int imaginary = Integer.parseInt(splitted[1]);
            this.number = new ComplexNumber(real, imaginary);
        } catch (final NumberFormatException exc) {
            throw new FormatException("illegal number format");
        }
        this.isOperation = false;
    }

    private void throwIfInvalidFormat(final String complexNumberString) throws FormatException {
        if (!complexNumberString.matches(COMPLEX_FORMAT)) {
            throw new FormatException("illegal complex number format");
        }
    }

    /**
     * Gets the operation of this token.
     * 
     * @return the operation of this token or null if this token holds a complex number
     */
    public Operation getOperation() {
        return this.operation;
    }

    /**
     * Gets the complex number of this token.
     * 
     * @return the complex number of this token or null if this token holds an operation
     */
    public ComplexNumber getNumber() {
        return this.number;
    }

    /**
     * Determines whether this token holds a complex number.
     * 
     * @return whether this token holds a complex number
     */
    public boolean isComplexNumber() {
        return !isOperation();
    }

    /**
     * Determines whether this token holds an operation.
     * 
     * @return whether this token holds an operation
     */
    public boolean isOperation() {
        return this.isOperation;
    }

    @Override
    public String toString() {
        return isOperation() ? "" + this.operation : "" + this.number;
    }

}
