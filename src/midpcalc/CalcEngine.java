package ral;

import javax.microedition.lcdui.*;
import java.io.*;

public final class CalcEngine
{
  // Commands == "keystrokes"
  public static final int DIGIT_0        =   0;
  public static final int DIGIT_1        =   1;
  public static final int DIGIT_2        =   2;
  public static final int DIGIT_3        =   3;
  public static final int DIGIT_4        =   4;
  public static final int DIGIT_5        =   5;
  public static final int DIGIT_6        =   6;
  public static final int DIGIT_7        =   7;
  public static final int DIGIT_8        =   8;
  public static final int DIGIT_9        =   9;
  public static final int DIGIT_A        =  10;
  public static final int DIGIT_B        =  11;
  public static final int DIGIT_C        =  12;
  public static final int DIGIT_D        =  13;
  public static final int DIGIT_E        =  14;
  public static final int DIGIT_F        =  15;
  public static final int SIGN_E         =  16;
  public static final int DEC_POINT      =  17;
  public static final int ENTER          =  18;
  public static final int CLEAR          =  19;
  public static final int ADD            =  20;
  public static final int SUB            =  21;
  public static final int MUL            =  22;
  public static final int DIV            =  23;
  public static final int NEG            =  24;
  public static final int RECIP          =  25;
  public static final int SQR            =  26;
  public static final int SQRT           =  27;
  public static final int PERCENT        =  28;
  public static final int PERCENT_CHG    =  29;
  public static final int YPOWX          =  30;
  public static final int XRTY           =  31;
  public static final int LN             =  32;
  public static final int EXP            =  33;
  public static final int LOG10          =  34;
  public static final int EXP10          =  35;
  public static final int LOG2           =  36;
  public static final int EXP2           =  37;
  public static final int PYX            =  38;
  public static final int CYX            =  39;
  public static final int FACT           =  40;
  public static final int GAMMA          =  41;
  public static final int RP             =  42;
  public static final int PR             =  43;
  public static final int ATAN2          =  44;
  public static final int HYPOT          =  45;
  public static final int SIN            =  46;
  public static final int COS            =  47;
  public static final int TAN            =  48;
  public static final int ASIN           =  49;
  public static final int ACOS           =  50;
  public static final int ATAN           =  51;
  public static final int SINH           =  52;
  public static final int COSH           =  53;
  public static final int TANH           =  54;
  public static final int ASINH          =  55;
  public static final int ACOSH          =  56;
  public static final int ATANH          =  57;
  public static final int PI             =  58;
  public static final int AND            =  59;
  public static final int OR             =  60;
  public static final int XOR            =  61;
  public static final int BIC            =  62;
  public static final int NOT            =  63;
  public static final int YUPX           =  64;
  public static final int YDNX           =  65;
  public static final int XCHG           =  66;
  public static final int CLS            =  67;
  public static final int RCLST          =  68;
  public static final int LASTX          =  69;
  public static final int UNDO           =  70;
  public static final int ROUND          =  71;
  public static final int CEIL           =  72;
  public static final int FLOOR          =  73;
  public static final int TRUNC          =  74;
  public static final int FRAC           =  75;
  public static final int STO            =  76;
  public static final int STP            =  77;
  public static final int RCL            =  78;
  public static final int XCHGMEM        =  79;
  public static final int CLMEM          =  80;
  public static final int SUMPL          =  81;
  public static final int SUMMI          =  82;
  public static final int CLST           =  83;
  public static final int AVG            =  84;
  public static final int AVGXW          =  85;
  public static final int STDEV          =  86;
  public static final int PSTDEV         =  87;
  public static final int LIN_AB         =  88;
  public static final int LIN_YEST       =  89;
  public static final int LIN_XEST       =  90;
  public static final int LIN_R          =  91;
  public static final int LOG_AB         =  92;
  public static final int LOG_YEST       =  93;
  public static final int LOG_XEST       =  94;
  public static final int LOG_R          =  95;
  public static final int EXP_AB         =  96;
  public static final int EXP_YEST       =  97;
  public static final int EXP_XEST       =  98;
  public static final int EXP_R          =  99;
  public static final int POW_AB         = 100;
  public static final int POW_YEST       = 101;
  public static final int POW_XEST       = 102;
  public static final int POW_R          = 103;
  public static final int N              = 104;
  public static final int SUMX           = 105;
  public static final int SUMXX          = 106;
  public static final int SUMLNX         = 107;
  public static final int SUMLN2X        = 108;
  public static final int SUMY           = 109;
  public static final int SUMYY          = 110;
  public static final int SUMLNY         = 111;
  public static final int SUMLN2Y        = 112;
  public static final int SUMXY          = 113;
  public static final int SUMXLNY        = 114;
  public static final int SUMYLNX        = 115;
  public static final int SUMLNXLNY      = 116;
  public static final int NORM           = 117;
  public static final int FIX            = 118;
  public static final int SCI            = 119;
  public static final int ENG            = 120;
  public static final int POINT_DOT      = 121;
  public static final int POINT_COMMA    = 122;
  public static final int POINT_REMOVE   = 123;
  public static final int POINT_KEEP     = 124;
  public static final int THOUSAND_DOT   = 125;
  public static final int THOUSAND_SPACE = 126;
  public static final int THOUSAND_QUOTE = 127;
  public static final int THOUSAND_NONE  = 128;
  public static final int BASE_BIN       = 129;
  public static final int BASE_OCT       = 130;
  public static final int BASE_DEC       = 131;
  public static final int BASE_HEX       = 132;
  public static final int TRIG_DEGRAD    = 133;
  public static final int TO_DEG         = 134;
  public static final int TO_RAD         = 135;
  public static final int RANDOM         = 136;
  public static final int TO_DHMS        = 137;
  public static final int TO_H           = 138;
  public static final int DHMS_PLUS      = 139;
  public static final int TIME           = 140;
  public static final int DATE           = 141;
  public static final int FACTORIZE      = 142;
  public static final int FINANCE_STO    = 143;
  public static final int FINANCE_RCL    = 144;
  public static final int FINANCE_SOLVE  = 145;
  public static final int FINANCE_CLEAR  = 146;
  public static final int FINANCE_BGNEND = 147;
  public static final int FINANCE_MULINT = 148;
  public static final int FINANCE_DIVINT = 149;
  public static final int MONITOR_NONE   = 150;
  public static final int MONITOR_MEM    = 151;
  public static final int MONITOR_STAT   = 152;
  public static final int MONITOR_FINANCE= 153;
  public static final int SIGN_POINT_E   = 154;
  public static final int TO_CPLX        = 155;
  public static final int CPLX_SPLIT     = 156;
  public static final int ABS            = 157;
  public static final int CPLX_ARG       = 158;
  public static final int CPLX_CONJ      = 159;
  public static final int ERFC           = 160;
  public static final int MOD            = 161;
  public static final int DIVF           = 162;
  public static final int XCHGST         = 163;
  public static final int ROLLDN         = 164;
  public static final int ROLLUP         = 165;
  public static final int CONST_c        = 166;
  public static final int CONST_h        = 167;
  public static final int CONST_mu_0     = 168;
  public static final int CONST_eps_0    = 169;
  public static final int CONST_NA       = 170;
  public static final int CONST_R        = 171;
  public static final int CONST_k        = 172;
  public static final int CONST_F        = 173;
  public static final int CONST_alpha    = 174;
  public static final int CONST_a_0      = 175;
  public static final int CONST_R_inf    = 176;
  public static final int CONST_mu_B     = 177;
  public static final int CONST_e        = 178;
  public static final int CONST_m_e      = 179;
  public static final int CONST_m_p      = 180;
  public static final int CONST_m_n      = 181;
  public static final int CONST_m_u      = 182;
  public static final int CONST_G        = 183;
  public static final int CONST_g_n      = 184;
  public static final int CONST_ly       = 185;
  public static final int CONST_AU       = 186;
  public static final int CONST_pc       = 187;
  public static final int CONST_km_mi    = 188;
  public static final int CONST_m_ft     = 189;
  public static final int CONST_cm_in    = 190;
  public static final int CONST_km_nm    = 191;
  public static final int CONST_m_yd     = 192;
  public static final int CONST_g_oz     = 193;
  public static final int CONST_kg_lb    = 194;
  public static final int CONST_mg_gr    = 195;
  public static final int CONST_kg_ton   = 196;
  public static final int CONST_J_cal    = 197;
  public static final int CONST_J_Btu    = 198;
  public static final int CONST_W_hp     = 199;
  public static final int CONST_l_pt     = 200;
  public static final int CONST_l_cup    = 201;
  public static final int CONST_l_gal    = 202;
  public static final int CONST_ml_floz  = 203;
  public static final int CONST_K_C      = 204;
  public static final int CONV_C_F       = 205;
  public static final int CONV_F_C       = 206;
  public static final int IF_EQUAL       = 207;
  public static final int IF_NEQUAL      = 208;
  public static final int IF_LESS        = 209;
  public static final int IF_LEQUAL      = 210;
  public static final int IF_GREATER     = 211;
  public static final int MIN            = 212;
  public static final int MAX            = 213;
  public static final int SELECT         = 214;
  public static final int RCL_X          = 215;
  public static final int STO_X          = 216;
  public static final int STP_X          = 217;
  public static final int TIME_NOW       = 218;
  public static final int DHMS_TO_UNIX   = 219;
  public static final int UNIX_TO_DHMS   = 220;
  public static final int DHMS_TO_JD     = 221;
  public static final int JD_TO_DHMS     = 222;
  public static final int DHMS_TO_MJD    = 223;
  public static final int MJD_TO_DHMS    = 224;
  public static final int SGN            = 225;
  public static final int PROG_NEW       = 226;
  public static final int PROG_FINISH    = 227;
  public static final int PROG_RUN       = 228;
  public static final int PROG_PURGE     = 229;
  public static final int PROG_CLEAR     = 230;

  // These commands are handled from CalcCanvas
  public static final int AVG_DRAW       = 300;
  public static final int LIN_DRAW       = 301;
  public static final int LOG_DRAW       = 302;
  public static final int EXP_DRAW       = 303;
  public static final int POW_DRAW       = 304;
  public static final int PROG_DRAW      = 305; // Uses 9 consecutive slots
  //public static final int PROG_DRAW2   = 306;
  //public static final int PROG_DRAW3   = 307;
  //public static final int PROG_DRAW4   = 308;
  //public static final int PROG_DRAW5   = 309;
  //public static final int PROG_DRAW6   = 310;
  //public static final int PROG_DRAW7   = 311;
  //public static final int PROG_DRAW8   = 312;
  //public static final int PROG_DRAW9   = 313;

  // Special commands
  public static final int FINALIZE       = 500;
  public static final int FREE_MEM       = 501;
  
  private static final int STACK_SIZE    = 16;
  private static final int MEM_SIZE      = 16;
  private static final int STAT_SIZE     = 13;
  private static final int STATLOG_SIZE  = 64;
  private static final int FINANCE_SIZE  = 5;
  private static final int NUM_PROGS     = 9;

  private static final Real Real180 = new Real(180);
  private static final String empty = "";

  public Real [] stack;
  public Real [] imagStack;
  public String [] strStack;
  public Real lastx,lasty,lastz;
  public Real lastxi,lastyi,lastzi;

  public Real [] mem;
  public Real [] imagMem;

  public Real SUM1,SUMx,SUMx2,SUMy,SUMy2,SUMxy;
  public Real SUMlnx,SUMln2x,SUMlny,SUMln2y,SUMxlny,SUMylnx,SUMlnxlny;
  public Real [] stat;
  public int [] statLog; // Low-precision statistics log
  public int statLogStart,statLogSize;

  public Real PV,FV,NP,PMT,IR;
  public Real [] finance;
  public boolean begin;

  public Real.NumberFormat format;
  public boolean degrees;
  public StringBuffer inputBuf;
  public boolean inputInProgress;
  private Real rTmp,rTmp2,rTmp3,rTmp4;
  private int repaintLines;

  public int monitorMode;
  public int monitorSize;
  public Real [] monitors;
  public Real [] imagMonitors;
  public String [] monitorStr;
  public String [] monitorLabels;  
  private static final String [] memLabels =
    { "M0=","M1=","M2=","M3=","M4=","M5=","M6=","M7=",
      "M8=","M9=","M10=","M11=","M12=","M13=","M14=","M15=" };
  private static final String [] statLabels =
    { "n=","$x=","$y=","$x\"=","$y\"=","$xy=","$&x=","$&\"x=",
      "$&y=","$&\"y=","$x&y=","$y&x=","$&x&y=" };
  private static final String [] financeLabels =
    { "pv=","fv=","np=","pmt=","ir%=" };

  private static final int UNDO_NONE   = 0;
  private static final int UNDO_UNARY  = 1;
  private static final int UNDO_BINARY = 2;
  private static final int UNDO_TRINARY= 3;
  private static final int UNDO_PUSH   = 4;
  private static final int UNDO_PUSH2  = 5;
  private static final int UNDO_XY     = 6;
  private static final int UNDO_PUSHXY = 7;
  private static final int UNDO_ROLLDN = 8;
  private static final int UNDO_ROLLUP = 9;
  private static final int UNDO_XCHGST =10;
  private int undoStackEmpty = 0;
  private int undoOp = UNDO_NONE;

  public boolean progRecording;
  public boolean progRunning;
  public static final int PROGLABEL_SIZE = 5;
  public static final String emptyProg = "< >";
  public String [] progLabels;
  private short [][] prog;
  private int progCounter;
  private int currentProg;
  
  public CalcEngine()
  {
    format = new Real.NumberFormat();
    stack = new Real[STACK_SIZE];
    for (int i=0; i<STACK_SIZE; i++)
      stack[i] = new Real();
    strStack = new String[STACK_SIZE];
    monitorStr = new String[MEM_SIZE];

    lastx = new Real();
    lasty = new Real();
    lastz = new Real();
    rTmp = new Real();
    rTmp2 = new Real();
    rTmp3 = new Real();
    rTmp4 = new Real();
    inputBuf = new StringBuffer(40);
    degrees = false;
    begin = false;
    clearStack();
    clearMem();
    clearStat();
    clearFinance();
    monitorMode = MONITOR_NONE;
    monitorSize = 0;
    clearMonitorStrings();

    progLabels = new String[NUM_PROGS];
    for (int i=0; i<NUM_PROGS; i++)
      progLabels[i] = emptyProg;

    Real.accumulateRandomness(System.currentTimeMillis());
  }

