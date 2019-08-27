/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.complex.parser;

/**
 * Represents an operation, i.e. an operator or a paranthesis.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public enum Operation {
    /**
     * Add operation
     */
    ADD('+', 1),
    
    /**
     * Subtract operation.
     */
    SUBTRACT('-', 1), 
    
    /**
     * Multiply operation.
     */
    MULTIPLY('*', 2), 
    
    /**
     * Divide operation.
     */
    DIVIDE('/', 2), 
    
    /**
     * Open parenthesis operation.
     */
    PARENTHESES_OPEN('[', 3),
    
    /**
     * Close parenthesis operation.
     */
    PARENTHESES_CLOSE(']', 3);
    
    private char symbol;
    private final int priority;
    
    /**
     * Constructor of the operation enum.
     * 
     * @param symbol - the symbol
     * @param priority - the priority
     */
    Operation(final char symbol, final int priority) {
        this.symbol = symbol;
        this.priority = priority;
    }
    
    /**
     * Gets the symbol of this operation.
     * 
     * @return the symbol
     */
    public char getSymbol() {
        return this.symbol;
    }
    
    /**
     * Gets the priority of this operation.
     * 
     * @return the priority of this operation
     */
    public int getPriority() {
        return this.priority;
    }
    
    /**
     * Gets all operators (+,-,*,/) which are not parentheses.
     * 
     * @return all operators (+,-,*,/)
     */
    public static Operation[] valuesOperators() {
        return new Operation[]{ADD, SUBTRACT, MULTIPLY, DIVIDE};
    }
    
    /**
     * Gets the operation which is represented by the given symbol.
     * 
     * @param symbol - the given symbol
     * @return the operation represented by the given symbol or null if none is found
     */
    public static Operation of(char symbol) {
        for (Operation op : values()) {
            if (op.getSymbol() == symbol) {
                return op;
            }
        }
        return null;
    }
}
