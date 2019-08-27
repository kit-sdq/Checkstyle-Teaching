package edu.kit.informatik;

public class Main {
    public static void main(String[] args) {
        if (args.length != 3) {
            Terminal.printError("wrong number of arguments, needs to be 3.");
            return;
        }
        
        try {
            Terminal.printLine(new EfficientRecurser(
                    Integer.parseInt(args[0]),
                    Integer.parseInt(args[1]), 
                    Integer.parseInt(args[2])));
        } catch (NumberFormatException e) {
            Terminal.printError("input for at least one of n, a or b ist not an integer.");
        } catch (IllegalArgumentException e) {
            Terminal.printError("n is not a positive number or zero.");
        }
    }
}
