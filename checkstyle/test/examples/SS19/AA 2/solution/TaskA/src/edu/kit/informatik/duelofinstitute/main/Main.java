package edu.kit.informatik.duelofinstitute.main;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.duelofinstitute.exception.InputException;
import edu.kit.informatik.duelofinstitute.exception.InvalidParameterException;
import edu.kit.informatik.duelofinstitute.game.Game;

/**
 * This is the entry point class for the program containing the main method.
 *
 * @author Peter
 * @version 1.0
 */
public class Main {

    /**
     * The main method of the program.
     *
     * @param args The arguments that are passed to the program at launch as array.
     */
    public static void main(String[] args) {
        
        Game game = new Game();
        
        Command command = null;
        do {
            try {
                command = Command.executeMatching(Terminal.readLine(), game);
                if (command.isRunning()) {
//                    Terminal.printLine(game);
                }
            } catch (InputException | InvalidParameterException e) {
                Terminal.printError(e.getMessage());
            }
        } while (command == null || command.isRunning());
    }
}
