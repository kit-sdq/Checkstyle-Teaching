package edu.kit.informatik._intern.util.memoizer;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.function.Function;

/**
 * Memoizer for objects.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/08/12
 * 
 * @param   <T> type parameter of the input argument
 * @param   <R> type parameter of the result
 */
public final class Memoizer<T, R> implements Computable<T, R>, Function<T, R> {
    
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    
    private final Map<T, Future<R>> cache;
    private final Computable<? super T, ? extends R> c;
    
    /**
     * Creates a new {@code Memoizer} with the provided computable as function and an initial capacity of
     * {@value #DEFAULT_INITIAL_CAPACITY}.
     * 
     * @param computing the computing function
     */
    public Memoizer(
            final Computable<? super T, ? extends R> computing) {
        ////
        this(computing, DEFAULT_INITIAL_CAPACITY);
    }
    
    /**
     * Creates a new {@code Memoizer} with the provided computable as function.
     * 
     * @param computing the computing function
     * @param initialCapacity the initial capacity
     */
    public Memoizer(
            final Computable<? super T, ? extends R> computing,
            final int initialCapacity) {
        ////
        this(computing, initialCapacity, DEFAULT_LOAD_FACTOR);
    }
    
    /**
     * Creates a new {@code Memoizer} with the provided computable as function.
     * 
     * @param computing the computing function
     * @param initialCapacity the initial capacity
     * @param loadFactor the load factor
     */
    public Memoizer(
            final Computable<? super T, ? extends R> computing,
            final int initialCapacity,
            final float loadFactor) {
        ////
        cache = new ConcurrentHashMap<>(initialCapacity, loadFactor);
        c = Objects.requireNonNull(computing);
    }
    
    @Override
    public R compute(
            final T value) throws InterruptedException {
        ////
        for (;;) {
            Future<R> f = cache.get(value);
            if (f == null) {
                final FutureTask<R> ft = new FutureTask<>(() -> c.compute(value));
                f = cache.putIfAbsent(value, ft);
                if (f == null) {
                    f = ft;
                    ft.run();
                }
            }
            try {
                return f.get();
            } catch (final CancellationException e) {
                cache.remove(value, f);
            } catch (final ExecutionException e) {
                throw new IllegalStateException("Exception during compution", e.getCause());
            }
        }
    }
    
    /**
     * Applies this function to the given argument.
     * 
     * <p>This method behaves exactly as if the {@link #compute(Object)} method got invoked except that this wraps any
     * thrown {@link InterruptedException} as an {@code IllegalStateException}.
     * 
     * @param  value the function argument
     * @return the function result
     * @throws IllegalStateException if the thread gets interrupted while obtaining the result
     */
    @Override
    public R apply(
            final T value) {
        ////
        try {
            return compute(value);
        } catch (final InterruptedException e) {
            throw new IllegalStateException("Thread interrupted while obtaining result", e);
        }
    }
}
