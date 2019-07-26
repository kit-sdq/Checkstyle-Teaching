/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.dawn.enums;

import edu.kit.informatik.dawn.InputException;
import edu.kit.informatik.dawn.data.BoardState;
import edu.kit.informatik.dawn.data.DiceSides;
import edu.kit.informatik.dawn.data.MissionControlTokens;
import edu.kit.informatik.dawn.ui.Command;

/**
 * @author Thomas Weber
 * @version 1.0
 */
public enum GlobalState {
    VESTA(new Command[]{Command.STATE, Command.PRINT, Command.RESET,
            Command.QUIT}, BoardState.VESTA),
    CERES(new Command[]{Command.STATE, Command.PRINT, Command.RESET,
            Command.QUIT}, BoardState.CERES), FINISHED(
            new Command[]{Command.STATE, Command.PRINT, Command.RESET,
                    Command.QUIT, Command.SHOWRESULT}, null);

    private RoundState roundState;

    private Command[] availableCommands;

    private MissionControlTokens[] availableStones;

    private BoardState boardState;

    GlobalState(final Command[] availableCommands,
            final BoardState boardState) {
        this.availableCommands = availableCommands;
        this.availableStones = new MissionControlTokens[]{
                new MissionControlTokens(DiceSides.TWO),
                new MissionControlTokens(DiceSides.THREE),
                new MissionControlTokens(DiceSides.FOUR),
                new MissionControlTokens(DiceSides.FIVE),
                new MissionControlTokens(DiceSides.SIX),
                new MissionControlTokens(DiceSides.DAWN)};
        this.boardState = boardState;
        roundState = RoundState.SETVC;
    }

    public void reset() {
        VESTA.roundState = RoundState.SETVC;
        CERES.roundState = RoundState.SETVC;
        this.availableStones = new MissionControlTokens[]{
                new MissionControlTokens(DiceSides.TWO),
                new MissionControlTokens(DiceSides.THREE),
                new MissionControlTokens(DiceSides.FOUR),
                new MissionControlTokens(DiceSides.FIVE),
                new MissionControlTokens(DiceSides.SIX),
                new MissionControlTokens(DiceSides.DAWN)};
    }

    public void nextRoundState() throws InputException {
        roundState = roundState.nextState(roundState);
    }

    public Command[] getAvailableCommands() {
        return getAllCommands();
    }

    private Command[] getAllCommands() {
        Command[] allCommands = new Command[
                roundState.getAvailableCommand().length
                + this.availableCommands.length];
        System.arraycopy(roundState.getAvailableCommand(), 0, allCommands, 0,
                roundState.getAvailableCommand().length);
        System.arraycopy(availableCommands, 0, allCommands,
                roundState.getAvailableCommand().length,
                availableCommands.length);
        return allCommands;
    }

    public BoardState getBoardState() {
        return boardState;
    }

    public MissionControlTokens isAvailable(DiceSides thrown, DiceSides wanted)
            throws InputException {
        if (availableStones[wanted.getIndex()] == null) {
            throw new InputException("stone is not available");
        }
        if (availableStones[thrown.getIndex()] != null) {
            if (thrown != wanted) {
                throw new InputException("cannot choose another length!");
            }
            return availableStones[thrown.getIndex()];
        } else {
            if (thrown.getLength() < wanted.getLength()) {
                for (int index = thrown.getIndex(); index < wanted.getIndex();
                     index++) {
                    if (availableStones[index] != null) {
                        throw new InputException(
                                "stone with index " + index + " is available!");
                    }
                }
            } else {
                for (int index = thrown.getIndex(); index > wanted.getIndex();
                     index--) {
                    if (availableStones[index] != null) {
                        throw new InputException(
                                "stone with index " + index + " is available!");
                    }
                }
            }
            return availableStones[wanted.getIndex()];
        }
    }

    public void removeStone(DiceSides stone) {
        availableStones[stone.getIndex()] = null;
    }

    public boolean stonesAvailable() {
        for (MissionControlTokens sides : availableStones) {
            if (sides != null) {
                return true;
            }
        }
        return false;
    }
}
