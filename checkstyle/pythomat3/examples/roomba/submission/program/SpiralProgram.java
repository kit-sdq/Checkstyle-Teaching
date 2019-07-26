package student.program;

import kit.Robot;

public class SpiralProgram extends AbstractProgram {
    
    public SpiralProgram(Robot robot) {
        super(robot);
    }

    @Override
    public ReturnState run() {
        int i = 0;
        int length = 1;
        while (i < timeout) {
            for (int j = 0; j < length; ++j) {                
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
            
            ++i;
            if (i % 2 == 0) ++length;
                        
            turnLeft();
        }
        return ReturnState.DONE;
    }

}
