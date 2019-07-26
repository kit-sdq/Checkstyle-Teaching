package edu.kit.informatik;

public enum Result implements RPSSLResult {
    /**
     * The result of the player winning.
     */
    WIN,
    /**
     * The result of the player losing.
     */
    LOSE,
    /**
     * The result of the player drawing with the computer.
     */
    DRAW;

    @Override
    public String displayResult() {
        return this.toString().toLowerCase();
    }

    /**
     * Converts a stranding (wins:losses) into a representation of the Result enum.
     * 
     * @param wins The amount of wins in the standing.
     * @param losses The amount of losses in the standing.
     * @return The fitting Result instance.
     */
    public static Result standingsToResult(int wins, int losses) {
        if (wins == losses) {
            return Result.DRAW;
        } else if (wins < losses) {
            return Result.LOSE;
        } else {
            return Result.WIN;
        }
    }
}
