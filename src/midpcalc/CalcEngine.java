package midpcalc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.microedition.lcdui.Graphics;

public final class CalcEngine
{
    // Commands == "keystrokes"
    // Preserve these constants for programs to work from one version to the
    // next
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
    // 150-153 previously occupied by MONITOR_*
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
    public static final int PUSH_INT       = 226;
    public static final int PUSH_INT_N     = 227;
    public static final int PUSH_INF       = 228;
    public static final int PUSH_INF_N     = 229;
    public static final int PROG_NEW       = 230;
    public static final int PROG_FINISH    = 231;
    public static final int PROG_RUN       = 232;
    public static final int PROG_PURGE     = 233;
    public static final int PROG_CLEAR     = 234;
    public static final int PROG_DIFF      = 235;
    public static final int TRANSP         = 236;
    public static final int DETERM         = 237;
    public static final int TRACE          = 238;
    public static final int MATRIX_NEW     = 239;
    public static final int MATRIX_CONCAT  = 240;
    public static final int MATRIX_STACK   = 241;
    public static final int MATRIX_SPLIT   = 242;
    public static final int MONITOR_NONE   = 243;
    public static final int MONITOR_MEM    = 244;
    public static final int MONITOR_STAT   = 245;
    public static final int MONITOR_FINANCE= 246;
    public static final int MONITOR_MATRIX = 247;
    public static final int MONITOR_ENTER  = 248;
    public static final int MONITOR_EXIT   = 249;
    public static final int MONITOR_UP     = 250;
    public static final int MONITOR_DOWN   = 251;
    public static final int MONITOR_LEFT   = 252;
    public static final int MONITOR_RIGHT  = 253;
    public static final int MONITOR_PUSH   = 254;
    public static final int MONITOR_PUT    = 255;
    public static final int MONITOR_GET    = 256;
    public static final int STAT_RCL       = 257;
    public static final int STAT_STO       = 258;
    public static final int MATRIX_SIZE    = 259;
    public static final int MATRIX_AIJ     = 260;
    public static final int TRANSP_CONJ    = 261;
    public static final int GUESS          = 262;
    public static final int INVERFC        = 263;
    public static final int PHI            = 264;
    public static final int INVPHI         = 265;
    public static final int MONITOR_PROG   = 266;
    public static final int PROG_APPEND    = 267;
    public static final int IF_EQUAL_Z     = 268;
    public static final int IF_NEQUAL_Z    = 269;
    public static final int IF_LESS_Z      = 270;
    public static final int IF_LEQUAL_Z    = 271;
    public static final int IF_GREATER_Z   = 272;
    public static final int LBL            = 273;
    public static final int GTO            = 274;
    public static final int GSB            = 275;
    public static final int RTN            = 276;
    public static final int STOP           = 277;
    public static final int DSE            = 278;
    public static final int ISG            = 279;
    public static final int MATRIX_ROW     = 280;
    public static final int MATRIX_COL     = 281;
    public static final int MATRIX_MIN     = 282;
    public static final int MATRIX_MAX     = 283;
    public static final int UNIT_CLEAR     = 284;
    public static final int UNIT_DESCRIBE  = 285;
    public static final int ROLLDN_N       = 286;
    public static final int ROLLUP_N       = 287;
    public static final int TO_ROW         = 288;
    public static final int TO_COL         = 289;
    public static final int TO_MATRIX      = 290;
    public static final int BREAK_MATRIX   = 291;
    public static final int GCD            = 292;
    public static final int TO_PRIME       = 293;
    public static final int IS_PRIME       = 294;

    public static final int MATRIX_STO     = 0x0200; // Special bit pattern
    public static final int MATRIX_RCL     = 0x0300; // Special bit pattern
    public static final int UNIT_SET       = 0x4200; // Special bit pattern
    public static final int UNIT_SET_INV   = 0x4280; // Special bit pattern
    public static final int UNIT_CONVERT   = 0x4300; // Special bit pattern

    // These commands are handled from CalcCanvas
    public static final int AVG_DRAW       = 300;
    public static final int LIN_DRAW       = 301;
    public static final int LOG_DRAW       = 302;
    public static final int EXP_DRAW       = 303;
    public static final int POW_DRAW       = 304;
    public static final int PROG_DRAW      = 305;
    public static final int PROG_DRAWPOL   = 306;
    public static final int PROG_DRAWPARM  = 307;
    public static final int PROG_DRAWZZ    = 308;
    public static final int PROG_SOLVE     = 309;
    public static final int PROG_INTEGR    = 310;
    public static final int PROG_MINMAX    = 311;

    // Special commands
    public static final int FINALIZE       = 400;
    public static final int FREE_MEM       = 401;
    public static final int VERSION        = 402;

    public static final int STACK_SIZE     = 16;
    public static final int MEM_SIZE       = 16;
    public static final int STAT_SIZE      = 13;
    public static final int STATLOG_SIZE   = 64;
    public static final int FINANCE_SIZE   = 5;
    public static final int NUM_PROGS      = 9;
    public static final int PROGLABEL_SIZE = 5;

    CanvasAccess canvas;

    public ComplexMatrixElement [] stack;
    public ComplexMatrixElement lastx,lasty,lastz;
    private final MonitorableElements stackMonitor;
    
    public ComplexMatrixElement [] mem;

    public Real SUM1,SUMx,SUMx2,SUMy,SUMy2,SUMxy;
    public Real SUMlnx,SUMln2x,SUMlny,SUMln2y,SUMxlny,SUMylnx,SUMlnxlny;
    public Element [] stat;
    public int [] statLog; // Low-precision statistics log
    public int statLogStart,statLogSize;
    static final String [] statLabels =
        { "n","ßx","ßx²","ßy","ßy²","ßxy","ß£x","ß£²x",
          "ß£y","ß£²y","ßx£y","ßy£x","ß£x£y" };

    public Real PV,FV,NP,PMT,IR;
    public Element [] finance;
    public boolean begin;
    static final String [] financeLabels = { "pv","fv","np","pmt","ir%" };

    public Real.NumberFormat format;
    public boolean degrees, grad;
    public StringBuffer inputBuf;
    public boolean inputInProgress;
    Real rTmp,rTmp2,rTmp3,rTmp4;
    Unit uTmp;
    private int repaintLines;
    public boolean initialized = false;
    private String message, messageCaption;

    public int monitorMode;
    public int requestedMonitorSize;
    public int maxDisplayedMonitorSize;
    public int monitorCol,monitorRow;
    public int monitorColOffset,monitorRowOffset;
    public boolean isInsideMonitor;
    private final MonitorableElements elementMonitor;
    private final MonitorableMatrix matrixMonitor;
    private Monitorable monitor;

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
    private int undoOp = UNDO_NONE;
    private int undoParam = 0;

    public boolean progRecording;
    public boolean progRunning;
    long progRunStart;
    final Program programs;
    public String newProgramName;
    private int currentStep;
    private boolean stopFlag;
    private boolean yieldFlag;
    private short[] returnStack;
    private int returnStackDepth;

    // for reopening monitor with same amount of lines by "edit" command
    public int progInitialMonitorSize = 0;

    private static Element[] allocElementArray(int n) {
        Element[] a = new Element[n];
        for (int i=0; i<n; i++)
            a[i] = new Element();
        return a;
    }

    private static ComplexMatrixElement[] allocComplexMatrixElementArray(int n) {
        ComplexMatrixElement[] a = new ComplexMatrixElement[n];
        for (int i=0; i<n; i++)
            a[i] = new ComplexMatrixElement();
        return a;
    }

    private static void clearElementArray(Element [] a) {
        if (a != null)
            for (int i=0; i<a.length; i++)
                a[i] = null;
    }

    public CalcEngine(CanvasAccess canvas)
    {
        this.canvas = canvas;
        format = new Real.NumberFormat();
        stack = allocComplexMatrixElementArray(STACK_SIZE);

        lastx  = new ComplexMatrixElement();
        lasty  = new ComplexMatrixElement();
        lastz  = new ComplexMatrixElement();
        rTmp   = new Real();
        rTmp2  = new Real();
        rTmp3  = new Real();
        rTmp4  = new Real();
        uTmp   = new Unit();
        inputBuf = new StringBuffer(40);
        degrees = false;
        grad = false;
        begin = false;
        clearStack();
        monitorMode = MONITOR_NONE;
        clearMonitorStrings();
        
        stackMonitor = new MonitorableElements(format).withElements(stack);
        elementMonitor = new MonitorableElements(format);
        matrixMonitor = new MonitorableMatrix(format);
        programs = new Program(NUM_PROGS, this, format);

        Real.accumulateRandomness(System.currentTimeMillis());
    }

    private void clearStrings() {
        clearStackStrings();
        clearMonitorStrings();
        repaintAll();
    }
    
    private void clearStackStrings() {
        for (int i=0; i<STACK_SIZE; i++)
            if (!stack[i].isEmpty()) {
                stack[i].clearStrings();
            }
        repaintAll();
    }

    private void clearMonitorStrings() {
        if (monitor != null)
            monitor.formatUpdated();
        repaintAll();
    }

    private void clearStack() {
        inputInProgress = false;
        for (int i=0; i<STACK_SIZE; i++) {
            stack[i].makeEmpty();
        }
        repaintAll();
    }

    private void clearMem() {
        clearElementArray(mem);
        mem = null;
        if (monitorMode == MONITOR_MEM) {
            allocMem();
            clearMonitorStrings();
            repaintAll();
        }
    }

    private void clearStat() {
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
        clearElementArray(stat);
        stat      = null;
        statLog   = null;
        if (monitorMode == MONITOR_STAT) {
            allocStat();
            clearMonitorStrings();
            repaintAll();
        }
    }

    private void clearFinance() {
        PV  = null;
        FV  = null;
        NP  = null;
        PMT = null;
        IR  = null;
        clearElementArray(finance);
        finance = null;
        if (monitorMode == MONITOR_FINANCE) {
            allocFinance();
            clearMonitorStrings();
            repaintAll();
        }
    }

    private void tryClearModules(boolean clearMem, boolean clearFinance) {
        int i;
        if (clearMem && mem != null && monitorMode != MONITOR_MEM) {
            for (i=0; i<MEM_SIZE; i++)
                if (!mem[i].isNothing())
                    break;
            if (i==MEM_SIZE)
                clearMem();
        }
        if (clearFinance && finance != null && monitorMode != MONITOR_FINANCE) {
            for (i=0; i<FINANCE_SIZE; i++)
                if (!finance[i].isZero())
                    break;
            if (i==FINANCE_SIZE)
                clearFinance();
        }
    }

    private void allocMem() {
        if (mem != null)
            return;
        mem = allocComplexMatrixElementArray(MEM_SIZE);
        for (int i=0; i<MEM_SIZE; i++) {
            mem[i].label = "M"+i;
        }
        if (monitorMode == MONITOR_MEM) {
            monitor = elementMonitor.withElements(mem);
        }
    }

    private void allocStat() {
        if (stat != null)
            return;
        stat = allocElementArray(STAT_SIZE);
        SUM1      = stat[ 0].r;
        SUMx      = stat[ 1].r;
        SUMx2     = stat[ 2].r;
        SUMy      = stat[ 3].r;
        SUMy2     = stat[ 4].r;
        SUMxy     = stat[ 5].r;
        SUMlnx    = stat[ 6].r;
        SUMln2x   = stat[ 7].r;
        SUMlny    = stat[ 8].r;
        SUMln2y   = stat[ 9].r;
        SUMxlny   = stat[10].r;
        SUMylnx   = stat[11].r;
        SUMlnxlny = stat[12].r;
        statLog = new int[STATLOG_SIZE*2];
        statLogStart = statLogSize = 0;
        for (int i=0; i<STAT_SIZE; i++) {
            stat[i].label = statLabels[i];
        }
        if (monitorMode == MONITOR_STAT) {
            monitor = elementMonitor.withElements(stat);
        }
    }

    private void allocFinance() {
        if (finance != null)
            return;
        finance = allocElementArray(FINANCE_SIZE);
        PV  = finance[0].r;
        FV  = finance[1].r;
        NP  = finance[2].r;
        PMT = finance[3].r;
        IR  = finance[4].r;
        for (int i=0; i<FINANCE_SIZE; i++) {
            finance[i].label = financeLabels[i];
        }
        if (monitorMode == MONITOR_FINANCE) {
            monitor = elementMonitor.withElements(finance);
        }
    }

    int getStackHeight() {
        int stackHeight;
        for (stackHeight=0; stackHeight<STACK_SIZE; stackHeight++)
            if (stack[stackHeight].isEmpty())
                break;
        return stackHeight;
    }
    
    int countReferences(Matrix M) {
        int refs = 0;
        for (int i=0; i<stack.length; i++)
            if (stack[i].M == M)
                refs++;
        if (mem != null)
            for (int i=0; i<mem.length; i++)
                if (mem[i].M == M)
                    refs++;
        if (lastx.M == M) refs++;
        if (lasty.M == M) refs++;
        if (lastz.M == M) refs++;
        return refs;
    }

    private void encodeMatrixReference(ComplexMatrixElement e, Matrix[] matrices) {
        if (!e.isMatrix())
            return;
        int i;
        for (i=0; i<matrices.length; i++)
            if (matrices[i] == null || matrices[i] == e.getMatrix())
                break;
        matrices[i] = e.getMatrix();
        e.makeEncodedNan(i); // Encode reference to i'th matrix
    }

    private Matrix[] encodeMatrixReferences() {
        Matrix[] matrices = new Matrix[STACK_SIZE+MEM_SIZE+1];
        for (int i=0; i<stack.length; i++)
            encodeMatrixReference(stack[i], matrices);
        if (mem != null)
            for (int i=0; i<mem.length; i++)
                encodeMatrixReference(mem[i], matrices);
        encodeMatrixReference(lastx, matrices);
        return matrices;
    }

    public void saveState(DataOutputStream out) throws IOException
    {
        tryClearModules(true,true);
        lasty.clear(); // Possibly free up some space
        lastz.clear();
        Matrix[] matrices = encodeMatrixReferences();

        if (monitorMode == MONITOR_PROG)
            monitorMode = MONITOR_NONE;

        int i,j;
        byte [] realBuf = new byte[12];

        // Stack
        int stackHeight = getStackHeight();
        if (stackHeight > 0) {
            out.writeShort(STACK_SIZE*(12*2+8));
            for (i=0; i<STACK_SIZE; i++) {
                stack[i].r.toBytes(realBuf,0);
                out.write(realBuf);
            }
            for (i=0; i<STACK_SIZE; i++) {
                stack[i].i.toBytes(realBuf,0);
                out.write(realBuf);
            }
            for (i=0; i<STACK_SIZE; i++) {
                out.writeLong(stack[i].unit);
            }
        } else {
            out.writeShort(0);
        }

        //Memory
        if (mem != null) {
            out.writeShort(MEM_SIZE*(12*2+8));
            for (i=0; i<MEM_SIZE; i++) {
                mem[i].r.toBytes(realBuf,0);
                out.write(realBuf);
            }
            for (i=0; i<MEM_SIZE; i++) {
                mem[i].i.toBytes(realBuf,0);
                out.write(realBuf);
            }
            for (i=0; i<MEM_SIZE; i++) {
                out.writeLong(stack[i].unit);
            }
        } else {
            out.writeShort(0);
        }

        // Statistics
        if (stat != null) {
            out.writeShort(STAT_SIZE*12+STATLOG_SIZE*2*4+2);
            for (i=0; i<STAT_SIZE; i++) {
                stat[i].r.toBytes(realBuf,0);
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
                finance[i].r.toBytes(realBuf,0);
                out.write(realBuf);
            }
        } else {
            out.writeShort(0);
        }

        // Settings
        out.writeShort(10+8*2+12*2+8);
        out.writeByte(stackHeight);
        out.writeByte((degrees ? 1 : 0) + (begin ? 2 : 0) + (grad ? 4 : 0));
        out.writeByte(format.base);
        out.writeByte(format.maxwidth);
        out.writeByte(format.precision);
        out.writeByte(format.fse);
        out.writeByte(format.point);
        out.writeBoolean(format.removePoint);
        out.writeByte(format.thousand);
        out.writeByte(((monitorMode-MONITOR_NONE)<<5) + requestedMonitorSize);
        out.writeLong(Real.randSeedA);
        out.writeLong(Real.randSeedB);
        lastx.r.toBytes(realBuf,0);
        out.write(realBuf);
        lastx.i.toBytes(realBuf,0);
        out.write(realBuf);
        out.writeLong(lastx.unit);

        // Programs
        for (i=0; i<NUM_PROGS; i++) {
            if (!programs.isEmpty(i)) {
                short[] prog = programs.getProg(i);
                out.writeShort(PROGLABEL_SIZE*2+prog.length*2);
                String label = programs.getProgLabel(i);
                for (j=0; j<PROGLABEL_SIZE; j++)
                    out.writeChar(j<label.length() ?
                                  label.charAt(j):0);
                for (j=0; j<prog.length; j++)
                    out.writeShort(prog[j]);
            } else {
                out.writeShort(0);
            }
        }

        // Matrices
        for (i=0; i<matrices.length; i++) {
            if (matrices[i] != null) {
                Matrix M = matrices[i];
                if (M.rows<0xffff && M.cols<0xffff) {
                    out.writeShort(2+2+M.rows*M.cols*(12+(M.isComplex()?12:0)));
                    out.writeShort((short)M.rows);
                    out.writeShort((short)M.cols);
                    for (int c=0; c<M.cols; c++)
                        for (int r=0; r<M.rows; r++) {
                            M.getElement(r,c,rTmp,null);
                            rTmp.toBytes(realBuf,0);
                            out.write(realBuf);
                        }
                    if (M.isComplex())
                        for (int c=0; c<M.cols; c++)
                            for (int r=0; r<M.rows; r++) {
                                M.getElement(r,c,null,rTmp);
                                rTmp.toBytes(realBuf,0);
                                out.write(realBuf);
                            }
                }
                matrices[i] = null; // Free memory as we go
            } else {
                out.writeShort(0);
            }
        }
    }

    private void decodeMatrixReference(ComplexMatrixElement e, Matrix[] matrices) {
        if (e.isEncodedNan())
            e.set(matrices[e.decodeNan()]);
    }

    private void decodeMatrixReferences(Matrix[] matrices) {
        for (int i=0; i<stack.length; i++)
            decodeMatrixReference(stack[i], matrices);
        if (mem != null)
            for (int i=0; i<mem.length; i++)
                decodeMatrixReference(mem[i], matrices);
        decodeMatrixReference(lastx, matrices);
    }

