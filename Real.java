// class Real: Integer implementation of 64-bit precision floating point
//
// Constructors/assignment:
//   Real()                                 <==  0
//   Real(Real)                             <==  Real
//   Real(int)                              <==  int
//   Real(long)                             <==  long
//   Real(String)                           <==  "-1.234E+56"
//   Real(String, int base)                 <==  "-1.234E+56" / "/fff3.2E-10"
//   Real(int s, int e, long m)             <==  (-1)^s * 2^(e-62) * m
//   Real(byte[] data, int offset)          <==  data[offset]..data[offset+11]
//   assign(Real)
//   assign(int)
//   assign(long)
//   assign(String)
//   assign(String, int base)
//   assign(int s, int e, long m)
//   assign(byte[] data, int offset)
//   assignFloatBits(int)                   <==  float 32-bits representation
//
// Output:
//   String toString()                      ==>  "-1.234E+56"
//   String toString(int base)              ==>  "-1.234E+56" / "03.feE+56"
//   String toString(NumberFormat)          ==>  e.g. "-1'234'567,8900"
//   int toInteger()                        ==>  int
//   long toLong()                          ==>  long
//   void toBytes(byte[] data, int offset)  ==>  data[offset]..data[offset+11]
//   int toFloatBits()                      ==>  float 32-bits representation
//
// Binary operators:
//   add(Real)
//   sub(Real)
//   mul(Real)
//   div(Real)
//   and(Real)
//   or(Real)
//   xor(Real)
//   bic(Real)
//
// Functions:
//   abs()
//   neg()
//   sqr()
//   sqrt()
//   cbrt()
//   recip()
//   rsqrt()
//   exp()
//   exp2()
//   exp10()
//   ln()
//   log2()
//   log10()
//   sin()
//   cos()
//   tan()
//   asin()
//   acos()
//   atan()
//   sinh()
//   cosh()
//   tanh()
//   asinh()
//   acosh()
//   atanh()
//   fact()
//   gamma()
//   toDHMS()
//   fromDHMS()
//   random()
//
// Binary functions:
//   hypot(Real)
//   atan2(Real x)
//   pow(Real)
//   pow(int)
//   nroot(Real)
//
// Integral values:
//   floor()
//   ceil()
//   round()
//   trunc()
//   frac()
//
// Comparisons:
//   boolean equalTo(Real)
//   boolean notEqualTo(Real)
//   boolean lessThan(Real)
//   boolean lessEqual(Real)
//   boolean greaterThan(Real)
//   boolean greaterEqual(Real)
//
// Utility functions:
//   copysign(Real)
//   nextafter(Real)
//   scalbn(int)
//
// Make special values:
//   makeZero(int sign)
//   makeInfinity(int sign)
//   makeNan()
//   makeExp10(int power)
//   int lowPow10()           // Convert to lower power of 10, return exponent
//
// Query state:
//   boolean isZero()
//   boolean isInfinity()
//   boolean isNan()
//   boolean isFinite()
//   boolean isFiniteNonZero()
//   boolean isIntegral()
//   boolean isOdd()
//   boolean isNegative()
//
// Constants:
//   ZERO     = 0
//   ONE      = 1
//   TWO      = 2
//   THREE    = 3
//   FIVE     = 5
//   TEN      = 10
//   HUNDRED  = 100
//   HALF     = 1/2
//   THIRD    = 1/3
//   PERCENT  = 1/100
//   SQRT2    = sqrt(2)
//   SQRT1_2  = sqrt(1/2)
//   PI2      = PI*2
//   PI       = PI
//   PI_2     = PI/2
//   PI_4     = PI/4
//   PI_8     = PI/8
//   E        = e
//   LN2      = ln(2)
//   LN10     = ln(10)
//   LOG2E    = log2(e)  = 1/ln(2)
//   LOG10E   = log10(e) = 1/ln(10)
//   LN10_LN2 = ln(10)/ln(2)
//   MAX      = max non-infinite positive number = 4.197E+323228496
//   MIN      = min non-zero positive number     = 2.383E-323228497
//   NAN      = not a number
//   INF      = infinity
//   INF_N    = -infinity
//   ZERO_N   = -0
//   ONE_N    = -1
//
package ral;

public final class Real
{
  long mantissa;
  int exponent;
  byte sign;

  public static final Real ZERO   = new Real(0,0x00000000,0x0000000000000000L);
  public static final Real ONE    = new Real(0,0x40000000,0x4000000000000000L);
  public static final Real TWO    = new Real(0,0x40000001,0x4000000000000000L);
  public static final Real THREE  = new Real(0,0x40000001,0x6000000000000000L);
  public static final Real FIVE   = new Real(0,0x40000002,0x5000000000000000L);
  public static final Real TEN    = new Real(0,0x40000003,0x5000000000000000L);
  public static final Real HUNDRED= new Real(0,0x40000006,0x6400000000000000L);
  public static final Real HALF   = new Real(0,0x3fffffff,0x4000000000000000L);
  public static final Real THIRD  = new Real(0,0x3ffffffe,0x5555555555555555L);
  public static final Real PERCENT= new Real(0,0x3ffffff9,0x51eb851eb851eb85L);
//public static final Real DIV2_3 = new Real(0,0x3fffffff,0x5555555555555555L);
  public static final Real SQRT2  = new Real(0,0x40000000,0x5a827999fcef3242L);
  public static final Real SQRT1_2= new Real(0,0x3fffffff,0x5a827999fcef3242L);
  public static final Real PI2    = new Real(0,0x40000002,0x6487ed5110b4611aL);
  public static final Real PI     = new Real(0,0x40000001,0x6487ed5110b4611aL);
  public static final Real PI_2   = new Real(0,0x40000000,0x6487ed5110b4611aL);
  public static final Real PI_4   = new Real(0,0x3fffffff,0x6487ed5110b4611aL);
  public static final Real PI_8   = new Real(0,0x3ffffffe,0x6487ed5110b4611aL);
  public static final Real E      = new Real(0,0x40000001,0x56fc2a2c515da54dL);
  public static final Real LN2    = new Real(0,0x3fffffff,0x58b90bfbe8e7bcd6L);
//public static final Real LN2_3  = new Real(1,0x3ffffffe,0x67cc8fb2fe612fcbL);
  public static final Real LN10   = new Real(0,0x40000001,0x49aec6eed554560bL);
  public static final Real LOG2E  = new Real(0,0x40000000,0x5c551d94ae0bf85eL);
  public static final Real LOG10E = new Real(0,0x3ffffffe,0x6f2dec549b9438cbL);
  public static final Real LN10_LN2=new Real(0,0x40000001,0x6a4d3c25e68dc57fL);
  public static final Real MAX    = new Real(0,0x7fffffff,0x7fffffffffffffffL);
  public static final Real MIN    = new Real(0,0x00000000,0x4000000000000000L);
  public static final Real NAN    = new Real(0,0x80000000,0x4000000000000000L);
  public static final Real INF    = new Real(0,0x80000000,0x0000000000000000L);
  public static final Real INF_N  = new Real(1,0x80000000,0x0000000000000000L);
  public static final Real ZERO_N = new Real(1,0x00000000,0x0000000000000000L);
  public static final Real ONE_N  = new Real(1,0x40000000,0x4000000000000000L);
  
  public Real() {
    assign(ZERO);
  }

  public Real(final Real a) {
    assign(a);
  }
  
  public Real(int a) {
    assign(a);
  }

  public Real(long a) {
    assign(a);
  }

  public Real(final String a) {
    assign(a,10);
  }
  
  public Real(final String a, int base) {
    assign(a,base);
  }
  
  public Real(int s, int e, long m) {
    assign(s,e,m);
  }
  
  public Real(byte [] data, int offset) {
    assign(data,offset);
  }

  public void assign(final Real a) {
    if (a == null) {
      makeZero(0);
      return;
    }
    sign = a.sign;
    exponent = a.exponent;
    mantissa = a.mantissa;
  }

  public void assign(int a) {
    sign = 0;
    if (a<0) {
      sign = 1;
      a = -a; // Also works for 0x80000000
    }
    exponent = 0x4000001E;
    mantissa = ((long)a)<<32;
    normalize();
  }

  public void assign(long a) {
    sign = 0;
    if (a<0) {
      sign = 1;
      a = -a; // Also works for 0x8000000000000000
    }
    exponent = 0x4000003E;
    mantissa = a;
    normalize();
  }

  public void assign(final String a) {
    assign(a,10);
  }

  public void assign(final String a, int base) {
    if (a==null || a.length()==0) {
      assign(ZERO);
      return;
    }
    atof(a,base);
  }

  public void assign(int s, int e, long m) {
    sign = (byte)s;
    exponent = e;
    mantissa = m;
  }

  public void assign(byte [] data, int offset) {
    sign = (byte)((data[offset+4]>>7)&1);
    exponent = (((data[offset   ]&0xff)<<24)+
                ((data[offset +1]&0xff)<<16)+
                ((data[offset +2]&0xff)<<8)+
                ((data[offset +3]&0xff)));
    mantissa = (((long)(data[offset+ 4]&0x7f)<<56)+
                ((long)(data[offset+ 5]&0xff)<<48)+
                ((long)(data[offset+ 6]&0xff)<<40)+
                ((long)(data[offset+ 7]&0xff)<<32)+
                ((long)(data[offset+ 8]&0xff)<<24)+
                ((long)(data[offset+ 9]&0xff)<<16)+
                ((long)(data[offset+10]&0xff)<< 8)+
                ((long)(data[offset+11]&0xff)));
  }

