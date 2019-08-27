package edu.kit.informatik.ludo.fields;

import java.util.Arrays;
import java.util.stream.Stream;

import edu.kit.informatik._intern.util.ObjectUtil;
import edu.kit.informatik.ludo.rules.IRule;

/**
 * Basic implementation of the {@code IFields} interface.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/07/15
 *
 */
abstract class Fields implements IFields {
    
    private final IRule[] rules;
    
    /**
     * Creates a new {@code Fields} using the provided stream.
     * 
     * @param  rules a stream containing the rules
     */
    protected Fields(final Stream<? extends IRule> rules) {
        this.rules = rules.unordered().distinct().peek(ObjectUtil::requireNonNull).toArray(IRule[]::new);
    }

    @Override public final Stream<IRule> rules() {
        return Arrays.stream(rules);
    }
}
