/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.praktomat.main;

import edu.kit.informatik.praktomat.task.Review;
import edu.kit.informatik.praktomat.task.Solution;
import edu.kit.informatik.praktomat.task.Task;
import edu.kit.informatik.praktomat.task.Tutorial;
import edu.kit.informatik.praktomat.users.Student;
import edu.kit.informatik.praktomat.users.Tutor;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static edu.kit.informatik.praktomat.main.Shell.error;
import static edu.kit.informatik.praktomat.main.Shell.println;

/**
 * Impelements the interface for praktomat interactions. Used to interact
 * with praktomat objects.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class PraktomatUsage implements PraktomatUsageInterface {


    /**
     * This int value is used to signal that an invalid int value could not
     * be parsed.
     */
    public static final int INVALID_INT = -1;

    /**
     * Tries to parse the given string to an int. If not possible,
     * {@value INVALID_INT} is returned.
     *
     * @param input string containing the int to be parsed
     * @return the int value contained if possible, {@value INVALID_INT}
     *         otherwise
     */
    private int parsePositiveInt(final String input) {
        int result;

        try {
            result = Integer.parseInt(input);
            if (!String.valueOf(result).equals(input)) {
                result = INVALID_INT;
            }
        } catch (IllegalArgumentException exception) {
            result = INVALID_INT;
        }

        return result;
    }


    @Override
    public void executeTutoriumsCommand(final State state, final String[] cmd) {
        final String name = cmd[1];
        Tutor ta = state.getPraktomat().findTutor(name);
        if (ta == null) {
            ta = state.getPraktomat().createTutor(name);
        }
        state.setTutor(ta);
    }


    @Override
    public void executeStudentCommand(final State state, final String[] cmd) {
        final String name = cmd[1];
        final int matrnr = parsePositiveInt(cmd[2]);

        if (matrnr < 0) {
            error("could not parse number.");
        } else if (state.getTutor() == null) {
            error("no current teaching assistent set.");
        } else if (state.getPraktomat().containsStudent(matrnr)) {
            error("a student with id " + matrnr + " already exists.");
        } else {
            state.getPraktomat().addStudent(name, matrnr, state.getTutor());
        }
    }


    @Override
    public void executeTaskCommand(final State state, final String[] cmd) {
        final String desc = cmd[1];
        final Task t = state.getPraktomat().createTask(desc);
        println(t);
    }


    @Override
    public void executeListStudentsCommand(final State state) {
        List<Student> studs = new LinkedList<>(
                state.getPraktomat().getStudents());
        Collections.sort(studs);

        for (Student st : studs) {
            Tutorial t = state.getPraktomat().getTutorial(st);
            println(st + ": " + t.getTutor());
        }
    }


    @Override
    public void executeSubmitCommand(final State state, final String[] cmd) {
        final int taskid = parsePositiveInt(cmd[1]);
        final int matrnr = parsePositiveInt(cmd[2]);
        final String desc = cmd[3];

        if (taskid < 0 || matrnr < 0) {
            error("could not parse number.");
        } else if (!state.getPraktomat().containsTask(taskid)) {
            error("assignment with id " + taskid + " not found.");
        } else if (!state.getPraktomat().containsStudent(matrnr)) {
            error("pupil with id " + matrnr + " not found.");
        } else {
            final Task t = state.getPraktomat().getTask(taskid);
            assert t != null;
            final Student st = state.getPraktomat().getStudent(matrnr);
            assert st != null;

            Solution sol = new Solution(t, st, desc);
            Tutorial tut = state.getPraktomat().getTutorial(st);

            if (!tut.submit(sol)) {
                error("could not submit solution.");
            }
        }
    }


    @Override
    public void executeReviewCommand(final State state, final String[] cmd) {
        final int taskid = parsePositiveInt(cmd[1]);
        final int matrnr = parsePositiveInt(cmd[2]);
        final int grade = parsePositiveInt(cmd[3]);
        final String desc = cmd[4];

        if (taskid < 0 || matrnr < 0 || grade < 0) {
            error("could not parse number.");
        } else if (!Review.isValidGrade(grade)) {
            error(grade + " is not a valid grade.");
        } else if (!state.getPraktomat().containsTask(taskid)) {
            error("assignment with id " + taskid + " not found.");
        } else if (!state.getPraktomat().containsStudent(matrnr)) {
            error("pupil with id " + matrnr + " not found.");
        } else {
            final Task t = state.getPraktomat().getTask(taskid);
            assert t != null;
            final Student st = state.getPraktomat().getStudent(matrnr);
            assert st != null;
            final Tutorial tut = state.getPraktomat().getTutorial(st);
            assert tut != null;
            final Solution sol = tut.getSolution(st, t);
            if (sol != null) {
                final Tutor ta = tut.getTutor();
                final Review review = ta.createReview(sol, desc, grade);
                println(review.getReviewer() + " reviewed " + review
                        .getSolution().getAuthor() + " with grade " + review
                                .getGrade());
            } else {
                error(st + " has not submitted a solution to " + t);
            }
        }
    }


    @Override
    public void executeListSolutionsCommand(final State state, final String[] cmd) {
        final int taskid = parsePositiveInt(cmd[1]);

        if (taskid < 0) {
            error("could not parse number.");
        } else if (!state.getPraktomat().containsTask(taskid)) {
            error("task with id " + taskid + " not found.");
        } else {
            final Task t = state.getPraktomat().getTask(taskid);
            assert t != null;
            final List<Solution> soltask = state.getPraktomat().getSolutions(t);
            Collections.sort(soltask, new Comparator<Solution>() {
                @Override
                public int compare(Solution o1, Solution o2) {
                    return o1.getAuthor().compareTo(o2.getAuthor());
                }
            });

            for (Solution sol : soltask) {
                println(sol);
            }
        }
    }


    @Override
    public void executeResultsCommand(final State state) {

        final List<Task> tasklist = state.getPraktomat().getTasks();
        Collections.sort(tasklist);

        for (Task t : tasklist) {
            println(t + ": " + t.getDescription());

            final List<Review> reviews = t.getReviews();
            reviews.sort(
                    Comparator.comparing(o -> o.getSolution().getAuthor()));

            for (Review r : reviews) {
                println(r.getSolution().getAuthor().getMatriculationNumber()
                        + ": " + r.getGrade());
            }
        }
    }


    @Override
    public void executeSummaryTaskCommand(final State state) {

        final List<Task> tasklist = state.getPraktomat().getTasks();
        Collections.sort(tasklist);

        for (Task t : tasklist) {
            final List<Solution> sols = t.getSolutions();
            final List<Review> reviews = t.getReviews();
            println(t + ": " + reviews.size() + " / " + sols.size());
        }
    }


    @Override
    public void executeSummaryTutorCommand(final State state) {

        final List<Tutorial> tuts = new LinkedList<>(
                state.getPraktomat().getTutorials());
        tuts.sort(Comparator.comparing(o -> o.getTutor().getName()));

        for (Tutorial tut : tuts) {
            final int submitted = tut.getNumberOfSolutions();
            final int students = tut.getNumberOfStudents();
            final int reviews = tut.getNumberOfReviews();
            final int missing = submitted - reviews;

            println(tut.getTutor() + ": " + students + " pupils, " + missing
                    + " missing review(s)");
        }
    }


    @Override
    public void executeResetCommand(final State state) {
        state.reset();
    }


    @Override
    public void executeQuiteCommand(final State state) {
        state.quit();
    }



}
