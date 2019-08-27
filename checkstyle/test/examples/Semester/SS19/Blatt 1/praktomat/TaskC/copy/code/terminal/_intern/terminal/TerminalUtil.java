/*
 * Copyright (c) 2016-2017 Tobias Bachert. All Rights Reserved.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as
 * published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.kit.informatik._intern.terminal;

import static java.lang.System.lineSeparator;
import static java.security.AccessController.doPrivileged;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.security.PrivilegedAction;
import java.util.Locale;
import java.util.Objects;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Utility class for terminal related classes.
 * 
 * @author  Tobias Bachert
 * @version 1.01, 2016/02/23
 */
@SuppressFBWarnings("CD_CIRCULAR_DEPENDENCY")
public final class TerminalUtil {

    private TerminalUtil() {}

    private static final Pattern LINE_BREAK_SPLITTER = Pattern.compile("(.++|(?=\\R))\\R?");

    /**
     * Splits the provided char-sequence at line-breaks.
     * 
     * <p>This will preserve trailing empty lines except the last one, unlike
     * <blockquote><pre>
     * Pattern.compile("\\R").splitAsStream(sequence);
     * Arrays.stream(Pattern.compile("\\R").split(sequence, -1));</pre>
     * </blockquote>
     * which either discards or keeps all trailing empty lines.<br>
     * For example, the result of this method will be
     * <blockquote><pre>
     * Line1\nLine2\n   // [Line1, Line2]
     * Line1\nLine2     // [Line1, Line2]
     * Line1\nLine2\n\n // [Line1, Line2, ]</pre>
     * </blockquote>
     * 
     * <p>The returned stream is late-binding.
     * 
     * @param  sequence the {@code CharSequence} to split
     * @return a stream containing the lines of {@code sequence}
     */
    public static Stream<String> splitAtLineBreaks(CharSequence sequence) {
        return StreamSupport.stream(new MatcherSpliterator(LINE_BREAK_SPLITTER, sequence), false).map(m -> m.group(1));
    }

    /**
     * Returns a stream containing all active threads of the {@linkplain ThreadGroup} of the current thread as by
     * {@linkplain ThreadGroup#enumerate(Thread[], boolean)} method with {@code true} provided as value for {@code
     * rescue}.
     * 
     * @return the stream
     * @throws SecurityException if a security manager exists and its {@code checkPermission} method does not permit
     *         reassigning of the standard output stream
     * 
     * @see    #aggregateActiveThreads(Consumer)
     */
    public static Stream<Thread> streamActiveThreads() {
        Stream.Builder<Thread> builder = Stream.builder();
        aggregateActiveThreads(builder::add);
        return builder.build();
    }

    /**
     * Aggregates all active threads of the {@linkplain ThreadGroup} of the current thread as by {@linkplain
     * ThreadGroup#enumerate(Thread[], boolean)} method with {@code true} provided as value for {@code rescue}.
     * 
     * @param  consumer a consumer used to accumulate the threads
     * @throws NullPointerException if {@code consumer} is {@code null}
     * @throws SecurityException if a security manager exists and its {@code checkPermission} method does not permit
     *         reassigning of the standard output stream
     */
    public static void aggregateActiveThreads(Consumer<? super Thread> consumer) {
        Objects.requireNonNull(consumer);
        final Thread currentThread = Thread.currentThread();
        aggregateActiveThreads(currentThread, currentThread.getThreadGroup(), consumer);
    }

