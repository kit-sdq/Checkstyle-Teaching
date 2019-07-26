package edu.kit.informatik._intern.util;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.function.LongConsumer;

import edu.kit.informatik._intern.util.function.LongCollector;

/**
 * This class consists exclusively of static mathematical methods.
 * 
 * @author  Tobias Bachert
 * @version 1.09, 2016/05/22
 */
public final class MathUtil {
    
    // Suppressed default constructor
    private MathUtil() { }
    
    /**
     * Returns a {@code long}, for each one-bit {@code b} in the returned {@code long} applies that {@code bits} has
     * {@code count} adjacent one-bits for this bit, more specific that {@code bits} has, for each one-bit {@code b} in
     * the returned {@code long}, one-bits at the indices {@code b}<sub>index</sub> {@code + dist * n} for {@code 0 <=
     * n <= count}.
     * 
     * <p>Collapses bits {@code count} times. On each collapse only bits with an adjacent one-bit with higher index are
     * kept. For a bit {@code b} the adjacent bits are defined as the bits with indices {@code i = b}<sub>index</sub> ±
     * {@code dist}.
     * 
     * <p>If {@code dist} or {@code count} is out of range, then the result of this method is undefined.
     * 
     * @param  bits the {@code long} to collapse
     * @param  dist the shifting distance
     * @param  count how many times {@code bits} will be collapsed
     * @return a {@code long}, for each one-bit {@code b} in the returned {@code long} applies that {@code bits} has
     *         one-bits at the indices {@code b}<sub>index</sub> {@code + dist * n} for {@code 0 <= n <= count}
     */
    public static long collapse(final long bits, final int dist, final int count) {
        long result = bits;
        int leftOver = count;
        
        /* Clusters shifting for leftOver > 2. */
        while (result != 0 && leftOver > 2) {
            int shiftLevel = 0;
            for (int i = leftOver + 1 >> 1; i > 0; i >>= 1)
                result &= result >>> (dist << shiftLevel++);
            leftOver -= (1 << shiftLevel) - 1;
        }
        
        for (; result != 0 && leftOver > 0; leftOver--)
            result &= result >>> dist;
        
        return result;
    }
    
    /**
     * Applies the extended euclidean algorithm to the given parameter.
     * 
     * @param  a the first parameter
     * @param  b the first parameter
     * @return the result of the extended euclidean algorithm applied to the given parameter
     */
    public static EuclideanResult extendEuclideanAlgorithm(final long a, final long b) {
        long sNew = 0, sOld = 1;
        long tNew = 1, tOld = 0;
        long rNew = b, rOld = a;
        
        while (rNew != 0) {
            final long quotient = rOld / rNew;
            sNew = sOld - quotient * (sOld = sNew);
            tNew = tOld - quotient * (tOld = tNew);
            rNew = rOld - quotient * (rOld = rNew);
        }
        return new EuclideanResult(a, b, sOld, tOld, rOld, tNew, sNew);
    }
    
    /**
     * Returns whether {@code input} is prime.
     * 
     * <p>If multiple calls to this method are expected, it is advised to use {@link Primes#isPrime(long)} method
     * instead.
     * 
     * @param  input the {@code long} to check
     * @return {@code true} if {@code input} is prime, {@code false} otherwise
     * @see    #primeFactors(long)
     */
    public static boolean isPrime(final long input) {
        if ((input & 1) == 0)
            return input == 2;
        if (input < 2)
            return false;
        if (input % 3 == 0)
            return input == 3;
        for (long v = 5, add = 2, border = (long) Math.sqrt(input); v <= border; v += add, add ^= 6)
            if (input % v == 0)
                return false;
        return true;
    }
    
    /**
     * Returns the greater of two {@code byte} values.
     * 
     * @param  a the first argument
     * @param  b the second argument
     * @return the greater of {@code a} and {@code b}
     */
    public static byte max(final byte a, final byte b) {
        return a > b ? a : b;
    }
    
    /**
     * Returns the greater of two {@code char} values.
     * 
     * @param  a the first argument
     * @param  b the second argument
     * @return the greater of {@code a} and {@code b}
     */
    public static char max(final char a, final char b) {
        return a > b ? a : b;
    }
    
    /**
     * Returns the greater of two {@code int} values. That is, the result the argument closer to the value of {@link
     * Integer#MAX_VALUE}. If the arguments have the same value, the result is that same value.
     * 
     * @param  a the first argument
     * @param  b the second argument
     * @return the greater of {@code a} and {@code b}
     * @see    Math#max(int, int)
     */
    public static int max(final int a, final int b) {
        return a > b ? a : b;
    }
    
