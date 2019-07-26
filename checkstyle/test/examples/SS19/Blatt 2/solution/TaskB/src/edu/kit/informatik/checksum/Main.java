/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.checksum;

import edu.kit.informatik.Terminal;

/**
 * This is the entry point class for the program containing the main method.
 * 
 * @author Thomas Weber
 * @version 1.0
 */
public class Main {
    /**
     * the regex for the quit command
     */
    public static final String QUIT = "quit";

    /**
     * the regex for the name of the digitsum command
     */
    public static final String DIGITSUM_REGEX = "digitsum ";

    /**
     * the regex for the name of the checksum command
     */
    public static final String CHECKSUM_REGEX = "checksum ";

    /**
     * the regex for the name of the isValid command
     */
    public static final String ISVALID_REGEX = "isValid ";

    /**
     * The regex for the digitsum command
     */
    public static final String DIGITSUM = DIGITSUM_REGEX + SerialNumberWithoutCheckDigit.SERIAL_NUMBER_REGEX;

    /**
     * The regex for the checksum command
     */
    public static final String CHECKSUM = CHECKSUM_REGEX + SerialNumberWithoutCheckDigit.SERIAL_NUMBER_REGEX;

    /**
     * The regex for the isValid command
     */
    public static final String ISVALID = ISVALID_REGEX + SerialNumber.SERIAL_NUMBER_REGEX;

    /**
     * The main method of the program.
     * 
     * @param args
     *            - the arguments that are passed to the program at launch as array.
     */
    public static void main(String[] args) {
        String input = Terminal.readLine();
        while (!input.matches(QUIT)) {
            if (input.matches(DIGITSUM)) {
                try {
                    Terminal.printLine(
                            new SerialNumberWithoutCheckDigit(input.substring(DIGITSUM_REGEX.length())).digitsum());
                } catch (IllegalArgumentException e) {
                    Terminal.printError(e.getMessage());
                }
            } else if (input.matches(CHECKSUM)) {
                try {
                    Terminal.printLine(
                            new SerialNumberWithoutCheckDigit(input.substring(CHECKSUM_REGEX.length())).checksum());
                } catch (IllegalArgumentException e) {
                    Terminal.printError(e.getMessage());
                }
            } else if (input.matches(ISVALID)) {
                try {
                    Terminal.printLine(new SerialNumber(input.substring(ISVALID_REGEX.length())).isValid());
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
