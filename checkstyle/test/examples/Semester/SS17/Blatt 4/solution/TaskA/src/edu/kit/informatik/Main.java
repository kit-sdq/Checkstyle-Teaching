package edu.kit.informatik;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String POSSIBLE_RULE_NUMBERS = "(45|90|270|315)";
    private static final String RULES_PATTERN = "(" + POSSIBLE_RULE_NUMBERS + "-){4}" + POSSIBLE_RULE_NUMBERS;
    private static final String ANT_REPRESENTATION = "[a-zA-Z]";
    private static final String COLOR_REPRESENTATION = "[0-4]";
    private static final String CELL_PATTERN = "(" + ANT_REPRESENTATION + "|" + COLOR_REPRESENTATION + ")";
    private static final String LINE_PATTERN = CELL_PATTERN + "+";

    private static final int[] DEFAULT_RULE = new int[] {270, 90, 315, 45, 90};

    public static void main(String[] args) {
        //Three seperate ifs may seem excessive, but I like the tidier look and readability.
        if (!checkArgs(args)) {
            return;
        }

        ParseFileResult result = parseFile(Terminal.readFile(args[0]));
        if (result == null) {
            return;
        }

        int[] rules;
        if (args.length == 1) {
            rules = DEFAULT_RULE;
        } else {
            rules = parseRules(args[1]);
        }
        if (rules == null) {
            return;
        }

        Command command = null;
        do {
            try {
                command = Command.executeMatching(Terminal.readLine(), new AntGame(result.board, result.ants, rules));
            } catch (GameException e) {
                Terminal.printError(e.getMessage());
            }
        } while (command == null || command.isRunning());
    }

    private static boolean checkArgs(String[] args) {
        if (args.length > 2 || args.length <= 0) {
            Terminal.printError("unexpected number of parameters.");
        } else if (args.length == 2 && !args[1].startsWith("rule=")) {
            Terminal.printError("expected a rule as second parameter or no at all.");
        } else {
            return true;
        }

        return false;
    }

    private static ParseFileResult parseFile(String[] lines) {
        if (lines.length == 0) {
            Terminal.printError("file can't be empty");
            return null;
        }

        int numberOfColumns = lines[0].length();
        Cell[][] board = new Cell[lines.length][numberOfColumns];
        List<Ant> ants = new ArrayList<>();
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].trim().isEmpty()) {
                printLineError(i, "line can't be empty.");
                return null;
            } else if (numberOfColumns != lines[i].length()) {
                printLineError(i, "number of columns must be the same for all lines.");
                return null;
            } else if (!lines[i].matches(LINE_PATTERN)) {
                printLineError(i, "syntax error. Every character needs to be either a digit"
                        + " between 0 and 4 or a letter.");
                return null;
            }

            char[] chars = lines[i].toCharArray();
            for (int j = 0; j < chars.length; j++) {
                if (Character.isDigit(chars[j])) {
                    board[i][j] = new Cell(Character.getNumericValue(chars[j]), i, j);
                } else {
                    board[i][j] = new Cell(0, i, j);

                    Orientation orientation = Character.isUpperCase(chars[j]) ? Orientation.N : Orientation.S;
                    Ant ant = new Ant(String.valueOf(chars[j]).toLowerCase(), orientation, board[i][j]);

                    if (ants.contains(ant)) {
                        printLineError(i, "ant " + chars[j] + " already exists.");
                        return null;
                    } else {
                        ants.add(ant);
                    }
                }
            }
        }

        return new ParseFileResult(board, ants);
    }

    private static void printLineError(int lineNumber, String message) {
        Terminal.printError("line " + (lineNumber + 1) + ": " + message);
    }

    private static int[] parseRules(String rule) {
        String rulesString = rule.replace("rule=", "");
        if (!rulesString.matches(RULES_PATTERN)) {
            Terminal.printError("rule is in a wrong format.");
            return null;
        }

        String[] values = rulesString.split("-");
        int[] rules = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            rules[i] = Integer.parseInt(values[i]);
        }

        return rules;
    }

    private static final class ParseFileResult {
        private Cell[][] board;
        private List<Ant> ants;

        private ParseFileResult(Cell[][] board, List<Ant> ants) {
            this.board = board;
            this.ants = ants;
        }
    }
}
