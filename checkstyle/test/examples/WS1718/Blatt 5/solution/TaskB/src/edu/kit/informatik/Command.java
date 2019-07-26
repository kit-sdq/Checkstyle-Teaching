package edu.kit.informatik;

//As util isn't allowed on this sheet, this would usually be rejected.
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Command {
    /**
     * The add-team command to add a team.
     */
    ADD_TEAM("add-team (\\d+);([^\\n;]+)") {
        @Override
        public void execute(MatchResult matcher, DELTracker delTracker) throws InputException {
            int id = Integer.parseInt(matcher.group(1));
            
            delTracker.addTeam(id, matcher.group(2));
            Terminal.printLine("OK");
        }
    },

    /**
     * The list-team command to list all teams.
     */
    LIST_TEAM("list-team") {
        @Override
        public void execute(MatchResult matcher, DELTracker delTracker) throws InputException {
            Terminal.printLine(delTracker.listTeams());
        }
    },

    /**
     * The add-ice-hockey-match command to add a new icehockey match.
     */
    ADD_ICE_HOCKEY_MATCH("add-ice-hockey-match (\\d+);(\\d+);(\\d+);(\\d+);(\\d+)") {
        @Override
        public void execute(MatchResult matcher, DELTracker delTracker) throws InputException {
        int homeID = Integer.parseInt(matcher.group(1));
            int homeGoals = Integer.parseInt(matcher.group(2));
            int guestID = Integer.parseInt(matcher.group(3));
            int guestGoals = Integer.parseInt(matcher.group(4));
            int time = Integer.parseInt(matcher.group(5));
            
            delTracker.addMatch(homeID, homeGoals, guestID, guestGoals, time);
            Terminal.printLine("OK");
        }
    },

    /**
     * The print-del-standings command to print the standings of the DEL league.
     */
    LIST_ICE_HOCKEY_MATCH("print-del-standings") {
        @Override
        public void execute(MatchResult matcher, DELTracker delTracker) throws InputException {
            Terminal.printLine(delTracker.listStandings());
        }
    },

    /**
     * The quit command to exit the program.
     */
    QUIT("quit") {
        @Override
        public void execute(MatchResult matcher, DELTracker delTracker) throws InputException {
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
     * @param delTracker The instance of the tracker to run the command on.
     * @return The command that got executed.
     * @throws InputException if no matching command is found. Contains an error message.
     */
    public static Command executeMatching(String input, DELTracker delTracker) throws InputException {
        for (Command command : Command.values()) {
            Matcher matcher = command.pattern.matcher(input);
            if (matcher.matches()) {
                command.execute(matcher, delTracker);
                return command;
            }
        }

        throw new InputException("not a valid command!");
    }

    /**
     * Executes a command.
     *
     * @param matcher The regex matcher that contains the groups of input of the command.
     * @param delTracker The instance of the DEL tracker.
     * @throws InputException if the command contains syntactical or symantical errors.
     */
    public abstract void execute(MatchResult matcher, DELTracker delTracker) throws InputException;

    /**
     * Checks if the program is still running or was exited.
     *
     * @return true if the program is still running, false otherwise.
     */
    public boolean isRunning() {
        return isRunning;
    }
}
