package edu.kit.informatik.aggregation;

import edu.kit.informatik.Helper;
import edu.kit.informatik.Type;

public class AverageAggregation extends OneValueAggregation {
    private Type type;
    private Double average;
    private int numberOfValues;

    public AverageAggregation(Type type) {
        this.type = type;
    }
    
    @Override
    public String getName() {
        return "avg";
    }

    @Override
    public Double getValue() {
        return average;
    }

    @Override
    public void addValue(double newValue) {
        if (average == null) {
            average = newValue;
        } else {
            average = ((average * numberOfValues) + newValue) / (numberOfValues + 1);
        }
        
        numberOfValues++;
    }

    @Override
    public String toString() {
        return type.toString() + " " + (average == null ? "none" : Helper.formatDouble(average));
    }
}
