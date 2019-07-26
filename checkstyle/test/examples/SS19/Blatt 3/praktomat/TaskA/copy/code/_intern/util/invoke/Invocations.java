package edu.kit.informatik._intern.util.invoke;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.stream.IntStream;

import edu.kit.informatik._intern.util.memoizer.Memoizer;

/**
 * Contains methods to invoke method-handles.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/06/20
 */
public final class Invocations {
    
    private static final Memoizer<MethodType, MethodHandle> MHS = new Memoizer<>(ConverterMethods::methodHandleForType);
    
    private Invocations() { }
    
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
        final MethodType t = methodhandle.type();
        final int pcdec = t.parameterCount() - 1;
        if (parameters.length >= pcdec - 1) {
            if (t.parameterCount() > 0 && t.parameterType(pcdec).isArray()) {
                final Class<?> ct = Conversion.wrap(t.parameterType(pcdec).getComponentType());
                return invoke0(methodhandle, IntStream.range(0, parameters.length).mapToObj((i) -> convert(i < pcdec
                        ? t.wrap().parameterType(i) : ct, parameters[i])).toArray());
            } else if (parameters.length == methodhandle.type().parameterCount()) {
                return invoke0(methodhandle, IntStream.range(0, parameters.length)
                        .mapToObj((i) -> convert(t.wrap().parameterType(i), parameters[i])).toArray());
            }
        }
        throw new IllegalStateException("Can not invoke " + methodhandle + " with " + Arrays.toString(parameters));
    }
    
    /**
     * Tries to convert the provided argument to an object of class {@code rtype}.
     * 
     * @param  rtype the class to convert to
     * @param  parameter the parameter to convert
     * @return {@code parameter} converted to an object of class {@code rtype}
     */
    public static Object convert(final Class<?> rtype, final Object parameter) {
        return rtype.isInstance(parameter) ? parameter : MHS.apply(MethodType.methodType(rtype, parameter.getClass()));
    }
    
    private static Object invoke0(final MethodHandle mh, final Object... params) {
        try {
            return mh.invokeWithArguments(params);
        } catch (final Throwable e) {
            throw new InvocationException(mh, params, e);
        }
    }
}
