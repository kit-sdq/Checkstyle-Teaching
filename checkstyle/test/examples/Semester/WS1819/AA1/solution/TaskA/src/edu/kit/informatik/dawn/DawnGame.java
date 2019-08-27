/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.dawn;

import edu.kit.informatik.dawn.data.*;
import edu.kit.informatik.dawn.enums.GlobalState;
import edu.kit.informatik.dawn.ui.Command;
import edu.kit.informatik.dawn.ui.InteractionStrings;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Thomas Weber
 * @version 1.0
 */
public class DawnGame {
    private Board board;

    private GlobalState globalState;

    private DiceSides recentRoll;

    private MissionControlTokens token;

    public DawnGame() {
        this.board = new Board();
        this.globalState = GlobalState.VESTA;
    }

    public Command[] getAvailableCommands() {
        return globalState.getAvailableCommands();
    }

    public String roll(final DiceSides diceSides) throws InputException {
        recentRoll = diceSides;
        globalState.nextRoundState();
        return InteractionStrings.OK.toString();
    }

    public String print() {
        return board.print();
    }

    public String state(Coordinates coordinates) {
        return board.getCellState(coordinates).toString();
    }

    public String printRow(RowIndex rowIndex) {
        return board.print(rowIndex);
    }

    public String printCol(ColIndex colIndex) {
        return board.print(colIndex);
    }

    public GlobalState getGlobalState() {
        return globalState;
    }

    public String setVC(Coordinates coordinates, BoardState boardState)
            throws InputException {
        String ret = board.placeCell(coordinates, boardState);
        globalState.nextRoundState();
        return ret;
    }

    public String reset() {
        this.board = new Board();
        this.globalState = GlobalState.VESTA;
        this.globalState.reset();
        return InteractionStrings.OK.toString();
    }

    public String move(final Coordinates[] newPosition) throws InputException {
        Coordinates position = board.getPlanet(globalState.getBoardState());
        if (newPosition.length > token.getLength().getLength()) {
            throw new InputException("move contains to many steps!");
        }
        if (newPosition[0].equals(position)) {
            throw new InputException("must move!");
        }
        if (!movePossible(position, newPosition[0], 1)) {
            throw new InputException(
                    "impossible move, cannot reach next " + "step "
                    + newPosition[0].toString());
        }
        for (int index = 1; index < newPosition.length; index++) {
            if (!movePossible(newPosition[index - 1], newPosition[index], 1)) {
                throw new InputException(
                        "impossible move from " + newPosition[index - 1]
                                .toString() + " to " + newPosition[index]
                                .toString());
            }
        }
        if (!position.equals(newPosition[newPosition.length - 1])) {
            board.placeCell(newPosition[newPosition.length - 1],
                    globalState.getBoardState());
            board.placeCellIgnoring(position, BoardState.EMPTY);
        }
        globalState.nextRoundState();
        if (!globalState.stonesAvailable()) {
            switch (globalState) {

                case VESTA:
                    globalState = GlobalState.CERES;
                    break;
                case CERES:
                    globalState = GlobalState.FINISHED;
                    break;
                case FINISHED:
                    throw new InputException("this should not occur!");
            }
        }
        return InteractionStrings.OK.toString();
    }

    private boolean movePossible(final Coordinates start,
            final Coordinates destination, int stepsRemaining)
            throws InputException {
        if (board.getCellState(destination) != BoardState.EMPTY
            && board.getCellState(destination) != globalState.getBoardState()) {
            return false;
        }
        if (start.equals(destination)) {
            return true;
        }
        if (stepsRemaining == 0) {
            return false;
        }
        boolean north = false;
        boolean south = false;
        boolean east = false;
        boolean west = false;
        if (RowIndex.isOnBoard(start.getRowIndex().getIndex() - 1)) {
            north = movePossible(new Coordinates(
                    new RowIndex(start.getRowIndex().getIndex() - 1),
                    start.getColIndex()), destination, stepsRemaining - 1);
        }
        if (RowIndex.isOnBoard(start.getRowIndex().getIndex() + 1)) {
            south = movePossible(new Coordinates(
                    new RowIndex(start.getRowIndex().getIndex() + 1),
                    start.getColIndex()), destination, stepsRemaining - 1);
        }
        if (ColIndex.isOnBoard(start.getColIndex().getIndex() - 1)) {
            west = movePossible(new Coordinates(start.getRowIndex(),
                            new ColIndex(start.getColIndex().getIndex() - 1)),
                    destination, stepsRemaining - 1);
        }
        if (ColIndex.isOnBoard(start.getColIndex().getIndex() + 1)) {
            east = movePossible(new Coordinates(start.getRowIndex(),
                            new ColIndex(start.getColIndex().getIndex() + 1)),
                    destination, stepsRemaining - 1);
        }
        return north || south || east || west;
    }