    /**
     * Returns the greater of two {@code long} values.
     * 
     * @param  a the first argument
     * @param  b the second argument
     * @return the greater of {@code a} and {@code b}
     */
    public static long max(final long a, final long b) {
        return a > b ? a : b;
    }
    
    /**
     * Returns the smaller of two {@code byte} values.
     * 
     * @param  a the first argument
     * @param  b the second argument
     * @return the smaller of {@code a} and {@code b}
     */
    public static byte min(final byte a, final byte b) {
        return a < b ? a : b;
    }
    
    /**
     * Returns the smaller of two {@code char} values.
     * 
     * @param  a the first argument
     * @param  b the second argument
     * @return the smaller of {@code a} and {@code b}
     */
    public static char min(final char a, final char b) {
        return a < b ? a : b;
    }
    
    /**
     * Returns the smallest of the specified {@code int} values. That is, the result the argument closest to the value
     * of {@link Integer#MIN_VALUE}.
     * 
     * @param  ints the values
     * @return the smallest of the specified {@code int} values
     */
    public static int min(final int... ints) {
        if (ints.length < 2) throw new IllegalArgumentException("not enough arguments provided, at least 2 required");
        
        return ArrayUtil.min(ints);
    }
    
    /**
     * Returns the smaller of two {@code int} values. That is, the result the argument closer to the value of {@link
     * Integer#MIN_VALUE}. If the arguments have the same value, the result is that same value.
     * 
     * @param  a the first argument
     * @param  b the second argument
     * @return the smaller of {@code a} and {@code b}
     * @see    Math#min(int, int)
     */
    public static int min(final int a, final int b) {
        return Math.min(a, b);
    }
    
    /**
     * Returns the smallest of three {@code int} values. That is, the result the argument closest to the value of {@link
     * Integer#MIN_VALUE}.
     * 
     * @param  a the first argument
     * @param  b the second argument
     * @param  c the third argument
     * @return the smallest of {@code a}, {@code b} and {@code c}
     */
    public static int min(final int a, final int b, final int c) {
        return min(min(a, b), c);
    }
    
    /**
     * Returns the smallest of four {@code int} values. That is, the result the argument closest to the value of {@link
     * Integer#MIN_VALUE}.
     * 
     * @param  a the first argument
     * @param  b the second argument
     * @param  c the third argument
     * @param  d the fourth argument
     * @return the smallest of {@code a}, {@code b}, {@code c} and {@code d}
     */
    public static int min(final int a, final int b, final int c, final int d) {
        return min(min(a, b, c), d);
    }
    
    /**
     * Returns the smallest of five {@code int} values. That is, the result the argument closest to the value of {@link
     * Integer#MIN_VALUE}.
     * 
     * @param  a the first argument
     * @param  b the second argument
     * @param  c the third argument
     * @param  d the fourth argument
     * @param  e the fifth argument
     * @return the smallest of {@code a}, {@code b}, {@code c}, {@code d} and {@code e}
     */
    public static int min(final int a, final int b, final int c, final int d, final int e) {
        return min(min(a, b, c, d), e);
    }
    
    /**
     * Returns the smaller of two {@code long} values.
     * 
     * @param  a the first argument
     * @param  b the second argument
     * @return the smaller of {@code a} and {@code b}
     */
    public static long min(final long a, final long b) {
        return a < b ? a : b;
    }
    
    /**
     * Returns the prime factors of {@code input}.
     * 
     * <p>If {@code input} is less than {@code 2}, an empty long-array will be returned. If the returned long-array
     * has the length 1, then {@code input} is prime.
     * 
     * <p>If multiple calls to this method are expected, it is advised to use {@link Primes#primeFactors(long)}
     * method instead.
     * 
     * @param  input the input {@code long}
     * @return the prime factors of {@code input}
     */
    public static long[] primeFactors(final long input) {
        if (input < 2)
            return new long[0];
        
        final long[] factors = new long[63 - Long.numberOfLeadingZeros(input)];
        long n = input;
        int  i = 0;
        for (; (n & 1) == 0; n >>= 1)
            factors[i++] = 2;
        for (; n % 3 == 0; n /= 3)
            factors[i++] = 3;
        for (long v = 5, add = 2, border = (long) Math.sqrt(n); v <= border; v += add, add ^= 6)
            while (n % v == 0)
                border = (long) Math.sqrt(n /= factors[i++] = v);
        if (n != 1)
            factors[i++] = n;
        return Arrays.copyOf(factors, i);
    }
    
