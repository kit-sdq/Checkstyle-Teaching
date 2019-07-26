package edu.kit.informatik;

import java.util.List;

public class Player {
    private Players player;
    private List<Token> tokens;

    public Player(Players player, List<Token> tokens) {
        this.player = player;
        this.tokens = tokens;
    }

    public Players getPlayer() {
        return player;
    }

    public boolean hasTokens() {
      return !tokens.isEmpty();
    }

    public Token getToken() {
        if (tokens.isEmpty()) return null;
        return tokens.remove(0);
    }

    public int getTokenCount() {
        return tokens.size();
    }
}
