package edu.kit.informatik._intern.util.collections;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Node-class.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/07/11
 * 
 * @param   <T> value type parameter
 */
public final class CNode<T> extends Node<T, CNode<T>> {
    
    private Function<T, BiFunction<CNode<T>, Stream<Object>, Object>> f;
    private Object r;
    
    private CNode(final T t, final Function<T, BiFunction<CNode<T>, Stream<Object>, Object>> f) {
        super(t);
        this.f = f;
    }
    
    /**
     * Returns a new root-node.
     * 
     * @param  <T> value type parameter
     * @param  convert function used to convert a value to a calculation function
     * @return a new root-node
     */
    public static <T> CNode<T> root(final Function<T, BiFunction<CNode<T>, Stream<Object>, Object>> convert) {
        return new CNode<>(null, Objects.requireNonNull(convert));
    }
    
    /**
     * Computes the node and all of its children.
     * 
     * <p>Further changes to this node or any sub-node will not affect the result of subsequent invocations.
     * 
     * @return the result of the computation
     */
    public Object compute() {
        if (isRoot()) {
            children().forEach(CNode::compute);
        } else if (f != null) {
            r = f.apply(value()).apply(this, children().map(CNode::compute));
            f = null;
        }
        return r;
    }
    
    /**
     * Adds the value as child to the node.
     * 
     * @param  t the value
     * @return the added child
     */
    public CNode<T> add(final T t) {
        return add(new CNode<>(Objects.requireNonNull(t), Objects.requireNonNull(f)));
    }
    
    @Override
    protected String appendForToString() {
        return f == null ? " -> " + r : null;
    }
}
