package edu.kit.informatik.aggregation;

import edu.kit.informatik.Helper;
import edu.kit.informatik.Type;

public class LastAggregation extends OneValueAggregation {
    private Type type;
    private Double value;

    public LastAggregation(Type type) {
        this.type = type;
    }
    
    @Override
    public String getName() {
        return "last";
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public void addValue(double newValue) {
        if (value == null) {
            value = newValue;
        } else {
            value = newValue;
        }
    }

    @Override
    public String toString() {
        return type.toString() + " " + (value == null ? "none" : Helper.formatDouble(value));
    }
}
