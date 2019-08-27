package edu.kit.informatik.goal;

import edu.kit.informatik.Helper;
import edu.kit.informatik.Type;
import edu.kit.informatik.aggregation.Aggregation;

public class BetweenGoal implements Goal {
    private Type type;
    private double min;
    private double max;

    public BetweenGoal(Type type, double min, double max) {
        this.type = type;
        this.min = min;
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }
    
    @Override
    public Type getType() {
        return type;
    }

    @Override
    public boolean evaluate(Aggregation aggregation) {
        return aggregation.goalReached(this);
    }

    @Override
    public String toString() {
        return type.toString() + " " + "between " + Helper.formatDouble(min) + " and " + Helper.formatDouble(max);
    }
}
