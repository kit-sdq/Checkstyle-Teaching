package edu.kit.informatik;

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
