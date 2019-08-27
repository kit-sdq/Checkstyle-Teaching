/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.util;

import java.util.StringJoiner;

/**
 * An utilit class for some String manipulations.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public final class StringUtil {
    private StringUtil() {
        assert false;
    }
    
    /**
     * Parenthesizes the given String.
     * 
     * @param toParenthesize - the given String
     * @return the string with "(" in front of it and ")" at the end
     */
    public static String parenthesize(final String toParenthesize) {
        return "(" + toParenthesize + ")";
    }
    
    /**
     * Joins the given Iterable with the given delimiter to one String.
     * 
     * @param iterable - the given Iterable
     * @param <T> - the contained type
     * @param delim - the given delimiter
     * @return the joined String
     */
    public static <T> String toString(final Iterable<T> iterable, final String delim) {
        final StringJoiner joiner = new StringJoiner(delim);
        
        for (final T element : iterable) {
            joiner.add(element.toString());
        }
        
        return joiner.toString();
    }
}
