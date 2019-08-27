
package _edu.kit.informatik;

import static java.util.Collections.emptySet;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.joining;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

//import edu.kit.informatik.matchthree.framework.Position;

/*
Don't modify stuff in here below the constructor as this stuff is auto generated, modify the builder instead.
 */
final class Assert {

    public static AssertError unexpectedCall(Method method) {
        /*
         * throw unexpectedCall(new Object() {}.getClass().getEnclosingMethod());
         */
        return new AssertError("Unexpected call: " + method.toGenericString());
    }

    /*public static void assertMatchesEqualLenient(Set<Set<Position>> actual, Position... expected) {
        assertMatchesEqualLenient(actual, Util.setOf(Util.setOf(expected)));
    }

    public static void assertMatchesEqual(Set<Set<Position>> actual, Position... expected) {
        assertMatchesEqual(actual, Util.setOf(Util.setOf(expected)));
    }

    private static final Set<Position> nullPosition = Collections.singleton(null);
    public static void assertMatchesEqualLenient(Set<Set<Position>> actual, Set<Set<Position>> expected) {
        final int ae = (actual.contains(emptySet())   ? 1 : 0) + (actual.contains(nullPosition)   ? 1 : 0);
        final int ee = (expected.contains(emptySet()) ? 1 : 0) + (expected.contains(nullPosition) ? 1 : 0);
        if (actual.size() - ae != expected.size() - ee)
            throw new AssertError("Invalid count of matches, expected " + expected.size() + ", but got " + actual.size()
                    + " (expected " + matchesToString(expected) + ", but got " + matchesToString(actual) + ")");
        /*
         * This feels somewhat wrong as it is way too lenient...
         *
        for (final Set<Position> p : expected)
            if (!p.equals(emptySet()) && !p.equals(nullPosition) && !actual.contains(p))
                throw new AssertError("Expected " + matchesToString(expected) + ", but got " + matchesToString(actual));
    }

    public static void assertMatchesEqual(Set<Set<Position>> actual, Set<Set<Position>> expected) {
        if (actual.size() != expected.size())
            throw new AssertError("Invalid count of matches, expected " + expected.size() + ", but got " + actual.size()
                    + " (expected " + matchesToString(expected) + ", but got " + matchesToString(actual) + ")");
        if (!expected.equals(actual))
            throw new AssertError("Expected " + matchesToString(expected) + ", but got " + matchesToString(actual));
        if (!actual.equals(expected))
            throw new AssertError("Bad set: " + matchesToString(actual));
    }

    private static final Comparator<Set<?>> matchSetCmp = Comparator
            .nullsFirst(comparingInt(Set<?>::size).thenComparingInt(Set::hashCode));
    private static final Comparator<Position> positionCmp = Comparator
            .nullsFirst(comparingInt((Position p) -> p.x).thenComparingInt(p -> p.y));

    private static String matchesToString(Set<Set<Position>> s) {
        return s.size() > 10
                ? "..."
                : s.stream().sorted(matchSetCmp)
                        .map(set -> set.size() > 20 ? "..." : set.stream().sorted(positionCmp).map(Position::toString)
                                .collect(joining(",", "[", "]")))
                        .collect(joining(",", "{", "}"));
    }*/

    // ---

    private Assert() {
    }

    public static void assertUnreachable(String msg) {
        throw new AssertError(msg);
    }

