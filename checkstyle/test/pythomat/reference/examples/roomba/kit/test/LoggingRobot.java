package kit.test;

import java.util.ArrayList;
import java.util.List;

import kit.example.MyRobot;
import kit.example.World;

public class LoggingRobot extends MyRobot {

	private String log;
	private List<String> errors = new ArrayList<String>();
    private boolean dirtCrossed = false;
	
	boolean wallChecked = false;
	boolean tangleChecked = true;

    public LoggingRobot(World world, int xpos, int ypos, Direction dir) {
		super(world, xpos, ypos, dir);
		log = new String();
    }
    
    public boolean hasErrors() {
        return !errors.isEmpty();
    }
    
    public List<String> getErrorList() {
        List<String> result = errors;
        errors = new ArrayList<String>();
        return result;
    }
    
    public boolean getDirtCrossed() {
        boolean result = dirtCrossed;
        dirtCrossed = false;
        return result;
    }
    
    @Override
    public void forward() {
        if (!wallChecked) {
            errors.add("Forward without wall-check");
        }
        if (!tangleChecked) {
            errors.add("Forward without tangle-check");
        }
        if (tangleDetected()) {
            errors.add("Forward over tangle");
        }
        if (wallDetected()) {
            errors.add("Forward into wall");
        }
        if (dirtDetected()) {
            dirtCrossed = true;
        }
        
        log += 'f';
        super.forward();
		
		wallChecked = false;
	    tangleChecked = false;
	}

    @Override
    public void untangle() {
        if (!tangleDetected()) {
            errors.add("Untangle without tangle");
        }
        log += 'u';
		super.untangle();
    }

    @Override
    public void turnLeft() {
        if (tangleDetected()) {
            errors.add("Turn over tangle");
        }
        log += 'l';
		super.turnLeft();
    }

    @Override
    public void turnRight() {
        if (tangleDetected()) {
            errors.add("Turn over tangle");
        }
        log += 'r';
		super.turnRight();
    }
    
    @Override
    public boolean wallDetected() {
        wallChecked = true;
        return super.wallDetected();
    }
    
    @Override
    public boolean dirtDetected() {
        return super.dirtDetected();
    }
    
    @Override
    public boolean tangleDetected() {
        tangleChecked = true;
        return super.tangleDetected();
    }

	public String getLog() {
		String ret = log;
		log = new String();
		return ret;
	}
}
