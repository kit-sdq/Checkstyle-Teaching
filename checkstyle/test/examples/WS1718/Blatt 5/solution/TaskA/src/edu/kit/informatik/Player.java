package edu.kit.informatik;

import java.util.Objects;

public class Player {
    private int number;
    private char token;

    /**
     * Constructs a new player with a player number and a token that the player uses to play on the board.
     * 
     * @param number The number of the player.
     * @param token The token that the player uses to play on the board.
     */
    public Player(int number, char token) {
        this.number = number;
        this.token = token;
    }

    /**
     * Gets the number of the player.
     * 
     * @return The player's number.
     */
    public int getNumber() {
        return number;
    }

    /**
     * Gets the token of the player.
     * 
     * @return The player's token.
     */
    public char getToken() {
        return token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        Player player = (Player) o;
        return number == player.number;
    }

    @Override
    public int hashCode() {

        return Objects.hash(number);
    }
}
