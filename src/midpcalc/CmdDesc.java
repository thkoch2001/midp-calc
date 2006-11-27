package midpcalc;

// contains the description for each (non-system) command:
// - brief string for menu
// - long string for monitor
// - flags for menu

public final class CmdDesc
{
  
    public static String getStr( int id, boolean brief ) {
        switch( id ) {
            case CalcEngine.DIGIT_0: return "0";
            case CalcEngine.DIGIT_1: return "1";
            case CalcEngine.DIGIT_2: return "2"; 
            case CalcEngine.DIGIT_3: return "3";      
            case CalcEngine.DIGIT_4: return "4";
            case CalcEngine.DIGIT_5: return "5";      
            case CalcEngine.DIGIT_6: return "6";      
            case CalcEngine.DIGIT_7: return "7";      
            case CalcEngine.DIGIT_8: return "8";      
            case CalcEngine.DIGIT_9: return "9";      
            case CalcEngine.DIGIT_A: return "A"; 
            case CalcEngine.DIGIT_B: return "B";      
            case CalcEngine.DIGIT_C: return "C";      
            case CalcEngine.DIGIT_D: return "D";      
            case CalcEngine.DIGIT_E: return "E";      
            case CalcEngine.DIGIT_F: return "F";
            case CalcEngine.SIGN_E:return "-/E";
            case CalcEngine.DEC_POINT:return ".";
            case CalcEngine.ENTER:return "ENTER";
            case CalcEngine.CLEAR:return "clear";
            case CalcEngine.ADD:return "+";
            case CalcEngine.SUB:return "-";
            case CalcEngine.MUL:return "*";
            case CalcEngine.DIV:return "/";
            case CalcEngine.NEG:return "+/-";
            case CalcEngine.RECIP:return "1/x";
            case CalcEngine.SQR:return "x²";
            case CalcEngine.SQRT:return "¿x";
            case CalcEngine.PERCENT:return "%";
            case CalcEngine.PERCENT_CHG:return "%chg";
            case CalcEngine.YPOWX:return brief ? "y^x": "math/pow/y^x";
            case CalcEngine.XRTY:return brief ? "^x^¿y": "math/pow/x¿y";
            case CalcEngine.LN:return brief ? "ln": "math/pow/£";
            case CalcEngine.EXP:return brief ? "e^x": "math/pow/e^x";
            case CalcEngine.LOG10:return brief ? "log_10": "math/pow/log_10";
            case CalcEngine.EXP10:return brief ? "10^x": "math/pow/10^x";
            case CalcEngine.LOG2:return brief ? "log_2": "math/pow/log_2";
            case CalcEngine.EXP2:return brief ? "2^x": "math/pow/2^x";
            case CalcEngine.PYX:return brief ? "P y,x": "math/prob/P y,x";
            case CalcEngine.CYX:return brief ? "C y,x": "math/prob/C y,x";
            case CalcEngine.FACT:return brief ? "x!": "math/prob/x!";
            case CalcEngine.GAMMA:return brief ? "¡x": "math/prob/¡x";
            case CalcEngine.RP:return brief ? "r­p": "trig/coord/r­p";
            case CalcEngine.PR:return brief ? "p­r": "trig/coord/p­r";
            case CalcEngine.ATAN2:return brief ? "atan_2": "trig/coord/atan_2";
            case CalcEngine.HYPOT:return brief ? "hypot": "trig/coord/hypot";
            case CalcEngine.SIN:return brief ? "sin": "trig/sin";
            case CalcEngine.COS:return brief ? "cos": "trig/cos";
            case CalcEngine.TAN:return brief ? "tan": "trig/tan";
            case CalcEngine.ASIN:return brief ? "asin": "trig/asin";
            case CalcEngine.ACOS:return brief ? "acos": "trig/acos";
            case CalcEngine.ATAN:return brief ? "atan": "trig/atan";
            case CalcEngine.SINH:return brief ? "sinh": "trig/sinh";
            case CalcEngine.COSH:return brief ? "cosh": "trig/cosh";
            case CalcEngine.TANH:return brief ? "tanh": "trig/tanh";
            case CalcEngine.ASINH:return brief ? "asinh": "trig/asinh";
            case CalcEngine.ACOSH:return brief ? "acosh": "trig/acosh";
            case CalcEngine.ATANH:return brief ? "atanh": "trig/atanh";
            case CalcEngine.PI:return brief ? "¶": "trig/¶";
            case CalcEngine.AND:return brief ? "and" : "bitop/and";
            case CalcEngine.OR:return brief ? "or" : "bitop/or";
            case CalcEngine.XOR:return brief ? "xor" : "bitop/xor";
            case CalcEngine.BIC:return brief ? "bic" : "bitop/bic";
            case CalcEngine.NOT:return brief ? "not" : "bitop/not";
            case CalcEngine.YUPX:return brief ? "y<<x" : "bitop/y<<x";
            case CalcEngine.YDNX:return brief ? "y>>x" : "bitop/y>>x";
            case CalcEngine.XCHG:return brief ? "x«y": "stack/x«y";
            case CalcEngine.CLS:return brief ? "clear": "stack/clear";
            case CalcEngine.RCLST:return brief ? "RCL st#": "stack/RCL st#";
            case CalcEngine.LASTX:return brief ? "LAST x": "stack/LAST x";
            case CalcEngine.UNDO:return brief ? "undo": "stack/undo";
            case CalcEngine.ROUND:return brief ? "round": "int/round";
            case CalcEngine.CEIL:return brief ? "ceil": "int/ceil";
            case CalcEngine.FLOOR:return brief ? "floor": "int/floor";
            case CalcEngine.TRUNC:return brief ? "trunc": "int/trunc";
            case CalcEngine.FRAC:return brief ? "frac": "int/frac";
            case CalcEngine.STO:return brief ? "STO": "mem/STO";
            case CalcEngine.STP:return brief ? "STO+": "mem/STO+";
            case CalcEngine.RCL:return brief ? "RCL": "mem/RCL";
            case CalcEngine.XCHGMEM:return brief ? "x«mem": "mem/x«mem";
            case CalcEngine.CLMEM:return brief ? "clear": "mem/clear";
            case CalcEngine.SUMPL:return brief ? "ß+" : "stat/ß+";
            case CalcEngine.SUMMI:return brief ? "ß-" : "stat/ß-";
            case CalcEngine.CLST:return brief ? "clear" : "stat/clear";
            case CalcEngine.AVG:return brief ? "~x~, ~y~" : "stat/res/avg/x,y";
            case CalcEngine.AVGXW:return brief ? "~x~w" : "stat/res/avg/xw";
            case CalcEngine.STDEV:return brief ? "s_x_, s_y_" : "stat/res/avg/sx,sy";
            case CalcEngine.PSTDEV:return brief ? "S_x_, S_y_" : "stat/res/avg/Sx,Sy";
            case CalcEngine.LIN_AB:return brief ? "a,b" : "stat/ax+b/a,b";
            case CalcEngine.LIN_YEST:return brief ? "y^*" : "stat/ax+b/y^*";
            case CalcEngine.LIN_XEST:return brief ? "x^*" : "stat/ax+b/x^*";
            case CalcEngine.LIN_R:return brief ? "r" : "stat/ax+b/r";
            case CalcEngine.LOG_AB:return brief ? "a,b" : "stat/a£x+b/a,b";
            case CalcEngine.LOG_YEST:return brief ? "y^*" : "stat/a£x+b/y^*";
            case CalcEngine.LOG_XEST:return brief ? "x^*" : "stat/a£x+b/x^*";
            case CalcEngine.LOG_R:return brief ? "r" : "stat/a£x+b/r";
            case CalcEngine.EXP_AB:return brief ? "a,b" : "stat/be^ax/a,b";
            case CalcEngine.EXP_YEST:return brief ? "y^*" : "stat/be^ax/y^*";
            case CalcEngine.EXP_XEST:return brief ? "x^*" : "stat/be^ax/x^*";
            case CalcEngine.EXP_R:return brief ? "r" : "stat/be^ax/r";
            case CalcEngine.POW_AB:return brief ? "a,b" : "stat/bx^a/a,b";
            case CalcEngine.POW_YEST:return brief ? "y^*" : "stat/bx^a/y^*";
            case CalcEngine.POW_XEST:return brief ? "x^*" : "stat/bx^a/x^*";
            case CalcEngine.POW_R:return brief ? "r" : "stat/bx^a/r";
            case CalcEngine.N:return brief ? "n" : "stat/sums/n";
            case CalcEngine.SUMX:return brief ? "ßx" : "stat/sums/ßx";
            case CalcEngine.SUMXX:return brief ? "ßx^2" : "stat/sums/ßx²";
            case CalcEngine.SUMLNX:return brief ? "ßlnx" : "stat/sums/ß£x";
            case CalcEngine.SUMLN2X:return brief ? "ßln^2^x" : "stat/sums/ß£²x";
            case CalcEngine.SUMY:return brief ? "ßy" : "stat/sums/ßy";
            case CalcEngine.SUMYY:return brief ? "ßy^2" : "stat/sums/ßy²";
            case CalcEngine.SUMLNY:return brief ? "ßlny" : "stat/sums/ß£y";
            case CalcEngine.SUMLN2Y:return brief ? "ßln^2^y" : "stat/sums/ß£²y";
            case CalcEngine.SUMXY:return brief ? "ßxy" : "stat/sums/ßxy";
            case CalcEngine.SUMXLNY:return brief ? "ßxlny" : "stat/sums/ßx£y";
            case CalcEngine.SUMYLNX:return brief ? "ßylnx" : "stat/sums/ßy£x";
            case CalcEngine.SUMLNXLNY:return brief ? "ßlnxlny" : "stat/sums/ß£x£y";
            case CalcEngine.NORM:return brief ? "normal" : "number/normal";
            case CalcEngine.FIX:return brief ? "FIX" : "number/FIX";
            case CalcEngine.SCI:return brief ? "SCI" : "number/SCI";
            case CalcEngine.ENG:return brief ? "ENG" : "number/ENG";
            case CalcEngine.POINT_DOT:return brief ? "." : "number/pnt/.";
            case CalcEngine.POINT_COMMA:return brief ? "," : "number/pnt/,";
            case CalcEngine.POINT_REMOVE:return brief ? "remove" : "number/pnt/remove";
            case CalcEngine.POINT_KEEP:return brief ? "keep" : "number/pnt/keep";
            case CalcEngine.THOUSAND_DOT:return brief ? ". or ," : "number/ths/. or ,";
            case CalcEngine.THOUSAND_SPACE:return brief ? "space" : "number/ths/space";
            case CalcEngine.THOUSAND_QUOTE:return brief ? "'" : "number/ths/'";
            case CalcEngine.THOUSAND_NONE:return brief ? "none" : "number/ths/none";
            case CalcEngine.BASE_BIN:return brief ? "BIN" : "base/BIN";
            case CalcEngine.BASE_OCT:return brief ? "OCT" : "base/OCT";
            case CalcEngine.BASE_DEC:return brief ? "DEC" : "base/DEC";
            case CalcEngine.BASE_HEX:return brief ? "HEX" : "base/HEX";
            case CalcEngine.TRIG_DEGRAD:return brief ? "RAD/DEG" : "trig/RAD«DEG";
            case CalcEngine.TO_DEG:return brief ? "­DEG" : "trig/­DEG";
            case CalcEngine.TO_RAD:return brief ? "­RAD" : "trig/­RAD";
            case CalcEngine.RANDOM:return brief ? "random" : "math/random";
            case CalcEngine.TO_DHMS:return brief ? "­DH.MS" : "conv/time/­DH.MS";
            case CalcEngine.TO_H:return brief ? "­H" : "conv/time/­H";
            case CalcEngine.DHMS_PLUS:return brief ? "DH.MS+": "conv/time/DH.MS+";
            case CalcEngine.TIME:return brief ? "time" : "conv/time/time";
            case CalcEngine.DATE:return brief ? "date" : "conv/time/date";
            case CalcEngine.FACTORIZE:return brief ? "factorize" : "math/factorize";
            case CalcEngine.FINANCE_STO:return brief ? "STO" : "finance/STO";
            case CalcEngine.FINANCE_RCL:return brief ? "RCL" : "finance/RCL";
            case CalcEngine.FINANCE_SOLVE:return brief ? "solve" : "finance/solve";
            case CalcEngine.FINANCE_CLEAR:return brief ? "clear" : "finance/clear";
            case CalcEngine.FINANCE_BGNEND:return brief ? "END/BGN" : "finance/END«BGN";
            case CalcEngine.FINANCE_MULINT:return brief ? "y%*x" : "finance/y%*x";
            case CalcEngine.FINANCE_DIVINT:return brief ? "y%/x" : "finance/y%/x";
            case CalcEngine.SIGN_POINT_E:return "-/./E"; 
            case CalcEngine.TO_CPLX:return brief ? "r­cplx" : "trig/coord/r­cplx";
            case CalcEngine.CPLX_SPLIT:return brief ? "cplx­r" : "trig/complex/cplx­r";
            case CalcEngine.ABS:return brief ? "abs" : "abs";
            case CalcEngine.CPLX_ARG:return brief ? "arg" : "trig/complex/arg";
            case CalcEngine.CPLX_CONJ:return brief ? "conj" : "trig/complex/conj";
            case CalcEngine.ERFC:return brief ? "erfc" : "math/prob/erfc";
            case CalcEngine.MOD:return brief ? "mod" : "math/mod";
            case CalcEngine.DIVF:return brief ? "div" : "math/div";
            case CalcEngine.XCHGST:return brief ? "x«st#" : "stack/x«st#";
            case CalcEngine.ROLLDN:return brief ? "rolldn" : "stack/rolldn";
            case CalcEngine.ROLLUP:return brief ? "rollup" : "stack/rollup";
            case CalcEngine.CONST_c:return brief ? "c" : "const/univ/c";
            case CalcEngine.CONST_h:return brief ? "h" : "const/univ/h";
            case CalcEngine.CONST_mu_0:return brief ? "µ_0" : "const/univ/µ0";
            case CalcEngine.CONST_eps_0:return brief ? "ë_0" : "const/univ/ë_0";
            case CalcEngine.CONST_NA:return brief ? "N_A" : "const/chem/N_A";
            case CalcEngine.CONST_R:return brief ? "R" : "const/chem/R";
            case CalcEngine.CONST_k:return brief ? "k" : "const/chem/k";
            case CalcEngine.CONST_F:return brief ? "F" : "const/chem/F";
            case CalcEngine.CONST_alpha:return brief ? "ã" : "const/phys/ã";
            case CalcEngine.CONST_a_0:return brief ? "a_0" : "const/phys/a_0";
            case CalcEngine.CONST_R_inf:return brief ? "R_Þ" : "const/phys/RÞ";
            case CalcEngine.CONST_mu_B:return brief ? "µ_B" : "const/phys/µ_B";
            case CalcEngine.CONST_e:return brief ? "e" : "const/atom/e";
            case CalcEngine.CONST_m_e:return brief ? "m_e" : "const/atom/m_e";
            case CalcEngine.CONST_m_p:return brief ? "m_p" : "const/atom/m_p";
            case CalcEngine.CONST_m_n:return brief ? "m_n" : "const/atom/m_n";
            case CalcEngine.CONST_m_u:return brief ? "m_u" : "const/atom/m_u";
            case CalcEngine.CONST_G:return brief ? "G" : "const/astro/G";
            case CalcEngine.CONST_g_n:return brief ? "g_n" : "const/astro/g_n";
            case CalcEngine.CONST_ly:return brief ? "l.y." : "const/astro/l.y.";
            case CalcEngine.CONST_AU:return brief ? "A.U." : "const/astro/A.U.";
            case CalcEngine.CONST_pc:return brief ? "pc" : "const/astro/pc";
            case CalcEngine.CONST_km_mi:return brief ? "mi/km" : "conv/length/mi/km";
            case CalcEngine.CONST_m_ft:return brief ? "ft/m" : "conv/length/ft/m";
            case CalcEngine.CONST_cm_in:return brief ? "in/cm" : "conv/length/in/cm";
            case CalcEngine.CONST_km_nm:return brief ? "n.m./km" : "conv/length/n.m./km";
            case CalcEngine.CONST_m_yd:return brief ? "yd/m" : "conv/length/yd/m";
            case CalcEngine.CONST_g_oz:return brief ? "oz/g" : "conv/weight/oz/g";
            case CalcEngine.CONST_kg_lb:return brief ? "lb/kg" : "conv/weight/lb/kg";
            case CalcEngine.CONST_mg_gr:return brief ? "gr/mg" : "conv/weight/gr/mg";
            case CalcEngine.CONST_kg_ton:return brief ? "ton/kg" : "conv/weight/ton/kg";
            case CalcEngine.CONST_J_cal:return brief ? "cal/J" : "conv/energy/cal/J";
            case CalcEngine.CONST_J_Btu:return brief ? "Btu/J" : "conv/energy/Btu/J";
            case CalcEngine.CONST_W_hp:return brief ? "hp/W" : "conv/energy/hp/W";
            case CalcEngine.CONST_l_pt:return brief ? "pt/l" : "conv/vol/pt/l";
            case CalcEngine.CONST_l_cup:return brief ? "cup/l" : "conv/vol/cup/l";
            case CalcEngine.CONST_l_gal:return brief ? "gal/l" : "conv/vol/gal/l";
            case CalcEngine.CONST_ml_floz:return brief ? "fl.oz/ml" : "conv/vol/fl.oz/ml";
            case CalcEngine.CONST_K_C:return brief ? "°K-°C" : "conv/temp/°K-°C";
            case CalcEngine.CONV_C_F:return brief ? "°C­°F" : "conv/temp/°C­°F";
            case CalcEngine.CONV_F_C:return brief ? "°F­°C" : "conv/temp/°F­°C";
            case CalcEngine.IF_EQUAL:return brief ? "x=y?" : "prog/flow/x=y?";
            case CalcEngine.IF_NEQUAL:return brief ? "x!=y?" : "prog/flow/x!=y?";
            case CalcEngine.IF_LESS:return brief ? "x<y?" : "prog/flow/x<y?";
            case CalcEngine.IF_LEQUAL:return brief ? "x<=y?" : "prog/flow/x<=y?";
            case CalcEngine.IF_GREATER:return brief ? "x>y?" : "prog/flow/x>y?";
            case CalcEngine.MIN:return brief ? "min" : "prog/util/min";
            case CalcEngine.MAX:return brief ? "max" : "prog/util/max";
            case CalcEngine.SELECT:return brief ? "select" : "prog/util/select";
            case CalcEngine.RCL_X:return brief ? "RCL[x]" : "prog/mem/RCL[x]";
            case CalcEngine.STO_X:return brief ? "STO[x]" : "prog/mem/STO[x]";
            case CalcEngine.STP_X:return brief ? "STO+[x]" : "prog/mem/STO+[x]";
            case CalcEngine.TIME_NOW:return brief ? "now" : "conv/time/now";
            case CalcEngine.DHMS_TO_UNIX:return brief ? "DH.MS­unix": "conv/t/u/DH.MS­unix";
            case CalcEngine.UNIX_TO_DHMS:return brief ? "unix­DH.MS" : "conv/t/u/unix­DH.MS";
            case CalcEngine.DHMS_TO_JD:return brief ? "DH.MS­JD" : "conv/time/JD/DH.MS­JD";
            case CalcEngine.JD_TO_DHMS:return brief ? "JD­DH.MS" : "conv/time/JD/JD­DH.MS";
            case CalcEngine.DHMS_TO_MJD:return brief ? "DH.MS­MJD" : "conv/time/MJD/DH.MS­MJD";
            case CalcEngine.MJD_TO_DHMS:return brief ? "MJD­DH.MS" : "conv/time/MJD/MJD­DH.MS";
            case CalcEngine.SGN:return brief ? "sgn" : "prog/util/sgn";
            case CalcEngine.PUSH_INT:return "";
            case CalcEngine.PUSH_INT_N:return "-";
            case CalcEngine.PUSH_INF:return "inf";
            case CalcEngine.PUSH_INF_N:return "-inf";
            case CalcEngine.PROG_NEW:return brief ? "new" : "prog/new";
            case CalcEngine.PROG_FINISH:return brief ? "finish" : "prog/finish";
            case CalcEngine.PROG_RUN:return brief ? "run" : "prog/run";
            case CalcEngine.PROG_PURGE:return brief ? "reset" : "prog/reset";
            case CalcEngine.PROG_CLEAR:return brief ? "clear" : "prog/clear";
            case CalcEngine.PROG_DIFF:return brief ? "diff." : "prog/diff.";
            case CalcEngine.TRANSP:return brief ? "A^T" : "matrix/transp";
            case CalcEngine.DETERM:return brief ? "det" : "matrix/det";
            case CalcEngine.TRACE:return brief ? "Tr" : "matrix/Tr";
            case CalcEngine.MATRIX_NEW:return brief ? "new" : "matrix/new";
            case CalcEngine.MATRIX_CONCAT:return brief ? "concat" : "matrix/concat";
            case CalcEngine.MATRIX_STACK:return brief ? "stack" : "matrix/stack";
            case CalcEngine.MATRIX_SPLIT:return brief ? "split" : "matrix/split";
            case CalcEngine.MONITOR_NONE:return brief ? "off" : "monitor/off";
            case CalcEngine.MONITOR_MEM:return brief ? "mem" : "monitor/mem";
            case CalcEngine.MONITOR_STAT:return brief ? "stat" : "monitor/stat";
            case CalcEngine.MONITOR_FINANCE:return brief ? "finance" : "monitor/finance";
            case CalcEngine.MONITOR_MATRIX:return brief ? "matrix" : "monitor/matrix";
            case CalcEngine.MONITOR_ENTER:return brief ? "[­]" : "[­]";
            case CalcEngine.MONITOR_EXIT:return brief ? "exit" : "monitor exit";
            case CalcEngine.MONITOR_UP:return brief ? "up" : "monitor up";
            case CalcEngine.MONITOR_DOWN:return brief ? "down" : "monitor down";
            case CalcEngine.MONITOR_LEFT:return brief ? "left" : "monitor left";
            case CalcEngine.MONITOR_RIGHT:return brief ? "right" : "monitor right";
            case CalcEngine.MONITOR_PUSH:return brief ? "push" : "monitor push";
            case CalcEngine.MONITOR_PUT:return brief ? "put" : "monitor put";
            case CalcEngine.MONITOR_GET:return brief ? "get" : "monitor get";
            case CalcEngine.STAT_RCL:return "RCL";
            case CalcEngine.STAT_STO:return "STO";
            case CalcEngine.MATRIX_SIZE:return brief ? "size" : "matrix/size";
            case CalcEngine.MATRIX_AIJ:return brief ? "a_ij" : "matrix/a_ij";
            case CalcEngine.TRANSP_CONJ:return brief ? "~A~^T" : "matrix/transp conj";
            case CalcEngine.GUESS:return brief ? "guess" : "conv/guess";
            case CalcEngine.INVERFC:return brief ? "erfc^-1" : "math/prob/erfc^-1";
            case CalcEngine.PHI:return brief ? "phi" : "math/prob/phi";
            case CalcEngine.INVPHI:return brief ? "phi^-1" : "math/prob/phi^-1";
            case CalcEngine.MATRIX_STO: return "STO";
            case CalcEngine.MATRIX_RCL: return "RCL";
            case CalcEngine.AVG_DRAW:return brief ? "draw" : "stat/res/avg/draw";
            case CalcEngine.LIN_DRAW:return brief ? "draw" : "stat/ax+b/draw";
            case CalcEngine.LOG_DRAW:return brief ? "draw" : "stat/a£x+b/draw";
            case CalcEngine.EXP_DRAW:return brief ? "draw" : "stat/be^ax/draw";
            case CalcEngine.POW_DRAW:return brief ? "draw" : "stat/bx^a/draw";
            case CalcEngine.PROG_DRAW:return brief ? "y=f(x)" : "prog/draw/y=f(x)";
            case CalcEngine.PROG_DRAWPOL:return brief ? "r=f(Ð)" : "prog/draw/r=f(Ð)";
            case CalcEngine.PROG_DRAWPARM:return brief ? "z=f(t)" : "prog/draw/z=f(t)";
            case CalcEngine.PROG_DRAWZZ:return brief ? "z=f(z)" : "prog/draw/z=f(z)";
            case CalcEngine.PROG_SOLVE:return brief ? "solve" : "prog/solve";
            case CalcEngine.PROG_INTEGR:return brief ? "integrate" : "prog/integrate";
            case CalcEngine.PROG_MINMAX:return brief ? "min/max" : "prog/min/max";
            case CalcEngine.MONITOR_PROG:return brief ? "prog" : "monitor/prog";
            case CalcEngine.PROG_APPEND:return brief ? "append" : "prog/append";
            case CalcEngine.IF_EQUAL_Z:return brief ? "x=0?" : "prog/flow/x=0?";
            case CalcEngine.IF_NEQUAL_Z:return brief ? "x!=0?" : "prog/flow/x!=0?";
            case CalcEngine.IF_LESS_Z:return brief ? "x<0?" : "prog/flow/x<0?";
            case CalcEngine.IF_LEQUAL_Z:return brief ? "x<=0?" : "prog/flow/x<=0?";
            case CalcEngine.IF_GREATER_Z:return brief ? "x>0?" : "prog/flow/x>0?";
            case CalcEngine.LBL:return brief ? "LBL" : "prog/flow/LBL";
            case CalcEngine.GTO:return brief ? "GTO" : "prog/flow/GTO";
            case CalcEngine.GSB:return brief ? "GSB" : "prog/flow/GSB";
            case CalcEngine.RTN:return brief ? "RTN" : "prog/flow/RTN";
            case CalcEngine.STOP:return brief ? "STOP" : "prog/flow/STOP";
            case CalcEngine.DSE:return brief ? "DSE" : "prog/flow/DSE";
            case CalcEngine.ISG:return brief ? "ISG" : "prog/flow/ISG";
            default:
                return "[nop]";
        }
    }
  
