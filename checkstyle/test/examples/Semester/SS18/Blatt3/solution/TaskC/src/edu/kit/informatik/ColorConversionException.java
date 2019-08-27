package edu.kit.informatik;

public class ColorConversionException extends Exception {
    /**
     * The constructor of the edu.kit.informatik.ColorConversionException that is thrown if a color in
     * string representation could not be converted to the enum representation (e.g. if the color does not exist).
     *
     * @param message The error message to display to the user.
     */
    public ColorConversionException(String message) {
        super(message);
    }
}