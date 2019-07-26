package edu.kit.informatik._intern.util.invoke;

import java.lang.invoke.MethodType;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * Class containing methods regarding method-types.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/06/13
 */
public final class MethodTypes {
    
    private static final Pattern PATTERN = Pattern.compile("((?<=\\(|\\)|;|\\G)|,)\\h*"
            + "([a-z]{3,7}|([a-z_][a-zA-Z0-9_]*\\.)+([A-Z][a-zA-Z0-9_]*\\$)*[A-Z][a-zA-Z0-9_]*|([A-Z][a-zA-Z0-9_]*\\$)*"
            + "[A-Z][a-zA-Z0-9_]+)\\h*((\\[\\])*(\\.{3})?)\\h*(?=(\\)|,|\\h|$))");
    
    private MethodTypes() {
        
    }
    
    /**
     * Returns whether the second method-type has assignable parameter-types.
     * 
     * @param  a the first method type
     * @param  b the second method type
     * @return {@code true} if each parameter of {@code b} is assignable to the belonging parameter of {@code a}, {@code
     *         false} otherwise
     */
    public static boolean assignablePType(final MethodType a, final MethodType b) {
        return a.parameterCount() == b.parameterCount()
                && IntStream.range(0, a.parameterCount())
                        .allMatch((i) -> Conversion.noPrecisionLoss(a.parameterType(i), b.parameterType(i)));
    }
    
    /**
     * Returns whether two method-types have identical parameter-types. Primitives and their wrapper-classes are treated
     * as equal.
     * 
     * @param  a the first method type
     * @param  b the second method type
     * @return {@code true} if the provided method-types have identical parameter-types, {@code false} otherwise
     */
    public static boolean identicalPType(final MethodType a, final MethodType b) {
        return a.parameterCount() == b.parameterCount()
                && IntStream.range(0, a.parameterCount())
                        .allMatch((i) -> Conversion.wrapper(a.parameterType(i), b.parameterType(i)));
    }
    
    /**
     * Returns a method-type for the provided string as described by the {@link
     * MethodType#fromMethodDescriptorString(String, ClassLoader)} method.
     *
     * <p>If the descriptor string is written java-like, then primitive types, their wrappers, Object and String do
     * not require the fully specified class-name.
     *
     * <p>Both of the following descriptor strings would be valid and result in equal method-types
     * <blockquote><pre>
     * (ILjava/lang/String;Ljava/lang/Object;)Z
     * (int, String, Object)boolean</pre>
     * </blockquote>
     *
     * <p>The return-type {@code void} may be omitted when using java-level descriptors.
     *
     * <p>The result of a mixture of bytecode-level and java-level is undefined.
     *
     * @param  descriptor a type descriptor, <b>either</b> bytecode-level <b>or</b> java-level
     * @return a {@code MethodType} matching the given string
     * @throws IllegalArgumentException if the string is not well-formed or if a named type cannot be found
     * @see    #toTypeNameMap(String, Map)
     */
    public static MethodType toType(final String descriptor) {
        return toType(descriptor, Collections.emptyMap(), Function.identity());
    }
    
    /**
     * Returns a method-type for the provided string as described by the {@link
     * MethodType#fromMethodDescriptorString(String, ClassLoader)} method.
     * 
     * <p>If the descriptor string is written java-like, then primitive types and the classes of the {@code java.lang}
     * package do not require their fully specified class-name.
     * 
     * <p>Both of the following descriptor strings would be valid and result in equal method-types
     * <blockquote><pre>
     * (ILjava/lang/String;Ljava/lang/Object;)Z
     * (int, String, Object)boolean</pre>
     * </blockquote>
     * 
     * <p>The return-type {@code void} may be omitted when using java-level descriptors.
     * 
     * <p>The result of a mixture of bytecode-level and java-level is undefined.
     * 
     * @param  descriptor a type descriptor, <b>either</b> bytecode-level <b>or</b> java-level
     * @param  map a map containing abbreviations mapped to their class, only used for java-level
     * @return a {@code MethodType} matching the given string
     * @throws IllegalArgumentException if the string is not well-formed or if a named type cannot be found
     */
    public static MethodType toTypeClassMap(final String descriptor, final Map<String, Class<?>> map) {
        return toType(descriptor, map, Class::getName);
    }
    
