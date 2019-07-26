package edu.kit.informatik;

public class Main {
    public static void main(String[] args) {
        Command command = null;
        Tracker tracker = new Tracker();
        do {
            try {
                command = Command.executeMatching(Terminal.readLine(), tracker);
            } catch (InteractionException e) {
                Terminal.printError(e.getMessage());
            }
        } while (command == null || command.isRunning());
    }
}