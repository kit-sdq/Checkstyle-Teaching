package edu.kit.informatik._intern.util.collections;

import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.Supplier;

/**
 * This class consists exclusively of static methods that operate on or return collections.
 * 
 * <p>The methods of this class all throw a {@code NullPointerException} if the parameter provided to them are {@code
 * null}.
 * 
 * @author  Tobias Bachert
 * @version 1.08, 2016/03/23
 * 
 * @since   1.8
 */
public final class CollectionUtil {
    
    // Suppressed default constructor
    private CollectionUtil() { }
    
    @SuppressWarnings("unused")
    private static <T> boolean ascending0(final List<? extends T> list, final int from, final int to,
            final Comparator<? super T> c) {
        if (list instanceof RandomAccess) {
            for (int i = from, m = to - 1; i < m;)
                if (c.compare(list.get(i), list.get(++i)) > 0)
                    return false;
        } else {
            if (list.size() > 1) {
                final Iterator<? extends T> it = list.listIterator(from);
                T prev = it.next();
                for (int i = to - from; i > 0; i--)
                    if (c.compare(prev, prev = it.next()) > 0)
                        return false;
            }
        }
        return true;
    }
    
    /**
     * Returns a new reversed version of the given {@code Deque}.
     * 
     * @param  <E> element type parameter
     * @param  deque the {@code Deque} the reversed version will use as source
     * @return a new reversed version of {@code Deque}
     */
    public static <E> Deque<E> asReversedDeque(final Deque<E> deque) {
        return new ReversedDeque<>(deque);
    }
    
    /**
     * Returns a new reversed version of the given {@code List}.
     * 
     * @param  <E> element type parameter
     * @param  list the {@code List} the reversed version will use as source
     * @return a new reversed version of {@code list}
     */
    public static <E> List<E> asReversedList(final List<E> list) {
        return new ReversedList<>(list);
    }
    
    /**
     * Returns whether or not the given collections contain equal elements in the same order.
     * 
     * <p>For a reliable result the collection have to be sorted by any means.
     * 
     * @param  <E> element type parameter
     * @param  cA the first {@code Collection}
     * @param  cB the second {@code Collection}
     * @return {@code true} if and only if both collections contain equal elements in the same order
     */
    public static <E> boolean equalInContent(final Collection<? extends E> cA, final Collection<? extends E> cB) {
        // LOWPRIO Add support for random access
        if (cA == cB) return true;
        if (cA.size() != cB.size()) return false;
        
        for (final Iterator<? extends E> iterA = cA.iterator(), iterB = cB.iterator();;) {
            if (!iterA.hasNext()) return !iterB.hasNext();  // cA empty = cB has to be empty
            if (!iterB.hasNext()) return false;             // cA not empty
            if (!Objects.equals(iterA.next(), iterB.next())) return false;
        }
    }
    
    /**
     * Creates a new {@code HashMap} with the default load-factor {@code 0.75} with enough capacity to hold at least
     * {@code initialCapacity} elements prior resizing.<br>
     * This method throws {@code Exception}s as specified by the {@link HashMap#HashMap(int)} constructor.
     * 
     * @param  <K> key type parameter
     * @param  <V> value type parameter
     * @param  size the initial size
     * @return a new {@code HashMap}
     */
    public static <K, V> HashMap<K, V> newHashMap(final int size) {
        return newHashMap(size, 0.75f);
    }
    
    /**
     * Creates a new {@code HashMap} with the given {@code loadFactor} and enough capacity to hold at least {@code
     * initialCapacity} elements prior resizing.<br>
     * This method throws {@code Exception}s as specified by the {@link HashMap#HashMap(int, float)} constructor.
     * 
     * @param  <K> key type parameter
     * @param  <V> value type parameter
     * @param  size the initial size
     * @param  loadFactor the {@code loadFactor}
     * @return a new {@code HashMap}
     */
    public static <K, V> HashMap<K, V> newHashMap(final int size, final float loadFactor) {
        return new HashMap<>(toHashTableSize(size, loadFactor), loadFactor);
    }
    
