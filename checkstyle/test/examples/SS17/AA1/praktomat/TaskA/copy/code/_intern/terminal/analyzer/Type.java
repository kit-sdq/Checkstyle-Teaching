package edu.kit.informatik._intern.terminal.analyzer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
                    } else if (!output.get(i).is(String::equals, new Line("#empty", "#empty")) && i >= actual.size()) {
                        sj.add("Line " + (i + 1) + " is missing: '" + output.get(i) + "'");
                    } else if (output.get(i).is(String::equals, new Line("#empty", "#empty")) && i >= actual.size()) {
                        sj.add("Line " + (i + 1) + " is expected to be empty but is missing");
                    } else if (output.get(i).is(String::equals, new Line("#empty", "#empty"))
                            && !actual.get(i).is(String::equals, new Line("", ""))) {
                        sj.add("Line " + (i + 1) + " is expected to be empty but isn't");
                    } else if (!output.get(i).is(String::equals, new Line("#empty", "#empty"))
                            && !output.get(i).is(String::equals, actual.get(i))) {
                        sj.add("Line " + (i + 1) + ": expected '" + output.get(i)
                                                 + "', but got '" + actual.get(i) + "'"
                                + (output.get(i).is(String::equalsIgnoreCase, actual.get(i)) ? " (wrong case)" : ""));
                    }
                }

                return Optional.of(sj).map(StringJoiner::toString).filter((s) -> !s.isEmpty());
            };
        }
    },
    RESPECTORDERSTARTFIELD("Respecting line order but allows arbitrary order in the first field") {

        @Override public BiFunction<List<Line>, List<Line>, Optional<String>> function(final String args) {
            return (output, actual) -> {
                final StringJoiner sj = new StringJoiner(System.lineSeparator());
                for (int i = 0, max = Math.max(output.size(), actual.size()); i < max; i++) {
                    if (i >= output.size()) {
                        sj.add("Line " + (i + 1) + " is unexpected: '" + actual.get(i) + "'");
                    } else if (!output.get(i).is(String::equals, new Line("#empty", "#empty")) && i >= actual.size()) {
                        sj.add("Line " + (i + 1) + " is missing: '" + output.get(i) + "'");
                    } else if (output.get(i).is(String::equals, new Line("#empty", "#empty")) && i >= actual.size()) {
                        sj.add("Line " + (i + 1) + " is expected to be empty but is missing");
                    } else if (output.get(i).is(String::equals, new Line("#empty", "#empty"))
                            && !actual.get(i).is(String::equals, new Line("", ""))) {
                        sj.add("Line " + (i + 1) + " is expected to be empty but isn't");
                    } else if (!output.get(i).is(String::equals, new Line("#empty", "#empty"))
                            && !output.get(i).is(String::equals, actual.get(i))) {
                        Matcher check = Pattern.compile("\\{(\\d+,)*\\d+\\}.*").matcher(actual.get(i).toString());
                        if (check.matches()) {
                            Matcher out = Pattern.compile("\\{(.*)\\}.*").matcher(output.get(i).toString());
                            Matcher act = Pattern.compile("\\{(.*)\\}.*").matcher(actual.get(i).toString());
                            if (out.matches() && act.matches()) {
                                List<Integer> numbersOutput = new ArrayList<>();
                                List<Integer> numbersActual = new ArrayList<>();
                                for (String current : out.group(1).split(",")) {
                                    numbersOutput.add(Integer.parseInt(current));
                                }
                                for (String current : act.group(1).split(",")) {
                                    numbersActual.add(Integer.parseInt(current));
                                }
                                Collections.sort(numbersOutput);
                                Collections.sort(numbersActual);
                                if (numbersOutput.equals(numbersActual) 
                                        && output.get(i).toString().split("}")[1].equals(actual.get(i).toString().split("}")[1])) {
                                    continue;
                                }
                            }
                        }
                        
                        if (output.get(i).toString().contains("}")) {
                            sj.add("Line " + (i + 1) + ": expected '" + output.get(i)
                                    + "', but got '" + actual.get(i) + "'"
                                    + (output.get(i).is(String::equalsIgnoreCase, actual.get(i)) ? " (wrong case)" : "")
                                    + " (numbers in curly brackets can be arbitrarily ordered)");
                        } else {
                            sj.add("Line " + (i + 1) + ": expected '" + output.get(i)
                                    + "', but got '" + actual.get(i) + "'"
                                    + (output.get(i).is(String::equalsIgnoreCase, actual.get(i)) ? " (wrong case)" : ""));
                        }
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

    FIRSTLASTFIXED("First and last line needs to be exactly there, the rest can be arbitrary.") {
        @Override
        public BiFunction<List<Line>, List<Line>, Optional<String>> function(String args) {
            return (output, actual) -> {
                Line firstLineExp = output.get(0);
                Line lastLineExp = output.get(output.size() - 1);
                Line firstLineAct = actual.get(0);
                Line lastLineAct = actual.get(actual.size() - 1);

                final StringJoiner sj = new StringJoiner(System.lineSeparator());

                if (!firstLineExp.isExactly(String::equals, firstLineAct)) {
                    sj.add("First line expected '" + firstLineExp
                            + "', but got '" + firstLineAct + "'"
                            + (firstLineExp.isExactly(String::equalsIgnoreCase, firstLineAct) ? " (wrong case)" : ""));
                } else {
                    output.remove(0);
                    actual.remove(0);
                }

                if (!lastLineExp.isExactly(String::equals, lastLineAct)) {
                    sj.add("Last line expected '" + lastLineExp
                            + "', but got '" + lastLineAct + "'"
                            + (lastLineExp.isExactly(String::equalsIgnoreCase, lastLineAct) ? " (wrong case)" : ""));
                } else {
                    output.remove(output.size() - 1);
                    actual.remove(actual.size() - 1);
                }

                output.sort(Comparator.comparing(Line::normalized));
                actual.sort(Comparator.comparing(Line::normalized));

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
                
                return Optional.of(output).filter((l) -> !l.isEmpty()).map((l) -> l.stream().map(Line::normalized)
                        .filter((s) -> actual.stream().noneMatch((m) -> m.normalized().contains(s)))
                        .collect(Collectors.joining(", "))).filter((s) -> !s.isEmpty())
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
        for (final Type t : TYPES)
            if (t.name().equalsIgnoreCase(type))
                return t;
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
