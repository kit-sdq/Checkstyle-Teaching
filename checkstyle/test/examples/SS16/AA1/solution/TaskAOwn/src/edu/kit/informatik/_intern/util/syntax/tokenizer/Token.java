package edu.kit.informatik._intern.util.syntax.tokenizer;

import java.util.Objects;
import java.util.regex.MatchResult;

import edu.kit.informatik._intern.util.StringUtil;

/**
 * Token of a tokenizer.
 * 
 * @author  Tobias Bachert
 * @version 1.01, 2016/07/09
 * 
 * @param   <T> type parameter of the module information
 */
public final class Token<T extends ModuleInformation> {
    
    private final T module;
    private final MatchResult result;
    
    /**
     * Creates a new {@code Token} with the provided arguments.
     * 
     * @param module the module-information
     * @param result the match-result
     */
    Token(final T module, final MatchResult result) {
        this.module = Objects.requireNonNull(module);
        this.result = Objects.requireNonNull(result);
    }
    
    /**
     * Returns the content of the {@code n'th} capture group of the match-result.
     * 
     * @param  group the index of the capture group to return
     * @return the content of the capture group with index {@code group}
     */
    public String group(final int group) {
        return result.group(group);
    }
    
    /**
     * Returns the module-information of the token.
     * 
     * @return the module-information of the token
     */
    public T module() {
        return module;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder().append('\'');
        result.group().chars().map(StringUtil::controlCharacterToSymbol).forEachOrdered(sb::appendCodePoint);
        return sb.append("'(").append(module).append(',').append(result.start()).append('-').append(result.end())
                .append(")").toString();
    }
}
