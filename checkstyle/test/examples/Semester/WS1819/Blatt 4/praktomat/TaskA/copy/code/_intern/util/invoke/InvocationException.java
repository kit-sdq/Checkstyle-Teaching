package edu.kit.informatik._intern.util.invoke;

import java.lang.invoke.MethodHandle;
import java.util.Arrays;

/**
 * Exception used to indicate that the invocation of a method-handle threw an exception.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/06/19
 */
public final class InvocationException extends RuntimeException {
    
    private static final long serialVersionUID = -6162901942097650506L;
    
    /**
     * Creates a new {@code InvocationException} with the provided parameters.
     * 
     * @param methodhandle the method-handle that threw an exception on invocation
     * @param parameters used for the invocation
     * @param e the exception thrown
     */
    public InvocationException(final MethodHandle methodhandle, final Object[] parameters, final Throwable e) {
        super("Thrown while invoking " + methodhandle + " with " + Arrays.toString(parameters), e);
    }
}