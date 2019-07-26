package edu.kit.informatik;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class InnerNode extends Node {
    private boolean root;
    private TreeSet<Node> nodes;

    public InnerNode(String identifier, boolean root, TreeSet<Node> nodes) {
        super(root, identifier);
        this.root = root;
        this.nodes = nodes;
    }

    public boolean isRoot() {
        return root;
    }

    public void add(String name, int points) throws InputException {
        if (name.isEmpty()) {  // Create a leaf
            if (!nodes.isEmpty()) {
                throw new InputException("student already has points");
            }
            nodes.add(new Leaf(points));
        } else {    // create an inner node or use existing one
            InnerNode node;
            if (nodes.contains(new InnerNode(String.valueOf(name.charAt(0)), false, new TreeSet<>()))) {
                // get the node from within the list
                node = findNode(name);
            } else {
                node = new InnerNode(String.valueOf(name.charAt(0)), false, new TreeSet<>());
                nodes.add(node);
            }
            node.add(name.substring(1), points);
        }
    }

    private InnerNode findNode(String identifier) throws InputException {
        for (Node node : this.nodes) {
            if (node.getIdentifier().equals(String.valueOf(identifier.charAt(0)))) {
                return (InnerNode) node;
            }

        }
        throw new InputException("invalid identifier " + identifier.charAt(0) + " " + this.getIdentifier());
    }

    public String print() {
        String id = isRoot() ? "#" : String.valueOf(getIdentifier());
        StringBuilder childs = new StringBuilder();

        for (Node node : nodes) {
            if (node.getClass() == Leaf.class) childs.append(node.print());
            else childs.append(node.print());
        }

        if (!childs.toString().equals("") && !childs.toString().startsWith("(")) {
            childs = new StringBuilder("[" + childs + "]");
        }

        return id + childs;
    }

    @Override
    public List<Integer> returnPoints() {
        List<Integer> points = new ArrayList<>();
        for (Node node : nodes) {
            points.addAll(node.returnPoints());
        }
        return points;
    }

    @Override
    public int childCount() {
        return nodes.size();
    }

    @Override
    public Map<Integer, Node> delete(String identifier) throws InputException {

        Map<Integer, Node> childNodes = new HashMap<>();
        if (nodes.size() == 1 && nodes.first().getClass() == Leaf.class) {
            childNodes.putAll(nodes.first().delete(""));
            childNodes.put(childNodes.size(), this);
            return childNodes;
        } else if (findNode(identifier) != null) {
            childNodes.putAll(findNode(identifier).delete(identifier.substring(1)));
            childNodes.put(childNodes.size(), this);
            return childNodes;
        }
        else {
            throw new InputException("invalid user name");
        }
    }


    @Override
    public Leaf getLeaf(String identifier) throws InputException {
        if (isRoot()) {
            return findNode(identifier).getLeaf(identifier.substring(1));
        } else if (nodes.size() == 1 && nodes.first().getClass() == Leaf.class) {
            return nodes.first().getLeaf("");
        } else if (findNode(identifier) != null) {
            return findNode(identifier).getLeaf(identifier.substring(1));
        }
        else {
            throw new InputException("invalid user name");
        }
    }

    @Override
    public boolean removeOnlyOneNode(Node node) {
        if (nodes.size() == 1 && !isRoot()) {
            nodes.clear();
            return true;
        } else if (isRoot()) {
            nodes.remove(node);
            return false;
        } else {
            nodes.remove(node);
            return false;
        }
    }
}
