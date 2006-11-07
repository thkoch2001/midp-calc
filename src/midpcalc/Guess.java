// Guess: Class to guess a fraction or a simple formula for a given input
//
// Original guess routine by Ondrej Palkovsky
// Accelerated Stern-Brocot tree traversal by Roar Lauritzsen
// Entropy and probability calculations by Kim Øyhus
//
// Copyright (C) 2005      Roar Lauritzsen  <roarl@pvv.org>
// Copyright (C) 2005      Kim Øyhus        <kim@pvv.org>
// Copyright (C) 1999,2000 Ondrej Palkovsky <ondrap@penguin.cz>
//
// This library is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// The following link provides a copy of the GNU General Public License:
//     http://www.gnu.org/licenses/gpl.txt
// If you are unable to obtain the copy from this address, write to
// the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
// Boston, MA 02111-1307 USA

package midpcalc;

public class Guess
{
  // Class to hold one guess expression, e.g. sqrt(a/b) or -e^(-a/b)
  public class GuessExpr
  {
    public static final int FRACTION = 0;
    public static final int MULT_PI  = 1;
    public static final int EXP      = 2;
    public static final int EXP2     = 3;
    public static final int LN       = 4;
    public static final int LOG2     = 5;

    public int a, b, op, sign, power;
    public Real value = new Real();
    public Real eI = new Real();

    // Initialize an expression that translates to "0"
    public void init() {
      a     = 0;
      b     = 1;
      op    = FRACTION;
      sign  = 0;
      power = 1;
      eI.makeZero();
      value.makeZero();
    }

    public void copy(GuessExpr g) {
      a     = g.a;
      b     = g.b;
      op    = g.op;
      sign  = g.sign;
      power = g.power;
      eI.assign(g.eI);
      value.assign(g.value);
    }

    // Calculate result according to operation
    public void calcGuessedValue() {
      int i;
      value.assign(a);
      value.div(b);

      switch (op) {
        case FRACTION:
          if (power == 2)
            value.sqrt();
          else if (power == 3)
            value.cbrt();
          if (sign != 0)
            value.neg();
          break;
        case MULT_PI:
          if (sign != 0)
            value.neg();
          for (i=0; i<power; i++)
            value.mul(Real.PI);
          for (i=0; i>power; i--)
            value.div(Real.PI);
          break;
        case EXP:
          if (sign != 0)
            value.neg();
          value.exp();
          if (power != 0)
            value.neg();
          break;
        case EXP2:
          if (sign != 0)
            value.neg();
          value.exp2();
          if (power != 0)
            value.neg();
          break;
        case LN:
          value.ln();
          break;
        case LOG2:
          value.log2();
          break;
      }
    }

    // Calculate the information e^I,
    // where I = -H = log2(d)-log2(error)-log2(a)-log2(2*b)
    public void calcInformation() {
      Real d      = tmp5;
      Real tmp    = tmp6;
      // eI = fabs(d)/((fabs(guess-d)+scalbn(fabs(d),-62))*a*(2*b-1)*
      //               (1<<entropyPenalty));
      // The 2*b-1 bit is a simple way of saying "if (b!=1) entropyPenalty++;"
      d.assign(origValue);
      eI.assign(value);
      eI.sub(d);
      eI.abs();
      d.abs();
      tmp.assign(d);
      tmp.scalbn(-63); // 1/2 ULP
      eI.add(tmp);
      tmp.assign((2L*b-1)*a);
      eI.mul(tmp);
      eI.scalbn(entropyPenalty);
      eI.rdiv(d);
    }

    // Calculate probability of this being the correct answer
    public void calcProbability(Real prob) {
      // P = 100*eI/(eI_sum+numberOfTries*log2(50))
      prob.assign("5.644"); // log2(50), 50-bit background noise
      prob.mul(numberOfTries);
      prob.add(eI_sum);
      prob.rdiv(eI);
      prob.mul(Real.HUNDRED);
      if (prob.lessThan(Real.TENTH)) // Don't want probability like 6.1e-14%
        prob.makeZero();
    }

    public boolean isBetterThan(GuessExpr g) {
      return eI.greaterThan(g.eI);
    }