    /**
     * Aggregates all active threads of the specified {@code threadGroup} as described by {@linkplain
     * ThreadGroup#enumerate(Thread[], boolean)} method with {@code true} provided as value for {@code rescue}.
     * 
     * <p>If {@linkplain System#out} is reset while this method is in progress, then the threads may be dumped to the
     * new standard output stream.
     * 
     * <p><b>Note:</b><br>
     * This small 'cheat' is required as neither the {@code RuntimePermission modifyThreadGroup}  nor the {@code
     * RuntimePermission getStackTrace} is given by the policy-file of the praktomat. This method directly relies on the
     * current implementation of the {@link ThreadGroup#list()} method (as the implementation didn't change the last few
     * years this can be considered to be quite safe).
     * 
     * @author Tobias Bachert
     * 
     * @param  currentThread the current thread, has to be {@linkplain Thread#currentThread()}
     * @param  threadGroup the thread group
     * @param  consumer a consumer used to accumulate the threads
     * @throws NullPointerException if {@code consumer} is {@code null}
     * @throws SecurityException if a security manager exists and its {@code checkPermission} method does not permit
     *         reassigning of the standard output stream
     */
    @SuppressFBWarnings("OS_OPEN_STREAM")
    private static void aggregateActiveThreads(Thread currentThread, ThreadGroup threadGroup,
                                               Consumer<? super Thread> consumer) {
        assert currentThread == Thread.currentThread();
        // @formatter:off
        /*
         * Note that this class represents a one-shot action - if it fails, a new instance has to be created. None of
         * the local variables may be used in the class to prevent holding a strong reference to these objects.
         */
        final class SystemOutHook extends PrintStream {

            final PrintStream out;
            volatile Thread thread = currentThread;
            Consumer<? super Thread> c = consumer;
            boolean silent;
            boolean success;

            @SuppressFBWarnings("DM_DEFAULT_ENCODING")
            SystemOutHook() {
                super(new OutputStream() { @Override public void write(int b) {} });
                close();
                out = System.out;
                doPrivileged(setOut(this));
                threadGroup.list();
                if (System.out == this)
                    doPrivileged(setOut(out));
                silent = true;
                c = null;
                thread = null;
            }

            // ThreadGroup#list invokes #print(String) and #println(Object)
            @Override public void print(String s)   { if (silent || Thread.currentThread() != thread) { out.print(s);   return; } }
            @Override public void println(Object x) { if (silent || Thread.currentThread() != thread) { out.println(x); return; }
                if (x instanceof Thread)
                    c.accept((Thread) x);
                success = true;
            }

            // Called once in constructor prior initializing out
            @Override public void close() { if (out != null) out.close(); else super.close(); }

            // Forwarding methods...
            @Override public void flush() { out.flush(); }
            @Override public boolean checkError() { return out.checkError(); }
            @Override public void write(int b) { out.write(b); }
            @Override public void write(byte[] buf, int off, int len) { out.write(buf, off, len); }
            @Override public void print(boolean b) { out.print(b); }
            @Override public void print(char c) { out.print(c); }
            @Override public void print(int i) { out.print(i); }
            @Override public void print(long l) { out.print(l); }
            @Override public void print(float f) { out.print(f); }
            @Override public void print(double d) { out.print(d); }
            @Override public void print(char[] s) { out.print(s); }
            @Override public void print(Object obj) { out.print(obj); }
            @Override public void println() { out.println(); }
            @Override public void println(boolean x) { out.println(x); }
            @Override public void println(char x) { out.println(x); }
            @Override public void println(int x) { out.println(x); }
            @Override public void println(long x) { out.println(x); }
            @Override public void println(float x) { out.println(x); }
            @Override public void println(double x) { out.println(x); }
            @Override public void println(char[] x) { out.println(x); }
            @Override public void println(String x) { out.println(x); }
            @Override public PrintStream printf(String format, Object... args) { return out.printf(format, args); }
            @Override public PrintStream printf(Locale l, String format, Object... args) { return out.printf(l, format, args); }
            @Override public PrintStream format(String format, Object... args) { return out.format(format, args); }
            @Override public PrintStream format(Locale l, String format, Object... args) { return out.format(l, format, args); }
            @Override public PrintStream append(CharSequence csq) { return out.append(csq); }
            @Override public PrintStream append(CharSequence csq, int start, int end) { return out.append(csq, start, end); }
            @Override public PrintStream append(char c) { return out.append(c); }
            @Override public void write(byte[] b) throws IOException { out.write(b); }
        }
        // @formatter:on
        synchronized (SystemOutHook.class) {
            while (!new SystemOutHook().success) {}
        }
    }

    private static PrivilegedAction<Void> setOut(PrintStream out) {
        return () -> {
            System.setOut(out);
            return null;
        };
    }

    /**
     * Returns a new daemon thread with the provided name and runnable.
     * 
     * @param  name the name of the thread
     * @param  r the runnable of the thread
     * @return the thread
     */
    public static Thread daemon(String name, Runnable r) {
        final Thread t = new Thread(r, name);
        t.setDaemon(true);
        return t;
    }

