package student.program;

import kit.Robot;

public abstract class AbstractProgram implements Program {
    
    int[] seq_timeout = {7,13,31, 2,3,17, 5,23,11, 3,19,29, 2,3,7, 13,17};
    boolean[] seq_turn = {true,true,true, true,false,false, false};
    
    Robot robot;
    int timeout = 0;
    static int nInstances = 0;
    boolean flipTurnDirection = true;
    
    public AbstractProgram(Robot robot) {
        this.robot = robot;
        ++nInstances;        
        timeout = seq_timeout[nInstances % seq_timeout.length];
        flipTurnDirection = seq_turn[nInstances % seq_turn.length];
    }
        
    public abstract ReturnState run();
    
    public void turnLeft() {
        if (flipTurnDirection) {
            robot.turnRight();
        } else {
            robot.turnLeft();
        }
    }
    
    public void turnRight() {
        if (flipTurnDirection) {
            robot.turnLeft();
        } else {
            robot.turnRight();
        }
    }
    
    public ReturnState forward() {
        if (robot.wallDetected()) {
            return ReturnState.WALL;
        }
        
        robot.forward();

        if (robot.tangleDetected()) {
            return ReturnState.TANGLE;
        } else if (robot.dirtDetected()) {
            return ReturnState.DIRT;
        }
        
        return ReturnState.DONE;
    }
}