    /**
     * Applies the sieve of eratosthenes to the given {@code long} value.
     * 
     * <p>If {@code n} is not positive, an empty {@code Primes} will be returned.
     * 
     * @param  n the {@code long} value
     * @return a {@code Primes} containing all prime numbers from {@code 2} to {@code n}
     */
    public static Primes sieveOfEratosthenes(final long n) {
        return Primes.sieveOfEratosthenes(n);
    }
    
    /**
     * Returns an approximation for the square root of the given {@code BigInteger}.
     * 
     * @param  n the {@code BigInteger}
     * @return an approximation for the square root of the given {@code BigInteger}
     */
    public static BigInteger sqrt(final BigInteger n) {
        // Using herons method to approximate the square root.
        BigInteger oldVal = BigInteger.ZERO.setBit(n.bitLength() >> 1);
        for (BigInteger newVal; !(newVal = n.divide(oldVal).add(oldVal).shiftRight(1)).equals(oldVal);)
            oldVal = newVal;
        return oldVal;
    }
    
    /**
     * Casts a {@code long} to an {@code int}. If the {@code long} can not be represented by an {@code int}, an {@code
     * ArithmeticException} is thrown.
     * 
     * @param  value the {@code long} to convert
     * @return {@code (int) value}
     */
    public static int toIntExact(final long value) {
        final int result = (int) value;
        if (value != result) throw new ArithmeticException("numeric overflow: (long) " + value + " to int");
        return result;
    }
    
    /**
     * Result of the extended euclidean algorithm.
     * 
     * @author  Tobias Bachert
     * @version 1.01, 2016/05/10
     */
    public static final class EuclideanResult {
        
        /** The first value. */
        public final long a;
        /** The second value. */
        public final long b;
        /** The first bézout coefficient. */
        public final long bezoutCoefficientA;
        /** The second bézout coefficient. */
        public final long bezoutCoefficientB;
        /** The greatest common divisor. */
        public final long greatestCommonDivisor;
        /** The first quotient. */
        public final long quotientA;
        /** The second quotient. */
        public final long quotientB;
        
        private EuclideanResult(final long a, final long b, final long bA, final long bB, final long gcd,
                final long qA, final long qB) {
            this.a = a;
            this.b = b;
            bezoutCoefficientA = bA;
            bezoutCoefficientB = bB;
            greatestCommonDivisor = gcd;
            quotientA = qA;
            quotientB = qB;
        }
        
        @Override
        public String toString() {
            return "EuclideanResult[a=" + a + ", " + "b=" + b
                    + ", Coefficients=" + bezoutCoefficientA + ":" + bezoutCoefficientB
                    + ", GCD=" + greatestCommonDivisor
                    + ", Quotients=" + quotientA + ":" + quotientB + "]";
        }
    }
    
    /**
     * Class offering methods regarding prime-numbers.
     * 
     * @author  Tobias Bachert
     * @version 1.00, 2016/05/21
     * 
     * @see     MathUtil#sieveOfEratosthenes(long)
     */
    public abstract static class Primes {
        
        private static final Primes PRIMECOUNT_0 = new Primes(-2) {
            @Override protected long countNonPrimes() { return 0; }
            @Override protected boolean isPositionPrime(final long position) { return false; }
        };
        private static final Primes PRIMECOUNT_1 = new Primes(-1) {
            @Override protected long countNonPrimes() { return 0; }
            @Override protected boolean isPositionPrime(final long position) { return false; }
        };
        private final long length;
        private long count;
        
        /**
         * Private constructor to allow sub-classing this class only within this class.
         * 
         * @param length the {@link #length}
         */
        private Primes(final long length) {
            this.length = length;
        }
        
        /**
         * Applies the sieve of eratosthenes to the given {@code long} value.
         * 
         * <p>If {@code n} is not positive, an empty {@code Primes} will be returned.
         * 
         * @param  n the {@code long} value
         * @return a {@code Primes} containing all prime numbers from {@code 2} to {@code n}
         */
        static Primes sieveOfEratosthenes(final long n) {
            return n <= 2 ? n == 2 ? PRIMECOUNT_1 : PRIMECOUNT_0
                    : n <= (-5 >>> 1) * 192L + 4 ? sieveOfEratosthenes0(n) : sieveOfEratosthenes1(n);
        }
        
