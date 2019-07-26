public class Date {
    private final int day;
    private final int month;
    private final int year;

    public Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    // getter to be able to work with the date
    public int getDay() {
        return day;
    }

    // no setter because a date object is immutable

    // getter to be able to work with the date
    public int getMonth() {
        return month;
    }

    // no setter because a date object is immutable

    // getter to be able to work with the date
    public int getYear() {
        return year;
    }

    // no setter because a date object is immutable


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Date date = (Date) o;
        return day == date.day && month == date.month && year == date.year;
    }

    @Override
    public String toString() {
        return "Date"
                + "day=" + day
                + ", month=" + month
                + ", year=" + year;
    }
}
