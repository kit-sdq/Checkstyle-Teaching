package edu.kit.informatik._intern.util.memoizer;

/**
 * Represents a function that accepts one argument and produces a result. This is the {@code
 * InterruptedException}-throwing specialization of {@link java.util.function.Function}.
 * 
 * <p>This is a {@linkplain FunctionalInterface functional interface} whose functional method is {@link
 * #compute(Object)}.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/08/12
 * 
 * @param   <T> the type parameter of the input argument
 * @param   <R> the type parameter of the result
 * @param   <X> the type parameter of the throwable
 */
@FunctionalInterface
public interface Computable<T, R> {
    
    /**
     * Applies this function to the given argument.
     * 
     * @param  value the function argument
     * @return the function result
     * @throws InterruptedException if the thread gets interrupted while applying this function
     */
    R compute(
            final T value) throws InterruptedException;
}
