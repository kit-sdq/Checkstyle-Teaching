/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.praktomat.task;

import edu.kit.informatik.praktomat.main.Praktomat;

import java.util.LinkedList;
import java.util.List;


/**
 * This class encapsulates a task as described in the assignment.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class Task implements Comparable<Task> {

    /**
     * id of the task.
     */
    private final int id;

    /**
     * the praktomat this task belongs to.
     */
    private final Praktomat praktomat;

    /**
     * A textual description of the task.
     */
    private String description;

    /**
     * Instantiates a new task with the given id, description and praktomat.
     * No restrictions apply.
     *
     * @param id integer id of the task
     * @param description description of the task
     * @param praktomat praktomat of the task
     * @throws NullPointerException occurs if one of the parameters is null
     */
    public Task(final int id, final String description, final Praktomat praktomat)
            throws NullPointerException {
        this.id = id;
        if (description == null || praktomat == null) {
            throw new NullPointerException("Given description or praktomat is"
                                           + " null!");
        }
        this.description = description;
        this.praktomat = praktomat;
    }

    /**
     * @return the description of the task
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the id of the task
     */
    public int getId() {
        return id;
    }


    /**
     * @return list of solutions for this task
     */
    public List<Solution> getSolutions() {
        return praktomat.getSolutions(this);
    }

    /**
     * @return list of reviews for this task
     */
    public List<Review> getReviews() {
        final List<Solution> sols = getSolutions();

        return filterReviews(sols);
    }

    /**
     * Returns all reviews for the list of given solutions as list.
     *
     * @param solutions list of solutions
     * @return list of reviews
     */
    public List<Review> filterReviews(final List<Solution> solutions) {
        final List<Review> reviews = new LinkedList<>();
        for (final Solution solution : solutions) {
            if (solution.hasReview()) {
                reviews.add(solution.getReview());
            }
        }
        /*
        alternative solution using streams
         filters the solutions for all solutions that have a review and add
         that review to the new review list
         solutions.stream().filter(Solution::hasReview).forEach(it -> reviews
         .add(it.getReview()));
        */
        return reviews;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof Task) {
            final Task other = (Task) o;
            return this.id == other.id;
        }

        return false;
    }

    @Override
    public String toString() {
        return "assignment id(" + id + ")";
    }

    @Override
    public int compareTo(Task o) {
        return this.id - o.id;
    }
}
