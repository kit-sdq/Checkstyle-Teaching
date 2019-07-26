package edu.kit.informatik._intern.terminal.analyzer;

import java.util.function.BiPredicate;

/**
 * Helper-class to hold the real and the normalized output to allow printing the real output rather than the
 * normalized one.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/06/22
 */
final class Line {
    
    private final String real;
    private final String normalized;
    
    /**
     * Creates a new {@code Line}.
     * 
     * @param real the real output of the line
     * @param normalized the normalized output of the line
     */
    Line(final String real, final String normalized) {
        this.real = real;
        this.normalized = normalized;
    }
    
    /**
     * Compares the normalized output of this line with the normalized output of {@code other} using the provided
     * predicate.
     * 
     * @param  other the other line
     * @return the result of the predicate applied to the normalized output of {@code this} and {@code other}
     */
    public boolean is(final BiPredicate<String, String> predicate, final Line other) {
        return predicate.test(normalized, other.normalized);
    }
    
    /**
     * Returns the normalized output of the line.
     * 
     * @return the normalized output
     */
    public String normalized() {
        return normalized;
    }
    
    /**
     * Returns the real output of the line.
     * 
     * @return the real output
     */
    @Override
    public String toString() {
        return real;
    }
}
