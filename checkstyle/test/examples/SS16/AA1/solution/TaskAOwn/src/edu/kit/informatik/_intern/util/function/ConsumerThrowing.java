package edu.kit.informatik._intern.util.function;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Represents an operation that accepts a single input argument and returns no result. This is the {@code
 * Exception}-throwing specialization of {@link Consumer}. Unlike most other functional interfaces,
 * {@code ConsumerThrowing} is expected to operate via side-effects.
 * 
 * <p>This is a {@linkplain FunctionalInterface functional interface} whose functional method is {@link
 * #accept(Object)}.
 * 
 * @author  Tobias Bachert
 * @version 1.03, 2016/03/04
 * 
 * @param   <T> the type of the input argument of the operation
 * @param   <X> the type parameter of the exception
 * 
 * @since   1.8
 */
@FunctionalInterface
public interface ConsumerThrowing<T, X extends Exception> {
    
    /**
     * Performs this operation on the given argument.
     * 
     * @param  t the input argument
     * @throws X if an exception of the generic type occurs
     */
    void accept(final T t) throws X;
    
    /**
     * Returns a composed {@code ConsumerThrowing} that performs, in sequence, this operation followed by the {@code
     * after} operation. If performing either operation throws an exception, it is relayed to the caller of the composed
     * operation. If performing this operation throws an exception, the {@code after} operation will not be performed.
     * 
     * @param  after the operation to perform after this operation
     * @return a composed {@code ConsumerThrowing} that performs in sequence this operation followed by the {@code
     *         after} operation.
     */
    default ConsumerThrowing<T, X> andThen(final ConsumerThrowing<? super T, ? extends X> after) {
        Objects.requireNonNull(after);
        
        return (t) -> {
            accept(t);
            after.accept(t);
        };
    }
    
    /**
     * Returns a composed {@code ConsumerThrowing} that performs, in sequence, this operation followed by the {@code
     * after} operation. If performing either operation throws an exception, it is relayed to the caller of the composed
     * operation. If performing this operation throws an exception, the {@code after} operation will not be performed.
     * 
     * @param  after the operation to perform after this operation
     * @return a composed {@code ConsumerThrowing} that performs in sequence this operation followed by the {@code
     *         after} operation.
     */
    default ConsumerThrowing<T, X> andThen(final Consumer<? super T> after) {
        Objects.requireNonNull(after);
        
        return (t) -> {
            accept(t);
            after.accept(t);
        };
    }
}
