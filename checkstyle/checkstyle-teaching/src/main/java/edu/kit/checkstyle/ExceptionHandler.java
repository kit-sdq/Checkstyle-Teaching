package edu.kit.checkstyle;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import edu.kit.checkstyle.util.SystemUtil;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;

import java.io.PrintWriter;

final class ExceptionHandler {
    private static final PrintWriter errorStream = new PrintWriter(System.out);

    static void handleParseException(final ParseException e) {
        e.printStackTrace(errorStream);
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(errorStream, 10, "java " + Main.class.getName() + " <source.java>* < <config.xml>", null,
                CheckstyleCommandLine.OPTIONS, 0, 0, null);
        flushAndExit();
    }

    static void handleConfigurationException(final CheckstyleException e) {
        errorStream.println("Error loading configuration file");
        e.printStackTrace(errorStream);
        flushAndExit();
    }

    static void handleCheckerException(final Exception e) {
        errorStream.println("Unable to create Checker: " + e.getMessage());
        e.printStackTrace(errorStream);
        flushAndExit();
    }

    static void handleProcessException(final CheckstyleException e) {
        errorStream.println("Unable to process files: " + e.getMessage());
        e.printStackTrace(errorStream);
        flushAndExit();
    }

    private static void flushAndExit() {
        errorStream.flush();
        SystemUtil.exitError();
    }

    private ExceptionHandler() {
        throw new IllegalStateException();
    }
}