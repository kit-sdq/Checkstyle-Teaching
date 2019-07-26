/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.addressbook.ui;

/**
 * Contains strings or characters used for input or output.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public enum InteractionStrings {
    /**
     * String for a successfully executed command.
     */
    OK("OK"),
    /**
     * Separator for the output like stated in the assignment.
     */
    OUTPUT_SEPARATOR(","),
    /**
     * Separator for the input like stated in the assignment.
     */
    INPUT_SEPARATOR(","),

    /**
     * The pattern of a valid name.
     */
    NAME_PATTERN("[a-z]+"),

    /**
     * The pattern of a valid domain.
     */
    DOMAIN_PATTERN(NAME_PATTERN + "\\." + "[a-z]{2,4}"),

    /**
     * The pattern of a valid email.
     */
    EMAIL_PATTERN(NAME_PATTERN + "@" + DOMAIN_PATTERN),

    /**
     * The pattern of a valid phone number.
     */
    PHONE_PATTERN("\\d{1,15}"),

    /**
     * The pattern for a contact id.
     */
    ID_PATTERN("\\d{4}"),

    /**
     * Message to display the user that this contact is already saved in the
     * address book.
     */
    CONTACT_ALREADY_EXISTS("a contact with this data already exists!"),
    /**
     * Message to display the user that this contact does not exist in this
     * address book.
     */
    CONTACT_NOT_EXISTING("a contact with this id is not existing!"),

    /**
     * Message to display the user that this address book is empty.
     */
    EMPTY_ADDRESS_BOOK("address book is empty!"),

    /**
     * Message to display the user that an address book with this name is
     * already existing.
     */
    ADDRESS_BOOK_ALREADY_EXISTING(
            "An address book with this name already exists."),

    /**
     * Message to display the user that an address book with this name is not
     * existing.
     */
    ADDRESS_BOOK_NOT_EXISTING(
            "An address book with this name does not " + "exist.");

    /**
     * String representation of the output.
     */
    private String content;

    /**
     * @param content the string representation for output
     */
    InteractionStrings(final String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return content;
    }
}
