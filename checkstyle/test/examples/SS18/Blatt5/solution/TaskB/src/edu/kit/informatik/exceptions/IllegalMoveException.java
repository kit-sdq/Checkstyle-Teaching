package edu.kit.informatik.exceptions;

/**
 * Thrown if a syntactical valid move is not legal for the current state of the UI.
 * 
 * @author Peter Oettig
 * @version 1.0
 */
public class IllegalMoveException extends Exception {
    /**
     * Creates a illegal move exception with an error message.
     * 
     * @param errorMessage
     *            This exception's message.
     */
    public IllegalMoveException(String errorMessage) {
        super(errorMessage);
    }
}
