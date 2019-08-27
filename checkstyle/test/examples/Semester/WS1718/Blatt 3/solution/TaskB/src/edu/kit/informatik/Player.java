package edu.kit.informatik;

public class Player {
    private int number;
    private char token;
    
    public Player(int number, char token) {
        this.number = number;
        this.token = token;
    }

    public int getNumber() {
        return number;
    }

    public char getToken() {
        return token;
    }
}
