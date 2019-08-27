package edu.kit.informatik;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import edu.kit.informatik._intern.terminal.Entry;
import edu.kit.informatik._intern.terminal.Prefix;
import edu.kit.informatik._intern.terminal.parsing.Parser;
import edu.kit.informatik._intern.util.StringUtil;

/**
 * Terminal-class for in- and output.
 * 
 * <p>This class will terminate the {@code JVM} after a set amount of time.
 * 
 * @author  Tobias Bachert
 * @version 1.02, 2016/06/23
 */
public final class Terminal {
//######################################################################################################################
//##                                             Relevant attributes                                                  ##
//######################################################################################################################
    private static final PrintStream out = System.out;
    
    private static final Object lock = new Object();
    private static final Parser reader;
    private static String enforcedExit;
    private static int position;
    
    static { // Timer for timeout
        Executors.newScheduledThreadPool(1, (r) -> daemon("Timeout-Timer", r)).schedule(() -> {
            throw shutdown("Timeout occured.");
        }, 7, TimeUnit.SECONDS);
    }
    static { // Loading interaction
        try {
            reader = Parser.of(Paths::get, "protocol_var.txt", "protocol.txt");
        } catch (final Exception e) {
            throw crash("Crash in initializer, " + e.getMessage());
        }
    }
    static { // Shutdown hook for analyzing
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            synchronized (lock) {
                if (reader.is("debug", "true")) {
                    reader.interaction().forEach(out::println);
                }
                if (position > 70) {
                    out.println(Prefix.INFO + "Adding elements...");
                    Stream.concat(
                            reader.interaction().stream().limit(73).filter((e) -> e.analyze().isPresent()),
                            reader.interaction().stream().limit(position + 1).skip(74))
                            .map(Entry::asLogMessage)
                            .reduce(Stream::concat)
                            .orElseGet(Stream::empty).forEachOrdered(out::println);
                } else {
                    reader.interaction().stream().limit(position + 1).map(Entry::asLogMessage).reduce(Stream::concat)
                            .orElseGet(Stream::empty).forEachOrdered(out::println);
                }
            }
            
            if (enforcedExit != null) {
                out.println(Prefix.ERROR + enforcedExit);
            } else {
                final boolean systemExit = getThreads(Stream::<Thread>builder, Builder::add).build()
                        .map(Thread::getName).noneMatch("DestroyJavaVM"::equals);
                
                if (position < reader.interaction().size() - 1) {
                    if (!systemExit) {
                        out.println(Prefix.ERROR + "Premature exit.");
                    } else {
                        out.println(Prefix.ERROR + "Premature exit, usage of System.exit.");
                    }
                } else if (!systemExit) {
                    out.println(Prefix.INFO + "Detected program exit.");
                } else if (reader.is("systemexit", "info")) {
                    out.println(Prefix.INFO + "Detected program exit, usage of System.exit.");
                } else {
                    out.println(Prefix.ERROR + "Detected program exit, usage of System.exit.");
                }
            }
        }));
    }
    static { // System.out/System.in
        System.setOut(new PrintStream(new OutputStream() {
            
            @Override public void close() {
                throw error();
            }
            
            @Override public void flush() {
                throw error();
            }
            
            @Override public void write(final byte[] b) {
                throw error();
            }
            
            @Override public void write(final byte[] b, final int off, final int len) {
                throw error();
            }
            
            @Override public void write(final int b) {
                throw error();
            }
            
            private RuntimeException error() {
                throw shutdown(prohibitedCall("System.out"));
            }
        }));
        System.setIn(new InputStream() {
            
            @Override public int available() {
                throw error();
            }
            
            @Override public void close() {
                throw error();
            }
            
            @Override public void mark(final int readlimit) {
                throw error();
            }
            
            @Override public boolean markSupported() {
                throw error();
            }
            
            @Override public int read() {
                throw error();
            }
            
            @Override public int read(final byte[] b) {
                throw error();
            }
            
            @Override public int read(final byte[] b, final int off, final int len) {
                throw error();
            }
            
            @Override public void reset() {
                throw error();
            }
            
            @Override public long skip(final long n) {
                throw error();
            }
            
            private RuntimeException error() {
                throw shutdown(prohibitedCall("System.in"));
            }
        });
    }
    
    private Terminal() {
        // Empty on purpose.
    }