    private Coordinates getPlanetPosition() throws InputException {
        return board.getPlanet(globalState.getBoardState());
    }

    public String place(String x1, String y1, String x2, String y2)
            throws InputException {
        if (recentRoll == DiceSides.DAWN) {
            return place(new Coordinates(new OuterRowIndex(x1),
                            new OuterColIndex(y1)),
                    new Coordinates(new OuterRowIndex(x2),
                            new OuterColIndex(y2)));
        }
        return place(new Coordinates(new RowIndex(x1), new ColIndex(y1)),
                new Coordinates(new RowIndex(x2), new ColIndex(y2)));

    }

    private String place(final Coordinates start, final Coordinates end)
            throws InputException {
        DiceSides stone = MissionControlTokens.getDiceSides(start, end);
        token = globalState.isAvailable(recentRoll, stone);
        Direction direction = MissionControlTokens.getDirection(start, end);
        place(Coordinates.min(start, end), Coordinates.max(start, end),
                direction);
        globalState.removeStone(stone);
        globalState.nextRoundState();
        if (reachableFieldsCount(board.getPlanet(globalState.getBoardState()),
                new HashSet<>(), globalState.getBoardState()).size() == 0) {
            globalState.nextRoundState();
        }
        return InteractionStrings.OK.toString();
    }

    private void place(final Coordinates start, final Coordinates end,
            Direction direction) throws InputException {
        switch (direction) {
            case HORIZONTAL:
                for (int index = start.getColIndex().getIndex();
                     index <= end.getColIndex().getIndex(); index++) {
                    board.placeCell(new Coordinates(start.getRowIndex(),
                                    new OuterColIndex(index)),
                            BoardState.MISSIONCONTROL);
                }
                break;
            case VERTICAL:
                for (int index = start.getRowIndex().getIndex();
                     index <= end.getRowIndex().getIndex(); index++) {
                    board.placeCell(new Coordinates(new OuterRowIndex(index),
                            start.getColIndex()), BoardState.MISSIONCONTROL);
                }
                break;
        }
    }

    public String showResult() throws InputException {
        return String.valueOf(reachableFieldsCount());
    }

    private int reachableFieldsCount() throws InputException {
        int fc = reachableFieldsCount(board.getPlanet(BoardState.CERES),
                new HashSet<>(), BoardState.CERES).size();
        int fv = reachableFieldsCount(board.getPlanet(BoardState.VESTA),
                new HashSet<>(), BoardState.VESTA).size();
        return Math.max(fc, fv) + (Math.max(fc, fv) - Math.min(fc, fv));
    }

    private Set<Coordinates> reachableFieldsCount(final Coordinates position,
            Set<Coordinates> reachableFields, BoardState testedState)
            throws InputException {
        switch (board.getCellState(position)) {

            case EMPTY:
                if (reachableFields.add(position)) {
                    expand(position, reachableFields, testedState);
                }
                break;
            case CERES:
                if (testedState == BoardState.CERES) {
                    expand(position, reachableFields, testedState);
                }
                break;
            case VESTA:
                if (testedState == BoardState.VESTA) {
                    expand(position, reachableFields, testedState);
                }
                break;
            case MISSIONCONTROL:
                return reachableFields;
        }
        return reachableFields;
    }

    private Set<Coordinates> expand(final Coordinates position,
            Set<Coordinates> reachableFields, BoardState testedState)
            throws InputException {
        if (RowIndex.isOnBoard(position.getRowIndex().getIndex() - 1)) {
            reachableFieldsCount(new Coordinates(
                    new RowIndex(position.getRowIndex().getIndex() - 1),
                    position.getColIndex()), reachableFields, testedState);
        }
        if (RowIndex.isOnBoard(position.getRowIndex().getIndex() + 1)) {
            reachableFieldsCount(new Coordinates(
                    new RowIndex(position.getRowIndex().getIndex() + 1),
                    position.getColIndex()), reachableFields, testedState);
        }
        if (ColIndex.isOnBoard(position.getColIndex().getIndex() - 1)) {
            reachableFieldsCount(new Coordinates(position.getRowIndex(),
                            new ColIndex(position.getColIndex().getIndex() - 1)),
                    reachableFields, testedState);
        }
        if (ColIndex.isOnBoard(position.getColIndex().getIndex() + 1)) {
            reachableFieldsCount(new Coordinates(position.getRowIndex(),
                            new ColIndex(position.getColIndex().getIndex() + 1)),
                    reachableFields, testedState);
        }
        return reachableFields;
    }

}
