package edu.kit.informatik;

public class Customer {
    private int id;
    private String firstName;
    private String lastName;

    /**
     * The cunstructor for the customer class.
     * 
     * @param id The ID of the customer.
     * @param firstName The first name of the customer.
     * @param lastName The last name of the customer.
     */
    public Customer(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Customer customer = (Customer) o;
        return id == customer.id;
    }

    @Override
    public int hashCode() {
        return id * 5 - 23;
    }

    /**
     * Gets the ID of the customer.
     * 
     * @return The ID of the customer.
     */
    public int getId() {
        return id;
    }
}
