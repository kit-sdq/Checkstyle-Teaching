package edu.kit.informatik.util.executable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The class {@code Parser} is used to convert a string parameter to a {@code SymbolMap}.
 * 
 * @author  Tobias Bachert
 * @version 1.07, 2016/07/12
 * 
 * @see     Symbol
 * @see     SymbolMap
 * 
 * @since   1.8
 */
public final class Parser2 {
    
    private final Map<Class<?>, Function<? super String, ?>> functions;
    private final Map<Symbol<?>, String> symbols;
    private final Pattern pattern;
    
    private Parser2(final Map<Class<?>, Function<? super String, ?>> functions, final Map<Symbol<?>, String> symbols,
            final Pattern pattern) {
        this.functions = functions;
        this.symbols = symbols;
        this.pattern = pattern;
    }
    
    /**
     * Returns a new {@code Builder} for a {@code Parser}.
     * 
     * @return a new {@code Builder}
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * Returns the input-string converted to a {@code SymbolMap}.
     * 
     * @param  s the input-string
     * @return an {@code Optional} holding the symbol-map, or an empty {@code Optional} if the input-string does not
     *         match the format of this parser
     */
    public Optional<SymbolMap> toSymbolMap(final String s) {
        final Matcher m = pattern.matcher(s);
        if (m.matches()) {
            final Map<Symbol<?>, Object> parsed = new HashMap<>();
            for (final Map.Entry<Symbol<?>, String> entry : symbols.entrySet()) {
                final Symbol<?> symbol = entry.getKey();
                parsed.put(symbol, functions.get(symbol.clazz).apply(m.group(entry.getValue())));
            }
            return Optional.of(new SymbolMap(parsed));
        }
        return Optional.empty();
    }
    
    /**
     * Builder class for a {@code Parser}.
     * 
     * @author  Tobias Bachert
     * @version 1.03, 2016/07/11<sub>simplified version</sub>
     */
    public static final class Builder {
        
        private final Map<Class<?>, Function<? super String, ?>> functions = new HashMap<>();
        private final Map<Symbol<?>, String> symbols = new HashMap<>();
        
        private Builder() {
            
        }
        
        /**
         * Adds the symbols of the look-up class of the given {@code Lookup} to the parser-builder.
         * 
         * <p>Fields that can not be accessed by the given {@code Lookup} are ignored thus it is advised to pass a
         * {@code Lookup} with full capabilities.
         * 
         * @param  lookup the {@code Lookup}
         * @return a reference to {@code this} builder
         */
        public Builder add(final Lookup lookup) {
            final ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Arrays.stream(lookup.lookupClass().getDeclaredFields())
                    .filter((field) -> (field.getModifiers() & 24) == 8)
                    .filter((field) -> (Integer.lowestOneBit(field.getModifiers() | 8) & lookup.lookupModes()) != 0)
                    .filter((field) -> Symbol.class.isAssignableFrom(field.getType()))
                    .forEach((field) -> add(lookup, field, loader));
            return this;
        }
        
