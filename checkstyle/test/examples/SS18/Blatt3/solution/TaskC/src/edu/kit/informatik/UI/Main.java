package edu.kit.informatik.UI;

import edu.kit.informatik.Color;
import edu.kit.informatik.ColorConversionException;
import edu.kit.informatik.Mastermind;
import edu.kit.informatik.Terminal;

import java.util.List;

public class Main {
    private Main() {
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            Terminal.printError("the filepath argument is missing.");
            return;
        } else if (args.length != 1) {
            Terminal.printError("only the filepath argument is expected.");
            return;
        }
        
        List<Color> expectedSequence = parseFile(Terminal.readFile(args[0]));
        if (expectedSequence == null) {
            return;
        } else if (expectedSequence.size() != Mastermind.SEQUENCE_LENGTH) {
            Terminal.printError("wrong number of colors.");
            return;
        }
        
        Mastermind mastermind = new Mastermind(expectedSequence);
        Terminal.printLine(mastermind.getPrompt());

        Command command = null;
        do {
            try {
                command = Command.executeMatching(Terminal.readLine(), mastermind);
            } catch (InputException e) {
                Terminal.printError(e.getMessage());
                Terminal.printLine(mastermind.getPrompt());
            }
        } while (command == null || command.isRunning());
    }

    private static List<Color> parseFile(String[] lines) {
        if (lines.length == 0) {
            Terminal.printError("file can't be empty");
            return null;
        } else if (lines.length > 1) {
            Terminal.printError("file must have only one line.");
            return null;
        } else if (lines[0].equals("")) {
            Terminal.printError("the line can't be empty.");
            return null;
        }

        try {
            return Color.convertStringsToColors(lines[0].split(","));
        } catch (ColorConversionException exception) {
            Terminal.printError(exception.getMessage());
            return null;
        }
    }
}
