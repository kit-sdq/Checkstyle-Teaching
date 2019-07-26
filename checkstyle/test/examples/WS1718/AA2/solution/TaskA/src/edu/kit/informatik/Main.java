package edu.kit.informatik;

public class Main {
    /**
     * The main method that is the entry point to the program.
     *
     * @param args An array of command line arguments.
     */
    public static void main(String[] args) {        
        OlympicGames connectSix = new OlympicGames();
        Command command = null;
        do {
            try {
                command = Command.executeMatching(Terminal.readLine(), connectSix);
            } catch (InputException e) {
                Terminal.printError(e.getMessage());
            }
        } while (command == null || command.isRunning());
    }
}