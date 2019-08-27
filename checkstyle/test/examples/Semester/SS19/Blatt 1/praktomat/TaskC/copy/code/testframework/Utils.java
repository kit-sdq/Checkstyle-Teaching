/*
 * Copyright (c) 2015-2017 Tobias Bachert. All Rights Reserved.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as
 * published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.b_privat.testframework;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractSet;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Utility class.
 * 
 * <p>Standalone version containing methods of different utility classes.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2017/03/10
 */
final class Utils {

    private Utils() {}

    /**
     * Appends the specified element to the specified array.
     * 
     * <p>The returned array may be of a type that differs from the type of {@code array} but is ensured to be able to
     * hold elements of the component type of {@code array}.
     * 
     * @param  <T> type parameter
     * @param  array the array to append to
     * @param  element the element to append
     * @return the resulting array
     * @throws ArrayStoreException if {@code element} cannot be stored in {@code array}
     * 
     * @see    #append(Object[], Object, Class)
     */
    public static <T> T[] append(T[] array, T element) {
        return append(array, element, componentType(array, element));
    }

    /**
     * Appends the specified element to the specified array.
     * 
     * <p>The returned array will be of the specified component type.
     * 
     * @param  <T> type parameter
     * @param  array the array to append to
     * @param  element the element to append
     * @param  componentType the component type
     * @return the resulting array
     * @throws ArrayStoreException if any element cannot be stored in an array with the specified componentType
     */
    public static <T> T[] append(T[] array, T element, Class<T> componentType) {
        int len = array.length;
        @SuppressWarnings("unchecked")
        T[] arr = (T[]) Array.newInstance(componentType, len + 1);
        System.arraycopy(array, 0, arr, 0, len);
        arr[len] = element;
        return arr;
    }

    /**
     * Merges the specified arrays.
     *
     * @param  <T> type parameter
     * @param  left the left array
     * @param  right the right array
     * @return the merged array
     * @throws ArrayStoreException if the elements cannot be stored in an array
     * 
     * @see    #merge(Object[], Object[], Class)
     */
    public static <T> T[] merge(T[] left, T[] right) {
        return merge(left, right, componentType(left, right));
    }

    /**
     * Merges the specified arrays.
     *
     * @param  <T> type parameter
     * @param  left the left array
     * @param  right the right array
     * @param  componentType the component type
     * @return the merged array
     * @throws ArrayStoreException if any element cannot be stored in an array with the specified componentType
     */
    public static <T> T[] merge(T[] left, T[] right, Class<T> componentType) {
        int llen = left.length, rlen = right.length;
        @SuppressWarnings("unchecked")
        T[] arr = (T[]) Array.newInstance(componentType, llen + rlen);
        System.arraycopy(left, 0, arr, 0, llen);
        System.arraycopy(right, 0, arr, llen, rlen);
        return arr;
    }

    /**
     * Returns the component type for the specified array and element.
     * 
     * @param  <T> type parameter
     * @param  array the array
     * @param  element the element
     * @return the component type
     * @throws ArrayStoreException if the super type cannot be inferred
     */
    @SuppressWarnings("unchecked")
    private static <T> Class<T> componentType(T[] array, T element) {
        Class<?> elementType, componentType = array.getClass().getComponentType();
        // @formatter:off
        return (Class<T>) (element == null || (elementType = element.getClass()) == componentType
                || componentType.isAssignableFrom(elementType) ? componentType
                 : elementType.isAssignableFrom(componentType) ? elementType
                 : inferSuperType(componentType, elementType));
        // @formatter:on
    }

