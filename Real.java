// class Real: Integer implementation of 64-bit precision floating point
//
// Constructors/assignment:
//   Real()
//   Real(Real)
//   Real(int)
//   Real(String)
//   assign(Real)
//   assign(int)
//   assign(String)
//
// Output:
//   String toString()
//   String toString(int base)
//   int toInteger()
//
// Binary operators:
//   add(Real)
//   sub(Real)
//   mul(Real)
//   div(Real)
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
//   gamma()
//
// Binary functions:
//   hypot(Real)
//   atan2(Real)
//   pow(Real)
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
//
// Query abnormal states:
//   boolean isZero()
//   boolean isInfinity()
//   boolean isNan()
//   boolean isFinite()
//
// Constants:
//   ZERO    = 0
//   ONE     = 1
//   TWO     = 2
//   FIVE    = 5
//   TEN     = 10
//   HALF    = 1/2
//   THIRD   = 1/3
//   SQRT2   = sqrt(2)
//   SQRT1_2 = sqrt(1/2)
//   PI2     = PI*2
//   PI      = PI
//   PI_2    = PI/2
//   PI_4    = PI/4
//   PI_8    = PI/8
//   E       = e
//   LN2     = ln(2)
//   LN10    = ln(10)
//   LOG2E   = log2(e)
//   LOG10E  = log10(e)
//   MAX     = max non-infinite positive number = 4.197E+323228496
//   MIN     = min non-zero positive number     = 2.383E-323228497
//   NAN     = not a number
//   INF     = infinity
//   INF_N   = -infinity
//   ZERO_N  = -0
//
public final class Real
{
  long mantissa;
  int exponent;
  byte sign;

  public static final Real ZERO   = new Real(0,0x00000000,0x0000000000000000L);
  public static final Real ONE    = new Real(0,0x40000000,0x4000000000000000L);
  public static final Real TWO    = new Real(0,0x40000001,0x4000000000000000L);
  public static final Real FIVE   = new Real(0,0x40000002,0x5000000000000000L);
  public static final Real TEN    = new Real(0,0x40000003,0x5000000000000000L);
  public static final Real HALF   = new Real(0,0x3fffffff,0x4000000000000000L);
  public static final Real THIRD  = new Real(0,0x3ffffffe,0x5555555555555555L);
//public static final Real DIV2_3 = new Real(0,0x3fffffff,0x5555555555555555L);
  public static final Real SQRT2  = new Real(0,0x40000000,0x5a827999fcef3242L);
  public static final Real SQRT1_2= new Real(0,0x3fffffff,0x5a827999fcef3242L);
  public static final Real PI2    = new Real(0,0x40000002,0x6487ed5110b4611aL);
  public static final Real PI     = new Real(0,0x40000001,0x6487ed5110b4611aL);
  public static final Real PI_2   = new Real(0,0x40000000,0x6487ed5110b4611aL);
  public static final Real PI_4   = new Real(0,0x3fffffff,0x6487ed5110b4611aL);
  public static final Real PI_8   = new Real(0,0x3ffffffe,0x6487ed5110b4611aL);
  public static final Real E      = new Real(0,0x40000001,0x56fc2a2c515da548L);
  public static final Real LN2    = new Real(0,0x3fffffff,0x58b90bfbe8e7bccfL);
//public static final Real LN2_3  = new Real(1,0x3ffffffe,0x67cc8fb2fe612fc2L);
  public static final Real LN10   = new Real(0,0x40000001,0x49aec6eed5545605L);
  public static final Real LOG2E  = new Real(0,0x40000000,0x5c551d94ae0bf865L);
  public static final Real LOG10E = new Real(0,0x3ffffffe,0x6f2dec549b9438d5L);
  public static final Real MAX    = new Real(0,0x7FFFFFFF,0x7FFFFFFFFFFFFFFFL);
  public static final Real MIN    = new Real(0,0x00000000,0x4000000000000000L);
  public static final Real NAN    = new Real(0,0x80000000,0x4000000000000000L);
  public static final Real INF    = new Real(0,0x80000000,0x0000000000000000L);
  public static final Real INF_N  = new Real(1,0x80000000,0x0000000000000000L);
  public static final Real ZERO_N = new Real(1,0x00000000,0x0000000000000000L);
  
  public Real() {
    assign(ZERO);
  }

  public Real(int s, int e, long m) {
    assign(s,e,m);
  }
  
