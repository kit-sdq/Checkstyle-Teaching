package edu.kit.informatik;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Leaf extends Node {
    private int points;

    public Leaf(int points) {
        super(true, "");
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public String print() {
        return "(" + points + ")";
    }

    @Override
    public List<Integer> returnPoints() {
        return Collections.singletonList(points);
    }

    @Override
    public int childCount() {
        return 0;
    }

    @Override
    public Map<Integer, Node> delete(String identifier) throws InputException {
        Map<Integer, Node> nodes = new HashMap<>();
        nodes.put(0, this);
        return nodes;
    }

    @Override
    public Leaf getLeaf(String identifier) {
        return this;
    }

    @Override
    public boolean removeOnlyOneNode(Node node) {
        return true;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
