package edu.kit.informatik._intern.util.function;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.LongConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import edu.kit.informatik._intern.util.ObjectUtil;

/**
 * This class consists exclusively of static methods that operate on or return {@linkplain FunctionalInterface
 * functional interfaces} and the {@linkplain #sneakyThrow(Exception)} method.
 * 
 * @author  Tobias Bachert
 * @version 1.07, 2016/03/26
 */
public final class FIUtil {
    
    // Suppressed default constructor
    private FIUtil() { }
    
    /**
     * Returns a composed {@code Consumer} that first applies a {@code Function} and then performs a {@code Consumer}
     * on the result of the function.
     * 
     * <p>The returned {@code Consumer} is equivalent to:
     * <blockquote><pre>
     * (t) -> c.accept(f.apply(t));</pre>
     * </blockquote>
     * 
     * @param  <T> type parameter of the argument
     * @param  <R> type parameter of the result
     * @param  f the function
     * @param  c the consumer
     * @return a composed {@code Consumer} that first applies a {@code Function} and then performs a {@code Consumer}
     *         on the result of the function
     */
    public static <T, R> Consumer<T> applyAndAccept(final Function<? super T, ? extends R> f,
            final Consumer<? super R> c) {
        ObjectUtil.requireNonNull(f, c);
        
        return (t) -> c.accept(f.apply(t));
    }
    
    /**
     * Returns a composed {@code IntConsumer} that first applies an {@code IntFunction} and then performs a {@code
     * Consumer} on the result of the function.
     * 
     * <p>The returned {@code IntConsumer} is equivalent to:
     * <blockquote><pre>
     * (i) -> c.accept(f.apply(i));</pre>
     * </blockquote>
     * 
     * @param  <R> type parameter of the result
     * @param  f the function
     * @param  c the consumer
     * @return a composed {@code IntConsumer} that first applies an {@code IntFunction} and then performs a {@code
     *         Consumer} on the result of the function
     */
    public static <R> IntConsumer applyAndAccept(final IntFunction<R> f, final Consumer<? super R> c) {
        ObjectUtil.requireNonNull(f, c);
        
        return (i) -> c.accept(f.apply(i));
    }
    
    /**
     * Returns a composed {@code Consumer} that first applies a {@code ToIntFunction} and then performs an {@code
     * IntConsumer} on the result of the  function.
     * 
     * <p>The returned {@code Consumer} is equivalent to:
     * <blockquote><pre>
     * (t) -> c.accept(f.applyAsInt(t));</pre>
     * </blockquote>
     * 
     * @param  <T> type parameter of the argument
     * @param  f the function
     * @param  c the consumer
     * @return a composed {@code Consumer} that first applies a {@code ToIntFunction} and then performs an {@code
     *         IntConsumer} on the result of the  function
     */
    public static <T> Consumer<T> applyAndAccept(final ToIntFunction<T> f, final IntConsumer c) {
        ObjectUtil.requireNonNull(f, c);
        
        return (t) -> c.accept(f.applyAsInt(t));
    }
    
    /**
     * Returns a composed {@code Predicate} that first applies a {@code Function} and then evaluates a {@code Predicate}
     * on the result of the function.
     * 
     * <p>The returned {@code Predicate} is equivalent to:
     * <blockquote><pre>
     * (t) -> p.test(f.apply(t));</pre>
     * </blockquote>
     * 
     * @param  <T> type parameter of the argument
     * @param  <R> type parameter of the result
     * @param  f the function
     * @param  p the predicate
     * @return a composed {@code Predicate} that first applies a {@code Function} and then evaluates a {@code Predicate}
     *         on the result of the function
     */
    public static <T, R> Predicate<T> applyAndTest(final Function<? super T, ? extends R> f,
            final Predicate<? super R> p) {
        ObjectUtil.requireNonNull(f, p);
        
        return (t) -> p.test(f.apply(t));
    }
    
    /**
     * Returns a composed {@code IntPredicate} that first applies an {@code IntFunction} and then evaluates a {@code
     * Predicate} on the result of the function.
     * 
     * <p>The returned {@code IntPredicate} is equivalent to:
     * <blockquote><pre>
     * (i) -> p.test(f.apply(i));</pre>
     * </blockquote>
     * 
     * @param  <R> type parameter of the result
     * @param  f the function
     * @param  p the predicate
     * @return a composed {@code IntPredicate} that first applies an {@code IntFunction} and then evaluates a {@code
     *         Predicate} on the result of the function
     */
    public static <R> IntPredicate applyAndTest(final IntFunction<R> f, final Predicate<? super R> p) {
        ObjectUtil.requireNonNull(f, p);
        
        return (i) -> p.test(f.apply(i));
    }
    
