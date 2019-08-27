package edu.kit.informatik.wincheck;

import edu.kit.informatik.Board;
import edu.kit.informatik.Player;

public interface WinCheck {
    /**
     * The distance to check at maximum in one direction.
     */
    int ROW_LENGTH = 5;

    /**
     * Alters the row coordinate in a direction dependent on the win check type.
     * 
     * @param i The row coordinate to alter.
     * @param offset The amount to alter the coordinate.
     * @param direction The direction to alter the coordinate in.
     * @return The altered coordinate.
     */
    int alterI(int i, int offset, Direction direction);

    /**
     * Alters the column coordinate in a direction dependent on the win check type.
     *
     * @param j The column coordinate to alter.
     * @param offset The amount to alter the coordinate.
     * @param direction The direction to alter the coordinate in.
     * @return The altered coordinate.
     */
    int alterJ(int j, int offset, Direction direction);

    /**
     * Checks if the player has won with his recent move in a way that the implementation defines.
     * 
     * @param board The board to check on.
     * @param player The player to check the win condition for.
     * @param i The row coordinate of the newly occupied cell.
     * @param j The column coordinate of the newly occupied cell.
     * @return true, if the player has won.
     */
    default boolean check(Board board, Player player, int i, int j) {
        //The newly occupied field is already a hit
        int counter = 1;

        for (Direction direction : Direction.values()) {
            for (int offset = 1; offset < ROW_LENGTH; offset++) {
                int alteredI = alterI(i, offset, direction);
                int alteredJ = alterJ(j, offset, direction);
                
                //getOccupyingPlayer returns null if the coordinates are out of bounds or the cell is not occupied
                //-> smth.equals(null) is always false
                if (player.equals(board.getOccupyingPlayer(alteredI, alteredJ))) {
                    counter++;
                } else {
                    //We can stop checking because there is a hole in the row
                    break;
                }
            }
        }

        return counter >= 5;
    }
}
