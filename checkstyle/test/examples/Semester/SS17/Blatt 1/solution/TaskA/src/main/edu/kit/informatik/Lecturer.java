package edu.kit.informatik;

/**
 * This class models a lecturer which is an {@link Employee} that gives lectures.
 * One lecturer gives only one lecture.
 *
 * @author Peter Oettig
 * @version 1.0
 */
public class Lecturer extends Employee {
    //No attribute lecture because each lecturer gives only one lecture.
    //This relationship is coded in the lecture itself.

    /**
     * The constructor to create a new lecturer.
     *
     * @param firstName The first name of the lecturer.
     * @param surname   The surname of the lecturer.
     */
    public Lecturer(String firstName, String surname) {
        super(firstName, surname);
    }
}
