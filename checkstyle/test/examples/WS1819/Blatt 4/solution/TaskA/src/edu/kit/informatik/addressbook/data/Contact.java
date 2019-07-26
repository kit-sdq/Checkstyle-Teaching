/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.addressbook.data;

import edu.kit.informatik.addressbook.ui.InteractionStrings;

/**
 * Encapsulates a contact as stated in the assignment.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class Contact {
    /**
     * Static class attribute to ensure ids are unique.
     */
    private static int idCounter = 1;

    /**
     * The name of the contact.
     */
    private NameOfContact name;

    /**
     * The e-mail of the contact.
     */
    private Email email;

    /**
     * The phone number of the contact.
     */
    private Phone phone;

    /**
     * Unique id for every contact in every address book.
     */
    private String id;

    /**
     * Instantiates a new Contact object with the given data. No checks or
     * calculations are performed. Access is package-private to restrict the
     * creation of objects of this class.
     *
     * @param name the name of the contact
     * @param email the e-mail of the contact
     * @param phone the phone number of the contact
     */
    Contact(final String name, final String email, final String phone) {
        this.name = new NameOfContact(name);
        this.email = new Email(email);
        this.phone = new Phone(phone);
        this.id = String.format("%04d", Contact.idCounter++);
    }

    /**
     * @return name of this contact
     */
    public NameOfContact getName() {
        return name;
    }

    /**
     * @return e-mail of this contact
     */
    public Email getEmail() {
        return email;
    }

    /**
     * @return phone number of this contact
     */
    public Phone getPhone() {
        return phone;
    }

    /**
     * @return unique id of this contact
     */
    public String getId() {
        return id;
    }

    /**
     * Checks if this contact has the same data as the given one.
     *
     * @param name name of the contact
     * @param email email of the contact
     * @param phone phone number of the contact
     * @return whether the given data equals the data of this contact
     */
    boolean isEqualTo(String name, String email, String phone) {
        if (!this.name.getName().equals(name)) {
            return false;
        }
        if (!this.email.getEmail().equals(email)) {
            return false;
        }
        return this.phone.getPhone().equals(phone);
    }

    @Override
    public String toString() {
        return this.id + InteractionStrings.OUTPUT_SEPARATOR + this.name
               + InteractionStrings.OUTPUT_SEPARATOR + this.email
               + InteractionStrings.OUTPUT_SEPARATOR + this.phone;
    }

}
