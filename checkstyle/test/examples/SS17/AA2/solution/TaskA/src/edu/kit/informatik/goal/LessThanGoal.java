package edu.kit.informatik.goal;

import edu.kit.informatik.Helper;
import edu.kit.informatik.Type;
import edu.kit.informatik.aggregation.Aggregation;

public class LessThanGoal implements Goal {
    private Type type;
    private double target;

    public LessThanGoal(Type type, double target) {
        this.type = type;
        this.target = target;
    }

    public double getTarget() {
        return target;
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
        return type.toString() + " " + "less-than " + Helper.formatDouble(target);
    }
}
