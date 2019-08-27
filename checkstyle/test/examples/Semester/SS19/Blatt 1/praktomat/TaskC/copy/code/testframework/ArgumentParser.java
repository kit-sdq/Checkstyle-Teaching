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

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * Map: {,}:@{key0:val0,key1:val1}
 * Col: {,}@{ele0,ele1}
 */
@SuppressWarnings("rawtypes")
public final class ArgumentParser {

    private static final TypeVariable<Class<Collection>> java_util_Collection_E;
    static {
        final TypeVariable<Class<Collection>>[] collection = Collection.class.getTypeParameters();
        java_util_Collection_E = collection[0];
    }
    private static final TypeVariable<Class<Map>> java_util_Map_K;
    private static final TypeVariable<Class<Map>> java_util_Map_V;
    static {
        final TypeVariable<Class<Map>>[] map = Map.class.getTypeParameters();
        java_util_Map_K = map[0];
        java_util_Map_V = map[1];
    }

    private final Map<Type, ConvertFunction<?>> parser = new HashMap<>();
    private String nullLiteral = "null";

    public ArgumentParser() {}

    public void register(Type type, ConvertFunction<?> function) {
        type.getClass();
        function.getClass();
        parser.put(type, function);
    }

    public void nullLiteral(String nullLiteral) {
        nullLiteral.getClass();
        this.nullLiteral = nullLiteral;
    }

    public Object parse(Type t, String s) throws Throwable {
        return objectFrom(null, t, s);
    }

    private Object objectFrom(Class<?> c, Type t, String s) throws Throwable {
        if (s.equals(nullLiteral))
            return null;
        ConvertFunction<?> f = parser.get(t);
        if (f == null) {
            if (c == null)
                c = c(t);
            if (c.isArray())
                f = arrayFrom(c, t);
            else if (Collection.class.isAssignableFrom(c))
                f = collectionFrom(c, t);
            else if (Map.class.isAssignableFrom(c))
                f = mapFrom(c, t);
            else
                f = Converter.stringParameter(c)::invoke;
            parser.put(t, f);
        }
        return f.apply(s);
    }

    private static Class<?> c(Type t) {
        if (t instanceof TypeVariable)
            return c(((TypeVariable<?>) t).getBounds()[0]);
        if (t instanceof WildcardType)
            return c(((WildcardType) t).getUpperBounds()[0]);
        if (t instanceof GenericArrayType)
            return Array.newInstance(c(((GenericArrayType) t).getGenericComponentType()), 0).getClass();
        if (t instanceof ParameterizedType)
            return (Class<?>) ((ParameterizedType) t).getRawType();
        return (Class<?>) t;
    }

    private static ParameterizedType pt(Type t) {
        if (t instanceof TypeVariable)
            return pt(((TypeVariable<?>) t).getBounds()[0]);
        if (t instanceof WildcardType)
            return pt(((WildcardType) t).getUpperBounds()[0]);
        return ((ParameterizedType) t);
    }

    @SuppressWarnings("unchecked")
    private ConvertFunction<?> mapFrom(Class<?> c, Type t) {
        if (c == EnumMap.class) {
            final Type[] types = pt(t).getActualTypeArguments();
            final Type K = types[0];
            final Type V = types[1];
            final Class<Enum> enumType = (Class<Enum>) c(K);
            return s -> makeMap(() -> new EnumMap<>(enumType), K, V, s);
        }

        final Map<TypeVariable<?>, Type> tvs = new HashMap<>();
        TypeUtil.resolveSpecificType(tvs, t);
        TypeUtil.resolveTypeVariables(c, Map.class, tvs);
        final Type K = tvs.get(java_util_Map_K);
        final Type V = tvs.get(java_util_Map_V);
        final Class<? extends Map> implType = (Class<? extends Map>) mapImpl(c);
        return s -> makeMap(implType::newInstance, K, V, s);
    }

