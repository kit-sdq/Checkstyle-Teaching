package edu.kit.informatik._intern.util.function;

/**
 * Collector-Interface.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/05/10
 * 
 * @param <A> type parameter of the container element
 * @param <R> type parameter of the result
 */
public interface LongCollector<A, R> {
    
    /**
     * Returns a joining to string collector.
     * 
     * @param  delimiter the delimiter used by the returned collector
     * @param  prefix the prefix used by the returned collector
     * @param  suffix the suffix used by the returned collector
     * @return a joining collector
     */
    static LongCollector<?, String> joining(final String delimiter, final String prefix, final String suffix) {
        return new LongCollector<StringBuilder, String>() {
            
            @Override
            public void accumulate(final StringBuilder builder, final long value) {
                if (builder.length() != 0)
                    builder.append(delimiter);
                builder.append(value);
            }
            
            @Override
            public StringBuilder container() {
                return new StringBuilder();
            }
            
            @Override
            public String finish(final StringBuilder builder) {
                return builder.insert(0, prefix).append(suffix).toString();
            }
        };
    }
    
    /**
     * Accumulates an {@code long} to a container.
     * 
     * @param a the container
     * @param value the {@code long}
     */
    void accumulate(final A a, final long value);
    
    /**
     * Returns the container of the collector.
     * 
     * @return the container of the collector
     */
    A container();
    
    /**
     * Finishes the container and returns the result of the collector.
     * 
     * @param  a the container
     * @return the result of the collector
     */
    R finish(final A a);
}
