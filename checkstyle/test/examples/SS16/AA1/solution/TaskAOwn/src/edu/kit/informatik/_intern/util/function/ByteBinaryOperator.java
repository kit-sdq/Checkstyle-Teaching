package edu.kit.informatik._intern.util.function;

/**
 * Represents an operation upon two {@code byte}-valued operands and producing an {@code byte}-valued result. This is
 * the primitive type specialization of {@link java.util.function.BinaryOperator} for {@code byte}.
 * 
 * <p>This is a {@linkplain FunctionalInterface functional interface} whose functional method is {@link
 * #applyAsByte(byte, byte)}.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/04/02
 */
@FunctionalInterface
public interface ByteBinaryOperator {
    
    /**
     * Applies this operator to the given operands.
     * 
     * @param  a the first operand
     * @param  b the second operand
     * @return the operator result
     */
    byte applyAsByte(final byte a, final byte b);
}
