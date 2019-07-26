package edu.kit.informatik._intern.util;

import edu.kit.informatik._intern.util.function.CharPredicate;

/**
 * Provides utility methods for strings.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/03/27
 */
public final class StringUtil {
    
    /** Empty string-array. */
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    
    private StringUtil() {
        
    }
    
    /**
     * Converts a control character to its graphic symbol.
     * 
     * <p><b>Implementation Note:</b><br>
     * Currently only the first 32 control characters ({@code '\u0000'} to {@code '\u001F'}) are treated as control
     * characters.
     * 
     * @param  codepoint the codepoint to convert
     * @return if {@code codepoint} is a control character, its graphic symbol, {@code codepoint} otherwise
     */
    public static int controlCharacterToSymbol(final int codepoint) {
        return (codepoint & 31) == codepoint ? codepoint + '\u2400' : codepoint;
    }
    
    /**
     * Returns a string with consecutive whitespace removed.
     * 
     * @param  string the string to remove the whitespace in
     * @return a string with consecutive whitespace removed
     */
    public static String removeConsecutiveWhitespace(final String string) {
        final StringBuilder sb = new StringBuilder(string.length());
        boolean ws = false;
        for (int i = 0, l = string.codePointCount(0, string.length()); i < l; i++) {
            final int c = string.codePointAt(i);
            if (Character.isWhitespace(c)) {
                if (ws) {
                    continue;
                }
                ws = true;
            } else {
                ws = false;
            }
            sb.appendCodePoint(c);
        }
        return sb.toString();
    }
    
    /**
     * Converts an empty string to a {@code null} string.
     * 
     * @param  s the string
     * @return {@code s}, or {@code null} if {@code s} has the length 0
     */
    public static String emptyToNull(final String s) {
        return s != null && s.isEmpty() ? null : s;
    }
    
    /**
     * Returns whether a string is either {@code null} or empty.
     * 
     * @param  s the string
     * @return {@code true} if the string is either {@code null} or empty, {@code false} otherwise
     */
    public static boolean isNullOrEmpty(final String s) {
        return s == null || s.length() == 0;
    }
    
    /**
     * Returns whether a string is neither {@code null} nor empty.
     * 
     * @param  s the string
     * @return {@code true} if the string is neither {@code null} nor empty, {@code false} otherwise
     */
    public static boolean notNullOrEmpty(final String s) {
        return s != null && s.isEmpty();
    }
    
    /**
     * Converts a {@code null} string to an empty string.
     * 
     * @param  s the string
     * @return {@code s}, or a string with length 0 if {@code s} is {@code null}
     */
    public static String nullToEmpty(final String s) {
        return s == null ? "" : s;
    }
    
    /**
     * Returns the first index of an character in a {@code String} matching a {@code CharPredicate}.
     * 
     * @param  s the string
     * @param  p the predicate
     * @return the first index of an character in a {@code String} matching a {@code CharPredicate}
     */
    public static int indexOf(final String s, final CharPredicate p) {
        return indexOf0(s, 0, s.length() - 1, p);
    }
    
    /**
     * Returns the first index of an character in a {@code String} matching a {@code CharPredicate}.
     * 
     * @param  s the string
     * @param  fromIndex the index to search from
     * @param  p the predicate
     * @return the first index of an character in a {@code String} matching a {@code CharPredicate}
     */
    public static int indexOf(final String s, final int fromIndex, final CharPredicate p) {
        return indexOf0(s, Integer.max(fromIndex, 0), s.length() - 1, p);
    }
    
    /**
     * Returns the first index of an character in a {@code String} matching a {@code CharPredicate}.
     * 
     * @param  s the string
     * @param  fromIndex the index to search from
     * @param  toIndex the index to search to
     * @param  p the predicate
     * @return the first index of an character in a {@code String} matching a {@code CharPredicate}
     */
    public static int indexOf(final String s, final int fromIndex, final int toIndex, final CharPredicate p) {
        return indexOf0(s, Integer.max(fromIndex, 0), Integer.min(toIndex, s.length() - 1), p);
    }
    
    /**
     * Returns the last index of an character in a {@code String} matching a {@code CharPredicate}.
     * 
     * @param  s the string
     * @param  p the predicate
     * @return the last index of an character in a {@code String} matching a {@code CharPredicate}
     */
    public static int lastIndexOf(final String s, final CharPredicate p) {
        return lastIndexOf0(s, s.length() - 1, 0, p);
    }
    
    /**
     * Returns the last index of an character in a {@code String} matching a {@code CharPredicate}.
     * 
     * @param  s the string
     * @param  fromIndex the index to search from
     * @param  p the predicate
     * @return the last index of an character in a {@code String} matching a {@code CharPredicate}
     */
    public static int lastIndexOf(final String s, final int fromIndex, final CharPredicate p) {
        return lastIndexOf0(s, Integer.min(fromIndex, s.length() - 1), 0, p);
    }
    
    /**
     * Returns the last index of an character in a {@code String} matching a {@code CharPredicate}.
     * 
     * @param  s the string
     * @param  fromIndex the index to search from
     * @param  toIndex the index to search to, exclusive
     * @param  p the predicate
     * @return the last index of an character in a {@code String} matching a {@code CharPredicate}
     */
    public static int lastIndexOf(final String s, final int fromIndex, final int toIndex, final CharPredicate p) {
        return lastIndexOf0(s, Integer.min(fromIndex, s.length() - 1), Integer.max(toIndex, 0), p);
    }
    
    private static int indexOf0(final String s, final int from, final int to, final CharPredicate p) {
        for (int i = from; i <= to; i++)
            if (p.test(s.charAt(i)))
                return i;
        
        return -1;
    }
    
    private static int lastIndexOf0(final String s, final int from, final int to, final CharPredicate p) {
        for (int i = from; i >= to; i--)
            if (p.test(s.charAt(i)))
                return i;
        
        return -1;
    }
}
