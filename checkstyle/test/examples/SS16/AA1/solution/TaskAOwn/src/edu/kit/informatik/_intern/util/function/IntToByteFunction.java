package edu.kit.informatik._intern.util.function;

/**
 * Represents a function that accepts one {@code int}-valued argument and produces a {@code byte}-valued result. This is
 * the {@code int} to {@code byte} specialization of {@link Function}.
 * 
 * <p>This is a {@linkplain FunctionalInterface functional interface} whose functional method is {@link #apply(int)}.
 * 
 * @author  Tobias Bachert
 * @version 1.02, 2016/03/18
 * 
 * @since   1.8
 */
@FunctionalInterface
public interface IntToByteFunction {
    
    /**
     * Applies this function to the given argument.
     * 
     * @param  value the function argument
     * @return the function result
     */
    byte apply(final int value);
}
