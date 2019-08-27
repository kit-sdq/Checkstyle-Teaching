package edu.kit.informatik.aggregation;

import edu.kit.informatik.goal.BetweenGoal;
import edu.kit.informatik.goal.GreaterThanGoal;
import edu.kit.informatik.goal.LessThanGoal;

public interface Aggregation {
    String getName();
    
    Double getHighestValue();
    
    boolean hasValue();
    
    void addValue(double newValue);
    
    boolean goalReached(LessThanGoal goal);
    
    boolean goalReached(GreaterThanGoal goal);
    
    boolean goalReached(BetweenGoal goal);
}
