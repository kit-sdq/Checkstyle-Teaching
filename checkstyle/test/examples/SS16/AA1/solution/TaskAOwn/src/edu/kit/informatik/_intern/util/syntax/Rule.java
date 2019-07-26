package edu.kit.informatik._intern.util.syntax;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import edu.kit.informatik._intern.util.collections.CNode;
import edu.kit.informatik._intern.util.syntax.tokenizer.ModuleInformation;
import edu.kit.informatik._intern.util.syntax.tokenizer.Token;

/**
 * Rule used for a Syntax.
 * 
 * @author  Tobias Bachert
 * @version 1.01, 2016/07/09
 */
public final class Rule implements ModuleInformation {
    
    private final Object identifier;
    private final String pattern;
    private final BiFunction<CNode<Token<Rule>>, Stream<Object>, Object> computing;
    private final BiFunction<CNode<Token<Rule>>, Token<Rule>, CNode<Token<Rule>>> position;
    
    /**
     * Creates a new {@code Rule} with the provided parameters.
     * 
     * @param identifier the identifier of the rule
     * @param pattern a string used to create the pattern of the rule
     * @param computing the computing-function of the rule
     * @param position the updating-function of the rule
     */
    public Rule(final Object identifier, final String pattern,
            final BiFunction<CNode<Token<Rule>>, Stream<Object>, Object> computing,
            final BiFunction<CNode<Token<Rule>>, Token<Rule>, CNode<Token<Rule>>> position) {
        this.identifier = Objects.requireNonNull(identifier);
        this.pattern    = Objects.requireNonNull(pattern);
        this.computing  = Objects.requireNonNull(computing);
        this.position   = Objects.requireNonNull(position);
    }
    
    /**
     * Returns the identifier of the rule.
     * 
     * @return the identifier
     */
    public Object id() {
        return identifier;
    }
    
    /**
     * Returns the computing-function of the rule.
     * 
     * @return the computing-function
     */
    BiFunction<CNode<Token<Rule>>, Stream<Object>, Object> computing() {
        return computing;
    }
    
    /**
     * Returns the updating-function of the rule.
     * 
     * @return the updating-function
     */
    BiFunction<CNode<Token<Rule>>, Token<Rule>, CNode<Token<Rule>>> updatefunction() {
        return position;
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Rule && identifier.equals(((Rule) obj).identifier);
    }
    
    @Override
    public int hashCode() {
        return identifier.hashCode();
    }
    
    @Override
    public String format() {
        return pattern;
    }
    
    @Override
    public String toString() {
        return "Rule[" + identifier + "," + pattern + "]";
    }
}
