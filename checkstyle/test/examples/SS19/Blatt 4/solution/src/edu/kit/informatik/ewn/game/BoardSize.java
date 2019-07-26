/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.ewn.game;

/**
 * The BoardSize enum represents the allowed board sizes.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public enum BoardSize {
    /**
     * The board size 5.
     */
    FIVE(5, 6), 
    /**
     * The board size 7.
     */
    SEVEN(7, 10);
    
    private final int boardSize;
    private final int stoneCount;
    
    /**
     * BoardSize constructor.
     * 
     * @param boardSize - the board size
     * @param stoneCount - the count of stones on this board per player at start of the game
     */
    BoardSize(final int boardSize, final int stoneCount) {
        this.boardSize = boardSize;
        this.stoneCount = stoneCount;
    }

    /**
     * Gets the board size.
     * 
     * @return the board size
     */
    public int getBoardSize() {
        return this.boardSize;
    }
    
    /**
     * Gets the stone count.
     * 
     * @return the stone count
     */
    public int getStoneCount() {
        return this.stoneCount;
    }
    
    /**
     * Gets the count of stones in the first row at start of the game.
     * 
     * @return the count of stones in the first row at start of the game
     */
    protected int getFirstRowStoneCount() {
        return this.stoneCount == 6 ? 3 : 4;
    }
    
    /**
     * Gets the board size to the given size string.
     * 
     * @param sizeStr - the given size string
     * @return the board size representing the given size string
     */
    public static BoardSize of(final String sizeStr) {
        for (final BoardSize size : values()) {
            if (Integer.parseInt(sizeStr) == size.boardSize) {
                return size;
            }
        }
        
        return null;
    }
}

