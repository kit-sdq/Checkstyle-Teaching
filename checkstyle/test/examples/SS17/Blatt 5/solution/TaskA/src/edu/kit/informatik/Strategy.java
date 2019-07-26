package edu.kit.informatik;

import java.util.List;

public interface Strategy {
    List<Product> execute(Graph graph, Product start); 
}
