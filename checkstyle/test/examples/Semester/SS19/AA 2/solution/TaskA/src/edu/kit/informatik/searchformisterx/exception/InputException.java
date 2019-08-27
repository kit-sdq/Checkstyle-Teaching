package edu.kit.informatik.searchformisterx.exception;

/**
 * This is an exception that is thrown if a user input is invalid or an
 * invalid user input is found in the processing of the input.
 *
 * @author Peter Oettig
 * @version 1.0
 */
public class InputException extends Exception {
    /**
     * The constructor of the InputException that is thrown if a user input is invalid.
     *
     * @param message The error message to display to the user.
     */
    public InputException(String message) {
        super(message);
    }
}