package edu.kit.informatik._intern.util.function;

import java.util.Objects;

/**
 * Represents an operation that accepts three input arguments and returns no result. This is the three-arity
 * specialization of {@link java.util.function.Consumer}. Unlike most other functional interfaces, {@code TriConsumer}
 * is expected to operate via side-effects.
 * 
 * <p>This is a {@linkplain FunctionalInterface functional interface} whose functional method is {@link #accept(Object,
 * Object, Object)}.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/02/27
 * 
 * @param   <T> the type of the first argument of the operation
 * @param   <U> the type of the second argument of the operation
 * @param   <V> the type of the third argument of the operation
 * 
 * @since   1.8
 */
@FunctionalInterface
public interface TriConsumer<T, U, V> {
    
    /**
     * Performs this operation on the given arguments.
     * 
     * @param t the first input argument
     * @param u the second input argument
     * @param v the third input argument
     */
    void accept(final T t, final U u, final V v);
    
    /**
     * Returns a composed {@code TriConsumer} that performs, in sequence, this operation followed by the {@code after}
     * operation. If performing either operation throws an exception, it is relayed to the caller of the composed
     * operation. If performing this operation throws an exception, the {@code after} operation will not be performed.
     * 
     * @param  after the operation to perform after this operation
     * @return a composed {@code TriConsumer} that performs in sequence this operation followed by the {@code after}
     *         operation.
     */
    default TriConsumer<T, U, V> andThen(final TriConsumer<? super T, ? super U, ? super V> after) {
        Objects.requireNonNull(after);
        
        return (t, u, v) -> {
            accept(t, u, v);
            after.accept(t, u, v);
        };
    }
}
