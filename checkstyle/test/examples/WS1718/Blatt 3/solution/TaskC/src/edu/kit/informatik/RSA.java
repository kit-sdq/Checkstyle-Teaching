package edu.kit.informatik;

import java.math.BigInteger;

public class RSA {
    public RSA() {
    }

    public String sieve(int end) {
        StringBuilder output = new StringBuilder();

        boolean[] notAPrime = new boolean[end + 1];
        for (int i = 2; i < end + 1; i++) {
            if (notAPrime[i]) {
                continue;
            }

            output.append(i);
            output.append(" ");
            for (int j = i * i; j < end + 1; j += i) {
                notAPrime[j] = true;
            }
        }

        return output.toString().substring(0, output.length() - 1);
    }
    
    public String extendedEuclidean(BigInteger a, BigInteger b) {
        BigInteger s = BigInteger.ZERO;
        BigInteger t = BigInteger.ONE;
        BigInteger remainder = b;
        BigInteger prevS = BigInteger.ONE;
        BigInteger prevT = BigInteger.ZERO;
        BigInteger prevRemainder = a;

        while (!remainder.equals(BigInteger.ZERO)) {
            BigInteger quotient = prevRemainder.divide(remainder);

            BigInteger temp = remainder;
            remainder = prevRemainder.subtract((quotient.multiply(remainder)));
            prevRemainder = temp;

            temp = s;
            s = prevS.subtract((quotient.multiply(s)));
            prevS = temp;

            temp = t;
            t = prevT.subtract((quotient.multiply(t)));
            prevT = temp;
        }

        return prevRemainder + " " + prevS + " " + prevT;
    }
}