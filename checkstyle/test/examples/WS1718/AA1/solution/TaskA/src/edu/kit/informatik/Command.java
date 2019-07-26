package edu.kit.informatik;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Command {
    /**
     * The place command.
     */
    PLACE("place (-?\\d+);(-?\\d+);(-?\\d+);(-?\\d+)") {
        @Override
        public void execute(MatchResult matcher, ConnectSix connectSix) throws InputException {
            int i1 = Integer.parseInt(matcher.group(1));
            int j1 = Integer.parseInt(matcher.group(2));
            int i2 = Integer.parseInt(matcher.group(3));
            int j2 = Integer.parseInt(matcher.group(4));
            connectSix.checkPlacement(i1, j1);
            connectSix.checkPlacement(i2, j2);
            Terminal.printLine(connectSix.place(i1, j1, i2, j2));
        }
    },

    /**
     * The print command.
     */
    ROWPRINT("rowprint (-?\\d+)") {
        @Override
        public void execute(MatchResult matcher, ConnectSix connectSix) throws InputException {
            Terminal.printLine(connectSix.rowprint(Integer.parseInt(matcher.group(1))));
        }
    },

    /**
     * The print command.
     */
    COLPRINT("colprint (-?\\d+)") {
        @Override
        public void execute(MatchResult matcher, ConnectSix connectSix) throws InputException {
            Terminal.printLine(connectSix.colprint(Integer.parseInt(matcher.group(1))));
        }
    },

    /**
     * The print command.
     */
    PRINT("print") {
        @Override
        public void execute(MatchResult matcher, ConnectSix connectSix) throws InputException {
            Terminal.printLine(connectSix.print());
        }
    },

    /**
     * The state command.
     */
    STATE("state (-?\\d+);(-?\\d+)") {
        @Override
        public void execute(MatchResult matcher, ConnectSix connectSix) throws InputException {
            int i = Integer.parseInt(matcher.group(1));
            int j = Integer.parseInt(matcher.group(2));
            Terminal.printLine(connectSix.state(i, j));
        }
    },
    
    RESET("reset") {
        @Override
        public void execute(MatchResult matcher, ConnectSix connectSix) throws InputException {
            connectSix.reset();
            Terminal.printLine("OK");
        }
    },

    /**
     * The quit command.
     */
    QUIT("quit") {
        @Override
        public void execute(MatchResult matcher, ConnectSix connectSix) throws InputException {
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
     * @param connectSix The instance of the game to run the command on.
     * @return The command that got executed.
     * @throws InputException if no matching command is found. Contains an error message.
     */
    public static Command executeMatching(String input, ConnectSix connectSix) throws InputException {
        for (Command command : Command.values()) {
            Matcher matcher = command.pattern.matcher(input);
            if (matcher.matches()) {
                command.execute(matcher, connectSix);
                return command;
            }
        }

        throw new InputException("not a valid command!");
    }

    /**
     * Executes a command.
     * 
     * @param matcher The regex matcher that contains the groups of input of the command.
     * @param connectSix The instance of the ConnectFive game.
     * @throws InputException if the command contains syntactical or symantical errors.
     */
    public abstract void execute(MatchResult matcher, ConnectSix connectSix) throws InputException;

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