  public void toBytes(byte [] data, int offset) {
    data[offset   ] = (byte)(exponent>>24);
    data[offset+ 1] = (byte)(exponent>>16);
    data[offset+ 2] = (byte)(exponent>>8);
    data[offset+ 3] = (byte)(exponent);
    data[offset+ 4] = (byte)((sign<<7)+(mantissa>>56));
    data[offset+ 5] = (byte)(mantissa>>48);
    data[offset+ 6] = (byte)(mantissa>>40);
    data[offset+ 7] = (byte)(mantissa>>32);
    data[offset+ 8] = (byte)(mantissa>>24);
    data[offset+ 9] = (byte)(mantissa>>16);
    data[offset+10] = (byte)(mantissa>>8);
    data[offset+11] = (byte)(mantissa);
  }

  public void assignFloatBits(int c) {
    sign = (byte)(c>>>31);
    exponent = (c>>23)&0xff;
    mantissa = (long)(c&0x7fffff)<<39;
    if (exponent == 0 && mantissa == 0)
      return; // Valid zero
    if (exponent == 0 && mantissa != 0) {
      // Degenerate small float
      exponent = 0x40000000-126;
      normalize();
      return;
    }
    if (exponent <= 254) {
      // Normal IEEE 754 float
      exponent += 0x40000000-127;
      mantissa |= 1L<<62;
      return;
    }
    if (mantissa == 0)
      makeInfinity(sign);
    else
      makeNan();
  }

  public int toFloatBits() {
    if (isNan())
      return 0x7fffffff; // nan
    int e = exponent-0x40000000+127;
    long m = mantissa;
    // Round properly!
    m += 1L<<38;
    if (m<0) {
      m >>>= 1;
      e++;
    }
    if (isInfinity() || e > 254)
      return (sign<<31)|0x7f800000; // inf
    if (isZero() || e < -22)
      return (sign<<31); // zero
    if (e > 0)
      // Normal IEEE 754 float
      return (sign<<31)|(e<<23)|((int)(m>>>39)&0x7fffff);
    // Degenerate small float
    return (sign<<31)|((int)(m>>>(40-e))&0x7fffff);
  }

  public void makeZero(int s) {
    sign = (byte)s;
    mantissa = 0;
    exponent = 0;
  }

  public void makeInfinity(int s) {
    sign = (byte)s;
    mantissa = 0;
    exponent = 0x80000000;
  }

  public void makeNan() {
    sign = 0;
    mantissa = 0x4000000000000000L;
    exponent = 0x80000000;
  }

  public boolean isZero() {
    return (exponent == 0 && mantissa == 0);
  }

  public boolean isInfinity() {
    return (exponent < 0 && mantissa == 0);
  }

  public boolean isNan() {
    return (exponent < 0 && mantissa != 0);
  }

  public boolean isFinite() {
    // That is, non-infinite and non-nan
    return (exponent >= 0);
  }

  public boolean isFiniteNonZero() {
    // That is, non-infinite and non-nan and non-zero
    return (exponent >= 0 && mantissa != 0);
  }

  public void abs() {
    sign = 0;
  }

  public void neg() {
    if (!isNan())
      sign ^= 1;
  }

  public void copysign(final Real a) {
    if (isNan() || a.isNan()) {
      makeNan();
      return;
    }
    sign = a.sign;
  }

  public void normalize() {
    if (mantissa==0) {
      if (!isInfinity())
        makeZero(sign);
      return;
    }
    if (mantissa < 0) {
      mantissa = (mantissa+1)>>>1;
      exponent ++;
      if (exponent < 0) // Overflow
        makeInfinity(sign);
      return;
    }
    while ((mantissa >>> 47) == 0) {
      mantissa <<= 16;
      exponent -= 16;
    }
    while ((mantissa >>> 59) == 0) {
      mantissa <<= 4;
      exponent -= 4;
    }
    while ((mantissa >>> 62) == 0) {
      mantissa <<= 1;
      exponent --;
    }
    if (exponent < 0) // Underflow
      makeZero(sign);
  }

  private int compare(final Real a) {
    // Compare of normal floats, zeros, but not nan or equal-signed inf
    if (isZero() && a.isZero())
      return 0;
    if (sign != a.sign)
      return a.sign-sign;
    int s = sign==0 ? 1 : -1;
    if (isInfinity())
      return s;
    if (a.isInfinity())
      return -s;
    if (exponent != a.exponent)
      return exponent<a.exponent ? -s : s;
    if (mantissa != a.mantissa)
      return mantissa<a.mantissa ? -s : s;
    return 0;
  }

  private boolean invalidCompare(final Real a) {
    return (isNan() || a.isNan() ||
            (isInfinity() && a.isInfinity() && sign == a.sign));
  }

  public boolean equalTo(final Real a) {
    if (invalidCompare(a))
      return false;
    return compare(a) == 0;
  }

  public boolean notEqualTo(final Real a) {
    if (invalidCompare(a))
      return false;
    return compare(a) != 0;
  }

  public boolean lessThan(final Real a) {
    if (invalidCompare(a))
      return false;
    return compare(a) < 0;
  }

  public boolean lessEqual(final Real a) {
    if (invalidCompare(a))
      return false;
    return compare(a) <= 0;
  }

  public boolean greaterThan(final Real a) {
    if (invalidCompare(a))
      return false;
    return compare(a) > 0;
  }

  public boolean greaterEqual(final Real a) {
    if (invalidCompare(a))
      return false;
    return compare(a) >= 0;
  }

  public void scalbn(int n) {
    if (!isFiniteNonZero())
      return;
    exponent += n;
    if (exponent < 0) {
      if (n<0)
        makeZero(sign);     // Underflow
      else
        makeInfinity(sign); // Overflow
    }
  }

  public void nextafter(final Real a) {
    if (isNan() || a.isNan()) {
      makeNan();
      return;
    }
    if (isInfinity() && a.isInfinity() && sign == a.sign)
      return;
    int dir = -compare(a);
    if (dir == 0)
      return;
    if (isZero()) {
      assign(MIN);
      sign = (byte)(dir<0 ? 1 : 0);
      return;
    }
    if (isInfinity()) {
      assign(MAX);
      sign = (byte)(dir<0 ? 0 : 1);
      return;
    }
    //if ((sign==0 && dir>0) || (sign!=0 && dir<0)) {
    if (sign==0 ^ dir<0) {
      mantissa ++;
    } else {
      if (mantissa == 0x4000000000000000L) {
        mantissa <<= 1;
        exponent--;
      }
      mantissa --;
    }
    normalize();
  }

  public void floor() {
    if (!isFiniteNonZero())
      return;
    if (exponent < 0x40000000) {
      if (sign == 0)
        makeZero(sign);
      else {
        exponent = ONE.exponent;
        mantissa = ONE.mantissa;
      }
      return;
    }
    int shift = 0x4000003e-exponent;
    if (shift <= 0)
      return;

    if (sign != 0)
      mantissa += ((1L<<shift)-1);

    mantissa &= ~((1L<<shift)-1);

    if (sign != 0)
      normalize();
  }

  public void ceil() {
    neg();
    floor();
    neg();
  }

  public void round() {
    if (!isFiniteNonZero())
      return;
    if (exponent < 0x3fffffff) {
      makeZero(sign);
      return;
    }
    int shift = 0x4000003e-exponent;
    if (shift <= 0)
      return;

    mantissa += 1L<<(shift-1); // Bla-bla, this works almost
    mantissa &= ~((1L<<shift)-1);
    normalize();
  }

  public void trunc() {
    if (!isFiniteNonZero())
      return;
    if (exponent < 0x40000000) {
      makeZero(sign);
      return;
    }
    int shift = 0x4000003e-exponent;
    if (shift <= 0)
      return;

    mantissa &= ~((1L<<shift)-1);
    normalize();
  }

  public void frac() {
    if (!isFiniteNonZero() || exponent < 0x40000000)
      return;

    int shift = 0x4000003e-exponent;
    if (shift <= 0) {
      makeZero(sign);
      return;
    }

    mantissa &= ((1L<<shift)-1);
    normalize();
  }

  public int toInteger() {
    if (isZero() || isNan())
      return 0;
    if (isInfinity()) {
      if (sign==0)
        return 0x7fffffff;
      else
        return 0x80000001; // So that you can take -x.toInteger()
    }
    if (exponent < 0x40000000)
      return 0;
    int shift = 0x4000003e-exponent;
    if (shift < 32) {
      if (sign==0)
        return 0x7fffffff;
      else
        return 0x80000001; // So that you can take -x.toInteger()
    }
    return (sign==0) ? (int)(mantissa>>>shift) : -(int)(mantissa>>>shift);
  }

  public long toLong() {
    if (isZero() || isNan())
      return 0;
    if (isInfinity()) {
      if (sign==0)
        return 0x7fffffffffffffffL;
      else
        return 0x8000000000000001L; // So that you can take -x.toLong()
    }
    if (exponent < 0x40000000)
      return 0;
    int shift = 0x4000003e-exponent;
    if (shift < 0) {
      if (sign==0)
        return 0x7fffffffffffffffL;
      else
        return 0x8000000000000001L; // So that you can take -x.toLong()
    }
    return (sign==0) ? (mantissa>>>shift) : -(mantissa>>>shift);
  }

