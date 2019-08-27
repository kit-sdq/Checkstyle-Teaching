package edu.kit.informatik;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Command {
    PRINT("print") {
        @Override
        public void execute(Matcher matcher, Game game) {
            Terminal.printLine(game.print());
        }
    },
    STATE("state ([0-7]);([0-7])") {
        @Override
        public void execute(Matcher matcher, Game game) {
            Terminal.printLine(game.state(Integer.parseInt(matcher.group(2)), Integer
                    .parseInt(matcher.group(1))));
        }
    },
    TOKEN("token") {
        @Override
        public void execute(Matcher matcher, Game game) {
            Terminal.printLine(game.token());
        }
    },
    THROWIN("throwin ([0-7])") {
        @Override
        public void execute(Matcher matcher, Game game) throws InputException {
            Terminal.printLine(game.throwin(Integer.parseInt(matcher.group(1))));
        }
    },
    FLIP("flip") {
        @Override
        public void execute(Matcher matcher, Game game) throws InputException {
            Terminal.printLine(game.flip());
        }
    },
    REMOVE("remove ([0-7])") {
        @Override
        public void execute(Matcher matcher, Game game) throws InputException {
            Terminal.printLine(game.remove(Integer.parseInt(matcher.group(1))));
        }
    },
    QUIT("quit") {
        @Override
        public void execute(Matcher matcher, Game game) {
            quit();
        }
    };

    private boolean isRunning;
    private Pattern pattern;

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
     */
    public static Command executeMatching(String input, Game game) throws InputException {
        for (Command command : game.getAvailableCommands()) {

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
     * @throws InputException if the command contains syntactical or symantical errors.
     */
    public abstract void execute(Matcher matcher, Game game) throws InputException;

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
