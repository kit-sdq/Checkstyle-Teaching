package edu.kit.informatik.UI;

import edu.kit.informatik.BookingSystem;
import edu.kit.informatik.Currency;
import edu.kit.informatik.Terminal;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Command {
    /**
     * The add command to add a new bookable flight.
     */
    ADD("add ("
            + Command.FLIGHT_ID
            + ");(" + Command.STRING_PATTERN
            + ");(" + Command.STRING_PATTERN
            + ");(" + Command.PRICE
            + ");(" + Currency.getRegex() + ")") {
        @Override
        public void execute(MatchResult matcher, BookingSystem bookingSystem) throws InputException {
            String flightId = matcher.group(1);
            String startingAirport = matcher.group(2);
            String destinationAirport = matcher.group(3);
            double price = Double.parseDouble(matcher.group(4));
            Currency currency = Currency.valueOf(matcher.group(5));

            bookingSystem.addFlight(flightId, startingAirport, destinationAirport, price, currency);
            Terminal.printLine("OK");
        }
    },
    /**
     * The remove command to remove a bookable flight.
     */
    REMOVE("remove (" + Command.FLIGHT_ID + ")") {
        @Override
        public void execute(MatchResult matcher, BookingSystem bookingSystem) throws InputException {
            String flightId = matcher.group(1);
            bookingSystem.removeFlight(flightId);
            Terminal.printLine("OK");
        }
    },
    /**
     * The list-flights command to list all bookable flights.
     */
    LIST_FLIGHTS("list-flights") {
        @Override
        public void execute(MatchResult matcher, BookingSystem bookingSystem) throws InputException {
            String output = bookingSystem.flightsToString();
            if (!output.isEmpty()) {
                Terminal.printLine(output);
            }
        }
    },
    /**
     * The book command to book a bookable flight.
     */
    BOOK("book (" + Command.FLIGHT_ID + ");(" + Command.STRING_PATTERN + ");(" + Command.STRING_PATTERN + ")") {
        @Override
        public void execute(MatchResult matcher, BookingSystem bookingSystem) throws InputException {
            String flightId = matcher.group(1);
            String firstName = matcher.group(2);
            String lastName = matcher.group(3);
            Terminal.printLine(bookingSystem.bookFlight(flightId, firstName, lastName));
        }
    },
    /**
     * The list-bookings command to list all booked tickets.
     */
    LIST_BOOKINGS("list-bookings") {
        @Override
        public void execute(MatchResult matcher, BookingSystem bookingSystem) throws InputException {
            String output = bookingSystem.bookingsToString();
            if (!output.isEmpty()) {
                Terminal.printLine(output);
            }
        }
    },
    /**
     * The quit command to exit the program.
     */
    QUIT("quit") {
        @Override
        public void execute(MatchResult matcher, BookingSystem bookingSystem) throws InputException {
            this.quit();
        }
    };

    private static final String STRING_PATTERN = "[^;\\n]+";
    private static final String FLIGHT_ID = "\\d{4}";
    private static final String PRICE = "\\d+\\.\\d{2}";

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
     * @param input         The user input.
     * @param bookingSystem The instance of the game to run the command on.
     * @return The command that got executed.
     * @throws InputException if no matching command is found. Contains an error message.
     */
    public static Command executeMatching(String input, BookingSystem bookingSystem) throws InputException {
        for (Command command : Command.values()) {
            Matcher matcher = command.pattern.matcher(input);
            if (matcher.matches()) {
                command.execute(matcher, bookingSystem);
                return command;
            }
        }

        throw new InputException("not a valid command!");
    }

    /**
     * Executes a command.
     *
     * @param matcher       The regex matcher that contains the groups of input of the command.
     * @param bookingSystem The instance of the ConnectFive game.
     * @throws InputException if the command contains syntactical or symantical errors.
     */
    public abstract void execute(MatchResult matcher, BookingSystem bookingSystem) throws InputException;

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
