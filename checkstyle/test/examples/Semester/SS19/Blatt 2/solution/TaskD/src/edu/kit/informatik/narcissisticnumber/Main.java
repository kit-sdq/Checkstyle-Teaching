/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.narcissisticnumber;

import edu.kit.informatik.Terminal;

/**
 * This is the entry point class for the program containing the main method.
 *
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public class Main {
    private static final String SEPERATOR_COMMAND = " ";
    private static final String SEPERATOR_ARGUMENT = ";";

    private static final String QUIT = "quit";
    private static final String CHECK = "check-narcissistic-number" + SEPERATOR_COMMAND + "\\d+" + SEPERATOR_ARGUMENT
            + "\\d+";

    /**
     * The main method of the program.
     * 
     * @param args
     *            - the arguments that are passed to the program at launch as array.
     */
    public static void main(final String[] args) {
        String input = Terminal.readLine();
        while (!input.equals(QUIT)) {
            if (input.matches(CHECK)) {
                final String argument = input.split(SEPERATOR_COMMAND)[1];
                final String[] splittedArgs = argument.split(SEPERATOR_ARGUMENT);
                try {
                    final int number = Integer.parseInt(splittedArgs[0]);
                    final int base = Integer.parseInt(splittedArgs[1]);
                    final BasedNumber basedNumber = new BasedNumber(number, base);
                    Terminal.printLine(basedNumber.isNarcissistic());
                } catch (NumberFormatException e) {
                    Terminal.printError("illegal number format!");
                } catch (IllegalArgumentException e) {
                    Terminal.printError(e.getMessage());
                }
            } else {
                Terminal.printError("unknown command!");
            }
            input = Terminal.readLine();
        }
    }

}
