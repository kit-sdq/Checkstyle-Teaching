package edu.kit.informatik;

/**
 * Interface used for tests.
 * 
 * @author  Tobias Bachert
 * @version 1.01, 2016/04/17
 */
interface TestCase {
    
    /**
     * Processes {@code test} for each of the given values.
     * 
     * @param test a {@code Consumer}
     * @param first the first value
     * @param values the remaining values
     */
    @SafeVarargs static <V> void forEach(final Consumer<? super V> test, final V first, final V... values) {
        test.accept(first);
        for (final V value : values)
            test.accept(value);
    }
    
    /**
     * Processes this {@code TestCase}.
     * 
     * @param consumer a {@code String}-{@code Consumer}
     */
    void process(final Consumer<? super String> consumer);
    
    /** Functional Interface, see {@link java.util.function.Consumer}. */
    @FunctionalInterface interface Consumer<T> {
        void accept(final T t);
    }
}
