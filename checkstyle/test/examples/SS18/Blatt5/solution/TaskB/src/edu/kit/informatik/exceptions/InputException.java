package edu.kit.informatik.exceptions;

/**
 * This is an exception that is thrown if a UI user input is invalid.
 *
 * @author Peter Oettig
 * @version 1.0
 */
public class InputException extends Exception {
    /**
     * The constructor of the edu.kit.informatik.exceptions.InputException that is thrown if a user input is invalid.
     *
     * @param message The error message to display to the user.
     */
    public InputException(String message) {
        super(message);
    }
}