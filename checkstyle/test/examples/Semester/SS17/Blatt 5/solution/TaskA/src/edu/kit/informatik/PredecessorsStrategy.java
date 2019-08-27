package edu.kit.informatik;

import java.util.List;

public class PredecessorsStrategy implements Strategy {
    @Override
    public List<Product> execute(Graph graph, Product start) {
        return new SucPredStrategy(Relation.SUCCESSOR_OF).execute(graph, start);
    }
}
