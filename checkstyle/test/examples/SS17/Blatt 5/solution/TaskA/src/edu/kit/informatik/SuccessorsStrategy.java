package edu.kit.informatik;

import java.util.List;

public class SuccessorsStrategy implements Strategy {
    @Override
    public List<Product> execute(Graph graph, Product start) {
        return new SucPredStrategy(Relation.PREDECESSOR_OF).execute(graph, start);
    }
}
