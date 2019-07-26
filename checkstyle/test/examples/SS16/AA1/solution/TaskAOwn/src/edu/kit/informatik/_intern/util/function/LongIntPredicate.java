package edu.kit.informatik._intern.util.function;

import java.util.Objects;

/**
 * Represents a predicate (boolean-valued function) of one {@code long}-valued and one {@code int}-valued argument. This
 * is the two-arity {@code long}-{@code int}-consuming primitive type specialization of {@link
 * java.util.function.Predicate}.
 * 
 * <p>This is a {@linkplain FunctionalInterface functional interface} whose functional method is {@link
 * #test(long, int)}.
 * 
 * @author  Tobias Bachert
 * @version 1.02, 2016/03/18
 * 
 * @since   1.8
 */
@FunctionalInterface
public interface LongIntPredicate {
    
    /**
     * Evaluates this predicate on the given argument.
     * 
     * @param  t the first input argument
     * @param  u the second input argument
     * @return {@code true} if the input arguments match the predicate, otherwise {@code false}
     */
    boolean test(final long t, final int u);
    
    /**
     * Returns a composed predicate that represents a short-circuiting logical AND of this predicate and another. When
     * evaluation the composed predicate, if this predicate is {@code false}, then the {@code other} predicate is not
     * evaluated.
     * 
     * <p>Any exceptions thrown during evaluation of either predicate are relayed to the caller; if evaluation of this
     * predicate throws an exception, the {@code other} predicate will not be evaluated.
     * 
     * @param  other a predicate that will be logically-ANDed with this predicate
     * @return a composed predicate that represents the short-circuiting logical AND of this predicate and the {@code
     *         other} predicate
     */
    default LongIntPredicate and(final LongIntPredicate other) {
        Objects.requireNonNull(other);
        
        return (t, u) -> test(t, u) && other.test(t, u);
    }
    
    /**
     * Returns a predicate that represents the logical negation of this predicate.
     * 
     * @return a predicate that represents the logical negation of this predicate
     */
    default LongIntPredicate negate() {
        return (t, u) -> !test(t, u);
    }
    
    /**
     * Returns a composed predicate that represents a short-circuiting logical OR of this predicate and another. When
     * evaluation the composed predicate, if this predicate is {@code true}, then the {@code other} predicate is not
     * evaluated.
     * 
     * <p>Any exceptions thrown during evaluation of either predicate are relayed to the caller; if evaluation of this
     * predicate throws an exception, the {@code other} predicate will not be evaluated.
     * 
     * @param  other a predicate that will be logically-ORed with this predicate
     * @return a composed predicate that represents the short-circuiting logical OR of this predicate and the {@code
     *         other} predicate
     */
    default LongIntPredicate or(final LongIntPredicate other) {
        Objects.requireNonNull(other);
        
        return (t, u) -> test(t, u) || other.test(t, u);
    }
}