  private void clearStrings() {
    for (int i=0; i<STACK_SIZE; i++)
      if (strStack[i] != empty)
        strStack[i] = null;
    clearMonitorStrings();
    repaint(-1);
  }

  private void clearMonitorStrings() {
    for (int i=0; i<MEM_SIZE; i++)
      monitorStr[i] = null;
    repaint(-1);
  }

  private void clearStack() {
    inputInProgress = false;
    for (int i=0; i<STACK_SIZE; i++) {
      stack[i].makeZero();
      strStack[i] = empty;
    }
    clearImagStack();
    repaint(-1);
  }
  
  private void clearImagStack() {
    if (imagStack == null)
      return;
    if (lastxi.isZero())
      lastxi = null;
    lastyi = null;
    lastzi = null;
    for (int i=0; i<STACK_SIZE; i++)
      imagStack[i] = null;
    imagStack = null;
  }
  
  private void clearMem() {
    if (mem == null)
      return;
    for (int i=0; i<MEM_SIZE; i++)
      mem[i] = null;
    mem = null;
    clearImagMem();
  }
  
  private void clearImagMem() {
    if (imagMem == null)
      return;
    for (int i=0; i<MEM_SIZE; i++)
      imagMem[i] = null;
    imagMem = null;
  }
  
  private void clearStat() {
    if (stat == null)
      return;
    SUM1      = null;
    SUMx      = null;
    SUMx2     = null;
    SUMy      = null;
    SUMy2     = null;
    SUMxy     = null;
    SUMlnx    = null;
    SUMln2x   = null;
    SUMlny    = null;
    SUMln2y   = null;
    SUMxlny   = null;
    SUMylnx   = null;
    SUMlnxlny = null;
    for (int i=0; i<STAT_SIZE; i++)
      stat[i] = null;
    stat      = null;
    statLog   = null;
  }

  private void clearFinance() {
    if (finance == null)
      return;
    PV  = null;
    FV  = null;
    NP  = null;
    PMT = null;
    IR  = null;
    for (int i=0; i<FINANCE_SIZE; i++)
      finance[i] = null;
    finance = null;
  }

  private void tryClearImag(boolean clearStack, boolean clearMem) {
    int i;
    if (clearStack && imagStack != null) {
      for (i=0; i<STACK_SIZE; i++)
        if (!imagStack[i].isZero())
          break;
      if (i==STACK_SIZE)
        clearImagStack();
    }
    if (clearMem && imagMem != null) {
      for (i=0; i<MEM_SIZE; i++)
        if (!imagMem[i].isZero())
          break;
      if (i==MEM_SIZE)
        clearImagMem();
    }
  }

  private void tryClearModules(boolean clearMem, boolean clearFinance) {
    int i;
    if (clearMem && mem != null && imagMem == null) {
      for (i=0; i<MEM_SIZE; i++)
        if (!mem[i].isZero())
          break;
      if (i==MEM_SIZE)
        clearMem();
    }
    if (clearFinance && finance != null) {
      for (i=0; i<FINANCE_SIZE; i++)
        if (!finance[i].isZero())
          break;
      if (i==FINANCE_SIZE)
        clearFinance();
    }
  }

  private void allocImagStack() {
    if (imagStack != null)
      return;
    imagStack = new Real[STACK_SIZE];
    for (int i=0; i<STACK_SIZE; i++)
      imagStack[i] = new Real();
    if (lastxi == null)
      lastxi = new Real();
    lastyi = new Real();
    lastzi = new Real();
  }
  
  private void allocMem() {
    if (mem != null)
      return;
    tryClearImag(true,false);
    mem = new Real[MEM_SIZE];
    for (int i=0; i<MEM_SIZE; i++)
      mem[i] = new Real();
    if (monitorMode == MONITOR_MEM) {
      monitors = mem;
      imagMonitors = null;
    }
  }
  
  private void allocImagMem() {
    if (imagMem != null)
      return;
    imagMem = new Real[MEM_SIZE];
    for (int i=0; i<MEM_SIZE; i++)
      imagMem[i] = new Real();
    if (monitorMode == MONITOR_MEM)
      imagMonitors = imagMem;
  }
  
  private void allocStat() {
    if (SUM1 != null)
      return;
    tryClearImag(true,true);
    stat = new Real[STAT_SIZE];
    stat[ 0] = SUM1      = new Real();
    stat[ 1] = SUMx      = new Real();
    stat[ 2] = SUMx2     = new Real();
    stat[ 3] = SUMy      = new Real();
    stat[ 4] = SUMy2     = new Real();
    stat[ 5] = SUMxy     = new Real();
    stat[ 6] = SUMlnx    = new Real();
    stat[ 7] = SUMln2x   = new Real();
    stat[ 8] = SUMlny    = new Real();
    stat[ 9] = SUMln2y   = new Real();
    stat[10] = SUMxlny   = new Real();
    stat[11] = SUMylnx   = new Real();
    stat[12] = SUMlnxlny = new Real();
    statLog = new int[STATLOG_SIZE*2];
    statLogStart = statLogSize = 0;
    if (monitorMode == MONITOR_STAT) {
      monitors = stat;
      imagMonitors = null;
    }
  }

  private void allocFinance() {
    if (finance != null)
      return;
    tryClearImag(true,true);
    finance = new Real[FINANCE_SIZE];
    finance[0] = PV  = new Real();
    finance[1] = FV  = new Real();
    finance[2] = NP  = new Real();
    finance[3] = PMT = new Real();
    finance[4] = IR  = new Real();
    if (monitorMode == MONITOR_FINANCE) {
      monitors = finance;
      imagMonitors = null;
    }
  }

  public void saveState(DataOutputStream out) throws IOException
  {
    tryClearImag(true,true);
    tryClearModules(true,true);

    int i,j;
    byte [] realBuf = new byte[12];

    // Stack
    int stackHeight;
    for (stackHeight=0; stackHeight<STACK_SIZE; stackHeight++)
      if (strStack[stackHeight] == empty)
        break;
    if (stackHeight > 0) {
      out.writeShort(imagStack != null ? STACK_SIZE*12*2 : STACK_SIZE*12);
      for (i=0; i<STACK_SIZE; i++) {
        stack[i].toBytes(realBuf,0);
        out.write(realBuf);
      }
      if (imagStack != null)
        for (i=0; i<STACK_SIZE; i++) {
          imagStack[i].toBytes(realBuf,0);
          out.write(realBuf);
        }
    } else {
      out.writeShort(0);
    }

    //Memory

    if (mem != null) {
      out.writeShort(imagMem != null ? MEM_SIZE*12*2 : MEM_SIZE*12);
      for (i=0; i<MEM_SIZE; i++) {
        mem[i].toBytes(realBuf,0);
        out.write(realBuf);
      }
      if (imagMem != null)
        for (i=0; i<MEM_SIZE; i++) {
          imagMem[i].toBytes(realBuf,0);
          out.write(realBuf);
        }
    } else {
      out.writeShort(0);
    }

    // Statistics
    if (SUM1 != null) {
      out.writeShort(STAT_SIZE*12+STATLOG_SIZE*2*4+2);
      for (i=0; i<STAT_SIZE; i++) {
        stat[i].toBytes(realBuf,0);
        out.write(realBuf);
      }
      for (i=0; i<STATLOG_SIZE*2; i++)
        out.writeInt(statLog[i]);
      out.writeByte(statLogStart);
      out.writeByte(statLogSize);
    } else {
      out.writeShort(0);
    }

    // Finance
    if (finance != null) {
      out.writeShort(FINANCE_SIZE*12);
      for (i=0; i<FINANCE_SIZE; i++) {
        finance[i].toBytes(realBuf,0);
        out.write(realBuf);
      }
    } else {
      out.writeShort(0);
    }

    // Settings
    out.writeShort(10+8*2+12+((lastxi!=null)?12:0));
    out.writeByte(stackHeight);
    out.writeByte((degrees ? 1 : 0) + (begin ? 2 : 0));
    out.writeByte(format.base);
    out.writeByte(format.maxwidth);
    out.writeByte(format.precision);
    out.writeByte(format.fse);
    out.writeByte(format.point);                          
    out.writeBoolean(format.removePoint);
    out.writeByte(format.thousand);
    out.writeByte(((monitorMode-MONITOR_NONE)<<5) + monitorSize);
    out.writeLong(Real.randSeedA);
    out.writeLong(Real.randSeedB);
    lastx.toBytes(realBuf,0);
    out.write(realBuf);
    if (lastxi != null) {
      lastxi.toBytes(realBuf,0);
      out.write(realBuf);
    }

    // Programs
    for (i=0; i<NUM_PROGS; i++) {
      if (prog!=null && prog[i]!=null && prog[i].length!=0) {
        out.writeShort(PROGLABEL_SIZE*2+prog[i].length*2);
        for (j=0; j<PROGLABEL_SIZE; j++)
          out.writeChar(j<progLabels[i].length() ? progLabels[i].charAt(j):0);
        for (j=0; j<prog[i].length; j++)
          out.writeShort(prog[i][j]);
      } else {
        out.writeShort(0);
      }
    }
  }
  
  public void restoreState(DataInputStream in) throws IOException {
    int length,i,j;
    byte [] realBuf = new byte[12];

    repaint(-1); // Better do it now in case of IOException

    // Stack
    length = in.readShort();
    if (length >= STACK_SIZE*12) {
      for (i=0; i<STACK_SIZE; i++) {
        in.read(realBuf);
        stack[i].assign(realBuf,0);
      }
      length -= STACK_SIZE*12;
      if (length >= STACK_SIZE*12) {
        allocImagStack();
        for (i=0; i<STACK_SIZE; i++) {
          in.read(realBuf);
          imagStack[i].assign(realBuf,0);
        }
        length -= STACK_SIZE*12;
      }
    }
    in.skip(length);

    // Memory
    length = in.readShort();
    if (length >= MEM_SIZE*12) {
      allocMem();
      for (i=0; i<MEM_SIZE; i++) {
        in.read(realBuf);
        mem[i].assign(realBuf,0);
      }
      length -= MEM_SIZE*12;
      if (length >= MEM_SIZE*12) {
        allocImagMem();
        for (i=0; i<MEM_SIZE; i++) {
          in.read(realBuf);
          imagMem[i].assign(realBuf,0);
        }
        length -= MEM_SIZE*12;
      }
    }
    in.skip(length);

    // Statistics
    length = in.readShort();
    if (length >= STAT_SIZE*12+STATLOG_SIZE*2*4+2) {
      allocStat();
      for (i=0; i<STAT_SIZE; i++) {
        in.read(realBuf);
        stat[i].assign(realBuf,0);
      }
      for (i=0; i<STATLOG_SIZE*2; i++)
        statLog[i] = in.readInt();
      statLogStart = in.readByte();
      statLogSize  = in.readByte();
      length -= STAT_SIZE*12+STATLOG_SIZE*2*4+2;
    }
    in.skip(length);

    // Finance
    length = in.readShort();
    if (length >= FINANCE_SIZE*12) {
      allocFinance();
      for (i=0; i<FINANCE_SIZE; i++) {
        in.read(realBuf);
        finance[i].assign(realBuf,0);
      }
      length -= FINANCE_SIZE*12;
    }
    in.skip(length);

    // Settings
    length = in.readShort();
    if (length >= 10+8*2+12) {
      int stackHeight = in.readByte();
      for (i=0; i<STACK_SIZE; i++)
        strStack[i] = i<stackHeight ? null : empty;
      int flags = in.readByte();
      degrees            = (flags&1) != 0;
      begin              = (flags&2) != 0;
      format.base        = in.readByte();
      format.maxwidth    = in.readByte();
      format.precision   = in.readByte();
      format.fse         = in.readByte();
      format.point       = (char)in.readByte();
      format.removePoint = in.readBoolean();
      format.thousand    = (char)in.readByte();
      int monitor        = in.readByte();
      Real.randSeedA     = in.readLong();
      Real.randSeedB     = in.readLong();
      in.read(realBuf);
      lastx.assign(realBuf,0);
      monitorMode = MONITOR_NONE+((monitor>>5)&7);
      monitorSize = monitor&0x1f;
      if (monitorMode == MONITOR_MEM) {
        monitors = mem;
        imagMonitors = imagMem;
        monitorLabels = memLabels;
      } else if (monitorMode == MONITOR_STAT) {
        monitors = stat;
        imagMonitors = null;
        monitorLabels = statLabels;
      } else if (monitorMode == MONITOR_FINANCE) {
        monitors = finance;
        imagMonitors = null;
        monitorLabels = financeLabels;
      }
      length -= 10+8*2+12;
    }
    if (length >= 12) {
      if (lastxi == null)
        lastxi = new Real();
      in.read(realBuf);
      lastxi.assign(realBuf,0);
      length -= 12;
    }
    in.skip(length);

    char [] label = new char[PROGLABEL_SIZE];
    for (i=0; i<NUM_PROGS; i++) {
      length = in.readShort();
      if (length >= PROGLABEL_SIZE*2+2) {
        int labelLen=0;
        for (j=0; j<PROGLABEL_SIZE; j++) {
          label[j] = in.readChar();
          if (label[j] != 0)
            labelLen = j+1;
        }
        progLabels[i] = new String(label,0,labelLen);
        if (prog == null)
          prog = new short[NUM_PROGS][];
        prog[i] = new short[(length-PROGLABEL_SIZE*2)/2];
        for (j=0; j<prog[i].length; j++)
          prog[i][j] = in.readShort();
        length -= PROGLABEL_SIZE*2+prog[i].length*2;
      }
      in.skip(length);
    }
  }

  public int numRepaintLines() {
    int tmp = repaintLines;
    repaintLines = 0;
    return tmp;
  }

