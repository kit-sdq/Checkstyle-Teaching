package edu.kit.informatik;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Database database = new Database(new ArrayList<>());
        Command command = null;
        do {
            try {
                command = Command.executeMatching(Terminal.readLine(), database);
            } catch (InputException e) {
                Terminal.printError(e.getMessage());
            }
        } while (command == null || command.isRunning());
    }
}
