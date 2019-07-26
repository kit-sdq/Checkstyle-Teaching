package edu.kit.informatik;

/**
 * This class models a person with a first name and a surname.
 *
 * @author Peter Oettig
 * @version 1.0
 */
public abstract class Person {
    private String firstName;
    private String surname;

    /**
     * The constructor to call from inheriting classes.
     * It sets the names of a person.
     *
     * @param firstName The first name of the person.
     * @param surname   The surname of the person.
     */
    public Person(String firstName, String surname) {
        this.firstName = firstName;
        this.surname = surname;
    }

    /**
     * Gets the first name of the person.
     *
     * @return The first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the surname of the person.
     *
     * @return The surname.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets the first name of the person to a new one.
     *
     * @param firstName The new first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the surname of the person to a new one.
     *
     * @param surname The new surname.
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String toString() {
        return getFirstName() + " " + getSurname();
    }
}