//######################################################################################################################
//##           Public methods, never modify these as changes have to be reflected onto the student version            ##
//######################################################################################################################
    /**
     * Prints the string representation of the given value and terminate the line.
     * 
     * @param value the {@code boolean} to be printed
     * @see   #printLine(String)
     * @see   String#valueOf(boolean)
     */
    public static void printLine(final boolean value) {
        Terminal.printLine0(String.valueOf(value));
    }
    
    /**
     * Prints the string representation of the given value and terminate the line.
     * 
     * @param value the {@code byte} to be printed
     * @see   #printLine(String)
     * @see   String#valueOf(byte)
     */
    public static void printLine(final byte value) {
        Terminal.printLine0(String.valueOf(value));
    }
    
    /**
     * Prints the string representation of the given value and terminate the line.
     * 
     * @param value the {@code char} to be printed
     * @see   #printLine(String)
     * @see   String#valueOf(char)
     */
    public static void printLine(final char value) {
        Terminal.printLine0(String.valueOf(value));
    }
    
    /**
     * Prints an array of characters and terminate the line.
     * 
     * <p>If {@code value} is {@code null}, then a {@code NullPointerException} is thrown.
     * 
     * @param array the array of chars to be printed
     * @see   #printLine(String)
     * @see   String#valueOf(char[])
     */
    public static void printLine(final char[] array) {
        Terminal.printLine0(String.valueOf(array));
    }
    
    /**
     * Prints the string representation of the given value and terminate the line.
     * 
     * @param value the {@code double} to be printed
     * @see   #printLine(String)
     * @see   String#valueOf(double)
     */
    public static void printLine(final double value) {
        Terminal.printLine0(String.valueOf(value));
    }
    
    /**
     * Prints the string representation of the given value and terminate the line.
     * 
     * @param value the {@code float} to be printed
     * @see   #printLine(String)
     * @see   String#valueOf(float)
     */
    public static void printLine(final float value) {
        Terminal.printLine0(String.valueOf(value));
    }
    
    /**
     * Prints the string representation of the given value and terminate the line.
     * 
     * @param value the {@code int} to be printed
     * @see   #printLine(String)
     * @see   String#valueOf(int)
     */
    public static void printLine(final int value) {
        Terminal.printLine0(String.valueOf(value));
    }
    
    /**
     * Prints the string representation of the given value and terminate the line.
     * 
     * @param value the {@code long} to be printed
     * @see   #printLine(String)
     * @see   String#valueOf(long)
     */
    public static void printLine(final long value) {
        Terminal.printLine0(String.valueOf(value));
    }
    
    /**
     * Prints the string representation of the given object, or {@code "null"} if the given object is {@code null}, and
     * terminate the line.
     * 
     * @param obj the {@code Object} to be printed
     * @see   #printLine(String)
     * @see   String#valueOf(Object)
     */
    public static void printLine(final Object obj) {
        Terminal.printLine0(String.valueOf(obj));
    }
    
    /**
     * Prints the string representation of the given value and terminate the line.
     * 
     * @param value the {@code short} to be printed
     * @see   #printLine(String)
     * @see   String#valueOf(short)
     */
    public static void printLine(final short value) {
        Terminal.printLine0(String.valueOf(value));
    }
    
    /**
     * Prints the given string, or {@code "null"} if the given string is {@code null}, and terminate the line.
     * 
     * @param str the {@code String} to be printed
     * @see   String#valueOf(String)
     */
    public static void printLine(final String str) {
        Terminal.printLine0(String.valueOf(str));
    }
    
    /**
     * Prints the given error-{@code message}, exactly as if "Error, " + message was provided as parameter to the {@link
     * #printLine(Object)} method.
     * 
     * <p>More specific, this method behaves as if the following code got executed:
     * <blockquote><pre>
     * Terminal.printLine("Error, " + message);</pre>
     * </blockquote>
     * 
     * @param message the error message to be printed
     */
    public static void printError(final String message) {
        Terminal.printLine0("Error, " + message);
    }
    
    /**
     * Reads a line of text. A line is considered to be terminated by any one of a line feed ('\n'), a carriage return
     * ('\r'), or a carriage return followed immediately by a linefeed.
     * 
     * @return a {@code String} containing the contents of the line, not including any line-termination characters, or
     *         {@code null} if the end of the stream has been reached
     */
    public static String readLine() {
        return Terminal.readLine0();
    }
    
    /**
     * Reads the file with the specified path and returns its content stored in a {@code String} array, whereas the
     * first array field contains the file's first line, the second field contains the second line, and so on.
     *
     * @param  path the path of the file to be read
     * @return the content of the file stored in a {@code String} array
     */
    public static String[] readFile(final String path) {
        return readFile0(path);
    }
//######################################################################################################################
//##                                   Private methods used for the public methods                                    ##
//######################################################################################################################
    /**
     * Adds the given string as actual output.
     * 
     * @param str the {@code String} to add
     * @see   Entry#addActualLine(String)
     */
    private static void printLine0(final String str) {
        assert str != null;
        synchronized (lock) {
            final Entry e = reader.interaction().get(position);
            e.print(str);
            e.newLine();
        }
    }
    
    /**
     * Returns the next line of input.
     * 
     * <p>If no input is available, the {@code Terminal} will initialize the shutdown and log an overdue exit.
     * 
     * @return the next line of input
     * @see    Entry#input()
     */
    private static String readLine0() {
        synchronized (lock) {
            if (++position < reader.interaction().size()) {
                return reader.interaction().get(position).input();
            }
        }
        throw shutdown("Expected program exit but got call to Terminal.readLine().");
    }
    
    /**
     * Reads the file with the specified path and returns its content stored in a {@code String} array, whereas the
     * first array field contains the file's first line, the second field contains the second line, and so on.
     *
     * @param  path the path of the file to be read
     * @return the content of the file stored in a {@code String} array
     */
    private static String[] readFile0(final String path) {
        List<String> file = reader.getFile(path);
        if (file == null) {
            try {
                file = Files.readAllLines(Paths.get(path));
            } catch (final IOException e) {
                throw shutdown("Exception while reading file, " + path + ", " + e);
            }
        }
        return file.toArray(StringUtil.EMPTY_STRING_ARRAY);
    }
