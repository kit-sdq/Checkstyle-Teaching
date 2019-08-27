package edu.kit.informatik;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class Trie {
    private InnerNode root;

    public Trie(String identifier) {
        this.root = new InnerNode(identifier, true, new TreeSet<>());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trie trie = (Trie) o;

        return root.equals(trie.root);
    }

    @Override
    public int hashCode() {
        return root.hashCode();
    }

    public void add(String name, int points) throws InputException {
        root.add(name, points);
    }

    public void modify(String name, int points) throws InputException {
        root.getLeaf(name).setPoints(points);
    }

    public void delete(String name) throws InputException {
        Map<Integer, Node> nodes = root.delete(name);
        int count = 0;
        Node node = nodes.get(count);
        while (nodes.get(++count).removeOnlyOneNode(node)) {
            node = nodes.get(count);
        }
    }

    public int credits(String name) throws InputException {
        return root.getLeaf(name).getPoints();
    }

    public String print() {
        return root.print();
    }

    public int average() throws InputException {
        List<Integer> points = getPoints();
        if (!(points.size()>0)) throw new InputException("Trie is empty");
        return points.stream().mapToInt(Integer::intValue).sum() / points.size();
    }

    public int median() throws InputException {
        List<Integer> points = getPoints();
        if (!(points.size()>0)) throw new InputException("Trie is empty");
        Collections.sort(points);
        if (points.size() % 2 == 0) {
            return (int) ((points.get(points.size() / 2 - 1) + points.get(points.size() / 2)) * 0.5);
        } else {
            return points.get((points.size()) / 2);
        }
    }

    private List<Integer> getPoints() {
        return root.returnPoints();
    }

    public void clear() {
        root = new InnerNode(root.getIdentifier(), true, new TreeSet<>());
    }
}
