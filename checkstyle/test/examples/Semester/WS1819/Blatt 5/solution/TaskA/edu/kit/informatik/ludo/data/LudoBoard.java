/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.ludo.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.StringJoiner;
import java.util.TreeSet;

import edu.kit.informatik.ludo.BoardState;
import edu.kit.informatik.ludo.InputException;
import edu.kit.informatik.ludo.PlayerColor;
import edu.kit.informatik.ludo.ui.InteractionStrings;

/**
 * Represents the german version of the Ludo board.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public class LudoBoard {
    /**
     * The number of meeples per player.
     */
    public static final int NUMBER_OF_MEEPLES_PER_PLAYER = 4;

    /**
     * The prefix char of the first final field.
     */
    public static final char PREFIX_NAME_FINAL_FIELD = 'A';

    /**
     * The prefix char of the start fields.
     */
    public static final char PREFIX_NAME_START_FIELD = 'S';

    /**
     * The number of normal numbered fields.
     */
    protected static final int NUMBER_OF_NORMAL_FIELDS = 40;

    private final List<Field> normalFields;
    private final Map<PlayerColor, Field> startFieldMap;
    private final Map<PlayerColor, List<Field>> finalFieldsMap;

    private final Map<PlayerColor, SortedSet<Field>> playerToFieldsMap;

    /**
     * Creates a new LudoBoard with standard start board state.
     */
    public LudoBoard() {
        this.normalFields = new ArrayList<Field>();
        this.startFieldMap = new HashMap<>();
        this.finalFieldsMap = new HashMap<>();
        this.playerToFieldsMap = new HashMap<>();
        initialize();
        initializePlayerToFieldsMap();
    }

    /**
     * Creates a new LudoBoard with the given board state.
     * 
     * @param boardState
     *            - the given board state
     * @throws InputException
     *             if board state is illegal
     */
    public LudoBoard(final BoardState boardState) throws InputException {
        Objects.requireNonNull(boardState);
        final LudoBoard board = boardState.getBoard();
        this.normalFields = board.normalFields;
        this.startFieldMap = board.startFieldMap;
        this.finalFieldsMap = board.finalFieldsMap;
        this.playerToFieldsMap = new HashMap<>();
        initializePlayerToFieldsMap();
        check();
    }

    private void initialize() {
        // creating normal fields
        for (int i = 0; i < NUMBER_OF_NORMAL_FIELDS; i++) {
            final Field field = new Field(i);
            this.normalFields.add(field);
        }

        final PlayerColor[] orderedColors = PlayerColor.valuesOrdered();
        for (final PlayerColor color : orderedColors) {
            // creating start field
            final Field startField = new Field("" + PREFIX_NAME_START_FIELD + color.getPattern());
            this.startFieldMap.put(color, startField);

            final List<Field> finalFields = new ArrayList<>();

            // filling start field and creating final fields
            for (int i = 0; i < NUMBER_OF_MEEPLES_PER_PLAYER; i++) {
                final Meeple meeple = new Meeple(color, i);
                startField.pushMeepleOnThisField(meeple);

                final char prefix = (char) (PREFIX_NAME_FINAL_FIELD + i);
                final Field finalField = new Field("" + prefix + color.getPattern());
                finalFields.add(finalField);
            }

            this.finalFieldsMap.put(color, finalFields);
        }
    }

    private void initializePlayerToFieldsMap() {
        final PlayerColor[] orderedColors = PlayerColor.valuesOrdered();
        for (final PlayerColor color : orderedColors) {
            final SortedSet<Field> set = new TreeSet<>();

            final Field startField = this.startFieldMap.get(color);
            if (!startField.isEmpty()) {
                set.add(startField);
            }

            for (final Field field : getNormalFields()) {
                if (!field.isEmpty() && field.getFirstMeepleOnThisField().getColor() == color) {
                    set.add(field);
                }
            }

            for (final Field field : getFinalFields(color)) {
                if (!field.isEmpty()) {
                    set.add(field);
                }
            }

            this.playerToFieldsMap.put(color, set);
        }
    }

    private void check() throws InputException {
        if (checkWin() != null) {
            throw new InputException("at least one player has already won!");
        }

        final Set<Field> allOneMeepleFields = new HashSet<Field>(this.normalFields);

        // checking for wrong meeples in homes and finals
        for (final PlayerColor color : PlayerColor.valuesOrdered()) {
            final Field startField = this.startFieldMap.get(color);
            if (!startField.isEmpty() && startField.getFirstMeepleOnThisField().getColor() != color) {
                throwWrongMeeple();
            }

            final List<Field> finalFields = this.finalFieldsMap.get(color);
            for (final Field field : finalFields) {
                allOneMeepleFields.add(field);
                if (!field.isEmpty() && field.getFirstMeepleOnThisField().getColor() != color) {
                    throwWrongMeeple();
                }
            }
        }

        // checking for more than one meeple on the same field
        for (final Field field : allOneMeepleFields) {
            if (field.getCountMeeplesOnThisField() > 1) {
                throwWrongMeeple();
            }
        }
    }

    private static void throwWrongMeeple() throws InputException {
        throw new InputException("at least one wrong meeple set!");
    }

    /**
     * Checks if a player has won and returns this player if existing.
     * 
     * @return the player which has won or {@code null} if no player has won
     */
    protected PlayerColor checkWin() {
        for (final PlayerColor player : PlayerColor.valuesOrdered()) {
            final List<Field> finalFields = getFinalFields(player);
            boolean won = true;
            inner: for (final Field field : finalFields) {
                won = won && !field.isEmpty();
                if (!won) {
                    break inner;
                }
            }

            if (won) {
                return player;
            }
        }

        return null;
    }

    /**
     * Gets the normal numbered fields.
     * 
     * @return the normal numbered fields
     */
    public List<Field> getNormalFields() {
        return new ArrayList<>(normalFields);
    }

    /**
     * Gets the start field of the given player.
     * 
     * @param color
     *            - the given player's color
     * @return the start field
     */
    public Field getStartField(final PlayerColor color) {
        return this.startFieldMap.get(color);
    }

    /**
     * Gets teh final fields of the given player.
     * 
     * @param color
     *            - the given player's color
     * @return a sorted list of the player's final fields
     */
    public List<Field> getFinalFields(final PlayerColor color) {
        return new ArrayList<>(this.finalFieldsMap.get(color));
    }

    /**
     * Gets the field represented by the given string.
     * 
     * @param fieldStr
     *            - the given string
     * @return the field represented by the given string
     */
    public Field getField(final String fieldStr) {
        if (fieldStr.matches("(\\d){1,2}")) {
            final int fieldNumber = Integer.parseInt(fieldStr);
            return getNormalFields().get(fieldNumber);
        } else {
            final String colorPattern = fieldStr.substring(1);
            final PlayerColor color = PlayerColor.ofPattern(colorPattern);
            assert color != null;
            if (fieldStr.startsWith("" + LudoBoard.PREFIX_NAME_START_FIELD)) {
                return getStartField(color);
            } else {
                final int indexFinalField = fieldStr.charAt(0) - LudoBoard.PREFIX_NAME_FINAL_FIELD;
                return getFinalFields(color).get(indexFinalField);
            }
        }
    }

    /**
     * Gets all fields of the given player.
     * 
     * @param color
     *            - the given player's color
     * @return a sorted set of all fields of the given player
     */
    public SortedSet<Field> getFieldsOfPlayer(final PlayerColor color) {
        return new TreeSet<Field>(playerToFieldsMap.get(color));
    }

    /**
     * Performs the given move on the board.
     * 
     * @param move
     *            - the given move
     * @param persistentMove
     *            - whether this move should be a persistent move, i.e. can be
     *            reverted
     */
    public void move(final Move move, final boolean persistentMove) {
        final Field from = move.getFrom();
        assert !from.isEmpty();
        final Meeple meeple = move.getFrom().popFirstMeepleOnThisField();
        final PlayerColor color = meeple.getColor();
        final Field to = move.getTo();
        to.pushMeepleOnThisField(meeple);

        if (persistentMove) {
            if (!from.isStartField() || from.isEmpty()) {
                this.playerToFieldsMap.get(color).remove(from);
            }
            this.playerToFieldsMap.get(color).add(to);
        }
    }

    @Override
    public String toString() {
        final StringJoiner joinerOuter = new StringJoiner(InteractionStrings.OUTPUT_SEPARATOR_OUTER.toString());

        final PlayerColor[] orderedColors = PlayerColor.valuesOrdered();
        for (final PlayerColor color : orderedColors) {
            final StringJoiner joinerInner = new StringJoiner(InteractionStrings.OUTPUT_SEPARATOR_INNER.toString());

            for (final Field field : this.playerToFieldsMap.get(color)) {
                for (int i = 0; i < field.getCountMeeplesOnThisField(); i++) {
                    joinerInner.add(field.toString());
                }
            }

            joinerOuter.add(joinerInner.toString());
        }

        return joinerOuter.toString();
    }
}
