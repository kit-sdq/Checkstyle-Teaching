/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

/**
 * Class to encapsulate the functionality of an address with a street name, a
 * house number, a post code and the name of the city.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class Address {
    /**
     * Name of the street.
     */
    private final String streetName;

    /**
     * String because of constructs like 7a or something similar.
     */
    private final String houseNumber;

    /**
     * Post code of the city.
     */
    private final int postCode;

    /**
     * Name of the city.
     */
    private final String cityName;

    /**
     * Initializes a new object with the given data. No checks or calculations
     * are performed.
     *
     * @param streetName name of the street of this address
     * @param houseNumber number of the house of this address
     * @param postCode post code of the city of this address
     * @param cityName name of the city of this address
     */
    public Address(final String streetName, final String houseNumber,
            final int postCode, final String cityName) {
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.postCode = postCode;
        this.cityName = cityName;
    }

    /**
     * Contains the street name of this address.
     *
     * @return the street name
     */
    public String getStreetName() {
        return streetName;
    }

    /**
     * Returns a String containing the house number of this address.
     *
     * @return the house number
     */
    public String getHouseNumber() {
        return houseNumber;
    }

    /**
     * Returns the post code of this address as int.
     *
     * @return the post code
     */
    public int getPostCode() {
        return postCode;
    }

    /**
     * Returns the city name of this address as String.
     *
     * @return the city name
     */
    public String getCityName() {
        return cityName;
    }

    /* no setters needed, class is immutable (address changes are almost
       impossible) in the rare case we have to change some of the data,
       create a new object */

    @Override
    public String toString() {
        // Returns the address with all relevant information
        return "Address{" + "streetName='" + streetName + '\''
               + ", houseNumber='" + houseNumber + '\'' + ", postCode="
               + postCode + ", cityName='" + cityName + '\'' + '}';
    }
}
