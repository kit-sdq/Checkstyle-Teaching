package edu.kit.informatik._intern.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoublePredicate;
import java.util.function.IntBinaryOperator;
import java.util.function.IntPredicate;
import java.util.function.LongBinaryOperator;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import edu.kit.informatik._intern.util.function.BooleanPredicate;
import edu.kit.informatik._intern.util.function.ByteBinaryOperator;
import edu.kit.informatik._intern.util.function.BytePredicate;
import edu.kit.informatik._intern.util.function.CharBinaryOperator;
import edu.kit.informatik._intern.util.function.CharPredicate;
import edu.kit.informatik._intern.util.function.FloatBinaryOperator;
import edu.kit.informatik._intern.util.function.FloatPredicate;
import edu.kit.informatik._intern.util.function.ShortPredicate;

/**
 * This class consists exclusively of static methods that operate on or return arrays.
 * 
 * <p>All methods of this class throw an {@code ArrayIndexOutOfBoundsException} if at least one provided index is out of
 * bounds.
 * 
 * @author  Tobias Bachert
 * @version 1.06, 2016/03/27
 * 
 * @see     java.util.Arrays
 * @since   1.8
 */
public final class ArrayUtil {
    
    // Suppressed default constructor
    private ArrayUtil() { }
    
    private static void checkIndex(final Object[] array, final int a, final int b) {
        if (a < 0) throw new ArrayIndexOutOfBoundsException(a);
        if (b >= array.length) throw new ArrayIndexOutOfBoundsException(b);
        if (a >= b) throw new IllegalArgumentException(a + " is not less than " + b);
    }
    
    /**
     * Returns the array-class belonging to the component-type class.
     * 
     * @param  <T> type parameter
     * @param  componentType the component type
     * @return the array-class of the component-type class
     */
    public static <T> Class<T[]> getArrayClass(final Class<T> componentType) {
        @SuppressWarnings("unchecked")
        final Class<T[]> result = (Class<T[]>) Array.newInstance(componentType, 0).getClass();
        return result;
    }
    
    private static <T> boolean ascending0(final T[] arr, final int a, final int b, final Comparator<? super T> c) {
        for (int i = a, m = b - 1; i < m;)
            if (c.compare(arr[i], arr[++i]) > 0)
                return false;
        return true;
    }
    
    /**
     * Returns whether the elements between {@code from} (inclusive) and {@code to} (exclusive) of the provided array
     * are in ascending order as defined by the provided comparator.
     * 
     * @param  <T> type parameter
     * @param  array the array to check
     * @param  from the start index
     * @param  to the end index
     * @param  comparator the comparator to use
     * @return {@code true} if the elements are in ascending order, {@code false} otherwise
     */
    public static <T> boolean ascending(final T[] array, final int from, final int to,
            final Comparator<? super T> comparator) {
        checkIndex(array, from, to);
        
        return ascending0(array, from, to, comparator);
    }
    
    /**
     * Returns whether the elements of the provided array are in ascending order as defined by the provided comparator.
     * 
     * @param  <T> type parameter
     * @param  array the array to check
     * @param  comparator the comparator to use
     * @return {@code true} if the elements are in ascending order, {@code false} otherwise
     * @see    #ascending(Object[], int, int, Comparator)
     */
    public static <T> boolean ascending(final T[] array, final Comparator<? super T> comparator) {
        return ascending0(array, 0, array.length, comparator);
    }
    
    /**
     * Returns whether the elements of the provided array are in ascending order as defined by the natural order.
     * 
     * @param  <T> type parameter
     * @param  array the array to check
     * @return {@code true} if the elements are in ascending order, {@code false} otherwise
     * @see    #ascending(Object[], Comparator)
     * @see    #ascending(Object[], int, int, Comparator)
     */
    public static <T extends Comparable<? super T>> boolean ascending(final T[] array) {
        return ascending0(array, 0, array.length, Comparator.naturalOrder());
    }
    
    /**
     * Returns the last index of an element in an array that matches a predicate.<br>
     * If no element matches the predicate, {@code -1} is returned.
     * 
     * @param  <T> type parameter
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return the last index of an element that matches the given predicate
     */
    public static <T> int lastIndexOf(final T[] array, final Predicate<? super T> p) {
        for (int i = array.length; --i >= 0;)
            if (p.test(array[i]))
                return i;
        return -1;
    }
    
    /**
     * Returns the last index of an element in an array that matches, together with any element of a second array, a
     * predicate.<br>
     * If no combination of elements matches the predicate, {@code -1} is returned.
     * 
     * @param  <T> type parameter
     * @param  <U> type parameter
     * @param  arrayA the first {@code Array}
     * @param  arrayB the second {@code Array}
     * @param  p the predicate
     * @return the last index of an element in {@code arrayA} that matches, together with any element of {@code arrayB},
     *         the given predicate
     * @see    #lastIndexOf(Object[], Predicate)
     */
    public static <T, U> int lastIndexOf(final T[] arrayA, final U[] arrayB,
            final BiPredicate<? super T, ? super U> p) {
        return arrayB.length == 0 ? -1 : lastIndexOf(arrayA, (t) -> anyMatch(arrayB, (u) -> p.test(t, u)));
    }
    
    /**
     * Returns the value of the indexed component in the specified array object. The value is automatically wrapped in
     * an object if it has a primitive type.
     * 
     * @param  array the array
     * @param  indices the indices
     * @return the value of the indexed component in the specified array
     * @throws NullPointerException if the specified object is {@code null}
     * @throws IllegalArgumentException if the specified object is not an array of dimension {@code indices.length - 1}
     * @throws ArrayIndexOutOfBoundsException if any of the specified {@code indices} is negative, greater than or equal
     *         to the length of the specific dimension of the specified array
     */
    public static Object get(final Object array, final int... indices) {
        return Array.get(getArray(array, indices), indices[indices.length - 1]);
    }
    
    /**
     * Returns the value of the indexed component in the specified array object, as a {@code boolean}.
     * 
     * @param  array the array
     * @param  indices the indices
     * @return the value of the indexed component in the specified array
     * @throws NullPointerException if the specified object is {@code null}
     * @throws IllegalArgumentException if the specified object is not an array of dimension {@code indices.length - 1},
     *         or if the indexed element cannot be converted to the return type by an identity or widening conversion
     * @throws ArrayIndexOutOfBoundsException if any of the specified {@code indices} is negative or greater than or
     *         equal to the length of the specific dimension of the specified array
     */
    public static boolean getBoolean(final Object array, final int... indices) {
        return Array.getBoolean(getArray(array, indices), indices[indices.length - 1]);
    }
    
