package edu.kit.informatik;

public class EulerianApproximation {
    public static void main(String[] args) {
       EulerianApproximation approximation = new EulerianApproximation();
        Command command = null;
        do {
            try {
                command = Command.executeMatching(Terminal.readLine(), approximation);
            } catch (InputException e) {
                Terminal.printError(e.getMessage());
            }
        } while (command == null || command.isRunning());
    }

    public double approximate(int n) {
        double sum = 0;
        for (int i = 0; i <= n; i++) {
            sum += 1.0 / factorial(i);
        }
        
        return sum;
    }
    
    private int factorial(int input) {
        if (input == 0) {
            return 1;
        }
        
        int result = 1;
        for (int i = 2; i <= input; i++) {
            result *= i;
        }
        
        return result;
    }
}
