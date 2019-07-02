package edu.kit.checkstyle;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import edu.kit.checkstyle.util.FileUtil;
import edu.kit.checkstyle.util.SystemUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ListThings implements AutoCloseable {
    private static final List<String> CHECKS =
            Arrays.asList("PackageList", "ImportList", "ClassList", "EnumList", "MethodList");

    public static void main(final String[] args) {
        try (final ListThings listThings = new ListThings()) {
            listThings.parseAndProcess(args);
        }

        SystemUtil.exitSuccess();
    }

    private static Checker buildChecker() {
        final Configuration configuration = defaultConfiguration();

        try {
            final Checker checker = new Checker();
            final ClassLoader classLoader = Checker.class.getClassLoader();
            checker.setModuleClassLoader(classLoader);
            checker.configure(configuration);
            return checker;
        } catch (final Exception e) {
            ExceptionHandler.handleCheckerException(e);
            return null;
        }
    }

    private static Configuration defaultConfiguration() {
        final DefaultConfiguration root = new DefaultConfiguration("Checker");
        final DefaultConfiguration treeWalker = new DefaultConfiguration("TreeWalker");
        root.addChild(treeWalker);

        for (final String check : CHECKS) {
            treeWalker.addChild(new DefaultConfiguration(check));
        }

        root.addChild(new DefaultConfiguration("edu.kit.checkstyle.listeners.ListListener"));
        return root;
    }

    private final Checker checker = buildChecker();

    private void parseAndProcess(final String[] args) {
        final List<File> files = parseFilesFromArgs(args);
        processFiles(files);
    }

    private List<File> parseFilesFromArgs(final String[] args) {
        final List<File> files = new ArrayList<>();

        for (final String element : args) {
            FileUtil.addFilesRecursively(new File(element), files);
        }

        return files;
    }

    private void processFiles(final List<File> files) {
        try {
            checker.process(files);
        } catch (final CheckstyleException e) {
            ExceptionHandler.handleProcessException(e);
        }
    }

    @Override
    public void close() {
        checker.destroy();
    }
}
