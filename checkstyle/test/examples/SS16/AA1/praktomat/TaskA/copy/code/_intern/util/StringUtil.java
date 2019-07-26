package edu.kit.informatik._intern.util;

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
        return isNullOrEmpty(s) ? null : s;
    }
    
    /**
     * Returns whether a string is either {@code null} or empty.
     * 
     * @param  s the string
     * @return {@code true} if the string is either {@code null} or empty, {@code false} otherwise
     */
    public static boolean isNullOrEmpty(final String s) {
        return s == null || s.isEmpty();
    }
    
    /**
     * Returns whether a string is neither {@code null} nor empty.
     * 
     * @param  s the string
     * @return {@code true} if the string is neither {@code null} nor empty, {@code false} otherwise
     */
    public static boolean notNullOrEmpty(final String s) {
        return !isNullOrEmpty(s);
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
}
