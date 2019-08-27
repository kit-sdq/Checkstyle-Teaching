package edu.kit.informatik.UI;

import edu.kit.informatik.BookingSystem;
import edu.kit.informatik.Terminal;

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
       BookingSystem bookingSystem = new BookingSystem();
        Command command = null;
        do {
            try {
                command = Command.executeMatching(Terminal.readLine(), bookingSystem);
            } catch (InputException e) {
                Terminal.printError(e.getMessage());
            }
        } while (command == null || command.isRunning());
    }
}
