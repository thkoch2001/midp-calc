package ral;

import javax.microedition.lcdui.*;
import java.io.*;

public final class CalcCanvas
    extends Canvas
    implements CommandListener
{
// Commands:
//               ENTER  +  0-9a-f  .  -/E  clear  menu
// Modes:
//               DEG/RAD   FIX/SCI/ENG   BIN/OCT/HEX   BGN   PRG/RUN
// Menu:
//   basic    -> -  *  /  +/-  %
//   math     -> simple  -> 1/x    x^2    sqrt    %chg
//                       -> int -> round ceil floor trunc frac
//            -> pow     -> y^x    y^1/x  ln      e^x
//                       -> pow10/2 -> log10 10^x log2 2^x
//            -> prob    -> Py,x   Cy,x   x!      erfc
//            -> misc    -> mod    div    random  factorize
//            -> coord   -> r->p   p->r   atan2   hypot   ->cplx
//            -> cplx*   -> split  abs    arg     conj
//   trig     -> normal  -> sin    cos    tan
//            -> arc     -> asin   acos   atan
//            -> hyp     -> sinh   cosh   tanh
//            -> archyp  -> asinh  acosh  atanh
//            -> more    -> RAD/DEG ->RAD  ->DEG  pi
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
//                                 -> x      -> SUMx SUMx� SUMlnx SUMln�x
//                                 -> y      -> SUMy SUMy� SUMlny SUMln�y
//                                 -> xy     -> SUMxy SUMxlny SUMylnx SUMlnxlny
//            -> finance -> STO RCL solve -> pv fv np pmt ir
//                       -> clear
//            -> conv    -> time   -> ->DH.MS  ->H  DH.MS+  now
//                                 -> more -> unix -> DH.MS->unix unix->DH.MS
//                                         -> JD   -> DH.MS->JD   JD->DH.MS
//                                         -> MJD  -> DH.MS->MJD  MJD->DH.MS
//                       -> metric -> length weight vol  energy temp
//                       -> const  -> univ   chem   phys atom   astro
//   mode     -> number  -> normal FIX#   SCI#   ENG#                  ( -> # )
//                       -> sepr   -> decimal  -> dot comma remove keep
//                                 -> thousand -> dot/comma space ' none
//            -> prog[1] -> run    -> #
//                       -> new    -> # -> name?
//                       -> clear  -> #
//                       -> draw   -> #
//                       -> more   -> integrate     -> #
//                                 -> differentiate -> #
//                                 -> solve         -> #
//                                 -> min/max       -> #
//            -> prog[2] -> finish
//                       -> cond   -> x=y? x!=y? x<y? x<=y? x>y?
//                       -> util   -> abs max min sgn select
//                       -> purge
//                       -> mem    -> RCL[x] STO[x] STO+[x]
//            -> base    -> dec    hex    oct      bin
//            -> monitor -> mem    stat   finance  off                 ( -> # )
//            -> sys     -> font   -> small  medium large  system
//                       -> exit
//                       -> reset
//
// *  replaces coord if x or y are complex
// ** replaces math/trig in hex/oct/bin mode
//
// Extentions:
//   special  -> conv    -> time   -> dow

// Complex operations:
//   + - * / +/- 1/x x� sqrt
//   <pow> <pow10/2> <trig/normal> <trig/hyp>
//   <cplx> <stack> <mem> <int>
// Not yet complex:
//   <trig/arc> <trig/archyp>
// Not complex:
//   % %chg and or xor bic not y<<x y>>x RAD/DEG ->RAD ->DEG
//   <prob> <coord> <misc> <stat> <finance> <time>

// Physical constants:
// Universal
//   Speed of light in vacuum          c == 299792458 m/s
//   Planck constant                   h = 6.6260693e-34 J�s
//   Permeability of a vacuum          �_0 == 4*pi*1e-7 N/A�
//   Permittivity of a vacuum          eps_0 == 1/�_0�c� F/m
// Chemical
//   Avogadro constant                 N_A = 6.0221415e23 mol^-1
//   Molar gas constant                R = 8.314472 J/mol�K
//   Boltzmann constant                k = 1.3806505e-23 J/K
//   Faraday constant                  F = 96485.3383 C/mol
// Physical and electromagnetic
//   Fine-structure constant           alpha = 7.297352568e-3
//   Bohr radius                       a_0 = 5.291772108e-11 m
//   Rydberg constant                  R_inf = 10973731.568525m^-1
//   Bohr magneton                     �_B = 9.27400949e-24 J/T
// Atomic
//   Elementary charge                 e = 1.60217653e-19 C
//   Mass of electron                  m_e = 9.1093826e-31 kg
//   Mass of proton                    m_p = 1.67262171e-27 kg
//   Mass of neutron                   m_n = 1.67492728e-27 kg
//   Unified atomic mass unit          m_u = 1.66053886e-27 kg
// Astronomical
//   Newtonian constant of gravitation G = 6.6742e-11 N�m�/kg�
//   Standard acceleration of gravity  g_n == 9.80665 m/s�
//   Light year                        l.y. == 365.25*24*60*60 * c
//   Astronomical unit                 A.U. == 149597870691 m
//   Parsec                            pc == 360*60*60/2�pi * A.U.
//
// Metric conversions:
// Length
//   Inch in centimeters               cm/in == 2.54         (def)
//   Foot in meters                    m/ft == 0.3048        (ft = 12 in)
//   Yard in meters                    m/yd == 0.9144        (yd = 3 ft)
//   Mile in kilometers                km/mi == 1.609344     (mi = 5280 ft)
//   Nautical mile in kilometers       km/n.m. == 1.852      (def)
// Weight
//   U.S. ounce in grams               g/oz == 28.349523125  (oz = 1/16 lb)
//   U.S. pound in kilos               kg/lb == 0.45359237   (def)
//   U.S. short ton in kilos           kg/ton == 907.18474   (ton = 2000 lb)
//   U.S. grain in milligrams          mg/gr == 64.79891     (gr = 1/7000 lb)
// Volume
//   U.S. cup in litres                l/cup == 0.2365882365 (cup = 1/2 pt)
//   U.S. gallon in litres             l/gal == 3.785411784  (gal = 231 in�)
//   U.S. pint in litres               l/pt == 0.473176473   (pt = 1/8 gal)
//   U.S. fluid ounce in millilitres   ml/fl.oz==29.5735295625(fl.oz=gal/128)
// Temperature
//   Celsius to Fahrenheit             �C->�F == x*1.8+32    (def)
//   Fahrenheit to Celsius             �F->�C == (x-32)/1.8  (def)
//   Kelvin minus Celsius              �K-�C == 273.15       (def)
// Energy
//   International calorie in Joules   J/cal = 4.1868
//   British thermal unit in Joules    J/Btu = 1055.06
//   Horsepower in Watts               W/hp  = 745.7
//
// Sources: http://physics.nist.gov/cuu/Constants
//          http://www.free-definition.com
// (== means "equals exactly" or "equals by definition")

  private static final class Menu
  {
    public String label;
    public int command;
    public byte flags;
    public Menu [] subMenu;

    public static final byte NUMBER_REQUIRED = 1;
    public static final byte FINANCE_REQUIRED = 2;
    public static final byte PROG_REQUIRED = 4;
    public static final byte SUBMENU_REQUIRED = 7;
    public static final byte TITLE_SKIP = 8;

    Menu(String l, int c) {
      label = l;
      command = c;
    }
    Menu(String l, int c, int f) {
      label = l;
      command = c;
      flags = (byte)f;
    }
    Menu(String l, Menu [] m) {
      label = l;
      subMenu = m;
    }
    Menu(String l, int f, Menu [] m) {
      label = l;
      flags = (byte)f;
      subMenu = m;
    }
  }

  private static final int EXIT = -999;
  private static final int RESET = -998;
  private static final int FONT_SMALL  = -50+GFont.SMALL;
  private static final int FONT_MEDIUM = -50+GFont.MEDIUM;
  private static final int FONT_LARGE  = -50+GFont.LARGE;
  private static final int FONT_SYSTEM = -50+GFont.SYSTEM;
  private static final int FONT_XLARGE = -50+GFont.XLARGE;
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
  
  private Menu menu = new Menu("menu",new Menu[] {
    new Menu("basic",new Menu[] {
      new Menu("-",CalcEngine.SUB),
      new Menu("*",CalcEngine.MUL),
      new Menu("/",CalcEngine.DIV),
      new Menu("+/-",CalcEngine.NEG),
      new Menu("%",CalcEngine.PERCENT),
      //new Menu("free",CalcEngine.FREE_MEM),
    }),
    null, // math or binop
    null, // trig or binop2
    new Menu("special",new Menu [] {
      new Menu("stack",new Menu[] {
        new Menu("LAST x",CalcEngine.LASTX),
        new Menu("x�y",CalcEngine.XCHG),
        new Menu("undo",CalcEngine.UNDO),
        new Menu("more",Menu.TITLE_SKIP,new Menu [] {
          new Menu("RCL st#",CalcEngine.RCLST,Menu.NUMBER_REQUIRED),
          new Menu("rolldn",CalcEngine.ROLLDN),
          new Menu("rollup",CalcEngine.ROLLUP),
          new Menu("x�st#",CalcEngine.XCHGST,Menu.NUMBER_REQUIRED),
        }),
        new Menu("clear",CalcEngine.CLS),
      }),
      new Menu("mem",new Menu[] {
        new Menu("STO",CalcEngine.STO,Menu.NUMBER_REQUIRED),
        new Menu("RCL",CalcEngine.RCL,Menu.NUMBER_REQUIRED),
        new Menu("STO+",CalcEngine.STP,Menu.NUMBER_REQUIRED),
        new Menu("x�mem",CalcEngine.XCHGMEM,Menu.NUMBER_REQUIRED),
        new Menu("clear",CalcEngine.CLMEM),
      }),
      new Menu("stat",new Menu[] {
        new Menu("�+",CalcEngine.SUMPL),
        new Menu("�-",CalcEngine.SUMMI),
        new Menu("result",Menu.TITLE_SKIP,new Menu[] {
          new Menu("average",new Menu [] {
            new Menu("~x~, ~y~",CalcEngine.AVG),
            new Menu("s_x_, s_y_",CalcEngine.STDEV),
            new Menu("~x~w",CalcEngine.AVGXW),
            new Menu("S_x_, S_y_",CalcEngine.PSTDEV),
            new Menu("draw",CalcEngine.AVG_DRAW),
          }),
          new Menu("ax+b",new Menu[] {
            new Menu("a,b", CalcEngine.LIN_AB),
            new Menu("y^*^",CalcEngine.LIN_YEST),
            new Menu("x^*^",CalcEngine.LIN_XEST),
            new Menu("r",   CalcEngine.LIN_R),
            new Menu("draw",CalcEngine.LIN_DRAW),
          }),
          new Menu("alnx+b",new Menu[] {
            new Menu("a,b", CalcEngine.LOG_AB),
            new Menu("y^*^",CalcEngine.LOG_YEST),
            new Menu("x^*^",CalcEngine.LOG_XEST),
            new Menu("r",   CalcEngine.LOG_R),
            new Menu("draw",CalcEngine.LOG_DRAW),
          }),
          new Menu("be^ax",new Menu[] {
            new Menu("a,b", CalcEngine.EXP_AB),
            new Menu("y^*^",CalcEngine.EXP_YEST),
            new Menu("x^*^",CalcEngine.EXP_XEST),
            new Menu("r",   CalcEngine.EXP_R),
            new Menu("draw",CalcEngine.EXP_DRAW),
          }),
          new Menu("bx^a",new Menu[] {
            new Menu("a,b", CalcEngine.POW_AB),
            new Menu("y^*^",CalcEngine.POW_YEST),
            new Menu("x^*^",CalcEngine.POW_XEST),
            new Menu("r",   CalcEngine.POW_R),
            new Menu("draw",CalcEngine.POW_DRAW),
          }),
        }),
        new Menu("sums",new Menu[] {
          new Menu("n",CalcEngine.N),
          new Menu("x",Menu.TITLE_SKIP,new Menu[] {
            new Menu("�x",CalcEngine.SUMX),
            new Menu("�x^2",CalcEngine.SUMXX),
            new Menu("�lnx",CalcEngine.SUMLNX),
            new Menu("�ln^2^x",CalcEngine.SUMLN2X),
          }),
          new Menu("y",Menu.TITLE_SKIP,new Menu[] {
            new Menu("�y",CalcEngine.SUMY),
            new Menu("�y^2",CalcEngine.SUMYY),
            new Menu("�lny",CalcEngine.SUMLNY),
            new Menu("�ln^2^y",CalcEngine.SUMLN2Y),
          }),
          new Menu("xy",Menu.TITLE_SKIP,new Menu[] {
            new Menu("�xy",CalcEngine.SUMXY),
            new Menu("�xlny",CalcEngine.SUMXLNY),
            new Menu("�ylnx",CalcEngine.SUMYLNX),
            new Menu("�lnxlny",CalcEngine.SUMLNXLNY),
          }),
        }),
        new Menu("clear",CalcEngine.CLST),
      }),
      new Menu("finance",new Menu [] {
        new Menu("STO",CalcEngine.FINANCE_STO,Menu.FINANCE_REQUIRED),
        new Menu("RCL",CalcEngine.FINANCE_RCL,Menu.FINANCE_REQUIRED),
        new Menu("solve",CalcEngine.FINANCE_SOLVE,Menu.FINANCE_REQUIRED),
        new Menu("more",Menu.TITLE_SKIP,new Menu [] {
          new Menu("END/BGN",CalcEngine.FINANCE_BGNEND),
          new Menu("y%*x",CalcEngine.FINANCE_MULINT),
          new Menu("y%/x",CalcEngine.FINANCE_DIVINT),
        }),
        new Menu("clear",CalcEngine.FINANCE_CLEAR),
      }),
      new Menu("conv",new Menu [] {
        new Menu("time",new Menu[] {
          new Menu("�DH.MS",CalcEngine.TO_DHMS),
          new Menu("�H",CalcEngine.TO_H),
          new Menu("now",CalcEngine.TIME_NOW),
          new Menu("DH.MS+",CalcEngine.DHMS_PLUS),
          new Menu("more",Menu.TITLE_SKIP,new Menu [] {
            new Menu("unix",Menu.TITLE_SKIP,new Menu [] {
              new Menu("DH.MS�unix",CalcEngine.DHMS_TO_UNIX),
              null,
              null,
              new Menu("unix�DH.MS",CalcEngine.UNIX_TO_DHMS),
            }),
            new Menu("JD",Menu.TITLE_SKIP,new Menu [] {
              new Menu("DH.MS�JD",CalcEngine.DHMS_TO_JD),
              null,
              null,
              new Menu("JD�DH.MS",CalcEngine.JD_TO_DHMS),
            }),
            new Menu("MJD",Menu.TITLE_SKIP,new Menu [] {
              new Menu("DH.MS�MJD",CalcEngine.DHMS_TO_MJD),
              null,
              null,
              new Menu("MJD�DH.MS",CalcEngine.MJD_TO_DHMS),
            }),
            new Menu("time",CalcEngine.TIME),
            new Menu("date",CalcEngine.DATE),
          }),
        }),
        new Menu("metric",new Menu [] {
          new Menu("length",new Menu [] {
            new Menu("km/mi",CalcEngine.CONST_km_mi),
            new Menu("cm/in",CalcEngine.CONST_cm_in),
            new Menu("m/yd",CalcEngine.CONST_m_yd),
            new Menu("km/n.m.",CalcEngine.CONST_km_nm),
            new Menu("m/ft",CalcEngine.CONST_m_ft),
          }),
          new Menu("weight",new Menu [] {
            new Menu("g/oz",CalcEngine.CONST_g_oz),
            new Menu("kg/lb",CalcEngine.CONST_kg_lb),
            new Menu("mg/gr",CalcEngine.CONST_mg_gr),
            new Menu("kg/ton",CalcEngine.CONST_kg_ton),
          }),
          new Menu("temp",new Menu [] {
            new Menu("�C��F",CalcEngine.CONV_C_F),
            new Menu("�F��C",CalcEngine.CONV_F_C),
            new Menu("�K-�C",CalcEngine.CONST_K_C),
          }),
          new Menu("energy",new Menu [] {
            new Menu("J/cal",CalcEngine.CONST_J_cal),
            new Menu("J/Btu",CalcEngine.CONST_J_Btu),
            new Menu("W/hp",CalcEngine.CONST_W_hp),
          }),
          new Menu("vol",new Menu [] {
            new Menu("l/pt",CalcEngine.CONST_l_pt),
            new Menu("l/cup",CalcEngine.CONST_l_cup),
            new Menu("l/gal",CalcEngine.CONST_l_gal),
            new Menu("ml/fl.oz",CalcEngine.CONST_ml_floz),
          }),
        }),
        new Menu("const",new Menu [] {
          new Menu("univ",new Menu [] {
            new Menu("c",CalcEngine.CONST_c),
            new Menu("h",CalcEngine.CONST_h),
            new Menu("�_0",CalcEngine.CONST_mu_0),
            new Menu("�_0",CalcEngine.CONST_eps_0),
          }),
          new Menu("chem",new Menu [] {
            new Menu("N_A",CalcEngine.CONST_NA),
            new Menu("R",CalcEngine.CONST_R),
            new Menu("k",CalcEngine.CONST_k),
            new Menu("F",CalcEngine.CONST_F),
          }),
          new Menu("phys",new Menu [] {
            new Menu("�",CalcEngine.CONST_alpha),
            new Menu("a_0",CalcEngine.CONST_a_0),
            new Menu("R_�",CalcEngine.CONST_R_inf),
            new Menu("�_B",CalcEngine.CONST_mu_B),
          }),
          new Menu("atom",new Menu [] {
            new Menu("e",CalcEngine.CONST_e),
            new Menu("m_e",CalcEngine.CONST_m_e),
            new Menu("m_p",CalcEngine.CONST_m_p),
            new Menu("m_n",CalcEngine.CONST_m_n),
            new Menu("m_u",CalcEngine.CONST_m_u),
          }),
          new Menu("astro",new Menu [] {
            new Menu("G",CalcEngine.CONST_G),
            new Menu("g_n",CalcEngine.CONST_g_n),
            new Menu("l.y.",CalcEngine.CONST_ly),
            new Menu("A.U.",CalcEngine.CONST_AU),
            new Menu("pc",CalcEngine.CONST_pc),
          }),
        }),
      }),
    }),
    new Menu("mode",new Menu[] {
      new Menu("number",new Menu[] {
        new Menu("normal",CalcEngine.NORM),
        new Menu("FIX",CalcEngine.FIX,Menu.NUMBER_REQUIRED),
        new Menu("SCI",CalcEngine.SCI,Menu.NUMBER_REQUIRED),
        new Menu("ENG",CalcEngine.ENG,Menu.NUMBER_REQUIRED),
        new Menu("sepr",new Menu[] {
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
            new Menu("'",CalcEngine.THOUSAND_QUOTE),
            new Menu("none",CalcEngine.THOUSAND_NONE),
          }),
        }),
      }),
      null, // prog1 or prog2
      new Menu("base",new Menu[] {
        new Menu("DEC",CalcEngine.BASE_DEC),
        new Menu("HEX",CalcEngine.BASE_HEX),
        new Menu("OCT",CalcEngine.BASE_OCT),
        new Menu("BIN",CalcEngine.BASE_BIN),
      }),
      new Menu("monitor",new Menu[] {
        new Menu("finance",CalcEngine.MONITOR_FINANCE),
        new Menu("stat",CalcEngine.MONITOR_STAT,Menu.NUMBER_REQUIRED),
        new Menu("mem",CalcEngine.MONITOR_MEM,Menu.NUMBER_REQUIRED),
        new Menu("off",CalcEngine.MONITOR_NONE),
      }),
      new Menu("sys",new Menu[] {
        new Menu("font",new Menu[] {
          new Menu("medium",FONT_MEDIUM),
          new Menu("small",FONT_SMALL),
          new Menu("large",FONT_LARGE),
          new Menu("xlarge",FONT_XLARGE),
          new Menu("sys",FONT_SYSTEM),
        }),
        new Menu("exit",EXIT),
        new Menu("reset",RESET),
      }),
    }),
  });
  
  private Menu numberMenu = new Menu(null,new Menu[] {
    new Menu("<0-3>",Menu.TITLE_SKIP,new Menu[] {
      new Menu("<0>",NUMBER_0),
      new Menu("<1>",NUMBER_1),
      new Menu("<2>",NUMBER_2),
      new Menu("<3>",NUMBER_3),
    }),
    new Menu("<4-7>",Menu.TITLE_SKIP,new Menu[] {
      new Menu("<4>",NUMBER_4),
      new Menu("<5>",NUMBER_5),
      new Menu("<6>",NUMBER_6),
      new Menu("<7>",NUMBER_7),
    }),
    new Menu("<8-11>",Menu.TITLE_SKIP,new Menu[] {
      new Menu("<8>",NUMBER_8),
      new Menu("<9>",NUMBER_9),
      new Menu("<10>",NUMBER_10),
      new Menu("<11>",NUMBER_11),
    }),
    new Menu("<12-15>",Menu.TITLE_SKIP,new Menu[] {
      new Menu("<12>",NUMBER_12),
      new Menu("<13>",NUMBER_13),
      new Menu("<14>",NUMBER_14),
      new Menu("<15>",NUMBER_15),
    }),
  });

  private Menu financeMenu = new Menu(null,new Menu[] {
    new Menu("pv" ,NUMBER_0),
    new Menu("fv" ,NUMBER_1),
    new Menu("np" ,NUMBER_2),
    new Menu("pmt",NUMBER_3),
    new Menu("ir%",NUMBER_4),
  });

  private Menu intMenu = new Menu("int",new Menu[] {
    new Menu("round",CalcEngine.ROUND),
    new Menu("ceil",CalcEngine.CEIL),
    new Menu("floor",CalcEngine.FLOOR),
    new Menu("trunc",CalcEngine.TRUNC),
    new Menu("frac",CalcEngine.FRAC),
  });

  private Menu coordMenu = new Menu("coord",new Menu[] {
    new Menu("hypot",CalcEngine.HYPOT),
    new Menu("r�p",CalcEngine.RP),
    new Menu("p�r",CalcEngine.PR),
    new Menu("atan_2",CalcEngine.ATAN2),
    new Menu("r�cplx",CalcEngine.TO_CPLX),
  });

  private Menu cplxMenu = new Menu("cplx",new Menu[] {
    new Menu("cplx�r",CalcEngine.CPLX_SPLIT),
    new Menu("abs",CalcEngine.ABS),
    new Menu("arg",CalcEngine.CPLX_ARG),
    new Menu("conj",CalcEngine.CPLX_CONJ),
  });

  private Menu math = new Menu("math",new Menu[] {
    new Menu("simple",Menu.TITLE_SKIP,new Menu[] {
      new Menu("�x",CalcEngine.SQRT),
      new Menu("x^2",CalcEngine.SQR),
      new Menu("1/x",CalcEngine.RECIP),
      new Menu("%chg",CalcEngine.PERCENT_CHG),
      intMenu,
    }),
    new Menu("pow",new Menu[] {
      new Menu("e^x",CalcEngine.EXP),
      new Menu("y^x",CalcEngine.YPOWX),
      new Menu("ln",CalcEngine.LN),
      new Menu("^x^�y",CalcEngine.XRTY),
      new Menu("pow_10,2",new Menu[] {
        new Menu("2^x",CalcEngine.EXP2),
        new Menu("10^x",CalcEngine.EXP10),
        new Menu("log_2",CalcEngine.LOG2),
        new Menu("log_10",CalcEngine.LOG10),
      }),
    }),
    null, // coord or cplx
    new Menu("misc",new Menu[] {
      new Menu("random",CalcEngine.RANDOM),
      new Menu("mod",CalcEngine.MOD),
      new Menu("div",CalcEngine.DIVF),
      new Menu("factorize",CalcEngine.FACTORIZE),
    }),
    new Menu("prob",new Menu[] {
      new Menu("P y,x",CalcEngine.PYX),
      new Menu("C y,x",CalcEngine.CYX),
      new Menu("x!",CalcEngine.FACT),
      new Menu("erfc",CalcEngine.ERFC),
      new Menu("�x",CalcEngine.GAMMA),
    }),
  });
  private Menu trig = new Menu("trig",new Menu[] {
    new Menu("normal",Menu.TITLE_SKIP,new Menu[] {
      new Menu("sin",CalcEngine.SIN),
      new Menu("cos",CalcEngine.COS),
      new Menu("tan",CalcEngine.TAN),
    }),
    new Menu("arc",Menu.TITLE_SKIP,new Menu[] {
      new Menu("asin",CalcEngine.ASIN),
      new Menu("acos",CalcEngine.ACOS),
      new Menu("atan",CalcEngine.ATAN),
    }),
    new Menu("hyp",Menu.TITLE_SKIP,new Menu[] {
      new Menu("sinh",CalcEngine.SINH),
      new Menu("cosh",CalcEngine.COSH),
      new Menu("tanh",CalcEngine.TANH),
    }),
    new Menu("archyp",Menu.TITLE_SKIP,new Menu[] {
      new Menu("asinh",CalcEngine.ASINH),
      new Menu("acosh",CalcEngine.ACOSH),
      new Menu("atanh",CalcEngine.ATANH),
    }),
    new Menu("more",Menu.TITLE_SKIP,new Menu[] {
      new Menu("RAD/DEG",CalcEngine.TRIG_DEGRAD),
      new Menu("�RAD",CalcEngine.TO_RAD),
      new Menu("�DEG",CalcEngine.TO_DEG),
      new Menu("�",CalcEngine.PI),
    }),
  });
  private Menu bitOp = new Menu("bitop",new Menu[] {
    new Menu("and",CalcEngine.AND),
    new Menu("or",CalcEngine.OR),
    new Menu("bic",CalcEngine.BIC),
    new Menu("xor",CalcEngine.XOR),
  });
  private Menu bitMath = new Menu("bitop_2",new Menu[] {
    new Menu("not",CalcEngine.NOT),
    new Menu("y<<x",CalcEngine.YUPX),
    new Menu("y>>x",CalcEngine.YDNX),
    intMenu,
  });
  private Menu prog1 = new Menu("prog",new Menu[] {
    new Menu("new",CalcEngine.PROG_NEW,Menu.PROG_REQUIRED),
    new Menu("run",CalcEngine.PROG_RUN,Menu.PROG_REQUIRED),
    new Menu("draw",CalcEngine.PROG_DRAW,Menu.PROG_REQUIRED),
    new Menu("more",Menu.TITLE_SKIP,new Menu[] {
      new Menu("integrate",CalcEngine.PROG_INTEGR,Menu.PROG_REQUIRED),
      new Menu("diff.",CalcEngine.PROG_DIFF,Menu.PROG_REQUIRED),
      new Menu("solve",CalcEngine.PROG_SOLVE,Menu.PROG_REQUIRED),
      new Menu("min/max",CalcEngine.PROG_MINMAX,Menu.PROG_REQUIRED),
    }),
    new Menu("clear",CalcEngine.PROG_CLEAR,Menu.PROG_REQUIRED),
  });
  private Menu prog2 = new Menu("prog",new Menu[] {
    new Menu("finish",CalcEngine.PROG_FINISH),
    new Menu("cond",new Menu[] {
      new Menu("x=y?",CalcEngine.IF_EQUAL),
      new Menu("x!=y?",CalcEngine.IF_NEQUAL),
      new Menu("x<y?",CalcEngine.IF_LESS),
      new Menu("x<=y?",CalcEngine.IF_LEQUAL),
      new Menu("x>y?",CalcEngine.IF_GREATER),
    }),
    new Menu("util",new Menu[] {
      new Menu("abs",CalcEngine.ABS),
      new Menu("max",CalcEngine.MAX),
      new Menu("min",CalcEngine.MIN),
      new Menu("select",CalcEngine.SELECT),
      new Menu("sgn",CalcEngine.SGN),
    }),
    new Menu("purge",CalcEngine.PROG_PURGE),
    new Menu("mem",new Menu[] {
      new Menu("RCL[x]",CalcEngine.RCL_X),
      new Menu("STO[x]",CalcEngine.STO_X),
      null,
      new Menu("STO+[x]",CalcEngine.STP_X),
    }),
  });
  private Menu progMenu = new Menu(null,new Menu[] {
    new Menu("",NUMBER_0),
    new Menu("",NUMBER_1),
    new Menu("",NUMBER_2),
    new Menu("",NUMBER_3),
    new Menu("more",Menu.TITLE_SKIP,new Menu[] {
      new Menu("",NUMBER_4),
      new Menu("",NUMBER_5),
      new Menu("",NUMBER_6),
      new Menu("",NUMBER_7),
      new Menu("",NUMBER_8),
      // Remember to set CalcEngine.NUM_PROGS accordingly
    }),
  });

  private static final int menuColor [] = {
    0x00e0e0,0x00fc00,0xe0e000,0xfca800,0xfc5400,0xfc0000,
  };

  private Font menuFont;
  private Font boldMenuFont;
  private Font smallMenuFont;
  private Font smallBoldMenuFont;
  private GFont numberFont;
  private int numberFontStyle;
  public CalcEngine calc;

  private final Command add;
  private final Command enter;
  private final Command breakCmd;

  private final Calc midlet;

  private int numRepaintLines;
  private boolean repeating = false;
  private boolean internalRepaint = false;
  private int offX, offY, nDigits, nLines, numberWidth, numberHeight;
  private int offY2, offYMonitor, nLinesMonitor;
  private boolean evenFrame = true;
  private int menuX,menuY,menuW,menuH;

  private Menu [] menuStack;
  private int menuStackPtr;
  private int menuCommand;

  public CalcCanvas(Calc m, DataInputStream in) {
    midlet = m;

    calc = new CalcEngine();

    numberFontStyle = (getWidth()>=160 ? GFont.XLARGE :
                       getWidth()>=128 ? GFont.LARGE  :
                       getWidth()>=96  ? GFont.MEDIUM : GFont.SMALL);
    if (in != null)
      restoreState(in);
    if (!midlet.display.isColor()) {
      numberFontStyle = GFont.SYSTEM;
      // Now, remove the font menu.
      // !!! NB !!! Beware if you change the menu layout
      menu.subMenu[4].subMenu[4].subMenu[0] = null;
    }

    enter = new Command(
      "ENTER", SetupCanvas.commandArrangement[m.commandArrangement*2], 1);
    add   = new Command(
      "+",     SetupCanvas.commandArrangement[m.commandArrangement*2+1], 1);
    breakCmd = new Command(
      "BREAK", SetupCanvas.commandArrangement[m.commandArrangement*2+1], 1);
    addCommand(enter);
    addCommand(add);
    setCommandListener(this);

    menuFont = Font.getFont(
      Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_MEDIUM);
    boldMenuFont = Font.getFont(
      Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_MEDIUM);
    smallMenuFont = Font.getFont(
      Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL);
    smallBoldMenuFont = Font.getFont(
      Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL);
    setNumberFont(numberFontStyle);

    menuStack = new Menu[6]; // One too many, I think
    menuStackPtr = -1;

    // Menu position
    menuW = 21+4*2;
    if (menuW<boldMenuFont.stringWidth("m/ft")+3*2)
      menuW = boldMenuFont.stringWidth("m/ft")+3*2;
    menuW = boldMenuFont.stringWidth("acosh")*2+3*2+menuW;
    if (menuW<(menuFont.stringWidth("thousand")+2*2)*2)
      menuW = (menuFont.stringWidth("thousand")+2*2)*2;
    if (menuW>getWidth()) menuW = getWidth();
    menuH = menuFont.getHeight()*2+3*2+5*2+21;
    if (menuH>getHeight()) menuH = getHeight();
    menuX = (getWidth()-menuW)/2;
    menuY = smallMenuFont.getHeight()+
      (getHeight()-menuH-smallMenuFont.getHeight())/2;
    if (menuY-menuFont.getHeight()-1<smallMenuFont.getHeight())
      menuY = smallMenuFont.getHeight()+menuFont.getHeight()+1;
    if (menuY+menuH > getHeight())
      menuY = getHeight()-menuH;

    numRepaintLines = 100;
    checkRepaint();
  }

  public void saveState(DataOutputStream out) {
    try {
      numberFontStyle = numberFont.getStyle();
      numberFont = null; // Free some memory before saveState()
      out.writeShort(1);
      out.writeByte(numberFontStyle);
      calc.command(CalcEngine.FINALIZE,0);
      calc.saveState(out);
    } catch (IOException ioe) {
    }
  }
  
  private void restoreState(DataInputStream in) {
    try {
      short length = in.readShort();
      if (length >= 1) {
        numberFontStyle = in.readByte();
        length -= 1;
      }
      in.skip(length);
      calc.restoreState(in);
    } catch (IOException ioe) {
    }
  }

  private void setNumberFont(int size) {
    numberFont = null;
    numberFont = new GFont(size | (midlet.bgrDisplay ? GFont.BGR_ORDER : 0));
    numberWidth = numberFont.charWidth();
    nDigits = getWidth()/numberWidth;
    offX = (getWidth()-nDigits*numberWidth)/2;
    numberHeight = numberFont.getHeight();
    nLines = (getHeight()-smallMenuFont.getHeight())/numberHeight;
    offY = (getHeight()-smallMenuFont.getHeight()-nLines*numberHeight)/2 +
      smallMenuFont.getHeight();
    nLinesMonitor = (getHeight()-smallMenuFont.getHeight()-4)/numberHeight;
    offYMonitor = (getHeight()-smallMenuFont.getHeight()-
                   nLinesMonitor*numberHeight)/4+smallMenuFont.getHeight();
    offY2 = 3*(getHeight()-smallMenuFont.getHeight()-
               nLinesMonitor*numberHeight)/4+smallMenuFont.getHeight();
    calc.setMaxWidth(nDigits);
  }

  public void drawModeIndicators(Graphics g, boolean toggleRun) {
    g.setColor(0xffffff);
    g.fillRect(0,0,getWidth(),smallMenuFont.getHeight()-1);
    g.setColor(0);
    g.setFont(smallMenuFont);
    int n = 4;
    if (calc.begin && (calc.progRecording || calc.progRunning))
      n = 5;

    int w = smallMenuFont.stringWidth("ENG");
    int x = getWidth()/(n*2)-w/2;
    if (x<0) x=0;
    
    if (calc.degrees)
      g.drawString("DEG",x,0,g.TOP|g.LEFT);
    else
      g.drawString("RAD",x,0,g.TOP|g.LEFT);

    x += Math.max(getWidth()/n,w+2);
      
    if (calc.format.fse == Real.NumberFormat.FSE_FIX)
      g.drawString("FIX",x,0,g.TOP|g.LEFT);
    else if (calc.format.fse == Real.NumberFormat.FSE_SCI)
      g.drawString("SCI",x,0,g.TOP|g.LEFT);
    else if (calc.format.fse == Real.NumberFormat.FSE_ENG)
      g.drawString("ENG",x,0,g.TOP|g.LEFT);

    x += Math.max(getWidth()/n,w+2);

    if (calc.format.base == 2)
      g.drawString("BIN",x,0,g.TOP|g.LEFT);
    else if (calc.format.base == 8)
      g.drawString("OCT",x,0,g.TOP|g.LEFT);
    else if (calc.format.base == 16)
      g.drawString("HEX",x,0,g.TOP|g.LEFT);

    x += Math.max(getWidth()/n,w+2);

    if (calc.begin)
      g.drawString("BGN",x,0,g.TOP|g.LEFT);

    if (n == 5)
      x += Math.max(getWidth()/n,w+2);

    if (calc.progRecording)
      g.drawString("PRG",x,0,g.TOP|g.LEFT);
    else if (calc.progRunning && (evenFrame || !toggleRun))
      g.drawString("RUN",x,0,g.TOP|g.LEFT);
    if (toggleRun)
      evenFrame = !evenFrame;
  }

  private void clearScreen(Graphics g) {
    // Clear screen and draw mode indicators
    drawModeIndicators(g,false);
    g.setColor(0);
    g.fillRect(0,smallMenuFont.getHeight()-1,getWidth(),
               getHeight()-smallMenuFont.getHeight()+1);
  }

  private boolean plainLabel(String label) {
    for (int i=0; i<label.length(); i++)
      if ("^~_���ߡ����".indexOf(label.charAt(i))>=0)
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
      else if ("���ߡ�".indexOf(c)>=0)
        width += font.charWidth('O');
      else if (c=='�')
        width += font.charWidth('o')*(6+4)/6;
      else if (c=='�') {
        int h2 = (font.getBaselinePosition()*2/3+1)&~1;
        width += h2/2+5+(h2<10?0:1);
      } else if (c=='�')
        width += font.charWidth('e')*67/112 + 1;
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
      } else if ("���ߡ����".indexOf(c)>=0) {
        int w = font.charWidth('O');
        int h = font.getBaselinePosition();
        switch (c) {
          case '�':
            g.drawLine(x,y+h/2+1,x+w-2,y+h/2+1);
            g.drawLine(x,y+h/2+2,x+w-2,y+h/2+2);
            g.drawLine(x+w-2,y+h/2+1,x+w-2-2,y+h/2+1-2);
            g.drawLine(x+w-3,y+h/2+1,x+w-3-1,y+h/2+1-1);
            g.drawLine(x+w-2,y+h/2+2,x+w-2-2,y+h/2+2+2);
            g.drawLine(x+w-3,y+h/2+2,x+w-3-1,y+h/2+2+1);
            break;
          case '�':
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
          case '�':
            g.drawLine(x,y+h-3,x+3,y+h);
            g.drawLine(x+1,y+h-3,x+4,y+h);
            g.drawLine(x+3,y,x+3,y+h);
            g.drawLine(x+4,y,x+4,y+h);
            g.drawLine(x+3,y,x+w,y);
            g.drawLine(x+3,y+1,x+w,y+1);
            overline = true;
            break;
          case '�':
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
          case '�':
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
          case '�':
            g.drawChar('o',x,y+normalFont.getHeight()-
                       smallFont.getBaselinePosition(),g.TOP|g.LEFT);
            g.drawChar('o',x+font.charWidth('o')*4/6,
                       y+normalFont.getHeight()-
                       smallFont.getBaselinePosition(),g.TOP|g.LEFT);
            w = font.charWidth('o')*(6+4)/6;
            break;
          case '�':
            int h2 = (h*2/3+1)&~1;
            w = h2/2+5+(h2<10?0:1);
            int x2 = x+w-h2/2;
            int y2 = y+h-h2;
            g.drawLine(x+w-2,y2,x2,y+h-3);
            g.drawLine(x+w-3,y2,x2-1,y+h-3);
            g.drawLine(x2-2,y+h-3,x2-1,y+h-2);
            g.fillRect(x2-4,y+h-2,3,2);
            g.drawLine(x2-5,y+h-2,x2-4,y+h-3);
            if (h2<10) {
              g.fillRect(x2-6,y2+2,2,y+h-y2-4);
            } else {
              int h3 = (h2-1)/4;
              g.fillRect(x2-6,y2+2,2,h3);
              g.fillRect(x2-7,y2+2+h3,2,y+h-y2-4-2*h3);
              g.fillRect(x2-6,y+h-2-h3,2,h3);
            }
            g.drawLine(x2-5,y2+1,x2-4,y2+2);
            g.fillRect(x2-4,y2,2,2);
            g.drawLine(x2-2,y2+2,x2-2,y2+1);
            g.drawLine(x2-1,y2+2,x+w-3,y+h-1);
            g.drawLine(x+w-2,y+h-1,x+w-2,y+h-2);
            g.drawLine(x+w-1,y+h-2,x+w-1,y+h-3);
            break;
          case '�':
            g.drawChar('e',x,y,g.TOP|g.LEFT);
            x += font.charWidth('e')*67/112;
            g.setColor(menuColor[menuStackPtr]);
            g.fillRect(x,y,w,h);
            g.setColor(0);
            w = 1;
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
    boolean bold = menu.subMenu==null && (menu.flags&Menu.SUBMENU_REQUIRED)==0;
    int width = labelWidth(menu.label,bold);
    if ((anchor & g.RIGHT) != 0)
      x -= width;
    else if ((anchor & g.HCENTER) != 0)
      x -= width/2;
    if ((anchor & g.BOTTOM) != 0)
      y -= menuFont.getHeight();
    drawLabel(g,menu.label,bold,x,y);
  }

  private void drawMenu(Graphics g) {
    int w = menuW;
    int h = menuH;
    int x = menuX;
    int y = menuY;
    int ym = ((y+h-3)-menuFont.getHeight()+(y+3))/2;
    // Draw menu title
    g.setColor(menuColor[menuStackPtr]/4);
    g.fillRect(x,y-menuFont.getHeight()-1,w/2,menuFont.getHeight()+1);
    g.setColor(menuColor[menuStackPtr]);
    g.setFont(menuFont);
    int titleStackPtr = menuStackPtr;
    while ((menuStack[titleStackPtr].flags & Menu.TITLE_SKIP)!=0)
      titleStackPtr--;
    String label = menuStack[titleStackPtr].label;
    drawLabel(g,label,false,x+2,y-menuFont.getHeight());
    // Draw 3D menu background
    g.fillRect(x+2,y+2,w-4,h-4);
    g.setColor((menuColor[menuStackPtr]+0xfcfcfc)/2);
    g.fillRect(x,y+1,2,h-1);
    g.setColor(menuColor[menuStackPtr]/2);
    g.fillRect(x+w-2,y+1,2,h-1);
    g.setColor((menuColor[menuStackPtr]+0xfcfcfc)/2);
    g.fillRect(x,y,w,1);
    g.fillRect(x+1,y+1,w-2,1);
    g.setColor(menuColor[menuStackPtr]/2);
    g.fillRect(x+2,y+h-2,w-4,1);
    g.fillRect(x+1,y+h-1,w-2,1);
    // Draw menu items
    g.setColor(0);
    Menu [] subMenu = menuStack[menuStackPtr].subMenu;
    if (subMenu.length>=1)
      drawMenuItem(g,subMenu[0],x+w/2,y+3,g.TOP|g.HCENTER);
    if (subMenu.length>=2)
      drawMenuItem(g,subMenu[1],x+3,ym,g.TOP|g.LEFT);
    if (subMenu.length>=3)
      drawMenuItem(g,subMenu[2],x+w-3,ym,g.TOP|g.RIGHT);
    if (subMenu.length>=4)
      drawMenuItem(g,subMenu[3],x+w/2,y+h-3,g.BOTTOM|g.HCENTER);
    if (subMenu.length>=5)
      drawMenuItem(g,subMenu[4],x+w/2,ym,
                   g.TOP|g.HCENTER);
    else {
      // Draw a small "joystick" in the center
      y += h/2;
      x += w/2;
      g.setColor(menuColor[menuStackPtr]/4*3);
      g.fillRect(x-1,y-10,3,21);
      g.fillRect(x-10,y-1,21,3);
      g.fillArc(x-5,y-5,11,11,0,360);
    }
  }

  private void drawNumber(Graphics g, int i, boolean cleared, int offY,
                          int nLines) {
    if (i==0 && calc.inputInProgress) {
      StringBuffer tmp = calc.inputBuf;
      tmp.append('_');
      if (tmp.length()>nDigits)
        numberFont.drawString(g,offX,offY+(nLines-1)*numberHeight,tmp,
                              tmp.length()-nDigits);
      else {
        numberFont.drawString(g,offX,offY+(nLines-1)*numberHeight,tmp,0);
        if (!cleared) {
          g.setColor(0);
          g.fillRect(offX+tmp.length()*numberWidth,
                     offY+(nLines-1)*numberHeight,
                     (nDigits-tmp.length())*numberWidth,numberHeight);
        }
      }
      tmp.setLength(tmp.length()-1);
    } else {
      String tmp = calc.getStackElement(i);
      if (tmp.length()>nDigits)
        tmp = "*****";
      numberFont.drawString(
        g,offX+(nDigits-tmp.length())*numberWidth,
        offY+(nLines-1-i)*numberHeight,tmp);
      if (!cleared) {
        g.setColor(0);
        g.fillRect(offX,offY+(nLines-1-i)*numberHeight,
                   (nDigits-tmp.length())*numberWidth,numberHeight);
      }
    }
  }

  private void drawMonitor(Graphics g, int i, boolean cleared) {
    String tmp = calc.getMonitorElement(i);
    String label = calc.getMonitorLabel(i);
    if (tmp.length()+label.length()>nDigits)
      tmp = "*****";
    numberFont.drawString(
      g,offX+(nDigits-tmp.length())*numberWidth,
      offYMonitor+i*numberHeight,tmp);
    numberFont.drawString(g,offX,offYMonitor+i*numberHeight,label);
    if (!cleared) {
      g.setColor(0);
      g.fillRect(offX+label.length()*numberWidth,
                 offYMonitor+i*numberHeight,
                 (nDigits-tmp.length()-label.length())*numberWidth,
                 numberHeight);
    }
  }

  public void paint(Graphics g) {
    boolean cleared = false;
    int i;
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
        int monitorSize = calc.getMonitorSize();
        if (monitorSize > 0) {
          if (monitorSize > nLinesMonitor-1)
            monitorSize = nLinesMonitor-1;
          if (numRepaintLines > nLinesMonitor-monitorSize)
            numRepaintLines = nLinesMonitor-monitorSize;

          for (i=0; i<numRepaintLines; i++)
            drawNumber(g,i,cleared,offY2,nLinesMonitor);
          for (i=0; i<monitorSize; i++)
            drawMonitor(g,i,cleared);
          g.setColor(255,255,255);
          g.drawLine(0,offYMonitor+monitorSize*numberHeight+1,
                     getWidth(),offYMonitor+monitorSize*numberHeight+1);
        } else {
          if (numRepaintLines > nLines)
            numRepaintLines = nLines;
          for (i=0; i<numRepaintLines; i++)
            drawNumber(g,i,cleared,offY,nLines);
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

  private void clearKeyPressed() {
    if (menuStackPtr >= 0) {
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

  private void clearKeyRepeated() {
    if (menuStackPtr >= 0) {
      menuStackPtr = -2; // should not continue by clearing the input...
      numRepaintLines = 100; // Force repaint of all
    } else {
      if (!calc.inputInProgress || menuStackPtr == -2)
        return;
      calc.command(CalcEngine.CLEAR,0);
    }
  }

  private void menuAction(int menuIndex) {
    boolean graph=false;
    
    if (menuStackPtr < 0) {
      // On entering the menu, switch math/trig menus with bit-op
      // menus if not base-10
      if (calc.format.base == 10) {
        menu.subMenu[1] = math;
        menu.subMenu[2] = trig;
        // Also switch coord menu with cplx menu if x or y are complex
        if (calc.imagStack != null &&
            (!calc.imagStack[0].isZero() ||
             !calc.imagStack[1].isZero())) {
          math.subMenu[2] = cplxMenu;
        } else {
          math.subMenu[2] = coordMenu;
        }
      } else {
        menu.subMenu[1] = bitOp;
        menu.subMenu[2] = bitMath;
      }
      // Switch between prog1 and prog2 if recording a program
      if (calc.progRecording) {
        menu.subMenu[4].subMenu[1] = prog2;
      } else {
        menu.subMenu[4].subMenu[1] = prog1;        
      }
    }
    if (menuStackPtr < 0 && menuIndex < 4) {
      // Go directly to submenu
      menuStack[0] = menu;
      menuStack[1] = menu.subMenu[menuIndex];
      menuStackPtr = 1;
      numRepaintLines = 0; // Force repaint of menu
    } else if (menuStackPtr < 0) {
      // Open top level menu
      menuStack[0] = menu;
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
        if (subItem == progMenu.subMenu[4])
          for (int i=0; i<5; i++)
            progMenu.subMenu[4].subMenu[i].label = calc.progLabels[i+4];
        numRepaintLines = 0; // Force repaint of menu
      } else if ((subItem.flags & Menu.SUBMENU_REQUIRED)!=0) {
        // Open number/finance/program submenu
        Menu sub =
          (subItem.flags & Menu.NUMBER_REQUIRED )!=0 ? numberMenu :
          (subItem.flags & Menu.FINANCE_REQUIRED)!=0 ? financeMenu :
          progMenu;
        // Set correct labels
        if (sub == progMenu)
          for (int i=0; i<4; i++)
            progMenu.subMenu[i].label = calc.progLabels[i];
        menuCommand = subItem.command; // Save current command
        sub.label = subItem.label; // Set correct label
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
        } else if (command >= FONT_SMALL && command <= FONT_XLARGE) {
          // Internal font command
          setNumberFont(command-FONT_SMALL);
        } else if (command >= NUMBER_0 && command <= NUMBER_15) {
          // Number has been entered for previous command
          if (menuCommand == CalcEngine.PROG_DRAW ||
              menuCommand == CalcEngine.PROG_SOLVE ||
              menuCommand == CalcEngine.PROG_INTEGR ||
              menuCommand == CalcEngine.PROG_MINMAX) {
            menuCommand += command-NUMBER_0;
            graph = true;
          } if (menuCommand == CalcEngine.PROG_NEW) {
            int n = command-NUMBER_0;
            String name = calc.progLabels[n]==calc.emptyProg ? "" :
              calc.progLabels[n];
            midlet.askNewProgram(name,n);
          } else {
            calc.command(menuCommand,command-NUMBER_0);
          }
        } else if (command >= CalcEngine.AVG_DRAW &&
                   command <= CalcEngine.POW_DRAW) {
          menuCommand = command;
          graph = true;
        } else {
          // Normal calculator command
          calc.command(command,0);
        }
        menuStackPtr = -1;     // Remove menu
        numRepaintLines = 100; // Force repaint of all
      }
    }

    if (graph && calc.prepareGraph(menuCommand)) {
      evenFrame = true;
      midlet.displayGraph(0,smallMenuFont.getHeight()-1,getWidth(),
                          getHeight()-smallMenuFont.getHeight()+1);
      numRepaintLines = 100;
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
            numRepaintLines = 100; // Force repaint of all
            break;
          } else {
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
        }
        calc.command(CalcEngine.DIGIT_0+key-'0',0);
        break;
      case '\b':
        clearKeyPressed();
        break;
      case '#': case '-': case 'e': case 'E':
        if (!midlet.hasClearKey) {
          clearKeyPressed();
          break;
        }
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
        if (menuStackPtr >= 0)
          return;
        calc.command(CalcEngine.ENTER,0);
        break;
      case '+':
        if (menuStackPtr >= 0)
          return;
        calc.command(CalcEngine.ADD,0);
        break;
      default:
        switch (getGameAction(key)) {
          case UP:    menuIndex = 0; break;
          case DOWN:  menuIndex = 3; break;
          case LEFT:  menuIndex = 1; break;
          case RIGHT: menuIndex = 2; break;
          case FIRE:  menuIndex = 4; break;
          case GAME_A: case GAME_B: case GAME_C: case GAME_D:
            // I have no idea how these keys are mapped, I just hope
            // one of them is mapped to something that we can use as
            // a "clear" key
            clearKeyPressed();
            break;
          default:
            // Some keys are not mapped to game keys and must be
            // handled directly in this dirty fashion...
            switch (key) {
              case -1: menuIndex = 0; break; // UP
              case -2: menuIndex = 3; break; // DOWN
              case -3: menuIndex = 1; break; // LEFT
              case -4: menuIndex = 2; break; // RIGHT
              case -5: menuIndex = 4; break; // PUSH
              case -8:
              default: // Clear key could be mapped as something else...
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
  }

  protected void keyRepeated(int key) {
    switch (key) {
      case '1': case '2': case '3': case '4': case '5': case '6':
        if (repeating || menuStackPtr >= 0)
          return;
        calc.command(CalcEngine.DIGIT_A+key-'1',0);
        break;
      case '0': case '7': case '8': case '9':
      case '#':
        if (!midlet.hasClearKey)
          clearKeyRepeated();
        break;
      case '*':
        // Do nothing, but do not fall into the "default" below
        break;
      default:
        switch (getGameAction(key)) {
          case GAME_A: case GAME_B: case GAME_C: case GAME_D:
            clearKeyRepeated();
            break;
          default:
            if (key == -8)
              clearKeyRepeated();
            break;
        }
        break;
    }
    repeating = true;
    checkRepaint();
  }

  protected void pointerPressed(int x, int y) {
    int menuIndex, q=0;

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
