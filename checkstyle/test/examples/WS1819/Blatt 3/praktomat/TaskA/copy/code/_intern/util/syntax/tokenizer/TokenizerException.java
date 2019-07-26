package edu.kit.informatik._intern.util.syntax.tokenizer;

import java.util.Collections;

import edu.kit.informatik._intern.util.StringUtil;

/**
 * Exception used to indicate that a tokenizer can not process a provided string.
 * 
 * @author  Tobias Bachert
 * @version 1.01, 2016/07/09
 */
public final class TokenizerException extends RuntimeException {
    
    private static final long serialVersionUID = -1502193108634314984L;
    
    /**
     * Creates a new {@code TokenizerException} with the provided parameter.
     * 
     * @param reason the reason
     * @param t the tokenizer
     * @param s the processed string
     * @param pos the position
     */
    private TokenizerException(final String reason, final Tokenizer<?> t, final String s, final int pos) {
        super("exception while tokenizing string"
                + System.lineSeparator() + s.chars().map(StringUtil::controlCharacterToSymbol)
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                + System.lineSeparator() + String.join("", Collections.nCopies(pos, " ")) + "^ (" + reason + ")"
                + System.lineSeparator() + t);
    }
    
    /**
     * Creates a new {@code TokenizerException} with the provided parameter and no usable module as reason.
     * 
     * @param t the tokenizer
     * @param s the processed string
     * @param pos the position
     */
    static TokenizerException noUsableModule(final Tokenizer<?> t, final String s, final int pos) {
        return new TokenizerException("no usable module", t, s, pos);
    }
    
    /**
     * Creates a new {@code TokenizerException} with the provided parameter and a zero width match as reason.
     * 
     * @param <T> type parameter of the module information
     * @param t the tokenizer
     * @param m the module
     * @param s the processed string
     * @param pos the position
     */
    static <T extends ModuleInformation> TokenizerException zeroWidthMatch(final Tokenizer<T> t, final Module<T> m,
            final String s, final int pos) {
        return new TokenizerException("zero width match for " + m, t, s, pos);
    }
}
