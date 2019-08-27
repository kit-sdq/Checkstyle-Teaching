package edu.kit.informatik.searchformisterx.main;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.searchformisterx.data.Direction;
import edu.kit.informatik.searchformisterx.data.Hop;
import edu.kit.informatik.searchformisterx.data.Path;
import edu.kit.informatik.searchformisterx.data.Tile;
import edu.kit.informatik.searchformisterx.exception.InputException;
import edu.kit.informatik.searchformisterx.exception.InvalidParameterException;
import edu.kit.informatik.searchformisterx.game.Game;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Command {

    /**
     * Starts the game with the given token if possible.
     */
    START("start " + Main.TOKEN_REGEX) {
        @Override
        public void execute(MatchResult matcher, Game game)
                throws InvalidParameterException {
            Terminal.printLine(game.start(matcher.group(1)));
        }
    },


    /**
     * Places the given token at the given position if possible.
     */
    PLACE("place " + Main.TOKEN_REGEX + Main.SEPARATOR + Direction
            .getAllDirectionsRegex() + Main.SEPARATOR + Main.TOKEN_REGEX) {
        @Override
        public void execute(MatchResult matcher, Game game)
                throws InputException, InvalidParameterException {
            Tile moveTile = Tile.getTile(matcher.group(1));
            Direction direction = Direction.fromString(matcher.group(5));
            Tile targetTile = Tile.getTile(matcher.group(6));
            if (moveTile == null || targetTile == null) {
                Terminal.printError("given tile is not valid");
            } else {
                Terminal.printLine(
                        game.place(moveTile, new Hop(direction, targetTile)));
            }
        }
    },

    /**
     * Moves the given token over the given path if possible.
     */
    MOVE("move " + Main.TOKEN_REGEX + "(" + Main.SEPARATOR + Direction
            .getAllDirectionsRegex() + Main.SEPARATOR + Main.TOKEN_REGEX + ")"
         + "+") {
        @Override
        public void execute(MatchResult matcher, Game game)
                throws InputException, InvalidParameterException {
            Tile moveTile = Tile.getTile(matcher.group(1));
            // remove the command "move ", the target tile and the
            // preceding whitespace
            String path = matcher.group(0).substring(
                    "move ".length() + matcher.group(1).length() + 1);
            Terminal.printLine(game.move(moveTile, new Path(moveTile, path)));
        }
    },

    /**
     * Passes if possible.
     */
    PASS("pass") {
        @Override
        public void execute(MatchResult matcher, Game game)
                throws InvalidParameterException {
            Terminal.printLine(game.pass());
        }
    },

    /**
     * Prints the board
     */
    PRINT("print") {
        @Override
        public void execute(MatchResult matcher, Game game)
                throws InvalidParameterException {
            String output = game.print();
            if (!output.equals("")) {
                Terminal.printLine(output);
            }
        }
    },

    /**
     * The quit command.
     */
    QUIT("quit") {
        @Override
        public void execute(MatchResult matcher, Game game) {
            this.quit();
        }
    };


    private boolean isRunning;

    private Pattern pattern;


    /**
     * Constructs a new command.
     *
     * @param pattern The regex pattern to use for command validation
     *         and processing.
     */
    Command(String pattern) {
        this.isRunning = true;
        this.pattern = Pattern.compile(pattern);
    }

    /**
     * Checks an input against all available commands and calls the command if
     * one is found.
     *
     * @param input The user input.
     * @param game The instance of the game to run the command on.
     * @return The command that got executed.
     * @throws InputException if no matching command is found. Contains
     *         an error message.
     * @throws InvalidParameterException if the command contains
     *         semantic errors
     */
    public static Command executeMatching(String input, Game game)
            throws InputException, InvalidParameterException {
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
     * @param matcher The regex matcher that contains the groups of
     *         input of the command.
     * @param game The instance of the ConnectFive game.
     * @throws InputException if the command contains syntactical
     *         errors.
     * @throws InvalidParameterException if the command contains
     *         semantic errors
     */
    public abstract void execute(MatchResult matcher, Game game)
            throws InputException, InvalidParameterException;

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