    public static void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertError("Expected true");
        }
    }

    public static void assertTrue(String msg, boolean condition) {
        if (!condition) {
            throw new AssertError(msg);
        }
    }

    public static void assertFalse(boolean condition) {
        if (condition) {
            throw new AssertError("Expected false");
        }
    }

    public static void assertFalse(String msg, boolean condition) {
        if (condition) {
            throw new AssertError(msg);
        }
    }

    public static void assertEquals(boolean actual, boolean expected) {
        if (actual != expected) {
            throw new AssertError("Expected " + expected + ", but got " + actual);
        }
    }

    public static void assertEquals(byte actual, byte expected) {
        if (actual != expected) {
            throw new AssertError("Expected " + expected + ", but got " + actual);
        }
    }

    public static void assertEquals(char actual, char expected) {
        if (actual != expected) {
            throw new AssertError("Expected " + expected + ", but got " + actual);
        }
    }

    public static void assertEquals(double actual, double expected) {
        if (actual != expected) {
            throw new AssertError("Expected " + expected + ", but got " + actual);
        }
    }

    public static void assertEquals(float actual, float expected) {
        if (actual != expected) {
            throw new AssertError("Expected " + expected + ", but got " + actual);
        }
    }

    public static void assertEquals(int actual, int expected) {
        if (actual != expected) {
            throw new AssertError("Expected " + expected + ", but got " + actual);
        }
    }

    public static void assertEquals(long actual, long expected) {
        if (actual != expected) {
            throw new AssertError("Expected " + expected + ", but got " + actual);
        }
    }

    public static void assertEquals(short actual, short expected) {
        if (actual != expected) {
            throw new AssertError("Expected " + expected + ", but got " + actual);
        }
    }

    public static void assertEquals(Object actual, Object expected) {
        if (actual == null ? expected != null : !actual.equals(expected)) {
            throw new AssertError("Expected " + expected + ", but got " + actual);
        }
    }

    public static void assertEquals(String msg, boolean actual, boolean expected) {
        if (actual != expected) {
            throw new AssertError(msg + ": expected " + expected + ", but got " + actual);
        }
    }

    public static void assertEquals(String msg, byte actual, byte expected) {
        if (actual != expected) {
            throw new AssertError(msg + ": expected " + expected + ", but got " + actual);
        }
    }

    public static void assertEquals(String msg, char actual, char expected) {
        if (actual != expected) {
            throw new AssertError(msg + ": expected " + expected + ", but got " + actual);
        }
    }

    public static void assertEquals(String msg, double actual, double expected) {
        if (actual != expected) {
            throw new AssertError(msg + ": expected " + expected + ", but got " + actual);
        }
    }

    public static void assertEquals(String msg, float actual, float expected) {
        if (actual != expected) {
            throw new AssertError(msg + ": expected " + expected + ", but got " + actual);
        }
    }

    public static void assertEquals(String msg, int actual, int expected) {
        if (actual != expected) {
            throw new AssertError(msg + ": expected " + expected + ", but got " + actual);
        }
    }

    public static void assertEquals(String msg, long actual, long expected) {
        if (actual != expected) {
            throw new AssertError(msg + ": expected " + expected + ", but got " + actual);
        }
    }

    public static void assertEquals(String msg, short actual, short expected) {
        if (actual != expected) {
            throw new AssertError(msg + ": expected " + expected + ", but got " + actual);
        }
    }

    public static void assertEquals(String msg, Object actual, Object expected) {
        if (actual == null ? expected != null : !actual.equals(expected)) {
            throw new AssertError(msg + ": expected " + expected + ", but got " + actual);
        }
    }

    public static void assertNotEquals(boolean actual, boolean expected) {
        if (actual == expected) {
            throw new AssertError("Expected not " + expected);
        }
    }

    public static void assertNotEquals(byte actual, byte expected) {
        if (actual == expected) {
            throw new AssertError("Expected not " + expected);
        }
    }

    public static void assertNotEquals(char actual, char expected) {
        if (actual == expected) {
            throw new AssertError("Expected not " + expected);
        }
    }

    public static void assertNotEquals(double actual, double expected) {
        if (actual == expected) {
            throw new AssertError("Expected not " + expected);
        }
    }

    public static void assertNotEquals(float actual, float expected) {
        if (actual == expected) {
            throw new AssertError("Expected not " + expected);
        }
    }

    public static void assertNotEquals(int actual, int expected) {
        if (actual == expected) {
            throw new AssertError("Expected not " + expected);
        }
    }

    public static void assertNotEquals(long actual, long expected) {
        if (actual == expected) {
            throw new AssertError("Expected not " + expected);
        }
    }

    public static void assertNotEquals(short actual, short expected) {
        if (actual == expected) {
            throw new AssertError("Expected not " + expected);
        }
    }

    public static void assertNotEquals(Object actual, Object expected) {
        if (actual == null ? expected == null : actual.equals(expected)) {
            String sa = String.valueOf(actual);
            String se = String.valueOf(expected);
            if (sa.equals(se)) {
                throw new AssertError("Expected not " + expected);
            } else {
                throw new AssertError("Expected not " + sa + ", but got " + se);
            }
        }
    }

    public static void assertNotEquals(String msg, boolean actual, boolean expected) {
        if (actual == expected) {
            throw new AssertError(msg + ": expected not " + expected);
        }
    }

    public static void assertNotEquals(String msg, byte actual, byte expected) {
        if (actual == expected) {
            throw new AssertError(msg + ": expected not " + expected);
        }
    }

    public static void assertNotEquals(String msg, char actual, char expected) {
        if (actual == expected) {
            throw new AssertError(msg + ": expected not " + expected);
        }
    }

    public static void assertNotEquals(String msg, double actual, double expected) {
        if (actual == expected) {
            throw new AssertError(msg + ": expected not " + expected);
        }
    }

    public static void assertNotEquals(String msg, float actual, float expected) {
        if (actual == expected) {
            throw new AssertError(msg + ": expected not " + expected);
        }
    }

    public static void assertNotEquals(String msg, int actual, int expected) {
        if (actual == expected) {
            throw new AssertError(msg + ": expected not " + expected);
        }
    }

    public static void assertNotEquals(String msg, long actual, long expected) {
        if (actual == expected) {
            throw new AssertError(msg + ": expected not " + expected);
        }
    }

    public static void assertNotEquals(String msg, short actual, short expected) {
        if (actual == expected) {
            throw new AssertError(msg + ": expected not " + expected);
        }
    }

    public static void assertNotEquals(String msg, Object actual, Object expected) {
        if (actual == null ? expected == null : actual.equals(expected)) {
            String sa = String.valueOf(actual);
            String se = String.valueOf(expected);
            if (sa.equals(se)) {
                throw new AssertError(msg + ": expected not " + expected);
            } else {
                throw new AssertError(msg + ": expected not " + sa + ", but got " + se);
            }
        }
    }

    public static void assertSame(boolean actual, boolean expected) {
        if (actual != expected) {
            throw new AssertError("Expected " + expected + ", but got " + actual);
        }
    }

    public static void assertSame(byte actual, byte expected) {
        if (actual != expected) {
            throw new AssertError("Expected " + expected + ", but got " + actual);
        }
    }

    public static void assertSame(char actual, char expected) {
        if (actual != expected) {
            throw new AssertError("Expected " + expected + ", but got " + actual);
        }
    }

    public static void assertSame(double actual, double expected) {
        if (actual != expected) {
            throw new AssertError("Expected " + expected + ", but got " + actual);
        }
    }

    public static void assertSame(float actual, float expected) {
        if (actual != expected) {
            throw new AssertError("Expected " + expected + ", but got " + actual);
        }
    }

    public static void assertSame(int actual, int expected) {
        if (actual != expected) {
            throw new AssertError("Expected " + expected + ", but got " + actual);
        }
    }

    public static void assertSame(long actual, long expected) {
        if (actual != expected) {
            throw new AssertError("Expected " + expected + ", but got " + actual);
        }
    }

    public static void assertSame(short actual, short expected) {
        if (actual != expected) {
            throw new AssertError("Expected " + expected + ", but got " + actual);
        }
    }

    public static void assertSame(Object actual, Object expected) {
        if (actual != expected) {
            throw new AssertError("Expected " + expected + ", but got " + actual);
        }
    }

    public static void assertSame(String msg, boolean actual, boolean expected) {
        if (actual != expected) {
            throw new AssertError(msg + ": expected " + expected + ", but got " + actual);
        }
    }

    public static void assertSame(String msg, byte actual, byte expected) {
        if (actual != expected) {
            throw new AssertError(msg + ": expected " + expected + ", but got " + actual);
        }
    }

    public static void assertSame(String msg, char actual, char expected) {
        if (actual != expected) {
            throw new AssertError(msg + ": expected " + expected + ", but got " + actual);
        }
    }

    public static void assertSame(String msg, double actual, double expected) {
        if (actual != expected) {
            throw new AssertError(msg + ": expected " + expected + ", but got " + actual);
        }
    }

    public static void assertSame(String msg, float actual, float expected) {
        if (actual != expected) {
            throw new AssertError(msg + ": expected " + expected + ", but got " + actual);
        }
    }

    public static void assertSame(String msg, int actual, int expected) {
        if (actual != expected) {
            throw new AssertError(msg + ": expected " + expected + ", but got " + actual);
        }
    }

    public static void assertSame(String msg, long actual, long expected) {
        if (actual != expected) {
            throw new AssertError(msg + ": expected " + expected + ", but got " + actual);
        }
    }

    public static void assertSame(String msg, short actual, short expected) {
        if (actual != expected) {
            throw new AssertError(msg + ": expected " + expected + ", but got " + actual);
        }
    }

    public static void assertSame(String msg, Object actual, Object expected) {
        if (actual != expected) {
            throw new AssertError(msg + ": expected " + expected + ", but got " + actual);
        }
    }

    public static void assertNot(boolean actual, boolean expected) {
        if (actual == expected) {
            throw new AssertError("Expected not " + expected);
        }
    }

    public static void assertNot(byte actual, byte expected) {
        if (actual == expected) {
            throw new AssertError("Expected not " + expected);
        }
    }

    public static void assertNot(char actual, char expected) {
        if (actual == expected) {
            throw new AssertError("Expected not " + expected);
        }
    }

    public static void assertNot(double actual, double expected) {
        if (actual == expected) {
            throw new AssertError("Expected not " + expected);
        }
    }

    public static void assertNot(float actual, float expected) {
        if (actual == expected) {
            throw new AssertError("Expected not " + expected);
        }
    }

    public static void assertNot(int actual, int expected) {
        if (actual == expected) {
            throw new AssertError("Expected not " + expected);
        }
    }

    public static void assertNot(long actual, long expected) {
        if (actual == expected) {
            throw new AssertError("Expected not " + expected);
        }
    }

    public static void assertNot(short actual, short expected) {
        if (actual == expected) {
            throw new AssertError("Expected not " + expected);
        }
    }

    public static void assertNot(Object actual, Object expected) {
        if (actual == expected) {
            throw new AssertError("Expected not " + expected);
        }
    }

    public static void assertNot(String msg, boolean actual, boolean expected) {
        if (actual == expected) {
            throw new AssertError(msg + ": expected not " + expected);
        }
    }

    public static void assertNot(String msg, byte actual, byte expected) {
        if (actual == expected) {
            throw new AssertError(msg + ": expected not " + expected);
        }
    }

    public static void assertNot(String msg, char actual, char expected) {
        if (actual == expected) {
            throw new AssertError(msg + ": expected not " + expected);
        }
    }

    public static void assertNot(String msg, double actual, double expected) {
        if (actual == expected) {
            throw new AssertError(msg + ": expected not " + expected);
        }
    }

    public static void assertNot(String msg, float actual, float expected) {
        if (actual == expected) {
            throw new AssertError(msg + ": expected not " + expected);
        }
    }

    public static void assertNot(String msg, int actual, int expected) {
        if (actual == expected) {
            throw new AssertError(msg + ": expected not " + expected);
        }
    }

    public static void assertNot(String msg, long actual, long expected) {
        if (actual == expected) {
            throw new AssertError(msg + ": expected not " + expected);
        }
    }

    public static void assertNot(String msg, short actual, short expected) {
        if (actual == expected) {
            throw new AssertError(msg + ": expected not " + expected);
        }
    }

    public static void assertNot(String msg, Object actual, Object expected) {
        if (actual == expected) {
            throw new AssertError(msg + ": expected not " + expected);
        }
    }

    public static void assertArrayEquals(boolean[] actual, boolean[] expected) {
        if (!Arrays.equals(actual, expected)) {
            throw new AssertError("Expected " + Arrays.toString(expected) + ", but got " + Arrays.toString(actual));
        }
    }

    public static void assertArrayEquals(byte[] actual, byte[] expected) {
        if (!Arrays.equals(actual, expected)) {
            throw new AssertError("Expected " + Arrays.toString(expected) + ", but got " + Arrays.toString(actual));
        }
    }

    public static void assertArrayEquals(char[] actual, char[] expected) {
        if (!Arrays.equals(actual, expected)) {
            throw new AssertError("Expected " + Arrays.toString(expected) + ", but got " + Arrays.toString(actual));
        }
    }

    public static void assertArrayEquals(double[] actual, double[] expected) {
        if (!Arrays.equals(actual, expected)) {
            throw new AssertError("Expected " + Arrays.toString(expected) + ", but got " + Arrays.toString(actual));
        }
    }

    public static void assertArrayEquals(float[] actual, float[] expected) {
        if (!Arrays.equals(actual, expected)) {
            throw new AssertError("Expected " + Arrays.toString(expected) + ", but got " + Arrays.toString(actual));
        }
    }

    public static void assertArrayEquals(int[] actual, int[] expected) {
        if (!Arrays.equals(actual, expected)) {
            throw new AssertError("Expected " + Arrays.toString(expected) + ", but got " + Arrays.toString(actual));
        }
    }

    public static void assertArrayEquals(long[] actual, long[] expected) {
        if (!Arrays.equals(actual, expected)) {
            throw new AssertError("Expected " + Arrays.toString(expected) + ", but got " + Arrays.toString(actual));
        }
    }

    public static void assertArrayEquals(short[] actual, short[] expected) {
        if (!Arrays.equals(actual, expected)) {
            throw new AssertError("Expected " + Arrays.toString(expected) + ", but got " + Arrays.toString(actual));
        }
    }

    public static void assertArrayEquals(Object[] actual, Object[] expected) {
        if (!Arrays.equals(actual, expected)) {
            throw new AssertError("Expected " + Arrays.toString(expected) + ", but got " + Arrays.toString(actual));
        }
    }

    public static void assertArrayEquals(String msg, boolean[] actual, boolean[] expected) {
        if (!Arrays.equals(actual, expected)) {
            throw new AssertError(msg + ": expected " + Arrays.toString(expected) + ", but got " + Arrays.toString(actual));
        }
    }

    public static void assertArrayEquals(String msg, byte[] actual, byte[] expected) {
        if (!Arrays.equals(actual, expected)) {
            throw new AssertError(msg + ": expected " + Arrays.toString(expected) + ", but got " + Arrays.toString(actual));
        }
    }

    public static void assertArrayEquals(String msg, char[] actual, char[] expected) {
        if (!Arrays.equals(actual, expected)) {
            throw new AssertError(msg + ": expected " + Arrays.toString(expected) + ", but got " + Arrays.toString(actual));
        }
    }

    public static void assertArrayEquals(String msg, double[] actual, double[] expected) {
        if (!Arrays.equals(actual, expected)) {
            throw new AssertError(msg + ": expected " + Arrays.toString(expected) + ", but got " + Arrays.toString(actual));
        }
    }

    public static void assertArrayEquals(String msg, float[] actual, float[] expected) {
        if (!Arrays.equals(actual, expected)) {
            throw new AssertError(msg + ": expected " + Arrays.toString(expected) + ", but got " + Arrays.toString(actual));
        }
    }

    public static void assertArrayEquals(String msg, int[] actual, int[] expected) {
        if (!Arrays.equals(actual, expected)) {
            throw new AssertError(msg + ": expected " + Arrays.toString(expected) + ", but got " + Arrays.toString(actual));
        }
    }

    public static void assertArrayEquals(String msg, long[] actual, long[] expected) {
        if (!Arrays.equals(actual, expected)) {
            throw new AssertError(msg + ": expected " + Arrays.toString(expected) + ", but got " + Arrays.toString(actual));
        }
    }

    public static void assertArrayEquals(String msg, short[] actual, short[] expected) {
        if (!Arrays.equals(actual, expected)) {
            throw new AssertError(msg + ": expected " + Arrays.toString(expected) + ", but got " + Arrays.toString(actual));
        }
    }

    public static void assertArrayEquals(String msg, Object[] actual, Object[] expected) {
        if (!Arrays.equals(actual, expected)) {
            throw new AssertError(msg + ": expected " + Arrays.toString(expected) + ", but got " + Arrays.toString(actual));
        }
    }

    public static void assertArrayNotEquals(boolean[] actual, boolean[] expected) {
        if (Arrays.equals(actual, expected)) {
            throw new AssertError("Expected not " + Arrays.toString(expected));
        }
    }

    public static void assertArrayNotEquals(byte[] actual, byte[] expected) {
        if (Arrays.equals(actual, expected)) {
            throw new AssertError("Expected not " + Arrays.toString(expected));
        }
    }

    public static void assertArrayNotEquals(char[] actual, char[] expected) {
        if (Arrays.equals(actual, expected)) {
            throw new AssertError("Expected not " + Arrays.toString(expected));
        }
    }

    public static void assertArrayNotEquals(double[] actual, double[] expected) {
        if (Arrays.equals(actual, expected)) {
            throw new AssertError("Expected not " + Arrays.toString(expected));
        }
    }

    public static void assertArrayNotEquals(float[] actual, float[] expected) {
        if (Arrays.equals(actual, expected)) {
            throw new AssertError("Expected not " + Arrays.toString(expected));
        }
    }

    public static void assertArrayNotEquals(int[] actual, int[] expected) {
        if (Arrays.equals(actual, expected)) {
            throw new AssertError("Expected not " + Arrays.toString(expected));
        }
    }

    public static void assertArrayNotEquals(long[] actual, long[] expected) {
        if (Arrays.equals(actual, expected)) {
            throw new AssertError("Expected not " + Arrays.toString(expected));
        }
    }

    public static void assertArrayNotEquals(short[] actual, short[] expected) {
        if (Arrays.equals(actual, expected)) {
            throw new AssertError("Expected not " + Arrays.toString(expected));
        }
    }

    public static void assertArrayNotEquals(Object[] actual, Object[] expected) {
        if (Arrays.equals(actual, expected)) {
            throw new AssertError("Expected not " + Arrays.toString(expected));
        }
    }

    public static void assertArrayNotEquals(String msg, boolean[] actual, boolean[] expected) {
        if (Arrays.equals(actual, expected)) {
            throw new AssertError(msg + ": expected not " + Arrays.toString(expected));
        }
    }

    public static void assertArrayNotEquals(String msg, byte[] actual, byte[] expected) {
        if (Arrays.equals(actual, expected)) {
            throw new AssertError(msg + ": expected not " + Arrays.toString(expected));
        }
    }

    public static void assertArrayNotEquals(String msg, char[] actual, char[] expected) {
        if (Arrays.equals(actual, expected)) {
            throw new AssertError(msg + ": expected not " + Arrays.toString(expected));
        }
    }

    public static void assertArrayNotEquals(String msg, double[] actual, double[] expected) {
        if (Arrays.equals(actual, expected)) {
            throw new AssertError(msg + ": expected not " + Arrays.toString(expected));
        }
    }

    public static void assertArrayNotEquals(String msg, float[] actual, float[] expected) {
        if (Arrays.equals(actual, expected)) {
            throw new AssertError(msg + ": expected not " + Arrays.toString(expected));
        }
    }

    public static void assertArrayNotEquals(String msg, int[] actual, int[] expected) {
        if (Arrays.equals(actual, expected)) {
            throw new AssertError(msg + ": expected not " + Arrays.toString(expected));
        }
    }

    public static void assertArrayNotEquals(String msg, long[] actual, long[] expected) {
        if (Arrays.equals(actual, expected)) {
            throw new AssertError(msg + ": expected not " + Arrays.toString(expected));
        }
    }

    public static void assertArrayNotEquals(String msg, short[] actual, short[] expected) {
        if (Arrays.equals(actual, expected)) {
            throw new AssertError(msg + ": expected not " + Arrays.toString(expected));
        }
    }

    public static void assertArrayNotEquals(String msg, Object[] actual, Object[] expected) {
        if (Arrays.equals(actual, expected)) {
            throw new AssertError(msg + ": expected not " + Arrays.toString(expected));
        }
    }

    public static final class AssertError extends Error {
        private static final long serialVersionUID = 5820885358793622089L;

        AssertError(String msg) {
            super(msg);
        }
    }
}
