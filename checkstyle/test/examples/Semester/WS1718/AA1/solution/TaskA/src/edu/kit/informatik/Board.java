package edu.kit.informatik;

import java.util.StringJoiner;

public class Board {
    protected Cell[][] board;
    private int occupiedCellCount;

    public Board(int size) {
        this.board = new Cell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
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
    public Cell getCell(int i, int j) throws CellOutOfBoundsException {
        if (i < 0 || j < 0 || i >= board.length || j >= board[i].length) {
            throw new CellOutOfBoundsException();
        }
        
        return board[i][j];
    }

    public Player getOccupyingPlayer(int i, int j) throws CellOutOfBoundsException {
        return getCell(i, j).getPlayer();
    }

    public void occupy(int i, int j, Player player) throws CellOutOfBoundsException {
        getCell(i, j).setPlayer(player);
        occupiedCellCount++;
    }
    
    public int getBoardSize() {
        return board.length;
    }
    
    public int getCellCount() {
        return board.length * board.length;
    }
    
    public boolean isFull() {
        return occupiedCellCount == getCellCount();
    }
    
    public String rowToString(int row) {
        if (row < 0 || row >= getBoardSize()) {
            return null;
        }
        
        StringJoiner rowJoiner = new StringJoiner(" ");
        for (Cell cell : board[row]) { 
            rowJoiner.add(cell.toString());
        }
        return rowJoiner.toString();
    }
    
    public String colToString(int col) {
        if (col < 0 || col >= getBoardSize()) {
            return null;
        }
        
        StringJoiner colJoiner = new StringJoiner(" ");
        for (Cell[] row : board) {
            colJoiner.add(row[col].toString());
        }
        return colJoiner.toString();  
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
}
