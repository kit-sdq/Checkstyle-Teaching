package edu.kit.informatik._intern.terminal;

import java.util.List;

public interface Properties {
    
    boolean is(final String key, final Object value);
    
    List<String> getFile(final String filename);
}
