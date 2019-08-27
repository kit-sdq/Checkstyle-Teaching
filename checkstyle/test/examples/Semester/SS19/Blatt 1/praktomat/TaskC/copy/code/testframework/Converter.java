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

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * Utility class to find converter methods.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/08/23
 */
final class Converter {

    private Converter() {}

    /**
     * Returns a string-to-clazz method handle for the provided class.
     * 
     * @param  clazz the class to parse to
     * @return a future that will hold the method handle on completion
     * @throws IllegalStateException if no usable method handle was found for the parsing as {@code clazz}
     */
    public static MethodHandle stringParameter(Class<?> clazz) {
        try {
            // @formatter:off
            return clazz.isPrimitive() ? primitiveType(clazz)
                 : clazz.isArray()     ? arrayType(clazz)
                                       : otherType(clazz);
            // @formatter:on
        } catch (final NoSuchMethodException | IllegalAccessException e) {
            throw new IllegalStateException("Can not find handle for " + clazz + ": " + e.getMessage(), e);
        }
    }

    /**
     * Returns a method handle to convert a string to the provided array type.
     * 
     * @param  cl the array type
     * @return the method handle
     * @throws NoSuchMethodException if no method was found for the parsing as {@code clazz}
     * @throws IllegalAccessException if the found method is not public accessible
     */
    private static MethodHandle arrayType(Class<?> cl) throws NoSuchMethodException, IllegalAccessException {
        assert cl.isArray() : cl;

        final MethodType type = MethodType.methodType(cl, String.class);

        if (cl == char[].class) return MethodHandles.publicLookup().findVirtual(String.class, "toCharArray", type);

        throw new NoSuchMethodException(cl.getName() + ".valueOf(String)" + cl.getSimpleName());
    }

    /**
     * Returns a method handle to convert a string to the provided type.
     * 
     * @param  cl the type
     * @return the method handle
     * @throws NoSuchMethodException if no method was found for the parsing as {@code clazz}
     * @throws IllegalAccessException if the found method is not public accessible
     */
    private static MethodHandle otherType(Class<?> cl) throws NoSuchMethodException, IllegalAccessException {
        assert !cl.isPrimitive() && !cl.isArray() : cl;

        final MethodType type = MethodType.methodType(cl, String.class);

        if (cl.isAssignableFrom(String.class))
            return Identity.STRING.asType(type);

        try {
            return MethodHandles.publicLookup().findStatic(cl, "valueOf", type);
        } catch (final NoSuchMethodException | IllegalAccessException e) {
            try {
                return MethodHandles.publicLookup().findConstructor(cl, void_String);
            } catch (final NoSuchMethodException | IllegalAccessException suppressed) {
                e.addSuppressed(suppressed);
                throw e;
            }
        }
    }

    private static final MethodType void_String = MethodType.methodType(void.class, String.class);

    /**
     * Returns a method handle to convert a string to the provided primitive type.
     * 
     * @param  cl the primitive type
     * @return the method handle
     * @throws NoSuchMethodException will not occur under normal circumstances
     * @throws IllegalAccessException will not occur under normal circumstances
     */
    private static MethodHandle primitiveType(Class<?> cl) throws NoSuchMethodException, IllegalAccessException {
        assert cl.isPrimitive() : cl;

        final MethodType type = MethodType.methodType(cl, String.class);

        // @formatter:off
        if (cl == Byte   .TYPE) return MethodHandles.publicLookup().findStatic(Byte.class   , "parseByte"   , type);
        if (cl == Short  .TYPE) return MethodHandles.publicLookup().findStatic(Short.class  , "parseShort"  , type);
        if (cl == Integer.TYPE) return MethodHandles.publicLookup().findStatic(Integer.class, "parseInt"    , type);
        if (cl == Long   .TYPE) return MethodHandles.publicLookup().findStatic(Long.class   , "parseLong"   , type);
        if (cl == Float  .TYPE) return MethodHandles.publicLookup().findStatic(Float.class  , "parseFloat"  , type);
        if (cl == Double .TYPE) return MethodHandles.publicLookup().findStatic(Double.class , "parseDouble" , type);
        if (cl == Boolean.TYPE) return MethodHandles.publicLookup().findStatic(Boolean.class, "parseBoolean", type);
        // @formatter:on

        if (cl == Character.TYPE) return MethodHandles.lookup().findStatic(Converter.class, "parseChar", type);

        // Primitive type void is not permitted as parameter type
        throw new AssertionError("Unreachable (" + cl + ")");
    }

    /**
     * Converts the string to a char.
     * 
     * @param  string the string to convert
     * @return the char
     * @throws IllegalArgumentException if the length of {@code string} is not exactly {@code 1}
     * @throws NullPointerException if {@code string} is {@code null}
     */
    @SuppressWarnings("unused") // Used as conversion method for primitiveType(Class)
    private static char parseChar(String string) {
        if (string.length() != 1)
            throw new IllegalArgumentException("Can not convert " + string + " to a single character");
        return string.charAt(0);
    }

    /** Class holding identity method handles. */
    private static final class Identity {

        /** Identity method handle of type (String)String. */
        public static final MethodHandle STRING = MethodHandles.identity(String.class);
    }
}