  private String makeString(Real x, Real xi) {
    String s;
    if (xi != null && !xi.isZero()) {
      int maxwidth = format.maxwidth;
      int imagSign = xi.isNegative() && format.base==10 ? 0 : 1;
      format.maxwidth = maxwidth/2-imagSign;
      String imag = xi.toString(format);
      format.maxwidth = maxwidth-1-imagSign-imag.length();
      String real = x.toString(format);
      if (real.length() < format.maxwidth) {
        format.maxwidth = maxwidth-1-imagSign-real.length();          
        imag = xi.toString(format);
      }
      s = real+(imagSign!=0?"+":"")+imag+'i';
      format.maxwidth = maxwidth;
    } else {
      s = x.toString(format);
    }
    return s;
  }

  public String getStackElement(int n) {
    if (strStack[n] == null) {
      if (imagStack != null)
        strStack[n] = makeString(stack[n],imagStack[n]);
      else
        strStack[n] = makeString(stack[n],null);
    }
    return strStack[n];
  }

  public String getMonitorElement(int n) {
    if (monitorStr[n] == null) {
      format.maxwidth -= monitorLabels[n].length();
      if (monitors != null && monitors[n] != null) {
        if (imagMonitors != null)
          monitorStr[n] = makeString(monitors[n],imagMonitors[n]);
        else
          monitorStr[n] = makeString(monitors[n],null);
      } else {
        monitorStr[n] = Real.ZERO.toString(format);
      }
      format.maxwidth += monitorLabels[n].length();
    }
    return monitorStr[n];
  }

  public String getMonitorLabel(int elementNo) {
    return monitorLabels[elementNo];
  }

  public int getMonitorSize() {
    return monitorSize;
  }

  public void setMaxWidth(int max) {
    format.maxwidth = max;
    if (inputInProgress)
      parseInput();
    clearStrings();
  }

  private void repaint(int nElements) {
    if (nElements < 0)
      nElements = 100;
    if (repaintLines<nElements)
      repaintLines = nElements;
  }

  private void input(int cmd) {
    int i;
    if (!inputInProgress)
      inputBuf.setLength(0);

    // Switch base to 10 after exponent marker (E)
    int base = format.base;
    char exp = 'e';
    if (cmd >= DIGIT_2 && cmd <= DIGIT_F)
      for (i=0; i<inputBuf.length(); i++)
        if (inputBuf.charAt(i)==exp) {
          base = 10;
          break;
        }

    switch (cmd) {
      case CLEAR:
        if (inputBuf.length()==0) {
          inputInProgress = false;
          undo(); // This will undo the "enter" that input started with
          return;
        }
        inputBuf.setLength(inputBuf.length()-1);
        break;
      case DEC_POINT:
        if (inputBuf.length()>0 &&
            inputBuf.charAt(inputBuf.length()-1)==format.point)
        {
          inputBuf.setLength(inputBuf.length()-1);
          break;
        }
        for (i=0; i<inputBuf.length(); i++)
          if (inputBuf.charAt(i)==format.point ||
              inputBuf.charAt(i)==exp)
            return;
        if (inputBuf.length()==0 || inputBuf.charAt(inputBuf.length()-1)=='-')
          inputBuf.append('0');
        else if (inputBuf.length()==1 && inputBuf.charAt(0)=='/')
          inputBuf.append(Real.hexChar.charAt(base-1));
        inputBuf.append(format.point);
        break;
      case SIGN_E:
        if (inputBuf.length()>0 && inputBuf.charAt(inputBuf.length()-1)=='-'){
          inputBuf.setLength(inputBuf.length()-1);
          break;
        }
        if (inputBuf.length()==0 && base!=10) {
          inputBuf.append('/');
          break;
        }
        if (inputBuf.length()==1 && inputBuf.charAt(0)=='/')
          inputBuf.setLength(0);

        if (inputBuf.length()==0 || inputBuf.charAt(inputBuf.length()-1)==exp){
          inputBuf.append('-');
          break;
        }
        for (i=0; i<inputBuf.length(); i++)
          if (inputBuf.charAt(i)==exp)
            return;
        inputBuf.append(exp);
        break;
      case SIGN_POINT_E:
        if (inputBuf.length()>0 && inputBuf.charAt(inputBuf.length()-1)=='-'){
          inputBuf.setLength(inputBuf.length()-1);
          break;
        }
        if (inputBuf.length()==0 && base!=10) {
          inputBuf.append('/');
          break;
        }
        if (inputBuf.length()==1 && inputBuf.charAt(0)=='/')
          inputBuf.setLength(0);

        if (inputBuf.length()==0 || inputBuf.charAt(inputBuf.length()-1)==exp){
          inputBuf.append('-');
          break;
        }
        boolean hasE = false, hasPoint = false;
        for (i=0; i<inputBuf.length(); i++) {
          if (inputBuf.charAt(i)==exp)
            hasE = true;
          if (inputBuf.charAt(i)==format.point)
            hasPoint = true;
        }
        if (!hasE && !hasPoint) {
          inputBuf.append(format.point);
          break;
        }
        if (inputBuf.length()>0 &&
            inputBuf.charAt(inputBuf.length()-1)==format.point)
          inputBuf.setLength(inputBuf.length()-1);
        if (!hasE)
          inputBuf.append(exp);
        break;
      case DIGIT_9:
      case DIGIT_8:
        if (base<10)
          return;
      case DIGIT_7:
      case DIGIT_6:
      case DIGIT_5:
      case DIGIT_4:
      case DIGIT_3:
      case DIGIT_2:
        if (base<8)
          return;
      case DIGIT_0:
      case DIGIT_1:
        inputBuf.append((char)('0'+cmd-DIGIT_0));
        break;
      case DIGIT_A:
      case DIGIT_B:
      case DIGIT_C:
      case DIGIT_D:
      case DIGIT_E:
      case DIGIT_F:
        if (base!=16)
          return;
        if (inputBuf.length()>0) // Just in case... shouldn't happen
          inputBuf.setLength(inputBuf.length()-1);
        inputBuf.append((char)('A'+cmd-DIGIT_A));
        break;
      default:
        return;
    }
    // If routine has not returned yet, we have new input data
    if (!inputInProgress)
      enter();
    inputInProgress = true;
    repaint(1);
  }

  private void parseInput() {
    stack[0].assign(inputBuf.toString(),format.base);
    if (imagStack != null)
      imagStack[0].makeZero();
    strStack[0] = null;
    repaint(1);
    inputInProgress = false;
  }

  private void rollUp(boolean whole) {
    int top;
    if (whole) {
      top = STACK_SIZE-1;
    } else {
      for (top=0; top<STACK_SIZE; top++)
        if (strStack[top] == empty)
          break;
      if (top==0)
        return;
      top--;
    }
    Real tmp = stack[top];
    String tmpStr = strStack[top];
    Real imagTmp = null;
    if (imagStack != null)
      imagTmp = imagStack[top];
    for (int i=top; i>0; i--) {
      stack[i] = stack[i-1];
      strStack[i] = strStack[i-1];
      if (imagStack != null)
        imagStack[i] = imagStack[i-1];
    }
    stack[0] = tmp;
    strStack[0] = tmpStr;
    if (imagStack != null)
      imagStack[0] = imagTmp;
  }

  private void rollDown(boolean whole) {
    int top;
    if (whole) {
      top = STACK_SIZE-1;
    } else {
      for (top=0; top<STACK_SIZE; top++)
        if (strStack[top] == empty)
          break;
      if (top==0)
        return;
      top--;
    }
    Real tmp = stack[0];
    String tmpStr = strStack[0];
    Real imagTmp = null;
    if (imagStack != null)
      imagTmp = imagStack[0];
    for (int i=0; i<top; i++) {
      stack[i] = stack[i+1];
      strStack[i] = strStack[i+1];
      if (imagStack != null)
        imagStack[i] = imagStack[i+1];
    }
    stack[top] = tmp;
    strStack[top] = tmpStr;
    if (imagStack != null)
      imagStack[top] = imagTmp;
  }

  private void xchgSt(int n) {
    Real tmp = stack[0];
    String tmpStr = strStack[0];
    Real imagTmp = null;
    if (imagStack != null)
      imagTmp = imagStack[0];
    stack[0] = stack[n];
    strStack[0] = strStack[n];
    if (imagStack != null)
      imagStack[0] = imagStack[n];
    stack[n] = tmp;
    strStack[n] = tmpStr;
    if (imagStack != null)
      imagStack[n] = imagTmp;
  }

  private void enter() {
    if (inputInProgress)
      parseInput();
    else {
      rollUp(true);
      lasty.assign(stack[0]);
      if (imagStack != null)
        lastyi.assign(imagStack[0]);
      undoStackEmpty = strStack[0]==empty ? 1 : 0;
      undoOp = UNDO_PUSH;
      stack[0].assign(stack[1]);
      if (imagStack != null)
        imagStack[0].assign(imagStack[1]);
      strStack[0] = strStack[1];
      repaint(-1);
    }
  }

  private void toRAD(Real x) {
    if (degrees) {
      x.mul(Real.PI);
      x.div(Real180);
    }
  }

  private void fromRAD(Real x) {
    if (degrees) {
      x.div(Real.PI);
      x.mul(Real180);
    }
  }

  private int greatestFactor(int a) {
    if (a==-1)
      return a;
    if (a<0) a = -a;
    if (a<=3)
      return a;
    while ((a&1) == 0) {
      a >>= 1;
      if (a<2*2) return a;
    }
    while (a%3 == 0) {
      a /= 3;
      if (a<3*3) return a;
    }
    int x=5;
    int s=5*5;
    if (a<s) return a;
    do {
      while (a%x == 0) {
        a /= x;
        if (a<s) return a;
      }
      x += 2;
      s = x*x;
      while (a%x == 0) {
        a /= x;
        if (a<s) return a;
      }
      x += 4;
      s = x*x;
    } while (a>=s && x<46343);
    return a;
  }

  private void binary(int cmd) {
    Real x = stack[0];
    Real y = stack[1];
    lastx.assign(x);
    lasty.assign(y);
    undoStackEmpty = strStack[1]==empty ? strStack[0]==empty ? 2 : 1 : 0;
    undoOp = UNDO_BINARY;

    if (imagStack != null) {
      lastxi.assign(imagStack[0]);
      lastyi.assign(imagStack[1]);
      if (!imagStack[0].isZero() || !imagStack[1].isZero()) {
        imagStack[1].makeZero();
        y.makeNan();
        cmd = -1;
      }
    } else {
      lastxi = null;
    }

    switch (cmd) {
      case MOD:   y.mod(x);                break;
      case DIVF:  y.divf(x);               break;
      case ATAN2: y.atan2(x); fromRAD(y);  break;
      case HYPOT: y.hypot(x);              break;
      case AND:   y.and(x);                break;
      case OR:    y.or(x);                 break;
      case XOR:   y.xor(x);                break;
      case BIC:   y.bic(x);                break;
      case YUPX:  x.round(); y.scalbn(x.toInteger()); break;
      case YDNX:  x.round(); y.scalbn(-x.toInteger());break;
      case PERCENT_CHG:
        x.sub(y);
        x.div(y);
        x.mul(Real.HUNDRED);
        y.assign(x);
        break;
      case DHMS_PLUS:
        x.fromDHMS();
        y.fromDHMS();
        y.add(x);
        y.toDHMS();
        break;
      case PYX:
        // fact(y)/fact(y-x)
        x.neg();
        x.add(y);
        x.fact();
        y.fact();
        y.div(x);
        break;
      case CYX:
        // fact(y)/(fact(y-x)*fact(x))
        rTmp.assign(x);
        rTmp.fact();
        x.neg();
        x.add(y);
        x.fact();
        x.mul(rTmp);
        y.fact();
        y.div(x);
        break;
      case FINANCE_DIVINT:
        y.mul(Real.PERCENT);
        y.add(Real.ONE);
        y.nroot(x);
        y.sub(Real.ONE);
        y.mul(Real.HUNDRED);
        break;
      case FINANCE_MULINT:
        y.mul(Real.PERCENT);
        y.add(Real.ONE);
        y.pow(x);
        y.sub(Real.ONE);
        y.mul(Real.HUNDRED);
        break;
      case MAX:
        if (x.isNan() || y.isNan() ||
            (x.isInfinity() && y.isInfinity() && x.sign == y.sign))
          y.makeNan();
        else if (x.greaterThan(y))
          y.assign(x);
        break;
      case MIN:
        if (x.isNan() || y.isNan() ||
            (x.isInfinity() && y.isInfinity() && x.sign == y.sign))
          y.makeNan();
        else if (x.lessThan(y))
          y.assign(x);
        break;
    }
    rollDown(true);
    stack[STACK_SIZE-1].makeZero();
    if (imagStack != null)
      imagStack[STACK_SIZE-1].makeZero();
    strStack[STACK_SIZE-1] = empty;
    strStack[0] = null;
    repaint(-1);
  }

  private void cplxMul(Real y, Real yi, Real x, Real xi) {
    rTmp.assign(yi);
    rTmp.mul(xi);
    rTmp2.assign(y);
    rTmp2.mul(xi);
    y.mul(x);
    y.sub(rTmp);
    yi.mul(x);
    yi.add(rTmp2);
  }

  private void cplxDiv(Real y, Real yi, Real x, Real xi) {
    rTmp.assign(yi);
    rTmp.mul(xi);
    rTmp2.assign(y);
    rTmp2.mul(xi);
    y.mul(x);
    y.add(rTmp);
    yi.mul(x);
    yi.sub(rTmp2);
    x.sqr();
    xi.sqr();
    x.add(xi);
    y.div(x);
    yi.div(x);
  }

