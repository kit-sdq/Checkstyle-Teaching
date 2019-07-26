/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.complex.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import edu.kit.informatik.complex.Addition;
import edu.kit.informatik.complex.Division;
import edu.kit.informatik.complex.Expression;
import edu.kit.informatik.complex.FormatException;
import edu.kit.informatik.complex.Multiplication;
import edu.kit.informatik.complex.Subtraction;
import edu.kit.informatik.complex.Variable;

/**
 * Represents a complex expression parser for a given expression string.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public class ComplexExpressionParser {
    private String expressionString;
    private final Map<String, Variable> variables;

    private final List<Token> tokens;
    private final Stack<Token> rpnTokens;
    
    private Expression result;

    /**
     * Creates a new parser for the given expression string and the variables defined in the given
     * copy of the variables map.
     * 
     * @param expressionString - the expression string without any spaces
     * @param variables - a copy of the variables map
     */
    public ComplexExpressionParser(final String expressionString, final Map<String, Variable> variables) {
        this.expressionString = expressionString;
        this.variables = variables;

        this.tokens = new ArrayList<Token>();
        this.rpnTokens = new Stack<Token>();
        
        this.result = null;
    }

    /**
     * Parses the expression, i.e. creating an expression which represents the expression string of this parser.
     * 
     * @return the parsed expression which can be easily evaluated
     * @throws FormatException - if the format of the expression string is invalid
     */
    public Expression parse() throws FormatException {
        if (this.result == null) {
            replaceVariables();
            tokenize();
            toReversePolishNotation();
            this.result = createExpression(); 
        }
        return this.result;
    }

    /*
     * replaces the variables inside the expression string with the variables defined in the variable map
     */
    private void replaceVariables() {
        for (final String name : this.variables.keySet()) {
            if (this.variables.get(name) != null) {
                this.expressionString 
                    = this.expressionString.replaceAll(name, this.variables.get(name).evaluate().toString());
            }
        }
        this.expressionString = this.expressionString.replaceAll(" ", "");
    }

    /*
     * tokenizes the expression string, savinf the 
     */
    private void tokenize() throws FormatException {
        String tokenString = "";
        boolean complexNumberDefinition = false;
        for (int i = 0; i < this.expressionString.length(); i++) {
            final char c = this.expressionString.charAt(i);
            tokenString += c;
            if (c == Token.COMPLEX_OPEN) {
                if (complexNumberDefinition) {
                    throw new FormatException("syntax error due to \"" + Token.COMPLEX_OPEN + "\"");
                }
                complexNumberDefinition = true;
            } else if (c == Token.COMPLEX_CLOSE) {
                if (!complexNumberDefinition) {
                    throw new FormatException("syntax error due to \"" + Token.COMPLEX_CLOSE + "\"");
                }
                this.tokens.add(new Token(tokenString));
                tokenString = "";
                complexNumberDefinition = false;
            } else if (!complexNumberDefinition && Operation.of(c) != null) {
                this.tokens.add(new Token(Operation.of(c)));
                tokenString = "";
            } else if (!complexNumberDefinition) {
                throw new FormatException("undefined variable was used");
            }
        }
    }

    /*
     * Shunting-yard algorithm for parsing left to right and respecting priorities
     */
    private void toReversePolishNotation() throws FormatException {
        final Stack<Operation> operatorStack = new Stack<>();

        for (final Token token : this.tokens) {
            if (token.isComplexNumber()) {
                this.rpnTokens.push(token);
            } else if (token.isOperation() && token.getOperation() != Operation.PARENTHESES_CLOSE) {
                if (!operatorStack.isEmpty()) {
                    final Operation top = operatorStack.peek();
                    final boolean higherOrEqualPriority = top.getPriority() >= token.getOperation().getPriority();
                    while (!operatorStack.isEmpty() && operatorStack.peek() != Operation.PARENTHESES_OPEN 
                            && higherOrEqualPriority) {
                        this.rpnTokens.push(new Token(operatorStack.pop()));
                    }
                }
                operatorStack.push(token.getOperation());
            } 
            else if (token.isOperation() && token.getOperation() == Operation.PARENTHESES_CLOSE) {
                if (operatorStack.isEmpty()) {
                    throw new FormatException("syntax error due to invalid parentheses");
                }
                while (operatorStack.peek() != Operation.PARENTHESES_OPEN) {
                    this.rpnTokens.push(new Token(operatorStack.pop()));
                }
                operatorStack.pop();
            }
        }

        while (!operatorStack.isEmpty()) {
            this.rpnTokens.push(new Token(operatorStack.pop()));
        }
    }

    /*
     * creates the expressions from the reverse polish notation created by the Shunting-yard algorithm
     */
    private Expression createExpression() throws FormatException {
        // the tokens in rpnTokens must be reversed in order to make the expression creation algorithm work
        final Stack<Token> pnTokens = new Stack<>();
        while (!this.rpnTokens.isEmpty()) {
            pnTokens.push(this.rpnTokens.pop());
        }
        
        final Stack<Expression> expressions = new Stack<>();
        while (!pnTokens.isEmpty()) {
            final Token token = pnTokens.pop();
            if (token.isComplexNumber()) {
                expressions.push(token.getNumber());
            } 
            else {
                // operation expression creation
                if (expressions.size() < 2) {
                    throw new FormatException("syntax error");
                }
                // second expression is on top of the stack, first is below the second, then the operator
                final Expression second = expressions.pop();
                final Expression first = expressions.pop();
                switch (token.getOperation()) {
                    case ADD:
                        expressions.push(new Addition(first, second));
                        break;
                    case SUBTRACT:
                        expressions.push(new Subtraction(first, second));
                        break;
                    case MULTIPLY:
                        expressions.push(new Multiplication(first, second));
                        break;
                    case DIVIDE:
                        expressions.push(new Division(first, second));
                        break;
                    default:
                        throw new FormatException("syntax error");
                }
            }
        }
        
        final Expression returnExpression;
        if (!expressions.isEmpty()) {
            returnExpression = expressions.pop();
        } else {
            throw new FormatException("syntax error");
        }
        
        if (!expressions.isEmpty()) {
            throw new FormatException("syntax error");
        }
        
        return returnExpression;
    }
}
