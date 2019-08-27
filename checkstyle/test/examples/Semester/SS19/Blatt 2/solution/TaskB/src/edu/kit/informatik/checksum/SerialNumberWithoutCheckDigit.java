/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.checksum;

/**
 * Represents a serial number without check digit.
 * 
 * @author Thomas Weber
 * @version 1.0
 */
public class SerialNumberWithoutCheckDigit {
    
    /**
     * unicode of '1' - 1
     */
    public static final int UNICODE_INDEX_ONE = '1' - 1;

    /**
     * unicode of 'A' - 1
     */
    public static final int UNICODE_INDEX_A = 'A' - 1;
    
    /**
     * regex of the serail number without check digit
     */
    public static final String SERIAL_NUMBER_REGEX = "[A-Z][A-Z][0-9]{9}";

    /**
     * The first character of the serial number
     */
    protected int firstCharacter;

    /**
     * The second character of the serial number
     */
    protected int secondCharacter;

    /**
     * The numbers of the serial number as a String
     */
    protected String numbers;

    /**
     * Creates a new serial number without check digit.
     * 
     * @param firstCharacter - the first character
     * @param secondCharacter - the second character
     * @param numbers - the numbers as a String
     */
    public SerialNumberWithoutCheckDigit(final int firstCharacter,
            final int secondCharacter, final String numbers) {
        if (!String.valueOf(firstCharacter).matches("[A-Z]") || !String
                .valueOf(secondCharacter).matches("[A-Z]") || !numbers
                .matches("[0-9]{9}")) {
            throw new IllegalArgumentException("input malformed!");
        }
        this.firstCharacter = firstCharacter;
        this.secondCharacter = secondCharacter;
        this.numbers = numbers;
    }

    /**
     * Creates a new serial number without check digit.
     * 
     * @param serialNumberWithoutCheckDigit - the serial number without check digit as a String
     */
    public SerialNumberWithoutCheckDigit(
            final String serialNumberWithoutCheckDigit) {
        if (!serialNumberWithoutCheckDigit.matches(SERIAL_NUMBER_REGEX)) {
            throw new IllegalArgumentException("input malformed!");
        }
        this.firstCharacter = serialNumberWithoutCheckDigit.charAt(0);
        this.secondCharacter = serialNumberWithoutCheckDigit.charAt(1);
        this.numbers = serialNumberWithoutCheckDigit.substring(2);
    }

    /**
     * Calculates the digitsum of this serial number.
     * 
     * @return the digitsum of this serial number.
     */
    public int digitsum() {
        int sum = firstCharacter - UNICODE_INDEX_A + secondCharacter
                  - UNICODE_INDEX_A;
        for (char number : numbers.toCharArray()) {
            sum += number - UNICODE_INDEX_ONE;
        }
        return sum;
    }

    /**
     * Calculates the checksum of this serial number. 
     * 
     * @return the checksum of this serial number
     */
    public int checksum() {
        int digitsum = digitsum();
        switch (7 - (digitsum % 9)) {
            case 0:
                return 9;
            case -1:
                return 8;
            default:
                return  7 - (digitsum % 9);
        }
    }
}
