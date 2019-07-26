package student.program;

public interface Program {
    
    public enum ReturnState { DONE, WALL, TANGLE, DIRT }
    
    public ReturnState run();
}
