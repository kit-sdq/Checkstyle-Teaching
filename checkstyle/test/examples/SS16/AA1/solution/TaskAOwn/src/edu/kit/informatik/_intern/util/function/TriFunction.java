package edu.kit.informatik._intern.util.function;

import java.util.Objects;
import java.util.function.Function;

/**
 * Represents a function that accepts three arguments and produces a result. This is the three-arity specialization of
 * {@link Function}.
 * 
 * <p>This is a {@linkplain FunctionalInterface functional interface} whose functional method is {@link #apply(Object,
 * Object, Object)}.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/03/03
 * 
 * @param   <T> the type of the first argument to the function
 * @param   <U> the type of the second argument to the function
 * @param   <V> the type of the third argument to the function
 * @param   <R> the type of the result of the function
 * 
 * @since   1.8
 */
@FunctionalInterface
public interface TriFunction<T, U, V, R> {
    
    /**
     * Returns a {@code TriFunction} that returns {@code null} when applied.
     * 
     * @return a {@code TriFunction} that returns {@code null} when applied
     */
    static <T, U, V, R> TriFunction<T, U, V, R> empty() {
        return (t, u, v) -> null;
    }
    
    /**
     * Applies this function to the given arguments.
     * 
     * @param  t the first function argument
     * @param  u the second function argument
     * @param  v the third function argument
     * @return the function result
     */
    R apply(final T t, final U u, final V v);
    
    /**
     * Returns a composed {@code TriFunction} that first applies this function to its input, and then applies the after
     * function to the result. If evaluation either function throws an exception, it is relayed to the caller of the
     * composed operation.
     * 
     * @param  <Z> the type of output of the {@code after} function, and of the composed function
     * @param  after the function to apply after this function is applied
     * @return a composed {@code TriFunction} that first applies this function and then applies the after function
     */
    default <Z> TriFunction<T, U, V, Z> andThen(final Function<? super R, ? extends Z> after) {
        Objects.requireNonNull(after);
        
        return (t, u, v) -> after.apply(apply(t, u, v));
    }
}
