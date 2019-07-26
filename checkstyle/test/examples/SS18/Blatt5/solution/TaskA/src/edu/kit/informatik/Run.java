package edu.kit.informatik;

import edu.kit.informatik.UI.Main;

/**
 * This is a single run of the monty hall problem.
 *
 * @author Peter Oettig
 * @version 1.0
 */
public class Run {
    private final Prize[] prizes;
    private int selectedGate;
    private int openedGate;
    private boolean madeChange;
    private GameState state;

    /**
     * The constructor for a new run. The prize per Gate needs to be given.
     *
     * @param prizes The prizes behind gate 1,2,3 at indices 0,1,2.
     */
    public Run(Prize[] prizes) {
        this.prizes = prizes;
        this.state = GameState.SELECTING;
        this.selectedGate = -1;
        this.openedGate = -1;
    }

    /**
     * Gets the currently up to date prompt according to the state of the run.
     *
     * @return The prompt.
     */
    public String getPrompt() {
        switch (state) {
            case SELECTING:
                return "Moderator - Waehlen Sie eins der drei Tore aus:";
            case CHANGING:
                return "Moderator - Moechten Sie das andere noch verschlossene Tor auswaehlen:";
            default:
                throw new IllegalStateException("the run is not state where a prompt should be happening.");
        }
    }

    /**
     * Checks if the run is currently in the gate selection state.
     *
     * @return true, if the run is currently in gate selection state, false otherwise.
     */
    public boolean isInSelectState() {
        return state.equals(GameState.SELECTING);
    }

    /**
     * Checks if the run is currently in the decision changing state.
     *
     * @return true if the run is currently in decision changing state, false otherwise.
     */
    public boolean isInChangeState() {
        return state.equals(GameState.CHANGING);
    }

    /**
     * Checks if the run is over, i.e. the player lost or won.
     *
     * @return true if the game is over, false otherwise.
     */
    public boolean isOver() {
        return state.equals(GameState.WIN) || state.equals(GameState.LOSE);
    }

    /**
     * Gets the win score for the run, either 1 if it was a win or 0 if not.
     * Can return a value according to these rules:
     * <ul>
     *     <li>
     *         withChange true: If the player changed his decision and won it returns 1, otherwise 0.<br />
     *         If the player kept his decision it returns 0 either way.
     *     </li>
     *     <li>
     *         withChange false: If the player kept his decision and won it returns 1, otherwise 0.<br />
     *         If the player changed his decision it returns 0 either way.         
     *     </li>
     * </ul>
     * 
     * @param withChange if true, returns a scoring for a changed decision, otherwise for a kept decision.
     * @return a score value according to the stated rules.
     */
    public int getScore(boolean withChange) {
        if (!isOver()) {
            throw new IllegalStateException("the run is not over.");
        }
        
        if (withChange) {
            if (state.equals(GameState.WIN) && madeChange) {
                return 1;
            } else {
                return 0;
            }
        } else {
            if (state.equals(GameState.WIN) && !madeChange) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    /**
     * Selects a gate with the number 1, 2 or 3.
     * 
     * @param gateNumber The number of the gate to select. Either 1, 2 or 3.
     */
    public void selectGate(int gateNumber) {
        if (!isInSelectState()) {
            throw new IllegalStateException("the run is not in selection state.");
        }
        
        setSelectedGate(gateNumber);
        state = GameState.CHANGING;
    }

    /**
     * Returns a gate with a goat behind it that the player has not selected.
     * 
     * @return The number of the goat gate.
     */
    public int getGoatGate() {
        if (!isInChangeState() || selectedGate == -1) {
            throw new IllegalStateException("the run is not in changing state.");
        }
        
        for (int i = 1; i < prizes.length + 1; i++) {
            if (i != selectedGate && prizes[i - 1].equals(Prize.GOAT)) {
                openedGate = i;
                return i;
            }
        }
        
        // This can never happen.
        throw new IllegalStateException("the game is in an inconsistent state.");
    }

    /**
     * Switches the selected gate to the other one that is not openend if the player wishes.
     * Afterwards it evaluates if the player has won.
     * 
     * @param doChange true, if the player wants to change his selection to the other gate.
     */
    public void decideAndEnd(boolean doChange) {
        if (!isInChangeState() || selectedGate == -1 || openedGate == -1) {
            throw new IllegalStateException("the run is not in changing state.");
        }
        
        if (doChange) {
            setSelectedGate(Main.NUMBER_OF_GATES * 2 - (selectedGate + openedGate));
            madeChange = true;
        }
        
        switch (prizes[selectedGate - 1]) {
            case CAR:
                state = GameState.WIN;
                break;
            case GOAT:
                state = GameState.LOSE;
                break;
            default:
                // This isn't possible.
                throw new IllegalStateException("There was an unknown prize behind the gate.");
        }
    }

    /**
     * Returns the result string for the run for output. The run needs to be finished for that.
     *
     * @return The result string meant for output.
     */
    public String getResultString() {
        if (!isOver()) {
            throw new IllegalStateException("the run is not over.");
        }
        
        return "Sie haben " + state.getResultString() + ". ";
    }

    private void setSelectedGate(int gateNumber) {
        if (gateNumber < 1 || gateNumber > Main.NUMBER_OF_GATES) {
            throw new IllegalArgumentException("there is no gate with this number.");
        }

        selectedGate = gateNumber;
    }
}