    /**
     * Returns the value of the indexed component in the specified array object, as a {@code byte}.
     * 
     * @param  array the array
     * @param  indices the indices
     * @return the value of the indexed component in the specified array
     * @throws NullPointerException if the specified object is {@code null}
     * @throws IllegalArgumentException if the specified object is not an array of dimension {@code indices.length - 1},
     *         or if the indexed element cannot be converted to the return type by an identity or widening conversion
     * @throws ArrayIndexOutOfBoundsException if any of the specified {@code indices} is negative or greater than or
     *         equal to the length of the specific dimension of the specified array
     */
    public static byte getByte(final Object array, final int... indices) {
        return Array.getByte(getArray(array, indices), indices[indices.length - 1]);
    }
    
    /**
     * Returns the value of the indexed component in the specified array object, as a {@code char}.
     * 
     * @param  array the array
     * @param  indices the indices
     * @return the value of the indexed component in the specified array
     * @throws NullPointerException if the specified object is {@code null}
     * @throws IllegalArgumentException if the specified object is not an array of dimension {@code indices.length - 1},
     *         or if the indexed element cannot be converted to the return type by an identity or widening conversion
     * @throws ArrayIndexOutOfBoundsException if any of the specified {@code indices} is negative or greater than or
     *         equal to the length of the specific dimension of the specified array
     */
    public static char getChar(final Object array, final int... indices) {
        return Array.getChar(getArray(array, indices), indices[indices.length - 1]);
    }
    
    /**
     * Returns the value of the indexed component in the specified array object, as a {@code double}.
     * 
     * @param  array the array
     * @param  indices the indices
     * @return the value of the indexed component in the specified array
     * @throws NullPointerException if the specified object is {@code null}
     * @throws IllegalArgumentException if the specified object is not an array of dimension {@code indices.length - 1},
     *         or if the indexed element cannot be converted to the return type by an identity or widening conversion
     * @throws ArrayIndexOutOfBoundsException if any of the specified {@code indices} is negative or greater than or
     *         equal to the length of the specific dimension of the specified array
     */
    public static double getDouble(final Object array, final int... indices) {
        return Array.getDouble(getArray(array, indices), indices[indices.length - 1]);
    }
    
    /**
     * Returns the value of the indexed component in the specified array object, as a {@code float}.
     * 
     * @param  array the array
     * @param  indices the indices
     * @return the value of the indexed component in the specified array
     * @throws NullPointerException if the specified object is {@code null}
     * @throws IllegalArgumentException if the specified object is not an array of dimension {@code indices.length - 1},
     *         or if the indexed element cannot be converted to the return type by an identity or widening conversion
     * @throws ArrayIndexOutOfBoundsException if any of the specified {@code indices} is negative or greater than or
     *         equal to the length of the specific dimension of the specified array
     */
    public static float getFloat(final Object array, final int... indices) {
        return Array.getFloat(getArray(array, indices), indices[indices.length - 1]);
    }
    
    /**
     * Returns the value of the indexed component in the specified array object, as an {@code int}.
     * 
     * @param  array the array
     * @param  indices the indices
     * @return the value of the indexed component in the specified array
     * @throws NullPointerException if the specified object is {@code null}
     * @throws IllegalArgumentException if the specified object is not an array of dimension {@code indices.length - 1},
     *         or if the indexed element cannot be converted to the return type by an identity or widening conversion
     * @throws ArrayIndexOutOfBoundsException if any of the specified {@code indices} is negative or greater than or
     *         equal to the length of the specific dimension of the specified array
     */
    public static int getInt(final Object array, final int... indices) {
        return Array.getInt(getArray(array, indices), indices[indices.length - 1]);
    }
    
    /**
     * Returns the value of the indexed component in the specified array object, as a {@code long}.
     * 
     * @param  array the array
     * @param  indices the indices
     * @return the value of the indexed component in the specified array
     * @throws NullPointerException if the specified object is {@code null}
     * @throws IllegalArgumentException if the specified object is not an array of dimension {@code indices.length - 1},
     *         or if the indexed element cannot be converted to the return type by an identity or widening conversion
     * @throws ArrayIndexOutOfBoundsException if any of the specified {@code indices} is negative or greater than or
     *         equal to the length of the specific dimension of the specified array
     */
    public static long getLong(final Object array, final int... indices) {
        return Array.getLong(getArray(array, indices), indices[indices.length - 1]);
    }
    
    /**
     * Returns the value of the indexed component in the specified array object, as a {@code short}.
     * 
     * @param  array the array
     * @param  indices the indices
     * @return the value of the indexed component in the specified array
     * @throws NullPointerException if the specified object is {@code null}
     * @throws IllegalArgumentException if the specified object is not an array of dimension {@code indices.length - 1},
     *         or if the indexed element cannot be converted to the return type by an identity or widening conversion
     * @throws ArrayIndexOutOfBoundsException if any of the specified {@code indices} is negative or greater than or
     *         equal to the length of the specific dimension of the specified array
     */
    public static short getShort(final Object array, final int... indices) {
        return Array.getShort(getArray(array, indices), indices[indices.length - 1]);
    }
    
    /**
     * Returns whether an array contains a specific element.
     * 
     * @param  <T> type parameter
     * @param  array the array
     * @param  t the element
     * @return {@code true} if {@code array} contains {@code t}, {@code false} otherwise
     */
    public static <T> boolean contains(final T[] array, final T t) {
        return anyMatch(array, t == null ? (e) -> e == null : t::equals);
    }
    
    /**
     * Performs a reduction on the elements of this array, using an associative accumulation function, and returns an
     * {@code Optional} describing the reduced value, if any.
     * 
     * @param  <T> type parameter
     * @param  array the array
     * @param  accumulator the accumulator
     * @return an {@code Optional} describing the result of the reduction
     */
    public static <T> Optional<T> reduce(final T[] array, final BinaryOperator<T> accumulator) {
        if (array.length == 0)
            return Optional.empty();
        
        T result = array[0];
        for (int i = 1; i < array.length; i++)
            result = accumulator.apply(result, array[i]);
        
        return Optional.of(result);
    }
    
    /**
     * Performs a reduction on the elements of this array, using the provided identity value and an associative
     * accumulation function, and returns the reduced value.
     * 
     * @param  <T> type parameter
     * @param  array the array
     * @param  identity the identity
     * @param  accumulator the accumulator
     * @return the result of the reduction
     */
    public static <T> T reduce(final T[] array, final T identity, final BinaryOperator<T> accumulator) {
        T result = identity;
        for (final T e : array)
            result = accumulator.apply(result, e);
        
        return result;
    }
    
