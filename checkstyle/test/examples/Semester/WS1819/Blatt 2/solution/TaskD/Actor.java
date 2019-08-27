/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

/**
 * Representing an actor with a unique identifier, a list of films the actor
 * participates in, a name, birth date and an address.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public class Actor {
    /**
     * Static class attribute to ensure ids are unique.
     */
    private static int uniqueIdCounter = 0;

    /**
     * Array of films the actor participates.
     */
    private Film[] filmsActingIn;

    /**
     * The name of the actor.
     */
    private String name;

    /**
     * Date the actor was born.
     */
    private final Date birthDate;

    /**
     * Current address the actor lives.
     */
    private Address address;

    /**
     * Unique Identifier of the actor.
     */
    private final int uniqueId;

    /**
     * Instantiates a new actor object with the given data. No checks or
     * calculations are performed.
     *
     * @param filmsActingIn list of films the actor participates in (may
     *         be an empty lis)
     * @param name name of the actor
     * @param birthDate birth date of the actor
     * @param address current address of the actor
     */
    public Actor(final Film[] filmsActingIn, final String name,
            final Date birthDate, final Address address) {
        this.filmsActingIn = filmsActingIn;
        this.name = name;
        this.birthDate = birthDate;
        this.address = address;
        this.uniqueId = uniqueIdCounter++;
    }

    /**
     * Adds the given film to the list of films the actor participates in.
     *
     * @param film the film the actor acts in
     * @return whether or not the film was added successfully
     */
    public boolean addFilm(final Film film) {
        int insertIndex = findFilmIndex(null);
        if (insertIndex != -1) {
            this.filmsActingIn[insertIndex] = film;
            return true;
        }
        return false;
    }

    /**
     * Removes a film from the list of films the actor participates (changed
     * cast or similar).
     *
     * @param film the file to remove from the list
     * @return whether or not the film was removed successfully
     */
    public boolean removeFilm(final Film film) {
        int removeIndex = findFilmIndex(film);
        if (removeIndex != -1) {
            this.filmsActingIn[removeIndex] = null;
            return true;
        }
        return false;
    }

    /**
     * @return name of the actor as String
     */
    public String getName() {
        return name;
    }

    /**
     * Changes the name of the actor in case the actor changes his or her name.
     *
     * @param name the new name of the actor
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the birth date of the actor as a {@link Date}
     */
    public Date getBirthDate() {
        return birthDate;
    }

    // no setter because you cannot change the date you are born

    /**
     * @return the address of the actor as {@link Address}
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Changes the address in case the actor moves.
     *
     * @param address the new address of the actor
     */
    public void setAddress(final Address address) {
        this.address = address;
    }

    /**
     * Checks if the actor acts in the given film (not requested in the task).
     *
     * @param film the film the actor is perhaps acting in
     * @return returns whether or not the actor acts in the given film
     */
    public boolean actsIn(final Film film) {
        int filmIndex = findFilmIndex(film);
        return (filmIndex != -1 && filmsActingIn[filmIndex] != null);
    }

    /**
     * @return the unique id of the actor
     */
    public int getUniqueId() {
        return uniqueId;
    }

    // change a unique id is never a good idea...

    /**
     * Searches the films the actor acts in for the given film and returns
     * the index of the film if possible, the first empty index otherwise. If
     * the array is full, return -1.
     *
     * @param film the film whose index we want to know
     * @return the index of the film if present, -1 otherwise
     */
    private int findFilmIndex(final Film film) {
        for (int index = 0; index < this.filmsActingIn.length; index++) {
            if (filmsActingIn[index] != null && filmsActingIn[index]
                    .equals(film)) {
                return index;
            }
        }
        // no film found, return first null index
        for (int index = 0; index < this.filmsActingIn.length; index++) {
            if (filmsActingIn[index] == null) {
                return index;
            }
        }
        // array full, return -1
        return -1;
    }

    /**
     * Returns a String representation for the given array, based on the
     * toString method of the given objects.
     *
     * @param array the array we need the representation from
     * @return a representation of the array elements, separated by a comma
     */
    private String arrayToStringRepresentation(Object[] array) {
        StringBuilder representation = new StringBuilder();
        for (Object object : array) {
            representation.append(object.toString());
            representation.append(", ");
        }
        return representation.toString();
    }

    @Override
    public String toString() {
        // returns all relevant information about the actor
        return "Actor{" + "filmsActingIn=" + arrayToStringRepresentation(
                filmsActingIn) + ", name='" + name + '\'' + ", birthDate="
               + birthDate + ", address=" + address + ", uniqueId=" + uniqueId
               + '}';
    }
}
