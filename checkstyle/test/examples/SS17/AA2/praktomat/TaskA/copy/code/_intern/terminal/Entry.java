package edu.kit.informatik._intern.terminal;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Spliterator;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import edu.kit.informatik._intern.terminal.analyzer.Analyzer;
import edu.kit.informatik._intern.terminal.analyzer.Type;
import edu.kit.informatik._intern.util.StringUtil;

/**
 * Interaction-entry for the Terminal-class.
 * 
 * @author  Tobias Bachert
 * @version 1.01, 2016/06/23
 */
public final class Entry {
    
    /*
     * Entry-structure:
     * 
     *          #############################################
     *          ##                                         ##
     * Builder ----------------> Information               ##
     *    |     ##                    |                    ##
     *    |     ##-----------------------------------------##
     *    |-----------------------> Input  -----------------------\
     *    |     ##                    |                    ##     |
     *    |     ##-----------------------------------------##     |
     *    |--------------------> Information               ##     |
     *    |     ##                    |                    ##     |
     *    |     ##-----------------------------------------##     |
     *    |---------------> Output          Actual <---------- Solution
     *    |     ##                \        /               ##
     *    \-------> Normalizer <-> Analyzer                ##
     *          ##                    |                    ##
     *          ##                  Result -------------------> (optional) Error-message
     *          ##                                         ##
     *          #############################################
     */
    
    private static final Pattern LINE_BREAK = Pattern.compile("\\R");
    
    private final Optional<String>   input;
    private final List<String>       output;
    private final StringBuilder      actual;
    private final Analyzer           analyzer;
    private final Collection<String> infoIn;
    private final Collection<String> infoOut;
    
    /**
     * Creates a new {@code Entry}. This constructor should only be called by the {@link Builder#build()} method.
     * 
     * @param input an {@code Optional} holding possible input
     * @param output the expected output
     * @param analyzer the analyzer to use
     * @param infoIn information to print for the input
     * @param infoOut information to print for the output
     */
    private Entry(final Optional<String> input, final List<String> output, final Analyzer analyzer,
            final Collection<String> infoIn, final Collection<String> infoOut) {
        this.input    = input;
        this.output   = output;
        this.actual   = new StringBuilder();
        this.analyzer = analyzer;
        this.infoIn   = infoIn;
        this.infoOut  = infoOut;
    }
    
    /**
     * Returns a new {@code Builder} for an {@code Entry}.
     * 
     * @return a new {@code Builder}
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * Adds the given string as actual output to the entry.
     * 
     * @param string the string to add
     */
    public void print(final String string) {
        actual.append(string);
    }
    
    /**
     * Terminates the current line by writing the line separator string.
     */
    public void newLine() {
        actual.append(System.lineSeparator());
    }
    
    /**
     * Returns the entry as log-message.
     * 
     * <p>The message will contain the for the praktomat required prefixes.
     * 
     * @return the entry as log-message
     */
    public Stream<String> asLogMessage() {
        final Stream<String> in   = input.map(Stream::of).orElseGet(Stream::empty);
        final Stream<String> out  = StringSupport.splitAtLineBreaks(actual);
        
        final Stream<String> err  = Stream.concat(
                analyze().map(LINE_BREAK::splitAsStream).orElseGet(Stream::empty),
                actual.lastIndexOf(System.lineSeparator()) + System.lineSeparator().length() >= actual.length()
                        ? Stream.empty() : Stream.of("Missing line terminator"));
        final Stream<String> infI = infoIn.stream();
        final Stream<String> infO = infoOut.stream();
        
        return Stream.of(
                infI .map((s) -> Prefix.INFO   + s),
                in   .map((s) -> Prefix.INPUT  + s),
                infO .map((s) -> Prefix.INFO   + s),
                out  .map((s) -> Prefix.OUTPUT + s),
                err  .map((s) -> Prefix.ERROR  + s)
        ).reduce(Stream::concat).get();
    }
    
    public Optional<String> analyze() {
        return analyzer.analyze(output.stream(), StringSupport.splitAtLineBreaks(actual));
    }
    
    /**
     * Returns the input of the entry.
     * 
     * <p>If the entry does not contain input, then a {@code NoSuchElementException} is thrown.
     * 
     * @return the input
     */
    public String input() {
        return input.orElseThrow(() -> new NoSuchElementException("no input available"));
    }
    
