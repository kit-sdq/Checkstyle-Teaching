package edu.kit.informatik;

/**
 * This enum models the state that a run is in. There are states with string representation, these states show
 * that a run is over.
 *
 * @author Peter Oettig
 * @version 1.0
 */
public enum GameState {
    /**
     * The win state.
     */
    WIN("gewonnen"),
    /**
     * The lose state.
     */
    LOSE("verloren"),
    /**
     * The selecting state.
     */
    SELECTING(),
    /**
     * The changing state.
     */
    CHANGING();

    private String resultString;

    /**
     * Creates a state object with no string representation, i.e. a state that isn't a 'run is over' state.
     */
    GameState() {
        this.resultString = null;
    }

    /**
     * Creates a state object with a string representation, i.e. a state that is a 'run is over' state.
     *
     * @param resultString the string representation of the state if it is a 'run is over' state.
     */
    GameState(String resultString) {
        this.resultString = resultString;
    }

    /**
     * Returns the string representation of the state.
     *
     * @return the string representation if the state is a 'run is over' state.
     * @throws IllegalStateException if the state is not a 'run is over' state.
     */
    public String getResultString() {
        if (resultString == null) {
            throw new IllegalStateException("this is not a 'run is over' state.");
        }
        return resultString;
    }
}