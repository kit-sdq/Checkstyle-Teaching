/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

public class Human {
    /* String must begin with a capital letter because the class is named java.lang.String and java is case-sesitive.
     * (Furthermore, java naming convention requests that classes should begin with a capital letter.)
     * Moreover, the semicolon is not there, so it must be inserted to complete the declaration statement. 
     * (The naming convention requires name to begin with a lower-case letter (not necessary for compilation)).
     */
    String name;
    int age;
    // one-line-commentaries have to begin with a double slash
    int height; // height in centimeter
    
    // String must begin with a capital letter (see above) and height is of type int, which must be explicitly coded.
    public Human (String name, int age, int height) {
        this.name = name;
        this.age = age;
        this.height = height;
    }
// closing bracket of class definition not there, must be inserted
}
