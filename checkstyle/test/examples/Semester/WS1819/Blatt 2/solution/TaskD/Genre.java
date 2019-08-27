/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

/**
 * Contains available genres for films to categorize our film database.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public enum Genre {
    /**
     * Film with genre action.
     */
    ACTION("Aktion"),

    /**
     * Film with genre horror.
     */
    HORROR("Horror"),

    /**
     * Film with genre science fiction.
     */
    SCIENCEFICTION("Science-Fiction"),

    /**
     * Film with genre comedy.
     */
    COMEDY("Komoedie"),

    /**
     * Film with genre thriller.
     */
    THRILLER("Thriller"),

    /**
     * Film with genre western.
     */
    WESTERN("Western"),

    /**
     * Film with genre adventure.
     */
    ADVENTURE("Abenteuer");


    /**
     * A basic String representation for displaying.
     */
    private final String representation;

    /**
     * Instantiates a new enum element.
     *
     * @param representation basic String representation
     */
    Genre(final String representation) {
        this.representation = representation;
    }

    /**
     * @return the representation of the genre for output
     */
    public String getRepresentation() {
        return representation;
    }

    // representations cannot be changed

    @Override
    public String toString() {
        // returns the representation of the genre
        return representation;
    }
}
