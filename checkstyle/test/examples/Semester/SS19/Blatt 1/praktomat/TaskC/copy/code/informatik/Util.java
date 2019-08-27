
package _edu.kit.informatik;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.stream.Collectors.joining;

import java.io.PrintStream;
import java.lang.annotation.Retention;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.b_privat.testframework.ArgumentParser;
import de.b_privat.testframework.DisplayName;
import de.b_privat.testframework.Expected;
import de.b_privat.testframework.Msg;
import de.b_privat.testframework.Param.Parameters;
import de.b_privat.testframework.Test;
import de.b_privat.testframework.Test.Disabled;
import edu.kit.informatik._intern.terminal.Prefix;
/*import edu.kit.informatik.matchthree.framework.Delta;
import edu.kit.informatik.matchthree.framework.Position;
import edu.kit.informatik.matchthree.framework.Token;
import edu.kit.informatik.matchthree.framework.interfaces.Board;
import edu.kit.informatik.matchthree.framework.interfaces.Matcher;*/

/*
This class as well as all classes in the testframework folder are outdated as
they use the old, rushed, parser implementation, any kind of modification has
to be manually copied to this old version.
*/
final class Util {

    private Util() {}

    @Expected({NullPointerException.class, IllegalArgumentException.class})
    @Retention(RUNTIME)
    @interface ExpectNPEorIAE {
    }

    private static final ArgumentParser PARSER = new ArgumentParser();
/*
    static {
        PARSER.register(Position.class, s -> {
            final int comma = s.indexOf(',');
            return Position.at(Integer.parseInt(s.substring(0, comma)), Integer.parseInt(s.substring(comma + 1)));
        });
        PARSER.register(Delta.class, s -> {
            final int comma = s.indexOf(',');
            return Delta.dxy(Integer.parseInt(s.substring(0, comma)), Integer.parseInt(s.substring(comma + 1)));
        });
        PARSER.register(Board.class, s -> edu.kit.informatik._internal.matchthree.MatchThreeBoard.parse(s, set(s)));
        PARSER.register(_edu.kit.informatik.TaskD.PseudoMatcher.class, s -> new _edu.kit.informatik.TaskD.PseudoMatcher());
        PARSER.register(Token.class, Token::new);
    }*/

    /*
     * LOWPRIO / TODO: Add injection for instances / fields (don't need it, so most likely won't add it, but might be
     * useful for the future)
     */
    static void run(Class<?> testClass) throws Throwable {
        if (false && new Throwable().getStackTrace().length > 2) { // XXX Disabled as not uploaded with the public tests
            out.println(Prefix.CRASH.prefix + "Assuming invalid caller, terminating");
            Runtime.getRuntime().halt(1);
            throw new AssertionError();
        }
        final Stream<Method> methods = Stream.of(testClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(Test.class))
                .filter(m -> !m.isAnnotationPresent(Disabled.class))
                .sorted(Comparator.comparingInt(m -> m.getAnnotation(Test.class).value()));
        for (final Method m : (Iterable<Method>) methods::iterator) {
            assert java.lang.reflect.Modifier.isStatic(m.getModifiers());
            runTest(m, null);
        }
    }

    private static void runTest(Method m, Object target) throws Throwable {
        final Expected expected = Expected.Support.get(m);
        print(" ");
        print(">>> " + (m.isAnnotationPresent(DisplayName.class) ? m.getAnnotation(DisplayName.class).value() : m.getName()));
        if (expected.value().length != 0)
            print("Expected: " + Stream.of(expected.value()).map(Class::getSimpleName).collect(joining(","))
                    + (expected.mandatory() ? "(mandatory)" : "(optional)"));
        print(" ");
        if (m.isAnnotationPresent(Msg.class))
            for (final String s : m.getAnnotation(Msg.class).value())
                print(s);
        print("---");
        for (final Object[] args : Parameters.of(PARSER, m).arguments) {
            print(args.length == 0 ? "Running test" : "Running test with parameters: " + toString(args));
            runTest(m, expected, target, args);
        }
    }

