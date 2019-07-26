package edu.kit.informatik;

/**
 * This class models a tutorial with the lecture it is associated to and a tutorial number.
 *
 * @author Peter Oettig
 * @version 1.0
 */
public class Tutorial {
    private final Lecture lecture;
    private final int tutorialNumber;

    /**
     * The constructor to create a new tutorial.
     *
     * @param lecture        The associated lecture.
     * @param tutorialNumber The number of the tutorial.
     */
    public Tutorial(Lecture lecture, int tutorialNumber) {
        this.lecture = lecture;
        this.tutorialNumber = tutorialNumber;
    }

    /**
     * Gets the associated lecture.
     *
     * @return The associated lecture.
     */
    public Lecture getLecture() {
        return lecture;
    }

    /**
     * Gets the name of the tutorial.
     *
     * @return The name.
     */
    public String getName() {
        //Returns the name of the lecure, because they are always the same.
        return lecture.getName();
    }

    /**
     * Gets the number of the tutorial.
     *
     * @return The number.
     */
    public int getTutorialNumber() {
        return tutorialNumber;
    }

    //No setter for the lecture because the specific tutorial is bound to it and changing it would destroy that.
    //No setter for the tutorial number because it shouldn't be changeable afterwards.
    //No setter for name because the name is delegated to the lecture.

    public String toString() {
        return lecture.getName() + " " + tutorialNumber;
    }
}