    /**
     * Creates a new {@code HashSet} with the default load-factor {@code 0.75} with enough capacity to hold at least
     * {@code initialCapacity} elements prior resizing.<br>
     * This method throws {@code Exception}s as specified by the {@link HashSet#HashSet(int)} constructor.
     * 
     * @param  <E> element type parameter
     * @param  size the initial size
     * @return a new {@code HashSet}
     */
    public static <E> HashSet<E> newHashSet(final int size) {
        return newHashSet(size, 0.75f);
    }
    
    /**
     * Creates a new {@code HashSet} with the given {@code loadFactor} and enough capacity to hold at least {@code
     * initialCapacity} elements prior resizing.<br>
     * This method throws {@code Exception}s as specified by the {@link HashSet#HashSet(int, float)} constructor.
     * 
     * @param  <E> element type parameter
     * @param  size the initial size
     * @param  loadFactor the {@code loadFactor}
     * @return a new {@code HashSet}
     */
    public static <E> HashSet<E> newHashSet(final int size, final float loadFactor) {
        return new HashSet<>(toHashTableSize(size, loadFactor), loadFactor);
    }
    
    /**
     * Returns a collection containing the given {@code elements}.
     * 
     * @param  <C> collection type parameter
     * @param  <E> element type parameter
     * @param  supplier a {@code Supplier} for a collection
     * @param  elements the elements
     * @return a collection containing the given {@code elements}
     */
    @SafeVarargs
    public static <C extends Collection<? super E>, E> C of(final Supplier<? extends C> supplier, final E... elements) {
        final C collection = supplier.get();
        for (final E element : elements)
            collection.add(element);
        return collection;
    }
    
    /**
     * Returns a collection containing the given {@code element}.
     * 
     * @param  <C> collection type parameter
     * @param  <E> element type parameter
     * @param  supplier a {@code Supplier} for a collection
     * @param  a the element
     * @return a collection containing the given {@code element}
     */
    public static <C extends Collection<? super E>, E> C of(final Supplier<? extends C> supplier, final E a) {
        final C collection = supplier.get();
        collection.add(a);
        return collection;
    }
    
    /**
     * Returns a collection containing the given {@code elements}.
     * 
     * @param  <C> collection type parameter
     * @param  <E> element type parameter
     * @param  supplier a {@code Supplier} for a collection
     * @param  a the first element
     * @param  b the second element
     * @return a collection containing the given {@code elements}
     */
    public static <C extends Collection<? super E>, E> C of(final Supplier<? extends C> supplier, final E a,
            final E b) {
        final C collection = supplier.get();
        collection.add(a);
        collection.add(b);
        return collection;
    }
    
    /**
     * Returns a collection containing the given {@code elements}.
     * 
     * @param  <C> collection type parameter
     * @param  <E> element type parameter
     * @param  supplier a {@code Supplier} for a collection
     * @param  a the first element
     * @param  b the second element
     * @param  c the third element
     * @return a collection containing the given {@code elements}
     */
    public static <C extends Collection<? super E>, E> C of(final Supplier<? extends C> supplier, final E a, final E b,
            final E c) {
        final C collection = supplier.get();
        collection.add(a);
        collection.add(b);
        collection.add(c);
        return collection;
    }
    
    /**
     * Returns a collection containing the given {@code elements}.
     * 
     * @param  <C> collection type parameter
     * @param  <E> element type parameter
     * @param  supplier a {@code Supplier} for a collection
     * @param  a the first element
     * @param  b the second element
     * @param  c the third element
     * @param  d the fourth element
     * @return a collection containing the given {@code elements}
     */
    public static <C extends Collection<? super E>, E> C of(final Supplier<? extends C> supplier, final E a, final E b,
            final E c, final E d) {
        final C collection = supplier.get();
        collection.add(a);
        collection.add(b);
        collection.add(c);
        collection.add(d);
        return collection;
    }
    
    /**
     * Returns a collection containing the given {@code elements}.
     * 
     * @param  <C> collection type parameter
     * @param  <E> element type parameter
     * @param  supplier a {@code Supplier} for a collection
     * @param  a the first element
     * @param  b the second element
     * @param  c the third element
     * @param  d the fourth element
     * @param  e the fifth element
     * @return a collection containing the given {@code elements}
     */
    public static <C extends Collection<? super E>, E> C of(final Supplier<? extends C> supplier, final E a, final E b,
            final E c, final E d, final E e) {
        final C collection = supplier.get();
        collection.add(a);
        collection.add(b);
        collection.add(c);
        collection.add(d);
        collection.add(e);
        return collection;
    }
    
