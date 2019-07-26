/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.dawn.enums;

import edu.kit.informatik.dawn.InputException;
import edu.kit.informatik.dawn.ui.Command;

/**
 * @author Thomas Weber
 * @version 1.0
 */
public enum RoundState {
    SETVC(new Command[]{Command.SETVC}), ROLL(new Command[]{Command.ROLL}),
    PLACE(new Command[]{Command.PLACE}), MOVE(new Command[]{Command.MOVE});

    private Command[] availableCommand;

    RoundState(final Command[] availableCommand) {
        this.availableCommand = availableCommand;
    }

    public Command[] getAvailableCommand() {
        return availableCommand;
    }

    public RoundState nextState(RoundState previous) throws InputException {
        switch (previous) {

            case SETVC:
                return ROLL;
            case ROLL:
                return PLACE;
            case PLACE:
                return MOVE;
            case MOVE:
                return ROLL;
        }
        throw new InputException("State modelling was incomplete");
    }
}
