package ral;

public final class CalcEngine
{
  public static final int DIGIT_0 = 0;
  public static final int DIGIT_1 = 1;
  public static final int DIGIT_2 = 2;
  public static final int DIGIT_3 = 3;
  public static final int DIGIT_4 = 4;
  public static final int DIGIT_5 = 5;
  public static final int DIGIT_6 = 6;
  public static final int DIGIT_7 = 7;
  public static final int DIGIT_8 = 8;
  public static final int DIGIT_9 = 9;
  public static final int DIGIT_A = 10;
  public static final int DIGIT_B = 11;
  public static final int DIGIT_C = 12;
  public static final int DIGIT_D = 13;
  public static final int DIGIT_E = 14;
  public static final int DIGIT_F = 15;
  public static final int SIGN_E = 16;
  public static final int DEC_POINT = 17;
  public static final int ENTER = 18;
  public static final int CLEAR = 19;
  public static final int ADD = 20;
  public static final int SUB = 21;
  public static final int MUL = 22;
  public static final int DIV = 23;
  public static final int NEG = 24;
  public static final int ABS = 25;
  public static final int RECIP = 26;
  public static final int SQR = 27;
  public static final int SQRT = 28;
  public static final int YPOWX = 29;
  public static final int XRTY = 30;
  public static final int LN = 31;
  public static final int EXP = 32;
  public static final int LOG10 = 33;
  public static final int EXP10 = 34;
  public static final int LOG2 = 35;
  public static final int EXP2 = 36;
  public static final int PYX = 37;
  public static final int CYX = 38;
  public static final int FACT = 39;
  public static final int GAMMA = 40;
  public static final int RP = 41;
  public static final int PR = 42;
  public static final int ATAN2 = 43;
  public static final int HYPOT = 44;
  public static final int SIN = 45;
  public static final int COS = 46;
  public static final int TAN = 47;
  public static final int ASIN = 48;
  public static final int ACOS = 49;
  public static final int ATAN = 50;
  public static final int SINH = 51;
  public static final int COSH = 52;
  public static final int TANH = 53;
  public static final int ASINH = 54;
  public static final int ACOSH = 55;
  public static final int ATANH = 56;
  public static final int PI = 57;
  public static final int AND = 58;
  public static final int OR = 59;
  public static final int XOR = 60;
  public static final int BIC = 61;
  public static final int NOT = 62;
  public static final int YUPX = 63;
  public static final int YDNX = 64;
  public static final int XCHG = 65;
  public static final int CLS = 66;
  public static final int XCHGN = 67;
  public static final int RCLN = 68;
  public static final int LASTX = 69;
  public static final int ROUND = 70;
  public static final int CEIL = 71;
  public static final int FLOOR = 72;
  public static final int TRUNC = 73;
  public static final int FRAC = 74;
  public static final int STO = 75;
  public static final int STP = 76;
  public static final int RCL = 77;
  public static final int SUMPL = 78;
  public static final int SUMMI = 79;
  public static final int CLST = 80;
  public static final int AVG = 81;
  public static final int S = 82;
  public static final int LR = 83;
  public static final int YR = 84;
  public static final int N = 85;
  public static final int SUMX = 86;
  public static final int SUMXX = 87;
  public static final int SUMY = 88;
  public static final int SUMYY = 89;
  public static final int SUMXY = 90;
  public static final int NORM = 91;
  public static final int FIX = 92;
  public static final int SCI = 93;
  public static final int ENG = 94;
  public static final int POINT_DOT = 95;
  public static final int POINT_COMMA = 96;
  public static final int POINT_REMOVE = 97;
  public static final int POINT_KEEP = 98;
  public static final int THOUSAND_DOT = 99;
  public static final int THOUSAND_SPACE = 100;
  public static final int THOUSAND_NONE = 101;
  public static final int BASE_BIN = 102;
  public static final int BASE_OCT = 103;
  public static final int BASE_DEC = 104;
  public static final int BASE_HEX = 105;
  public static final int TRIG_DEG = 106;
  public static final int TRIG_RAD = 107;

  private static final Real Real180 = new Real(180);
  private static final Real RealFFFF = new Real("4294967295");
  
  private Real.NumberFormat format;
  private Real [] stack;
  private String [] strStack;
  private StringBuffer inputBuf;
  private boolean degrees;
  private boolean clearOnInput;
  private boolean inputInProgress;
  private int repaintLines;

  private void clearStrings() {
    if (inputInProgress)
      parseInput();
    for (int i=0; i<stack.length; i++)
      strStack[i] = null;
    repaint(-1);
    clearOnInput = false;
  }

  private void clearStack() {
    inputInProgress = false;
    clearOnInput = false;
    for (int i=0; i<stack.length; i++) {
      stack[i] = new Real(Real.NAN);
      strStack[i] = "";
    }
    repaint(-1);
  }
  
