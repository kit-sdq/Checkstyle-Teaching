package edu.kit.informatik;

public class Token {
    private Players player;

    public Token(Players player) {
        this.player = player;
    }

    public Players getPlayer() {
        return player;
    }

    public String getLiteral() {
        return getPlayer().getLiteral();
    }
}
