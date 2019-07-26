package edu.kit.informatik;

public class Main {
    /**
     * This is the program entry method main.
     * 
     * @param args Array of strings of the given command line arguments.
     */
    public static void main(String[] args) {     
        ChristmasShop christmasShop = new ChristmasShop();
        
        boolean running = true;
        while (running) {
            String[] input = Terminal.readLine().split(" ", 2);
            String[] params = null;
            if (input.length > 1) {
                params = input[1].split(";");
            }
            
            switch (input[0]) {
                case "add-customer": {
                    int id = Integer.parseInt(params[2]);
                    christmasShop.addCustomer(params[0], params[1], id);
                    Terminal.printLine("OK");
                    break;
                }
                case "add-shoppingitem": {
                    int id = Integer.parseInt(params[1]);
                    double price = Double.parseDouble(params[2]);
                    christmasShop.addItem(params[0], id, price);
                    Terminal.printLine("OK");
                    break;
                }
                case "order": {
                    int orderId = Integer.parseInt(params[0]);
                    int customerId = Integer.parseInt(params[1]);
                    int itemId = Integer.parseInt(params[2]);
                    int amount = Integer.parseInt(params[3]);
                    christmasShop.addOrder(orderId, customerId, itemId, amount);
                    Terminal.printLine("OK");
                    break;
                }
                case "print": {
                    int orderId = Integer.parseInt(params[0]);
                    Terminal.printLine(christmasShop.orderToString(orderId));
                    break;
                }
                case "quit": {
                    running = false;
                    break;
                }
                default:
                    Terminal.printError("unknown command.");
            }
        }
    }
}