        /* Uses a single long array thus limited in size. */
        private static Primes sieveOfEratosthenes0(final long n) {
            final long length = toPosition(n - 3);
            final long[] array = new long[(int) (length + 63 >> 6)]; // 0 is prime, 1 is not prime; 63=(1<<6)-1
            long k = 0;
            for (final long border = toPosition((long) Math.sqrt(n) - 3); k < border; k++) {
                if ((array[(int) (k >> 6)] & 1L << k) == 0) {
                    /*
                     * p = current position, starting at the position of val=v*v
                     * d = distance between two multiples of v with (v*x)%3!=0 && (v*(x+1))%3!=0, d=2k+3
                     * s = whether to skip the next value due to it being a multiple of 3, jumping o instead of d then
                     * o = distance in case of jumping to the after next value, o=2d+(s?-1:1)=4k+(s?5:7)
                     */
                    boolean s = (k & 1) == 1;
                    final long d = (k << 1) + 3;
                    final long o = (d << 1) + (s ? -1 : 1);
                    final long v = (k << 1) + (k & ~1) + 5;
                    for (long p = toPosition(v * v - 5); p < length; p += s ? o : d, s = !s)
                        array[(int) (p >> 6)] |= 1L << p;
                }
            }
            return new Primes(length) {
                
                @Override
                protected long countNonPrimes() {
                    long count = 0;
                    for (long l : array)
                        count += Long.bitCount(l);
                    return count;
                }
                
                @Override
                protected boolean isPositionPrime(final long position) {
                    return (array[(int) (position >> 6)] & 1L << position) == 0;
                }
            };
        }
        
        /* Uses multiple long-arrays. */
        private static Primes sieveOfEratosthenes1(final long n) {
            final long length = toPosition(n - 3);
            final long[][] array = new long[(int) (length + (-1L >>> 28) >> 36)][(int) (length + 63 >> 6) & ~(1 << 31)];
            long k = 0;
            for (final long border = toPosition((long) Math.sqrt(n) - 3); k < border; k++) {
                if ((array[(int) (k >> 36)][(int) (k >> 6 & (1 << 30) - 1)] & 1L << k) == 0) {
                    boolean s = (k & 1) == 1;
                    final long d = (k << 1) + 3;
                    final long o = (d << 1) + (s ? -1 : 1);
                    final long v = (k << 1) + (k & ~1) + 5;
                    for (long p = toPosition(v * v - 5); p < length; p += s ? o : d, s = !s)
                        array[(int) (p >> 36)][(int) (p >> 6 & (1 << 30) - 1)] |= 1L << p;
                }
            }
            return new Primes(length) {
                
                @Override
                protected long countNonPrimes() {
                    long count = 0;
                    for (long[] arr : array)
                        for (long l : arr)
                            count += Long.bitCount(l);
                    return count;
                }
                
                @Override
                protected boolean isPositionPrime(final long position) {
                    return (array[(int) (position >> 36)][(int) (position >> 6 & (1 << 30) - 1)] & 1L << position) == 0;
                }
            };
        }
        
        private static long toPosition(final long value) {
            /*
             * This does not take any offset into account to allow using this method for the calculation of positions of
             * values (passing val-5) as well as for the calculation of border-positions (passing val-3).
             */
            return (value >> 1) - value / 6;
        }
        
        private static long toValue(final long position) {
            return 5 + (position << 1) + (position & ~1);
        }
        
        /**
         * Collects the elements of the collection using the given collector.
         * 
         * @param  <A> container type parameter
         * @param  <R> result type parameter
         * @param  collector the {@code IntCollector}
         * @return the result of the collector
         */
        public final <A, R> R collect(final LongCollector<A, ? extends R> collector) {
            final A container = collector.container();
            forEach((t) -> collector.accumulate(container, t));
            return collector.finish(container);
        }
        
        @Override
        public final boolean equals(final Object obj) {
            if (this == obj)
                return true;
            if (obj == null || !(obj instanceof Primes))
                return false;
            
            final Primes other = (Primes) obj;
            return length == other.length;
        }
        
        /**
         * Processes the given action for each element in the collection in order.
         * 
         * @param action the {@code LongConsumer} to process for each element
         */
        public final void forEach(final LongConsumer action) {
            if (length >= -1)
                action.accept(2);
            if (length >= 0)
                action.accept(3);
            for (long k = 0; k < length; k++)
                if (isPositionPrime(k))
                    action.accept(toValue(k));
        }
        