    /**
     * Returns a composed {@code Predicate} that first applies a {@code ToIntFunction} and then evaluates an {@code
     * IntPredicate} on the result of the function.
     * 
     * <p>The returned {@code Predicate} is equivalent to:
     * <blockquote><pre>
     * (t) -> p.test(f.applyAsInt(t));</pre>
     * </blockquote>
     * 
     * @param  <T> type parameter of the argument
     * @param  f the function
     * @param  p the predicate
     * @return a composed {@code Predicate} that first applies a {@code ToIntFunction} and then evaluates an {@code
     *         IntPredicate} on the result of the function
     */
    public static <T> Predicate<T> applyAndTest(final ToIntFunction<T> f, final IntPredicate p) {
        ObjectUtil.requireNonNull(f, p);
        
        return (t) -> p.test(f.applyAsInt(t));
    }
    
    /**
     * Converts the {@code ActionThrowing} to an {@code Action}.<br>
     * The returned {@code Action} throws exceptions as if the operation of {@code action} is executed.
     * 
     * <p>This method allows to write for the usage of an exception-throwing method in a lambda-expression:
     * <blockquote><pre>
     * void doSomething() throws Foobar;
     * void foo(Action a);</pre>
     * </blockquote>
     * the following expressions:
     * <blockquote><pre>
     * void someMethod() throws Foobar {
     *     Bar.foo(hidden(Bar::doSomething));
     * }
     * 
     * void someOtherMethod() throws Foobar {
     *     Action a = hidden(Bar::doSomething));
     *     Bar.foo(a);
     * }</pre>
     * </blockquote>
     * instead of wrapping the exception as {@code RuntimeException} within the lambda-expression and catching the
     * wrapper-exception instead of the real exception on every call:
     * <blockquote><pre>
     * void someMethod() throws Foobar {
     *     try {
     *         Bar.foo(() -> {
     *             try {
     *                 Bar.doSomething();
     *             } catch (Foobar e) {
     *                 throw new FoobarRuntimeWrapper(e);
     *             }
     *         });
     *     } catch (FoobarRuntimeWrapper e) {
     *         throw (Foobar) e.getCause();
     *     }
     * }
     * 
     * void someOtherMethod() throws Foobar {
     *     Action a = () -> {
     *         try {
     *             Bar.doSomething();
     *         } catch (Foobar e) {
     *             throw new FoobarRuntimeWrapper(e);
     *         }
     *     };
     *     try {
     *         Bar.foo(a);
     *     } catch (FoobarRuntimeWrapper e) {
     *         throw (Foobar) e.getCause();
     *     }
     * }</pre>
     * </blockquote>
     * 
     * @param  action the {@code ActionThrowing} to convert
     * @return the {@code ActionThrowing} converted to an {@code Action}
     */
    public static Action hidden(final ActionThrowing<? extends Exception> action) {
        ObjectUtil.requireNonNull(action);
        
        return () -> {
            try {
                action.perform();
            } catch (final Exception e) {
                throw FIUtil.sneakyThrow(e);
            }
        };
    }
    
