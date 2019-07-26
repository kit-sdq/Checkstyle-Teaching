package edu.kit.informatik.goal;

import edu.kit.informatik.Type;

public enum GoalType {
    LESS_THAN {
        @Override
        public Goal createInstance(Type type, double... values) {
            return new LessThanGoal(type, values[0]);
        }
    },
    
    GREATER_THAN {
        @Override
        public Goal createInstance(Type type, double... values) {
            return new GreaterThanGoal(type, values[0]);
        }
    },
    
    BETWEEN {
        @Override
        public Goal createInstance(Type type, double... values) {
            return new BetweenGoal(type, values[0], values[1]);
        }
    };
    
    public abstract Goal createInstance(Type type, double... values);
    
    public static GoalType fromString(String name) {
        return valueOf(name.toUpperCase().replace("-", "_"));
    }
}
