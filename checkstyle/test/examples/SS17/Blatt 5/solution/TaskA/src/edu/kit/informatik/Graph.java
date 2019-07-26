package edu.kit.informatik;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Graph {
    private List<Node> nodes;
    private Map<Node, List<Edge>> connections;

    public Graph() {
        this.nodes = new ArrayList<>();
        this.connections = new HashMap<>();
    }

    public String listNodes() {
        Collections.sort(nodes);
        return nodes.stream().map(Node::toString).collect(Collectors.joining(","));
    }

    public String listEdges() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Node, List<Edge>> entry : connections.entrySet()) {
            Stream<Edge> edgeStream = entry.getValue().stream();
            sb.append(edgeStream.map(edge -> entry.getKey() + edge.toString()).collect(Collectors.joining("\n")));
            sb.append("\n");
        }
        return sb.toString().substring(0, sb.length() - 1);
    }
    
    public String toDOT() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph {\n");
        
        for (Map.Entry<Node, List<Edge>> entry : connections.entrySet()) {
            Stream<Edge> edgeStream = entry.getValue().stream();
            sb.append(edgeStream.map(edge -> "  " + entry.getKey().toDOT() + " " + edge.toDOT())
                    .collect(Collectors.joining("\n")));
            sb.append("\n");
        }
        
        sb.append(nodes.stream()
                .filter(node -> node.getClass() == Category.class)
                .map(node -> "  " + node.toDOT() + " [shape=box]")
                .collect(Collectors.joining("\n")));
        
        sb.append("\n}");
        
        return sb.toString();
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public <T extends Node> List<T> getDirectlyReachableNodes(Node node, Class<T> clazz, Relation type) {
        return connections.get(node).stream()
                .filter(edge -> edge.getType() == type && edge.getTarget().getClass() == clazz)
                .map(edge -> clazz.cast(edge.getTarget()))
                .collect(Collectors.toList());
    }

    public Product getProductById(int id) throws GraphException {
        return nodes.stream()
                .filter(node -> node.getClass() == Product.class)
                .map(node -> (Product) node)
                .filter(product -> product.getId() == id)
                .findFirst().orElseThrow(() -> new GraphException("node not found."));
    }

    public void addNode(Node node) throws GraphException {
        Node collision = nodes.stream().filter(n -> n.isAmbigiousWith(node)).findFirst().orElse(null);
        if (collision != null) {
            throw new GraphException("Node " + node.toString()
                    + " is ambigious with the existing node " + collision.toString() + ".");
        }

        if (!nodes.contains(node)) {
            nodes.add(node);
        }
    }

    public void addNodesAndEdge(Relation type, Node source, Node target) throws GraphException {
        addNode(source);
        addNode(target);

        if (type == Relation.CONTAINED_IN
                && source.getClass() == Category.class && target.getClass() == Product.class) {
            throw new GraphException("A category can't be the source node "
                    + "in a contained-in relation with a Product node.");
        } else if (type == Relation.CONTAINS
                && target.getClass() == Category.class && source.getClass() == Product.class) {
            throw new GraphException("A category can't be the target node in a contains relation with a Product node.");
        } else if ((type == Relation.PREDECESSOR_OF || type == Relation.SUCCESSOR_OF)
                && (source.getClass() == Category.class || target.getClass() == Category.class)) {
            throw new GraphException("A category can't be part of a product series relation.");
        } else if (connections.containsKey(source)
                && connections.get(source).stream()
                .anyMatch(e -> e.getTarget().equals(target) && e.getType() != type)) {
            throw new GraphException("Only one connection between a source and a target node is allowed.");
        }

        //Add the relation itself
        addEdge(type, source, target);

        //Add the reverse relation
        addEdge(type.getReverseRelation(), target, source);
    }

    private void addEdge(Relation type, Node source, Node target) {
        if (!connections.containsKey(source)) {
            connections.put(source, new ArrayList<>());
        } else if (connections.get(source).stream().anyMatch(
                e -> e.getTarget().equals(target) && e.getType() == type)) {
            //If the edge already exists, don't add it.
            return;
        }
        connections.get(source).add(new Edge(type, target));
    }
}