  private void binaryCplx(int cmd) {
    Real x = stack[0];
    Real y = stack[1];
    Real xi = null;
    Real yi = null;
    boolean cplx = false;
    if (imagStack != null) {
      xi = imagStack[0];
      yi = imagStack[1];
      cplx = !xi.isZero() || !yi.isZero();
      lastxi.assign(xi);
      lastyi.assign(yi);
    } else {
      lastxi = null;
    }

    lastx.assign(x);
    lasty.assign(y);
    undoStackEmpty = strStack[1]==empty ? strStack[0]==empty ? 2 : 1 : 0;
    undoOp = UNDO_BINARY;
    switch (cmd) {
      case ADD:
        if (cplx) yi.add(xi);
        y.add(x);
        break;
      case SUB:
        if (cplx) yi.sub(xi);
        y.sub(x);
        break;
      case MUL:
        if (cplx) {
          cplxMul(y,yi,x,xi);
        } else {
          y.mul(x);
        }
        break;
      case DIV:
        if (cplx) {
          cplxDiv(y,yi,x,xi);
        } else {
          y.div(x);
        }
        break;
      case YPOWX:
        if (!y.isZero() && y.isNegative() && !x.isIntegral()) {
          allocImagStack();
          cplx = true;
          xi = imagStack[0];
          yi = imagStack[1];
        }
        if (cplx) {
          cplxLn(y,yi);
          cplxMul(y,yi,x,xi);
          cplxExp(y,yi);
        } else {
          y.pow(x);
        }
        break;
      case XRTY:
        if (!y.isZero() && y.isNegative() && !(x.isIntegral() && x.isOdd())) {
          allocImagStack();
          cplx = true;
          xi = imagStack[0];
          yi = imagStack[1];
        }
        if (cplx) {
          cplxLn(y,yi);
          cplxDiv(y,yi,x,xi);
          cplxExp(y,yi);
        } else {
          y.nroot(x);
        }
        break;
      case TO_CPLX:
        allocImagStack();
        cplx = true;
        yi = imagStack[1];
        yi.assign(y);
        y.assign(x);
        break;
      case CLEAR:
        break;
    }
    if (cplx && (y.isNan() || yi.isNan())) {
      y.makeNan();
      yi.makeZero();
    }
    if (cplx && y.isZero())
      y.abs(); // Remove annoying "-"
    rollDown(true);
    stack[STACK_SIZE-1].makeZero();
    if (imagStack != null)
      imagStack[STACK_SIZE-1].makeZero();
    strStack[STACK_SIZE-1] = empty;
    if (cmd != CLEAR)
      strStack[0] = null;
    repaint(-1);
  }

  private void statAB(Real a, Real b, Real SUMx, Real SUMx2,
                      Real SUMy, Real SUMy2, Real SUMxy)
  {
    // a = (SUMxy-SUMx*SUMy/n)/(SUMx2-sqr(SUMx)/n)
    a.assign(SUMx);
    a.mul(SUMy);
    a.div(SUM1);
    a.neg();
    a.add(SUMxy);
    rTmp.assign(SUMx);
    rTmp.sqr();
    rTmp.div(SUM1);
    rTmp.neg();
    rTmp.add(SUMx2);
    a.div(rTmp);
    // b = (SUMy - a*SUMx)/n
    b.assign(SUMx);
    b.mul(a);
    b.neg();
    b.add(SUMy);
    b.div(SUM1);
  }

  private void unary(int cmd, int param) {
    Real tmp;
    Real x = stack[0];
    lastx.assign(x);
    undoStackEmpty = strStack[0]==empty ? 1 : 0;
    undoOp = UNDO_UNARY;

    if (imagStack != null) {
      lastxi.assign(imagStack[0]);
      if (!imagStack[0].isZero()) {
        imagStack[0].makeZero();
        x.makeNan();
        cmd = -1;
      }
    } else {
      lastxi = null;
    }

    switch (cmd) {
      case FACT:  x.fact();  break;
      case GAMMA: x.gamma(); break;
      case ERFC:  x.erfc();  break;
      case NOT:   x.xor(Real.ONE_N); break;
      case XCHGMEM:
        allocMem();
        tmp = stack[0];
        stack[0] = mem[param];
        mem[param] = tmp;
        if (monitorMode == MONITOR_MEM) {
          monitorStr[param] = null;
          repaint(-1);
        }
        break;
      case TO_DEG:  x.div(Real.PI); x.mul(Real180); break;
      case TO_RAD:  x.mul(Real.PI); x.div(Real180); break;
      case TO_DHMS: x.toDHMS(); break;
      case TO_H:    x.fromDHMS(); break;
      case SGN:
        rTmp.assign(Real.ONE);
        rTmp.copysign(x);
        x.assign(rTmp);
        break;
      case DHMS_TO_UNIX:
      case UNIX_TO_DHMS:
        if (cmd == DHMS_TO_UNIX) {
          x.fromDHMS();
          x.sub(17268672);
          x.mul(3600);
        } else {
          x.div(3600);
          x.add(17268672);
          x.toDHMS();
        }
        break;
      case DHMS_TO_JD:
      case JD_TO_DHMS:
        rTmp.assign(3442119);
        rTmp.scalbn(-1);
        if (cmd == DHMS_TO_JD) {
          x.fromDHMS();
          x.div(24);
          x.add(rTmp);
        } else {
          x.sub(rTmp);
          x.mul(24);
          x.toDHMS();
        }
        break;
      case DHMS_TO_MJD:
      case MJD_TO_DHMS:
        if (cmd == DHMS_TO_MJD) {
          x.fromDHMS();
          x.div(24);
          x.sub(678941);
        } else {
          x.add(678941);
          x.mul(24);
          x.toDHMS();
        }
        break;
      case CONV_C_F:
      case CONV_F_C:
        rTmp.assign(0, 0x40000000, 0x7333333333333333L);
        if (cmd == CONV_C_F) {
          x.mul(rTmp);
          x.add(32);
        } else {
          x.sub(32);
          x.div(rTmp);
        }
        break;
      case LIN_YEST:
        allocStat();
        statAB(rTmp2,rTmp3,SUMx,SUMx2,SUMy,SUMy2,SUMxy);
        x.mul(rTmp2);
        x.add(rTmp3);
        break;
      case LIN_XEST:
        allocStat();
        statAB(rTmp2,rTmp3,SUMx,SUMx2,SUMy,SUMy2,SUMxy);
        x.sub(rTmp3);
        x.div(rTmp2);
        break;
      case LOG_YEST:
        allocStat();
        statAB(rTmp2,rTmp3,SUMlnx,SUMln2x,SUMy,SUMy2,SUMylnx);
        x.ln();
        x.mul(rTmp2);
        x.add(rTmp3);
        break;
      case LOG_XEST:
        allocStat();
        statAB(rTmp2,rTmp3,SUMlnx,SUMln2x,SUMy,SUMy2,SUMylnx);
        x.sub(rTmp3);
        x.div(rTmp2);
        x.exp();
        break;
      case EXP_YEST:
        allocStat();
        statAB(rTmp2,rTmp3,SUMx,SUMx2,SUMlny,SUMln2y,SUMxlny);
        x.mul(rTmp2);
        x.add(rTmp3);
        x.exp();
        break;
      case EXP_XEST:
        allocStat();
        statAB(rTmp2,rTmp3,SUMx,SUMx2,SUMlny,SUMln2y,SUMxlny);
        x.ln();
        x.sub(rTmp3);
        x.div(rTmp2);
        break;
      case POW_YEST:
        allocStat();
        statAB(rTmp2,rTmp3,SUMlnx,SUMln2x,SUMlny,SUMln2y,SUMlnxlny);
        x.ln();
        x.mul(rTmp2);
        x.add(rTmp3);
        x.exp();
        break;
      case POW_XEST:
        allocStat();
        statAB(rTmp2,rTmp3,SUMlnx,SUMln2x,SUMlny,SUMln2y,SUMlnxlny);
        x.ln();
        x.sub(rTmp3);
        x.div(rTmp2);
        x.exp();
        break;
    }
    strStack[0] = null;
    repaint(1);
  }

  private void cplxSqr(Real x, Real xi) {
    rTmp.assign(xi);
    rTmp.sqr();
    xi.mul(x);
    xi.scalbn(1);
    x.sqr();
    x.sub(rTmp);
  }

  private void cplxSqrt(Real x, Real xi) {
    rTmp.assign(x);
    rTmp.hypot(xi);
    rTmp2.assign(x);
    rTmp2.abs();
    rTmp.add(rTmp2);
    rTmp.scalbn(1);
    rTmp.sqrt();
    if (!x.isNegative()) {
      x.assign(rTmp);
      x.scalbn(-1);
      xi.div(rTmp);
    } else {
      x.assign(xi);
      x.abs();
      x.div(rTmp);
      rTmp.copysign(xi);
      xi.assign(rTmp);
      xi.scalbn(-1);
    }
  }

  private void cplxSinh(Real x, Real xi) {
    degrees = false; // "Alert" the user to the fact that we use strictly RAD
    rTmp.assign(x);
    rTmp2.assign(xi);
    x.sinh();
    rTmp2.cos();
    x.mul(rTmp2);
    xi.sin();
    rTmp.cosh();
    xi.mul(rTmp);
  }

  private void cplxCosh(Real x, Real xi) {
    degrees = false; // "Alert" the user to the fact that we use strictly RAD
    rTmp.assign(x);
    rTmp2.assign(xi);
    x.cosh();
    rTmp2.cos();
    x.mul(rTmp2);
    xi.sin();
    rTmp.sinh();
    xi.mul(rTmp);
  }

  private void cplxTanh(Real x, Real xi) {
    rTmp3.assign(x);
    rTmp4.assign(xi);
    cplxSinh(x,xi);
    cplxCosh(rTmp3,rTmp4);
    cplxDiv(x,xi,rTmp3,rTmp4);
  }
  
  private void cplxAsinh(Real x, Real xi) {
    rTmp3.assign(x);
    rTmp4.assign(xi);
    cplxSqr(x,xi);
    x.add(Real.ONE);
    cplxSqrt(x,xi);
    x.add(rTmp3);
    xi.add(rTmp4);
    cplxLn(x,xi);
  }

  private void cplxAcosh(Real x, Real xi) {
    Real rTmp5 = new Real();
    Real rTmp6 = new Real();
    rTmp3.assign(x);
    rTmp4.assign(xi);
    rTmp3.add(Real.ONE);
    cplxSqrt(rTmp3,rTmp4);
    rTmp5.assign(x);
    rTmp6.assign(xi);
    rTmp5.sub(Real.ONE);
    cplxSqrt(rTmp5,rTmp6);
    cplxMul(rTmp3,rTmp4,rTmp5,rTmp6);
    x.add(rTmp3);
    xi.add(rTmp4);
    cplxLn(x,xi);
  }

  private void cplxAtanh(Real x, Real xi) {
    rTmp3.assign(x);
    rTmp4.assign(xi);
    x.add(Real.ONE);
    cplxLn(x,xi);
    rTmp3.neg();
    rTmp4.neg();
    rTmp3.add(Real.ONE);
    cplxLn(rTmp3,rTmp4);
    x.sub(rTmp3);
    xi.sub(rTmp4);
    x.scalbn(-1);
    xi.scalbn(-1);
  }

  private void cplxExp(Real x, Real xi) {
    degrees = false; // "Alert" the user to the fact that we use strictly RAD
    x.exp();
    rTmp.assign(xi);
    xi.sin();
    xi.mul(x);
    rTmp.cos();
    x.mul(rTmp);
  }
  
  private void cplxLn(Real x, Real xi) {
    degrees = false; // "Alert" the user to the fact that we use strictly RAD
    rTmp.assign(x);
    if (!xi.isZero())
      x.hypot(xi);
    else
      x.abs();
    x.ln();
    xi.atan2(rTmp);
  }

