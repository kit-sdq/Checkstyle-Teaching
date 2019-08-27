package edu.kit.informatik;

import java.math.BigInteger;

public class Main {
    /**
     * This is the program entry method main.
     *
     * @param args Array of strings of the given command line arguments.
     */
    public static void main(String[] args) {     
        RSA rsa = new RSA();

        boolean running = true;
        while (running) {
            String[] input = Terminal.readLine().split(" ", 2);
            String[] params = null;
            if (input.length > 1) {
                params = input[1].split(";");
            }

            switch (input[0]) {
                case "sieve": {
                    int end = Integer.parseInt(params[0]);
                    Terminal.printLine(rsa.sieve(end));
                    break;
                }
                case "keypair": {
                    BigInteger p = new BigInteger(params[0]);
                    BigInteger q = new BigInteger(params[1]);
                    Terminal.printLine(rsa.getKeypair(p, q));
                    break;
                }
                case "crypt": {
                    BigInteger message = new BigInteger(params[0]);
                    BigInteger exponent = new BigInteger(params[1]);
                    BigInteger n = new BigInteger(params[2]);
                    Terminal.printLine(rsa.crypt(message, exponent, n));
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