    /**
     * Performs a reduction on the elements of this array, using the provided identity value and an associative
     * accumulation function, and returns the reduced value.
     * 
     * @param  array the array
     * @param  identity the identity
     * @param  accumulator the accumulator
     * @return the result of the reduction
     */
    public static byte reduce(final byte[] array, final byte identity, final ByteBinaryOperator accumulator) {
        byte result = identity;
        for (final byte b : array)
            result = accumulator.applyAsByte(result, b);
        
        return result;
    }
    
    /**
     * Performs a reduction on the elements of this array, using the provided identity value and an associative
     * accumulation function, and returns the reduced value.
     * 
     * @param  array the array
     * @param  identity the identity
     * @param  accumulator the accumulator
     * @return the result of the reduction
     */
    public static char reduce(final char[] array, final char identity, final CharBinaryOperator accumulator) {
        char result = identity;
        for (final char b : array)
            result = accumulator.applyAsChar(result, b);
        
        return result;
    }
    
    /**
     * Performs a reduction on the elements of this array, using the provided identity value and an associative
     * accumulation function, and returns the reduced value.
     * 
     * @param  array the array
     * @param  identity the identity
     * @param  accumulator the accumulator
     * @return the result of the reduction
     */
    public static double reduce(final double[] array, final double identity, final DoubleBinaryOperator accumulator) {
        double result = identity;
        for (final double b : array)
            result = accumulator.applyAsDouble(result, b);
        
        return result;
    }
    
    /**
     * Performs a reduction on the elements of this array, using the provided identity value and an associative
     * accumulation function, and returns the reduced value.
     * 
     * @param  array the array
     * @param  identity the identity
     * @param  accumulator the accumulator
     * @return the result of the reduction
     */
    public static float reduce(final float[] array, final float identity, final FloatBinaryOperator accumulator) {
        float result = identity;
        for (final float b : array)
            result = accumulator.applyAsFloat(result, b);
        
        return result;
    }
    
    /**
     * Performs a reduction on the elements of this array, using the provided identity value and an associative
     * accumulation function, and returns the reduced value.
     * 
     * @param  array the array
     * @param  identity the identity
     * @param  accumulator the accumulator
     * @return the result of the reduction
     */
    public static int reduce(final int[] array, final int identity, final IntBinaryOperator accumulator) {
        int result = identity;
        for (final int b : array)
            result = accumulator.applyAsInt(result, b);
        
        return result;
    }
    
    /**
     * Performs a reduction on the elements of this array, using the provided identity value and an associative
     * accumulation function, and returns the reduced value.
     * 
     * @param  array the array
     * @param  identity the identity
     * @param  accumulator the accumulator
     * @return the result of the reduction
     */
    public static long reduce(final long[] array, final long identity, final LongBinaryOperator accumulator) {
        long result = identity;
        for (final long b : array)
            result = accumulator.applyAsLong(result, b);
        
        return result;
    }
    
    /**
     * Returns the greatest {@code byte} value in an array.
     * 
     * <p>If the array is empty, then {@link Byte#MIN_VALUE} is returned.
     * 
     * @param  array the array
     * @return the greatest {@code byte} value in the array
     */
    public static byte max(final byte[] array) {
        return reduce(array, Byte.MIN_VALUE, MathUtil::max);
    }
    
    /**
     * Returns the greatest {@code char} value in an array.
     * 
     * <p>If the array is empty, then {@link Character#MIN_VALUE} is returned.
     * 
     * @param  array the array
     * @return the greatest {@code char} value in the array
     */
    public static char max(final char[] array) {
        return reduce(array, Character.MIN_VALUE, MathUtil::max);
    }
    
    /**
     * Returns the greatest {@code double} value in an array.
     * 
     * <p>If the array is empty, then -{@link Double#MAX_VALUE} is returned.
     * 
     * @param  array the array
     * @return the greatest {@code double} value in the array
     */
    public static double max(final double[] array) {
        double result = -Double.MAX_VALUE;
        for (final double d : array)
            if (Double.isNaN(result = Math.max(result, d)))
                break;
        
        return result;
    }
    
    /**
     * Returns the greatest {@code float} value in an array.
     * 
     * <p>If the array is empty, then -{@link Float#MAX_VALUE} is returned.
     * 
     * @param  array the array
     * @return the greatest {@code float} value in the array
     */
    public static float max(final float[] array) {
        float result = -Float.MAX_VALUE;
        for (final float f : array)
            if (Float.isNaN(result = Math.max(result, f)))
                break;
        
        return result;
    }
    
    /**
     * Returns the greatest {@code int} value in an array.
     * 
     * <p>If the array is empty, then {@link Integer#MIN_VALUE} is returned.
     * 
     * @param  array the array
     * @return the greatest {@code int} value in the array
     */
    public static int max(final int[] array) {
        return reduce(array, Integer.MIN_VALUE, MathUtil::max);
    }
    
    /**
     * Returns the greatest {@code long} value in an array.
     * 
     * <p>If the array is empty, then {@link Long#MIN_VALUE} is returned.
     * 
     * @param  array the array
     * @return the greatest {@code long} value in the array
     */
    public static long max(final long[] array) {
        return reduce(array, Long.MIN_VALUE, MathUtil::max);
    }
    
    /**
     * Returns the smallest {@code byte} value in an array.
     * 
     * <p>If the array is empty, then {@link Byte#MAX_VALUE} is returned.
     * 
     * @param  array the array
     * @return the smallest {@code byte} value in the array
     */
    public static byte min(final byte[] array) {
        return reduce(array, Byte.MAX_VALUE, MathUtil::max);
    }
    
    /**
     * Returns the smallest {@code char} value in an array.
     * 
     * <p>If the array is empty, then {@link Character#MAX_VALUE} is returned.
     * 
     * @param  array the array
     * @return the smallest {@code char} value in the array
     */
    public static char min(final char[] array) {
        return reduce(array, Character.MAX_VALUE, MathUtil::max);
    }
    
    /**
     * Returns the smallest {@code double} value in an array.
     * 
     * <p>If the array is empty, then {@link Double#MAX_VALUE} is returned.
     * 
     * @param  array the array
     * @return the smallest {@code double} value in the array
     */
    public static double min(final double[] array) {
        double result = Double.MAX_VALUE;
        for (final double d : array)
            if (Double.isNaN(result = Math.min(result, d)))
                break;
        
        return result;
    }
    