  private void unaryCplx(int cmd) {
    Real tmp;
    Real x = stack[0];
    Real xi = null;
    boolean cplx = false;
    if (imagStack != null) {
      xi = imagStack[0];
      cplx = !xi.isZero();
      lastxi.assign(xi);
    } else {
      lastxi = null;
    }
    lastx.assign(x);
    undoStackEmpty = strStack[0]==empty ? 1 : 0;
    undoOp = UNDO_UNARY;
    switch (cmd) {
      case NEG:
        if (cplx)
          xi.neg();
        x.neg();
        break;
      case PERCENT:
        if (imagStack != null)
          cplx |= !imagStack[1].isZero();
        x.mul(Real.PERCENT);
        if (cplx) {
          xi.mul(Real.PERCENT);
          cplxMul(x,xi,stack[1]/*y*/,imagStack[1]/*yi*/);
        } else {
          x.mul(stack[1]/*y*/);
        }
        break;
      case SQR:
        if (cplx) {
          cplxSqr(x,xi);
        } else {
          x.sqr();
        }
        break;
      case RECIP:
        if (cplx) {
          rTmp.assign(x);
          rTmp.sqr();
          rTmp2.assign(xi);
          rTmp2.sqr();
          rTmp.add(rTmp2);
          x.div(rTmp);
          xi.div(rTmp);
          xi.neg();
        } else {
          x.recip();
        }
        break;
      case SQRT:
        if (cplx) {
          cplxSqrt(x,xi);
        } else if (x.isNegative() && !x.isZero()) {
          allocImagStack();
          cplx = true;
          xi = imagStack[0];
          xi.assign(x);
          xi.neg();
          xi.sqrt();
          x.makeZero();
        } else {
          x.sqrt();
        }
        break;
      case ABS:
        if (cplx) {
          x.hypot(xi);
          xi.makeZero();
        } else {
          x.abs();
        }
        break;
      case CPLX_ARG:
        if (cplx) {
          degrees = false;          
          xi.atan2(x);
          x.assign(xi);
          xi.makeZero();
        } else {
          x.makeZero();
        }
        break;
      case CPLX_CONJ:
        if (cplx)
          xi.neg();
        break;
      case COS:
        if (cplx) {
          x.swap(xi);
          cplxCosh(x,xi);
          xi.neg();
        } else {
          toRAD(x);
          x.cos();
        }
        break;
      case COSH:
        if (cplx) {
          cplxCosh(x,xi);
        } else {
          x.cosh();
        }
        break;
      case SIN:
        if (cplx) {
          cplxSinh(xi,x);
        } else {
          toRAD(x);
          x.sin();
        }
        break;
      case SINH:
        if (cplx) {
          cplxSinh(x,xi);
        } else {
          x.sinh();
        }
        break;
      case TAN:
        if (cplx) {
          xi.neg();
          cplxTanh(xi,x);
          xi.neg();
        } else {
          toRAD(x);
          x.tan();
        }
        break;
      case TANH:
        if (cplx) {
          cplxTanh(x,xi);
        } else {
          x.tanh();
        }
        break;
      case ASIN:
        if (!cplx && (x.greaterThan(Real.ONE) || x.lessThan(Real.ONE_N))) {
          allocImagStack();
          cplx = true;
          xi = imagStack[0];
        }
        if (cplx) {
          cplxAsinh(xi,x);
        } else {
          x.asin();
          fromRAD(x);
        }
        break;
      case ASINH:
        if (cplx) {
          cplxAsinh(x,xi);
        } else {
          x.asinh();
        }
        break;
      case ACOS:
        if (!cplx && (x.greaterThan(Real.ONE) || x.lessThan(Real.ONE_N))) {
          allocImagStack();
          cplx = true;
          xi = imagStack[0];
        }
        if (cplx) {
          cplxAsinh(xi,x);
          x.neg();
          xi.neg();
          x.add(Real.PI_2);
        } else {
          x.acos();
          fromRAD(x);
        }
        break;
      case ACOSH:
        if (!cplx && x.lessThan(Real.ONE)) {
          allocImagStack();
          cplx = true;
          xi = imagStack[0];
        }
        if (cplx) {
          cplxAcosh(x,xi);
        } else {
          x.acosh();
        }
        break;
      case ATAN:
        if (cplx) {
          xi.neg();
          cplxAtanh(xi,x);
          xi.neg();
        } else {
          x.atan();
          fromRAD(x);
        }
        break;
      case ATANH:
        if (!cplx && (x.greaterThan(Real.ONE) || x.lessThan(Real.ONE_N))) {
          allocImagStack();
          cplx = true;
          xi = imagStack[0];
        }
        if (cplx) {
          cplxAtanh(x,xi);
        } else {
          x.atanh();
        }
        break;
      case EXP:
        if (cplx) {
          cplxExp(x,xi);
        } else {
          x.exp();
        }
        break;
      case EXP2:
        if (cplx) {
          x.mul(Real.LN2);
          xi.mul(Real.LN2);
          cplxExp(x,xi);
        } else {
          x.exp2();
        }
        break;
      case EXP10:
        if (cplx) {
          x.mul(Real.LN10);
          xi.mul(Real.LN10);
          cplxExp(x,xi);
        } else {
          x.exp10();
        }
        break;
      case LN:
        if (x.isNegative() && !x.isZero()) {
          allocImagStack();
          cplx = true;
          xi = imagStack[0];
        }
        if (cplx) {
          cplxLn(x,xi);
        } else {
          x.ln();
        }
        break;
      case LOG2:
        if (x.isNegative() && !x.isZero()) {
          allocImagStack();
          cplx = true;
          xi = imagStack[0];
        }
        if (cplx) {
          cplxLn(x,xi);
          x.mul(Real.LOG2E);
          xi.mul(Real.LOG2E);
        } else {
          x.log2();
        }
        break;
      case LOG10:
        if (x.isNegative() && !x.isZero()) {
          allocImagStack();
          cplx = true;
          xi = imagStack[0];
        }
        if (cplx) {
          cplxLn(x,xi);
          x.mul(Real.LOG10E);
          xi.mul(Real.LOG10E);
        } else {
          x.log10();
        }
        break;
      case ROUND:
        if (cplx)
          xi.round();
        x.round();
        break;
      case CEIL:
        if (cplx)
          xi.ceil();
        x.ceil();
        break;
      case FLOOR:
        if (cplx)
          xi.floor();
        x.floor();
        break;
      case TRUNC:
        if (cplx)
          xi.trunc();
        x.trunc();
        break;
      case FRAC:
        if (cplx)
          xi.frac();
        x.frac();
        break;
    }
    if (cplx && (x.isNan() || xi.isNan())) {
      x.makeNan();
      xi.makeZero();
    }
    if (cplx && x.isZero())
      x.abs(); // Remove annoying "-"
    strStack[0] = null;
    repaint(1);
  }

  private void xyOp(int cmd) {
    Real x = stack[0];
    Real y = stack[1];
    lastx.assign(x);
    lasty.assign(y);
    if (imagStack != null) {
      lastxi.assign(imagStack[0]);
      lastyi.assign(imagStack[1]);
    } else {
      lastxi = null;
    }
    undoStackEmpty = strStack[1]==empty ? strStack[0]==empty ? 2 : 1 : 0;
    undoOp = UNDO_XY;
    switch (cmd) {
      case RP:
        rTmp.assign(y);
        rTmp.atan2(x);
        x.hypot(y);
        y.assign(rTmp);
        fromRAD(y);
        if (imagStack != null) {
          imagStack[0].makeZero();
          imagStack[1].makeZero();
        }
        strStack[0] = null;
        strStack[1] = null;
        break;
      case PR:
        toRAD(y);
        rTmp.assign(y);
        rTmp.cos();
        y.sin();
        y.mul(x);
        x.mul(rTmp);
        if (imagStack != null) {
          imagStack[0].makeZero();
          imagStack[1].makeZero();
        }
        strStack[0] = null;
        strStack[1] = null;
        break;
      case XCHG:
        Real tmp = stack[0];
        stack[0] = stack[1];
        stack[1] = tmp;
        if (imagStack != null) {
          tmp = imagStack[0];
          imagStack[0] = imagStack[1];
          imagStack[1] = tmp;
        }
        String tmpStr = strStack[0];
        strStack[0] = strStack[1];
        strStack[1] = tmpStr;
        break;
    }
    repaint(2);
  }

  private void push(Real x, Real xi) {
    rollUp(true);
    lasty.assign(stack[0]);
    if (imagStack != null)
      lastyi.assign(imagStack[0]);
    undoStackEmpty = strStack[0]==empty ? 1 : 0;
    undoOp = UNDO_PUSH;
    stack[0].assign(x);
    if (xi != null && !xi.isZero()) {
      allocImagStack();
      imagStack[0].assign(xi);
    } else if (imagStack != null) {
      imagStack[0].makeZero();
    }
    strStack[0] = null;
    repaint(-1);
  }

  private void push(int e, long m) {
    rTmp.assign(0,e,m);
    push(rTmp,null);
  }

  private void cond(int cmd) {
    Real x = stack[0];
    Real y = stack[1];
    if (x.isNan() || y.isNan() ||
        (x.isInfinity() && y.isInfinity() && x.sign == y.sign) ||
        (imagStack!=null && (!imagStack[0].isZero()|| !imagStack[1].isZero())))
    {
      push(Real.NAN, null);
      return;
    }
    switch (cmd) {
      case IF_EQUAL:   push(x.equalTo(y)    ?Real.ONE:Real.ZERO, null); break;
      case IF_NEQUAL:  push(x.notEqualTo(y) ?Real.ONE:Real.ZERO, null); break;
      case IF_LESS:    push(x.lessThan(y)   ?Real.ONE:Real.ZERO, null); break;
      case IF_LEQUAL:  push(x.lessEqual(y)  ?Real.ONE:Real.ZERO, null); break;
      case IF_GREATER: push(x.greaterThan(y)?Real.ONE:Real.ZERO, null); break;
    }
  }

  private void trinaryCplx(int cmd) {
    Real x = stack[0];
    Real y = stack[1];
    Real z = stack[2];
    Real xi = null;
    Real yi = null;
    Real zi = null;
    boolean cplx = false;
    if (imagStack != null) {
      xi = imagStack[0];
      yi = imagStack[1];
      zi = imagStack[2];
      cplx = !xi.isZero() || !yi.isZero() || !zi.isZero();
      lastxi.assign(xi);
      lastyi.assign(yi);
      lastzi.assign(zi);
    } else {
      lastxi = null;
    }

    lastx.assign(x);
    lasty.assign(y);
    lastz.assign(z);
    undoStackEmpty = strStack[2]==empty ? strStack[1]==empty ?
      strStack[0]==empty ? 3 : 2 : 1 : 0;
    undoOp = UNDO_TRINARY;

    switch (cmd) {
      case SELECT:
        rTmp3.assign(Real.ONE);
        rTmp3.sub(x);
        if (cplx) {
          rTmp4.assign(xi);
          rTmp4.neg();
          cplxMul(z,zi,rTmp3,rTmp4);
          cplxMul(y,yi,x,xi);
          z.add(y);
          zi.add(yi);
        } else {
          z.mul(rTmp3);
          y.mul(x);
          z.add(y);
        }
        break;
    }
    // Result is in z...
    if (cplx && (z.isNan() || zi.isNan())) {
      z.makeNan();
      zi.makeZero();
    }
    if (cplx && z.isZero())
      z.abs(); // Remove annoying "-"
    rollDown(true);
    rollDown(true);
    stack[STACK_SIZE-1].makeZero();
    stack[STACK_SIZE-2].makeZero();
    if (imagStack != null) {
      imagStack[STACK_SIZE-1].makeZero();
      imagStack[STACK_SIZE-2].makeZero();
    }
    strStack[STACK_SIZE-1] = empty;
    strStack[STACK_SIZE-2] = empty;
    strStack[0] = null;
    repaint(-1);
  }

  private void sum(int cmd) {
    allocStat();
    Real x = stack[0];
    Real y = stack[1];
    int index;
    switch (cmd) {
      case SUMPL:
        index = (statLogStart+statLogSize)%STATLOG_SIZE;
        statLog[index*2] = x.toFloatBits();
        statLog[index*2+1] = y.toFloatBits();
        if (statLogSize < STATLOG_SIZE)
          statLogSize++;
        else
          statLogStart = (statLogStart+1)%STATLOG_SIZE;

        SUM1.add(Real.ONE);
        SUMx.add(x);
        rTmp.assign(x);
        rTmp.sqr();
        SUMx2.add(rTmp);
        SUMy.add(y);
        rTmp.assign(y);
        rTmp.sqr();
        SUMy2.add(rTmp);
        rTmp.assign(x);
        rTmp.mul(y);
        SUMxy.add(rTmp);
        rTmp2.assign(x);
        rTmp2.ln();
        SUMlnx.add(rTmp2);
        rTmp.assign(rTmp2);
        rTmp.sqr();
        SUMln2x.add(rTmp);
        rTmp3.assign(y);
        rTmp3.ln();
        SUMlny.add(rTmp3);
        rTmp.assign(rTmp3);
        rTmp.sqr();
        SUMln2y.add(rTmp);
        rTmp.assign(x);
        rTmp.mul(rTmp3);
        SUMxlny.add(rTmp);
        rTmp.assign(y);
        rTmp.mul(rTmp2);
        SUMylnx.add(rTmp);
        rTmp2.mul(rTmp3);
        SUMlnxlny.add(rTmp2);
        break;
      case SUMMI:
        // Statistics log: search for point and remove if found
        int xf = x.toFloatBits();
        int yf = y.toFloatBits();
        for (int i=statLogSize-1; i>=0; i--) {
          index = (statLogStart+i)%STATLOG_SIZE;
          if (statLog[index*2]==xf && statLog[index*2+1]==yf) {
            for (; i<statLogSize-1; i++) {
              int index2 = (statLogStart+i+1)%STATLOG_SIZE;
              statLog[index*2] = statLog[index2*2];
              statLog[index*2+1] = statLog[index2*2+1];
              index = index2;
            }
            statLogSize--;
            break;
          }
        }

        SUM1.sub(Real.ONE);
        SUMx.sub(x);
        rTmp.assign(x);
        rTmp.sqr();
        SUMx2.sub(rTmp);
        SUMy.sub(y);
        rTmp.assign(y);
        rTmp.sqr();
        SUMy2.sub(rTmp);
        rTmp.assign(x);
        rTmp.mul(y);
        SUMxy.sub(rTmp);
        rTmp2.assign(x);
        rTmp2.ln();
        SUMlnx.sub(rTmp2);
        rTmp.assign(rTmp2);
        rTmp.sqr();
        SUMln2x.sub(rTmp);
        rTmp3.assign(y);
        rTmp3.ln();
        SUMlny.sub(rTmp3);
        rTmp.assign(rTmp3);
        rTmp.sqr();
        SUMln2y.sub(rTmp);
        rTmp.assign(x);
        rTmp.mul(rTmp3);
        SUMxlny.sub(rTmp);
        rTmp.assign(y);
        rTmp.mul(rTmp2);
        SUMylnx.sub(rTmp);
        rTmp2.mul(rTmp3);
        SUMlnxlny.sub(rTmp2);
        break;
    }
    if (monitorMode == MONITOR_STAT) {
      clearMonitorStrings();
      repaint(-1);
    }
    push(SUM1,null);
  }

  private void stat2(int cmd) {
    allocStat();
    rollUp(true);
    rollUp(true);
    lasty.assign(stack[0]);
    lastz.assign(stack[1]);
    if (imagStack != null) {
      lastyi.assign(imagStack[0]);
      lastzi.assign(imagStack[1]);
      imagStack[0].makeZero();
      imagStack[1].makeZero();
    }
    undoStackEmpty = strStack[1]==empty ? strStack[0]==empty ? 2 : 1 : 0;
    undoOp = UNDO_PUSH2;
    Real x = stack[0];
    Real y = stack[1];
    switch (cmd) {
      case AVG:
        // x_avg = SUMx/n
        x.assign(SUMx);
        x.div(SUM1);
        // y_avg = SUMy/n
        y.assign(SUMy);
        y.div(SUM1);
        break;
      case STDEV:
      case PSTDEV:
        // s_x = sqrt((SUMx2-sqr(SUMx)/n)/(n-1))
        // S_x = sqrt((SUMx2-sqr(SUMx)/n)/n)
        x.assign(SUMx);
        x.sqr();
        x.div(SUM1);
        x.neg();
        x.add(SUMx2);
        rTmp.assign(SUM1);
        if (cmd == STDEV)
          rTmp.sub(Real.ONE);
        x.div(rTmp);
        x.sqrt();
        // s_y = sqrt((SUMy2-sqr(SUMy)/n)/(n-1))
        // S_y = sqrt((SUMy2-sqr(SUMy)/n)/n)
        y.assign(SUMy);
        y.sqr();
        y.div(SUM1);
        y.neg();
        y.add(SUMy2);
        y.div(rTmp);
        y.sqrt();
        break;
      case LIN_AB:
        statAB(x,y,SUMx,SUMx2,SUMy,SUMy2,SUMxy);
        break;
      case LOG_AB:
        statAB(x,y,SUMlnx,SUMln2x,SUMy,SUMy2,SUMylnx);
        break;
      case EXP_AB:
        statAB(x,y,SUMx,SUMx2,SUMlny,SUMln2y,SUMxlny);
        y.exp();
        break;
      case POW_AB:
        statAB(x,y,SUMlnx,SUMln2x,SUMlny,SUMln2y,SUMlnxlny);
        y.exp();
        break;
    }
    strStack[0] = null;
    strStack[1] = null;
    repaint(-1);
  }

