package kit;

/**
 * An interface to the basic cleaning robot's controls and inputs.
 * @author markus
 * @version 1
 */
public interface Robot {
    /**
     * Moves the robot one field forward (into viewing direction).
     */
    void forward();
    /**
     * Turns the robot left by 90 degrees.
     */
    void turnLeft();
    /**
     * Turns the robot right by 90 degrees.
     */
    void turnRight();
    
    /**
     * Moves the robot one field backwards (opposite of viewing direction).
     */
    void untangle();
    
    /**
     * The robot is able to detect if it currently is on a tangle.
     * @return true if the robot is on a tangle, false otherwise
     */
    boolean tangleDetected();
    /**
     * If the current field is extraordinarily dirty this method returns true.
     * @return true if the current field is extraordinarily dirty, false otherwise
     */
    boolean dirtDetected();
    /**
     * The robot can detect if it is directly in front of a wall (in viewing direction).
     * @return true if on the next field forward is an obstacle, false otherwise 
     */
    boolean wallDetected();
}
