package kit.example;

/**
 * Robot that prints the world-state after every action.
 * @author markus
 * @version 1
 */
public class PrintingRobot extends MyRobot {

    /**
     * Public constructor just wraps the super-constructor.
     * @param world The world the robot has to clean
     * @param xpos The horizontal coordinate of the robot's position in the world
     * @param ypos The vertical coordinate of the robot's position in the world
     * @param dir The direction in which this robot looks
     */
    public PrintingRobot(World world, int xpos, int ypos, Direction dir) {
		super(world, xpos, ypos, dir);
    }
   
    /* (non-Javadoc)
     * @see kit.MyRobot#forward()
     */
    @Override
    public void forward() {
		super.forward();
		printWorld();
	}

    /* (non-Javadoc)
     * @see kit.MyRobot#untangle()
     */
    @Override
    public void untangle() {
		super.untangle();
		printWorld();
    }

    /* (non-Javadoc)
     * @see kit.MyRobot#turnLeft()
     */
    @Override
    public void turnLeft() {
		super.turnLeft();
		printWorld();
    }

    /* (non-Javadoc)
     * @see kit.MyRobot#turnRight()
     */
    @Override
    public void turnRight() {
		super.turnRight();
		printWorld();
    }
}
