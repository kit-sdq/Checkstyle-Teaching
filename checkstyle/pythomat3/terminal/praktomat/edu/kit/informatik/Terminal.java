package edu.kit.informatik;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public final class Terminal {
    /** define the files locations */
    private static final String PROTOCOL_FILE = "protocol.txt";

    /** time out in seconds (default is 50 seconds) */
    private static int timeout = 50;

    /** Enum for distinguishing message types */
    enum Type {
        // This typedef is used for both the protocol file to which a program
        // run is compared to as well as the comparison that is passed to
        // pythomat. The enum values have slightly different meaning in these
        // cases.
        INPUT("input"), /// Terminal.readLine() expected. Print regular input line.
        OUTPUT("output"), /// Terminal.printLine() expected. Print regular output line.
        INFO("info"), /// Display additional information in both cases.
        ERROR("error"), /// Expect an error message in the program. Display an error message in pythomat.
        CRASH("crash"), /// Should not occur in the protocol. Internal problem in Terminal.
        TIMEOUT("timeout"), /// Timeout to be set. Timeout occured.
        TERM("term"), /// Protocol was prematurely terminated. Should not be passed to pythomat.
        EXIT("exit"); /// Protocol was exited. Submission exited.

        Type(String tag) { this.tag = tag; }

        static Type fromTag(String tag) {
            for (Type type : Type.values()) {
                if (type.tag.equals(tag)) {
                    return type;
                }
            }

            throw new IllegalArgumentException(tag + " is not a valid message type");
        }

        String getTag() { return tag; }

        String asCall() {
            switch (this) {
                case INPUT:
                    return "call to Terminal.readLine()";
                case OUTPUT:
                    return "call to Terminal.printLine()";
                case ERROR:
                    return "error message";
                case EXIT:
                    return "program exit";
                default:
                    crash("Unexpected type '" + getTag() + "'");
                    return null;
            }
    }

        private String tag;
    }


    /** Helper class to ensure the program terminates on System.out */
    private static class TerminatingOutputStream extends OutputStream
    {
        private void logError() {
            Terminal.logError("Using System.out is illegal");
        }

        public void close() { logError(); }
        public void flush() { logError(); }
        public void write(byte[] b) { logError(); }
        public void write(byte[] b, int off, int len) { logError(); }
        public void write(int b) { logError(); }
    }


    /** Helper class to ensure the program terminates on System.in */
    private static class TerminatingInputStream extends InputStream
    {
        private void logError() {
            Terminal.logError("Using System.in is illegal");
        }

        public int available() { logError(); return 0; }
        public void close() { logError(); }
        public void mark(int readlimit) { logError(); }
        public boolean markSupported() { logError(); return false; }
        public int read() { logError(); return 0; }
        public int read(byte[] b) { logError(); return 0; }
        public int read(byte[] b, int off, int len) { logError(); return 0; }
        public void reset() { logError(); }
        public long skip(long n) { logError(); return 0; }
    }


    /** Helper class to ensure Exits down happen prematurely. */
    private static class PrematureExitCheck extends Thread
    {
        private Terminal terminal;

        PrematureExitCheck(Terminal terminal) { this.terminal = terminal; }

        public void run() {
            // make sure we don't try removeShutdownHook or call System.exit()
            // while the shutdown process has already started.
            // Set shutdownHook to indicate, that this a shutdown is happening.

            Terminal.shutdownHook = null;
            // check if the student's exiting is expected
            Event event = terminal.next(Type.EXIT);

            log(Type.EXIT, event.text);
        }
    }


    private static Terminal instance;
    private static InputStream in;
    private static PrintStream out;
    private static Thread shutdownHook;
    private static Timer timer;

    static {
        instance  = new Terminal();

        out = System.out;
        System.setOut(new PrintStream(new TerminatingOutputStream()));

        in = System.in;
        System.setIn(new TerminatingInputStream());

        shutdownHook = new PrematureExitCheck(instance);
        Runtime.getRuntime().addShutdownHook(shutdownHook);

        instance.initialize();

        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                logError("Timeout occured");
            }
        }, timeout * 1000);
    }


    /** Events in the protocol */
    private class Event {
        Type type;
        String text;

        Event(Type type, String text) {
            this.type = type;
            this.text = text;
        }

    }


    private Iterator<Event> eventIterator;

    private void initialize() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(new File(PROTOCOL_FILE)));
        } catch (FileNotFoundException e) {
            crash("Could not open protocol file");
            return;
        }
        List<Event> events = new ArrayList<Event>();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                // parse a single line
                String[] tokens = line.split(":", 2);
                if (tokens.length != 2) {
                    crash("Could not parse protocol file");
                }
                Event event = new Event(Type.fromTag(tokens[0]), tokens[1]);
                if (event.type == Type.TERM) {
                    crash("Protocol contains a crash");
                } else if (event.type == Type.TIMEOUT) {
                    timeout = Integer.parseInt(event.text);
                } else {
                    events.add(event);
                }
            }
        } catch (IOException e) {
            crash("Could not read protocol file");
            return;
        }

        eventIterator = events.iterator();
    }

    private static void shutdown() {
        // shutdownHook == null means a shutdown is already happening. In this
        // case, just wait for it to happend.
        if (shutdownHook != null) {
            Runtime.getRuntime().removeShutdownHook(shutdownHook);
            System.exit(0);
        }
    }

    private static void log(Type type, String content) {
        out.println(type.getTag() + ":" + content);
    }

    private static void crash(String message) {
        log(Type.CRASH, message);
        shutdown();
    }

    private static void logError(String message) {
        log(Type.ERROR, message);
        shutdown();
    }

    private static void logAndTerminate(String message) {
        log(Type.INFO, message);
        shutdown();
    }

    private static void logInfo(String message) {
        log(Type.INFO, message);
    }

    /**
     * Return the next expected event, skip and print info events.
     */
    private Event nextNonInfo() {
        while (eventIterator.hasNext()) {
            Event event = eventIterator.next();
            if (event.type != Type.INFO) {
                return event;
            } else {
                log(Type.INFO, event.text);
            }
        }

        // If the protocol is lacking the explicit exit, insert it here
        return new Event(Type.EXIT, "0");
    }

    /**
     * Get the next Event from the list of expected events.
     *
     * Also checks the expected event type and the observed event type.
     */
    private Event next(Type type) {
        Event event = nextNonInfo();

        if (event.type == Type.TERM) {
            // Special case TERM (a submission cannot generate a TERM event)
            logAndTerminate(event.text);
        } else if (event.type != type) {
            // All non-matching event types
            if (event.type == Type.ERROR) {
                // special case this message, otherwise it is confusing
                logError("Expected " + event.type.asCall() + " starting with 'Error, '");
            }else {
                logError("Expected " + event.type.asCall() + ", but got " + type.asCall());
            }
        }

        return event;
    }

    private boolean isErrorMessage(String str) {
        return
            str.toLowerCase().startsWith("error,") ||
            str.toLowerCase().startsWith("error!") ||
            str.toLowerCase().startsWith("error:");
    }

    private void internalPrintLine(String str) {
        log(Type.OUTPUT, str);

        String trimmed = str.trim();
        if (trimmed.isEmpty()) {
            return;
        }

        Type type = isErrorMessage(trimmed) ? Type.ERROR : Type.OUTPUT;
        Event event = next(type);

        // If the student printed something that looks like an error message,
        // then don't check the text of that message
        if (type == Type.ERROR) {
            logAndTerminate(event.text);
        }

        // check if the string matches
        if (!trimmed.equals(event.text.trim())) {
            logError("Expected '" + event.text + "', but got '" + str + "'");
        }
    }

    private String internalReadLine() {
        Event event = next(Type.INPUT);
        if (!event.text.equals("EOF")) {
            log(Type.INPUT, event.text);
            return event.text;
        } else {
            log(Type.INFO, "End of input file reached");
            return null;
        }
    }

    // --- public functions ---
    public static void printLine(String str) {
        if (str == null) {
            logError("Tried passing null to Terminal.printLine()");
        }

        for (String line : java.util.Arrays.asList(str.split("[\r\n]+"))) {
            instance.internalPrintLine(line);
        }
    }

    public static String readLine() throws IOException {
        return instance.internalReadLine();
    }

    // These are required for our own instrumented solution, but not for the
    // student solutions. Delete these before upload into praktomat to make
    // sure they don't get called.
    public static void exit(int exitcode) {
        System.exit(exitcode);
    }

    public static void setTimeout(int timeout) {
    }

    public static void printError(String str) {
        printLine(str);
    }

    public static void printInfo(String str) {
    }
}
