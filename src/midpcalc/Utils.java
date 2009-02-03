package midpcalc;

public class Utils {

    private static long mulMod(long a, long b, long n) {
        long result = 0;
        if (a < b) {
            long tmp = a;
            a = b;
            b = tmp;
        }
        for (; b != 0; b >>= 1) {
            if ((b & 1) != 0) {
                result += a;
                if (result > n || result < 0)
                    result -= n;
            }
            a <<= 1;
            if (a > n || a < 0)
                a -= n;
        }
        return result;
    }
    
    private static long powMod(long a, long exp, long n) {
        long result = 1;
        while (exp > 0) {
            if ((exp & 1) == 1)
                result = mulMod(result, a, n);
            exp >>= 1;
            a = mulMod(a, a, n);
        }
        return result;
    }
    
    private static int powMod(int a, int exp, int n) {
        int result = 1;
        while (exp > 0) {
            if ((exp & 1) == 1)
                result = (int)(((long)result * (long)a) % n);
            exp >>= 1;
            a = (int)(((long)a * (long)a) % n);
        }
        return result;
    }
    
    private static long f(long x, long c, long n)
    {
        // Calculate f(x) = x² + c (mod n)
        long result = mulMod(x, x, n);
        result += c;
        if (result > n || result < 0)
            result -= n;
        return result;
    }

    static long gcd(long u, long v)
    {
        int shift;
        if (u < 0 || v < 0)
            return 0;
        if (u == 0 || v == 0)
            return u + v;
        for (shift = 0; ((u | v) & 1) == 0; ++shift) {
            u >>= 1;
            v >>= 1;
        }
        while ((u & 1) == 0)
            u >>= 1;
        do {
            while ((v & 1) == 0)
                v >>= 1;
            if (u < v) {
                v -= u;
            } else {
                long diff = u - v;
                u = v;
                v = diff;
            }
            v >>= 1;
        } while (v != 0);
        return u << shift;
    }
    
    static long prevN = 0;
    static long prevC = 0;
    static long prevX = 0;
    static long prevY = 0;

    private static long pollardsRho(long n, boolean testPrime)
    {
        long startTime = System.currentTimeMillis();
        boolean seenBefore = (n == prevN) && !testPrime;
        boolean wasSeenBefore = seenBefore;
        prevN = 0;
        long c = seenBefore ? prevC : 1;
        int count = 0;
        for (;;) {
            long x = seenBefore ? prevX : 2;
            long y = seenBefore ? prevY : 2;
            seenBefore = false;
            long d;
            do {
                ++count;
                if ((count & 63) == 0 &&
                    System.currentTimeMillis()-startTime > 5000)
                {
                    // Save state and give up for now
                    prevN = n;
                    prevC = c;
                    prevX = x;
                    prevY = y;
                    return 1;
                }
                if (count == 12 && !wasSeenBefore) {
                    if (testPrime)
                        return multipleMillerRabin(n) ? n : 1;
                    else if (multipleMillerRabin(n))
                        return n;
                }
                // Now for the real work...
                x = f(x,c,n);
                y = f(f(y,c,n),c,n);
                d = gcd(Math.abs(x-y), n);
            } while (d == 1);
            if (d != n)
                return d;
            c++;
        }
    }
    
    private static int greatestFactor(int a) {
        if (a<=3)
            return a;
        while ((a&1) == 0) {
            a >>= 1;
            if (a<2*2) return a;
        }
        while (a%3 == 0) {
            a /= 3;
            if (a<3*3) return a;
        }
        int x=5;
        int s=5*5;
        while (a >= s && x <= 46337) {
            while (a%x == 0) {
                a /= x;
                if (a<s) return a;
            }
            x += 2;
            s = x*x;
            while (a%x == 0) {
                a /= x;
                if (a<s) return a;
            }
            x += 4;
            s = x*x;
        }
        return a;
    }

