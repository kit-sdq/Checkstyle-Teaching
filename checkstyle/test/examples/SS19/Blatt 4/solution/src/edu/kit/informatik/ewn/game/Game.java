/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.ewn.game;

import java.util.SortedSet;

import edu.kit.informatik.ewn.board.Board;
import edu.kit.informatik.ewn.board.Player;
import edu.kit.informatik.ewn.board.StandardBoard;
import edu.kit.informatik.ewn.board.Token;
import edu.kit.informatik.ewn.board.TorusBoard;

/**
 * Represents the "EWN" game.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public class Game {
    private static final int NOT_ROLLED = -1;

    private final BoardSize boardSize;
    private final Board board;

    private boolean started;
    private boolean won;
    private int rollNumber;
    private Player activePlayer;

    /**
     * Creates a new game.
     * 
     * @param mode
     *            - the game mode
     * @param size
     *            - the board size
     */
    public Game(final Gamemode mode, final BoardSize size) {
        assert (mode != null);
        assert (size != null);
        this.boardSize = size;
        this.board = mode == Gamemode.STANDARD ? new StandardBoard(size.getBoardSize())
                : new TorusBoard(size.getBoardSize());
        this.started = false;
        this.won = false;
        this.rollNumber = NOT_ROLLED;
    }

    /**
     * Starts the game with the stones in the order like defined in the assignment.
     * 
     * @param stonesPlayerOne
     *            - stones of player one
     * @param stonesPlayerTwo
     *            - stones of player two
     * @throws IllegalArgumentException
     *             - if one of the stones are invalid
     * @throws IllegalStateException
     *             - if game is already started
     */
    public void start(final int[] stonesPlayerOne, final int[] stonesPlayerTwo) throws IllegalArgumentException {
        if (!this.started) {
            checkStones(stonesPlayerOne);
            checkStones(stonesPlayerTwo);

            int row = 0;
            int index = 0;
            for (int stonesInRow = boardSize.getFirstRowStoneCount(); stonesInRow > 0; stonesInRow--) {
                for (int column = 0; column < stonesInRow; column++) {
                    final Token token = new Token(Player.ONE, stonesPlayerOne[index]);
                    this.board.setCell(token, row, column);
                    index++;
                }
                row++;
            }

            final int sizeMinusOne = this.boardSize.getBoardSize() - 1;
            row = sizeMinusOne;
            index = stonesPlayerTwo.length - 1;
            for (int stonesInRow = boardSize.getFirstRowStoneCount(); stonesInRow > 0; stonesInRow--) {
                for (int column = sizeMinusOne; sizeMinusOne - column < stonesInRow; column--) {
                    final Token token = new Token(Player.TWO, stonesPlayerTwo[index]);
                    this.board.setCell(token, row, column);
                    index--;
                }
                row--;
            }

            this.started = true;
            this.activePlayer = Player.ONE;
        } else {
            throw new IllegalStateException("game already started");
        }
    }

    private void checkStones(final int[] stones) throws IllegalArgumentException {
        final int stoneCount = this.boardSize.getStoneCount();
        if (stones.length != stoneCount) {
            throw new IllegalArgumentException("not enough stones given");
        }

        for (final int stone : stones) {
            if (stone <= 0 || stone > stoneCount) {
                throw new IllegalArgumentException("stone with value " + stone + " does not exist");
            }
        }
    }

    /**
     * Rolls the dice to the given number.
     * 
     * @param number
     *            - the given number
     * @throws IllegalArgumentException
     *             - if roll value is not valid
     * @throws IllegalStateException
     *             - if game is not started or dice is already rolled
     */
    public void roll(final int number) throws IllegalArgumentException, IllegalStateException {
        if (this.started && !this.won) {
            if (this.rollNumber == NOT_ROLLED) {
                if (number <= 0 || number > this.boardSize.getStoneCount()) {
                    throw new IllegalArgumentException("roll value " + number + " does not exist");
                }
                this.rollNumber = number;
            } else {
                throw new IllegalStateException("dice already rolled");
            }
        } else {
            throw new IllegalStateException("game not started");
        }
    }

    /**
     * Moves the token of active player with the given number to the given
     * coordinates if and only if it is a possible move.
     * 
     * @param tokenNumber
     *            - the token number of the token to be moved
     * @param row
     *            - the row to which the token should be moved
     * @param column
     *            - the column to which the token should be moved
     * @return whether game was won after this move
     * @throws IllegalArgumentException
     *             - if an argument is invalid
     * @throws IllegalStateException
     *             - if game is not started
     */
    public boolean moveToken(final int tokenNumber, final int row, final int column)
            throws IllegalArgumentException, IllegalStateException {
        if (this.rollNumber != NOT_ROLLED) {
            final SortedSet<Token> tokenSet = this.board.getTokens(this.activePlayer);
            final Token exactToken = new Token(this.activePlayer, this.rollNumber);
            final Token selectedToken;
            if (tokenSet.contains(exactToken)) {
                selectedToken = tokenSet.tailSet(exactToken).first();
            } else {
                final SortedSet<Token> headSet = tokenSet.headSet(exactToken);
                final SortedSet<Token> tailSet = tokenSet.tailSet(exactToken);
                final Token upperPossibleToken = tailSet.isEmpty() ? null : tailSet.first();
                final Token lowerPossibleToken = headSet.isEmpty() ? null : headSet.last();
                selectedToken = tokenNumber > this.rollNumber ? upperPossibleToken : lowerPossibleToken;
            }
            if (selectedToken.getValue() == tokenNumber) {
                this.board.move(selectedToken, row, column);
            } else {
                throw new IllegalArgumentException("token number is not valid for roll number " + this.rollNumber);
            }
            this.won = winCheck();

            this.rollNumber = NOT_ROLLED;
            this.activePlayer = this.activePlayer == Player.ONE ? Player.TWO : Player.ONE;
            return this.won;
        } else {
            throw new IllegalStateException("dice not rolled");
        }
    }

    private boolean winCheck() {
        if (this.activePlayer == Player.ONE) {
            final int boardMax = this.boardSize.getBoardSize() - 1;
            return this.board.getCell(boardMax, boardMax).getPlayer() == Player.ONE
                    || getTokenCountOfInactivePlayer() == 0;
        }
        return this.board.getCell(0, 0).getPlayer() == Player.TWO || getTokenCountOfInactivePlayer() == 0;
    }

    /**
     * Gets the winner after moveToken returned true.
     * 
     * @return the winner after moveToken returned true, otherwise the player which
     *         is not active
     */
    public Player getWinner() {
        return this.activePlayer == Player.ONE ? Player.TWO : Player.ONE;
    }

    /**
     * Gets the token at the given coordinates.
     * 
     * @param row
     *            - the row of the cell
     * @param column
     *            - the column of the cell
     * @return the token at the given coordinates
     * @throws IllegalArgumentException
     *             - if coordinates are invalid
     */
    public Token getCell(final int row, final int column) throws IllegalArgumentException {
        return this.board.getCell(row, column);
    }

    /**
     * Gets the count of tokens of the active player.
     * 
     * @return the count of tokens of the active player
     * @throws IllegalStateException
     *             - if game is not started or game has already ended
     */
    public int getTokenCountOfActivePlayer() throws IllegalStateException {
        if (!this.started) {
            throw new IllegalStateException("game not started");
        } else if (this.won) {
            throw new IllegalStateException("game already ended");
        }

        return this.board.getTokens(this.activePlayer).size();
    }

    private int getTokenCountOfInactivePlayer() {
        final Player other = this.activePlayer == Player.ONE ? Player.TWO : Player.ONE;
        return this.board.getTokens(other).size();
    }

    @Override
    public String toString() {
        return this.board.toString();
    }
}
