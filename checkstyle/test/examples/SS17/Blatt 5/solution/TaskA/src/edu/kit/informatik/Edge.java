package edu.kit.informatik;

public class Edge {
    private Relation type;
    private Node target;

    public Edge(Relation type, Node target) {
        this.type = type;
        this.target = target;
    }

    public Relation getType() {
        return type;
    }

    public Node getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return "-[" + type + "]->" + target.toString();
    }
    
    public String toDOT() {
        return "-> " + target.toDOT() + " [label=" + type.toDOT() + "]"; 
    }
}
