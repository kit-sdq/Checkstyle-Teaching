package edu.kit.informatik.entity.board;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import edu.kit.informatik.entity.Player;
import edu.kit.informatik.entity.IPositionAware;
import edu.kit.informatik.exception.SyntaxException;

// we translate into field indices
public final class FieldIdentifier implements IPositionAware, Comparable<FieldIdentifier> {
    // TODO explain why this makes sense
    public static final int START_POSITION_INDEX = -1;
    
    private final int mIndex;
    private final String mIdentifierString;
    
    public FieldIdentifier(String pIdentifier) throws SyntaxException {
        this.mIdentifierString = pIdentifier;
        
        if(pIdentifier == null) {
            throw new NullPointerException("Error, the identifier must not be null.");
        } else if(pIdentifier.isEmpty()) {
            throw new SyntaxException("Error, you must specify a non-empty identifier.");
        }
        
        int index = 0;
        try {
            index = Integer.parseInt(pIdentifier);
            
            if(index < 0 || index >= Board.SIZE) {
                throw new SyntaxException("Error, illegal field identifier (as index): " + index + ".");
            }
        } catch(NumberFormatException e) {
            if(pIdentifier.length() < 2) {
                throw new SyntaxException(
                        "Error, the given field identifier '" + pIdentifier + "' is invalid- not an Integer and too short for a special id");
            }
            
            if(isStartIdentifier(pIdentifier)) {
                index = START_POSITION_INDEX;
            } else if(isDestinationIdentifier(pIdentifier)) {
                index = Board.SIZE - 1 + pIdentifier.charAt(0) - 64;
            } else {
                throw new SyntaxException("Error, the given field identifier '" + pIdentifier + "' is invalid.");
            }
        }
        
        this.mIndex = index;
    }
    
    public int getIndex() {
        return this.mIndex;
    }
    
    public String getName() {
        return this.mIdentifierString;
    }
    
    // returns null if player not available
    public Player extractPlayer() {
        if(this.mIndex < 0 || this.mIndex >= Board.SIZE) {
            try {
                return Player.getPlayerById(this.mIdentifierString.charAt(1));
            } catch (SyntaxException e) {
                // should not happen
                return null;
            }
        } else {
            return null;
        }
    }
    
    public boolean isDestinationIdentifier() {
        return isDestinationIdentifier(this.mIndex);
    }
    
    public static boolean isDestinationIdentifier(int pIndex) {
        return pIndex >= Board.SIZE;
    }
    
    @Override
    public String toString() {
        return getName();
    }
    
    public static final boolean isDestinationIdentifier(String pIdentifier) {
        return (pIdentifier.charAt(0) >= 65 && pIdentifier.charAt(0) <= 68) && Player.isValidIdentifier(pIdentifier.charAt(1));
    }
    
    public static final boolean isStartIdentifier(String pIdentifier) {
        return (pIdentifier.charAt(0) == 'S') && Player.isValidIdentifier(pIdentifier.charAt(1));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + mIndex;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FieldIdentifier other = (FieldIdentifier) obj;
        if (mIndex != other.mIndex)
            return false;
        return true;
    }

    public static Map<Player, FieldIdentifier[]> fromCliArgument(String[] pArguments) throws SyntaxException {
        if(pArguments.length == 0) {
            throw new SyntaxException("Error, need a string of identifiers to parse.");
        }
        
        return parseFieldIdentifiers(pArguments[pArguments.length - 1]);
    }
    
    public static Map<Player, FieldIdentifier[]> parseFieldIdentifiers(String pIdentifiersString) throws SyntaxException {
        String argument = pIdentifiersString;
        EnumMap<Player, FieldIdentifier[]> identifiers = new EnumMap<>(Player.class);
        
        String[] playersIdentifiers = argument.split(";", -1);
        if(playersIdentifiers.length != Player.values().length) {
            throw new SyntaxException("Error, expected the position argument to contain exactly 4 section; each player one!");
        }
        
        for(int i = 0; i < playersIdentifiers.length; i++) {
            String cPlayerIdentifiers = playersIdentifiers[i];
            String[] cIds = cPlayerIdentifiers.split(",");
            if(cIds.length != Board.DESTINATION_FIELD_NUMBER) {
                throw new SyntaxException("Error, expected exactly 4 positions, but got: " + cIds.length + " for index " + i + ".");
            }
            
            FieldIdentifier[] cFieldIdentifiers = new FieldIdentifier[Board.DESTINATION_FIELD_NUMBER];
            for(int j = 0; j < cFieldIdentifiers.length; j++) {                
                cFieldIdentifiers[j] = new FieldIdentifier(cIds[j]);
                
                /* check if the fields are ordered ascending */
                if(j > 0 && cFieldIdentifiers[j].compareTo(cFieldIdentifiers[j - 1]) < 0) {
                    throw new SyntaxException("Error, the fields must be sorted ascendent. This is not the case for player at position " + i + ".");
                }
            }
            
            /* the following ensures that the arguments are ordered by player as required by the task */
            identifiers.put(Player.getPlayerByIndex(i), cFieldIdentifiers);
        }
        
        return identifiers;
    }

    @Override
    public int getAbsolutePosition() {
        return this.mIndex;
    }

    @Override
    public int compareTo(FieldIdentifier pOtherIdentifier) {
        return this.mIndex - pOtherIdentifier.mIndex;
    }
}
