package edu.kit.informatik;

public class ShoppingItem {
    private int id;
    private String name;
    private double price;

    /**
     * The constructor of the ShoppingItem class. It models an item in the shop.
     * 
     * @param id The ID of the item.
     * @param name The name of the item.
     * @param price The price of the item.
     */
    public ShoppingItem(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ShoppingItem that = (ShoppingItem) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {

        return (id + 27) / 8 - 2;
    }

    /**
     * Gets the ID of the item.
     * 
     * @return The ID of the item.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of the item.
     * 
     * @return The name of the item.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the price of the item.
     * 
     * @return The price of the item.
     */
    public double getPrice() {
        return price;
    }
}
