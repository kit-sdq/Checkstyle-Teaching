package edu.kit.informatik;

import edu.kit.informatik.ludo.Board;
import edu.kit.informatik.util.executable.Command.Executor;

/**
 * The class {@code Main} contains the entry point of the program.
 *
 * @author  Tobias Bachert
 * @version 1.01, 2016/06/26
 *
 * @see     #main(String[])
 */
public final class Main {
    
    
    //==================================================================================================================
    // |/ Constructor \|
    /**
     * Hidden constructor of {@code Main}.
     *
     * @throws AssertionError if called
     * @deprecated Utility-class constructor, throws {@code AssertionError}.
     */
    @Deprecated
    private Main() {
        throw new AssertionError("instantiating this class is disallowed");
    }
    
    //==================================================================================================================
    // |/ Methods - Static \|
    /**
     * Main-routine.<br>
     * Starts the user-input.
     *
     * @param args command-line parameter, has to contain the path to an input-file
     */
    public static void main(final String args[]) {
        new Executor<>(new Board(), Commands.values()).loop(Terminal::readLine, Terminal::printError);
    }
}
