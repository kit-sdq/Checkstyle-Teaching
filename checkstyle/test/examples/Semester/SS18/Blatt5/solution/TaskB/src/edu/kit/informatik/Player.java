package edu.kit.informatik;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * The Player of a UI of santorini.
 * 
 * @see SantoriniGame
 * 
 * @author Peter Oettig
 * @version 1.0
 */
public class Player {
    private final String name;
    private final Set<Pawn> pawns = new HashSet<>();

    /**
     * Creates a player named {@code name}.
     * 
     * @param name
     *            The new player's name.
     */
    public Player(String name) {
        this.name = name;
    }

    /**
     * Registers {@code pawn} as a pawn of this player. Sets the owner of {@code pawn} to this player.
     * 
     * @param pawn
     *            A new pawn of this player.
     */
    public void addPawn(Pawn pawn) {
        pawns.add(pawn);
        pawn.setOwner(this);
    }

    /**
     * @param pawn
     *            A pawn to check.
     * @return {@code true} only if {@code pawn} is owned by this player.
     */
    public boolean hasPawn(Pawn pawn) {
        return pawns.contains(pawn);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Player player = (Player) o;
        return Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
