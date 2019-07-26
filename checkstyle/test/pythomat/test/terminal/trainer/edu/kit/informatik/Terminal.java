package edu.kit.informatik;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

public final class Terminal {

    /** define the files locations */
    private static final String PROTOCOL_FILE = "protocol.txt";

    enum Type {
        INPUT("input"), OUTPUT("output"), INFO("info"), ERROR("error"), CRASH(
                "crash"), TIMEOUT("timeout"), TERM("term"), EXIT("exit");

        Type(String tag) {
            this.tag = tag;
        }

        static Type fromTag(String tag) {
            for (Type type : Type.values()) {
                if (type.tag == tag) {
                    return type;
                }
            }

            return null;
        }

        String getTag() {
            return tag;
        }

        private final String tag;
    }

    /** Helper class to ensure the program terminates on System.out */
    private static class TerminatingOutputStream extends OutputStream {
        private final Terminal terminal;

        TerminatingOutputStream(Terminal terminal) {
            this.terminal = terminal;
        }

        private void terminate() {
            Terminal.terminate("Using System.out is illegal");
        }

        @Override
        public void close() {
            terminate();
        }

        @Override
        public void flush() {
            terminate();
        }

        @Override
        public void write(byte[] b) {
            terminate();
        }

        @Override
        public void write(byte[] b, int off, int len) {
            terminate();
        }

        @Override
        public void write(int b) {
            terminate();
        }
    }

    /** Helper class to ensure the program terminates on System.in */
    private static class TerminatingInputStream extends InputStream {
        private final Terminal terminal;

        TerminatingInputStream(Terminal terminal) {
            this.terminal = terminal;
        }

        private void terminate() {
            Terminal.terminate("Using System.in is illegal");
        }

        @Override
        public int available() {
            terminate();
            return 0;
        }

        @Override
        public void close() {
            terminate();
        }

        @Override
        public void mark(int readlimit) {
            terminate();
        }

        @Override
        public boolean markSupported() {
            terminate();
            return false;
        }

        @Override
        public int read() {
            terminate();
            return 0;
        }

        @Override
        public int read(byte[] b) {
            terminate();
            return 0;
        }

        @Override
        public int read(byte[] b, int off, int len) {
            terminate();
            return 0;
        }

        @Override
        public void reset() {
            terminate();
        }

        @Override
        public long skip(long n) {
            terminate();
            return 0;
        }
    }

    /** Helper class to ensure the reason for termination is noted */
    private static class CleanShutdown extends Thread {
        private final Terminal terminal;

        CleanShutdown(Terminal terminal) {
            this.terminal = terminal;
        }

        @Override
        public void run() {
            Terminal.log(Type.TERM, "Program terminated by interrupt");
        }
    }

    private static Terminal instance;
    private static BufferedReader in;
    private static PrintStream out;
    private static PrintStream protocol;
    private static Thread shutdownHook;
    static {
        instance = new Terminal();

        out = System.out;
        try {
            protocol = new PrintStream(new File(PROTOCOL_FILE));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.setOut(new PrintStream(new TerminatingOutputStream(instance)));

        in = new BufferedReader(new InputStreamReader(System.in));
        System.setIn(new TerminatingInputStream(instance));

        shutdownHook = new CleanShutdown(instance);
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    // --- Logging functions ---
    private static void log(Type type, String content) {
        protocol.println(type.tag + ":" + content);
    }

    private static void terminate(String message) {
        log(Type.ERROR, message);
        Runtime.getRuntime().removeShutdownHook(shutdownHook);
        System.exit(1);
    }

    // --- public functions ---

    public static void printLine(String str) {
        out.println(str);

        if (str.equals("")) {
            return;
        }

        for (String line : java.util.Arrays.asList(str.split("[\r\n]+"))) {
            log(Type.OUTPUT, line);
        }
    }

    public static String readLine() throws IOException {
        try {
            String cmd = in.readLine();
            if (cmd == null) {
                log(Type.INPUT, "EOF");
                return null;
            } else {
                log(Type.INPUT, cmd);
                return cmd;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    // The following methods do not exist in the student's solution and are
    // purely for the trainer's use.

    public static void printError(String str) {
        out.println(str);

        if (str.equals("")) {
            return;
        }

        if (str.contains("\n") || str.contains("\r")) {
            out.println("Please use only single-line error messages.");
            System.exit(1);
        }

        log(Type.ERROR, "Looks like an error message: we're done here");
    }

    public static void printInfo(String message) {
        log(Type.INFO, message);
    }

    public static void setTimeout(int timeout) {
        Integer to = new Integer(timeout);
        log(Type.TIMEOUT, to.toString());
    }

    public static void exit(int exitcode) {
        log(Type.EXIT, String.valueOf(exitcode));
        Runtime.getRuntime().removeShutdownHook(shutdownHook);
        System.exit(exitcode);
    }
}
