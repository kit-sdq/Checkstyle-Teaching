package edu.kit.informatik.UI;

import edu.kit.informatik.GameState;
import edu.kit.informatik.SantoriniGame;
import edu.kit.informatik.Terminal;
import edu.kit.informatik.exceptions.IllegalMoveException;
import edu.kit.informatik.exceptions.InputException;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Command {
    /**
     * The move command.
     */
    MOVE("move (" + Main.STRING_PATTERN + ");(\\d);(\\d)") {
        @Override
        public void execute(MatchResult matcher, SantoriniGame game) throws IllegalMoveException {
            String pawnName = matcher.group(1);
            int row = Integer.parseInt(matcher.group(2));
            int column = Integer.parseInt(matcher.group(3));

            game.assertState(GameState.BEFORE_MOVE);
            game.move(pawnName, row, column);

            if (game.isWon()) {
                Terminal.printLine(game.getWinner() + " wins");
            } else {
                Terminal.printLine("OK");
            }
        }
    },

    /**
     * The build command.
     */
    BUILD("build (C|D);(\\d);(\\d)") {
        @Override
        public void execute(MatchResult matcher, SantoriniGame game) throws IllegalMoveException {
            String pieceSymbol = matcher.group(1);
            int row = Integer.parseInt(matcher.group(2));
            int column = Integer.parseInt(matcher.group(3));

            game.assertState(GameState.IN_MOVE);

            if (pieceSymbol.equals("D")) {
                game.buildDome(row, column);
            } else {
                game.buildCuboid(row, column);
            }

            Terminal.printLine("OK");
        }
    },

    /**
     * The cellprint command.
     */
    CELLPRINT("cellprint (\\d);(\\d)") {
        @Override
        public void execute(MatchResult matcher, SantoriniGame game) throws IllegalMoveException {
            int row = Integer.parseInt(matcher.group(1));
            int column = Integer.parseInt(matcher.group(2));

            Terminal.printLine(game.getCellPrint(row, column));
        }
    },

    /**
     * The turn command.
     */
    TURN("turn") {
        @Override
        public void execute(MatchResult matcher, SantoriniGame game) throws IllegalMoveException {
            game.assertState(GameState.AFTER_MOVE);

            Terminal.printLine(game.nextPlayer());
        }
    },

    /**
     * The quit command.
     */
    QUIT("quit") {
        @Override
        public void execute(MatchResult matcher, SantoriniGame game) {
            this.quit();
        }
    };
    
    private boolean isRunning;
    private final Pattern pattern;

    /**
     * Constructs a new command.
     * 
     * @param pattern The regex pattern to use for command validation and processing.
     */
    Command(String pattern) {
        this.isRunning = true;
        this.pattern = Pattern.compile(pattern);
    }
    
    /**
     * Checks an input against all available commands and calls the command if one is found.
     * 
     * @param input The user input.
     * @param game The instance of the game to run the command on.
     * @return The command that got executed.
     * @throws InputException if no matching command is found. Contains an error message.
     * @throws IllegalMoveException if the move that the player tried to do is not valid.
     */
    public static Command executeMatching(String input, SantoriniGame game)
            throws InputException, IllegalMoveException {
        for (Command command : Command.values()) {
            Matcher matcher = command.pattern.matcher(input);
            if (matcher.matches()) {
                command.execute(matcher, game);                
                return command;
            }
        }

        throw new InputException("not a valid command!");
    }

    /**
     * Executes a command.
     * 
     * @param matcher The regex matcher that contains the groups of input of the command.
     * @param game The instance of the ConnectFive game.
     * @throws IllegalMoveException if the command contains syntactical or symantical errors.
     */
    public abstract void execute(MatchResult matcher, SantoriniGame game) throws IllegalMoveException;

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
