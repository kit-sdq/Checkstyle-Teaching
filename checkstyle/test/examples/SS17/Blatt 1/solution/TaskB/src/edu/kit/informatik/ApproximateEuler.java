package edu.kit.informatik;

/**
 * This class provides a method to approximate the euler number.
 *
 * @author Peter Oettig
 * @version 1.0
 */
public class ApproximateEuler {
    /**
     * The main method.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        ApproximateEuler approxE = new ApproximateEuler();
        for (int i = 5; i <= 20; i += 5) {
            System.out.println(approxE.approximateE(i));
        }
    }

    /**
     * Approximates the euler number with n iterations.
     *
     * @param n The number of iterations.
     * @return The approximated euler number.
     */
    private double approximateE(int n) {
        //Interprete every n smaller than zero as zero.
        if (n <= 0) {
            return 1.0;
        }

        //Set e to the result with n = 0
        double e = 1.0;
        long faculty = 1;

        for (int i = 1; i <= n; i++) {
            faculty *= i;
            e += 1.0 / faculty;
        }

        return e;
    }
}