  public boolean isIntegral() {
    if (isNan())
      return false;
    if (isZero() || isInfinity())
      return true;
    if (exponent < 0x40000000)
      return false;
    int shift = 0x4000003e-exponent;
    if (shift <= 0)
      return true;
    return (mantissa&((1L<<shift)-1)) == 0;
  }

  public boolean isOdd() {
    if (!isFiniteNonZero() ||
        exponent < 0x40000000 || exponent > 0x4000003e)
      return false;
    int shift = 0x4000003e-exponent;
    return ((mantissa>>>shift)&1) != 0;
  }

  public boolean isNegative() {
    return sign!=0;
  }

  // Temporary values used by functions (to avoid "new" inside functions)
  private static Real subTmp = new Real();
  private static Real recipTmp = new Real();
  private static Real recipTmp2 = new Real();
  private static Real divTmp = new Real();
  private static Real rsTmp = new Real();
  private static Real rsTmp2 = new Real();
  private static Real sqrtTmp = new Real();
  private static Real expTmp = new Real();
  private static Real expTmp2 = new Real();
  private static Real expTmp3 = new Real();
  private static Real lnTmp = new Real();
  private static Real lnTmp2 = new Real();
  private static Real lnTmp3 = new Real();
  private static Real lnTmp4 = new Real();
  private static Real tmp1 = new Real();
  private static Real tmp2 = new Real();
  private static Real tmp3 = new Real();
  private static Real tmp4 = new Real();
  private static Real tmp5 = new Real();
  
  public void add(final Real a) {
    if (isNan() || a.isNan()) {
      makeNan();
      return;
    }
    if (isInfinity() || a.isInfinity()) {
      if (isInfinity() && a.isInfinity() && sign != a.sign)
        makeNan();
      else
        makeInfinity(isInfinity() ? sign : a.sign);
      return;
    }
    if (isZero() || a.isZero()) {
      if (isZero())
        assign(a);
    }
    else
    {
      byte s;
      int e;
      long m;
      if (exponent > a.exponent ||
          (exponent == a.exponent && mantissa>=a.mantissa))
      {
        s = a.sign;
        e = a.exponent;
        m = a.mantissa;
      } else {
        s = sign;
        e = exponent;
        m = mantissa;
        sign = a.sign;
        exponent = a.exponent;
        mantissa = a.mantissa;
      }
      int shift = exponent-e;
      if (shift>=64)
        return;

      if (shift!=0)
        m = (m+(1L<<(shift-1)))>>>shift;

      if (sign == s)
        mantissa += m;
      else
        mantissa -= m;

      normalize();
    }

    if (isZero())
      sign=0;
  }

  public void sub(final Real a) {
    subTmp.assign(a);
    subTmp.neg();
    add(subTmp);
  }

  public void mul(final Real a) {
    if (isNan() || a.isNan()) {
      makeNan();
      return;
    }
    sign ^= a.sign;
    if (isZero() || a.isZero()) {
      if (isInfinity() || a.isInfinity())
        makeNan();
      else
        makeZero(sign);
      return;
    }    
    if (isInfinity() || a.isInfinity()) {
      makeInfinity(sign);
      return;
    }
    exponent += a.exponent-0x40000000;
    if (exponent < 0) {
      if (a.exponent < 0x40000000)
        makeZero(sign);     // Underflow
      else
        makeInfinity(sign); // Overflow
      return;
    }
    long a0 = mantissa & 0x7fffffff;
    long a1 = mantissa >>> 31;
    long b0 = a.mantissa & 0x7fffffff;
    long b1 = a.mantissa >>> 31;

    mantissa = a1*b1 + ((a0*b1 + a1*b0 + ((a0*b0)>>>31) + 0x40000000)>>>31);

    normalize();
  }

  private void mul10() {
    if (!isFiniteNonZero())
      return;
    mantissa += (mantissa+2)>>>2;
    exponent += 3;
    if (mantissa < 0) {
      mantissa = (mantissa+1)>>>1;
      exponent++;
    }
    if (exponent < 0)
      makeInfinity(sign); // Overflow
  }

  private void recipInternal() {
    // Calculates recipocal of normalized Real, not zero, nan or infinity

    byte s = sign;
    sign = 0;

    // Special case, simple power of 2
    if (mantissa == 0x4000000000000000L) {
      exponent = 0x80000000-exponent;
      if (exponent<0) // Overflow
        makeInfinity(s);
      return;
    }

    // Normalize exponent
    int exp = 0x40000000-exponent;
    exponent = 0x40000000;

    // Save -A    
    recipTmp.assign(this);
    recipTmp.neg();

    // First establish approximate result (actually 31 bit accurate)
    mantissa = (0x4000000000000000L/(mantissa>>>31))<<31;
    normalize();

    // Now perform Newton-Raphson iteration
    // Xn+1 = Xn + Xn*(1-A*Xn)

    for (int i=0; i<2; i++) {
      // For speed, use only one iteration. Error will be max 10 ulp
      recipTmp2.assign(this);
      mul(recipTmp);
      add(ONE);
      mul(recipTmp2);
      add(recipTmp2);
    }

    // Fix exponent
    scalbn(exp);
    // Fix sign
    if (!isNan())
      sign = s;
  }

  public void recip() {
    if (isNan())
      return;
    if (isInfinity()) {
      makeZero(sign);
      return;
    }
    if (isZero()) {
      makeInfinity(sign);
      return;
    }
    recipInternal();
  }

  public void div(final Real a) {
    if (isNan() || a.isNan()) {
      makeNan();
      return;
    }
    if (isInfinity()) {
      if (a.isInfinity())
        makeNan();
      else
        sign ^= a.sign;
      return;
    }
    if (a.isInfinity()) {
      makeZero(sign^a.sign);
      return;
    }
    if (isZero()) {
      if (a.isZero())
        makeNan();
      else
        sign ^= a.sign;
      return;
    }
    if (a.isZero()) {
      makeInfinity(sign^a.sign);
      return;
    }
    divTmp.assign(a);
    divTmp.recipInternal();
    mul(divTmp);
  }

  public void and(final Real a) {
    if (isNan() || a.isNan()) {
      makeNan();
      return;
    }
    if (isZero() || a.isZero()) {
      makeZero(0);
      return;
    }
    if (isInfinity() || a.isInfinity()) {
      if (!isInfinity() && sign!=0)
        assign(a);
      else if (!a.isInfinity() && a.sign!=0)
        ; // assign(this)
      else if (isInfinity() && a.isInfinity() && sign!=0 && a.sign!=0)
        ; // makeInfinity(1)
      else
        makeZero(0);
      return;
    }

    byte s;
    int e;
    long m;
    if (exponent >= a.exponent) {
      s = a.sign;
      e = a.exponent;
      m = a.mantissa;
    } else {
      s = sign;
      e = exponent;
      m = mantissa;
      sign = a.sign;
      exponent = a.exponent;
      mantissa = a.mantissa;
    }
    int shift = exponent-e;
    if (shift>=64) {
      if (s == 0)
        makeZero(sign);
      return;
    }
    if (s != 0)
      m = -m;
    if (sign != 0)
      mantissa = -mantissa;

    mantissa &= m>>shift;
    sign = 0;
    if (mantissa < 0) {
      mantissa = -mantissa;
      sign = 1;
    }
    normalize();
  }

  public void or(final Real a) {
    if (isNan() || a.isNan()) {
      makeNan();
      return;
    }
    if (isZero() || a.isZero()) {
      if (isZero())
        assign(a);
      return;
    }
    if (isInfinity() || a.isInfinity()) {
      if (!isInfinity() && sign!=0)
        ; // assign(this);
      else if (!a.isInfinity() && a.sign!=0)
        assign(a);
      else
        makeInfinity(sign | a.sign);
      return;
    }

    byte s;
    int e;
    long m;
    if ((sign!=0   && exponent <= a.exponent) ||
        (a.sign==0 && exponent >= a.exponent))
    {
      s = a.sign;
      e = a.exponent;
      m = a.mantissa;
    } else {
      s = sign;
      e = exponent;
      m = mantissa;
      sign = a.sign;
      exponent = a.exponent;
      mantissa = a.mantissa;
    }
    int shift = exponent-e;
    if (shift>=64 || shift<=-64)
      return;

    if (s != 0)
      m = -m;
    if (sign != 0)
      mantissa = -mantissa;

    if (shift>=0)
      mantissa |= m>>shift;
    else
      mantissa |= m<<(-shift);

    sign = 0;
    if (mantissa < 0) {
      mantissa = -mantissa;
      sign = 1;
    }
    normalize();
  }

  public void xor(final Real a) {
    if (isNan() || a.isNan()) {
      makeNan();
      return;
    }
    if (isZero() || a.isZero()) {
      if (isZero())
        assign(a);
      return;
    }
    if (isInfinity() || a.isInfinity()) {
      makeInfinity(sign ^ a.sign);
      return;
    }

    byte s;
    int e;
    long m;
    if (exponent >= a.exponent) {
      s = a.sign;
      e = a.exponent;
      m = a.mantissa;
    } else {
      s = sign;
      e = exponent;
      m = mantissa;
      sign = a.sign;
      exponent = a.exponent;
      mantissa = a.mantissa;
    }
    int shift = exponent-e;
    if (shift>=64)
      return;

    if (s != 0)
      m = -m;
    if (sign != 0)
      mantissa = -mantissa;

    mantissa ^= m>>shift;

    sign = 0;
    if (mantissa < 0) {
      mantissa = -mantissa;
      sign = 1;
    }
    normalize();
  }

