public final class Calc 
{
  // basic keys -> 0-9 . -/E
  // menu keys  -> CLR  ENTER  last  menu  exit
  //
  // Menu:
  //   binop    ->            +      -      *      /
  //   math     -> elem    -> 1/x    -x     x^2    x^1/2
  //            -> power   -> y^x    y^1/x  ln     e^x
  //            -> pow10/2 -> log    10^x   log2   2^x
  //            -> combin  -> nPr    nCr    x!     gamma
  //            -> polar   -> r->p   p->r   atan2  hypot
  //   trig     -> normal  -> sin    cos    tan
  //            -> arc     -> asin   acos   atan
  //            -> hyp     -> sinh   cosh   tanh
  //            -> archyp  -> asinh  acosh  atanh
  //            -> pi
  //   bitop*   ->            and    or     xor    bic
  //   bitmath* ->            not    neg    y<<x   y>>x
  //   special  -> stack   -> x<->y  clear  x<->#  RCL#  ( -> # )
  //            -> integer -> round  ceil   floor  frac
  //            -> memory  -> STO#   STO+#  RCL#           -> #
  //            -> stat    -> SUM+   SUM-   ...
  //            -> user...
  //   mode     -> number  -> normal FIX#   SCI#   ENG#  ( -> # )
  //            -> base    -> dec    hex    oct    bin
  //            -> trig    -> deg    rad
  //            -> font    -> small  medium large
  //            -> prog
  //
  // * replaces math/trig in hex/oct/bin mode
  //
  // What about:
  //               modulo  -> mod    rem
  //               time    -> ->HMS  HMS->  HMS+   HMS-

  public static void main(String[] args) {
    Real a = new Real();
    System.out.println(a);
  }
}