    public void restoreState(DataInputStream in) throws IOException {
        int length,i,j;
        byte [] realBuf = new byte[12];

        repaintAll(); // Better do it now in case of IOException

        // Stack
        length = in.readShort();
        if (length >= STACK_SIZE*12) {
            for (i=0; i<STACK_SIZE; i++) {
                in.readFully(realBuf);
                stack[i].r.assign(realBuf,0);
            }
            length -= STACK_SIZE*12;
            if (length >= STACK_SIZE*12) {
                for (i=0; i<STACK_SIZE; i++) {
                    in.readFully(realBuf);
                    stack[i].i.assign(realBuf,0);
                }
                length -= STACK_SIZE*12;
            }
            if (length >= STACK_SIZE*8) {
                for (i=0; i<STACK_SIZE; i++) {
                    stack[i].unit = in.readLong();
                }
                length -= STACK_SIZE*8;
            }
        }
        in.skip(length);

        // Memory
        length = in.readShort();
        if (length >= MEM_SIZE*12) {
            allocMem();
            for (i=0; i<MEM_SIZE; i++) {
                mem[i].clear();
                in.readFully(realBuf);
                mem[i].r.assign(realBuf,0);
            }
            length -= MEM_SIZE*12;
            if (length >= MEM_SIZE*12) {
                for (i=0; i<MEM_SIZE; i++) {
                    in.readFully(realBuf);
                    mem[i].i.assign(realBuf,0);
                }
                length -= MEM_SIZE*12;
            }
            if (length >= MEM_SIZE*8) {
                for (i=0; i<MEM_SIZE; i++) {
                    mem[i].unit = in.readLong();
                }
                length -= MEM_SIZE*8;
            }
        }
        in.skip(length);

        // Statistics
        length = in.readShort();
        if (length >= STAT_SIZE*12+STATLOG_SIZE*2*4+2) {
            allocStat();
            for (i=0; i<STAT_SIZE; i++) {
                in.readFully(realBuf);
                stat[i].r.assign(realBuf,0);
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
                in.readFully(realBuf);
                finance[i].r.assign(realBuf,0);
            }
            length -= FINANCE_SIZE*12;
        }
        in.skip(length);

        // Settings
        length = in.readShort();
        if (length >= 10+8*2+12) {
            int stackHeight = in.readByte();
            for (i=0; i<stackHeight; i++)
                stack[i].clearStrings();
            for (i=stackHeight; i<STACK_SIZE; i++)
                stack[i].makeEmpty();
            int flags = in.readByte();
            degrees            = (flags&1) != 0;
            begin              = (flags&2) != 0;
            grad               = (flags&4) != 0;
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
            in.readFully(realBuf);
            lastx.r.assign(realBuf,0);

            monitorMode = MONITOR_NONE+((monitor>>5)&7);
            int size = monitor&0x1f;
            if (monitorMode == MONITOR_MEM)
                setMonitoring(monitorMode, size, elementMonitor.withElements(mem));
            else if (monitorMode == MONITOR_STAT)
                setMonitoring(monitorMode, size, elementMonitor.withElements(stat));
            else if (monitorMode == MONITOR_FINANCE)
                setMonitoring(monitorMode, size, elementMonitor.withElements(finance));
            else if (monitorMode == MONITOR_MATRIX) {
                setMonitoring(monitorMode, size, matrixMonitor);
                // updateMatrixMonitor() will be run later
            }
            length -= 10+8*2+12;
        }
        if (length >= 12) {
            in.readFully(realBuf);
            lastx.i.assign(realBuf,0);
            length -= 12;
        }
        if (length >= 8) {
            lastx.unit = in.readLong();
            length -= 8;
        }
        in.skip(length);

        // Programs
        char [] label = new char[PROGLABEL_SIZE];
        for (i=0; i<NUM_PROGS; i++) {
            length = in.readShort();
            if (length >= PROGLABEL_SIZE*2) {
                int labelLen=0;
                for (j=0; j<PROGLABEL_SIZE; j++) {
                    label[j] = in.readChar();
                    if (label[j] != 0)
                        labelLen = j+1;
                }
                String progLabel = new String(label,0,labelLen);
                short[] prog = new short[(length-PROGLABEL_SIZE*2)/2];
                for (j=0; j<prog.length; j++)
                    prog[j] = in.readShort();
                programs.setProg(i, prog, progLabel);
                length -= PROGLABEL_SIZE*2+prog.length*2;
            }
            in.skip(length);
        }

        // Matrices
        Matrix[] matrices = new Matrix[STACK_SIZE+MEM_SIZE+1];
        for (i=0; i<matrices.length; i++) {
            length = in.readShort();
            if (length >= 2+2) {
                int rows = in.readShort()&0xffff;
                int cols = in.readShort()&0xffff;
                length -= 2*2;
                if (length >= rows*cols*12) {
                    Matrix M = new Matrix(rows,cols);
                    for (int c=0; c<cols; c++)
                        for (int r=0; r<rows; r++) {
                            in.readFully(realBuf);
                            rTmp.assign(realBuf,0);
                            M.setElement(r,c,rTmp,null);
                        }
                    length -= rows*cols*12;
                    if (length >= rows*cols*12) {
                        for (int c=0; c<cols; c++)
                            for (int r=0; r<rows; r++) {
                                in.read(realBuf);
                                rTmp.assign(realBuf,0);
                                M.setElement(r,c,null,rTmp);
                            }
                        length -= rows*cols*12;
                    }
                    matrices[i] = M;
                }
            }
            in.skip(length);
        }
        decodeMatrixReferences(matrices);

        if (monitorMode == MONITOR_MATRIX)
            updateMatrixMonitor();
    }

    private void setMonitorRow(int row, boolean wrapCol) {
        if (monitor.rows() <= 1) {
            monitorRow = 0;
            return;
        }
        if (row < 0) {
            if (wrapCol) {
                row = monitor.rows()-1;
                setMonitorCol(monitorCol-1,false);
            } else {
                row = 0;
            }
        }
        if (row >= monitor.rows()) {
            if (wrapCol) {
                row = 0;
                setMonitorCol(monitorCol+1,false);
            } else {
                row = monitor.rows()-1;
            }
        }
        monitorRow = row;
        repaintAll();
    }

    private void setMonitorCol(int col, boolean wrapRow) {
        if (monitor.cols() <= 1) {
            monitorCol = 0;
            return;
        }
        if (col < 0) {
            if (wrapRow) {
                col = monitor.cols()-1;
                setMonitorRow(monitorRow-1,false);
            } else {
                col = 0;
            }
        }
        if (col >= monitor.cols()) {
            if (wrapRow) {
                col = 0;
                setMonitorRow(monitorRow+1,false);
            } else {
                col = monitor.cols()-1;
            }
        }
        monitorCol = col;
        repaintAll();
    }

    private Matrix getCurrentMatrix() {
        for (int i=0; i<STACK_SIZE; i++)
            if (stack[i].isMatrix())
                return stack[i].M;
        return null;
    }

    private void updateMatrixMonitor() {
        Matrix M = getCurrentMatrix();
        if (M != matrixMonitor.matrix()) {
            matrixMonitor.setMatrix(M);
            if (M == null || monitorCol >= M.cols || monitorRow >= M.rows) {
                monitorCol = 0;
                monitorRow = 0;
            }
            setMonitorCol(monitorCol,false);
            setMonitorRow(monitorRow,false);
            repaintAll();
        }
    }
    
    private void initProgMonitor(boolean pointToEnd) {
        if (pointToEnd)
            setMonitorRow(programs.numProgSteps(),false);

        repaintAll();
    }

    private void setMonitoring(int mode, int requestedSize, Monitorable monitor) {
        monitorMode = mode;
        if (requestedSize == 0) {
            mode = MONITOR_NONE;
            monitor = null;
        }
        if (mode == MONITOR_MEM)
            allocMem();
        else if (mode == MONITOR_STAT)
            allocStat();
        else if (mode == MONITOR_FINANCE)
            allocFinance();

        requestedMonitorSize = requestedSize;
        this.monitor = monitor;
        monitorCol = 0;
        monitorRow = 0;
        monitorColOffset = 0;
        monitorRowOffset = 0;
        clearMonitorStrings();
        repaintAll();
    }
    
    public Monitorable getStack() {
        return stackMonitor;
    }
    
    public Monitorable getMonitor() {
        return monitor;
    }

    public boolean hasComplexArgs() {
        ComplexMatrixElement x = stack[0];
        ComplexMatrixElement y = stack[1];
        return x.isComplex() || y.isComplex() ||
            (x.isMatrix() && x.M.isComplex()) ||
            (y.isMatrix() && y.M.isComplex());
    }

    public void setDisplayProperties(int nDigits, int maxDisplayedMonitorSize) {
        format.maxwidth = nDigits;
        this.maxDisplayedMonitorSize = maxDisplayedMonitorSize;
        if (inputInProgress)
            parseInput();
        clearStackStrings();
        clearMonitorStrings();
    }

    public void adjustMonitorOffsets(int actualMonitorRows, int actualMonitorCols) {
        if (monitorRow < monitorRowOffset)
            monitorRowOffset = monitorRow;
        if (monitorRow >= monitorRowOffset + actualMonitorRows)
            monitorRowOffset = monitorRow + 1 - actualMonitorRows;
        if (monitorRowOffset > Math.max(0, monitor.rows()-actualMonitorRows))
            monitorRowOffset = Math.max(0, monitor.rows()-actualMonitorRows);
        if (monitorCol < monitorColOffset)
            monitorColOffset = monitorCol;
        if (monitorCol >= monitorColOffset + actualMonitorCols)
            monitorColOffset = monitorCol + 1 - actualMonitorCols;
        if (monitorColOffset > Math.max(0, monitor.cols()-actualMonitorCols))
            monitorColOffset = Math.max(0, monitor.cols()-actualMonitorCols);
    }

    void setMessage(String mc, String m) {
        messageCaption = mc;
        message = m;
        stopFlag = true;
        repaintAll(); // Ensure repaint
    }

    public String getMessage() {
        String m = message;
        message = null;
        return m;
    }

    public String getMessageCaption() {
        return messageCaption;
    }

    public int numRepaintLines() {
        int tmp = repaintLines;
        repaintLines = 0;
        return tmp;
    }
    
    public String progLabel(int i) {
        return programs.getProgLabel(i);
    }

    private void repaint(int nElements) {
        if (repaintLines<nElements)
            repaintLines = nElements;
    }

