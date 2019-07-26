public class Person {
    private static int personCounter = 0;

    private final int id;
    private final String firstName;
    private String lastName;

    public Person(String firstName, String lastName) {
        this.id = personCounter++;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // getter for use in e.g. a database
    public int getId() {
        return id;
    }

    // no setter because an id can't change

    // getter for usage in different parts of the program and for subclasses
    public String getFirstName() {
        return firstName;
    }

    // no setter because a person can't usually change his first name

    // getter for usage in different parts of the program and for subclasses
    public String getLastName() {
        return lastName;
    }

    // setter because a person can e.g. marry
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Person person = (Person) o;
        return id == person.id;
    }

    @Override
    public String toString() {
        return "Person "
                + "id=" + id
                + ", firstName='" + firstName + '\''
                + ", lastName='" + lastName + '\'';
    }
}
