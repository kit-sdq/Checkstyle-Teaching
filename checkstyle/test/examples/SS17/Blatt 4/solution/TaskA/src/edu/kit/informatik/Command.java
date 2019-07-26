package edu.kit.informatik;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Command {
    MOVE("move (\\d+)") {
        @Override
        public void execute(MatchResult matcher, AntGame game) throws GameException {
            int moveCount = Integer.parseInt(matcher.group(1));

            for (int i = 0; i < moveCount; i++) {
                game.doTurn();

                if (!game.antsLeft()) {
                    isRunning = false;
                }
            }
        }
    },

    PRINT("print") {
        @Override
        public void execute(MatchResult matcher, AntGame game) throws GameException {
            Terminal.printLine(game.getBoardStringRepresentation());
        }
    },

    POSITION("position ([a-zA-Z])") {
        @Override
        public void execute(MatchResult matcher, AntGame game) throws GameException {
            Ant ant = game.getAnt(matcher.group(1).toLowerCase());
            Terminal.printLine(ant.getCurrentCell().getCoordinates());
        }
    },

    FIELD("field (\\d+),(\\d+)") {
        @Override
        public void execute(MatchResult matcher, AntGame game) throws GameException {
            int row = Integer.parseInt(matcher.group(1));
            int column = Integer.parseInt(matcher.group(2));

            Cell cell = game.getCell(row, column);
            if (cell == null) {
                throw new GameException("coordinates not inside the board.");
            }

            Ant ant = game.checkForAnt(cell);
            if (ant != null) {
                Terminal.printLine(ant);
            } else {
                Terminal.printLine(cell.getColor());
            }
        }
    },

    DIRECTION("direction ([a-zA-Z])") {
        @Override
        public void execute(MatchResult matcher, AntGame game) throws GameException {
            Ant ant = game.getAnt(matcher.group(1).toLowerCase());
            Terminal.printLine(ant.getOrientation());
        }
    },

    ANT("ant") {
        @Override
        public void execute(MatchResult matcher, AntGame game) throws GameException {
            Terminal.printLine(game.getAntsStringRepresentation());
        }
    },

    CREATE("create ([a-zA-Z]),(\\d+),(\\d+)") {
        @Override
        public void execute(MatchResult matcher, AntGame game) throws GameException {
            char antName = matcher.group(1).charAt(0);
            int row = Integer.parseInt(matcher.group(2));
            int column = Integer.parseInt(matcher.group(3));

            Cell cell = game.getCell(row, column);
            if (cell == null) {
                throw new GameException("coordinates not inside the board.");
            }

            Orientation orientation = Character.isUpperCase(antName) ? Orientation.N : Orientation.S;
            Ant ant = new Ant(matcher.group(1).toLowerCase(), orientation, cell);

            game.addAnt(ant);
        }
    },

    ESCAPE("escape ([a-zA-Z])") {
        @Override
        public void execute(MatchResult matcher, AntGame game) throws GameException {
            game.removeAnt(matcher.group(1).toLowerCase());

            if (!game.antsLeft()) {
                isRunning = false;
            }
        }
    },

    QUIT("quit") {
        @Override
        public void execute(MatchResult matcher, AntGame game) throws GameException {
            isRunning = false;
        }
    };


    private static boolean isRunning = true;
    private Pattern pattern;

    Command(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    public static Command executeMatching(String input, AntGame game) throws GameException {
        for (Command command : Command.values()) {
            Matcher matcher = command.pattern.matcher(input);
            if (matcher.matches()) {
                command.execute(matcher, game);
                return command;
            }
        }

        throw new GameException("not a valid command!");
    }

    public abstract void execute(MatchResult matcher, AntGame game) throws GameException;

    public boolean isRunning() {
        return isRunning;
    }
}
