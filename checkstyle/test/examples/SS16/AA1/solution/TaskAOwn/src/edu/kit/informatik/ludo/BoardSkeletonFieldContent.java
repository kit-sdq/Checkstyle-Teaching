package edu.kit.informatik.ludo;

import java.util.function.UnaryOperator;

import edu.kit.informatik.ludo.fields.Field;
import edu.kit.informatik.ludo.fields.FieldContent;
import edu.kit.informatik.ludo.fields.Move;
import edu.kit.informatik.ludo.fields.Player;

/**
 * More specific skeleton implementation of the board interface that uses field-contents mapped to fields to lower the
 * effort required when implementing it.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/07/14
 */
abstract class BoardSkeletonFieldContent extends BoardSkeleton {
    
    @Override protected final void move0(final Move move) {
        compute(move.from(), FieldContent::remove);
        compute(move.to(), (fc) -> {
            final FieldContent current;
            if (fc.isOwnedByOther(player())) {
                compute(Field.base(fc.owner), (content) -> content.add(fc.owner, fc.count));
                current = FieldContent.empty();
            } else {
                current = fc;
            }
            return current.add(player());
        });
    }
    
    /**
     * Computes a new mapping for the provided field using the provided function.
     * 
     * @param field the field to compute a new mapping for
     * @param function the function
     */
    protected abstract void compute(final Field field, final UnaryOperator<FieldContent> function);
    
    @Override protected final Player owner0(final Field field) {
        return get(field).owner;
    }
    
    @Override protected final int count0(final Field field) {
        return get(field).count;
    }
    
    /**
     * Returns the field-content mapped to the provided field.
     * 
     * @param  field the field
     * @return the mapped field-content
     */
    protected abstract FieldContent get(final Field field);
}
