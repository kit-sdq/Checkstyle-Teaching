package student.program;

import kit.Robot;

public class UntangleProgram extends AbstractProgram {

    public UntangleProgram(Robot robot) {
        super(robot);
    }

    @Override
    public ReturnState run() {
        robot.untangle();
        robot.turnLeft();
        while (robot.wallDetected()) {
            robot.turnLeft();
        }
        return ReturnState.DONE;
    }

}