        /**
         * Adds the given symbol field which has to be accessible with the given lookup to the parser-builder.
         * 
         * <p><b>Implementation Note:</b><br>
         * This does <b>not</b> fail fast, which means in detail, that a symbol without the option to convert a string
         * to the class of the symbol will not raise an exception here but when converting an input-string.
         * 
         * @param lookup the {@code Lookup}
         * @param field the {@code Field}
         * @param loader the class-loader used to get the class of the generic
         */
        private void add(final Lookup lookup, final Field field, final ClassLoader loader) {
            synchronized (field) {
                final MethodHandle getter;
                try {
                    getter = lookup.unreflectGetter(field);
                } catch (final IllegalAccessException e) {
                    throw new AssertionError(e);
                }
                final Symbol<?> current = (Symbol<?>) invoke(getter);
                final String name = field.getName().substring(1).toLowerCase(Locale.ENGLISH);
                if (current != null) {
                    synchronized (symbols) {
                        if (!name.equals(symbols.computeIfAbsent(current, (s) -> s.name))) {
                            throw new IllegalStateException(current + " assigned to multiple fields");
                        }
                    }
                } else {
                    final Symbol<?> symbol;
                    synchronized (symbols) {
                        if (symbols.values().stream().anyMatch(name::equals)) {
                            throw new IllegalStateException("same identifier '" + name + "' for different symbols");
                        }
                        final Type t = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                        final Type raw = t instanceof ParameterizedType ? ((ParameterizedType) t).getRawType() : t;
                        final Class<?> genericClass;
                        try {
                            genericClass = Class.forName(raw.getTypeName(), false, loader);
                        } catch (final ClassNotFoundException e) {
                            throw new TypeNotPresentException(raw.getTypeName(), e);
                        }
                        symbol = new Symbol<>(name, genericClass);
                        symbols.put(symbol, name);
                    }
                    final MethodHandle setter;
                    try {
                        setter = lookup.unreflectSetter(field);
                    } catch (final IllegalAccessException e) {
                        throw new AssertionError(e);
                    }
                    invoke(setter, symbol);
                }
            }
        }
        
        private Object invoke(final MethodHandle mh, final Object... args) {
            try {
                return mh.invokeWithArguments(args);
            } catch (final Throwable e) {
                throw new InvocationException(mh, args, e);
            }
        }

        /**
         * Exception used to indicate that the invocation of a method-handle threw an exception.
         * 
         * @author  Tobias Bachert
         * @version 1.00, 2016/06/19
         */
        private static final class InvocationException extends RuntimeException {
            
            private static final long serialVersionUID = -6162901942097650506L;
            
            /**
             * Creates a new {@code InvocationException} with the provided parameters.
             * 
             * @param methodhandle the method-handle that threw an exception on invocation
             * @param parameters used for the invocation
             * @param e the exception thrown
             */
            public InvocationException(final MethodHandle methodhandle, final Object[] parameters, final Throwable e) {
                super("exception thrown while invoking " + methodhandle + " with " + Arrays.toString(parameters), e);
            }
        }
        
        /**
         * Adds a parse-function to the builder.
         * 
         * @param  <T> type parameter of the class and function
         * @param  clazz the clazz to use the parse function for
         * @param  function the parse function
         * @return a reference to {@code this} builder
         * @throws IllegalStateException if a parse function for {@code clazz} is already assigned
         */
        public <T> Builder add(final Class<T> clazz, final Function<? super String, T> function) {
            if (functions.putIfAbsent(clazz, function) != null) {
                throw new IllegalStateException("parse function for " + clazz + " already assigned");
            }
            return this;
        }
        
        /**
         * Builds a parser using the given format-string.
         * 
         * <p>The identifier of a symbol is used for the format-strings and has to be highlighted by a leading '&lt;'
         * and a trailing '&gt;', thus a possible format-string containing the two symbols
         * <blockquote><pre>
         * Symbol&lt;String&gt;  sNAME;
         * Symbol&lt;Integer&gt; sVALUE;</pre>
         * </blockquote>
         * could be represented by a format-string with the syntax:
         * <blockquote><pre>
         * &lt;name&gt;someDelimiter&lt;value&gt;abc</pre>
         * </blockquote>
         * A symbol-map of a parser {@code p} created with this format-string would return for example:
         * <blockquote><pre>
         * SymbolMap args = p.toSymbolMap("theNamesomeDelimiter57abc").get();
         * args.get(sNAME);  // String "theName"
         * args.get(sVALUE); // Integer 57</pre>
         * </blockquote>
         * 
         * @param  format the format-string to use
         * @return a parser for the given format-string
         */
        public Parser2 build(final String format) {
            final StringBuilder pattern = new StringBuilder(format);
            final Map<Symbol<?>, String> formatSymbols = new HashMap<>();
            synchronized (symbols) {
                symbols.forEach((symbol, name) -> {
                    final int index = pattern.indexOf('<' + name + '>');
                    if (index != -1) {
                        pattern.replace(index, index + name.length() + 2, "(?<" + name + ">.+?)");
                        formatSymbols.put(symbol, name);
                    }
                });
            }
            return new Parser2(functions, formatSymbols, Pattern.compile(pattern.toString()));
        }
    }
    