  private void statR(Real r, Real SUMx, Real SUMx2,
                     Real SUMy, Real SUMy2, Real SUMxy)
  {
    // r =(SUMxy-SUMx*SUMy/n)/sqrt((SUMx2-sqr(SUMx)/n)*(SUMy2-sqr(SUMy)/n))
    r.assign(SUMx);
    r.sqr();
    r.div(SUM1);
    r.neg();
    r.add(SUMx2);
    rTmp.assign(SUMy);
    rTmp.sqr();
    rTmp.div(SUM1);
    rTmp.neg();
    rTmp.add(SUMy2);
    r.mul(rTmp);
    r.rsqrt();
    rTmp.assign(SUMx);
    rTmp.mul(SUMy);
    rTmp.div(SUM1);
    rTmp.neg();
    rTmp.add(SUMxy);
    r.mul(rTmp);
  }

  private void stat1(int cmd) {
    allocStat();
    rollUp(true);
    lasty.assign(stack[0]);
    if (imagStack != null) {
      lastyi.assign(imagStack[0]);
      imagStack[0].makeZero();
    }
    undoStackEmpty = strStack[0]==empty ? 1 : 0;
    undoOp = UNDO_PUSH;
    Real x = stack[0];
    switch (cmd) {
      case AVGXW:
        x.assign(SUMxy);
        x.div(SUMy);
        break;
      case LIN_R:
        statR(x,SUMx,SUMx2,SUMy,SUMy2,SUMxy);
        break;
      case LOG_R:
        statR(x,SUMlnx,SUMln2x,SUMy,SUMy2,SUMylnx);
        break;
      case EXP_R:
        statR(x,SUMx,SUMx2,SUMlny,SUMln2y,SUMxlny);
        break;
      case POW_R:
        statR(x,SUMlnx,SUMln2x,SUMlny,SUMln2y,SUMlnxlny);
        break;
    }
    strStack[0] = null;
    repaint(-1);
  }

  private void financeSolve(int which) {
    allocFinance();
    switch (which) {
      case 0: // PV
        if (IR.isZero()) {
          // pv = -(np*pmt+fv)
          PV.assign(NP);
          PV.mul(PMT);
          PV.add(FV);
          PV.neg();
        } else {
          // pv = -(((1+ir)^np-1)*pmt*(1+ir*bgn)/ir + fv)/((1+ir)^np)
          rTmp.assign(IR);
          rTmp.mul(Real.PERCENT);
          rTmp2.assign(rTmp);
          rTmp2.add(Real.ONE);
          rTmp2.pow(NP);
          PV.assign(rTmp2);
          PV.sub(Real.ONE);
          PV.mul(PMT);
          PV.div(rTmp);
          if (begin) {
            rTmp.add(Real.ONE);
            PV.mul(rTmp);
          }
          PV.add(FV);
          PV.div(rTmp2);
          PV.neg();
        }
        break;
      case 1: // FV
        if (IR.isZero()) {
          // fv = -(np*pmt+pv)
          FV.assign(NP);
          FV.mul(PMT);
          FV.add(PV);
          FV.neg();
        } else {
          // fv = -((1+ir)^np*pv + ((1+ir)^np - 1)*pmt*(1+ir*bgn)/ir)
          rTmp.assign(IR);
          rTmp.mul(Real.PERCENT);
          rTmp2.assign(rTmp);
          rTmp2.add(Real.ONE);
          rTmp2.pow(NP);
          FV.assign(rTmp2);
          FV.mul(PV);
          rTmp2.sub(Real.ONE);
          rTmp2.mul(PMT);
          rTmp2.div(rTmp);
          if (begin) {
            rTmp.add(Real.ONE);
            rTmp2.mul(rTmp);
          }
          FV.add(rTmp2);
          FV.neg();
        }
        break;
      case 2: // NP
        if (IR.isZero()) {
          // np = -(fv+pv)/pmt
          NP.assign(FV);
          NP.add(PV);
          if (!NP.isZero()) {
            NP.div(PMT);
            NP.neg();
          }
        } else {
          // np = ln((pmt/ir + pmt*bgn - fv) /
          //         (pmt/ir + pmt*bgn + pv)) /
          //      ln(1 + ir)
          rTmp.assign(IR);
          rTmp.mul(Real.PERCENT);
          NP.assign(PMT);
          NP.div(rTmp);
          if (begin)
            NP.add(PMT);
          rTmp2.assign(NP);
          NP.sub(FV);
          rTmp2.add(PV);
          NP.div(rTmp2);
          NP.ln();
          rTmp.add(Real.ONE);
          rTmp.ln();
          NP.div(rTmp);
        }
        break;
      case 3: // PMT
        // pmt = -(fv+pv)/np
        if (IR.isZero()) {
          PMT.assign(FV);
          PMT.add(PV);
          if (!PMT.isZero()) {
            PMT.div(NP);
            PMT.neg();
          }
        } else {
          // pmt = -(((1+ir)^np*pv+fv)*ir)/(((1+ir)^np-1)*(1 + ir*bgn));
          rTmp.assign(IR);
          rTmp.mul(Real.PERCENT);
          rTmp2.assign(rTmp);
          rTmp2.add(Real.ONE);
          rTmp2.pow(NP);
          PMT.assign(rTmp2);
          PMT.mul(PV);
          PMT.add(FV);
          PMT.mul(rTmp);
          rTmp2.sub(Real.ONE);
          if (begin) {
            rTmp.add(Real.ONE);
            rTmp2.mul(rTmp);
          }
          PMT.div(rTmp2);
          PMT.neg();
        }
        break;
      case 4: // IR
        if ((FV.isZero() && PV.isZero()) ||
            !PV.isFinite() ||
            !FV.isFinite() ||
            !NP.isFinite() ||
            !PMT.isFinite())
        {
          IR.makeNan();
          break;
        }
        // Educated guess at start value = 2*(np*pmt+pv+fv)/(np*(fv-pv))
        rTmp.assign(FV);
        rTmp.sub(PV);
        rTmp.mul(NP);
        if (!rTmp.isZero()) {
          IR.assign(NP);
          IR.mul(PMT);
          IR.add(PV);
          IR.add(FV);
          IR.scalbn(1);
          IR.div(rTmp);
        } else {
          // If start value fails, use ir itself
          IR.mul(Real.PERCENT);
        }
        if (!IR.isFiniteNonZero()) {
          IR.assign(Real.TEN);
          IR.pow(-2); // When all else fails, just start with 1%
        }
        Real X1 = new Real();
        Real Y1 = new Real();
        Real X2 = new Real();
        Real Y2 = new Real();
        for (int n=0; n<20; n++) {
          if (n>=2) {
            // Calculate secant approximation:
            // ir = X1 - Y1*(X1-X2)/(Y1-Y2)
            rTmp.assign(X1);
            rTmp.sub(X2);
            rTmp2.assign(Y1);
            rTmp2.sub(Y2);
            if (!rTmp.isFinite() || !rTmp2.isFinite() ||
                rTmp.exponent <= 0x40000000-56 ||
                rTmp2.exponent <= 0x40000000-46)
              break;
            rTmp.div(rTmp2);
            rTmp.mul(Y1);
            rTmp.neg();
            rTmp.add(X1);
            IR.assign(rTmp);
          } else if (n==1) {
            // Use ir and 2*ir as starting values
            IR.scalbn(1);
          }
          if (IR.isZero()) {
            // f(ir) = fv + np*pmt + pv
            rTmp.assign(NP);
            rTmp.mul(PMT);
            rTmp.add(PV);
          } else {
            // f(ir) = fv + (1+ir)^np*pv + ((1+ir)^np - 1)*pmt*(1+ir*bgn)/ir
            rTmp2.assign(IR);
            rTmp2.add(Real.ONE);
            rTmp2.pow(NP);
            rTmp.assign(rTmp2);
            rTmp.mul(PV);
            rTmp2.sub(Real.ONE);
            rTmp2.mul(PMT);
            rTmp2.div(IR);
            if (begin) {
              rTmp3.assign(IR);
              rTmp3.add(Real.ONE);
              rTmp2.mul(rTmp3);
            }
            rTmp.add(rTmp2);
          }
          rTmp.add(FV);

          X2.assign(X1);
          Y2.assign(Y1);
          X1.assign(IR);
          Y1.assign(rTmp);
        }
        if (!Y1.isFinite() || Y1.exponent > 0x40000000-16) {
          // We didn't reach zero
          IR.makeNan();
          break;
        }
        IR.mul(Real.HUNDRED);
        break;
    }
    push(finance[which],null);
  }

  private void undo() {
    switch (undoOp) {
      case UNDO_NONE:
        break;
      case UNDO_UNARY:
        stack[0].assign(lastx);
        if (imagStack != null)
          imagStack[0].assign(lastxi);
        strStack[0] = undoStackEmpty >= 1 ? empty : null;
        repaint(1);
        break;
      case UNDO_BINARY:
        rollUp(true);
        stack[0].assign(lastx);
        stack[1].assign(lasty);
        if (imagStack != null) {
          imagStack[0].assign(lastxi);
          imagStack[1].assign(lastyi);
        }          
        strStack[0] = undoStackEmpty >= 2 ? empty : null;
        strStack[1] = undoStackEmpty >= 1 ? empty : null;
        repaint(-1);
        break;
      case UNDO_TRINARY:
        rollUp(true);
        rollUp(true);
        stack[0].assign(lastx);
        stack[1].assign(lasty);
        stack[2].assign(lastz);
        if (imagStack != null) {
          imagStack[0].assign(lastxi);
          imagStack[1].assign(lastyi);
          imagStack[2].assign(lastzi);
        }          
        strStack[0] = undoStackEmpty >= 3 ? empty : null;
        strStack[1] = undoStackEmpty >= 2 ? empty : null;
        strStack[2] = undoStackEmpty >= 1 ? empty : null;        
        repaint(-1);
        break;
      case UNDO_PUSH:
        stack[0].assign(lasty);
        if (imagStack != null)
          imagStack[0].assign(lastyi);
        strStack[0] = undoStackEmpty >= 1 ? empty : null;
        rollDown(true);
        repaint(-1);
        break;
      case UNDO_PUSH2:
        stack[0].assign(lasty);
        stack[1].assign(lastz);
        if (imagStack != null) {
          imagStack[0].assign(lastyi);
          imagStack[1].assign(lastzi);
        }          
        strStack[0] = undoStackEmpty >= 2 ? empty : null;
        strStack[1] = undoStackEmpty >= 1 ? empty : null;
        rollDown(true);
        rollDown(true);
        repaint(-1);
        break;
      case UNDO_XY:
        stack[0].assign(lastx);
        stack[1].assign(lasty);
        if (imagStack != null) {
          imagStack[0].assign(lastxi);
          imagStack[1].assign(lastyi);
        }          
        strStack[0] = undoStackEmpty >= 2 ? empty : null;
        strStack[1] = undoStackEmpty >= 1 ? empty : null;
        repaint(2);
        break;
      case UNDO_PUSHXY:
        stack[0].assign(lasty);
        stack[1].assign(lastx);
        if (imagStack != null) {
          imagStack[0].assign(lastyi);
          imagStack[1].assign(lastxi);
        }          
        strStack[0] = undoStackEmpty >= 1 ? empty : null;
        strStack[1] = undoStackEmpty >= 2 ? empty : null;
        // Different this time         ^^^
        rollDown(true);
        repaint(-1);
        break;
      case UNDO_ROLLDN:
        rollUp(false);
        break;
      case UNDO_ROLLUP:
        rollDown(false);
        break;
      case UNDO_XCHGST:
        xchgSt(undoStackEmpty);
        break;
    }
    undoOp = UNDO_NONE; // Cannot undo this
  }

  private void record(int cmd, int param) {
    if (prog == null || prog[currentProg] == null ||
        (cmd >= AVG_DRAW && cmd <= POW_DRAW) ||
        (cmd >= PROG_NEW && cmd <= PROG_CLEAR) ||
        (cmd >= PROG_DRAW && cmd < PROG_DRAW+NUM_PROGS))
      return; // Such commands cannot be recorded
    if (progCounter == prog[currentProg].length) {
      short [] prog2 = new short[progCounter*2];
      System.arraycopy(prog[currentProg],0,prog2,0,progCounter);
      prog[currentProg] = prog2;
    }
    prog[currentProg][progCounter] = (short)(cmd+(param<<10));
    progCounter++;
  }

  private void execute(short prog) {
    command(prog&0x3ff, prog>>>10);
  }
  
