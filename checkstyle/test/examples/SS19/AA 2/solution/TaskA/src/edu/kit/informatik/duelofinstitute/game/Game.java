/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.duelofinstitute.game;

import edu.kit.informatik.duelofinstitute.data.Direction;
import edu.kit.informatik.duelofinstitute.data.GamePlayer;
import edu.kit.informatik.duelofinstitute.data.Hop;
import edu.kit.informatik.duelofinstitute.data.Path;
import edu.kit.informatik.duelofinstitute.data.Player;
import edu.kit.informatik.duelofinstitute.data.Position;
import edu.kit.informatik.duelofinstitute.data.Role;
import edu.kit.informatik.duelofinstitute.data.Tile;
import edu.kit.informatik.duelofinstitute.exception.InputException;
import edu.kit.informatik.duelofinstitute.exception.InvalidParameterException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.StringJoiner;

/**
 * @author Thomas Weber
 * @version 1.0
 */
public class Game {
    private Queue<GamePlayer> gameQueue;

    private int round;

    private boolean pass;

    private boolean running;


    public Game() {
        this.gameQueue = new ArrayDeque<>();
        for (Player player : Player.values()) {
            gameQueue.add(new GamePlayer(player));
        }
        round = 0;
        pass = false;
        running = true;
    }

    private GamePlayer getActivePlayer() {
        return gameQueue.peek();
    }

    private boolean isActivePlayerSpecialTileSet() {
        return getActivePlayer().getSpecialTile().isPlaced();
    }

    public String start(String startTile) throws InvalidParameterException {
        // is the new tile valid?
        Tile startTileTraced = Tile.getTile(startTile);
        // if not do nothing and throw an exception
        if (startTileTraced == null) {
            throw new InvalidParameterException(
                    "given start tile is not " + "valid");
        }
        // otherwise clear all positions from the previous game
        Tile.clearPositions();
        // and reset the round counter
        round = 0;
        // and set the game as running
        running = true;
        while (!(gameQueue.peek() != null && gameQueue.peek().getPlayer()
                .equals(startTileTraced.getPlayer()))) {
            changePlayer();
        }
        // found the correct player so we can set the position for the tile
        startTileTraced.setPosition(new Position(0, 0, 0, true, 0));
        // and change the player
        changePlayer();
        round++;
        pass = false;
        return "OK";
    }

    private void gameNotRunning() throws InvalidParameterException {
        if (!running) {
            throw new InvalidParameterException("game is not running");
        }
    }

    public String pass() throws InvalidParameterException {
        gameNotStarted();
        gameNotRunning();
        specialTokenNotPlaced();
        if (pass) {
            running = false;
            return "DRAW";
        }
        changePlayer();
        pass = true;
        round++;
        return calculateOutputString();
    }

    private void changePlayer() throws InvalidParameterException {
        gameQueue.add(gameQueue.poll());
    }

    public String print() throws InvalidParameterException {
        gameNotStarted();
        gameNotRunning();
        StringJoiner joiner = new StringJoiner(System.lineSeparator());
        List<Tile> tiles = new ArrayList<>(Tile.getAvailableTiles());
        Collections.sort(tiles);
        tiles.stream().filter(it -> it.getPosition() != null)
                .forEach(it -> joiner.add(it.print(this)));
        return joiner.toString();
    }

