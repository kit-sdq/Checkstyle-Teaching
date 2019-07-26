package edu.kit.informatik;

/**
 * This class models an employee, which is a {@link Person} with a employee number.
 */
public class Employee extends Person {
    public static int nextEmployeeNumber = 0;

    private final int employeeNumber;

    public Employee(String firstName, String surname) {
        super(firstName, surname);
        this.employeeNumber = nextEmployeeNumber++;
    }

    public int getEmployeeNumber() {
        return employeeNumber;
    }

    //No setter for the employee number because it's determined at creation time and shouldn't be changed.

    public String toString() {
        return super.toString() + " " + employeeNumber;
    }
}