    @Override
    public String toString() {
        final StringJoiner sj = new StringJoiner(System.lineSeparator());
        sj.add("=============== Entry ===============");
        sj.add("input:    " + input);
        sj.add("expected: " + output);
        sj.add("actual:   " + actual.chars().map(StringUtil::controlCharacterToSymbol)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append));
        sj.add("analyzer: " + analyzer);
        sj.add("infoIn:   " + infoIn);
        sj.add("infoOut:  " + infoOut);
        analyze().map((s) -> s.chars().map(StringUtil::controlCharacterToSymbol)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append))
                .ifPresent((s) -> sj.add("error:    " + s));
        return sj.toString();
    }
    
    /**
     * Builder class for an {@code Entry}.
     * 
     * @author  Tobias Bachert
     * @version 1.00, 2016/06/06
     */
    public static final class Builder {
        
        private static final Analyzer DEFAULT_ANALYZER = Analyzer.of(Type.RESPECTORDERWITHFLOATCHECK);
        
        private       String            input;
        private final List<String>      output     = new ArrayList<>();
        private       Analyzer          analyzer;
        private final Queue<String>     infoIn     = new ArrayDeque<>();
        private final Queue<String>     infoOut    = new ArrayDeque<>();
        
        private Builder() {
            
        }
        
        /**
         * Creates a new {@code Entry} using the data of the builder.
         * 
         * <p>Note: This method makes no assurance that the data is copied in any form thus the builder should not be
         * modified afterwards if the interaction of the created entry has to be constant as these changes might or
         * might not be reflected on the returned entry.
         * 
         * @return a new {@code Entry}
         */
        public Entry build() {
            return new Entry(
                    Optional.ofNullable(input),
                    output,
                    analyzer != null ? analyzer : DEFAULT_ANALYZER,
                    infoIn,
                    infoOut);
        }
        
        /**
         * Returns whether the builder contains input.
         * 
         * @return {@code true} if the builder contains input, {@code false} otherwise
         */
        public boolean containsInput() {
            return input != null;
        }
        
        @Override
        public String toString() {
            final StringJoiner sj = new StringJoiner(System.lineSeparator());
            sj.add("### Entry.Builder ###");
            sj.add(String.valueOf(input));
            sj.add(String.valueOf(output));
            sj.add(String.valueOf(analyzer));
            sj.add(String.valueOf(infoIn));
            sj.add(String.valueOf(infoOut));
            return sj.toString();
        }
        
        /**
         * Adds an analyzer to the builder.
         * 
         * @param a the analyzer to add
         */
        public void addAnalyzer(final Analyzer a) {
            if (analyzer != null) {
                throw new IllegalStateException("analyzer already set");
            }
            analyzer = a;
            analyzer.message().ifPresent(infoOut::add);
        }
        
        /**
         * Adds an information to the builder.
         * 
         * @param string the information to add
         */
        public void addInformation(final String string) {
            if (input == null && analyzer == null) {
                infoIn.add(string);
            } else {
                infoOut.add(string);
            }
        }
        
        /**
         * Adds an interaction to the builder.
         * 
         * @param string the interaction to add
         */
        public void addInteraction(final String string) {
            if (input == null && analyzer == null) {
                input = string;
            } else {
                output.add(string);
            }
        }
    }
    
    private static final class StringSupport {
        
        private static final Pattern LINE_BREAK_SPLITTER = Pattern.compile("(.++|.*+(?=\\R))\\R?");
        
        /**
         * Splits the provided char-sequence at line-breaks.
         * 
         * <p>This will preserve trailing empty lines except the last one, unlike
         * <blockquote><pre>
         * Pattern.compile("\\R").splitAsStream(sequence);
         * Arrays.stream(Pattern.compile("\\R").split(sequence, -1));</pre>
         * </blockquote>
         * which either discards or keeps all trailing empty lines.<br>
         * For example, the result of this method will be
         * <blockquote><pre>
         * Line1\nLine2\n   // [Line1, Line2]
         * Line1\nLine2     // [Line1, Line2]
         * Line1\nLine2\n\n // [Line1, Line2, ]</pre>
         * </blockquote>
         * 
         * @param  sequence the {@code CharSequence} to split
         * @return a stream over the lines of {@code sequence}
         */
        public static Stream<String> splitAtLineBreaks(final CharSequence sequence) {
            Objects.requireNonNull(sequence);
            return StreamSupport.stream(new Spliterator<String>() {
                
                private final Matcher m = LINE_BREAK_SPLITTER.matcher(sequence);
                
                @Override public int characteristics() {
                    return ORDERED;
                }
                
                @Override public long estimateSize() {
                    return Long.MAX_VALUE;
                }
                
                @Override public boolean tryAdvance(final Consumer<? super String> action) {
                    // Does not check for modifications.
                    if (m.find()) {
                        action.accept(m.group(1));
                        return true;
                    }
                    return false;
                }
                
                @Override public Spliterator<String> trySplit() {
                    return null;
                }
            }, false);
        }
    }
}
