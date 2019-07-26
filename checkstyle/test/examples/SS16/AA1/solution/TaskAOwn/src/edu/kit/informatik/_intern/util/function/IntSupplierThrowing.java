package edu.kit.informatik._intern.util.function;

/**
 * Represents a supplier of results. This is the {@code Exception}-throwing specialization of {@link
 * java.util.function.IntSupplier}.
 * 
 * <p>There is no requirement that a new or distinct result be returned each time the supplier is invoked.
 * 
 * <p>This is a {@linkplain FunctionalInterface functional interface} whose functional method is {@link #getAsInt()}.
 * 
 * @author  Tobias Bachert
 * @version 1.03, 2016/03/04
 * 
 * @param   <X> the type parameter of the exception
 * 
 * @since   1.8
 */
@FunctionalInterface
public interface IntSupplierThrowing<X extends Exception> {
    
    /**
     * Gets a result.
     * 
     * @return a result
     * @throws X if an exception of the generic type occurs
     */
    int getAsInt() throws X;
}
