package edu.kit.informatik.ludo;

import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import edu.kit.informatik.ludo.fields.Field;
import edu.kit.informatik.ludo.fields.FieldContent;
import edu.kit.informatik.ludo.fields.FieldsArray;
import edu.kit.informatik.ludo.fields.IFields;
import edu.kit.informatik.ludo.rules.IRule;

/**
 * Board implementation.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/07/14
 */
public final class Board extends BoardSkeletonFieldContent {
    
    private IFields fields;
    
    @Override protected Stream<IRule> rules() {
        return fields.rules();
    }
    
    @Override protected void start0(final Stream<? extends IRule> rules,
            final Stream<? extends Stream<? extends Field>> positions) {
        fields = new FieldsArray(rules, positions);
    }
    
    @Override protected void abort0() {
        fields = null;
    }
    
    @Override protected void compute(final Field field, final UnaryOperator<FieldContent> function) {
        fields.compute(field, function);
    }
    
    @Override protected FieldContent get(final Field field) {
        return fields.get(field);
    }
    
    @Override public String toString() {
        return Field.toString(fields != null ? fields::get : (f) -> FieldContent.empty());
    }
}
