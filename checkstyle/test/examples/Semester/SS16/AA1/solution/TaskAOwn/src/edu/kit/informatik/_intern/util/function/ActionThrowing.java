package edu.kit.informatik._intern.util.function;

/**
 * Represents an operation that accepts no input arguments and returns no result. This is the {@code Exception}-throwing
 * specialization of {@link Action}. Unlike most other functional interfaces, {@code Action} is expected to operate via
 * side-effects.
 * 
 * <p>This is a {@linkplain FunctionalInterface functional interface} whose functional method is {@link #perform()}.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/04/02
 * 
 * @param   <X> the type parameter of the exception
 * 
 * @since   1.8
 */
@FunctionalInterface
public interface ActionThrowing<X extends Exception> {
    
    /**
     * Performs this operation.
     * 
     * @throws X if an exception of the generic type occurs
     */
    void perform() throws X;
}