    /**
     * Returns the component type for the specified arrays.
     * 
     * @param  <T> type parameter
     * @param  left the left array
     * @param  right the right array
     * @return the component type
     * @throws ArrayStoreException if neither of the component types is a direct super type of the other type
     */
    @SuppressWarnings("unchecked")
    private static <T> Class<T> componentType(T[] left, T[] right) {
        Class<?> lType = left.getClass().getComponentType(), rType = right.getClass().getComponentType();
        // @formatter:off
        return (Class<T>) (lType == rType
                || lType.isAssignableFrom(rType) ? lType
                 : rType.isAssignableFrom(lType) ? rType
                 : inferSuperType(lType, rType));
        // @formatter:on
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<? super T> inferSuperType(Class<? extends T> lType, Class<? extends T> rType) {
        final List<Class<?>> types = TypeUtil.commonSuperclasses(lType, rType);
        if (types.size() != 1)
            throw new ArrayStoreException("Cannot infer super type for " + lType.getName() + " and " + rType.getName());
        return (Class<T>) types.get(0);
    }

    private static final class TypeUtil {

        private TypeUtil() {}

        /**
         * Returns the common super classes of the specified classes.
         * 
         * @param  classes the classes
         * @return the common super classes
         */
        public static List<Class<?>> commonSuperclasses(Class<?>... classes) {
            return filterToSubTypes(commonSuperTypes(classes));
        }

        private static List<Class<?>> filterToSubTypes(Set<Class<?>> classes) {
            final List<Class<?>> result = new ArrayList<>(classes.size());
            while (!classes.isEmpty()) {
                final Iterator<Class<?>> it = classes.iterator();
                Class<?> c = it.next();
                it.remove();
                while (it.hasNext()) {
                    final Class<?> c2 = it.next();
                    if (c2.isAssignableFrom(c)) {
                        it.remove();
                    } else if (c.isAssignableFrom(c2)) {
                        c = c2;
                        it.remove();
                    }
                }
                result.add(c);
            }
            return result;
        }

        private static Set<Class<?>> commonSuperTypes(Class<?>... classes) {
            final int len = classes.length;
            if (len == 0)
                return Collections.emptySet();
            final Set<Class<?>> result = superTypes(classes[0]);
            for (int i = -1; ++i < len;) {
                Class<?> c = classes[i];
                result.removeIf(sup -> !sup.isAssignableFrom(c));
            }
            return result;
        }

        private static Set<Class<?>> superTypes(Class<?> clazz) {
            final Set<Class<?>> result = new IdentityProbingTable<>();
            final Queue<Class<?>> queue = new ArrayDeque<>();
            queue.add(clazz);
            for (Class<?> head; (head = queue.poll()) != null;) {
                if (result.add(head)) {
                    addOrElse(queue, head.getSuperclass(), Object.class);
                    addAll(queue, head.getInterfaces());
                }
            }
            return result;
        }

        private static <T> void addOrElse(Collection<T> c, T element, T orElse) {
            if (element != null)
                c.add(element);
            else
                c.add(orElse);
        }

        private static <T> void addAll(Collection<T> c, T[] a) {
            for (final T element : a)
                c.add(element);
        }
    }

    public static final class IdentityProbingTable<E> extends AbstractSet<E> {

        private Object[] table;
        private int size;
        private int resizeSize; // capacity * loadfactor

        public IdentityProbingTable() {
            table = new Object[16];
            resizeSize = 12;
        }

        public IdentityProbingTable(int capacity) {
            final int tabLen = Math.max(4, Integer.highestOneBit(capacity - 1) << 1);
            table = new Object[tabLen];
            resizeSize = tabLen * 3 >>> 2;
        }

        private static int hash(Object o) {
            final int h = System.identityHashCode(o);
            return h ^ h >>> 16;
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean contains(Object o) {
            o = wrapNull(o);
            final Object[] tab = table;
            final int mask = tab.length - 1;
            for (int p = hash(o) & mask;; p = p + 1 & mask) {
                final Object n = tab[p];
                if (n == null) return false;
                if (n == o) return true;
            }
        }

        @Override
        public boolean removeIf(Predicate<? super E> filter) {
            @SuppressWarnings("unchecked")
            final Predicate<Object> erased = (Predicate<Object>) filter;
            final Object[] tab = table;
            boolean removed = false;
            for (int i = tab.length; --i >= 0;) {
                final Object o = tab[i];
                if (o != null && erased.test(unwrapNull(o))) {
                    removed = true;
                    tab[i] = null;
                    size--;
                }
            }
            return removed;
        }

        @Override
        public boolean remove(Object o) {
            o = wrapNull(o);
            final Object[] tab = table;
            final int mask = tab.length - 1;
            for (int p = hash(o) & mask;; p = p + 1 & mask) {
                final Object n = tab[p];
                if (n == null) return false;
                if (n == o) {
                    tab[p] = null;
                    size--;
                    return true;
                }
            }
        }

        @Override
        public Iterator<E> iterator() {
            return new Iterator<E>() {

                private int index;

                @Override
                public boolean hasNext() {
                    final Object[] tab = table;
                    int i = index;
                    final int len = tab.length;
                    for (; i < len && tab[i] == null; i++) {}
                    return (index = i) < len;
                }

                @Override
                @SuppressWarnings("unchecked")
                public E next() {
                    if (!hasNext())
                        throw new NoSuchElementException();
                    return unwrapNull((E) table[index++]);
                }

                @Override
                public void remove() {
                    final Object[] tab;
                    final int ind = index - 1;
                    if (ind < 0 || (tab = table)[ind] == null)
                        throw new IllegalStateException();
                    tab[ind] = null;
                    size--;
                }
            };
        }

        @Override
        public boolean add(E e) {
            e = wrapNull(e);
            final int newSize = size + 1;
            Object[] tab = table;
            int len = tab.length;
            if (newSize > resizeSize) {
                final int newLen = len << 1;
                if (newLen < 0) {
                    if (size == len) {
                        if (contains(e))
                            return false;
                        else
                            throw new IllegalStateException("Capacity reached");
                    }
                    resizeSize = len;
                } else {
                    transfer(tab, len, tab = new Object[newLen], len = newLen);
                    table = tab;
                    resizeSize <<= 1;
                }
            }
            final int mask = len - 1;
            int p = hash(e) & mask;
            for (Object o; (o = tab[p]) != null; p = p + 1 & mask)
                if (o == e)
                    return false;
            tab[p] = e;
            size = newSize;
            return true;
        }

        private static void transfer(Object[] oldTab, int oldLen, Object[] newTab, int newLen) {
            assert oldTab.length == oldLen && newTab.length == newLen;
            final int mask = newLen - 1;
            for (int n = oldLen; --n >= 0;) {
                final Object o = oldTab[n];
                if (o != null) {
                    int p = hash(o) & mask;
                    for (; newTab[p] != null; p = p + 1 & mask) {}
                    newTab[p] = o;
                }
            }
        }
    }

    private static final NullWrapper NULL = new NullWrapper();
    private static final class NullWrapper implements Serializable {

        private static final long serialVersionUID = -2821147397069519360L;

        NullWrapper() {}

        @Override
        public boolean equals(Object o) {
            return o instanceof NullWrapper;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public String toString() {
            return "null";
        }

        private Object readResolve() {
            return NULL;
        }
    }

    /**
     * Unwraps values wrapped by {@linkplain #wrapNull(Object)}.
     * 
     * @param  <T> type parameter
     * @param  value the value to unwrap
     * @return {@code value}, or {@code null} if {@code value} is a wrapped {@code null} value
     */
    static <T> T unwrapNull(T value) {
        return value == NULL ? null : value;
    }

    /**
     * Wraps {@code null} in a wrapper object. To unwrap the value, the {@linkplain #unwrapNull(Object)} has to be used.
     * 
     * <p>Note that the runtime type of the returned object is not of the generic type, but ensured to be serializable.
     * 
     * @param  <T> type parameter
     * @param  value the value to wrap
     * @return {@code value}, or a wrapper object if {@code value} is {@code null}
     */
    @SuppressWarnings("unchecked")
    static <T> T wrapNull(T value) {
        return value == null ? (T) NULL : value;
    }
}
