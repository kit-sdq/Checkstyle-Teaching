package edu.kit.informatik._intern.util.collections;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import edu.kit.informatik._intern.util.StringUtil;

/**
 * Node-class.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/07/11
 * 
 * @param   <T> value type parameter
 * @param   <E> node type parameter
 */
abstract class Node<T, E extends Node<T, E>> implements Iterable<E> {
    
    private Node<T, E> p;
    private final T t;
    private Deque<E> children;
    
    /**
     * Creates a new {@code Node} with the provided value.
     * 
     * @param t the value
     */
    protected Node(final T t) {
        this.t = t;
    }
    
    /**
     * Returns the parent of the node.
     * 
     * @return the parent
     */
    public final E parent() {
        return p == null ? null : p.asE();
    }
    
    /**
     * Returns the value of the node.
     * 
     * @return the value
     */
    public final T value() {
        return t;
    }
    
    /**
     * Returns a stream containing the children of the node.
     * 
     * @return a stream containing the children
     */
    protected final Stream<E> children() {
        return children == null ? Stream.empty() : children.stream();
    }
    
    /**
     * Returns whether the node is a root-node.
     * 
     * @return {@code true} if the node is a root-node, {@code false} otherwise
     */
    public boolean isRoot() {
        return p == null;
    }
    
    /**
     * Leaves the scope of nodes matching the predicate.
     * 
     * @param  predicate the predicate
     * @return the first node with a value that does not match the predicate, or the root-node if all nodes are matching
     *         the predicate
     */
    public final E leave(final Predicate<T> predicate) {
        Node<T, E> e = this;
        for (; e.p != null && predicate.test(e.t); e = e.p) { }
        return e.asE();
    }
    
    /**
     * Pulls {@code count} children of the parent node to the level of the node.
     * 
     * <p>If less than {@code count} children are available, then all of the available children will be pulled
     * 
     * @param count the count of children to pull
     */
    public final void pull(final int count) {
        if (count > 0 && p.children != null && !p.children.isEmpty()) {
            final Queue<E> queue = new ArrayDeque<>();
            final Iterator<E> it = p.children.iterator();
            for (E e; it.hasNext() && (e = it.next()) != this;) {
                if (queue.size() == count) {
                    queue.remove();
                }
                queue.add(e);
            }
            for (final E pulled : queue) {
                p.children.remove(pulled);
                add(pulled);
            }
        }
    }
    
    /**
     * Adds the child to the children of the node.
     * 
     * @param  child the child to add
     * @return the added child
     */
    protected final E add(final E child) {
        child.n().p = this;
        if (children == null) {
            children = new ArrayDeque<>();
        }
        children.add(child);
        return child;
    }
    
    /**
     * Returns this.
     * 
     * @return this
     */
    protected final Node<T, E> n() {
        return this;
    }
    
    @Override
    public final String toString() {
        final StringBuilder sb = new StringBuilder().append("Structure:").append(System.lineSeparator());
        toString(sb, 0);
        return sb.toString();
    }
    
    /**
     * Returns an additional string to append when {@link #toString()} is called.
     * 
     * @return an additional string to append when {@link #toString()} is called
     */
    @SuppressWarnings("static-method")
    protected /* non-final */ String appendForToString() {
        return null;
    }
    
    @SuppressWarnings("unchecked")
    private E asE() {
        return (E) this;
    }
    
    private void toString(final StringBuilder sb, final int level) {
        sb.append(/*'\u251C'*/'|');
        IntStream.range(0, level).forEach((i) -> sb.append(/*'\u253C'*/' '));
        sb.append(/*'\u2524'*/'|');
        StringUtil.removeConsecutiveWhitespace(String.valueOf(t)).concat(StringUtil.nullToEmpty(appendForToString()))
                .chars().map(StringUtil::controlCharacterToSymbol).forEachOrdered(sb::appendCodePoint);
        sb.append(System.lineSeparator());
        if (children != null) {
            children.forEach((n) -> n.n().toString(sb, level + 1));
        }
    }
    
    @Override
    public Iterator<E> iterator() {
        return new NodeIterator<>(this);
    }
    
    private static final class NodeIterator<E extends Node<?, E>> implements Iterator<E> {
        
        private Node<?, E> cr;
        
        /**
         * Creates a new {@code NodeIterator} towards the root.
         * 
         * @param cr the current node
         */
        NodeIterator(final Node<?, E> cr) {
            this.cr = cr;
        }
        
        @Override public boolean hasNext() {
            return cr != null;
        }
        
        @Override public E next() {
            if (cr == null) {
                throw new NoSuchElementException();
            }
            final E e = cr.asE();
            cr = cr.p;
            return e;
        }
    }
}