  public CalcEngine()
  {
    stack = new Real[20];
    strStack = new String[stack.length];
    format = new Real.NumberFormat();
    inputBuf = new StringBuffer(40);
    degrees = false;
    clearStack();
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

  public boolean inputIsInProgress() {
    return inputInProgress;
  }

  public StringBuffer getInputBuffer() {
    return inputBuf;
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
        inputBuf.append(format.point);
        break;
      case SIGN_E:
        if (inputBuf.length()>0 && inputBuf.charAt(inputBuf.length()-1)=='-'){
          inputBuf.setLength(inputBuf.length()-1);
          break;
        }
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
    if (!inputInProgress && !clearOnInput)
      enter();
    clearOnInput = false;
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
    Real tmp = stack[stack.length-1];
    String tmpStr = strStack[stack.length-1];
    for (int i=stack.length-1; i>0; i--) {
      stack[i] = stack[i-1];
      strStack[i] = strStack[i-1];
    }
    stack[0] = tmp;
    strStack[0] = tmpStr;
  }

  private void rollDown() {
    Real tmp = stack[0];
    String tmpStr = strStack[0];
    for (int i=0; i<stack.length-1; i++) {
      stack[i] = stack[i+1];
      strStack[i] = strStack[i+1];
    }
    stack[stack.length-1] = tmp;
    strStack[stack.length-1] = tmpStr;
  }

  private void enter() {
    if (inputInProgress)
      parseInput();
    clearOnInput = true;
    rollUp();
    stack[0].assign(stack[1]);
    strStack[0] = strStack[1];
    repaint(-1);
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

  private void binary(int cmd) {
    if (inputInProgress)
      parseInput();
    Real x = stack[0];
    Real y = stack[1];
    switch (cmd) {
      case ADD:   y.add(x);               break;
      case SUB:   x.neg();    y.add(x);   break;
      case MUL:   y.mul(x);               break;
      case DIV:   x.recip();  y.mul(x);   break;
      case YPOWX: y.pow(x);               break;
      case XRTY:  y.nroot(x);             break;
      case ATAN2: y.atan2(x); fromRAD(x); break;
      case HYPOT: y.hypot(x);             break;
      case PYX:
      case CYX:
        return; // NOP for now
      case AND: //y.and(x);               break;
      case OR:  //y.or(x);                break;
      case XOR: //y.xor(x);               break;
      case BIC: //y.bic(x);               break;
        return; //NOP for now
      case YUPX: y.scalbn(x.toInteger()); break;
      case YDNX: y.scalbn(-x.toInteger());break;
      case CLEAR:                         break;
    }
    rollDown();
    stack[stack.length-1].assign(Real.NAN);
    strStack[stack.length-1] = "";
    if (cmd!=CLEAR)
      strStack[0] = null;
    clearOnInput = false;
    repaint(-1);
  }

  private void unary(int cmd) {
    if (inputInProgress)
      parseInput();
    Real x = stack[0];
    switch (cmd) {
      case NEG:   x.neg();   break;
      case ABS:   x.abs();   break;
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
      case NOT:   return; // NOP for now  x.xor(RealFFFF); break;
      case ROUND: x.round(); break;
      case CEIL:  x.ceil();  break;
      case FLOOR: x.floor(); break;
      case TRUNC: x.trunc(); break;
      case FRAC:  x.frac();  break;
      case XCHGN: return; // NOP for now
    }
    strStack[0] = null;
    clearOnInput = false;
    repaint(1);
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
      case YPOWX: case XRTY:
      case ATAN2: case HYPOT:
      case PYX:   case CYX:
      case AND:   case OR:    case XOR:   case BIC:
      case YUPX:  case YDNX:
        binary(cmd);
        break;
      case NEG:   case ABS:   case RECIP: case SQR:   case SQRT:
      case LN:    case EXP:   case LOG10: case EXP10: case LOG2: case EXP2:
      case FACT:  case GAMMA:
      case SIN:   case COS:   case TAN:
      case ASIN:  case ACOS:  case ATAN:
      case SINH:  case COSH:  case TANH:
      case ASINH: case ACOSH: case ATANH:
      case NOT:
      case ROUND: case CEIL:  case FLOOR: case TRUNC: case FRAC:
      case XCHGN:
        unary(cmd);
        break;
      case RP:
      case PR:
      case PI:
      case XCHG:
        break; // NOP for now
      case CLS:
        clearStack();
        break; // NOP for now
      case RCLN:
      case LASTX:
      case STO:
      case STP:
      case RCL:
      case SUMPL:
      case SUMMI:
      case CLST:
      case AVG:
      case S:
      case LR:
      case YR:
      case N:
      case SUMX:
      case SUMXX:
      case SUMY:
      case SUMYY:
      case SUMXY:
        break; // NOP for now
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
      case TRIG_DEG:
        degrees = true;
        break;
      case TRIG_RAD:
        degrees = false;
        break;
    }
  }

  public void setMaxWidth(int max) {
    format.maxwidth = max;
  }
}
