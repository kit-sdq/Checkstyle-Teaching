/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.praktomat.main;

/**
 * @author Thomas Weber
 * @version 1.0
 */
public interface PraktomatUsageInterface {
    /**
     * In case the given cmd contains a new tutor name, creates a new tutor.
     * Always sets the given tutor as active.
     *
     * @param state a state of the program
     * @param cmd splitted input string as string array
     */
    void executeTutoriumsCommand(State state, String[] cmd);

    /**
     * Adds a new student to the praktomat system if possible, otherwise
     * prints an error message through {@link Shell#println(Object)}.
     *
     * @param state a state of the program
     * @param cmd splitted input string as string array
     */
    void executeStudentCommand(State state, String[] cmd);

    /**
     * Adds a new task with the given information to the praktomat and prints
     * it.
     *
     * @param state a state of the program
     * @param cmd splitted input string as string array
     */
    void executeTaskCommand(State state, String[] cmd);

    /**
     * Prints a list of all students as described in the assignment.
     *
     * @param state a state of the program
     */
    void executeListStudentsCommand(State state);

    /**
     * Adds a submit to the praktomat if possible, otherwise prints an error
     * message. For restrictions refer to the assignment.
     *
     * @param state a state of the program
     * @param cmd splitted input string as string array
     */
    void executeSubmitCommand(State state, String[] cmd);


    /**
     * Adds a review to the praktomat if possible, otherwise prints an
     * error message. For restrictions refer to the assignment.
     *
     * @param state a state of the program
     * @param cmd splitted input string as string array
     */
    void executeReviewCommand(State state, String[] cmd);


    /**
     * Prints a list of all solutions for the given task, otherwise prints an
     * error message. For restrictions refer to the assignment.
     *
     * @param state a state of the program
     * @param cmd splitted input string as string array
     */
    void executeListSolutionsCommand(State state, String[] cmd);


    /**
     * Prints a list of all results if possible, otherwise prints an
     * error message. For restrictions refer to the assignment.
     *
     * @param state a state of the program
     */
    void executeResultsCommand(State state);


    /**
     * Prints a list of all summarized reviews if possible, otherwise prints an
     * error message. For restrictions refer to the assignment.
     *
     * @param state a state of the program
     */
    void executeSummaryTaskCommand(State state);


    /**
     * Prints a list of all summarized tutors if possible, otherwise prints an
     * error message. For restrictions refer to the assignment.
     *
     * @param state a state of the program
     */
    void executeSummaryTutorCommand(State state);


    /**
     * Resets the program, therefore resets the current state.
     *
     * @param state a state of the program
     */
    void executeResetCommand(State state);


    /**
     * Quits the program, therefore sets the state to not running.
     *
     * @param state a state of the program
     */
    void executeQuiteCommand(State state);
}
