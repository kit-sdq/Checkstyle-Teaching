/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

/**
 * Contains information to store a date.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class Date {
    /**
     * Day of the month.
     */
    private final int day;

    /**
     * Month of the year.
     */
    private final int month;

    /**
     * Year
     */
    private final int year;

    /**
     * Instantiates a new Date object with the given parameters. No checks or
     * calculations are performed.
     *
     * @param day the day of the date
     * @param month the month of the date
     * @param year the year of the date
     */
    public Date(final int day, final int month, final int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    /**
     * @return the day of the date
     */
    public int getDay() {
        return day;
    }

    /**
     * @return the month of the date
     */
    public int getMonth() {
        return month;
    }

    /**
     * @return the year of the date
     */
    public int getYear() {
        return year;
    }

    // no setters, how can you change a date?

    @Override
    public String toString() {
        // Returns the date information
        return "Date{" + day + "." + month + "." + year + '}';
    }
}
