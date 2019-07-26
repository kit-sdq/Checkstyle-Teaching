package edu.kit.informatik.UI;

import edu.kit.informatik.MontyHallGame;
import edu.kit.informatik.Terminal;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Command {
    /**
     * The gate input command that accepts the selected gate.
     */
    GATE_INPUT("([123])") {
        @Override
        public void execute(MatchResult matcher, MontyHallGame game) throws InputException {
            if (!game.isInSelectState()) {
                throw new InputException("there is no selection expected now.");
            }
            
            int gateNumber = Integer.parseInt(matcher.group(1));
            game.selectGate(gateNumber);
            Terminal.printLine("Moderator - Hinter Tor " + game.getGoatGate() + " befindet sich eine Ziege.");
        }
    },

    /**
     * The change input command that accepts the yes or no decision for changing the gate.
     */
    CHANGE_INPUT("(ja|nein)") {
        @Override
        public void execute(MatchResult matcher, MontyHallGame game) throws InputException {
            if (!game.isInChangeState()) {
                throw new InputException("there is no decision expected now.");
            }

            String decisionString = matcher.group(1);
            if (decisionString.equals("ja")) {
                game.decideAndEnd(true);
            } else {
                game.decideAndEnd(false);
            }
            
            game.evaluateResult();
            Terminal.printLine(game.getResultString());
            if (!game.nextRun()) {
                Terminal.printLine("Simulation beendet");
                this.quit();
            }
        }
    },

    /**
     * The quit command.
     */
    QUIT("quit") {
        @Override
        public void execute(MatchResult matcher, MontyHallGame game) throws InputException {
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
     * @param game The instance of the game to run the command on.
     * @return The command that got executed.
     * @throws InputException if no matching command is found. Contains an error message.
     */
    public static Command executeMatching(String input, MontyHallGame game) throws InputException {
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
     * @throws InputException if the command contains syntactical or symantical errors.
     */
    public abstract void execute(MatchResult matcher, MontyHallGame game) throws InputException;

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
