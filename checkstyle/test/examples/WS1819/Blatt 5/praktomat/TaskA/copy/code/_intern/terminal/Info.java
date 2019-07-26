package edu.kit.informatik._intern.terminal;


public interface Info {
    
    public static Info of(final String information) {
        return new Info() {
            
            @Override public String toString() {
                return information;
            }
        };
    }
}