    /**
     * Symbol-class used for a symbol-map.
     * 
     * <p>By convention the field-name of a symbol has to start with a lower case '{@code s}' followed by the symbol
     * identifier in upper case.<br>
     * For example, a string-symbol with the identifier {@code name} has to be defined as following
     * <blockquote><pre>
     * private static Symbol&lt;String&gt; sNAME;</pre>
     * </blockquote>
     * The identifier of the symbol is used for the format-strings, thus the symbol {@code sName} would be
     * represented by
     * <blockquote><pre>
     * &lt;name&gt;</pre>
     * </blockquote>
     * 
     * <p>Symbol-fields have to be static and not final. Although any access-modifier is permitted, it is advised to use
     * private symbol-fields. The type-parameter has to be a specific (public accessible) class and may never be another
     * generic. The reference of an initialized Symbol-field may never be assigned to another field.
     * 
     * @author  Tobias Bachert
     * @version 1.01, 2016/06/12
     * 
     * @param   <T> type parameter
     */
    public static final class Symbol<T> {
        
        private final String name;
        private final Class<T> clazz;
        
        private Symbol(final String name, final Class<T> clazz) {
            this.name = name;
            this.clazz = clazz;
        }
        
        @Override public String toString() {
            return "Symbol[" + name + "]";
        }
    }
    
    /**
     * A symbol-map that holds values for specific symbols.
     * 
     * @author  Tobias Bachert
     * @version 1.01, 2016/06/12
     * 
     * @see     Symbol
     */
    public static final class SymbolMap {
        
        private static final Object NOT_MAPPED = new Object();
        private final Map<Symbol<?>, Object> map;
        
        private SymbolMap(final Map<Symbol<?>, Object> map) {
            this.map = map;
        }
        
        /**
         * Returns whether the given symbol is assigned in the map and thus its value can be obtained using the {@link
         * #get(Symbol)} method.
         * 
         * @param  symbol the symbol
         * @return {@code true} if {@code symbol} is present, {@code false} otherwise
         */
        public boolean contains(final Symbol<?> symbol) {
            return map.containsKey(symbol);
        }
        
        /**
         * Returns the argument assigned to the given symbol.
         * 
         * @param  <T> type parameter
         * @param  symbol the symbol
         * @return the argument assigned to {@code symbol}
         * @throws NoSuchSymbolException if the given symbol is not assigned in this symbol-map
         * @see    #contains(Symbol)
         */
        public <T> T get(final Symbol<T> symbol) {
            final Object o = map.getOrDefault(symbol, NOT_MAPPED);
            if (o == NOT_MAPPED) {
                throw new NoSuchSymbolException(symbol);
            }
            @SuppressWarnings("unchecked")
            final T t = (T) o;
            return t;
        }
        
        @Override
        public String toString() {
            final StringJoiner sj = new StringJoiner(", ");
            map.forEach((s, o) -> sj.add("'" + s.name + "'=[" + o.getClass().getSimpleName() + ":" + o + "]"));
            return sj.toString();
        }
        
        /**
         * Thrown by a {@code SymbolMap} when a not mapped {@code Symbol} is requested using the {@link
         * SymbolMap#get(Symbol)} method.
         * 
         * @author  Tobias Bachert
         * @version 1.00, 2016/06/12
         */
        @SuppressWarnings("serial")
        public static final class NoSuchSymbolException extends NoSuchElementException {
            
            private NoSuchSymbolException(final Symbol<?> symbol) {
                super(symbol + " not mapped");
            }
        }
    }
}
