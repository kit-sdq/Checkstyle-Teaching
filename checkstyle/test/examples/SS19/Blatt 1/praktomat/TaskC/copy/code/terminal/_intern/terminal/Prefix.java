
package edu.kit.informatik._intern.terminal;

public enum Prefix {
    INFO   ( "info:"   ),
    INPUT  ( "input:"  ),
    OUTPUT ( "output:" ),
    ERROR  ( "error:"  ),
    CRASH  ( "crash:"  );
    
    public final String prefix;
    
    Prefix(String prefix) {
        this.prefix = prefix;
    }
    
    @Override
    public String toString() {
        return prefix;
    }
}