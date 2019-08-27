package edu.kit.informatik;

import java.math.BigInteger;

public class Ggtmethod {
    public static String ggt(BigInteger a, BigInteger b) {
        BigInteger zero = BigInteger.valueOf(0);
        BigInteger one = BigInteger.valueOf(1);
        BigInteger negone = BigInteger.valueOf(-1);
        BigInteger divider = BigInteger.valueOf(1);
        if ((a.compareTo(b) < 0) || (a.compareTo(b) == 0)) {
            for (BigInteger i = a; (i.compareTo(zero) > 0); i = i.add(negone)) {
                if ((b.mod(i).equals(zero)) && (a.mod(i).equals(zero))) {
                    divider = i;
                    break;
                }
            }
        } else if (a.compareTo(b) > 0) {
            for (BigInteger i = b; i.compareTo(zero) > 0; i = i.add(negone)) {
                if ((b.mod(i).equals(zero)) && (a.mod(i).equals(zero))) {
                    divider = i;
                    break;
                }
            }
        }
        boolean value = true;
        String returnstring = "";
        for (BigInteger u = BigInteger.valueOf(0); value; u = u.add(one)) {
            for (BigInteger v = BigInteger.valueOf(0); ((((v.abs().multiply(b)).compareTo(u.multiply(a))) < 0)
                    || ((v.negate().multiply(b)).equals(u.multiply(a)))) && value; v = v.add(negone)) {
                if (((u.multiply(a).add(v.multiply(b)).equals(divider)))) {
                    returnstring = "" + divider.toString() + " " + u.toString() + " " + v.toString();
                    value = false;
                }
            }
        }
        return returnstring;
    }
}