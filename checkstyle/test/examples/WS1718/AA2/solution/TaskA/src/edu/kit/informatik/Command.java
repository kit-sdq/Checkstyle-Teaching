package edu.kit.informatik;

import java.util.Collections;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Command {
    ADD_ADMIN("add-admin (" + Constants.STRING_PATTERN + "+);(" + Constants.STRING_PATTERN + "+);(" + Constants.STRING_PATTERN + "{4,8});(" + Constants.STRING_PATTERN + "{8,12})") {
        @Override
        public void execute(MatchResult matcher, OlympicGames olympicGames) throws InputException {
            olympicGames.addAdmin(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4));
            Terminal.printLine("OK");
        }
    },

    LOGIN_ADMIN("login-admin (" + Constants.STRING_PATTERN + "{4,8});(" + Constants.STRING_PATTERN + "{8,12})") {
        @Override
        public void execute(MatchResult matcher, OlympicGames olympicGames) throws InputException {
            olympicGames.loginAdmin(matcher.group(1), matcher.group(2));
            Terminal.printLine("OK");
        }
    },

    LOGOUT_ADMIN("logout-admin") {
        @Override
        public void execute(MatchResult matcher, OlympicGames olympicGames) throws InputException {
            olympicGames.logoutAdmin();
            Terminal.printLine("OK");
        }
    },

    ADD_SPORTS_VENUE("add-sports-venue (\\d{3});(" + Constants.STRING_PATTERN + "+);(" + Constants.STRING_PATTERN + "+);(" + Constants.STRING_PATTERN + "+);(\\d{4});(\\d+)") {
        @Override
        public void execute(MatchResult matcher, OlympicGames olympicGames) throws InputException {
            int date = Integer.parseInt(matcher.group(5));
            int number = Integer.parseInt(matcher.group(6));
            olympicGames.addSportsVenue(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), date, number);
            Terminal.printLine("OK");
        }
    },

    LIST_SPORTS_VENUE("list-sports-venues (" + Constants.STRING_PATTERN + "+)") {
        @Override
        public void execute(MatchResult matcher, OlympicGames olympicGames) throws InputException {
            List<Venue> venues = olympicGames.getVenues(matcher.group(1));
            Collections.sort(venues);
            for (int i = 0; i < venues.size(); i++) {
                Terminal.printLine("(" + (i + 1) + " " + venues.get(i).toString() + ")");
            }
        }
    },

    ADD_OLYMPIC_SPORTS("add-olympic-sport (" + Constants.STRING_PATTERN + "+);(" + Constants.STRING_PATTERN + "+)") {
        @Override
        public void execute(MatchResult matcher, OlympicGames olympicGames) throws InputException {
            olympicGames.addSport(matcher.group(1), matcher.group(2));
            Terminal.printLine("OK");
        }
    },

    LIST_OLYMPIC_SPORTS("list-olympic-sports") {
        @Override
        public void execute(MatchResult matcher, OlympicGames olympicGames) throws InputException {
            List<Discipline> disciplines = olympicGames.getDisciplines();
            Collections.sort(disciplines);
            for (Discipline discipline : disciplines) {
                Terminal.printLine(discipline.toString());
            }
        }
    },

    ADD_IOC_CODE("add-ioc-code (\\d{3});([a-z]{3});(" + Constants.STRING_PATTERN + "+);(\\d{4})") {
        @Override
        public void execute(MatchResult matcher, OlympicGames olympicGames) throws InputException {
            olympicGames.addCountry(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4));
            Terminal.printLine("OK");
        }
    },

    LIST_IOC_CODE("list-ioc-codes") {
        @Override
        public void execute(MatchResult matcher, OlympicGames olympicGames) throws InputException {
            List<Country> countries = olympicGames.getCountries();
            Collections.sort(countries);
            for (int i = 0; i < countries.size(); i++) {
                Terminal.printLine(countries.get(i).toString());
            }
        }
    },

    ADD_ATHLETE("add-athlete (\\d+{4});(" + Constants.STRING_PATTERN + "+);(" + Constants.STRING_PATTERN + "+);(" + Constants.STRING_PATTERN + "+);(" + Constants.STRING_PATTERN + "+);(" + Constants.STRING_PATTERN + "+)") {
        @Override
        public void execute(MatchResult matcher, OlympicGames olympicGames) throws InputException {
            olympicGames.addAthlete(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5), matcher.group(6));
            Terminal.printLine("OK");
        }
    },

    SUMMARY_ATHLETES("summary-athletes (" + Constants.STRING_PATTERN + "+);(" + Constants.STRING_PATTERN + "+)") {
        @Override
        public void execute(MatchResult matcher, OlympicGames olympicGames) throws InputException {
            String result = olympicGames.printAthleteRaking(matcher.group(1), matcher.group(2));
            if (!result.isEmpty()) {
                Terminal.printLine(result);
            }
        }
    },

    ADD_COMPETITION("add-competition (\\d+{4});(\\d+{4});(" + Constants.STRING_PATTERN + "+);(" + Constants.STRING_PATTERN + "+);(" + Constants.STRING_PATTERN + "+);([01]);([01]);([01])") {
        @Override
        public void execute(MatchResult matcher, OlympicGames olympicGames) throws InputException {
            int year = Integer.parseInt(matcher.group(2));
            if (year < 1926 || year > 2018 || year % 4 != 2) {
                throw new InputException("the year is not a year with olympic winter games.");
            }

            int gold = Integer.parseInt(matcher.group(6));
            int silver = Integer.parseInt(matcher.group(7));
            int bronze = Integer.parseInt(matcher.group(8));
            if (gold + silver + bronze > 1) {
                throw new InputException("illegal medal input, an athlete can only win one medal.");
            }

            olympicGames.addCompetition(matcher.group(1), year, matcher.group(3), matcher.group(4), matcher.group(5), gold, silver, bronze);
            Terminal.printLine("OK");
        }
    },

    OLYMPIC_MEDAL_TABLE("olympic-medal-table") {
        @Override
        public void execute(MatchResult matcher, OlympicGames olympicGames) throws InputException {
            String result = olympicGames.printTable();
            if (!result.isEmpty()) {
                Terminal.printLine(result);
            }
        }
    },
    
    RESET("reset") {
        @Override
        public void execute(MatchResult matcher, OlympicGames olympicGames) throws InputException {
            olympicGames.reset();
            Terminal.printLine("OK");
        }
    },

    /**
     * The quit command.
     */
    QUIT("quit") {
        @Override
        public void execute(MatchResult matcher, OlympicGames olympicGames) throws InputException {
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
     * @param olympicGames The instance of the game to run the command on.
     * @return The command that got executed.
     * @throws InputException if no matching command is found. Contains an error message.
     */
    public static Command executeMatching(String input, OlympicGames olympicGames) throws InputException {
        for (Command command : Command.values()) {
            Matcher matcher = command.pattern.matcher(input);
            if (matcher.matches()) {
                command.execute(matcher, olympicGames);
                return command;
            }
        }

        throw new InputException("not a valid command!");
    }

    /**
     * Executes a command.
     * 
     * @param matcher The regex matcher that contains the groups of input of the command.
     * @param olympicGames The instance of the ConnectFive game.
     * @throws InputException if the command contains syntactical or symantical errors.
     */
    public abstract void execute(MatchResult matcher, OlympicGames olympicGames) throws InputException;

    /**
     * Checks if the program is still running or was exited.
     * 
     * @return true if the program is still running, false otherwise.
     */
    public boolean isRunning() {
        return isRunning;
    }
    
    private static class Constants {
        private static final String STRING_PATTERN = "[^\\nA-Z;]";
    }
}