  public void command(int cmd, int param) {
    int i;

    if (progRecording && cmd!=PROG_FINISH)
      record(cmd, param);

    switch (cmd) {
      case DIGIT_0: case DIGIT_1: case DIGIT_2: case DIGIT_3:
      case DIGIT_4: case DIGIT_5: case DIGIT_6: case DIGIT_7:
      case DIGIT_8: case DIGIT_9: case DIGIT_A: case DIGIT_B:
      case DIGIT_C: case DIGIT_D: case DIGIT_E: case DIGIT_F:
      case SIGN_E:
      case DEC_POINT:
      case SIGN_POINT_E:
        input(cmd);
        return;
      case ENTER:
        enter();
        return;
      case CLEAR:
        if (inputInProgress)
          input(cmd);
        else
          binaryCplx(cmd);
        return;
    }

    // For all the commands below, do implicit enter
    if (inputInProgress)
      parseInput();
    
    switch (cmd) {
      case ADD:   case SUB:   case MUL:   case DIV:
      case YPOWX: case XRTY:
      case TO_CPLX:
        binaryCplx(cmd);
        break;
      case PERCENT_CHG:
      case ATAN2: case HYPOT:
      case PYX:   case CYX:
      case AND:   case OR:    case XOR:   case BIC:
      case YUPX:  case YDNX:
      case DHMS_PLUS:
      case FINANCE_MULINT: case FINANCE_DIVINT:
      case MOD:   case DIVF:
      case MAX:   case MIN:
        binary(cmd);
        break;
      case NEG:   case RECIP: case SQR:   case SQRT:
      case PERCENT:
      case ABS:   case CPLX_ARG: case CPLX_CONJ:
      case LN:    case EXP:
      case LOG10: case EXP10: case LOG2: case EXP2:
      case SIN:   case COS:   case TAN:
      case SINH:  case COSH:  case TANH:
      case ASIN:  case ACOS:  case ATAN:
      case ASINH: case ACOSH: case ATANH:
      case ROUND: case CEIL:  case FLOOR: case TRUNC: case FRAC:
        unaryCplx(cmd);
        break;
      case FACT:  case GAMMA: case ERFC:
      case NOT:
      case XCHGMEM:
      case TO_DEG: case TO_RAD: case TO_DHMS: case TO_H:
      case SGN:
      case DHMS_TO_UNIX: case UNIX_TO_DHMS:
      case DHMS_TO_JD: case JD_TO_DHMS:
      case DHMS_TO_MJD: case MJD_TO_DHMS:
      case LIN_YEST: case LIN_XEST: case LOG_YEST: case LOG_XEST:
      case EXP_YEST: case EXP_XEST: case POW_YEST: case POW_XEST:
      case CONV_C_F: case CONV_F_C:
        unary(cmd,param);
        break;
      case PI:          push(Real.PI,null);                    break;
      case CONST_c:     push(0x4000001c, 0x4779e12800000000L); break;
      case CONST_h:     push(0x3fffff91, 0x6e182e8b16bd5f42L); break;
      case CONST_mu_0:  push(0x3fffffec, 0x5454dc3e67db2c21L); break;
      case CONST_eps_0: push(0x3fffffdb, 0x4de1dbc537b4c1b4L); break;
      case CONST_NA:    push(0x4000004e, 0x7f86183045affe27L); break;
      case CONST_R:     push(0x40000003, 0x428409e55c0fcb4fL); break;
      case CONST_k:     push(0x3fffffb4, 0x42c3a0166b61ae01L); break;
      case CONST_F:     push(0x40000010, 0x5e3955a6b50b0f28L); break;
      case CONST_alpha: push(0x3ffffff8, 0x778f50a81fcfba71L); break;
      case CONST_a_0:   push(0x3fffffdd, 0x745e07537412adf4L); break;
      case CONST_R_inf: push(0x40000017, 0x53b911c8c56d5cfbL); break;
      case CONST_mu_B:  push(0x3fffffb3, 0x59b155d92797eb1eL); break;
      case CONST_e:     push(0x3fffffc1, 0x5e93683d3137633fL); break;
      case CONST_m_e:   push(0x3fffff9c, 0x49e7728ced335c92L); break;
      case CONST_m_p:   push(0x3fffffa7, 0x42426639a512e22fL); break;
      case CONST_m_n:   push(0x3fffffa7, 0x4259c7d3bd6cba4fL); break;
      case CONST_m_u:   push(0x3fffffa7, 0x41c7dd5a667f9950L); break;
      case CONST_G:     push(0x3fffffde, 0x496233f0f7af9494L); break;
      case CONST_g_n:   push(0x40000003, 0x4e7404ea4a8c154dL); break;
      case CONST_ly:    push(0x40000035, 0x4338f7ee448d8000L); break;
      case CONST_AU:    push(0x40000025, 0x45a974b4c6000000L); break;
      case CONST_pc:    push(0x40000036, 0x6da012f9404b0988L); break;
      case CONST_km_mi: push(0x40000000, 0x66ff7dfa00e27e0fL); break;
      case CONST_m_ft:  push(0x3ffffffe, 0x4e075f6fd21ff2e5L); break;
      case CONST_cm_in: push(0x40000001, 0x5147ae147ae147aeL); break;
      case CONST_km_nm: push(0x40000000, 0x76872b020c49ba5eL); break;
      case CONST_m_yd:  push(0x3fffffff, 0x750b0f27bb2fec57L); break;
      case CONST_g_oz:  push(0x40000004, 0x7165e963dc486ad3L); break;
      case CONST_kg_lb: push(0x3ffffffe, 0x741ea12add794261L); break;
      case CONST_mg_gr: push(0x40000006, 0x40cc855da272862fL); break;
      case CONST_kg_ton:push(0x40000009, 0x7165e963dc486ad3L); break;
      case CONST_J_cal: push(0x40000002, 0x42fd21ff2e48e8a7L); break;
      case CONST_J_Btu: push(0x4000000a, 0x41f0f5c28f5c28f6L); break;
      case CONST_W_hp:  push(0x40000009, 0x5d36666666666666L); break;
      case CONST_l_pt:  push(0x3ffffffe, 0x792217e4c58958fcL); break;
      case CONST_l_cup: push(0x3ffffffd, 0x792217e4c58958fcL); break;
      case CONST_l_gal: push(0x40000001, 0x792217e4c58958fcL); break;
      case CONST_ml_floz:push(0x40000004,0x764b4b5568e820e6L); break;
      case CONST_K_C:   push(0x40000008, 0x444999999999999aL); break;
      case RANDOM:
        rTmp.random();
        push(rTmp,null);
        break;
      case TIME:
        rTmp.time();
        push(rTmp,null);
        break;
      case DATE:
        rTmp.date();
        push(rTmp,null);
        break;
      case TIME_NOW:
        rTmp.time();
        rTmp2.date();
        rTmp.add(rTmp2);
        push(rTmp,null);
        break;
      case IF_EQUAL:
      case IF_NEQUAL:
      case IF_LESS:
      case IF_LEQUAL:
      case IF_GREATER:
        cond(cmd);
        break;
      case SELECT:
        trinaryCplx(cmd);
        break;
      case RP:
      case PR:
      case XCHG:
        xyOp(cmd);
        break;
      case CLS:
        lastx.assign(stack[0]);
        if (imagStack != null)
          lastxi.assign(imagStack[0]);
        clearStack();
        undoOp = UNDO_NONE; // Cannot undo this
        break;
      case RCLST:
        if (imagStack != null)
          push(stack[param],imagStack[param]);
        else
          push(stack[param],null);
        break;
      case ROLLDN:
        rollDown(false);
        undoOp = UNDO_ROLLDN;
        break;
      case ROLLUP:
        rollUp(false);
        undoOp = UNDO_ROLLUP;
        break;
      case XCHGST:
        if (strStack[param] != empty) {
          xchgSt(param);
          undoOp = UNDO_XCHGST;
          undoStackEmpty = param; // Using this otherwise unused variable
        }
        break;
      case LASTX:
        push(lastx,lastxi);
        break;
      case UNDO:
        undo();
        break;

      case STO_X:
        param = stack[0].toInteger();
        if (param<0 || param>15)
          break;
        // fall-through to next case...
      case STO:
        i = cmd==STO ? 0 : 1;
        allocMem();
        mem[param].assign(stack[i]);
        if (imagStack != null && !imagStack[i].isZero()) {
          allocImagMem();
          imagMem[param].assign(imagStack[i]);
        }
        if (monitorMode == MONITOR_MEM) {
          monitorStr[param] = null;
          repaint(-1);
        }
        break;
      case STP_X:
        param = stack[0].toInteger();
        if (param<0 || param>15)
          break;
        // fall-through to next case...
      case STP:
        i = cmd==STP ? 0 : 1;
        allocMem();
        mem[param].add(stack[i]);
        if (imagStack != null && !imagStack[i].isZero()) {
          allocImagMem();
          imagMem[param].add(imagStack[i]);
        }
        if (monitorMode == MONITOR_MEM) {
          monitorStr[param] = null;
          repaint(-1);
        }
        break;
      case RCL_X:
        param = stack[0].toInteger();
        if (param<0 || param>15)
          break;
        // fall-through to next case...
      case RCL:
        if (mem != null) {
          if (imagMem != null) {
            push(mem[param],imagMem[param]);
          } else {
            push(mem[param],null);
          }
        } else {
          push(Real.ZERO,null);
        }
        break;
      case CLMEM:
        clearMem();
        if (monitorMode == MONITOR_MEM) {
          clearMonitorStrings();
          repaint(-1);
        }
        break;

      case SUMPL:
      case SUMMI:
        sum(cmd);
        break;
      case CLST:
        clearStat();
        if (monitorMode == MONITOR_STAT) {
          clearMonitorStrings();
          repaint(-1);
        }
        break;
      case AVG:
      case STDEV:
      case PSTDEV:
      case LIN_AB:
      case LOG_AB:
      case EXP_AB:
      case POW_AB:
        stat2(cmd);
        break;
      case AVGXW:
      case LIN_R:
      case LOG_R:
      case EXP_R:
      case POW_R:
        stat1(cmd);
        break;
      case N:         push(SUM1,     null); break;
      case SUMX:      push(SUMx,     null); break;
      case SUMXX:     push(SUMx2,    null); break;
      case SUMY:      push(SUMy,     null); break;
      case SUMYY:     push(SUMy2,    null); break;
      case SUMXY:     push(SUMxy,    null); break;
      case SUMLNX:    push(SUMlnx,   null); break;
      case SUMLN2X:   push(SUMln2x,  null); break;
      case SUMLNY:    push(SUMlny,   null); break;
      case SUMLN2Y:   push(SUMln2y,  null); break;
      case SUMXLNY:   push(SUMxlny,  null); break;
      case SUMYLNX:   push(SUMylnx,  null); break;
      case SUMLNXLNY: push(SUMlnxlny,null); break;

      case FACTORIZE:
        lastx.assign(stack[0]);
        if (imagStack != null)
          lastxi.assign(imagStack[0]);
        else
          lastxi = null;
        stack[0].round();
        if (stack[0].exponent > 0x4000001e ||
            (imagStack != null && !imagStack[0].isZero()))
        {
          push(Real.NAN,null);
        } else {
          int a = stack[0].toInteger();
          int b = greatestFactor(a);
          rollUp(true);
          lasty.assign(stack[0]);
          if (imagStack != null)
            lastyi.assign(imagStack[0]);
          undoStackEmpty = strStack[0]==empty ? strStack[1]==empty ? 2 : 1 : 0;
          stack[0].assign((b!=0) ? a/b : 0);
          stack[1].assign(b);
          if (imagStack != null) {
            imagStack[0].makeZero();
            imagStack[1].makeZero();
          }
          strStack[0] = null;
          strStack[1] = null;
          repaint(-1);
        }
        undoOp = UNDO_PUSHXY;
        break;
      case CPLX_SPLIT:
        if (imagStack != null) {
          lastx.assign(stack[0]);
          lastxi.assign(imagStack[0]);
          rollUp(true);
          lasty.assign(stack[0]);
          lastyi.assign(imagStack[0]);
          stack[0].assign(stack[1]);
          stack[1].assign(imagStack[1]);
          imagStack[0].makeZero();
          imagStack[1].makeZero();
        } else {
          lastx.assign(stack[0]);
          lastxi = null;
          rollUp(true);
          lasty.assign(stack[0]);
          stack[0].assign(stack[1]);
          stack[1].makeZero();
        }
        undoStackEmpty = strStack[0]==empty ? strStack[1]==empty ? 2 : 1 : 0;
        strStack[0] = null;
        strStack[1] = null;
        repaint(-1);
        undoOp = UNDO_PUSHXY;
        break;

      case FINANCE_STO:
        allocFinance();
        finance[param].assign(stack[0]);
        if (monitorMode == MONITOR_FINANCE) {
          monitorStr[param] = null;
          repaint(-1);
        }
        break;
      case FINANCE_RCL:
        if (finance != null)
          push(finance[param],null);
        else
          push(Real.ZERO,null);
        break;
      case FINANCE_SOLVE:
        financeSolve(param);
        if (monitorMode == MONITOR_FINANCE) {
          monitorStr[param] = null;
          repaint(-1);
        }
        break;
      case FINANCE_CLEAR:
        clearFinance();
        if (monitorMode == MONITOR_FINANCE) {
          clearMonitorStrings();
          repaint(-1);
        }
        break;
      case FINANCE_BGNEND:
        begin = !begin;
        break;

      case MONITOR_NONE:
        monitorMode = cmd;
        monitorSize = 0;
        clearMonitorStrings();
        repaint(-1);
        break;
      case MONITOR_MEM:
        monitorMode = cmd;
        monitorSize = param > MEM_SIZE ? MEM_SIZE : param;
        monitors = mem;
        imagMonitors = imagMem;
        monitorLabels = memLabels;
        clearMonitorStrings();
        repaint(-1);
        break;
      case MONITOR_STAT:
        monitorMode = cmd;
        monitorSize = param > STAT_SIZE ? STAT_SIZE : param;
        monitors = stat;
        imagMonitors = null;
        monitorLabels = statLabels;
        clearMonitorStrings();
        repaint(-1);
        break;
      case MONITOR_FINANCE:
        monitorMode = cmd;
        monitorSize = FINANCE_SIZE;
        monitors = finance;
        imagMonitors = null;
        monitorLabels = financeLabels;
        clearMonitorStrings();
        repaint(-1);
        break;

      case NORM:
        if (format.fse != Real.NumberFormat.FSE_NONE) {
          format.fse = Real.NumberFormat.FSE_NONE;
          clearStrings();
        }
        break;
      case FIX:
        if (format.fse != Real.NumberFormat.FSE_FIX ||
            format.precision != param)
        {
          format.fse = Real.NumberFormat.FSE_FIX;
          format.precision = param;
          clearStrings();
        }
        break;
      case SCI:
        if (format.fse != Real.NumberFormat.FSE_SCI ||
            format.precision != param)
        {
          format.fse = Real.NumberFormat.FSE_SCI;
          format.precision = param;
          clearStrings();
        }
        break;
      case ENG:
        if (format.fse != Real.NumberFormat.FSE_ENG ||
            format.precision != param)
        {
          format.fse = Real.NumberFormat.FSE_ENG;
          format.precision = param;
          clearStrings();
        }
        break;
      case POINT_DOT:
        if (format.point != '.') {
          format.point = '.';
          if (format.thousand == '.')
            format.thousand = ',';
          clearStrings();
        }
        break;
      case POINT_COMMA:
        if (format.point != ',') {
          format.point = ',';
          if (format.thousand == ',')
            format.thousand = '.';
          clearStrings();
        }
        break;
      case POINT_REMOVE:
        if (!format.removePoint) {
          format.removePoint = true;
          clearStrings();
        }
        break;
      case POINT_KEEP:
        if (format.removePoint) {
          format.removePoint = false;
          clearStrings();
        }
        break;
      case THOUSAND_DOT:
        if (format.thousand != '.' && format.thousand != ',') {
          format.thousand = (format.point=='.') ? ',' : '.';
          clearStrings();
        }
        break;
      case THOUSAND_SPACE:
        if (format.thousand != ' ') {
          format.thousand = ' ';
          clearStrings();
        }
        break;
      case THOUSAND_QUOTE:
        if (format.thousand != '\'') {
          format.thousand = '\'';
          clearStrings();
        }
        break;
      case THOUSAND_NONE:
        if (format.thousand != 0) {
          format.thousand = 0;
          clearStrings();
        }
        break;
      case BASE_BIN:
        if (format.base != 2) {
          format.base = 2;
          clearStrings();
        }
        break;
      case BASE_OCT:
        if (format.base != 8) {
          format.base = 8;
          clearStrings();
        }
        break;
      case BASE_DEC:
        if (format.base != 10) {
          format.base = 10;
          clearStrings();
        }
        break;
      case BASE_HEX:
        if (format.base != 16) {
          format.base = 16;
          clearStrings();
        }
        break;
      case TRIG_DEGRAD:
        degrees = !degrees;
        break;
      case FREE_MEM:
        rollUp(true);
        rollUp(true);
        Runtime.getRuntime().gc();
        stack[0].assign(Runtime.getRuntime().freeMemory());
        stack[1].assign(Runtime.getRuntime().totalMemory());
        if (imagStack != null) {
          imagStack[0].makeZero();
          imagStack[1].makeZero();
        }
        strStack[0] = null;
        strStack[1] = null;
        repaint(-1);
        break;

      case PROG_NEW:
        progRecording = true;
        currentProg = param;
        if (prog == null)
          prog = new short[NUM_PROGS][];
        prog[currentProg] = new short[10];
        progCounter = 0;
        break;
      case FINALIZE:
      case PROG_FINISH:
        if (progRecording && prog!=null && prog[currentProg]!=null) {
          progRecording = false;
          if (progCounter > 0) {
            short [] prog2 = new short[progCounter];
            System.arraycopy(prog[currentProg],0,prog2,0,progCounter);
            prog[currentProg] = prog2;
          } else {
            prog[currentProg] = null;
          }
        }
        break;
      case PROG_RUN:
        currentProg = param;
        if (prog != null && prog[currentProg] != null) {
          progRunning = true;
          progCounter = 0;
          for (i=0; i<prog[currentProg].length; i++)
            execute(prog[currentProg][i]);
          if (inputInProgress) // From the program...
            parseInput();
          progRunning = false;
        }
        break;
      case PROG_PURGE:
        progCounter = 0;
        break;
      case PROG_CLEAR:
        currentProg = param;
        if (prog != null)
          prog[currentProg] = null;
        progLabels[currentProg] = emptyProg;
        progRecording = false;
        progRunning = false;
        break;
    }
  }

