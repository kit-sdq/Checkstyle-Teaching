package edu.kit.informatik;

public class Player implements Comparable<Player> {
    private int number;
    private Cell currentCell;

    public Player(int number, Cell startingCell) {
        this.number = number;
        this.currentCell = startingCell;
    }

    public int getNumber() {
        return number;
    }

    public Cell getCurrentCell() {
        return currentCell;
    }

    public void setCurrentCell(Cell currentCell) {
        this.currentCell = currentCell;
    }

    @Override
    public String toString() {
        return number + ":" + currentCell.toString();
    }

    @Override
    public int compareTo(Player other) {
        return Integer.compare(number, other.number);
    }
}
