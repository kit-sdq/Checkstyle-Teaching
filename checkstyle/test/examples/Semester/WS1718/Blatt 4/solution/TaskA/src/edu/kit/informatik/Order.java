package edu.kit.informatik;

public class Order {
    private int id;
    private Customer customer;
    private ShoppingItem item;
    private int amount;

    /**
     * The constructor of the order class. Models an order in the shop.
     * 
     * @param id The ID of the order.
     * @param customer The customer who places the order.
     * @param item The item which is ordered.
     * @param amount The amount of the item ordered.
     */
    public Order(int id, Customer customer, ShoppingItem item, int amount) {
        this.id = id;
        this.customer = customer;
        this.item = item;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return id == order.id;
    }

    @Override
    public int hashCode() {
        return id * 25 / 2 + 4;
    }
    
    @Override
    public String toString() {
        return "(" + id + "," + customer.getId() + "," + amount + "," + item.getName() + "," + item.getId() + ","
                + item.getPrice() + "," + amount * item.getPrice() + ")";
    }
}