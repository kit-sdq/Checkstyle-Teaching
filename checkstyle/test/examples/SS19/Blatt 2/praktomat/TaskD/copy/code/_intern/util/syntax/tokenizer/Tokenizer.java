package edu.kit.informatik._intern.util.syntax.tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Tokenizer used to tokenize strings.
 * 
 * @author  Tobias Bachert
 * @version 1.01, 2016/07/09
 * 
 * @param   <T> type of the module information
 */
public final class Tokenizer<T extends ModuleInformation> {
    
    private final List<Module<T>> mdls;
    
    /**
     * Creates a new {@code Tokenizer} with modules build from the provided module information.
     * 
     * @param  modules a stream of module-information to build the modules from
     * @throws PatternSyntaxException if the syntax of a pattern is invalid
     */
    public Tokenizer(final Stream<T> modules) {
        mdls = modules.map(Module::of).collect(Collectors.toCollection(ArrayList::new));
    }
    
    /**
     * Tokenizes the provided string using the assigned modules.
     * 
     * @param  string the string to tokenize
     * @return a list containing the created tokens
     * @throws NullPointerException if {@code string} is {@code null}
     * @throws TokenizerException if the tokenizer can not tokenize the string
     */
    public List<Token<T>> tokenize(final String string) {
        return tokenize(string, 0, string.length());
    }
    
    /**
     * Tokenizes the provided string within the specified interval using the assigned modules.
     * 
     * <p>If {@code start} is greater than {@code end}, then an empty list will be returned.
     * 
     * @param  string the string to tokenize
     * @param  start the start position, inclusive
     * @param  end the end position, exclusive
     * @return a list containing the created tokens
     * @throws NullPointerException if {@code string} is {@code null}
     * @throws TokenizerException if the tokenizer can not tokenize the string
     */
    public List<Token<T>> tokenize(final String string, final int start, final int end) {
        final List<Token<T>> t = new ArrayList<>();
        final int l = Math.min(string.length(), end);
        int pos = Math.max(0, start);
        outer:
        while (pos < l) {
            for (int i = 0, m = mdls.size(); i < m; i++) {
                final int npos = mdls.get(i).match(string, pos, l, t::add);
                if (npos >= pos) {
                    if (npos == pos) {
                        throw TokenizerException.zeroWidthMatch(this, mdls.get(i), string, pos);
                    }
                    pos = npos;
                    continue outer;
                }
            }
            throw TokenizerException.noUsableModule(this, string, pos);
        }
        return t;
    }
    
    @Override public String toString() {
        return "Tokenizer[" + mdls + "]";
    }
}