    // Convert the guess expression to a string
    public String toString() {
      StringBuffer s = new StringBuffer();

      switch (op) {
        case FRACTION:
          if (sign != 0)
            s.append('-');
          switch (power) {
            case 1:
              if (b == 1) {
                s.append(a);
              } else {
                s.append(a);
                s.append('/');
                s.append(b);
              }
              break;
            case 2:
              s.append("sqrt(");
              if (b == 1) {
                s.append(a);
              } else {
                s.append(a);
                s.append('/');
                s.append(b);
              }
              s.append(')');
              break;
            default:
              if (b == 1) {
                s.append(a);
              } else {
                s.append('(');
                s.append(a);
                s.append('/');
                s.append(b);
                s.append(')');
              }
              s.append("^(1/");
              s.append(power);
              s.append(')');
              break;
          }
          break;
        case MULT_PI:
          if (sign != 0)
            s.append('-');
          if (power>0) {
            if (a>1)  {
              s.append(a);
              s.append('*');
            }
            s.append("pi");
            if (power>1) {
              s.append('^');
              s.append(power);
            }
            if (b>1) {
              s.append('/');
              s.append(b);
            }
          } else {
            s.append(a);
            s.append('/');
            if (b>1) {
              s.append('(');
              s.append(b);
              s.append('*');
            }
            s.append("pi");
            if (power<-1) {
              s.append('^');
              s.append(-power);
            }
            if (b>1)
              s.append(')');
          }
          break;
        case EXP:
        case EXP2:
          if (power != 0)
            s.append('-');
          s.append(op==EXP?"e^":"2^");
          if (b==1) {
            if (sign != 0) {
              s.append("(-");
              s.append(a);
              s.append(')');
            } else {
              s.append(a);
            }
          } else {
            s.append(sign!=0?"(-":"(");
            s.append(a);
            s.append('/');
            s.append(b);
            s.append(')');
          }
          break;
        case LN:
        case LOG2:
          s.append(op==LN?"ln(":"log2(");
          if (b == 1) {
            s.append(a);
          } else {
            s.append(a);
            s.append('/');
            s.append(b);
          }
          s.append(')');
          break;
      }

      calcProbability(tmp1);
      Real.NumberFormat format = new Real.NumberFormat();
      format.fse = Real.NumberFormat.FSE_FIX;
      format.precision = 1;
      format.maxwidth = 4;
      s.append("  [");
      s.append(tmp1.toString(format));
      s.append("%]");

      return s.toString();
    }
  }

  public Real origValue,origValueI;
  public Real eI_sum;
  public GuessExpr firstGuess, secondGuess;

  private GuessExpr newGuess;
  int entropyPenalty,minInteger,maxAB;
  int numberOfTries;

  Real tmp1,tmp2,tmp3,tmp4,tmp5,tmp6;

  Guess() {
    firstGuess  = new GuessExpr();
    secondGuess = new GuessExpr();
    newGuess    = new GuessExpr();
    eI_sum      = new Real();
    origValue   = new Real();
    origValueI  = new Real();

    // Temporary objects to avoid garbage production during calculations
    tmp1 = new Real();
    tmp2 = new Real();
    tmp3 = new Real();
    tmp4 = new Real();
    tmp5 = new Real();
    tmp6 = new Real();
  }

  // Check if new guess is more probable than what we've already got
  private void updateGuess(int a, int b) {
    Real tmp = tmp6;

    if (b == 0 || (b == 1 && (a < minInteger)))
      return;

    newGuess.a = a;
    newGuess.b = b;
    newGuess.calcGuessedValue();  // calculate newGuess.value
    newGuess.calcInformation();   // calculate newGuess.eI

    eI_sum.add(newGuess.eI);      // Accumulate total information

    // Check for duplicate representations, such as 2 and sqrt(4)
    boolean prevRounding = Real.magicRounding;
    Real.magicRounding = true;
    tmp.assign(firstGuess.value);
    tmp.sub(newGuess.value);// Subtract with magic rounding ==0 if <4 bits diff
    Real.magicRounding = prevRounding;

    if (tmp.isZero()) {
      // We have a guess with duplicate representation
      if (newGuess.isBetterThan(firstGuess)) {
        // Keeping the most probable representation
        newGuess.eI.add(firstGuess.eI);// Add together for increased confidence
        firstGuess.copy(newGuess);
      } else {
        firstGuess.eI.add(newGuess.eI);// Add together for increased confidence
      }
    }
    else if (newGuess.isBetterThan(firstGuess))
    {
      // We have a better guess
      secondGuess.copy(firstGuess);
      firstGuess.copy(newGuess);
    }
    else if (newGuess.isBetterThan(secondGuess))
    {
      // We have a better second guess
      secondGuess.copy(newGuess);
    }
  }

