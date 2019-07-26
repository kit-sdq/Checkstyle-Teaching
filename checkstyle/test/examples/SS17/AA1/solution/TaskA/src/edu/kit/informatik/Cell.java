package edu.kit.informatik;

public class Cell {
    private Coordinates ownCoordinates;
    private Cell referencedCell;
    
    public Cell(Coordinates ownCoordinates) {
        this.ownCoordinates = ownCoordinates;
    }

    public Cell(Coordinates ownCoordinates, Cell referencedCell) {
        this.ownCoordinates = ownCoordinates;
        this.referencedCell = referencedCell;
    }

    public Coordinates getOwnCoordinates() {
        return ownCoordinates;
    }

    public boolean isReferencing() {
        return referencedCell != null;    
    }
    
    public Cell getReferencedCell() {
        return referencedCell;
    }

    public void setReferencedCell(Cell referencedCell) {
        this.referencedCell = referencedCell;
    }
    
    @Override
    public String toString() {
        return "(" + ownCoordinates.getX() + "," + ownCoordinates.getY() + ")";
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

        return ownCoordinates.equals(cell.ownCoordinates);
    }

    @Override
    public int hashCode() {
        return ownCoordinates.hashCode();
    }
}
