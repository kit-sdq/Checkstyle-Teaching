package student.program;

import kit.Robot;

public enum Programs {
    ROOM_CROSSING() {
        public Program getInstance(Robot robot) {
            return new RoomCrossingProgram(robot);
        }
    },
    SPIRAL {
        public Program getInstance(Robot robot) {
            return new SpiralProgram(robot);
        }
    }, 
    WALL_FOLLOWING {
        public Program getInstance(Robot robot) {
            return new WallFollowingProgram(robot);
        }
    }, 
    UNTANGLE {
        public Program getInstance(Robot robot) {
            return new UntangleProgram(robot);
        }
    }, 
    CLEAN {
        public Program getInstance(Robot robot) {
            return new CleanProgram(robot);
        }
    };
    
    public abstract Program getInstance(Robot robot);
    
    public static final int length = Programs.values().length;
}
