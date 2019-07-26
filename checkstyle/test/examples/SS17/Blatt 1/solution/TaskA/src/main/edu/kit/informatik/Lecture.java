package edu.kit.informatik;

/**
 * This class models a edu.kit.informatik.Lecture with a name, a number and a lecturer.
 *
 * @author Peter Oettig
 * @version 1.0
 */
public class Lecture {
    private static int nextLectureNumber = 0;

    private String name;
    private final int lectureNumber;
    private Lecturer lecturer;

    /**
     * The constructor to create a new lecture.
     *
     * @param name     The name of the lecture.
     * @param lecturer The lecturer which gives the lecture.
     */
    public Lecture(String name, Lecturer lecturer) {
        this.name = name;
        this.lectureNumber = nextLectureNumber++;
        this.lecturer = lecturer;
    }

    /**
     * Gets the name of the lecture.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the number of the lecture.
     *
     * @return The lecture number.
     */
    public int getLectureNumber() {
        return lectureNumber;
    }

    /**
     * Gets the lecturer of the lecture.
     *
     * @return The lecturer.
     */
    public Lecturer getLecturer() {
        return lecturer;
    }

    /**
     * Sets the name of the lecture to a new one.
     *
     * @param name The new name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the lecturer of the lecture to a new one.
     *
     * @param lecturer The new lecturer.
     */
    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }

    //No setter for the lecture number because it's determined at creation time and shouldn't be changed.

    public String toString() {
        return name + " " + lectureNumber;
    }
}
