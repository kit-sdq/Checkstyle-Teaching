package edu.kit.informatik;

import java.util.Objects;

/**
 * This class models a customer with a first name, a last name and a customer number for uniqueness.
 *
 * @author Peter Oettig
 * @version 1.0
 */
public class Customer implements Comparable<Customer> {
    private static int customerCounter = 1;
    
    private final int customerNumber;
    private String firstName;
    private String lastName;

    /**
     * The constructor for a new customer with a first- and last name.
     * 
     * @param firstName The first name of the customer
     * @param lastName The last name of the customer
     */
    public Customer(String firstName, String lastName) {
        this.customerNumber = customerCounter++;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Gets the customer number of the customer.
     * 
     * @return The customer number.
     */
    public int getCustomerNumber() {
        return customerNumber;
    }

    /**
     * This compares this customer object to a first- and last name, 
     * which identifies a person in this system, for equality.
     * It provides equality checking without creating an actual customer object.
     * 
     * @param firstName The first name of the other customer.
     * @param lastName The last name of the other customer.
     * @return true, if this customer equals the provided first- and last name, false otherwise.
     */
    public boolean equals(String firstName, String lastName) {
        return Objects.equals(this.firstName, firstName) && Objects.equals(this.lastName, lastName);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        
        Customer customer = (Customer) other;
        // Only first- and lastName for identification as described on the sheet: a naming combination is unique
        return Objects.equals(firstName, customer.firstName) && Objects.equals(lastName, customer.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

    @Override
    public int compareTo(Customer other) {
        return Integer.compare(customerNumber, other.customerNumber);
    }
}