    static long greatestFactor(long a) {
        long f;
        if (a == -1)
            return a;
        if (a < 0)
            a = -a;
        if (a <= 0x7fffffff)
            return greatestFactor((int)a);
        f = pollardsRho(a, false);
        if (f == 1 || f == a)
            return f;
        // The factor found by pollardsRho may be composite
        a /= f;
        f = greatestFactor(f);
        a = greatestFactor(a);
        return a > f ? a : f;
    }
    
    private static boolean primeTestMillerRabin(long n, long a) {
        long d, x;
        int s, r;

        d = n-1;
        s = 0;
        while ((d & 1) == 0 && d != 0) {
            s++;
            d >>= 1;
        }
        while (a >= n)
            a >>= 1;

        x = powMod(a, d, n);
        if (x != 1 && x != n-1) {
            r = 1;
            while (r <= s-1 && x != n-1) {
                x = mulMod(x, x, n);
                if (x==1)
                    return false;
                r++;
            }
            if (x != n-1)
                return false;
        }
        return true;
    }
    
    private static boolean primeTestMillerRabin(int n, int a) {
        int d, x;
        int s, r;

        d = n-1;
        s = 0;
        while ((d & 1) == 0 && d != 0) {
            s++;
            d >>= 1;
        }
        while (a >= n)
            a >>= 1;

        x = powMod(a, d, n);
        if (x != 1 && x != n-1) {
            r = 1;
            while (r <= s-1 && x != n-1) {
                x = (int)(((long)x * (long)x) % n);
                if (x==1)
                    return false;
                r++;
            }
            if (x != n-1)
                return false;
        }
        return true;
    }
    
    private static boolean multipleMillerRabin(long n) {
        if (n <= 1)
            return false;
        if (n <= 3)
            return true;
        if ((n & 1) == 0)
            return false;
        // The first 4 prime bases prove primality up to 3215031751, etc...
        return
            primeTestMillerRabin(n, 2) &&
            primeTestMillerRabin(n, 3) &&
            primeTestMillerRabin(n, 5) &&
            primeTestMillerRabin(n, 7) &&
            (n < 3215031751L ||
             (primeTestMillerRabin(n, 11) &&
              (n < 2152302898747L ||
               (primeTestMillerRabin(n, 13) &&
                (n < 3474749660383L ||
                 (primeTestMillerRabin(n, 17) &&
                  (n < 341550071728321L ||
                   (primeTestMillerRabin(n, 23)))))))));
    }

    private static boolean tripleMillerRabin(int a) {
        // These 3 bases prove primality up to 4759123141
        return
            primeTestMillerRabin(a, 2) &&
            primeTestMillerRabin(a, 7) &&
            primeTestMillerRabin(a, 61);
    }

    private static boolean isPrime(int a) {
        if (a <= 1)
            return false;
        if (a <= 3)
            return true;
        if ((a & 1) == 0)
            return false;
        if (a < 3*3)
            return true;
        if (a%3 == 0)
            return false;
        int x=5;
        while (a >= x*x && x < 50)
        {
            if (a%x == 0)
                return false;
            x += 2;
            if (a < x*x)
                return true;
            if (a%x == 0)
                return false;
            x += 4;
        }
        if (a > 0x7fffff)
            return tripleMillerRabin(a);
        while (a >= x*x && x <= 46337)
        {
            if (a%x == 0)
                return false;
            x += 2;
            if (a < x*x)
                return true;
            if (a%x == 0)
                return false;
            x += 4;
        }
        return true;
    }
    
    public static boolean isPrime(long n) {
        if (n <= 0x7fffffff)
            return isPrime((int)n);
        return pollardsRho(n, true) == n;
    }
    
    public static long nextPrime(long n) {
        if (n <= 2)
            return 2;
        if ((n&1) == 0)
            n++;
        while (!isPrime(n) && n > 0)
            n += 2;
        return n;
    }
}
