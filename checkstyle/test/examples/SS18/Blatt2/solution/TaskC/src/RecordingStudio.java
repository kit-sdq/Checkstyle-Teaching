public class RecordingStudio {
    private String name;
    private Address address;

    public RecordingStudio(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    // getter to be able to work with the studio in other parts of the program
    public String getName() {
        return name;
    }

    // setter because the name can change over time
    public void setName(String name) {
        this.name = name;
    }

    // getter e.g. for navigation
    public Address getAddress() {
        return address;
    }

    //  setter because the studio can move
    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecordingStudio that = (RecordingStudio) o;
        return name.equals(that.name) && address.equals(that.address);
    }

    @Override
    public String toString() {
        return "RecordingStudio"
                + "name='" + name + '\''
                + ", address=" + address;
    }
}