  private int rangeScale(Real x, Real min, Real max, int size, Real offset) {
    if (!x.isFinite())
      return (x.sign*2-1)*0x4000;
    rTmp.assign(x);
    rTmp.sub(min);
    rTmp2.assign(max);
    rTmp2.sub(min);
    rTmp.div(rTmp2);
    rTmp2.assign(size-1);
    rTmp.mul(rTmp2);
    rTmp.add(offset);
    rTmp.round();
    int i = rTmp.toInteger();
    return (i < -0x4000) ? -0x4000 : ((i > 0x4000) ? 0x4000 : i);
  }

  private int findTickStep(Real step, Real min, Real max, int size) {
    rTmp.assign(max);
    rTmp.sub(min);
    rTmp.mul(10); // minimum tick distance
    rTmp.div(size);
    step.assign(rTmp);
    step.lowPow10(); // convert to lower power of 10
    rTmp.div(step);
    if (rTmp.lessThan(2)) {
      step.mul(2);
      return 5;
    }
    if (rTmp.lessThan(5)) {
      step.mul(5);
      return 2;
    }
    step.mul(10);
    return 10;
  }

  Real xMin,xMax,yMin,yMax,a,b;
  int graphCmd;

  public boolean prepareGraph(int cmd) {
    graphCmd = cmd;
    
    if (cmd >= PROG_DRAW) {
      if (prog == null)
        return false;
    } else if (SUM1 == null || statLogSize == 0)
      return false;

    xMin = new Real();
    xMax = new Real();
    yMin = new Real();
    yMax = new Real();
    a = new Real();
    b = new Real();
    Real x = rTmp3;
    Real y = rTmp4;
    int i;

    // Find boundaries
    if (cmd >= PROG_DRAW) {
      if (inputInProgress)
        parseInput();
      xMin.assign(stack[3]);
      xMax.assign(stack[2]);
      yMin.assign(stack[1]);
      yMax.assign(stack[0]);
    } else {
      for (i=0; i<statLogSize; i++) {
        int index = (statLogStart+i)%STATLOG_SIZE;
        x.assignFloatBits(statLog[index*2]);
        y.assignFloatBits(statLog[index*2+1]);
        if (x.isFiniteNonZero() && y.isFiniteNonZero()) {
          if (x.lessThan(xMin))    xMin.assign(x);
          if (x.greaterThan(xMax)) xMax.assign(x);
          if (y.lessThan(yMin))    yMin.assign(y);
          if (y.greaterThan(yMax)) yMax.assign(y);
        }
      }
      // Expand boundaries by 10%
      rTmp.assign("1.1");
      xMin.mul(rTmp);
      xMax.mul(rTmp);
      yMin.mul(rTmp);
      yMax.mul(rTmp);
    }
    if (xMin.equals(xMax) || yMin.equals(yMax))
      return false;

    progRunning = true;
    return true;
  }

  public void startGraph(Graphics g, int gx, int gy, int gw, int gh) {
    int i,xi,yi,pyi,inc,bigTick;
    Real x = rTmp3;
    Real y = rTmp4;

    g.setClip(gx,gy,gw,gh);
    // shrink window by 4 pixels
    gx += 2;
    gy += 2;
    gw -= 4;
    gh -= 4;

    // Draw X axis
    g.setColor(0,255,128);
    yi = gy+rangeScale(Real.ZERO,yMax,yMin,gh,Real.ZERO);
    g.drawLine(gx-2,yi,gx+gw+1,yi);
    bigTick = findTickStep(a,xMin,xMax,gw);
    x.assign(a);
    x.neg();
    i = -1;
    y.assign(a);
    y.scalbn(-1);
    y.neg();
    y.add(xMin);
    while (x.greaterThan(y)) {
      xi = gx+rangeScale(x,xMin,xMax,gw,Real.ZERO);
      inc = (i%bigTick == 0) ? 2 : 1;
      g.setColor(0,32,16);
      g.drawLine(xi,gy-1,xi,gy+gh);
      g.setColor(0,255,128);
      g.drawLine(xi,yi-inc,xi,yi+inc);
      x.sub(a);
      i--;
    }
    x.assign(a);
    i = 1;
    y.assign(a);
    y.scalbn(-1);
    y.add(xMax);
    while (x.lessThan(y)) {
      xi = gx+rangeScale(x,xMin,xMax,gw,Real.ZERO);
      inc = (i%bigTick == 0) ? 2 : 1;
      g.setColor(0,32,16);
      g.drawLine(xi,gy-1,xi,gy+gh);
      g.setColor(0,255,128);
      g.drawLine(xi,yi-inc,xi,yi+inc);
      x.add(a);
      i++;
    }

    // Draw Y axis
    xi = gx+rangeScale(Real.ZERO,xMin,xMax,gw,Real.ZERO);
    g.drawLine(xi,gy-2,xi,gy+gh+1);
    bigTick = findTickStep(a,yMin,yMax,gh);
    y.assign(a);
    y.neg();
    i = -1;
    x.assign(a);
    x.scalbn(-1);
    x.neg();
    x.add(yMin);
    while (y.greaterThan(x)) {
      yi = gy+rangeScale(y,yMax,yMin,gh,Real.ZERO);
      inc = (i%bigTick == 0) ? 2 : 1;
      g.setColor(0,32,16);
      g.drawLine(gx-1,yi,gx+gw,yi);
      g.setColor(0,255,128);
      g.drawLine(xi-inc,yi,xi+inc,yi);
      y.sub(a);
      i--;
    }
    y.assign(a);
    i = 1;
    x.assign(a);
    x.scalbn(-1);
    x.add(yMax);
    while (y.lessThan(x)) {
      yi = gy+rangeScale(y,yMax,yMin,gh,Real.ZERO);
      inc = (i%bigTick == 0) ? 2 : 1;
      g.setColor(0,32,16);
      g.drawLine(gx-1,yi,gx+gw,yi);
      g.setColor(0,255,128);
      g.drawLine(xi-inc,yi,xi+inc,yi);
      y.add(a);
      i++;
    }

    if (graphCmd >= PROG_DRAW) {
      // Return now to continue drawing graph indefinitely
      currentProg = graphCmd-PROG_DRAW;
      a.assign(0, 0x3fffffff, 0x4f1bbcdcbfa53e0bL); // a = golden ratio, 0.618
      b.makeZero();
      return;
    }
    
    // Draw statistics graph
    g.setColor(255,0,128);
    
    switch (graphCmd) {
      case LIN_DRAW:
        statAB(a,b,SUMx,SUMx2,SUMy,SUMy2,SUMxy);
        break;
      case LOG_DRAW:
        statAB(a,b,SUMlnx,SUMln2x,SUMy,SUMy2,SUMylnx);
        break;
      case EXP_DRAW:
        statAB(a,b,SUMx,SUMx2,SUMlny,SUMln2y,SUMxlny);
        break;
      case POW_DRAW:
        statAB(a,b,SUMlnx,SUMln2x,SUMlny,SUMln2y,SUMlnxlny);
        break;
    }
    if (graphCmd != AVG_DRAW && a.isFinite() && b.isFinite()) {
      pyi = -1000;
      inc = (graphCmd==LIN_DRAW) ? gw-1 : 5;
      for (xi=0; xi<gw+5; xi+=inc) {
        x.assign(xi);
        x.div(gw-1);
        rTmp.assign(xMax);
        rTmp.sub(xMin);
        x.mul(rTmp);
        x.add(xMin);
        if (graphCmd==LOG_DRAW || graphCmd==POW_DRAW)
          x.ln();
        y.assign(x);
        y.mul(a);
        y.add(b);
        if (graphCmd==EXP_DRAW || graphCmd==POW_DRAW)
          y.exp();
        yi = rangeScale(y,yMax,yMin,gh,Real.HALF);
        if (yi > -1000 && yi<1000+gh) {
          if (pyi > -1000) {
            g.drawLine(xi-inc+gx,pyi+gy,xi+gx,yi+gy);
            g.drawLine(xi-inc+gx,pyi-1+gy,xi+gx,yi-1+gy);
          }
          pyi = yi;
        } else {
          if (pyi > -1000)
            break; // We have drawn from inside to outside
          pyi = -1000;
        }
      }
    }

    // Draw points
    g.setColor(255,255,255);
    for (i=0; i<statLogSize; i++) {
      int index = (statLogStart+i)%STATLOG_SIZE;
      x.assignFloatBits(statLog[index*2]);
      y.assignFloatBits(statLog[index*2+1]);
      if (x.isFinite() && y.isFinite()) {
        xi = gx+rangeScale(x,xMin,xMax,gw,Real.ZERO);
        yi = gy+rangeScale(y,yMax,yMin,gh,Real.ZERO);
        g.drawLine(xi,yi-1,xi,yi+1);
        g.drawLine(xi-1,yi,xi+1,yi);
      }
    }

    // Draw average
    if (graphCmd == AVG_DRAW) {
      g.setColor(255,0,128);
      x.assign(SUMx);
      x.div(SUM1);
      y.assign(SUMy);
      y.div(SUM1);
      if (x.isFinite() && y.isFinite()) {
        xi = gx+rangeScale(x,xMin,xMax,gw,Real.ZERO);
        yi = gy+rangeScale(y,yMax,yMin,gh,Real.ZERO);
        g.fillRect(xi-1,yi-1,3,3);
      }
    }

    progRunning = false;
  }

  public void continueGraph(Graphics g, int gx, int gy, int gw, int gh) {
    long start = System.currentTimeMillis();
    Real x = rTmp3;
    int i,xi,yi;

    g.setClip(gx,gy,gw,gh);
    // shrink window by 4 pixels
    gx += 2;
    gy += 2;
    gw -= 4;
    gh -= 4;

    do
    {
      b.add(a);
      if (b.greaterThan(Real.ONE))
        b.sub(Real.ONE);
      x.assign(xMax);
      x.sub(xMin);
      x.mul(b);
      x.add(xMin);
      xi = gx+rangeScale(x,xMin,xMax,gw,Real.ZERO);
      
      push(x,null);
      for (i=0; i<prog[currentProg].length; i++)
        execute(prog[currentProg][i]);
      if (inputInProgress) // From the program... (boring graph)
        parseInput();
        
      Real y = stack[0];
      Real yimag = null;
      if (imagStack!=null && !imagStack[0].isZero())
        yimag = imagStack[0];
      yi = -100;
      if (y.isFinite() && (!y.isZero() || yimag==null)) {
        yi = gy+rangeScale(y,yMax,yMin,gh,Real.HALF);
        g.setColor(255,0,128);
        g.drawLine(xi,yi-1,xi,yi);
      }
      if (yimag!=null && yimag.isFinite()) {
        int yi2 = gy+rangeScale(yimag,yMax,yMin,gh,Real.HALF);
        g.setColor(255,255,yi2==yi ? 255 : 0);
        g.drawLine(xi,yi2-1,xi,yi2);
      }
      command(CLEAR,0); // Remove result from stack to avoid clutter
    }
    while (System.currentTimeMillis()-start < 500);
  }
}