    /**
     * Returns the smallest {@code float} value in an array.
     * 
     * <p>If the array is empty, then {@link Float#MAX_VALUE} is returned.
     * 
     * @param  array the array
     * @return the smallest {@code float} value in the array
     */
    public static float min(final float[] array) {
        float result = Float.MAX_VALUE;
        for (final float f : array)
            if (Float.isNaN(result = Math.min(result, f)))
                break;
        
        return result;
    }
    
    /**
     * Returns the smallest {@code int} value in an array.
     * 
     * <p>If the array is empty, then {@link Integer#MAX_VALUE} is returned.
     * 
     * @param  array the array
     * @return the smallest {@code int} value in the array
     */
    public static int min(final int[] array) {
        return reduce(array, Integer.MAX_VALUE, MathUtil::min);
    }
    
    /**
     * Returns the smallest {@code long} value in an array.
     * 
     * <p>If the array is empty, then {@link Long#MAX_VALUE} is returned.
     * 
     * @param  array the array
     * @return the smallest {@code long} value in the array
     */
    public static long min(final long[] array) {
        return reduce(array, Long.MAX_VALUE, MathUtil::min);
    }
    
    /**
     * Returns the greatest {@code double} value in an array, in parallel.
     * 
     * <p>If the array is empty, then -{@link Double#MAX_VALUE} is returned.
     * 
     * @param  array the array
     * @return the greatest {@code double} value in the array
     */
    public static double parallelMax(final double[] array) {
        return Arrays.stream(array).parallel().max().orElse(-Double.MAX_VALUE);
    }
    
    /**
     * Returns the greatest {@code int} value in an array, in parallel.
     * 
     * <p>If the array is empty, then {@link Integer#MIN_VALUE} is returned.
     * 
     * @param  array the array
     * @return the greatest {@code int} value in the array
     */
    public static int parallelMax(final int[] array) {
        return Arrays.stream(array).parallel().max().orElse(Integer.MIN_VALUE);
    }
    
    /**
     * Returns the greatest {@code long} value in an array, in parallel.
     * 
     * <p>If the array is empty, then {@link Long#MIN_VALUE} is returned.
     * 
     * @param  array the array
     * @return the greatest {@code long} value in the array
     */
    public static long parallelMax(final long[] array) {
        return Arrays.stream(array).parallel().max().orElse(Long.MIN_VALUE);
    }
    
    /**
     * Returns the greatest {@code double} value in an array, in parallel.
     * 
     * <p>If the array is empty, then -{@link Double#MAX_VALUE} is returned.
     * 
     * @param  array the array
     * @return the smallest {@code double} value in the array
     */
    public static double parallelMin(final double[] array) {
        return Arrays.stream(array).parallel().min().orElse(Double.MAX_VALUE);
    }
    
    /**
     * Returns the smallest {@code int} value in an array, in parallel.
     * 
     * <p>If the array is empty, then {@link Integer#MAX_VALUE} is returned.
     * 
     * @param  array the array
     * @return the smallest {@code int} value in the array
     */
    public static int parallelMin(final int[] array) {
        return Arrays.stream(array).parallel().min().orElse(Integer.MAX_VALUE);
    }
    
    /**
     * Returns the smallest {@code long} value in an array, in parallel.
     * 
     * <p>If the array is empty, then {@link Long#MAX_VALUE} is returned.
     * 
     * @param  array the array
     * @return the smallest {@code long} value in the array
     */
    public static long parallelMin(final long[] array) {
        return Arrays.stream(array).parallel().min().orElse(Long.MAX_VALUE);
    }
    
    /**
     * Returns whether all elements of an array match a predicate.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code allMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if either all elements of the match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static boolean allMatch(final boolean[] array, final BooleanPredicate p) {
        for (final boolean e : array)
            if (!p.test(e))
                return false;
        
        return true;
    }
    
    /**
     * Returns whether all elements of an array match a predicate.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code allMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if either all elements of the match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static boolean allMatch(final byte[] array, final BytePredicate p) {
        for (final byte e : array)
            if (!p.test(e))
                return false;
        
        return true;
    }
    
    /**
     * Returns whether all elements of an array match a predicate.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code allMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if either all elements of the match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static boolean allMatch(final char[] array, final CharPredicate p) {
        for (final char e : array)
            if (!p.test(e))
                return false;
        
        return true;
    }
    
    /**
     * Returns whether all elements of an array match a predicate.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code allMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if either all elements of the match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static boolean allMatch(final double[] array, final DoublePredicate p) {
        for (final double e : array)
            if (!p.test(e))
                return false;
        
        return true;
    }
    
    /**
     * Returns whether all elements of an array match a predicate.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code allMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if either all elements of the match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static boolean allMatch(final float[] array, final FloatPredicate p) {
        for (final float e : array)
            if (!p.test(e))
                return false;
        
        return true;
    }
    
    /**
     * Returns whether all elements of an array match a predicate.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code allMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if either all elements of the match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static boolean allMatch(final int[] array, final IntPredicate p) {
        for (final int e : array)
            if (!p.test(e))
                return false;
        
        return true;
    }
    
    /**
     * Returns whether all elements of an array match a predicate.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code allMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if either all elements of the match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static boolean allMatch(final long[] array, final LongPredicate p) {
        for (final long e : array)
            if (!p.test(e))
                return false;
        
        return true;
    }
    
    /**
     * Returns whether all elements of an array match a predicate.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code allMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if either all elements of the match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static boolean allMatch(final short[] array, final ShortPredicate p) {
        for (final short e : array)
            if (!p.test(e))
                return false;
        
        return true;
    }
    
    /**
     * Returns whether all elements of an array match a predicate.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code allMatch}.
     * 
     * @param  <T> type parameter
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if either all elements of the match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static <T> boolean allMatch(final T[] array, final Predicate<? super T> p) {
        for (final T e : array)
            if (!p.test(e))
                return false;
        
        return true;
    }
    
    /**
     * Returns whether any elements of an array match a predicate.<br>
     * An empty array returns {@code false}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code anyMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if any elements of the array match the provided predicate, {@code false} otherwise
     */
    public static boolean anyMatch(final boolean[] array, final BooleanPredicate p) {
        for (final boolean e : array)
            if (p.test(e))
                return true;
        
        return false;
    }
    
