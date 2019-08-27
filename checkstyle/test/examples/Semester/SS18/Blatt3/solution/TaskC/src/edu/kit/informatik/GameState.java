package edu.kit.informatik;

public enum GameState {
    RUNNING,
    WIN,
    END;

    public boolean isGameOverState() {
        return this != GameState.RUNNING;
    }
}
