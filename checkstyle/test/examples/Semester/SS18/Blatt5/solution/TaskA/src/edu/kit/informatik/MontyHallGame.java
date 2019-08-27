package edu.kit.informatik;

import java.util.Queue;

/**
 * The main game implementation of the RPSSL game.
 * 
 * @author Peter
 * @version 1.0
 */
public class MontyHallGame {
    private int winsWithChange;
    private int winsWithoutChange;
    private Queue<Run> runs;
    private Run currentRun;

    /**
     * The constructor for the Monty Hall Game. It takes an amount of runs that are to be simulated.
     * @param runs The FIFO queue of runs to simulate.
     */
    public MontyHallGame(Queue<Run> runs) {
        this.runs = runs;
        this.currentRun = runs.remove();
    }

    /**
     * Checks if the game is currently in the gate selection state.
     * 
     * @return true, if the game is currently in gate selection state, false otherwise.
     */
    public boolean isInSelectState() {
        return currentRun.isInSelectState();
    }

    /**
     * Checks if the game is currently in the decision changing state.
     *
     * @return true, if the game is currently in decision changing state, false otherwise.
     */
    public boolean isInChangeState() {
        return currentRun.isInChangeState();
    }

    /**
     * Gets the currently up to date prompt according to the state of the run.
     *
     * @return The prompt.
     */
    public String getPrompt() {
        return currentRun.getPrompt();
    }

    /**
     * Selects a gate with the number 1, 2 or 3.
     *
     * @param gateNumber The number of the gate to select. Either 1, 2 or 3.
     */
    public void selectGate(int gateNumber) {
       currentRun.selectGate(gateNumber);
    }

    /**
     * Returns a gate with a goat behind it that the player has not selected.
     *
     * @return The number of the goat gate.
     */
    public int getGoatGate() {
        return currentRun.getGoatGate();
    }

    /**
     * Switches the selected gate to the other one that is not openend if the player wishes.
     * Afterwards it evaluates if the player has won.
     *
     * @param doChange true, if the player wants to change his selection to the other gate.
     */
    public void decideAndEnd(boolean doChange) {
        currentRun.decideAndEnd(doChange);
    }

    /**
     * Evaluates the result of the currently ongoing run. The run needs to be finished to be evaluated.
     */
    public void evaluateResult() {
        if (!currentRun.isOver()) {
            throw new IllegalStateException("the run is not over yet");
        }
        
        winsWithChange += currentRun.getScore(true);
        winsWithoutChange += currentRun.getScore(false);
    }

    /**
     * Proceeds to the next run that should be simulated.
     *
     * @return true if there is another run to be simulated, false otherwise.
     */
    public boolean nextRun() {
        if (runs.isEmpty()) {
            return false;
        }
        
        currentRun = runs.remove();
        return true;
    }

    /**
     * Returns the result string for the current run for output. The run needs to be finished for that.
     *
     * @return The result string meant for output.
     */
    public String getResultString() {
        return currentRun.getResultString() + winsWithChange + ":" + winsWithoutChange;
    }
}
