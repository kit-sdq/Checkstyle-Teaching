package edu.kit.informatik;

import java.util.Objects;

public class Country implements Comparable<Country> {
    private String id;
    private String code;
    private String name;
    private String yearOfDetermination;

    public Country(String id, String code, String name, String yearOfDetermination) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.yearOfDetermination = yearOfDetermination;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return yearOfDetermination + " " + id + " " + code + " " + name;
    }

    @Override
    public int compareTo(Country o) {
        int result = yearOfDetermination.compareTo(o.yearOfDetermination);
        if (result == 0) {
            return id.compareTo(o.id);
        } else {
            return result;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return Objects.equals(id, country.id) 
                && Objects.equals(code, country.code)
                && Objects.equals(name, country.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, code, name);
    }
}
