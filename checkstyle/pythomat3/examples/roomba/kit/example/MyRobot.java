package kit.example;

import kit.Robot;

/**
 * @author markus
 * @version 1
 */
public class MyRobot implements Robot {

    /**
     * @author markus
     * @version 1
     */
    public enum Direction { NORTH, EAST, SOUTH, WEST, UNDEFINED }
            
    private World world;
    private int xpos;
    private int ypos;
    private Direction dir;

    /**
     * Public constructor initializes the robot.
     * @param world The world the robot has to clean
     * @param xpos The horizontal coordinate of the robot's position in the world
     * @param ypos The vertical coordinate of the robot's position in the world
     * @param dir The direction in which this robot looks
     */
    public MyRobot(World world, int xpos, int ypos, Direction dir) {
        this.world = world;
        this.xpos = xpos;
        this.ypos = ypos;
        this.dir = dir;
    }
    
    /**
     * Sets the current position and direction of the robot in the world
     * and prints the world.
     */
    protected void printWorld() {
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        world.setRobot(xpos, ypos, dir);
        System.out.println(world);
    }
    
    public String worldToString() {
        world.setRobot(xpos, ypos, dir);
        return world.toString();
    }

    /* (non-Javadoc)
     * @see kit.Robot#forward()
     */
    @Override
    public void forward() {
        world.clean(xpos, ypos);
        
        switch (dir) {
        case NORTH: --ypos; break;
        case SOUTH: ++ypos; break;
        case EAST: ++xpos; break;
        case WEST: --xpos; break;
        default: throw new RuntimeException("Unknown Direction");
        }
        
        if (world.hasWallAt(xpos, ypos)) {
            throw new RuntimeException("Invalid Move");
        }
    }

    /* (non-Javadoc)
     * @see kit.Robot#untangle()
     */
    @Override
    public void untangle() {
        switch (dir) {
        case NORTH: ++ypos; break;
        case SOUTH: --ypos; break;
        case EAST: --xpos; break;
        case WEST: ++xpos; break;
        default: throw new RuntimeException("Unknown Direction");
        }
        
        if (world.hasWallAt(xpos, ypos)) {
            throw new RuntimeException("Invalid Move");
        }
    }

    /* (non-Javadoc)
     * @see kit.Robot#turnLeft()
     */
    @Override
    public void turnLeft() {
        switch (dir) {
        case NORTH: dir = Direction.WEST; break;
        case SOUTH: dir = Direction.EAST; break;
        case EAST: dir = Direction.NORTH; break;
        case WEST: dir = Direction.SOUTH; break;
        default: throw new RuntimeException("Unknown Direction");
        }
    }

    /* (non-Javadoc)
     * @see kit.Robot#turnRight()
     */
    @Override
    public void turnRight() {
        switch (dir) {
        case NORTH: dir = Direction.EAST; break;
        case SOUTH: dir = Direction.WEST; break;
        case EAST: dir = Direction.SOUTH; break;
        case WEST: dir = Direction.NORTH; break;
        default: throw new RuntimeException("Unknown Direction");
        }
    }

    /* (non-Javadoc)
     * @see kit.Robot#tangleDetected()
     */
    @Override
    public boolean tangleDetected() {
        return world.hasTanglesAt(xpos, ypos);
    }

    /* (non-Javadoc)
     * @see kit.Robot#dirtDetected()
     */
    @Override
    public boolean dirtDetected() {
        return world.isDirtyAt(xpos, ypos);
    }

    /* (non-Javadoc)
     * @see kit.Robot#wallDetected()
     */
    @Override
    public boolean wallDetected() {
        switch (dir) {
        case NORTH: return world.hasWallAt(xpos, ypos - 1);
        case SOUTH: return world.hasWallAt(xpos, ypos + 1);
        case EAST: return world.hasWallAt(xpos + 1, ypos);
        case WEST: return world.hasWallAt(xpos - 1, ypos);
        default: return false;
        }
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        switch (dir) {
        case NORTH: return "^";
        case SOUTH: return "v";
        case EAST: return ">";
        case WEST: return "<";
        default: return "?";
        }
    }
}
