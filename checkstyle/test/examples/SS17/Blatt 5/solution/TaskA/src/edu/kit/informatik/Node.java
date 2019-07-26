package edu.kit.informatik;

public abstract class Node implements Comparable<Node> {
    private String name;
    
    public Node(String name) {
        this.name = name.toLowerCase();
    }
    
    public String getName() {
        return name;
    }

    public boolean isAmbigiousWith(Node o) {
        if (this == o || o == null) {
            return false;
        }

        if (getClass() != o.getClass()) {
            return name.equals(o.name);
        }

        return false;
    }
    
    @Override
    public int compareTo(Node other) {
        return name.compareTo(other.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Node node = (Node) o;

        return name.equals(node.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return this.name;
    }
    
    public String toDOT() {
        return this.name;
    }
}