    /**
     * Returns a method-type for the provided string as described by the {@link
     * MethodType#fromMethodDescriptorString(String, ClassLoader)} method.
     * 
     * <p>If the descriptor string is written java-like, then primitive types and the classes of the {@code java.lang}
     * package do not require their fully specified class-name.
     * 
     * <p>Both of the following descriptor strings would be valid and result in equal method-types
     * <blockquote><pre>
     * (ILjava/lang/String;Ljava/lang/Object;)Z
     * (int, String, Object)boolean</pre>
     * </blockquote>
     * 
     * <p>The return-type {@code void} may be omitted when using java-level descriptors.
     * 
     * <p>The result of a mixture of bytecode-level and java-level is undefined.
     * 
     * @param  descriptor a type descriptor, <b>either</b> bytecode-level <b>or</b> java-level
     * @param  map a map containing abbreviations mapped to their fully specified name, only used for java-level
     * @return a {@code MethodType} matching the given string
     * @throws IllegalArgumentException if the string is not well-formed or if a named type cannot be found
     */
    public static MethodType toTypeNameMap(final String descriptor, final Map<String, String> map) {
        return toType(descriptor, map, Function.identity());
    }
    
    /**
     * Returns a method-type for the provided string as described by the {@link
     * MethodType#fromMethodDescriptorString(String, ClassLoader)} method.
     * 
     * <p>If the descriptor string is written java-like, then primitive types and the classes of the {@code java.lang}
     * package do not require their fully specified class-name.
     * 
     * <p>Both of the following descriptor strings would be valid and result in equal method-types
     * <blockquote><pre>
     * (ILjava/lang/String;Ljava/lang/Object;)Z
     * (int, String, Object)boolean</pre>
     * </blockquote>
     * 
     * <p>The return-type {@code void} may be omitted when using java-level descriptors.
     * 
     * <p>The result of a mixture of bytecode-level and java-level is undefined.
     * 
     * @param  <T> type parameter
     * @param  descriptor a type descriptor, <b>either</b> bytecode-level <b>or</b> java-level
     * @param  map a map containing abbreviations mapped to an object representing the replacement, only used for
     *         java-level
     * @param  function function to convert an object of type {@code T} to the corresponding class name
     * @return a {@code MethodType} matching the given string
     * @throws IllegalArgumentException if the string is not well-formed or if a named type cannot be found
     */
    public static <T> MethodType toType(final String descriptor, final Map<String, ? extends T> map,
            final Function<? super T, String> function) {
        final String result;
        if (descriptor.equals("()")) {
            result = "()V";
        } else {
            final Matcher m = PATTERN.matcher(descriptor);
            if (m.find()) {
                final StringBuilder sb = new StringBuilder(descriptor.length() << 1);
                int pos = 0;
                do {
                    sb.append(descriptor, pos, m.start());
                    pos = m.end();
                    final String arr = m.group(6);
                    for (int i = 0, l = arr.length() >> 1; i < l; i++) {
                        sb.append('[');
                    }
                    final String cl = m.group(2);
                    final Conversion c = Conversion.getForPrimitive(cl);
                    if (c != null) {
                        sb.append(c.primitiveByteDescriptor());
                    } else {
                        final String mapped = Optional.of(cl).map(map::get).map(function::apply).orElse(cl);
                        final String replaced = mapped.replace('.', '/');
                        sb.append('L');
                        if (replaced == mapped) {
                            sb.append("java/lang/");
                        }
                        sb.append(replaced).append(';');
                    }
                } while (m.find());
                sb.append(descriptor, pos, descriptor.length());
                if (sb.charAt(sb.length() - 1) == ')') {
                    sb.append('V');
                }
                result = sb.toString();
            } else {
                result = descriptor;
            }
        }
        try {
            return MethodType.fromMethodDescriptorString(result, null);
        } catch (final IllegalArgumentException | TypeNotPresentException e) {
            throw new IllegalArgumentException("malformed method type descriptor '" + descriptor
                    + "', exception thrown for replaced version '" + result + "'", e);
        }
    }
}