    /**
     * Returns whether any elements of an array match a predicate.<br>
     * An empty array returns {@code false}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code anyMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if any elements of the array match the provided predicate, {@code false} otherwise
     */
    public static boolean anyMatch(final byte[] array, final BytePredicate p) {
        for (final byte e : array)
            if (p.test(e))
                return true;
        
        return false;
    }
    
    /**
     * Returns whether any elements of an array match a predicate.<br>
     * An empty array returns {@code false}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code anyMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if any elements of the array match the provided predicate, {@code false} otherwise
     */
    public static boolean anyMatch(final char[] array, final CharPredicate p) {
        for (final char e : array)
            if (p.test(e))
                return true;
        
        return false;
    }
    
    /**
     * Returns whether any elements of an array match a predicate.<br>
     * An empty array returns {@code false}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code anyMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if any elements of the array match the provided predicate, {@code false} otherwise
     */
    public static boolean anyMatch(final double[] array, final DoublePredicate p) {
        for (final double e : array)
            if (p.test(e))
                return true;
        
        return false;
    }
    
    /**
     * Returns whether any elements of an array match a predicate.<br>
     * An empty array returns {@code false}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code anyMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if any elements of the array match the provided predicate, {@code false} otherwise
     */
    public static boolean anyMatch(final float[] array, final FloatPredicate p) {
        for (final float e : array)
            if (p.test(e))
                return true;
        
        return false;
    }
    
    /**
     * Returns whether any elements of an array match a predicate.<br>
     * An empty array returns {@code false}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code anyMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if any elements of the array match the provided predicate, {@code false} otherwise
     */
    public static boolean anyMatch(final int[] array, final IntPredicate p) {
        for (final int e : array)
            if (p.test(e))
                return true;
        
        return false;
    }
    
    /**
     * Returns whether any elements of an array match a predicate.<br>
     * An empty array returns {@code false}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code anyMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if any elements of the array match the provided predicate, {@code false} otherwise
     */
    public static boolean anyMatch(final long[] array, final LongPredicate p) {
        for (final long e : array)
            if (p.test(e))
                return true;
        
        return false;
    }
    
    /**
     * Returns whether any elements of an array match a predicate.<br>
     * An empty array returns {@code false}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code anyMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if any elements of the array match the provided predicate, {@code false} otherwise
     */
    public static boolean anyMatch(final short[] array, final ShortPredicate p) {
        for (final short e : array)
            if (p.test(e))
                return true;
        
        return false;
    }
    
    /**
     * Returns whether any elements of an array match a predicate.<br>
     * An empty array returns {@code false}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code anyMatch}.
     * 
     * @param  <T> type parameter
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if any elements of the array match the provided predicate, {@code false} otherwise
     */
    public static <T> boolean anyMatch(final T[] array, final Predicate<? super T> p) {
        for (final T e : array)
            if (p.test(e))
                return true;
        
        return false;
    }
    
    /**
     * Returns whether any combination of elements of two arrays match a predicate.<br>
     * If at least one array is empty, {@code false} will be returned.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code anyMatch}.
     * 
     * @param  <T> type parameter
     * @param  <U> type parameter
     * @param  arrayA the first {@code Array}
     * @param  arrayB the second {@code Array}
     * @param  p the predicate
     * @return {@code true} if any combination of elements of the given arrays match the provided predicate, {@code
     *         false} otherwise
     */
    public static <T, U> boolean anyMatch(final T[] arrayA, final U[] arrayB,
            final BiPredicate<? super T, ? super U> p) {
        return anyMatch(arrayA, (a) -> anyMatch(arrayB, (b) -> p.test(a, b)));
    }
    
    /**
     * Returns whether no elements of an array match a predicate.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code noneMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if no elements of the array match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static boolean noneMatch(final boolean[] array, final BooleanPredicate p) {
        for (final boolean e : array)
            if (p.test(e))
                return false;
        
        return true;
    }
    
    /**
     * Returns whether no elements of an array match a predicate.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code noneMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if no elements of the array match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static boolean noneMatch(final byte[] array, final BytePredicate p) {
        for (final byte e : array)
            if (p.test(e))
                return false;
        
        return true;
    }
    
    /**
     * Returns whether no elements of an array match a predicate.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code noneMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if no elements of the array match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static boolean noneMatch(final char[] array, final CharPredicate p) {
        for (final char e : array)
            if (p.test(e))
                return false;
        
        return true;
    }
    
    /**
     * Returns whether no elements of an array match a predicate.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code noneMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if no elements of the array match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static boolean noneMatch(final double[] array, final DoublePredicate p) {
        for (final double e : array)
            if (p.test(e))
                return false;
        
        return true;
    }
    
    /**
     * Returns whether no elements of an array match a predicate.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code noneMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if no elements of the array match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static boolean noneMatch(final float[] array, final FloatPredicate p) {
        for (final float e : array)
            if (p.test(e))
                return false;
        
        return true;
    }
    
    /**
     * Returns whether no elements of an array match a predicate.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code noneMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if no elements of the array match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static boolean noneMatch(final int[] array, final IntPredicate p) {
        for (final int e : array)
            if (p.test(e))
                return false;
        
        return true;
    }
    
    /**
     * Returns whether no elements of an array match a predicate.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code noneMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if no elements of the array match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static boolean noneMatch(final long[] array, final LongPredicate p) {
        for (final long e : array)
            if (p.test(e))
                return false;
        
        return true;
    }
    
    /**
     * Returns whether no elements of an array match a predicate.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code noneMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if no elements of the array match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static boolean noneMatch(final short[] array, final ShortPredicate p) {
        for (final short e : array)
            if (p.test(e))
                return false;
        
        return true;
    }
    
    /**
     * Returns whether no elements of an array match a predicate.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code noneMatch}.
     * 
     * @param  <T> type parameter
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if no elements of the array match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static <T> boolean noneMatch(final T[] array, final Predicate<? super T> p) {
        for (final T e : array)
            if (p.test(e))
                return false;
        
        return true;
    }
    
    /**
     * Returns whether all elements of an array match a predicate, in parallel.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code parallelAllMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if either all elements of the match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static boolean parallelAllMatch(final boolean[] array, final BooleanPredicate p) {
        return IntStream.range(0, array.length).parallel().allMatch((i) -> p.test(array[i]));
    }
    
    /**
     * Returns whether all elements of an array match a predicate, in parallel.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code parallelAllMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if either all elements of the match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static boolean parallelAllMatch(final byte[] array, final BytePredicate p) {
        return IntStream.range(0, array.length).parallel().allMatch((i) -> p.test(array[i]));
    }
    
    /**
     * Returns whether all elements of an array match a predicate, in parallel.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code parallelAllMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if either all elements of the match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static boolean parallelAllMatch(final char[] array, final CharPredicate p) {
        return IntStream.range(0, array.length).parallel().allMatch((i) -> p.test(array[i]));
    }
    
    /**
     * Returns whether all elements of an array match a predicate, in parallel.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code parallelAllMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if either all elements of the match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static boolean parallelAllMatch(final double[] array, final DoublePredicate p) {
        return IntStream.range(0, array.length).parallel().allMatch((i) -> p.test(array[i]));
    }
    
    /**
     * Returns whether all elements of an array match a predicate, in parallel.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code parallelAllMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if either all elements of the match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static boolean parallelAllMatch(final float[] array, final FloatPredicate p) {
        return IntStream.range(0, array.length).parallel().allMatch((i) -> p.test(array[i]));
    }
    
    /**
     * Returns whether all elements of an array match a predicate, in parallel.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code parallelAllMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if either all elements of the match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static boolean parallelAllMatch(final int[] array, final IntPredicate p) {
        return IntStream.range(0, array.length).parallel().allMatch((i) -> p.test(array[i]));
    }
    
    /**
     * Returns whether all elements of an array match a predicate, in parallel.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code parallelAllMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if either all elements of the match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static boolean parallelAllMatch(final long[] array, final LongPredicate p) {
        return IntStream.range(0, array.length).parallel().allMatch((i) -> p.test(array[i]));
    }
    
    /**
     * Returns whether all elements of an array match a predicate, in parallel.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code parallelAllMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if either all elements of the match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static boolean parallelAllMatch(final short[] array, final ShortPredicate p) {
        return IntStream.range(0, array.length).parallel().allMatch((i) -> p.test(array[i]));
    }
    
    /**
     * Returns whether all elements of an array match a predicate, in parallel.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code parallelAllMatch}.
     * 
     * @param  <T> type parameter
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if either all elements of the match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static <T> boolean parallelAllMatch(final T[] array, final Predicate<? super T> p) {
        return IntStream.range(0, array.length).parallel().allMatch((i) -> p.test(array[i]));
    }
    
    /**
     * Returns whether any elements of a an array match a predicate.<br>
     * An empty array returns {@code false}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code parallelAnyMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if any elements of the array match the provided predicate, {@code false} otherwise
     */
    public static boolean parallelAnyMatch(final boolean[] array, final BooleanPredicate p) {
        return IntStream.range(0, array.length).parallel().anyMatch((i) -> p.test(array[i]));
    }
    
