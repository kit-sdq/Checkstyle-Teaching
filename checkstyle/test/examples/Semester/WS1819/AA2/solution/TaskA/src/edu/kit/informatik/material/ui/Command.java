/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.material.ui;

import edu.kit.informatik.material.data.Assembly;
import edu.kit.informatik.material.data.AssemblyAmountPair;
import edu.kit.informatik.material.data.AssemblyManager;
import edu.kit.informatik.material.InputException;
import edu.kit.informatik.util.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Encapsulates the available commands with description.
 *
 * @author original Jonathan Schenkenberger, adapted by Thomas Weber
 * @version 1.3
 */
public enum Command {
    ADD_ASSEMBLY("addAssembly " + StringUtil.parenthesize("" + InteractionStrings.ASSEMBLY_DESC)) {
        @Override
        public void execute(MatchResult matcher, AssemblyManager assemblyManager) throws InputException {
            final String args = matcher.group(1);
            final String[] argsSplitted = args.split("" + InteractionStrings.SEPARATOR_EQL);
            final String name = argsSplitted[0];
            final Map<String, Integer> assemblies = new HashMap<>();
            if (argsSplitted[1].contains("" + InteractionStrings.SEPARATOR_OUTER)) {
                final String[] outerSplitted = argsSplitted[1].split("" + InteractionStrings.SEPARATOR_OUTER);
                for (String str : outerSplitted) {
                    innerSplitAndPut(str, assemblies);
                }
            } else {
                innerSplitAndPut(argsSplitted[1], assemblies);
            }
            if (assemblyManager.addAssembly(name, assemblies)) {
                output = "" + InteractionStrings.OK;
            } else {
                throw new InputException("some semantical error in argslist!");
            }
        }
    },

    REMOVE_ASSEMBLY("removeAssembly " + StringUtil.parenthesize("" + InteractionStrings.NAME)) {
        @Override
        public void execute(MatchResult matcher, AssemblyManager assemblyManager) throws InputException {
            final String name = matcher.group(1);
            if (assemblyManager.removeAssembly(name)) {
                output = "" + InteractionStrings.OK;
            } else {
                throw new InputException("assembly with name " + name + " does not exist or is a component!");
            }
        }
    },

    PRINT_ASSEMBLY("printAssembly " + StringUtil.parenthesize("" + InteractionStrings.NAME)) {
        @Override
        public void execute(MatchResult matcher, AssemblyManager assemblyManager) throws InputException {
            final String name = matcher.group(1);
            final Assembly assembly = assemblyManager.getAssembly(name);
            if (assembly != null) {
                final List<AssemblyAmountPair> listToPrint = assembly.getAllDirectComponentsAndAssemblies();
                if (!listToPrint.isEmpty()) {
                    output = StringUtil.toString(listToPrint, "" + InteractionStrings.SEPARATOR_OUTER);
                } else {
                    output = "" + InteractionStrings.COMP;
                }
            } else {
                throw new InputException("assembly with name " + name + " does not exist!");
            }
        }
    },

    GET_ASSEMBLIES("getAssemblies " + StringUtil.parenthesize("" + InteractionStrings.NAME)) {
        @Override
        public void execute(MatchResult matcher, AssemblyManager assemblyManager) throws InputException {
            final String name = matcher.group(1);
            final Assembly assembly = assemblyManager.getAssembly(name);
            if (assembly != null) {
                final List<AssemblyAmountPair> listToPrint = assembly.getAllAssemblies();
                if (!listToPrint.isEmpty()) {
                    output = StringUtil.toString(listToPrint, "" + InteractionStrings.SEPARATOR_OUTER);
                } else {
                    if (assembly.isAssembly()) {
                        output = "" + InteractionStrings.EMPTY; 
                    } else {
                        throw new InputException("component cannot have assemblies!");
                    }
                }
            } else {
                throw new InputException("assembly with name " + name + " does not exist!");
            }
        }
    },

    GET_COMPONENTS("getComponents " + StringUtil.parenthesize("" + InteractionStrings.NAME)) {
        @Override
        public void execute(MatchResult matcher, AssemblyManager assemblyManager) throws InputException {
            final String name = matcher.group(1);
            final Assembly assembly = assemblyManager.getAssembly(name);
            if (assembly != null) {
                final List<AssemblyAmountPair> listToPrint = assembly.getAllComponents();
                if (!listToPrint.isEmpty()) {
                    output = StringUtil.toString(listToPrint, "" + InteractionStrings.SEPARATOR_OUTER);
                } else {
                    if (assembly.isAssembly()) {
                        output = "" + InteractionStrings.EMPTY;
                    }
                    else {
                        throw new InputException("component cannot have components!");
                    }
                }
            } else {
                throw new InputException("assembly with name " + name + " does not exist!");
            }
        }
    },

