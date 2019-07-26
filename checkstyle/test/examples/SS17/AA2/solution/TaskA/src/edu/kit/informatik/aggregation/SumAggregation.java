package edu.kit.informatik.aggregation;

import edu.kit.informatik.Helper;
import edu.kit.informatik.Type;

public class SumAggregation extends OneValueAggregation {
    private Type type;
    private Double sum;
    
    public SumAggregation(Type type) {
        this.type = type;
    }
    
    @Override
    public String getName() {
        return "sum";
    }

    @Override
    public Double getValue() {
        return sum;
    }

    @Override
    public void addValue(double newValue) {
        if (sum == null) {
            sum = newValue;
        } else {
            sum += newValue;
        }
    }
    
    @Override
    public String toString() {
        return type.toString() + " " + (sum == null ? "none" : Helper.formatDouble(sum));
    }
}
