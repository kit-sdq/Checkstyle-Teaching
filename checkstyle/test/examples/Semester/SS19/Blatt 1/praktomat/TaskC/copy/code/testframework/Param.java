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
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

@Target({METHOD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Repeatable(Param.Params.class)
public @interface Param {

    String[] value();

    @Target({METHOD, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    public @interface Params {

        Param[] value();
    }

    final class Parameters {

        public Object[][] arguments;

        private Parameters(Object[][] arguments) {
            this.arguments = arguments;
        }

        private static final Object[] emptyArg = {};

        public static Parameters of(ArgumentParser parser, Method method) throws Throwable {
            final Param[] p = getParams(method, new HashSet<>());
            if (p.length == 0)
                return new Parameters(new Object[][] {emptyArg});
            final Type[] types = method.getGenericParameterTypes();
            final Object[][] a = new Object[p.length][types.length];
            for (int n = a.length; --n >= 0;) {
                final String[] val = p[n].value();
                final Object[] args = a[n];
                for (int i = args.length; --i >= 0;)
                    args[i] = parser.parse(types[i], val[i]);
            }
            return new Parameters(a);
        }

        private static final Param[] emptyParams = {};
        private static Param[] getParams(AnnotatedElement m, Set<Class<?>> dejaVu) {
            /*
             * Should cache class->Param[]
             */
            Param[] params = emptyParams;
            if (m.isAnnotationPresent(Params.class))
                params = Utils.merge(params, m.getAnnotation(Params.class).value());
            if (m.isAnnotationPresent(Param.class))
                params = Utils.append(params, m.getAnnotation(Param.class));
            for (final Annotation a : m.getAnnotations())
                if (dejaVu.add(a.annotationType()))
                    params = Utils.merge(params, getParams(a.annotationType(), dejaVu));
            return params;
        }
    }
}
