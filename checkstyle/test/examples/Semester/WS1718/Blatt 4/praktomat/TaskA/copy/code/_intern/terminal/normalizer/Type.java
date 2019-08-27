package edu.kit.informatik._intern.terminal.normalizer;

import java.util.Locale;
import java.util.StringJoiner;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import edu.kit.informatik._intern.util.StringUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Enum holding the different normalizer types.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/06/06
 */
public enum Type { // LOWPRIO Automatically sort normalizer in a non-inferring way when used for analyzer
    /** Converts strings to lowercase. \u000A @author unascribed @author Tobias Bachert */
    LOWERCASE("ignoring case") {
        
        @Override public UnaryOperator<String> function(final String args) {
            return (s) -> s.toLowerCase(Locale.ENGLISH);
        }
    },
    /** Sorts strings. \u000A @author Tobias Bachert */
    SORT("sorting substrings") {
        
        @Override public UnaryOperator<String> function(final String args) {
            final String del = StringUtil.nullToEmpty(args);
            final Pattern p = Pattern.compile(del, Pattern.LITERAL);
            
            return (s) -> {
                if (s.isEmpty())
                    return s;
                if (del.isEmpty())
                    return p.splitAsStream(s).sorted().collect(Collectors.joining());
                
                final StringJoiner sj = new StringJoiner(del);
                if (p.splitAsStream(s).sorted().peek(sj::add).count() == 0)
                    sj.add("");
                for (int sP = s.length(), dP = del.length(); sP > 0 && s.charAt(--sP) == del.charAt(--dP);) {
                    if (dP == 0) {
                        dP += del.length();
                        sj.add("");
                    }
                }
                return sj.toString();
            };
        }
    },
    /** Replaces substring. \u000A @author Tobias Bachert */
    REPLACE("replacing substrings") {
        
        private final Pattern p = Pattern.compile("^'(.+?)(?<!\\\\)'\\h*->\\h*'(.+)(?<!\\\\)'$");
        
        @Override public UnaryOperator<String> function(final String args) {
            final Matcher m = p.matcher(StringUtil.nullToEmpty(args));
            if (!m.find())
                throw new IllegalStateException("missing indicator");
            
            final String toreplace = m.group(1);
            final String replacement = m.group(2);
            
            return (s) -> s.replace(toreplace, replacement);
        }
    },
    /** Removes trailing chars. \u000A @author Tobias Bachert */
    REMOVETRAILING("removing trailing characters") {
        
        @Override public UnaryOperator<String> function(final String args) {
            final String del = StringUtil.nullToEmpty(args);
            
            return (s) -> {
                int c = 0;
                for (int sP = s.length(), dP = del.length(); sP > 0 && s.charAt(--sP) == del.charAt(--dP);) {
                    if (dP == 0) {
                        dP += del.length();
                        c++;
                    }
                }
                return s.substring(0, s.length() - c * del.length());
            };
        }
    },
    /** Removes surrounding separators. \u000A @author Tobias Bachert */
    REMOVESURROUNDING("removing surrounding separators") {
        
        @Override public UnaryOperator<String> function(final String args) {
            final String del = StringUtil.nullToEmpty(args);
            final Pattern p = Pattern.compile(del + "*(.*?)" + del + "*");
            
            return (s) -> {
                final Matcher m = p.matcher(s);
                return m.find() ? m.group(1) : s; // Always true as long as s contains no line-terminators
            };
        }
    },
    /** Removes duplicates, maintains the first appearance. \u000A @author Tobias Bachert */
    DISTINCT("removing duplicates") {
        
        @Override public UnaryOperator<String> function(final String args) {
            final String del = StringUtil.nullToEmpty(args);
            final Pattern p = Pattern.compile(del, Pattern.LITERAL);
            
            return (s) -> {
                if (s.isEmpty())
                    return s;
                if (del.isEmpty())
                    return p.splitAsStream(s).distinct().collect(Collectors.joining());
                
                final StringJoiner sj = new StringJoiner(del);
                if (p.splitAsStream(s).distinct().peek(sj::add).count() == 0)
                    sj.add("");
                for (int sP = s.length(), dP = del.length(); sP > 0 && s.charAt(--sP) == del.charAt(--dP);) {
                    if (dP == 0) {
                        dP += del.length();
                        sj.add("");
                    }
                }
                return sj.toString();
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
        throw new IllegalArgumentException("no such normalizer " + type);
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
     * Returns the {@code Function} of the type used to normalize a string.
     * 
     * @param  args a string containing possible arguments
     * @return the {@code Function} of the type used to normalize
     */
    public abstract UnaryOperator<String> function(final String args);
}
