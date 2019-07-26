package student.program;

import kit.Robot;

public class WallFollowingProgram extends AbstractProgram {
    
    public WallFollowingProgram(Robot robot) {
        super(robot);
    }

    @Override
    public ReturnState run() {
        if (!robot.wallDetected()) {
            return ReturnState.DONE;
        }
        
        for (int i = 0; i < timeout; ++i) {
            // staring at wall? turn
            while (robot.wallDetected()) {
                turnLeft();
            }
            
            // move
            robot.forward();
            
            if (robot.tangleDetected()) {
                return ReturnState.TANGLE;
            } else if (robot.dirtDetected()) {
                return ReturnState.DIRT;
            }
            
            // turn to wall
            turnRight();
        }
        
        turnLeft();
        turnLeft();
        
        return ReturnState.DONE;
    }

}