  public void bic(final Real a) {
    if (isNan() || a.isNan()) {
      makeNan();
      return;
    }
    if (isZero() || a.isZero())
      return;
    if (isInfinity() || a.isInfinity()) {
      if (!isInfinity()) {
        if (sign!=0)
          if (a.sign!=0)
            makeInfinity(0);
          else
            makeInfinity(1);
      } else if (a.sign!=0) {
        if (a.isInfinity())
          makeInfinity(0);
        else
          makeZero(0);
      }
      return;
    }

    int shift = exponent-a.exponent;
    if (shift>=64 || (shift<=-64 && sign==0))
      return;

    long m = a.mantissa;
    if (a.sign != 0)
      m = -m;
    if (sign != 0)
      mantissa = -mantissa;

    if (shift<0) {
      if (sign != 0) {
        if (shift<=-64)
          mantissa = ~m;
        else
          mantissa = (mantissa>>(-shift)) & ~m;
        exponent = a.exponent;
      } else
        mantissa &= ~(m<<(-shift));
    } else
      mantissa &= ~(m>>shift);

    sign = 0;
    if (mantissa < 0) {
      mantissa = -mantissa;
      sign = 1;
    }
    normalize();
  }

  public void sqr() {
    sign = 0;
    if (!isFiniteNonZero())
      return;
    int e = exponent;
    exponent += exponent-0x40000000;
    if (exponent < 0) {
      if (e < 0x40000000)
        makeZero(sign);     // Underflow
      else
        makeInfinity(sign); // Overflow
      return;
    }
    long a0 = mantissa&0x7fffffff;
    long a1 = mantissa>>>31;

    mantissa = a1*a1 + ((((a0*a1)<<1) + ((a0*a0)>>>31) + 0x40000000)>>>31);

    normalize();
  }
  
  private void rsqrtInternal() {
    // Calculates recipocal square root of normalized Real,
    // not zero, nan or infinity
    final long start = 0x4e60000000000000L;

    // Save -A    
    rsTmp.assign(this);
    rsTmp.neg();

    // First establish approximate result
    
    mantissa = start-(mantissa>>>2);
    boolean oddexp = ((exponent&1) != 0);
    exponent = 0x60000000-(exponent>>1);
    normalize();
    if (oddexp)
      mul(SQRT1_2);

    // Now perform Newton-Raphson iteration
    // Xn+1 = Xn + Xn*(1-A*Xn*Xn)/2
    
    for (int i=0; i<4; i++) {
      rsTmp2.assign(this);
      sqr();
      mul(rsTmp);
      add(ONE);
      scalbn(-1);
      mul(rsTmp2);
      add(rsTmp2);
    }
  }

  public void rsqrt() {
    if (isNan())
      return;
    if (sign!=0) {
      makeNan();
      return;
    }
    if (isInfinity()) {
      makeZero(sign);
      return;
    }
    if (isZero()) {
      makeInfinity(sign);
      return;
    }

    rsqrtInternal();
  }

  public void sqrt() {
    if (isNan())
      return;
    if (sign!=0) {
      makeNan();
      return;
    }
    if (isInfinity() || isZero())
      return;

    sqrtTmp.assign(this);
    sqrtTmp.rsqrtInternal();
    mul(sqrtTmp);
  }

  public void cbrt() {
    if (!isFiniteNonZero())
      return;

    byte s = sign;
    abs();
    log2();
    mul(THIRD);
    exp2();
    if (!isNan())
      sign = s;
  }

  public void nroot(Real root) {
    boolean negative = false;
    if (isNegative() && root.isIntegral() && root.isOdd()) {
      negative = true;
      abs();
    }
    tmp2.assign(root);
    tmp2.recip();
    pow(tmp2);
    if (negative)
      neg();
  }

  public void hypot(final Real a) {
    tmp1.assign(a);
    tmp1.sqr();
    sqr();
    add(tmp1);
    sqrt();
  }

  private void exp2Internal() {
    expTmp.assign(this);
    expTmp.floor();
    int exp = expTmp.toInteger();
    if (exp >= 0x40000000) {
      makeInfinity(sign);
      return;
    }
    if (exp < -0x40000000) {
      makeZero(sign);
      return;
    }
    sub(expTmp);
    mul(LN2);

    // Calculate e^x for 0 < x < ln 2
    // Using the classic Taylor series
    //                       2        3        4        5
    //   x                  x        x        x        x
    //  e  =   1  +  x  +  ----  +  ----  +  ----  +  ----  +  ...
    //                      2!       3!       4!       5!
    //
    // , factorized as follows:
    //
    //  x   (((((...)*x + 5...)*x + 4*5...)*x + 3*4*5...)*x + 2*3*4*5...)*x
    // e  = --------------------------------------------------------------- + 1
    //                              2*3*4*5...

    expTmp.assign(this);
    expTmp2.assign(ONE);
    for (int i=18; i>=2; i--) {
      expTmp3.assign(i);
      expTmp2.mul(expTmp3);
      add(expTmp2);
      mul(expTmp);
    }
    div(expTmp2);
    add(ONE);

    exponent += exp;
  }

  public void exp() {
    if (isNan())
      return;
    if (isInfinity()) {
      if (sign!=0)
        makeZero(sign);
      return;
    }
    if (isZero()) {
      assign(ONE);
      return;
    }
    mul(LOG2E);
    exp2();
  }

  public void exp2() {
    if (isNan())
      return;
    if (isInfinity()) {
      if (sign!=0)
        makeZero(sign);
      return;
    }
    if (isZero()) {
      assign(ONE);
      return;
    }
    exp2Internal();
  }

  public void exp10() {
    if (isNan())
      return;
    if (isInfinity()) {
      if (sign!=0)
        makeZero(sign);
      return;
    }
    if (isZero()) {
      assign(ONE);
      return;
    }
    mul(LN10_LN2);
    exp2();
  }

  public void makeExp10(int power) {
    // Calculate power of 10 by successive squaring for increased accuracy
    // It is not so accurate for large arguments, but better than exp10()
    assign(TEN);
    pow(power);
  }
  
  private void lnInternal() {
    // Calculates natural logarithm ln(a) for numbers between 1 and 2, using
    // the series
    //                          3        5        7
    //  1      x+1             x        x        x
    // --- ln ----- =  x  +   ----  +  ----  +  ---- + ...
    //  2      x-1             3        5        7
    //
    // ,where x = (a-1)/(a+1)
    // The expression is factorized as follows:
    //
    // ((((...)*7x² + 9...)*5x² + 7*9...)*3x² + 5*7*9...)*x² + 3*5*7*9...)*x
    // ---------------------------------------------------------------------
    //                              1*3*5*7*9...

    lnTmp.assign(this);
    lnTmp.add(ONE);
    sub(ONE);
    div(lnTmp);

    lnTmp.assign(this);
    lnTmp2.assign(this);
    lnTmp2.sqr();
    assign(ONE);
    lnTmp3.assign(ONE);
    for (int i=37; i>=3; i-=2) {
      mul(lnTmp2);
      lnTmp4.assign(i-2);
      mul(lnTmp4);
      lnTmp4.assign(i);
      lnTmp3.mul(lnTmp4);
      add(lnTmp3);
    }
    mul(lnTmp);
    div(lnTmp3);
    scalbn(1);
  }
  
  public void ln() {
    if (isNan())
      return;
    if (sign!=0) {
      makeNan();
      return;
    }
    if (isZero()) {
      makeInfinity(1);
      return;
    }
    if (isInfinity())
      return;

    int exp = exponent-0x40000000;
    exponent = 0x40000000;

    //mul(DIV2_3);  // Comment in for quicker convergence, less precision
    lnInternal();
    //sub(LN2_3);

    lnTmp.assign(exp);
    lnTmp.mul(LN2);
    add(lnTmp);
  }

  public void log2() {
    if (isNan())
      return;
    if (sign!=0) {
      makeNan();
      return;
    }
    if (isZero()) {
      makeInfinity(1);
      return;
    }
    if (isInfinity())
      return;

    int exp = exponent-0x40000000;
    exponent = 0x40000000;

    lnInternal();
    mul(LOG2E);

    lnTmp.assign(exp);
    add(lnTmp);
  }

  public void log10() {
    if (isNan())
      return;
    if (sign!=0) {
      makeNan();
      return;
    }
    if (isZero()) {
      makeInfinity(1);
      return;
    }
    if (isInfinity())
      return;

    int exp = exponent-0x40000000;
    exponent = 0x40000000;

    lnInternal();

    lnTmp.assign(exp);
    lnTmp.mul(LN2);
    add(lnTmp);
    mul(LOG10E);
  }

