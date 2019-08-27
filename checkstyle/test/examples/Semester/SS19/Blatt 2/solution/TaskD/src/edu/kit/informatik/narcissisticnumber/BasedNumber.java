/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.narcissisticnumber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a number with a base.
 *
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public class BasedNumber {
    private static final int MIN_BASE = 2;
    private static final int MAX_BASE = 10;
    private static final int MIN_NUMBER = 10;
    private static final int MAX_NUMBER = 99999999;

    private final int base;
    private final int[] digits;

    /**
     * Creates a new based number.
     * 
     * @param number
     *            - the number
     * @param base
     *            - the base
     * @throws IllegalArgumentException
     *             - if any of the input values is invalid
     */
    public BasedNumber(final int number, final int base) throws IllegalArgumentException {
        if (base < MIN_BASE || base > MAX_BASE) {
            throw new IllegalArgumentException("illegal base!");
        }
        this.base = base;
        this.digits = extractDigits(number);
    }

    private int[] extractDigits(final int number) {
        if (number < MIN_NUMBER || number > MAX_NUMBER) {
            throw new IllegalArgumentException("illegal digit count!");
        }

        final List<Integer> digitsList = new ArrayList<Integer>();
        int remainingNumber = number;
        while (remainingNumber != 0) {
            final int digit = remainingNumber % 10;
            if (digit >= this.base) {
                throw new IllegalArgumentException("illegal digit " + digit + "!");
            }
            digitsList.add(digit);
            remainingNumber = remainingNumber / 10;
        }

        // reversing digit list so that digits are in correct order
        Collections.reverse(digitsList);

        // converting Integer list to int array
        return digitsList.stream().mapToInt(x -> x.intValue()).toArray();
    }

    /**
     * Checks whether the number is narcissistic.
     * 
     * @return whether the number is narcissistic
     */
    public boolean isNarcissistic() {
        return getNumber() == digitsToThePowerN();
    }

    private int digitsToThePowerN() {
        int sum = 0;

        final int n = this.digits.length;
        for (int i = 0; i < n; i++) {
            sum += Math.pow(this.digits[i], n);
        }

        return sum;
    }

    private int getNumber() {
        int number = 0;

        final int n = this.digits.length;
        for (int i = 0; i < n; i++) {
            number += (int) (this.digits[i] * Math.pow(this.base, (n - 1) - i));
        }

        return number;
    }
}
