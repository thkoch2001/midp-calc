// basic keys -> 0-9 . -/E
// menu keys  -> CLR  ENTER  last  menu  exit
//
// Menu:
//   binop    ->            +      -      *      /
//   math     -> elem    -> 1/x    -x     x^2    x^1/2
//            -> power   -> y^x    y^1/x  ln     e^x
//            -> pow10/2 -> log    10^x   log2   2^x
//            -> combin  -> Py,x   Cy,x   x!     gamma
//            -> polar   -> r->p   p->r   atan2  hypot
//   trig     -> normal  -> sin    cos    tan
//            -> arc     -> asin   acos   atan
//            -> hyp     -> sinh   cosh   tanh
//            -> archyp  -> asinh  acosh  atanh
//            -> pi
//   bitop*   ->            and    or     xor    bic
//   bitmath* ->            not    neg    y<<x   y>>x
//   special  -> stack   -> x<->y  clear  x<->#  RCL#  LASTx  ( -> # )
//            -> integer -> round  ceil   floor  frac
//            -> memory  -> STO#   STO+#  RCL#           -> #
//            -> stat    -> SUM+   SUM-   clear
//                          avg    s      L.R.   y,r
//                          n      SUMx   SUMx²  SUMy  SUMy²  SUMxy
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
//               time    -> ->HMS  ->H
//                          ->RAD  ->DEG
//               random

import java.io.*;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public final class Calc
    extends MIDlet
{
  private Display display;
  private CalcCanvas screen;
  
  //private PropertyStore propertyStore;
  //private static final byte PROPERTY_PERMANENT_ID = 10;
  //private static final byte PROPERTY_TEMPORARY_ID = 11;

  public Calc()
  {
    display = Display.getDisplay(this);
    screen = new CalcCanvas(this);

    //propertyStore = PropertyStore.open("CalcData");
    //if (propertyStore == null) {
    //  // Fatal error
    //  System.out.println("Cannot read application properties");
    //}
  }
  
  public void startApp()
  {
    display.setCurrent(screen);
  }

  public void pauseApp()
  {
  }

  public void destroyApp(boolean unconditional)
  {
  }
    
  public void exitRequested()
  {
    destroyApp(false);
    notifyDestroyed();
  }

}
