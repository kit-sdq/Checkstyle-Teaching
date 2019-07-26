package edu.kit.informatik;

import java.math.BigInteger;

class Crypto {
    public static void main(String[] args) {
        if (args[0].equals("egcd")) {
            System.out.println(Ggtmethod.ggt(new BigInteger(args[1]), new BigInteger(args[2])));
        }
    }
}