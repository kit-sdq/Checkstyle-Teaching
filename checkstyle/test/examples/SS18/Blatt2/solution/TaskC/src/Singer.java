public class Singer extends Person implements Artist {
    private final Date birthday;
    private Date dateOfDeath;

    public Singer(String firstName, String lastName, Date birthday) {
        super(firstName, lastName);
        this.birthday = birthday;
    }

    // for when the singer is already dead
    public Singer(String firstName, String lastName, Date birthday, Date dateOfDeath) {
        super(firstName, lastName);
        this.birthday = birthday;
        this.dateOfDeath = dateOfDeath;
    }

    // getter to get info for e.g. a biography
    public Date getBirthday() {
        return birthday;
    }

    // no setter because a birthday can't change and you can't add someone that isn't even born yet

    // getter t oget info for e.g. a biography
    public Date getDateOfDeath() {
        return dateOfDeath;
    }

    // setter because a singer can die after creation of the object
    public void setDateOfDeath(Date dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    // No equals because a singer is a person and a person is unique by id, so the inherited equals is enough

    @Override
    public String toString() {
        return super.toString() + " Singer "
                + "birthday=" + birthday
                + ", dateOfDeath=" + dateOfDeath;
    }
}
