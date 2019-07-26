/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

/**
 * Representing a film studio with a name and an address.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class FilmStudio {
    /**
     * The name of the film studio.
     */
    private String name;

    /**
     * The address of the film studio.
     */
    private Address address;

    /**
     * Instantiates a new film studio with the given date.
     *
     * @param name the name of the film studio
     * @param address the address of the film studio
     */
    public FilmStudio(final String name, final Address address) {
        this.name = name;
        this.address = address;
    }

    /**
     * Returns the address of the film studio as {@link Address}.
     *
     * @return the address of the film studio.
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Changes the address of the film studio.
     *
     * @param address the new address of the film studio as {@link
     *         Address}
     */
    public void setAddress(final Address address) {
        this.address = address;
    }

    /**
     * @return the name of the film studio
     */
    public String getName() {
        return name;
    }

    /**
     * Changes the name of the film studio.
     *
     * @param name the new name of the film studio
     */
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        // Returns existing information about the film studio
        return "FilmStudio{" + "name='" + name + '\'' + ", address=" + address
               + '}';
    }
}
