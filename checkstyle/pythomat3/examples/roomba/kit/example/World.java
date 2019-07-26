package kit.example;

import kit.example.MyRobot.Direction;

/**
 * @author markus
 * @version 1
 */
public class World {
    
    /**
     * Constant Integer to indicate a "tangled" world state.
     */
    public static final int TANGLE = -2;
    /**
     * Constant Integer to indicate the presence of an obstacle.
     */
    public static final int WALL = -1;
    /**
     * Constant Integer to indicate a "clean" world state.
     */
    public static final int CLEAN = 0;
    /**
     * Constant Integer to indicate "dust".
     */
    public static final int DUSTY = 1;
    /**
     * Constant Integer to indicate "dirt".
     */
    public static final int DIRTY = 2;
    
    /**
     * Matrix that stores the discrete world state.
     * First dimension represents the y-axis, 
     * second dimension the x-axis of the world
     */
    private int[][] matrix;
    
    private int robotX = -1;
    private int robotY = -1;
    private Direction direction = Direction.UNDEFINED;
    
    /**
     * Public constructor initializes a world object in its initial state. 
     * @param matrix the initial world state (entries should be in the interval [-2,2])
     */
    public World(int[][] matrix) {
        this.matrix = matrix;
    }
    
    /**
     * Sets the current position and direction of a robot. 
     * This is respected in the string-representation of the world.
     * @see kit.example.World#toString()
     * @param x the robot's horizontal position
     * @param y the robot's vertical position
     * @param direction the viewing direction of the robot
     */
    public void setRobot(int x, int y, Direction direction) {
        this.robotX = x;
        this.robotY = y;
        this.direction = direction;
    }
    
    /**
     * Checks for presence of a wall at the given coordinate.
     * @param x the horizontal coordinate
     * @param y the vertical coordinate
     * @return true in presence of an obstacle, false otherwise
     */
    public boolean hasWallAt(int x, int y) {
        if (x < 0 || y < 0) return true;
        if (y >= matrix.length) return true;
        if (x >= matrix[y].length) return true;
        return matrix[y][x] == WALL;
    }
    
    /**
     * Checks for presence of dirt at the given coordinate.
     * @param x the horizontal coordinate
     * @param y the vertical coordinate
     * @return true in presence of dirt, false otherwise
     */
    public boolean isDirtyAt(int x, int y) {
        return matrix[y][x] >= DIRTY;
    }
    
    /**
     * Checks for presence of tangles at the given coordinate.
     * @param x the horizontal coordinate
     * @param y the vertical coordinate
     * @return true in presence of tangles, false otherwise
     */
    public boolean hasTanglesAt(int x, int y) {
        return matrix[y][x] <= TANGLE;
    }
    
    /**
     * Decrement the dirt state at the given coordinate. 
     * @param x the horizontal coordinate
     * @param y the vertical coordinate
     */
    public void clean(int x, int y) {
        if (matrix[y][x] > CLEAN) --matrix[y][x];
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < matrix.length; ++row) {
            for (int col = 0; col < matrix[row].length; ++col) {
                if (robotX == col && robotY == row) {
                    switch (direction) {
                    case NORTH: builder.append('^'); break;
                    case EAST: builder.append('>'); break;
                    case SOUTH: builder.append('v'); break;
                    case WEST: builder.append('<'); break;
                    default: builder.append('x');
                    }
                    continue;
                }
                switch (matrix[row][col]) {
                case TANGLE: builder.append('~'); break;
                case WALL: builder.append('#'); break;
                case CLEAN: builder.append(' '); break;
                case DUSTY: builder.append('1'); break;
                case DIRTY: builder.append('2'); break;
                default: builder.append('?'); break;
                }
            }
            builder.append('\n'); 
        }
        return builder.toString();
    }
}
