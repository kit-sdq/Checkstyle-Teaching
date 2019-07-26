package edu.kit.informatik.UI;

/**
 * This is an exception that is thrown if the input file contains invalid data.
 *
 * @author Peter Oettig
 * @version 1.0
 */
public class IllegalFileFormatException extends Exception {
    /**
     * The constructor of the edu.kit.informatik.UI.IllegalFileFormatException that is thrown
     * if there is invalid data in the input file.
     *
     * @param message The error message to display to the user.
     */
    public IllegalFileFormatException(String message) {
        super(message);
    }
}