// The name of the class started with lowercase letter. That doesn't fit the conventions, but isn't a syntactical error.
public class TestFoo {
    // String was lowercase, the data type "string" doesn't exist as data types are case sensitive.
    public static void main(String[] args) {
        // For assignment and initialization only one "=" is used. "==" is for comparisons.
        // The semicolon was missing at the end of the lane which ends the statement.
        Foo foo = new Foo();
        // System was lowercase which isn't the name of the class.
        // The semicolon was missing at the end of the lane which ends the statement.
        // The quotes around "Name:" where missing which mark it as a String.
        System.out.println("Name:" + foo.name);
    }
}