    /**
     * Converts the {@code ConsumerThrowing} to a {@code Consumer}.<br>
     * The returned {@code Consumer} throws exceptions as if the operation of {@code consumer} is executed.
     * 
     * <p>This method allows to write for the usage of an exception-throwing method in a lambda-expression:
     * <blockquote><pre>
     * void doSomething(T t) throws Foobar;
     * void foo(Consumer&lt;T&gt; c);</pre>
     * </blockquote>
     * the following expressions:
     * <blockquote><pre>
     * void someMethod() throws Foobar {
     *     Bar.foo(hidden(Bar::doSomething));
     * }
     * 
     * void someOtherMethod() throws Foobar {
     *     Consumer&lt;T&gt; c = hidden(Bar::doSomething));
     *     Bar.foo(c);
     * }</pre>
     * </blockquote>
     * instead of wrapping the exception as {@code RuntimeException} within the lambda-expression and catching the
     * wrapper-exception instead of the real exception on every call:
     * <blockquote><pre>
     * void someMethod() throws Foobar {
     *     try {
     *         Bar.foo((t) -> {
     *             try {
     *                 Bar.doSomething(t);
     *             } catch (Foobar e) {
     *                 throw new FoobarRuntimeWrapper(e);
     *             }
     *         });
     *     } catch (FoobarRuntimeWrapper e) {
     *         throw (Foobar) e.getCause();
     *     }
     * }
     * 
     * void someOtherMethod() throws Foobar {
     *     Consumer&lt;T&gt; c = (t) -> {
     *         try {
     *             Bar.doSomething(t);
     *         } catch (Foobar e) {
     *             throw new FoobarRuntimeWrapper(e);
     *         }
     *     };
     *     try {
     *         Bar.foo(c);
     *     } catch (FoobarRuntimeWrapper e) {
     *         throw (Foobar) e.getCause();
     *     }
     * }</pre>
     * </blockquote>
     * 
     * @param  <T> the type of the input argument of the operation
     * @param  consumer the {@code ConsumerThrowing} to convert
     * @return the {@code ConsumerThrowing} converted to a {@code Consumer}
     */
    public static <T> Consumer<T> hidden(final ConsumerThrowing<? super T, ? extends Exception> consumer) {
        ObjectUtil.requireNonNull(consumer);
        
        return (t) -> {
            try {
                consumer.accept(t);
            } catch (final Exception e) {
                FIUtil.sneakyThrow(e);
            }
        };
    }
    
    /**
     * Converts the {@code IntSupplierThrowing} to an {@code IntSupplier}.<br>
     * The returned {@code IntSupplier} throws exceptions as if the result of {@code supplier} is obtained.
     * 
     * <p>This method allows to write for the usage of an exception-throwing method in a lambda-expression:
     * <blockquote><pre>
     * int getSomething() throws Foobar;
     * void foo(IntSupplier s);</pre>
     * </blockquote>
     * the following expressions:
     * <blockquote><pre>
     * void someMethod() throws Foobar {
     *     Bar.foo(hidden(Bar::getSomething));
     * }
     * 
     * void someOtherMethod() throws Foobar {
     *     Supplier&lt;R&gt; s = hidden(Bar::getSomething));
     *     Bar.foo(s);
     * }</pre>
     * </blockquote>
     * instead of wrapping the exception as {@code RuntimeException} within the lambda-expression and catching the
     * wrapper-exception instead of the real exception on every call:
     * <blockquote><pre>
     * void someMethod() throws Foobar {
     *     try {
     *         Bar.foo(() -> {
     *             try {
     *                 return Bar.getSomething();
     *             } catch (Foobar e) {
     *                 throw new FoobarRuntimeWrapper(e);
     *         });
     *     } catch (FoobarRuntimeWrapper e) {
     *         throw (Foobar) e.getCause();
     *     }
     * }
     * 
     * void someOtherMethod() throws Foobar {
     *     IntSupplier; s = () -> {
     *         try {
     *             return Bar.getSomething();
     *         } catch (Foobar e) {
     *             throw new FoobarRuntimeWrapper(e);
     *         }
     *     };
     *     try {
     *         Bar.foo(s);
     *     } catch (FoobarRuntimeWrapper e) {
     *         throw (Foobar) e.getCause();
     *     }
     * }</pre>
     * </blockquote>
     * 
     * @param  supplier the {@code IntSupplierThrowing} to convert
     * @return the {@code IntSupplierThrowing} converted to an {@code IntSupplier}
     */
    public static IntSupplier hidden(final IntSupplierThrowing<? extends Exception> supplier) {
        ObjectUtil.requireNonNull(supplier);
        
        return () -> {
            try {
                return supplier.getAsInt();
            } catch (final Exception e) {
                throw FIUtil.sneakyThrow(e);
            }
        };
    }
    
