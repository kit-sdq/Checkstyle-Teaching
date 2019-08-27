package edu.kit.informatik._intern.util.invoke;

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
    private final Convertible conv = new PrimitiveVersion();
    private final Class<?> prim;
    private final Class<?> wrap;
    private final int safe;
    private final int loss;
    private final char desc;
    
    /**
     * Creates a new {@code Conversion} with the provided parameters.
     * 
     * @param prim the primitive type class
     * @param wrap the wrapper type class
     * @param safe a bit-mask containing the conversions without precision-loss
     * @param loss a bit-mask containing all possible conversions
     * @param desc the char used as byte-descriptor for the primitive type
     */
    Conversion(final Class<?> prim, final Class<?> wrap, final int safe, final int loss, final char desc) {
        this.prim = prim;
        this.wrap = wrap;
        this.safe = safe;
        this.loss = loss;
        this.desc = desc;
    }
    
    public static Convertible get(final Class<?> clazz) {
        final Conversion c = Conversion.getByClass(clazz);
        return c == null ? new ObjectVersion(clazz) : c.get();
    }
    
    public Convertible get() {
        return conv;
    }
    
    /**
     * Returns the char that is used as byte-descriptor for the primitive type of the conversion.
     * 
     * @return the char used as byte-descriptor for the primitive type
     */
    public char primitiveByteDescriptor() {
        return desc;
    }
    
    /**
     * Returns whether {@code left} is either equal to {@code right} or is the primitive or wrapper type of {@code
     * right}.
     * 
     * @param  left the first type
     * @param  right the second type
     * @return {@code true} if {@code left} is either equal to {@code right} or is the primitive or wrapper type of
     *         {@code right}, {@code false} otherwise
     */
    public static boolean same(final Class<?> left, final Class<?> right) {
        final Conversion _right;
        return left.isPrimitive() == right.isPrimitive() ? left == right
                : (_right = getByClass(right)) != null && (left == _right.prim || left == _right.wrap);
    }
    
    /**
     * Returns the wrapper class for a primitive type, or {@code clazz} if the class object does not represent a
     * primitive type.
     * 
     * @param  clazz the class to wrap
     * @return the wrapper for the class
     */
    public static Class<?> wrap(final Class<?> clazz) {
        return clazz.isPrimitive() ? getByClass(clazz).wrap : clazz;
    }
    
    /**
     * Returns the conversion for the primitive name {@code n}.
     * 
     * @param  n the name of the primitive class
     * @return the conversion
     */
    public static Conversion getByPrimitiveName(final String n) {
        final Conversion c;
        return n.length() < 8 && n.length() > 2 && (c = get(n, 0)).prim.getName().equals(n) ? c : null;
    }
    
    private static Conversion getByClass(final Class<?> cl) {
        final String n = cl.getName();
        final Conversion c;
        return cl.isPrimitive() ? get(n, 0) : n.length() > 12 && (c = get(n, 10)).wrap == cl ? c : null;
    }
    
    private static Conversion get(final String n, final int offset) {
        return VALUES[(n.charAt(1 + offset) ^ n.charAt(2 + offset) * 21) % 9];
    }
    
    public enum Precision {
        SAFE { @Override int forConversion(final Conversion conversion) { return conversion.safe; } },
        LOSS { @Override int forConversion(final Conversion conversion) { return conversion.loss; } };
        
        abstract int forConversion(final Conversion conversion);
    }
    
    public interface Convertible {
        
        /**
         * Returns whether {@code this} is assignable from {@code other}.<br>
         * Contrary to the {@link Class#isAssignableFrom(Class)} method, this returns true for wrapper-classes, thus
         * objects have to be converted properly prior assigning them.
         * 
         * @param  other the class to check for
         * @param  precision the precision to use
         * @return {@code true} if {@code this} is assignable from {@code other}, {@code false} otherwise
         */
        boolean assignableFrom(final Class<?> other, final Precision precision);
    }
    
    private final class PrimitiveVersion implements Convertible {
        
        @Override public boolean assignableFrom(final Class<?> other, final Precision prec) {
            final Conversion _o;
            return other.isPrimitive()
                    ? other == prim || (_o = getByClass(other)) != null && (wrap.isAssignableFrom(_o.wrap)
                            || (prec.forConversion(_o) & 1 << ordinal()) != 0)
                    : wrap.isAssignableFrom(other) || (_o = getByClass(other)) != null
                            && (prec.forConversion(_o) & 1 << ordinal()) != 0;
        }
    }
    
    private static final class ObjectVersion implements Convertible {
        
        private final Class<?> createdFor;
        
        private ObjectVersion(final Class<?> clazz) {
            createdFor = clazz;
        }
        
        @Override public boolean assignableFrom(final Class<?> other, final Precision precision) {
            return createdFor.isAssignableFrom(other.isPrimitive() ? wrap(other) : other);
        }
    }
}
