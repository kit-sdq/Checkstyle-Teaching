/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.sortedlist;

import edu.kit.informatik.Terminal;

/**
 * A constant of this enum represents a command for the sorted list program's
 * command line interface.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public enum Command {
    /**
     * Represents the add command. It adds the argument which is a number to the list at the correct position.
     */
    ADD("add") {
        @Override
        protected void execute(final CommandLineInterface cli) {
            cli.getList().add(getArgument());
        }
    },

    /**
     * Represents the print command. It prints the requested String representation of the list.
     */
    PRINT("print") {
        @Override
        protected void execute(final CommandLineInterface cli) {
            Terminal.printLine(cli.getList());
        }
    },

    /**
     * Represents the size command. It prints the size of the list.
     */
    SIZE("size") {
        @Override
        protected void execute(final CommandLineInterface cli) {
            Terminal.printLine(cli.getList().size());
        }
    },

    /**
     * Represents the isEmpty command. It prints whether the list is empty.
     */
    IS_EMPTY("isEmpty") {
        @Override
        protected void execute(final CommandLineInterface cli) {
            Terminal.printLine(cli.getList().isEmpty());
        }
    },

    /**
     * Represents the clear command. It clears the list, i.e. it removes all the elements from the list.
     */
    CLEAR("clear") {
        @Override
        protected void execute(final CommandLineInterface cli) {
            cli.getList().clear();
        }
    },

    /**
     * Represents the get command. Prints the number at the given argument which is an index.
     */
    GET("get") {
        @Override
        protected void execute(final CommandLineInterface cli) {
            Terminal.printLine(cli.getList().get(getArgument()));
        }
    },

    /**
     * Represents the indexOf command. <br />
     * Prints the index of the first occurence of the given argument which is a number. <br />
     * Prints -1 if the number is not contained in the list.
     */
    INDEX_OF("indexOf") {
        @Override
        protected void execute(final CommandLineInterface cli) {
            Terminal.printLine(cli.getList().indexOf(getArgument()));
        }
    },

    /**
     * Represents the lastIndexOf command. <br />
     * Prints the index of the last occurence of the given argument which is a number. <br />
     * Prints -1 if the number is not contained in the list.
     */
    LAST_INDEX_OF("lastIndexOf") {
        @Override
        protected void execute(final CommandLineInterface cli) {
            Terminal.printLine(cli.getList().lastIndexOf(getArgument()));
        }
    },

    /**
     * Represents the remove command. <br />
     * Removes the first occurence of the given argument which is a number. <br />
     * Prints whether the number was removed, i.e. it was formerly present in the list.
     */
    REMOVE("remove") {
        @Override
        protected void execute(final CommandLineInterface cli) {
            Terminal.printLine(cli.getList().remove(getArgument()));
        }
    },

    /**
     * Represents the contains command. <br />
     * Prints whether the given argument which is a number is contained in the list
     */
    CONTAINS("contains") {
        @Override
        protected void execute(final CommandLineInterface cli) {
            Terminal.printLine(cli.getList().contains(getArgument()));
        }
    },

    /**
     * Represents the quit command. Quits the program.
     */
    QUIT("quit") {
        @Override
        protected void execute(final CommandLineInterface cli) {
            cli.quit();
        }
    };

    private static final String DELIM_ARG = " ";
    private static final String DELIM_ARG_REGEX = "\\s";

    private final String commandStr;
    private int argument;

    private Command(final String commandStr) {
        this.commandStr = commandStr;
    }

    /**
     * Gets the command corresponding the given command string. This method also
     * parses the argument (if any) and sets the argument of the returned command
     * accordingly.
     * 
     * @param commandStr
     *            - the given command string including the argument
     * @return the corresponding command with argument set (if existent)
     */
    protected static Command of(final String commandStr) {
        final String commandStrWithoutArg;
        final int argument;
        if (commandStr.contains(DELIM_ARG)) {
            final String[] split = commandStr.split(DELIM_ARG_REGEX);
            commandStrWithoutArg = split[0];
            argument = Integer.parseInt(split[1]);
        } else {
            commandStrWithoutArg = commandStr;
            // never-mind the argument, it will not be used in this case because this
            // command has no argument
            argument = 0;
        }

        Command ret = null;
        for (final Command cmd : Command.values()) {
            if (cmd.commandStr.equals(commandStrWithoutArg)) {
                ret = cmd;
                ret.argument = argument;
                break;
            }
        }

        return ret;
    }

    /**
     * Gets the argument value of this command.
     * 
     * @return the argument value
     */
    protected int getArgument() {
        return argument;
    }
    
    /**
     * Executes this command.
     * 
     * @param cli - the command line interface on which the command should be executed
     */
    protected abstract void execute(CommandLineInterface cli);
}
