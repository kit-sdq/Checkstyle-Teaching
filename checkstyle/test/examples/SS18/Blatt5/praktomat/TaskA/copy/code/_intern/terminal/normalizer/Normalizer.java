package edu.kit.informatik._intern.terminal.normalizer;

import java.util.Optional;
import java.util.function.UnaryOperator;

/**
 * Normalizer class for protocols.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/06/06
 */
public final class Normalizer {
    
    private final Type type;
    private final UnaryOperator<String> function;
    private final Optional<String> message;
    
    /**
     * Creates a new {@code Normalizer}.
     * 
     * @param type the type
     * @param function the function to use for normalizing
     * @param message the message
     */
    private Normalizer(final Type type, final UnaryOperator<String> function, final Optional<String> message) {
        this.type = type;
        this.function = function;
        this.message = message;
    }
    
    public static Normalizer of(final String type) {
        return of(Type.of(type));
    }
    
    /**
     * Returns a normalizer with the provided argument.
     * 
     * @param  type the type
     * @return an normalizer
     */
    public static Normalizer of(final Type type) {
        return of(type, null);
    }
    
    /**
     * Returns a normalizer with the provided arguments.
     * 
     * @param  type the type
     * @param  args a string representing possible required arguments
     * @return an normalizer
     */
    public static Normalizer of(final Type type, final String args) {
        return of(type, args, Optional.empty());
    }
    
    /**
     * Returns a normalizer with the provided arguments.
     * 
     * @param  type a string representing the type
     * @param  args a string representing possible required arguments
     * @param  msg a message for the normalizer, may be {@code empty}, a not-{@code empty} string means that the
     *         default-message is used
     * @return an normalizer
     */
    public static Normalizer of(final Type type, final String args, final Optional<String> msg) {
        return new Normalizer(type, type.function(args), msg.map((s) -> type.message()));
    }
    
    /**
     * Returns the message of the normalizers.
     * 
     * @return an {@code Optional} holding the message for the normalizer, or an empty {@code Optional} if the
     *         normalizer has no message.
     */
    public Optional<String> message() {
        return message;
    }
    
    /**
     * Returns the string normalized by the normalizer.
     * 
     * @param  string the string to normalize
     * @return the normalized string
     */
    public String normalize(final String string) {
        return function.apply(string);
    }
    
    @Override
    public String toString() {
        return "Normalizer[" + type + "]";
    }
}