    /**
     * Converts the {@code SupplierThrowing} to a {@code Supplier}.<br>
     * The returned {@code Supplier} throws exceptions as if the result of {@code supplier} is obtained.
     * 
     * <p>This method allows to write for the usage of an exception-throwing method in a lambda-expression:
     * <blockquote><pre>
     * R getSomething() throws Foobar;
     * void foo(Supplier&lt;R&gt; s);</pre>
     * </blockquote>
     * the following expressions:
     * <blockquote><pre>
     * void someMethod() throws Foobar {
     *     Bar.foo(hidden(Bar::getSomething));
     * }
     * 
     * void someOtherMethod() throws Foobar {
     *     Supplier&lt;R&gt; s = hidden(Bar::getSomething));
     *     Bar.foo(s);
     * }</pre>
     * </blockquote>
     * instead of wrapping the exception as {@code RuntimeException} within the lambda-expression and catching the
     * wrapper-exception instead of the real exception on every call:
     * <blockquote><pre>
     * void someMethod() throws Foobar {
     *     try {
     *         Bar.foo(() -> {
     *             try {
     *                 return Bar.getSomething();
     *             } catch (Foobar e) {
     *                 throw new FoobarRuntimeWrapper(e);
     *         });
     *     } catch (FoobarRuntimeWrapper e) {
     *         throw (Foobar) e.getCause();
     *     }
     * }
     * 
     * void someOtherMethod() throws Foobar {
     *     Supplier&lt;R&gt; s = () -> {
     *         try {
     *             return Bar.getSomething();
     *         } catch (Foobar e) {
     *             throw new FoobarRuntimeWrapper(e);
     *         }
     *     };
     *     try {
     *         Bar.foo(s);
     *     } catch (FoobarRuntimeWrapper e) {
     *         throw (Foobar) e.getCause();
     *     }
     * }</pre>
     * </blockquote>
     * 
     * @param  <R> the type parameter of the result
     * @param  supplier the {@code SupplierThrowing} to convert
     * @return the {@code SupplierThrowing} converted to a {@code Supplier}
     */
    public static <R> Supplier<R> hidden(final SupplierThrowing<? extends R, ? extends Exception> supplier) {
        ObjectUtil.requireNonNull(supplier);
        
        return () -> {
            try {
                return supplier.get();
            } catch (final Exception e) {
                throw FIUtil.sneakyThrow(e);
            }
        };
    }
    
    /**
     * Converts the {@code ActionThrowing} to an {@code Action} using the {@link #hidden(ActionThrowing)} method.<br>
     * The returned {@code Action} throws exceptions as if the operation of {@code action} is performed.
     * 
     * <p>This method allows the usage in a try-catch-block.
     * 
     * @param  <X> the type parameter of the {@code Exception}
     * @param  action the {@code ActionThrowing} to convert
     * @return the {@code ActionThrowing} converted to an {@code Action}
     * @throws X ensures that the calling method has to handle the {@code Exception}, allows usage in a try-catch block
     */
    public static <X extends Exception> Action hiddenX(final ActionThrowing<? extends Exception> action) throws X {
        return hidden(action);
    }
    
    /**
     * Converts the {@code ConsumerThrowing} to a {@code Consumer} using the {@link #hidden(ConsumerThrowing)}
     * method.<br>
     * The returned {@code Consumer} throws exceptions as if the operation of {@code consumer} is executed.
     * 
     * <p>This method allows the usage in a try-catch-block.
     * 
     * @param  <T> the type of the input argument of the operation
     * @param  <X> the type parameter of the {@code Exception}
     * @param  consumer the {@code ConsumerThrowing} to convert
     * @return the {@code ConsumerThrowing} converted to a {@code Consumer}
     * @throws X ensures that the calling method has to handle the {@code Exception}, allows usage in a try-catch block
     */
    public static <T, X extends Exception> Consumer<T> hiddenX(final ConsumerThrowing<? super T, ? extends X> consumer)
            throws X {
        return hidden(consumer);
    }
    
    /**
     * Converts the {@code IntSupplierThrowing} to an {@code IntSupplier} using the {@link #hidden(IntSupplierThrowing)}
     * method.<br>
     * The returned {@code IntSupplier} throws exceptions as if the result of {@code supplier} is obtained.
     * 
     * <p>This method allows the usage in a try-catch-block.
     * 
     * @param  <X> the type parameter of the {@code Exception}
     * @param  supplier the {@code IntSupplierThrowing} to convert
     * @return the {@code IntSupplierThrowing} converted to an {@code IntSupplier}
     * @throws X ensures that the calling method has to handle the {@code Exception}, allows usage in a try-catch block
     */
    public static <X extends Exception> IntSupplier hiddenX(final IntSupplierThrowing<? extends X> supplier) throws X {
        return hidden(supplier);
    }
    
