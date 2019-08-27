package edu.kit.informatik.ludo.rules;

import edu.kit.informatik.ludo.IBoard;
import edu.kit.informatik.ludo.fields.Field;
import edu.kit.informatik.ludo.fields.Move;
import edu.kit.informatik.ludo.fields.Player;

/**
 * Enum holding specific rules for a board.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/07/14
 */
public enum Rule implements IRule {
    /** Represents the standard behavior. */
    BASE {
        
        /**
         * Allows to move if the destination field does not contain a token of the same player. If a {@link
         * IBoard#MOVE_OUT} is rolled, a token may be moved out of the base.
         */
        @Override public Move allowed(final IBoard board, final Field start) {
            final Player player = board.player();
            if (board.owner(start) == player) {
                final int r = board.lastRolled();
                final Move move = start.move(player, start.isBase() ? r == IBoard.MOVE_OUT ? 1 : 0 : r);
                if (move != null && board.owner(move.to()) != player) {
                    return move;
                }
            }
            return super.allowed(board, start);
        }
        
        /**
         * Allows to move again if and only if the move did not move a token out of the base and the rolled value was
         * {@link IBoard#MOVE_AGAIN}.
         */
        @Override public boolean moveAgain(final IBoard board, final Move move) {
            if (!move.from().isBase() && move.distance() == IBoard.MOVE_AGAIN) {
                return true;
            }
            return super.moveAgain(board, move);
        }
        
        /**
         * Allows a token on usable fields. For base fields up to four tokens are allowed.
         */
        @Override public boolean allows(final IBoard board, final Field field) {
            final Player owner = board.owner(field);
            if (owner == null || field.isUsableBy(owner) && board.count(field) <= (field.isBase() ? 4 : 1)) {
                return true;
            }
            return super.allows(board, field);
        }
        
        /**
         * Allows to roll again if and only if the rolled value was {@link IBoard#MOVE_AGAIN}.
         */
        @Override public boolean rollAgain(final IBoard board) {
            if (board.lastRolled() == IBoard.MOVE_AGAIN) {
                return true;
            }
            return super.rollAgain(board);
        }
    },
    /** The optional rule BACKWARD as described in the task. */
    BACKWARD {
        
        /**
         * Allows to move backward if and only if the token can beat a token of an other player with this move. The
         * token may not move over the start-field of its owner.
         */
        @Override public Move allowed(final IBoard board, final Field start) {
            final Player player = board.player();
            if (board.owner(start) == player) {
                final Move move = start.move(player, -board.lastRolled());
                if (move != null) {
                    final Player other = board.owner(move.to());
                    if (other != null && other != player) {
                        return move;
                    }
                }
            }
            return super.allowed(board, start);
        }
    },
    /** The optional rule BARRIER as described in the task. */
    BARRIER {
        
        /**
         * Allows to move a second token onto a {@linkplain Field#isNormal() normal} field which has already an own
         * token placed onto it.
         */
        @Override public Move allowed(final IBoard board, final Field start) {
            final Player player = board.player();
            if (!start.isBase() && board.owner(start) == player) {
                final Move move = start.move(player, board.lastRolled());
                if (move != null) {
                    final Field to = move.to();
                    if (to.isNormal() && board.owner(to) == player && board.count(to) == 1) {
                        return move;
                    }
                }
            }
            return super.allowed(board, start);
        }
        
        /**
         * Blocks movements if any of the fields to move over has a barrier (2 or more tokens) placed onto it.
         */
        @Override public boolean blocks(final IBoard board, Move move) {
            if (move.fields().skip(1).anyMatch((f) -> board.count(f) >= 2)) {
                return true;
            }
            return super.blocks(board, move);
        }
        
        /**
         * Allows two tokens of the same player to be placed on {@linkplain Field#isNormal() normal} fields.
         */
        @Override public boolean allows(final IBoard board, final Field field) {
            final Player owner = board.owner(field);
            if (owner != null && field.isUsableBy(owner) && field.isNormal() && board.count(field) == 2) {
                return true;
            }
            return super.allows(board, field);
        }
    },
    /** The optional rule NOJUMP as described in the task. */
    NOJUMP {
        
        /**
         * Blocks movements on goal-fields if any of the fields to move over has a token placed onto it.
         */
        @Override public boolean blocks(final IBoard board, Move move) {
            if (move.fields().skip(1).anyMatch((f) -> f.isGoal() && board.count(f) >= 1)) {
                return true;
            }
            return super.blocks(board, move);
        }
    },
    /** The optional rule TRIPLY as described in the task. */
    TRIPLY {
        
        /**
         * Allows to roll up to 3 times if and only if all tokens of the player are either in the base or on goal fields
         * and none of the tokens can move with the rolled value.
         */
        @Override public boolean rollAgain(final IBoard board) {
            if (board.rollCount() < 3 && board.positions(board.player()).allMatch((f) -> f.isBase() || f.isGoal())) {
                return true;
            }
            return super.rollAgain(board);
        }
    };
}
