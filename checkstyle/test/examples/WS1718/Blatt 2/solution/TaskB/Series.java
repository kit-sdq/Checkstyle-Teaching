/**
 * Models a simple series, having a name, a director and a genre.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 */
public class Series {
    private String name;
    private Person director;
    private Genre genre;

    /**
     * Constructs a Series with the provided {@code name}. All other attributes remain uninitialized.
     * 
     * @param name
     *            The series' name.
     */
    public Series(String name) {
        this.name = name;
    }
}
