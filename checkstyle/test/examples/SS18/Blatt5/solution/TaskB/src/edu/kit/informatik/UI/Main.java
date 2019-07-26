package edu.kit.informatik.UI;

import edu.kit.informatik.exceptions.IllegalMoveException;
import edu.kit.informatik.exceptions.InputException;

import edu.kit.informatik.Pawn;
import edu.kit.informatik.Player;
import edu.kit.informatik.SantoriniGame;
import edu.kit.informatik.Terminal;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is the entry point class for the program containing the main method.
 *
 * @author Peter Oettig
 * @version 1.0
 */
public class Main {
    /**
     * The pattern that identifies a string according to the game rules.
     */
    // Visible in package for Main and Command
    static final String STRING_PATTERN = "[^;\\n]+";

    private static final int NUMBER_OF_ARGUMENTS = 1;
    private static final int NUMBER_OF_PAWNS = 4;

    /**
     * The main method of the program.
     *
     * @param args The arguments that are passed to the program at launch as array.
     */
    public static void main(String[] args) {
        if (args.length < NUMBER_OF_ARGUMENTS) {
            Terminal.printError("not enough arguments, there needs to be a filepath.");
            return;
        } else if (args.length > NUMBER_OF_ARGUMENTS) {
            Terminal.printError("too many arguments, there needs to be a filepath.");
            return;
        }

        int boardSize;
        Matcher argMatcher = Pattern.compile("([5-8]);(.*;.*;.*);(.*;.*;.*);(.*;.*;.*);(.*;.*;.*)").matcher(args[0]);
        if (!argMatcher.matches()) {
            Terminal.printError("there are not exactly four pawn describing blocks and a board size.");
            return;
        }

        Queue<Player> players = new LinkedList<>();
        players.add(new Player("P1"));
        players.add(new Player("P2"));

        boardSize = Integer.parseInt(argMatcher.group(1));
        List<PawnInformation> infoAboutPawns = new LinkedList<>();
        for (int i = 1; i <= NUMBER_OF_PAWNS; i++) {
            PawnInformation pawnInfo = parsePawn(argMatcher.group(i + 1), boardSize - 1);
            if (pawnInfo == null) {
                Terminal.printError("not a valid pawn describing block.");
                return;
            }

            infoAboutPawns.add(pawnInfo);
        }

        int counter = 0;
        Player owner = null;
        List<Pawn> pawns = new LinkedList<>();
        for (PawnInformation pawnInfo : infoAboutPawns) {
            if (counter % 2 == 0) {
                owner = players.remove();
                players.add(owner);
            }
            Pawn pawn = new Pawn(pawnInfo.pawnName);
            pawns.add(pawn);

            // Check if owner already has a pawn with that name.
            if (owner.hasPawn(pawn)) {
                Terminal.printError("there is already a pawn with the name " + pawn.getName() + " for this player.");
                return;
            }

            owner.addPawn(pawn);
            counter++;
        }

        SantoriniGame game = new SantoriniGame(players, pawns, boardSize);
        for (int i = 0; i < infoAboutPawns.size(); i++) {
            PawnInformation pawnInfo = infoAboutPawns.get(i);
            try {
                game.placePawn(pawns.get(i), pawnInfo.startingRow, pawnInfo.startingColumn);
            } catch (IllegalMoveException e) {
                Terminal.printError(e.getMessage());
                return;
            }
        }

        Command command = null;
        do {
            try {
                command = Command.executeMatching(Terminal.readLine(), game);
            } catch (InputException | IllegalMoveException e) {
                Terminal.printError(e.getMessage());
            }
        } while (command == null || command.isRunning());
    }

    private static PawnInformation parsePawn(String pawnString, int maxRowColIndex) {
        String rowColIndex = "[0-" + maxRowColIndex + "]";
        String pattern = "(" + STRING_PATTERN + ");(" + rowColIndex + ");(" + rowColIndex + ")";
        Matcher pawnMatcher = Pattern.compile(pattern).matcher(pawnString);

        if (!pawnMatcher.matches()) {
            return null;
        }

        String pawnName = pawnMatcher.group(1);
        int row = Integer.parseInt(pawnMatcher.group(2));
        int column = Integer.parseInt(pawnMatcher.group(3));

        return new PawnInformation(pawnName, row, column);
    }

    /**
     * Pawn information class meant for parsing, only visible inside the class.
     */
    private static final class PawnInformation {
        private final String pawnName;
        private final int startingRow;
        private final int startingColumn;

        private PawnInformation(String pawnName, int startingRow, int startingColumn) {
            this.pawnName = pawnName;
            this.startingRow = startingRow;
            this.startingColumn = startingColumn;
        }
    }
}
