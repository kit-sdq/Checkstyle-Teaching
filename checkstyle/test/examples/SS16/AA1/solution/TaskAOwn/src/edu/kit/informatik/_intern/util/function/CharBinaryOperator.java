package edu.kit.informatik._intern.util.function;

/**
 * Represents an operation upon two {@code char}-valued operands and producing an {@code char}-valued result. This is
 * the primitive type specialization of {@link java.util.function.BinaryOperator} for {@code char}.
 * 
 * <p>This is a {@linkplain FunctionalInterface functional interface} whose functional method is {@link
 * #applyAsChar(char, char)}.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/04/02
 */
@FunctionalInterface
public interface CharBinaryOperator {
    
    /**
     * Applies this operator to the given operands.
     * 
     * @param  a the first operand
     * @param  b the second operand
     * @return the operator result
     */
    char applyAsChar(final char a, final char b);
}
