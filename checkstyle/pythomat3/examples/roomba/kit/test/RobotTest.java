package kit.test;

import java.io.*;
import java.util.*;

import student.Scheduler;
import kit.example.InputFormatException;
import kit.example.MyRobot.Direction;
import kit.example.World;
import kit.test.LogInterpreter.Programs;

/**
 * Test program to run the student scheduler on an example world 
 * and visualize the behavior of the robot.
 * @author markus
 * @version 1
 */
public final class RobotTest {
    
    final static Programs[] allPrograms = {
        Programs.SPIRAL, Programs.ROOM_CROSSING, Programs.WALL_FOLLOWING, 
        Programs.CLEAN, Programs.UNTANGLE
    };
    
    final static int MAX_ERROR = 1000;
    
    private RobotTest() { }

    /**
     * Main program, initializes the given world and runs the cleaning robot. 
     * First command-line argument must specify a path to the world description. 
     * Second argument might specify a timeout which is the number of programs the 
     * cleaning program is allowed to run. This timeout is set to 100 by default.
     * @param args Array of command-line arguments.
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            println("Usage: RoombaTest path-to-world-description-file [timeout]");
            System.exit(0);
        }
        
        File file = new File(args[0]);
        
        int timeout = 100;
        if (args.length > 1) {
            try {
                timeout = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                println("Second argument must be a natural number");
                System.exit(1);
            }
        }

        boolean immediateExitOnError = true;
        if (args.length > 2) {
            immediateExitOnError = Boolean.parseBoolean(args[2]);
        }
        
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            println("Path to world description file invalid. File not found");
            System.exit(1);
        }
                
        // initialize matrix-size
        World world = null;
        LoggingRobot robot = null;
        try {
            int[][] matrix = initMatrixSize(fileReader);
            fileReader = new FileReader(file);
            world = initWorld(fileReader, matrix);
            fileReader = new FileReader(file);
            robot = initRobot(fileReader, world);
        } catch (IOException e) {
            println("Error: " + e.getMessage());
            System.exit(1);
        } catch (InputFormatException e) {
            println("Error: " + e.getMessage());
            System.exit(1);
        }
        
        String initialState = robot.worldToString();
        
        // start cleaning program
		Scheduler cleaningProgram = new Scheduler(robot);
		LogInterpreter log = new LogInterpreter(robot);
		        				
		Set<Programs> lastPrograms = new TreeSet<Programs>();
		Set<Programs> expectedPrograms = new TreeSet<Programs>();
        expectedPrograms.add(Programs.ROOM_CROSSING);
        
        int errorCount = 0;
        int i = 0;
		for (i = 0; i < timeout; ++i) {
		    String lastWorld = robot.worldToString();
		    
		    try {
		        cleaningProgram.run(1);
		    } catch(RuntimeException e) {
		        println("Danger, caught runtime exception: " + e.getMessage());
		    }
	        
		    // interpret program-trace and intersect with expectations
	        Set<Programs> interpretedPrograms = log.getPossiblePrograms();
            TreeSet<Programs> intersection = new TreeSet<Programs>(expectedPrograms);
            intersection.retainAll(interpretedPrograms);
            
            // classify errors
            boolean uninterpretableTrace = interpretedPrograms.size() == 0;
            boolean robotErrors = robot.hasErrors();
            boolean dirtWithoutClean = robot.getDirtCrossed() && !interpretedPrograms.contains(Programs.CLEAN);
            boolean unexpectedProgram = intersection.isEmpty() && interpretedPrograms.size() > 0;
            
            boolean errorOccured = uninterpretableTrace || robotErrors || dirtWithoutClean || unexpectedProgram;
            
            if (errorOccured) {
                println("World-state before program execution: ");
                println(lastWorld);
                println("Last program looked like " + setToString(lastPrograms) + " so we now expected " + setToString(expectedPrograms) +".");
                println();
                
                if (uninterpretableTrace) {
                    println("UNCLASSIFIABLE EXECUTION TRACE: " + log.getLastTrace());
                    println();
                } else {
                    println("The execution-trace " + log.getLastTrace() + " looks like the program-trace of " + setToString(interpretedPrograms) + ".");
                    println();
                }
                    
                if (robotErrors) {
                    List<String> errors = robot.getErrorList();
                    for (String error : errors) {
                        println("INVALID ACTION: " + error);
                    }
                    println();
                }
                
                if (dirtWithoutClean) {
                    println("INVALID ACTION: Robot crossed dirt, while not running clean-program");
                    println();
                }
                
                if (unexpectedProgram) {
                    println("UNEXPECTED PROGRAM: " + setToString(interpretedPrograms));
                    println();
                }

                if (immediateExitOnError) {
                    System.exit(137);
                } else {
                    ++errorCount;
                    if (errorCount > MAX_ERROR) {
                        println("Reached maximum number of errors ( " + MAX_ERROR + " ) after " + i + " program executions");
                        break;
                    }
                    println();
                }
                
                lastPrograms = new TreeSet<Programs>(Arrays.asList(allPrograms));
            } else {
                lastPrograms = intersection;
            }

            expectedPrograms = calculateExpectedPrograms(robot, lastPrograms);
		}
		
		String finalState = robot.worldToString();
		println("Final state after execution of " + i + " programs");
		println(finalState);
        
        int initialScore = LogInterpreter.countCharacter(initialState, '2', 0) * 2 + LogInterpreter.countCharacter(initialState, '1', 0);
        int finalScore = LogInterpreter.countCharacter(finalState, '2', 0) * 2 + LogInterpreter.countCharacter(finalState, '1', 0);
        println("Dirt remaining: " + 100 * finalScore / initialScore + "%");
		
        if (errorCount > 0) {
		    System.exit(137);
		}
    }

    private static Set<Programs> calculateExpectedPrograms(LoggingRobot robot, Set<Programs> lastPrograms) {
        Set<Programs> expectedPrograms = new TreeSet<Programs>();

        for (Programs program : lastPrograms) {
            switch (program) {
            case WALL_FOLLOWING:
                expectedPrograms.add(Programs.ROOM_CROSSING);
                if (robot.tangleDetected()) {
                    expectedPrograms.add(Programs.UNTANGLE);
                } else if (robot.dirtDetected()) {
                    expectedPrograms.add(Programs.CLEAN);
                }
                break;
            case SPIRAL:
                expectedPrograms.add(Programs.ROOM_CROSSING);
                if (robot.tangleDetected()) {
                    expectedPrograms.add(Programs.UNTANGLE);
                } else if (robot.dirtDetected()) {
                    expectedPrograms.add(Programs.CLEAN);
                } 
                if (robot.wallDetected()) {
                    expectedPrograms.add(Programs.WALL_FOLLOWING);
                }
                break;
            case ROOM_CROSSING:
                expectedPrograms.add(Programs.SPIRAL);
                if (robot.tangleDetected()) {
                    expectedPrograms.add(Programs.UNTANGLE);
                } else if (robot.dirtDetected()) {
                    expectedPrograms.add(Programs.CLEAN);
                } 
                if (robot.wallDetected()) {
                    expectedPrograms.add(Programs.WALL_FOLLOWING);
                }
                break;
            case CLEAN:
                expectedPrograms.add(Programs.ROOM_CROSSING);
                if (robot.tangleDetected()) {
                    expectedPrograms.add(Programs.UNTANGLE);
                } else if (robot.dirtDetected()) {
                    expectedPrograms.add(Programs.CLEAN);
                } 
                break;
            case UNTANGLE:
                expectedPrograms.add(Programs.ROOM_CROSSING);
                break;
            }
        }
        return expectedPrograms;
    }

	private static void println() {
		System.out.println("!§$%&/()=");
	}
	
	private static void print(String string) {
	    String[] split = string.split("\n");
        for(int i = 0; i < split.length-1; ++i) {
            System.out.println("!§$%&/()=" + split[i]);
        }
        System.out.print("!§$%&/()=" + split[split.length-1]);
    }

	private static void println(String string) {
		String[] split = string.split("\n");
		for(String line : split) {
			System.out.println("!§$%&/()=" + line);
		}
	}

    private static String setToString(Set<Programs> set) {
		String result = new String();
        ArrayList<Programs> list = new ArrayList<Programs>(set);
        for (int i = 0; i < list.size()-2; ++i) {
            result += list.get(i) + ", ";
        }
        if (list.size() > 1) {
            result += list.get(list.size()-2) + " or " + list.get(list.size()-1);
        } else if (list.size() > 0) {
            result += list.get(0);
        } else {
            result += "NOTHING";
        }

		return result;
    }
    
    private static int[][] initMatrixSize(FileReader fileReader) throws IOException {
        BufferedReader reader = new BufferedReader(fileReader);
        int maxRow = 0;
        int maxCol = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.length() > maxCol) maxCol = line.length();
            maxRow++;
        }        
        return new int[maxRow][maxCol];
    }
    
    private static World initWorld(FileReader fileReader, int[][] matrix) throws IOException, InputFormatException {
        BufferedReader reader = new BufferedReader(fileReader);
        String line;
        int row = 0;
        while ((line = reader.readLine()) != null) {
            for (int col = 0; col < line.length(); ++col) {
                char c = line.charAt(col);
                matrix[row][col] = getWorldState(c);
            }
            ++row;
        }
        return new World(matrix);
    }
    
    private static LoggingRobot initRobot(FileReader fileReader, World world) throws IOException, InputFormatException {
        BufferedReader reader = new BufferedReader(fileReader);
        int xpos = -1;
        int ypos = -1;
        Direction dir = Direction.UNDEFINED;
                
        String line;
        int row = 0;
        while ((line = reader.readLine()) != null) {
            for (int col = 0; col < line.length(); ++col) {
                char c = line.charAt(col);
                if (c == '<' || c == '>' || c == '^' || c == 'v') {
                    xpos = col; 
                    ypos = row;
                    switch (c) {
                    case '<': dir = Direction.WEST; break;
                    case '>': dir = Direction.EAST; break;
                    case '^': dir = Direction.NORTH; break;
                    case 'v': dir = Direction.SOUTH; break;
                    default: dir = Direction.UNDEFINED;
                    }
                }
            }
            ++row;
        }
        
        if (dir == Direction.UNDEFINED || xpos == -1 || ypos == -1) {
            throw new InputFormatException("Robot position undefined: (" + xpos + ", " + ypos + ", " + dir + ")");
        }
        
        return new LoggingRobot(world, xpos, ypos, dir);
    }
    
    private static int getWorldState(char state) throws InputFormatException {
        switch (state) {
        case '~': return World.TANGLE;
        case '#': return World.WALL;
        case '0': return World.CLEAN;
        case '1': return World.DUSTY;
        case '2': return World.DIRTY;
        case '<': return World.CLEAN;
        case '>': return World.CLEAN;
        case 'v': return World.CLEAN;
        case '^': return World.CLEAN;
        default: throw new InputFormatException("Invalid world state: " + state);
        }
    }
}