    /**
     * Returns whether any elements of a an array match a predicate.<br>
     * An empty array returns {@code false}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code parallelAnyMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if any elements of the array match the provided predicate, {@code false} otherwise
     */
    public static boolean parallelAnyMatch(final byte[] array, final BytePredicate p) {
        return IntStream.range(0, array.length).parallel().anyMatch((i) -> p.test(array[i]));
    }
    
    /**
     * Returns whether any elements of a an array match a predicate.<br>
     * An empty array returns {@code false}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code parallelAnyMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if any elements of the array match the provided predicate, {@code false} otherwise
     */
    public static boolean parallelAnyMatch(final char[] array, final CharPredicate p) {
        return IntStream.range(0, array.length).parallel().anyMatch((i) -> p.test(array[i]));
    }
    
    /**
     * Returns whether any elements of a an array match a predicate.<br>
     * An empty array returns {@code false}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code parallelAnyMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if any elements of the array match the provided predicate, {@code false} otherwise
     */
    public static boolean parallelAnyMatch(final double[] array, final DoublePredicate p) {
        return IntStream.range(0, array.length).parallel().anyMatch((i) -> p.test(array[i]));
    }
    
    /**
     * Returns whether any elements of a an array match a predicate.<br>
     * An empty array returns {@code false}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code parallelAnyMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if any elements of the array match the provided predicate, {@code false} otherwise
     */
    public static boolean parallelAnyMatch(final float[] array, final FloatPredicate p) {
        return IntStream.range(0, array.length).parallel().anyMatch((i) -> p.test(array[i]));
    }
    
    /**
     * Returns whether any elements of a an array match a predicate.<br>
     * An empty array returns {@code false}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code parallelAnyMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if any elements of the array match the provided predicate, {@code false} otherwise
     */
    public static boolean parallelAnyMatch(final int[] array, final IntPredicate p) {
        return IntStream.range(0, array.length).parallel().anyMatch((i) -> p.test(array[i]));
    }
    
    /**
     * Returns whether any elements of a an array match a predicate.<br>
     * An empty array returns {@code false}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code parallelAnyMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if any elements of the array match the provided predicate, {@code false} otherwise
     */
    public static boolean parallelAnyMatch(final long[] array, final LongPredicate p) {
        return IntStream.range(0, array.length).parallel().anyMatch((i) -> p.test(array[i]));
    }
    
    /**
     * Returns whether any elements of a an array match a predicate.<br>
     * An empty array returns {@code false}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code parallelAnyMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if any elements of the array match the provided predicate, {@code false} otherwise
     */
    public static boolean parallelAnyMatch(final short[] array, final ShortPredicate p) {
        return IntStream.range(0, array.length).parallel().anyMatch((i) -> p.test(array[i]));
    }
    
    /**
     * Returns whether any elements of a an array match a predicate.<br>
     * An empty array returns {@code false}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code parallelAnyMatch}.
     * 
     * @param  <T> type parameter
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if any elements of the array match the provided predicate, {@code false} otherwise
     */
    public static <T> boolean parallelAnyMatch(final T[] array, final Predicate<? super T> p) {
        return IntStream.range(0, array.length).parallel().anyMatch((i) -> p.test(array[i]));
    }
    