    // Flags for menu entries, some are not commands
    // listed in this class but top menus etc.
    public static final byte NUMBER_REQUIRED = 1;
    public static final byte FINANCE_REQUIRED = 2;
    public static final byte PROG_REQUIRED = 4;
    public static final byte SUBMENU_REQUIRED = 7;
    public static final byte TITLE_SKIP = 8;
    public static final byte NO_REPEAT = 16;
    public static final byte REPEAT_PARENT = 32;
    public static final byte NO_PROG = 64;
  

    public static byte getFlags( int id ) {
        switch (id) {
            case CalcEngine.DIGIT_0:
            case CalcEngine.DIGIT_1:
            case CalcEngine.DIGIT_2:
            case CalcEngine.DIGIT_3:
            case CalcEngine.DIGIT_4:
            case CalcEngine.DIGIT_5:
            case CalcEngine.DIGIT_6:
            case CalcEngine.DIGIT_7:
            case CalcEngine.DIGIT_8:
            case CalcEngine.DIGIT_9:
            case CalcEngine.DIGIT_A:
            case CalcEngine.DIGIT_B:
            case CalcEngine.DIGIT_C:
            case CalcEngine.DIGIT_D:
            case CalcEngine.DIGIT_E:
            case CalcEngine.DIGIT_F:
            case CalcEngine.SIGN_E:
            case CalcEngine.DEC_POINT:
            case CalcEngine.ENTER:
            case CalcEngine.CLEAR:
            case CalcEngine.ADD:
            case CalcEngine.SUB:
            case CalcEngine.MUL:
            case CalcEngine.DIV:
            case CalcEngine.NEG:
            case CalcEngine.CLS:
            case CalcEngine.CLMEM:
            case CalcEngine.CLST:
            case CalcEngine.FINANCE_CLEAR:
            case CalcEngine.PROG_FINISH:
            case CalcEngine.PROG_PURGE:
            case CalcEngine.MONITOR_ENTER:
                return NO_REPEAT;

            case CalcEngine.RCLST:
            case CalcEngine.STO:
            case CalcEngine.STP:
            case CalcEngine.RCL:
            case CalcEngine.XCHGMEM:
            case CalcEngine.FIX:
            case CalcEngine.SCI:
            case CalcEngine.ENG:
            case CalcEngine.XCHGST:
            case CalcEngine.LBL:
            case CalcEngine.GTO:
            case CalcEngine.GSB:
            case CalcEngine.DSE:
            case CalcEngine.ISG:
                return NUMBER_REQUIRED;

            case CalcEngine.POINT_DOT:
            case CalcEngine.POINT_COMMA:
            case CalcEngine.POINT_REMOVE:
            case CalcEngine.POINT_KEEP:
            case CalcEngine.THOUSAND_DOT:
            case CalcEngine.THOUSAND_SPACE:
            case CalcEngine.THOUSAND_QUOTE:
            case CalcEngine.THOUSAND_NONE:
            case CalcEngine.BASE_BIN:
            case CalcEngine.BASE_OCT:
            case CalcEngine.BASE_DEC:
            case CalcEngine.BASE_HEX:
            case CalcEngine.MONITOR_NONE:
            case CalcEngine.MONITOR_FINANCE:
                return REPEAT_PARENT;

            case CalcEngine.FINANCE_STO:
            case CalcEngine.FINANCE_RCL:
            case CalcEngine.FINANCE_SOLVE:
                return FINANCE_REQUIRED;

            case CalcEngine.PROG_NEW:
            case CalcEngine.PROG_CLEAR:
            case CalcEngine.PROG_APPEND:
                return PROG_REQUIRED|NO_REPEAT;

            case CalcEngine.PROG_RUN:
            case CalcEngine.PROG_DIFF:
            case CalcEngine.PROG_SOLVE:
            case CalcEngine.PROG_INTEGR:
            case CalcEngine.PROG_MINMAX:
                return PROG_REQUIRED|NO_PROG;
      
            case CalcEngine.MONITOR_PROG:
                return NUMBER_REQUIRED|NO_REPEAT;

            case CalcEngine.PROG_DRAW:
            case CalcEngine.PROG_DRAWPOL:
            case CalcEngine.PROG_DRAWPARM:
            case CalcEngine.PROG_DRAWZZ:
                return PROG_REQUIRED|REPEAT_PARENT;

            case CalcEngine.MONITOR_MEM:
            case CalcEngine.MONITOR_STAT:
            case CalcEngine.MONITOR_MATRIX:
                return NUMBER_REQUIRED|REPEAT_PARENT;    
 
            default:
                return 0;
        }
    }
}
