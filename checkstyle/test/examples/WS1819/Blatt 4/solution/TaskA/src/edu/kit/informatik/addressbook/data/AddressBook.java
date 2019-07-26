/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.addressbook.data;

import edu.kit.informatik.addressbook.InputException;
import edu.kit.informatik.addressbook.ui.InteractionStrings;

import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;


/**
 * Provides the functions of an address book like stated in the assignment.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class AddressBook {
    /**
     * The name of the address book.
     */
    private String name;

    /**
     * List to store the contacts and keep track of the order.
     */
    private List<Contact> contacts;

    /**
     * Instantiates a new Phone object with the given data. No checks or
     * calculations are performed. Access is package-private to restrict the
     * creation of objects of this class.
     *
     * @param name the name of the new address book
     */
    AddressBook(final String name) {
        this.name = name;
        this.contacts = new LinkedList<>();
    }

    /**
     * Adds a new contact with the given data to this address book. If
     * another contact with the given data is already existing, an exception
     * is thrown and the address book remains unchanged.
     *
     * @param name the name of the new contact
     * @param email the e-mail of the new contact
     * @param phone the phone number of the new contact
     * @return unique contact id if adding was successful
     * @throws InputException occurs if a contact with this data already
     *         exists
     */
    String addContact(String name, String email, String phone)
            throws InputException {
        if (getContact(name, email, phone) != null) {
            throw new InputException(
                    InteractionStrings.CONTACT_ALREADY_EXISTS.toString());
        }
        Contact contact = new Contact(name, email, phone);
        this.contacts.add(contact);
        return "" + contact.getId();
    }

    /**
     * Removes the contact with the given id if existing, throws an exception
     * otherwise.
     *
     * @param contactId the id of the contact
     * @return string to display to the user
     * @throws InputException occurs if no contact with this data exists
     */
    String removeContact(String contactId) throws InputException {
        Contact contactToBeRemoved = getContact(contactId);
        if (contactToBeRemoved != null) {
            this.contacts.remove(contactToBeRemoved);
            return InteractionStrings.OK.toString();
        } else {
            throw new InputException(
                    InteractionStrings.CONTACT_NOT_EXISTING.toString());
        }
    }

    /**
     * Returns the name of this address book.
     *
     * @return the name of the address book
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringJoiner result = new StringJoiner(System.lineSeparator());
        for (Contact contact : contacts) {
                result.add(contact.toString());
        }
        return result.length() > 0 ? result.toString() : null;
    }

    /**
     * Returns the contact with the given data if existing or null.
     *
     * @param name the name of the new contact
     * @param email the e-mail of the new contact
     * @param phone the phone number of the new contact
     * @return the contact if existing, null otherwise
     */
    private Contact getContact(String name, String email, String phone) {
        for (Contact contact : contacts) {
            if (contact.isEqualTo(name, email, phone)) {
                return contact;
            }
        }
        return null;
    }

    /**
     * Returns the contact with the given id if existing or null.
     *
     * @param contactId the id of the contact
     * @return the contact if existing, null otherwise
     */
    private Contact getContact(String contactId) {
        for (Contact contact : contacts) {
            if (contact.getId().equals(contactId)) {
                return contact;
            }
        }
        return null;
    }
}
