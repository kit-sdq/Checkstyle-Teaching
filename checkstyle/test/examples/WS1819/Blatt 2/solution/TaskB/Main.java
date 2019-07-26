/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

/**
 * Class to test the functionality provided by {@link PersonalDate}. Part of the
 * implementation of assignment 2 task B.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public final class Main {

    /**
     * Private constructor because this is a helper class.
     */
    private Main() {
    }

    /**
     * Prints the current date formatted "Aktuelles Datum: dd.mm.yyyy".
     *
     * @param args all params passed will be ignored
     */
    public static void main(final String[] args) {
        PersonalDate personalDate = PersonalDate.getPersonalDate();
    }
}
