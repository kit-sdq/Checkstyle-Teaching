package edu.kit.informatik;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AntGame {
    private Cell[][] board;
    private List<Ant> ants;
    private int[] rotationRules;

    private Orientation[] orientationCache;

    public AntGame(Cell[][] board, List<Ant> ants, int[] rotationRules) {
        this.board = board;
        this.ants = ants;
        this.rotationRules = rotationRules;
        this.orientationCache = Orientation.values();
    }

    public boolean antsLeft() {
        return !ants.isEmpty();
    }

    public void doTurn() {
        //Avoid ConcurrentModificationException by using a temporary copy of the ant list
        List<Ant> tempAnts = new ArrayList<>(ants);

        for (Ant ant : tempAnts) {
            move(ant);
            //Don't rotate and recolor when the ant has left the field.
            if (ants.contains(ant)) {
                rotate(ant);
                recolor(ant.getCurrentCell());
            }
        }
    }

    public Ant getAnt(String name) throws GameException {
        for (Ant ant : ants) {
            if (ant.getName().equals(name)) {
                return ant;
            }
        }

        throw new GameException("ant not found.");
    }

    public void addAnt(Ant ant) throws GameException {
        if (checkForAnt(ant.getCurrentCell()) != null) {
            throw new GameException("cell already occupied.");
        } else if (antExists(ant)) {
            throw new GameException("ant already exists.");
        } else {
            ants.add(ant);
        }
    }

    public void removeAnt(String antName) throws GameException {
        //Check list with dummy ant, because ant equality cares only for name.
        Ant ant = new Ant(antName, null, null);
        if (ants.contains(ant)) {
            ants.remove(ant);
        } else {
            throw new GameException("ant doesn't exist.");
        }
    }

    public Cell getCell(Coordinates coordinates) {
        if (checkCoordinates(coordinates)) {
            return board[coordinates.getRow()][coordinates.getColumn()];
        } else {
            return null;
        }
    }

    public Cell getCell(int row, int column) {
        return getCell(new Coordinates(row, column));
    }

    public Ant checkForAnt(Cell cell) {
        for (Ant ant : ants) {
            if (ant.getCurrentCell().equals(cell)) {
                return ant;
            }
        }

        return null;
    }

    public boolean antExists(Ant ant) {
        return ants.contains(ant);
    }

    public String getBoardStringRepresentation() {
        StringBuilder sb = new StringBuilder();

        for (Cell[] row : board) {
            for (Cell cell : row) {
                Ant ant = checkForAnt(cell);

                if (ant != null) {
                    sb.append(ant.getName());
                } else {
                    sb.append(cell.getColor());
                }
            }
            sb.append("\n");
        }

        String result = sb.toString();
        return result.substring(0, result.length() - 1);
    }

    public String getAntsStringRepresentation() {
        StringBuilder sb = new StringBuilder();

        Collections.sort(ants);
        for (Ant ant : ants) {
            sb.append(ant.getName());
            sb.append(",");
        }

        String result = sb.toString();
        return result.substring(0, result.length() - 1);
    }

    private void rotate(Ant ant) {
        Cell cell = ant.getCurrentCell();
        int orientation = ant.getOrientation().ordinal();

        int stepCount = rotationRules[cell.getColor()] / 45;
        int newOrientationOrdinal = (orientation + stepCount) % orientationCache.length;

        ant.setOrientation(orientationCache[newOrientationOrdinal]);
    }

    private void recolor(Cell cell) {
        int newColor = (4 * cell.getColor() + 23) % 5;
        cell.setColor(newColor);
    }

    private void move(Ant ant) {
        Orientation orientation = ant.getOrientation();
        Coordinates coordinates = ant.getCurrentCell().getCoordinates();
        int row = coordinates.getRow();
        int column = coordinates.getColumn();

        Coordinates newCoordinates = null;
        switch (orientation) {
            case N:
                newCoordinates = new Coordinates(row - 1, column);
                break;
            case NE:
                newCoordinates = new Coordinates(row - 1, column + 1);
                break;
            case E:
                newCoordinates = new Coordinates(row, column + 1);
                break;
            case SE:
                newCoordinates = new Coordinates(row + 1, column + 1);
                break;
            case S:
                newCoordinates = new Coordinates(row + 1, column);
                break;
            case SW:
                newCoordinates = new Coordinates(row + 1, column - 1);
                break;
            case W:
                newCoordinates = new Coordinates(row, column - 1);
                break;
            case NW:
                newCoordinates = new Coordinates(row - 1, column - 1);
                break;
            default:
                //TODO ERROR
        }

        Cell newCell = getCell(newCoordinates);
        if (newCell != null) {
            ant.setCurrentCell(newCell);
        } else {
            ants.remove(ant);
        }
    }

    private boolean checkCoordinates(Coordinates coordinates) {
        boolean checkRow = coordinates.getRow() >= 0 && coordinates.getRow() < board.length;
        boolean checkColumn = coordinates.getColumn() >= 0 && coordinates.getColumn() < board[0].length;

        return checkRow && checkColumn;
    }
}