  public Real(final Real a) {
    assign(a);
  }
  
  public Real(int a) {
    assign(a);
  }

  public Real(final String a) {
    atof(a);
  }
  
  public void assign(int s, int e, long m) {
    sign = (byte)s;
    exponent = e;
    mantissa = m;
  }

  public void assign(final Real a) {
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

  public void assign(final String a) {
    atof(a);
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
    return (mantissa == 0 && exponent == 0);
  }

  public boolean isInfinity() {
    return (mantissa == 0 && exponent < 0);
  }

  public boolean isNan() {
    return (mantissa != 0 && exponent < 0);
  }

  public boolean isFinite() {
    return exponent >= 0;
  }

  public void abs() {
    sign = 0;
  }

  public void neg() {
    if (!isNan())
      sign ^= 1;
  }

  public void copysign(final Real a) {
    if (!isNan())
      sign = a.sign;
  }

  public void normalize() {
    if (mantissa==0) {
      if (!isInfinity())
        makeZero(sign);
      return;
    }
    if ((mantissa >>> 63) != 0) {
      mantissa >>>= 1;
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
    if (isInfinity())
      return sign==0 ? 1 : -1;
    if (a.isInfinity())
      return a.sign==0 ? -1 : 1;
    if (exponent != a.exponent)
      return exponent<a.exponent ? -1 : 1;
    if (mantissa != a.mantissa)
      return mantissa<a.mantissa ? -1 : 1;
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
    if (isZero() || isInfinity() || isNan())
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
    if (isZero() || isInfinity() || isNan())
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
    if (isZero() || isInfinity() || isNan())
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
    if (isZero() || isInfinity() || isNan())
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
    if (isZero() || isInfinity() || isNan() || exponent < 0x40000000)
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
        return 0x7ffffffe; // Deliberately not odd (because of pow())
      else
        return 0x80000000;
    }
    if (exponent < 0x40000000)
      return 0;
    int shift = 0x4000003e-exponent;
    if (shift < 32) {
      if (sign==0)
        return 0x7ffffffe; // Deliberately not odd (because of pow())
      else
        return 0x80000000;
    }
    return (sign==0) ? (int)(mantissa>>>shift) : -(int)(mantissa>>>shift);
  }

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
        makeInfinity(sign);
      return;
    }
    if (isZero() || a.isZero())
    {
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

      if (shift==0) {
        if (sign == s)
          mantissa += m;
        else
          mantissa -= m;
      } else {
        if (sign == s)
          mantissa += (m+(1<<(shift-1)))>>>shift;
        else
          mantissa -= (m-(1<<(shift-1)))>>>shift;
      }
    }

