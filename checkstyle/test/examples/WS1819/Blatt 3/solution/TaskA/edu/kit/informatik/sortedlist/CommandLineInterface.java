/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.sortedlist;

import edu.kit.informatik.Terminal;

/**
 * Represents the sorted list program's command line interface. 
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public final class CommandLineInterface {
    private boolean quit;
    private final SortedIntLinkedList list;
    
    /**
     * Creates a new CommandLineInterface, connects the commands to it and initializes an empty list.
     */
    public CommandLineInterface() {
        this.quit = false;
        this.list = new SortedIntLinkedList();
    }
    
    /**
     * Starts this command line.
     */
    protected void start() {
        while (!this.quit) {
            final String commandStr = Terminal.readLine();
            final Command cmd = Command.of(commandStr);
            cmd.execute(this);
        }
    }
    
    /**
     * Gets the list connected with this command line interface.
     * 
     * @return the list
     */
    protected SortedIntLinkedList getList() {
        return this.list;
    }
    
    /**
     * Quits this command line.
     */
    protected void quit() {
        this.quit = true;
    }
}
