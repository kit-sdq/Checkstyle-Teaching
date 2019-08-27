package edu.kit.informatik;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Command {
    MOVE("move ([1-9]\\d*)") {
        @Override
        public void execute(MatchResult matcher, BoardGame game) throws GameException {
            if (!game.isRunning()) {
                Terminal.printError("the game is over.");
                return;
            }
            
            int winner = game.move(Integer.parseInt(matcher.group(1)));
            if (winner == 0) {
                Terminal.printLine("OK");
            } else {
                Terminal.printLine(winner + " wins");
            }
        }
    },
    
    PLAYERS("players") {
        @Override
        public void execute(MatchResult matcher, BoardGame game) throws GameException {
            List<Player> players = new ArrayList<>(game.getPlayers());
            Collections.sort(players);
            for (Player player : players) {
                Terminal.printLine(player.toString());   
            }
        }
    },
    
    ROWPRINT("rowprint (\\d+)") {
        @Override
        public void execute(MatchResult matcher, BoardGame game) throws GameException {
            int row = Integer.parseInt(matcher.group(1));
            if (row < 0 || row >= game.getBoardSize()) {
                throw new GameException("row number not valid");
            }
            
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < game.getBoardSize(); i++) {
                Cell cell = game.getCellByCarthesian(i, row);
                sb.append(stringifyCell(game, cell));
                sb.append(" ");
            }

            sb.setLength(sb.length() - 1);
            Terminal.printLine(sb.toString());
        }
    },
    
    COLPRINT("colprint (\\d+)") {
        @Override
        public void execute(MatchResult matcher, BoardGame game) throws GameException {
            int column = Integer.parseInt(matcher.group(1));
            if (column < 0 || column >= game.getBoardSize()) {
                throw new GameException("column number not valid");
            }
            
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < game.getBoardSize(); i++) {
                Cell cell = game.getCellByCarthesian(column, i);
                sb.append(stringifyCell(game, cell));
                sb.append(" ");
            }

            sb.setLength(sb.length() - 1);
            Terminal.printLine(sb.toString());
        }
    },    
    
    QUIT("quit") {
        @Override
        public void execute(MatchResult matcher, BoardGame game) throws GameException {
            isRunning = false;
        }
    };

    private static boolean isRunning = true;
    private Pattern pattern;

    Command(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    public static Command executeMatching(String input, BoardGame game) throws GameException {
        for (Command command : Command.values()) {
            Matcher matcher = command.pattern.matcher(input);
            if (matcher.matches()) {
                command.execute(matcher, game);
                return command;
            }
        }

        throw new GameException("not a valid command!");
    }

    public abstract void execute(MatchResult matcher, BoardGame game) throws GameException;

    public boolean isRunning() {
        return isRunning;
    }

    private static String stringifyCell(BoardGame game, Cell cell) {
        StringBuilder sb = new StringBuilder();
        if (game.isFirstCell(cell)) {
            sb.append("{");
            for (Player player : game.getPlayers()) {
                if (game.isFirstCell(player.getCurrentCell())) {
                    sb.append(player.getNumber());
                    sb.append(",");
                }
            }
            if (sb.substring(sb.length() - 1).equals(",")) {
                sb.setLength(sb.length() - 1);
            }
            sb.append("}");
        } else if (cell.getReferencedCell() != null) {
            sb.append(cell.getReferencedCell().toString());
        } else {
            Player occupier = game.getOccupier(cell);
                    
            if (occupier != null) {
                sb.append(occupier.getNumber());
            } else {
                sb.append("#");
            }
        }
        
        return sb.toString();
    }
}