  public int lowPow10() {
    if (!isFiniteNonZero())
      return 0;
    tmp2.assign(this);
    // Approximate log10 using exponent only
    int e = exponent - 0x40000000;
    if (e<0) // it's important to achieve floor(exponent*ln2/ln10)
      e = -(int)(((-e)*0x4d104d43L+((1L<<32)-1)) >> 32);
    else
      e = (int)(e*0x4d104d43L >> 32);
    // Now, e < log10(this) < e+1
    makeExp10(e);
    tmp3.assign(this);
    tmp3.mul10();
    if (tmp3.lessEqual(tmp2)) {
      // First estimate of log10 was too low
      e++;
      assign(tmp3);
    }
    return e;
  }

  private void pow(int power) {
    // Calculate power of integer by successive squaring
    boolean recp=false;
    if (power < 0) {
      power = -power; // Also works for 0x80000000
      recp = true;
    }
    expTmp.assign(this);
    assign(ONE);
    for (; power!=0; power>>>=1) {
      if ((power & 1) != 0)
        mul(expTmp);
      expTmp.sqr();
    }
    if (recp)
      recip();
  }

  public void pow(final Real exp) {
    // Special cases:
    // if y is 0.0 or -0.0 then result is 1.0
    // if y is 1.0 then result is x
    // if y is NaN then result is NaN
    // if x is NaN and y is not zero then result is NaN
    // if |x| > 1.0 and y is +Infinity then result is +Infinity
    // if |x| < 1.0 and y is -Infinity then result is +Infinity
    // if |x| > 1.0 and y is -Infinity then result is +0
    // if |x| < 1.0 and y is +Infinity then result is +0
    // if |x| = 1.0 and y is +/-Infinity then result is NaN
    // if x = +0 and y > 0 then result is +0
    // if x = +0 and y < 0 then result is +Inf
    // if x = -0 and y > 0, and odd integer then result is -0
    // if x = -0 and y < 0, and odd integer then result is -Inf
    // if x = -0 and y > 0, not odd integer then result is +0
    // if x = -0 and y < 0, not odd integer then result is +Inf
    // if x = +Inf and y > 0 then result is +Inf
    // if x = +Inf and y < 0 then result is +0
    // if x = -Inf and y not integer then result is NaN
    // if x = -Inf and y > 0, and odd integer then result is -Inf
    // if x = -Inf and y > 0, not odd integer then result is +Inf
    // if x = -Inf and y < 0, and odd integer then result is -0
    // if x = -Inf and y < 0, not odd integer then result is +0
    // if x < 0 and y not integer then result is NaN
    // if x < 0 and y odd integer then result is -(|x|^y)
    // if x < 0 and y not odd integer then result is |x|^y
    // else result is pow2(log2(x)*y)
    
    if (exp.isZero()) {
      assign(ONE);
      return;
    }
    if (isNan() || exp.isNan()) {
      makeNan();
      return;
    }
    if (exp.compare(ONE)==0)
      return;
    if (exp.isInfinity()) {
      tmp1.assign(this);
      tmp1.abs();
      int test = tmp1.compare(ONE);
      if (test>0) {
        if (exp.sign==0)
          makeInfinity(0);
        else
          makeZero(0);
      } else if (test<0) {
        if (exp.sign!=0)
          makeInfinity(0);
        else
          makeZero(0);
      } else {
        makeNan();
      }
      return;
    }
    if (isZero()) {
      if (sign==0) {
        if (exp.sign==0)
          makeZero(0);
        else
          makeInfinity(0);
      } else {
        if (exp.isIntegral() && exp.isOdd()) {
          if (exp.sign==0)
            makeZero(1);
          else
            makeInfinity(1);
        } else {
          if (exp.sign==0)
            makeZero(0);
          else
            makeInfinity(0);
        }
      }
      return;
    }
    if (isInfinity()) {
      if (sign==0) {
        if (exp.sign==0)
          makeInfinity(0);
        else
          makeZero(0);
      } else {
        if (exp.isIntegral()) {
          if (exp.isOdd()) {
            if (exp.sign==0)
              makeInfinity(1);
            else
              makeZero(1);
          } else {
            if (exp.sign==0)
              makeInfinity(0);
            else
              makeZero(0);
          }
        } else {
          makeNan();
        }
      }
      return;
    }

    if (exp.isIntegral() && exp.exponent <= 0x4000001e) {
      pow(exp.toInteger());
      return;
    }
    
    byte s=0;
    if (sign!=0) {
      if (exp.isIntegral()) {
        if (exp.isOdd())
          s = 1;
      } else {
        makeNan();
        return;
      }
      sign = 0;
    }

    tmp1.assign(exp);
    log2();
    mul(tmp1);
    exp2();
    if (!isNan())
      sign = s;
  }

  private void sinInternal() {
    // Calculate sine for 0 < x < pi/4
    // Using the classic Taylor series
    //                     3        5        7
    //                    x        x        x
    // sin(x)  =   x  -  ----  +  ----  -  ----  +  ...
    //                    3!       5!       7!
    //
    // , factorized as follows:
    //
    //         ((((...)*-x² + 6*7...)*-x² + 4*5*6*7...)*-x² + 2*3*4*5*6*7...)*x
    // sin(x) = ---------------------------------------------------------------
    //                                  2*3*4*5*6*7...

    tmp1.assign(this);
    tmp2.assign(this);
    tmp2.sqr();
    tmp2.neg();
    assign(ONE);
    tmp3.assign(ONE);
    for (int i=19; i>=3; i-=2) {
      mul(tmp2);
      tmp4.assign(i*(i-1));
      tmp3.mul(tmp4);
      add(tmp3);
    }
    mul(tmp1);
    div(tmp3);
  }

  private void cosInternal() {
    // Calculate cosine for 0 < x < pi/4
    // Using the classic Taylor series
    //                     2        4        6
    //                    x        x        x
    // cos(x)  =   1  -  ----  +  ----  -  ----  +  ...
    //                    2!       4!       6!
    //
    // , factorized as follows:
    //
    //          (((...)*-x² + 5*6...)*-x² + 3*4*5*6...)*-x²
    // cos(x) = ------------------------------------------- + 1
    //                          1*2*3*4*5*6...

    tmp1.assign(this);
    tmp2.assign(this);
    tmp2.sqr();
    tmp2.neg();
    assign(tmp2);
    tmp3.assign(ONE);
    for (int i=18; i>=4; i-=2) {
      tmp4.assign(i*(i-1));
      tmp3.mul(tmp4);
      add(tmp3);
      mul(tmp2);
    }
    tmp3.scalbn(1);
    div(tmp3);
    add(ONE);
  }

  public void sin() {
    if (!isFinite()) {
      makeNan();
      return;
    }

    // First reduce the argument to the range of 0 < x < pi*2
    div(PI2);
    tmp1.assign(this);
    tmp1.floor();
    sub(tmp1);
    mul(PI2);

    // Since sin(pi*2 - x) = -sin(x) we can reduce the range 0 < x < pi
    boolean negative = false;
    if (this.greaterThan(PI)) {
      sub(PI2);
      neg();
      negative = true;
    }

    // Since sin(x) = sin(pi - x) we can reduce the range to 0 < x < pi/2
    if (this.greaterThan(PI_2)) {
      sub(PI);
      neg();
    }

    // Since sin(x) = cos(pi/2 - x) we can reduce the range to 0 < x < pi/4
    if (this.greaterThan(PI_4)) {
      sub(PI_2);
      neg();
      cosInternal();
    } else {
      sinInternal();
    }

    if (negative)
      neg();
  }

  public void cos() {
    add(PI_2);
    sin();
  }

  public void tan() {
    tmp5.assign(this);
    tmp5.cos();
    sin();
    div(tmp5);
  }

  public void asin() {
    tmp1.assign(this);
    sqr();
    neg();
    add(ONE);
    rsqrt();
    mul(tmp1);
    atan();
  }

  public void acos() {
    boolean negative = (sign!=0);
    abs();

    tmp1.assign(this);
    sqr();
    neg();
    add(ONE);
    sqrt();
    div(tmp1);
    atan();
 
    if (negative) {
      neg();
      add(PI);
    }
  }

  private void atanInternal() {
    // Calculate atan for 0 < x < sqrt(2)-1
    // Using the classic Taylor series.
    //                      3        5        7
    //                     x        x        x
    // atan(x)  =   x  -  ----  +  ----  -  ----  +  ...
    //                     3        5        7
    //
    // , factorized as follows:
    //
    //           (((...)*-5x² + 7...)*-3x² + 5*7...)*-x² + 3*5*7...)*x
    // atan(x) = -----------------------------------------------------
    //                                 1*3*5*7...

    tmp1.assign(this);
    tmp2.assign(this);
    tmp2.sqr();
    tmp2.neg();
    assign(ONE);
    tmp3.assign(ONE);
    for (int i=45; i>=3; i-=2) {
      mul(tmp2);
      tmp4.assign(i-2);
      mul(tmp4);
      tmp4.assign(i);
      tmp3.mul(tmp4);
      add(tmp3);
    }
    mul(tmp1);
    div(tmp3);
  }

  public void atan() {
    if (isZero() || isNan())
      return;
    if (isInfinity()) {
      byte s = sign;
      assign(PI_2);
      sign = s;
      return;
    }
    
    byte s = sign;
    sign = 0;

    boolean recp = false;
    if (this.greaterThan(ONE)) {
      recp = true;
      recip();
    }

    // atan(x) = atan[ (x - a) / (1 + x*a) ] + PI/8
    // ,where a = sqrt(2)-1
    tmp1.assign(SQRT2);
    tmp1.sub(ONE);
    boolean sub = false;
    if (this.greaterThan(tmp1))
    {
      sub = true;
      tmp2.assign(this);
      sub(tmp1);
      tmp2.mul(tmp1);
      tmp2.add(ONE);
      div(tmp2);
    }

    atanInternal();

    if (sub)
      add(PI_8);

    if (recp) {
      neg();
      add(PI_2);
    }

    sign = s;
  }

