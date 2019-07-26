package edu.kit.informatik;

import edu.kit.informatik.entity.Player;
import edu.kit.informatik.entity.board.FieldIdentifier;
import edu.kit.informatik.entity.manager.GameManager;
import edu.kit.informatik.entity.rule.Rule;
import edu.kit.informatik.exception.SemanticsException;
import edu.kit.informatik.exception.SyntaxException;

/**
 * This class initializes the game and finally starts the interactive
 * command-line sequence.
 * 
 * @author Martin Löper
 * @version 1.0
 */
public final class MainClass {
 	/**
     * This class does not need to be instantiated. The only significant element
     * is the main method.
     */
    private MainClass() { }

    /**
     * The program's main entry point. It initializes the game.
     * 
     * @param args no command-line arguments allowed (i.e. empty string array)
     */
    public static void main(String[] args) {
        /* check if exactly one command-line argument is provided */
        if (args.length != 0) {
            Terminal.printLine("Error, no command-line arguments allowed.");
            return;
        }
        
        //Terminal.printLine("Interactive command-line started.\n");
        //Terminal.printLine("You can run: " + CommandLine.Command.getAllCommandsAsString() + "\n");
        //Terminal.printLine("You have the special options to run: \nki [RULES/optional]\nki auto [RULES/optional]\n");
        CommandLine.run(GameManager.DUMMY_INACTIVE_GAME_MANAGER);
    }
}
