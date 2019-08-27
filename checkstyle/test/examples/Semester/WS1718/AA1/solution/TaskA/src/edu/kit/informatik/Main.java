package edu.kit.informatik;

public class Main {
    /**
     * The main method that is the entry point to the program.
     *
     * @param args An array of command line arguments.
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            Terminal.printError("there must be exactly 3 arguments.");
            return;
        } else if (!args[0].equals("standard") && !args[0].equals("torus")) {
            Terminal.printError("the game type is not valid.");
            return;
        } else if (!args[1].matches("\\d\\d")) {
            Terminal.printError("the board size is not a positive integer with 2 digits.");
            return;
        } else if (!args[2].matches("\\d")) {
            Terminal.printError("the player count is not a positive single digit integer.");
            return;
        }
        
        int boardSize = Integer.parseInt(args[1]);
        int playerCount = Integer.parseInt(args[2]);
        
        if (boardSize != 18 && boardSize != 20) {
            Terminal.printError("the board size is not in the range 18 or 20.");
            return;
        } else if (playerCount < 2 || playerCount > 4) {
            Terminal.printError("the player count is not in the range 1 < count < 5");
            return;
        }
        
        ConnectSix connectSix = new ConnectSix(playerCount, boardSize, BoardType.valueOf(args[0].toUpperCase()));
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