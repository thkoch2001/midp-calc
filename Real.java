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
//   String toString(int)
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
//   scalb(int)
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
//   HALF    = 1/2
//   TEN     = 10
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
//   MAX     = maximum representable non-infinite positive number
//   MIN     = minimum representable non-zero positive number
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
  public static final Real HALF   = new Real(0,0x3fffffff,0x4000000000000000L);
  public static final Real TEN    = new Real(0,0x40000003,0x5000000000000000L);
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
    assign((byte)s,e,m);
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
  
  public void assign(byte s, int e, long m) {
    sign = s;
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

  public void scalb(int n) {
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
      else
        assign(ONE);
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

    byte s;
    int e;
    long m;
    if (exponent > a.exponent ||
        (exponent == a.exponent) && mantissa>=a.mantissa)
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

    normalize();
    if (isZero())
      sign=0;
  }

  private static Real subTmp = new Real();
  
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

  private static Real recipTmp = new Real();
  private static Real recipTmp2 = new Real();
  
  private void recipInternal() {
    // Calculates recipocal of normalized Real, not zero, nan or infinity

    // Special case, simple power of 2
    if (mantissa == 0x4000000000000000L) {
      exponent = 0x80000000-exponent;
      if (exponent<0) // Overflow
        makeInfinity(sign);
      return;
    }

    // Save -A    
    recipTmp.assign(this);
    recipTmp.neg();

    // First establish approximate result (actually 30 bit accurate)
    
    mantissa = (0x4000000000000000L/(mantissa>>>32))<<30;
    exponent = 0x80000000-exponent;
    normalize();

    // Now perform Newton-Raphson iteration
    // Xn+1 = Xn + Xn*(1-A*Xn)

    for (int i=0; i<2; i++) {
      recipTmp2.assign(this);
      mul(recipTmp);
      add(ONE);
      mul(recipTmp2);
      add(recipTmp2);
    }
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

  private static Real divTmp = new Real();
  
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
  
  private static Real rsTmp = new Real();
  private static Real rsTmp2 = new Real();

  private void rsqrtInternal() {
    // Calculates recipocal square root of normalized Real,
    // not zero, nan or infinity
    final long start = 0x4e60000000000000L;

    // Save -A    
    rsTmp.assign(this);
    rsTmp.neg();

    // First establish approximate result
    
    mantissa = start-(mantissa>>2);
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
      scalb(-1);
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

  private static Real sqrtTmp = new Real();

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

  private static Real hypotTmp = new Real();

  public void hypot(final Real a) {
    hypotTmp.assign(a);
    hypotTmp.sqr();
    sqr();
    add(hypotTmp);
    sqrt();
  }

  private static Real expTmp = new Real();
  private static Real expTmp2 = new Real();
  private static Real expTmp3 = new Real();

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

    expTmp.assign(this);
    expTmp2.assign(this);
    add(ONE);
    for (int i=2; i<18; i++) {
      expTmp2.mul(expTmp);
      expTmp3.assign(i);
      expTmp2.div(expTmp3);
      add(expTmp2);
    }

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

  private static Real lnTmp = new Real();
  private static Real lnTmp2 = new Real();
  private static Real lnTmp3 = new Real();

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
      System.out.println(this);
      lnTmp.mul(lnTmp2);
      lnTmp3.assign(i);
      lnTmp3.recipInternal();
      lnTmp3.mul(lnTmp);
      add(lnTmp3);
    }
    scalb(1);
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

  private static Real powTmp = new Real();

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
      powTmp.assign(this);
      powTmp.abs();
      int test = powTmp.compare(ONE);
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
        powTmp.assign(exp);
        powTmp.floor();
        if (exp.equalTo(powTmp) && (powTmp.toInteger() & 1)!=0) {
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
        powTmp.assign(exp);
        powTmp.floor();
        if (exp.equalTo(powTmp)) {
          if ((powTmp.toInteger() & 1)!=0) {
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
      powTmp.assign(exp);
      powTmp.floor();
      if (exp.equalTo(powTmp)) {
        if ((powTmp.toInteger() & 1)!=0)
          s = 1;
      } else {
        makeNan();
        return;
      }
      sign = 0;
    }

    powTmp.assign(exp);
    log2();
    mul(powTmp);
    exp2();
    if (!isNan())
      sign = s;
  }

  private static Real sinTmp = new Real();
  private static Real sinTmp2 = new Real();
  private static Real sinTmp3 = new Real();
  private static Real sinTmp4 = new Real();

  private void sinInternal() {
    // Calculate sine for 0 < x < pi/4
    // Using the classic Taylor series.
    //                      3        5        7
    //                     x        x        x
    //  sin(x)  =   x  -  ----  +  ----  -  ----  +  ...
    //                     3!       5!       7!

    sinTmp.assign(this);
    sinTmp2.assign(this);
    sinTmp2.sqr();
    sinTmp2.neg();
    for (int i=3; i<18; i+=2) {
      sinTmp.mul(sinTmp2);
      sinTmp3.assign(i);
      sinTmp4.assign(i-1);
      sinTmp3.mul(sinTmp4);
      sinTmp.div(sinTmp3);
      add(sinTmp);
    }
  }

  private static Real cosTmp = new Real();
  private static Real cosTmp2 = new Real();
  private static Real cosTmp3 = new Real();
  private static Real cosTmp4 = new Real();

  private void cosInternal() {
    // Calculate cosine for 0 < x < pi/4
    // Using the classic Taylor series.
    //                      2        4        6
    //                     x        x        x
    //  cos(x)  =   1  -  ----  +  ----  -  ----  +  ...
    //                     2!       4!       6!

    cosTmp2.assign(this);
    cosTmp2.sqr();
    cosTmp2.neg();
    assign(ONE);
    cosTmp.assign(ONE);
    for (int i=2; i<19; i+=2) {
      cosTmp.mul(cosTmp2);
      cosTmp3.assign(i);
      cosTmp4.assign(i-1);
      cosTmp3.mul(cosTmp4);
      cosTmp.div(cosTmp3);
      add(cosTmp);
    }
  }

  public void sin() {
    if (isNan() || isInfinity()) {
      makeNan();
      return;
    }

    // First reduce the argument to the range of 0 < x < pi*2
    div(PI2);
    sinTmp.assign(this);
    sinTmp.floor();
    sub(sinTmp);
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

  private static Real tanTmp = new Real();

  public void tan() {
    tanTmp.assign(this);
    tanTmp.cos();
    sin();
    div(tanTmp);
  }

  private static Real asinTmp = new Real();

  public void asin() {
    asinTmp.assign(this);
    sqr();
    neg();
    add(ONE);
    rsqrt();
    mul(asinTmp);
    atan();
  }

  private static Real acosTmp = new Real();

  public void acos() {
    boolean negative = false;
    if (sign!=0)
      negative = true;
    abs();

    acosTmp.assign(this);
    sqr();
    neg();
    add(ONE);
    sqrt();
    div(acosTmp);
    atan();
 
    if (negative) {
      neg();
      add(PI);
    }
  }

  private static Real atanTmp = new Real();
  private static Real atanTmp2 = new Real();
  private static Real atanTmp3 = new Real();

  private void atanInternal() {
    // Calculate atan for 0 < x < sqrt(2)-1
    // Using the classic Taylor series.
    //                       3        5        7
    //                      x        x        x
    //  atan(x)  =   x  -  ----  +  ----  -  ----  +  ...
    //                      3        5        7

    atanTmp.assign(this);
    atanTmp2.assign(this);
    atanTmp2.sqr();
    atanTmp2.neg();
    for (int i=3; i<48; i+=2) {
      System.out.println(this);
      atanTmp.mul(atanTmp2);
      atanTmp3.assign(i);
      atanTmp3.recipInternal();
      atanTmp3.mul(atanTmp);
      add(atanTmp3);
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
    
    atanTmp.assign(SQRT2);
    atanTmp.sub(ONE);

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
    if (greaterThan(atanTmp))
    {
      sub = true;
      atanTmp2.assign(this);
      sub(atanTmp);
      atanTmp2.mul(atanTmp);
      atanTmp2.add(ONE);
      div(atanTmp2);
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

  private static Real sinhTmp = new Real();

  public void sinh() {
    sinhTmp.assign(this);
    sinhTmp.neg();
    sinhTmp.exp();
    exp();
    sub(sinhTmp);
    scalb(-1);
  }

  private static Real coshTmp = new Real();

  public void cosh() {
    coshTmp.assign(this);
    coshTmp.neg();
    coshTmp.exp();
    exp();
    add(coshTmp);
    scalb(-1);
  }

  private static Real tanhTmp = new Real();
  private static Real tanhTmp2 = new Real();

  public void tanh() {
    tanhTmp.assign(this);
    tanhTmp.neg();
    tanhTmp.exp();
    exp();
    tanhTmp2.assign(this);
    tanhTmp2.add(tanhTmp);
    sub(tanhTmp);
    div(tanhTmp2);
  }

  private static Real asinhTmp = new Real();

  public void asinh() {
    asinhTmp.assign(this);
    asinhTmp.sqr();
    asinhTmp.add(ONE);
    asinhTmp.sqrt();
    add(asinhTmp);
    ln();
  }

  private static Real acoshTmp = new Real();

  public void acosh() {
    acoshTmp.assign(this);
    acoshTmp.sqr();
    acoshTmp.sub(ONE);
    acoshTmp.sqrt();
    add(acoshTmp);
    ln();
  }

  private static Real atanhTmp = new Real();

  public void atanh() {
    atanhTmp.assign(this);
    atanhTmp.neg();
    atanhTmp.add(ONE);
    add(ONE);
    div(atanhTmp);
    ln();
    scalb(-1);
  }

  public void fact() {
    //...
  }

  private static Real atofTmp = new Real();
  private static Real atofTmp2 = new Real();

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
      atofTmp.assign((int)(a.charAt(index)-'0'));
      add(atofTmp);
      index++;
    }
    if (index<length && a.charAt(index)=='.') {
      index++;
      atofTmp2.assign(ONE);
      while (index<length && a.charAt(index)>='0' && a.charAt(index)<='9') {
        mul(TEN);
        atofTmp.assign((int)(a.charAt(index)-'0'));
        add(atofTmp);
        atofTmp2.mul(TEN);
        index++;
      }
      div(atofTmp2);
    }
    while (index<length && a.charAt(index)==' ')
      index++;
    if (index<length && (a.charAt(index)=='e' || a.charAt(index)=='E')) {
      index++;
      atofTmp2.makeZero(0);
      byte eTmpSign = 0;
      if (index<length && a.charAt(index)=='-') {
        eTmpSign=1;
        index++;
      } else if (index<length && a.charAt(index)=='+') {
        index++;
      }
      while (index<length && a.charAt(index)>='0' && a.charAt(index)<='9') {
        atofTmp2.mul(TEN);
        atofTmp.assign((int)(a.charAt(index)-'0'));
        atofTmp2.add(atofTmp);
        index++;
      }
      atofTmp2.sign = eTmpSign;
      atofTmp2.exp10();
      mul(atofTmp2);
    }
    sign = tmpSign;
  }

  private String ftoa(int base) {
    if (isNan())
      return "nan";
    if (isInfinity())
      return ((sign!=0)?"-":"")+"inf";
    if (isZero())
      return ((sign!=0)?"-":"")+"0";

    if (base==16) {
      return ((sign!=0)?"-":"")+"0x"+Long.toHexString(mantissa)+" E"+
        ((exponent>=0x40000000)?"+":"")+(exponent-0x40000000);
    } else {
      return ((sign!=0)?"-":"")+"x";
    }
  }

  public String toString() {
    return ftoa(16); // Should be 10
  }
  
  public String toString(int base) {
    return ftoa(base);
  }
  
}