    /**
     * Returns whether no elements of an array match a predicate, in parallel.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code parallelNoneMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if no elements of the array match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static boolean parallelNoneMatch(final boolean[] array, final BooleanPredicate p) {
        return IntStream.range(0, array.length).parallel().noneMatch((i) -> p.test(array[i]));
    }
    
    /**
     * Returns whether no elements of an array match a predicate, in parallel.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code parallelNoneMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if no elements of the array match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static boolean parallelNoneMatch(final byte[] array, final BytePredicate p) {
        return IntStream.range(0, array.length).parallel().noneMatch((i) -> p.test(array[i]));
    }
    
    /**
     * Returns whether no elements of an array match a predicate, in parallel.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code parallelNoneMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if no elements of the array match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static boolean parallelNoneMatch(final char[] array, final CharPredicate p) {
        return IntStream.range(0, array.length).parallel().noneMatch((i) -> p.test(array[i]));
    }
    
    /**
     * Returns whether no elements of an array match a predicate, in parallel.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code parallelNoneMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if no elements of the array match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static boolean parallelNoneMatch(final double[] array, final DoublePredicate p) {
        return IntStream.range(0, array.length).parallel().noneMatch((i) -> p.test(array[i]));
    }
    
    /**
     * Returns whether no elements of an array match a predicate, in parallel.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code parallelNoneMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if no elements of the array match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static boolean parallelNoneMatch(final float[] array, final FloatPredicate p) {
        return IntStream.range(0, array.length).parallel().noneMatch((i) -> p.test(array[i]));
    }
    
    /**
     * Returns whether no elements of an array match a predicate, in parallel.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code parallelNoneMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if no elements of the array match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static boolean parallelNoneMatch(final int[] array, final IntPredicate p) {
        return IntStream.range(0, array.length).parallel().noneMatch((i) -> p.test(array[i]));
    }
    
    /**
     * Returns whether no elements of an array match a predicate, in parallel.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code parallelNoneMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if no elements of the array match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static boolean parallelNoneMatch(final long[] array, final LongPredicate p) {
        return IntStream.range(0, array.length).parallel().noneMatch((i) -> p.test(array[i]));
    }
    
    /**
     * Returns whether no elements of an array match a predicate, in parallel.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code parallelNoneMatch}.
     * 
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if no elements of the array match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static boolean parallelNoneMatch(final short[] array, final ShortPredicate p) {
        return IntStream.range(0, array.length).parallel().noneMatch((i) -> p.test(array[i]));
    }
    
    /**
     * Returns whether no elements of an array match a predicate, in parallel.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code parallelNoneMatch}.
     * 
     * @param  <T> type parameter
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if no elements of the array match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    public static <T> boolean parallelNoneMatch(final T[] array, final Predicate<? super T> p) {
        return IntStream.range(0, array.length).parallel().noneMatch((i) -> p.test(array[i]));
    }
    
    /**
     * Reverses the order of the elements in an array between position {@code posA} and {@code posB}.
     * 
     * @param array the {@code Array}
     * @param posA the first position
     * @param posB the second position
     */
    public static void reverse(final boolean[] array, final int posA, final int posB) {
        for (int a = posA, b = posB; a < b; a++, b--)
            swap(array, a, b);
    }
    
    /**
     * Reverses the order of the elements in an array between inclusive position {@code posA} and {@code posB}.
     * 
     * @param array the {@code Array}
     * @param posA the first position
     * @param posB the second position
     */
    public static void reverse(final byte[] array, final int posA, final int posB) {
        for (int a = posA, b = posB; a < b; a++, b--)
            swap(array, a, b);
    }
    
    /**
     * Reverses the order of the elements in an array between inclusive position {@code posA} and {@code posB}.
     * 
     * @param array the {@code Array}
     * @param posA the first position
     * @param posB the second position
     */
    public static void reverse(final char[] array, final int posA, final int posB) {
        for (int a = posA, b = posB; a < b; a++, b--)
            swap(array, a, b);
    }
    
    /**
     * Reverses the order of the elements in an array between inclusive position {@code posA} and {@code posB}.
     * 
     * @param array the {@code Array}
     * @param posA the first position
     * @param posB the second position
     */
    public static void reverse(final double[] array, final int posA, final int posB) {
        for (int a = posA, b = posB; a < b; a++, b--)
            swap(array, a, b);
    }
    
    /**
     * Reverses the order of the elements in an array between inclusive position {@code posA} and {@code posB}.
     * 
     * @param array the {@code Array}
     * @param posA the first position
     * @param posB the second position
     */
    public static void reverse(final float[] array, final int posA, final int posB) {
        for (int a = posA, b = posB; a < b; a++, b--)
            swap(array, a, b);
    }
    
    /**
     * Reverses the order of the elements in an array between inclusive position {@code posA} and {@code posB}.
     * 
     * @param array the {@code Array}
     * @param posA the first position
     * @param posB the second position
     */
    public static void reverse(final int[] array, final int posA, final int posB) {
        for (int a = posA, b = posB; a < b; a++, b--)
            swap(array, a, b);
    }
    
    /**
     * Reverses the order of the elements in an array between inclusive position {@code posA} and {@code posB}.
     * 
     * @param array the {@code Array}
     * @param posA the first position
     * @param posB the second position
     */
    public static void reverse(final long[] array, final int posA, final int posB) {
        for (int a = posA, b = posB; a < b; a++, b--)
            swap(array, a, b);
    }
    
    /**
     * Reverses the order of the elements in an array between inclusive position {@code posA} and {@code posB}.
     * 
     * @param array the {@code Array}
     * @param posA the first position
     * @param posB the second position
     */
    public static void reverse(final short[] array, final int posA, final int posB) {
        for (int a = posA, b = posB; a < b; a++, b--)
            swap(array, a, b);
    }
    
    /**
     * Reverses the order of the elements in an array between inclusive position {@code posA} and {@code posB}.
     * 
     * @param <T> type parameter
     * @param array the {@code Array}
     * @param posA the first position
     * @param posB the second position
     */
    public static <T> void reverse(final T[] array, final int posA, final int posB) {
        for (int a = posA, b = posB; a < b; a++, b--)
            swap(array, a, b);
    }
    
    /**
     * Rotates the elements in an array by the given {@code offset}.
     * 
     * @param array the {@code Array}
     * @param offset the offset
     */
    public static void rotate(final boolean[] array, final int offset) {
        reverse(array, array.length - offset, array.length - 1);
        reverse(array, 0, array.length - 1 - offset);
        reverse(array, 0, array.length - 1);
    }
    
    /**
     * Rotates the elements in an array by the given {@code offset}.
     * 
     * @param array the {@code Array}
     * @param offset the offset
     */
    public static void rotate(final byte[] array, final int offset) {
        reverse(array, array.length - offset, array.length - 1);
        reverse(array, 0, array.length - 1 - offset);
        reverse(array, 0, array.length - 1);
    }
    
    /**
     * Rotates the elements in an array by the given {@code offset}.
     * 
     * @param array the {@code Array}
     * @param offset the offset
     */
    public static void rotate(final char[] array, final int offset) {
        reverse(array, array.length - offset, array.length - 1);
        reverse(array, 0, array.length - 1 - offset);
        reverse(array, 0, array.length - 1);
    }
    
