/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Class to implement the functionality  assignment 2 task B requires. This
 * class uses the singleton design pattern.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public final class PersonalDate {
    /**
     * Self-referenced class attribute.
     */
    private static PersonalDate personalDate;

    /**
     * Private constructor without parameters like stated in the assignment.
     */
    private PersonalDate() {
        /* GregorianCalendar constructor without parameters to get the
           current date */
        GregorianCalendar calendar = new GregorianCalendar();
        /* calendar.get returns information about the stored date depending
           on the arguments passed (Calendar.MONTH range is 0 to 11 but
           calendar.get requires 1 to 12 {see https://docs.oracle
           .com/javase/1.5.0/docs/api/java/util/Calendar.html#MONTH for more
           details}) */
        System.out.println(
                "Aktuelles Datum: " + calendar.get(Calendar.DAY_OF_MONTH) + "."
                + (calendar.get(Calendar.MONTH) + 1) + "." + calendar
                        .get(Calendar.YEAR));
    }

    /**
     * Static method to create instance of singleton class.
     *
     * @return instance of this class
     */
    public static PersonalDate getPersonalDate() {
        if (PersonalDate.personalDate == null) {
            PersonalDate.personalDate = new PersonalDate();
        }
        return PersonalDate.personalDate;
    }

}
