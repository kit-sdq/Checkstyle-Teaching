/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.praktomat.main;

import edu.kit.informatik.praktomat.task.Solution;
import edu.kit.informatik.praktomat.task.Task;
import edu.kit.informatik.praktomat.task.Tutorial;
import edu.kit.informatik.praktomat.users.Student;
import edu.kit.informatik.praktomat.users.Tutor;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The praktomat class encapsulates a system for submitting tasks and
 * evaluating them.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class Praktomat {

    /**
     * Counter for the task ids like stated in the assignment.
     */
    private int taskid = 1;

    /**
     * Map of all students, maps from the pupilId to the student objects.
     */
    private Map<Integer, Student> students = new HashMap<>();

    /**
     * List of all tutors.
     */
    private List<Tutor> tutors = new LinkedList<>();

    /**
     * List of all tutorials.
     */
    private List<Tutorial> tutorials = new LinkedList<>();

    /**
     * Map of all tasks, maps from the assignmentId to the task.
     */
    private Map<Integer, Task> tasks = new HashMap<>();

    /**
     * Adds a new student to the praktomat.
     *
     * @param name the name of the student
     * @param matrnr the matriculation number of the student
     * @param tutor the tutor of the student
     * @return the newly created student object
     * @throws IllegalArgumentException if a student with this
     *         matriculation
     *         number already exists, an exception is thrown
     */
    public Student addStudent(final String name, final int matrnr, final Tutor tutor)
            throws IllegalArgumentException {
        if (students.containsKey(matrnr)) {
            throw new IllegalArgumentException();
        }

        final Student stud = new Student(name, matrnr, tutor);

        students.put(stud.getMatriculationNumber(), stud);

        return stud;
    }

    /**
     * Returns the task with the given id if possible, otherwise null.
     *
     * @param id the id of the task
     * @return the task with the given id if it exists, null otherwise
     */
    public Task getTask(final int id) {
        return tasks.get(id);
    }

    /**
     * Returns the tutorial of the given student.
     *
     * @param student a student
     * @return the tutorial of the given student
     */
    public Tutorial getTutorial(final Student student) {
        Tutorial result = null;

        for (Tutorial t : tutorials) {
            if (t.contains(student)) {
                result = t;
                break;
            }
        }

        return result;
    }

    /**
     * Returns the tutor with the given name or null.
     *
     * @param name the name of the tutor
     * @return the tutor with the given name if it exists, null otherwise
     */
    public Tutor findTutor(final String name) {
        for (Tutor tutor : tutors) {
            if (name.equals(tutor.getName())) {
                return tutor;
            }
        }

        return null;
    }

    /**
     * Adds a new tutor to the praktomat.
     *
     * @param name the name of the new tutor
     * @return the newly created tutor
     */
    public Tutor createTutor(final String name) {
        if (containsTutor(name)) {
            throw new IllegalArgumentException();
        }

        final Tutor tutor = new Tutor(name);
        tutors.add(tutor);
        tutorials.add(tutor.getTutorial());

        return tutor;
    }

    /**
     * Adds a new task to the praktomat.
     *
     * @param description the description of the task
     * @return the newly created task
     */
    public Task createTask(final String description) {
        final Task t = new Task(taskid, description, this);
        taskid++;
        tasks.put(t.getId(), t);

        return t;
    }

    /**
     * Returns the student with the given matriculation number if possible,
     * null otherwise.
     *
     * @param matrnr the matriculation number of the student
     * @return the student if it exists, null otherwise
     */
    public Student getStudent(final int matrnr) {
        return students.get(matrnr);
    }

    /**
     * Returns whether or not a tutor with the given name exists.
     *
     * @param name the name of the tutor
     * @return if a tutor with that name exists
     */
    public boolean containsTutor(final String name) {
        for (Tutor tutor : tutors) {
            if (name.equals(tutor.getName())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns whether or not a task with the given id exists.
     *
     * @param taskid the id of the task
     * @return if a task with this id exists
     */
    public boolean containsTask(final int taskid) {
        return tasks.containsKey(taskid);
    }

    /**
     * Returns whether or not a student with the given matriculation number
     * exists.
     *
     * @param matrnr the matriculation number of the student
     * @return if a student with that number exists
     */
    public boolean containsStudent(final int matrnr) {
        return students.containsKey(matrnr);
    }

    /**
     * Returns a list of all solutions for the given task currently submitted
     * to this praktomat.
     *
     * @param task a task
     * @return list of solutions for this task, may be an empty list
     */
    public List<Solution> getSolutions(final Task task) {
        final List<Solution> sols = new LinkedList<>();

        for (Tutorial tut : tutorials) {
            for (Solution sol : tut.getSolutions()) {
                if (task.equals(sol.getTask())) {
                    sols.add(sol);
                }
            }
        }

        return sols;
    }

    /**
     * @return List of all saved tasks
     */
    public List<Task> getTasks() {
        return new LinkedList<>(tasks.values());
    }

    /**
     * @return List of all saved students
     */
    public List<Student> getStudents() {
        return new LinkedList<>(students.values());
    }

    /**
     * @return List of all saved tutorials
     */
    public List<Tutorial> getTutorials() {
        return Collections.unmodifiableList(tutorials);
    }

}
