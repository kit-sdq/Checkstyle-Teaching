package edu.kit.informatik.ludo;

import java.util.stream.Stream;

import edu.kit.informatik.ludo.fields.Field;
import edu.kit.informatik.ludo.fields.Move;
import edu.kit.informatik.ludo.fields.Player;
import edu.kit.informatik.ludo.rules.IRule;

/**
 * Interface for a board.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/07/14
 */
public interface IBoard {
    
    /** The lowest roll-able value. */
    int LOWEST_ROLL = 1;
    /** The highest roll-able value. */
    int HIGHEST_ROLL = 6;
    /** The value required to move out of the base. */
    int MOVE_OUT = 6;
    /** The value that allows to move again. */
    int MOVE_AGAIN = 6;
    
    /**
     * Returns whether a game is currently in process.
     * 
     * @return {@code true} if a game is in process, {@code false} otherwise
     */
    boolean inProcess();
    
    /**
     * Returns the current phase of the game.
     * 
     * @return the current phase
     */
    Phase phase();
    
    /**
     * Starts a new game with the default rules.
     */
    default void start() {
        start(Stream.empty(), Stream.empty());
    }
    
    /**
     * Starts a new game with the provided rules and starting situation.
     * 
     * @param rules the rules
     * @param positions the starting situation
     */
    void start(final Stream<? extends IRule> rules, final Stream<? extends Stream<? extends Field>> positions);
    
    /**
     * Aborts the current game.
     */
    void abort();
    
    /**
     * Returns the active player.
     * 
     * @return the active player
     */
    Player player();
    
    /**
     * Returns the last rolled value, a value between {@value #LOWEST_ROLL} and {@value #HIGHEST_ROLL}.
     * 
     * @return the last rolled value
     */
    int lastRolled();
    
    /**
     * Returns how often the active player has rolled.
     * 
     * @return the roll count
     */
    int rollCount();
    
    /**
     * Rolls the provided value and returns the possible moves.
     * 
     * @param  value the value to roll, has to be between {@value #LOWEST_ROLL} and {@value #HIGHEST_ROLL}
     * @return the possible moves
     */
    Stream<Move> roll(final int value);
    
    /**
     * Rolls a random value between {@value #LOWEST_ROLL} and {@value #HIGHEST_ROLL} and returns the possible moves.
     * 
     * @return the possible moves
     */
    default Stream<Move> rollx() {
        return roll(LOWEST_ROLL + (int) (Math.random() * (1 + HIGHEST_ROLL - LOWEST_ROLL)));
    }
    
    /**
     * Moves from {@code start} to {@code end}.
     * 
     * @param  start the start position
     * @param  end the end position
     * @return the active player, or the winner in case that the move leads to a win
     */
    Player move(final Field start, final Field end);
    
    /**
     * Returns whether the active player can do the provided move.
     * 
     * @param  start the start field
     * @param  end the end field
     * @return {@code true} if the move is possible, {@code false} otherwise
     */
    boolean canMove(final Field start, final Field end);
    
    /**
     * Returns the fields the provided player has his tokens placed onto.
     * 
     * @param  player the player
     * @return the fields
     */
    Stream<Field> positions(final Player player);
    
    /**
     * Returns the fields the player have their tokens placed onto.
     * 
     * @return the fields
     */
    default Stream<Stream<Field>> positions() {
        return Player.stream().map(this::positions);
    }
    
    /**
     * Returns the owner of the field.
     * 
     * @param  field the field
     * @return the owner
     */
    Player owner(final Field field);
    
    /**
     * Returns the count of tokens on the specific field.
     * 
     * @param  field the field
     * @return the count of tokens
     */
    int count(final Field field);
}
