package edu.kit.informatik.aggregation;

import edu.kit.informatik.Helper;
import edu.kit.informatik.goal.BetweenGoal;
import edu.kit.informatik.goal.GreaterThanGoal;
import edu.kit.informatik.goal.LessThanGoal;

public abstract class OneValueAggregation implements Aggregation {
    public abstract Double getValue();

    @Override
    public boolean hasValue() {
        return getValue() != null;
    }

    @Override
    public Double getHighestValue() {
        return getValue();
    }
    
    @Override
    public abstract void addValue(double newValue);

    @Override
    public boolean goalReached(LessThanGoal goal) {
        return getValue() != null && Helper.lessThan(getValue(), goal.getTarget());
    }

    @Override
    public boolean goalReached(GreaterThanGoal goal) {
        return getValue() != null && Helper.greaterThan(getValue(), goal.getTarget());
    }

    @Override
    public boolean goalReached(BetweenGoal goal) {
        return getValue() != null
                && Helper.lessThan(goal.getMin(), getValue()) && Helper.lessThan(getValue(), goal.getMax());
    }
}
