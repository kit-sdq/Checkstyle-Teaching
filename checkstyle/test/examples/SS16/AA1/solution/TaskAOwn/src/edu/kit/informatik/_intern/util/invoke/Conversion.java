package edu.kit.informatik._intern.util.invoke;

import java.util.Objects;

/**
 * Contains methods regarding conversions.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/06/20
 */
public enum Conversion {
    
    INT     ( int.class     , Integer.class   , 0b001001001 , 0b001001101 , 'I' ),
    BYTE    ( byte.class    , Byte.class      , 0b011001111 , 0b011001111 , 'B' ),
    FLOAT   ( float.class   , Float.class     , 0b000000100 , 0b000001100 , 'F' ),
    DOUBLE  ( double.class  , Double.class    , 0b000001000 , 0b000001000 , 'D' ),
    VOID    ( void.class    , Void.class      , 0b000010000 , 0b000010000 , 'V' ),
    CHAR    ( char.class    , Character.class , 0b001101101 , 0b001101101 , 'C' ),
    LONG    ( long.class    , Long.class      , 0b001000000 , 0b001001100 , 'J' ),
    SHORT   ( short.class   , Short.class     , 0b011001101 , 0b011001101 , 'S' ),
    BOOLEAN ( boolean.class , Boolean.class   , 0b100000000 , 0b100000000 , 'Z' );
    
    private static final Conversion[] VALUES = values();
    private final Class<?> primitive;
    private final Class<?> wrapper;
    private final int safe;
    private final int loss;
    private final char primDesc;
    
    /**
     * Creates a new {@code Conversion} with the provided parameters.
     * 
     * @param primitive the primitive type class
     * @param wrapper the wrapper type class
     * @param safe a bit-mask containing the conversions without precision-loss
     * @param loss a bit-mask containing all possible conversions
     * @param primDesc the char used as byte-descriptor for the primitive type
     */
    Conversion(final Class<?> primitive, final Class<?> wrapper, final int safe, final int loss, final char primDesc) {
        this.primitive = primitive;
        this.wrapper = wrapper;
        this.safe = safe;
        this.loss = loss;
        this.primDesc = primDesc;
    }
    
    /**
     * Returns the char that is used as byte-descriptor for the primitive type of the conversion.
     * 
     * @return the char used as byte-descriptor for the primitive type
     */
    public char primitiveByteDescriptor() {
        return primDesc;
    }
    
    /**
     * Returns whether {@code a} can be converted to {@code b} via a widening conversion as specified in {@code
     * https://docs.oracle.com/javase/specs/jls/se8/html/jls-5.html#jls-5.1}.
     * 
     * <p>For non-primitive/wrapper-types this is equal to
     * <blockquote><pre>
     * a.isAssignableFrom(b);</pre>
     * </blockquote>
     * 
     * @param  a the first type
     * @param  b the second type
     * @return {@code true} if {@code a} can be converted to {@code b} via a widening conversion, {@code false}
     *         otherwise
     */
    public static boolean convertibleTo(final Class<?> a, final Class<?> b) {
        return convertibleTo(a, b, false);
    }
    
    /**
     * Returns whether {@code a} can be converted to {@code b} via a widening conversion without the risk of a precision
     * loss as specified in {@code https://docs.oracle.com/javase/specs/jls/se8/html/jls-5.html#jls-5.1}.
     * 
     * <p>For non-primitive/wrapper-types this is equal to
     * <blockquote><pre>
     * a.isAssignableFrom(b);</pre>
     * </blockquote>
     * 
     * @param  a the first type
     * @param  b the second type
     * @return {@code true} if {@code a} can be converted to {@code b} via a widening conversion, {@code false}
     *         otherwise
     */
    public static boolean noPrecisionLoss(final Class<?> a, final Class<?> b) {
        return convertibleTo(a, b, true);
    }
    
    /**
     * Returns whether {@code a} is either equal to {@code b} or is the primitive or wrapper type of {@code b}.
     * 
     * @param  a the first type
     * @param  b the second type
     * @return {@code true} if {@code a} is either equal to {@code b} or is the primitive or wrapper type of {@code b},
     *         {@code false} otherwise
     */
    public static boolean wrapper(final Class<?> a, final Class<?> b) {
        final Conversion _b;
        return Objects.requireNonNull(a) == b
                || a.isPrimitive() ^ b.isPrimitive() && (_b = get(b)) != null && (a == _b.primitive || a == _b.wrapper);
    }
    
    private static boolean convertibleTo(final Class<?> a, final Class<?> b, final boolean precisionSafe) {
        final Conversion _a, _b;
        return Objects.requireNonNull(a) == b || a.isAssignableFrom(b)
                || (_b = get(b)) != null && ((_a = get(a)) == null ? a.isAssignableFrom(_b.wrapper)
                        : ((precisionSafe ? _b.safe : _b.loss) & 1 << _a.ordinal()) != 0);
    }
    
    public static Class<?> wrap(final Class<?> a) {
        return a.isPrimitive() ? get(a).wrapper : a;
    }
    
    public static Conversion getForPrimitive(final String n) {
        final Conversion c;
        return n.length() < 8 && n.length() > 2 && (c = getPrim(n)).primitive.getName().equals(n) ? c : null;
    }
    
    public static Conversion getForWrapper(final String n) {
        final Conversion c;
        return n.length() < 20 && n.length() > 12 && (c = getWrap(n)).wrapper.getName().equals(n) ? c : null;
    }
    
    public static Conversion get(final String n) {
        return n.length() > 12 ? getForWrapper(n) : getForPrimitive(n);
    }
    
    public static Conversion get(final Class<?> cl) {
        /*
         * Using the second and third character of each class-name as they are equal for each primitive<->wrapper; at
         * first I planned to use getSimpleName, but it creates a new substring on each call thus using this version.
         * This is most likely not even close to be the most efficient way but the easiest I found on the fly (in a few
         * tests) (and still way better than using a hash-map with all 18 classes).
         */
        final String n = cl.getName();
        final Conversion c;
        return cl.isPrimitive() ? getPrim(n) : n.length() > 12 && (c = getWrap(n)).wrapper.equals(cl) ? c : null;
    }
    
    private static Conversion getPrim(final String n) {
        return VALUES[(n.charAt(1) ^ n.charAt(2) * 21) % 9];
    }
    
    private static Conversion getWrap(final String n) {
        return VALUES[(n.charAt(11) ^ n.charAt(12) * 21) % 9];
    }
}