  public void atan2(final Real x) {
    if (isNan() || x.isNan() || (isInfinity() && x.isInfinity())) {
      makeNan();
      return;
    }
    if (isZero() && x.isZero())
      return;

    byte s = sign;
    sign = 0;
    byte s2 = x.sign;
    x.sign = 0;

    div(x); 
    atan();

    if (s2 != 0) {
      neg();
      add(PI);
    }

    sign = s;
  }

  public void sinh() {
    tmp1.assign(this);
    tmp1.neg();
    tmp1.exp();
    exp();
    sub(tmp1);
    scalbn(-1);
  }

  public void cosh() {
    tmp1.assign(this);
    tmp1.neg();
    tmp1.exp();
    exp();
    add(tmp1);
    scalbn(-1);
  }

  public void tanh() {
    tmp1.assign(this);
    tmp1.neg();
    tmp1.exp();
    exp();
    tmp2.assign(this);
    tmp2.add(tmp1);
    sub(tmp1);
    div(tmp2);
  }

  public void asinh() {
    tmp1.assign(this);
    tmp1.sqr();
    tmp1.add(ONE);
    tmp1.sqrt();
    add(tmp1);
    ln();
  }

  public void acosh() {
    tmp1.assign(this);
    tmp1.sqr();
    tmp1.sub(ONE);
    tmp1.sqrt();
    add(tmp1);
    ln();
  }

  public void atanh() {
    tmp1.assign(this);
    tmp1.neg();
    tmp1.add(ONE);
    add(ONE);
    div(tmp1);
    ln();
    scalbn(-1);
  }

  public void fact() {
    if (!isFinite())
      return;

    tmp1.assign(25);
    if (!isIntegral() || this.lessThan(ZERO) || this.greaterThan(tmp1))
    {
      // x<0, x>25 or not integer: fact(x) = gamma(x+1)
      add(ONE);
      gamma();
      return;
    }
    tmp1.assign(this);
    assign(ONE);
    while (tmp1.greaterThan(ONE)) {
      mul(tmp1);
      tmp1.sub(ONE);
    }
  }

  public void gamma() {
    if (!isFinite())
      return;

    // x<0: gamma(-x) = -pi/(x*gamma(x)*sin(pi*x))
    boolean negative = (sign != 0);
    abs();
    tmp1.assign(this);

    // x<n: gamma(x) = gamma(x+m)/x*(x+1)*(x+2)*...*(x+m-1)
    int n=20;
    tmp3.assign(n);
    tmp2.assign(ONE);
    boolean divide = false;
    while (this.lessThan(tmp3)) {
      divide = true;
      tmp2.mul(this);
      add(ONE);
    }

    // x>n: gamma(x) = exp((x-1/2)*ln(x)-x+ln(2*pi)/2+1/12x-1/360x³+1/1260x^5
    //                     -1/1680x^7+1/1188x^9)
    tmp3.assign(this); // x
    tmp4.assign(this);
    tmp4.sqr();        // x²

    // (x-1/2)*ln(x)-x
    ln(); tmp5.assign(tmp3); tmp5.sub(HALF); mul(tmp5); sub(tmp3);
    // + ln(2*pi)/2
    tmp5.assign( PI2); tmp5.ln();   tmp5.scalbn(-1); add(tmp5);
    // + 1/12x
    tmp5.assign(  12); tmp5.mul(tmp3); tmp5.recip(); add(tmp5); tmp3.mul(tmp4);
    // - 1/360x³
    tmp5.assign( 360); tmp5.mul(tmp3); tmp5.recip(); sub(tmp5); tmp3.mul(tmp4);
    // + 1/1260x^5
    tmp5.assign(1260); tmp5.mul(tmp3); tmp5.recip(); add(tmp5); tmp3.mul(tmp4);
    // - 1/1680x^7
    tmp5.assign(1680); tmp5.mul(tmp3); tmp5.recip(); sub(tmp5); tmp3.mul(tmp4);
    // + 1/1188x^9
    tmp5.assign(1188); tmp5.mul(tmp3); tmp5.recip(); add(tmp5);

    exp();

    if (divide)
      div(tmp2);

    if (negative) {
      tmp5.assign(tmp1); // sin() uses tmp1
      // -pi/(x*gamma(x)*sin(pi*x))
      mul(tmp5); tmp5.mul(PI); tmp5.sin(); mul(tmp5); recip(); mul(PI); neg();
    }
  }

//*****************************************************************************

  private static int floorDiv(int a, int b) {
    if (a>=0)
      return a/b;
    return -((-a+b-1)/b);
  }
  private static int floorMod(int a, int b) {
    if (a>=0)
      return a%b;
    return a+((-a+b-1)/b)*b;
  }

  private static boolean leap_gregorian(int year) {
    return ((year % 4) == 0) &&
      (!(((year % 100) == 0) && ((year % 400) != 0)));
  }

  //  GREGORIAN_TO_JD -- Determine Julian day number from Gregorian
  //  calendar date -- Except that we use 1/1-0 as day 0
  public static int gregorian_to_jd(int year, int month, int day) {
    return ((366 - 1) +
            (365 * (year - 1)) +
            (floorDiv(year - 1, 4)) +
            (-floorDiv(year - 1, 100)) +
            (floorDiv(year - 1, 400)) +
            ((((367 * month) - 362) / 12) +
             ((month <= 2) ? 0 : (leap_gregorian(year) ? -1 : -2)) + day));
  }

  //  JD_TO_GREGORIAN -- Calculate Gregorian calendar date from Julian
  //  day -- Except that we use 1/1-0 as day 0
  public static int jd_to_gregorian(int jd) {
    int wjd, depoch, quadricent, dqc, cent, dcent, quad, dquad,
        yindex, dyindex, year, yearday, leapadj, month, day;

    wjd = jd;
    depoch = wjd - 366;
    quadricent = floorDiv(depoch, 146097);
    dqc = floorMod(depoch, 146097);
    cent = floorDiv(dqc, 36524);
    dcent = floorMod(dqc, 36524);
    quad = floorDiv(dcent, 1461);
    dquad = floorMod(dcent, 1461);
    yindex = floorDiv(dquad, 365);
    year = (quadricent * 400) + (cent * 100) + (quad * 4) + yindex;
    if (!((cent == 4) || (yindex == 4)))
      year++;
    yearday = wjd - gregorian_to_jd(year, 1, 1);
    leapadj = ((wjd < gregorian_to_jd(year, 3, 1)) ? 0
               : (leap_gregorian(year) ? 1 : 2));
    month = floorDiv(((yearday + leapadj) * 12) + 373, 367);
    day = (wjd - gregorian_to_jd(year, month, 1)) + 1;

    return (year*100+month)*100+day;
  }

  public void toDHMS() {
    // Convert from "hours" to "days, hours, minutes and seconds"
    // Actually, it converts to YYYYMMDDhh.mmss if input>=8784
    if (!isFiniteNonZero())
      return;
    boolean negative = sign != 0;
    abs();

    int D,m;
    long h;
    h = toLong();
    frac();
    tmp1.assign(60);
    mul(tmp1);
    m = toInteger();
    frac();
    mul(tmp1);

    // MAGIC ROUNDING: Check if we are 2^-16 second short of a whole minute
    // i.e. "seconds" > 59.999985
    tmp2.assign(ONE);
    tmp2.scalbn(-16);
    add(tmp2);
    if (this.greaterEqual(tmp1)) {
      // Yes. So set zero seconds instead and carry over to minutes and hours
      assign(ZERO);
      m++;
      if (m >= 60) {
        m -= 60;
        h++;
      }
      // Phew! That was close. From now on it is integer arithmetic...
    } else {
      // Nope. So try to undo the damage...
      sub(tmp2);
    }

    D = (int)(h/24);
    h %= 24;
    if (D >= 366)
      D = jd_to_gregorian(D);

    tmp1.assign(m*100);
    add(tmp1);
    tmp1.assign(10000);
    div(tmp1);
    tmp1.assign(D*100L+h);
    add(tmp1);

    if (negative)
      neg();
  }

  public void fromDHMS() {
    // Convert from "days, hours, minutes and seconds" to "hours"
    // Actually, it converts from YYYYMMDDhh.mmss, if input>=1000000
    if (!isFiniteNonZero())
      return;
    boolean negative = sign != 0;
    abs();

    int Y,M,D,m,s;
    long h;
    h = toLong();
    frac();
    tmp1.assign(100);
    mul(tmp1);
    m = toInteger();
    frac();
    mul(tmp1);

    // MAGIC ROUNDING: Check if we are 2^-10 second short of 100 seconds
    // i.e. "seconds" > 99.999
    tmp2.assign(ONE);
    tmp2.scalbn(-10);
    add(tmp2);
    if (this.greaterEqual(tmp1)) {
      // Yes. So set zero seconds instead and carry over to minutes and hours
      assign(ZERO);
      m++;
      if (m >= 100) {
        m -= 100;
        h++;
      }
      // Phew! That was close. From now on it is integer arithmetic...
    } else {
      // Nope. So try to undo the damage...
      sub(tmp2);
    }

    D = (int)(h/100);
    h %= 100;
    if (D>=10000) {
      M = D/100;
      D %= 100;
      if (D==0) D=1;
      Y = M/100;
      M %= 100;
      if (M==0) M=1;
      D = gregorian_to_jd(Y,M,D);
    }

    tmp1.assign(m*60);
    add(tmp1);
    tmp1.assign(3600);
    div(tmp1);
    tmp1.assign(D*24L+h);
    add(tmp1);

    if (negative)
      neg();
  }

