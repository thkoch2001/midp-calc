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
  public static final int XCHGST         =  67;
  public static final int RCLST          =  68;
  public static final int LASTX          =  69;
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
  public static final int AVG_DRAW       = 200;
  public static final int LIN_DRAW       = 201;
  public static final int LOG_DRAW       = 202;
  public static final int EXP_DRAW       = 203;
  public static final int POW_DRAW       = 204;
  public static final int FINALIZE       = 500;
  public static final int FREE_MEM       = 501;
  
  private static final int STACK_SIZE = 16;
  private static final int MEM_SIZE = 16;
  private static final int STATLOG_SIZE = 64;

  private static final Real Real180 = new Real(180);
  private static final String empty = "";
  
  public Real.NumberFormat format;
  public Real [] stack;
  public String [] strStack;
  public Real [] mem;
  public Real SUM1,SUMx,SUMx2,SUMy,SUMy2,SUMxy;
  public Real SUMlnx,SUMln2x,SUMlny,SUMln2y,SUMxlny,SUMylnx,SUMlnxlny;
  public int [] statLog; // Low-precision statistics log
  public int statLogStart,statLogEnd;
  public Real lastx;
  public boolean degrees;
  public StringBuffer inputBuf;
  public boolean inputInProgress;
  private Real rTmp,rTmp2,rTmp3;
  private int repaintLines;

  public CalcEngine()
  {
    format = new Real.NumberFormat();
    stack = new Real[STACK_SIZE];
    for (int i=0; i<STACK_SIZE; i++)
      stack[i] = new Real();
    strStack = new String[STACK_SIZE];
    mem = null;

    lastx = new Real();
    rTmp = new Real();
    rTmp2 = new Real();
    rTmp3 = new Real();
    inputBuf = new StringBuffer(40);
    degrees = false;
    clearStack();
    clearMem();
    clearStat();

    Real.accumulateRandomness(System.currentTimeMillis());
  }

  private void clearStrings() {
    if (inputInProgress)
      parseInput();
    for (int i=0; i<STACK_SIZE; i++)
      if (strStack[i] != empty)
        strStack[i] = null;
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
    mem = null;
  }
  
  private void clearStat() {
    if (inputInProgress)
      parseInput();
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
    statLog   = null;
  }

  private void allocMem() {
    if (mem != null)
      return;
    mem = new Real[MEM_SIZE];
    for (int i=0; i<MEM_SIZE; i++)
      mem[i] = new Real();
  }
  
  private void allocStat() {
    if (SUM1 != null)
      return;
    SUM1      = new Real();
    SUMx      = new Real();
    SUMx2     = new Real();
    SUMy      = new Real();
    SUMy2     = new Real();
    SUMxy     = new Real();
    SUMlnx    = new Real();
    SUMln2x   = new Real();
    SUMlny    = new Real();
    SUMln2y   = new Real();
    SUMxlny   = new Real();
    SUMylnx   = new Real();
    SUMlnxlny = new Real();
    statLog = new int[2*STATLOG_SIZE];
    statLogStart = statLogEnd = 0;
  }

  private static final byte PROPERTY_SETTINGS = 10;
  private static final byte PROPERTY_STACK    = 11;
  private static final byte PROPERTY_MEM      = 12;
  private static final byte PROPERTY_STAT     = 13;

  public void saveState(PropertyStore propertyStore) {
    int i;
    byte [] buf = new byte[STACK_SIZE*12+1];
    buf[0] = PROPERTY_STACK;
    for (i=0; i<STACK_SIZE; i++)
      stack[i].toBytes(buf, i*12+1);
    propertyStore.setProperty(buf,STACK_SIZE*12+1);
    buf[0] = PROPERTY_MEM;
    if (mem != null) {
      for (i=0; i<MEM_SIZE; i++)
        mem[i].toBytes(buf, i*12+1);
      propertyStore.setProperty(buf,MEM_SIZE*12+1);
    } else {
      propertyStore.setProperty(buf,1);
    }
    buf[0] = PROPERTY_STAT;
    if (SUM1 != null) {
      SUM1     .toBytes(buf, 0*12+1);
      SUMx     .toBytes(buf, 1*12+1);
      SUMx2    .toBytes(buf, 2*12+1);
      SUMy     .toBytes(buf, 3*12+1);
      SUMy2    .toBytes(buf, 4*12+1);
      SUMxy    .toBytes(buf, 5*12+1);
      SUMlnx   .toBytes(buf, 6*12+1);
      SUMln2x  .toBytes(buf, 7*12+1);
      SUMlny   .toBytes(buf, 8*12+1);
      SUMln2y  .toBytes(buf, 9*12+1);
      SUMxlny  .toBytes(buf,10*12+1);
      SUMylnx  .toBytes(buf,11*12+1);
      SUMlnxlny.toBytes(buf,12*12+1);
      propertyStore.setProperty(buf,13*12+1);
    } else {
      propertyStore.setProperty(buf,1);
    }
    // Settings
    for (i=0; i<STACK_SIZE; i++)
      if (strStack[i] == empty)
        break;
    buf[ 0] = PROPERTY_SETTINGS;
    buf[ 1] = (byte)i; // Height of stack
    buf[ 2] = (byte)(degrees ? 1 : 0);
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
    propertyStore.setProperty(buf,26+12);
  }
  
  public void restoreState(PropertyStore propertyStore) {
    int length,i;
    byte [] buf = new byte[STACK_SIZE*12+1];
    buf[0] = PROPERTY_STACK;
    length = propertyStore.getProperty(buf);
    if (length >= STACK_SIZE*12+1)
      for (i=0; i<STACK_SIZE; i++)
        stack[i].assign(buf, i*12+1);
    buf[0] = PROPERTY_MEM;
    length = propertyStore.getProperty(buf);
    if (length >= MEM_SIZE*12+1) {
      allocMem();
      for (i=0; i<MEM_SIZE; i++)
        mem[i].assign(buf, i*12+1);
    }
    buf[0] = PROPERTY_STAT;
    length = propertyStore.getProperty(buf);
    if (length >= 13*12+1) {
      allocStat();
      SUM1     .assign(buf, 0*12+1);
      SUMx     .assign(buf, 1*12+1);
      SUMx2    .assign(buf, 2*12+1);
      SUMy     .assign(buf, 3*12+1);
      SUMy2    .assign(buf, 4*12+1);
      SUMxy    .assign(buf, 5*12+1);
      SUMlnx   .assign(buf, 6*12+1);
      SUMln2x  .assign(buf, 7*12+1);
      SUMlny   .assign(buf, 8*12+1);
      SUMln2y  .assign(buf, 9*12+1);
      SUMxlny  .assign(buf,10*12+1);
      SUMylnx  .assign(buf,11*12+1);
      SUMlnxlny.assign(buf,12*12+1);
    }
    // Settings
    buf[0] = PROPERTY_SETTINGS;
    length = propertyStore.getProperty(buf);
    if (length >= 26+12) {
      for (i=0; i<STACK_SIZE; i++)
        strStack[i] = i<buf[1] ? null : empty;
      degrees            = buf[2] != 0;
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
          binary(CLEAR);
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
    lastx.assign(x);
    Real y = stack[1];
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
        x.div(y);
        y.assign(100);
        y.mul(x);
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
      case CLEAR:                          break;
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
      case XCHGST:
        if (strStack[param] != empty) {
          tmp = stack[0];
          stack[0] = stack[param];
          stack[param] = tmp;
          strStack[param] = strStack[0];
          repaint(param+1);
        }
        break;
      case XCHGMEM:
        allocMem();
        tmp = stack[0];
        stack[0] = mem[param];
        mem[param] = tmp;
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
    lastx.assign(x);
    Real y = stack[1];
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

  private void recall(Real x) {
    if (inputInProgress)
      parseInput();
    rollUp();
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
    switch (cmd) {
      case SUMPL:
        statLog[statLogEnd*2] = x.toFloatBits();
        statLog[statLogEnd*2+1] = y.toFloatBits();
        statLogEnd = (statLogEnd+1)%STATLOG_SIZE;
        if (statLogEnd == statLogStart) // This leaves one unused entry!
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
        // Statistics log: can't do anything except delete last input
        if (statLogEnd != statLogStart)
          statLogStart = (statLogStart+STATLOG_SIZE-1)%STATLOG_SIZE;

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
    recall(SUM1);
  }

  private void stat2(int cmd) {
    if (inputInProgress)
      parseInput();
    allocStat();
    rollUp();
    rollUp();
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

  public void command(int cmd, int param) {
    switch (cmd) {
      case DIGIT_0: case DIGIT_1: case DIGIT_2: case DIGIT_3:
      case DIGIT_4: case DIGIT_5: case DIGIT_6: case DIGIT_7:
      case DIGIT_8: case DIGIT_9: case DIGIT_A: case DIGIT_B:
      case DIGIT_C: case DIGIT_D: case DIGIT_E: case DIGIT_F:
      case SIGN_E:
      case DEC_POINT:
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
      case XCHGST:
      case XCHGMEM:
      case TO_DEG: case TO_RAD: case TO_DHMS: case TO_H:
      case LIN_YEST: case LIN_XEST: case LOG_YEST: case LOG_XEST:
      case EXP_YEST: case EXP_XEST: case POW_YEST: case POW_XEST:
        unary(cmd,param);
        break;
      case PI:
        recall(Real.PI);
        break;
      case RANDOM:
        rTmp.random();
        recall(rTmp);
        break;
      case TIME:
        rTmp.time();
        recall(rTmp);
        break;
      case DATE:
        rTmp.date();
        recall(rTmp);
        break;
      case RP:
      case PR:
      case XCHG:
        xyOp(cmd);
        break;
      case CLS:
        clearStack();
        break;
      case RCLST:
        recall(stack[param]);
        break;
      case LASTX:
        recall(lastx);
        break; // NOP for now
      case STO:
        if (inputInProgress)
          parseInput();
        allocMem();
        mem[param].assign(stack[0]);
        break;
      case STP:
        if (inputInProgress)
          parseInput();
        allocMem();
        mem[param].add(stack[0]);
        break;
      case RCL:
        if (mem != null)
          recall(mem[param]);
        else
          recall(Real.ZERO);
        break;
      case CLMEM:
        clearMem();
        break;
      case SUMPL:
      case SUMMI:
        sum(cmd);
        break;
      case CLST:
        clearStat();
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
        recall(SUM1);
        break;
      case SUMX:
        recall(SUMx);
        break;
      case SUMXX:
        recall(SUMx2);
        break;
      case SUMY:
        recall(SUMy);
        break;
      case SUMYY:
        recall(SUMy2);
        break;
      case SUMXY:
        recall(SUMxy);
        break;
      case SUMLNX:
        recall(SUMlnx);
        break;
      case SUMLN2X:
        recall(SUMln2x);
        break;
      case SUMLNY:
        recall(SUMlny);
        break;
      case SUMLN2Y:
        recall(SUMln2y);
        break;
      case SUMXLNY:
        recall(SUMxlny);
        break;
      case SUMYLNX:
        recall(SUMylnx);
        break;
      case SUMLNXLNY:
        recall(SUMlnxlny);
        break;
      case FACTORIZE:
        if (inputInProgress)
          parseInput();
        stack[0].round();
        if (stack[0].exponent > 0x4000001e) {
          recall(Real.NAN);
        } else {
          int a = stack[0].toInteger();
          int b = greatestFactor(a);
          rollUp();
          stack[0].assign(a/b);
          stack[1].assign(b);
          strStack[0] = null;
          strStack[1] = null;
          repaint(-1);
        }
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

  private int rangeScale(Real x, Real min, Real max, int size) {
    if (!x.isFinite())
      return 0x7fffffff;
    rTmp.assign(x);
    rTmp.sub(min);
    rTmp2.assign(max);
    rTmp2.sub(min);
    rTmp.div(rTmp2);
    rTmp2.assign(size-1);
    rTmp.mul(rTmp2);
    rTmp.round();
    return rTmp.toInteger();
  }

  public boolean draw(int cmd, Graphics g, int gx, int gy, int gw, int gh) {
    if (SUM1 == null || statLogStart == statLogEnd)
      return false;
    Real xMin = new Real();
    Real xMax = new Real();
    Real yMin = new Real();
    Real yMax = new Real();
    Real x = new Real();
    Real y = new Real();
    Real a = new Real();
    Real b = new Real();
    int i,xi,yi,pyi;

    // Find boundaries
    for (i=statLogStart; i!=statLogEnd; i = (i+1)%STATLOG_SIZE) {
      x.assignFloatBits(statLog[i*2]);
      y.assignFloatBits(statLog[i*2+1]);
      if (x.isFinite() && y.isFinite()) {
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

    // shrink window by 4 pixels
    gx += 2;
    gy += 2;
    gw -= 4;
    gh -= 4;

    // Draw axis
    g.setColor(0,255,0);
    xi = gx+rangeScale(Real.ZERO,xMin,xMax,gw);
    yi = gy+rangeScale(Real.ZERO,yMax,yMin,gh);
    g.drawLine(xi,gy-1,xi,gy+gh);
    g.drawLine(gx-1,yi,gx+gw,yi);

    // Draw points
    g.setColor(255,255,255);
    for (i=statLogStart; i!=statLogEnd; i = (i+1)%STATLOG_SIZE) {
      x.assignFloatBits(statLog[i*2]);
      y.assignFloatBits(statLog[i*2+1]);
      if (x.isFinite() && y.isFinite()) {
        xi = gx+rangeScale(x,xMin,xMax,gw);
        yi = gy+rangeScale(y,yMax,yMin,gh);
        g.drawLine(xi,yi-1,xi,yi+1);
        g.drawLine(xi-1,yi,xi+1,yi);
      }
    }

    // Draw graph
    g.setColor(255,0,0);
    switch (cmd) {
      case AVG_DRAW:
        x.assign(SUMx);
        x.div(SUM1);
        y.assign(SUMy);
        y.div(SUM1);
        if (x.isFinite() && y.isFinite()) {
          xi = gx+rangeScale(x,xMin,xMax,gw);
          yi = gy+rangeScale(y,yMax,yMin,gh);
          g.fillRect(xi-1,yi-1,3,3);
        }
        return true;
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
    pyi = -1;
    for (xi=0; xi<gw; xi++) {
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
      yi = rangeScale(y,yMax,yMin,gh);
      if (yi>=0 && yi<gh) {
        if (pyi > 0)
          g.drawLine(xi-1+gx,pyi+gy,xi+gx,yi+gy);
        pyi = yi;
      } else {
        if (pyi > 0)
          break; // We have drawn from inside to outside
        pyi = -1;
      }
    }
    return true;
  }
}
