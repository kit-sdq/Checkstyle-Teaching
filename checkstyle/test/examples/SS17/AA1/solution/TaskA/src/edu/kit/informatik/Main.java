package edu.kit.informatik;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final String BOARD_TYPE_PATTERN = "standard|torus";
    private static final String BOARD_SIZE_PATTERN = "([4-9]|[1-9]\\d+)";
    private static final String PLAYER_COUNT_PATTERN = "([2-9]|[1-9]\\d+)";
    private static final String LINE_PATTERN = "\\((\\d+),(\\d+)\\):\\((\\d+),(\\d+)\\)";

    public static void main(String[] args) {
        BoardGame game = null;
        List<Cell> cellsWithCoords = null;
        try {
            ParseArgsResult parsedArgs = checkAndParseArgs(args);
            game = new BoardGame(parsedArgs.boardType, parsedArgs.boardSize, parsedArgs.playerCount);
            cellsWithCoords = parseFile(Terminal.readFile(args[3]), parsedArgs.boardSize);
            checkPlayerCount(parsedArgs.playerCount, parsedArgs.boardSize, cellsWithCoords.size());
        } catch (GameCreationException e) {
            Terminal.printError(e.getMessage());
            return;
        }
        
        for (Cell cell : cellsWithCoords) {
            game.setCell(cell);
        }

        Command command = null;
        do {
            try {
                command = Command.executeMatching(Terminal.readLine(), game);
            } catch (GameException e) {
                Terminal.printError(e.getMessage());
            }
        } while (command == null || command.isRunning());
    }

    private static ParseArgsResult checkAndParseArgs(String[] args) throws GameCreationException {
        if (args.length != 4) {
            throw new GameCreationException("unexpected number of parameters.");
        } 
        
        BoardType boardType = null;
        if (!args[0].matches(BOARD_TYPE_PATTERN)) {
            throw new GameCreationException("the second parameter needs to be either standard or torus.");
        } else {
            boardType = BoardType.valueOf(args[0].toUpperCase());
        }
        
        int boardSize = 0;
        if (!args[1].matches(BOARD_SIZE_PATTERN)) {
            throw new GameCreationException("the size of the board needs to be a natural number greater than 3.");
        } else {
            boardSize = Integer.parseInt(args[1]);
        }
        
        if (!args[2].matches(PLAYER_COUNT_PATTERN)) {
            throw new GameCreationException("the number of players needs to be a natural number.");
        } 
        
        int playerCount = Integer.parseInt(args[2]);
        
        return new ParseArgsResult(boardType, boardSize, playerCount);
    }

    private static List<Cell> parseFile(String[] lines, int boardSize) throws GameCreationException {
        List<Cell> cellsWithCoords = new ArrayList<>();
        Pattern linePattern = Pattern.compile(LINE_PATTERN);
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].trim().isEmpty()) {
                throw new GameCreationException(getLineError(i, "line can't be empty."));
            }
            
            Matcher lineMatcher = linePattern.matcher(lines[i]);
            boolean match = lineMatcher.matches();
            if (!match) {
                throw new GameCreationException(getLineError(i, "the line doesn't match the serialization syntax"));
            }
            
            Coordinates source = new Coordinates(Integer.parseInt(lineMatcher.group(1)), Integer.parseInt(lineMatcher.group(2)));
            if (!checkCoordinates(source, boardSize)) {
                throw new GameCreationException(getLineError(i, "the source coordinates are invalid"));
            }
            
            Coordinates target = new Coordinates(Integer.parseInt(lineMatcher.group(3)), Integer.parseInt(lineMatcher.group(4)));
            if (!checkCoordinates(target, boardSize)) {
                throw new GameCreationException(getLineError(i, "the target coordinates are invalid"));
            }
            
            Coordinates firstCellCoords = new Coordinates(0, 0);
            if (firstCellCoords.equals(source)) {
                throw new GameCreationException(getLineError(i, "the first cell can't reference another cell."));
            } else if (firstCellCoords.equals(target)) {
                throw new GameCreationException(getLineError(i, "the first cell can't be referenced to."));
            }
            
            Coordinates lastCellCoords = new Coordinates(boardSize - 1, boardSize - 1);
            if (lastCellCoords.equals(source)) {
                throw new GameCreationException(getLineError(i, "the last cell can't reference another cell."));
            } else if (lastCellCoords.equals(target)) {
                throw new GameCreationException(getLineError(i, "the last cell can't be referenced to."));
            }
            
            Cell cell = new Cell(source, new Cell(target));
            if (cellsWithCoords.contains(cell)) {
                throw new GameCreationException(getLineError(i, "the source cell already references another cell."));
            }
            
            for (Cell current : cellsWithCoords) {
                if (current.getReferencedCell().equals(cell)) {
                    throw new GameCreationException(getLineError(i, "the source cell is already a reference target."));
                }
            }
            
            cellsWithCoords.add(cell);
        }
        
        return cellsWithCoords;
    }

    private static boolean checkCoordinates(Coordinates coordinates, int boardSize) {
        return coordinates.getX() >= 0 && coordinates.getX() < boardSize && coordinates.getY() >= 0 && coordinates.getY() < boardSize;
    }

    private static void checkPlayerCount(int playerCount, int boardSize, int cellsWithCoordsCount) throws GameCreationException {
        if (playerCount <= 1 || Math.min(26, boardSize * boardSize - cellsWithCoordsCount) <= playerCount) {
            throw new GameCreationException("the player count doesn't match the contraints.");
        }
    }
    
    private static String getLineError(int lineNumber, String message) {
        return "line " + (lineNumber + 1) + ": " + message;
    }

    private static class ParseArgsResult {
        private BoardType boardType;
        private int boardSize;
        private int playerCount;
        
        private ParseArgsResult(BoardType boardType, int boardSize, int playerCount) {
            this.boardType = boardType;
            this.boardSize = boardSize;
            this.playerCount = playerCount;
        }
    }
    
    private static final class ParseFileResult {
        private Cell[][] board;
        private List<Object> ants;

        private ParseFileResult(Cell[][] board, List<Object> ants) {
            this.board = board;
            this.ants = ants;
        }
    }
}