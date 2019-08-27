package edu.kit.informatik;

/**
 * This class models a booking of a flight by a customer. It also contains a invoice number for uniqueness.
 * 
 * @author Peter Oettig
 * @version 1.0
 */
public class Booking implements Comparable<Booking> {
    private static int invoiceCounter = 1;
    
    private final int invoiceNumber;
    // Flight is in here to be able to create independent bookings without using the map in BookingSystem.
    // This increases flexibility and extendability.
    private Flight flight;
    private Customer customer;

    /**
     * Creates a new booking for a flight by a customer.
     * 
     * @param flight The booked flight.
     * @param customer The booking customer.
     */
    public Booking(Flight flight, Customer customer) {
        this.flight = flight;
        this.customer = customer;
        this.invoiceNumber = invoiceCounter++;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        
        Booking booking = (Booking) other;
        return invoiceNumber == booking.invoiceNumber;
    }

    @Override
    public int hashCode() {
        return invoiceNumber;
    }

    @Override
    public String toString() {
        return invoiceNumber + "," + customer.getCustomerNumber();
    }

    /**
     * Converts a booking to a string representation but with the flight id included.
     * 
     * @return The string representation with the flight id.
     */
    public String toStringWithFlightId() {
        return customer.getCustomerNumber() + "," + flight.getFlightId() + "," + invoiceNumber;
    }
    
    @Override
    public int compareTo(Booking other) {
        int resultCustomer = customer.compareTo(other.customer); 
        if (resultCustomer == 0) {
            // Reverse sorting
            return other.flight.compareTo(flight);
        }
        
        return resultCustomer;
    }
}
