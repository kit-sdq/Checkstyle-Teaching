package edu.kit.informatik;

import java.util.Objects;

public class Discipline implements Comparable<Discipline> {
    private String sport;
    private String disciplineName;

    public Discipline(String sport, String disciplineName) {
        this.sport = sport;
        this.disciplineName = disciplineName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Discipline that = (Discipline) o;
        return Objects.equals(sport, that.sport) 
                && Objects.equals(disciplineName, that.disciplineName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(sport, disciplineName);
    }

    @Override
    public String toString() {
        return sport + " " + disciplineName;
    }

    @Override
    public int compareTo(Discipline o) {
        int result = sport.compareTo(o.sport);
        if (result == 0) {
            return disciplineName.compareTo(o.disciplineName);
        } else {
            return result;
        }
    }
}
