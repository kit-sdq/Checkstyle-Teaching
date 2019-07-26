/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.praktomat.task;

import edu.kit.informatik.praktomat.users.Tutor;

/**
 * Encapsulates a review as stated in the assignment.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class Review {

    /**
     * Best grade possible.
     */
    public static final int MIN_GRADE = 1;

    /**
     * Worst grade possible.
     */
    public static final int MAX_GRADE = 6;

    /**
     * The tutor that reviewed the solution.
     */
    private final Tutor reviewer;

    /**
     * The reviewed solution.
     */
    private final Solution reviewedSolution;

    /**
     * A comment for the review.
     */
    private final String comment;

    /**
     * The grade of the review.
     */
    private final int grade;

    /**
     * Insantiates a new review with the given reviewer, solution, review
     * comment and review grade.
     *
     * @param reviewer a tutor
     * @param reviewedSolution a solution
     * @param comment a string comment for the review
     * @param grade an int grade for the review
     * @throws NullPointerException occurs if one of the parameters is null
     */
    public Review(final Tutor reviewer, final Solution reviewedSolution, final String comment,
            final int grade) throws NullPointerException {
        if (reviewer == null || reviewedSolution == null || comment == null) {
            throw new NullPointerException("Some of the given data is null!");
        }
        this.comment = comment;
        this.grade = grade;
        this.reviewedSolution = reviewedSolution;
        this.reviewer = reviewer;
    }

    /**
     * Checks if the given grade is valid as a review grade, therefore if it
     * is between the {@value MIN_GRADE} and {@value MAX_GRADE}.
     *
     * @param grade a grade we want to check
     * @return whether its valid or not
     */
    public static boolean isValidGrade(final int grade) {
        return grade >= MIN_GRADE && grade <= MAX_GRADE;
    }

    /**
     * @return the grade for this review
     */
    public int getGrade() {
        return grade;
    }

    /**
     * @return the comment for this review
     */
    public String getComment() {
        return comment;
    }

    /**
     * @return the tutor that created this review
     */
    public Tutor getReviewer() {
        return reviewer;
    }

    /**
     * @return the solution that has been reviewed with this review
     */
    public Solution getSolution() {
        return reviewedSolution;
    }
}