    /**
     * Returns the minimal hash table size for the given size and load factor.
     * 
     * @param  size the size
     * @param  loadFactor the load factor
     * @return the minimal hash table size for the given size and load factor
     * @throws ArithmeticException if the required size would be greater than {@code Integer.MAX_VALUE}.
     */
    static int toHashTableSize(final int size, final float loadFactor) {
        final double reqCapacity = ((double) size / loadFactor);
        
        if (reqCapacity > 1 << 30) throw new ArithmeticException("out of range");
        
        return Integer.highestOneBit((int) reqCapacity - 1) << 1;
    }
    
    /**
     * The class {@code ReversedDeque} allows reversed access on a {@code Deque}.
     * 
     * @param <E> element type parameter
     */
    private static class ReversedDeque<E> extends AbstractCollection<E> implements Deque<E> {
        
        private final Deque<E> source;
        
        /**
         * Creates a new reversed deque with the given {@code Deque} as source.
         * 
         * @param source the underlying {@code Deque}.
         */
        ReversedDeque(final Deque<E> source) {
            this.source = Objects.requireNonNull(source);
        }
        
        @Override public boolean add(final E e) { source.addFirst(e); return true; }
        @Override public void addFirst(final E e) { source.addLast(e); }
        @Override public void addLast(final E e) { source.addFirst(e); }
        @Override public void clear() { source.clear(); }
        @Override public boolean contains(final Object o) { return source.contains(o); }
        @Override public boolean containsAll(final Collection<?> c) { return source.containsAll(c); }
        @Override public Iterator<E> descendingIterator() { return source.iterator(); }
        @Override public E element() { return source.getLast(); }
        @Override public E getFirst() { return source.getLast(); }
        @Override public E getLast() { return source.getFirst(); }
        @Override public boolean isEmpty() { return source.isEmpty(); }
        @Override public Iterator<E> iterator() { return source.descendingIterator(); }
        @Override public boolean offer(final E e) { return source.offerFirst(e); }
        @Override public boolean offerFirst(final E e) { return source.offerLast(e); }
        @Override public boolean offerLast(final E e) { return source.offerFirst(e); }
        @Override public E peek() { return source.peekLast(); }
        @Override public E peekFirst() { return source.peekLast(); }
        @Override public E peekLast() { return source.peekFirst(); }
        @Override public E poll() { return source.pollLast(); }
        @Override public E pollFirst() { return source.pollLast(); }
        @Override public E pollLast() { return source.pollFirst(); }
        @Override public E pop() { return source.removeLast(); }
        @Override public void push(final E e) { source.addLast(e); }
        @Override public E remove() { return source.removeLast(); }
        @Override public boolean remove(final Object o) { return source.removeLastOccurrence(o); }
        @Override public boolean removeAll(final Collection<?> c) { return source.removeAll(c); }
        @Override public E removeFirst() { return source.removeLast(); }
        @Override public boolean removeFirstOccurrence(final Object o) { return source.removeLastOccurrence(o); }
        @Override public E removeLast() { return source.removeFirst(); }
        @Override public boolean removeLastOccurrence(final Object o) { return source.remove(o); }
        @Override public boolean retainAll(final Collection<?> c) { return source.retainAll(c); }
        @Override public int size() { return source.size(); }
    }
    
    /**
     * The class {@code ReversedList} allows reversed access on a {@code List}.
     * 
     * @param <E> element type parameter
     */
    private static class ReversedList<E> extends AbstractList<E> {
        
        private final List<E> source;
        
        /**
         * Creates a new reversed list with the given {@code List} as source.
         * 
         * @param list the underlying {@code List}.
         */
        ReversedList(final List<E> list) {
            source = Objects.requireNonNull(list);
        }
        
        @Override public void add(final int index, final E element) { source.add(reversed(index), element); }
        @Override public E get(final int index) { return source.get(reversed(index)); }
        @Override public E remove(final int index) { return source.remove(reversed(index)); }
        @Override public E set(final int index, final E element) { return source.set(reversed(index), element); }
        @Override public int size() { return source.size(); }
        
        private int reversed(final int index) {
            return size() - 1 - index;
        }
    }
}
