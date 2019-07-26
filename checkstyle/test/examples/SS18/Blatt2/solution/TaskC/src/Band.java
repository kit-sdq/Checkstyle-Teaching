public class Band implements Artist {
    // this is not in the task and not needed because of that, but a band definitely needs a name.
    private String name;
    private final Date dateOfFormation;
    private Date dateOfDisband;

    public Band(String name, Date dateOfFormation) {
        this.name = name;
        this.dateOfFormation = dateOfFormation;
    }

    // for when the band doesn't exist anymore on creation
    public Band(String name, Date dateOfFormation, Date dateOfDisband) {
        this.name = name;
        this.dateOfFormation = dateOfFormation;
        this.dateOfDisband = dateOfDisband;
    }

    // getter for usage in e.g. a biography
    public String getName() {
        return name;
    }

    // setter because the name could need to change for e.g. legal reasons
    public void setName(String name) {
        this.name = name;
    }

    // getter for e.g. biographies
    public Date getDateOfFormation() {
        return dateOfFormation;
    }

    // no setter because the date of formation can't change

    // getter for e.g. biographies
    public Date getDateOfDisband() {
        return dateOfDisband;
    }

    // setter because a band can disband after object creation
    public void setDateOfDisband(Date dateOfDisband) {
        this.dateOfDisband = dateOfDisband;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Band band = (Band) o;
        return name.equals(band.name) && dateOfFormation.equals(band.dateOfFormation)
                && dateOfDisband.equals(band.dateOfDisband);
    }

    @Override
    public String toString() {
        return "Band "
                + "name='" + name + '\''
                + ", dateOfFormation=" + dateOfFormation
                + ", dateOfDisband=" + dateOfDisband;
    }
}
