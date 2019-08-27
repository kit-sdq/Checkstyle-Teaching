package edu.kit.informatik._intern.util.parsing;

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
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.kit.informatik._intern.util.collections.Cache;
import edu.kit.informatik._intern.util.invoke.InvocationException;
import edu.kit.informatik._intern.util.invoke.Invocations;

/**
 * The class {@code Parser} is used to convert a string parameter to a {@code SymbolMap}.
 * 
 * @author  Tobias Bachert
 * @version 1.05, 2016/06/27
 * 
 * @see     Symbol
 * @see     SymbolMap
 * 
 * @since   1.8
 */
public final class Parser {
    
    private final Cache<String, SymbolMap> cache = new Cache<>();
    private final Map<Symbol<?>, String> symbols;
    private final Pattern pattern;
    
    private Parser(final Map<Symbol<?>, String> symbols, final Pattern pattern) {
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
     * @param  input the input-string
     * @return the symbol-map
     * @throws IllegalArgumentException if {@code input} is not {@link #parsable(String)}
     */
    public SymbolMap toSymbolMap(final String input) {
        if (!parsable(input, (e) -> { })) {
            throw new IllegalArgumentException("not parsable");
        }
        final SymbolMap cached = cache.get(input);
        return cached != null ? cached : cache.putIfAbsent(input, toSymbolMap(input, (e) -> { }).get());
    }
    
    /**
     * Returns whether an input-string can be converted using the {@link #toSymbolMap(String)} method.
     * 
     * @param  input the input-string
     * @param  exceptionHandler a {@code Consumer} used when an exception occurs while parsing, the occurred exception
     *         will be provided as arguments
     * @return {@code true} if the input-string can be converted, {@code false} otherwise
     */
    public boolean parsable(final String input, final Consumer<? super Throwable> exceptionHandler) {
        if (cache.get(input) != null) {
            return true;
        }
        final Optional<SymbolMap> opt = toSymbolMap(input, exceptionHandler);
        if (opt.isPresent()) {
            cache.putIfAbsent(input, opt.get());
            return true;
        }
        return false;
    }
    
    /**
     * Returns the input-string converted to a {@code SymbolMap}.
     * 
     * <p>This method fails fast when an exception is thrown while parsing, thus only one exception will be passed to
     * the provided exception-handler prior returning an empty optional.
     * 
     * @param  s the input-string
     * @param  exceptionHandler a {@code Consumer} used when an exception occurs while parsing, the occurred exception
     *         will be provided as arguments
     * @return an {@code Optional} holding the symbol-map, or an empty {@code Optional} if the input-string does not
     *         match the format of this parser
     */
    private Optional<SymbolMap> toSymbolMap(final String s, final Consumer<? super Throwable> exceptionHandler) {
        final Matcher m = pattern.matcher(s);
        if (m.matches()) {
            final Map<Symbol<?>, Object> parsed = new HashMap<>();
            for (final Map.Entry<Symbol<?>, String> entry : symbols.entrySet()) {
                final Symbol<?> symbol = entry.getKey();
                final String val = m.group(entry.getValue());
                try {
                    parsed.put(symbol, Invocations.convert(symbol.clazz, val));
                } catch (final InvocationException e) {
                    exceptionHandler.accept(e.getCause());
                    return Optional.empty();
                }
            }
            return Optional.of(new SymbolMap(parsed));
        } else {
            return Optional.empty();
        }
    }
    
    /**
     * Builder class for a {@code Parser}.
     * 
     * @author  Tobias Bachert
     * @version 1.02, 2016/06/12
     */
    public static final class Builder {
        
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
         * (&lt;name&gt;someDelimiter&lt;value&gt;abc</pre>
         * </blockquote>
         * A symbol-map of a parser {@code p} created with this format-string would return for example:
         * <blockquote><pre>
         * SymbolMap args = p.toSymbolMap("(theNamesomeDelimiter57abc").get();
         * args.get(sNAME);  // String "theName"
         * args.get(sVALUE); // Integer 57</pre>
         * </blockquote>
         * 
         * @param  format the format-string used
         * @return a parser for the given format-string
         */
        public Parser build(final String format) {
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
            return new Parser(formatSymbols, Pattern.compile(pattern.toString()));
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
                final Symbol<?> current = (Symbol<?>) Invocations.invoke(getter);
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
                    Invocations.invoke(setter, symbol);
                }
            }
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
        
        @Override
        public String toString() {
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
