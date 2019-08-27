package edu.kit.informatik._intern.util.function;

import java.util.Objects;
import java.util.function.Function;

/**
 * Represents a function that accepts one argument and produces a result. This is the {@code Throwable}-throwing
 * specialization of {@link java.util.function.Function}.
 * 
 * <p>This is a {@linkplain FunctionalInterface functional interface} whose functional method is {@link #apply(Object)}.
 * 
 * <p>JavaDoc of this class is inspired by the JavaDoc of the {@code Function}-interface.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/05/26
 * 
 * @param   <T> the type parameter of the input argument
 * @param   <R> the type parameter of the result
 * @param   <X> the type parameter of the throwable
 * 
 * @since   1.8
 */
@FunctionalInterface
public interface FunctionThrowing<T, R, X extends Throwable> {
    
    /**
     * Applies this function to the given argument.
     * 
     * @param  value the function argument
     * @return the function result
     * @throws X if a throwable of the generic type is thrown
     */
    R apply(final T t) throws X;
    
    /**
     * Returns a composed {@code FunctionThrowing} that first applies this function to its input, and then applies the
     * after function to the result. If evaluation either function throws an exception, it is relayed to the caller of
     * the composed operation.
     * 
     * @param  <V> the type of output of the {@code after} function, and of the composed function
     * @param  after the function to apply after this function is applied
     * @return a composed {@code FunctionThrowing} that first applies this function and then applies the {@code after}
     *         function
     * @throws NullPointerException if {@code before} is {@code null}
     */
    default <V> FunctionThrowing<T, V, X> andThen(final Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        
        return (t) -> after.apply(apply(t));
    }
    
    /**
     * Returns a composed {@code FunctionThrowing} that first applies this function to its input, and then applies the
     * after function to the result. If evaluation either function throws an exception, it is relayed to the caller of
     * the composed operation.
     * 
     * @param  <V> the type of output of the {@code after} function, and of the composed function
     * @param  after the function to apply after this function is applied
     * @return a composed {@code FunctionThrowing} that first applies this function and then applies the {@code after}
     *         function
     * @throws NullPointerException if {@code before} is {@code null}
     */
    default <V> FunctionThrowing<T, V, X> andThen(final FunctionThrowing<? super R, ? extends V, ? extends X> after) {
        Objects.requireNonNull(after);
        
        return (t) -> after.apply(apply(t));
    }
    
    /**
     * Returns a composed {@code FunctionThrowing} that first applies the {@code before} function to its input, and then
     * applies this function to the result. If evaluation either function throws an exception, it is relayed to the
     * caller of the composed operation.
     * 
     * @param  <V> the type of input to the {@code before} function, and of the composed function
     * @param  before the function to apply before this function is applied
     * @return a composed {@code FunctionThrowing} that first applies the {@code before} function and then applies this
     *         function
     * @throws NullPointerException if {@code before} is {@code null}
     */
    default <V> FunctionThrowing<V, R, X> compose(final Function<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        
        return (v) -> apply(before.apply(v));
    }
    
    /**
     * Returns a composed {@code FunctionThrowing} that first applies the {@code before} function to its input, and then
     * applies this function to the result. If evaluation either function throws an exception, it is relayed to the
     * caller of the composed operation.
     * 
     * @param  <V> the type of input to the {@code before} function, and of the composed function
     * @param  before the function to apply before this function is applied
     * @return a composed {@code FunctionThrowing} that first applies the {@code before} function and then applies this
     *         function
     * @throws NullPointerException if {@code before} is {@code null}
     */
    default <V> FunctionThrowing<V, R, X> compose(final FunctionThrowing<? super V, ? extends T, ? extends X> before) {
        Objects.requireNonNull(before);
        
        return (v) -> apply(before.apply(v));
    }
}
