package edu.kit.informatik;

public class ChristmasShop {
    private MinimalList<Customer> customers;
    private MinimalList<ShoppingItem> items;
    private MinimalList<Order> orders;

    /**
     * Constructor for the interface and management class ChristmasShop between the user interface and the data storage.
     * Creates a completely empty shop.
     */
    public ChristmasShop() {
        customers = new MinimalList<>();
        items = new MinimalList<>();
        orders = new MinimalList<>();
    }

    /**
     * Adds a new customer to the shop.
     * 
     * @param firstName The customers first name.
     * @param lastName The customers last name.
     * @param id The customers ID.
     */
    public void addCustomer(String firstName, String lastName, int id) {
        customers.add(new Customer(id, firstName, lastName));
    }

    /**
     * Adds a new item to the shop.
     * 
     * @param name The name of the item.
     * @param id The ID of the item.
     * @param price The price of the item.
     */
    public void addItem(String name, int id, double price) {
        items.add(new ShoppingItem(id, name, price));
    }

    /**
     * Places a new order in the shop.
     * 
     * @param orderID The ID of the order.
     * @param customerID The customer ID of the ordering customer.
     * @param itemID The item ID of the item that's ordered.
     * @param amount The amount of the item to order.
     */
    public void addOrder(int orderID, int customerID, int itemID, int amount) {
        Customer customer = customers.get(new Customer(customerID, null, null));
        ShoppingItem item = items.get(new ShoppingItem(itemID, null, 0.0));
        orders.add(new Order(orderID, customer, item, amount));
    }

    /**
     * Coverts an order to a string according to the specified format on the assignment sheet.
     * 
     * @param id The ID of the order to convert.
     * @return The string representation of the order.
     */
    public String orderToString(int id) {
        return orders.get(new Order(id, null, null, 0)).toString();
    }
}