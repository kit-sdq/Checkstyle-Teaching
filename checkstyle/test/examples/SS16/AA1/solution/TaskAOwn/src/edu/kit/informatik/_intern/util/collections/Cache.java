package edu.kit.informatik._intern.util.collections;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Concurrent cache that uses weak references to store keys and values.
 * 
 * <p>This implementation should only be used if values are strongly referencing their own keys, otherwise it is advised
 * to use {@link java.util.WeakHashMap} instead.
 * 
 * <p>This map does not permit {@code null} keys or values.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/06/20
 * 
 * @param   <K> the key type parameter
 * @param   <V> the value type parameter
 */
public final class Cache<K, V> {
    
    private final Map<Entry<K>, Entry<V>> mK = new ConcurrentHashMap<>();
    private final Map<Entry<V>, Entry<K>> mV = new ConcurrentHashMap<>();
    private final ReferenceQueue<K> qK = new ReferenceQueue<>();
    private final ReferenceQueue<V> qV = new ReferenceQueue<>();
    
    /**
     * Creates a new cache.
     */
    public Cache() {

    }
    
    /**
     * Returns the value assigned to the key, or {@code null} if no value is cached for the specific key.
     * 
     * @param  key the key
     * @return the value assigned to the key
     */
    public V get(final K key) {
        clear();
        final Entry<V> entry = mK.get(new Entry<>(key, null));
        if (entry != null) {
            final V v = entry.get();
            if (v != null) {
                return v;
            }
        }
        return null;
    }
    
    /**
     * Returns a value for the key.
     * 
     * <p>If the value for the key is currently cached, then the cached value will be returned, otherwise the provided
     * value will be mapped to the key and the to {@code key} assigned value returned (which may differ from {@code val}
     * in case that another thread tried to put a value for this specific key too).
     * 
     * @param  key the key
     * @param  val the value
     * @return a value for the key
     */
    public V putIfAbsent(final K key, final V val) {
        final Entry<K> eK = new Entry<>(key, qK);
        final Entry<V> eV = new Entry<>(val, qV);
        for (;;) {
            clear();
            final Entry<V> cr = mK.putIfAbsent(eK, eV);
            if (cr != null) {
                final V v = cr.get();
                if (v != null) {
                    mV.put(cr, eK);
                    return v;
                }
            }
        }
    }
    
    private void clear() {
        for (Reference<? extends V> rV; (rV = qV.poll()) != null;) {
            final Entry<K> k = mV.remove(rV);
            if (k != null) {
                mK.remove(k);
            }
        }
        for (Reference<? extends K> rK; (rK = qK.poll()) != null;) {
            final Entry<V> v = mK.remove(rK);
            if (v != null) {
                mV.remove(v);
            }
        }
    }
    
    @Override
    public String toString() {
        return "Cache[" + mK.toString() + "]";
    }
    
    private static final class Entry<T> extends WeakReference<T> {
        
        private final int hashcode;
        
        Entry(final T t, final ReferenceQueue<? super T> queue) {
            super(t, queue);
            hashcode = t.hashCode();
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof Entry && hashcode == obj.hashCode()) {
                final Object a = get();
                final Object b = ((Entry<?>) obj).get();
                return a == null || b == null ? this == obj : a.equals(b);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return hashcode;
        }
        
        @Override
        public String toString() {
            final T t = get();
            return t == null ? "Entry[CLEARED]" : "Entry[" + t + "]";
        }
    }
}
