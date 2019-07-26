package student.program;

import kit.Robot;

public class RoomCrossingProgram extends AbstractProgram {
    
    public RoomCrossingProgram(Robot robot) {
        super(robot);
    }

    @Override
    public ReturnState run() {
        for (int i = 0; i < timeout; ++i) {
            if (robot.wallDetected()) {
                return ReturnState.WALL;
            }
            
            robot.forward();
            
            if (robot.tangleDetected()) {
                return ReturnState.TANGLE;
            } else if (robot.dirtDetected()) {
                return ReturnState.DIRT;
            }
        }
        
        return ReturnState.DONE;
    }
    
}
