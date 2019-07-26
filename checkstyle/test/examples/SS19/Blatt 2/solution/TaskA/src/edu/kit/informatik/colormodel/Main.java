/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.colormodel;

import edu.kit.informatik.Terminal;

import static edu.kit.informatik.colormodel.RgbColor.RANGE_REGEX;

/**
 * This is the entry point class for the program containing the main method.
 * 
 * @author Thomas Weber
 * @version 1.0
 */
public class Main {

    /**
     * the input seperator
     */
    public static final String SEPARATOR = ";";

    /**
     * the regex for the quit command
     */
    public static final String QUIT = "quit";

    /**
     * the regex for the convert command
     */
    public static final String CONVERT = "convert " + RANGE_REGEX + SEPARATOR + RANGE_REGEX + SEPARATOR + RANGE_REGEX;

    /**
     * The main method of the program.
     * @param args - the arguments that are passed to the program at launch as array.
     */
    public static void main(String[] args) {
        String input = Terminal.readLine();
        while (!input.matches(QUIT)) {
            if (input.matches(CONVERT)) {
                String shortened = input.substring(8);
                String[] numbersStr = shortened.split(SEPARATOR);
                int[] numbers = new int[numbersStr.length];
                for (int i = 0; i < numbers.length; i++) {
                    try {
                        numbers[i] = Integer.parseInt(numbersStr[i]);
                    } catch (NumberFormatException e) {
                        Terminal.printError("invalid number format!");
                    }
                }
                try {
                    Terminal.printLine(new RgbColor(numbers[0], numbers[1], numbers[2]).computeConversion().toString());
                } catch (IllegalArgumentException e) {
                    Terminal.printError(e.getMessage());
                }
            } else {
                Terminal.printError("invalid command format " + input);
            }
            input = Terminal.readLine();
        }

    }
}
