package edu.kit.informatik;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * Created by Thomas on 15.08.2018.
 */
public class WinningRow implements Iterable<Point>{
    private TreeSet<Point> points;

    public WinningRow() {
        this.points = new TreeSet<>();
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WinningRow)) return false;

        WinningRow that = (WinningRow) o;

        return points.equals(that.points);
    }

    @Override
    public int hashCode() {
        return points.hashCode();
    }

    @Override
    public Iterator<Point> iterator() {
        return points.iterator();
    }
}
