package edu.kit.informatik._intern.terminal;


public enum Prefix {
    INFO   ( "info:"   ),
    INPUT  ( "input:"  ),
    OUTPUT ( "output:" ),
    ERROR  ( "error:"  ),
    CRASH  ( "crash:"  );
    
    private final String string;
    
    Prefix(final String string) {
        this.string = string;
    }
    
    @Override public String toString() {
        return string;
    }
}