    /**
     * Rotates the elements in an array by the given {@code offset}.
     * 
     * @param array the {@code Array}
     * @param offset the offset
     */
    public static void rotate(final double[] array, final int offset) {
        reverse(array, array.length - offset, array.length - 1);
        reverse(array, 0, array.length - 1 - offset);
        reverse(array, 0, array.length - 1);
    }
    
    /**
     * Rotates the elements in an array by the given {@code offset}.
     * 
     * @param array the {@code Array}
     * @param offset the offset
     */
    public static void rotate(final float[] array, final int offset) {
        reverse(array, array.length - offset, array.length - 1);
        reverse(array, 0, array.length - 1 - offset);
        reverse(array, 0, array.length - 1);
    }
    
    /**
     * Rotates the elements in an array by the given {@code offset}.
     * 
     * @param array the {@code Array}
     * @param offset the offset
     */
    public static void rotate(final int[] array, final int offset) {
        reverse(array, array.length - offset, array.length - 1);
        reverse(array, 0, array.length - 1 - offset);
        reverse(array, 0, array.length - 1);
    }
    
    /**
     * Rotates the elements in an array by the given {@code offset}.
     * 
     * @param array the {@code Array}
     * @param offset the offset
     */
    public static void rotate(final long[] array, final int offset) {
        reverse(array, array.length - offset, array.length - 1);
        reverse(array, 0, array.length - 1 - offset);
        reverse(array, 0, array.length - 1);
    }
    
    /**
     * Rotates the elements in an array by the given {@code offset}.
     * 
     * @param array the {@code Array}
     * @param offset the offset
     */
    public static void rotate(final short[] array, final int offset) {
        reverse(array, array.length - offset, array.length - 1);
        reverse(array, 0, array.length - 1 - offset);
        reverse(array, 0, array.length - 1);
    }
    
    /**
     * Rotates the elements in an array by the given {@code offset}.
     * 
     * @param <T> type parameter
     * @param array the {@code Array}
     * @param offset the offset
     */
    public static <T> void rotate(final T[] array, final int offset) {
        reverse(array, array.length - offset, array.length - 1);
        reverse(array, 0, array.length - 1 - offset);
        reverse(array, 0, array.length - 1);
    }
    
    /**
     * Swaps the elements in an array at position {@code posA} and {@code posB}.
     * 
     * @param array the {@code Array}
     * @param posA the first position
     * @param posB the second position
     */
    public static void swap(final boolean[] array, final int posA, final int posB) {
        final boolean tmp = array[posA];
        array[posA] = array[posB];
        array[posB] = tmp;
    }
    
    /**
     * Swaps the elements in an array at position {@code posA} and {@code posB}.
     * 
     * @param array the {@code Array}
     * @param posA the first position
     * @param posB the second position
     */
    public static void swap(final byte[] array, final int posA, final int posB) {
        final byte tmp = array[posA];
        array[posA] = array[posB];
        array[posB] = tmp;
    }
    
    /**
     * Swaps the elements in an array at position {@code posA} and {@code posB}.
     * 
     * @param array the {@code Array}
     * @param posA the first position
     * @param posB the second position
     */
    public static void swap(final char[] array, final int posA, final int posB) {
        final char tmp = array[posA];
        array[posA] = array[posB];
        array[posB] = tmp;
    }
    
    /**
     * Swaps the elements in an array at position {@code posA} and {@code posB}.
     * 
     * @param array the {@code Array}
     * @param posA the first position
     * @param posB the second position
     */
    public static void swap(final double[] array, final int posA, final int posB) {
        final double tmp = array[posA];
        array[posA] = array[posB];
        array[posB] = tmp;
    }
    
    /**
     * Swaps the elements in an array at position {@code posA} and {@code posB}.
     * 
     * @param array the {@code Array}
     * @param posA the first position
     * @param posB the second position
     */
    public static void swap(final float[] array, final int posA, final int posB) {
        final float tmp = array[posA];
        array[posA] = array[posB];
        array[posB] = tmp;
    }
    
    /**
     * Swaps the elements in an array at position {@code posA} and {@code posB}.
     * 
     * @param array the {@code Array}
     * @param posA the first position
     * @param posB the second position
     */
    public static void swap(final int[] array, final int posA, final int posB) {
        final int tmp = array[posA];
        array[posA] = array[posB];
        array[posB] = tmp;
    }
    
    /**
     * Swaps the elements in an array at position {@code posA} and {@code posB}.
     * 
     * @param array the {@code Array}
     * @param posA the first position
     * @param posB the second position
     */
    public static void swap(final long[] array, final int posA, final int posB) {
        final long tmp = array[posA];
        array[posA] = array[posB];
        array[posB] = tmp;
    }
    
    /**
     * Swaps the elements in an array at position {@code posA} and {@code posB}.
     * 
     * @param array the {@code Array}
     * @param posA the first position
     * @param posB the second position
     */
    public static void swap(final short[] array, final int posA, final int posB) {
        final short tmp = array[posA];
        array[posA] = array[posB];
        array[posB] = tmp;
    }
    
    /**
     * Swaps the elements in an array at position {@code posA} and {@code posB}.
     * 
     * @param <T> type parameter
     * @param array the {@code Array}
     * @param posA the first position
     * @param posB the second position
     */
    public static <T> void swap(final T[] array, final int posA, final int posB) {
        final T tmp = array[posA];
        array[posA] = array[posB];
        array[posB] = tmp;
    }
    
    /**
     * Returns the object in the {@code indices.length - 1} dimension of the specified array.
     * 
     * <p>If {@code indices.length} is {@code 1}, then the specified array is returned.
     * 
     * @param  array the array
     * @param  indices the indices
     * @return the value of the indexed component in the specified array
     * @throws NullPointerException if the specified object is {@code null}
     * @throws IllegalArgumentException if {@code indices.length} is {@code 0}, or if the specified object is not an
     *         array of dimension {@code indices.length - 2}
     * @throws ArrayIndexOutOfBoundsException if any of the specified {@code indices} is negative or greater than or
     *         equal to the length of the specific dimension of the specified array
     */
    private static Object getArray(final Object array, final int[] indices) {
        if (indices.length == 0)
            throw new IllegalArgumentException("indices has length 0");
        
        Object current = array;
        for (int pos = 0; pos < indices.length - 1; pos++)
            current = Array.get(current, indices[pos]);
        
        return current;
    }
}
