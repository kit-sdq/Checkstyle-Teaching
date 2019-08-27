/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

/**
 * Representing a film with a unique identifier, a length, title, release date
 * and a genre.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class Film {
    /**
     * Static class attribute to ensure ids are unique.
     */
    private static int uniqueIdCounter = 0;

    /**
     * The title of the film.
     */
    private final String title;

    /**
     * The length of the film.
     */
    private final int length;

    /**
     * The release Date of the film as {@link Date}.
     */
    private final Date releaseDate;

    /**
     * Unique Identifier of the film.
     */
    private final int uniqueId;

    /**
     * The genre of the film as {@link Genre}.
     */
    private final Genre genre;

    /**
     * Instantiates a new film with the given data. No checks or calculations
     * are performed.
     *
     * @param title title of the film
     * @param length length of the film
     * @param releaseDate release date of the film as {@link Date}
     * @param genre genre of the film as {@link Genre}
     */
    public Film(final String title, final int length, final Date releaseDate,
            final Genre genre) {
        this.title = title;
        this.length = length;
        this.releaseDate = releaseDate;
        this.uniqueId = uniqueIdCounter++;
        this.genre = genre;
    }

    /**
     * @return the title of the film
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the length of the film
     */
    public int getLength() {
        return length;
    }

    /**
     * @return the release date of the film
     */
    public Date getReleaseDate() {
        return releaseDate;
    }

    /**
     * @return the unique identifier of the film
     */
    public int getUniqueId() {
        return uniqueId;
    }

    /**
     * @return the genre of the film
     */
    public Genre getGenre() {
        return genre;
    }

    /* no setters needed, remember we build a film database, so we have final
       film data that wont change */

    @Override
    public String toString() {
        // Returns the information about the film
        return "Film{" + "title='" + title + '\'' + ", length=" + length + ","
               + " releaseDate=" + releaseDate + ", uniqueId=" + uniqueId + ","
               + " genre=" + genre + '}';
    }


}
