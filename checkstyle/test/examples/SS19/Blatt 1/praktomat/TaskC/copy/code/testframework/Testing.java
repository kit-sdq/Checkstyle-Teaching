
package de.b_privat.testframework;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

public final class Testing {

    public static void main(final String[] args) throws Throwable {
        System.out.println(Expected.Support.get(Testing.class.getMethod("test")));
    }

    @B
    @Expected(NullPointerException.class)
    @Retention(RUNTIME)
    @interface A {
    }

    @C
    @Expected(IllegalArgumentException.class)
    @Retention(RUNTIME)
    @interface B {
    }

    @A
    @Expected(IllegalStateException.class)
    @Retention(RUNTIME)
    @interface C {

    }

    @A
    @Expected(value = Error.class, mandatory = false)
    @Retention(RUNTIME)
    @interface D {

    }

//    @A
    @D
    public static void test() {

    }
}
