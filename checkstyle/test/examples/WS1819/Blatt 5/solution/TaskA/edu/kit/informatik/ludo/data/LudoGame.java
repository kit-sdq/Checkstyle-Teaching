/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.ludo.data;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.kit.informatik.ludo.BoardState;
import edu.kit.informatik.ludo.InputException;
import edu.kit.informatik.ludo.PlayerColor;
import edu.kit.informatik.ludo.ui.InteractionStrings;

/**
 * Represents the german version of the Ludo game ("Mensch-Aergere-Dich-Nicht").
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public class LudoGame {
    private static final String GAME_INACTIVE = "GAME_INACTIVE";

    private static final int LEAVING_START_NUMBER = 6;

    private static final int NOT_ROLLED = 0;

    private LudoBoard ludoBoard;
    private PlayerColor activePlayer;

    private int rollResult;
    private SortedSet<Move> possibleMovesSet;

    /**
     * Creates a new LudoGame instance with no game active.
     */
    public LudoGame() {
        abort();
    }

    /**
     * Starts the LudoGame with standard start board state.
     * 
     * @throws InputException
     *             cannot happen
     */
    public void start() throws InputException {
        start(null);
    }

    /**
     * Starts the LudoGame with the given board state.
     * 
     * @param boardState
     *            - the given board state
     * @throws InputException
     *             if board state is illegal
     */
    public void start(final BoardState boardState) throws InputException {
        this.ludoBoard = boardState == null ? new LudoBoard() : new LudoBoard(boardState);
        this.activePlayer = PlayerColor.valuesOrdered()[0];
        }

    /**
     * Returns the active player.
     * 
     * @return the active player
     */
    public PlayerColor getActivePlayer() {
        assert isGameActive();
        return this.activePlayer;
    }

    /**
     * Lets the active player roll the given number.
     * 
     * @param number
     *            - the roll result
     * @return a sorted set of possible moves
     * @throws InputException
     *             if rolling is not possible because a move must be made first
     */
    public SortedSet<Move> roll(final int number) throws InputException {
        assert isGameActive();

        if (this.rollResult != NOT_ROLLED) {
            final boolean ok = this.rollResult == LEAVING_START_NUMBER && this.possibleMovesSet.isEmpty();
            if (!ok) {
                throw new InputException("you already rolled, please move!");
            }
        }

        final SortedSet<Move> possibleMoves = new TreeSet<Move>();

        final SortedSet<Field> fieldsOfPlayer = this.ludoBoard.getFieldsOfPlayer(getActivePlayer());

        final Field startField = this.ludoBoard.getStartField(this.activePlayer);
        if (number == LEAVING_START_NUMBER && !startField.isEmpty()) {
            final Move move = getStartMove(startField, startIndex());
            possibleMoves.add(move);
        } else {
            for (final Field from : fieldsOfPlayer) {
                final Field to = add(from, number);
                if (to != null) {
                    final Move move = new Move(from, to);
                    this.ludoBoard.move(move, false);
                    final boolean success = checkOnly(move);
                    this.ludoBoard.move(move.reverseMove(), false);
                    if (success) {
                        possibleMoves.add(move);
                    }
                }
            }
        }

        this.rollResult = number;
        this.possibleMovesSet = possibleMoves;

        if (possibleMoves.isEmpty() && number != LEAVING_START_NUMBER) {
            this.activePlayer = this.activePlayer.getNext();
            this.rollResult = NOT_ROLLED;
        }

        return possibleMoves;
    }

    private Move getStartMove(final Field startField, final int index) {
        final Field normalField = this.ludoBoard.getNormalFields().get(cycle(index));
        if (normalField.isEmpty() || normalField.getFirstMeepleOnThisField().getColor() != this.activePlayer) {
            return new Move(startField, normalField);
        } else {
            return getStartMove(normalField, index + LEAVING_START_NUMBER);
        }
    }

    private int startIndex() {
        return this.activePlayer.getNumber() * 10;
    }

    private static int cycle(final int index) {
        return index % LudoBoard.NUMBER_OF_NORMAL_FIELDS;
    }

    private int normalize(final int index) {
        final int startIndex = startIndex();
        final int supremum = LudoBoard.NUMBER_OF_NORMAL_FIELDS;
        return index < startIndex ? index + (supremum - startIndex) : index - startIndex;
    }

    private Field add(final Field from, final int toAdd) {
        final PlayerColor color = from.getFirstMeepleOnThisField().getColor();
        if (from.isStartField() && toAdd == LEAVING_START_NUMBER) {
            return this.ludoBoard.getNormalFields().get(startIndex());
        } else if (from.isNumberedField()) {
            final int fromNumber = from.getFieldNumber();
            final int normalized = normalize(fromNumber + toAdd);
            final int supremum = LudoBoard.NUMBER_OF_NORMAL_FIELDS;
            final int cycled = cycle(fromNumber + toAdd);
            if (normalized > normalize(fromNumber) && normalized < supremum) {
                // walking on the normal fields
                return this.ludoBoard.getNormalFields().get(cycled);
            } else if (cycled >= startIndex() 
                    && cycled < startIndex() + LudoBoard.NUMBER_OF_MEEPLES_PER_PLAYER) {
                // walking into the final fields
                final List<Field> finalFields = this.ludoBoard.getFinalFields(color);
                return finalFields.get(cycled - startIndex());
            }
        } else {
            // from is already a final field
            final String fromName = from.getFieldName();
            int index = fromName.charAt(0) - LudoBoard.PREFIX_NAME_FINAL_FIELD;
            index += toAdd;
            if (index < LudoBoard.NUMBER_OF_MEEPLES_PER_PLAYER) {
                return this.ludoBoard.getFinalFields(color).get(index);
            }
        }

        return null; // adding not possible (to field does not exist)
    }

    /**
     * Gets the field represented by the given string.
     * 
     * @param fieldStr
     *            - the given string
     * @return the field represented by the given string
     */
    public Field getField(final String fieldStr) {
        return this.ludoBoard.getField(fieldStr);
    }

    /**
     * Lets the active player move one meeple from the given field.
     * 
     * @param from
     *            - the field from which the move starts
     * @return the field where the move has moved the meeple
     * @throws InputException
     *             if die was not rolled or move is not possible
     */
    public Field move(final Field from) throws InputException {
        assert isGameActive();
        if (this.rollResult != NOT_ROLLED) {
            final Move move = getMove(from);
            if (move != null) {
                final Field to = move.getTo();
                this.ludoBoard.move(move, false);
                final boolean success = beat(move);
                assert success; // move != null --> checkOnly()
                final PlayerColor color = this.activePlayer;
                if (this.rollResult != LEAVING_START_NUMBER || from.equals(this.ludoBoard.getStartField(color))) {
                    this.activePlayer = this.activePlayer.getNext();
                }
                this.rollResult = NOT_ROLLED;
                this.ludoBoard.move(move.reverseMove(), false);
                this.ludoBoard.move(move, true);
                this.possibleMovesSet.clear();
                return to;
            } else {
                throw new InputException("the move is not allowed!"); 
            }
        } else {
            throw new InputException("the die was not rolled before this move!");
        }
    }

    private Move getMove(final Field from) {
        for (final Move move : this.possibleMovesSet) {
            if (move.getFrom().equals(from)) {
                return move;
            }
        }
        return null;
    }

    /*
     * checks if board is in a correct state and triggers the side-effect of beating
     * another player's meeple from the board if it is in the way
     */
    private boolean beat(final Move move) {
        if (checkOnly(move)) {
            final Field to = move.getTo();
            if (to.getCountMeeplesOnThisField() == 2) {
                // beating another player's meeple
                final Meeple newMeeple = to.popFirstMeepleOnThisField();
                final Meeple oldMeeple = to.getFirstMeepleOnThisField();
                final Move backToStart = new Move(to, this.ludoBoard.getStartField(oldMeeple.getColor()));
                this.ludoBoard.move(backToStart, true);
                assert to.getCountMeeplesOnThisField() == 0;
                to.pushMeepleOnThisField(newMeeple);
            }
            return true;
        }
        return false;
    }

    /*
     * checks if board is in a correct state after the given move
     */
    private boolean checkOnly(final Move move) {
        final Field to = move.getTo();
        if (to.getCountMeeplesOnThisField() == 1) {
            return true;
        } else {
            assert to.getCountMeeplesOnThisField() == 2;
            final Meeple newMeeple = to.popFirstMeepleOnThisField();
            final Meeple oldMeeple = to.getFirstMeepleOnThisField();

            to.pushMeepleOnThisField(newMeeple); // restoring state

            return newMeeple.getColor() != oldMeeple.getColor();
        }
    }

    /**
     * Checks if a player has won and returns this player if existing.
     * 
     * @return the player which has won or {@code null} if no player has won
     */
    public PlayerColor checkWin() {
        return this.ludoBoard.checkWin();
    }

    /**
     * Aborts the active game.
     */
    public void abort() {
        assert isGameActive();
        this.ludoBoard = null;
        this.activePlayer = null;
        this.rollResult = NOT_ROLLED;
    }

    /**
     * Determines whether the game is active.
     * 
     * @return whether the game is active
     */
    public boolean isGameActive() {
        return (this.ludoBoard != null);
    }

    @Override
    public String toString() {
        return isGameActive()
                ? this.ludoBoard.toString() + InteractionStrings.OUTPUT_SEPARATOR_OUTER + activePlayer.getColor()
                : GAME_INACTIVE;
    }
}
