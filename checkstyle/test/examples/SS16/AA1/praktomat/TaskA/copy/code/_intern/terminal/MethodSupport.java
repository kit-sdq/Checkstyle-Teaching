package edu.kit.informatik._intern.terminal;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import edu.kit.informatik._intern.util.invoke.Invocations;
import edu.kit.informatik._intern.util.invoke.MethodTypes;
import edu.kit.informatik._intern.util.invoke.UncheckedReflectiveOperationException;

final class MethodSupport { // LOWPRIO Allow loading real code-blocks (if possible with security manager...)
    
    //                Class   ->  Method  ->  Arity    ->   MethodHandle
    private final Map<String, Map<String, Map<Integer, List<MethodHandle>>>> methods = new HashMap<>();
    private final Map<String, String> imports = new HashMap<>();
    private final Map<Class<?>, List<Class<?>>> subclasses = new HashMap<>();
    
    public void importClass(final String s) {
        final int index = s.lastIndexOf('$');
        final String classname = s.substring((index > 0 ? index : s.lastIndexOf('.')) + 1);
        final String current = imports.get(classname);
        if (current == null) {
            clazz(s);
            imports.putIfAbsent(classname, s);
        } else if (!current.equals(s)) {
            throw new IllegalStateException("ambiguous import of " + classname + " (" + current + ", " + s + ")");
        }
    }
    
    private String fullSpecified(final String s) {
        final int index = s.indexOf('$');
        if (index < 0) {
            return imports.getOrDefault(s, s);
        }
        final String classname = s.substring(0, index);
        final String mapped = imports.get(classname);
        return mapped != null ? mapped + s.substring(index) : s;
    }
    
    private Class<?> clazz(final String name) {
        final Class<?> cl;
        try {
            cl = Class.forName(name, false, Thread.currentThread().getContextClassLoader());
        } catch (final ClassNotFoundException e) {
            throw new TypeNotPresentException(name, e);
        }
        if (!subclasses.containsKey(cl)) {
            subclasses.put(cl, subclasses.keySet().stream().filter((clazz) -> cl.isAssignableFrom(clazz))
                    .collect(Collectors.toCollection(ArrayList::new)));
            subclasses.entrySet().stream().filter((e) -> e.getKey().isAssignableFrom(cl)).map(Map.Entry::getValue)
                    .forEach((l) -> l.add(cl));
        }
        return cl;
    }
    
    public void addConstructor(final String clazz, final String d1, final String d2) {
        final String classname = fullSpecified(clazz);
        final MethodType lookup = MethodTypes.toTypeNameMap(d1, imports);
        final MethodType type = d2 == null ? lookup : MethodTypes.toTypeNameMap(d2, imports);
        final boolean alreadyMapped = Optional.of(methods)
                .map((m) -> m.get(classname))
                .map((m) -> m.get(null))
                .map((m) -> m.get(type.parameterCount())).map(List::stream)
                .map((s) -> s.anyMatch((mh) -> MethodTypes.identicalPType(mh.type(), type)))
                .filter((b) -> b).isPresent();
        if (!alreadyMapped) {
            final Collection<MethodHandle> mhs = methods.computeIfAbsent(classname, (s) -> new HashMap<>())
                    .computeIfAbsent(null, (s) -> new HashMap<>(2))
                    .computeIfAbsent(type.parameterCount(), (i) -> new ArrayList<>(1));
            final Class<?> cl = clazz(classname);
            final MethodHandle mh;
            try {
                mh = MethodHandles.publicLookup().findConstructor(cl, lookup);
            } catch (final NoSuchMethodException | IllegalAccessException e) {
                throw new UncheckedReflectiveOperationException(e);
            }
            mhs.add(mh.asType(type.changeReturnType(cl)));
        }
    }
    
