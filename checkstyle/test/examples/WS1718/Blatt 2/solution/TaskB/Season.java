/**
 * Represents a season of a {@link Series}. Additional to the associated series, it has a name and a number.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 */
public class Season {
    private String name;
    private int number;
    private Series series;

    /**
     * Constructs a Season of the given {@code series} with the given {@code number}. All other attributes remain
     * uninitialized.
     * 
     * @param series
     *            The series this season is a part of.
     * @param number
     *            This season's number.
     */
    public Season(Series series, int number) {
        this.series = series;
        this.number = number;
    }
}
