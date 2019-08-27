package edu.kit.informatik._intern.util.syntax.tokenizer;

/**
 * Interface used to build a module for a tokenizer.
 * 
 * @author  Tobias Bachert
 * @version 1.01, 2016/07/09
 */
public interface ModuleInformation {
    
    /**
     * Returns the pattern-format for the module.
     * 
     * @return the pattern-format
     */
    String format();
}
