package edu.kit.informatik;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SucPredStrategy implements Strategy {
    private Relation type;
    
    public SucPredStrategy(Relation type) {
        this.type = type;
    }
    
    @Override
    public List<Product> execute(Graph graph, Product start) {
        //Used Breadth-first search to find all reachable nodes.
        Map<Product, Boolean> visited = new HashMap<>();
        Deque<Product> queue = new ArrayDeque<>();
        
        for (Node node : graph.getNodes()) {
            if (node.getClass() == Product.class) {
                visited.put((Product) node, false);
            }
        }
        queue.push(start);
        visited.put(start, true);
        while (!queue.isEmpty()) {
            Node current = queue.pop();
            List<Product> reachableProducts = graph.getDirectlyReachableNodes(current, Product.class, type);
            for (Product product : reachableProducts) {
                if (!visited.get(product)) {
                    queue.push(product);
                    visited.put(product, true);
                }
            }
        }
        
        visited.put(start, false);
        
        //Return all visited nodes: These nodes are direct and indirect successors/predecessors
        return visited.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
