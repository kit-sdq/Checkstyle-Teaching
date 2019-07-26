package edu.kit.informatik;

import edu.kit.informatik.UI.InputException;

import java.util.ArrayList;
import java.util.List;

public class Mastermind {
    public static final int MAX_TRIES = 10;
    public static final int SEQUENCE_LENGTH = 4;
    
    private GameState state;
    private int tryNumber;
    private List<Color> expectedSequence;
    
    public Mastermind(List<Color> expectedSequence) {
        this.state = GameState.RUNNING;
        this.tryNumber = 1;
        this.expectedSequence = expectedSequence;
    }

    public String getPrompt() {
        return "Versuch " + tryNumber + ":";
    }

    public boolean isGameOver() {
        return state.isGameOverState();
    }
    
    public Result checkGuess(List<Color> guess) throws InputException {
        if (state.isGameOverState()) {
            throw new InputException("the game is already over.");
        }
        
        tryNumber++;
        if (guess.equals(expectedSequence)) {
            state = GameState.WIN;
            return new Result(state, 4, 0, 4, tryNumber);    
        }
        
        int colorsCorrect = 0;
        int placesCorrect = 0;
        List<Color> tempSequence = new ArrayList<>(expectedSequence);
        for (int i = 0; i < SEQUENCE_LENGTH; i++) {
            Color color = guess.get(i);
            if (color.equals(expectedSequence.get(i))) {
                placesCorrect++;
            }
            
            if (tempSequence.contains(color)) {
                colorsCorrect++;
                tempSequence.remove(color);
            }
        }
        
        if (tryNumber <= MAX_TRIES) {
            state = GameState.RUNNING;
        } else {
            state = GameState.END;
        }

        return new Result(state, colorsCorrect, SEQUENCE_LENGTH - colorsCorrect, placesCorrect, tryNumber);
    }
}
