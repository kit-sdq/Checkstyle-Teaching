package edu.kit.informatik._intern.util.initialize;

import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

/**
 * Utility class regarding initialization.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/03/27
 */
public final class Initialization {
    
    // Suppressed default constructor
    private Initialization() { }
    
    /**
     * Returns a {@link BooleanSupplier} that returns on every subsequent call of the {@link
     * BooleanSupplier#getAsBoolean()} method the result of the first call.
     * 
     * <p>This method allows to use the following statement:
     * <blockquote><pre>
     * BooleanSupplier s = of(supplier);</pre>
     * </blockquote>
     * as a short cut for:
     * <blockquote><pre>
     * BooleanSupplier s = () -> {
     *     boolean r = supplier.getAsBoolean();
     *     s = () -> r;
     *     return r;
     * };</pre>
     * </blockquote>
     * 
     * <p>The returned {@code BooleanSupplier} is thread safe and will compute the value exactly once.
     * 
     * @param  supplier the {@code Supplier} to convert
     * @return a {@link BooleanSupplier} that returns the same result on every call of the {@link
     *         BooleanSupplier#getAsBoolean()} method
     */
    public static LazyBooleanInitializer of(final BooleanSupplier supplier) {
        Objects.requireNonNull(supplier);
        
        return new LazyBooleanInitializer() {
            
            private final Object lock = new Object();
            private BooleanSupplier sup = supplier;
            private boolean value;
            
            @Override
            public boolean getAsBoolean() {
                if (sup != null) {
                    synchronized (lock) {
                        if (sup != null) {
                            value = sup.getAsBoolean();
                            sup = null;
                        }
                    }
                }
                return value;
            }
            
            @Override
            public boolean isInitialized() {
                return sup == null;
            }
        };
    }
    
    /**
     * Returns a {@link DoubleSupplier} that returns on every subsequent call of the {@link
     * DoubleSupplier#getAsDouble()} method the result of the first call.
     * 
     * <p>This method allows to use the following statement:
     * <blockquote><pre>
     * DoubleSupplier s = of(supplier);</pre>
     * </blockquote>
     * as a short cut for:
     * <blockquote><pre>
     * DoubleSupplier s = () -> {
     *     double r = supplier.getAsDouble();
     *     s = () -> r;
     *     return r;
     * };</pre>
     * </blockquote>
     * 
     * <p>The returned {@code DoubleSupplier} is thread safe and will compute the value exactly once.
     * 
     * @param  supplier the {@code Supplier} to convert
     * @return a {@link DoubleSupplier} that returns the same result on every call of the {@link
     *         DoubleSupplier#getAsDouble()} method
     */
    public static LazyDoubleInitializer of(final DoubleSupplier supplier) {
        Objects.requireNonNull(supplier);
        
        return new LazyDoubleInitializer() {
            
            private final Object lock = new Object();
            private DoubleSupplier sup = supplier;
            private double value;
            
            @Override
            public double getAsDouble() {
                if (sup != null) {
                    synchronized (lock) {
                        if (sup != null) {
                            value = sup.getAsDouble();
                            sup = null;
                        }
                    }
                }
                return value;
            }
            
            @Override
            public boolean isInitialized() {
                return sup == null;
            }
        };
    }
    
    /**
     * Returns an {@link IntSupplier} that returns on every subsequent call of the {@link IntSupplier#getAsInt()} method
     * the result of the first call.
     * 
     * <p>This method allows to use the following statement:
     * <blockquote><pre>
     * IntSupplier s = of(supplier);</pre>
     * </blockquote>
     * as a short cut for:
     * <blockquote><pre>
     * IntSupplier s = () -> {
     *     int r = supplier.getAsInt();
     *     s = () -> r;
     *     return r;
     * };</pre>
     * </blockquote>
     * 
     * <p>The returned {@code IntSupplier} is thread safe and will compute the value exactly once.
     * 
     * @param  supplier the {@code Supplier} to convert
     * @return an {@link IntSupplier} that returns the same result on every call of the {@link IntSupplier#getAsInt()}
     *         method
     */
    public static LazyIntInitializer of(final IntSupplier supplier) {
        Objects.requireNonNull(supplier);
        
        return new LazyIntInitializer() {
            
            private final Object lock = new Object();
            private IntSupplier sup = supplier;
            private int value;
            
            @Override
            public int getAsInt() {
                if (sup != null) {
                    synchronized (lock) {
                        if (sup != null) {
                            value = sup.getAsInt();
                            sup = null;
                        }
                    }
                }
                return value;
            }
            
            @Override
            public boolean isInitialized() {
                return sup == null;
            }
        };
    }
    
    /**
     * Returns a {@link LongSupplier} that returns on every subsequent call of the {@link LongSupplier#getAsLong()}
     * method the result of the first call.
     * 
     * <p>This method allows to use the following statement:
     * <blockquote><pre>
     * LongSupplier s = of(supplier);</pre>
     * </blockquote>
     * as a short cut for:
     * <blockquote><pre>
     * LongSupplier s = () -> {
     *     long r = supplier.getAsLong();
     *     s = () -> r;
     *     return r;
     * };</pre>
     * </blockquote>
     * 
     * <p>The returned {@code LongSupplier} is thread safe and will compute the value exactly once.
     * 
     * @param  supplier the {@code Supplier} to convert
     * @return a {@link LongSupplier} that returns the same result on every call of the {@link LongSupplier#getAsLong()}
     *         method
     */
    public static LazyLongInitializer of(final LongSupplier supplier) {
        Objects.requireNonNull(supplier);
        
        return new LazyLongInitializer() {
            
            private final Object lock = new Object();
            private LongSupplier sup = supplier;
            private long value;
            
            @Override
            public long getAsLong() {
                if (sup != null) {
                    synchronized (lock) {
                        if (sup != null) {
                            value = sup.getAsLong();
                            sup = null;
                        }
                    }
                }
                return value;
            }
            
            @Override
            public boolean isInitialized() {
                return sup == null;
            }
        };
    }
    
    /**
     * Returns a {@link Supplier} that returns on every subsequent call of the {@link Supplier#get()} method the result
     * of the first call.
     * 
     * <p>This method allows to use the following statement:
     * <blockquote><pre>
     * Supplier&lt;R&gt; s = of(supplier);</pre>
     * </blockquote>
     * as a short cut for:
     * <blockquote><pre>
     * Supplier&lt;R&gt; s = () -> {
     *     R r = supplier.get();
     *     s = () -> r;
     *     return r;
     * };</pre>
     * </blockquote>
     * 
     * <p>The returned {@code Supplier} is thread safe and will compute the value exactly once.
     * 
     * @param  <R> type parameter of result
     * @param  supplier the {@code Supplier} to convert
     * @return a {@link Supplier} that returns the same result on every call of the {@link Supplier#get()} method
     */
    public static <R> LazyObjectInitializer<R> of(final Supplier<? extends R> supplier) {
        Objects.requireNonNull(supplier);
        
        return new LazyObjectInitializer<R>() {
            
            private final Object lock = new Object();
            private Supplier<? extends R> sup = supplier;
            private R r;
            
            @Override
            public R get() {
                if (sup != null) {
                    synchronized (lock) {
                        if (sup != null) {
                            r = sup.get();
                            sup = null;
                        }
                    }
                }
                return r;
            }
            
            @Override
            public boolean isInitialized() {
                return sup == null;
            }
        };
    }
}
