public class Address {
    private final String street;
    // String because the number can contain letters (e.g. 5a)
    private final String number;
    private final int zip;
    private final String city;

    public Address(String street, String number, int zip, String city) {
        this.street = street;
        this.number = number;
        this.zip = zip;
        this.city = city;
    }

    // getter to be able to work with the address in other parts of the program
    public String getStreet() {
        return street;
    }

    // no setter because an address can't change over time

    // getter to be able to work with the address in other parts of the program
    public String getNumber() {
        return number;
    }

    // no setter because an address can't change over time

    // getter to be able to work with the address in other parts of the program
    public int getZip() {
        return zip;
    }

    // no setter because an address can't change over time

    // getter to be able to work with the address in other parts of the program
    public String getCity() {
        return city;
    }

    // no setter because an address can't change over time


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Address address = (Address) o;
        return zip == address.zip && street.equals(address.street) 
                && number.equals(address.number) && city.equals(address.city);
    }

    @Override
    public String toString() {
        return "Address"
                + "street='" + street + '\''
                + ", number='" + number + '\''
                + ", zip=" + zip
                + ", city='" + city + '\'';
    }
}
