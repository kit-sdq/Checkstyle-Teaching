package edu.kit.informatik;

/**
 * This class models a student. A student is a {@link Person} with a matriculation number and a semester count.
 * A student can attend lectures and tutorials.
 *
 * @author Peter Oettig
 * @version 1.0
 */
public class Student extends Person {
    private static int nextMatrNum = 0;

    private final int matriculationNumber;
    private int semesterCount;

    private Lecture[] visitedLectures;
    private Tutorial[] visitedTutorials;

    /**
     * The constructor to create a student.
     *
     * @param firstName     The first name of the student.
     * @param surname       The surname of the student.
     * @param semesterCount The count of semesters a student has had.
     */
    public Student(String firstName, String surname, int semesterCount) {
        super(firstName, surname);
        this.matriculationNumber = nextMatrNum++;
        this.semesterCount = semesterCount;
        //Arbitrary initialization sizes because there was no specification.
        this.visitedLectures = new Lecture[10];
        this.visitedTutorials = new Tutorial[10];
    }

    /**
     * Gets the matriculation number of the student.
     *
     * @return The matriculation number.
     */
    public int getMatriculationNumber() {
        return matriculationNumber;
    }

    /**
     * Gets the semester count of the student.
     *
     * @return The semester count.
     */
    public int getSemesterCount() {
        return semesterCount;
    }


    /**
     *  Gets the visited lectures of the student.
     *
     * @return An array of the visited lectures.
     */
    public Lecture[] getVisitedLectures() {
        return visitedLectures;
    }

    /**
     * Gets the visited tutorials of the student.
     *
     * @return An array of the visited tutorials.
     */
    public Tutorial[] getVisitedTutorials() {
        return visitedTutorials;
    }

    /**
     * Sets the semester count of the student.
     *
     * @param semesterCount The new semester count.
     */
    public void setSemesterCount(int semesterCount) {
        this.semesterCount = semesterCount;
    }

    //No setter for the matriculation number because it's determined at creation time and shouldn't be changed.
    //No setter for the lecture or tutorial array because they are usually added and removed one by one,
    //but this functionality would exceed the task.

    public String toString() {
        return super.toString() + " " + matriculationNumber + " " + semesterCount;
    }
}
