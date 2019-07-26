package edu.kit.informatik;

import edu.kit.informatik.aggregation.Aggregation;
import edu.kit.informatik.aggregation.AggregationType;

public class Type implements Comparable<Type> {
    private String name;
    private AggregationType aggregationType;
    
    public Type(String name, AggregationType aggregationType) {
        this.name = name;
        this.aggregationType = aggregationType;
    }
        
    public String getName() {
        return name;
    }

    public Aggregation createAggregation() {
        return aggregationType.createInstance(this);
    }
    
    @Override
    public String toString() {
        return name + ":";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Type type = (Type) o;

        return name != null ? name.equals(type.name) : type.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public int compareTo(Type o) {
        return name.compareTo(o.name);
    }
}
