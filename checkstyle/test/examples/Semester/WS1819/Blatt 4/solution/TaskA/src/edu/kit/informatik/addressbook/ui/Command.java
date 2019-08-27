/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.addressbook.ui;

import edu.kit.informatik.addressbook.InputException;
import edu.kit.informatik.addressbook.data.AddressBooks;

/**
 * Encapsulates the available commands with description.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public enum Command {
    /**
     * Adds a new address book to the existing ones if the name is not
     * already in use.
     */
    ADD_ADDRESS_BOOK(
            "add-addressbook (" + InteractionStrings.NAME_PATTERN.toString()
            + ")") {
        @Override
        public void execute(final String parameters,
                final AddressBooks addressBooks) throws InputException {
            output = addressBooks.addAddressBook(parameters);
        }
    },
    /**
     * Removes an existing address book if it exists.
     */
    REMOVE_ADDRESS_BOOK(
            "remove-addressbook (" + InteractionStrings.NAME_PATTERN.toString()
            + ")") {
        @Override
        public void execute(final String parameters,
                final AddressBooks addressBooks) throws InputException {
            output = addressBooks.removeAddressBook(parameters);
        }
    },
    /**
     * Adds a new contact to an existing address book if valid data is passed.
     */
    ADD_CONTACT(
            "add-contact (" + InteractionStrings.NAME_PATTERN.toString() + ")"
            + InteractionStrings.INPUT_SEPARATOR.toString() + "("
            + InteractionStrings.NAME_PATTERN.toString() + ")"
            + InteractionStrings.INPUT_SEPARATOR.toString() + "("
            + InteractionStrings.EMAIL_PATTERN.toString() + ")"
            + InteractionStrings.INPUT_SEPARATOR.toString() + "("
            + InteractionStrings.PHONE_PATTERN.toString() + ")") {
        @Override
        public void execute(final String parameters,
                final AddressBooks addressBooks) throws InputException {
            String[] params = parameters
                    .split(InteractionStrings.INPUT_SEPARATOR.toString());
            output = addressBooks.addContact(params[0], params[1],
                    params[2], params[3]);
        }
    },
    /**
     * Removes an existing contact from an existing address book if valid data
     * is passed.
     */
    REMOVE_CONTACT(
            "remove-contact (" + InteractionStrings.NAME_PATTERN.toString()
            + ")" + InteractionStrings.INPUT_SEPARATOR.toString() + "("
            + InteractionStrings.ID_PATTERN.toString() + ")") {
        @Override
        public void execute(final String parameters,
                final AddressBooks addressBooks) throws InputException {
            String[] params = parameters
                    .split(InteractionStrings.INPUT_SEPARATOR.toString());
            output = addressBooks.removeContact(params[0], params[1]);
        }
    },
    /**
     * Prints the contacts from an existing address book.
     */
    PRINT_ADDRESS_BOOK(
            "print-addressbook (" + InteractionStrings.NAME_PATTERN.toString()
            + ")") {
        @Override
        public void execute(final String parameters,
                final AddressBooks addressBooks) throws InputException {
            output = addressBooks.printAddressBook(parameters);
        }
    },
    /**
     * Finishes the interaction.
     */
    QUIT("quit") {
        @Override
        public void execute(final String parameters,
                final AddressBooks addressBooks) {
            quit();
        }
    };

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
    private String pattern;

    /**
     * Constructs a new command.
     *
     * @param pattern The regex pattern to use for command validation
     *         and processing.
     */
    Command(String pattern) {
        this.isRunning = true;
        this.pattern = pattern;
    }

    /**
     * Checks an input against all available commands and calls the command if
     * one is found.
     *
     * @param input The user input.
     * @param addressBooks The instance of the address books object.
     * @return The command that got executed.
     * @throws InputException if no matching command is found. Contains
     *         an error message.
     */
    public static Command executeMatching(String input,
            AddressBooks addressBooks) throws InputException {
        for (Command command : Command.values()) {
            if (input.matches(command.pattern)) {
                command.execute(input.substring(input.indexOf(" ") + 1),
                        addressBooks);
                return command;
            }
        }

        throw new InputException("not a valid command!");
    }

    /**
     * Executes a command.
     *
     * @param parameters The regex matcher that contains the groups of
     *         input of the command.
     * @param addressBooks The instance of the address books object.
     * @throws InputException if the command contains syntactical or
     *         semantic errors.
     */
    public abstract void execute(String parameters, AddressBooks addressBooks)
            throws InputException;

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
}
