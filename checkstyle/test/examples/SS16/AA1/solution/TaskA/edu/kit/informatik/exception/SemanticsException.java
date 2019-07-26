package edu.kit.informatik.exception;

import java.util.function.Supplier;

/**
 * This exception is used to indicate a semantic fault.
 * 
 * @author MartinLoeper
 * @version 1.0
 */
@SuppressWarnings("serial")
public class SemanticsException extends IllegalInputException {
    
    /**
     * Creates a new SemanticsException with given {@code msg}.
     * @param msg the message
     */
    public SemanticsException(String msg) {
        super(msg);
    }
    
    public static final Supplier<SemanticsException> createInstance(String message) {
        return IllegalInputException.<SemanticsException>createInstance(SemanticsException.class, message);
    }
}
