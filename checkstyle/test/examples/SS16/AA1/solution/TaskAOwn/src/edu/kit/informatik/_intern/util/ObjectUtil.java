package edu.kit.informatik._intern.util;

import java.util.function.Supplier;

/**
 * This class contains general utility methods for objects.
 * 
 * @author  Tobias Bachert
 * @version 1.01, 2016/05/22
 */
public final class ObjectUtil {
    
    // Suppressed default constructor
    private ObjectUtil() { }
    
    /**
     * Returns the first non-{@code null} value, or {@code null} if all values are {@code null}.
     * 
     * @param  <S> type parameter of suppliers
     * @param  <R> type parameter of the result
     * @param  suppliers supplier of values
     * @return the first non-{@code null} value
     */
    @SafeVarargs
    public static <S extends Supplier<? extends R>, R> R coalesce(final S... suppliers) {
        requireNonNull(suppliers);
        
        if (suppliers.length == 0)
            return null;
        final int lastIndex = suppliers.length - 1;
        for (int i = 0; i < lastIndex; i++) {
            final R result = suppliers[i].get();
            if (result != null)
                return result;
        }
        return suppliers[lastIndex].get();
    }
    
    /**
     * Returns the first non-{@code null} value, or {@code null} if all values are {@code null}.
     * 
     * @param  <S> type parameter of suppliers
     * @param  <R> type parameter of the result
     * @param  a the first supplier
     * @param  b the second supplier
     * @return the first non-{@code null} value
     */
    public static <S extends Supplier<? extends R>, R> R coalesce(final S a, final S b) {
        requireNonNull(a, b);
        
        final R result = a.get();
        return result != null ? result : b.get();
    }
    
    /**
     * Returns the first non-{@code null} value, or {@code null} if all values are {@code null}.
     * 
     * @param  <S> type parameter of suppliers
     * @param  <R> type parameter of the result
     * @param  a the first supplier
     * @param  b the second supplier
     * @param  c the third supplier
     * @return the first non-{@code null} value
     */
    public static <S extends Supplier<? extends R>, R> R coalesce(final S a, final S b, final S c) {
        requireNonNull(a, b, c);
        
        final R result = coalesce(a, b);
        return result != null ? result : c.get();
    }
    
    /**
     * Returns the first non-{@code null} value, or {@code null} if all values are {@code null}.
     * 
     * @param  <S> type parameter of suppliers
     * @param  <R> type parameter of the result
     * @param  a the first supplier
     * @param  b the second supplier
     * @param  c the third supplier
     * @param  d the fourth supplier
     * @return the first non-{@code null} value
     */
    public static <S extends Supplier<? extends R>, R> R coalesce(final S a, final S b, final S c, final S d) {
        requireNonNull(a, b, c, d);
        
        final R result = coalesce(a, b, c);
        return result != null ? result : d.get();
    }
    
    /**
     * Returns the first non-{@code null} value, or {@code null} if all values are {@code null}.
     * 
     * @param  <S> type parameter of suppliers
     * @param  <R> type parameter of the result
     * @param  a the first supplier
     * @param  b the second supplier
     * @param  c the third supplier
     * @param  d the fourth supplier
     * @param  e the fifth supplier
     * @return the first non-{@code null} value
     */
    public static <S extends Supplier<? extends R>, R> R coalesce(final S a, final S b, final S c, final S d,
            final S e) {
        requireNonNull(a, b, c, d, e);
        
        final R result = coalesce(a, b, c, d);
        return result != null ? result : e.get();
    }
    
    /**
     * Checks that the specified object references are not {@code null}.
     * 
     * @param  <T> type parameter
     * @param  references the references
     * @return the provided references
     * @throws NullPointerException if at least one reference is {@code null}
     */
    @SafeVarargs
    public static <T> T[] requireNonNull(final T... references) {
        for (final T t : references)
            requireNonNull(t);
        return references;
    }
    
    /**
     * Checks that the specified object reference is not {@code null}.
     * 
     * @param  <T> type parameter
     * @param  a the reference
     * @return the reference
     * @throws NullPointerException if the reference is {@code null}
     * @see    java.util.Objects#requireNonNull(Object)
     */
    public static <T> T requireNonNull(final T a) {
        if (a == null)
            throw new NullPointerException();
        return a;
    }
    
    /**
     * Checks that the specified object references are not {@code null}.
     * 
     * @param  <T> type parameter
     * @param  a the first reference
     * @param  b the second reference
     * @throws NullPointerException if at least one reference is {@code null}
     */
    public static <T> void requireNonNull(final T a, final T b) {
        if (a == null || b == null)
            throw new NullPointerException();
    }
    
    /**
     * Checks that the specified object references are not {@code null}.
     * 
     * @param  <T> type parameter
     * @param  a the first reference
     * @param  b the second reference
     * @param  c the third reference
     * @throws NullPointerException if at least one reference is {@code null}
     */
    public static <T> void requireNonNull(final T a, final T b, final T c) {
        if (a == null || b == null || c == null)
            throw new NullPointerException();
    }
    
    /**
     * Checks that the specified object references are not {@code null}.
     * 
     * @param  <T> type parameter
     * @param  a the first reference
     * @param  b the second reference
     * @param  c the third reference
     * @param  d the fourth reference
     * @throws NullPointerException if at least one reference is {@code null}
     */
    public static <T> void requireNonNull(final T a, final T b, final T c, final T d) {
        if (a == null || b == null || c == null || d == null)
            throw new NullPointerException();
    }
    
    /**
     * Checks that the specified object references are not {@code null}.
     * 
     * @param  <T> type parameter
     * @param  a the first reference
     * @param  b the second reference
     * @param  c the third reference
     * @param  d the fourth reference
     * @param  e the fifth reference
     * @throws NullPointerException if at least one reference is {@code null}
     */
    public static <T> void requireNonNull(final T a, final T b, final T c, final T d, final T e) {
        if (a == null || b == null || c == null || d == null || e == null)
            throw new NullPointerException();
    }
}
