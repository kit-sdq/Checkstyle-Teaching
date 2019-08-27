package edu.kit.informatik;

public class Result {
    private GameState state;
    private int colorsCorrect;
    private int colorsWrong;
    private int placesCorrect;
    private int tryNumber;
    

    public Result(int colorsCorrect, int colorsWrong, int placesCorrect, int tryNumber) {
        this.state = GameState.RUNNING;
        this.colorsCorrect = colorsCorrect;
        this.colorsWrong = colorsWrong;
        this.placesCorrect = placesCorrect;
        this.tryNumber = tryNumber;
    }

    public Result(GameState state, int colorsCorrect, int colorsWrong, int placesCorrect, int tryNumber) {
        this.state = state;
        this.colorsCorrect = colorsCorrect;
        this.colorsWrong = colorsWrong;
        this.placesCorrect = placesCorrect;
        this.tryNumber = tryNumber;
    }
    
    @Override
    public String toString() {
        if (state.isGameOverState()) {
            return state.toString().toLowerCase();
        } else {
            return colorsCorrect + "," + colorsWrong + "," + placesCorrect + ","
                    + (Mastermind.MAX_TRIES - tryNumber + 1);
        }
    }
}