    @SuppressWarnings("unchecked")
    private ConvertFunction<?> collectionFrom(Class<?> c, Type t) {
        if (c == EnumSet.class) {
            final Type E = pt(t).getActualTypeArguments()[0];
            final Class<Enum> rawE = (Class<Enum>) c(E);
            return s -> makeCollection(() -> EnumSet.noneOf(rawE), null, E, s);
        }

        final Map<TypeVariable<?>, Type> tvs = new HashMap<>();
        TypeUtil.resolveSpecificType(tvs, t);
        TypeUtil.resolveTypeVariables(c, Collection.class, tvs);
        final Type E = tvs.get(java_util_Collection_E);
        final Class<? extends Collection> implType = (Class<? extends Collection>) collectionImpl(c);
        return s -> makeCollection(implType::newInstance, null, E, s);
    }

    private ConvertFunction<?> arrayFrom(Class<?> c, Type t) {
        final Class<?> rawComponentType = c.getComponentType();
        final Type genericComponentType = t instanceof Class ? rawComponentType
                : ((GenericArrayType) t).getGenericComponentType();
        return s -> {
            final List<Object> list = makeCollection(ArrayList::new, rawComponentType, genericComponentType, s);
            final Object array = Array.newInstance(rawComponentType, list.size());
            for (int i = list.size(); --i >= 0;)
                Array.set(array, i, list.get(i));
            return array;
        };
    }

    @SuppressWarnings("unchecked")
    private <M extends Map> M makeMap(Callable<M> supplier, Type K, Type V, String s) throws Throwable {
        final M map = supplier.call();
        final char pre = s.charAt(0);
        final char del = s.charAt(1);
        final char suf = s.charAt(2);
        final char sep = s.charAt(3);
        assert s.charAt(4) == '@';
        assert s.charAt(5) == pre;
        int i = 6;
        for (int next = i, len = s.indexOf(suf, next); next != len; i = next + 1) {
            next = s.indexOf(del, next + 1);
            if (next == -1 || next > len)
                next = len;
            final int sepIndex = s.indexOf(sep, i + 1);
            final Object key = objectFrom(null, K, s.substring(i, sepIndex));
            final Object val = objectFrom(null, V, s.substring(sepIndex + 1, next));
            map.put(key, val);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    private <C extends Collection> C makeCollection(Callable<C> supplier, Class<?> c, Type E, String s)
            throws Throwable {
        final C col = supplier.call();
        final char pre = s.charAt(0);
        final char del = s.charAt(1);
        final char suf = s.charAt(2);
        assert s.charAt(3) == '@';
        assert s.charAt(4) == pre;
        int i = 5;
        for (int next = i, len = s.indexOf(suf, next); next != len; i = next + 1) {
            next = s.indexOf(del, next + 1);
            if (next == -1 || next > len)
                next = len;
            col.add(objectFrom(c, E, s.substring(i, next)));
        }
        return col;
    }

    private static Class<?> collectionImpl(Class<?> c) {
        if (!Modifier.isAbstract(c.getModifiers()))
            return c;

        if (c == Collection.class)
            return ArrayList.class;

        if (c == Set.class)
            return HashSet.class;
        if (c == SortedSet.class)
            return TreeSet.class;
        if (c == NavigableSet.class)
            return TreeSet.class;

        if (c == List.class)
            return ArrayList.class;

        if (c == Queue.class)
            return ArrayDeque.class;
        if (c == Deque.class)
            return ArrayDeque.class;

        throw new UnsupportedOperationException(c.getName());
    }

    private static Class<?> mapImpl(Class<?> c) {
        if (!Modifier.isAbstract(c.getModifiers()))
            return c;

        if (c == Map.class)
            return HashMap.class;

        if (c == SortedMap.class)
            return TreeMap.class;
        if (c == NavigableMap.class)
            return TreeMap.class;

        if (c == ConcurrentMap.class)
            return ConcurrentHashMap.class;

        throw new UnsupportedOperationException(c.getName());
    }

    public interface ConvertFunction<R> {

        R apply(String s) throws Throwable;
    }
}