    /**
     * Converts the {@code SupplierThrowing} to a {@code Supplier} using the {@link #hidden(SupplierThrowing)}
     * method.<br>
     * The returned {@code Supplier} throws exceptions as if the result of {@code supplier} is obtained.
     * 
     * <p>This method allows the usage in a try-catch-block.
     * 
     * @param  <R> the type parameter of the result
     * @param  <X> the type parameter of the {@code Exception}
     * @param  supplier the {@code SupplierThrowing} to convert
     * @return the {@code SupplierThrowing} converted to a {@code Supplier}
     * @throws X ensures that the calling method has to handle the {@code Exception}, allows usage in a try-catch block
     */
    public static <R, X extends Exception> Supplier<R> hiddenX(final SupplierThrowing<? extends R,
            ? extends X> supplier) throws X {
        return hidden(supplier);
    }
    
    /**
     * Returns a {@code Consumer}. Consecutive {@link Consumer#andThen(Consumer)} calls are reflected to the returned
     * {@code Consumer}.
     * 
     * @param  <T> type parameter
     * @return a {@code Consumer}
     */
    public static <T> Consumer<T> reflectiveConsumer() {
        return new ReflectingConsumer<>();
    }
    
    /**
     * Returns a {@code Consumer}. Consecutive {@link Consumer#andThen(Consumer)} calls are reflected to the returned
     * {@code Consumer}.
     * 
     * <p>The result of this method is equivalent to:
     * <blockquote><pre>
     * {@link #reflectiveConsumer()}.andThen(consumer);</pre>
     * </blockquote>
     * 
     * @param  <T> type parameter
     * @param  consumer a {@code Consumer} to convert
     * @return a {@code Consumer}
     */
    public static <T> Consumer<T> reflectiveConsumer(final Consumer<? super T> consumer) {
        return FIUtil.<T>reflectiveConsumer().andThen(consumer);
    }
    
    /**
     * Returns a {@code DoubleConsumer}. Consecutive {@link DoubleConsumer#andThen(DoubleConsumer)} calls are reflected
     * to the returned {@code DoubleConsumer}.
     * 
     * @return a {@code DoubleConsumer}
     */
    public static DoubleConsumer reflectiveDoubleConsumer() {
        return new ReflectingDoubleConsumer();
    }
    
    /**
     * Returns a {@code DoubleConsumer}. Consecutive {@link DoubleConsumer#andThen(DoubleConsumer)} calls are reflected
     * to the returned {@code DoubleConsumer}.
     * 
     * <p>The result of this method is equivalent to:
     * <blockquote><pre>
     * {@link #reflectiveDoubleConsumer()}.andThen(consumer);</pre>
     * </blockquote>
     * 
     * @param  consumer a {@code DoubleConsumer} to convert
     * @return a {@code DoubleConsumer}
     */
    public static DoubleConsumer reflectiveDoubleConsumer(final DoubleConsumer consumer) {
        return reflectiveDoubleConsumer().andThen(consumer);
    }
    
    /**
     * Returns an {@code IntConsumer}. Consecutive {@link IntConsumer#andThen(IntConsumer)} calls are reflected to the
     * returned {@code IntConsumer}.
     * 
     * @return an {@code IntConsumer}
     */
    public static IntConsumer reflectiveIntConsumer() {
        return new ReflectingIntConsumer();
    }
    
    /**
     * Returns an {@code IntConsumer}. Consecutive {@link IntConsumer#andThen(IntConsumer)} calls are reflected to the
     * returned {@code IntConsumer}.
     * 
     * <p>The result of this method is equivalent to:
     * <blockquote><pre>
     * {@link #reflectiveIntConsumer()}.andThen(consumer);</pre>
     * </blockquote>
     * 
     * @param  consumer a {@code IntConsumer} to convert
     * @return an {@code IntConsumer}
     */
    public static IntConsumer reflectiveIntConsumer(final IntConsumer consumer) {
        return reflectiveIntConsumer().andThen(consumer);
    }
    
    /**
     * Returns a {@code LongConsumer}. Consecutive {@link LongConsumer#andThen(LongConsumer)} calls are reflected to the
     * returned {@code LongConsumer}.
     * 
     * @return a {@code LongConsumer}
     */
    public static LongConsumer reflectiveLongConsumer() {
        return new ReflectingLongConsumer();
    }
    
