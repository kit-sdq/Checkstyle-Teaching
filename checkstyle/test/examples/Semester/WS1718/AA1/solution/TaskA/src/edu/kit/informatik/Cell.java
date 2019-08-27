package edu.kit.informatik;

public class Cell {
    private int i;
    private int j;
    private Player player;

    /**
     * Constructs a new empty cell.
     * 
     * @param i The row coordinate of the Cell in the board it is a part of.
     * @param j The column coordinate of the Cell in the board it is a part of.
     */
    public Cell(int i, int j) {
        this.i = i;
        this.j = j;
        this.player = null;
    }

    /**
     * Constructs a new cell occupied by a player.
     *
     * @param i The row coordinate of the Cell in the board it is a part of.
     * @param j The column coordinate of the Cell in the board it is a part of.
     * @param player The player that occupies that cell.
     */
    public Cell(int i, int j, Player player) {
        this.i = i;
        this.j = j;
        this.player = player;
    }

    /**
     * Gets the player that occupies the cell.
     * 
     * @return the occupying player or <code>null</code> if the cell is empty.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the player that occupies the cell.
     * 
     * @param newPlayer The player to occupy this cell.
     */
    public void setPlayer(Player newPlayer) {
        this.player = newPlayer;
    }
    
    @Override
    public String toString() {
        return player != null ? "P" + String.valueOf(player.getToken()) : "**";  
    }
}