    public void addStaticMethod(final String clazz, final String methodname, final String d1, final String d2) {
        final String classname = fullSpecified(clazz);
        final MethodType lookup = MethodTypes.toTypeNameMap(d1, imports);
        final MethodType type = d2 == null ? lookup : MethodTypes.toTypeNameMap(d2, imports);
        final boolean alreadyMapped = Optional.of(methods)
                .map((m) -> m.get(classname))
                .map((m) -> m.get(methodname))
                .map((m) -> m.get(type.parameterCount())).map(List::stream)
                .map((s) -> s.anyMatch((mh) -> MethodTypes.identicalPType(mh.type(), type)))
                .filter((b) -> b).isPresent();
        if (!alreadyMapped) {
            final Collection<MethodHandle> mhs = methods.computeIfAbsent(classname, (s) -> new HashMap<>())
                    .computeIfAbsent(methodname, (s) -> new HashMap<>(2))
                    .computeIfAbsent(type.parameterCount(), (i) -> new ArrayList<>(1));
            final MethodHandle mh;
            try {
                mh = MethodHandles.publicLookup().findStatic(clazz(classname), methodname, lookup);
            } catch (final NoSuchMethodException | IllegalAccessException e) {
                throw new UncheckedReflectiveOperationException(e);
            }
            mhs.add(mh.asType(type));
        }
    }
    
    public void addVirtualMethod(final String clazz, final String methodname, final String d1, final String d2) {
        final String classname = fullSpecified(clazz);
        final MethodType lookup = MethodTypes.toTypeNameMap(d1, imports);
        final MethodType type = d2 == null ? lookup : MethodTypes.toTypeNameMap(d2, imports);
        final boolean alreadyMapped = Optional.of(methods)
                .map((m) -> m.get(classname))
                .map((m) -> m.get(methodname))
                .map((m) -> m.get(type.parameterCount() + 1)).map(List::stream)
                .map((s) -> s.anyMatch((mh) -> MethodTypes.identicalPType(mh.type().dropParameterTypes(0, 1), type)))
                .filter((b) -> b).isPresent();
        if (!alreadyMapped) {
            final Class<?> cl = clazz(classname);
            final MethodHandle mh;
            try {
                mh = MethodHandles.publicLookup().findVirtual(cl, methodname, lookup);
            } catch (final NoSuchMethodException | IllegalAccessException e) {
                throw new UncheckedReflectiveOperationException(e);
            }
            final MethodHandle asType = mh.asType(type.insertParameterTypes(0, cl));
            subclasses.get(cl).forEach((c) -> methods.computeIfAbsent(c.getName(), (s) -> new HashMap<>())
                    .computeIfAbsent(methodname, (s) -> new HashMap<>(2))
                    .computeIfAbsent(type.parameterCount() + 1, (i) -> new ArrayList<>(1)).add(asType));
        }
    }
    
    public Object invoke(final String clazz, final String methodname, final List<?> params) {
        return invoke(clazz, methodname, params.toArray());
    }
    
    public Object invoke(final String clazz, final String methodname, final Object[] params) {
        final Map<Integer, List<MethodHandle>> map = Optional.of(methods)
                .map((m) -> m.get(fullSpecified(clazz)))
                .map((m) -> m.get(methodname)).orElse(null);
        if (map == null) {
            throw new NoSuchElementException("no methodhandle assigned for " + fullSpecified(clazz) + "." + methodname);
        }
        for (int i = params.length; i > 0; i--) {
            final List<MethodHandle> list = map.get(i);
            if (list != null) {
                for (int n = 0, l = list.size(); n < l; n++) {
                    try {
                        return Invocations.invoke(list.get(n), params);
                    } catch (final Throwable e) {
                        // You saw nothing. (Currently have to test every single method-handle due to var-args etc.)
                    }
                }
            }
        }
        throw new IllegalStateException("can not invoke " + fullSpecified(clazz) + "." + methodname
                + Arrays.stream(params).map((o) -> o == null ? "null" : "(" + o.getClass().getSimpleName() + ") " + o)
                        .collect(Collectors.joining(", ", "(", ")"))
                + ".");
    }
    
    @Override
    public String toString() {
        return methods + System.lineSeparator() + subclasses + System.lineSeparator() + imports;
    }
}
