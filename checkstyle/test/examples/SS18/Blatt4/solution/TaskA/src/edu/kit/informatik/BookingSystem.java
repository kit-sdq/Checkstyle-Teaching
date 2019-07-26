package edu.kit.informatik;

import edu.kit.informatik.UI.InputException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * This class is for management and data storage of flights and booked tickets.
 *
 * @author Peter Oettig
 * @version 1.0
 */
public class BookingSystem {
    private Set<Flight> flights;
    private Set<Customer> customers;
    private Map<Flight, Set<Booking>> tickets;

    /**
     * The constructor for the booking system management class for data storage initialization.
     */
    public BookingSystem() {
        this.flights = new TreeSet<>();
        this.customers = new HashSet<>();
        this.tickets = new HashMap<>();
    }

    /**
     * Adds a new flight to the booking system.
     * 
     * @param flightId The ID of the new flight. 
     * @param start The starting airport of the new flight.
     * @param destination The destination airport of the new flight.
     * @param price The price of a ticket for the new flight.
     * @param currency The currency that the price is in.
     * @throws InputException if a flight with this ID already exists.
     */
    public void addFlight(String flightId, String start, String destination, double price, Currency currency)
            throws InputException {
        Flight flight = new Flight(flightId, start, destination, price, currency);
        if (flights.add(flight)) {
            tickets.put(flight, new HashSet<>());
        } else {
            throw new InputException("a flight with this number already exists.");
        }
    }

    /**
     * Removes the flight with a given flightId and all booked tickets for it.
     * 
     * @param flightId The ID of the flight to remove.
     * @throws InputException if the flight does not exist.
     */
    public void removeFlight(String flightId) throws InputException {
        // Gets the flight object to remove
        Flight flightToRemove = getFlightById(flightId);
        flights.remove(flightToRemove);
        tickets.remove(flightToRemove);
    }

    /**
     * Converts the list of existing flights to a string representation. One line per flight.
     * 
     * @return The string representation.
     */
    public String flightsToString() {
        return flights.stream().map(Flight::toString).collect(Collectors.joining("\n"));
    }

    /**
     * Books a flight for a specified customer.
     * Automatically creates a new customer entry if there is none already for this person.
     * 
     * @param flightId The id of the flight to book.
     * @param firstName The first name of the customer.
     * @param lastName The last name of the customer.
     * @return The finalized booking.
     * @throws InputException if the flight does not exist.
     */
    public Booking bookFlight(String flightId, String firstName, String lastName) throws InputException {
        // Check if a customer with this name combination already exists, sets it or otherwise creates a new one
        Customer customer = customers.stream()
                .filter(current -> current.equals(firstName, lastName))
                .findFirst()
                .orElse(null);
        
        if (customer == null) {
            customer = new Customer(firstName, lastName);
            customers.add(customer);
        }
        
        Flight flight = getFlightById(flightId);
        Booking booking = new Booking(flight, customer);
        tickets.get(flight).add(booking);
        return booking;
    }

    /**
     * Converts the list of bookings into a string representation. One line per booking.
     * The string representation is sorted by customer number ascending, then invoice number descending.
     * 
     * @return The string representation.
     */
    public String bookingsToString() {
        // Create a list of all booking and return the string representation of it
        Set<Booking> allBookings = tickets.values().stream().collect(TreeSet::new, Set::addAll, Set::addAll);
        return allBookings.stream().map(Booking::toStringWithFlightId).collect(Collectors.joining("\n"));
    }
    
    private Flight getFlightById(String flightId) throws InputException {
        return flights.stream()
                .filter(flight -> flight.getFlightId().equals(flightId))
                .findFirst()
                .orElseThrow(() -> new InputException("there is no flight with this number."));
    }
}
