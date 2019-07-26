package edu.kit.informatik._intern.util.function;

import java.util.Objects;

/**
 * Represents an operation that accepts one {@code long}-valued and one {@code int}-valued input and returns no result.
 * This is the two-arity {@code long}-{@code int}-consuming primitive type specialization of {@link
 * java.util.function.Consumer}. Unlike most other functional interfaces, {@code LongIntConsumer} is expected to operate
 * via side-effects.
 * 
 * <p>This is a {@linkplain FunctionalInterface functional interface} whose functional method is {@link
 * #accept(long, int)}.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/03/21
 * 
 * @since   1.8
 */
@FunctionalInterface
public interface LongIntConsumer {
    
    /**
     * Performs this operation on the given arguments.
     * 
     * @param t the first input argument
     * @param u the second input argument
     */
    void accept(final long t, final int u);
    
    /**
     * Returns a composed {@code LongIntConsumer} that performs, in sequence, this operation followed by the {@code
     * after} operation. If performing either operation throws an exception, it is relayed to the caller of the composed
     * operation. If performing this operation throws an exception, the {@code after} operation will not be performed.
     * 
     * @param  after the operation to perform after this operation
     * @return a composed {@code LongIntConsumer} that performs in sequence this operation followed by the {@code after}
     *         operation.
     */
    default LongIntConsumer andThen(final LongIntConsumer after) {
        Objects.requireNonNull(after);
        
        return (t, u) -> {
            accept(t, u);
            after.accept(t, u);
        };
    }
}
