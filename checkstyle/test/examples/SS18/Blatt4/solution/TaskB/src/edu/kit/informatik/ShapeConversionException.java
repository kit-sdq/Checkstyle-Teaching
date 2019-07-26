package edu.kit.informatik;

/**
 * This is an exception that is thrown if a string representation of a shape
 * couldn't be converted successfully into an acutal {@link Shape} instance.
 *
 * @author Peter Oettig
 * @version 1.0
 */
public class ShapeConversionException extends Exception {
    /**
     * The constructor of the edu.kit.informatik.ShapeConversionException that is thrown if a shape in
     * string representation could not be converted to the enum representation (e.g. if the shape does not exist).
     *
     * @param message The error message to display to the user.
     */
    public ShapeConversionException(String message) {
        super(message);
    }
}