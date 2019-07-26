package kit.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import student.Scheduler;
import kit.Robot;
import kit.example.MyRobot.Direction;

/**
 * Test program to run the student scheduler on an example world 
 * and visualize the behavior of the robot.
 * @author markus
 * @version 1
 */
public final class RobotTest {
    
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
            System.out.println("Usage: RoombaTest path-to-world-description-file [timeout]");
            System.exit(0);
        }
        
        File file = new File(args[0]);
        
        int timeout = 100;
        if (args.length > 1) {
            try {
                timeout = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Second argument must be a natural number");
                System.exit(0);
            }
        }
        
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            System.out.println("Path to world description file invalid. File not found");
            System.exit(0);
        }
                
        // initialize matrix-size
        World world = null;
        Robot robot = null;
        try {
            int[][] matrix = initMatrixSize(fileReader);
            fileReader = new FileReader(file);
            world = initWorld(fileReader, matrix);
            fileReader = new FileReader(file);
            robot = initRobot(fileReader, world);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        } catch (InputFormatException e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        }
        
        // start cleaning program
		Scheduler cleaningProgram = new Scheduler(robot);
	    cleaningProgram.run(timeout);
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
    
    private static Robot initRobot(FileReader fileReader, World world) throws IOException, InputFormatException {
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
        
        return new PrintingRobot(world, xpos, ypos, dir);
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
