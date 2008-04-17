package midpcalc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;

public final class CalcCanvas
    extends MyCanvas
    implements CommandListener, CanvasAccess
{
// Commands:
//               ENTER  +  0-9a-f  .  -/E  clear  menu
// Modes:
//               DEG/RAD   FIX/SCI/ENG   BIN/OCT/HEX   BGN   PRG/RUN
// Menu:
//   basic    -> -  *  /  +/-  (repeat)
//   math     -> simple  -> 1/x    x²     sqrt    %       %chg
//            -> pow     -> y^x    y^1/x  ln      e^x
//                       -> pow10/2 -> log10 10^x log2 2^x
//            -> prob    -> Py,x   Cy,x   x!      Gam(x)
//                       -> erfc   phi    inverfc invphi
//            -> misc    -> mod    div    random  factorize
//                       -> int -> round ceil floor trunc frac
//            -> matrix  -> new    concat stack   split
//                       -> more -> det A^T A*^T |A|_F Tr
//                               -> more -> size a_yx
//   trig     -> normal  -> sin    cos    tan
//            -> arc     -> asin   acos   atan
//            -> hyp     -> sinh   cosh   tanh
//            -> archyp  -> asinh  acosh  atanh
//            -> more    -> RD/DG/GR   ->RAD  ->DEG  pi
//                       -> coord   -> r->p   p->r   atan2   hypot   ->cplx
//                       -> cplx*   -> split  abs    arg     conj
//   bitop**  -> and     or     xor     bic
//   bitop2** -> not     y<<x   y>>x
//               int     -> round ceil floor trunc frac
//   special  -> stack   -> x<->y  clear  LASTx   undo
//                       -> more   -> rolldn rollup RCLst# x<->st#     ( -> # )
//            -> mem     -> STO#   STO+#  RCL#    x<->mem#               -> #
//            -> stat    -> SUM+   SUM-   clear
//                       -> result -> avg    -> x,y sx,sy dx,dy xw draw
//                                 -> ax+b   -> a,b x* y* r draw
//                                 -> alnx+b -> a,b x* y* r draw
//                                 -> be^ax  -> a,b x* y* r draw
//                                 -> bx^a   -> a,b x* y* r draw
//                       -> sums   -> n
//                                 -> x      -> SUMx SUMx² SUMlnx SUMln²x
//                                 -> y      -> SUMy SUMy² SUMlny SUMln²y
//                                 -> xy     -> SUMxy SUMxlny SUMylnx SUMlnxlny
//            -> finance -> STO RCL solve -> pv fv np pmt ir
//                       -> clear
//            -> conv    -> time   -> ->DH.MS  ->H  DH.MS+  now
//                                 -> more -> unix -> DH.MS->unix unix->DH.MS
//                                         -> JD   -> DH.MS->JD   JD->DH.MS
//                                         -> MJD  -> DH.MS->MJD  MJD->DH.MS
//                       -> metric -> length weight vol  energy temp
//                       -> const  -> univ   chem   phys atom   astro
//                       -> guess
//   mode     -> number  -> normal FIX#   SCI#   ENG#                  ( -> # )
//                       -> sepr   -> decimal  -> dot comma remove keep
//                                 -> thousand -> dot/comma space ' none
//            -> prog[1] -> run    -> #
//                       -> new    -> # -> name?
//                       -> append -> # 
//                       -> draw   -> y=f(x)      -> #
//                                 -> r=f(theta)  -> #
//                                 -> z=f(t)      -> #
//                       -> more   -> integrate   -> #
//                                 -> diff        -> #
//                                 -> solve       -> #
//                                 -> min/max     -> #
//                                 -> clear       -> #
//            -> prog[2] -> finish
//                       -> flow   -> cmp-y -> x=y? x!=y? x<y? x<=y? x>y?
//                                 -> cmp-x -> x=0? x!=0? x<0? x<=0? x>0?
//                                 -> label -> LBL GTO GSB               -> #
//                                             RTN STOP
//                                 -> loop  -> ISG DSE                   -> #
//                       -> util   -> abs max min sgn select
//                       -> reset
//                       -> mem    -> RCL[x] STO[x] STO+[x]
//            -> base    -> dec    hex    oct      bin
//            -> monitor -> mem    stat   finance  matrix  off (prog)  ( -> # )
//            -> sys     -> font   -> small  medium large  system
//                       -> exit
//                       -> reset
//                       -> fullscreen
//
// *  replaces coord if x or y are complex
// ** replaces math/trig in hex/oct/bin mode
//
// Future extentions:
//   special  -> conv    -> time   -> dow
//                       -> D->D.MS
//                       -> D.MS->D
//   math     -> misc    -> gcd
//            -> matrix  -> more   -> draw
//   mode     -> prog[1] -> draw   -> z=f(z)
//            -> sys     -> screen -> double-buffering

// Complex operations:
//   + - * / +/- 1/x x² sqrt
//   x==y? x!=y? select
//   <pow> <trig> <cplx> <stack> <mem> <int> <matrix>
//   prog/integrate prog/diff
//
// Matrix operations:
//   + - * / +/- 1/x x²
//   y^x cplx/conj
//   x==y? x!=y?
//   <matrix> <stack> <mem>
// Not yet matrix:
//   <int> select ->cplx cplx/split cplx/abs cplx/arg

// Physical constants:
// Universal
//   Speed of light in vacuum          c == 299792458 m/s
//   Planck constant                   h = 6.6260693e-34 J·s
//   Permeability of a vacuum          µ_0 == 4*pi*1e-7 N/A² = kg·m/C² = H/m
//   Permittivity of a vacuum          eps_0 == 1/µ_0·c² F/m
// Chemical
//   Avogadro constant                 N_A = 6.0221415e23 mol^-1
//   Molar gas constant                R = 8.314472 J/mol·K
//   Boltzmann constant                k = 1.3806505e-23 J/K
//   Faraday constant                  F = 96485.3383 C/mol
// Physical and electromagnetic
//   Fine-structure constant           alpha = 7.297352568e-3
//   Bohr radius                       a_0 = 5.291772108e-11 m
//   Rydberg constant                  R_inf = 10973731.568525m^-1
//   Bohr magneton                     µ_B = 9.27400949e-24 J/T = m²·A
// Atomic
//   Elementary charge                 e = 1.60217653e-19 C
//   Mass of electron                  m_e = 9.1093826e-31 kg
//   Mass of proton                    m_p = 1.67262171e-27 kg
//   Mass of neutron                   m_n = 1.67492728e-27 kg
//   Unified atomic mass unit          u = 1.66053886e-27 kg
// Astronomical
//   Newtonian constant of gravitation G = 6.6742e-11 N·m²/kg² = m³/kg·s²
//   Standard acceleration of gravity  g_n == 9.80665 m/s²
//   Light year                        ly == 365.25*24*60*60 * c
//   Astronomical unit                 AU == 149597870691 m
//   Parsec                            pc == 360*60*60/2·pi * A.U.
//
// Sources: http://physics.nist.gov/cuu/Units/units.html
//          http://physics.nist.gov/cuu/Constants
//          http://en.wikipedia.org/wiki/Conversion_of_units
// (== means "equals exactly" or "equals by definition")

    static final int EXIT = -999;
    static final int RESET = -998;
    static final int FULLSCREEN = -997;
    static final int NUMBER_0  = -50;
    static final int NUMBER_1  = NUMBER_0+1;
    static final int NUMBER_2  = NUMBER_0+2;
    static final int NUMBER_3  = NUMBER_0+3;
    static final int NUMBER_4  = NUMBER_0+4;
    static final int NUMBER_5  = NUMBER_0+5;
    static final int NUMBER_6  = NUMBER_0+6;
    static final int NUMBER_7  = NUMBER_0+7;
    static final int NUMBER_8  = NUMBER_0+8;
    static final int NUMBER_9  = NUMBER_0+9;
    static final int NUMBER_10 = NUMBER_0+10;
    static final int NUMBER_11 = NUMBER_0+11;
    static final int NUMBER_12 = NUMBER_0+12;
    static final int NUMBER_13 = NUMBER_0+13;
    static final int NUMBER_14 = NUMBER_0+14;
    static final int NUMBER_15 = NUMBER_0+15;
    static final int FONT_SMALL  = NUMBER_0+UniFont.SMALL;
    static final int FONT_MEDIUM = NUMBER_0+UniFont.MEDIUM;
    static final int FONT_LARGE  = NUMBER_0+UniFont.LARGE;
    static final int FONT_XLARGE = NUMBER_0+UniFont.XLARGE;
    static final int FONT_XXLARGE= NUMBER_0+UniFont.XXLARGE;
    static final int FONT_XXXLARGE=NUMBER_0+UniFont.XXXLARGE;
    static final int FONT_SYS_SML= NUMBER_0+(UniFont.SMALL  | UniFont.SYSTEM_FONT);
    static final int FONT_SYS_MED= NUMBER_0+(UniFont.MEDIUM | UniFont.SYSTEM_FONT);
    static final int FONT_SYS_LRG= NUMBER_0+(UniFont.LARGE  | UniFont.SYSTEM_FONT);
    static final int MENU_FONT = -100;
    static final int NUMBER_FONT = -101;
    static final int MONITOR_FONT = -102;
    static final int UNIT = -200;

    private UniFont menuFont;
    private UniFont numberFont;
    private UniFont monitorFont;
    private int menuFontStyle;
    private int numberFontStyle;
    private int monitorFontStyle;
    public boolean fullScreen;
    public CalcEngine calc;

    private Command add;
    private Command enter;

    private final Calc midlet;

    private int numRepaintLines;
    private boolean repeating = false;
    private boolean unknownKeyPressed = false;
    private boolean internalRepaint = false;
    private int offX, offY, offYMonitor, nDigits, maxLines, maxLinesMonitor, numberFontWidth, numberFontHeight;
    private int monitorOffY, monitorOffX, nMonitorDigits, maxMonitorLines, monitorFontWidth, monitorFontHeight;
    private boolean evenFrame = true;
    private int menuX,menuY,menuW,menuH;
    private int header,footer;
    private static final int TOP_LEFT = Graphics.TOP | Graphics.LEFT;

    public static final int MENU_SIZE = 8;
    private Menu [] menuStack;
    private int menuStackPtr;
    private int menuCommand;
    private Menu menuItem;
    private Menu repeatedMenuItem;

    public CalcCanvas(Calc m, DataInputStream in) {
        midlet = m;

        calc = new CalcEngine(this);

        menuFontStyle = UniFont.MEDIUM | UniFont.SYSTEM_FONT; 
        numberFontStyle = (getWidth()>=320 ? UniFont.XXXLARGE :
                           getWidth()>=256 ? UniFont.XXLARGE :
                           getWidth()>=160 ? UniFont.XLARGE :
                           getWidth()>=128 ? UniFont.LARGE :
                           getWidth()>=96  ? UniFont.MEDIUM :
                                             UniFont.SMALL);
        monitorFontStyle = numberFontStyle;

        if (in != null)
            restoreState(in);
        calc.initialized = true;

        if (!midlet.display.isColor()) {
            numberFontStyle = UniFont.MEDIUM | UniFont.SYSTEM_FONT;
            menuFontStyle = numberFontStyle;
            monitorFontStyle = numberFontStyle;
            // Switch to system font menu
            Menu.fontMenu = Menu.sysFontMenu;
        }
        if (!canToggleFullScreen()) {
            // Remove the fullscreen command.
            Menu.systemMenu.subMenu[3] = null;
        }

        setCommands("ENTER","+");
        setCommandListener(this);

        setMenuFont(menuFontStyle);
        setNumberFont(numberFontStyle);
        setMonitorFont(monitorFontStyle);

        setFullScreen(fullScreen);

        menuStack = new Menu[MENU_SIZE];
        menuStackPtr = -1;

        numRepaintLines = 100;
        checkRepaint();

        if (!hasRepeatEvents()) {
            keyRepeatSignal = new Object();
            keyRepeater = new Runnable() {
                public void run() { keyRepeated(currentRepeatingKey); }
            };
            keyRepeatThread = new Thread() {
                public void run() { runKeyRepeatThread(); }
            };
            keyRepeatThread.start();
        }
    }

    private Thread keyRepeatThread = null;
    private Object keyRepeatSignal;
    private Runnable keyRepeater;
    private int repeatedKey = 0;
    int currentRepeatingKey;
    private long repeatedKeyDueTime;
    private static final long keyRepeatInitialDelay = 750;
    private static final long keyRepeatSubsequentDelay = 200;

    void runKeyRepeatThread() {
        Thread thread = Thread.currentThread();
        while (thread == keyRepeatThread) {
            try {
                synchronized (keyRepeatSignal) {
                    keyRepeatSignal.wait();
                    long delay;
                    while (thread == keyRepeatThread && repeatedKey != 0 &&
                           (delay = repeatedKeyDueTime-
                            System.currentTimeMillis()) > 0) {
                        keyRepeatSignal.wait(delay+10);
                    }
                    if (thread == keyRepeatThread && repeatedKey != 0 &&
                        isShown()) {
                        currentRepeatingKey = repeatedKey;
                        midlet.display.callSerially(keyRepeater);
                    }
                    repeatedKey = 0;
                }
            } catch (InterruptedException e) {
            }
        }
    }

    private void setCommands(String enterStr, String addStr) {
        if (enter != null)
            removeCommand(enter);
        if (add != null)
            removeCommand(add);

        if ((midlet.commandArrangement & 0x80) == 0) {
            enter = new Command(enterStr,
                                SetupCanvas.commandArrangement[
                                    (midlet.commandArrangement&0x7f)*2], 1);
            add   = new Command(addStr,
                                SetupCanvas.commandArrangement[
                                    (midlet.commandArrangement&0x7f)*2+1],1);
            addCommand(enter);
            addCommand(add);
        } else {
            // Reverse order
            add   = new Command(addStr,
                                SetupCanvas.commandArrangement[
                                    (midlet.commandArrangement&0x7f)*2], 1);
            enter = new Command(enterStr,
                                SetupCanvas.commandArrangement[
                                    (midlet.commandArrangement&0x7f)*2+1],1);
            addCommand(add);
            addCommand(enter);
        }
    }

    protected void sizeChanged(int w, int h) {
        if (menuFont==null || numberFont==null || monitorFont==null)
            return;

        // Menu position
        menuW = 21+4*2;
        menuFont.setBold(false);
        menuW = Math.max(21+4*2, menuFont.stringWidth("energy")+3*2);
        menuFont.setBold(true);
        menuW = Math.max(menuW, menuFont.stringWidth("m/ft")+3*2);
        menuW = menuFont.stringWidth("acosh")*2+3*2+menuW;
        menuFont.setBold(false);
        menuW = Math.max(menuW, (menuFont.stringWidth("thousand")+2*2)*2);
        if (menuW>w) menuW = w;
        menuH = menuFont.getHeight()*2+3*2+5*2+21;
        if (menuH>h) menuH = h;
        menuX = (w-menuW)/2;
        menuY = header+(h-menuH-header-footer)/2;
        if (menuY-menuFont.getHeight()-1<header)
            menuY = header+menuFont.getHeight()+1;
        if (menuY+menuH > h)
            menuY = h-menuH;

        // Number font
        nDigits = w/numberFontWidth;
        offX = (w-nDigits*numberFontWidth)/2;
        maxLines = (h-header-footer)/numberFontHeight;
        offY = header + (h-header-footer-maxLines*numberFontHeight)/2;
        maxLinesMonitor = (h-header-footer-3-monitorFontHeight)/numberFontHeight;
        nMonitorDigits = w/monitorFontWidth;
        monitorOffX = (w-nMonitorDigits*monitorFontWidth)/2;
        maxMonitorLines = (h-header-footer-3-numberFontHeight)/monitorFontHeight;
        int minSpace = h;
        for (int monitorLines=1; monitorLines<=maxMonitorLines; monitorLines++) {
            int numberLines = (h-header-footer-3-monitorLines*monitorFontHeight)/numberFontHeight;
            int space = h-header-footer-3-monitorLines*monitorFontHeight-numberLines*numberFontHeight;
            if (space < minSpace)
                minSpace = space;
        }
        offYMonitor = h-footer - maxLinesMonitor*numberFontHeight - (minSpace+1)/3;
        monitorOffY = header + minSpace/3;
        calc.setDisplayProperties(nDigits, maxMonitorLines);
    }

    public void saveState(DataOutputStream out) {
        try {
            menuFont.close();
            numberFont.close();
            monitorFont.close();
            menuFont = numberFont = monitorFont = null; // Free some memory before saveState()

            out.writeShort(1+1+1+1);
            out.writeByte(numberFontStyle);
            out.writeBoolean(fullScreen);
            out.writeByte(menuFontStyle);
            out.writeByte(monitorFontStyle);
            calc.command(CalcEngine.FINALIZE,0);
            calc.saveState(out);

            // Try to stop the keyRepeatThread
            if (keyRepeatThread != null) {
                keyRepeatThread = null;
                repeatedKey = 0;
                keyRepeatSignal.notify();
            }
        } catch (Throwable e) {
        }
    }
  
    private void restoreState(DataInputStream in) {
        try {
            short length = in.readShort();
            if (length >= 1) {
                numberFontStyle = in.readByte();
                length -= 1;
            }
            if (length >= 1) {
                fullScreen = in.readBoolean();
                length -= 1;
            }
            if (length >= 1) {
                menuFontStyle = in.readByte();
                length -= 1;
            }
            if (length >= 1) {
                monitorFontStyle = in.readByte();
                length -= 1;
            }
            in.skip(length);
            calc.restoreState(in);
        } catch (IOException ioe) {
        }
    }
    
    private void setMenuFont(int style) {
        if (menuFont != null) {
            menuFont.close();
            menuFont = null;
        }
        try {
            menuFont = GFont.newFont(
                style | (midlet.bgrDisplay ? UniFont.BGR_ORDER : 0), true, true, this);
        } catch (OutOfMemoryError e) {
            // Fallback to System font
            menuFont = GFont.newFont(style | UniFont.SYSTEM_FONT, true, true, this);
            midlet.outOfMemory();
        }
        menuFontStyle = menuFont.getStyle();
        header = menuFont.smallerFont.getHeight()+2;
        footer = automaticCommands() ? 0 : menuFont.getHeight()+1;
        sizeChanged(getWidth(),getHeight());
    }

    private void setNumberFont(int style) {
        if (numberFont != null) {
            numberFont.close();
            numberFont = null;
        }
        try {
            boolean needSmallerFont = UniFont.isSystemFontStyle(style);
            numberFont = GFont.newFont(
                style | (midlet.bgrDisplay ? UniFont.BGR_ORDER : 0), false, needSmallerFont, this);
        } catch (OutOfMemoryError e) {
            // Fallback to System font
            numberFont = GFont.newFont(style | UniFont.SYSTEM_FONT, false, true, this);
            midlet.outOfMemory();
        }
        numberFontStyle = numberFont.getStyle();
        numberFont.setColor(Colors.NUMBER, Colors.BACKGROUND);
        numberFont.setMonospaced(true);
        numberFontWidth = numberFont.charWidth();
        numberFontHeight = numberFont.getHeight();
        sizeChanged(getWidth(),getHeight());
    }

    private void setMonitorFont(int style) {
        if (monitorFont != null) {
            monitorFont.close();
            monitorFont = null;
        }
        try {
            monitorFont = GFont.newFont(
                style | (midlet.bgrDisplay ? UniFont.BGR_ORDER : 0), true, true, this);
        } catch (OutOfMemoryError e) {
            // Fallback to System font
            monitorFont = GFont.newFont(style | UniFont.SYSTEM_FONT, true, true, this);
            midlet.outOfMemory();
        }
        monitorFontStyle = monitorFont.getStyle();
        monitorFont.setColor(Colors.NUMBER, Colors.BACKGROUND);
        monitorFont.setMonospaced(true);
        monitorFontWidth = monitorFont.charWidth();
        monitorFontHeight = monitorFont.getHeight();
        sizeChanged(getWidth(),getHeight());
    }

    public void drawModeIndicators(Graphics g, boolean toggleRun, boolean stop)
    {
        g.setColor(Colors.c[Colors.FOREGROUND]);
        g.fillRect(0,0,getWidth(),header-1);
        menuFont.setColor(Colors.BACKGROUND, Colors.FOREGROUND);
        menuFont.setBold(false);
        int n = 4;
        if (calc.begin && (calc.progRecording || calc.progRunning)) {
            n = 5;
        }

        UniFont font = menuFont.smallerFont;
        int w = font.stringWidth("ENG");
        int x = getWidth()/(n*2)-w/2;
        if (x<0) x=0;
        int y = 1;
    
        if (calc.degrees) {
            font.drawString(g,x,y,"DEG");
        } else if (calc.grad) {
            font.drawString(g,x,y,"GRAD");
        } else {
            font.drawString(g,x,y,"RAD");
        }

        x += Math.max(getWidth()/n,w+2);
      
        if (calc.format.fse == Real.NumberFormat.FSE_FIX) {
            font.drawString(g,x,y,"FIX");
        } else if (calc.format.fse == Real.NumberFormat.FSE_SCI) {
            font.drawString(g,x,y,"SCI");
        } else if (calc.format.fse == Real.NumberFormat.FSE_ENG) {
            font.drawString(g,x,y,"ENG");
        }

        x += Math.max(getWidth()/n,w+2);

        if (calc.format.base == 2) {
            font.drawString(g,x,y,"BIN");
        } else if (calc.format.base == 8) {
            font.drawString(g,x,y,"OCT");
        } else if (calc.format.base == 16) {
            font.drawString(g,x,y,"HEX");
        }

        x += Math.max(getWidth()/n,w+2);

        if (calc.begin) {
            font.drawString(g,x,y,"BGN");
        }

        if (n == 5) {
            x += Math.max(getWidth()/n,w+2);
        }

        if (calc.progRecording) {
            font.drawString(g,x,y,"PRG");
        } else if (stop) {
            w = font.stringWidth("STOP");
            if (x+w-1>getWidth())
                x = getWidth()-w-1;
            font.drawString(g,x,y,"STOP");      
        } else if (calc.progRunning && (evenFrame || !toggleRun)) {
            font.drawString(g,x,y,"RUN");
        }
        
        if (toggleRun) {
            evenFrame = !evenFrame;
        }
    }

    private void clearScreen(Graphics g) {
        // Clear screen and draw mode indicators
        drawModeIndicators(g,false,false);
        g.setColor(Colors.c[Colors.BACKGROUND]);
        g.fillRect(0,header-1,getWidth(),
                   getHeight()-header-footer+1);
        if (!automaticCommands())
            paintCommands(g, menuFont, calc.isInsideMonitor ? "move" : "menu");
    }

    private void drawMenuItem(Graphics g, Menu menu, int x, int y, int anchor)
    {
        if (menu==null)
            return;
        boolean bold = (menu.subMenu == null &&
            (menu.flags & CmdDesc.SUBMENU_REQUIRED) == 0);
        menuFont.setBold(bold);
        int width = menuFont.stringWidth(menu.label);
        if ((anchor & Graphics.RIGHT) != 0)
            x -= width;
        else if ((anchor & Graphics.HCENTER) != 0)
            x -= width/2;
        if ((anchor & Graphics.BOTTOM) != 0)
            y -= menuFont.getHeight();
        menuFont.drawString(g, x, y, menu.label);
    }

    private void drawMenu(Graphics g) {
        int w = menuW;
        int h = menuH;
        int x = menuX;
        int y = menuY;
        int ym = ((y+h-3)-menuFont.getHeight()+(y+3))/2;
        // Draw menu title
        g.setColor(Colors.c[Colors.MENU_DARK+menuStackPtr]);
        g.fillRect(x,y-menuFont.getHeight()-1,w/2,menuFont.getHeight()+1);
        int c = Colors.c[Colors.MENU+menuStackPtr];
        g.setColor(c);
        menuFont.setColor(Colors.MENU+menuStackPtr, Colors.MENU_DARK+menuStackPtr);
        int titleStackPtr = menuStackPtr;
        while ((menuStack[titleStackPtr].flags & CmdDesc.TITLE_SKIP)!=0)
            titleStackPtr--;
        String label = menuStack[titleStackPtr].label;
        menuFont.setBold(false);
        menuFont.drawString(g, x+2, y-menuFont.getHeight(), label);
        // Draw 3D menu background
        g.fillRect(x+2,y+2,w-4,h-4);
        g.setColor((c+0xfcfcfc)/2);
        g.fillRect(x,y+1,2,h-1);
        g.setColor(c/2);
        g.fillRect(x+w-2,y+1,2,h-1);
        g.setColor((c+0xfcfcfc)/2);
        g.fillRect(x,y,w,1);
        g.fillRect(x+1,y+1,w-2,1);
        g.setColor(c/2);
        g.fillRect(x+2,y+h-2,w-4,1);
        g.fillRect(x+1,y+h-1,w-2,1);
        // Draw menu items
        g.setColor(Colors.c[Colors.BLACK]);
        menuFont.setColor(Colors.BLACK, Colors.MENU+menuStackPtr);
        Menu [] subMenu = menuStack[menuStackPtr].subMenu;
        if (subMenu.length>=1) {
            drawMenuItem(g,subMenu[0],x+w/2,y+3,Graphics.TOP|Graphics.HCENTER);
        }
        if (subMenu.length>=2) {
            drawMenuItem(g,subMenu[1],x+3,ym,TOP_LEFT);
        }
        if (subMenu.length>=3) {
            drawMenuItem(g,subMenu[2],x+w-3,ym,Graphics.TOP|Graphics.RIGHT);
        }
        if (subMenu.length>=4) {
            drawMenuItem(g,subMenu[3],x+w/2,y+h-3,
                         Graphics.BOTTOM|Graphics.HCENTER);
        }
        if (subMenu.length>=5 && subMenu[4]!=null) {
            drawMenuItem(g,subMenu[4],x+w/2,ym,
                         Graphics.TOP|Graphics.HCENTER);
        } else {
            // Draw a small "joystick" in the center
            y += h/2;
            x += w/2;
            g.setColor(Colors.c[Colors.MENU+menuStackPtr]/4*3);
            g.fillRect(x-1,y-10,3,21);
            g.fillRect(x-10,y-1,21,3);
            g.fillArc(x-5,y-5,11,11,0,360);
        }
    }
    
    private void drawLine(Graphics g, UniFont font, Monitorable data, int row,
                          int col, int x, int y, int w, boolean cleared) {
        int charWidth = font.charWidth();
        int charHeight = font.getHeight();
        int digits = w/charWidth;
        x += (w-digits*charWidth)/2;
        w = digits*charWidth;

        String label = data.label(row, col);
        int labelWidth = 0;
        int labelDigits = 0;
        if (label != null) {
            boolean currentPosition = row == calc.monitorRow && col == calc.monitorCol;
            String lead = data.lead(row, col, currentPosition, calc.isInsideMonitor);
            font.setMonospaced(false);
            int w1 = font.stringWidth(label); 
            labelWidth = w1 + font.stringWidth(lead);
            labelDigits = (labelWidth+charWidth-1)/charWidth;
            font.drawString(g, x, y, label);
            font.drawString(g, x+w1, y, lead);
        }

        String suffix = data.elementSuffix(row, col);
        int suffixWidth = 0;
        int suffixDigits = 0;
        int suffixStart = 0;
        int suffixEnd = 0;
        if (suffix != null) {
            suffixEnd = suffix.length();
            font.setMonospaced(false);
            int maxWidth = w-labelWidth;
            suffixWidth = font.substringWidth(suffix, suffixStart, suffixEnd);
            while (suffixWidth > maxWidth && suffixStart < suffixEnd) {
                suffixStart += (suffixWidth-maxWidth+charWidth-1)/charWidth;
                suffixWidth = font.substringWidth(suffix, suffixStart, suffixEnd);
            }
            suffixDigits = (suffixWidth+charWidth-1)/charWidth;
            suffixDigits = Math.min(suffixDigits, digits-labelDigits);
        }

        digits -= labelDigits + suffixDigits;
        String element = data.element(row, col, digits);
        int elementWidth = 0;
        if (element != null) {
            if (element.length()>digits || suffixStart != 0) {
                element = "*****";
                suffix = null;
                digits += suffixDigits;
                suffixDigits = 0;
            }
            font.setMonospaced(true);
            elementWidth = font.stringWidth(element);
            font.drawString(g, x+(labelDigits+digits)*charWidth-elementWidth, y, element);
        }

        if (suffix != null) {
            font.setMonospaced(false);
            font.drawSubstring(g, x+w-suffixWidth, y, suffix, suffixStart, suffixEnd);
            if (!cleared) {
                g.setColor(Colors.c[Colors.BACKGROUND]);
                g.fillRect(x+w-suffixWidth, y,
                           suffixDigits*charWidth-suffixWidth,charHeight);
            }
        }

        if (!cleared) {
            g.setColor(Colors.c[Colors.BACKGROUND]);
            g.fillRect(x+labelWidth, y,
                       (labelDigits+digits)*charWidth-labelWidth-elementWidth,
                       charHeight);
        }
    }
    
    private void drawInputLine(Graphics g, int y, boolean cleared) {
        StringBuffer tmp = calc.inputBuf;
        tmp.append('_');
        numberFont.setMonospaced(true);
        if (tmp.length()>nDigits)
            numberFont.drawString(g,offX,y,tmp,tmp.length()-nDigits);
        else {
            numberFont.drawString(g,offX,y,tmp,0);
            if (!cleared) {
                g.setColor(Colors.c[Colors.BACKGROUND]);
                g.fillRect(offX+tmp.length()*numberFontWidth,
                           y,
                           (nDigits-tmp.length())*numberFontWidth,
                           numberFontHeight);
            }
        }
        tmp.setLength(tmp.length()-1);
    }
    
    private void drawStack(Graphics g, int offY, int nLines, boolean cleared) {
        Monitorable stack = calc.getStack();
        int w = nDigits*numberFontWidth;
        for (int i=0; i<numRepaintLines && i<stack.rows(); i++) {
            int y = offY+(nLines-1-i)*numberFontHeight;
            if (i==0 && calc.inputInProgress)
                drawInputLine(g, y, cleared);
            else
                drawLine(g, numberFont, stack, i, 0, offX, y, w, cleared);
        }
    }
    
    private int actualMonitorRows() {
        Monitorable monitor = calc.getMonitor();
        if (monitor == null)
            return 0;
        return Math.max(0, Math.min(
                maxMonitorLines - (monitor.hasCaption() ? 1 : 0),
                Math.min(calc.requestedMonitorSize, monitor.rows())));
    }
    
    private int monitorSize() {
        Monitorable monitor = calc.getMonitor();
        if (monitor == null)
            return 0;
        return actualMonitorRows() + (monitor.hasCaption() ? 1 : 0); 
    }
    
    private void drawMonitor(Graphics g, boolean cleared) {
        Monitorable monitor = calc.getMonitor();
        int actualMonitorRows = actualMonitorRows();
        int actualMonitorCols = 1; //Math.min(1, monitor.cols());
        if (monitor == null || (actualMonitorRows == 0 && !monitor.hasCaption()))
            return;
        calc.adjustMonitorOffsets(actualMonitorRows, actualMonitorCols);
        monitor.setDisplayedSize(calc.requestedMonitorSize, 1 /*calc.requestedMonitorCols*/);
        int w = nMonitorDigits*monitorFontWidth;
        for (int col=0; col<actualMonitorCols; col++) {
            if (monitor.hasCaption()) {
                String caption = monitor.caption(calc.monitorColOffset);
                monitorFont.setMonospaced(false);
                int width = monitorFont.stringWidth(caption);
                int x = monitorOffX + (w-width)/2;
                monitorFont.drawString(g, x, monitorOffY, caption);
            }
            for (int row=0; row<actualMonitorRows; row++) {
                int y = monitorOffY+(row+(monitor.hasCaption() ? 1 : 0))*monitorFontHeight;
                drawLine(g, monitorFont, monitor, row+calc.monitorRowOffset,
                         col+calc.monitorColOffset, monitorOffX, y, w, cleared);
            }
        }
    }

    public void paint(Graphics g) {
        boolean cleared = false;
        String message;

        message = calc.getMessage();
        if (message != null) {
            midlet.displayMessage(calc.getMessageCaption(), message);
            return;
        }
    
        if (numRepaintLines == 100 || !internalRepaint) {
            clearScreen(g);
            cleared = true;
            numRepaintLines = 100;
        }
        internalRepaint = false;

        if (numRepaintLines >= 0) {
            if (menuStackPtr < 0 || cleared) {
                if (numRepaintLines > 16)
                    numRepaintLines = 16;
                int monitorSize = monitorSize();
                if (monitorSize > 0) {
                    int maxRepaintLines = (getHeight()-header-footer-3-
                            monitorSize*monitorFontHeight)/numberFontHeight;
                    if (numRepaintLines > maxRepaintLines)
                        numRepaintLines = maxRepaintLines;

                    drawStack(g,offYMonitor,maxLinesMonitor,cleared);
                    if (cleared)
                        drawMonitor(g,cleared);
                    g.setColor(Colors.c[Colors.FOREGROUND]);
                    g.drawLine(0,monitorOffY+monitorSize*monitorFontHeight+1,
                               getWidth(),
                               monitorOffY+monitorSize*monitorFontHeight+1);
                } else {
                    if (numRepaintLines > maxLines)
                        numRepaintLines = maxLines;
                    drawStack(g,offY,maxLines,cleared);
                }
            }
            if (menuStackPtr >= 0)
                drawMenu(g);
        }
        numRepaintLines = -1;
    }

    private void checkRepaint() {
        int repaintLines = calc.numRepaintLines();
        if (repaintLines == 0)
            repaintLines = -1; // Because "0" repaints menu
        if (repaintLines > numRepaintLines)
            numRepaintLines = repaintLines;
        if (numRepaintLines >= 0) {
            internalRepaint = true;
            repaint();
        }
    }

    private void clearKeyPressed() throws OutOfMemoryError {
        if (calc.isInsideMonitor) {
            calc.command(CalcEngine.MONITOR_EXIT,0);
            setCommands("ENTER","+");
            return;
        } else if (menuStackPtr >= 0) {
            menuStackPtr--;
            if (menuStackPtr >= 0)
                numRepaintLines = 0; // Force repaint of menu
            else
                numRepaintLines = 100; // Force repaint of all
            return;
        }
        menuStackPtr = -1; // In case it was -2, which signals no-repeat
        calc.command(CalcEngine.CLEAR,0);
    }

    private void clearKeyRepeated() throws OutOfMemoryError {
        if (calc.isInsideMonitor) {
            menuStackPtr = -2; // should not continue by clearing the input...
        } else if (menuStackPtr >= 0) {
            menuStackPtr = -2; // should not continue by clearing the input...
            numRepaintLines = 100; // Force repaint of all
        } else {
            if (!calc.inputInProgress || menuStackPtr == -2)
                return;
            calc.command(CalcEngine.CLEAR,0);
        }
    }

    private void setFullScreen(boolean fs) {
        fullScreen = fs;
        setFullScreenMode(fullScreen);
    }

    private void menuAction(int menuIndex) throws OutOfMemoryError {
        boolean graph=false;
        int graphParam=0;

        if (calc.isInsideMonitor) {
            switch (menuIndex) {
                case 0: calc.command(CalcEngine.MONITOR_UP   ,0); break;
                case 3: calc.command(CalcEngine.MONITOR_DOWN ,0); break;
                case 1: calc.command(CalcEngine.MONITOR_LEFT ,0); break;
                case 2: calc.command(CalcEngine.MONITOR_RIGHT,0); break;
                case 4:
                    calc.command(CalcEngine.MONITOR_PUSH ,0);
                    setCommands("ENTER","+");
                    break;
            }
            return;
        }
    
        if (menuStackPtr < 0) {
            // On entering the menu, switch math/trig menus with bit-op
            // menus if not base-10
            if (calc.format.base == 10) {
                Menu.menu.subMenu[1] = Menu.math;
                Menu.menu.subMenu[2] = Menu.trig;
                // Also switch coord menu with cplx menu if x or y are complex
                if (calc.hasComplexArgs()) {
                    Menu.trig.subMenu[4].subMenu[3] = Menu.cplxMenu;
                } else {
                    Menu.trig.subMenu[4].subMenu[3] = Menu.coordMenu;
                }
            } else {
                Menu.menu.subMenu[1] = Menu.bitOp;
                Menu.menu.subMenu[2] = Menu.bitMath;
            }
            // Switch between prog1 and prog2 if recording a program
            // and between monitor off and prog monitor
            if (calc.progRecording) {
                Menu.menu.subMenu[4].subMenu[1] = Menu.prog2;
                Menu.menu.subMenu[4].subMenu[3].subMenu[4] = Menu.monitorProgMenu;
                // Cannot use NO_PROG commands during program recording
                if (repeatedMenuItem!=null &&
                    (repeatedMenuItem.flags & CmdDesc.NO_PROG)!=0)
                    repeatedMenuItem = null;
            } else {
                Menu.menu.subMenu[4].subMenu[1] = Menu.prog1;        
                Menu.menu.subMenu[4].subMenu[3].subMenu[4] = Menu.monitorOffMenu;
            }
            // Change basicMenu[4] to enterMonitor if monitoring or
            // repeated item
            if (actualMonitorRows() > 0)
                Menu.basicMenu.subMenu[4] = Menu.enterMonitor;
            else
                Menu.basicMenu.subMenu[4] = repeatedMenuItem;
        }

        if (menuStackPtr < 0 && menuIndex < 4) {
            // Go directly to submenu
            menuStack[0] = Menu.menu;
            menuStack[1] = Menu.menu.subMenu[menuIndex];
            menuStackPtr = 1;
            numRepaintLines = 0; // Force repaint of menu
        } else if (menuStackPtr < 0) {
            // Open top level menu
            menuStack[0] = Menu.menu;
            menuStackPtr = 0;
            numRepaintLines = 0; // Force repaint of menu
        } else if (menuIndex < menuStack[menuStackPtr].subMenu.length) {
            Menu subItem = menuStack[menuStackPtr].subMenu[menuIndex];
            if (subItem == null) {
                ; // NOP
            } else if (subItem.subMenu != null) {
                // Open submenu
                menuStackPtr++;
                menuStack[menuStackPtr] = subItem;
                // Set correct labels
                if (subItem == Menu.progMenu.subMenu[4])
                    for (int i=0; i<5; i++)
                        Menu.progMenu.subMenu[4].subMenu[i].label =
                            calc.progLabel(i+4);
                numRepaintLines = 0; // Force repaint of menu
            } else if ((subItem.flags & CmdDesc.SUBMENU_REQUIRED)!=0) {
                // Open number/finance/program submenu
                Menu sub =
                    (subItem.flags&CmdDesc.NUMBER_REQUIRED )!=0 ? Menu.numberMenu :
                    (subItem.flags&CmdDesc.FINANCE_REQUIRED)!=0 ? Menu.financeMenu :
                    (subItem.flags&CmdDesc.FONT_REQUIRED)   !=0 ? Menu.fontMenu :
                    (subItem.flags&CmdDesc.PROG_REQUIRED)   !=0 ? Menu.progMenu :
                    (subItem.flags&CmdDesc.UNIT_REQUIRED)   !=0 ? Menu.unitMenu :
                   Menu.unitConvertMenu;
                // Set correct labels
                if (sub == Menu.progMenu)
                    for (int i=0; i<4; i++)
                        Menu.progMenu.subMenu[i].label = calc.progLabel(i);
                menuCommand = subItem.command; // Save current command
                menuItem = subItem;
                sub.label = subItem.label; // Set correct label
                sub.flags = subItem.flags; // Set correct flags
                menuStackPtr++;
                menuStack[menuStackPtr] = sub;
                numRepaintLines = 0; // Force repaint of menu
            } else {
                int command = subItem.command;
                if (command == EXIT) {
                    // Internal exit command
                    midlet.exitRequested();
                } else if (command == RESET) {
                    midlet.resetRequested();
                } else if (command == FULLSCREEN) {
                    setFullScreen(!fullScreen);
                } else if (command >= NUMBER_0 && command <= FONT_SYS_LRG) {
                    int number = command-NUMBER_0;
                    // Number has been entered for previous command
                    if (menuCommand >= CalcEngine.PROG_DRAW &&
                        menuCommand <= CalcEngine.PROG_MINMAX) {
                        graph = true;
                        graphParam = number;
                    } if (menuCommand == CalcEngine.PROG_NEW) {
                        int n = number;
                        String name = calc.progLabel(n)==Program.emptyProg
                            ? "" : calc.progLabel(n);
                        midlet.askNewProgram(name,n);
                    } else if (menuCommand == MENU_FONT) {
                        setMenuFont(number);
                    } else if (menuCommand == NUMBER_FONT) {
                        setNumberFont(number);
                    } else if (menuCommand == MONITOR_FONT) {
                        setMonitorFont(number);
                    } else {
                        calc.command(menuCommand,number);
                    }
                } else if (command == UNIT) {
                    calc.command(menuCommand, ((subItem.param & 0xff00) << 8) | (subItem.param & 0xff));
                } else if (command >= CalcEngine.AVG_DRAW &&
                           command <= CalcEngine.POW_DRAW) {
                    menuCommand = command;
                    graph = true;
                } else {
                    // Normal calculator command
                    if (command == CalcEngine.MONITOR_ENTER)
                        if (calc.monitorMode == CalcEngine.MONITOR_PROG)
                            // Different commands inside monitor 
                            setCommands("SST","DEL");
                        else
                            setCommands("STO","RCL"); 
                    calc.command(command,0);
                }

                if ((subItem.flags & CmdDesc.NO_REPEAT)==0) {
                    // Repeat this command or parent in basicMenu[4]
                    Menu item = subItem;
                    while ((item.flags & CmdDesc.REPEAT_PARENT)!=0)
                        item = menuStack[menuStackPtr--];
                    if ((item.flags & CmdDesc.SUBMENU_REQUIRED)!=0)
                        // Switch from submenu to actual menu item
                        item = menuItem;
                    if ((item.flags & CmdDesc.NO_REPEAT)==0)
                        repeatedMenuItem = item;
                }

                menuStackPtr = -1;     // Remove menu
                numRepaintLines = 100; // Force repaint of all
            }
        }

        if (graph)
            prepareGraph(menuCommand, graphParam);
    }
    
    public void prepareGraph(int graphCommand, int graphParam) {
        if (calc.prepareGraph(graphCommand,graphParam)) {
            evenFrame = true;
            midlet.displayGraph(0,header-1,getWidth(),
                                getHeight()-header+1);
            numRepaintLines = 100;
        }
    }

    protected void keyPressed(int key) {
        try {
            repeating = false;
            if (keyRepeatThread != null) {
                synchronized (keyRepeatSignal) {
                    repeatedKey = key;
                    repeatedKeyDueTime = System.currentTimeMillis()+
                        keyRepeatInitialDelay;
                    keyRepeatSignal.notify();
                }
            }
    
            int menuIndex = -1;
            switch (key) {
                case '0': case '1': case '2': case '3': case '4':
                case '5': case '6': case '7': case '8': case '9':
                    if (menuStackPtr >= 0 || calc.isInsideMonitor) {
                        if ((menuStackPtr>=0 && menuStack[menuStackPtr  ] ==
                             Menu.numberMenu) ||
                            (menuStackPtr>=1 && menuStack[menuStackPtr-1] ==
                             Menu.numberMenu))
                        {
                            calc.command(menuCommand,key-'0');

                            // Repeat menu or parent in basicMenu[4]
                            Menu item = menuStack[menuStackPtr--];
                            while ((item.flags & CmdDesc.REPEAT_PARENT)!=0)
                                item = menuStack[menuStackPtr--];
                            if ((item.flags & CmdDesc.SUBMENU_REQUIRED)!=0)
                                // Switch from submenu to actual menu item
                                item = menuItem;
                            if ((item.flags & CmdDesc.NO_REPEAT)==0)
                                repeatedMenuItem = item;

                            menuStackPtr = -1;
                            numRepaintLines = 100; // Force repaint of all
                            break;
                        }
                        switch (getGameAction(key)) {
                            case UP:    menuIndex = 0; break;
                            case DOWN:  menuIndex = 3; break;
                            case LEFT:  menuIndex = 1; break;
                            case RIGHT: menuIndex = 2; break;
                            case FIRE:  menuIndex = 4; break;
                            default:
                                switch (key) {
                                    case '2': menuIndex = 0; break; // UP
                                    case '8': menuIndex = 3; break; // DOWN
                                    case '4': menuIndex = 1; break; // LEFT
                                    case '6': menuIndex = 2; break; // RIGHT
                                    case '5': menuIndex = 4; break; // PUSH
                                }
                                break;
                        }
                        break;
                    }
                    calc.command(CalcEngine.DIGIT_0+key-'0',0);
                    break;
                case KEY_SEND:
                    if (midlet.hasClearKey)
                        clearKeyPressed();
                    else
                        menuIndex =  4;
                    break;
                case '\b': case KEY_END:
                    clearKeyPressed();
                    break;
                case '#':
                    if (!midlet.hasClearKey || menuStackPtr>=0 ||
                        calc.isInsideMonitor) {
                        clearKeyPressed();
                        break;
                    }
                    // deliberate fall-through
                case '-': case 'e': case 'E':
                    if (menuStackPtr >= 0)
                        return;
                    calc.command(CalcEngine.SIGN_E,0);
                    break;
                case '*': case '.': case ',': case 65452:
                    if (menuStackPtr >= 0)
                        return;
                    if (!midlet.hasClearKey)
                        calc.command(CalcEngine.SIGN_POINT_E,0);
                    else
                        calc.command(CalcEngine.DEC_POINT,0);
                    break;
                case '\n': case '\r':
                case KEY_SOFTKEY1:
                    commandAction(enter,this);
                    return;
                case '+':
                case KEY_SOFTKEY2:
                    commandAction(add,this);
                    return;
                default:
                    switch (getGameAction(key)) {
                        case UP:    menuIndex = 0; break;
                        case DOWN:  menuIndex = 3; break;
                        case LEFT:  menuIndex = 1; break;
                        case RIGHT: menuIndex = 2; break;
                        case FIRE:  menuIndex = 4; break;
                        case GAME_A: case GAME_B: case GAME_C: case GAME_D:
                            // I have no idea how these keys are mapped, I just
                            // hope one of them is mapped to something that we
                            // can use as a "clear" key
                            clearKeyPressed();
                            break;
                        default:
                            // Nokia and other direct key mappings
                            switch (key) {
                                case KEY_UP_ARROW:    menuIndex = 0; break;
                                case KEY_DOWN_ARROW:  menuIndex = 3; break;
                                case KEY_LEFT_ARROW:  menuIndex = 1; break;
                                case KEY_RIGHT_ARROW: menuIndex = 2; break;
                                case KEY_SOFTKEY3:    menuIndex = 4; break;
                                case -8:  // SonyEricsson "c"
                                case -23: // Motorola "menu"
                                    clearKeyPressed();
                                    break;
                                default:
                                    // Clear key could be mapped as something
                                    // else...
                                    if (midlet.doubleKeyEvents)
                                        // We don't yet know if we can treat
                                        // this as "clear"
                                        unknownKeyPressed = true;
                                    else
                                        clearKeyPressed();
                                    break;
                            }
                            break;
                    }
                    break;
            }
            if (menuIndex >= 0)
                menuAction(menuIndex);

            checkRepaint();
        } catch (OutOfMemoryError e) {
            menuStackPtr = -1;     // Remove menu
            numRepaintLines = 100; // Force repaint of all
            midlet.outOfMemory();
        }
    }

    protected void keyRepeated(int key) {
        try {
            if (unknownKeyPressed) {
                // Can't repeat "delayed clear key"
                return;
            }
            if (keyRepeatThread != null) {
                if (key == 0)
                    return;
                synchronized (keyRepeatSignal) {
                    repeatedKey = key;
                    repeatedKeyDueTime = System.currentTimeMillis() +
                        keyRepeatSubsequentDelay;
                    keyRepeatSignal.notify();
                }
            }
            switch (key) {
                case '1': case '2': case '3': case '4': case '5': case '6':
                    if (repeating || menuStackPtr >= 0)
                        return;
                    calc.command(CalcEngine.DIGIT_A+key-'1',0);
                    break;
                case KEY_SEND:
                    if (midlet.hasClearKey)
                        clearKeyRepeated();
                    break;
                case '\b': case KEY_END:
                    clearKeyRepeated();
                    break;
                case '#':
                    if (!midlet.hasClearKey)
                        clearKeyRepeated();
                    break;
                case '0': case '7': case '8': case '9':
                case '*':
                    // Do nothing, but do not fall into the "default" below
                    break;
                default:
                    switch (getGameAction(key)) {
                        case GAME_A: case GAME_B: case GAME_C: case GAME_D:
                            clearKeyRepeated();
                            break;
                        default:
                            if (key == -8 || key == -23)
                                clearKeyRepeated();
                            break;
                    }
                    break;
            }
            repeating = true;
            checkRepaint();
        } catch (OutOfMemoryError e) {
            menuStackPtr = -1;     // Remove menu
            numRepaintLines = 100; // Force repaint of all
            midlet.outOfMemory();
        }
    }

    protected void keyReleased(int key) {
        try {
            if (keyRepeatThread != null) {
                synchronized (keyRepeatSignal) {
                    repeatedKey = 0;
                    keyRepeatSignal.notify();
                }
            }
            if (unknownKeyPressed) {
                // It's a "delayed clear key"
                unknownKeyPressed = false;
                clearKeyPressed();
                checkRepaint();
            }
        } catch (OutOfMemoryError e) {
            menuStackPtr = -1;     // Remove menu
            numRepaintLines = 100; // Force repaint of all
            midlet.outOfMemory();
        }
    }

    protected void pointerPressed(int x, int y) {
        try {
            int menuIndex, q=0;

            if (!automaticCommands() && y >= getHeight()-footer) {
                if (x < getWidth()/2)
                    commandAction(enter,this);
                else
                    commandAction(add,this);
                return;
            }

            x = x-menuX-menuW/2; if (x<0) { x = -x; q += 1; }
            y = y-menuY-menuH/2; if (y<0) { y = -y; q += 2; }

            if (x*6 < menuW && y*6 < menuH) {
                menuIndex = 4;
            } else if ((x-y)*6 > menuW-menuH) {
                menuIndex = (q&1)==0 ? 2/*RIGHT*/: 1/*LEFT*/;
            } else {
                menuIndex = (q&2)==0 ? 3/*DOWN*/ : 0/*UP*/;
            }
            menuAction(menuIndex);
            checkRepaint();
        } catch (OutOfMemoryError e) {
            menuStackPtr = -1;     // Remove menu
            numRepaintLines = 100; // Force repaint of all
            midlet.outOfMemory();
        }
    }

    public void commandAction(Command c, Displayable d)
    {
        try {
            // If an unknown key has beed pressed but not released, ignore it
            unknownKeyPressed = false;

            // Nothing happens inside menu
            if (menuStackPtr >= 0)
                return;

            if (c == enter) {
                if (calc.isInsideMonitor) {
                    calc.command(CalcEngine.MONITOR_PUT,0);
                } else {
                    calc.command(CalcEngine.ENTER,0);
                }
            } else if (c == add) {
                if (calc.isInsideMonitor) {
                    calc.command(CalcEngine.MONITOR_GET,0);
                } else {
                    calc.command(CalcEngine.ADD,0);
                }
            }
            checkRepaint();
        } catch (OutOfMemoryError e) {
            menuStackPtr = -1;     // Remove menu
            numRepaintLines = 100; // Force repaint of all
            midlet.outOfMemory();
        }
    }

}
