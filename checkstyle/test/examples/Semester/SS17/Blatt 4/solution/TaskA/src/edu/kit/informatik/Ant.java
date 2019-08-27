package edu.kit.informatik;

public class Ant implements Comparable<Ant> {
    private String name;
    private Orientation orientation;
    private Cell currentCell;

    public Ant(String name, Orientation orientation, Cell startingCell) {
        this.name = name;
        this.orientation = orientation;
        this.currentCell = startingCell;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public Cell getCurrentCell() {
        return currentCell;
    }

    public void setCurrentCell(Cell currentCell) {
        this.currentCell = currentCell;
    }

    @Override
    public int compareTo(Ant o) {
        return this.getName().compareTo(o.getName());
    }

    @Override
    public String toString() {
        return name;
    }

    //Needed for removal from list
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Ant ant = (Ant) o;

        return name.equals(ant.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
