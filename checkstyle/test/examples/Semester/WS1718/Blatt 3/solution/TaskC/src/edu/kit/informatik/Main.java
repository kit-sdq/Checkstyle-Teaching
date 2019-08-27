package edu.kit.informatik;

import java.math.BigInteger;

public class Main {
    public static void main(String[] args) {     
        RSA rsa = new RSA();
        
        if (args[0].equals("sieve")) {
            int end = Integer.parseInt(args[1]);
            System.out.println(rsa.sieve(end));
        } else if (args[0].equals("egcd")) {
            BigInteger a = new BigInteger(args[1]);
            BigInteger b = new BigInteger(args[2]);
            System.out.println(rsa.extendedEuclidean(a, b));
        }
    }
}