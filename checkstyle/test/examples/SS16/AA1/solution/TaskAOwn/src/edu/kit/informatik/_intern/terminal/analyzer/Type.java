package edu.kit.informatik._intern.terminal.analyzer;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Enum holding the different analyzer types.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/06/06
 */
public enum Type {
    /** Respects order of lines. \u000A @author unascribed @author Tobias Bachert */
    RESPECTORDER("Respecting line order") {
        
        @Override public BiFunction<List<Line>, List<Line>, Optional<String>> function(final String args) {
            return (output, actual) -> {
                final StringJoiner sj = new StringJoiner(System.lineSeparator());
                for (int i = 0, max = Math.max(output.size(), actual.size()); i < max; i++) {
                    if (i >= output.size()) {
                        sj.add("Line " + (i + 1) + " is unexpected: '" + actual.get(i) + "'");
                    } else if (i >= actual.size()) {
                        sj.add("Line " + (i + 1) + " is missing: '" + output.get(i) + "'");
                    } else if (!output.get(i).is(String::equals, actual.get(i))) {
                        sj.add("Line " + (i + 1) + ": expected '" + output.get(i)
                                                 + "', but got '" + actual.get(i) + "'"
                                + (output.get(i).is(String::equalsIgnoreCase, actual.get(i)) ? " (wrong case)" : ""));
                    }
                }
                return Optional.of(sj).map(StringJoiner::toString).filter((s) -> !s.isEmpty());
            };
        }
    },
    /** Ignores order of lines. \u000A @author unascribed @author Tobias Bachert */
    IGNOREORDER("Arbitrary order of lines allowed from here on.") {
        
        @Override public BiFunction<List<Line>, List<Line>, Optional<String>> function(final String args) {
            return (output, actual) -> {
                output.sort(Comparator.comparing(Line::normalized));
                actual.sort(Comparator.comparing(Line::normalized));
                
                final StringJoiner sj = new StringJoiner(System.lineSeparator());
                final int lengthoutput = output.size();
                final int lengthactual = actual.size();
                int posoutput = 0;
                int posactual = 0;
                while (posoutput < lengthoutput && posactual < lengthactual) {
                    final Line croutput = output.get(posoutput);
                    final Line cractual = actual.get(posactual);
                    
                    if (croutput.is(String::equals, cractual)) {
                        posoutput++;
                        posactual++;
                    } else if (croutput.is(String::equalsIgnoreCase, cractual)) {
                        sj.add("Detected wrong case. Excepted '" + croutput + "', but got '" + cractual + "'");
                        posoutput++;
                        posactual++;
                    } else if (croutput.normalized().compareTo(cractual.normalized()) < 0) {
                        sj.add("Missing line: '" + croutput + "'");
                        posoutput++;
                    } else {
                        sj.add("Unexpected line: '" + cractual + "'");
                        posactual++;
                    }
                }
                
                for (; posoutput < lengthoutput; sj.add("Missing line: '"    + output.get(posoutput++) + "'")) { }
                for (; posactual < lengthactual; sj.add("Unexpected line: '" + actual.get(posactual++) + "'")) { }
                
                return Optional.of(sj).map(StringJoiner::toString).filter((s) -> !s.isEmpty());
            };
        }
    },
    /** Expects an error-message containing specific substrings. \u000A @author Tobias Bachert */
    ERROR("Expecting an error-message") {
        
        @Override public BiFunction<List<Line>, List<Line>, Optional<String>> function(final String args) {
            return (output, actual) -> {
                if (actual.isEmpty() || !actual.get(0).normalized().toLowerCase(Locale.ENGLISH).startsWith("error"))
                    return Optional.of("Expected error-message.");
                
                final StringJoiner sj = new StringJoiner(", ");
                if (!output.isEmpty()) {
                    final String actualMsg = actual.stream().map(Line::normalized)
                            .collect(Collectors.joining(System.lineSeparator()));
                    for (final Line sub : output) {
                        final String normalized = sub.normalized();
                        if (!actualMsg.contains(normalized)) {
                            sj.add(normalized);
                        }
                    }
                }
                return Optional.of(sj).map(StringJoiner::toString).filter((s) -> !s.isEmpty())
                        .map((s) -> "Expected in message, but not present: " + s);
            };
        }
    };
    
    private static final Type[] TYPES = values();
    private final String message;
    
    /**
     * Creates a new {@code Type} with the given string as standard message.
     * 
     * @param message the standard message
     */
    Type(final String message) {
        this.message = message;
    }
    
    /**
     * Returns the type represented by the given string.
     * 
     * <p>If the string does not match a type, then an {@code IllegalArgumentException} is thrown.
     * 
     * @param  type the string
     * @return the type represented by {@code type}
     */
    public static Type of(final String type) {
        for (final Type t : TYPES) {
            if (t.name().equalsIgnoreCase(type)) {
                return t;
            }
        }
        throw new IllegalArgumentException("no such analyzer " + type);
    }
    
    /**
     * Returns the standard message of the type.
     * 
     * @return the standard message
     */
    public String message() {
        return message;
    }
    
    /**
     * Returns the {@code BiFunction} of the type used to analyze expected with actual output.
     * 
     * @param  args a string containing possible arguments
     * @return the {@code BiFunction} of the type used to analyze
     */
    public abstract BiFunction<List<Line>, List<Line>, Optional<String>> function(final String args);
}
