package edu.kit.informatik;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Command {
    /**
     * The place command.
     */
    PLACE("place (-?\\d+);(-?\\d+)") {
        @Override
        public void execute(MatchResult matcher, ConnectFive connectFive) throws InputException {
            int i = Integer.parseInt(matcher.group(1));
            int j = Integer.parseInt(matcher.group(2));
            Terminal.printLine(connectFive.place(i, j));
        }
    },

    /**
     * The print command.
     */
    print("print") {
        @Override
        public void execute(MatchResult matcher, ConnectFive connectFive) throws InputException {
            Terminal.printLine(connectFive.print());
        }
    },

    /**
     * The state command.
     */
    STATE("state (-?\\d+);(-?\\d+)") {
        @Override
        public void execute(MatchResult matcher, ConnectFive connectFive) throws InputException {
            int i = Integer.parseInt(matcher.group(1));
            int j = Integer.parseInt(matcher.group(2));
            Terminal.printLine(connectFive.state(i, j));
        }
    },

    /**
     * The quit command.
     */
    QUIT("quit") {
        @Override
        public void execute(MatchResult matcher, ConnectFive connectFive) throws InputException {
            isRunning = false;
        }
    };
    
    private static boolean isRunning = true;
    private Pattern pattern;

    /**
     * Constructs a new command.
     * 
     * @param pattern The regex pattern to use for command validation and processing.
     */
    Command(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    /**
     * Checks an input against all available commands and calls the command if one is found.
     * 
     * @param input The user input.
     * @param connectFive The instance of the game to run the command on.
     * @return The command that got executed.
     * @throws InputException if no matching command is found. Contains an error message.
     */
    public static Command executeMatching(String input, ConnectFive connectFive) throws InputException {
        for (Command command : Command.values()) {
            Matcher matcher = command.pattern.matcher(input);
            if (matcher.matches()) {
                command.execute(matcher, connectFive);
                return command;
            }
        }

        throw new InputException("not a valid command!");
    }

    /**
     * Executes a command.
     * 
     * @param matcher The regex matcher that contains the groups of input of the command.
     * @param connectFive The instance of the ConnectFive game.
     * @throws InputException if the command contains syntactical or symantical errors.
     */
    public abstract void execute(MatchResult matcher, ConnectFive connectFive) throws InputException;

    /**
     * Checks if the program is still running or was exited.
     * 
     * @return true if the program is still running, false otherwise.
     */
    public boolean isRunning() {
        return isRunning;
    }
}
