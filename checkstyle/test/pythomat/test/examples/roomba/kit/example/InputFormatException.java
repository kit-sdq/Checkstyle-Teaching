package kit.example;

/**
 * Exceptions for input format errors in the RobotTest example.
 * @author markus
 * @version 1
 */
public class InputFormatException extends Exception {
    private static final long serialVersionUID = -6836516356927561599L;

    /**
     * Create new InputFormatException.
     * @param message A message describing the error
     */
    public InputFormatException(String message) {
        super(message);
    }
}