    ADD_PART("addPart " + StringUtil
            .parenthesize("" + InteractionStrings.NAME + InteractionStrings.ADD_REGEX + InteractionStrings.PAIR)) {
        @Override
        public void execute(MatchResult matcher, AssemblyManager assemblyManager) throws InputException {
            final String args = matcher.group(1);
            final String[] splitted = args.split("" + InteractionStrings.ADD_REGEX);
            final String name = splitted[0];
            final String[] pair = splitted[1].split("" + InteractionStrings.SEPARATOR_INNER);
            final int amount = Integer.parseInt(pair[0]);
            final String compName = pair[1];
            final Assembly assembly = assemblyManager.getAssembly(name);
            if (assembly != null) {
                if (assembly.isComponent()) {
                    throw new InputException("adding parts to a component is not allowed!");
                }
                
                Assembly compAssembly = assemblyManager.getAssembly(compName);
                if (compAssembly == null) {
                    assemblyManager.addAssembly(compName, new HashMap<>());
                    compAssembly = assemblyManager.getAssembly(compName);
                }
                if (assembly.addComponent(compAssembly, amount)) {
                    output = "" + InteractionStrings.OK;
                } else {
                    throw new InputException("cyclic dependencies are not allowed or too high amount!");
                }
                
            } else {
                throw new InputException("assembly with name " + name + " does not exist!");
            }
        }
    },
    
    REMOVE_PART("removePart " + StringUtil
            .parenthesize("" + InteractionStrings.NAME + InteractionStrings.SUB + InteractionStrings.PAIR)) {
        @Override
        public void execute(MatchResult matcher, AssemblyManager assemblyManager) throws InputException {
            final String args = matcher.group(1);
            final String[] splitted = args.split("" + InteractionStrings.SUB);
            final String name = splitted[0];
            final String[] pair = splitted[1].split("" + InteractionStrings.SEPARATOR_INNER);
            final int amount = Integer.parseInt(pair[0]);
            final String compName = pair[1];
            final Assembly assembly = assemblyManager.getAssembly(name);
            if (assembly != null) {
                final Assembly compAssembly = assemblyManager.getAssembly(compName);
                if (compAssembly != null) {
                    if (assemblyManager.removePart(assembly, compAssembly, amount)) {
                        output = "" + InteractionStrings.OK;
                    } else {
                        throw new InputException("not enough of the given component available!");
                    }
                } else {
                    throw new InputException("assembly with name " + compName + " does not exist!");
                }
            } else {
                throw new InputException("assembly with name " + name + " does not exist!");
            }
        }
    },

    /**
     * Finishes the interaction.
     */
    QUIT("quit") {
        @Override
        public void execute(final MatchResult matcher, final AssemblyManager manager) {
            quit();
        }
    };

    //private static String input;

    /**
     * Contains the output of the command if there is any.
     */
    protected String output;

    /**
     * Contains the game state.
     */
    private boolean isRunning;

    /**
     * Contains this commands input format.
     */
    private Pattern pattern;

    /**
     * Constructs a new command.
     *
     * @param pattern
     *            The regex pattern to use for command validation and processing.
     */
    Command(final String pattern) {
        this.isRunning = true;
        this.pattern = Pattern.compile(pattern);
    }

    /**
     * Checks an input against all available commands and calls the command if one
     * is found.
     *
     * @param input
     *            The user input.
     * @param dawnGame
     *            The instance of the Ludo object.
     * @return The command that got executed.
     * @throws InputException
     *             if no matching command is found. Contains an error message.
     */
    public static Command executeMatching(final String input, final AssemblyManager manager) throws InputException {
        for (final Command command : values()) {
            //Command.input = input;
            final Matcher matcher = command.pattern.matcher(input);
            if (matcher.matches()) {
                command.execute(matcher, manager);
                return command;
            }
        }

        throw new InputException("not a valid command!");
    }

    /**
     * Executes a command.
     *
     * @param matcher
     *            The regex matcher that contains the groups of input of the
     *            command.
     * @param dawnGame
     *            The instance of the Ludo object.
     * @throws InputException
     *             if the command contains syntactical or semantic errors.
     */
    public abstract void execute(MatchResult matcher, AssemblyManager assemblyManager) throws InputException;

    /**
     * Checks if the program is still running or was exited.
     *
     * @return true if the program is still running, false otherwise.
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Returns the string passed by the last active command.
     *
     * @return string to display to the user
     */
    public String getOutput() {
        return output;
    }

    /**
     * Exits the program gracefully.
     */
    protected void quit() {
        isRunning = false;
    }

    private static void innerSplitAndPut(final String outerSplit, final Map<String, Integer> assemblies) throws InputException{
        final String[] innerSplit = outerSplit.split("" + InteractionStrings.SEPARATOR_INNER);
        final int compAmount = Integer.parseInt(innerSplit[0]);
        final String compName = innerSplit[1];
        if (!assemblies.containsKey(compName)) {
            assemblies.put(compName, compAmount);
        } else {
            throw new InputException("duplicate name " + compName + " !");
        }
    }

}
