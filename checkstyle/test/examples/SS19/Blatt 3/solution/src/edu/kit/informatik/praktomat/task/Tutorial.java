/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.praktomat.task;

import edu.kit.informatik.praktomat.users.Student;
import edu.kit.informatik.praktomat.users.Tutor;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Encapsulates a tutorial as stated in the assignment.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class Tutorial {

    /**
     * The tutor of this tutorial.
     */
    private final Tutor tutor;

    /**
     * List of all students in this tutorial.
     */
    private List<Student> members = new LinkedList<>();

    /**
     * Maps the matriculation number of a student to all of his solutions.
     */
    private Map<Integer, List<Solution>> matr2sol = new HashMap<>();

    /**
     * Instantiates a new tutorial with the given tutor.
     *
     * @param tutor the tutor of the tutorial
     * @throws NullPointerException occurs if given tutor is null
     */
    public Tutorial(final Tutor tutor) throws NullPointerException {
        if (tutor == null) {
            throw new NullPointerException("Given tutor object is null!");
        }
        this.tutor = tutor;
    }

    /**
     * Checks whether or not the given student is part of this tutorial.
     *
     * @param student a student
     * @return true if the given student is in this tutorial, false otherwise
     */
    public boolean contains(final Student student) {
        return members.contains(student);
    }

    /**
     * Adds the given student to this tutorial.
     *
     * @param student a student
     */
    public void add(final Student student) {
        members.add(student);
    }

    /**
     * @return the tutor of this tutorial
     */
    public Tutor getTutor() {
        return tutor;
    }

    /**
     * Adds the given solution to this tutorial if possible.
     *
     * @param solution a solution
     * @return if the submit was successful
     * @throws IllegalArgumentException occurs if the author of the
     *         given
     *         solution is not part of this tutorial
     */
    public boolean submit(final Solution solution) throws IllegalArgumentException {
        if (!members.contains(solution.getAuthor())) {
            throw new IllegalArgumentException();
        }

        final Integer matrnr = solution.getAuthor().getMatriculationNumber();
        List<Solution> solutions = matr2sol.get(matrnr);
        if (solutions == null) {
            solutions = new LinkedList<>();
            matr2sol.put(matrnr, solutions);
        }

        if (!solutions.contains(solution)) {
            solutions.add(solution);
            return true;
        }

        return false;
    }

    /**
     * Returns the solution of the given student if possible, null otherwise.
     *
     * @param student a student
     * @param task a task
     * @return the solution of the given student for the given task if
     *         present, null otherwise
     */
    public Solution getSolution(final Student student, final Task task) {
        if (matr2sol.containsKey(student.getMatriculationNumber())) {
            for (Solution sol : getSolutions(student)) {
                if (task.equals(sol.getTask())) {
                    return sol;
                }
            }
        }

        return null;
    }

    /**
     * Returns the submitted solutions for the given student (may be an empty
     * list).
     *
     * @param student a student
     * @return a list of all solutions for the given student
     */
    public List<Solution> getSolutions(final Student student) {
        if (!members.contains(student)) {
            throw new IllegalArgumentException();
        }

        List<Solution> sols = matr2sol.get(student.getMatriculationNumber());
        if (sols == null) {
            return new LinkedList<>();
        } else {
            return Collections.unmodifiableList(sols);
        }
    }

    /**
     * Returns a list of all solutions in this tutorial, ordered by the
     * matriculation number of the author.
     *
     * @return a list of all solutions in this tutorial
     */
    public List<Solution> getSolutions() {
        final List<Solution> sols = new LinkedList<>();

        for (List<Solution> studSolutions : matr2sol.values()) {
            sols.addAll(studSolutions);
        }

        return Collections.unmodifiableList(sols);
    }

    /**
     * Returns the number of reviewed solutions in this tutorial.
     *
     * @return the number of reviews
     */
    public int getNumberOfReviews() {
        int count = 0;
        for (Solution sol : getSolutions()) {
            if (sol.hasReview()) {
                count++;
            }
        }

        return count;
    }

    /**
     * @return number of solutions in this tutorial
     */
    public int getNumberOfSolutions() {
        return getSolutions().size();
    }

    /**
     * @return number of students in this tutorial
     */
    public int getNumberOfStudents() {
        return members.size();
    }

}
