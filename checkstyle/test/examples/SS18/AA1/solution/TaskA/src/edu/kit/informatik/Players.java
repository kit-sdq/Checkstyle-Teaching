package edu.kit.informatik;

/**
 * Created by Thomas on 10.07.2018.
 */
public enum Players {
    P1("P1"),
    P2("P2"),
    None("**");

    private String literal;

    private Players(String literal) {
        this.literal = literal;
    }

    public String getLiteral() {
        return this.literal;
    }
}