//######################################################################################################################
//##                                           Private utility methods                                                ##
//######################################################################################################################
    /**
     * Returns a string containing information about the location of the call of a method. The method will be
     * represented by the provided string.
     * 
     * @param  methodname a string to represent the method in the returned string
     * @return a string containing information about the location of the call
     */
    private static String prohibitedCall(final String methodname) {
        final String[] hidden = {"java.", "sun.", Terminal.class.getName()};
        final String located = Arrays.stream(new Throwable().getStackTrace())
                .filter((ste) -> noneMatch(hidden, ste.getClassName()::startsWith))
                .collect(StringBuilder::new, (sb, ste) -> sb.append(System.lineSeparator()).append("\tat ").append(ste),
                        StringBuilder::append)
                .toString();
        return "Using " + methodname + " is prohibited:" + (located.isEmpty() ? " insufficient information" : located);
    }
    
    /**
     * Shuts the {@code JVM} down using the {@link Runtime#halt(int)} method.
     * 
     * @param  reason the reason for the shutdown
     * @return dummy to allow the usage with {@code throw} tags as this method never returns normally
     */
    private static RuntimeException crash(final String reason) {
        out.println(Prefix.CRASH + reason);
        Runtime.getRuntime().halt(1);
        return null;
    }
    
    /**
     * Shuts the {@code JVM} down using the {@link Runtime#exit(int)} method.
     * 
     * @param  reason the reason for the shutdown
     * @return dummy to allow the usage with {@code throw} tags as this method never returns normally
     */
    private static RuntimeException shutdown(final String reason) {
        enforcedExit = reason;
        Runtime.getRuntime().exit(0);
        return null;
    }
    
    /**
     * Returns a new daemon thread with the provided name and runnable.
     * 
     * @param  name the name of the thread
     * @param  r the runnable of the thread
     * @return the thread
     */
    private static Thread daemon(final String name, final Runnable r) {
        final Thread t = new Thread(r, name);
        t.setDaemon(true);
        return t;
    }
    
    /**
     * Returns whether no elements of an array match a predicate.<br>
     * An empty array returns {@code true}.
     * 
     * <p>If the predicate throws an exception, an exception is thrown from {@code noneMatch}.
     * 
     * @param  <T> type parameter
     * @param  array the {@code Array}
     * @param  p the predicate
     * @return {@code true} if no elements of the array match the provided predicate or the array is empty, {@code
     *         false} otherwise
     */
    private static <T> boolean noneMatch(final T[] array, final Predicate<? super T> p) {
        for (final T e : array)
            if (p.test(e))
                return false;
        return true;
    }
    
    /**
     * Returns all active threads as defined by the {@link ThreadGroup#enumerate(Thread[], boolean)} method with {@code
     * true} provided as value for {@code recurse}.
     * 
     * <p>Note: This small 'cheat' is required as neither the {@code RuntimePermission modifyThreadGroup} nor the {@code
     * RuntimePermission getStackTrace} is given by the policy-file of the praktomat. This method directly relies on the
     * current implementation of the {@link ThreadGroup#list()} method (as the implementation didn't change the last few
     * years this can be considered to be quite safe).
     * 
     * @author Tobias Bachert
     * @param  <R> result type parameter
     * @param  supplier a supplier for the container
     * @param  accumulator a consumer used to accumulate the threads into the container
     * @return all active threads
     */
    private static <R> R getThreads(
            final Supplier<R> supplier,
            final BiConsumer<? super R, ? super Thread> accumulator) {
        ////
        final Thread currentThread = Thread.currentThread();
        final R container = supplier.get();
        final boolean[] isSet = {true};
        final PrintStream out = System.out;
        final PrintStream own = new PrintStream(out) {
            
            @Override public void print(final String out) {
                if (Thread.currentThread() != currentThread) {
                    super.print(out);
                } else {
                    isSet[0] = true;
                }
            }
            
            @Override public void println(final Object out) {
                if (Thread.currentThread() != currentThread) {
                    super.println(out);
                } else if (out instanceof Thread) {
                    if (isSet[0]) {
                        accumulator.accept(container, (Thread) out);
                    } else if (currentThread == out) {
                        isSet[0] = true;
                    }
                }
            }
        };
        System.setOut(own);
        currentThread.getThreadGroup().list();
        
        if (System.out != own) {
            final PrintStream currentOut = System.out;
            isSet[0] = false;
            currentOut.print("");
            if (isSet[0]) {
                isSet[0] = false;
                currentOut.println(currentThread);
            }
            if (!isSet[0]) {
                throw new IllegalStateException("System.out got re-set while obtaining threads.");
            }
        } else {
            System.setOut(out);
        }
        return container;
    }
}
