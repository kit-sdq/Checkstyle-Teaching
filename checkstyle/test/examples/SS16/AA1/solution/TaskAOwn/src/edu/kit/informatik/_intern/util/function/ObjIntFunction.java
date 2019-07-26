package edu.kit.informatik._intern.util.function;

import java.util.Objects;
import java.util.function.Function;

/**
 * Represents a function that accepts one object-valued and one {@code int}-valued argument and produces a result. This
 * is the ({@code reference}, {@code int}) specialization of {@link Function}.
 * 
 * <p>This is a {@linkplain FunctionalInterface functional interface} whose functional method is {@link
 * #apply(Object, int)}.
 * 
 * @author  Tobias Bachert
 * @version 1.02, 2016/03/18
 * 
 * @param   <T> the type of the first argument to the function
 * @param   <R> the type of the result of the function
 * 
 * @since   1.8
 */
@FunctionalInterface
public interface ObjIntFunction<T, R> {
    
    /**
     * Applies this function to the given arguments.
     * 
     * @param  t the first function argument
     * @param  u the second function argument
     * @return the function result
     */
    R apply(final T t, final int u);
    
    /**
     * Returns a composed {@code ObjIntFunction} that first applies this function to its input, and then applies the
     * after function to the result. If evaluation either function throws an exception, it is relayed to the caller of
     * the composed operation.
     * 
     * @param  <Z> the type of output of the after function, and of the composed function
     * @param  after the function to apply after this function is applied
     * @return a composed {@code ObjIntFunction} that first applies this function and then applies the after function
     */
    default <Z> ObjIntFunction<T, Z> andThen(final Function<? super R, ? extends Z> after) {
        Objects.requireNonNull(after);
        
        return (t, u) -> after.apply(apply(t, u));
    }
}
