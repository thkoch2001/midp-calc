// Symbols needed:
//   sqrt -> <-> SUM E pi gamma
package ral;

import javax.microedition.lcdui.*;

public class CalcCanvas
    extends Canvas
    implements CommandListener
{
// Menu:
//   binop    ->            -      *      /      +/-
//   math     -> elem    -> abs    1/x    x^2    x^1/2
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
//   bitmath* ->            not    y<<x   y>>x
//   special  -> stack   -> x<->y  clear  x<->#  RCL#  LASTx  ( -> # )
//            -> integer -> round  ceil   floor  trunc frac
//            -> memory  -> STO#   STO+#  RCL#           -> #
//            -> stat    -> SUM+   SUM-   clear
//                          avg    s      L.R.   y,r
//                          n      SUMx   SUMx²  SUMy  SUMy²  SUMxy
//            -> exit
//   mode     -> number  -> normal FIX#   SCI#   ENG#  ( -> # )
//            -> sep     -> decimal  ->   dot comma remove keep
//                          thousand ->   dot/comma space none
//            -> base    -> dec    hex    oct    bin
//            -> trig    -> deg    rad
//
// * replaces math/trig in hex/oct/bin mode
//
// Extensions:
//   math     -> modulo  -> mod    rem
//            -> percent -> %      %DIFF
//            -> time    -> ->HMS  ->H
//                          ->RAD  ->DEG
//            -> random
//   special  -> user...
//   mode     -> font    -> small  medium large
//            -> prog

  private static class Menu
  {
    public String name;
    public int command;
    public boolean numberRequired;
    public Menu [] subMenu;

    Menu(String n, int c) {
      name=n;
      command=c;
    }
    Menu(String n, int c, boolean r) {
      name=n;
      command=c;
      numberRequired = r;
    }
    Menu(String n, Menu [] m) {
      name = n;
      subMenu = m;
    }
  }
  
  private static final Menu menu = new Menu(null,new Menu[] {
    new Menu("binop",new Menu[] {
      new Menu("-",CalcEngine.SUB),
      new Menu("*",CalcEngine.MUL),
      new Menu("/",CalcEngine.DIV),
      new Menu("+/-",CalcEngine.NEG),
    }),
    new Menu("math",new Menu[] {
      new Menu("elem",new Menu[] {
        new Menu("abs",CalcEngine.ABS),
        new Menu("1/x",CalcEngine.RECIP),
        new Menu("x^2",CalcEngine.SQR),
        new Menu("sqrt",CalcEngine.SQRT),
      }),
      new Menu("power",new Menu[] {
        new Menu("y^x",CalcEngine.YPOWX),
        new Menu("y^1/x",CalcEngine.XRTY),
        new Menu("ln",CalcEngine.LN),
        new Menu("e^x",CalcEngine.EXP),
      }),
      new Menu("pow10/2",new Menu[] {
        new Menu("log",CalcEngine.LOG10),
        new Menu("10^x",CalcEngine.EXP10),
        new Menu("log2",CalcEngine.LOG2),
        new Menu("2^x",CalcEngine.EXP2),
      }),
      new Menu("combin",new Menu[] {
        new Menu("Py,x",CalcEngine.PYX),
        new Menu("Cy,x",CalcEngine.CYX),
        new Menu("x!",CalcEngine.FACT),
        new Menu("gamma",CalcEngine.GAMMA),
      }),
      new Menu("polar",new Menu[] {
        new Menu("r->p",CalcEngine.RP),
        new Menu("p->r",CalcEngine.PR),
        new Menu("atan2",CalcEngine.ATAN2),
        new Menu("hypot",CalcEngine.HYPOT),
      }),
    }),
    new Menu("trig",new Menu[] {
      new Menu("normal",new Menu[] {
        new Menu("sin",CalcEngine.SIN),
        new Menu("cos",CalcEngine.COS),
        new Menu("tan",CalcEngine.TAN),
      }),
      new Menu("arc",new Menu[] {
        new Menu("asin",CalcEngine.ASIN),
        new Menu("acos",CalcEngine.ACOS),
        new Menu("atan",CalcEngine.ATAN),
      }),
      new Menu("hyp",new Menu[] {
        new Menu("sinh",CalcEngine.SINH),
        new Menu("cosh",CalcEngine.COSH),
        new Menu("tanh",CalcEngine.TANH),
      }),
      new Menu("archyp",new Menu[] {
        new Menu("asinh",CalcEngine.ASINH),
        new Menu("acosh",CalcEngine.ACOSH),
        new Menu("atanh",CalcEngine.ATANH),
      }),
    }),
    new Menu("special",new Menu [] {
      new Menu("stack",new Menu[] {
        new Menu("x<->y",CalcEngine.XCHG),
        new Menu("clear",CalcEngine.CLS),
        new Menu("x<->#",CalcEngine.XCHGN,true),
        new Menu("RCL#",CalcEngine.RCLN,true),
        new Menu("LASTx",CalcEngine.LASTX),
      }),
      new Menu("integer",new Menu[] {
        new Menu("round",CalcEngine.ROUND),
        new Menu("ceil",CalcEngine.CEIL),
        new Menu("floor",CalcEngine.FLOOR),
        new Menu("trunc",CalcEngine.TRUNC),
        new Menu("frac",CalcEngine.FRAC),
      }),
      new Menu("memory",new Menu[] {
        new Menu("STO",CalcEngine.STO,true),
        new Menu("STO+",CalcEngine.STP,true),
        new Menu("RCL",CalcEngine.RCL,true),
      }),
      new Menu("stat",new Menu[] {
        new Menu("SUM+",CalcEngine.SUMPL),
        new Menu("SUM-",CalcEngine.SUMMI),
        new Menu("clear",CalcEngine.CLST),
        new Menu("result",new Menu[] {
          new Menu("avg",CalcEngine.AVG),
          new Menu("s",CalcEngine.S),
          new Menu("L.R.",CalcEngine.LR),
          new Menu("y,r",CalcEngine.YR),
          new Menu("n",CalcEngine.N),
        }),
        new Menu("regs",new Menu[] {
          new Menu("SUMx",CalcEngine.SUMX),
          new Menu("SUMx^2",CalcEngine.SUMXX),
          new Menu("SUMy",CalcEngine.SUMY),
          new Menu("SUMY^2",CalcEngine.SUMYY),
          new Menu("SUMxy",CalcEngine.SUMXY),
        }),
      }),
      new Menu("exit",-999),
    }),
    new Menu("mode",new Menu[] {
      new Menu("number",new Menu[] {
        new Menu("normal",CalcEngine.NORM),
        new Menu("FIX",CalcEngine.FIX,true),
        new Menu("SCI",CalcEngine.SCI,true),
        new Menu("ENG",CalcEngine.ENG,true),
      }),
      new Menu("sep",new Menu[] {
        new Menu("decimal",new Menu[] {
          new Menu(".",CalcEngine.POINT_DOT),
          new Menu(",",CalcEngine.POINT_COMMA),
          new Menu("remove",CalcEngine.POINT_REMOVE),
          new Menu("keep",CalcEngine.POINT_KEEP),
        }),
        new Menu("thousand",new Menu[] {
          new Menu("./,",CalcEngine.THOUSAND_DOT),
          new Menu("\" \"",CalcEngine.THOUSAND_SPACE),
          new Menu("none",CalcEngine.THOUSAND_NONE),
        }),
      }),
      new Menu("base",new Menu[] {
        new Menu("DEC",CalcEngine.BASE_DEC),
        new Menu("HEX",CalcEngine.BASE_HEX),
        new Menu("OCT",CalcEngine.BASE_OCT),
        new Menu("BIN",CalcEngine.BASE_BIN),
      }),
      new Menu("trig",new Menu[] {
        new Menu("DEG",CalcEngine.TRIG_DEG),
        new Menu("RAD",CalcEngine.TRIG_RAD),
      }),
    }),
  });
  
  private static final Menu numberMenu = new Menu(null,new Menu[] {
    new Menu("0-3",new Menu[] {
      new Menu("0",0),
      new Menu("1",1),
      new Menu("2",2),
      new Menu("3",3),
    }),
    new Menu("4-7",new Menu[] {
      new Menu("4",4),
      new Menu("5",5),
      new Menu("6",6),
      new Menu("7",7),
    }),
    new Menu("8-11",new Menu[] {
      new Menu("8",8),
      new Menu("9",9),
      new Menu("10",10),
      new Menu("11",11),
    }),
    new Menu("12-15",new Menu[] {
      new Menu("12",12),
      new Menu("13",13),
      new Menu("14",14),
      new Menu("15",15),
    }),
  });

  private GFont mediumFont,smallFont,largeFont;
  private GFont currentFont;
  private CalcEngine calc;

  private final Command add;
  private final Command enter;

  private final Calc midlet;

  private int numRepaintLines = 0;
  private boolean repeating = false;
  private boolean internalRepaint = false;
  private int offX, offY, width, height, charWidth, charHeight;

  public CalcCanvas(Calc m) {
    midlet = m;

    mediumFont = new GFont(GFont.MEDIUM);
    smallFont  = new GFont(GFont.SMALL);
    largeFont  = new GFont(GFont.LARGE);
    currentFont = mediumFont;

    enter = new Command("ENTER" , Command.OK  , 1);
    add   = new Command("+"     , Command.ITEM, 1);
    addCommand(enter);
    addCommand(add);
    setCommandListener(this);

    charWidth = currentFont.charWidth();
    width = getWidth()/charWidth;
    offX = (getWidth()-width*charWidth)/2;
    charHeight = currentFont.getHeight();
    height = getHeight()/charHeight;
    offY = (getHeight()-height*charHeight)/2;

    calc = new CalcEngine();
    calc.setMaxWidth(width);
    numRepaintLines = 100;
  }

  public void paint(Graphics g) {
    boolean cleared = false;
    if (numRepaintLines == 100 || !internalRepaint) {
      g.setColor(0);
      g.fillRect(0,0,getWidth(),getHeight());
      cleared = true;
      numRepaintLines = 100;
    }
    internalRepaint = false;

    if (numRepaintLines > 0) {
      if (numRepaintLines > height)
        numRepaintLines = height;

      for (int i=0; i<numRepaintLines; i++) {
        if (i==0 && calc.inputIsInProgress()) {
          StringBuffer tmp = calc.getInputBuffer();
          tmp.append('_');
          if (tmp.length()>width)
            currentFont.drawString(g,offX,offY+(height-1)*charHeight,tmp,
                                   tmp.length()-width);
          else {
            currentFont.drawString(g,offX,offY+(height-1)*charHeight,tmp,0);
            if (!cleared) {
              g.setColor(0);
              g.fillRect(offX+tmp.length()*charWidth,
                         offY+(height-1)*charHeight,
                         (width-tmp.length())*charWidth,charHeight);
            }
          }
          tmp.setLength(tmp.length()-1);
        } else {
          String tmp = calc.getStackElement(i);
          if (tmp.length()>width)
            tmp = "*****";
          currentFont.drawString(
            g,offX+(width-tmp.length())*charWidth,
            offY+(height-1-i)*charHeight,tmp);
          if (!cleared) {
            g.setColor(0);
            g.fillRect(offX,offY+(height-1-i)*charHeight,
                       (width-tmp.length())*charWidth,charHeight);
          }
        }
      }
    }
    numRepaintLines = 0;
  }

  private void checkRepaint() {
    int repaintLines = calc.numRepaintLines();
    if (repaintLines > numRepaintLines)
      numRepaintLines = repaintLines;
    if (numRepaintLines > 0) {
      internalRepaint = true;
      repaint();
    }
  }

  protected void keyPressed(int key) {
    repeating = false;
    switch (key) {
      case '0': case '1': case '2': case '3': case '4':
      case '5': case '6': case '7': case '8': case '9':
        calc.command(CalcEngine.DIGIT_0+key-'0',0);
        break;
      case '#':
        calc.command(CalcEngine.SIGN_E,0);
        break;
      case '*':
        calc.command(CalcEngine.DEC_POINT,0);
        break;
      case -8:
        calc.command(CalcEngine.CLEAR,0);
        break;
      case -1: // UP
      case -2: // DOWN
      case -3: // LEFT
      case -4: // RIGHT
      case -5: // PUSH
        // Menu-stuff
        break;
    }
    checkRepaint();
  }

  protected void keyRepeated(int key) {
    switch (key) {
      case '1': case '2': case '3': case '4': case '5': case '6':
        if (repeating)
          return;
        calc.command(CalcEngine.DIGIT_A+key-'1',0);
        break;
      case -8:
        calc.command(CalcEngine.CLEAR,0);
        break;
    }
    repeating = true;
    checkRepaint();
  }

  public void commandAction(Command c, Displayable d)
  {
    if (c == enter)
      calc.command(CalcEngine.ENTER,0);
    if (c == add)
      calc.command(CalcEngine.ADD,0);
    checkRepaint();
  }

}
