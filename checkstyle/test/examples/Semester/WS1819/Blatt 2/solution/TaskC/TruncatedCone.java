/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

/**
 * Class to implement the functionality  assignment 2 task C requires.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public final class TruncatedCone {
    /**
     * Use the library value of PI.
     */
    private static final double PI = Math.PI;

    /**
     * Separator for the output as stated in the task.
     */
    private static final String SEPARATOR = ";";

    /**
     * Private constructor because this is a helper class.
     */
    private TruncatedCone() {
    }

    /**
     * Prints the volume, the surface, the lateral surface and the length of the
     * surface line, all of these calculated with the given radius of the base
     * of the truncated cone, the given radius of the top of the truncated cone
     * and the height of it.
     *
     * @param args expects three ints in the args array, the base
     *         radius, the top radius and the height of the truncated cone, no
     *         checks on the input are performed
     */
    public static void main(final String[] args) {
        // format like stated in the assignment
        int baseRadius = Integer.parseInt(args[0]);
        int topRadius = Integer.parseInt(args[1]);
        int height = Integer.parseInt(args[2]);
        // print the calculated values separated by a the specified separator
        String output
                = String.valueOf(calculateVolume(baseRadius, topRadius, height))
                + SEPARATOR + calculateSurface(baseRadius, topRadius, height)
                + SEPARATOR + calculateLateralSurface(baseRadius, topRadius,
                        height) + SEPARATOR + calculateSurfaceLine(baseRadius,
                        topRadius, height);
        System.out.println(output);
    }

    /**
     * Calculates the volume of a truncated cone based on the passed data.
     * Volume = h*PI / 3 * ((R)^2 + R*r + (r)^2)
     *
     * @param baseRadius R base radius of the truncated cone
     * @param topRadius r top radius of the truncated cone
     * @param height h height of the truncated cone
     * @return the calculated volume of a truncated cone with the given
     *         dimensions as a double
     */
    public static double calculateVolume(final int baseRadius,
            final int topRadius, final int height) {
        return height * PI / 3 * (Math.pow(baseRadius, 2)
                                  + baseRadius * topRadius + Math
                                          .pow(topRadius, 2));
    }

    /**
     * Calculates the surface of a truncated cone based on the passed data.
     * Surface = (PI * (R)^2) + (PI * (r)^2) + lateral surface for lateral
     * surface see {@link #calculateLateralSurface(int, int, int)}
     *
     * @param baseRadius R base radius of the truncated cone
     * @param topRadius r top radius of the truncated cone
     * @param height h height of the truncated cone
     * @return the calculated surface of a truncated cone with the given
     *         dimensions as a double
     */
    public static double calculateSurface(final int baseRadius,
            final int topRadius, final int height) {
        return (PI * Math.pow(baseRadius, 2)) + (PI * Math.pow(topRadius, 2))
               + calculateLateralSurface(baseRadius, topRadius, height);
    }

    /**
     * Calculates the lateral surface of a truncated cone based on the passed
     * data. Lateral surface = (R + r) * PI * m for m see {@link
     * #calculateSurfaceLine(int, int, int)}
     *
     * @param baseRadius R base radius of the truncated cone
     * @param topRadius r top radius of the truncated cone
     * @param height h height of the truncated cone
     * @return the calculated lateral surface of a truncated cone with the given
     *         dimensions as a double
     */
    public static double calculateLateralSurface(final int baseRadius,
            final int topRadius, final int height) {
        return (baseRadius + topRadius) * PI * calculateSurfaceLine(baseRadius,
                topRadius, height);
    }

    /**
     * Calculates the length of the surface line of a truncated cone based on
     * the passed data. Length of the surface line = sqrt ( (R - r)^2 + (h)^2 )
     *
     * @param baseRadius R base radius of the truncated cone
     * @param topRadius r top radius of the truncated cone
     * @param height h height of the truncated cone
     * @return the calculated length of the surface line of a truncated cone
     *         with the given dimensions as a double
     */
    public static double calculateSurfaceLine(final int baseRadius,
            final int topRadius, final int height) {
        return Math.sqrt(Math.pow((baseRadius - topRadius), 2) + Math
                .pow(height, 2));
    }

}
