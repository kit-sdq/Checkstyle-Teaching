/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.praktomat.main;

/**
 * Encapsulates all available commands for the command line interface as
 * stated in the assignment.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public abstract class Command {

    /**
     * Array which contains all available commands, for a detailed
     * description of the commands, refer to the assignment.
     */
    private static final Command[] CMDS = new Command[]{
            new Command("teacher", new Format[]{Format.NAME}) {
                @Override
                public void execute(State state, String[] cmdArguments) {
                    PRAKTOMAT_USAGE_INTERFACE.executeTutoriumsCommand(state, cmdArguments);
                }
            }, new Command("pupil",
            new Format[]{Format.NAME, Format.MATRICULATION_NUMBER}) {
        @Override
        public void execute(State state, String[] cmdArguments) {
            PRAKTOMAT_USAGE_INTERFACE.executeStudentCommand(state, cmdArguments);
        }
    }, new Command("assignment", new Format[]{Format.DESCRIPTION}) {
        @Override
        public void execute(State state, String[] cmdArguments) {
            PRAKTOMAT_USAGE_INTERFACE.executeTaskCommand(state, cmdArguments);
        }
    }, new Command("list-pupils", new Format[]{}) {
        @Override
        public void execute(State state, String[] cmdArguments) {
            PRAKTOMAT_USAGE_INTERFACE.executeListStudentsCommand(state);
        }
    }, new Command("submit",
            new Format[]{Format.ID, Format.MATRICULATION_NUMBER,
                    Format.DESCRIPTION}) {
        @Override
        public void execute(State state, String[] cmdArguments) {
            PRAKTOMAT_USAGE_INTERFACE.executeSubmitCommand(state, cmdArguments);
        }
    }, new Command("review",
            new Format[]{Format.ID, Format.MATRICULATION_NUMBER, Format.GRADE,
                    Format.DESCRIPTION}) {
        @Override
        public void execute(State state, String[] cmdArguments) {
            PRAKTOMAT_USAGE_INTERFACE.executeReviewCommand(state, cmdArguments);
        }
    }, new Command("list-solutions", new Format[]{Format.ID}) {
        @Override
        public void execute(State state, String[] cmdArguments) {
            PRAKTOMAT_USAGE_INTERFACE.executeListSolutionsCommand(state, cmdArguments);
        }
    }, new Command("results", new Format[]{}) {
        @Override
        public void execute(State state, String[] cmdArguments) {
            PRAKTOMAT_USAGE_INTERFACE.executeResultsCommand(state);
        }
    }, new Command("summary-assignment", new Format[]{}) {
        @Override
        public void execute(State state, String[] cmdArguments) {
            PRAKTOMAT_USAGE_INTERFACE.executeSummaryTaskCommand(state);
        }
    }, new Command("summary-teacher", new Format[]{}) {
        @Override
        public void execute(State state, String[] cmdArguments) {
            PRAKTOMAT_USAGE_INTERFACE.executeSummaryTutorCommand(state);
        }
    }, new Command("reset", new Format[]{}) {
        @Override
        public void execute(State state, String[] cmdArguments) {
            PRAKTOMAT_USAGE_INTERFACE.executeResetCommand(state);
        }
    }, new Command("quit", new Format[]{}) {
        @Override
        public void execute(State state, String[] cmdArguments) {
            PRAKTOMAT_USAGE_INTERFACE.executeQuiteCommand(state);
        }
    } //
    };

    /**
     * Command without parameters.
     */
    private final String commandString;

    /**
     * Used for interactions with the praktomat.
     */
    private static final PraktomatUsageInterface PRAKTOMAT_USAGE_INTERFACE = new PraktomatUsage();

    /**
     * {@link Format} array containing the expected parameters for format
     * checks.
     */
    private final Format[] params;

    /**
     * Instantiates a new command with given command string and an array of
     * formats for the parameters.
     *
     * @param commandString command without parameters
     * @param params {@link Format} array
     */
    private Command(String commandString, Format[] params) {
        this.commandString = commandString;
        this.params = params.clone();
    }

    /**
     * @return an array of available commands
     */
    public static Command[] getAvailableCommands() {
        return CMDS;
    }

    /**
     * @return the command string without parameters
     */
    public String getCommandString() {
        return commandString;
    }

    /**
     * Checks whether or not the format of the given string array equals the
     * format the parameters should have (the format array of each command).
     *
     * @param cmdline String array of the command line input, split by a
     *         separator (whitespace)
     * @return boolean if format is ok
     */
    public boolean isFormatOk(String[] cmdline) {
        if (cmdline.length != params.length + 1 || !isCmd(cmdline[0])) {
            return false;
        }

        boolean formatOk = true;

        for (int i = 0; i < params.length; i++) {
            final String cur = cmdline[i + 1];

            switch (params[i]) {
                case DESCRIPTION:
                    formatOk = Format.DESCRIPTION.checkCommandRegex(cur);
                    break;
                case ID:
                    formatOk = Format.ID.checkCommandRegex(cur);
                    break;
                case NAME:
                    formatOk = Format.NAME.checkCommandRegex(cur);
                    break;
                case GRADE:
                    formatOk = Format.GRADE.checkCommandRegex(cur);
                    break;
                case MATRICULATION_NUMBER:
                    formatOk = Format.MATRICULATION_NUMBER
                            .checkCommandRegex(cur);
                    break;
                default:
                    throw new IllegalStateException(
                            "Unhandled parameter type: " + params[i]);
            }
        }

        return formatOk;
    }

    /**
     * Checks whether or not the given string contains a command string from
     * the CDMS array.
     *
     * @param command string representation of the command
     * @return if the given string contains a valid command, therefore if it
     *         can be found in CMDS.
     */
    public boolean isCmd(String command) {
        return command != null && this.commandString
                .equals(command.trim().toLowerCase());
    }

    /**
     * Specifies the actions for every available command.
     *
     * @param state the state of the command line interface
     * @param cmdArguments the arguments from the command line as string
     *         array
     */
    public abstract void execute(State state, String[] cmdArguments);
}