    normalize();
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
    int e = a.exponent;
    exponent += a.exponent-0x40000000;
    if (exponent < 0) {
      if (e<0x4000000)
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

  private void recipInternal() {
    // Calculates recipocal of normalized Real, not zero, nan or infinity

    // Special case, simple power of 2
    if (mantissa == 0x4000000000000000L) {
      exponent = 0x80000000-exponent;
      if (exponent<0) // Overflow
        makeInfinity(sign);
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
    sign ^= a.sign;
    if (isInfinity()) {
      if (a.isInfinity())
        makeNan();
      return;
    }
    if (a.isInfinity()) {
      makeZero(sign);
      return;
    }
    if (isZero()) {
      if (a.isZero())
        makeNan();
      return;
    }
    if (a.isZero()) {
      makeInfinity(sign);
      return;
    }
    divTmp.assign(a);
    divTmp.recipInternal();
    mul(divTmp);
  }

  public void sqr() {
    if (isNan())
      return;
    sign = 0;
    if (isInfinity() || isZero())
      return;

    int e = exponent;
    exponent += exponent-0x40000000;
    if (exponent < 0) {
      if (e<0x4000000)
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
    boolean flag=false;
    if ((exponent&1) != 0)
      flag=true;
    exponent = 0x20000000+(exponent>>1);
    normalize();
    if (flag)
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
    if (isZero() || isInfinity() || isNan())
      return;

    byte s = sign;
    sign = 0;
    log2();
    mul(THIRD);
    exp2();
    if (!isNan())
      sign = s;
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

    // Using the classic Taylor series.
    //                       2        3        4
    //   x                  x        x        x
    //  e  =   1  +  x  +  ----  +  ----  +  ----  +  ...
    //                      2!       3!       4!

//     expTmp.assign(this);
//     expTmp2.assign(this);
//     add(ONE);
//     for (int i=2; i<18; i++) {
//       expTmp2.mul(expTmp);
//       expTmp3.assign(i);
//       expTmp2.div(expTmp3);
//       add(expTmp2);
//     }

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
    mul(LN10);
    mul(LOG2E);
    exp2();
  }

  public void makeExp10(int power) {
    // Calculate power of 10 by successive squaring for increased accuracy
    // (Perhaps it is not so accurate for large arguments?)
    boolean recp=false;
    if (power < 0) {
      power = -power; // Also works for 0x80000000 (but will underflow)
      recp = true;
    }
    assign(ONE);
    expTmp.assign(TEN);
    for (; power!=0; power>>>=1) {
      if ((power & 1) != 0)
        mul(expTmp);
      expTmp.sqr();
    }
    if (recp)
      recip();
  }
  
  private void lnInternal() {
    // Calculates natural logarithm ln(a) for numbers between 1 and 2, using
    //
    //                                3        5        7
    //      x+1           /          x        x        x          \
    //  ln ----- =   2 *  |  x  +   ----  +  ----  +  ---- + ...  |
    //      x-1           \          3        5        7          /
    //
    // ,where x = (a-1)/(a+1)

    lnTmp.assign(this);
    lnTmp.add(ONE);
    sub(ONE);
    div(lnTmp);

    lnTmp.assign(this);
    lnTmp2.assign(this);
    lnTmp2.sqr();
    for (int i=3; i<40; i+=2) {
      lnTmp.mul(lnTmp2);
      lnTmp3.assign(i);
      lnTmp3.recipInternal();
      lnTmp3.mul(lnTmp);
      add(lnTmp3);
    }
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
        tmp1.assign(exp);
        tmp1.floor();
        if (exp.equalTo(tmp1) && (tmp1.toInteger() & 1)!=0) {
          // Ignoring possible overflow in toInteger()
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
        tmp1.assign(exp);
        tmp1.floor();
        if (exp.equalTo(tmp1)) {
          if ((tmp1.toInteger() & 1)!=0) {
            // Ignoring possible overflow in toInteger()
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
    byte s=0;
    if (sign!=0) {
      tmp1.assign(exp);
      tmp1.floor();
      if (exp.equalTo(tmp1)) {
        if ((tmp1.toInteger() & 1)!=0)
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
    // Using the classic Taylor series.
    //                      3        5        7
    //                     x        x        x
    //  sin(x)  =   x  -  ----  +  ----  -  ----  +  ...
    //                     3!       5!       7!

    tmp1.assign(this);
    tmp2.assign(this);
    tmp2.sqr();
    tmp2.neg();
    for (int i=3; i<18; i+=2) {
      tmp1.mul(tmp2);
      tmp3.assign(i);
      tmp4.assign(i-1);
      tmp3.mul(tmp4);
      tmp1.div(tmp3);
      add(tmp1);
    }
  }

  private void cosInternal() {
    // Calculate cosine for 0 < x < pi/4
    // Using the classic Taylor series.
    //                      2        4        6
    //                     x        x        x
    //  cos(x)  =   1  -  ----  +  ----  -  ----  +  ...
    //                     2!       4!       6!

    tmp2.assign(this);
    tmp2.sqr();
    tmp2.neg();
    assign(ONE);
    tmp1.assign(ONE);
    for (int i=2; i<19; i+=2) {
      tmp1.mul(tmp2);
      tmp3.assign(i);
      tmp4.assign(i-1);
      tmp3.mul(tmp4);
      tmp1.div(tmp3);
      add(tmp1);
    }
  }

  public void sin() {
    if (isNan() || isInfinity()) {
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
    boolean neg = false;
    if (greaterThan(PI)) {
      sub(PI2);
      neg();
      neg = true;
    }

    // Since sin(x) = sin(pi - x) we can reduce the range to 0 < x < pi/2
    if (greaterThan(PI_2)) {
      sub(PI);
      neg();
    }

    // Since sin(x) = cos(pi/2 - x) we can reduce the range to 0 < x < pi/4
    if (greaterThan(PI_4)) {
      sub(PI_2);
      neg();
      cosInternal();
    } else {
      sinInternal();
    }

    if (neg)
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
    boolean negative = false;
    if (sign!=0)
      negative = true;
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
    //                       3        5        7
    //                      x        x        x
    //  atan(x)  =   x  -  ----  +  ----  -  ----  +  ...
    //                      3        5        7

    tmp1.assign(this);
    tmp2.assign(this);
    tmp2.sqr();
    tmp2.neg();
    for (int i=3; i<48; i+=2) {
      tmp1.mul(tmp2);
      tmp3.assign(i);
      tmp3.recipInternal();
      tmp3.mul(tmp1);
      add(tmp3);
    }
  }

  public void atan() {
    if (isNan() || isZero())
      return;
    if (isInfinity()) {
      byte s = sign;
      assign(PI_2);
      sign = s;
      return;
    }
    
    tmp1.assign(SQRT2);
    tmp1.sub(ONE);

    byte s = sign;
    sign = 0;

    boolean recp = false;
    if (compare(ONE)>0) {
      recp = true;
      recip();
    }

    // atan(x) = atan[ (x - a) / (1 + x*a) ] + PI/8
    // ,where a = sqrt(2)-1
    boolean sub = false;
    if (greaterThan(tmp1))
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

  public void gamma() {
    //...
  }

  private void atof(final String a) {
    makeZero(0);
    int length = a.length();
    int index = 0;
    byte tmpSign = 0;
    while (index<length && a.charAt(index)==' ')
      index++;
    if (index<length && a.charAt(index)=='-') {
      tmpSign=1;
      index++;
    } else if (index<length && a.charAt(index)=='+') {
      index++;
    }
    while (index<length && a.charAt(index)>='0' && a.charAt(index)<='9') {
      mul(TEN);
      tmp1.assign((int)(a.charAt(index)-'0'));
      add(tmp1);
      index++;
    }
    if (index<length && a.charAt(index)=='.') {
      index++;
      tmp2.assign(ONE);
      while (index<length && a.charAt(index)>='0' && a.charAt(index)<='9') {
        tmp2.mul(TEN);
        mul(TEN);
        tmp1.assign((int)(a.charAt(index)-'0'));
        add(tmp1);
        index++;
      }
      div(tmp2);
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
      tmp1.makeExp10(exp);
      mul(tmp1);
    }
    sign = tmpSign;
  }

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
    tmp1.log10();
    tmp1.floor();
    exponent = tmp1.toInteger(); // Can not cause overflow
    tmp1.makeExp10(exponent);
    if (tmp1.greaterThan(tmp2)) {
      // Inaccuracy may cause log10(99999) to turn out as e.g. 5.0001
      exponent--;
      tmp1.makeExp10(exponent);
    } else {
      tmp3.makeExp10(exponent+1);
      if (tmp3.lessEqual(tmp2)) {
        // Inaccuracy may cause log10(100000) to turn out as e.g. 4.9999
        exponent++;
        tmp1.assign(tmp3);
      }
    }
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
      tmp2.mul(TEN);
    }
    if (tmp2.greaterEqual(FIVE))
      mantissa++; // Rounding
    normalizeBCD();
  }

  private static StringBuffer ftoaBuf = new StringBuffer(30);

  private String ftoa(int base) {
    if (isNan())
      return "nan";
    if (isInfinity())
      return (sign!=0)?"-inf":"inf";
    if (isZero())
      return (sign!=0)?"-0":"0";

    if (base==16) {
      return ((sign!=0)?"-":"")+"0x"+Long.toHexString(mantissa)+" E"+
        ((exponent>=0x40000000)?"+":"")+(exponent-0x40000000);
    } else if (base==10) {
      tmp4.assign(this);
      tmp4.toBCD();
      ftoaBuf.setLength(0);
      if (tmp4.sign!=0)
        ftoaBuf.append('-');
      ftoaBuf.append((char)('0'+(tmp4.mantissa>>>60)));
      ftoaBuf.append('.');
      for (int i=56; i>=0; i-=4)
        ftoaBuf.append((char)('0'+((tmp4.mantissa>>>i)&0xf)));
      ftoaBuf.append(" E");
      if (tmp4.exponent>=0)
        ftoaBuf.append('+');
      ftoaBuf.append(tmp4.exponent);
      return ftoaBuf.toString();
    } else {
      return ((sign!=0)?"-?":"?");
    }
  }

  public String toString() {
    return ftoa(10);
  }
  
  public String toString(int base) {
    return ftoa(base);
  }
  
}
