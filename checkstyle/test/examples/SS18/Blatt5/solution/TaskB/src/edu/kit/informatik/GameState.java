package edu.kit.informatik;

/**
 * The state the UI is in, used to assert only commands making sense at the moment can be performed.
 */
public enum GameState {
    /**
     * The state before the move command of the current player.
     */
    BEFORE_MOVE("need a valid move first."),

    /**
     * The state while the move of the current player is ongoing (i.e. after move and before build).
     */
    IN_MOVE("need a valid build first."),

    /**
     * The state after the build command of the current player.
     */
    AFTER_MOVE("%s already made a move.");

    private final String requirement;

    private GameState(String requirement) {
        this.requirement = requirement;
    }

    /**
     * Gets the message why a command isn't allowed now.
     *
     * @param player The player thats turn is currently ongoing.
     * @return The message.
     */
    public String getStateRequirement(Player player) {
        return String.format(requirement, player);
    }
}