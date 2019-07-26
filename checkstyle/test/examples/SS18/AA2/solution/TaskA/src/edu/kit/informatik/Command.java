package edu.kit.informatik;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Command {
    CREATE("create ([a-z]+)") {
        @Override
        public void execute(Matcher matcher, Database database) throws InputException {
            Terminal.printLine(database.create(matcher.group(1)));
        }
    },
    RESET("reset ([a-z]+)") {
        @Override
        public void execute(Matcher matcher, Database database) throws InputException {
            Terminal.printLine(database.reset(matcher.group(1)));
        }
    },
    ADD("add ([a-z]+);(u[a-z]{4});(\\d+)") {
        @Override
        public void execute(Matcher matcher, Database database) throws InputException {
            Terminal.printLine(database.add(matcher.group(1), matcher.group(2), Integer.parseInt(matcher.group(3))));
        }
    },
    MODIFY("modify ([a-z]+);(u[a-z0-9]{4});(\\d+)") {
        @Override
        public void execute(Matcher matcher, Database database) throws InputException {
            Terminal.printLine(database.modify(matcher.group(1), matcher.group(2), Integer.parseInt(matcher.group(3))));
        }
    },
    DELETE("delete ([a-z]+);(u[a-z0-9]{4})") {
        @Override
        public void execute(Matcher matcher, Database database) throws InputException {
            Terminal.printLine(database.delete(matcher.group(1), matcher.group(2)));
        }
    },
    CREDITS("credits ([a-z]+);(u[a-z0-9]{4})") {
        @Override
        public void execute(Matcher matcher, Database database) throws InputException {
            Terminal.printLine(database.credits(matcher.group(1), matcher.group(2)));
        }
    },
    PRINT("print ([a-z]+)") {
        @Override
        public void execute(Matcher matcher, Database database) throws InputException {
            Terminal.printLine(database.print(matcher.group(1)));
        }
    },
    AVERAGE("average ([a-z]+)") {
        @Override
        public void execute(Matcher matcher, Database database) throws InputException {
            //Terminal.printLine(matcher.group(1) + ";" + database.average(matcher.group(1)));
            Terminal.printLine(database.average(matcher.group(1)));
        }
    },
    MEDIAN("median ([a-z]+)") {
        @Override
        public void execute(Matcher matcher, Database database) throws InputException {
            Terminal.printLine(database.median(matcher.group(1)));
        }
    },
    QUIT("quit") {
        @Override
        public void execute(Matcher matcher, Database database) {
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
     * @param database The instance of the game to run the command on.
     * @return The command that got executed.
     * @throws InputException if no matching command is found. Contains an error message.
     */
    public static Command executeMatching(String input, Database database) throws InputException {
        for (Command command : Command.values()) {

            Matcher matcher = command.pattern.matcher(input);
            if (matcher.matches()) {
                command.execute(matcher, database);
                return command;
            }
        }

        throw new InputException("not a valid command!");
    }

    /**
     * Executes a command.
     *
     * @param matcher The regex matcher that contains the groups of input of the command.
     * @param database The instance of the ConnectFive game.
     * @throws InputException if the command contains syntactical or symantical errors.
     */
    public abstract void execute(Matcher matcher, Database database) throws InputException;

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