        @Override
        public final int hashCode() {
            final int prime = 31;
            return prime * (int) (length ^ length >>> 32);
        }
        
        /**
         * Returns whether {@code input} is prime.
         * 
         * @param  value the {@code long} to check
         * @return {@code true} if {@code input} is prime, {@code false} otherwise
         * @see    #primeFactors(long)
         * @see    MathUtil#isPrime(long)
         */
        public final boolean isPrime(final long value) {
            if ((value & 1) == 0) // First checking the easiest and most likely possibility
                return value == 2;
            if (value < 2)  // Somewhat unlikely but a bit cheaper than multiplying+shifting -> checking before %3
                return false;
            if (value % 3 == 0)
                return value == 3;
            for (final long pos = toPosition(value - 5); pos < length;) // Checking if lookup possible
                return isPositionPrime(pos);
            final long border = (long) Math.sqrt(value);
            long k = 0;
            for (final long brd = Math.min(toPosition(border - 3), length); k < brd; k++)
                if (isPositionPrime(k) && value % toValue(k) == 0)
                    return false;
            for (long v = toValue(k), add = toValue(k + 1) - v; v <= border; v += add, add ^= 6)
                if (value % v == 0)
                    return false;
            return true;
        }
        
        /**
         * Returns the prime factors of {@code input}.
         * 
         * <p>If {@code input} is less than {@code 2}, an empty long-array will be returned. If the returned long-array
         * has the length 1, then {@code input} is prime.
         * 
         * @param  value the {@code long} to factorize
         * @return the prime factors of {@code input}
         * @see    MathUtil#primeFactors(long)
         */
        public final long[] primeFactors(final long value) {
            if (value < 2)
                return new long[0];
            final long[] factors = new long[63 - Long.numberOfLeadingZeros(value)];
            long n = value;
            int  i = 0;
            for (; (n & 1) == 0; n >>= 1)
                factors[i++] = 2;
            for (; n % 3 == 0; n /= 3)
                factors[i++] = 3;
            if (n < toValue(length) && isPositionPrime(toPosition(n - 5))) { // If we're lucky, lookup possible
                factors[i++] = n;
            } else {
                long border = (long) Math.sqrt(n);
                long k = 0;
                for (long brd = Math.min(toPosition(border - 3), length); k < brd; k++)
                    if (isPositionPrime(k))
                        for (final long v = toValue(k); n % v == 0;)
                            brd = Math.min(toPosition((border = (long) Math.sqrt(n /= factors[i++] = v)) - 3), length);
                for (long v = toValue(k), add = toValue(k + 1) - v; v <= border; v += add, add ^= 6)
                    while (n % v == 0)
                        border = (long) Math.sqrt(n /= factors[i++] = v);
                if (n != 1)
                    factors[i++] = n;
            }
            return Arrays.copyOf(factors, i);
        }
        
        /**
         * Returns the amount of prime-numbers stored into this {@code PrimeArray}.
         * 
         * @return the amount of prime-numbers
         */
        public final long size() {
            return count > 0 ? count : (count = length + 2 - countNonPrimes());
        }
        
        /**
         * Returns a long-array containing all primes of {@code this}.
         * 
         * @return a long-array containing all primes of {@code this}
         */
        public final long[] toArray() {
            if (size() > -5 >>> 1)
                throw new UnsupportedOperationException("size too large to convert to a single long array");
            
            final long[] result = new long[(int) size()];
            for (int k = 0, i = 0; k < length; k++)
                if (isPositionPrime(k))
                    result[i++] = toValue(k);
            return result;
        }
        
        @Override
        public final String toString() {
            return collect(LongCollector.joining(", ", "[", "]"));
        }
        
        /**
         * Returns the amount of non-prime-numbers stored into this {@code Primes}.
         * 
         * <p>The value returned has to be equals to {@code length+2-amount_of_prime-numbers} (as {@code 2} and {@code
         * 3} are only indirectly stored in the array).
         * 
         * @return the amount of non-prime-numbers
         */
        protected abstract long countNonPrimes();
        
        /**
         * Returns whether the value at a position is prime or not.
         * 
         * @param  position the position
         * @return {@code true} if the value at the position is prime, {@code false} otherwise
         */
        protected abstract boolean isPositionPrime(final long position);
    }
}
