/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.ludo;

import edu.kit.informatik.util.StringUtil;

/**
 * Represents a player's color and since a player's color represents a player
 * also a player.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public enum PlayerColor {
    /**
     * The red player.
     */
    RED("red", "R", 0),

    /**
     * The blue player.
     */
    BLUE("blue", "B", 1),

    /**
     * The green player.
     */
    GREEN("green", "G", 2),

    /**
     * The yellow player.
     */
    YELLOW("yellow", "Y", 3);

    private static final PlayerColor[] VALUES_ORDERED = new PlayerColor[] {RED, BLUE, GREEN, YELLOW};

    private final String color;
    private final String pattern;
    private final int number;

    /**
     * Constructor for PlyerColor.
     * 
     * @param color - the color
     * @param pattern - the pattern
     * @param number - the number
     */
    PlayerColor(final String color, final String pattern, final int number) {
        this.color = color;
        this.pattern = pattern;
        this.number = number;
    }

    /**
     * Gets the player's color String.
     * 
     * @return the player's color String
     */
    public String getColor() {
        return color;
    }

    /**
     * Gets the player's color pattern, i.e. a single letter abbreviating the
     * player's color.
     * 
     * @return the player's color pattern
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * Gets the number of the player.
     * 
     * @return the number of the player
     */
    public int getNumber() {
        return number;
    }

    /**
     * Gets all players in order.
     * 
     * @return all players in order
     */
    public static PlayerColor[] valuesOrdered() {
        return VALUES_ORDERED;
    }

    /**
     * Gets all player's color patterns in order and linked with or.
     * 
     * @return all player's color patterns in order and linked with or
     */
    public static String getPatterns() {
        final StringBuilder patterns = new StringBuilder();

        for (final PlayerColor color : valuesOrdered()) {
            patterns.append(color.getPattern() + "|");
        }

        return StringUtil.parenthesize(patterns.toString());
    }

    /**
     * Gets the color represented by the given color pattern.
     * 
     * @param colorPattern
     *            - the given color pattern
     * @return the color represented by the given color pattern or {@code null} if
     *         pattern is wrong
     */
    public static PlayerColor ofPattern(final String colorPattern) {
        for (final PlayerColor color : valuesOrdered()) {
            if (color.getPattern().equals(colorPattern)) {
                return color;
            }
        }
        return null;
    }

    /**
     * Gets the player after this player.
     * 
     * @return the player after this player
     */
    public PlayerColor getNext() {
        return valuesOrdered()[(this.number + 1) % valuesOrdered().length];
    }
}
