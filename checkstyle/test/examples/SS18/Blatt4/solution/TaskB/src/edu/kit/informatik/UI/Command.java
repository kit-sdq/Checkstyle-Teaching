package edu.kit.informatik.UI;

import edu.kit.informatik.RPSSLGame;
import edu.kit.informatik.Shape;
import edu.kit.informatik.Terminal;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Command {
    /**
     * The input command that accepts the shape input for the move of the player.
     */
    INPUT("(?i)" + Shape.getCapturingRegex()) {
        @Override
        public void execute(MatchResult matcher, RPSSLGame rpssl) throws InputException {
            if (rpssl.isGameOver()) {
                throw new InputException("the game is already over.");
            }
            
            String shapeString = matcher.group(1);
            Shape shape = Shape.valueOf(shapeString.toUpperCase());
            Terminal.printLine(rpssl.evaluate(shape).displayResult());
            
            if (rpssl.isGameOver()) {
                Terminal.printLine(rpssl.displayResult());
            }
        }
    },

    /**
     * The quit command.
     */
    QUIT("quit") {
        @Override
        public void execute(MatchResult matcher, RPSSLGame rpssl) throws InputException {
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
     * @param rpssl The instance of the game to run the command on.
     * @return The command that got executed.
     * @throws InputException if no matching command is found. Contains an error message.
     */
    public static Command executeMatching(String input, RPSSLGame rpssl) throws InputException {
        for (Command command : Command.values()) {
            Matcher matcher = command.pattern.matcher(input);
            if (matcher.matches()) {
                command.execute(matcher, rpssl);
                
                if (!rpssl.isGameOver() && !command.equals(QUIT)) {
                    Terminal.printLine(rpssl.getPrompt());
                }
                
                return command;
            }
        }

        throw new InputException("not a valid command!");
    }

    /**
     * Executes a command.
     * 
     * @param matcher The regex matcher that contains the groups of input of the command.
     * @param rpssl The instance of the ConnectFive game.
     * @throws InputException if the command contains syntactical or symantical errors.
     */
    public abstract void execute(MatchResult matcher, RPSSLGame rpssl) throws InputException;

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
