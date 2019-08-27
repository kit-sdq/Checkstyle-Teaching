package edu.kit.informatik;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SiblingsStrategy implements Strategy {
    @Override
    public List<Product> execute(Graph graph, Product start) {
        //Search all direct supercategories
        List<Category> superCategories = graph.getDirectlyReachableNodes(start, Category.class, Relation.CONTAINED_IN);
        
        //Search all products reachable by "contains" for all categories using a set to prevent duplicates.
        Set<Product> recommendesProducts = new HashSet<>();
        for (Category category : superCategories) {
            recommendesProducts.addAll(graph.getDirectlyReachableNodes(category, Product.class, Relation.CONTAINS));
        }
        
        //Remove the product itself and return
        recommendesProducts.remove(start);
        return new ArrayList<>(recommendesProducts);
    }
}
