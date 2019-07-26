package edu.kit.informatik;

import java.util.List;

/**
 * Created by Thomas on 10.07.2018.
 */
public class Game {
    private Board board;
    private Player activePlayer;
    private Player[] players;
    private Modes mode;
    private State state;

    public Game(Board board, Player[] players, Modes mode) {
        this.board = board;
        this.players = players;
        this.activePlayer = players[0];
        this.mode = mode;
        this.state = State.PLAYING;
    }

    ///////////////output methods////////////////////
    public String print() {
        return board.print();
    }

    public String state(int x, int y) {
        return board.state(x, y);
    }

    public String token() {
        return String.valueOf(activePlayer.getTokenCount());
    }

    ///////////////////Wenn win oder draw dann state changen, rest wird ueber modes
    // geregelt/////////////////////////

    ////////////////board changing methods/////////////////
    public String throwin(int x) throws InputException {
        if (activePlayer.hasTokens()) {
            String ret = board.throwin(x, activePlayer.getToken());
            if (!ret.equals("OK")) state = State.WIN;
            swapPlayer();
            if (!activePlayer.hasTokens()) {
                state = State.DRAW;
                ret = "draw";
            }
            return ret;
        }
        state = State.DRAW;
        return "draw";
    }

    public String flip() throws InputException {
        String ret = board.flip();
        if (!ret.equals("OK")) state = State.WIN;
        swapPlayer();
        return ret;
    }

    public String remove(int i) throws InputException {
        String ret = board.remove(i, activePlayer);
        if (!ret.equals("OK")) state = State.WIN;
        swapPlayer();
        return ret;
    }

    //////////////////get commands depending on mode////////////////
    public List<Command> getAvailableCommands() {
        return mode.getAvailableCommands(state);
    }
    ///////////////////swap the active player/////////////////////

    private void swapPlayer() {
        if (activePlayer == players[0]) activePlayer = players[1];
        else activePlayer = players[0];
    }
}
