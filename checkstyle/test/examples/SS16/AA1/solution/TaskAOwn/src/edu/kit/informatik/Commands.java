package edu.kit.informatik;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import edu.kit.informatik._intern.util.StringUtil;
import edu.kit.informatik.ludo.IBoard;
import edu.kit.informatik.ludo.Phase;
import edu.kit.informatik.ludo.fields.Field;
import edu.kit.informatik.ludo.rules.Rule;
import edu.kit.informatik.util.executable.Command;
import edu.kit.informatik.util.executable.Parser;
import edu.kit.informatik.util.executable.Parser.Symbol;
import edu.kit.informatik.util.executable.Parser.SymbolMap;

/**
 * The enum {@code Commands} contains the orders for the task.
 *
 * @author  Tobias Bachert
 * @version 1.03, 2016/06/25
 *
 * @since   1.8
 */
public enum Commands implements Command<IBoard> {
    /** Represents the start command. */
    START_NOARGS("start") {
        
        @Override public void apply(final IBoard board, final SymbolMap args) {
            
            if (board.inProcess()) {
                Terminal.printError("game in process");
            } else {
                board.start();
                Terminal.printLine("OK");
            }
        }
    },
    /** Represents the start command. */
    START_WITHARGS("start <arguments>") {
        
        private final Pattern argsplitter = Pattern.compile("(((^|(?<!^)\\h)("
                + Arrays.stream(Rule.values()).map(Rule::name).collect(Collectors.joining("|"))
                + "))++)?+((^|(?<!^)\\h)"
                + "([1-3]?[0-9]|[SABCD]R),([1-3]?[0-9]|[SABCD]R),([1-3]?[0-9]|[SABCD]R),([1-3]?[0-9]|[SABCD]R);"
                + "([1-3]?[0-9]|[SABCD]B),([1-3]?[0-9]|[SABCD]B),([1-3]?[0-9]|[SABCD]B),([1-3]?[0-9]|[SABCD]B);"
                + "([1-3]?[0-9]|[SABCD]G),([1-3]?[0-9]|[SABCD]G),([1-3]?[0-9]|[SABCD]G),([1-3]?[0-9]|[SABCD]G);"
                + "([1-3]?[0-9]|[SABCD]Y),([1-3]?[0-9]|[SABCD]Y),([1-3]?[0-9]|[SABCD]Y),([1-3]?[0-9]|[SABCD]Y))?+");
        private final Pattern ws = Pattern.compile("\\h");
        
        @Override public void apply(final IBoard board, final SymbolMap args) {
            final String arguments = args.get(sARGUMENTS);
            if (board.inProcess()) {
                Terminal.printError("game in process");
            } else {
                final Matcher m = argsplitter.matcher(arguments);
                try {
                    if (m.matches()) {
                        final Stream<Stream<Field>> fields = m.group(5) == null ? Stream.empty()
                                : IntStream.range(0, 4).map((i) -> 4 * i).mapToObj((i) -> IntStream.range(0, 4)
                                        .map((n) -> i + n + 7).mapToObj(m::group).map(Field::of));
                        board.start(ws.splitAsStream(StringUtil.nullToEmpty(m.group(1))).map(Rule::valueOf), fields);
                        Terminal.printLine("OK");
                        return;
                    }
                } catch (final IllegalArgumentException | IllegalStateException e) { /* Leads to error-message. */ }
                Terminal.printError("invalid arguments '" + arguments + "'");
            }
        }
    },
    /** Represents the roll command. */
    ROLL("roll <number>") {
        
        @Override public void apply(final IBoard board, final SymbolMap args) {
            final int number = args.get(sNUMBER);
            
            if (!board.inProcess()) {
                Terminal.printError("game not in process");
            } else if (board.phase() != Phase.ROLL) {
                Terminal.printError("game not in roll-phase");
            } else if (number < 1 || number > 6) {
                Terminal.printError("invalid value");
            } else {
                board.roll(number).map((m) -> m.from() + "-" + m.to()).forEachOrdered(Terminal::printLine);
                Terminal.printLine(board.player());
            }
        }
    },
    /** Represents the rollx command. */
    ROLLX("rollx") {
        
        @Override public void apply(final IBoard board, final SymbolMap args) {
            if (!board.inProcess()) {
                Terminal.printError("game not in process");
            } else if (board.phase() != Phase.ROLL) {
                Terminal.printError("game not in roll-phase");
            } else {
                board.rollx().map((m) -> m.from() + "-" + m.to()).forEachOrdered(Terminal::printLine);
                Terminal.printLine(board.player());
                Terminal.printLine("(rolled value: " + board.lastRolled() + ")");
            }
        }
    },
    /** Represents the move command. */
    MOVE("move <start> <end>") {
        
        @Override public void apply(final IBoard board, final SymbolMap args) {
            final Field start = args.get(sSTART);
            final Field end   = args.get(sEND);
            
            if (!board.inProcess()) {
                Terminal.printError("game not in process");
            } else if (board.phase() != Phase.MOVE) {
                Terminal.printError("game not in move-phase");
            } else if (!board.canMove(start, end)) {
                Terminal.printError("the move is not allowed");
            } else {
                Terminal.printLine(end);
                Terminal.printLine(board.move(start, end) + (board.inProcess() ? "" : " winner"));
            }
        }
    },
    /** Represents the print command. */
    PRINT("print") {
        
        @Override public void apply(final IBoard board, final SymbolMap args) {
            if (!board.inProcess()) {
                Terminal.printError("game not in process");
            } else {
                Terminal.printLine(board.positions().map((s) -> s.map(Field::toString).collect(Collectors.joining(",")))
                        .collect(Collectors.joining(System.lineSeparator())));
                Terminal.printLine(board.player());
            }
        }
    },
    /** Represents the printx command. */
    PRINTX("printx") {

        @Override public void apply(final IBoard board, final SymbolMap args) {
            if (!board.inProcess()) {
                Terminal.printError("game not in process");
            } else {
                Terminal.printLine(board);
            }
        }
    },
    /** Represents the abort command. */
    ABORT("abort") {

        @Override public void apply(final IBoard board, final SymbolMap args) {
            if (!board.inProcess()) {
                Terminal.printError("game not in process");
            } else {
                board.abort();
            }
        }
    },
    /** Represents the quit command. */
    QUIT("quit") {

        @Override public boolean isQuit() {
            return true;
        }

        @Override public void apply(final IBoard board, final SymbolMap args) {
            /* Empty on purpose. */
        }
    };
    
    private static Symbol<String>  sARGUMENTS;
    private static Symbol<Integer> sNUMBER;
    private static Symbol<Field>   sSTART;
    private static Symbol<Field>   sEND;

/*####################################################################################################################*/
/*#                                     Don't modify anything below this line.                                       #*/
/*####################################################################################################################*/

    //==================================================================================================================
    // |/ Parameter \|
    private final String format;
    private Parser parser;

    //==================================================================================================================
    // |/ Constructor \|
    /**
     * Creates a new command.
     *
     * <p>The format string has to have the format described in the {@link Parser.Builder#build(String)} method.
     *
     * @param format a string representing the format of the command
     */
    Commands(final String format) {
        this.format = format;
    }

    //==================================================================================================================
    // |/ Methods - Executable \|
    @Override public Parser parser() {
        return parser;
    }

    @Override public String toString() {
        return format;
    }

    //==================================================================================================================
    // |/ Static initializer for symbols and parsers \|
    static {
        final Parser.Builder parserBuilder = Parser.builder().add(java.lang.invoke.MethodHandles.lookup());
        for (final Commands command : values()) {
            command.parser = parserBuilder.build(command.format);
        }
    }
}