    private void repaintAll() {
        repaint(100);
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

        switch (cmd)
        {
            case CLEAR:
                if (inputBuf.length()==0) {
                    inputInProgress = false;
                    // This will undo the rollUp that input started with
                    rollDown(0, true);
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
                if (inputBuf.length()==0 ||
                    inputBuf.charAt(inputBuf.length()-1)=='-')
                    inputBuf.append('0');
                else if (inputBuf.length()==1 && inputBuf.charAt(0)=='/')
                    inputBuf.append(Real.hexChar.charAt(base-1));
                inputBuf.append(format.point);
                break;

            case SIGN_E:
                if (inputBuf.length()>0 &&
                    inputBuf.charAt(inputBuf.length()-1)=='-'){
                    inputBuf.setLength(inputBuf.length()-1);
                    break;
                }
                if (inputBuf.length()==0 && base!=10) {
                    inputBuf.append('/');
                    break;
                }
                if (inputBuf.length()==1 && inputBuf.charAt(0)=='/')
                    inputBuf.setLength(0);

                if (inputBuf.length()==0 ||
                    inputBuf.charAt(inputBuf.length()-1)==exp){
                    inputBuf.append('-');
                    break;
                }
                for (i=0; i<inputBuf.length(); i++)
                    if (inputBuf.charAt(i)==exp)
                        return;
                inputBuf.append(exp);
                break;

            case SIGN_POINT_E:
                if (inputBuf.length()>0 &&
                    inputBuf.charAt(inputBuf.length()-1)=='-'){
                    inputBuf.setLength(inputBuf.length()-1);
                    break;
                }
                if (inputBuf.length()==0 && base!=10) {
                    inputBuf.append('/');
                    break;
                }
                if (inputBuf.length()==1 && inputBuf.charAt(0)=='/')
                    inputBuf.setLength(0);

                if (inputBuf.length()==0 ||
                    inputBuf.charAt(inputBuf.length()-1)==exp){
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
        }
        // If routine has not returned yet, we have new input data
        if (!inputInProgress) {
            inputInProgress = true;
            rollUp(0, true);
        } else {
            repaint(1);
        }
    }

    void parseInput() {
        saveUndo(UNDO_PUSH, null, stack[0], null);
        stack[0].clear();
        stack[0].r.assign(inputBuf.toString(),format.base);

        if (progRecording) {
            currentStep = programs.numProgSteps();
            if (monitorMode == MONITOR_PROG) {
                currentStep = monitorRow;
            }
            programs.recordPush(stack[0].r, currentStep);
            if (monitorMode == MONITOR_PROG) {
                setMonitorRow(monitorRow+1, false);
            }
        }

        repaint(1);
        inputInProgress = false;
    }

    private void rollUp(int top, boolean whole) {
        if (whole) {
            top = STACK_SIZE-1;
        } else {
            top = Math.min(top, getStackHeight()-1);
            if (top<=0)
                return;
        }
        ComplexMatrixElement tmp = stack[top];
        for (int i=top; i>0; i--) {
            stack[i] = stack[i-1];
        }
        stack[0] = tmp;

        repaintAll();
    }

    private void rollDown(int top, boolean whole) {
        if (whole) {
            top = STACK_SIZE-1;
        } else {
            top = Math.min(top, getStackHeight()-1);
            if (top<=0)
                return;
        }
        ComplexMatrixElement tmp = stack[0];
        for (int i=0; i<top; i++) {
            stack[i] = stack[i+1];
        }
        stack[top] = tmp;

        repaintAll();
    }
    
    private void xchgSt(int n) {
        stack[0].xchg(stack[n], true);
        repaint(n+1);
    }

    private void enter() {
        rollUp(0, true);
        saveUndo(UNDO_PUSH, null, stack[0], null);
        stack[0].copy(stack[1]);
    }

    private void toRAD(Real x) {
        if (degrees || grad) {
            x.div(degrees ? 180 : 200);
            x.mul(Real.PI);
        }
    }

    private void fromRAD(Real x) {
        if (degrees || grad) {
            x.div(Real.PI);
            x.mul(degrees ? 180 : 200);
        }
    }

    private void compare(Real result, ComplexMatrixElement x, ComplexMatrixElement y, boolean testEquality, int cmd) {
        result.makeNan(); // Default "nan" return value specifies incomparable numbers

        if (x.isMatrix() || (y!=null && y.isMatrix())) {
            if (!x.isMatrix() || (y!=null && !y.isMatrix())) {
                setMessage(CmdDesc.getStr(cmd, true), "Comparison of matrix and number ignored");
                return;
            }
            if (!testEquality) {
                setMessage(CmdDesc.getStr(cmd, true), "Magnitude comparison of matrices ignored");
                return;
            }
            rTmp.assign((y==null ? Matrix.equalsZero(x.M) : Matrix.equals(x.M,y.M)) ? Real.ZERO : Real.ONE);
            return;
        }

        boolean complex = x.isComplex() || (y!=null && y.isComplex());
        if (complex && !testEquality) {
            setMessage(CmdDesc.getStr(cmd, true), "Magnitude comparison of complex numbers ignored");
            return;
        }

        if (x.r.isInfinity() && y!=null && y.r.isInfinity() && x.r.sign == y.r.sign && !complex && !testEquality) {
            // Infinite return value specifies that both numbers are "the same infinity"
            result.makeInfinity(0);
            return;
        }

        result.assign(x.r);
        if (complex)
            rTmp4.assign(x.i);
        
        if (y!=null) {
            if (x.hasUnit() || y.hasUnit()) {
                long unit = Unit.convertTo(x.unit, y.unit, rTmp2, rTmp3);
                if (unit != y.unit) {
                    result.makeNan();
                    setMessage(CmdDesc.getStr(cmd, true), "Comparison of numbers with incompatible units ignored");
                    return;
                }
                result.mul(rTmp2);
                result.add(rTmp3);
                if (complex)
                    rTmp4.mul(rTmp2);
            }
            result.sub(y.r);
            if (complex)
                rTmp4.sub(y.i);
        }

        if (!result.isZero() || (complex && !rTmp4.isZero())) {
            rTmp2.assign(Real.ONE);
            rTmp2.copysign(result);
            result.assign(rTmp2);
        }
    }

    void binary(int cmd) {
        ComplexMatrixElement x = stack[0];
        ComplexMatrixElement y = stack[1];
        boolean complex   = x.isComplex() || y.isComplex();
        boolean complexOk = false;
        boolean matrix    = x.isMatrix() || y.isMatrix();
        boolean matrixOk  = false;
        boolean unit      = x.hasUnit() || y.hasUnit();
        boolean unitOk    = false;

        Complex.degrees = degrees;
        Complex.grad = grad;

        saveUndo(UNDO_BINARY, x, y, null);

        switch (cmd)
        {
            case CLEAR:
                break;

            case ADD:
                if (matrix) {
                    matrixOk = true;
                    y.M = Matrix.add(y.M, x.M);
                } else {
                    complexOk = unitOk = true;
                    if (unit) {
                        y.unit = Unit.add(y.unit, x.unit, rTmp, rTmp2);
                        y.r.mul(rTmp);
                        y.r.add(rTmp2);
                        if (complex) y.i.mul(rTmp);
                    }
                    y.r.add(x.r);
                    if (complex) y.i.add(x.i);
                }
                break;

            case SUB:
                if (matrix) {
                    matrixOk = true;
                    y.M = Matrix.sub(y.M, x.M);
                } else {
                    complexOk = unitOk = true;
                    if (unit) {
                        y.unit = Unit.sub(y.unit, x.unit, rTmp, rTmp2);
                        y.r.mul(rTmp);
                        y.r.add(rTmp2);
                        if (complex) y.i.mul(rTmp);
                    }
                    y.r.sub(x.r);
                    if (complex) y.i.sub(x.i);
                }
                break;

            case MUL:
                if (matrix) {
                    matrixOk = true;
                    if (x.isMatrix() && y.isMatrix()) {
                        y.M = Matrix.mul(y.M, x.M);
                    } else if (x.isMatrix()) {
                        y.M = Matrix.mul(x.M, y.r, y.i);
                    } else {
                        y.M = Matrix.mul(y.M, x.r, x.i);
                    }
                } else {
                    complexOk = unitOk = true;
                    if (unit) {
                        y.unit = Unit.mul(y.unit, x.unit, rTmp);
                        y.r.mul(rTmp);
                        if (complex) y.i.mul(rTmp);
                    }
                    if (complex) {
                        Complex.mul(y.r, y.i, x.r, x.i);
                    } else {
                        y.r.mul(x.r);
                    }
                }
                break;

            case DIV:
                if (matrix) {
                    matrixOk = true;
                    if (x.isMatrix() && y.isMatrix()) {
                        y.M = Matrix.div(y.M, x.M);
                    } else if (x.isMatrix()) {
                        y.M = Matrix.div(y.r, y.i, x.M);
                    } else {
                        y.M = Matrix.div(y.M, x.r, x.i);
                    }
                } else {
                    complexOk = unitOk = true;
                    if (unit) {
                        y.unit = Unit.div(y.unit, x.unit, rTmp);
                        y.r.mul(rTmp);
                        if (complex) y.i.mul(rTmp);
                    }
                    if (complex) {
                        Complex.div(y.r, y.i, x.r, x.i);
                    } else {
                        y.r.div(x.r);
                    }
                }
                break;

            case PERCENT_CHG:
                if (unit) {
                    unitOk = true;
                    y.unit = Unit.add(y.unit, x.unit, rTmp, null);
                    if ((y.unit & Unit.unitError) == 0) {
                        y.r.mul(rTmp);
                        y.unit = 0;
                    }
                }
                x.r.sub(y.r);
                x.r.div(y.r);
                x.r.mul(Real.HUNDRED);
                y.r.assign(x.r);
                break;

            case YPOWX:
                if (matrix) {
                    matrixOk = true;
                    if (x.isMatrix() || !x.r.isIntegral() || complex) {
                        y.makeNan();
                        matrix = false;
                    } else {
                        y.M = Matrix.pow(y.M, x.r.toInteger());
                    }
                } else {
                    if (!y.r.isZero() && y.r.isNegative() && !x.r.isIntegral())
                        complex = true;
                    if (unit) {
                        if (x.r.isIntegral() && !x.hasUnit()) {
                            unitOk = true;
                            y.unit = Unit.pow(y.unit, x.r.toInteger());
                        }
                    }
                    if (complex) {
                        complexOk = true;
                        Complex.ln(y.r, y.i);
                        Complex.mul(y.r, y.i, x.r, x.i);
                        Complex.exp(y.r, y.i);
                    } else {
                        y.r.pow(x.r);
                    }
                }
                break;

            case XRTY:
                if (!y.r.isZero() && y.r.isNegative() &&
                    !(x.r.isIntegral() && x.r.isOdd()))
                    complex = true;
                if (unit) {
                    if (x.r.isIntegral() && !x.hasUnit()) {
                        unitOk = true;
                        y.unit = Unit.nroot(y.unit, x.r.toInteger(), rTmp);
                        y.r.mul(rTmp);
                        if (complex) y.i.mul(rTmp);
                    }
                }
                if (complex) {
                    complexOk = true;
                    Complex.ln(y.r, y.i);
                    Complex.div(y.r, y.i, x.r, x.i);
                    Complex.exp(y.r, y.i);
                } else {
                    y.r.nroot(x.r);
                }
                break;

            case PYX:
            {
                boolean integral = x.r.isIntegral() && y.r.isIntegral();
                if (y.r.isNegative() && x.r.isIntegral()) {
                    // Special case
                    // Py,x = (-1)^x * fact(-y-1+x)/fact(-y-1)
                    boolean negative = x.r.isOdd();
                    y.r.neg();
                    y.r.sub(Real.ONE);
                    x.r.add(y.r);
                    x.r.fact();
                    y.r.fact();
                    y.r.rdiv(x.r);
                    if (negative)
                        y.r.neg();
                } else {
                    // Py,x = fact(y)/fact(y-x)
                    x.r.neg();
                    x.r.add(y.r);
                    x.r.fact();
                    y.r.fact();
                    y.r.div(x.r);
                }
                if (integral)
                    y.r.round();
                break;
            }

            case CYX:
            {
                boolean integral = x.r.isIntegral() && y.r.isIntegral();
                if (y.r.isNegative() && x.r.isIntegral()) {
                    // Special case
                    // Cy,x = (-1)^x * fact(-y-1+x)/(fact(-y-1) * fact(x))
                    boolean negative = x.r.isOdd();
                    rTmp.assign(x.r);
                    rTmp.fact();
                    y.r.neg();
                    y.r.sub(Real.ONE);
                    x.r.add(y.r);
                    x.r.fact();
                    y.r.fact();
                    y.r.mul(rTmp);
                    y.r.rdiv(x.r);
                    if (negative)
                        y.r.neg();
                } else {
                    // Cy,x = fact(y)/(fact(y-x)*fact(x))
                    rTmp.assign(x.r);
                    rTmp.fact();
                    x.r.neg();
                    x.r.add(y.r);
                    x.r.fact();
                    x.r.mul(rTmp);
                    y.r.fact();
                    y.r.div(x.r);
                }
                if (integral)
                    y.r.round();
                break;
            }

            case ATAN2:
                if (unit) {
                    unitOk = true;
                    y.unit = Unit.div(y.unit, x.unit, rTmp);
                    if (!y.hasUnit()) {
                        y.r.mul(rTmp);
                    } else {
                        y.unit = Unit.unitError;
                    }
                }
                y.r.atan2(x.r);
                fromRAD(y.r);
                break;

            case HYPOT:
                if (unit) {
                    unitOk = true;
                    y.unit = Unit.add(y.unit, x.unit, rTmp, null);
                    y.r.mul(rTmp);
                }
                y.r.hypot(x.r);
                break;

            case AND:   y.r.and(x.r);                break;
            case OR:    y.r.or(x.r);                 break;
            case XOR:   y.r.xor(x.r);                break;
            case BIC:   y.r.bic(x.r);                break;
            case YUPX:  x.r.round(); y.r.scalbn(x.r.toInteger()); break;
            case YDNX:  x.r.round(); y.r.scalbn(-x.r.toInteger());break;
            case MOD:   y.r.mod(x.r);                break;
            case DIVF:  y.r.divf(x.r);               break;

            case FINANCE_MULINT:
                y.r.mul(Real.PERCENT);
                y.r.add(Real.ONE);
                y.r.pow(x.r);
                y.r.sub(Real.ONE);
                y.r.mul(Real.HUNDRED);
                break;

            case FINANCE_DIVINT:
                y.r.mul(Real.PERCENT);
                y.r.add(Real.ONE);
                y.r.nroot(x.r);
                y.r.sub(Real.ONE);
                y.r.mul(Real.HUNDRED);
                break;

            case DHMS_PLUS:
                x.r.fromDHMS();
                y.r.fromDHMS();
                y.r.add(x.r);
                y.r.toDHMS();
                break;

            case TO_CPLX:
                if (matrix) {
                    if (x.isMatrix() && y.isMatrix()) {
                        matrixOk = true;
                        y.M = Matrix.toComplex(x.M, y.M);
                    }
                } else {
                    complexOk = true;
                    complex = true;
                    if (unit) {
                        unitOk = true;
                        y.unit = Unit.add(y.unit, x.unit, rTmp, null);
                        y.r.mul(rTmp);
                    }
                    y.i.assign(y.r);
                    y.r.assign(x.r);
                }
                break;

            case MIN:
                unitOk = true;
                compare(rTmp, x, y, false, cmd);
                if (rTmp.isNan())
                    y.makeNan();
                else if (rTmp.isInfinity() || rTmp.isNegative())
                    y.copy(x);
                break;

            case MAX:
                unitOk = true;
                compare(rTmp, x, y, false, cmd);
                if (rTmp.isNan())
                    y.makeNan();
                else if (rTmp.isInfinity() || !rTmp.isNegative())
                    y.copy(x);
                break;

            case MATRIX_NEW:
                if (complex || matrix || !x.r.isFinite() || !y.r.isFinite()) {
                    y.makeNan();
                } else {
                    x.r.round();
                    y.r.round();
                    int cols = x.r.toInteger();
                    int rows = y.r.toInteger();
                    matrix = true;
                    if (rows > 0 && rows < 65536 && cols > 0 && cols < 65536) {
                        matrixOk = true;
                        y.M = new Matrix(rows,cols);
                    } else {
                        setMessage("Matrix", "Illegal matrix size");
                    }
                }
                break;

            case MATRIX_CONCAT:
                if (matrix) {
                    matrixOk = true;
                    if (x.isMatrix() && y.isMatrix()) {
                        y.M = Matrix.concat(y.M,x.M);
                    } else if (x.isMatrix()) {
                        y.M = Matrix.concat(y.r,y.i,x.M);
                    } else {
                        y.M = Matrix.concat(y.M,x.r,x.i);
                    }
                } else {
                    matrix = true;
                    matrixOk = true;
                    y.M = Matrix.concat(y.r,y.i,x.r,x.i);
                }
                break;

            case MATRIX_STACK:
                if (matrix) {
                    matrixOk = true;
                    if (x.isMatrix() && y.isMatrix()) {
                        y.M = Matrix.stack(y.M,x.M);
                    } else if (x.isMatrix()) {
                        y.M = Matrix.stack(y.r,y.i,x.M);
                    } else {
                        y.M = Matrix.stack(y.M,x.r,x.i);
                    }
                } else {
                    matrix = true;
                    matrixOk = true;
                    y.M = Matrix.stack(y.r,y.i,x.r,x.i);
                }
                break;

            case MATRIX_AIJ:
                // Not matrixOk, arguments must be normal numbers
                Matrix M = getCurrentMatrix();
                if (M == null || complex || matrix || !x.r.isFinite() || !y.r.isFinite()) {
                    y.makeNan();
                } else {
                    x.r.round();
                    y.r.round();
                    int col = x.r.toInteger()-1;
                    int row = y.r.toInteger()-1;
                    M.getElement(row,col,y.r,y.i);
                }
                break;

            case GCD:
                if (x.isAbnormalOrComplex() || x.r.isNegative() || x.r.exponent > 0x4000003e ||
                    y.isAbnormalOrComplex() || y.r.isNegative() || y.r.exponent > 0x4000003e)
                {
                    setMessage("GCD","Arguments must not be abnormal, "+
                        "complex, negative or greater than 2^63-1.");
                    y.makeNan();
                } else {
                    x.r.round();
                    y.r.round();
                    long a = x.r.toLong();
                    long b = y.r.toLong();
                    rTmp.assign(Utils.gcd(a, b));
                    y.set(rTmp);
                }
                break;
        }

        if (cmd != CLEAR)
            y.postProcess(complex, complexOk, matrix, matrixOk, unit, unitOk, x.unit);

        degrees = Complex.degrees;
        grad = Complex.grad;

        x.makeEmpty();
        rollDown(0, true);
    }

    void statAB(Real a, Real b, Real SUMx, Real SUMx2,
                Real SUMy, Real SUMxy)
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

    private void unary(int cmd) {
        ComplexMatrixElement x = stack[0];
        saveUndo(UNDO_UNARY, x, null, null);

        if (x.isComplex() || x.isMatrix()) {
            // Complex/Matrix undefined for these operations
            x.makeNan();
            cmd = -1;
        }
        if (x.hasUnit()) {
            // Unit calculations undefined for these operations
            x.unit = Unit.undefinedUnaryOperation(x.unit);
        }

        switch (cmd)
        {
            case FACT:    x.r.fact();  break;
            case GAMMA:   x.r.gamma(); break;
            case ERFC:    x.r.erfc();  break;
            case INVERFC: x.r.inverfc(); break;
            case NOT:     x.r.xor(Real.ONE_N); break;
            case TO_DEG:  x.r.div(Real.PI); x.r.mul(180); break;
            case TO_RAD:  x.r.div(180); x.r.mul(Real.PI); break;
            case TO_DHMS: x.r.toDHMS(); break;
            case TO_H:    x.r.fromDHMS(); break;

            case PHI:
                x.r.mul(Real.SQRT1_2);
                x.r.neg();
                x.r.erfc();
                x.r.scalbn(-1);
                break;
            case INVPHI:
                x.r.scalbn(1);
                x.r.inverfc();
                if (!x.r.isZero())
                    x.r.neg();
                x.r.mul(Real.SQRT2);
                break;

            case SGN:
                rTmp.assign(Real.ONE);
                rTmp.copysign(x.r);
                x.r.assign(rTmp);
                break;

            case DHMS_TO_UNIX:
            case UNIX_TO_DHMS:
                if (cmd == DHMS_TO_UNIX) {
                    x.r.fromDHMS();
                    x.r.sub(17268672);
                    x.r.mul(3600);
                } else {
                    x.r.div(3600);
                    x.r.add(17268672);
                    x.r.toDHMS();
                }
                break;

            case DHMS_TO_JD:
            case JD_TO_DHMS:
                rTmp.assign(3442119);
                rTmp.scalbn(-1);
                if (cmd == DHMS_TO_JD) {
                    x.r.fromDHMS();
                    x.r.div(24);
                    x.r.add(rTmp);
                } else {
                    x.r.sub(rTmp);
                    x.r.mul(24);
                    x.r.toDHMS();
                }
                break;

            case DHMS_TO_MJD:
            case MJD_TO_DHMS:
                if (cmd == DHMS_TO_MJD) {
                    x.r.fromDHMS();
                    x.r.div(24);
                    x.r.sub(678941);
                } else {
                    x.r.add(678941);
                    x.r.mul(24);
                    x.r.toDHMS();
                }
                break;

            case CONV_C_F:
            case CONV_F_C:
                rTmp.assign(0, 0x40000000, 0x7333333333333333L); // 1.8
                if (cmd == CONV_C_F) {
                    x.r.mul(rTmp);
                    x.r.add(32);
                } else {
                    x.r.sub(32);
                    x.r.div(rTmp);
                }
                break;

            case LIN_YEST:
                allocStat();
                statAB(rTmp2,rTmp3,SUMx,SUMx2,SUMy,SUMxy);
                x.r.mul(rTmp2);
                x.r.add(rTmp3);
                break;

            case LIN_XEST:
                allocStat();
                statAB(rTmp2,rTmp3,SUMx,SUMx2,SUMy,SUMxy);
                x.r.sub(rTmp3);
                x.r.div(rTmp2);
                break;

            case LOG_YEST:
                allocStat();
                statAB(rTmp2,rTmp3,SUMlnx,SUMln2x,SUMy,SUMylnx);
                x.r.ln();
                x.r.mul(rTmp2);
                x.r.add(rTmp3);
                break;

            case LOG_XEST:
                allocStat();
                statAB(rTmp2,rTmp3,SUMlnx,SUMln2x,SUMy,SUMylnx);
                x.r.sub(rTmp3);
                x.r.div(rTmp2);
                x.r.exp();
                break;

            case EXP_YEST:
                allocStat();
                statAB(rTmp2,rTmp3,SUMx,SUMx2,SUMlny,SUMxlny);
                x.r.mul(rTmp2);
                x.r.add(rTmp3);
                x.r.exp();
                break;

            case EXP_XEST:
                allocStat();
                statAB(rTmp2,rTmp3,SUMx,SUMx2,SUMlny,SUMxlny);
                x.r.ln();
                x.r.sub(rTmp3);
                x.r.div(rTmp2);
                break;

            case POW_YEST:
                allocStat();
                statAB(rTmp2,rTmp3,SUMlnx,SUMln2x,SUMlny,SUMlnxlny);
                x.r.ln();
                x.r.mul(rTmp2);
                x.r.add(rTmp3);
                x.r.exp();
                break;

            case POW_XEST:
                allocStat();
                statAB(rTmp2,rTmp3,SUMlnx,SUMln2x,SUMlny,SUMlnxlny);
                x.r.ln();
                x.r.sub(rTmp3);
                x.r.div(rTmp2);
                x.r.exp();
                break;

            case TO_PRIME:
                if (x.isAbnormalOrComplex() || x.r.exponent > 0x4000003e)
                {
                    setMessage("To prime","Argument must not be abnormal, "+
                        "complex, negative or greater than 2^63-1.");
                    x.makeNan();
                } else {
                    x.r.round();
                    long a = x.r.toLong();
                    a = Utils.nextPrime(a);
                    if (a < 0) // Overflow
                        rTmp.makeNan();
                    else
                        rTmp.assign(a);
                    x.set(rTmp);
                }
                break;
        }

        x.clearStrings();
        repaint(1);
    }

    private void unaryComplexMatrix(int cmd, int param) {
        ComplexMatrixElement x = stack[0];
        boolean complex  = x.isComplex();
        boolean matrix   = x.isMatrix();
        boolean matrixOk = false;
        boolean unit     = x.hasUnit();
        boolean unitOk   = false;

        Complex.degrees = degrees;
        Complex.grad = grad;

        saveUndo(UNDO_UNARY, x, null, null);

        switch (cmd)
        {
            case NEG:
                unitOk = true;
                if (matrix) {
                    matrixOk = true;
                    x.M = Matrix.neg(x.M);
                } else {
                    x.r.neg();
                    if (complex) x.i.neg();
                }
                break;

            case SQR:
                if (matrix) {
                    matrixOk = true;
                    x.M = Matrix.mul(x.M,x.M);
                } else {
                    if (unit) {
                        unitOk = true;
                        x.unit = Unit.pow(x.unit, 2);
                    }
                    if (complex) {
                        Complex.sqr(x.r,x.i);
                    } else {
                        x.r.sqr();
                    }
                }
                break;

            case RECIP:
                if (matrix) {
                    matrixOk = true;
                    x.M = Matrix.invert(x.M);
                } else {
                    if (unit) {
                        unitOk = true;
                        x.unit = Unit.recip(x.unit);
                    }
                    if (complex) {
                        Complex.recip(x.r,x.i);
                    } else {
                        x.r.recip();
                    }
                }
                break;

            case ABS:
                unitOk = true;
                if (matrix) {
                    x.M.norm_F(x.r,x.i);
                    matrix = false;
                    complex = x.isComplex();
                } else if (complex) {
                    x.r.hypot(x.i);
                    x.i.makeZero();
                } else {
                    x.r.abs();
                }
                break;

            case TRANSP:
            case TRANSP_CONJ:
                unitOk = true;
                if (matrix) {
                    matrixOk = true;
                    x.M = Matrix.transp(x.M,cmd==TRANSP_CONJ);
                } else if (complex && cmd==TRANSP_CONJ) {
                    // It is like a 1x1 matrix, after all
                    x.i.neg();
                } // else do nothing
                break;

            case DETERM:
                unitOk = true;
                if (matrix) {
                    x.M.det(x.r,x.i);
                    matrix = false;
                    complex = x.isComplex();
                } // else do nothing
                break;

            case TRACE:
                unitOk = true;
                if (matrix) {
                    x.M.trace(x.r,x.i);
                    matrix = false;
                    complex = x.isComplex();
                } // else do nothing
                break;

            case PERCENT:
                complex |= stack[1].isComplex();
                x.r.mul(Real.PERCENT);
                if (complex)
                    x.i.mul(Real.PERCENT);
                if (stack[1].isMatrix() && !matrix) {
                    matrix = matrixOk = true;
                    x.M = Matrix.mul(stack[1].M, x.r, x.i);
                } else if (complex) {
                    Complex.mul(x.r,x.i,stack[1].r/*y*/,stack[1].i/*yi*/);
                } else {
                    x.r.mul(stack[1].r/*y*/);
                }
                if (stack[1].hasUnit() && !unit) {
                    unit = unitOk = true;
                    x.unit = stack[1].unit;
                }
                break;

            case SQRT:
                if (unit) {
                    unitOk = true;
                    x.unit = Unit.nroot(x.unit, 2, rTmp);
                    x.r.mul(rTmp);
                    if (complex) x.i.mul(rTmp);
                }
                if (complex) {
                    Complex.sqrt(x.r,x.i);
                } else if (x.r.isNegative() && !x.r.isZero()) {
                    complex = true;
                    x.i.assign(x.r);
                    x.i.neg();
                    x.i.sqrt();
                    x.r.makeZero();
                } else {
                    x.r.sqrt();
                }
                break;

            case CPLX_ARG:
                if (complex) {
                    Complex.degrees = false;
                    Complex.grad = false;
                    x.i.atan2(x.r);
                    x.r.assign(x.i);
                    x.i.makeZero();
                    fromRAD(x.r);
                } else {
                    x.r.makeZero();
                }
                break;

            case CPLX_CONJ:
                unitOk = true;
                if (matrix) {
                    matrixOk = true;
                    x.M = Matrix.conj(x.M);
                } else if (complex) {
                    x.i.neg();
                }
                break;

            case COS:
                if (complex) {
                    x.r.swap(x.i);
                    Complex.cosh(x.r,x.i);
                    x.i.neg();
                } else {
                    toRAD(x.r);
                    x.r.cos();
                }
                break;

            case COSH:
                if (complex) {
                    Complex.cosh(x.r,x.i);
                } else {
                    x.r.cosh();
                }
                break;

            case SIN:
                if (complex) {
                    Complex.sinh(x.i,x.r);
                } else {
                    toRAD(x.r);
                    x.r.sin();
                }
                break;

            case SINH:
                if (complex) {
                    Complex.sinh(x.r,x.i);
                } else {
                    x.r.sinh();
                }
                break;

            case TAN:
                if (complex) {
                    x.i.neg();
                    Complex.tanh(x.i,x.r);
                    x.i.neg();
                } else {
                    toRAD(x.r);
                    x.r.tan();
                }
                break;

            case TANH:
                if (complex) {
                    Complex.tanh(x.r,x.i);
                } else {
                    x.r.tanh();
                }
                break;

            case ASIN:
                if (x.r.greaterThan(Real.ONE) || x.r.lessThan(Real.ONE_N))
                    complex = true;
                if (complex) {
                    Complex.asinh(x.i,x.r);
                } else {
                    x.r.asin();
                    fromRAD(x.r);
                }
                break;

            case ASINH:
                if (complex) {
                    Complex.asinh(x.r,x.i);
                } else {
                    x.r.asinh();
                }
                break;

            case ACOS:
                if (x.r.greaterThan(Real.ONE) || x.r.lessThan(Real.ONE_N))
                    complex = true;
                if (complex) {
                    Complex.asinh(x.i,x.r);
                    x.r.neg();
                    x.i.neg();
                    x.r.add(Real.PI_2);
                } else {
                    x.r.acos();
                    fromRAD(x.r);
                }
                break;

            case ACOSH:
                if (x.r.lessThan(Real.ONE))
                    complex = true;
                if (complex) {
                    Complex.acosh(x.r,x.i);
                } else {
                    x.r.acosh();
                }
                break;

            case ATAN:
                if (complex) {
                    x.i.neg();
                    Complex.atanh(x.i,x.r);
                    x.i.neg();
                } else {
                    x.r.atan();
                    fromRAD(x.r);
                }
                break;

            case ATANH:
                if (x.r.greaterThan(Real.ONE) || x.r.lessThan(Real.ONE_N))
                    complex = true;
                if (complex) {
                    Complex.atanh(x.r,x.i);
                } else {
                    x.r.atanh();
                }
                break;

            case EXP:
                if (complex) {
                    Complex.exp(x.r,x.i);
                } else {
                    x.r.exp();
                }
                break;

            case EXP2:
                if (complex) {
                    x.r.mul(Real.LN2);
                    x.i.mul(Real.LN2);
                    Complex.exp(x.r,x.i);
                } else {
                    x.r.exp2();
                }
                break;

            case EXP10:
                if (complex) {
                    x.r.mul(Real.LN10);
                    x.i.mul(Real.LN10);
                    Complex.exp(x.r,x.i);
                } else {
                    x.r.exp10();
                }
                break;

            case LN:
                if (x.r.isNegative() && !x.r.isZero())
                    complex = true;
                if (complex) {
                    Complex.ln(x.r,x.i);
                } else {
                    x.r.ln();
                }
                break;

            case LOG2:
                if (x.r.isNegative() && !x.r.isZero())
                    complex = true;
                if (complex) {
                    Complex.ln(x.r,x.i);
                    x.r.mul(Real.LOG2E);
                    x.i.mul(Real.LOG2E);
                } else {
                    x.r.log2();
                }
                break;

            case LOG10:
                if (x.r.isNegative() && !x.r.isZero())
                    complex = true;
                if (complex) {
                    Complex.ln(x.r,x.i);
                    x.r.mul(Real.LOG10E);
                    x.i.mul(Real.LOG10E);
                } else {
                    x.r.log10();
                }
                break;

            case ROUND:
                unitOk = true;
                x.r.round();
                if (complex) x.i.round();
                break;

            case CEIL:
                unitOk = true;
                x.r.ceil();
                if (complex) x.i.ceil();
                break;

            case FLOOR:
                unitOk = true;
                x.r.floor();
                if (complex) x.i.floor();
                break;

            case TRUNC:
                unitOk = true;
                x.r.trunc();
                if (complex) x.i.trunc();
                break;

            case FRAC:
                unitOk = true;
                x.r.frac();
                if (complex) x.i.frac();
                break;

            case XCHGMEM:
                matrixOk = unitOk = true;
                allocMem();
                stack[0].xchg(mem[param], false);
                if (monitorMode == MONITOR_MEM) {
                    repaintAll();
                }
                break;

            case MATRIX_ROW: {
                Matrix M = getCurrentMatrix();
                if (M == null || complex || matrix || !x.r.isFinite()) {
                    matrixOk = false;
                    x.makeNan();
                } else {
                    matrix = matrixOk = true;
                    x.r.round();
                    int row = x.r.toInteger()-1;
                    x.M = M.subMatrix(row, 0, 1, M.cols);
                }
                break;
            }
            case MATRIX_COL: {
                Matrix M = getCurrentMatrix();
                if (M == null || complex || matrix || !x.r.isFinite()) {
                    matrixOk = false;
                    x.r.makeNan();
                } else {
                    matrix = matrixOk = true;
                    x.r.round();
                    int col = x.r.toInteger()-1;
                    x.M = M.subMatrix(0, col, M.rows, 1);
                }
                break;
            }
            case UNIT_SET:
                if (!matrix) {
                    unit = unitOk = true;
                    int unitType = param & 0xffff;
                    int unitNo = param >>> 16;
                    x.unit = Unit.mul(x.unit, uTmp.setUnit(unitType,unitNo).pack(), rTmp);
                    x.r.mul(rTmp);
                    if (complex) x.i.mul(rTmp);
                }
                break;
            case UNIT_SET_INV:
                if (!matrix) {
                    unit = unitOk = true;
                    int unitType = param & 0xffff;
                    int unitNo = param >>> 16;
                    Unit u = uTmp.setUnit(unitType, unitNo);
                    u.recip();
                    x.unit = Unit.mul(x.unit, u.pack(), rTmp);
                    x.r.mul(rTmp);
                    if (complex) x.i.mul(rTmp);
                }
                break;
            case UNIT_CONVERT:
                if (!matrix) {
                    unit = unitOk = true;
                    int unitType = param & 0xffff;
                    int unitNo = param >>> 16;
                    x.unit = Unit.convertTo(x.unit, uTmp.setUnit(unitType,unitNo).pack(), rTmp, rTmp2);
                    x.r.mul(rTmp);
                    x.r.add(rTmp2);
                    if (complex) x.i.mul(rTmp);
                }
                break;
            case UNIT_CLEAR:
                if (!matrix) {
                    unit = unitOk = true;
                    x.unit = 0;
                }
                break;
        }

        x.postProcess(complex, true, matrix, matrixOk, unit, unitOk, 0);

        degrees = Complex.degrees;
        grad = Complex.grad;

        repaint(1);
    }

    private void xyOp(int cmd) {
        ComplexMatrixElement x = stack[0];
        ComplexMatrixElement y = stack[1];
        saveUndo(UNDO_XY, x, y, null);
        boolean complex = x.isComplex() || y.isComplex();
        boolean matrix = false;
        boolean unit = x.hasUnit() || y.hasUnit();
        boolean unitOk = false;

        switch (cmd)
        {
            case RP:
                if (unit) {
                    unitOk = true;
                    x.unit = Unit.add(y.unit, x.unit, rTmp, null);
                    y.unit = 0;
                    y.r.mul(rTmp);
                }
                rTmp.assign(y.r);
                rTmp.atan2(x.r);
                x.r.hypot(y.r);
                y.r.assign(rTmp);
                fromRAD(y.r);
                x.i.makeZero();
                y.i.makeZero();
                break;

            case PR:
                if (unit) {
                    unitOk = !y.hasUnit();
                    y.unit = x.unit;
                }
                toRAD(y.r);
                rTmp.assign(y.r);
                rTmp.cos();
                y.r.sin();
                y.r.mul(x.r);
                x.r.mul(rTmp);
                x.i.makeZero();
                y.i.makeZero();
                break;

            case MATRIX_SPLIT:
                rTmp.assign(x.r);
                rTmp.round();
                int n = rTmp.toInteger();
                if (x.r.isFinite() && x.i.isZero() &&
                    y.isMatrix() && ((n>=0 && n<=y.M.rows) || (n<0 && -n<=y.M.cols))) {
                    matrix = true;
                    if (n >= 0) {
                        x.M = y.M.subMatrix(n,0,y.M.rows-n,y.M.cols);
                        y.M = y.M.subMatrix(0,0,n,y.M.cols);
                    } else {
                        n = -n;
                        x.M = y.M.subMatrix(0,n,y.M.rows,y.M.cols-n);
                        y.M = y.M.subMatrix(0,0,y.M.rows,n);
                    }
                } // else do nothing
                break;
        }
        x.postProcess(complex, false, matrix, true, unit, unitOk, y.unit);
        y.postProcess(complex, false, matrix, true, unit, unitOk, x.unit);
        repaint(2);
    }

    void push(Real r, Real i, long unit) {
        rollUp(0, true);
        saveUndo(UNDO_PUSH, null, stack[0], null);
        stack[0].set(r, i, unit);
    }

    void push(Real r) {
        push(r, null, 0);
    }

    void push(Real r, Real i) {
        push(r, i, 0);
    }

    private void push(int e, long m) {
        push(e, m, 0);
    }

    private void push(int e, long m, long unit) {
        rTmp.assign(0,e,m);
        push(rTmp, null, unit);
    }
    
    void push(Matrix M) {
        rollUp(0, true);
        saveUndo(UNDO_PUSH, null, stack[0], null);
        stack[0].set(M);
    }

    private void push(Element e) {
        if (e.isMatrix())
            push(e.getMatrix());
        else
            push(e.getReal(), e.getImag(), e.getUnit());
    }

    private void skipIf(boolean skip) {
        if (skip && currentStep < programs.numProgSteps())
            currentStep++;
    }
    
    private void cond(int cmd) {
        if (!progRunning) {
            return;
        }

        boolean compareToZero = (cmd==IF_EQUAL_Z || cmd==IF_NEQUAL_Z || cmd==IF_LESS_Z || cmd==IF_LEQUAL_Z || cmd==IF_GREATER_Z);
        boolean testEquality = (cmd==IF_EQUAL || cmd==IF_NEQUAL || cmd==IF_EQUAL_Z || cmd==IF_NEQUAL_Z);
        ComplexMatrixElement x = stack[0];
        ComplexMatrixElement y = compareToZero ? null : stack[1];

        compare(rTmp, x, y, testEquality, cmd);
        if (!rTmp.isFinite()) {
            skipIf(true);
            return;
        }
        switch (cmd) {
            case IF_EQUAL:
            case IF_EQUAL_Z:
                skipIf(!rTmp.isZero());
                break;
            case IF_NEQUAL:
            case IF_NEQUAL_Z:
                skipIf(rTmp.isZero());
                break;
            case IF_LESS:
            case IF_LESS_Z:
                skipIf(!rTmp.isNegative() || rTmp.isZero());
                break;
            case IF_LEQUAL:
            case IF_LEQUAL_Z:
                skipIf(!rTmp.isNegative() && !rTmp.isZero());
                break;
            case IF_GREATER:
            case IF_GREATER_Z:
                skipIf(rTmp.isNegative() || rTmp.isZero());
                break;
        }
    }

    private void flow(int cmd, int param) {
        if (!progRunning) {
            return;
        }

        switch (cmd) {
            case GSB:
                if (returnStackDepth<returnStack.length) {
                    returnStack[returnStackDepth++] = (short)currentStep;
                } else {
                    setMessage("GSB", "Return stack overflow");
                }
                // fall-through to next case...
            case GTO:
                for (int i=currentStep; i<programs.numProgSteps(); i++) {
                    if (programs.isLabel(i, param)) {
                        currentStep = i;
                        return;
                    }
                }
                for (int i=currentStep-2; i>=0; i--) {
                    if (programs.isLabel(i, param)) {
                        currentStep = i;
                        // Consider yield when jumping backwards
                        considerYield();
                        return;
                    }
                }
                setMessage(cmd == GTO ? "GTO" : "GSB", "Nonexistent label");
                break;
            case RTN:
                if (returnStackDepth > 0) {
                    short newStep = returnStack[--returnStackDepth];
                    // Consider yield when jumping backwards
                    if (newStep < currentStep-1)
                        considerYield();
                    currentStep = newStep;
                } else {
                    currentStep = 0;
                    stopFlag = true;
                }
                break;
            case STOP:
                stopFlag = true;
                break;
            case DSE:
            case ISG:
                allocMem();
                rTmp.assign(mem[param].r);
                rTmp.mul(100000);
                rTmp.round();
                long n = rTmp.toLong();
                boolean neg = n<0;
                n = Math.abs(n);
                int step = (int)(n%100);
                n /= 100;
                int limit = (int)(n%1000);
                n /= 1000;
                if (neg) {
                    n = -n;
                }
                if (cmd == DSE) {
                    n -= step == 0 ? 1 : step;
                    skipIf(n<=limit);
                } else { // ISG
                    n += step == 0 ? 1 : step;
                    skipIf(n>limit);
                }
                neg = n<0;
                n = Math.abs(n);
                n = (n*1000 + limit)*100 + step;
                if (neg) {
                    n = -n;
                }
                rTmp.assign(n);
                rTmp.div(100000);
                mem[param].set(rTmp);
                break;
        }
    }
    
    private void trinary(int cmd) {
        ComplexMatrixElement x = stack[0];
        ComplexMatrixElement y = stack[1];
        ComplexMatrixElement z = stack[2];
        boolean complex = x.isComplex() || y.isComplex() || z.isComplex();
        boolean matrix = x.isMatrix() || y.isMatrix() || z.isMatrix();

        saveUndo(UNDO_TRINARY, x, y, z);

        switch (cmd)
        {
            case SELECT:
                // Calculate x*y + (1-x)*z
                if (x.isZero()) {
                    complex = z.isComplex();
                    matrix = z.isMatrix();
                    if (x.hasUnit())
                        z.unit = Unit.unitError;
                } else if (x.r.equalTo(Real.ONE) && !x.isComplex()) {
                    z.copy(y);
                    complex = y.isComplex();
                    matrix = y.isMatrix();
                    if (x.hasUnit())
                        z.unit = Unit.unitError;
                } else {
                    if (x.hasUnit() || y.unit != z.unit) {
                        z.unit = Unit.unitError;
                    }
                    if (matrix) {
                        if (x.isMatrix()) {
                            if (x.M.cols != x.M.rows || (y.isMatrix() != z.isMatrix())) {
                                matrix = false;
                                z.r.makeNan();
                            } else {
                                // Weird, but perhaps sometimes useful
                                Matrix Tmp = new Matrix(x.M.cols);
                                for (int i=0; i<x.M.cols; i++)
                                    Tmp.setElement(i,i,Real.ONE,null);
                                Tmp = Matrix.sub(Tmp,x.M);
                                if (!y.isMatrix() /*&& !z.isMatrix() */) {
                                    // X*y + (I-X)*z
                                    z.M = Matrix.mul(Tmp,z.r,z.i);
                                    y.M = Matrix.mul(x.M,y.r,y.i);
                                } else {
                                    // X*Y + (I-X)*Z
                                    z.M = Matrix.mul(Tmp,z.M);
                                    y.M = Matrix.mul(x.M,y.M);
                                }
                                z.M = Matrix.add(y.M,z.M);
                            }
                        } else {
                            if (!y.isMatrix() || !z.isMatrix()) {
                                matrix = false;
                                z.r.makeNan();
                            } else {
                                // x*Y + (1-x)*Z
                                rTmp3.assign(Real.ONE);
                                rTmp3.sub(x.r);
                                rTmp4.assign(x.i);
                                rTmp4.neg();
                                z.M = Matrix.mul(z.M,rTmp3,rTmp4);
                                y.M = Matrix.mul(y.M,x.r,x.i);
                                z.M = Matrix.add(y.M,z.M);
                            }
                        }
                    } else {
                        rTmp3.assign(Real.ONE);
                        rTmp3.sub(x.r);
                        if (complex) {
                            rTmp4.assign(x.i);
                            rTmp4.neg();
                            Complex.mul(z.r,z.i,rTmp3,rTmp4);
                            Complex.mul(y.r,y.i,x.r,x.i);
                            z.r.add(y.r);
                            z.i.add(y.i);
                        } else {
                            z.r.mul(rTmp3);
                            y.r.mul(x.r);
                            z.r.add(y.r);
                        }
                    }
                }
                break;
        }
        z.postProcess(complex, true, matrix, true, true, true, 0);

        x.makeEmpty();
        y.makeEmpty();
        rollDown(0, true);
        rollDown(0, true);
    }

    private void sum(int cmd) {
        allocStat();
        ComplexMatrixElement x = stack[0];
        ComplexMatrixElement y = stack[1];
        int index;

        switch (cmd)
        {
            case SUMPL:
                index = (statLogStart+statLogSize)%STATLOG_SIZE;
                statLog[index*2] = x.r.toFloatBits();
                statLog[index*2+1] = y.r.toFloatBits();
                if (statLogSize < STATLOG_SIZE)
                    statLogSize++;
                else
                    statLogStart = (statLogStart+1)%STATLOG_SIZE;

                SUM1.add(Real.ONE);
                SUMx.add(x.r);
                rTmp.assign(x.r);
                rTmp.sqr();
                SUMx2.add(rTmp);
                SUMy.add(y.r);
                rTmp.assign(y.r);
                rTmp.sqr();
                SUMy2.add(rTmp);
                rTmp.assign(x.r);
                rTmp.mul(y.r);
                SUMxy.add(rTmp);
                rTmp2.assign(x.r);
                rTmp2.ln();
                SUMlnx.add(rTmp2);
                rTmp.assign(rTmp2);
                rTmp.sqr();
                SUMln2x.add(rTmp);
                rTmp3.assign(y.r);
                rTmp3.ln();
                SUMlny.add(rTmp3);
                rTmp.assign(rTmp3);
                rTmp.sqr();
                SUMln2y.add(rTmp);
                rTmp.assign(x.r);
                rTmp.mul(rTmp3);
                SUMxlny.add(rTmp);
                rTmp.assign(y.r);
                rTmp.mul(rTmp2);
                SUMylnx.add(rTmp);
                rTmp2.mul(rTmp3);
                SUMlnxlny.add(rTmp2);
                break;

            case SUMMI:
                // Statistics log: search for point and remove if found
                int xf = x.r.toFloatBits();
                int yf = y.r.toFloatBits();
                for (int i=statLogSize-1; i>=0; i--) {
                    index = (statLogStart+i)%STATLOG_SIZE;
                    if (statLog[index*2]==xf && statLog[index*2+1]==yf) {
                        statLogSize--;
                        for (; i<statLogSize; i++) {
                            int index2 = (statLogStart+i+1)%STATLOG_SIZE;
                            statLog[index*2] = statLog[index2*2];
                            statLog[index*2+1] = statLog[index2*2+1];
                            index = index2;
                        }
                        break;
                    }
                }

                SUM1.sub(Real.ONE);
                SUMx.sub(x.r);
                rTmp.assign(x.r);
                rTmp.sqr();
                SUMx2.sub(rTmp);
                SUMy.sub(y.r);
                rTmp.assign(y.r);
                rTmp.sqr();
                SUMy2.sub(rTmp);
                rTmp.assign(x.r);
                rTmp.mul(y.r);
                SUMxy.sub(rTmp);
                rTmp2.assign(x.r);
                rTmp2.ln();
                SUMlnx.sub(rTmp2);
                rTmp.assign(rTmp2);
                rTmp.sqr();
                SUMln2x.sub(rTmp);
                rTmp3.assign(y.r);
                rTmp3.ln();
                SUMlny.sub(rTmp3);
                rTmp.assign(rTmp3);
                rTmp.sqr();
                SUMln2y.sub(rTmp);
                rTmp.assign(x.r);
                rTmp.mul(rTmp3);
                SUMxlny.sub(rTmp);
                rTmp.assign(y.r);
                rTmp.mul(rTmp2);
                SUMylnx.sub(rTmp);
                rTmp2.mul(rTmp3);
                SUMlnxlny.sub(rTmp2);
                break;
        }
        if (monitorMode == MONITOR_STAT) {
            clearMonitorStrings();
            repaintAll();
        }
        push(SUM1);
    }

    private void stat2(int cmd) {
        rollUp(0, true);
        rollUp(0, true);
        ComplexMatrixElement x = stack[0];
        ComplexMatrixElement y = stack[1];
        saveUndo(UNDO_PUSH2, null, x, y);
        x.clear();
        y.clear();

        switch (cmd)
        {
            case AVG:
                allocStat();
                // x_avg = SUMx/n
                x.r.assign(SUMx);
                x.r.div(SUM1);
                // y_avg = SUMy/n
                y.r.assign(SUMy);
                y.r.div(SUM1);
                break;

            case STDEV:
            case PSTDEV:
                allocStat();
                // s_x = sqrt((SUMx2-sqr(SUMx)/n)/(n-1))
                // S_x = sqrt((SUMx2-sqr(SUMx)/n)/n)
                x.r.assign(SUMx);
                x.r.sqr();
                x.r.div(SUM1);
                x.r.neg();
                x.r.add(SUMx2);
                rTmp.assign(SUM1);
                if (cmd == STDEV)
                    rTmp.sub(Real.ONE);
                x.r.div(rTmp);
                x.r.sqrt();
                // s_y = sqrt((SUMy2-sqr(SUMy)/n)/(n-1))
                // S_y = sqrt((SUMy2-sqr(SUMy)/n)/n)
                y.r.assign(SUMy);
                y.r.sqr();
                y.r.div(SUM1);
                y.r.neg();
                y.r.add(SUMy2);
                y.r.div(rTmp);
                y.r.sqrt();
                break;

            case LIN_AB:
                allocStat();
                statAB(x.r,y.r,SUMx,SUMx2,SUMy,SUMxy);
                break;

            case LOG_AB:
                allocStat();
                statAB(x.r,y.r,SUMlnx,SUMln2x,SUMy,SUMylnx);
                break;

            case EXP_AB:
                allocStat();
                statAB(x.r,y.r,SUMx,SUMx2,SUMlny,SUMxlny);
                y.r.exp();
                break;

            case POW_AB:
                allocStat();
                statAB(x.r,y.r,SUMlnx,SUMln2x,SUMlny,SUMlnxlny);
                y.r.exp();
                break;

            case MATRIX_SIZE:
                Matrix M = getCurrentMatrix();
                if (M == null) {
                    x.r.makeNan();
                    y.r.makeNan();
                } else {
                    x.r.assign(M.cols);
                    y.r.assign(M.rows);
                }
                break;
        }
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
        rollUp(0, true);
        ComplexMatrixElement x = stack[0];
        saveUndo(UNDO_PUSH, null, x, null);
        x.clear();

        switch (cmd)
        {
            case AVGXW:
                x.r.assign(SUMxy);
                x.r.div(SUMy);
                break;
            case LIN_R:
                statR(x.r,SUMx,SUMx2,SUMy,SUMy2,SUMxy);
                break;
            case LOG_R:
                statR(x.r,SUMlnx,SUMln2x,SUMy,SUMy2,SUMylnx);
                break;
            case EXP_R:
                statR(x.r,SUMx,SUMx2,SUMlny,SUMln2y,SUMxlny);
                break;
            case POW_R:
                statR(x.r,SUMlnx,SUMln2x,SUMlny,SUMln2y,SUMlnxlny);
                break;
        }
    }

    private void financeSolve(int which) {
        allocFinance();

        switch (which)
        {
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
                    // pmt= -(((1+ir)^np*pv+fv)*ir)/(((1+ir)^np-1)*(1+ir*bgn));
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
                // Educated guess at start value= 2*(np*pmt+pv+fv)/(np*(fv-pv))
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
                        //f(ir)=fv+(1+ir)^np*pv+((1+ir)^np-1)*pmt*(1+ir*bgn)/ir
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

    public static int getMatrixSize(int param) {
        int rows = 0, cols = 0;
        switch (param) {
            case 0: rows = 2; cols = 2; break;
            case 1: rows = 2; cols = 3; break;
            case 2: rows = 3; cols = 2; break;
            case 3: rows = 3; cols = 3; break;
            case 4: rows = 2; cols = 4; break;
            case 5: rows = 3; cols = 4; break;
            case 6: rows = 4; cols = 3; break;
            case 7: rows = 4; cols = 2; break;
            case 8: rows = 4; cols = 4; break;
            case 9: rows = 2; cols = 5; break;
            case 10: rows = 3; cols = 5; break;
            case 11: rows = 5; cols = 3; break;
            case 12: rows = 5; cols = 2; break;
            case 13: rows = 2; cols = 6; break;
            case 14: rows = 2; cols = 7; break;
            case 15: rows = 7; cols = 2; break;
            case 16: rows = 6; cols = 2; break;
            case 17: rows = 2; cols = 8; break;
            case 18: rows = 8; cols = 2; break;
        }
        return (rows << 16) + cols;
    }

    private void buildMatrix(int cmd, int param) {
        int rows = 0, cols = 0;

        saveUndo(UNDO_NONE, null, null, null);

        switch (cmd) {
            case TO_ROW:
                rows = 1;
                cols = param;
                break;
            case TO_COL:
                rows = param;
                cols = 1;
                break;
            case TO_MATRIX:
                int s = getMatrixSize(param);
                rows = s >> 16;
                cols = s & 0xffff;
                break;
        }
        Matrix M = new Matrix(rows,cols);
        for (int i = 0; i < rows*cols; i++) {
            if (i > 0) {
                stack[0].makeEmpty();
                rollDown(0, true);
            }
            int row = (rows*cols-i-1)/cols;
            int col = (rows*cols-i-1)%cols;
            M.setElement(row, col, stack[0].r, stack[0].i);
        }
        stack[0].M = M;
        stack[0].postProcess(false, false, true, true, false, false, 0);
    }

    private void saveUndo(int op, ComplexMatrixElement x, ComplexMatrixElement y, ComplexMatrixElement z) {
        if (x != null)
            lastx.copy(x);
        if (y != null)
            lasty.copy(y);
        else
            lasty.clear();
        if (z != null)
            lastz.copy(z);
        else
            lastz.clear();
        undoOp = op;
    }

    private void undo() {
        switch (undoOp)
        {
            case UNDO_NONE:
                break;

            case UNDO_UNARY:
                stack[0].copy(lastx);
                repaint(1);
                break;

            case UNDO_BINARY:
                rollUp(0, true);
                stack[0].copy(lastx);
                stack[1].copy(lasty);
                break;

            case UNDO_TRINARY:
                rollUp(0, true);
                rollUp(0, true);
                stack[0].copy(lastx);
                stack[1].copy(lasty);
                stack[2].copy(lastz);
                break;

            case UNDO_PUSH:
                stack[0].copy(lasty);
                rollDown(0, true);
                break;

            case UNDO_PUSH2:
                stack[0].copy(lasty);
                stack[1].copy(lastz);
                rollDown(0, true);
                rollDown(0, true);
                break;

            case UNDO_XY:
                stack[0].copy(lastx);
                stack[1].copy(lasty);
                repaint(2);
                break;

            case UNDO_PUSHXY:
                stack[0].copy(lasty);
                stack[1].copy(lastx);
                rollDown(0, true);
                break;

            case UNDO_ROLLDN:
                rollUp(undoParam, false);
                break;

            case UNDO_ROLLUP:
                rollDown(undoParam, false);
                break;

            case UNDO_XCHGST:
                xchgSt(undoParam);
                break;
        }
        saveUndo(UNDO_NONE /* Can't undo any more */, null, null, null);
    }

    boolean executeProgram() {
        currentStep = 0;
        stopFlag = false;
        return continueProgram();
    }
    
    boolean continueProgram() {
        yieldFlag = false;
        while (currentStep < programs.numProgSteps() && !stopFlag && !yieldFlag) {
            programs.executeProgStep(currentStep++);
        }
        return currentStep < programs.numProgSteps() && !stopFlag;
    }

    void considerYield() {
        if (graphCmd!=0 && graphCmd!=PROG_RUN) // what about PROG_DIFF???
            return;
        long timeUsed = System.currentTimeMillis()-progRunStart;
        if (graphCmd==PROG_RUN && timeUsed>500) {
            yieldFlag = true;
        } else if (graphCmd==0 && timeUsed>1000) {
            yieldFlag = true;
            canvas.prepareGraph(PROG_RUN, programs.currentProg());
        }
    }

    void enterProgState(int progNo, boolean forEdit) {
        programs.prepareProg(progNo, forEdit);
        returnStack = new short[STACK_SIZE];
        returnStackDepth = 0;
    }
    
    void exitProgState() {
        programs.cleanup();
        returnStack = null;
    }

    private void differentiateProgram() {
        Real x   = new Real(stack[0].r);
        Real xi  = new Real(stack[0].i);
        Real h   = new Real();
        Real y0  = new Real();
        Real y0i = new Real();
        Real y1  = new Real();
        Real y1i = new Real();
        Real y2  = new Real();
        Real y2i = new Real();

        push(Real.NAN);

        if (!x.isFinite() || !xi.isFinite()) {
            // Abnormal x. (nan is already pushed)
            return;
        }

        Real.magicRounding = false;
        h.assign(Real.ONE);
        h.scalbn(Math.max(-21, x.exponent-0x40000000-21));

        boolean finished = false;

        stack[0].set(x, xi);
        executeProgram();
        y0.assign(stack[0].r);
        y0i.assign(stack[0].i);
        if (!y0.isFinite() || !y0i.isFinite()) {
            stack[0].makeNan();
            Real.magicRounding = true;
            return;
        }

        for (int n=0; n<3 && !finished; n++)
        {
            // y1 = f(x-h);
            stack[0].set(x, xi);
            stack[0].r.sub(h);
            executeProgram();
            y1.assign(stack[0].r);
            y1i.assign(stack[0].i);

            // y2 = f(x+h);
            stack[0].set(x, xi);
            stack[0].r.add(h);
            executeProgram();
            y2.assign(stack[0].r);
            y2i.assign(stack[0].i);

            if (!y1.isFinite() || !y1i.isFinite() ||
                !y2.isFinite() || !y2i.isFinite())
            {
                stack[0].makeNan();
                Real.magicRounding = true;
                return;
            }

            int exp = Math.max(y0.exponent, y0i.exponent);
            exp = Math.max(exp, y1.exponent);
            exp = Math.max(exp, y1i.exponent);
            exp = Math.max(exp, y2.exponent);
            exp = Math.max(exp, y2i.exponent);

            rTmp.assign(y1);
            rTmp.add(y2);
            int exp3 = rTmp.exponent;
            rTmp.assign(y1i);
            rTmp.add(y2i);
            exp3 = Math.max(exp3, rTmp.exponent);
            y2.sub(y1);
            y2i.sub(y1i);
            int exp2 = Math.max(y2.exponent, y2i.exponent);
            y1.sub(y0);
            y1i.sub(y0i);
            exp2 = Math.max(exp2, y1.exponent+1);
            exp2 = Math.max(exp2, y1i.exponent+1);

            if (exp3 < exp-5)       // i.e. y1 == -y2  => pathological case
                finished = true;
            else if (exp-exp2 > 63) // i.e. y1 == y2   => pathological case
                h.scalbn(42);
            else if (exp-exp2 > 22 || exp-exp2 < 20) {
                // Try to adjust h so that 2/3 of the bits of y2-y1 are valid
                h.scalbn(((exp-exp2)-21)/2);
                if (x.exponent - h.exponent > 59)
                    h.exponent = x.exponent-59;
            } else
                finished = true;
        }
        y2.div(h);
        y2i.div(h);
        y2.scalbn(-1);
        y2i.scalbn(-1);
        stack[0].set(y2, y2i);
        Real.magicRounding = true;
    }

    public void command(int cmd, int param) throws OutOfMemoryError {
        int i;

        // The following commands are NOT recorded in a program,
        // but they may call other "command"s which are programmed
        switch (cmd)
        {
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
                if (inputInProgress) {
                    parseInput();
                    return;
                }
                break; // Fall-through to ENTER command below

            case CLEAR:
                if (inputInProgress) {
                    input(cmd);
                    return;
                }
                break; // Fall-through to CLEAR command below

            case MONITOR_ENTER:
                if (inputInProgress) // Do implicit enter
                    parseInput();
                isInsideMonitor = true;
                repaintAll();
                return;

            case MONITOR_EXIT:
                isInsideMonitor = false;
                repaintAll();
                return;

            case MONITOR_UP:
                setMonitorRow(monitorRow-1,true);
                return;

            case MONITOR_DOWN:
                setMonitorRow(monitorRow+1,true);
                return;

            case MONITOR_LEFT:
                if (monitorMode == MONITOR_PROG) // page up
                    setMonitorRow(monitorRow-maxDisplayedMonitorSize+1, false);
                else
                    setMonitorCol(monitorCol-1,true);
                return;

            case MONITOR_RIGHT:
                if (monitorMode == MONITOR_PROG) // page down
                    setMonitorRow(monitorRow+maxDisplayedMonitorSize-1, false);
                else
                    setMonitorCol(monitorCol+1,true);
                return;

            case MONITOR_PUSH:
                if (monitorMode == MONITOR_PROG) {
                    command(MONITOR_EXIT,0);
                    return;
                }
                // no break here
            case MONITOR_PUT:
                switch (monitorMode) {
                    case MONITOR_MEM:     command(STO,monitorRow);         break;
                    case MONITOR_STAT:    command(STAT_STO,monitorRow);    break;
                    case MONITOR_FINANCE: command(FINANCE_STO,monitorRow); break;
                    case MONITOR_MATRIX:
                        if (stack[0].isMatrix()) {
                            // Cannot store Matrix in matrix
                            if (cmd == MONITOR_PUSH)
                                command(MONITOR_EXIT,0);
                            return;
                        }
                        command(MATRIX_STO,(monitorRow<<16)+monitorCol);
                        break;
                    case MONITOR_PROG:                   // label: SST
                        if (monitorRow < programs.numProgSteps()) {
                            progRecording = false;
                            progRunning = true;
                            currentStep = monitorRow;
                            graphCmd = -1; // Prevent considerYield() from switching to GraphCanvas
                            programs.executeProgStep(currentStep++);
                            progRunning = false;
                            progRecording = true;
                            setMonitorRow(currentStep, false);
                        }
                        return;
                }

                setMonitorRow(monitorRow+1,true); // Proceed to next element
                if (cmd == MONITOR_PUSH) {
                    // Put, pop and return
                    command(CLEAR,0);
                    command(MONITOR_EXIT,0);
                }
                return;

            case MONITOR_GET:
                switch (monitorMode) {
                    case MONITOR_MEM:     command(RCL,monitorRow);         break;
                    case MONITOR_STAT:    command(STAT_RCL,monitorRow);    break;
                    case MONITOR_FINANCE: command(FINANCE_RCL,monitorRow); break;
                    case MONITOR_MATRIX:
                        command(MATRIX_RCL,(monitorRow<<16)+monitorCol);
                        break;
                    case MONITOR_PROG: // delete current line
                        // cannot delete end of program mark
                        if (monitorRow < programs.numProgSteps()) {
                            currentStep = monitorRow;
                            programs.deleteStep(currentStep);
                            repaintAll();
                        }
                        return;
                }
                setMonitorRow(monitorRow+1,true); // Proceed to next element
                return;
        }

        // For all the commands below, do implicit enter
        if (inputInProgress)
            parseInput();

        if (progRecording) {
            currentStep = programs.numProgSteps();
            if (monitorMode == MONITOR_PROG) {
                currentStep = monitorRow;
            }
            boolean inserted = programs.record(cmd, param, currentStep);
            if (inserted && monitorMode == MONITOR_PROG) {
                setMonitorRow(monitorRow+1, false);
            }
        }

        switch (cmd)
        {
            case ENTER:
                enter();
                break;

            case CLEAR:
            case ADD:   case SUB:   case MUL:   case DIV:
            case PERCENT_CHG:
            case YPOWX: case XRTY:
            case PYX:   case CYX:
            case ATAN2: case HYPOT:
            case AND:   case OR:    case XOR:   case BIC:
            case YUPX:  case YDNX:
            case FINANCE_MULINT:    case FINANCE_DIVINT:
            case DHMS_PLUS:
            case TO_CPLX:
            case MOD:   case DIVF:
            case MIN:   case MAX:
            case MATRIX_NEW: case MATRIX_CONCAT:case MATRIX_STACK:
            case MATRIX_AIJ:
            case GCD:
                binary(cmd);
                break;

            case NEG:   case RECIP: case SQR:   case ABS:
            case TRANSP:case TRANSP_CONJ:       case DETERM:case TRACE:
            case SQRT:  case PERCENT:
            case CPLX_ARG: case CPLX_CONJ:
            case LN:    case EXP:
            case LOG10: case EXP10: case LOG2:  case EXP2:
            case SIN:   case COS:   case TAN:
            case SINH:  case COSH:  case TANH:
            case ASIN:  case ACOS:  case ATAN:
            case ASINH: case ACOSH: case ATANH:
            case ROUND: case CEIL:  case FLOOR: case TRUNC: case FRAC:
            case XCHGMEM:
            case MATRIX_ROW: case MATRIX_COL:
            case UNIT_SET:   case UNIT_SET_INV: case UNIT_CONVERT: case UNIT_CLEAR:
                unaryComplexMatrix(cmd,param);
                break;

            case FACT:  case GAMMA:
            case ERFC:  case INVERFC: case PHI: case INVPHI:
            case NOT:
            case TO_DEG:case TO_RAD:case TO_DHMS:case TO_H:
            case SGN:
            case DHMS_TO_UNIX: case UNIX_TO_DHMS:
            case DHMS_TO_JD:   case JD_TO_DHMS:
            case DHMS_TO_MJD:  case MJD_TO_DHMS:
            case LIN_YEST: case LIN_XEST: case LOG_YEST: case LOG_XEST:
            case EXP_YEST: case EXP_XEST: case POW_YEST: case POW_XEST:
            case CONV_C_F: case CONV_F_C:
            case TO_PRIME:
                unary(cmd);
                break;

            case TO_ROW: case TO_COL: case TO_MATRIX:
                buildMatrix(cmd,param);
                break;

            case PI:          push(Real.PI);                         break;
            case CONST_c:     push(0x4000001c, 0x4779e12800000000L, uTmp.unity().m(1).s(-1).pack()); break;
            case CONST_h:     push(0x3fffff91, 0x6e182e2c4f8769d7L, uTmp.unity().J(1).s(1).pack()); break;
            case CONST_mu_0:  push(0x3fffffec, 0x5454dc3e67db2c21L, uTmp.unity().kg(1).m(1).C(-2).pack()); break;
            case CONST_eps_0: push(0x3fffffdb, 0x4de1dbc537b4c1b4L, uTmp.unity().s(2).C(2).kg(-1).m(-3).pack()); break;
            case CONST_NA:    push(0x4000004e, 0x7f8618974d08a8a3L, uTmp.unity().mol(-1).pack()); break;
            case CONST_R:     push(0x40000003, 0x428409e55c0fcb4fL, uTmp.unity().J(1).mol(-1).K(-1).pack()); break;
            case CONST_k:     push(0x3fffffb4, 0x42c39fc54a31b251L, uTmp.unity().J(1).K(-1).pack()); break;
            case CONST_F:     push(0x40000010, 0x5e3955c0ebedfa44L, uTmp.unity().C(1).mol(-1).pack()); break;
            case CONST_alpha: push(0x3ffffff8, 0x778f509fc49a037cL); break;
            case CONST_a_0:   push(0x3fffffdd, 0x745e074b4cc9779dL, uTmp.unity().m(1).pack()); break;
            case CONST_R_inf: push(0x40000017, 0x53b911c8c57e23f2L, uTmp.unity().m(-1).pack()); break;
            case CONST_mu_B:  push(0x3fffffb3, 0x59b155a1fc8bc518L, uTmp.unity().m(2).C(1).s(-1).pack()); break;
            case CONST_e:     push(0x3fffffc1, 0x5e9368129b6bd383L, uTmp.unity().C(1).pack()); break;
            case CONST_m_e:   push(0x3fffff9c, 0x49e7724facfd39d3L, uTmp.unity().kg(1).pack()); break;
            case CONST_m_p:   push(0x3fffffa7, 0x4242660920c47270L, uTmp.unity().kg(1).pack()); break;
            case CONST_m_n:   push(0x3fffffa7, 0x4259c7a5e1aedcf5L, uTmp.unity().kg(1).pack()); break;
            case CONST_m_u:   push(0x3fffffa7, 0x41c7dd268f7c7292L, uTmp.unity().kg(1).pack()); break;
            case CONST_G:     push(0x3fffffde, 0x49626d965cdf45a7L, uTmp.unity().m(3).kg(-1).s(-2).pack()); break;
            case CONST_g_n:   push(0x40000003, 0x4e7404ea4a8c154dL, uTmp.unity().m(1).s(-2).pack()); break;
            case CONST_ly:    push(0x40000035, 0x4338f7ee448d8000L, uTmp.unity().m(1).pack()); break;
            case CONST_AU:    push(0x40000025, 0x45a974b4c6000000L, uTmp.unity().m(1).pack()); break;
            case CONST_pc:    push(0x40000036, 0x6da012f9404b0988L, uTmp.unity().m(1).pack()); break;
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
            case PUSH_INF:    push(Real.INF);                        break;
            case PUSH_INF_N:  push(Real.INF_N);                      break;

            case PUSH_INT:
                rTmp.assign( param);
                push(rTmp);
                break;

            case PUSH_INT_N:
                rTmp.assign(param);
                rTmp.neg();
                push(rTmp);
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

            case TIME_NOW:
                rTmp.time();
                rTmp2.date();
                rTmp.add(rTmp2);
                push(rTmp);
                break;

            case GUESS:
                push(stack[0]);
                Guess g = new Guess();
                String guess = g.guess(stack[0].r,stack[0].i);
                if (!progRunning) // No message while program is running
                    setMessage("Guess", guess);
                break;

            case UNIT_DESCRIBE:
                if (!progRunning) // No message while program is running
                    setMessage("Unit", Unit.describe(stack[0].unit));
                break;

            case IF_EQUAL:
            case IF_NEQUAL:
            case IF_LESS:
            case IF_LEQUAL:
            case IF_GREATER:
            case IF_EQUAL_Z:
            case IF_NEQUAL_Z:
            case IF_LESS_Z:
            case IF_LEQUAL_Z:
            case IF_GREATER_Z:
                cond(cmd);
                break;

            case LBL:
                break;
            case GTO:
            case GSB:
            case RTN:
            case STOP:
            case DSE:
            case ISG:
                flow(cmd, param);
                break;

            case SELECT:
                trinary(cmd);
                break;

            case RP:
            case PR:
            case MATRIX_SPLIT:
                xyOp(cmd);
                break;

            case CLS:
                saveUndo(UNDO_NONE /* Cannot undo this */, stack[0], null, null);
                clearStack();
                break;

            case RCLST:
                push(stack[param]);
                break;

            case ROLLDN:
                param = getStackHeight()-1;
                // fall-through to next case...
            case ROLLDN_N:
                if (param > 0 && !stack[param].isEmpty()) {
                    rollDown(param, false);
                    saveUndo(UNDO_ROLLDN, null, null, null);
                    undoParam = param; // Extra undo information!
                }
                break;

            case ROLLUP:
                param = getStackHeight()-1;
                // fall-through to next case...
            case ROLLUP_N:
                if (param > 0 && !stack[param].isEmpty()) {
                    rollUp(param, false);
                    saveUndo(UNDO_ROLLUP, null, null, null);
                    undoParam = param; // Extra undo information!
                }
                break;

            case XCHG:
                param = 1;
                // fall-through to next case...
            case XCHGST:
                if (param != 0 && !stack[param].isEmpty()) {
                    xchgSt(param);
                    saveUndo(UNDO_XCHGST, null, null, null);
                    undoParam = param; // Extra undo information!
                }
                break;

            case LASTX:
                push(lastx);
                break;

            case UNDO:
                undo();
                break;

            case STO_X:
            case STP_X:
                rTmp.assign(stack[0].r);
                rTmp.round();
                param = rTmp.toInteger();
                if (param<0 || param>15)
                    break;
                // fall-through to next case...
            case STO:
            case STP:
                i = cmd==STO || cmd==STP ? 0 : 1;
                allocMem();
                if (cmd==STO || cmd==STO_X) {
                    mem[param].copy(stack[i]);
                } else {
                    Matrix X = stack[i].M;
                    Matrix Y = mem[param].M;
                    if (X != null || Y != null) {
                        if (X != null && Y != null) {
                            Y = Matrix.add(Y,X);
                            mem[param].set(Y);
                        } else {
                            mem[param].makeNan();
                        }
                    } else {
                        mem[param].unit = Unit.add(mem[param].unit, stack[i].unit, rTmp, rTmp2);
                        mem[param].r.mul(rTmp);
                        mem[param].r.add(rTmp2);
                        mem[param].i.mul(rTmp);
                        mem[param].r.add(stack[i].r);
                        mem[param].i.add(stack[i].i);
                    }
                }
                mem[param].clearStrings();
                if (monitorMode == MONITOR_MEM) {
                    repaintAll();
                }
                break;

            case RCL_X:
                rTmp.assign(stack[0].r);
                rTmp.round();
                param = stack[0].r.toInteger();
                if (param<0 || param>15) {
                    push(Real.NAN);
                    break;
                }
                // fall-through to next case...
            case RCL:
                if (mem != null)
                    push(mem[param]);
                else
                    push(Real.ZERO);
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
            case MATRIX_SIZE:
                stat2(cmd);
                break;

            case AVGXW:
            case LIN_R:
            case LOG_R:
            case EXP_R:
            case POW_R:
                stat1(cmd);
                break;

            case STAT_STO:
                allocStat();
                if (!stack[0].isMatrix()) // Cannot store Matrix in stat
                    stat[param].copy(stack[0]);
                else
                    stat[param].makeNan();
                stat[param].clearStrings();
                if (monitorMode == MONITOR_STAT) {
                    repaintAll();
                }
                break;

            case STAT_RCL:
                if (stat != null)
                    push(stat[param]);
                else
                    push(Real.ZERO);
                break;

            case N:         push(SUM1);      break;
            case SUMX:      push(SUMx);      break;
            case SUMXX:     push(SUMx2);     break;
            case SUMY:      push(SUMy);      break;
            case SUMYY:     push(SUMy2);     break;
            case SUMXY:     push(SUMxy);     break;
            case SUMLNX:    push(SUMlnx);    break;
            case SUMLN2X:   push(SUMln2x);   break;
            case SUMLNY:    push(SUMlny);    break;
            case SUMLN2Y:   push(SUMln2y);   break;
            case SUMXLNY:   push(SUMxlny);   break;
            case SUMYLNX:   push(SUMylnx);   break;
            case SUMLNXLNY: push(SUMlnxlny); break;

            case FACTORIZE:
                rTmp.assign(stack[0].r);
                rTmp.round();
                if (rTmp.exponent > 0x4000003e || !rTmp.isFinite() ||
                    stack[0].isComplex())
                {
                    setMessage("Factorize","Argument must not be abnormal, "+
                               "complex or greater than 2^63-1.");
                    push(Real.NAN);
                } else {
                    rollUp(0, true);
                    saveUndo(UNDO_PUSHXY, stack[1], stack[0], null);
                    rTmp.round();
                    long a = rTmp.toLong();
                    long b = Utils.greatestFactor(a);
                    rTmp.assign((b!=0) ? a/b : 0);
                    stack[0].set(rTmp, null, stack[1].unit);
                    rTmp.assign(b);
                    stack[1].set(rTmp);
                }
                break;

            case IS_PRIME:
                rTmp.assign(stack[0].r);
                rTmp.round();
                if (rTmp.exponent > 0x4000003e || !rTmp.isFinite() ||
                    stack[0].isComplex())
                {
                    setMessage("Prime?","Argument must not be abnormal, "+
                               "complex or greater than 2^63-1.");
                    push(Real.NAN);
                } else {
                    rollUp(0, true);
                    saveUndo(UNDO_PUSHXY, stack[1], stack[0], null);
                    rTmp.round();
                    long a = rTmp.toLong();
                    stack[1].set(rTmp, null, stack[1].unit);
                    rTmp.assign(Utils.isPrime(a) ? 1 : 0);
                    stack[0].set(rTmp);
                }
                break;

            case CPLX_SPLIT:
                rollUp(0, true);
                saveUndo(UNDO_PUSHXY, stack[1], stack[0], null);
                if (stack[1].isMatrix()) {
                    Matrix M = stack[1].M;
                    stack[0].set(Matrix.re(M));
                    stack[1].set(Matrix.im(M));
                } else {
                    rTmp.assign(stack[1].i);
                    stack[0].set(stack[1].r, null, stack[1].unit);
                    stack[1].set(rTmp, null, stack[1].unit);
                }
                break;

            case BREAK_MATRIX:
                saveUndo(UNDO_NONE, stack[0], null, null);
                if (stack[0].isMatrix()) {
                    Matrix M = stack[0].M;
                    stack[0].makeEmpty();
                    rollDown(0, true);
                    for (int row = 0; row < M.rows; row++)
                        for (int col = 0; col < M.cols; col++) {
                            M.getElement(row, col, rTmp, rTmp2);
                            rollUp(0, true);
                            stack[0].set(rTmp, rTmp2);
                        }
                }
                break;

            case FINANCE_STO:
                allocFinance();
                if (!stack[0].isMatrix())
                    // Cannot store Matrix in finance
                    finance[param].copy(stack[0]);
                else
                    finance[param].makeNan();
                finance[param].clearStrings();
                if (monitorMode == MONITOR_FINANCE) {
                    repaintAll();
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
                finance[param].clearStrings();
                if (monitorMode == MONITOR_FINANCE) {
                    repaintAll();
                }
                break;

            case FINANCE_CLEAR:
                clearFinance();
                break;

            case FINANCE_BGNEND:
                begin = !begin;
                break;

            case MONITOR_NONE:
                setMonitoring(cmd,0,null);
                break;

            case MONITOR_MEM:
                setMonitoring(cmd,param,elementMonitor.withElements(mem));
                break;

            case MONITOR_STAT:
                setMonitoring(cmd,param,elementMonitor.withElements(stat));
                break;

            case MONITOR_FINANCE:
                setMonitoring(cmd,FINANCE_SIZE,elementMonitor.withElements(finance));
                break;

            case MONITOR_MATRIX:
                setMonitoring(cmd,param,matrixMonitor);
                // updateMatrixMonitor() will be run later
                break;

            case MONITOR_PROG:
                if (!progRecording)
                    break;            // what happened here? Should not happen!
                progInitialMonitorSize = param; // also for size = 0
                setMonitoring(MONITOR_PROG,param,programs);
                initProgMonitor(true);
                break;

            case MATRIX_STO:
                Matrix M = getCurrentMatrix();
                if (M == null)
                    break;
                int col = param&0xffff;
                int row = (param>>16)&0xffff;
                if (col>=M.cols || row>=M.rows)
                    break;
                if (countReferences(M) > 1) { // "Copy on write"
                    M = new Matrix(M);
                    // Find original matrix position, and automagically
                    // replace it
                    for (i=0; i<STACK_SIZE; i++)
                        if (stack[i].isMatrix()) {
                            stack[i].set(M);
                            break;
                        }
                }
                if (!stack[0].isMatrix())
                    // Cannot store Matrix in matrix
                    M.setElement(row,col,stack[0].r,stack[0].i);
                else
                    M.setElement(row,col,Real.NAN,null);
                if (monitorMode == MONITOR_MATRIX) {
                    matrixMonitor.matrixElementChanged(row, col);
                    repaintAll();
                }
                break;

            case MATRIX_RCL:
                M = getCurrentMatrix();
                if (M == null) {
                    push(Real.NAN,null);
                } else {
                    col = param&0xffff;
                    row = (param>>16)&0xffff;
                    M.getElement(row,col,rTmp,rTmp2);
                    push(rTmp,rTmp2);
                }
                break;

            case MATRIX_MAX:
            case MATRIX_MIN:
                M = getCurrentMatrix();
                if (M == null) {
                    push(Real.NAN,null);
                } else {
                    if (cmd == MATRIX_MAX)
                        M.max(rTmp);
                    else
                        M.min(rTmp);
                    push(rTmp,null);
                }
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
                if (degrees) {
                    degrees = false;
                    grad = true;
                } else if (grad) {
                    degrees = false;
                    grad = false;
                } else {
                    degrees = true;
                    grad = false;
                }
                break;

            case FREE_MEM:
                rollUp(0, true);
                rollUp(0, true);
                Runtime.getRuntime().gc();
                stack[0].clear();
                stack[1].clear();
                stack[0].r.assign(Runtime.getRuntime().freeMemory());
                stack[1].r.assign(Runtime.getRuntime().totalMemory());
                break;
	
            case VERSION: push(0x40000002,0x4800000000000000L); break;

            case PROG_APPEND:
                if (programs.isEmpty(param))
                    break; // cannot modify a nonexistent program
                // fall-through to next case...
            case PROG_NEW:
                progRecording = true;
                if (cmd == PROG_NEW) {
                    programs.newProgram(param, newProgramName);
                }
                enterProgState(param, true);
                if (progInitialMonitorSize > 0) {
                    setMonitoring(MONITOR_PROG, progInitialMonitorSize, programs);
                    initProgMonitor(true);
                }
                break;

            case FINALIZE:
            case PROG_FINISH:
                if (progRecording && !programs.isEmpty()) {
                    progRecording = false;
                    programs.trim();
                    exitProgState();
                    if (monitorMode == MONITOR_PROG) 
                        setMonitoring(MONITOR_NONE,0,null);
                }
                break;

            case PROG_RUN:
                if (!programs.isEmpty(param)) {
                    progRunning = true;
                    graphCmd = 0;
                    enterProgState(param, false);
                    progRunStart = System.currentTimeMillis();
                    progRunning = executeProgram();
                    if (!progRunning) {
                        exitProgState();
                    }
                }
                break;

            case PROG_PURGE:
                programs.purge();
                if (monitorMode == MONITOR_PROG) 
                    initProgMonitor(true);
                break;

            case PROG_CLEAR:
                programs.clear(param);
                progRecording = false;
                progRunning = false;
                break;

            case PROG_DIFF:
                if (!programs.isEmpty(param)) {
                    progRunning = true;
                    graphCmd = PROG_DIFF;
                    enterProgState(param, false);
                    progRunStart = System.currentTimeMillis();
                    differentiateProgram();
                    exitProgState();
                    progRunning = false;
                }
                break;
        }

        // Do this here, because almost *anything* can change current matrix
        if (monitorMode == MONITOR_MATRIX)
            updateMatrixMonitor();
    }

    static class GridStep {
        short pos;
        byte type;
        String label;

        static final byte ZERO = 1;
        static final byte BIGTICK = 2;
    }

    int graphCmd;

    // Create an inner class to limit each class size to 64kB
    // This is a temporary solution
    protected class CalcEngineInner {


    Real xMin,xMax,yMin,yMax,a,b,c,bi,x0,x0i,y0,y1,y2,y0i,y1i,y2i,total,totalI;
    long integralN,totalExtra,totalExtraI;
    int integralDepth;
    boolean integralFailed;
    boolean maximizing;
    int zzN, zzNbits;
    Random random;

    public boolean prepareGraph(int cmd, int param) {
        graphCmd = cmd;
        progRunning = false;

        if (cmd >= PROG_DRAW) {
            if (param<0 || param>=NUM_PROGS || programs.isEmpty(param)) {
                setMessage("Draw", "The selected program is empty");
                return false;
            }
            enterProgState(param, false);
            // Warning, exitProgState not called when graph finished
        } else if (cmd == PROG_RUN) {
            // enterProgState already done, everything should be ok
            // Warning, exitProgState not called when graph finished
            random = new Random();
        } else if (stat == null || statLogSize == 0) {
            setMessage("Draw", "The statistics are empty");
            return false;
        }

        xMin = new Real();
        xMax = new Real();
        yMin = new Real();
        yMax = new Real();
        a = new Real();
        b = new Real();
        Real x = rTmp3;
        Real y = rTmp4;
        int i;
        if (inputInProgress)
            parseInput();

        // Find boundaries
        if (cmd >= PROG_DRAW && cmd <= PROG_DRAWZZ) {
            xMin.assign(stack[3].r);
            xMax.assign(stack[2].r);
            yMin.assign(stack[1].r);
            yMax.assign(stack[0].r);
            if (getStackHeight()<4 ||
                stack[0].isAbnormalOrComplex() || stack[1].isAbnormalOrComplex() ||
                stack[2].isAbnormalOrComplex() || stack[3].isAbnormalOrComplex() ||
                xMin.greaterEqual(xMax) || yMin.greaterEqual(yMax)) {
                setMessage("Draw", "The draw area limits, xMin, xMax, yMin "+
                           "and yMax must be pushed to the stack (in that "+
                           "order) before drawing. xMin must be less than "+
                           "xMax and yMin must be less than yMax.");
                return false;
            }
        }
        else if (cmd == PROG_SOLVE)
        {
            if (getStackHeight()<2) {
                setMessage("Solve", "The search bounds, a and b, must be "+
                           "pushed to the stack before solving. The search "+
                           "bounds must straddle the root, i.e. either f(a) "+
                           "or f(b) must be negative, but not both");
                return false;
            }
            y1 = new Real();
            y2 = new Real();

            // Dummy limits for painting a nice progress bar
            xMin.assign(1);
            xMax.assign(2);
            yMin.assign(-1);
            yMax.assign(1);

            // Fetch solve interval [a, b]
            a.assign(stack[1].r);
            b.assign(stack[0].r);
            push(Real.NAN,null);

            if (stack[1].isAbnormalOrComplex() || stack[2].isAbnormalOrComplex()) {
                // Abnormal limits. (nan is already pushed)
                setMessage("Solve", "Abnormal or complex search bounds");
                return false;
            }
            Real.magicRounding = false;

            if (a.sign != b.sign) {
                // Check first the pathological case f(0)==0
                stack[0].r.makeZero();
                executeProgram();
                y1.assign(stack[0].r);
                if (stack[0].isAbnormalOrComplex()) {
                    // Discontinuous or complex function
                    stack[0].makeNan();
                    Real.magicRounding = true;
                    setMessage("Solve", "Discontinuous or complex function");
                    return false;
                }
                if (y1.isZero()) {
                    Real.magicRounding = true;
                    return false;// We're done; f(0)=0, and stack[0] contains 0
                }
            }

            // Evaluate function at limits
            stack[0].set(a);
            executeProgram();
            y1.assign(stack[0].r);
            if (stack[0].isAbnormalOrComplex()) {
                // Discontinuous or complex function
                stack[0].makeNan();
                Real.magicRounding = true;
                setMessage("Solve", "Discontinuous or complex function");
                return false;
            }
            if (y1.isZero()) {
                stack[0].set(a);
                Real.magicRounding = true;
                return false;
            }

            stack[0].set(b);
            executeProgram();
            y2.assign(stack[0].r);
            if (stack[0].isAbnormalOrComplex() || y1.sign == y2.sign) {
                // Discontinuous or complex function, or
                // initial bounds do not straddle the root
                stack[0].makeNan();
                Real.magicRounding = true;
                if (y1.sign == y2.sign)
                    setMessage("Solve", "The search bounds, a and b, must be "+
                               "pushed to the stack before solving. The "+
                               "search bounds must straddle the root, i.e. "+
                               "either f(a) or f(b) must be negative, but "+
                               "not both");
                else
                    setMessage("Solve", "Discontinuous or complex function");
                return false;
            }
            if (y2.isZero()) {
                stack[0].set(b);
                Real.magicRounding = true;
                return false;
            }

            // Let stack always hold best value till now
            stack[0].set(y1.absLessThan(y2) ? a : b);
            Real.magicRounding = true;
        }
        else if (cmd == PROG_INTEGR)
        {
            if (getStackHeight()<3 || stack[0].isNothing()) {
                setMessage("Integrate", "The integration limits and the "+
                           "desired accuracy (i.e. some small nonzero "+
                           "number) must be pushed to the stack in that "+
                           "order before integrating");
                return false;
            }
            total  = new Real();
            totalI = new Real();
            y0  = new Real();
            y0i = new Real();
            y1  = new Real();
            y1i = new Real();
            y2  = new Real();
            y2i = new Real();
            x0  = new Real();
            x0i = new Real();
            bi  = new Real();

            totalExtra = 0;
            totalExtraI = 0;
            integralN = 0;
            integralDepth = 0;
            integralFailed = false;
            y0.makeNan();

            x0.assign(stack[2].r);
            x0i.assign(stack[2].i);
            b.assign(stack[1].r);
            bi.assign(stack[1].i);
            b.sub(x0);
            bi.sub(x0i);
            a.assign(stack[0].r); // Error term

            xMin.assign(1);
            xMax.assign(2);
            yMin.assign(-2);
            yMax.assign(1);

            push(Real.ZERO,null);
        }
        else if (cmd == PROG_MINMAX)
        {
            if (getStackHeight()<2) {
                setMessage("Min/max", "The search bounds, a and b, must be "+
                           "pushed to the stack before activating this "+
                           "function. The search bounds must be set so that "+
                           "the function value at the midpoint, f((a+b)/2), "+
                           "is either greater than both f(a) and f(b), or "+
                           "less than both f(a) and f(b).");
                return false;
            }
            c = new Real();
            y0 = new Real();
            y1 = new Real();
            y2 = new Real();

            // Dummy limits for painting a nice progress bar
            xMin.assign(1);
            xMax.assign(2);
            yMin.assign(-1);
            yMax.assign(1);

            Real.magicRounding = false;

            // Fetch min/max interval [a, b]
            a.assign(stack[1].r);
            c.assign(stack[0].r);
            push(Real.NAN,null);
            b.assign(a);
            b.add(c);
            b.scalbn(-1);

            if (stack[1].isAbnormalOrComplex() || stack[2].isAbnormalOrComplex()) {
                // Abnormal limits. (nan is already pushed)
                Real.magicRounding = true;
                setMessage("Min/max", "Abnormal or complex search bounds");
                return false;
            }

            // Evaluate function at limits
            stack[0].set(a);
            executeProgram();
            y0.assign(stack[0].r);
            if (stack[0].isAbnormalOrComplex()) {
                // Discontinuous or complex function
                stack[0].makeNan();
                Real.magicRounding = true;
                setMessage("Min/max", "Discontinuous or complex function");
                return false;
            }

            stack[0].set(b);
            executeProgram();
            y1.assign(stack[0].r);
            if (stack[0].isAbnormalOrComplex()) {
                // Discontinuous or complex function
                stack[0].makeNan();
                Real.magicRounding = true;
                setMessage("Min/max", "Discontinuous or complex function");
                return false;
            }

            stack[0].set(c);
            executeProgram();
            y2.assign(stack[0].r);
            if (stack[0].isAbnormalOrComplex()) {
                // Discontinuous or complex function
                stack[0].makeNan();
                Real.magicRounding = true;
                setMessage("Min/max", "Discontinuous or complex function");
                return false;
            }

            if ((y1.greaterThan(y0) && y1.greaterEqual(y2)) ||
                (y1.greaterEqual(y0) && y1.greaterThan(y2))) {
                maximizing = true;
            } else if ((y1.lessThan(y0) && y1.lessEqual(y2)) ||
                       (y1.lessEqual(y0) && y1.lessThan(y2))) {
                maximizing = false; // i.e. minimizing
            } else {
                // undecidable max/min condition
                stack[0].makeNan();
                Real.magicRounding = true;
                setMessage("Min/max", "Undecidable min/max condition. The "+
                           "initial search bounds a and b must be set so "+
                           "that the function value at the midpoint, "+
                           "f((a+b)/2), is either greater than both f(a) and "+
                           "f(b), or less than both f(a) and f(b).");
                return false;
            }

            // Let stack always hold best value till now
            stack[0].set(b);
            Real.magicRounding = true;
        }
        else if (cmd == PROG_RUN)
        {
            // Nothing to prepare
        }
        else // Draw statistics
        {
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
            if (xMin.greaterEqual(xMax) || yMin.greaterEqual(yMax)) {
                setMessage("Draw", "Statistics contain only zeros");
                return false;
            }
        }

        progRunning = true;
        return true;
    }

    private int rangeScale(Real x, Real min, Real max, int size, Real offset) {
        if (!x.isFinite())
            return (x.sign*2-1)*0x4000;
        rTmp.assign(x);
        rTmp.sub(min);
        max.sub(min); // May be inexact, yes, but avoids using temporary
        rTmp.div(max);
        max.add(min);
        rTmp.mul(size-1);
        rTmp.add(offset);
        rTmp.round();
        int i = rTmp.toInteger();
        return (i < -0x4000) ? -0x4000 : ((i > 0x4000) ? 0x4000 : i);
    }

    private int findTickStep(Real step, Real fac, Real min, Real max, int size)
    {
        int pow,bigTickInterval;
        rTmp.assign(max);
        rTmp.sub(min);
        rTmp.mul(11); // minimum tick distance, 11 pixels
        rTmp.div(size); // range of 11 pixels on screen
        step.assign(rTmp);
        pow = step.lowPow10(); // convert to lower power of 10
        rTmp.div(step);

        if (rTmp.lessThan(2)) {
            // Minor 2, major 10
            step.mul(2);
            bigTickInterval = 5;
            if (pow > 0)
                pow--; // preserve tick labels 1000,2000,...
        } else if (rTmp.lessThan(5)) {
            // Minor 5, major 10
            step.mul(5);
            bigTickInterval = 2;
        } else {
            // Minor 1, major 5
            step.mul(10);
            bigTickInterval = 5;
            if (pow < 0)
                pow++; // preserve tick labels 0.05,0.1,...
        }

        if (pow > 0)
            pow = ((pow+1)/3)*3;
        else
            pow = (pow/3)*3;
        fac.assign(Real.TEN);
        fac.pow(pow);

        return bigTickInterval;

        // Sample ticks              step pow  fac
        //
        //  -10-20-30-40             5e-3  -3 1e-3
        //  ----0.05----0.1----0.15  0.01  -3    1
        //  ----0.1----0.2----0.3    0.02  -2    1
        //  -0.1-0.2-0.3-0.4         0.05  -2    1
        //  ----0.5----1----1.5       0.1  -2    1
        //  ----1----2----3----4      0.2  -1    1
        //  -1-2-3-4                  0.5  -1    1
        //  ----5----10----15           1  -1    1
        //  ----10----20----30          2   0    1
        //  -10-20-30-40                5   0    1
        //  ----50----100----150       10   0    1
        //  ----100----200----300      20   1    1
        //  -100-200-300-400           50   1    1
        //  ----500----1000----1500   100   1    1
        //  ----1000----2000----3000  200   2    1
        //  -1-2-3-4                  500   2  1e3
    }

    private GridStep[] calcGridSteps(Real min, Real max, int off, int size) {
        Real step = rTmp2;
        Real pos = rTmp3;
        Real fac = new Real();
        Real.NumberFormat fmt = new Real.NumberFormat();
        fmt.point = format.point;

        boolean inverted = false;
        if (min.greaterThan(max)) {
            min.swap(max);
            inverted = true;
        }

        int bigTick = findTickStep(step,fac,min,max,size);
        pos.assign(max);
        pos.div(step);
        pos.floor();
        long lastTick = pos.toLong();
        pos.assign(min);
        pos.div(step);
        pos.ceil();
        long tickNo = pos.toLong();
        pos.mul(step);

        int n = (int)(lastTick-tickNo+1);
        GridStep[] gridSteps = new GridStep[n];

        for (int i=0; i<n; i++) {
            gridSteps[i] = new GridStep();
            if (inverted)
                gridSteps[i].pos =
                    (short)(off + rangeScale(pos, max, min, size, Real.ZERO));
            else
                gridSteps[i].pos =
                    (short)(off + rangeScale(pos, min, max, size, Real.ZERO));

            if (tickNo == 0) {
                gridSteps[i].type = GridStep.ZERO;
            } else if (tickNo%bigTick == 0) {
                gridSteps[i].type = GridStep.BIGTICK;
                rTmp4.assign(pos);
                rTmp4.div(fac);
                gridSteps[i].label = rTmp4.toString(fmt);
            }
            pos.add(step);
            tickNo++;
        }
        if (inverted)
            min.swap(max); // Swap back

        return gridSteps;
    }

    public void drawAxes(Graphics g, int gx, int gy, int gw, int gh,
                         boolean bgrDisplay, boolean sparse) {
        if (graphCmd == PROG_RUN)
            return; // No axis drawn in this case
        
        int i,j,xi,yi,x0,y0,inc,lx,ly;
        UniFont font =
            GFont.newFont(UniFont.SMALL | (bgrDisplay ? UniFont.BGR_ORDER : 0),
                          false, false, canvas);
        int fh = font.getHeight()-1;
        int fw = font.charWidth();
        boolean skipYAxis = graphCmd>=PROG_SOLVE;

        // shrink window by 4 pixels
        gx += 2;
        gy += 2;
        gw -= 4;
        gh -= 4;

        // Calculate grid steps
        GridStep[] gridStepsX = calcGridSteps(xMin,xMax,gx,gw);
        GridStep[] gridStepsY = calcGridSteps(yMax,yMin,gy,gh);

        // Draw X axis
        g.setColor(0,255,128);
        y0 = gy+rangeScale(Real.ZERO,yMax,yMin,gh,Real.ZERO);
        g.drawLine(gx-2,y0,gx+gw+1,y0);
        x0 = gx+rangeScale(Real.ZERO,xMin,xMax,gw,Real.ZERO);
        g.drawLine(x0,gy-2,x0,gy+gh+1);

        if (sparse && !skipYAxis) {
            for (i=0; i<gridStepsX.length; i++) {
                for (j=0; j<gridStepsY.length; j++) {
                    xi = gridStepsX[i].pos;
                    yi = gridStepsY[j].pos;
                    if (gridStepsX[i].type == GridStep.ZERO ||
                        gridStepsY[j].type == GridStep.ZERO) {
                        g.setColor(0,255,128);
                        if (gridStepsX[i].type == GridStep.BIGTICK ||
                            gridStepsY[j].type == GridStep.BIGTICK)
                            inc = 2;
                        else
                            inc = 1;
                    } else {
                        g.setColor(0,32,16);
                        if (gridStepsX[i].type == GridStep.BIGTICK &&
                            gridStepsY[j].type == GridStep.BIGTICK)
                            inc = 3;
                        else
                            inc = 2;
                    }
                    g.drawLine(xi,yi-inc,xi,yi+inc);
                    g.drawLine(xi-inc,yi,xi+inc,yi);
                }
            }
        } else {
            for (i=0; i<gridStepsX.length; i++) {
                if (gridStepsX[i].type == GridStep.ZERO)
                    continue;
                xi = gridStepsX[i].pos;
                g.setColor(0,32,16);
                g.drawLine(xi,gy-2,xi,gy+gh+1);
                inc = (gridStepsX[i].type == GridStep.BIGTICK) ? 2 : 1;
                g.setColor(0,255,128);
                g.drawLine(xi,y0-inc,xi,y0+inc);
            }

            if (!skipYAxis) {
                for (i=0; i<gridStepsY.length; i++) {
                    if (gridStepsY[i].type == GridStep.ZERO)
                        continue;
                    yi = gridStepsY[i].pos;
                    g.setColor(0,32,16);
                    g.drawLine(gx-2,yi,gx+gw+1,yi);
                    inc = (gridStepsY[i].type == GridStep.BIGTICK) ? 2 : 1;
                    g.setColor(0,255,128);
                    g.drawLine(x0-inc,yi,x0+inc,yi);
                }
            }
        }

        if (sparse)
            return;

        // Draw labels
        for (i=0; i<gridStepsX.length; i++) {
            String label = gridStepsX[i].label;
            if (label != null && !skipYAxis) {
                xi = gridStepsX[i].pos;
                lx = xi-fw*label.length()/2;
                if (lx < gx-2)
                    lx = gx-2;
                if (lx > gx+gw+2-fw*label.length())
                    lx = gx+gw+2-fw*label.length();

                if ((y0>=gy+gh/2 && y0+4+fh <= gy+gh+1) || y0-3-fh<gy-1)
                    ly = y0+4;
                else
                    ly = y0-3-fh;

                if (ly<gy-1)
                    ly = gy-1;
                if (ly > gy+gh+1-fh)
                    ly = gy+gh+1-fh;
                font.drawString(g,lx,ly,label);
            }
        }

        if (!skipYAxis) {
            boolean sideSelected = false;
            boolean rightSide = false;
            for (i=0; i<gridStepsY.length; i++) {
                String label = gridStepsY[i].label;
                if (label != null) {
                    yi = gridStepsY[i].pos;
                    ly = yi-fh/2;
                    if (ly < gy-1)
                        ly = gy-1;
                    if (ly > gy+gh+1-fh)
                        ly = gy+gh+1-fh;

                    if ((sideSelected && rightSide) ||
                        (!sideSelected &&
                         ((x0>gx+gw/2 && x0+4+fw*label.length() <= gx+gw+2) ||
                          x0-3-fw*label.length()<gx-2)))
                    {
                        rightSide = true;
                        lx = x0+4;
                    } else {
                        lx = x0-3-fw*label.length();
                    }
                    // Keep to one side of axis once it is selected
                    sideSelected = true;

                    if (lx < gx-2)
                        lx = gx-2;
                    if (lx > gx+gw+2-fw*label.length())
                        lx = gx+gw+2-fw*label.length();
                    font.drawString(g,lx,ly,label);
                }
            }
        }
    }

    public void startGraph(Graphics g, int gx, int gy, int gw, int gh,
                           boolean bgrDisplay)
    {
        int i,xi,yi,pyi,inc;
        Real x = rTmp3;
        Real y = rTmp4;

        g.setClip(gx,gy,gw,gh);
        drawAxes(g,gx,gy,gw,gh,bgrDisplay,false);
        // shrink window by 4 pixels
        gx += 2;
        gy += 2;
        gw -= 4;
        gh -= 4;

        if (graphCmd == PROG_RUN || graphCmd >= PROG_SOLVE)
            return; // Return now to continue later

        if (graphCmd >= PROG_DRAW) {
            if (graphCmd==PROG_DRAWZZ) {
                zzN = 0;
                zzNbits = 0;
                for (int s2=1; s2<gw+4 || s2<gh+4; s2<<=1)
                    zzNbits+=2;
            }
            // a = golden ratio, 0.618
            a.assign(0, 0x3fffffff, 0x4f1bbcdcbfa53e0bL);
            b.makeZero();
            return; // Return now to continue later
        }

        // Draw statistics graph
        g.setColor(255,0,128);

        switch (graphCmd) {
            case LIN_DRAW:
                statAB(a,b,SUMx,SUMx2,SUMy,SUMxy);
                break;
            case LOG_DRAW:
                statAB(a,b,SUMlnx,SUMln2x,SUMy,SUMylnx);
                break;
            case EXP_DRAW:
                statAB(a,b,SUMx,SUMx2,SUMlny,SUMxlny);
                break;
            case POW_DRAW:
                statAB(a,b,SUMlnx,SUMln2x,SUMlny,SUMlnxlny);
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

    // Alternating bisection and interpolation steps in root finding
    private boolean bisect(Real x1, Real y1, Real x2, Real y2) {
        Real x = xMin; // use xMin as temporary value
        Real y = rTmp;

        // Stop when the interval is 0
        if (x1.equalTo(x2))
            return false;

        for (int i=0; i<2; i++) {
            if (i==0) {
                // bisect
                x.assign(x1);
                x.add(x2);
                x.scalbn(-1);
                if (x.equalTo(x1) || x.equalTo(x2)) {
                    // Pathological case may oscillate between two "nextafter"
                    // x-values => root is the x with the smallest y
                    if (y2.absLessThan(y1))
                        x1.assign(x2);
                    return false;
                }
            } else {
                // interpolate
                x.assign(x1);
                x.sub(x2);
                y.assign(y2);
                y.sub(y1);
                x.div(y);
                x.mul(y1);
                x.add(x1);
            }
            stack[0].set(x);
            executeProgram();
            // Results must always be finite
            y.assign(stack[0].r);
            if (stack[0].isAbnormalOrComplex()) {
                // Discontinuous or complex function
                x1.makeNan();
                setMessage("Solve", "Discontinuous or complex function");
                return false;
            }
            if (y.isZero()) { // perhaps we nailed the root?
                x1.assign(x);
                return false;
            }

            if (y1.sign == y.sign) {
                x1.assign(x);
                y1.assign(y);
            } else {
                x2.assign(x);
                y2.assign(y);
            }
        }
        xMin.assign(1); // Restore dummy limit
        return true;
    }

    // Gauss-Legendre Quadrature of order 4
    private void GL4_stripe(Real sum, Real sumI, Real x0, Real x0i,
                            Real H, Real HI, int depth, long n) {
        H.scalbn(-depth);
        HI.scalbn(-depth);
        sum.makeZero();
        sumI.makeZero();
        for (int i=0; i<4; i++) {
            stack[0].clear();
            if (i<2)
                stack[0].r.assign(0, 0x3ffffffe, 0x6e39b6f3d8e61419L);
            else
                stack[0].r.assign(0, 0x3ffffffd, 0x5708ff6774f7f08aL);
            if ((i&1)!=0)
                stack[0].r.neg();
            stack[0].r.add(Real.HALF);
            stack[0].i.assign(stack[0].r);
            stack[0].r.mul(H);
            stack[0].i.mul(HI);
            rTmp.assign(H);  // Should do this outside loop, but lack temp
            rTmp2.assign(n); // .
            rTmp.mul(rTmp2); // .
            rTmp.add(x0);    // .
            stack[0].r.add(rTmp);
            rTmp.assign(HI); // Should do this outside loop, but lack temp
            rTmp.mul(rTmp2); // .
            rTmp.add(x0i);   // .
            stack[0].i.add(rTmp);

            executeProgram();
            if (stack[0].isAbnormal()) {
                // Discontinuous function
                sum.makeNan();
                sumI.makeZero();
                H.scalbn(depth); // Restore H
                HI.scalbn(depth);
                return;
            }
            if (i<2)
                rTmp.assign(0, 0x3ffffffd, 0x590d03df9ed9ac8dL);
            else
                rTmp.assign(0, 0x3ffffffe, 0x53797e10309329b9L);
            stack[0].r.mul(rTmp);
            stack[0].i.mul(rTmp);

            sum.add(stack[0].r);
            sumI.add(stack[0].i);
        }
        Complex.mul(sum,sumI,H,HI);
        H.scalbn(depth); // Restore H
        HI.scalbn(depth);
    }
    
    int random(int max) {
        return (random.nextInt()>>>1)%max;
    }

    public void continueGraph(Graphics g, int gx, int gy, int gw, int gh) {
        progRunStart = System.currentTimeMillis();
        Real x = rTmp3;
        int i,xi=0,yi=0,size=0;

        g.setClip(gx,gy,gw,gh);
        // shrink window by 4 pixels
        gx += 2;
        gy += 2;
        gw -= 4;
        gh -= 4;

        do
        {
            if (graphCmd >= PROG_DRAW && graphCmd <= PROG_DRAWZZ) {
                Real.magicRounding = false;
                b.add(a);
                if (b.greaterThan(Real.ONE))
                    b.sub(Real.ONE);
                if (graphCmd == PROG_DRAW) {
                    x.assign(xMax);
                    x.sub(xMin);
                    x.mul(b);
                    x.add(xMin);
                    xi = gx+rangeScale(x,xMin,xMax,gw,Real.ZERO);
                    push(x,null);
                } else if (graphCmd == PROG_DRAWZZ) {
                    if (zzN >= (1<<zzNbits)) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {}
                        return;
                    }
                    do {
                        xi = 0;
                        yi = 0;
                        size = 1<<(zzNbits/2);
                        for (i=0; i<zzNbits; i+=2) {
                            xi = (xi<<1)+((zzN>>i)&1);
                            yi = (yi<<1)+((zzN>>(i+1))&1);
                            if ((zzN>>i)!=0)
                                size >>= 1;
                        }
                        if (size>16) size=16;
                        yi ^= xi;
                        zzN++;
                    } while (xi>=gw+4 || yi>=gh+4);
                    if (zzN >= (1<<zzNbits))
                        return;
                    x.assign(xi-2);
                    x.div(gw);
                    b.assign(xMax);
                    b.sub(xMin);
                    x.mul(b);
                    x.add(xMin);

                    Real ximag = a;
                    ximag.assign(yi-2);
                    ximag.div(gh);
                    b.assign(yMin);
                    b.sub(yMax);
                    ximag.mul(b);
                    ximag.add(yMax);
                    push(x,ximag);
                } else {
                    x.assign(b);
                    if (graphCmd == PROG_DRAWPOL) {
                        if (degrees)
                            x.mul(360);
                        else if (grad)
                            x.mul(400);
                        else
                            x.mul(Real.PI2);
                        x.mul(Real.TEN); // 10 "rounds"
                    }
                    push(x,null);
                }

                executeProgram();

                Real y = stack[0].r;
                Real yimag = stack[0].i;

                if (graphCmd == PROG_DRAW) {
                    yi = -100;
                    if (y.isFinite() && (!y.isZero() || yimag.isZero())) {
                        yi = gy+rangeScale(y,yMax,yMin,gh,Real.HALF);
                        g.setColor(255,0,128);
                        g.drawLine(xi,yi-1,xi,yi);
                    }
                    if (!yimag.isZero() && yimag.isFinite()) {
                        int yi2 = gy+rangeScale(yimag,yMax,yMin,gh,Real.HALF);
                        g.setColor(255,255,yi2==yi ? 255 : 0);
                        g.drawLine(xi,yi2-1,xi,yi2);
                    }
                } else if (graphCmd == PROG_DRAWZZ) {
                    rTmp.assign(y);
                    rTmp.hypot(yimag);
                    if (rTmp.isZero()) {
                        g.setColor(0);
                    } else {
                        y.div(rTmp);
                        yimag.div(rTmp);
                        rTmp.nextafter(Real.ZERO); // Make 1.0 into 0.9999...
                        rTmp.mod(Real.ONE);
                        y.scalbn(8);
                        yimag.scalbn(8);
                        rTmp.scalbn(8);
                        int X = y.toInteger();
                        int Y = yimag.toInteger();
                        int L = rTmp.toInteger();
                        int R = ((255*128-X*90+Y*90)*L)>>16;
                        int G = ((255*128+X*90)*L)>>16;
                        int B = ((255*128-X*90-Y*90)*L)>>16;
                        g.setColor(R,G,B);
                    }
                    g.fillRect(gx-2+xi,gy-2+yi,size,size);
                } else { // PROG_DRAWPOL or PROG_DRAWPARM
                    if (graphCmd == PROG_DRAWPOL) {
                        x.assign(b);
                        x.mul(Real.PI2);
                        x.mul(10); // 10 rounds
                        rTmp4.assign(x);
                        rTmp4.sin();
                        x.cos();
                        x.mul(y);
                        y.mul(rTmp4);
                    } else { // PROG_DRAWPARM
                        x.assign(y);
                        y.assign(yimag);
                    }
                    if (x.isFinite() && y.isFinite()) {
                        yi = gy+rangeScale(y,yMax,yMin,gh,Real.ZERO);
                        xi = gx+rangeScale(x,xMin,xMax,gw,Real.ZERO);
                        g.setColor(255,0,128);
                        g.drawLine(xi,yi,xi,yi);
                    }
                }
                command(CLEAR,0); // Remove result from stack to avoid clutter
                Real.magicRounding = true;
            }
            else if (graphCmd == PROG_SOLVE)
            {
                Real.magicRounding = false;
                if (bisect(a,y1,b,y2)) {
                    // Let stack always hold best value till now
                    stack[0].set(y1.absLessThan(y2) ? a : b);
                } else {
                    // We're done
                    stack[0].set(a);
                    progRunning = false;
                    Real.magicRounding = true;
                    return;
                }
                stack[0].i.makeZero(); // We don't want anything complex
                Real.magicRounding = true;
            }
            else if (graphCmd == PROG_INTEGR)
            {
                Real.magicRounding = false;
                if (gh/3+3+integralDepth<gh) {
                    // Draw progress
                    rTmp3.assign(Real.ONE);
                    rTmp3.scalbn(-integralDepth);
                    rTmp2.assign(integralN);
                    rTmp2.mul(rTmp3);
                    rTmp2.add(Real.ONE);
                    int x1 = rangeScale(rTmp2,xMin,xMax,gw,Real.ZERO);
                    rTmp2.add(rTmp3);
                    int x2 = rangeScale(rTmp2,xMin,xMax,gw,Real.ZERO);
                    g.setColor(255,255,0);
                    g.fillRect(gx+x1,gy+gh/3+3+integralDepth,x2-x1+1,1);
                }

                if (y0.isNan())
                    GL4_stripe(y0,y0i,x0,x0i,b,bi,integralDepth,integralN);
                GL4_stripe(y1,y1i,x0,x0i,b,bi,integralDepth+1,(integralN<<1));
                GL4_stripe(y2,y2i,x0,x0i,b,bi,integralDepth+1,(integralN<<1)+1);

                boolean recurse = false;
                if (!y1.isFinite() || !y1i.isFinite() ||
                    !y2.isFinite() || !y2i.isFinite())
                {
                    // Reached a singularity
                    totalExtra  = total. add128(totalExtra, y0, 0);
                    totalExtraI = totalI.add128(totalExtraI,y0i,0);
                    integralFailed = true;
                } else {
                    rTmp.assign(y1);
                    rTmp.add(y2);
                    rTmp.sub(y0);
                    rTmp2.assign(y1i);
                    rTmp2.add(y2i);
                    rTmp2.sub(y0i);
                    if ((rTmp.absLessThan(a) && rTmp2.absLessThan(a)) ||
                        (integralN<<2)<0 || integralDepth>=2000)
                    {
                        totalExtra  = total. add128(totalExtra, y1, 0);
                        totalExtraI = totalI.add128(totalExtraI,y1i,0);
                        totalExtra  = total. add128(totalExtra, y2, 0);
                        totalExtraI = totalI.add128(totalExtraI,y2i,0);
                        if ((integralN<<2)<0 || integralDepth>=2000)
                            integralFailed = true; // Too much recursion
                    } else {
                        recurse = true;
                    }
                }

                if (recurse) {
                    integralDepth++;
                    integralN <<= 1;
                    y0.assign(y1);
                    y0i.assign(y1i);
                } else {
                    while ((integralN & 1)!=0) {
                        integralDepth--;
                        integralN >>= 1;
                    }
                    integralN++;
                    y0.makeNan();
                }

                // Let stack always hold best value till now
                stack[0].clear();
                stack[0].r.assign(total);
                stack[0].i.assign(totalI);
                stack[0].r.roundFrom128(totalExtra);
                stack[0].i.roundFrom128(totalExtraI);

                Real.magicRounding = true;
                if (integralDepth<63 && (integralN>>integralDepth)!=0) {
                    // We're done
                    if (!stack[0].isAbnormal() && integralFailed)
                        setMessage("Integrate",
                                   "Failed to produce the desired accuracy");
                    progRunning = false;
                    return;
                }
            }
            else if (graphCmd == PROG_RUN)
            {
                if (!continueProgram()) {
                    progRunning = false;
                } else {
                    // Update "screen saver"
                    int x1 = gx+random(gw);
                    int x2 = gx+random(gw);
                    int y1 = gy+random(gh);
                    int y2 = gy+random(gh);
                    g.setColor(random(0x1000000));
                    g.drawLine(x1,y1,x2,y2);
                }
                return; // Return directly, since continueProgram runs for 100ms
            }
            else // Must be min/max
            {
                Real.magicRounding = false;
                if ((maximizing && y0.lessThan(y2)) ||
                    (!maximizing && y0.greaterThan(y2))) {
                    a.swap(c);
                    y0.swap(y2);
                }

                stack[0].set(a);
                stack[0].r.add(b);
                stack[0].r.scalbn(-1);
                executeProgram();
                rTmp.assign(stack[0].r);
                if (stack[0].isAbnormalOrComplex()) {
                    // Discontinuous or complex function
                    stack[0].makeNan();
                    progRunning = false;
                    Real.magicRounding = true;
                    setMessage("Min/max", "Discontinuous or complex function");
                    return;
                }

                if ((maximizing && rTmp.greaterEqual(y1)) ||
                    (!maximizing && rTmp.lessEqual(y1))) {
                    c.assign(b);
                    y2.assign(y1);
                    b.add(a);
                    b.scalbn(-1);
                    y1.assign(rTmp);
                } else {
                    a.add(b);
                    a.scalbn(-1);
                    y0.assign(rTmp);

                    stack[0].set(b);
                    stack[0].r.add(c);
                    stack[0].r.scalbn(-1);
                    executeProgram();
                    rTmp.assign(stack[0].r);
                    if (stack[0].isAbnormalOrComplex()) {
                        // Discontinuous or complex function
                        stack[0].makeNan();
                        progRunning = false;
                        Real.magicRounding = true;
                        setMessage("Min/max",
                                   "Discontinuous or complex function");
                        return;
                    }

                    if ((maximizing && rTmp.greaterEqual(y1)) ||
                        (!maximizing && rTmp.lessEqual(y1))) {
                        a.assign(b);
                        y0.assign(y1);
                        b.add(c);
                        b.scalbn(-1);
                        y1.assign(rTmp);
                    } else {
                        c.add(b);
                        c.scalbn(-1);
                        y2.assign(rTmp);
                    }
                }

                // Let stack always hold best value till now
                stack[0].set(b);
                Real.magicRounding = true;

                if (y0.equalTo(y1) && y1.equalTo(y2)) {
                    // We're done, it's flat
                    progRunning = false;
                    return;
                }
            }
        }
        while (System.currentTimeMillis()-progRunStart < 100);

        if (graphCmd == PROG_SOLVE || graphCmd == PROG_MINMAX) {
            // Draw progress
            rTmp.assign(a);
            rTmp.sub(b);
            int progress =
                Math.min(Math.max(0,Math.max(a.exponent,b.exponent)-
                                  rTmp.exponent),63);
            g.setColor(255,0,128);
            g.fillRect(gx,gy+gh/2-10,progress*gw/63,7);
            g.setColor(255,255,255);
            g.fillRect(gx+progress*(gw-1)/63,gy+gh/2-10,1,7);
        }
        else if (graphCmd == PROG_INTEGR) {
            if (integralN>0) {
                // Draw progress
                x.assign(Real.ONE);
                x.scalbn(-integralDepth);
                rTmp.assign(integralN);
                x.mul(rTmp);
                x.add(Real.ONE);
                xi = rangeScale(x,xMin,xMax,gw-1,Real.ZERO)+1;
                g.setColor(255,0,128);
                g.fillRect(gx,gy+gh/3-10,xi,7);
            }
        }
    }

    public void zoomGraph(Real factor, Real xOff, Real yOff) {
        xMax.sub(xMin);
        xOff.mul(xMax);
        xMax.mul(factor);
        xMin.add(xOff);
        xMax.add(xMin);
        yMax.sub(yMin);
        yOff.mul(yMax);
        yMax.mul(factor);
        yMin.add(yOff);
        yMax.add(yMin);
        // In case the program *doesn't* clutter the stack, update limits on
        // stack
        if (graphCmd >= PROG_DRAW && graphCmd <= PROG_DRAWZZ) {
            binary(CLEAR);
            binary(CLEAR);
            binary(CLEAR);
            binary(CLEAR);
            push(xMin,null);
            push(xMax,null);
            push(yMin,null);
            push(yMax,null);
        }
    }

    // This ends the inner class
    }
    // Create an inner class object to use from the outer
    CalcEngineInner inner = new CalcEngineInner();

    // Now for some convenience methods to access the inner class from the
    // outer
    public boolean prepareGraph(int cmd, int param) {
        return inner.prepareGraph(cmd,param);
    }
    public void drawAxes(Graphics g, int gx, int gy, int gw, int gh,
                         boolean bgrDisplay, boolean sparse) {
        inner.drawAxes(g,gx,gy,gw,gh,bgrDisplay,sparse);
    }
    public void startGraph(Graphics g, int gx, int gy, int gw, int gh,
                           boolean bgrDisplay) {
        inner.startGraph(g,gx,gy,gw,gh,bgrDisplay);
    }
    public void continueGraph(Graphics g, int gx, int gy, int gw, int gh) {
        inner.continueGraph(g,gx,gy,gw,gh);
    }
    public void zoomGraph(Real factor, Real xOff, Real yOff) {
        inner.zoomGraph(factor,xOff,yOff);
    }
}
