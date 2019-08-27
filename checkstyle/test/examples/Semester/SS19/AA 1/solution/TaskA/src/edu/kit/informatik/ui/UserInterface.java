/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.ui;

import java.util.Arrays;

import javax.lang.model.SourceVersion;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.complex.ComplexNumber;
import edu.kit.informatik.complex.FormatException;
import edu.kit.informatik.complex.parser.ComplexEvaluation;
import edu.kit.informatik.complex.parser.Operation;
import edu.kit.informatik.complex.parser.Token;

/**
 * Represents the user interface of the complex number calculator.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public class UserInterface {
    private static final String QUIT = "quit";
    private static final String SPACE = " ";
    private static final String ASSIGNMENT_SYMBOL = "=";
    private static final String ALLOWED_SYMBOLS = "(\\(|\\)|[a-zA-Z]|\\$|_|=|[0-9]|\\+|\\*|-|/|\\[|\\]|\\s)+";
    
    private static final int NUMBER_ASSIGNMENT_SIDES = 2;
    
    private String input;
    private final ComplexEvaluation evaluator;
    private boolean isQuit;
    
    /**
     * Creates a new user interface.
     */
    public UserInterface() {
        this.input = null;
        this.evaluator = new ComplexEvaluation();
        this.isQuit = false;
    }
    
    /**
     * Interacts with the user, i.e. requesting input, evaluating it and printing a result or
     * - in case of an error - an error message.
     */
    public void interact() {
        this.input = Terminal.readLine();
        if (!input.equals(QUIT)) {
            try {
                evaluate();
            } catch (final FormatException e) {
                Terminal.printError(e.getMessage());
            }
        } else {
            this.isQuit = true;
        }
    }
    
    /**
     * Determines whether the user interface was quit.
     * 
     * @return whether the user interface was quit
     */
    public boolean isQuit() {
        return this.isQuit;
    }
    
    /*
     * evaluates the input using the ComplexEvaluation evaluator
     */
    private void evaluate() throws FormatException {
        if (!this.input.matches(ALLOWED_SYMBOLS)) {
            throw new FormatException("empty input or illegal symbol in input");
        }
        
        final boolean ok = isSpacesFormatOk();
        if (!ok) {
            throw new FormatException("invalid format of spaces");
        }
        final String inputWithoutSpaces = this.input.replaceAll(SPACE, "");
        
        if (inputWithoutSpaces.contains(ASSIGNMENT_SYMBOL)) {
            final String[] splitted = inputWithoutSpaces.split(ASSIGNMENT_SYMBOL);
            if (splitted.length == NUMBER_ASSIGNMENT_SIDES) {
                final String variableName = splitted[0];
                if (variableName.equals(QUIT) || SourceVersion.isKeyword(variableName)) {
                    throw new FormatException("variable must not be named \"" + variableName + "\".");
                }
                final String expressionString = splitted[1];
                printAssignment(variableName, this.evaluator.assign(variableName, expressionString));
            } else {
                throw new FormatException("too many \"" + ASSIGNMENT_SYMBOL + "\" symbols or no expression");
            }
        } else {
            printAssignment(inputWithoutSpaces, this.evaluator.evaluateVariable(inputWithoutSpaces));
        }
    }

    /*
     * checks the format of spaces
     */
    private boolean isSpacesFormatOk() throws FormatException {
        if (this.input.contains(ASSIGNMENT_SYMBOL)) {
            boolean spacesOk = !this.input.startsWith(SPACE) && !input.endsWith(SPACE);
            boolean beforeAssignmentSymbol = true;
            for (int i = 1; i < this.input.length() - 1 && spacesOk; i++) {
                final char before = this.input.charAt(i - 1);
                final char now = this.input.charAt(i);
                final char next = this.input.charAt(i + 1);
                
                final char space = SPACE.charAt(0);
                final char parenthesisOpen = Operation.PARENTHESES_OPEN.getSymbol();
                final char parenthesisClose = Operation.PARENTHESES_CLOSE.getSymbol();
                final char complexOpen = Token.COMPLEX_OPEN;
                final char complexClose = Token.COMPLEX_CLOSE;
                
                if (now == ASSIGNMENT_SYMBOL.charAt(0)) {
                    beforeAssignmentSymbol = false;
                    spacesOk = before == space && next == space;
                } else if (now == parenthesisOpen) {
                    spacesOk = (before == parenthesisOpen || before == space) && next != space;
                } else if (now == parenthesisClose) {
                    spacesOk = (next == parenthesisClose || next == space) && before != space;
                } else if (now == complexOpen) {
                    spacesOk = before == space && next != space;
                } else if (now == complexClose) {
                    spacesOk = next == space && before != space;
                } else if (!beforeAssignmentSymbol 
                        && Arrays.stream(Operation.valuesOperators()).anyMatch(x -> x.getSymbol() == now)) {
                    // now is an operator (+,-,*,/)
                    
                    // minus can be proceeded by a complex open and followed by a number
                    final boolean isMinus = now == Operation.SUBTRACT.getSymbol();
                    final boolean isMinusFormatOk = isMinus && (before == complexOpen || before == space)
                            && Character.isDigit(next);
                    spacesOk = (before == space && next == space) || isMinusFormatOk;
                } else if (now == space) {
                    spacesOk = before != space && next != space;
                }
            }
            
            return spacesOk;
        }
        return !this.input.contains(SPACE);
    }

    private static void printAssignment(final String variableName, final ComplexNumber result) {
        Terminal.printLine(variableName + SPACE + ASSIGNMENT_SYMBOL + SPACE + result);
    }
}
