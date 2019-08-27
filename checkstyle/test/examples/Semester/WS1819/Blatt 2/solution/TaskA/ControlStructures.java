/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

/**
 * Class to implement the functionality assignment 2 task A required.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public final class ControlStructures {

    /**
     * Number of iterations for the first sequence.
     */
    private static final int NUMBER_ITERATIONS_1 = 35;

    /**
     * Number of iterations for the second sequence.
     */
    private static final int NUMBER_ITERATIONS_2 = 20;

    /**
     * Initial value for the first sequence.
     */
    private static final int INITIAL_VALUE_1 = 0;

    /**
     * Initial value for the second sequence.
     */
    private static final int INITIAL_VALUE_2 = 4;

    /**
     * Lower border for the third task.
     */
    private static final int LEFT_BORDER_3 = 45;

    /**
     * Upper border for the third task.
     */
    private static final int RIGHT_BORDER_3 = 82;

    /**
     * Number of iterations for the fourth task.
     */
    private static final int NUMBER_ITERATIONS_4 = 30;

    /**
     * Private constructor because this is a helper class.
     */
    private ControlStructures() {
    }

    /**
     * Main method which prints the results of the calculations. These
     * calculations use given data.
     *
     * @param args all params passed will be ignored
     */
    public static void main(final String[] args) {
        System.out.println(sequenceOne(INITIAL_VALUE_1));
        System.out.println(sequenceTwo(INITIAL_VALUE_2));
        System.out.println(sumInterval(LEFT_BORDER_3, RIGHT_BORDER_3));
        System.out.println(approxPi(NUMBER_ITERATIONS_4));
    }

    /**
     * Calculates a sequence with a given initial value. n % 12 == 0 -> n+1 =
     * n/3 + 1 else -> n+1 = n*2 + 4
     *
     * @param initialValue Initial value to start the sequence with.
     * @return String representation of the sequence
     */
    public static String sequenceOne(final int initialValue) {
        int sequenceElement = initialValue;
        String sequence = "" + sequenceElement;
        for (int count = 1; count < NUMBER_ITERATIONS_1; count++) {
            if (sequenceElement % 12 == 0) {
                sequenceElement = sequenceElement / 3 + 1;
            } else {
                sequenceElement = sequenceElement * 2 + 4;
            }
            sequence += ";" + sequenceElement;
        }
        return sequence;
    }

    /**
     * Calculates a sequence with a given initial value. counter even -> n+1 =
     * -7 + 3*counter else -> n+1 = 3*counter - 7
     *
     * @param initialValue Initial value to start the sequence with.
     * @return String representation of the sequence
     */
    public static String sequenceTwo(final int initialValue) {
        int sequenceElement = initialValue;
        String sequence = "" + sequenceElement;
        for (int count = 1; count < NUMBER_ITERATIONS_2; count++) {
            if (count % 2 == 0) {
                sequenceElement = -7 + 3 * count;
            } else {
                sequenceElement = 7 - count * 3;
            }
            sequence += ";" + sequenceElement;
        }
        return sequence;
    }

    /**
     * Calculates the sum of all integers inside the given interval.
     *
     * @param leftBorder lower border of the interval
     * @param rightBorder upper border of the interval
     * @return The sum of all elements between the lower and upper border,
     *         borders included.
     */
    public static int sumInterval(final int leftBorder, final int rightBorder) {
        int sum = 0;
        for (int count = leftBorder; count <= rightBorder; count++) {
            sum += count;
        }
        return sum;
    }

    /**
     * Calculates an approximation of pi/4 based on the method of Leibniz.
     *
     * @param numberIterations The number of iterations used.
     * @return The approximation of pi/4.
     */
    public static double approxPi(final int numberIterations) {
        double approximation = 0;
        for (int count = 0; count <= numberIterations; count++) {
            approximation += (Math.pow((-1), count) / (2 * count + 1));
        }
        return approximation;
    }
}

