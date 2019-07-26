package edu.kit.informatik.entity;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import edu.kit.informatik.exception.SyntaxException;

// following Effective Java #31 Use instance fields instead of ordinals and #3 Enforce the singleton property with an enum type
// class is immutable!!
public enum Player implements IPositionAware {
    RED("red", 'R', 0, 0),
    BLUE("blue", 'B', 1, 10),
    GREEN("green", 'G', 2, 20),
    YELLOW("yellow", 'Y', 3, 30);
    
    public static final List<Player> ORDERED_PLAYERS;
    
    // use instead of values() if you need a natural ordering
    static {
        ORDERED_PLAYERS = Collections.unmodifiableList(Stream.of(values()).sorted(Comparator.comparing(Player::getIndex)).collect(Collectors.toList()));
    }
        
    private final String mName;
    private final int mIndex;
    private final int mStartPosition;
    private final char mId;
    
    private Player(String pName, char pId, int pIndex, int pStartPosition) {
        this.mName = pName;
        this.mIndex = pIndex;
        this.mStartPosition = pStartPosition;
        this.mId = pId;
    }

    public int getIndex() {
        return mIndex;
    }

    public String getName() {
       return mName;
    }
    
    public int getStartPosition() {
        return this.mStartPosition;
    }
    
    public char getId() {
        return this.mId;
    }
    
    @Override
    public int getAbsolutePosition() {
        return getStartPosition();
    }
    
    public static boolean isValidIdentifier(char pIdentifier) {
        return Stream.of(Player.values()).anyMatch(p -> p.mId == pIdentifier);
    }
    
    public static final Player getPlayerById(char pIdentifier) throws SyntaxException {
        return Stream.of(Player.values()).filter(p -> p.mId == pIdentifier).findAny().orElseThrow(
                SyntaxException.createInstance("Error, a player with the given identifier '" + pIdentifier + "' does not exist."));
    }
    
    // Convenience method; do not pass null or empty string!
    public static final Player getPlayerById(String pIdentifier) throws SyntaxException {
        if(pIdentifier == null) {
            throw new NullPointerException();
        } else if(pIdentifier.isEmpty()) {
            throw new SyntaxException("Error, player id must not be empty!");
        }
        
        return getPlayerById(pIdentifier.charAt(0));
    }

    public static Player getPlayerByIndex(int i) {
        return ORDERED_PLAYERS.stream().skip(i).findFirst().get();
    }
    
    public Player getNextPlayer() {
        Iterator<Player> it = ORDERED_PLAYERS.iterator();
        while(it.hasNext()) {
            if(this == it.next()) {
                return (it.hasNext()) ? it.next() : ORDERED_PLAYERS.get(0);
            }
        }
        
        throw new IllegalStateException("Error, could not find the current player in ordered players list.");   // should not happen
    }
    
    @Override
    public String toString() {
        return getName();
    }

    // is the given index a start position of any player?
    public static boolean isStartPositionIndex(int pAnyPlayersPosition) {
        return Stream.of(values()).filter(player -> player.getStartPosition() == pAnyPlayersPosition).findAny().isPresent();
    }
}
