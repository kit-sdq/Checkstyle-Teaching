package edu.kit.checkstyle.util;

public final class StringUtil {
    // todo: unnecessary as of java 8, use String#join instead
    public static String join(final String separator, final String... strings) {
        final StringBuilder joined = new StringBuilder();
        if (strings.length == 0) return "";
        joined.append(strings[0]);

        for (int i = 1; i < strings.length; i++) {
            joined.append(separator);
            joined.append(strings[i]);
        }

        return joined.toString();
    }

    private StringUtil() {
        throw new IllegalStateException();
    }
}