/*
 * Copyright (c) 2017-2017 Tobias Bachert. All Rights Reserved.
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

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Annotation;
import java.lang.annotation.AnnotationFormatError;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.locks.StampedLock;

/**
 * Indicates expected exceptions. Annotations have to be obtained using the {@linkplain Expected.Support#get(Method)}
 * method.
 * 
 * <p>This annotation may be used as meta annotation to annotate another annotation type. Any method that is annotated
 * with such an annotated annotation type will inherit any directly or indirectly present {@code @Expected} annotation.
 * The resulting annotation will report the combination of all {@linkplain #value() expected exceptions} and the logical
 * <em>or</em> of all {@linkplain #mandatory() mandatory} values.
 * 
 * <p><b>Example:</b>
 * <blockquote><pre>
 * &#64;Expected(NullPointerException.class}
 * &#64;interface NPE {}
 * 
 * &#64;NPE
 * void someMethod();
 * 
 * // behaves exactly like
 * 
 * &#64;Expected(NullPointerException.class}
 * void someMethod();</pre>
 * </blockquote>
 * 
 * @author  Tobias Bachert
 * @version 1.02, 2017/03/14
 */
@Documented
@Target({METHOD, ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface Expected {

    /**
     * Returns the expected exceptions.
     * 
     * @return the expected exceptions
     */
    Class<? extends Throwable>[] value();

    /**
     * Returns whether throwing any of the exceptions is mandatory or optional.
     * 
     * <p>The default implementation returns {@code true}.
     * 
     * @return {@code true} if throwing is mandatory, {@code false} otherwise
     */
    boolean mandatory() default true;

    /**
     * Provides utility methods regarding {@linkplain Expected} annotations.
     */
    final class Support {

        private static final Map<Class<? extends Annotation>, Expected> cache = new WeakHashMap<>();
        private static final StampedLock lock = new StampedLock();

        @SuppressWarnings("unchecked")
        private static final Class<? extends Throwable>[] emptyArray = new Class[0];
        private static final Expected empty = new ExpectedImpl(emptyArray, false);

        private Support() {}

        /**
         * Returns the {@linkplain Expected} annotation for the specified method.
         * 
         * <p>Note that the returned annotation includes any indirectly present annotations (contrary to the annotation
         * returned by {@linkplain Method#getAnnotation(Class) m.getAnnotation(Expected.class)}). If no annotation is
         * present, then this method returns an annotation that reports {@linkplain Expected#value() no exceptions} and
         * {@linkplain Expected#mandatory() optional}.
         * 
         * @param  m the method
         * @return the annotation if present, {@code @Expected(value=[], mandatory=false)} otherwise
         */
        public static Expected get(Method m) {
            final Builder builder = new Builder();
            for (final Annotation a : m.getAnnotations())
                builder.add(get(a.annotationType()));
            if (m.isAnnotationPresent(Expected.class))
                builder.add(m.getAnnotation(Expected.class));
            return builder.build();
        }

        /**
         * Returns whether the specified annotation defines the provided throwable as {@linkplain Expected#value()
         * expected}.
         * 
         * @param  expected the annotation
         * @param  t the throwable to check for
         * @return {@code true} if {@code expected} expects throwables of the type of {@code t}, {@code false} otherwise
         */
        public static boolean isExpected(Expected expected, Throwable t) {
            for (final Class<? extends Throwable> c : exceptions(expected))
                if (c.isInstance(t))
                    return true;
            return false;
        }

        private static Expected get(Class<? extends Annotation> c) {
            Expected e;
            if ((e = getCached(c)) == null) {
                final Expected o = cache(c, e = make(c, new HashSet<>()));
                if (o != null) {
                    e = o;
                } else {
                    for (final Annotation a : c.getAnnotations())
                        get(a.annotationType());
                }
            }
            return e;
        }

        private static Expected make(Class<? extends Annotation> c, Set<Class<?>> dejaVu) {
            Expected e;
            if ((e = getCached(c)) == null) {
                final Builder builder = new Builder();
                for (final Annotation a : c.getAnnotations())
                    if (dejaVu.add(a.annotationType()))
                        builder.add(make(a.annotationType(), dejaVu));
                if (c.isAnnotationPresent(Expected.class))
                    builder.add(c.getAnnotation(Expected.class));
                e = builder.build();
            }
            return e;
        }

        private static Class<? extends Throwable>[] exceptions(Expected expected) {
            return expected instanceof ExpectedImpl ? ((ExpectedImpl) expected).value : expected.value();
        }

        private static Expected cache(Class<? extends Annotation> c, Expected expected) {
            Expected e;
            long stamp = lock.tryOptimisticRead();
            if ((e = cache.get(c)) != null)
                return e;
            final boolean b;
            if (b = (stamp = lock.tryConvertToWriteLock(stamp)) == 0)
                lock.writeLock();
            try {
                return b && (e = cache.get(c)) != null ? e : cache.put(c, expected);
            } finally {
                lock.unlockWrite(stamp);
            }
        }

        private static Expected getCached(Class<? extends Annotation> c) {
            final Expected e;
            long stamp = lock.tryOptimisticRead();
            if ((e = cache.get(c)) != null || lock.validate(stamp))
                return e;
            stamp = lock.readLock();
            try {
                return cache.get(c);
            } finally {
                lock.unlockRead(stamp);
            }
        }

        private static final class Builder {

            private final Set<Class<? extends Throwable>> exceptions = new LinkedHashSet<>();
            private boolean mandatory;

            Builder() {}

            Expected build() {
                return !mandatory && exceptions.isEmpty() ? empty
                        : new ExpectedImpl(filterRedundantTypes(exceptions.toArray(emptyArray)), mandatory);
            }

            void add(Expected expected) {
                for (final Class<? extends Throwable> c : exceptions(expected)) {
                    if (!Throwable.class.isAssignableFrom(c))
                        throw new AnnotationFormatError("@Expected annotation reports invalid #value(): " + expected);
                    exceptions.add(c);
                }
                mandatory |= expected.mandatory();
            }

            private static Class<? extends Throwable>[] filterRedundantTypes(Class<? extends Throwable>[] classes) {
                int len = classes.length;
                for (int i = len; --i >= 0;) {
                    final Class<?> c = classes[i];
                    for (int n = len; --n >= 0;) {
                        if (i != n && classes[n].isAssignableFrom(c)) {
                            classes[i] = classes[--len];
                            break;
                        }
                    }
                }
                return len == classes.length ? classes : Arrays.copyOf(classes, len);
            }
        }

        private static final class ExpectedImpl implements Expected, java.io.Serializable {

            private static final long serialVersionUID = -5403454443123977980L;

            private final Class<? extends Throwable>[] value;
            private final boolean mandatory;

            private ExpectedImpl(Class<? extends Throwable>[] value, boolean mandatory) {
                this.value = value;
                this.mandatory = mandatory;
            }

            @Override
            public Class<? extends Throwable>[] value() {
                return value.length == 0 ? value : value.clone();
            }

            @Override
            public boolean mandatory() {
                return mandatory;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Expected.class;
            }

            @Override
            public boolean equals(Object o) {
                final Expected e;
                return o == this || o instanceof Expected && mandatory == (e = (Expected) o).mandatory()
                        && Arrays.equals(value, exceptions(e));
            }

            @Override
            public int hashCode() {
                return 1335633679 ^ Arrays.hashCode(value) + 1639989927 ^ Boolean.hashCode(mandatory);
            }

            @Override
            public String toString() {
                return "@" + annotationType().getName() + "(value=" + Arrays.toString(value)
                        + ", mandatory=" + mandatory + ")";
            }
        }
    }
}
