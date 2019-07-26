package edu.kit.informatik;

import java.util.List;
import java.util.Map;

public abstract class Node implements Comparable {
    private boolean leaf;
    private String identifier;

    protected Node(boolean leaf, String identifier) {
        this.leaf = leaf;
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public boolean isLeaf() {
        return leaf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;

        Node node = (Node) o;

        return identifier.equals(node.identifier);
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }

    public abstract String print();

    public abstract List<Integer> returnPoints();

    public abstract int childCount();

    public abstract Map<Integer, Node> delete(String identifier) throws InputException;

    public abstract Leaf getLeaf(String identifier) throws InputException;

    public abstract boolean removeOnlyOneNode(Node node);

    @Override
    public int compareTo(Object o) {
        if (this == o) return 0;
        if (!(o instanceof Node)) return 0;

        Node node = (Node) o;

        return identifier.compareTo(node.identifier);
    }
}
