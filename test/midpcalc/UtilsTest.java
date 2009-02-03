package midpcalc;

import java.util.Random;
import junit.framework.TestCase;

public class UtilsTest extends TestCase {

    int primes[] = { 2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,59,61,67,
            71,73,79,83,89,97,101,103,107,109,113,127,131,137,139,149,151,157,
            163,167,173,179,181,191,193,197,199};

    public void testGCD() {
        Random random = new Random();
        for (int i=0; i<1000000; i++)
        {
            long u = random.nextInt() & 0x7fffffff;
            long v = random.nextInt() & 0x7fffffff;
            long gcd = Utils.gcd(u, v);
            
            assertEquals(0, u % gcd);
            assertEquals(0, v % gcd);
        }
    }

    public void testGreatestFactor() {
        Random random = new Random();
        for (int i=0; i<1000; i++) {
            long n = random.nextLong() & 0x7fffffffffffffffL;
            long f = Utils.greatestFactor(n);

            assertTrue(f <= n);
            assertEquals(0, n % f);
        }
    }
    
    public void testGreatestFactor_small() {
        for (int i=0; i<primes.length; i++)
            assertEquals(primes[i], Utils.greatestFactor(primes[i]));
        Random random = new Random();
        for (int i=0; i<100000; i++) {
            long n = random.nextInt() & 0x7fffffff;
            long f = Utils.greatestFactor(n);

            assertTrue(f <= n);
            assertEquals(0, n % f);
        }
    }
    
    public void testGreatestFactor_carmichael() {
        long n = 4954039956700380001L;
        assertEquals(151, Utils.greatestFactor(n));
    }

    public void testNextPrime() {
        Random random = new Random();
        for (int i=0; i<1000; i++) {
            long n = random.nextLong() & 0x7fffffffffffffffL;
            long m = Utils.nextPrime(n);

            assertTrue(m >= n);
            assertEquals(m, Utils.greatestFactor(m));
        }
    }
    
    public void testIsPrime() {
        Random random = new Random();
        long time0 = System.currentTimeMillis();
        random.setSeed(0);
        for (int i=0; i<10000000; i++) {
            int n = (random.nextInt() & 0x7fffffff) | 1;
            Utils.isPrime(n);
        }
        long time1 = System.currentTimeMillis();
        System.out.println(time1-time0);
    }
    
    public void testNextPrime_small() {
        for (int i=-3; i<199; i++) {
            int j;
            for (j=0; j<primes.length; j++)
                if (primes[j] >= i)
                    break;

            assertEquals(primes[j], Utils.nextPrime(i));
        }
    }

    public void testIsPrime_small() {
        for (int i=-3; i<200; i++) {
            int j;
            for (j=0; j<primes.length; j++)
                if (i == primes[j])
                    break;
            boolean shouldBePrime = j<primes.length;

            assertEquals(shouldBePrime, Utils.isPrime(i));
        }
    }
    
    public void testIsPrime_difficult() {
        assertFalse(Utils.isPrime(2047));
        assertFalse(Utils.isPrime(1373653));
        assertFalse(Utils.isPrime(9080191));
        assertFalse(Utils.isPrime(25326001));
        assertFalse(Utils.isPrime(3215031751L));
        assertFalse(Utils.isPrime(4759123141L));
        assertFalse(Utils.isPrime(2152302898747L));
        assertFalse(Utils.isPrime(3474749660383L));
        assertFalse(Utils.isPrime(341550071728321L));
    }

}
