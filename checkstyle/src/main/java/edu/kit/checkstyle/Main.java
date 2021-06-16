package edu.kit.checkstyle;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.PropertiesExpander;
import com.puppycrawl.tools.checkstyle.PropertyResolver;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import edu.kit.checkstyle.util.SystemUtil;
import edu.kit.kastel.sdq.artemis.checkstyle.listeners.QualifiedListener;

import org.apache.commons.cli.ParseException;
import org.xml.sax.InputSource;

import java.io.File;
import java.util.List;
import java.util.Properties;

public final class Main implements AutoCloseable {
    public static void main(final String[] args) {
        try (final Main main = new Main()) {
            main.parseAndProcess(args);
        }

        SystemUtil.exitSuccess();
    }

    private static Checker buildChecker() {
        final Properties properties = System.getProperties();
        final Configuration configuration = readConfiguration(properties);

        try {
            final Checker checker = new Checker();
            final ClassLoader classLoader = Checker.class.getClassLoader();
            checker.setModuleClassLoader(classLoader);
            checker.configure(configuration);

            final AuditListener listener = new QualifiedListener();
            checker.addListener(listener);

            return checker;
        } catch (final CheckstyleException | SecurityException e) {
            ExceptionHandler.handleCheckerException(e);
            return null;
        }
    }

    private static Configuration readConfiguration(final Properties properties) {
        final InputSource configurationSource = new InputSource(System.in);
        final PropertyResolver propertyResolver = new PropertiesExpander(properties);

        try {
            return ConfigurationLoader.loadConfiguration(configurationSource, propertyResolver,
                    ConfigurationLoader.IgnoredModulesOptions.OMIT);
        } catch (final CheckstyleException e) {
            ExceptionHandler.handleConfigurationException(e);
            return null;
        }
    }

    private final Checker checker = buildChecker();

    private void parseAndProcess(final String[] args) {
        final List<File> files = parseFilesFromArgs(args);
        processFiles(files);
    }

    private List<File> parseFilesFromArgs(final String[] args) {
        try {
            final CheckstyleCommandLine commandLine = new CheckstyleCommandLine(args);
            return commandLine.getFiles();
        } catch (final ParseException e) {
            ExceptionHandler.handleParseException(e);
            return null;
        }
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