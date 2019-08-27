package edu.kit.informatik;

import java.util.Locale;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Command {
    APPROXIMATION("approximation (\\d+)") {
        @Override
        public void execute(MatchResult matcher, EulerianApproximation approximation) throws InputException {
            int n = Integer.parseInt(matcher.group(1));
            double result = approximation.approximate(n);
            result = (double) Math.round(result * 10000.0) / 10000.0;
            Terminal.printLine(String.format(Locale.ENGLISH, "%.4f", result));
        }
    },

    /**
     * The quit command.
     */
    QUIT("quit") {
        @Override
        public void execute(MatchResult matcher, EulerianApproximation approximation) throws InputException {
            this.quit();
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
     * @param approximation The instance of the game to run the command on.
     * @return The command that got executed.
     * @throws InputException if no matching command is found. Contains an error message.
     */
    public static Command executeMatching(String input, EulerianApproximation approximation) throws InputException {
        for (Command command : Command.values()) {
            Matcher matcher = command.pattern.matcher(input);
            if (matcher.matches()) {
                command.execute(matcher, approximation);
                return command;
            }
        }

        throw new InputException("not a valid command!");
    }

    /**
     * Executes a command.
     * 
     * @param matcher The regex matcher that contains the groups of input of the command.
     * @param approximation The instance of the ConnectFive game.
     * @throws InputException if the command contains syntactical or symantical errors.
     */
    public abstract void execute(MatchResult matcher, EulerianApproximation approximation) throws InputException;

    /**
     * Checks if the program is still running or was exited.
     * 
     * @return true if the program is still running, false otherwise.
     */
    public boolean isRunning() {
        return isRunning;
    }
    
    protected void quit() {
        isRunning = false;
    }
}
