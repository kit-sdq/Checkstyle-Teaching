/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.addressbook.data;

import edu.kit.informatik.addressbook.InputException;
import edu.kit.informatik.addressbook.data.AddressBook;
import edu.kit.informatik.addressbook.ui.InteractionStrings;

import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulates all address books for task A at the assignment.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class AddressBooks {
    /**
     * A map to gain quick access with a given name.
     */
    private Map<String, AddressBook> addressBooks;

    /**
     * Instantiates a new AddressBooks object. No checks or
     * calculations are performed.
     */
    public AddressBooks() {
        this.addressBooks = new HashMap<>();
    }

    /**
     * Adds a new address book with the given name if possible.
     *
     * @param name the name of the new address book
     * @return string to display to the user
     * @throws InputException occurs if an address book with this name
     *         already exists
     */
    public String addAddressBook(String name) throws InputException {
        existenceCheck(name, false);
        this.addressBooks.put(name, new AddressBook(name));
        return InteractionStrings.OK.toString();
    }

    /**
     * Tries to delete the address book with the given name if possible.
     *
     * @param name the name of the address book
     * @return string to display to the user
     * @throws InputException occurs if no address book with the given
     *         name
     *         exists
     */
    public String removeAddressBook(String name) throws InputException {
        existenceCheck(name, true);
        this.addressBooks.remove(name);
        return InteractionStrings.OK.toString();
    }

    /**
     * Tries to add a new contact with the given data to the address book
     * with the given name.
     *
     * @param name the name of the address book
     * @param nameOfContact the name of the new contact
     * @param email the email of the new contact
     * @param phone the phone number of the new contact
     * @return the unique id for the contact if adding was successful
     * @throws InputException occurs if the address book does not exist
     *         or a
     *         contact with the given data already exists
     */
    public String addContact(String name, String nameOfContact, String email,
            String phone) throws InputException {
        existenceCheck(name, true);
        return addressBooks.get(name).addContact(nameOfContact, email, phone);
    }

    /**
     * Tries to delete the contact with the given id from the address book
     * with the given name.
     *
     * @param name the name of the address book
     * @param contaceId the unique id of the contact to be removed
     * @return string to display to the user
     * @throws InputException occurs if either address book or id do not
     *         exist
     */
    public String removeContact(String name, String contaceId)
            throws InputException {
        existenceCheck(name, true);
        return addressBooks.get(name).removeContact(contaceId);
    }

    /**
     * Returns the content of the address book with the given name, properly
     * formatted.
     *
     * @param name the name of the address book
     * @return a string representation of the address book
     * @throws InputException occurs if the address book does not exist
     *         or is
     *         empty
     */
    public String printAddressBook(String name) throws InputException {
        existenceCheck(name, true);
        String addressBookTextualRepresentation = addressBooks.get(name)
                .toString();
        if (addressBookTextualRepresentation == null) {
            throw new InputException(
                    InteractionStrings.EMPTY_ADDRESS_BOOK.toString());
        }
        return addressBooks.get(name).toString();

    }

    /**
     * Checks if an address book with the given name exists and throws an
     * exception if the condition in shouldExist is not met.
     *
     * @param name the name of the address book
     * @param shouldExist if the address book should exist or not
     * @throws InputException occurs if the condition with shouldExist
     *         is not
     *         met
     */
    private void existenceCheck(String name, boolean shouldExist)
            throws InputException {
        if (shouldExist) {
            if (!addressBooks.containsKey(name)) {
                throw new InputException(
                        InteractionStrings.ADDRESS_BOOK_NOT_EXISTING
                                .toString());
            }
        } else {
            if (addressBooks.containsKey(name)) {
                throw new InputException(
                        InteractionStrings.ADDRESS_BOOK_ALREADY_EXISTING
                                .toString());
            }
        }
    }
}
