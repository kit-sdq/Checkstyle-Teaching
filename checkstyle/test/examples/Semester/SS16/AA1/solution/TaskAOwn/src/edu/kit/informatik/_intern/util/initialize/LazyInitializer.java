package edu.kit.informatik._intern.util.initialize;

/**
 * Interface used for lazy initializations.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/03/27
 */
interface LazyInitializer {
    
    /**
     * Returns whether the initializer got executed and the attribute is initialized.
     * 
     * @return {@code true} if the initializer got executed and the value is initialized, {@code false} otherwise
     */
    boolean isInitialized();
}
