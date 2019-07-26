package edu.kit.informatik.aggregation;

import edu.kit.informatik.Type;

public enum AggregationType {
    SUM {
        @Override
        public Aggregation createInstance(Type type) {
            return new SumAggregation(type);
        }
    },
    
    AVG {
        @Override
        public Aggregation createInstance(Type type) {
            return new AverageAggregation(type);
        }
    },
    
    LAST {
        @Override
        public Aggregation createInstance(Type type) {
            return new LastAggregation(type);
        }
    },
    
    MINMAX {
        @Override
        public Aggregation createInstance(Type type) {
            return new MinMaxAggregation(type);
        }
    };
    
    public abstract Aggregation createInstance(Type type);

    public static AggregationType fromString(String name) {
        return valueOf(name.toUpperCase().replace("-", "_"));
    }
//    public static Aggregation createAggregation(AggregationType name) {
//        switch (name) {
//            case SUM:
//                return new SumAggregation();
//            case AVERAGE:
//                return new AverageAggregation();
//            case LAST:
//                return new LastAggregation();
//            case MINMAX:
//                return new MinMaxAggregation();
//            default:
//                throw new IllegalArgumentException("Aggregation not known!");
//        }
//    }
}
