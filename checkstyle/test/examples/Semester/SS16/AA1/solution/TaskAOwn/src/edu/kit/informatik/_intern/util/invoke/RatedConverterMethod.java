package edu.kit.informatik._intern.util.invoke;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

abstract class RatedConverterMethod {
    
    private final int rating;
    
    private RatedConverterMethod(final int rating) {
        this.rating = rating;
    }
    
    public static MethodHandle methodHandle(final MethodType type) {
        return Stream.concat(ratedMethods(type), ratedConstructors(type)).reduce((a, b) -> a.rating >= b.rating ? a : b)
                .filter((r) -> r.rating >= 0).map(RatedConverterMethod::unreflect)
                .orElseThrow(() -> new NoSuchElementException("no matching method handle found for " + type));
    }
    
    private static Stream<RatedConverterMethod> ratedMethods(final MethodType type) {
        final Stream<Class<?>> rtype  = Stream.of(type).unordered().map(MethodType::wrap).map(MethodType::returnType);
        final Stream<Class<?>> ptypes = IntStream.range(0, type.parameterCount()).mapToObj(type.wrap()::parameterType);
        return Stream.concat(rtype, ptypes).distinct().map(Class::getMethods).flatMap(Arrays::stream)
                .map((m) -> RatedConverterMethod.of(m, type));
    }
    
    private static Stream<RatedConverterMethod> ratedConstructors(final MethodType type) {
        return Stream.of(type).unordered().map(MethodType::wrap).map(MethodType::returnType)
                .filter((clazz) -> !Modifier.isAbstract(clazz.getModifiers()))
                .map(Class::getConstructors).flatMap(Arrays::stream)
                .map((c) -> RatedConverterMethod.of(c, type));
    }
    
    public static RatedConverterMethod of(final Constructor<?> constructor, final MethodType type) {
        return new RatedConverterMethod(rateSignature(constructor, type)) {
            
            @Override protected MethodHandle unreflect0() throws IllegalAccessException {
                return MethodHandles.publicLookup().unreflectConstructor(constructor);
            }
        };
    }
    
    public static RatedConverterMethod of(final Method method, final MethodType type) {
        return new RatedConverterMethod(rateSignature(method, type)) {
            
            @Override protected MethodHandle unreflect0() throws IllegalAccessException {
                return MethodHandles.publicLookup().unreflect(method);
            }
        };
    }
    
    public final MethodHandle unreflect() {
        try {
            return unreflect0();
        } catch (final IllegalAccessException e) {
            throw new UncheckedReflectiveOperationException(e);
        }
    }
    
    protected abstract MethodHandle unreflect0() throws IllegalAccessException;
    
    private static int rateSignature(final Constructor<?> constructor, final MethodType type) {
        if (Conversion.noPrecisionLoss(type.returnType(), constructor.getDeclaringClass())) {
            final int rating = rateParam(constructor, type);
            if (rating >= 0) {
                return rating + 256 * 5;
            }
        }
        return -1;
    }
    
    private static int rateSignature(final Method method, final MethodType type) {
        if (Modifier.isStatic(method.getModifiers())) {
            if (Conversion.noPrecisionLoss(type.returnType(), method.getReturnType())) {
                final int rating = rateParam(method, type);
                if (rating >= 0) {
                    return rating + Rating.rateName(method) * 256;
                }
            }
        } else {
            if (method.getParameterCount() == type.parameterCount() - 1
                    && Conversion.noPrecisionLoss(type.returnType(), method.getReturnType())
                    && Conversion.noPrecisionLoss(method.getDeclaringClass(), type.parameterType(0))) {
                final int rating = rateParameter(method, type.dropParameterTypes(0, 1));
                if (rating >= 0) {
                    return rating + 1 + Rating.rateName(method) * 256;
                }
            }
        }
        return -1;
    }
    
    private static final class Rating {
        
        private static final UnaryOperator<String> REPLACE_ARRAY;
        private static final List<String> NAMES = Collections
                .unmodifiableList(Arrays.asList("of", "valueOf", "instance", "getInstance", "to", "parse", "from"));
        static {
            final Pattern array = Pattern.compile("\\[\\]+");
            REPLACE_ARRAY = (s) -> array.matcher(s).replaceAll("array");
        }
        
        private static int rateName(final Method method) {
            int rating = 0;
            if (!Modifier.isStatic(method.getModifiers())) {
                rating += 1;
            }
            if (isConvertName(method) || isConvertNameWithRType(method) || isConvertNameWithPType(method)) {
                rating += 9;
            }
            if (method.getDeclaringClass().isEnum() && method.getName().equals("valueOf")) {
                rating -= 1;
            }
            return rating;
        }
        
        private static boolean isConvertName(final Method method) {
            return NAMES.stream().anyMatch(method.getName()::equalsIgnoreCase);
        }
        
        private static boolean isConvertNameWithRType(final Method method) {
            final String rtypeName = REPLACE_ARRAY.apply(method.getReturnType().getSimpleName());
            return NAMES.stream().map((s) -> s.concat(rtypeName)).anyMatch(method.getName()::equalsIgnoreCase);
        }
        
        private static boolean isConvertNameWithPType(final Method method) {
            if (method.getParameterCount() != 1) {
                return false;
            }
            final String ptypeName = REPLACE_ARRAY.apply(method.getParameterTypes()[0].getSimpleName());
            return NAMES.stream().map((s) -> s.concat(ptypeName)).anyMatch(method.getName()::equalsIgnoreCase);
        }
    }
    
    private static int rateParam(final Executable executable, final MethodType type) {
        final int ec = executable.getParameterCount();
        final int tc = type.parameterCount();
        return executable.isVarArgs()
                ? ec <= tc ? rateParameterVarArgs(executable, type) : -1
                : ec == tc ? rateParameter(executable, type) : -1;
    }
    
    private static int rateParameterVarArgs(final Executable executable, final MethodType type) {
        assert executable.getParameterCount() <= type.parameterCount() && executable.isVarArgs();
        final Class<?>[] parameters = executable.getParameterTypes();
        final Class<?> var = parameters[parameters.length - 1].getComponentType();
        int rating = 0;
        for (int i = type.parameterCount() - executable.getParameterCount(); i < type.parameterCount(); i++) {
            final Class<?> a = var;
            final Class<?> b = type.parameterType(i);
            final int crRating = Conversion.wrapper(a, b) ? 1 : Conversion.noPrecisionLoss(a, b) ? 0 : -1;
            if (crRating < 0) {
                return crRating;
            }
            rating += crRating;
        }
        for (int i = 0; i < executable.getParameterCount() - 1; i++) {
            final Class<?> a = parameters[i];
            final Class<?> b = type.parameterType(i);
            final int crRating = Conversion.wrapper(a, b) ? 1 : Conversion.noPrecisionLoss(a, b) ? 0 : -1;
            if (crRating < 0) {
                return crRating;
            }
            rating += crRating;
        }
        return rating;
    }
    
    private static int rateParameter(final Executable executable, final MethodType type) {
        assert executable.getParameterCount() == type.parameterCount();
        final Class<?>[] parameters = executable.getParameterTypes();
        int rating = 0;
        for (int i = 0; i < type.parameterCount(); i++) {
            final Class<?> a = parameters[i];
            final Class<?> b = type.parameterType(i);
            final int crRating = Conversion.wrapper(a, b) ? 1 : Conversion.noPrecisionLoss(a, b) ? 0 : -1;
            if (crRating < 0) {
                return crRating;
            }
            rating += crRating;
        }
        return rating;
    }
}