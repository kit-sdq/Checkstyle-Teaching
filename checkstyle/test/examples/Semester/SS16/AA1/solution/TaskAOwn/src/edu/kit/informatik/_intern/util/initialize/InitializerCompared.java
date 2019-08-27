package edu.kit.informatik._intern.util.initialize;

/**
 * Represents the comparison of initializer.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/03/27
 */
public enum InitializerCompared {
    /** Both initialized and equals value. */
    EQUALS,
    /** Both initialized and not equals value. */
    NON_EQUALS,
    /** At least one not initialized . */
    UNKOWN;
}
