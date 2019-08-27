/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.praktomat.users;

import edu.kit.informatik.praktomat.task.Review;
import edu.kit.informatik.praktomat.task.Solution;
import edu.kit.informatik.praktomat.task.Tutorial;


/**
 * Encapsulates a tutor as stated in the assignment.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class Tutor extends User {

    /**
     * Every tutor has a tutorial.
     */
    private Tutorial tutorial;

    /**
     * Instantiates a new tutor with the given name. There are no restrictions.
     *
     * @param name the name of the tutor
     */
    public Tutor(String name) {
        super(name);
        this.tutorial = new Tutorial(this);
    }

    /**
     * @return the tutorial of the tutor
     */
    public Tutorial getTutorial() {
        return tutorial;
    }

    /**
     * Adds the given student to the tutorial of this tutor.
     *
     * @param student a student
     */
    public void addToTeach(Student student) {
        tutorial.add(student);
    }

    /**
     * Checks whether or not the given student is teached by this tutor.
     *
     * @param student the student we want to check
     * @return true if the student is teached by this tutor, false otherwise
     */
    public boolean isTeaching(Student student) {
        return tutorial.contains(student);
    }

    /**
     * Creates a review for the given solution with the given comment and
     * grade, if the author of the solution is teached by this tutor.
     *
     * @param solution the solution for the assignment
     * @param comment the comment for the solution
     * @param grade the grade for the solution
     * @return the newly created review
     */
    public Review createReview(Solution solution, String comment, int grade) {
        if (!isTeaching(solution.getAuthor())) {
            throw new IllegalArgumentException();
        }

        Review rev = new Review(this, solution, comment, grade);
        solution.setReview(rev);

        return rev;
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tutor) {
            Tutor other = (Tutor) obj;
            return getName().equals(other.getName());
        }

        return false;
    }


}
