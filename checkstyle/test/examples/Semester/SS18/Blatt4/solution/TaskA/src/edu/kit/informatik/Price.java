package edu.kit.informatik;

/**
 * This models the price of something, in this scenario a flight.
 * A price consists of a currency and the amount of currency.
 * 
 * @author Peter
 * @version 1.0
 */
public class Price {
    private final double amount;
    private final Currency currency;

    /**
     * The constructor to create a new price object.
     * 
     * @param amount The amount of currency.
     * @param currency The currency as instance of {@link Currency}
     */
    public Price(double amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    /**
     * Gets the amount of currency that makes the price.
     * 
     * @return The amount of currency.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Gets the currency of the price as instance of {@link Currency}.
     * 
     * @return The currency.
     */
    public Currency getCurrency() {
        return currency;
    }
}
