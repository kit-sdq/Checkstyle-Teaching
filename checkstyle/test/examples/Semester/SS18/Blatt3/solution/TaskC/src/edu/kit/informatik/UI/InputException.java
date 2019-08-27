package edu.kit.informatik.UI;

public class InputException extends Exception {
    /**
     * The constructor of the edu.kit.informatik.UI.InputException that is thrown if a user input is invalid.
     *
     * @param message The error message to display to the user.
     */
    public InputException(String message) {
        super(message);
    }
}