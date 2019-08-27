package edu.kit.informatik;

/**
 * This class contains the main method to execute the program.
 */
public class Main {

    /**
     * The main method.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        Student student = new Student("Sauron", "Gorthaur", 3);
        Lecturer lecturer = new Lecturer("Morgoth", "Belegur");
        Lecture lecture = new Lecture("Forging", lecturer);
        Tutorial tutorial = new Tutorial(lecture, 1);

        System.out.println(student);
        System.out.println(lecturer);
        System.out.println(lecture);
        System.out.println(tutorial);
    }
}
