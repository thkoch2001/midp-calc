package ral;

import javax.microedition.lcdui.*;

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
  public static final int PERCENT        = 300;
  public static final int PERCENT_CHG    =  28;
  public static final int YPOWX          =  29;
  public static final int XRTY           =  30;
  public static final int LN             =  31;
  public static final int EXP            =  32;
  public static final int LOG10          =  33;
  public static final int EXP10          =  34;
  public static final int LOG2           =  35;
  public static final int EXP2           =  36;
  public static final int PYX            =  37;
  public static final int CYX            =  38;
  public static final int FACT           =  39;
  public static final int GAMMA          =  40;
  public static final int RP             =  41;
  public static final int PR             =  42;
  public static final int ATAN2          =  43;
  public static final int HYPOT          =  44;
  public static final int SIN            =  45;
  public static final int COS            =  46;
  public static final int TAN            =  47;
  public static final int ASIN           =  48;
  public static final int ACOS           =  49;
  public static final int ATAN           =  50;
  public static final int SINH           =  51;
  public static final int COSH           =  52;
  public static final int TANH           =  53;
  public static final int ASINH          =  54;
  public static final int ACOSH          =  55;
  public static final int ATANH          =  56;
  public static final int PI             =  57;
  public static final int AND            =  58;
  public static final int OR             =  59;
  public static final int XOR            =  60;
  public static final int BIC            =  61;
  public static final int NOT            =  62;
  public static final int YUPX           =  63;
  public static final int YDNX           =  64;
  public static final int XCHG           =  65;
  public static final int CLS            =  66;
  public static final int RCLST          =  67;
  public static final int LASTX          =  68;
  public static final int UNDO           =  69;
  public static final int ROUND          =  70;
  public static final int CEIL           =  71;
  public static final int FLOOR          =  72;
  public static final int TRUNC          =  73;
  public static final int FRAC           =  74;
  public static final int STO            =  75;
  public static final int STP            =  76;
  public static final int RCL            =  77;
  public static final int XCHGMEM        =  78;
  public static final int CLMEM          =  79;
  public static final int SUMPL          =  80;
  public static final int SUMMI          =  81;
  public static final int CLST           =  82;
  public static final int AVG            =  83;
  public static final int AVGXW          =  84;
  public static final int STDEV          =  85;
  public static final int PSTDEV         =  86;
  public static final int LIN_AB         =  87;
  public static final int LIN_YEST       =  88;
  public static final int LIN_XEST       =  89;
  public static final int LIN_R          =  90;
  public static final int LOG_AB         =  91;
  public static final int LOG_YEST       =  92;
  public static final int LOG_XEST       =  93;
  public static final int LOG_R          =  94;
  public static final int EXP_AB         =  95;
  public static final int EXP_YEST       =  96;
  public static final int EXP_XEST       =  97;
  public static final int EXP_R          =  98;
  public static final int POW_AB         =  99;
  public static final int POW_YEST       = 100;
  public static final int POW_XEST       = 101;
  public static final int POW_R          = 102;
  public static final int N              = 103;
  public static final int SUMX           = 104;
  public static final int SUMXX          = 105;
  public static final int SUMLNX         = 106;
  public static final int SUMLN2X        = 107;
  public static final int SUMY           = 108;
  public static final int SUMYY          = 109;
  public static final int SUMLNY         = 110;
  public static final int SUMLN2Y        = 111;
  public static final int SUMXY          = 112;
  public static final int SUMXLNY        = 113;
  public static final int SUMYLNX        = 114;
  public static final int SUMLNXLNY      = 115;
  public static final int NORM           = 116;
  public static final int FIX            = 117;
  public static final int SCI            = 118;
  public static final int ENG            = 119;
  public static final int POINT_DOT      = 120;
  public static final int POINT_COMMA    = 121;
  public static final int POINT_REMOVE   = 122;
  public static final int POINT_KEEP     = 123;
  public static final int THOUSAND_DOT   = 124;
  public static final int THOUSAND_SPACE = 125;
  public static final int THOUSAND_QUOTE = 126;
  public static final int THOUSAND_NONE  = 127;
  public static final int BASE_BIN       = 128;
  public static final int BASE_OCT       = 129;
  public static final int BASE_DEC       = 130;
  public static final int BASE_HEX       = 131;
  public static final int TRIG_DEGRAD    = 132;
  public static final int TO_DEG         = 133;
  public static final int TO_RAD         = 134;
  public static final int RANDOM         = 135;
  public static final int TO_DHMS        = 136;
  public static final int TO_H           = 137;
  public static final int DHMS_PLUS      = 138;
  public static final int TIME           = 139;
  public static final int DATE           = 140;
  public static final int FACTORIZE      = 141;
  public static final int FINANCE_STO    = 142;
  public static final int FINANCE_RCL    = 143;
  public static final int FINANCE_SOLVE  = 144;
  public static final int FINANCE_CLEAR  = 145;
  public static final int FINANCE_BGNEND = 146;
  public static final int FINANCE_MULINT = 147;
  public static final int FINANCE_DIVINT = 148;
  public static final int MONITOR_NONE   = 149;
  public static final int MONITOR_MEM    = 150;
  public static final int MONITOR_STAT   = 151;
  public static final int MONITOR_FINANCE= 152;
  public static final int AVG_DRAW       = 200;
  public static final int LIN_DRAW       = 201;
  public static final int LOG_DRAW       = 202;
  public static final int EXP_DRAW       = 203;
  public static final int POW_DRAW       = 204;
  public static final int SIGN_POINT_E   = 205;

  public static final int FINALIZE       = 500;
  public static final int FREE_MEM       = 501;
  
  private static final int STACK_SIZE    = 16;
  private static final int MEM_SIZE      = 16;
  private static final int STAT_SIZE     = 13;
  private static final int STATLOG_SIZE  = 64;
  private static final int FINANCE_SIZE  = 5;

  private static final Real Real180 = new Real(180);
  private static final String empty = "";

  public Real.NumberFormat format;
  public Real [] stack;
  public String [] strStack;
  public Real [] mem;
  public Real SUM1,SUMx,SUMx2,SUMy,SUMy2,SUMxy;
  public Real SUMlnx,SUMln2x,SUMlny,SUMln2y,SUMxlny,SUMylnx,SUMlnxlny;
  public Real [] stat;
  public int [] statLog; // Low-precision statistics log
  public Real PV,FV,NP,PMT,IR;
  public Real [] finance;
  public boolean begin;
  public int statLogStart,statLogSize;
  public Real lastx,lasty,lastz;
  public boolean degrees;
  public StringBuffer inputBuf;
  public boolean inputInProgress;
  private Real rTmp,rTmp2,rTmp3;
  private int repaintLines;

  public int monitorMode;
  public int monitorSize;
  public Real [] monitors;
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
  private static final int UNDO_PUSH   = 3;
  private static final int UNDO_PUSH2  = 4;
  private static final int UNDO_XY     = 5;
  private static final int UNDO_PUSHXY = 6;
  private int undoStackEmpty = 0;
  private int undoOp = UNDO_NONE;
  
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

    Real.accumulateRandomness(System.currentTimeMillis());
  }

  private void clearStrings() {
    if (inputInProgress)
      parseInput();
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
      stack[i].makeZero(0);
      strStack[i] = empty;
    }
    repaint(-1);
  }
  
  private void clearMem() {
    if (inputInProgress)
      parseInput();
    if (mem == null)
      return;
    for (int i=0; i<MEM_SIZE; i++)
      mem[i] = null;
    mem = null;
  }
  
  private void clearStat() {
    if (inputInProgress)
      parseInput();
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
    if (inputInProgress)
      parseInput();
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

  private void allocMem() {
    if (mem != null)
      return;
    mem = new Real[MEM_SIZE];
    for (int i=0; i<MEM_SIZE; i++)
      mem[i] = new Real();
    if (monitorMode == MONITOR_MEM)
      monitors = mem;
  }
  
  private void allocStat() {
    if (SUM1 != null)
      return;
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
    if (monitorMode == MONITOR_STAT)
      monitors = stat;
  }

  private void allocFinance() {
    if (finance != null)
      return;
    finance = new Real[FINANCE_SIZE];
    finance[0] = PV  = new Real();
    finance[1] = FV  = new Real();
    finance[2] = NP  = new Real();
    finance[3] = PMT = new Real();
    finance[4] = IR  = new Real();
    if (monitorMode == MONITOR_FINANCE)
      monitors = finance;
  }

  private static final byte PROPERTY_SETTINGS = 10;
  private static final byte PROPERTY_STACK    = 11;
  private static final byte PROPERTY_MEM      = 12;
  private static final byte PROPERTY_STAT     = 13;
  private static final byte PROPERTY_FINANCE  = 14;

  public void saveState(PropertyStore propertyStore) {
    int i;
    byte [] buf = new byte[1+STAT_SIZE*12+STATLOG_SIZE*2*4+2];
    int stackHeight;
    for (stackHeight=0; stackHeight<STACK_SIZE; stackHeight++)
      if (strStack[stackHeight] == empty)
        break;
    buf[0] = PROPERTY_STACK;
    if (stackHeight > 0) {
      for (i=0; i<STACK_SIZE; i++)
        stack[i].toBytes(buf, 1+i*12);
      propertyStore.setProperty(buf,1+STACK_SIZE*12);
    } else {
      propertyStore.setProperty(buf,1);
    }
    buf[0] = PROPERTY_MEM;
    if (mem != null) {
      for (i=0; i<MEM_SIZE; i++)
        mem[i].toBytes(buf, 1+i*12);
      propertyStore.setProperty(buf,1+MEM_SIZE*12);
    } else {
      propertyStore.setProperty(buf,1);
    }
    buf[0] = PROPERTY_STAT;
    if (SUM1 != null) {
      for (i=0; i<STAT_SIZE; i++)
        stat[i].toBytes(buf, 1+i*12);
      for (i=0; i<STATLOG_SIZE*2; i++) {
        buf[1+STAT_SIZE*12+4*i+0] = (byte)(statLog[i]>>24);
        buf[1+STAT_SIZE*12+4*i+1] = (byte)(statLog[i]>>16);
        buf[1+STAT_SIZE*12+4*i+2] = (byte)(statLog[i]>>8);
        buf[1+STAT_SIZE*12+4*i+3] = (byte)(statLog[i]);
      }
      buf[1+STAT_SIZE*12+4*i+0] = (byte)(statLogStart);
      buf[1+STAT_SIZE*12+4*i+1] = (byte)(statLogSize);
      propertyStore.setProperty(buf,1+STAT_SIZE*12+STATLOG_SIZE*2*4+2);
    } else {
      propertyStore.setProperty(buf,1);
    }
    buf[0] = PROPERTY_FINANCE;
    if (finance != null) {
      for (i=0; i<FINANCE_SIZE; i++)
        finance[i].toBytes(buf, 1+i*12);
      propertyStore.setProperty(buf,1+FINANCE_SIZE*12);
    } else {
      propertyStore.setProperty(buf,1);
    }
    // Settings
    buf[ 0] = PROPERTY_SETTINGS;
    buf[ 1] = (byte)stackHeight;
    buf[ 2] = (byte)((degrees ? 1 : 0) + (begin ? 2 : 0));
    buf[ 3] = (byte)format.base;
    buf[ 4] = (byte)format.maxwidth;
    buf[ 5] = (byte)format.precision;
    buf[ 6] = (byte)format.fse;
    buf[ 7] = (byte)format.point;
    buf[ 8] = (byte)(format.removePoint ? 1 : 0);
    buf[ 9] = (byte)format.thousand;
    buf[10] = (byte)(Real.randSeedA>>56);
    buf[11] = (byte)(Real.randSeedA>>48);
    buf[12] = (byte)(Real.randSeedA>>40);
    buf[13] = (byte)(Real.randSeedA>>32);
    buf[14] = (byte)(Real.randSeedA>>24);
    buf[15] = (byte)(Real.randSeedA>>16);
    buf[16] = (byte)(Real.randSeedA>>8);
    buf[17] = (byte)(Real.randSeedA);
    buf[18] = (byte)(Real.randSeedB>>56);
    buf[19] = (byte)(Real.randSeedB>>48);
    buf[20] = (byte)(Real.randSeedB>>40);
    buf[21] = (byte)(Real.randSeedB>>32);
    buf[22] = (byte)(Real.randSeedB>>24);
    buf[23] = (byte)(Real.randSeedB>>16);
    buf[24] = (byte)(Real.randSeedB>>8);
    buf[25] = (byte)(Real.randSeedB);    
    lastx.toBytes(buf,26);
    buf[26+12] = (byte)(((monitorMode-MONITOR_NONE)<<5) + monitorSize);
    propertyStore.setProperty(buf,26+12+1);
  }
  
  public void restoreState(PropertyStore propertyStore) {
    int length,i;
    byte [] buf = new byte[1+STAT_SIZE*12+STATLOG_SIZE*2*4+2];
    buf[0] = PROPERTY_STACK;
    length = propertyStore.getProperty(buf);
    if (length >= 1+STACK_SIZE*12)
      for (i=0; i<STACK_SIZE; i++)
        stack[i].assign(buf, 1+i*12);
    buf[0] = PROPERTY_MEM;
    length = propertyStore.getProperty(buf);
    if (length >= 1+MEM_SIZE*12) {
      allocMem();
      for (i=0; i<MEM_SIZE; i++)
        mem[i].assign(buf, 1+i*12);
    }
    buf[0] = PROPERTY_STAT;
    length = propertyStore.getProperty(buf);
    if (length >= 1+STAT_SIZE*12+STATLOG_SIZE*2*4+2) {
      allocStat();
      for (i=0; i<STAT_SIZE; i++)
        stat[i].assign(buf, 1+i*12);
      for (i=0; i<STATLOG_SIZE*2; i++) {
        statLog[i] = (((buf[1+STAT_SIZE*12+4*i+0]&0xff)<<24)+
                      ((buf[1+STAT_SIZE*12+4*i+1]&0xff)<<16)+
                      ((buf[1+STAT_SIZE*12+4*i+2]&0xff)<<8)+
                      ((buf[1+STAT_SIZE*12+4*i+3]&0xff)));
      }
      statLogStart = buf[1+STAT_SIZE*12+4*i+0]&0xff;
      statLogSize  = buf[1+STAT_SIZE*12+4*i+1]&0xff;
    }
    buf[0] = PROPERTY_FINANCE;
    length = propertyStore.getProperty(buf);
    if (length >= 1+FINANCE_SIZE*12) {
      allocFinance();
      for (i=0; i<FINANCE_SIZE; i++)
        finance[i].assign(buf, 1+i*12);
    }
    // Settings
    buf[0] = PROPERTY_SETTINGS;
    length = propertyStore.getProperty(buf);
    if (length >= 26+12) {
      for (i=0; i<STACK_SIZE; i++)
        strStack[i] = i<buf[1] ? null : empty;
      degrees            = (buf[2]&1) != 0;
      begin              = (buf[2]&2) != 0;
      format.base        = buf[3];
      format.maxwidth    = buf[4];
      format.precision   = buf[5];
      format.fse         = buf[6];
      format.point       = (char)buf[7];
      format.removePoint = buf[8] != 0;
      format.thousand    = (char)buf[9];
      Real.randSeedA = (((long)(buf[10]&0xff)<<56)+
                        ((long)(buf[11]&0xff)<<48)+
                        ((long)(buf[12]&0xff)<<40)+
                        ((long)(buf[13]&0xff)<<32)+
                        ((long)(buf[14]&0xff)<<24)+
                        ((long)(buf[15]&0xff)<<16)+
                        ((long)(buf[16]&0xff)<< 8)+
                        ((long)(buf[17]&0xff)));
      Real.randSeedB = (((long)(buf[18]&0xff)<<56)+
                        ((long)(buf[19]&0xff)<<48)+
                        ((long)(buf[20]&0xff)<<40)+
                        ((long)(buf[21]&0xff)<<32)+
                        ((long)(buf[22]&0xff)<<24)+
                        ((long)(buf[23]&0xff)<<16)+
                        ((long)(buf[24]&0xff)<< 8)+
                        ((long)(buf[25]&0xff)));
      lastx.assign(buf,26);
      if (length >= 26+12+1) {
        monitorMode = MONITOR_NONE+((buf[26+12]>>5)&7);
        monitorSize = buf[26+12]&0x1f;
        if (monitorMode == MONITOR_MEM) {
          monitors = mem;
          monitorLabels = memLabels;
        } else if (monitorMode == MONITOR_STAT) {
          monitors = stat;
          monitorLabels = statLabels;
        } else if (monitorMode == MONITOR_FINANCE) {
          monitors = finance;
          monitorLabels = financeLabels;
        }
      }
    }
    repaint(-1);
  }

  public int numRepaintLines() {
    int tmp = repaintLines;
    repaintLines = 0;
    return tmp;
  }

  public String getStackElement(int elementNo) {
    if (strStack[elementNo] == null)
      strStack[elementNo] = stack[elementNo].toString(format);
    return strStack[elementNo];
  }

  public String getMonitorElement(int elementNo) {
    if (monitorStr[elementNo] == null) {
      format.maxwidth -= monitorLabels[elementNo].length();
      if (monitors != null && monitors[elementNo] != null)
        monitorStr[elementNo] = monitors[elementNo].toString(format);
      else
        monitorStr[elementNo] = Real.ZERO.toString(format);
      format.maxwidth += monitorLabels[elementNo].length();
    }
    return monitorStr[elementNo];
  }

  public String getMonitorLabel(int elementNo) {
    return monitorLabels[elementNo];
  }

  public int getMonitorSize() {
    return monitorSize;
  }

  public void setMaxWidth(int max) {
    format.maxwidth = max;
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
    if (cmd >= DIGIT_2 && cmd <= DIGIT_F)
      for (i=0; i<inputBuf.length(); i++)
        if (inputBuf.charAt(i)=='E') {
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
              inputBuf.charAt(i)=='E')
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

        if (inputBuf.length()==0 || inputBuf.charAt(inputBuf.length()-1)=='E'){
          inputBuf.append('-');
          break;
        }
        for (i=0; i<inputBuf.length(); i++)
          if (inputBuf.charAt(i)=='E')
            return;
        inputBuf.append('E');
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

        if (inputBuf.length()==0 || inputBuf.charAt(inputBuf.length()-1)=='E'){
          inputBuf.append('-');
          break;
        }
        boolean hasE = false, hasPoint = false;
        for (i=0; i<inputBuf.length(); i++) {
          if (inputBuf.charAt(i)=='E')
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
          inputBuf.append('E');
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
        inputBuf.append((char)('a'+cmd-DIGIT_A));
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
    strStack[0] = null;
    repaint(1);
    inputInProgress = false;
  }

  private void rollUp() {
    Real tmp = stack[STACK_SIZE-1];
    String tmpStr = strStack[STACK_SIZE-1];
    for (int i=STACK_SIZE-1; i>0; i--) {
      stack[i] = stack[i-1];
      strStack[i] = strStack[i-1];
    }
    stack[0] = tmp;
    strStack[0] = tmpStr;
  }

  private void rollDown() {
    Real tmp = stack[0];
    String tmpStr = strStack[0];
    for (int i=0; i<STACK_SIZE-1; i++) {
      stack[i] = stack[i+1];
      strStack[i] = strStack[i+1];
    }
    stack[STACK_SIZE-1] = tmp;
    strStack[STACK_SIZE-1] = tmpStr;
  }

  private void enter() {
    if (inputInProgress)
      parseInput();
    else {
      rollUp();
      lasty.assign(stack[0]);
      undoStackEmpty = strStack[0]==empty ? 1 : 0;
      undoOp = UNDO_PUSH;
      stack[0].assign(stack[1]);
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
    if (inputInProgress)
      parseInput();
    Real x = stack[0];
    Real y = stack[1];
    lastx.assign(x);
    lasty.assign(y);
    undoStackEmpty = strStack[1]==empty ? strStack[0]==empty ? 2 : 1 : 0;
    undoOp = UNDO_BINARY;
    switch (cmd) {
      case ADD:   y.add(x);                break;
      case SUB:   x.neg();    y.add(x);    break;
      case MUL:   y.mul(x);                break;
      case DIV:   x.recip();  y.mul(x);    break;
      case YPOWX: y.pow(x);                break;
      case XRTY:  y.nroot(x);              break;
      case ATAN2: y.atan2(x); fromRAD(x);  break;
      case HYPOT: y.hypot(x);              break;
      case AND:   y.and(x);                break;
      case OR:    y.or(x);                 break;
      case XOR:   y.xor(x);                break;
      case BIC:   y.bic(x);                break;
      case YUPX:  x.round(); y.scalbn(x.toInteger()); break;
      case YDNX:  x.round(); y.scalbn(-x.toInteger());break;
      case PERCENT_CHG:
        x.sub(y);
        y.recip();
        y.mul(x);
        y.mul(Real.HUNDRED);
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
      case CLEAR:
        break;
    }
    rollDown();
    stack[STACK_SIZE-1].assign(Real.ZERO);
    strStack[STACK_SIZE-1] = empty;
    if (cmd!=CLEAR)
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
    if (inputInProgress)
      parseInput();
    Real x = stack[0];
    Real tmp;
    lastx.assign(x);
    undoStackEmpty = strStack[0]==empty ? 1 : 0;
    undoOp = UNDO_UNARY;
    switch (cmd) {
      case NEG:   x.neg();   break;
      case RECIP: x.recip(); break;
      case SQR:   x.sqr();   break;
      case SQRT:  x.sqrt();  break;
      case LN:    x.ln();    break;
      case EXP:   x.exp();   break;
      case LOG10: x.log10(); break;
      case EXP10: x.exp10(); break;
      case LOG2:  x.log2();  break;
      case EXP2:  x.exp2();  break;
      case FACT:  x.fact();  break;
      case GAMMA: x.gamma(); break;
      case SIN:   toRAD(x); x.sin();    break;
      case COS:   toRAD(x); x.cos();    break;
      case TAN:   toRAD(x); x.tan();    break;
      case ASIN:  x.asin(); fromRAD(x); break;
      case ACOS:  x.acos(); fromRAD(x); break;
      case ATAN:  x.atan(); fromRAD(x); break;
      case SINH:  x.sinh();  break;
      case COSH:  x.cosh();  break;
      case TANH:  x.tanh();  break;
      case ASINH: x.asinh(); break;
      case ACOSH: x.acosh(); break;
      case ATANH: x.atanh(); break;
      case NOT:   x.xor(Real.ONE_N); break;
      case ROUND: x.round(); break;
      case CEIL:  x.ceil();  break;
      case FLOOR: x.floor(); break;
      case TRUNC: x.trunc(); break;
      case FRAC:  x.frac();  break;
      case PERCENT:
        x.mul(Real.PERCENT);
        x.mul(stack[1]/*y*/);
        break;
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

  private void xyOp(int cmd) {
    if (inputInProgress)
      parseInput();
    Real x = stack[0];
    Real y = stack[1];
    lastx.assign(x);
    lasty.assign(y);
    undoStackEmpty = strStack[1]==empty ? strStack[0]==empty ? 2 : 1 : 0;
    undoOp = UNDO_XY;
    switch (cmd) {
      case RP:
        rTmp.assign(y);
        rTmp.atan2(x);
        x.hypot(y);
        y.assign(rTmp);
        fromRAD(y);
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
        strStack[0] = null;
        strStack[1] = null;
        break;
      case XCHG:
        Real tmp = stack[0];
        stack[0] = stack[1];
        stack[1] = tmp;
        String tmpStr = strStack[0];
        strStack[0] = strStack[1];
        strStack[1] = tmpStr;
        break;
    }
    repaint(2);
  }

  private void push(Real x) {
    if (inputInProgress)
      parseInput();
    rollUp();
    lasty.assign(stack[0]);
    undoStackEmpty = strStack[0]==empty ? 1 : 0;
    undoOp = UNDO_PUSH;
    stack[0].assign(x);
    strStack[0] = null;
    repaint(-1);
  }

  private void sum(int cmd) {
    if (inputInProgress)
      parseInput();
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
    push(SUM1);
  }

  private void stat2(int cmd) {
    if (inputInProgress)
      parseInput();
    allocStat();
    rollUp();
    rollUp();
    lasty.assign(stack[0]);
    lastz.assign(stack[1]);
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
    if (inputInProgress)
      parseInput();
    allocStat();
    rollUp();
    lasty.assign(stack[0]);
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
    if (inputInProgress)
      parseInput();
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
        if (!IR.isFiniteNonZero())
          IR.makeExp10(-2); // When all else fails, just start with 1%
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
    push(finance[which]);
  }

  private void undo() {
    if (inputInProgress)
      parseInput();
    switch (undoOp) {
      case UNDO_NONE:
        break;
      case UNDO_UNARY:
        stack[0].assign(lastx);
        strStack[0] = undoStackEmpty >= 1 ? empty : null;
        repaint(1);
        break;
      case UNDO_BINARY:
        rollUp();
        stack[0].assign(lastx);
        stack[1].assign(lasty);
        strStack[0] = undoStackEmpty >= 2 ? empty : null;
        strStack[1] = undoStackEmpty >= 1 ? empty : null;
        repaint(-1);
        break;
      case UNDO_PUSH:
        stack[0].assign(lasty);
        strStack[0] = undoStackEmpty >= 1 ? empty : null;
        rollDown();
        repaint(-1);
        break;
      case UNDO_PUSH2:
        stack[0].assign(lasty);
        stack[1].assign(lastz);
        strStack[0] = undoStackEmpty >= 2 ? empty : null;
        strStack[1] = undoStackEmpty >= 1 ? empty : null;
        rollDown();
        rollDown();
        repaint(-1);
        break;
      case UNDO_XY:
        stack[0].assign(lastx);
        stack[1].assign(lasty);
        strStack[0] = undoStackEmpty >= 2 ? empty : null;
        strStack[1] = undoStackEmpty >= 1 ? empty : null;
        repaint(2);
        break;
      case UNDO_PUSHXY:
        stack[0].assign(lasty);
        stack[1].assign(lastx);
        strStack[0] = undoStackEmpty >= 1 ? empty : null;
        strStack[1] = undoStackEmpty >= 2 ? empty : null;
        // Different this time         ^^^
        rollDown();
        repaint(-1);
        break;
    }
    undoOp = UNDO_NONE; // Cannot undo this
  }

  public void command(int cmd, int param) {
    switch (cmd) {
      case DIGIT_0: case DIGIT_1: case DIGIT_2: case DIGIT_3:
      case DIGIT_4: case DIGIT_5: case DIGIT_6: case DIGIT_7:
      case DIGIT_8: case DIGIT_9: case DIGIT_A: case DIGIT_B:
      case DIGIT_C: case DIGIT_D: case DIGIT_E: case DIGIT_F:
      case SIGN_E:
      case DEC_POINT:
      case SIGN_POINT_E:
        input(cmd);
        break;
      case ENTER:
        enter();
        break;
      case CLEAR:
        if (inputInProgress)
          input(cmd);
        else
          binary(cmd);
        break;
      case ADD:   case SUB:   case MUL:   case DIV:
      case PERCENT_CHG:
      case YPOWX: case XRTY:
      case ATAN2: case HYPOT:
      case PYX:   case CYX:
      case AND:   case OR:    case XOR:   case BIC:
      case YUPX:  case YDNX:
      case DHMS_PLUS:
      case FINANCE_MULINT: case FINANCE_DIVINT:
        binary(cmd);
        break;
      case NEG:   case RECIP: case SQR:   case SQRT:
      case LN:    case EXP:   case LOG10: case EXP10: case LOG2: case EXP2:
      case FACT:  case GAMMA:
      case SIN:   case COS:   case TAN:
      case ASIN:  case ACOS:  case ATAN:
      case SINH:  case COSH:  case TANH:
      case ASINH: case ACOSH: case ATANH:
      case NOT:
      case ROUND: case CEIL:  case FLOOR: case TRUNC: case FRAC:
      case PERCENT:
      case XCHGMEM:
      case TO_DEG: case TO_RAD: case TO_DHMS: case TO_H:
      case LIN_YEST: case LIN_XEST: case LOG_YEST: case LOG_XEST:
      case EXP_YEST: case EXP_XEST: case POW_YEST: case POW_XEST:
        unary(cmd,param);
        break;
      case PI:
        push(Real.PI);
        break;
      case RANDOM:
        rTmp.random();
        push(rTmp);
        break;
      case TIME:
        rTmp.time();
        push(rTmp);
        break;
      case DATE:
        rTmp.date();
        push(rTmp);
        break;
      case RP:
      case PR:
      case XCHG:
        xyOp(cmd);
        break;
      case CLS:
        lastx.assign(stack[0]);
        clearStack();
        undoOp = UNDO_NONE; // Cannot undo this
        break;
      case RCLST:
        push(stack[param]);
        break;
      case LASTX:
        push(lastx);
        break;
      case UNDO:
        undo();
        break;
      case STO:
        if (inputInProgress)
          parseInput();
        allocMem();
        mem[param].assign(stack[0]);
        if (monitorMode == MONITOR_MEM) {
          monitorStr[param] = null;
          repaint(-1);
        }
        break;
      case STP:
        if (inputInProgress)
          parseInput();
        allocMem();
        mem[param].add(stack[0]);
        if (monitorMode == MONITOR_MEM) {
          monitorStr[param] = null;
          repaint(-1);
        }
        break;
      case RCL:
        if (mem != null)
          push(mem[param]);
        else
          push(Real.ZERO);
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
      case N:
        push(SUM1);
        break;
      case SUMX:
        push(SUMx);
        break;
      case SUMXX:
        push(SUMx2);
        break;
      case SUMY:
        push(SUMy);
        break;
      case SUMYY:
        push(SUMy2);
        break;
      case SUMXY:
        push(SUMxy);
        break;
      case SUMLNX:
        push(SUMlnx);
        break;
      case SUMLN2X:
        push(SUMln2x);
        break;
      case SUMLNY:
        push(SUMlny);
        break;
      case SUMLN2Y:
        push(SUMln2y);
        break;
      case SUMXLNY:
        push(SUMxlny);
        break;
      case SUMYLNX:
        push(SUMylnx);
        break;
      case SUMLNXLNY:
        push(SUMlnxlny);
        break;
      case FACTORIZE:
        if (inputInProgress)
          parseInput();
        lastx.assign(stack[0]);
        stack[0].round();
        if (stack[0].exponent > 0x4000001e) {
          push(Real.NAN);
        } else {
          int a = stack[0].toInteger();
          int b = greatestFactor(a);
          rollUp();
          lasty.assign(stack[0]);
          undoStackEmpty = strStack[0]==empty ? strStack[1]==empty ? 2 : 1 : 0;
          stack[0].assign((b!=0) ? a/b : 0);
          stack[1].assign(b);
          strStack[0] = null;
          strStack[1] = null;
          repaint(-1);
        }
        undoOp = UNDO_PUSHXY;
        break;

      case FINANCE_STO:
        if (inputInProgress)
          parseInput();
        allocFinance();
        finance[param].assign(stack[0]);
        if (monitorMode == MONITOR_FINANCE) {
          monitorStr[param] = null;
          repaint(-1);
        }
        break;
      case FINANCE_RCL:
        if (finance != null)
          push(finance[param]);
        else
          push(Real.ZERO);
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
        if (inputInProgress)
          parseInput();
        begin = !begin;
        break;

      case MONITOR_NONE:
        if (inputInProgress)
          parseInput();
        monitorMode = cmd;
        monitorSize = 0;
        clearMonitorStrings();
        repaint(-1);
        break;
      case MONITOR_MEM:
        if (inputInProgress)
          parseInput();
        monitorMode = cmd;
        monitorSize = param > MEM_SIZE ? MEM_SIZE : param;
        monitors = mem;
        monitorLabels = memLabels;
        clearMonitorStrings();
        repaint(-1);
        break;
      case MONITOR_STAT:
        if (inputInProgress)
          parseInput();
        monitorMode = cmd;
        monitorSize = param > STAT_SIZE ? STAT_SIZE : param;
        monitors = stat;
        monitorLabels = statLabels;
        clearMonitorStrings();
        repaint(-1);
        break;
      case MONITOR_FINANCE:
        if (inputInProgress)
          parseInput();
        monitorMode = cmd;
        monitorSize = FINANCE_SIZE;
        monitors = finance;
        monitorLabels = financeLabels;
        clearMonitorStrings();
        repaint(-1);
        break;

      case NORM:
        if (inputInProgress)
          parseInput();
        if (format.fse != Real.NumberFormat.FSE_NONE) {
          format.fse = Real.NumberFormat.FSE_NONE;
          clearStrings();
        }
        break;
      case FIX:
        if (inputInProgress)
          parseInput();
        if (format.fse != Real.NumberFormat.FSE_FIX ||
            format.precision != param)
        {
          format.fse = Real.NumberFormat.FSE_FIX;
          format.precision = param;
          clearStrings();
        }
        break;
      case SCI:
        if (inputInProgress)
          parseInput();
        if (format.fse != Real.NumberFormat.FSE_SCI ||
            format.precision != param)
        {
          format.fse = Real.NumberFormat.FSE_SCI;
          format.precision = param;
          clearStrings();
        }
        break;
      case ENG:
        if (inputInProgress)
          parseInput();
        if (format.fse != Real.NumberFormat.FSE_ENG ||
            format.precision != param)
        {
          format.fse = Real.NumberFormat.FSE_ENG;
          format.precision = param;
          clearStrings();
        }
        break;
      case POINT_DOT:
        if (inputInProgress)
          parseInput();
        if (format.point != '.') {
          format.point = '.';
          if (format.thousand == '.')
            format.thousand = ',';
          clearStrings();
        }
        break;
      case POINT_COMMA:
        if (inputInProgress)
          parseInput();
        if (format.point != ',') {
          format.point = ',';
          if (format.thousand == ',')
            format.thousand = '.';
          clearStrings();
        }
        break;
      case POINT_REMOVE:
        if (inputInProgress)
          parseInput();
        if (!format.removePoint) {
          format.removePoint = true;
          clearStrings();
        }
        break;
      case POINT_KEEP:
        if (inputInProgress)
          parseInput();
        if (format.removePoint) {
          format.removePoint = false;
          clearStrings();
        }
        break;
      case THOUSAND_DOT:
        if (inputInProgress)
          parseInput();
        if (format.thousand != '.' && format.thousand != ',') {
          format.thousand = (format.point=='.') ? ',' : '.';
          clearStrings();
        }
        break;
      case THOUSAND_SPACE:
        if (inputInProgress)
          parseInput();
        if (format.thousand != ' ') {
          format.thousand = ' ';
          clearStrings();
        }
        break;
      case THOUSAND_QUOTE:
        if (inputInProgress)
          parseInput();
        if (format.thousand != '\'') {
          format.thousand = '\'';
          clearStrings();
        }
        break;
      case THOUSAND_NONE:
        if (inputInProgress)
          parseInput();
        if (format.thousand != 0) {
          format.thousand = 0;
          clearStrings();
        }
        break;
      case BASE_BIN:
        if (inputInProgress)
          parseInput();
        if (format.base != 2) {
          format.base = 2;
          clearStrings();
        }
        break;
      case BASE_OCT:
        if (inputInProgress)
          parseInput();
        if (format.base != 8) {
          format.base = 8;
          clearStrings();
        }
        break;
      case BASE_DEC:
        if (inputInProgress)
          parseInput();
        if (format.base != 10) {
          format.base = 10;
          clearStrings();
        }
        break;
      case BASE_HEX:
        if (inputInProgress)
          parseInput();
        if (format.base != 16) {
          format.base = 16;
          clearStrings();
        }
        break;
      case TRIG_DEGRAD:
        if (inputInProgress)
          parseInput();
        degrees = !degrees;
        break;
      case FINALIZE:
        if (inputInProgress)
          parseInput();
        break;
      case FREE_MEM:
        if (inputInProgress)
          parseInput();
        rollUp();
        rollUp();
        Runtime.getRuntime().gc();
        stack[0].assign(Runtime.getRuntime().freeMemory());
        stack[1].assign(Runtime.getRuntime().totalMemory());
        strStack[0] = null;
        strStack[1] = null;
        repaint(-1);
        break;
    }
  }

  private int rangeScale(Real x, Real min, Real max, int size, Real offset) {
    if (!x.isFinite())
      return 0x7fffffff;
    rTmp.assign(x);
    rTmp.sub(min);
    rTmp2.assign(max);
    rTmp2.sub(min);
    rTmp.div(rTmp2);
    rTmp2.assign(size-1);
    rTmp.mul(rTmp2);
    rTmp.add(offset);
    rTmp.round();
    return rTmp.toInteger();
  }

  private int findTickStep(Real step, Real min, Real max, int size) {
    rTmp.assign(max);
    rTmp.sub(min);
    rTmp2.assign(10); // minimum tick distance
    rTmp.mul(rTmp2);
    rTmp2.assign(size);
    rTmp.div(rTmp2);
    step.assign(rTmp);
    step.lowPow10(); // convert to lower power of 10
    rTmp.div(step);
    rTmp2.assign(2);
    if (rTmp.lessThan(rTmp2)) {
      step.mul(rTmp2);
      return 5;
    }
    rTmp2.assign(5);
    if (rTmp.lessThan(rTmp2)) {
      step.mul(rTmp2);
      return 2;
    }
    rTmp2.assign(10);
    step.mul(rTmp2);
    return 10;
  }

  public boolean draw(int cmd, Graphics g, int gx, int gy, int gw, int gh) {
    if (SUM1 == null || statLogSize == 0)
      return false;
    Real xMin = new Real();
    Real xMax = new Real();
    Real yMin = new Real();
    Real yMax = new Real();
    Real x = new Real();
    Real y = new Real();
    Real a = new Real();
    Real b = new Real();
    int i,xi,yi,pyi,inc,bigTick;

    // Find boundaries
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
    if (xMin.equals(xMax) || yMin.equals(yMax))
      return false;
    // Expand boundaries by 10%
    rTmp.assign("1.1");
    xMin.mul(rTmp);
    xMax.mul(rTmp);
    yMin.mul(rTmp);
    yMax.mul(rTmp);

    g.setClip(gx,gy,gw,gh);
    // shrink window by 4 pixels
    gx += 2;
    gy += 2;
    gw -= 4;
    gh -= 4;

    // Draw X axis
    g.setColor(0,255,128);
    yi = gy+rangeScale(Real.ZERO,yMax,yMin,gh,Real.ZERO);
    g.drawLine(gx-1,yi,gx+gw,yi);
    bigTick = findTickStep(a,xMin,xMax,gw);
    x.assign(a);
    x.neg();
    i = -1;
    while (x.greaterThan(xMin)) {
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
    while (x.lessThan(xMax)) {
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
    g.drawLine(xi,gy-1,xi,gy+gh);
    bigTick = findTickStep(a,yMin,yMax,gh);
    y.assign(a);
    y.neg();
    i = -1;
    while (y.greaterThan(yMin)) {
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
    while (y.lessThan(yMax)) {
      yi = gy+rangeScale(y,yMax,yMin,gh,Real.ZERO);
      inc = (i%bigTick == 0) ? 2 : 1;
      g.setColor(0,32,16);
      g.drawLine(gx-1,yi,gx+gw,yi);
      g.setColor(0,255,128);
      g.drawLine(xi-inc,yi,xi+inc,yi);
      y.add(a);
      i++;
    }

    // Draw graph
    g.setColor(255,0,128);
    switch (cmd) {
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
    if (cmd != AVG_DRAW && a.isFinite() && b.isFinite()) {
      pyi = -1000;
      inc = (cmd==LIN_DRAW) ? gw-1 : 5;
      for (xi=0; xi<gw+5; xi+=inc) {
        x.assign(xi);
        rTmp.assign(gw-1);
        x.div(rTmp);
        rTmp.assign(xMax);
        rTmp.sub(xMin);
        x.mul(rTmp);
        x.add(xMin);
        if (cmd==LOG_DRAW || cmd==POW_DRAW)
          x.ln();
        y.assign(x);
        y.mul(a);
        y.add(b);
        if (cmd==EXP_DRAW || cmd==POW_DRAW)
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
    if (cmd == AVG_DRAW) {
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

    return true;
  }
}
