package edu.kit.informatik.ludo.rules;

import edu.kit.informatik.ludo.IBoard;
import edu.kit.informatik.ludo.fields.Field;
import edu.kit.informatik.ludo.fields.Move;

/**
 * Interface for a board-rule.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/07/14
 */
public interface IRule {
    
    /**
     * Returns the move allowed by the rule, or {@code null} if the rule allows no move for the specific starting field.
     * 
     * <p>The default-implementation returns {@code null}.
     * 
     * @param  board the board
     * @param  start the starting field
     * @return the move, or {@code null}
     */
    default Move allowed(final IBoard board, final Field start) {
        return null;
    }
    
    /**
     * Returns whether the rule blocks the specific move.
     * 
     * <p>The default-implementation returns {@code false}.
     * 
     * @param  board the board
     * @param  move the move
     * @return {@code true} if the rule blocks the move, {@code false} otherwise
     */
    default boolean blocks(final IBoard board, final Move move) {
        return false;
    }
    
    /**
     * Returns whether the rule allows to move again after processing the provided move.
     * 
     * <p>The default-implementation returns {@code false}.
     * 
     * @param  board the board
     * @param  move the move
     * @return {@code true} if the rule allows the active player to roll and move again, {@code false} otherwise
     */
    default boolean moveAgain(final IBoard board, final Move move) {
        return false;
    }
    
    /**
     * Returns whether the rule allows to roll again if no move is possible with the previous roll.
     * 
     * <p>The default-implementation returns {@code false}.
     * 
     * @param  board the board
     * @return {@code true} if the rule allows the active player to roll again if he can not move, {@code false}
     *         otherwise
     */
    default boolean rollAgain(final IBoard board) {
        return false;
    }
    
    /**
     * Returns whether the rule allows the current state of the field.
     * 
     * <p>The default-implementation returns {@code false}.
     * 
     * @param  board the board
     * @param  field the field
     * @return {@code true} if the rule allows the current state of the field, {@code false} otherwise
     */
    default boolean allows(final IBoard board, final Field field) {
        return false;
    }
}
