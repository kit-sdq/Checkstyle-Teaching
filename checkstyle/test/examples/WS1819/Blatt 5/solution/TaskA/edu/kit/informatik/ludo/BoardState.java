/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.ludo;

import edu.kit.informatik.ludo.data.Field;
import edu.kit.informatik.ludo.data.LudoBoard;
import edu.kit.informatik.ludo.data.Meeple;
import edu.kit.informatik.ludo.ui.InteractionStrings;

/**
 * Represents a board state. This is an adapter class for fitting UI with the
 * data representation.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public class BoardState {
    private final LudoBoard board;

    /**
     * Creates a new BoardState using the given state string.
     * 
     * @param stateString
     *            - the string representing the board state
     * @throws InputException
     *             if meeple sorting is wrong
     */
    public BoardState(final String stateString) throws InputException {
        this.board = new LudoBoard();

        final String[] fieldsPerPlayerStrArr = stateString.split(InteractionStrings.INPUT_SEPARATOR_OUTER.toString());

        final PlayerColor[] colorsOrdered = PlayerColor.valuesOrdered();
        for (int i = 0; i < colorsOrdered.length; i++) {
            final PlayerColor color = colorsOrdered[i];

            // putting meeples on the fields given by the input
            final Field startField = this.board.getStartField(color);
            final String[] fieldsStrArr = fieldsPerPlayerStrArr[i]
                    .split(InteractionStrings.INPUT_SEPARATOR_INNER.toString());
            assert fieldsStrArr.length == startField.getCountMeeplesOnThisField();

            Field before = startField;
            for (final String fieldStr : fieldsStrArr) {
                final Field field = this.board.getField(fieldStr);
                assert field != null;

                // check for wrong sorting
                if (field.compareTo(before) < 0) {
                    throw new InputException("wrong meeple sorting!");
                }

                final Meeple meeple = startField.popFirstMeepleOnThisField();
                field.pushMeepleOnThisField(meeple);

                before = field;
            }
        }
    }

    /**
     * Gets the created board.
     * 
     * @return the created board
     */
    public LudoBoard getBoard() {
        return board;
    }
}
