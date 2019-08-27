package edu.kit.informatik._intern.util.function;

/**
 * Represents an operation upon two {@code float}-valued operands and producing an {@code float}-valued result. This is
 * the primitive type specialization of {@link java.util.function.BinaryOperator} for {@code float}.
 * 
 * <p>This is a {@linkplain FunctionalInterface functional interface} whose functional method is {@link
 * #applyAsFloat(float, float)}.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/04/02
 */
@FunctionalInterface
public interface FloatBinaryOperator {
    
    /**
     * Applies this operator to the given operands.
     * 
     * @param  a the first operand
     * @param  b the second operand
     * @return the operator result
     */
    float applyAsFloat(final float a, final float b);
}
