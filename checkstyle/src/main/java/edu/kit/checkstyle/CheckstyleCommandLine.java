package edu.kit.checkstyle;

import edu.kit.checkstyle.util.FileUtil;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

final class CheckstyleCommandLine {
    static final Options OPTIONS =
            new Options()/*todo .addOption(new Option("r", "recursive", true, "Checks all paths recursively."))*/;

    private final CommandLine commandLine;

    CheckstyleCommandLine(final String[] args) throws ParseException {
        final CommandLineParser parser = new DefaultParser();
        commandLine = parser.parse(OPTIONS, args);
    }

    List<File> getFiles() {
        final List<File> files = new ArrayList<>();
        addFiles(files);

        if (commandLine.hasOption("r")) {
            addFilesRecursively(files);
        }

        return files;
    }

    private void addFiles(final List<File> files) {
        for (final String element : commandLine.getArgs()) {
            final File file = new File(element);
            files.add(file);
        }
    }

    private void addFilesRecursively(final List<File> files) {
        for (final String element : commandLine.getOptionValues("r")) {
            final File file = new File(element);
            FileUtil.addFilesRecursively(file, files);
        }
    }
}