package edu.kit.informatik._intern.util.function;

/**
 * Represents an operation that accepts no input arguments and returns no result. Unlike most other functional
 * interfaces, {@code Action} is expected to operate via side-effects.
 * 
 * <p>This is a {@linkplain FunctionalInterface functional interface} whose functional method is {@link #perform()}.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/04/02
 * 
 * @since   1.8
 */
@FunctionalInterface
public interface Action {
    
    /**
     * Performs this operation.
     */
    void perform();
}
