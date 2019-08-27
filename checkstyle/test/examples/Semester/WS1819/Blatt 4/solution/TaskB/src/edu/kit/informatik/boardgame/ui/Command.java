/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.boardgame.ui;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.boardgame.InputException;
import edu.kit.informatik.boardgame.board.ConnectFive;

/**
 * Encapsulates the available commands with description.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public enum Command {
    /**
     * The place command. This command places a token at the given position
     * if possible.
     */
    PLACE("place (\\d);(\\d)") {
        @Override
        public void execute(String parameters, ConnectFive connectFive)
                throws InputException {
            int y = Integer.parseInt(parameters.split(";")[0]);
            int x = Integer.parseInt(parameters.split(";")[1]);
            Terminal.printLine(connectFive.place(y, x));
        }
    },

    /**
     * The print command. This command prints the whole board as stated in
     * the task.
     */
    print("print") {
        @Override
        public void execute(String parameters, ConnectFive connectFive) {
            Terminal.printLine(connectFive.print());
        }
    },

    /**
     * The state command. Prints the state of the board cell with given
     * coordinates.
     */
    STATE("state (\\d);(\\d)") {
        @Override
        public void execute(String parameters, ConnectFive connectFive)
                throws InputException {
            int y = Integer.parseInt(parameters.split(";")[0]);
            int x = Integer.parseInt(parameters.split(";")[1]);
            Terminal.printLine(connectFive.state(y, x));
        }
    },

    /**
     * The turn-right command. Turns the board quarter with the given index
     * 90 degrees to the right.
     */
    TURN_RIGHT("turn-right (\\d)") {
        @Override
        public void execute(String parameters, ConnectFive connectFive)
                throws InputException {
            int numberOfQuadrant = Integer.parseInt(parameters);
            Terminal.printLine(connectFive.turnRight(numberOfQuadrant));
        }
    },

    /**
     * The turn-left command. Turns the board quarter with the given index
     * 90 degrees to the left.
     */
    TURN_LEFT("turn-left (\\d)") {
        @Override
        public void execute(String parameters, ConnectFive connectFive)
                throws InputException {
            int numberOfQuadrant = Integer.parseInt(parameters);
            Terminal.printLine(connectFive.turnLeft(numberOfQuadrant));
        }
    },

    /**
     * The token command. Prints the coordinates of all tokens of the Player
     * with the given number.
     */
    TOKEN("token (P)([1-2])") {
        @Override
        public void execute(String parameters, ConnectFive connectFive) {
            int playerNumber = Integer.parseInt(parameters.substring(1));
            Terminal.printLine(connectFive.token(playerNumber));
        }
    },


    /**
     * The quit command. Finishes the program.
     */
    QUIT("quit") {
        @Override
        public void execute(String parameters, ConnectFive connectFive) {
            quit();
        }
    };

    /**
     * Contains the game state.
     */
    private boolean isRunning;

    /**
     * Contains this commands input format.
     */
    private String pattern;

    /**
     * Constructs a new command.
     *
     * @param pattern The regex pattern to use for command validation
     *         and processing.
     */
    Command(String pattern) {
        this.isRunning = true;
        this.pattern = pattern;
    }

    /**
     * Checks an input against all available commands and calls the command if
     * one is found.
     *
     * @param input The user input.
     * @param connectFive The instance of the game to run the command
     *         on.
     * @return The command that got executed.
     * @throws InputException if no matching command is found. Contains
     *         an error message.
     */
    public static Command executeMatching(String input, ConnectFive connectFive)
            throws InputException {
        for (Command command : Command.values()) {
            if (input.matches(command.pattern)) {
                command.execute(input.substring(input.indexOf(" ") + 1),
                        connectFive);
                return command;
            }
        }

        throw new InputException("not a valid command!");
    }

    /**
     * Executes a command.
     *
     * @param parameters String containing parameters for the command.
     * @param connectFive The instance of the ConnectFive game.
     * @throws InputException if the command contains syntactical or
     *         symantical errors.
     */
    public abstract void execute(String parameters, ConnectFive connectFive)
            throws InputException;

    /**
     * Checks if the program is still running or was exited.
     *
     * @return true if the program is still running, false otherwise.
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Exits the program gracefully.
     */
    protected void quit() {
        isRunning = false;
    }
}
