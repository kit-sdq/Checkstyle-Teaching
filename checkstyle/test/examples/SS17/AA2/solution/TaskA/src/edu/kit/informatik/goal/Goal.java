package edu.kit.informatik.goal;

import edu.kit.informatik.Type;
import edu.kit.informatik.aggregation.Aggregation;

public interface Goal {
    Type getType();
    boolean evaluate(Aggregation aggregation);
}