  public void time() {
    long now = System.currentTimeMillis();
    int h,m,s;
    now /= 1000;
    s = (int)(now % 60);
    now /= 60;
    m = (int)(now % 60);
    now /= 60;
    h = (int)(now % 24);
    assign((h*100+m)*100+s);
    tmp1.assign(10000);
    div(tmp1);
  }

  public void date() {
    long now = System.currentTimeMillis();
    now /= 86400000; // days
    now *= 24;       // hours
    assign(now);
    tmp1.assign(719528*24); // 1970-01-01 era
    add(tmp1);
    toDHMS();
  }
  
//*****************************************************************************

  public static long randSeedA = 0x6487ed5110b4611aL;// Something to start with
  public static long randSeedB = 0x56fc2a2c515da54dL;// (mantissas of pi and e)
  private static int randBitPos = 0;

  // 64 Bit Linear Congruential Generators with Prime Addend.
  // Multipliers 27bb2ee687b0b0fd, 369dea0f31a53f85 suggested by
  // L'cuyer in his table of multipliers:
  // http://www.iro.umontreal.ca/~lecuyer/myftp/papers/latrules.ps
  // Prime numbers are randomly generated with Unix command "primes".
  //
  // The generators used here are not cryptographically secure, but
  // two weak generators are combined into one strong generator by
  // skipping bits from one generator whenever the other generator
  // produces a 0-bit.
  private static void advanceBit() {
    randBitPos++;
    if (randBitPos>=64) {
      // Rehash seeds
      randSeedA = (randSeedA * 0x369dea0f31a53f85L + 3184845299L);
      randSeedB = (randSeedB * 0x27bb2ee687b0b0fdL + 2295936121L);
      randBitPos = 0;
    }
  }

  // Get next bits from the pseudo-random sequence
  private static long nextBits(int bits) {
    long answer = 0;
    while (bits-- > 0) {
      while (((int)(randSeedA>>>randBitPos)&1) == 0)
        advanceBit();
      answer = (answer<<1) + ((int)(randSeedB>>>randBitPos)&1);
      advanceBit();
    }
    return answer;
  }

  // Accumulate randomness from seed, e.g. System.currentTimeMillis()
  public static void accumulateRandomness(long seed) {
    randSeedA ^= seed & 0x5555555555555555L;
    randSeedB ^= seed & 0xaaaaaaaaaaaaaaaaL;
    randBitPos = 63; // Force rehash
    advanceBit();
  }

  public void random() {
    sign = 0;
    exponent = 0x3fffffff;
    mantissa = nextBits(63);
    normalize();
  }

//*****************************************************************************

  private int digit(char a, int base, boolean twosComplement) {
    int digit = -1;
    if (a>='0' && a<='9')
      digit = (int)(a-'0');
    else if (a>='a' && a<='f')
      digit = (int)(a-'a')+10;
    if (digit >= base)
      return -1;
    if (twosComplement)
      digit ^= base-1;
    return digit;
  }

  private void shiftUp(int base) {
    if (base==2)
      scalbn(1);
    else if (base==8)
      scalbn(3);
    else if (base==16)
      scalbn(4);
    else
      mul10();
  }

  private void atof(final String a, int base) {
    makeZero(0);
    int length = a.length();
    int index = 0;
    byte tmpSign = 0;
    boolean compl = false;
    while (index<length && a.charAt(index)==' ')
      index++;
    if (index<length && a.charAt(index)=='-') {
      tmpSign=1;
      index++;
    } else if (index<length && a.charAt(index)=='+') {
      index++;
    } else if (index<length && a.charAt(index)=='/') {
      // Input is twos complemented negative number
      compl=true;
      tmpSign=1;
      index++;
    }
    int d;
    while (index<length && (d=digit(a.charAt(index),base,compl))>=0) {
      shiftUp(base);
      tmp1.assign(d);
      add(tmp1);
      index++;
    }
    if (index<length && (a.charAt(index)=='.' || a.charAt(index)==',')) {
      index++;
      tmp2.assign(ONE);
      while (index<length && (d=digit(a.charAt(index),base,compl))>=0) {
        tmp2.shiftUp(base);
        shiftUp(base);
        tmp1.assign(d);
        add(tmp1);
        index++;
      }
      if (compl)
        add(ONE);
      div(tmp2);
    } else if (compl) {
      add(ONE);
    }
    while (index<length && a.charAt(index)==' ')
      index++;
    if (index<length && (a.charAt(index)=='e' || a.charAt(index)=='E')) {
      index++;
      int exp = 0;
      boolean expNeg = false;
      if (index<length && a.charAt(index)=='-') {
        expNeg = true;
        index++;
      } else if (index<length && a.charAt(index)=='+') {
        index++;
      }
      while (index<length && a.charAt(index)>='0' && a.charAt(index)<='9') {
        if (exp < 400000000) // This takes care of overflows and makes inf or 0
          exp = (exp*10)+(int)(a.charAt(index)-'0');
        index++;
      }
      if (expNeg)
        exp = -exp;

      if (base==2)
        scalbn(exp);
      else if (base==8)
        scalbn(exp*3);
      else if (base==16)
        scalbn(exp*4);
      else {
        tmp1.makeExp10(exp);
        mul(tmp1);
      }
    }
    sign = tmpSign;
  }

//*****************************************************************************

  private void normalizeBCD() {
    if (mantissa == 0) {
      exponent = 0;
      return;
    }
    int carry = 0;
    for (int i=0; i<64; i+=4) {
      int d = (int)((mantissa>>>i)&0xf) + carry;
      carry = 0;
      if (d >= 10) {
        d -= 10;
        carry = 1;
      }
      mantissa &= ~(0xfL<<i);
      mantissa += (long)d<<i;
    }
    if (carry != 0) {
      if ((int)(mantissa&0xf)>=5)
        mantissa += 0x10; // Rounding, may be inaccurate
      mantissa >>>= 4;
      mantissa += 1L<<60;
      exponent++;
      if ((int)(mantissa&0xf)>=10) {
        // Oh, no, not again!
        normalizeBCD();
      }
    }
    while ((mantissa >>> 60) == 0) {
      mantissa <<= 4;
      exponent--;
    }
  }

  private void toBCD() {
    // Convert normal nonzero finite Real to BCD format, represented by 16
    // 4-bit decimal digits in the mantissa, and exponent as a power of 10
    tmp1.assign(this);
    tmp1.abs();
    tmp2.assign(tmp1);
    exponent = tmp1.lowPow10();
    if (exponent > 300000000 || exponent < -300000000) {
      // Kludge to be able to print very large and very small numbers
      // without causing over/underflows
      tmp1.makeExp10(exponent/2);
      tmp2.div(tmp1); // So, divide twice by not-so-extreme numbers
      tmp1.makeExp10(exponent-(exponent/2));
    }
    tmp2.div(tmp1);
    mantissa = 0;
    for (int i=60; i>=0; i-=4) {
      tmp1.assign(tmp2);
      tmp1.floor();
      mantissa += (long)tmp1.toInteger()<<i;
      tmp2.sub(tmp1);
      tmp2.mul10();
    }
    if (tmp2.greaterEqual(FIVE))
      mantissa++; // Rounding
    normalizeBCD();
  }

  private boolean testCarryWhenRounded(int base,int bitsPerDigit,int precision)
  {
    if (mantissa >= 0)
      return false; // msb not set -> carry cannot occur
    int shift = 64-precision*bitsPerDigit;
    if (shift<=0)
      return false; // too high precision -> no rounding necessary
    tmp5.assign(this);
    if (base==10) {
      tmp5.mantissa += (5L<<(shift-4));
      tmp5.normalizeBCD();
    } else {
      tmp5.mantissa += (1L<<(shift-1));
    }
    if (tmp5.mantissa >= 0) {
      // msb has changed from 1 to 0 -> we have carry
      // now modify the original number
      mantissa = (1L<<(64-bitsPerDigit));
      exponent++;
      return true;
    }
    return false;
  }

  private void round(int base, int bitsPerDigit, int precision) {
    if (isZero())
      return;
    int shift = 64-precision*bitsPerDigit;
    if (shift<=0)
      return; // too high precision -> no rounding necessary
    long orgMantissa = mantissa;
    if (base==10) {
      mantissa += (5L<<(shift-4));
      normalizeBCD();
    } else {
      mantissa += (1L<<(shift-1));
    }
    if (orgMantissa < 0 && mantissa >= 0) {
      // msb has changed from 1 to 0 -> we have carry
      mantissa = (1L<<(64-bitsPerDigit));
      exponent++;
    }
  }