    /**
     * Returns a {@code LongConsumer}. Consecutive {@link LongConsumer#andThen(LongConsumer)} calls are reflected to the
     * returned {@code LongConsumer}.
     * 
     * <p>The result of this method is equivalent to:
     * <blockquote><pre>
     * {@link #reflectiveLongConsumer()}.andThen(consumer);</pre>
     * </blockquote>
     * 
     * @param  consumer a {@code LongConsumer} to convert
     * @return a {@code LongConsumer}
     */
    public static LongConsumer reflectiveLongConsumer(final LongConsumer consumer) {
        return reflectiveLongConsumer().andThen(consumer);
    }
    
    /**
     * Hides the given (checked) {@code Exception} as {@code RuntimeException} without wrapping it.
     * 
     * <p>Exceptions thrown with this method should always be thrown using the following syntax:
     * <blockquote><pre>
     * throw sneakyThrow(e);</pre>
     * </blockquote>
     * Despite the fact that the {@code throw} tag is not required, it should always be added to indicate the motivation
     * of the method-call as well as to prevent the possible requirement of a following return-statement.</p>
     * 
     * @param  e the {@code Exception} to hide
     * @return {@code null}, dummy to allow usage with a {@code throw} tag
     * @deprecated Due to the fact that this is not intended by the language it should only be used if necessary.
     */
    @Deprecated
    public static RuntimeException sneakyThrow(final Throwable e) {
        /*
         * Credit for this method goes to Reinier Zwitserloot.
         * (http://www.mail-archive.com/javaposse@googlegroups.com/msg05984.html)
         */
        hideAndThrow(e);
        return null;
    }
    
    /**
     * Hides the given {@code Exception} through type erasure of the generic {@code X} to {@code RuntimeException} and
     * throws it.
     * 
     * <p>This method will be removed as soon as the official Java-API offers a better solution for the usage of checked
     * {@code Exception}s within lambda-expressions.
     * 
     * @param  <X> the type parameter of the {@code Exception}
     * @param  e the {@code Exception} to throw
     * @throws X the given {@code Exception}
     */
    private static <X extends Exception> void hideAndThrow(final Throwable e) throws X {
        @SuppressWarnings("unchecked")
        final X x = (X) e;
        throw x;
    }
    
    /**
     * Wrapper class used to build action chains.
     * 
     * @param <T> type parameter
     */
    static class ReflectingConsumer<T> implements Consumer<T> {
        
        private final Object lock = new Object();
        private volatile Consumer<T> cons = (t) -> { };
        
        @Override
        public void accept(final T t) {
            cons.accept(t);
        }
        
        @Override
        public ReflectingConsumer<T> andThen(final Consumer<? super T> after) {
            Objects.requireNonNull(after);
            
            synchronized (lock) {
                cons = cons.andThen(after);
                return this;
            }
        }
    }
    
    /** Wrapper class used to build action chains. */
    static class ReflectingDoubleConsumer implements DoubleConsumer {
        
        private final Object lock = new Object();
        private volatile DoubleConsumer cons = (t) -> { };
        
        @Override
        public void accept(final double value) {
            cons.accept(value);
        }
        
        @Override
        public ReflectingDoubleConsumer andThen(final DoubleConsumer after) {
            Objects.requireNonNull(after);
            
            synchronized (lock) {
                cons = cons.andThen(after);
                return this;
            }
        }
    }
    
    /** Wrapper class used to build action chains. */
    static class ReflectingIntConsumer implements IntConsumer {
        
        private final Object lock = new Object();
        private volatile IntConsumer cons = (t) -> { };
        
        @Override
        public void accept(final int value) {
            cons.accept(value);
        }
        
        @Override
        public ReflectingIntConsumer andThen(final IntConsumer after) {
            Objects.requireNonNull(after);
            
            synchronized (lock) {
                cons = cons.andThen(after);
                return this;
            }
        }
    }
    
    /** Wrapper class used to build action chains. */
    static class ReflectingLongConsumer implements LongConsumer {
        
        private final Object lock = new Object();
        private volatile LongConsumer cons = (t) -> { };
        
        @Override
        public void accept(final long value) {
            cons.accept(value);
        }
        
        @Override
        public ReflectingLongConsumer andThen(final LongConsumer after) {
            Objects.requireNonNull(after);
            
            synchronized (lock) {
                cons = cons.andThen(after);
                return this;
            }
        }
    }
}
