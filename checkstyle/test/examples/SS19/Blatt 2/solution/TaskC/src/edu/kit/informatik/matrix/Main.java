/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.matrix;

import edu.kit.informatik.InputException;
import edu.kit.informatik.Terminal;

/**
 * This is the entry point class for the program containing the main method.
 *
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public class Main {
    private static final String QUIT = "quit";

    /**
     * The main method of the program.
     * 
     * @param args
     *            - the arguments that are passed to the program at launch as array.
     */
    public static void main(final String[] args) {
        String input = Terminal.readLine();
        while (!input.equals(QUIT)) {
            CommandEvaluator evaluator;
            try {
                evaluator = new CommandEvaluator(input);
                Terminal.printLine(evaluator.evaluate());
            } catch (InputException e) {
                Terminal.printError(e.getMessage());
            } catch (IllegalArgumentException e) {
                Terminal.printError(e.getMessage());
            }
            input = Terminal.readLine();
        }
    }

}
