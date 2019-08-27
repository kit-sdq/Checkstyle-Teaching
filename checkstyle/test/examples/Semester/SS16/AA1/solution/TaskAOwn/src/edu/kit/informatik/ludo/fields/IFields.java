package edu.kit.informatik.ludo.fields;

import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import edu.kit.informatik.ludo.rules.IRule;

/**
 * Interface for a field to field-content mapping used for a board.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/07/14
 */
public interface IFields {
    
    /**
     * Computes a new mapping for the provided field using the provided function.
     * 
     * @param field the field to compute a new mapping for
     * @param function the function
     */
    void compute(final Field field, final UnaryOperator<FieldContent> function);
    
    /**
     * Returns the field-content mapped to the provided field.
     * 
     * @param  field the field
     * @return the mapped field-content
     */
    FieldContent get(final Field field);
    
    /**
     * Returns a stream of all active rules.
     * 
     * @return a stream of all active rules
     */
    Stream<IRule> rules();
}
