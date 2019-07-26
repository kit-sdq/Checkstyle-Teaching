package edu.kit.informatik._intern.util.invoke;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import edu.kit.informatik._intern.util.invoke.Conversion.Convertible;
import edu.kit.informatik._intern.util.invoke.Conversion.Precision;

/**
 * Class containing methods regarding converter methods.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/06/20
 */
public final class ConverterMethods {
    
    private static final Precision DEFAULT_PRECISION = Precision.SAFE;
    
    private ConverterMethods() { }
    
    /**
     * Returns a method-handle for the provided method-type.
     * 
     * @param  type the method-type
     * @return a method-handle for {@code type}
     */
    public static MethodHandle methodHandleForType(final MethodType type) {
        return RatedConverterMethod.methodHandle(type);
    }
    
    /**
     * Returns a stream containing methods of the specific method-type.
     * 
     * <p>This returns all matching methods of classes, that are part of the method-type (either return-type or
     * parameter-type).
     * 
     * @param  type the method-type
     * @return a stream containing methods of the specific method-type
     */
    public static Stream<Method> methodsWithType(final MethodType type) {
        final Stream<Class<?>> rtype  = Stream.of(type).unordered().map(MethodType::wrap).map(MethodType::returnType);
        final Stream<Class<?>> ptypes = IntStream.range(0, type.parameterCount()).mapToObj(type.wrap()::parameterType);
        return Stream.concat(rtype, ptypes).distinct().map(Class::getMethods).flatMap(Arrays::stream)
                .filter((m) -> matchingSignature(m, type));
    }
    
    /**
     * Returns a stream containing constructors of the specific method-type.
     * 
     * <p>This returns all matching constructors in the return-type-class (does not search in sub-classes) of the
     * method-type. If the return-type class is abstract, an empty stream will be returned.
     * 
     * @param  type the method-type
     * @return a stream containing constructors of the specific method-type
     */
    public static Stream<Constructor<?>> constructorsWithType(final MethodType type) {
        return Stream.of(type).unordered().map(MethodType::wrap).map(MethodType::returnType)
                .filter((clazz) -> !Modifier.isAbstract(clazz.getModifiers()))
                .map(Class::getConstructors).flatMap(Arrays::stream).filter((c) -> matchingSignature(c, type));
    }
    
    /**
     * Returns whether the given constructor matches to given method-type.
     * 
     * @param  constructor the constructor
     * @param  type the method-type
     * @return {@code true} if the method-type is a valid representation of the constructor, {@code false} otherwise
     */
    public static boolean matchingSignature(final Constructor<?> constructor, final MethodType type) {
        return Conversion.get(type.returnType()).assignableFrom(constructor.getDeclaringClass(), DEFAULT_PRECISION)
                && matchingParam(constructor, type);
    }
    
    /**
     * Returns whether the given method matches to given method-type.
     * 
     * @param  method the method
     * @param  type the method-type
     * @return {@code true} if the method-type is a valid representation of the method, {@code false} otherwise
     */
    public static boolean matchingSignature(final Method method, final MethodType type) {
        return Conversion.get(type.returnType()).assignableFrom(method.getReturnType(), DEFAULT_PRECISION)
                && (Modifier.isStatic(method.getModifiers())
                        ? matchingParam(method, type)
                        : matchingParam(method, type.dropParameterTypes(0, 1)));
    }
    
    private static boolean matchingParam(final Executable executable, final MethodType type) {
        return executable.isVarArgs()
                ? matchingParameterVarArgs(executable, type)
                : matchingParameter(executable, type);
    }
    
    private static boolean matchingParameterVarArgs(final Executable executable, final MethodType type) {
        final int ec = executable.getParameterCount();
        final int tc = type.parameterCount();
        
        if (tc + 1 < ec)
            return false;
        
        final Class<?>[] params = executable.getParameterTypes();
        if (tc >= ec) {
            final Convertible var = Conversion.get(params[params.length - 1].getComponentType());
            if (!IntStream.range(type.parameterCount() - executable.getParameterCount(), type.parameterCount())
                    .mapToObj(type::parameterType).allMatch((cl) -> var.assignableFrom(cl, DEFAULT_PRECISION)))
                return false;
        }
        return IntStream.range(0, ec - 1)
                .allMatch((i) -> Conversion.get(params[i]).assignableFrom(type.parameterType(i), DEFAULT_PRECISION));
    }
    
    private static boolean matchingParameter(final Executable executable, final MethodType type) {
        final int ec = executable.getParameterCount();
        final int tc = type.parameterCount();
        
        if (tc != ec)
            return false;
        
        final Class<?>[] params = executable.getParameterTypes();
        return IntStream.range(0, type.parameterCount())
                .allMatch((i) -> Conversion.get(params[i]).assignableFrom(type.parameterType(i), DEFAULT_PRECISION));
    }
}