  public static class NumberFormat
  {
    public int base;              // 2 8 10 16
    public int maxwidth;          // 30
    public int precision;         // 0-16 (for decimal)
    public int fse;               // NONE FIX SCI ENG
    public char point;            // '.' ','
    public boolean removePoint;   // true/false
    public char thousand;         // '.' ',' ' ' 0
    
    public static final int FSE_NONE = 0;
    public static final int FSE_FIX  = 1;
    public static final int FSE_SCI  = 2;
    public static final int FSE_ENG  = 3;
    
    NumberFormat() {
      base = 10;
      maxwidth = 30;
      precision = 16;
      fse = FSE_NONE;
      point = '.';
      removePoint = true;
      thousand = 0;
    }
  }

  private static StringBuffer ftoaBuf = new StringBuffer(40);
  private static StringBuffer ftoaExp = new StringBuffer(15);
  public static final String hexChar = "0123456789abcdef";

  private String ftoa(NumberFormat format) {
    if (isNan())
      return "nan";
    if (isInfinity())
      return (sign!=0)?"-inf":"inf";

    int bitsPerDigit,digitsPerThousand;
    switch (format.base) {
      case 2:
        bitsPerDigit = 1;
        digitsPerThousand = 8;
        break;
      case 8:
        bitsPerDigit = 3;
        digitsPerThousand = 1000; // Disable thousands separator
        break;
      case 16:
        bitsPerDigit = 4;
        digitsPerThousand = 4;
        break;
      case 10:
      default:
        bitsPerDigit = 4;
        digitsPerThousand = 3;
        break;
    }
    if (format.thousand == 0)
      digitsPerThousand = 1000; // Disable thousands separator

    int accurateBits = 64;
    tmp4.assign(this);
    if (isZero()) {
      tmp4.exponent = 0;
      if (format.base != 10)
        tmp4.sign = 0; // Two's complement cannot display -0
    } else {
      if (format.base == 10) {
        tmp4.toBCD();
      } else {
        if (tmp4.sign != 0) {
          tmp4.mantissa = -tmp4.mantissa;
          if ((tmp4.mantissa >>> 62) == 3) {
            tmp4.mantissa <<= 1;
            tmp4.exponent--;
            accurateBits--;
          }
        }
        tmp4.exponent -= 0x40000000-1;
        int shift = bitsPerDigit-1 - floorMod(tmp4.exponent,bitsPerDigit);
        tmp4.exponent = floorDiv(tmp4.exponent,bitsPerDigit);
        if (shift == bitsPerDigit-1) {
          // More accurate to shift up instead
          tmp4.mantissa <<= 1;
          tmp4.exponent--;
          accurateBits--;
        }
        else if (shift>0)
          tmp4.mantissa = (tmp4.mantissa+(1L<<(shift-1)))>>shift; // >> not >>>
      }
    }
    int accurateDigits = (accurateBits+bitsPerDigit-1)/bitsPerDigit;

    int precision;
    int pointPos = 0;
    do
    {
      int width = format.maxwidth-1; // subtract 1 for decimal point
      int prefix = 0;
      if (format.base != 10)
        prefix = 1; // want room for at least one "0" or "f/7/1"
      else if (tmp4.sign != 0)
        width--; // subtract 1 for sign

      boolean useExp = false;
      switch (format.fse) {
        case NumberFormat.FSE_SCI:
          precision = format.precision+1;
          useExp = true;
          break;
        case NumberFormat.FSE_ENG:
          pointPos = floorMod(tmp4.exponent,3);
          precision = format.precision+1+pointPos;
          useExp = true;
          break;
        case NumberFormat.FSE_FIX:
        case NumberFormat.FSE_NONE:
        default:
          precision = 1000;
          if (format.fse == NumberFormat.FSE_FIX)
            precision = format.precision+1;
          if (tmp4.exponent+1 >
                width-(tmp4.exponent+prefix)/digitsPerThousand-prefix+
                (format.removePoint ? 1:0) ||
              tmp4.exponent+1 > accurateDigits ||
              -tmp4.exponent >= width ||
              -tmp4.exponent >= precision)
          {
            useExp = true;
          } else {
            pointPos = tmp4.exponent;
            precision += tmp4.exponent;
            if (tmp4.exponent > 0)
              width -= (tmp4.exponent+prefix)/digitsPerThousand;
            if (format.removePoint && tmp4.exponent == width-prefix)
              width++; // Add  1 for the decimal point that will be removed
          }
          break;
      }

      if (prefix!=0 && pointPos>=0)
        width -= prefix;

      ftoaExp.setLength(0);
      if (useExp) {
        ftoaExp.append('E');
        if (tmp4.exponent-pointPos >= 0)
          ftoaExp.append('+');
        ftoaExp.append(tmp4.exponent-pointPos);
        width -= ftoaExp.length();
      }
      if (precision > accurateDigits)
        precision = accurateDigits;
      if (precision > width)
        precision = width;
      if (precision > width+pointPos) // In case of negative pointPos
        precision = width+pointPos;
      if (precision <= 0)
        precision = 1;
    }
    while (tmp4.testCarryWhenRounded(format.base,bitsPerDigit,precision));

    tmp4.round(format.base,bitsPerDigit,precision);

    // Start generating the string. First the sign
    ftoaBuf.setLength(0);
    if (tmp4.sign!=0 && format.base == 10)
      ftoaBuf.append('-');

    // Save pointPos for hex/oct/bin prefixing with thousands-sep
    int pointPos2 = pointPos;

    // Add leading zeros (or f/7/1)
    char prefixChar = (format.base==10 || tmp4.sign==0) ? '0' :
                      hexChar.charAt(format.base-1);
    if (pointPos < 0) {
      ftoaBuf.append(prefixChar);
      ftoaBuf.append(format.point);
      while (pointPos < -1) {
        ftoaBuf.append(prefixChar);
        pointPos++;
      }
    }

    // Add fractional part
    while (precision > 0) {
      ftoaBuf.append(hexChar.charAt((int)(tmp4.mantissa>>>(64-bitsPerDigit))));
      tmp4.mantissa <<= bitsPerDigit;
      if (pointPos>0 && pointPos%digitsPerThousand==0)
        ftoaBuf.append(format.thousand);
      if (pointPos == 0)
        ftoaBuf.append(format.point);
      precision--;
      pointPos--;
    }

    if (format.fse == NumberFormat.FSE_NONE) {
      // Remove trailing zeros
      while (ftoaBuf.charAt(ftoaBuf.length()-1) == '0')
        ftoaBuf.setLength(ftoaBuf.length()-1);
    }
    if (format.removePoint) {
      // Remove trailing point
      if (ftoaBuf.charAt(ftoaBuf.length()-1) == format.point)
        ftoaBuf.setLength(ftoaBuf.length()-1);
    }

    // Add exponent
    ftoaBuf.append(ftoaExp);

    // In case hex/oct/bin number, prefix with 0's or f/7/1's
    if (format.base!=10) {
      while (ftoaBuf.length()<format.maxwidth) {
        pointPos2++;
        if (pointPos2>0 && pointPos2%digitsPerThousand==0)
          ftoaBuf.insert(0,format.thousand);
        if (ftoaBuf.length()<format.maxwidth)
          ftoaBuf.insert(0,prefixChar);
      }
      if (ftoaBuf.charAt(0) == format.thousand)
        ftoaBuf.deleteCharAt(0);
    }

    return ftoaBuf.toString();
  }

  private static NumberFormat tmpFormat = new NumberFormat();

  public String toString() {
    tmpFormat.base = 10;
    return ftoa(tmpFormat);
  }

  public String toString(int base) {
    tmpFormat.base = base;
    return ftoa(tmpFormat);
  }

  public String toString(NumberFormat format) {
    return ftoa(format);
  }

///////////////////////////////////////////////////////////////////////////////
// To make library re-entrant, change private static tmp variables with this:
//
//   private static final int FREE = 2;
//
//   private boolean isFree() {
//     return (sign == FREE);
//   }
//   private void setFree() {
//     sign = FREE;
//   }
//
//   private static Real [] tmpArray = {
//     new Real(FREE,0,0), new Real(FREE,0,0),
//     new Real(FREE,0,0), new Real(FREE,0,0),
//     new Real(FREE,0,0), new Real(FREE,0,0),
//     new Real(FREE,0,0), new Real(FREE,0,0)
//   };
//
//   private static Real allocTmp() {
//     synchronized (tmpArray) {
//       int i;
//       for (i=0; i<tmpArray.length; i++)
//         if (tmpArray[i].isFree()) {
//           tmpArray[i].makeZero(0);
//           return tmpArray[i];
//         }
//       Real [] newTmpArray = new Real[tmpArray.length*2];
//       for (i=0; i<tmpArray.length; i++)
//         newTmpArray[i] = tmpArray[i];
//       for (i=tmpArray.length; i<newTmpArray.length; i++)
//         newTmpArray[i] = new Real(FREE,0,0);
//       tmpArray = newTmpArray;
//
//       i = tmpArray.length;
//       tmpArray[i].makeZero(0);
//       return tmpArray[i];
//     }
//   }
//
//   private static void freeTmp(Real a) {
//     synchronized (tmpArray) {
//       for (int i=0; i<tmpArray.length; i++)
//         if (a == tmpArray[i]) {
//           a.setFree();
//           return;
//         }
//     }
//   }
  
}
