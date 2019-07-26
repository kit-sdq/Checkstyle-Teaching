package edu.kit.informatik.UI;

import edu.kit.informatik.RPSSLGame;
import edu.kit.informatik.Shape;
import edu.kit.informatik.ShapeConversionException;
import edu.kit.informatik.Terminal;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This is the entry point class for the program containing the main method.
 *
 * @author Peter
 * @version 1.0
 */
public class Main {
    private static final int NUMBER_OF_ARGUMENTS = 2;
    private static final Set<Integer> NUMBER_OF_RUNS 
            = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(1, 3, 5)));
    
    /**
     * The main method of the program.
     *
     * @param args The arguments that are passed to the program at launch as array.
     */
    public static void main(String[] args) {
        if (args.length < NUMBER_OF_ARGUMENTS) {
            Terminal.printError("not enough arguments, there needs to be a filepath and the number of runs.");
            return;
        } else if (args.length > NUMBER_OF_ARGUMENTS) {
            Terminal.printError("too many arguments, there needs to be a filepath and the number of runs.");
            return;
        }
        
        int numberOfRuns;
        try {
            numberOfRuns = Integer.parseInt(args[1]);
        } catch (NumberFormatException exception) {
            Terminal.printError("the number of runs is not a number.");
            return;
        }
        
        if (!NUMBER_OF_RUNS.contains(numberOfRuns)) {
            String possibleNumberOfRuns = NUMBER_OF_RUNS.stream()
                    .map(number -> Integer.toString(number))
                    .collect(Collectors.joining(",", "[", "]"));
            Terminal.printError(numberOfRuns + " runs is not supported, needs to be one of "
                    + possibleNumberOfRuns + ".");
            return;
        }
        
        List<List<Shape>> enemyRuns = parseFile(Terminal.readFile(args[0]), numberOfRuns);
        if (enemyRuns == null) {
            return;
        }
        
        RPSSLGame rpssl = new RPSSLGame(enemyRuns);
        Terminal.printLine(rpssl.getPrompt());
        
        Command command = null;
        do {
            try {
                command = Command.executeMatching(Terminal.readLine(), rpssl);
            } catch (InputException e) {
                Terminal.printError(e.getMessage());
                if (!rpssl.isGameOver()) {
                    Terminal.printLine(rpssl.getPrompt());
                }
            }
        } while (command == null || command.isRunning());
    }

    private static List<List<Shape>> parseFile(String[] lines, int numberOfRuns) {
        if (lines.length == 0) {
            Terminal.printError("file can't be empty");
            return null;
        } else if (lines.length != numberOfRuns) {
            Terminal.printError("file must have one line for each run (" + numberOfRuns + ").");
            return null;
        }
      
        List<List<Shape>> runs = new LinkedList<>();
        for (int i = 0; i < lines.length; i++) {
            String[] shapes = lines[i].split(",");
            
            if (shapes.length != RPSSLGame.ITERATIONS_PER_RUN) {
                Terminal.printError("there need to be " + RPSSLGame.ITERATIONS_PER_RUN + " shapes per line.");
                return null;
            }

            try {
                runs.add(Shape.convertStringsToShapes(shapes));
            } catch (ShapeConversionException exception) {
                printLineError(i + 1, exception.getMessage());
                return null;
            }
        }
        
        return runs;
    }

    private static void printLineError(int lineNumber, String message) {
        Terminal.printError("line " + (lineNumber + 1) + ": " + message);
    }
}