    /**
     * Replaces {@linkplain System#out} with an exception throwing output stream.
     * 
     * @param blocker a function used to create the exception that is thrown on {@linkplain System#out} usage
     */
    @SuppressFBWarnings("DM_DEFAULT_ENCODING")
    public static void blockSystemOut(Function<String, ? extends RuntimeException> blocker) {
        Objects.requireNonNull(blocker);
        final PrintStream out = new PrintStream(new OutputStream() {

            @Override
            public void close() {
                throw error();
            }

            @Override
            public void flush() {
                throw error();
            }

            @Override
            public void write(byte[] b) {
                throw error();
            }

            @Override
            public void write(byte[] b, int off, int len) {
                throw error();
            }

            @Override
            public void write(int b) {
                throw error();
            }

            private RuntimeException error() {
                throw blocker.apply(prohibitedCall("System.out"));
            }
        });
        System.setOut(out);
    }

    /**
     * Replaces {@linkplain System#in} with an exception throwing input stream.
     * 
     * @param blocker a function used to create the exception that is thrown on {@linkplain System#in} usage
     */
    public static void blockSystemIn(Function<String, ? extends RuntimeException> blocker) {
        Objects.requireNonNull(blocker);
        System.setIn(new InputStream() {

            @Override
            public int available() {
                throw error();
            }

            @Override
            public void close() {
                throw error();
            }

            @Override
            public void mark(int readlimit) {
                throw error();
            }

            @Override
            public boolean markSupported() {
                throw error();
            }

            @Override
            public int read() {
                throw error();
            }

            @Override
            public int read(byte[] b) {
                throw error();
            }

            @Override
            public int read(byte[] b, int off, int len) {
                throw error();
            }

            @Override
            public void reset() {
                throw error();
            }

            @Override
            public long skip(long n) {
                throw error();
            }

            private RuntimeException error() {
                throw blocker.apply(prohibitedCall("System.in"));
            }
        });
    }

    /**
     * Returns a string containing information about the location of the call of a method. The method will be
     * represented by the provided string.
     * 
     * @param  name a string to represent the method in the returned string
     * @return a string containing information about the location of the call
     */
    private static String prohibitedCall(String name) {
        final String[] exclude = {"edu.kit.informatik.Terminal", "edu.kit.informatik.Terminal$Instance"};
        final String[] excludeStartsWith = {"java.", "sun.", "edu.kit.informatik._intern.", "edu.kit.informatik._internal.", "_edu.kit.informatik."};
        final String located = trace(new Throwable(), exclude, excludeStartsWith).collect(StringBuilder::new,
                (sb, ste) -> sb.append(lineSeparator()).append("\tat ").append(ste), StringBuilder::append).toString();
        return "Using " + name + " is prohibited:" + (located.isEmpty() ? " insufficient information" : located);
    }

    /**
     * Returns the stack-trace without any class that is either contained in {@code exclude} or starts with any string
     * of {@code excludeStartsWith}.
     * 
     * @param  e the throwable to return the stack trace from
     * @param  exclude the classes to exclude
     * @param  excludeStartsWith the prefixes to exclude
     * @return a stream without excluded stack-trace elements
     */
    private static Stream<StackTraceElement> trace(Throwable e, String[] exclude, String[] excludeStartsWith) {
        return Stream.of(e.getStackTrace()).filter(ste -> noneMatch(exclude, ste.getClassName()::equals))
                .filter(ste -> noneMatch(excludeStartsWith, ste.getClassName()::startsWith));
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
     * @return {@code true} if no elements of the array match the provided predicate, {@code false} otherwise
     */
    private static <T> boolean noneMatch(T[] array, Predicate<? super T> p) {
        for (final T e : array)
            if (p.test(e))
                return false;
        return true;
    }

    private static final class MatcherSpliterator extends AbstractSpliterator<MatchResult> {

        private final Pattern pattern;
        private final CharSequence sequence;
        private Matcher matcher;

        MatcherSpliterator(Pattern pattern, CharSequence s) {
            super(Long.MAX_VALUE, NONNULL | ORDERED);
            this.pattern = pattern;
            this.sequence = s;
        }

        @Override
        public boolean tryAdvance(Consumer<? super MatchResult> action) {
            Objects.requireNonNull(action);
            final Matcher m;
            if ((m = matcher()).find()) {
                action.accept(m);
                return true;
            }
            return false;
        }

        private Matcher matcher() {
            return matcher != null ? matcher : (matcher = pattern.matcher(sequence));
        }
    }
}
