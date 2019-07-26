package edu.kit.informatik.exception;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

/**
 * This exception is used as base class for all possible failures of the user to provide
 * a valid command-line argument.
 * Thus it cannot be directly instantiated but one of its subtypes has to be used to further
 * specify the precise type of failure.
 * 
 * @author MartinLoeper
 * @version 1.0
 */
@SuppressWarnings("serial")
public abstract class IllegalInputException extends Exception {
    
    /**
     * Creates an Exception with the given {@code msg}.
     * @param msg the message
     */
    IllegalInputException(String msg) { 
        super(msg); 
    }

    IllegalInputException(String msg, Throwable wrappedThrowable) {
        super(msg, wrappedThrowable);
    }

    // please note that a subclass may only call this method iff it implements a string constructor
    protected static <X extends IllegalInputException> Supplier<X> createInstance(Class<X> exception, String message) {
        return new Supplier<X>() {
            @Override
            public X get() {
                try {
                    return exception.getConstructor(String.class).newInstance(message);
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    // will not happen in this package, or programmer error
                    throw new IllegalStateException("Error, a String constructor for passing a message is necessary in class: " + exception.getCanonicalName());
                }
            }
        };
    }
}
