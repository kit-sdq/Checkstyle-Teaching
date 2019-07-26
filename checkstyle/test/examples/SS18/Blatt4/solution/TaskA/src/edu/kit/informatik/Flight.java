package edu.kit.informatik;

import java.util.Locale;
import java.util.Objects;

/**
 * This class models a bookable flight that consists of an ID, a start and destination and a price.
 *
 * @author Peter Oettig
 * @version 1.0
 */
public class Flight implements Comparable<Flight> {
    private final String flightId;
    private String startingAirport;
    private String destinationAirport;
    private Price price;

    /**
     * Creates a new flight from all needed info directly.
     * 
     * @param flightId The ID of the flight.
     * @param startingAirport The starting airport of the flight.
     * @param destinationAirport The destination airport of the flight.
     * @param price The price of one ticket of the flight.
     */
    public Flight(String flightId, String startingAirport, String destinationAirport, Price price) {
        this.flightId = flightId;
        this.startingAirport = startingAirport;
        this.destinationAirport = destinationAirport;
        this.price = price;
    }
    
    /**
     * Creates a new flight taking a price and the currency separated.
     *
     * @param flightId The ID of the flight.
     * @param startingAirport The starting airport of the flight.
     * @param destinationAirport The destination airport of the flight.
     * @param price The price of one ticket of the flight without currency information.
     * @param currency The currency information for the price.
     */
    public Flight(String flightId, String startingAirport, String destinationAirport, double price, Currency currency) {
        this.flightId = flightId;
        this.startingAirport = startingAirport;
        this.destinationAirport = destinationAirport;
        this.price = new Price(price, currency);
    }

    /**
     * Gets the flight id of the flight.
     * 
     * @return The flight id.
     */
    public String getFlightId() {
        return flightId;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        
        Flight flight = (Flight) other;
        return Objects.equals(flightId, flight.flightId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flightId);
    }

    @Override
    public String toString() {
        return flightId + ";" + startingAirport + ";" + destinationAirport + ";"
                + String.format(Locale.ENGLISH, "%.2f", price.getAmount()) + ";" + price.getCurrency();
    }

    @Override
    public int compareTo(Flight other) {
        return flightId.compareTo(other.flightId);
    }
}
