package edu.kit.informatik._intern.util.initialize;

import java.util.function.LongSupplier;

/**
 * Represents a lazy initializer for a {@code long}-valued attribute. This is the {@code long}-valued primitive
 * specialization of {@link LazyObjectInitializer}.
 * 
 * <p>The {@link #getAsLong()} method returns a distinct result each time the supplier is invoked.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/03/27
 * 
 * @see     LongSupplier
 * @see     LazyInitializer
 */
public interface LazyLongInitializer extends LongSupplier, LazyInitializer {
    
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
    default InitializerCompared compare(final LazyLongInitializer other) {
        return !isInitialized() || !other.isInitialized() ? InitializerCompared.UNKOWN
                : getAsLong() == other.getAsLong()        ? InitializerCompared.EQUALS
                                                          : InitializerCompared.NON_EQUALS;
    }
    
    /**
     * Returns the value of the lazy initializer.
     */
    @Override
    long getAsLong();
}
