package edu.kit.informatik.UI;

import edu.kit.informatik.Color;
import edu.kit.informatik.ColorConversionException;
import edu.kit.informatik.Mastermind;
import edu.kit.informatik.Terminal;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Command {
    // (?i) means match case insensitive.
    GUESS("(?i)(?:" + Color.getRegex() + " ){3}" + Color.getRegex()) {
        @Override
        public void execute(MatchResult matcher, Mastermind mastermind) throws InputException {
            List<Color> colors;
            try {
                colors = Color.convertStringsToColors(matcher.group().split(" "));
            } catch (ColorConversionException exception) {
                Terminal.printError("one of the colors does not exist.");
                return;
            }

            Terminal.printLine(mastermind.checkGuess(colors));
        }
    },

    /**
     * The quit command.
     */
    QUIT("quit") {
        @Override
        public void execute(MatchResult matcher, Mastermind mastermind) throws InputException {
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
     * @param mastermind The instance of the game to run the command on.
     * @return The command that got executed.
     * @throws InputException if no matching command is found. Contains an error message.
     */
    public static Command executeMatching(String input, Mastermind mastermind) throws InputException {
        for (Command command : Command.values()) {
            Matcher matcher = command.pattern.matcher(input);
            if (matcher.matches()) {
                command.execute(matcher, mastermind);

                if (!mastermind.isGameOver() && !command.equals(QUIT)) {
                    Terminal.printLine(mastermind.getPrompt());
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
     * @param mastermind The instance of the ConnectFive game.
     * @throws InputException if the command contains syntactical or symantical errors.
     */
    public abstract void execute(MatchResult matcher, Mastermind mastermind) throws InputException;

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
