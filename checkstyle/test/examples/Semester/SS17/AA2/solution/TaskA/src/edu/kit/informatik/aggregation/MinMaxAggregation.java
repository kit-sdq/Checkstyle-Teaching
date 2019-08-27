package edu.kit.informatik.aggregation;

import edu.kit.informatik.Helper;
import edu.kit.informatik.Type;
import edu.kit.informatik.goal.BetweenGoal;
import edu.kit.informatik.goal.GreaterThanGoal;
import edu.kit.informatik.goal.LessThanGoal;

public class MinMaxAggregation implements Aggregation {
    private Type type;
    private Double min;
    private Double max;
    
    public MinMaxAggregation(Type type) {
        this.type = type;
    }
    
    @Override
    public String getName() {
        return "minmax";
    }

    @Override
    public boolean hasValue() {
        return min != null && max != null;
    }

    @Override
    public Double getHighestValue() {
        return max;
    }

    @Override
    public void addValue(double newValue) {
        if (min == null || max == null) {
            min = newValue;
            max = newValue;
            return;
        }
        
        if (Helper.lessThan(newValue, min)) {
            min = newValue;
        }
        
        if (Helper.greaterThan(newValue, max)) {
            max = newValue;
        }
    }

    @Override
    public boolean goalReached(LessThanGoal goal) {
        return min != null && max != null && Helper.lessThan(max, goal.getTarget());
    }

    @Override
    public boolean goalReached(GreaterThanGoal goal) {
        return min != null && max != null && Helper.greaterThan(min, goal.getTarget());
    }

    @Override
    public boolean goalReached(BetweenGoal goal) {
        return min != null && max != null
                && Helper.greaterThan(min, goal.getMin()) && Helper.lessThan(max, goal.getMax());
    }

    @Override
    public String toString() {
        return type.toString() + " "
                + (min == null || max == null ? "none" : Helper.formatDouble(min) + " to " + Helper.formatDouble(max));
    }
}
