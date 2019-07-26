package edu.kit.informatik._intern.util.syntax.tokenizer;

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Module of a tokenizer.
 * 
 * @author  Tobias Bachert
 * @version 1.01, 2016/07/09
 * 
 * @param   <T> type parameter of the module information
 */
final class Module<T extends ModuleInformation> {
    
    private final T module;
    private final Pattern pattern;
    
    /**
     * Creates a new tokenizer-module.
     * 
     * @param  module the module-information used to build the module
     * @throws PatternSyntaxException if the syntax of the pattern is invalid
     */
    private Module(final T module) {
        this.module = module;
        pattern = Pattern.compile("^" + module.format());
    }
    
    /**
     * Returns a new tokenizer-module.
     * 
     * @param  <T> type parameter of the module information
     * @param  module the module-information used to build the module
     * @return a new tokenizer-module
     * @throws PatternSyntaxException if the syntax of the pattern is invalid
     */
    public static <T extends ModuleInformation> Module<T> of(final T t) {
        return new Module<>(t);
    }
    
    /**
     * Tries to match the provided string within the given range. If the match is successful, then the token of the
     * match will be consumed using the provided action.
     * 
     * @param  s the string to match
     * @param  start the start-position
     * @param  end the end-position
     * @param  action the consumer to perform
     * @return the end-position of the match, or {@code start-1} if the match was not successful
     * @throws NullPointerException if {@code s} is {@code null} or if the match was successful and {@code action} is
     *         {@code null}
     */
    public int match(final String s, final int start, final int end, final Consumer<? super Token<T>> action) {
        final Matcher m = pattern.matcher(s).useTransparentBounds(true).region(start, end);
        if (m.find()) {
            action.accept(new Token<>(module, m));
            return m.end();
        }
        return start - 1;
    }
    
    @Override
    public String toString() {
        return "Module[" + pattern + "]";
    }
}