    private static void runTest(Method m, Expected expected, Object target, Object... args)
            throws IllegalAccessException, IllegalArgumentException {
        try {
            m.invoke(target, args);
            if (expected.mandatory())
                printErr("Expected: " + Stream.of(expected.value()).map(Class::getSimpleName).collect(joining(",")));
        } catch (final InvocationTargetException e) {
            final Throwable cause = e.getCause();
            if (Expected.Support.isExpected(expected, cause))
                return;
            if (cause instanceof Assert.AssertError)
                printErr(cause.getMessage());
            else
                printErr("Unexpected exception: " + cause + "\n" + collectStackTrace(cause));
        } catch (final Error e) {
            printErr("Error: " + e + collectStackTrace(e));
        }
    }

    private static String collectStackTrace(Throwable throwable) {
        return Stream.of(throwable.getStackTrace())
                .filter(e -> !e.getClassName().startsWith("java."))
                .filter(e -> !e.getClassName().startsWith("sun."))
                .filter(e -> !e.getClassName().startsWith("_edu.kit.informatik."))
                .limit(15)
                .map(e -> e.getClassName().substring(e.getClassName().lastIndexOf('.') + 1) + '#' + e.getMethodName()
                        + (e.getFileName() == null ? "" : '(' + e.getFileName() + ':' + e.getLineNumber() + ')'))
                .collect(joining(", "));
    }

    private static String toString(Object[] obj) {
        return Stream.of(obj).map(Util::toString).collect(Collectors.joining(", "));
    }

    private static String toString(Object obj) {
        if (obj instanceof CharSequence) return "'" + obj + "'";
        if (obj instanceof Object[]) return Arrays.deepToString((Object[]) obj);
        // @formatter:off
        if (obj == null)               return "null";
        if (!obj.getClass().isArray()) return obj.toString();
        if (obj instanceof byte[])     return Arrays.toString((byte[])    obj);
        if (obj instanceof int[])      return Arrays.toString((int[])     obj);
        if (obj instanceof double[])   return Arrays.toString((double[])  obj);
        if (obj instanceof float[])    return Arrays.toString((float[])   obj);
        if (obj instanceof short[])    return Arrays.toString((short[])   obj);
        if (obj instanceof boolean[])  return Arrays.toString((boolean[]) obj);
        if (obj instanceof char[])     return Arrays.toString((char[])    obj);
        if (obj instanceof long[])     return Arrays.toString((long[])    obj);
        throw new AssertionError(obj.getClass());
        // @formatter:on
    }

    // ---

    @SafeVarargs
    static <T> Set<T> setOf(T... values) {
        return new HashSet<>(Arrays.asList(values));
    }
/*
    static Stream<Token> s(String s) {
        return s.chars().filter(c -> c != ' ' && c != ';').mapToObj(c -> new Token((char) c));
    }

    static Set<Token> set(String s) {
        final Set<Token> set = new HashSet<>();
        for (int i = s.length(); --i >= 0;) {
            final char c = s.charAt(i);
            if (c != ' ' && c != ';')
                set.add(new Token(c));
        }
        if (set.size() < 2) {
            set.add(new Token("A"));
            set.add(new Token("B"));
        }
        return set;
    }*/

    // ---

    private static final Pattern LB = Pattern.compile("\\R");
    private static final PrintStream out = System.out;

    static {
        java.util.concurrent.Executors.newSingleThreadScheduledExecutor(r -> edu.kit.informatik._intern.terminal.TerminalUtil.daemon("Timeout-Timer", r)).schedule(() -> {
            out.println(Prefix.ERROR.prefix + "Timeout occurred.");
            Runtime.getRuntime().halt(1);
        }, 5, java.util.concurrent.TimeUnit.SECONDS);
    }

    static void init() {}

    static void printErr(String o) {
        if (!Main.PREFIX)
            LB.splitAsStream(o).forEachOrdered(System.err::println);
        else
            LB.splitAsStream(o).map(Prefix.ERROR.toString()::concat).forEachOrdered(out::println);
    }

    static void print(String o) {
        if (!Main.PREFIX)
            LB.splitAsStream(o).forEachOrdered(out::println);
        else
            LB.splitAsStream(o).map(Prefix.INPUT.toString()::concat).forEachOrdered(out::println);
    }
}
