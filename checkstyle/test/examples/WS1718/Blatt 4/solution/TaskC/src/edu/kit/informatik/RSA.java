package edu.kit.informatik;

import java.math.BigInteger;

public class RSA {
    /**
     * Creates a new RSA object.
     */
    public RSA() {
    }

    /**
     * Returns a string of all primes (one per line) from 2 up to the given number inclusive.
     * 
     * @param end The number to stop the prime finding at.
     * @return A string of all primes in the range.
     */
    public String sieve(int end) {
        StringBuilder output = new StringBuilder();

        boolean[] notAPrime = new boolean[end + 1];
        for (int i = 2; i < end + 1; i++) {
            if (notAPrime[i]) {
                continue;
            }

            output.append(i);
            output.append("\n");
            for (int j = i * i; j < end + 1; j += i) {
                notAPrime[j] = true;
            }
        }

        return output.toString().substring(0, output.length() - 1);
    }

    /**
     * Gets a keypair (e,d,N) in string representation for two given primes.
     * 
     * @param p The first prime.
     * @param q The second prime.
     * @return A string with the resulting keypair.
     */
    public String getKeypair(BigInteger p, BigInteger q) {
        BigInteger n = getRSAModule(p, q);
        BigInteger phiN = getEulerianFunction(p, q);
        
        BigInteger[] extEucResult = new BigInteger[3];        
        BigInteger e = BigInteger.valueOf(2);
        for (; e.compareTo(phiN) < 0; e = e.add(BigInteger.ONE)) {
            extEucResult = calculateExtendedEuclidean(phiN, e);
            if (extEucResult[0].equals(BigInteger.ONE)) {
                break;
            }
        }
        
        BigInteger d = extEucResult[2].mod(phiN);
        return "(" + e + "," + d + "," + n + ")";
    }

    /**
     * Encrypts or decrypts a given message with the given public or private key. 
     * 
     * @param input The message to crypt.
     * @param exponent The exponent part of the public/private key.
     * @param n The module part of the public/private key.
     * @return A String with the crypted message.
     */
    public String crypt(BigInteger input, BigInteger exponent, BigInteger n) {
        return input.modPow(exponent, n).toString();
    }
    
    private BigInteger getRSAModule(BigInteger p, BigInteger q) {
        return p.multiply(q);
    }

    private BigInteger getEulerianFunction(BigInteger p, BigInteger q) {
        return p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
    }

    /**
     * Returns the greatest common divisor, s and t as array position 0, 1, 2.
     */
    private BigInteger[] calculateExtendedEuclidean(BigInteger a, BigInteger b) {
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
        
        BigInteger[] result = new BigInteger[3];
        result[0] = prevRemainder;
        result[1] = prevS;
        result[2] = prevT;
        return result; 
    }
}