/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.checksum;

/**
 * Represents a complete serial number including the check digit.
 * 
 * @author Thomas Weber
 * @version 1.0
 */
public class SerialNumber extends SerialNumberWithoutCheckDigit {

    /**
     * The regex for the complete serial number
     */
    public static final String SERIAL_NUMBER_REGEX = "[A-Z][A-Z][0-9]{9}[1-9]";

    private final int checkDigit;

    /**
     * Creates a new serial number.
     * 
     * @param checkDigit
     *            - the check digit
     * @param firstCharacter
     *            - the first character
     * @param secondCharacter
     *            - the second character
     * @param numbers
     *            - the numbers as a String
     */
    public SerialNumber(final int checkDigit, final char firstCharacter, final char secondCharacter,
            final String numbers) {
        super(firstCharacter, secondCharacter, numbers);
        this.checkDigit = checkDigit;
    }

    /**
     * Creates a new serial number.
     * 
     * @param checkDigit
     *            - the check digit
     * @param serialNumberWithoutCheckDigit
     *            - the serial number without check digit
     */
    public SerialNumber(final int checkDigit, final SerialNumberWithoutCheckDigit serialNumberWithoutCheckDigit) {
        super(serialNumberWithoutCheckDigit.firstCharacter, serialNumberWithoutCheckDigit.secondCharacter,
                serialNumberWithoutCheckDigit.numbers);
        this.checkDigit = checkDigit;
    }

    /**
     * Creates a new serial number.
     * 
     * @param serialNumber
     *            - the serial number as a String
     */
    public SerialNumber(final String serialNumber) {
        super(serialNumber.substring(0, serialNumber.length() - 1));
        this.checkDigit = serialNumber.charAt(serialNumber.length() - 1) - UNICODE_INDEX_ONE;
    }

    /**
     * Checks whether this serial number is a valid serial number.
     * 
     * @return whether this serial number is a valid serial number
     */
    public boolean isValid() {
        return checkDigit == this.checksum();
    }
}
