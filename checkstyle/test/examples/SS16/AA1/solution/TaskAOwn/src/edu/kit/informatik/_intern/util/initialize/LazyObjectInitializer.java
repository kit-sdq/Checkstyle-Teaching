package edu.kit.informatik._intern.util.initialize;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Represents a lazy initializer for an object attribute.
 * 
 * <p>The {@link #get()} method returns a distinct result each time the supplier is invoked.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/03/27
 * 
 * @param   <R> type of the result
 * 
 * @see     DoubleSupplier
 * @see     LazyInitializer
 */
public interface LazyObjectInitializer<R> extends Supplier<R>, LazyInitializer {
    
    /**
     * Returns the comparison of this lazy initializer with {@code other}.
     * 
     * <h1></h1>
     * <ul>
     *  <li>{@link InitializerCompared#UNKOWN} if at least one initializer is not yet initialized
     *  <li>{@link InitializerCompared#EQUALS} if both lazy initializer are initialized and hold equal values
     *  <li>{@link InitializerCompared#NON_EQUALS} if both lazy initializer are initialized and hold non-equal values
     * </ul>
     * 
     * @param  other the other initializer
     * @return  the comparison of this lazy initializer with {@code other}
     */
    default InitializerCompared compare(final LazyObjectInitializer<?> other) {
        return !isInitialized() || !other.isInitialized() ? InitializerCompared.UNKOWN
                : Objects.equals(get(), other.get())      ? InitializerCompared.EQUALS
                                                          : InitializerCompared.NON_EQUALS;
    }
    
    /**
     * Returns the value of the lazy initializer.
     */
    @Override
    R get();
}
