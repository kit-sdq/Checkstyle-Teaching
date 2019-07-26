package student;

import kit.Robot;
import student.program.*;
import student.program.Program.ReturnState;

public class Scheduler {
    Robot robot;
    
    TransitionRelation trans;
    
    public static Programs prog;
        
    public Scheduler(Robot robot) {
        this.robot = robot;
        prog = Programs.ROOM_CROSSING;
        initializeTransitionRelation();
    }
    
    private void initializeTransitionRelation() {
        trans = new TransitionRelation();
        // regular transitions
        trans.add(trans.new Key(Programs.ROOM_CROSSING, ReturnState.DONE), Programs.SPIRAL);
        trans.add(trans.new Key(Programs.ROOM_CROSSING, ReturnState.WALL), Programs.WALL_FOLLOWING);
        trans.add(trans.new Key(Programs.SPIRAL, ReturnState.DONE), Programs.ROOM_CROSSING);
        trans.add(trans.new Key(Programs.SPIRAL, ReturnState.WALL), Programs.WALL_FOLLOWING);
        trans.add(trans.new Key(Programs.WALL_FOLLOWING, ReturnState.DONE), Programs.ROOM_CROSSING);
        trans.add(trans.new Key(Programs.WALL_FOLLOWING, ReturnState.WALL), Programs.WALL_FOLLOWING); //avoid
        // interrupt transitions (entry)
        trans.add(trans.new Key(Programs.ROOM_CROSSING, ReturnState.DIRT), Programs.CLEAN);
        trans.add(trans.new Key(Programs.ROOM_CROSSING, ReturnState.TANGLE), Programs.UNTANGLE);
        trans.add(trans.new Key(Programs.SPIRAL, ReturnState.DIRT), Programs.CLEAN);
        trans.add(trans.new Key(Programs.SPIRAL, ReturnState.TANGLE), Programs.UNTANGLE);
        trans.add(trans.new Key(Programs.WALL_FOLLOWING, ReturnState.DIRT), Programs.CLEAN);
        trans.add(trans.new Key(Programs.WALL_FOLLOWING, ReturnState.TANGLE), Programs.UNTANGLE);
        // interrupt transitions (exit)
        trans.add(trans.new Key(Programs.CLEAN, ReturnState.DIRT), Programs.CLEAN);
        trans.add(trans.new Key(Programs.CLEAN, ReturnState.TANGLE), Programs.UNTANGLE);
        trans.add(trans.new Key(Programs.CLEAN, ReturnState.DONE), Programs.ROOM_CROSSING);
        trans.add(trans.new Key(Programs.CLEAN, ReturnState.WALL), Programs.ROOM_CROSSING);
        trans.add(trans.new Key(Programs.UNTANGLE, ReturnState.DIRT), Programs.CLEAN);
        trans.add(trans.new Key(Programs.UNTANGLE, ReturnState.TANGLE), Programs.UNTANGLE);
        trans.add(trans.new Key(Programs.UNTANGLE, ReturnState.DONE), Programs.ROOM_CROSSING);
        trans.add(trans.new Key(Programs.UNTANGLE, ReturnState.WALL), Programs.ROOM_CROSSING);
    }
    
    boolean isInterrupt(Programs prog) {
        return prog == Programs.CLEAN || prog == Programs.UNTANGLE;
    }
    
    public void run(int timeout) {
        int step = 0;
        
        while (step < timeout) {
            // increment counter
            ++step;
            
            // run program
            ReturnState state = prog.getInstance(robot).run();            
            
            // evaluate return code
            prog = trans.get(trans.new Key(prog, state));
            
            if (prog == null) {
                System.out.println("Error! No Entry for transition (" + prog + " * " + state + ")");
                System.exit(1);
            }
        }
    }
}
