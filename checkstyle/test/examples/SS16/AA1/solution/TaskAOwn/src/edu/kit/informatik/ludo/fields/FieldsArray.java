package edu.kit.informatik.ludo.fields;

import java.util.Arrays;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import edu.kit.informatik._intern.util.ArrayUtil;
import edu.kit.informatik.ludo.rules.IRule;

/**
 * Field to field-content mapping.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/07/14
 */
public final class FieldsArray extends Fields {
    
    private final FieldContent[] map = Field.stream().map((f) -> FieldContent.empty()).toArray(FieldContent[]::new);
    
    /**
     * Creates a new {@code FieldsArray} using the provided streams for creation.
     * 
     * @param  rules a stream containing the rules
     * @param  positions a stream containing the positions
     * @throws IllegalArgumentException if positions is not empty and contains not exactly {@code 4*4} positions
     * @throws IllegalStateException if any position is not usable by the specific player
     */
    public FieldsArray(final Stream<? extends IRule> rules, final Stream<? extends Stream<? extends Field>> positions) {
        super(rules);
        
        final Field[][] fields = positions.map((s) -> s.toArray(Field[]::new)).toArray(Field[][]::new);
        if (fields.length == 0) {
            IntStream.range(0, 4).forEach((i) -> compute0(i, (fc) -> fc.add(Player.byIndex(i), 4)));
        } else if (fields.length == 4 && ArrayUtil.allMatch(fields, (a) -> a.length == 4 && ArrayUtil.ascending(a))) {
            IntStream.range(0, 4).forEach((i) -> Arrays.stream(fields[i]).map(Field::ordinal)
                    .forEach((o) -> compute0(o, (fc) -> fc.add(Player.byIndex(i)))));
        } else {
            throw new IllegalArgumentException("invalid positions");
        }
    }
    
    @Override public void compute(final Field field, final UnaryOperator<FieldContent> function) {
        compute0(field.ordinal(), function);
    }
    
    @Override public FieldContent get(final Field field) {
        return map[field.ordinal()];
    }
    
    private void compute0(final int o, final UnaryOperator<FieldContent> function) {
        map[o] = function.apply(map[o]);
    }
}
