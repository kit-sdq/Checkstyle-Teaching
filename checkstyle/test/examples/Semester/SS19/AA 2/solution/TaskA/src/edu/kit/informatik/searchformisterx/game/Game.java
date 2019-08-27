/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.searchformisterx.game;

import edu.kit.informatik.searchformisterx.data.Direction;
import edu.kit.informatik.searchformisterx.data.GamePlayer;
import edu.kit.informatik.searchformisterx.data.Hop;
import edu.kit.informatik.searchformisterx.data.Path;
import edu.kit.informatik.searchformisterx.data.Player;
import edu.kit.informatik.searchformisterx.data.Position;
import edu.kit.informatik.searchformisterx.data.Role;
import edu.kit.informatik.searchformisterx.data.Tile;
import edu.kit.informatik.searchformisterx.exception.InputException;
import edu.kit.informatik.searchformisterx.exception.InvalidParameterException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.StringJoiner;

/**
 * Encapsulates a game of mister x as stated in the assignment.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class Game {
    /**
     * Queue of players that play the game.
     */
    private Queue<GamePlayer> gameQueue;

    /**
     * The round of the game.
     */
    private int round;

    /**
     * If a player has past the last round.
     */
    private boolean pass;

    /**
     * If a game is running right now.
     */
    private boolean running;

    /**
     * Instantiates a new game with all available players and tokens.
     */
    public Game() {
        this.gameQueue = new ArrayDeque<>();
        for (Player player : Player.values()) {
            gameQueue.add(new GamePlayer(player));
        }
        round = 0;
        pass = false;
        running = true;
    }

    /**
     * @return the active player
     */
    private GamePlayer getActivePlayer() {
        return gameQueue.peek();
    }

    /**
     * @return whether or not the special tile of the active player is set
     */
    private boolean isActivePlayerSpecialTileSet() {
        return getActivePlayer().getSpecialTile().isPlaced();
    }

    /**
     * Starts a new game.
     *
     * @param startTile the tile that is placed to start the game
     * @return a string return value
     * @throws InvalidParameterException occurs if the input is somehow
     *         malformed, i.e the startTile cannot be parsed to a available tile
     */
    public String start(String startTile) throws InvalidParameterException {
        Tile startTileTraced = Tile.getTile(startTile);
        // if the given tile is not valid do nothing and throw an exception
        if (startTileTraced == null) {
            throw new InvalidParameterException(
                    "given start tile is not " + "valid");
        }
        Tile.clearPositions();
        round = 0;
        running = true;
        while (!(gameQueue.peek() != null && gameQueue.peek().getPlayer()
                .equals(startTileTraced.getPlayer()))) {
            changePlayer();
        }
        // found the correct player so we can set the position for the tile
        startTileTraced.setPosition(new Position(0, 0, 0, true, 0));
        changePlayer();
        round++;
        pass = false;
        return "OK";
    }

    /**
     * @throws InvalidParameterException if the game is not running
     */
    private void gameNotRunning() throws InvalidParameterException {
        if (!running) {
            throw new InvalidParameterException("game is not running");
        }
    }

    /**
     * Passes a round, two passes after each other result in a draw.
     *
     * @return String return message
     * @throws InvalidParameterException occurs if the game is not
     *         started, not running anymore of the special token of the
     *         player that wants to pass is not placed yet
     */
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

    /**
     * Changes the active player.
     */
    private void changePlayer() {
        gameQueue.add(gameQueue.poll());
    }

    /**
     * Returns a representation of the game board for output.
     *
     * @return a String representation of the game board like stated in the
     *         assignment
     * @throws InvalidParameterException occurs if the game is not
     *         running or not started
     */
    public String print() throws InvalidParameterException {
        gameNotStarted();
        gameNotRunning();
        StringJoiner joiner = new StringJoiner(System.lineSeparator());
        List<Tile> tiles = new ArrayList<>(Tile.getAvailableTiles());
        Collections.sort(tiles);
        tiles.stream().filter(it -> it.getPosition() != null)
                .forEach(it -> joiner.add(it.print()));
        return joiner.toString();
    }

    /**
     * Places the given tile on the given position if possible, throws an
     * exception otherwise.
     *
     * @param moveTile the tile that should be placed
     * @param hop the hop that encapsulates the position it should be
     *         placed on
     * @return a return value in case the placing is successful
     * @throws InvalidParameterException if game restrictions are not
     *         fulfilled, i.e. the given tile is already placed
     * @throws InputException occurs if the tile is not valid
     */
    public String place(Tile moveTile, Hop hop)
            throws InvalidParameterException, InputException {
        gameNotStarted();
        gameNotRunning();
        if (moveTile == null) {
            throw new InvalidParameterException("move tile cannot be null");
        }
        if (hop.getDirection() == null) {
            throw new InvalidParameterException("direction cannot be null");
        }
        if (hop.getTile() == null) {
            throw new InvalidParameterException("target tile cannot be null");
        }
        if (hop.getTile().getPosition() == null) {
            throw new InvalidParameterException(
                    "given target tile is not " + "placed");
        }
        if (Tile.getTileFromPosition(
                hop.getTargetPosition().getGroundNeighbour(Direction.ZERO),
                true) != null) {
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
        // Mister X has to be placed before round 4, because every placement
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

    /**
     * Moves the given moveTile if possible, throws an exception otherwise.
     *
     * @param moveTile the tile that should be moved
     * @param path the path the tile should be moved on
     * @return string return when the moving was successful
     * @throws InvalidParameterException occurs if i.e. the given
     *         moveTile is
     *         not from the active player
     * @throws InputException occurs if the tiles are invalid
     */
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
        if (path == null) {
            throw new InputException("given data is invalid");
        }
        if (!moveTile.isHighestTile()) {
            throw new InputException("tile cannot be moved because it is not "
                                     + "the highest tile on its position");
        }

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

    /**
     * @return the output String, WINNER or DRAW in case they occur, OK
     *         otherwise
     */
    private String calculateOutputString() {
        if (hasWinner()) {
            StringJoiner joiner = new StringJoiner(" ");
            if (calculateWinner().size() == 0) {
                running = false;
                return "DRAW";
            }
            for (GamePlayer player : calculateWinner()) {
                joiner.add("WINNER");
                joiner.add(player.getPlayer().getWinningIdentifier());
            }
            running = false;
            return joiner.toString();
        }
        return "OK";
    }

    /**
     * @return list of GamePlayers that are winners
     */
    private List<GamePlayer> calculateWinner() {
        List<GamePlayer> winners = new ArrayList<>();
        for (GamePlayer player : gameQueue) {
            if (!playerLost(player)) {
                winners.add(player);
            }
        }
        return winners;
    }

    /**
     * @return whether or not the game has a winner
     */
    private boolean hasWinner() {
        for (GamePlayer player : gameQueue) {
            if (playerLost(player)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param player a GamePlayer
     * @return whether or not the given player lost the game
     */
    private boolean playerLost(GamePlayer player) {
        return !player.getSpecialTile().canMove();
    }

    /**
     * @throws InvalidParameterException occurs if the game is not
     *         started yet
     */
    private void gameNotStarted() throws InvalidParameterException {
        if (round == 0) {
            throw new InvalidParameterException(
                    "the game has not been " + "started yet or is over");
        }
    }

    /**
     * @throws InvalidParameterException occurs if the active player has
     *         not placed his special token
     */
    private void specialTokenNotPlaced() throws InvalidParameterException {
        if (!isActivePlayerSpecialTileSet()) {
            throw new InvalidParameterException(
                    "the special token has to be " + "placed");
        }
    }
}
