/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.praktomat.task;

import edu.kit.informatik.praktomat.users.Student;

/**
 * This class encapsulates a solution as described in the assignment.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class Solution {

    /**
     * The task or this solution.
     */
    private final Task task;

    /**
     * The author of the solution. The author is always a student.
     */
    private final Student author;

    /**
     * The solution for the task as string comment.
     */
    private final String solution;

    /**
     * The review for this solution.
     */
    private Review review;

    /**
     * Instantiates a new Solution for the given task with the given author
     * and the given string description of the solution.
     *
     * @param task a task
     * @param author a student
     * @param solution a String
     * @throws NullPointerException occurs if one of the parameters is null
     */
    public Solution(final Task task, final Student author, final String solution)
            throws NullPointerException {
        if (task == null || author == null || solution == null) {
            throw new NullPointerException("Some of the given arguments are "
                                           + "null!");
        }
        this.task = task;
        this.author = author;
        this.solution = solution;
    }


    /**
     * @return the author of this solution
     */
    public Student getAuthor() {
        return author;
    }

    /**
     * @return the text of this solution
     */
    public String getText() {
        return solution;
    }

    /**
     * @return the task of this solution
     */
    public Task getTask() {
        return task;
    }

    /**
     * Returns the review of this solution. Might be null if no review has
     * been added before.
     *
     * @return the review of this solution
     */
    public Review getReview() {
        return review;
    }

    /**
     * @return whether or not this solution has a review
     */
    public boolean hasReview() {
        return review != null;
    }

    /**
     * Sets the given review as new one if possible.
     *
     * @param rev sets the review of this solution to the given one
     * @throws IllegalArgumentException Occurs if the reviewer is not
     *         the tutor for the student for this solution
     */
    public void setReview(final Review rev) throws IllegalArgumentException {
        if (!rev.getReviewer().equals(author.getTutor())) {
            throw new IllegalArgumentException();
        }

        this.review = rev;
    }

    @Override
    public int hashCode() {
        return task.getId() * author.getMatriculationNumber();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Solution) {
            Solution other = (Solution) obj;

            return author.equals(other.author) && task.equals(other.task);
        }

        return false;
    }

    @Override
    public String toString() {
        return author + ": " + solution;
    }
}
