package edu.kit.informatik._intern.util.invoke;

/**
 * Unchecked version of {@link ReflectiveOperationException}.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/06/19
 */
public final class UncheckedReflectiveOperationException extends RuntimeException {
    
    private static final long serialVersionUID = -9213892038104458732L;
    
    /**
     * Creates a new {@code UncheckedReflectiveOperationException} with the provided exception as cause.
     * 
     * @param cause the cause
     */
    public UncheckedReflectiveOperationException(final ReflectiveOperationException cause) {
        super(cause);
    }
}
