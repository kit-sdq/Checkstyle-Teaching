package edu.kit.informatik;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        if (args.length != 2) {
            Terminal.printError("invalid number of parameters");
            return;
        }
        Modes mode;
        Player[] players;
        try {
            mode = Modes.parseString(args[0]);
            players = new Player[2];
            if (Integer.parseInt(args[1]) < 28 || Integer.parseInt(args[1]) > 32) {
                Terminal.printError("incorrect number of tokens");
                return;
            }
            players[0] = new Player(Players.P1, generateTokenList(Integer.parseInt(args[1]),
                    Players.P1));
            players[1] = new Player(Players.P2, generateTokenList(Integer.parseInt(args[1]),
                    Players.P2));

        } catch (InputException e) {
            Terminal.printError("incorrect start parameters " + e.getMessage());
            return;
        } catch (ArrayIndexOutOfBoundsException e) {
            Terminal.printError("not enough start parameters");
            return;
        } catch (Exception e) {
            Terminal.printError("incorrect start parameters");
            return;
        }
        Game game = new Game(new Board(), players, mode);
        Command command = null;
        do {
            try {
                command = Command.executeMatching(Terminal.readLine(), game);
            } catch (InputException e) {
                Terminal.printError(e.getMessage());
            }
        } while (command == null || command.isRunning());
    }

    public static List<Token> generateTokenList(int length, Players player) {
        List<Token> tokens = new ArrayList<>();
        for (int a = 0; a < length; a++) {
            tokens.add(new Token(player));
        }
        return tokens;
    }
}