  // Find the fraction most closely matching the input
  private void findFraction(Real d) {
    int a1=0, b1=1, a2=1, b2=0;
    int n, t;
    Real d1 = tmp3, d2 = tmp4, t2;
    boolean done = false;

    if (d.isZero() || !d.isFinite())
      return;
    numberOfTries++;
    newGuess.sign = d.isNegative() ? 1 : 0;
    tmp2.assign(d);
    tmp2.abs();

    // Using accelerated Stern-Brocot tree traversal
    do
    {
      //d1 = fabs(a1-d*b1);
      //d2 = fabs(a2-d*b2);
      d1.assign(tmp2); d1.mul(b1); d1.neg(); d1.add(a1); d1.abs();
      d2.assign(tmp2); d2.mul(b2); d2.neg(); d2.add(a2); d2.abs();

      if (d1.isZero() || d2.isZero()) return;
      if (d2.lessThan(d1)) {
        t = a1; a1 = a2; a2 = t;
        t = b1; b1 = b2; b2 = t;
        t2= d1; d1 = d2; d2 = t2;
      }

      // Calculate how many Stern-Brocot mediants to skip
      // n = (int)ceil(d2/d1);
      d2.div(d1);
      d2.ceil();
      n = d2.toInteger(); // OK also if overflow

      // Limit numerator and denominator to maxAB
      if (b1!=0 && n>(t=(maxAB-b2)/b1)) { n=t; if (n==0) return; done=true; }
      if (a1!=0 && n>(t=(maxAB-a2)/a1)) { n=t; if (n==0) return; done=true; }

      // Calculate new mediants on either side of "d"
      a2 = (n-1)*a1+a2; a1 += a2;
      b2 = (n-1)*b1+b2; b1 += b2;

      // See if any of the new fractions makes the guess better
      updateGuess(a1,b1);
      updateGuess(a2,b2);
    }
    while (!done);
  }

