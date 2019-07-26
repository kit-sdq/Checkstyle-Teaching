package edu.kit.informatik;

public class Cell {
    private int color;
    private Coordinates coordinates;

    public Cell(int color, int row, int column) {
        this.color = color;
        this.coordinates = new Coordinates(row, column);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Cell cell = (Cell) o;

        return coordinates.equals(cell.coordinates);
    }

    @Override
    public int hashCode() {
        return coordinates.hashCode();
    }
}
