package edu.kit.informatik;

/**
 * A result of the RPSSL game, that provides a string representation of the result of a move.
 * The result can be presented in different format depending on the type of move (e.g. last of a run, last of the game)
 *
 * @author Peter
 * @version 1.0
 */
public interface RPSSLResult {
    /**
     * Returns the string representation of the result of a RPSSL move. 
     * 
     * @return The string representation of the result.
     */
    String displayResult();
}
