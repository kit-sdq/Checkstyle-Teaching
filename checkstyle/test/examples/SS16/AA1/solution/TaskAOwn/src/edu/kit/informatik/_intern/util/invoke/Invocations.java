package edu.kit.informatik._intern.util.invoke;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import edu.kit.informatik._intern.util.collections.Cache;

/**
 * Contains methods to invoke method-handles.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/06/20
 */
public final class Invocations {
    
    private static final Cache<MethodType, MethodHandle> MHS = new Cache<>();
    
    private Invocations() {
        
    }
    
    /**
     * Tries to bind the given method-handle with the given parameters. If any of the provided parameter is not usable
     * as the specific parameter for the method-handle, this method will try to find an option to convert the provided
     * parameter to the correct type.
     * 
     * <p>The count of parameters has to be equal or less than the count of by the method-handle required parameters.
     * 
     * @param  methodhandle the method-handle
     * @param  parameters the parameters
     * @return the bound method-handle
     * @throws IllegalArgumentException if {@code param.size()} is greater than the parameter count of the method-handle
     * @throws InvocationException if anything is thrown while binding
     * @see    #bind(MethodHandle, Object...)
     */
    public static MethodHandle bind(final MethodHandle methodhandle, final List<?> parameters) {
        // LOWPRIO allow n -> m parameter count
        parameterCountMaximal(parameters.size(), methodhandle.type().parameterCount());
        return MethodHandles.insertArguments(methodhandle, 0, IntStream.range(0, parameters.size())
                .mapToObj((i) -> invoke0(methodhandle.type().wrap().parameterType(i), parameters.get(i))).toArray());
    }
    
    /**
     * Tries to bind the given method-handle with the given parameters. If any of the provided parameter is not usable
     * as the specific parameter for the method-handle, this method will try to find an option to convert the provided
     * parameter to the correct type.
     * 
     * <p>The count of parameters has to be equal or less than the count of by the method-handle required parameters.
     * 
     * @param  methodhandle the method-handle
     * @param  parameters the parameters
     * @return the bound method-handle
     * @throws IllegalArgumentException if {@code param.length} is greater than the parameter count of the method-handle
     * @throws InvocationException if anything is thrown while binding
     */
    public static MethodHandle bind(final MethodHandle methodhandle, final Object... parameters) {
        // LOWPRIO allow n -> m parameter count
        parameterCountMaximal(parameters.length, methodhandle.type().parameterCount());
        return MethodHandles.insertArguments(methodhandle, 0, IntStream.range(0, parameters.length)
                .mapToObj((i) -> invoke0(methodhandle.type().wrap().parameterType(i), parameters[i])).toArray());
    }
    
    /**
     * Tries to invoke the given method-handle with the given parameters. If any of the provided parameter is not usable
     * as the specific parameter for the method-handle, this method will try to find an option to convert the provided
     * parameter to the correct type.
     * 
     * <p>The count of parameters has to match the count of by the method-handle required parameters.
     * 
     * @param  methodhandle the method-handle
     * @param  parameters the parameters
     * @return the result of the invoke
     * @throws IllegalStateException if the method-handle can not be invoked with the parameters
     * @throws InvocationException if anything is thrown by the target method invocation
     * @see    #invoke(MethodHandle, Object...)
     */
    public static Object invoke(final MethodHandle methodhandle, final List<?> parameters) {
        return invoke(methodhandle, parameters.toArray()); // LOWPRIO Remove list->array
    }
    
    /**
     * Tries to invoke the given method-handle with the given parameters. If any of the provided parameter is not usable
     * as the specific parameter for the method-handle, this method will try to find an option to convert the provided
     * parameter to the correct type.
     * 
     * <p>The count of parameters has to match the count of by the method-handle required parameters.
     * 
     * @param  methodhandle the method-handle
     * @param  parameters the parameters
     * @return the result of the invoke
     * @throws IllegalStateException if the method-handle can not be invoked with the parameters
     * @throws InvocationException if anything is thrown by the target method invocation
     */
    public static Object invoke(final MethodHandle methodhandle, final Object... parameters) {
        // LOWPRIO Add n -> m parameter count support?
        // LOWPRIO Fix this (working but ugly)
        final MethodType t = methodhandle.type();
        final int pcdec = t.parameterCount() - 1;
        if (parameters.length >= pcdec - 1) {
            if (t.parameterCount() > 0 && t.parameterType(pcdec).isArray()) {
                final Class<?> ct = t.parameterType(pcdec).getComponentType();
                return invoke0(methodhandle, IntStream.range(0, parameters.length).mapToObj((i) -> invoke0(i < pcdec
                                ? t.wrap().parameterType(i) : Conversion.wrap(ct), parameters[i])).toArray());
            } else if (parameters.length == methodhandle.type().parameterCount()) {
                return invoke0(methodhandle, IntStream.range(0, parameters.length)
                        .mapToObj((i) -> invoke0(t.wrap().parameterType(i), parameters[i])).toArray());
            }
        }
        throw new IllegalStateException("can not invoke " + methodhandle + " with " + Arrays.toString(parameters));
    }
    
    /**
     * Tries to convert the provided argument to an object of class {@code rtype}.
     * 
     * @param  rtype the class to convert to
     * @param  parameter the parameter to convert
     * @return {@code parameter} converted to an object of class {@code rtype}
     */
    public static Object convert(final Class<?> rtype, final Object parameter) {
        return invoke0(rtype, parameter);
    }
    
    private static Object invoke0(final Class<?> rtype, final Object param) {
        if (rtype.isInstance(param)) {
            return param;
        }
        final MethodType mt = MethodType.methodType(rtype, param.getClass());
        final MethodHandle mh = MHS.get(mt);
        return invoke0(mh != null ? mh : MHS.putIfAbsent(mt, ConverterMethods.methodHandleForType(mt)), param);
    }
    
    private static Object invoke0(final MethodHandle mh, final Object param) {
        assert mh.type().parameterCount() == 1;
        try {
            return mh.invoke(param);
        } catch (final Throwable e) {
            throw new InvocationException(mh, new Object[] {param}, e);
        }
    }
    
    private static Object invoke0(final MethodHandle mh, final Object[] params) {
        assert mh.type().parameterCount() == params.length;
        try {
            return mh.invokeWithArguments(params);
        } catch (final Throwable e) {
            throw new InvocationException(mh, params, e);
        }
    }
    
    private static void parameterCountMaximal(final int provided, final int required) {
        if (provided > required) {
            throw new IllegalArgumentException("invalid parameter count"
                    + ", provided " + provided + ", required maximal " + required);
        }
    }
}
