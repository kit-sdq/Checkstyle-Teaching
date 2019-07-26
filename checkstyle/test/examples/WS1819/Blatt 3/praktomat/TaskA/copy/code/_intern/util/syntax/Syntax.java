package edu.kit.informatik._intern.util.syntax;

import java.util.List;
import java.util.stream.Stream;

import edu.kit.informatik._intern.util.collections.CNode;
import edu.kit.informatik._intern.util.syntax.tokenizer.Token;
import edu.kit.informatik._intern.util.syntax.tokenizer.Tokenizer;

/**
 * A syntax used to tokenize and process a string.
 * 
 * @author  Tobias Bachert
 * @version 1.01, 2016/07/09
 */
public final class Syntax {
    
    private final Tokenizer<Rule> tokenizer;
    
    /**
     * Creates a new {@code Syntax} with the given rules.
     * 
     * @param  rules the rules
     * @throws IllegalStateException if the identifiers are not unique
     * @throws PatternSyntaxException if the syntax of a pattern of a rule is invalid
     */
    public Syntax(final Stream<Rule> rules) {
        tokenizer = new Tokenizer<>(rules);
    }
    
    /**
     * Processes the provided string with the syntax.
     * 
     * @param  s the string
     * @return the root of the syntax-tree
     * @throws TokenizerException if the tokenizer can not tokenize the string
     * @see    #process(String, int, int)
     */
    public CNode<Token<Rule>> process(final String s) {
        return process(s, 0, s.length());
    }
    
    /**
     * Processes the provided string starting at the specified position with the syntax.
     * 
     * @param  s the string
     * @param  start the start position, inclusive
     * @return the root of the syntax-tree
     * @throws TokenizerException if the tokenizer can not tokenize the string
     * @see    #process(String, int, int)
     */
    public CNode<Token<Rule>> process(final String s, final int start) {
        return process(s, start, s.length());
    }
    
    /**
     * Processes the provided string within the specified interval with the syntax.
     * 
     * @param  s the string
     * @param  start the start position, inclusive
     * @param  end the end position, exclusive
     * @return the root of the syntax-tree
     * @throws TokenizerException if the tokenizer can not tokenize the string
     */
    public CNode<Token<Rule>> process(final String s, final int start, final int end) {
        final List<Token<Rule>> tokens = tokenizer.tokenize(s, start, end);
        final CNode<Token<Rule>> root = CNode.root((t) -> t.module().computing());
        CNode<Token<Rule>> n = root;
        for (int i = 0, l = tokens.size(); i < l; i++) {
            final Token<Rule> t = tokens.get(i);
            n = t.module().updatefunction().apply(n, t);
        }
        return root;
    }
}
