package edu.kit.informatik.UI;

import edu.kit.informatik.Prize;
import edu.kit.informatik.MontyHallGame;
import edu.kit.informatik.Run;
import edu.kit.informatik.Terminal;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This is the entry point class for the program containing the main method.
 *
 * @author Peter
 * @version 1.0
 */
public class Main {
    /**
     * The number of gates the game uses.
     */
    public static final int NUMBER_OF_GATES = 3;
    private static final int NUMBER_OF_ARGUMENTS = 1;

    /**
     * The main method of the program.
     *
     * @param args The arguments that are passed to the program at launch as array.
     */
    public static void main(String[] args) {
        if (args.length < NUMBER_OF_ARGUMENTS) {
            Terminal.printError("not enough arguments, there needs to be a filepath.");
            return;
        } else if (args.length > NUMBER_OF_ARGUMENTS) {
            Terminal.printError("too many arguments, there needs to be a filepath.");
            return;
        }
        
        Queue<Run> runs;
        try {
            runs = parseFile(Terminal.readFile(args[0]));
        } catch (IllegalFileFormatException e) {
            Terminal.printError(e.getMessage());
            return;
        }
        
        MontyHallGame game = new MontyHallGame(runs);
        Terminal.printLine(game.getPrompt());
        
        Command command = null;
        do {
            try {
                command = Command.executeMatching(Terminal.readLine(), game);
                if (command.isRunning()) {
                    Terminal.printLine(game.getPrompt());
                }
            } catch (InputException e) {
                Terminal.printError(e.getMessage());
                Terminal.printLine(game.getPrompt());
            }
        } while (command == null || command.isRunning());
    }

    /**
     * Parses an monty hall input file with one line per simulation.
     *
     * @param lines The input file as a string array of lines.
     * @return A FIFO queue of runs to simulate.
     * @throws IllegalFileFormatException if there is a syntactical or semantical error in the file.
     */
    private static Queue<Run> parseFile(String[] lines) throws IllegalFileFormatException {
        if (lines.length == 0) {
            throw new IllegalFileFormatException("file can't be empty");
        }
      
        Queue<Run> runs = new LinkedList<>();
        for (String line : lines) {
            String[] prizeStrings = line.split(",");
            if (prizeStrings.length != NUMBER_OF_GATES) {
                throw new IllegalFileFormatException("the number of gates given is not " + NUMBER_OF_GATES + ".");
            }

            boolean foundCar = false;
            Prize[] prizes = new Prize[NUMBER_OF_GATES];
            for (int i = 0; i < prizeStrings.length; i++) {
                prizes[i] = Prize.fromIdentifier(prizeStrings[i]);
                if (prizes[i].equals(Prize.CAR) && foundCar) {
                    throw new IllegalFileFormatException("there is more than one car present in a line.");
                } else if (prizes[i].equals(Prize.CAR) && !foundCar) {
                    foundCar = true;
                }
            }
            
            runs.add(new Run(prizes));
        }
        
        return runs;
    }
}
