package edu.kit.informatik;

public class Date implements Comparable<Date> {
    private final int day;
    private final int week;
    
    public Date(int day) {
        this.day = day;
        this.week = day / 7 + 1;
    }

    public int getDay() {
        return day;
    }

    public int getWeek() {
        return week;
    }

    @Override
    public int compareTo(Date o) {
        return Integer.compare(day, o.day);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Date date = (Date) o;

        if (day != date.day) {
            return false;
        }
        
        return week == date.week;
    }

    @Override
    public int hashCode() {
        return day;
    }

    @Override
    public String toString() {
        return "Day " + day + ", week " + week;
    }
}
