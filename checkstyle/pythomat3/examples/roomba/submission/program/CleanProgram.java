package student.program;

import kit.Robot;

public class CleanProgram extends AbstractProgram {
        
    public CleanProgram(Robot robot) {
        super(robot);
    }

    @Override
    public ReturnState run() {
         for (int i = 0; i < 4; ++i) {
            robot.turnLeft();
            
            if (!robot.wallDetected()) {
                robot.forward();
                if (robot.tangleDetected()) {
                    return ReturnState.TANGLE;
                }
            }
        }
        
        if (robot.dirtDetected()) {
            return ReturnState.DIRT;
        } else {
            return ReturnState.DONE;
        }
    }

}
