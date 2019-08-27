/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

public class Main {
    public static void main (String args[]) {
        /* Initializations are made with only one = instead of == (comparison).
         * Human must begin with a capital letter because the class is named Human and java is case-sensitive.
         * (newHuman should start with a lower-case letter (not necessary for compilation) as requested by the naming convention)
         * Since "John" is a String literal it must be put inside double quotation marks (")
         */
        Human newHuman = new Human ("John", 20, 180);
        
        /* newHuman must be named exactly the same as in its declaration.
         * Furthermore, a String concatenation operator (+) must be added between "is" and newHuman.age in order to concatenate the two Strings.
         * The last String must end with a double quotation mark(").
         */
        System.out.println(newHuman.name + " is " + newHuman.age + "years old.");
    }
}
