/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.praktomat.users;


/**
 * Encapsulates a student as stated in the assignment.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class Student extends User implements Comparable<Student> {

    /**
     * Matriculation number of the student.
     */
    private final int matriculationNumber;

    /**
     * Tutor of the student.
     */
    private final Tutor tutor;

    /**
     * Instantiates a new student with the given name, matriculation number
     * and tutor.
     *
     * @param name the name of the student
     * @param matriculationNumber the matriculation number of the
     *         student
     * @param tutor the tutor of the student
     * @throws NullPointerException occurs if one of the parameters is null
     */
    public Student(final String name, final int matriculationNumber,
            final Tutor tutor) throws NullPointerException {
        super(name);
        if (tutor == null) {
            throw new NullPointerException("Given tutor is null!");
        }
        this.matriculationNumber = matriculationNumber;
        this.tutor = tutor;
        tutor.addToTeach(this);
    }


    /**
     * @return the matriculation number of the student
     */
    public int getMatriculationNumber() {
        return matriculationNumber;
    }

    /**
     * @return the tutor of the student
     */
    public Tutor getTutor() {
        return tutor;
    }

    @Override
    public int hashCode() {
        return matriculationNumber;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Student) {
            Student other = (Student) obj;
            return matriculationNumber == other.matriculationNumber;
        }

        return false;
    }

    @Override
    public int compareTo(final Student o) {
        return matriculationNumber - o.matriculationNumber;
    }

    @Override
    public String toString() {
        return "(" + matriculationNumber + "," + getName() + ")";
    }

}
