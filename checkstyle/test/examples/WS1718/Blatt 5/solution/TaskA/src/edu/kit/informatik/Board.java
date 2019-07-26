package edu.kit.informatik;

import java.util.StringJoiner;

public class Board {
    private Cell[][] board;

    /**
     * The constructor for a new playing board consisting of cells.
     * 
     * @param width The width of the board.
     * @param height The height of the board.
     */
    public Board(int width, int height) {
        this.board = new Cell[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                board[i][j] = new Cell(i, j);
            }
        }
    }

    /**
     * Gets a cell of the board with specific coordinates.
     * 
     * @param i The row coordinate of the cell to get.
     * @param j The column coordinate of the cell to get.
     * @return The cell.
     */
    public Cell getCell(int i, int j) {
        if (!boundCheck(i, j)) {
            return null;
        }
        
        return board[i][j];
    }

    /**
     * Gets the player that occupies a cell with specified coordinates.
     * 
     * @param i The row coordinate of the cell to get the occupying player from.
     * @param j The column coordinate of the cell to get the occupying player from.
     * @return The player occupying the cell or <code>null</code> if the cell is empty.
     */
    public Player getOccupyingPlayer(int i, int j) {
        Cell cell = getCell(i, j);
        
        if (cell == null) {
            return null;
    }
        
        return getCell(i, j).getPlayer();
    }

    /**
     * Occupies the cell with the specified coordinates with a given player.
     * 
     * @param i The row coordinate of the cell to get occupied.
     * @param j The column coordinate of the cell to get occupied.
     * @param player The player to occupy the cell.
     * @return true if the player occupies the cell now, false if the cell isn't empty.
     */
    public boolean occupy(int i, int j, Player player) {
        Cell cell = getCell(i, j);
        
        if (cell == null || cell.getPlayer() != null) {
            return false;
        }
        
        cell.setPlayer(player);
        return true;
    }
    
    @Override
    public String toString() {
        StringJoiner result = new StringJoiner("\n");
        for (Cell[] row : board) {
            StringJoiner rowJoiner = new StringJoiner(" ");
            for (Cell cell : row) {
                rowJoiner.add(cell.toString());
            }
            result.add(rowJoiner.toString());
        }
        
        return result.toString();
    }

    private boolean boundCheck(int i, int j) {
        return i >= 0 && j >= 0 && i < board.length && j < board[i].length;
    }
}
