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
//   math     -> basic   -> abs    1/x    x^2    x^1/2
//            -> power   -> y^x    y^1/x  ln     e^x
//            -> comb    -> Py,x   Cy,x   x!     gamma
//            -> pow10/2 -> log    10^x   log2   2^x
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
//                          n      SUMx   SUMx�  SUMy  SUMy�  SUMxy
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
//                       -> time   date
//            -> random
//   trig     -> ->RAD  ->DEG
//   special  -> user...
//   mode     -> font    -> small  medium large
//            -> prog

  private static class Menu
  {
    public String label;
    public int command;
    public boolean numberRequired;
    public Menu [] subMenu;

    Menu(String l, int c) {
      label = l;
      command = c;
    }
    Menu(String l, int c, boolean r) {
      label = l;
      command = c;
      numberRequired = r;
    }
    Menu(String l, Menu [] m) {
      label = l;
      subMenu = m;
    }
  }

  private static final int EXIT = -999;
  private static final int NUMBER_0 = -20+0;
  private static final int NUMBER_1 = -20+1;
  private static final int NUMBER_2 = -20+2;
  private static final int NUMBER_3 = -20+3;
  private static final int NUMBER_4 = -20+4;
  private static final int NUMBER_5 = -20+5;
  private static final int NUMBER_6 = -20+6;
  private static final int NUMBER_7 = -20+7;
  private static final int NUMBER_8 = -20+8;
  private static final int NUMBER_9 = -20+9;
  private static final int NUMBER_10 = -20+10;
  private static final int NUMBER_11 = -20+11;
  private static final int NUMBER_12 = -20+12;
  private static final int NUMBER_13 = -20+13;
  private static final int NUMBER_14 = -20+14;
  private static final int NUMBER_15 = -20+15;
  
  private static final Menu menu = new Menu(null,new Menu[] {
    new Menu("basic",new Menu[] {
      new Menu("-",CalcEngine.SUB),
      new Menu("*",CalcEngine.MUL),
      new Menu("/",CalcEngine.DIV),
      new Menu("+/-",CalcEngine.NEG),
    }),
    null,
    null,
    new Menu("special",new Menu [] {
      new Menu("stack",new Menu[] {
        new Menu("LAST x",CalcEngine.LASTX),
        new Menu("x=y",CalcEngine.XCHG),
        new Menu("x=st#",CalcEngine.XCHGST,true),
        new Menu("RCL st#",CalcEngine.RCLST,true),
        new Menu("clear",CalcEngine.CLS),
      }),
      new Menu("int",new Menu[] {
        new Menu("round",CalcEngine.ROUND),
        new Menu("ceil",CalcEngine.CEIL),
        new Menu("floor",CalcEngine.FLOOR),
        new Menu("trunc",CalcEngine.TRUNC),
        new Menu("frac",CalcEngine.FRAC),
      }),
      new Menu("mem",new Menu[] {
        new Menu("STO",CalcEngine.STO,true),
        new Menu("STO+",CalcEngine.STP,true),
        new Menu("RCL",CalcEngine.RCL,true),
        new Menu("x=mem",CalcEngine.XCHGMEM,true),
      }),
      new Menu("stat",new Menu[] {
        new Menu("Z+",CalcEngine.SUMPL),
        new Menu("Z-",CalcEngine.SUMMI),
        new Menu("clear",CalcEngine.CLST),
        new Menu("result",new Menu[] {
          new Menu("~x~, ~y~",CalcEngine.AVG),
          new Menu("s_x_, s_y_",CalcEngine.S),
          new Menu("L.R.",CalcEngine.LR),
          new Menu("y^*^, r",CalcEngine.YR),
          new Menu("n",CalcEngine.N),
        }),
        new Menu("regs",new Menu[] {
          new Menu("Zx",CalcEngine.SUMX),
          new Menu("Zx^2",CalcEngine.SUMXX),
          new Menu("Zy",CalcEngine.SUMY),
          new Menu("Zy^2",CalcEngine.SUMYY),
          new Menu("Zxy",CalcEngine.SUMXY),
        }),
      }),
      //new Menu("exit",-999),
    }),
    new Menu("mode",new Menu[] {
      new Menu("format",new Menu[] {
        new Menu("normal",CalcEngine.NORM),
        new Menu("FIX",CalcEngine.FIX,true),
        new Menu("SCI",CalcEngine.SCI,true),
        new Menu("ENG",CalcEngine.ENG,true),
      }),
      new Menu("number",new Menu[] {
        new Menu("point",new Menu[] {
          new Menu(".",CalcEngine.POINT_DOT),
          new Menu(",",CalcEngine.POINT_COMMA),
          new Menu("keep",CalcEngine.POINT_KEEP),
          new Menu("remove",CalcEngine.POINT_REMOVE),
        }),
        null,
        null,
        new Menu("thousand",new Menu[] {
          new Menu(". or ,",CalcEngine.THOUSAND_DOT),
          new Menu("space",CalcEngine.THOUSAND_SPACE),
          new Menu("none",CalcEngine.THOUSAND_NONE),
        }),
      }),
      new Menu("trig",new Menu[] {
        new Menu("DEG",CalcEngine.TRIG_DEG),
        null,
        null,
        new Menu("RAD",CalcEngine.TRIG_RAD),
      }),
      new Menu("base",new Menu[] {
        new Menu("DEC",CalcEngine.BASE_DEC),
        new Menu("HEX",CalcEngine.BASE_HEX),
        new Menu("OCT",CalcEngine.BASE_OCT),
        new Menu("BIN",CalcEngine.BASE_BIN),
      }),
    }),
  });
  
  private static final Menu numberMenu = new Menu(null,new Menu[] {
    new Menu("0-3",new Menu[] {
      new Menu("0",NUMBER_0),
      new Menu("1",NUMBER_1),
      new Menu("2",NUMBER_2),
      new Menu("3",NUMBER_3),
    }),
    new Menu("4-7",new Menu[] {
      new Menu("4",NUMBER_4),
      new Menu("5",NUMBER_5),
      new Menu("6",NUMBER_6),
      new Menu("7",NUMBER_7),
    }),
    new Menu("8-11",new Menu[] {
      new Menu("8",NUMBER_8),
      new Menu("9",NUMBER_9),
      new Menu("10",NUMBER_10),
      new Menu("11",NUMBER_11),
    }),
    new Menu("12-15",new Menu[] {
      new Menu("12",NUMBER_12),
      new Menu("13",NUMBER_13),
      new Menu("14",NUMBER_14),
      new Menu("15",NUMBER_15),
    }),
  });

  private static final Menu math = new Menu("math",new Menu[] {
    new Menu("simple",new Menu[] {
      new Menu("abs",CalcEngine.ABS),
      new Menu("x^2",CalcEngine.SQR),
      new Menu("1/x",CalcEngine.RECIP),
      new Menu("Qx",CalcEngine.SQRT),
    }),
    new Menu("pow",new Menu[] {
      new Menu("e^x",CalcEngine.EXP),
      new Menu("y^x",CalcEngine.YPOWX),
      new Menu("ln",CalcEngine.LN),
      new Menu("^x^Qy",CalcEngine.XRTY),
    }),
    new Menu("pol",new Menu[] {
      new Menu("atan_2",CalcEngine.ATAN2),
      new Menu("r\\p",CalcEngine.RP),
      new Menu("p\\r",CalcEngine.PR),
      new Menu("hypot",CalcEngine.HYPOT),
    }),
    new Menu("pow_10,2",new Menu[] {
      new Menu("2^x",CalcEngine.EXP2),
      new Menu("10^x",CalcEngine.EXP10),
      new Menu("log_2",CalcEngine.LOG2),
      new Menu("log_10",CalcEngine.LOG10),
    }),
    new Menu("comb",new Menu[] {
      new Menu("P y,x",CalcEngine.PYX),
      new Menu("C y,x",CalcEngine.CYX),
      new Menu("x!",CalcEngine.FACT),
      new Menu("$x",CalcEngine.GAMMA),
    }),
  });
  private static final Menu trig = new Menu("trig",new Menu[] {
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
    new Menu("�",CalcEngine.PI),
  });
  private static final Menu bitOp = new Menu("bitop",new Menu[] {
    new Menu("and",CalcEngine.AND),
    new Menu("or",CalcEngine.OR),
    new Menu("bic",CalcEngine.BIC),
    new Menu("xor",CalcEngine.XOR),
  });
  private static final Menu bitMath = new Menu("bitop_2",new Menu[] {
    new Menu("NOT",CalcEngine.NOT),
    new Menu("y<<x",CalcEngine.YUPX),
    new Menu("y>>x",CalcEngine.YDNX),
  });

  private static final int menuColor [] = {
    0x00e0e0,0x00fc00,0xe0e000,0xfca800,0xfc5400
  };

  private GFont mediumFont,smallFont,largeFont;
  private GFont currentFont;
  private Font menuFont;
  private Font boldMenuFont;
  private Font smallMenuFont;
  private Font smallBoldMenuFont;
  private CalcEngine calc;

  private final Command add;
  private final Command enter;

  private final Calc midlet;

  private int numRepaintLines = 0;
  private boolean repeating = false;
  private boolean internalRepaint = false;
  private int offX, offY, width, height, charWidth, charHeight;

  private Menu [] menuStack;
  private int menuStackPtr;
  private int menuCommand;

  public CalcCanvas(Calc m) {
    midlet = m;

    mediumFont = new GFont(GFont.MEDIUM);
    smallFont  = new GFont(GFont.SMALL);
    largeFont  = new GFont(GFont.LARGE);
    currentFont = mediumFont;
    menuFont = Font.getFont(
      Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_MEDIUM);
    boldMenuFont = Font.getFont(
      Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_MEDIUM);
    smallMenuFont = Font.getFont(
      Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL);
    smallBoldMenuFont = Font.getFont(
      Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL);

    enter = new Command("ENTER", Command.OK    , 1);
    add   = new Command("+"    , Command.SCREEN, 1);
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

    menuStack = new Menu[5];
    menuStackPtr = -1;
  }

  private boolean plainLabel(String label) {
    for (int i=0; i<label.length(); i++)
      if ("^~_\\=QZ$�".indexOf(label.charAt(i))>=0)
        return false;
    return true;
  }

  private int labelWidth(String label, boolean bold) {
    Font normalFont = bold ? boldMenuFont : menuFont;
    Font smallFont = bold ? smallBoldMenuFont : smallMenuFont;
    if (plainLabel(label))
      return normalFont.stringWidth(label);
    int width = 0;
    Font font = normalFont;
    for (int i=0; i<label.length(); i++) {
      char c = label.charAt(i);
      if (c=='^' || c=='_')
        font = font==normalFont ? smallFont : normalFont;
      else if (c=='~')
        ; // overline... no font change
      else if ("\\=QZ$�".indexOf(c)>=0)
        width += font.charWidth('O');
      else
        width += font.charWidth(c);
    }
    return width;
  }

  private void drawLabel(Graphics g, String label, boolean bold, int x,int y) {
    Font normalFont = bold ? boldMenuFont : menuFont;
    Font smallFont = bold ? smallBoldMenuFont : smallMenuFont;
    Font font = normalFont;
    g.setFont(font);
    if (plainLabel(label)) {
      g.drawString(label,x,y,g.TOP|g.LEFT);
      return;
    }
    boolean sub=false,sup=false,overline=false;
    for (int i=0; i<label.length(); i++) {
      char c = label.charAt(i);
      if (c=='^' || c=='_') {
        font = font==normalFont ? smallFont : normalFont;
        g.setFont(font);
        sub = sup = false;
        if (font == smallFont) {
          sub = c=='_';
          sup = c=='^';
        }
      } else if (c=='~') {
        overline = !overline;
      } else if ("\\=QZ$�".indexOf(c)>=0) {
        int w = font.charWidth('O');
        int h = font.getBaselinePosition();
        switch (c) {
          case '\\':
            g.drawLine(x,y+h/2+1,x+w-2,y+h/2+1);
            g.drawLine(x,y+h/2+2,x+w-2,y+h/2+2);
            g.drawLine(x+w-2,y+h/2+1,x+w-2-2,y+h/2+1-2);
            g.drawLine(x+w-3,y+h/2+1,x+w-3-1,y+h/2+1-1);
            g.drawLine(x+w-2,y+h/2+2,x+w-2-2,y+h/2+2+2);
            g.drawLine(x+w-3,y+h/2+2,x+w-3-1,y+h/2+2+1);
            break;
          case '=':
            g.drawLine(x,y+h/2,x+w-2,y+h/2);
            g.drawLine(x+w-2,y+h/2,x+w-2-2,y+h/2-2);
            g.drawLine(x,y+h/2+3,x+w-2,y+h/2+3);
            g.drawLine(x,y+h/2+3,x+2,y+h/2+3+2);
            if (bold) {
              g.drawLine(x,y+h/2-1,x+w-2,y+h/2-1);
              g.drawLine(x+w-2,y+h/2-1,x+w-2-2,y+h/2-1-2);
              g.drawLine(x,y+h/2+2,x+w-2,y+h/2+2);
              g.drawLine(x,y+h/2+2,x+2,y+h/2+2+2);
            }
            break;
          case 'Q':
            g.drawLine(x,y+h-3,x+3,y+h);
            g.drawLine(x+1,y+h-3,x+4,y+h);
            g.drawLine(x+3,y,x+3,y+h);
            g.drawLine(x+4,y,x+4,y+h);
            g.drawLine(x+3,y,x+w,y);
            g.drawLine(x+3,y+1,x+w,y+1);
            overline = true;
            break;
          case 'Z':
            int b = (h&1)^1;
            int s = (h-b-4)/2;
            g.drawLine(x,y+b,x+w-2,y+b);
            g.drawLine(x,y+b+1,x+w-2,y+b+1);
            g.drawLine(x,y+b+2,x+s,y+b+2+s);
            g.drawLine(x+1,y+b+2,x+1+s,y+b+2+s);
            g.drawLine(x,y+h-3,x+s,y+b+2+s);
            g.drawLine(x+1,y+h-3,x+1+s,y+b+2+s);
            g.drawLine(x,y+h-1,x+w-2,y+h-1);
            g.drawLine(x,y+h-2,x+w-2,y+h-2);
            break;
          case '$':
            g.drawLine(x+1,y,x+1,y+h-1);
            g.drawLine(x+2,y,x+2,y+h-1);
            g.drawLine(x,y+h-1,x+3,y+h-1);
            g.drawLine(x,y,x+w-2,y);
            g.drawLine(x,y+1,x+w-2,y+1);
            g.drawLine(x+w-2,y,x+w-2,y+3);
            break;
          case '�':
            g.drawLine(x,y+h/3,x+7,y+h/3);
            g.drawLine(x+2,y+h/3,x+2,y+h-1);
            g.drawLine(x+3,y+h/3,x+3,y+h-1);
            g.drawLine(x,y+h/3,x,y+h/3+1);
            g.drawLine(x+5,y+h/3,x+5,y+h-1);
            g.drawLine(x+6,y+h/3,x+6,y+h-1);
            break;
        }
        x += w;
      }
      else {
        if (sub)
          g.drawChar(c,x,y+normalFont.getHeight()-
                     smallFont.getBaselinePosition(),g.TOP|g.LEFT);
        else if (sup)
          g.drawChar(c,x,y-1,g.TOP|g.LEFT);
        else
          g.drawChar(c,x,y,g.TOP|g.LEFT);
        if (overline) {
          g.drawLine(x-1,y,x+font.charWidth(c)-1,y);
          if (bold)
            g.drawLine(x-1,y+1,x+font.charWidth(c)-1,y+1);
        }
        x += font.charWidth(c);
      }
    }
  }

  private void drawMenuItem(Graphics g, Menu menu, int x, int y, int anchor) {
    if (menu==null)
      return;
    boolean bold = menu.subMenu==null && !menu.numberRequired;
    int width = labelWidth(menu.label,bold);
    if ((anchor & g.RIGHT) != 0)
      x -= width;
    else if ((anchor & g.HCENTER) != 0)
      x -= width/2;
    if ((anchor & g.BOTTOM) != 0)
      y -= menuFont.getHeight();
    drawLabel(g,menu.label,bold,x,y);
  }

  public void paint(Graphics g) {
    boolean cleared = false;
    if (numRepaintLines == 100 || !internalRepaint) {
      g.setColor(0);
      g.fillRect(0,0,getWidth(),getHeight());
      cleared = true;
      numRepaintLines = 100;
    }

    if (numRepaintLines > 0) {
      if (menuStackPtr < 0 || !internalRepaint) {
        if (numRepaintLines > height)
          numRepaintLines = height;

        for (int i=0; i<numRepaintLines; i++) {
          if (i==0 && calc.inputInProgress) {
            StringBuffer tmp = calc.inputBuf;
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
      if (menuStackPtr >= 0) {
        int x = getWidth()/12;
        int w = getWidth()-2*x;
        int y = getHeight()/4;
        int h = getHeight()-2*y;
        int fm = menuFont.getBaselinePosition()/2;
        g.setColor(menuColor[menuStackPtr]);
        g.fillRoundRect(x,y,w,h,21,21);
        g.setColor(menuColor[menuStackPtr]/2);
        g.drawRoundRect(x,y,w,h,21,21);
        g.setColor(0);
        Menu [] subMenu = menuStack[menuStackPtr].subMenu;
        if (subMenu.length>=1)
          drawMenuItem(g,subMenu[0],getWidth()/2,y+3,g.TOP|g.HCENTER);
        if (subMenu.length>=2)
          drawMenuItem(g,subMenu[1],x+3,getHeight()/2-fm,g.TOP|g.LEFT);
        if (subMenu.length>=3)
          drawMenuItem(g,subMenu[2],x+w-3,getHeight()/2-fm,g.TOP|g.RIGHT);
        if (subMenu.length>=4)
          drawMenuItem(g,subMenu[3],getWidth()/2,y+h-3,g.BOTTOM|g.HCENTER);
        if (subMenu.length>=5)
          drawMenuItem(g,subMenu[4],getWidth()/2,getHeight()/2-fm,
                       g.TOP|g.HCENTER);
        else {
          // Draw a small "joystick" in the center
          g.setColor(menuColor[menuStackPtr]/4*3);
          g.fillRect(getWidth()/2-1,getHeight()/2-10,3,20);
          g.fillRect(getWidth()/2-10,getHeight()/2-1,20,3);
          g.fillArc(getWidth()/2-5,getHeight()/2-5,10,10,0,360);
        }
      }
    }
    internalRepaint = false;
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
    int menuIndex = -1;
    switch (key) {
      case '0': case '1': case '2': case '3': case '4':
      case '5': case '6': case '7': case '8': case '9':
        if (menuStackPtr >= 0) {
          if (menuStack[menuStackPtr] == numberMenu ||
              (menuStackPtr>=1 && menuStack[menuStackPtr-1] == numberMenu))
          {
            calc.command(menuCommand,key-'0');
            menuStackPtr = -1;
            numRepaintLines = 100; // Force repaint
            break;
          } else {
            return;
          }
        }
        calc.command(CalcEngine.DIGIT_0+key-'0',0);
        break;
      case '#':
        if (menuStackPtr >= 0)
          return;
        calc.command(CalcEngine.SIGN_E,0);
        break;
      case '*':
        if (menuStackPtr >= 0)
          return;
        calc.command(CalcEngine.DEC_POINT,0);
        break;
      case -8:
        if (menuStackPtr >= 0) {
          menuStackPtr--;
          if (menuStackPtr >= 0)
            numRepaintLines = 1;
          else
            numRepaintLines = 100;
          break;
        }
        calc.command(CalcEngine.CLEAR,0);
        break;
      case -1: // UP
        menuIndex = 0;
        break;
      case -2: // DOWN
        menuIndex = 3;
        break;
      case -3: // LEFT
        menuIndex = 1;
        break;
      case -4: // RIGHT
        menuIndex = 2;
        break;
      case -5: // PUSH
        menuIndex = 4;
        break;
    }
    if (menuIndex >= 0) {
      if (menuStackPtr < 0) {
        if (calc.format.base == 10) {
          menu.subMenu[1] = math;
          menu.subMenu[2] = trig;
        } else {
          menu.subMenu[1] = bitOp;
          menu.subMenu[2] = bitMath;
        }
      }
      if (menuStackPtr < 0 && menuIndex < 4) {
        menuStack[0] = menu;
        menuStack[1] = menu.subMenu[menuIndex];
        menuStackPtr = 1;
        numRepaintLines = 1; // Force repaint
      } else if (menuStackPtr < 0) {
        menuStack[0] = menu;
        menuStackPtr = 0;
        numRepaintLines = 1; // Force repaint
      } else if (menuStack[menuStackPtr].subMenu.length > menuIndex) {
        Menu [] subMenu = menuStack[menuStackPtr].subMenu;
        if (subMenu[menuIndex] == null) {
          ; // NOP
        } else if (subMenu[menuIndex].subMenu != null) {
          menuStackPtr++;
          menuStack[menuStackPtr] = subMenu[menuIndex];
          numRepaintLines = 1; // Force repaint
        } else if (subMenu[menuIndex].numberRequired) {
          menuCommand = subMenu[menuIndex].command;
          menuStackPtr++;
          menuStack[menuStackPtr] = numberMenu;
          numRepaintLines = 1; // Force repaint
        } else {
          int command = subMenu[menuIndex].command;
          if (command == EXIT) {
            midlet.exitRequested();
            menuStackPtr = -1;
            numRepaintLines = 100; // Force repaint
          } else if (command >= NUMBER_0 && command <= NUMBER_15) {
            calc.command(menuCommand,command-NUMBER_0);
            menuStackPtr = -1;
            numRepaintLines = 100; // Force repaint
          } else {
            calc.command(command,0);
            menuStackPtr = -1;
            numRepaintLines = 100; // Force repaint
          }
        }
      }
    }
    checkRepaint();
  }

  protected void keyRepeated(int key) {
    switch (key) {
      case '1': case '2': case '3': case '4': case '5': case '6':
        if (repeating || menuStackPtr >= 0)
          return;
        calc.command(CalcEngine.DIGIT_A+key-'1',0);
        break;
      case -8:
        if (!calc.inputInProgress || menuStackPtr >= 0)
          return;
        calc.command(CalcEngine.CLEAR,0);
        break;
    }
    repeating = true;
    checkRepaint();
  }

  public void commandAction(Command c, Displayable d)
  {
    if (c == enter) {
      if (menuStackPtr >= 0)
        return;
      calc.command(CalcEngine.ENTER,0);
    } else if (c == add) {
      if (menuStackPtr >= 0)
        return;
      calc.command(CalcEngine.ADD,0);
    }
    checkRepaint();
  }

}
