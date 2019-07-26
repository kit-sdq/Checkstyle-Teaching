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
package edu.kit.informatik._internal;

import java.io.Serializable;
import java.util.AbstractQueue;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.IntStream;

/**
 * Utility class.
 * 
 * <p>Standalone version containing methods of different utility classes.
 * 
 * @author  Tobias Bachert
 * @version 1.01, 2017/02/27
 */
public final class Utils {

    private Utils() {}

    /**
     * Returns whether {@linkplain CharSequence#subSequence(int, int) charSequence.subSequence(start, end)} would not
     * throw an exception.
     * 
     * <p>This method returns {@code false} if {@code charSequence} is {@code null}.
     * 
     * @param  charSequence the char sequence
     * @param  start the start index (inclusive)
     * @param  end the end index (exclusive)
     * @return {@code true} if the specified sub sequence exists in {@code charSequence}
     */
    public static boolean isValidSubSequence(CharSequence charSequence, int start, int end) {
        return charSequence != null && (start | ~start + end | charSequence.length() - end) >= 0;
    }

    /**
     * Checks whether {@code start->end} is a {@linkplain #isValidSubSequence(CharSequence, int, int) valid
     * subsequence}.
     * 
     * @param  charSequence the char sequence
     * @param  start the start index (inclusive)
     * @param  end the end index (exclusive)
     * @throws NullPointerException if {@code charSequence} is {@code null}
     * @throws IndexOutOfBoundsException if {@code start->end} is not a valid sub sequence
     */
    public static void ensureValidSubSequence(CharSequence charSequence, int start, int end) {
        if (!isValidSubSequence(charSequence, start, end))
            throw new IndexOutOfBoundsException("start=" + start + ",end=" + end + ",length=" + charSequence.length());
    }

    /**
     * Returns whether {@linkplain CharSequence#charAt(int) charSequence.charAt(index)} would not throw an exception.
     * 
     * <p>This method returns {@code false} if {@code charSequence} is {@code null}.
     * 
     * @param  charSequence the char sequence
     * @param  index the index
     * @return {@code true} if the specified index exists in {@code charSequence}
     */
    public static boolean isValidIndex(CharSequence charSequence, int index) {
        return charSequence != null && (index | ~index + charSequence.length()) >= 0;
    }

    /**
     * Checks whether {@code index} is a {@linkplain #isValidIndex(CharSequence, int) valid index}.
     * 
     * <p>This method returns {@code false} if {@code charSequence} is {@code null}.
     * 
     * @param  charSequence the char sequence
     * @param  index the index
     * @throws NullPointerException if {@code charSequence} is {@code null}
     * @throws IndexOutOfBoundsException if {@code index} is not a valid index
     */
    public static void ensureValidIndex(CharSequence charSequence, int index) {
        if (!isValidIndex(charSequence, index))
            throw new IndexOutOfBoundsException("index=" + index + ",length=" + charSequence.length());
    }

    /**
     * Returns a char sequence that contains the specified character {@code n} times.
     * 
     * <p>The type of the returned char sequence is undefined but is ensured to be serializable.
     * 
     * @param  ch the character
     * @param  n the length of the returned sequence
     * @return the char sequence
     * @throws IllegalArgumentException if {@code n} is negative
     */
    public static CharSequence nTimes(char ch, int n) {
        if (n < 0)
            throw new IllegalArgumentException("negative length " + n);
        return new RepeatedCharacter(ch, n);
    }

    /**
     * Represents a char sequence consisting of a single repeated character.
     * 
     * @author  Tobias Bachert
     * @version 1.02, 2016/11/13
     */
    private static final class RepeatedCharacter implements CharSequence, Serializable {

        private static final long serialVersionUID = -8134602170909111931L;

        private final char ch;
        private final int length;

        /**
         * Constructs a new {@code RepeatedCharacter}.
         * 
         * @param ch the character to repeat
         * @param length the count of repetitions
         */
        RepeatedCharacter(char ch, int length) {
            this.ch = ch;
            this.length = length;
        }

        @Override
        public int length() {
            return length;
        }

        @Override
        public char charAt(int index) {
            ensureValidIndex(this, index);
            return ch;
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            ensureValidSubSequence(this, start, end);
            return (start | end ^ length) == 0 ? this : new RepeatedCharacter(ch, end - start);
        }

        @Override
        public boolean equals(Object o) {
            final RepeatedCharacter r;
            return o instanceof RepeatedCharacter && (ch ^ (r = (RepeatedCharacter) o).ch | length ^ r.length) == 0;
        }

        @Override
        public int hashCode() {
            return length << 16 ^ ch;
        }

        @Override
        public String toString() {
            final char[] arr = new char[length];
            Arrays.fill(arr, ch);
            return new String(arr);
        }

        @Override
        public IntStream chars() {
            final char ch = this.ch;
            return IntStream.range(0, length()).map(i -> ch);
        }

        @Override
        public IntStream codePoints() {
            return chars();
        }
    }

    /**
     * Returns an empty, unmodifiable queue.
     * 
     * @param  <T> type parameter
     * @return the queue
     */
    @SuppressWarnings("unchecked")
    public static <T> Queue<T> emptyQueue() {
        return (Queue<T>) EmptyQueue.EMPTY_QUEUE;
    }

    private static final class EmptyQueue<E> extends AbstractQueue<E> implements Serializable {

        private static final long serialVersionUID = -3912595352267297618L;

        static final EmptyQueue<?> EMPTY_QUEUE = new EmptyQueue<>();

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @Override
        public Iterator<E> iterator() {
            return Collections.emptyIterator();
        }

        @Override
        public Spliterator<E> spliterator() {
            return Spliterators.emptySpliterator();
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public <T> T[] toArray(T[] a) {
            if (a.length != 0)
                a[0] = null;
            return a;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {}

        @Override
        public boolean add(Object e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean offer(Object e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public E remove() {
            throw new NoSuchElementException();
        }

        @Override
        public E poll() {
            return null;
        }

        @Override
        public E element() {
            throw new NoSuchElementException();
        }

        @Override
        public E peek() {
            return null;
        }

        private Object readResolve() {
            return EMPTY_QUEUE;
        }
    }
}
