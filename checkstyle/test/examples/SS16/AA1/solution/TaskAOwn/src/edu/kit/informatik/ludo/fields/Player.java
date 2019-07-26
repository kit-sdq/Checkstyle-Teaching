package edu.kit.informatik.ludo.fields;

import java.util.Arrays;
import java.util.stream.Stream;

/*
 * Any change to the count of players has to be reflected onto the other classes in this package as they are partly
 * written for exactly four players.
 */
/**
 * Enum holding the different players.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/07/14
 */
public enum Player {
    /** */
    /** The first player.  */ RED    ("red"),
    /** The second player. */ BLUE   ("blue"),
    /** The third player.  */ GREEN  ("green"),
    /** The fourth player. */ YELLOW ("yellow");
    
    private static final Player[] VALUES = values();
    /** Count of players, equal to the value of {@code values().length}. */
            static final int COUNT = VALUES.length;
    
    private final String strRep;
    
    /**
     * Creates a new {@code Player} with the provided parameter.
     * 
     * @param strRep a string representing the player
     */
    Player(final String strRep) {
        this.strRep = strRep;
    }
    
    /**
     * Returns the player with the provided index.
     * 
     * <p>The index is equal to the value returned by the {@link #ordinal()} method.
     * 
     * @param  index the index
     * @return the player with the provided index
     */
    public static Player byIndex(final int index) {
        return VALUES[index];
    }
    
    /**
     * Returns a stream containing all players.
     * 
     * @return a stream containing all players
     */
    public static Stream<Player> stream() {
        return Arrays.stream(VALUES);
    }
    
    /**
     * Returns the player that is active after this player has finished his turn.
     * 
     * @return the next player
     */
    public Player next() {
        return byIndex((ordinal() + 1) % COUNT);
    }
    
    @Override public String toString() {
        return strRep;
    }
}
