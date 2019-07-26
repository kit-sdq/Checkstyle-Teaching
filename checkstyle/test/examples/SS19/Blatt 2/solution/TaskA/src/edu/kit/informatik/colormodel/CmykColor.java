/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.colormodel;

import java.util.Locale;

/**
 * Represents a CMYK color.
 * 
 * @author Thomas Weber
 * @version 1.0
 */
public class CmykColor {
    /**
     * the minimal value
     */
    public static final int MIN_VALUE = 0;

    /**
     * the maximal value
     */
    public static final int MAX_VALUE = 1;

    /**
     * the separator for the string representation
     */
    public static final String SEPARATOR = ";";

    private final double cyan;

    private final double magenta;

    private final double yellow;

    private final double key;

    /**
     * Creates a new CMYK color.
     * 
     * @param cyan - the cyan value
     * @param magenta - the magenta value
     * @param yellow - the yellow value
     * @param key - the key value
     */
    public CmykColor(final double cyan, final double magenta,
            final double yellow, final double key) {
        if (MIN_VALUE > cyan || cyan > MAX_VALUE) {
            throw new IllegalArgumentException("cyan is out of range!");
        }
        if (MIN_VALUE > magenta || magenta > MAX_VALUE) {
            throw new IllegalArgumentException("magenta is out of range!");
        }
        if (MIN_VALUE > yellow || yellow > MAX_VALUE) {
            throw new IllegalArgumentException("yellow is out of range!");
        }
        if (MIN_VALUE > key || key > MAX_VALUE) {
            throw new IllegalArgumentException("key is out of range!");
        }
        this.cyan = cyan;
        this.magenta = magenta;
        this.yellow = yellow;
        this.key = key;
    }

    /**
     * Gets the cyan value.
     * @return the cyan value
     */
    public double getCyan() {
        return cyan;
    }

    /**
     * Gets the magenta value.
     * @return the magenta value
     */
    public double getMagenta() {
        return magenta;
    }

    /**
     * Gets the yellow value.
     * @return the yellow value
     */
    public double getYellow() {
        return yellow;
    }

    /**
     * Gets the key value.
     * @return the key value
     */
    public double getKey() {
        return key;
    }

    @Override
    public String toString() {
        return formatColor(cyan) + SEPARATOR + formatColor(yellow)
            + SEPARATOR + formatColor(magenta) + SEPARATOR
            + formatColor(key);
    }

    private String formatColor(double color) {
        return String.format(Locale.US, "%.4f", color);
    }
}
