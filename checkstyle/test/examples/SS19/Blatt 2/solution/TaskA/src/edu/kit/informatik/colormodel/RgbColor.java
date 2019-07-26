/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.colormodel;

/**
 * Represents a RGB color.
 * 
 * @author Thomas Weber
 * @version 1.0
 */
public class RgbColor {
    /**
     * the minimal value
     */
    public static final int MIN_VALUE = 0;

    /**
     * the maximal value
     */
    public static final int MAX_VALUE = 255;

    /**
     * the regex for the allowed range
     */
    public static final String RANGE_REGEX = "(1?[0-9]{1,2}|2[0-4][0-9]|25[0-5"
                                             + "])";

    private final double divisor = 255d;

    private final int red;

    private final int green;

    private final int blue;
    
    /**
     * Creates a new RGB color.
     * 
     * @param red - the red value
     * @param green - the green value
     * @param blue - the blue value
     */
    public RgbColor(final int red, final int green, final int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        if (MIN_VALUE > this.red || this.red > MAX_VALUE) {
            throw new IllegalArgumentException("red is no color value!");
        }
        if (MIN_VALUE > this.green || this.green > MAX_VALUE) {
            throw new IllegalArgumentException("green is no color value!");
        }
        if (MIN_VALUE > this.blue || this.blue > MAX_VALUE) {
            throw new IllegalArgumentException("blue is no color value!");
        }
    }

    /**
     * Gets the red value.
     * 
     * @return the red value
     */
    public int getRed() {
        return red;
    }

    /**
     * Gets the green value.
     * 
     * @return the green value
     */
    public int getGreen() {
        return green;
    }

    /**
     * Gets the blue value.
     * 
     * @return the blue value
     */
    public int getBlue() {
        return blue;
    }

    /**
     * Calculates a CMYK color which represents the same color as this one.
     * 
     * @return this color as CMYK color
     */
    public CmykColor computeConversion() {
        double redDivided = red / divisor;
        double greenDivided = green / divisor;
        double blueDivided = blue / divisor;
        double w = Math.max(Math.max(redDivided, greenDivided), blueDivided);
        double cyan = w != 0 ? (w - redDivided) / (w) : 0;
        double magenta = w != 0 ? (w - greenDivided) / (w) : 0;
        double yellow = w != 0 ? (w - blueDivided) / (w) : 0;
        double key = 1 - w;
        return new CmykColor(cyan, magenta, yellow, key);
    }
}