    public String place(Tile moveTile, Hop hop)
            throws InvalidParameterException, InputException {
        gameNotStarted();
        gameNotRunning();
        if (moveTile == null) {
            throw new NullPointerException("move tile cannot be null");
        }
        if (hop.getDirection() == null) {
            throw new NullPointerException("direction cannot be null");
        }
        if (hop.getTile() == null) {
            throw new NullPointerException("target tile cannot be null");
        }
        if (hop.getTile().getPosition() == null) {
            throw new InvalidParameterException(
                    "given target tile is not " + "placed");
        }
        if (Tile.getTileFromPosition(hop.getTargetPosition().getGroundNeighbour(
                Direction.ZERO), true) != null) {
            throw new InvalidParameterException(
                    "the given position is " + "already occupied");
        }
        if (!(gameQueue.peek() != null && gameQueue.peek().getPlayer()
                .equals(moveTile.getPlayer()))) {
            throw new InvalidParameterException(
                    "tile is from the wrong player");
        }
        if (Tile.getTile(hop.getDirection(), hop.getTile().getPosition())
            != null) {
            throw new InvalidParameterException(
                    "this position is already occupied");
        }
        if (round != 1 && Tile.hasOtherPlayerNeighbour(moveTile.getPlayer(),
                hop.getTile().getPosition().getNeighbour(hop.getDirection()))) {
            throw new InvalidParameterException(
                    "tile has neighbours from the wrong player");
        }
        GamePlayer gamePlayer = gameQueue.peek();
        if (moveTile.getPosition() != null) {
            throw new InvalidParameterException("tile already placed");
        }
        // professor has to be placed before round 4, because every placement
        // is counted a round before round 8 here -> after round 6 he either
        // has to be placed or to be already placed
        if (round > 5) {
            if (!moveTile.getRole().equals(Role.MISTER_X) && !gamePlayer
                    .getSpecialTile().isPlaced()) {
                throw new InputException("professor has to be placed by now");
            }
        }
        // the position is checked so we do not need the null check here
        moveTile.setPosition(
                hop.getTile().getPosition().getNeighbour(hop.getDirection()));
        changePlayer();
        round++;
        pass = false;
        return calculateOutputString();
    }

    public String move(Tile moveTile, Path path)
            throws InvalidParameterException, InputException {
        // check conditions and throw an exception if they are not met
        gameNotStarted();
        gameNotRunning();
        specialTokenNotPlaced();
        if (!(gameQueue.peek() != null && gameQueue.peek().getPlayer()
                .equals(moveTile.getPlayer()))) {
            throw new InvalidParameterException(
                    "tile is from the wrong player");
        }
        if (moveTile == null || path == null) {
            throw new InputException("given data is invalid");
        }

        GamePlayer gamePlayer = gameQueue.peek();
        if (!moveTile.allowedToMove()) {
            throw new InputException("selected tile is not movable");
        }
        if (!path.isValidPath()) {
            throw new InputException("path is not valid " + path.toString());
        }
        moveTile.setPosition(path.getTargetPosition());
        round++;
        changePlayer();
        pass = false;
        return calculateOutputString();
    }

    private String calculateOutputString() {
        if (hasWinner()) {
            StringJoiner joiner = new StringJoiner(" ");
            if (calculateWinner().size() == 0) {
                running = false;
                return "DRAW";
            }
            for (GamePlayer player : calculateWinner()) {
                joiner.add("WINNER");
                joiner.add(player.getPlayer().getColor());
            }
            running = false;
            return joiner.toString();
        }
        return "OK";
    }

    public List<GamePlayer> calculateWinner() {
        List<GamePlayer> winners = new ArrayList<>();
        for (GamePlayer player : gameQueue) {
            if (!playerLost(player)) {
                winners.add(player);
            }
        }
        return winners;
    }

    public boolean hasWinner() {
        for (GamePlayer player : gameQueue) {
            if (playerLost(player)) {
                return true;
            }
        }
        return false;
    }

    private boolean playerLost(GamePlayer player) {
        return !player.getSpecialTile().canMove();
    }

    private void gameNotStarted() throws InvalidParameterException {
        if (round == 0) {
            throw new InvalidParameterException(
                    "the game has not been " + "started yet or is over");
        }
    }

    private void specialTokenNotPlaced() throws InvalidParameterException {
        if (!isActivePlayerSpecialTileSet()) {
            throw new InvalidParameterException(
                    "the special token has to be " + "placed");
        }
    }
}
