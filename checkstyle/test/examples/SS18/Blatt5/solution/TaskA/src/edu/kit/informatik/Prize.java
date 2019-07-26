package edu.kit.informatik;

import edu.kit.informatik.UI.IllegalFileFormatException;

/**
 * This is an enum of possible prizes behind gates that can have a string identifier for output und string conversion.
 *
 * @author Peter Oettig
 * @version 1.0
 */
public enum Prize {
    /**
     * A car as prize.
     */
    CAR("Auto"),
    /**
     * A goat as prize.
     */
    GOAT("Ziege");

    private final String identifier;

    /**
     * The constructor for a new prize.
     *
     * @param identifier The string identifier used for conversion of a string to a prize and for output.
     */
    Prize(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Converts a string identifier to a prize object. The identifier is defined at enum object instantiation.
     *
     * @param identifier The string that is to be converted to a prize object.
     * @return The enum representation of the string
     * @throws IllegalFileFormatException if there is no prize with this identifier.
     */
    public static Prize fromIdentifier(String identifier) throws IllegalFileFormatException {
        for (Prize prize : Prize.values()) {
            if (prize.identifier.equals(identifier)) {
                return prize;
            }
        }
        
        throw new IllegalFileFormatException("no prize with this identifier exists.");
    }
}
