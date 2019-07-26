package edu.kit.informatik;

/**
 * Created by Thomas on 15.08.2018.
 */
public class Point implements Comparable<Point>{
    private int row;
    private int column;

    public Point(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;

        Point point = (Point) o;

        if (row != point.row) return false;
        return column == point.column;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + column;
        return result;
    }

    @Override
    public int compareTo(Point o) {
        if (row != o.row) return row-o.row;
        return column - o.column;
    }
}
