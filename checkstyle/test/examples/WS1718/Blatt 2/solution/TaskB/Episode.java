/**
 * One episode of a {@link Season}. Additional to its associated season, it has a name, a number, a {@link Date} of
 * first broadcast and a main actor.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 */
public class Episode {
    private Season season;
	private String name;
    private int number;
    private Date firstBroadcast;
    private Person mainActor;

    /**
     * Constructs an Episode of the given {@code season} with the given {@code number}. All other attributes remain
     * uninitialized.
     * 
     * @param season
     *            The season this episode is a part of.
     * @param number
     *            This episode's number.
     */
    public Episode(Season season, int number) {
        this.season = season;
        this.number = number;
    }
}
