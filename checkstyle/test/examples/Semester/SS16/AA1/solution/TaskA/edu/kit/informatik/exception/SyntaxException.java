package edu.kit.informatik.exception;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

/**
 * This exception is used to indicate a syntactic fault.
 * 
 * @author MartinLoeper
 * @version 1.0
 */
@SuppressWarnings("serial")
public class SyntaxException extends IllegalInputException {
    
    /**
     * Creates a new SyntaxException with given {@code msg}.
     * @param msg the message
     */
    public SyntaxException(String msg) {
        super(msg);
    }
    
    public static final Supplier<SyntaxException> createInstance(String message) {
        return IllegalInputException.<SyntaxException>createInstance(SyntaxException.class, message);
    }
}
