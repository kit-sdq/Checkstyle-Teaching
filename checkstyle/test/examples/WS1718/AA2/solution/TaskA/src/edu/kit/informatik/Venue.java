package edu.kit.informatik;

public class Venue implements Comparable<Venue> {
    private String id;
    private Country country;
    private String location;
    private String name;
    private int yearOfOpening;
    private int numberOfSeats;

    public Venue(String id, Country country, String location, String name, int yearOfOpening, int numberOfSeats) {
        this.id = id;
        this.country = country;
        this.location = location;
        this.name = name;
        this.yearOfOpening = yearOfOpening;
        this.numberOfSeats = numberOfSeats;
    }

    public String getId() {
        return id;
    }

    public Country getCountry() {
        return country;
    }

    @Override
    public int compareTo(Venue o) {
        if (numberOfSeats == o.numberOfSeats) {
            return id.compareTo(o.id);
        } else {
            return Integer.compare(numberOfSeats, o.numberOfSeats);
        }
    }

    @Override
    public String toString() {
        return id + " " + location + " " + numberOfSeats;
    }
}