  // Guess a fraction or a simple expression for a given input
  //   maxAB = maximum numerator or denominator
  public void doGuess(Real d, Real di, int maxAB) {
    int i;

    // Expression      Entropy penalty
    // -------------------------------
    // whenever b!=1         +1    (Achieved by 2*b-1 in information formula)
    // for every "-" sign    +1
    // ±a/b                   0
    // ±sqrt(a/b)             2
    // ±cbrt(a/b)             4
    // ±a*pi/b                2
    // ±a*pi^2/b              3
    // ±a/(b*pi)              3
    // ±a/(b*pi^2)            4
    // ±e^(±a/b)              3
    // ±2^(±a/b)              4
    // ln(a/b)                3
    // log2(a/b)              4

    firstGuess.init();
    secondGuess.init();
    firstGuess.eI.assign(Real.ONE);
    firstGuess.eI.scalbn(-64);
    eI_sum.assign(firstGuess.eI);
    this.maxAB = maxAB;
    minInteger = 1;
    numberOfTries = 0;
    origValue.assign(d);
    origValueI.assign(di != null ? di : Real.ZERO);

    if (d.isZero() || !d.isFinite())
      return;

    // Guess a/b
    newGuess.op = GuessExpr.FRACTION;
    tmp1.assign(1);
    for (i=1; i<=3; i++) {
      tmp1.mul(d);
      newGuess.power = i;
      entropyPenalty = 2*(i-1)+d.sign;
      minInteger = i>1 ? 2 : 1;
      tmp2.assign(tmp1);
      tmp2.copysign(d);
      findFraction(tmp2);
    }
    minInteger = 1;

    // Guess a*pi^n/b
    newGuess.op = GuessExpr.MULT_PI;
    tmp1.assign(d);
    for (i=1; i<=2; i++) {
      tmp1.mul(Real.PI);
      newGuess.power = -i;
      entropyPenalty = i+1+d.sign;
      findFraction(tmp1);
    }
    // Guess a/(b*pi^n)
    tmp1.assign(d);
    for (i=1; i<=2; i++) {
      tmp1.div(Real.PI);
      newGuess.power = i;
      entropyPenalty = i+2+d.sign;
      findFraction(tmp1);
    }

    // Guess e^(a/b)
    newGuess.op = GuessExpr.EXP;
    newGuess.power = d.sign;
    tmp1.assign(d);
    tmp1.abs();
    tmp1.ln();
    entropyPenalty = 3+newGuess.power+tmp1.sign;
    findFraction(tmp1);

    // Guess 2^(a/b)
    newGuess.op = GuessExpr.EXP2;
    newGuess.power = d.sign;
    tmp1.assign(d);
    tmp1.abs();
    tmp1.log2();
    entropyPenalty = 4+newGuess.power+tmp1.sign;
    findFraction(tmp1);

    // Guess ln(a/b)
    newGuess.op = GuessExpr.LN;
    newGuess.power = 1;
    entropyPenalty = 3;
    minInteger = 2;
    tmp1.assign(d);
    tmp1.exp();
    findFraction(tmp1);

    // Guess log2(a/b)
    newGuess.op = GuessExpr.LOG2;
    newGuess.power = 1;
    entropyPenalty = 4;
    minInteger = 3;
    tmp1.assign(d);
    tmp1.exp2();
    findFraction(tmp1);
  }

//   private static boolean guess_complex(Complex arg,char *result) {
//     Complex_gon arg_gon;
//     char *text;
//     Int32 upper,lower;
//     Int16 power;
//     boolean found=false;
//     double tmp;

//     if (!finite(arg.real) || !finite(arg.imag))
//       return false;

//     if (fabs(arg.real)>100000 || fabs(arg.imag)>100000)
//       return false;

//     arg_gon = cplx_to_gon(arg);

//     if (is_int(arg_gon.r))
//       StrPrintF(result,"%d",(int)round(arg_gon.r));
//     else if (guess_div(arg_gon.r,&upper,&lower,MAX_DIV_F)) {
//       StrPrintF(result,"(%ld/%ld)",upper,lower);
//       found = true;
//     }
//     else if (guess_pow1(arg_gon.r,&upper,&lower,&power) &&
//              upper && lower) {
//       convert_pow1(result,arg_gon.r<0.0,upper,lower,power);
//     }
//     else {
//       text = display_real(arg_gon.r);
//       StrCopy(result,text);
//       MemPtrFree(text);
//     }

//     tmp = arg_gon.angle/M_PI;
//     if (is_int(tmp) && (int)round(tmp)!=0) {
//       StrPrintF(result+StrLen(result),"e^(%ldpi*i)",(Int32)round(tmp));
//       found = true;
//     }
//     else if (guess_div(tmp,&upper,&lower,MAX_DIV_F) && upper) {
//       found = true;
//       StrCat(result,"e^(");
//       if (upper<0) {
//         StrCat(result,"-");
//         upper = -upper;
//       }
//       if (upper==1)
//         StrPrintF(result+StrLen(result),"pi*i/%ld",lower);
//       else
//         StrPrintF(result+StrLen(result),"%ldpi*i/%ld",upper,lower);
//       StrCat(result,")");
//     }
//     return found;
//   }

  public String guess(Real d, Real di) {
    if (di != null && !di.isZero())
      return "This is a quite complex number.";

    if (d.isZero())
      return "This is zero.";

    if (d.isNan())
      return "This is Not A Number.";

    if (d.isInfinity())
      return "This is "+(d.isNegative()?"negative ":"")+"infinity.";

    doGuess(d,di,2147483647);
    d.assign(firstGuess.value);
    firstGuess.calcProbability(tmp2);

    tmp3.assign("99.95");
    if (tmp2.greaterEqual(tmp3))
      return "This is definitely "+firstGuess.toString()+".";

    tmp3.assign("98");
    if (tmp2.greaterEqual(tmp3))
      return "This is probably "+firstGuess.toString()+
        ",  but there is a tiny chance it could be "+
        secondGuess.toString()+".";

    tmp3.assign("80");
    if (tmp2.greaterEqual(tmp3))
      return "This looks like "+firstGuess.toString()+
        ",  but it also looks a bit like "+secondGuess.toString()+".";

    tmp3.assign("50");
    if (tmp2.greaterEqual(tmp3))
      return "This looks somewhat like "+firstGuess.toString()+
        ",  but it could also be "+secondGuess.toString()+".";

    return "This could be "+firstGuess.toString()+
      ",  but it might as well be "+secondGuess.toString()+".";
  }
}
