package edu.kit.informatik.util.executable;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import edu.kit.informatik.util.executable.Parser.SymbolMap;

/**
 * Interface for executable objects that operate depending on user-input.
 * 
 * @author  Tobias Bachert
 * @version 1.05, 2016/05/24
 * 
 * @param   <T> the type parameter
 */
public interface Command<T> {
    
    //==================================================================================================================
    // |/ Methods - Executable \|
    /**
     * Executes the order.
     * 
     * <p>The provided {@code SymbolMap} has to be created by the {@code Parser} returned by the {@link #parser()}
     * method, otherwise the behavior of this method is undefined.
     * 
     * <p>This method should generally only be invoked by the {@link #tryApply(Object, String)} method.
     * 
     * @param t the {@code Object} the order will be executed for
     * @param args a {@code SymbolMap} containing the arguments for the order
     */
    void apply(final T t, final SymbolMap args);
    
    /**
     * Returns if the command has to quit the current loop.<br>
     * Has to be overridden in case that the command has to quit the current loop.
     * 
     * @return {@code true} if the command has to quit the current loop, otherwise {@code false}.
     * @see    Executor#loop()
     */
    default boolean isQuit() {
        return false;
    }
    
    /**
     * Returns the parser of the order.
     * 
     * @return the {@code Parser}
     */
    Parser parser();
    
    //==================================================================================================================
    // |/ Executor \|
    /**
     * Executor for executables.
     * 
     * @param <T> the type parameter
     */
    final class Executor<T> {
        
        private final T target;
        private final Command<? super T>[] array;
        
        /**
         * Creates a new executor.
         * 
         * @param target the {@code Object} the orders will be executed for
         * @param array an array of commands
         */
        public Executor(final T target, final Command<? super T>[] array) {
            this.target = Objects.requireNonNull(target);
            @SuppressWarnings("unchecked")
            final Command<? super T>[] a = Arrays.stream(array).map(Objects::requireNonNull).toArray(Command[]::new);
            this.array = a;
        }
        
        /**
         * Loop for user-input.<br>
         * {@linkplain Command#apply(Object, SymbolMap) Executes} commands until the executed command {@linkplain
         * Command#isQuit() is quit}.
         * 
         * @param supplier a supplier for input-strings
         * @param error a consumer for used for error-messages
         */
        public void loop(final Supplier<String> supplier, final Consumer<? super String> error) {
            outer:
            for (;;) {
                final String input = supplier.get();
                for (final Command<? super T> command : array) {
                    if (command.parser().parsable(input, (e) -> error.accept("exception while parsing '" + input
                            + "' for '" + command + "' (" + e + ")"))) {
                        command.apply(target, command.parser().toSymbolMap(input));
                        if (command.isQuit()) return;
                        continue outer;
                    }
                }
                error.accept("'" + input + "' does not match a command");
            }
        }
    }
}
