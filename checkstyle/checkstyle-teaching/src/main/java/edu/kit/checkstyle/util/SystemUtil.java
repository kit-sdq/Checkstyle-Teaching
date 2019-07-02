package edu.kit.checkstyle.util;

public final class SystemUtil {
    public static void exitSuccess() {
        System.exit(0);
        assert false;
    }

    public static void exitError() {
        System.exit(1);
        assert false;
    }

    private SystemUtil() {
        throw new IllegalStateException();
    }
}