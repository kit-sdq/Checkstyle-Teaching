package edu.kit.informatik._intern.terminal.analyzer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import edu.kit.informatik._intern.terminal.normalizer.Normalizer;

/**
 * Analyzer class for protocols.
 * 
 * @author  Tobias Bachert
 * @version 1.02, 2016/06/22
 */
public final class Analyzer {
    
    private final Type type;
    private final BiFunction<List<Line>, List<Line>, Optional<String>> function;
    private final Optional<String> message;
    private final Collection<Normalizer> normalizer;
    
    /**
     * Creates a new {@code Analyzer}.
     * 
     * @param type the type
     * @param function the function to use for analyzing
     * @param message the message
     */
    private Analyzer(final Type type, final BiFunction<List<Line>, List<Line>, Optional<String>> function,
            final Optional<String> message) {
        this(type, function, message, Collections.emptyList());
    }
    
    /**
     * Creates a new {@code Analyzer}.
     * 
     * @param type the type
     * @param function the function to use for analyzing
     * @param message the message
     * @param normalizer the normalizer to apply
     */
    private Analyzer(final Type type, final BiFunction<List<Line>, List<Line>, Optional<String>> function,
            final Optional<String> message, final Collection<Normalizer> normalizer) {
        this.type = type;
        this.function = function;
        this.message = message;
        this.normalizer = normalizer;
    }
    
    public static Analyzer of(final String type) {
        return of(Type.of(type));
    }
    
    /**
     * Returns an analyzer with the provided argument.
     * 
     * @param  type the type
     * @return an analyzer
     */
    public static Analyzer of(final Type type) {
        return of(type, (String) null);
    }
    
    /**
     * Returns an analyzer with the provided arguments.
     * 
     * @param  type the type
     * @param  args a string representing possible required arguments
     * @return an analyzer
     */
    public static Analyzer of(final Type type, final String args) {
        return of(type, args, Optional.empty());
    }
    
    /**
     * Returns an analyzer with the provided arguments.
     * 
     * @param  type the type
     * @param  args a string representing possible required arguments
     * @param  msg a message for the analyzer, may be {@code empty}, an empty string means that the default-message is
     *         used
     * @return an analyzer
     */
    public static Analyzer of(final Type type, final String args, final Optional<String> msg) {
        return new Analyzer(type, type.function(args), msg.map((s) -> type.message() + (s.isEmpty() ? "" : " " + s)));
    }
    
    /**
     * Analyzes the given expected output with the actual output.
     * 
     * @param  output the expected output
     * @param  actual the actual output
     * @return an empty {@code Optional} if the analyzation returned that {@code output} and {@code actual} are
     *         matching, or an {@code Optional} holding the error-message containing the reason for the failure
     */
    public Optional<String> analyze(final Stream<? extends CharSequence> output,
            final Stream<? extends CharSequence> actual) {
        return function.apply(
                output.map(CharSequence::toString).map((s) -> new Line(s, normalized(s)))
                        .collect(Collectors.toCollection(ArrayList::new)),
                actual.map(CharSequence::toString).map((s) -> new Line(s, normalized(s)))
                        .collect(Collectors.toCollection(ArrayList::new))
        );
    }
    
    /**
     * Returns the message of the analyzer.
     * 
     * @return an {@code Optional} holding the message for the analyzer, or an empty {@code Optional} if the analyzer
     *         has no message.
     */
    public Optional<String> message() {
        return message;
    }
    
    /**
     * Returns a normalized version of the analyzer.
     * 
     * @param  toAdd the normalizer to apply
     * @return a normalized version of the analyzer
     */
    public Analyzer normalized(final Normalizer... toAdd) {
        if (toAdd.length == 0) {
            return this;
        }
        final Collection<Normalizer> normalized = new ArrayDeque<>(normalizer);
        for (final Normalizer n : toAdd)
            normalized.add(Objects.requireNonNull(n));
        return new Analyzer(type, function, message, normalized);
    }
    
    /**
     * Returns a string normalized with all assigned normalizer.
     * 
     * @param  string the string to normalize
     * @return {@code string} normalized
     */
    private String normalized(final String string) {
        String result = string;
        if (!normalizer.isEmpty())
            for (final Normalizer n : normalizer)
                result = n.normalize(result);
        return result;
    }
    
    @Override
    public String toString() {
        return "Analyzer[" + type + normalizer + "]";
    }
}
