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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;
import java.util.stream.Stream;

final class TypeUtil {

    private TypeUtil() {}

    static void resolveTypeVariables(Class<?> clazz, Class<?> root, Map<TypeVariable<?>, Type> map) {
        new Object() {

            void resolve(Class<?> cl) {
                if (root.isInterface())
                    toInterface(cl);
                else
                    toClass(cl);
            }

            private void toClass(Class<?> cl) {
                Class<?> type;
                for (Type t = type = cl; root.isAssignableFrom(type); t = type.getGenericSuperclass()) {
                    if (t == null)
                        return;
                    type = (Class<?>) (t instanceof Class ? t : ((ParameterizedType) t).getRawType());
                    resolveSpecificType(map, t);
                }
            }

            private void toInterface(Class<?> cl) {
                Stream.concat(
                        Stream.of(cl.getGenericSuperclass()).filter(type -> type != null),
                        Stream.of(cl.getGenericInterfaces())).forEach(t -> {
                    final Class<?> type = (Class<?>) (t instanceof Class ? t : ((ParameterizedType) t).getRawType());
                    if (root.isAssignableFrom(type)) {
                        resolveSpecificType(map, t);
                        toInterface(type);
                    }
                });
            }
        }.resolve(clazz);
    }

    static void resolveSpecificType(Map<TypeVariable<?>, Type> map, Type type) {
        for (Type t = type; t instanceof ParameterizedType;) {
            final ParameterizedType pt = (ParameterizedType) t;
            @SuppressWarnings("unchecked")
            final TypeVariable<Class<?>>[] param = (TypeVariable<Class<?>>[]) (TypeVariable<?>[])
            ((Class<?>) pt.getRawType()).getTypeParameters();
            final Type[] arg = pt.getActualTypeArguments();
            for (int i = 0; i < arg.length; i++)
                map.put(param[i], map.getOrDefault(arg[i], arg[i]));
            t = pt.getOwnerType();
        }
    }
}
