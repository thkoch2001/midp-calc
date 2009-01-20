package midpcalc;

class Menu
{
    public void setLabel(String l) {
    }

    public String getLabel() {
        return null;
    }

    public short getCommand() {
        return 0;
    }

    public void setFlags(short f) {
    }

    public short getFlags() {
        return 0;
    }

    public short getParam() {
        return 0;
    }
    
    public boolean hasSubMenu() {
        return false;
    }
    
    public void setSubMenu(int i, Menu m) {
    }

    public Menu getSubMenu(int i) {
        return null;
    }

    private static Menu command(int c) {
        return new MenuCommand(c);
    }

    private static Menu unit(String l) {
        return new MenuUnitCmd(l);
    }

    private static Menu command(String l, int c, int f) {
        return new MenuGeneralCmd(l, c, f);
    }

    private static Menu subMenu(String l, Menu m1, Menu m2, Menu m3) {
        return new SubMenu(l, 0, m1, m2, m3, null, null);
    }

    private static Menu subMenu(String l, Menu m1, Menu m2, Menu m3, Menu m4) {
        return new SubMenu(l, 0, m1, m2, m3, m4, null);
    }

    private static Menu subMenu(String l, Menu m1, Menu m2, Menu m3, Menu m4, Menu m5) {
        return new SubMenu(l, 0, m1, m2, m3, m4, m5);
    }

    private static Menu subMenu(String l, int f, Menu m1, Menu m2) {
        return new SubMenu(l, f, m1, m2, null, null, null);
    }

    private static Menu subMenu(String l, int f, Menu m1, Menu m2, Menu m3) {
        return new SubMenu(l, f, m1, m2, m3, null, null);
    }

    private static Menu subMenu(String l, int f, Menu m1, Menu m2, Menu m3, Menu m4) {
        return new SubMenu(l, f, m1, m2, m3, m4, null);
    }

    private static Menu subMenu(String l, int f, Menu m1, Menu m2, Menu m3, Menu m4, Menu m5) {
        return new SubMenu(l, f, m1, m2, m3, m4, m5);
    }

    static Menu basicMenu = subMenu("basic",
        command(CalcEngine.SUB),
        command(CalcEngine.MUL),
        command(CalcEngine.DIV),
        command(CalcEngine.NEG),
        null
    );

    static Menu enterMonitor = command(CalcEngine.MONITOR_ENTER);

    static Menu systemMenu = subMenu("sys",
        subMenu("font",
            command("number", CalcCanvas.NUMBER_FONT, CmdDesc.FONT_REQUIRED),
            command("menu", CalcCanvas.MENU_FONT, CmdDesc.FONT_REQUIRED),
            null,
            command("monitor", CalcCanvas.MONITOR_FONT, CmdDesc.FONT_REQUIRED)
        ),
        command("exit", CalcCanvas.EXIT, CmdDesc.NO_REPEAT),
        command("reset", CalcCanvas.RESET, CmdDesc.NO_REPEAT),
        command("fullscreen", CalcCanvas.FULLSCREEN, CmdDesc.NO_REPEAT),
        command(CalcEngine.VERSION)
        //command("free",CalcEngine.FREE_MEM,CmdDesc.NO_REPEAT)
    );

    static Menu menu = subMenu("menu",
        basicMenu,
        null, // math or binop
        null, // trig or binop2
        subMenu("special",
            subMenu("stack",
                command(CalcEngine.LASTX),
                command(CalcEngine.XCHG),
                command(CalcEngine.UNDO),
                subMenu("more", CmdDesc.TITLE_SKIP,
                    command(CalcEngine.RCLST),
                    command(CalcEngine.ROLLDN),
                    command(CalcEngine.ROLLUP),
                    command(CalcEngine.XCHGST),
                    subMenu("more", CmdDesc.TITLE_SKIP,
                        command(CalcEngine.ROLLUP_N),
                        null,
                        null,
                        command(CalcEngine.ROLLDN_N)
                    )
                ),
                command(CalcEngine.CLS)
            ),
            subMenu("mem",
                command(CalcEngine.STO),
                command(CalcEngine.RCL),
                command(CalcEngine.STP),
                command(CalcEngine.XCHGMEM),
                command(CalcEngine.CLMEM)
            ),
            subMenu("stat",
                command(CalcEngine.SUMPL),
                command(CalcEngine.SUMMI),
                subMenu("result", CmdDesc.TITLE_SKIP,
                    subMenu("average",
                        command(CalcEngine.AVG),
                        command(CalcEngine.STDEV),
                        command(CalcEngine.AVGXW),
                        command(CalcEngine.PSTDEV),
                        command(CalcEngine.AVG_DRAW)
                    ),
                    subMenu("ax+b",
                        command(CalcEngine.LIN_AB),
                        command(CalcEngine.LIN_YEST),
                        command(CalcEngine.LIN_XEST),
                        command(CalcEngine.LIN_R),
                        command(CalcEngine.LIN_DRAW)
                    ),
                    subMenu("a£x+b",
                        command(CalcEngine.LOG_AB),
                        command(CalcEngine.LOG_YEST),
                        command(CalcEngine.LOG_XEST),
                        command(CalcEngine.LOG_R),
                        command(CalcEngine.LOG_DRAW)
                    ),
                    subMenu("be^ax",
                        command(CalcEngine.EXP_AB),
                        command(CalcEngine.EXP_YEST),
                        command(CalcEngine.EXP_XEST),
                        command(CalcEngine.EXP_R),
                        command(CalcEngine.EXP_DRAW)
                    ),
                    subMenu("bx^a",
                        command(CalcEngine.POW_AB),
                        command(CalcEngine.POW_YEST),
                        command(CalcEngine.POW_XEST),
                        command(CalcEngine.POW_R),
                        command(CalcEngine.POW_DRAW)
                    )
                ),
                subMenu("sums",
                    command(CalcEngine.N),
                    subMenu("x", CmdDesc.TITLE_SKIP,
                        command(CalcEngine.SUMX),
                        command(CalcEngine.SUMXX),
                        command(CalcEngine.SUMLNX),
                        command(CalcEngine.SUMLN2X)
                    ),
                    subMenu("y", CmdDesc.TITLE_SKIP,
                        command(CalcEngine.SUMY),
                        command(CalcEngine.SUMYY),
                        command(CalcEngine.SUMLNY),
                        command(CalcEngine.SUMLN2Y)
                    ),
                    subMenu("xy", CmdDesc.TITLE_SKIP,
                        command(CalcEngine.SUMXY),
                        command(CalcEngine.SUMXLNY),
                        command(CalcEngine.SUMYLNX),
                        command(CalcEngine.SUMLNXLNY)
                    )
                ),
                command(CalcEngine.CLST)
            ),
            subMenu("finance",
                command(CalcEngine.FINANCE_STO),
                command(CalcEngine.FINANCE_RCL),
                command(CalcEngine.FINANCE_SOLVE),
                subMenu("more", CmdDesc.TITLE_SKIP,
                    command(CalcEngine.FINANCE_BGNEND),
                    command(CalcEngine.FINANCE_MULINT),
                    command(CalcEngine.FINANCE_DIVINT)
                ),
                command(CalcEngine.FINANCE_CLEAR)
            ),
            subMenu("conv",
                subMenu("time",
                    command(CalcEngine.TO_DHMS),
                    command(CalcEngine.TO_H),
                    command(CalcEngine.TIME_NOW),
                    command(CalcEngine.DHMS_PLUS),
                    subMenu("more", CmdDesc.TITLE_SKIP,
                        subMenu("unix", CmdDesc.TITLE_SKIP,
                            command(CalcEngine.DHMS_TO_UNIX),
                            null,
                            null,
                            command(CalcEngine.UNIX_TO_DHMS)
                        ),
                        subMenu("JD", CmdDesc.TITLE_SKIP,
                            command(CalcEngine.DHMS_TO_JD),
                            null,
                            null,
                            command(CalcEngine.JD_TO_DHMS)
                        ),
                        subMenu("MJD", CmdDesc.TITLE_SKIP,
                            command(CalcEngine.DHMS_TO_MJD),
                            null,
                            null,
                            command(CalcEngine.MJD_TO_DHMS)
                        ),
                        command(CalcEngine.TIME),
                        command(CalcEngine.DATE)
                    )
                ),
                subMenu("unit",
                    command(CalcEngine.UNIT_SET),
                    command(CalcEngine.UNIT_SET_INV),
                    command(CalcEngine.UNIT_CONVERT),
                    command(CalcEngine.UNIT_DESCRIBE),
                    command(CalcEngine.UNIT_CLEAR)
                ),
                subMenu("const",
                    subMenu("univ",
                        command(CalcEngine.CONST_c),
                        command(CalcEngine.CONST_h),
                        command(CalcEngine.CONST_mu_0),
                        command(CalcEngine.CONST_eps_0)
                    ),
                    subMenu("chem",
                        command(CalcEngine.CONST_NA),
                        command(CalcEngine.CONST_R),
                        command(CalcEngine.CONST_k),
                        command(CalcEngine.CONST_F)
                    ),
                    subMenu("phys",
                        command(CalcEngine.CONST_alpha),
                        command(CalcEngine.CONST_a_0),
                        command(CalcEngine.CONST_R_inf),
                        command(CalcEngine.CONST_mu_B)
                    ),
                    subMenu("atom",
                        command(CalcEngine.CONST_e),
                        command(CalcEngine.CONST_m_e),
                        command(CalcEngine.CONST_m_p),
                        command(CalcEngine.CONST_m_n),
                        command(CalcEngine.CONST_m_u)
                    ),
                    subMenu("astro",
                        command(CalcEngine.CONST_G),
                        command(CalcEngine.CONST_g_n),
                        command(CalcEngine.CONST_ly),
                        command(CalcEngine.CONST_AU),
                        command(CalcEngine.CONST_pc)
                    )
                ),
                command(CalcEngine.GUESS)
            )
        ),
        subMenu("mode",
            subMenu("number",
                command(CalcEngine.NORM),
                command(CalcEngine.FIX),
                command(CalcEngine.SCI),
                command(CalcEngine.ENG),
                subMenu("sepr",
                    subMenu("point",
                        command(CalcEngine.POINT_DOT),
                        command(CalcEngine.POINT_COMMA),
                        command(CalcEngine.POINT_KEEP),
                        command(CalcEngine.POINT_REMOVE)
                    ),
                    null,
                    null,
                    subMenu("thousand",
                        command(CalcEngine.THOUSAND_DOT),
                        command(CalcEngine.THOUSAND_SPACE),
                        command(CalcEngine.THOUSAND_QUOTE),
                        command(CalcEngine.THOUSAND_NONE)
                    )
                )
            ),
            null, // prog1 or prog2
            subMenu("base",
                command(CalcEngine.BASE_DEC),
                command(CalcEngine.BASE_HEX),
                command(CalcEngine.BASE_OCT),
                command(CalcEngine.BASE_BIN)
            ),
            subMenu("monitor",
                command(CalcEngine.MONITOR_FINANCE),
                command(CalcEngine.MONITOR_STAT),
                command(CalcEngine.MONITOR_MEM),
                command(CalcEngine.MONITOR_MATRIX),
                null // off or prog
            ),
            systemMenu
        )
    );

    static Menu monitorOffMenu = command(CalcEngine.MONITOR_NONE);

    static Menu monitorProgMenu = command(CalcEngine.MONITOR_PROG);

    static Menu numberMenu = subMenu(null,
        subMenu("<0-3>", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            command("<0>", CalcCanvas.NUMBER_0, CmdDesc.REPEAT_PARENT),
            command("<1>", CalcCanvas.NUMBER_1, CmdDesc.REPEAT_PARENT),
            command("<2>", CalcCanvas.NUMBER_2, CmdDesc.REPEAT_PARENT),
            command("<3>", CalcCanvas.NUMBER_3, CmdDesc.REPEAT_PARENT)
        ),
        subMenu("<4-7>", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            command("<4>", CalcCanvas.NUMBER_4, CmdDesc.REPEAT_PARENT),
            command("<5>", CalcCanvas.NUMBER_5, CmdDesc.REPEAT_PARENT),
            command("<6>", CalcCanvas.NUMBER_6, CmdDesc.REPEAT_PARENT),
            command("<7>", CalcCanvas.NUMBER_7, CmdDesc.REPEAT_PARENT)
        ),
        subMenu("<8-11>", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            command("<8>", CalcCanvas.NUMBER_8, CmdDesc.REPEAT_PARENT),
            command("<9>", CalcCanvas.NUMBER_9, CmdDesc.REPEAT_PARENT),
            command("<10>", CalcCanvas.NUMBER_10, CmdDesc.REPEAT_PARENT),
            command("<11>", CalcCanvas.NUMBER_11, CmdDesc.REPEAT_PARENT)
        ),
        subMenu("<12-15>", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            command("<12>", CalcCanvas.NUMBER_12, CmdDesc.REPEAT_PARENT),
            command("<13>", CalcCanvas.NUMBER_13, CmdDesc.REPEAT_PARENT),
            command("<14>", CalcCanvas.NUMBER_14, CmdDesc.REPEAT_PARENT),
            command("<15>", CalcCanvas.NUMBER_15, CmdDesc.REPEAT_PARENT)
        )
    );

    static Menu financeMenu = subMenu(null,
        command("pv", CalcCanvas.NUMBER_0, CmdDesc.REPEAT_PARENT),
        command("fv", CalcCanvas.NUMBER_1, CmdDesc.REPEAT_PARENT),
        command("np", CalcCanvas.NUMBER_2, CmdDesc.REPEAT_PARENT),
        command("pmt", CalcCanvas.NUMBER_3, CmdDesc.REPEAT_PARENT),
        command("ir%", CalcCanvas.NUMBER_4, CmdDesc.REPEAT_PARENT)
    );

    static Menu fontMenu = subMenu(null,
        command("medium", CalcCanvas.FONT_MEDIUM, CmdDesc.REPEAT_PARENT),
        command("small", CalcCanvas.FONT_SMALL, CmdDesc.REPEAT_PARENT),
        command("large", CalcCanvas.FONT_LARGE, CmdDesc.REPEAT_PARENT),
        command("xlarge", CalcCanvas.FONT_XLARGE, CmdDesc.REPEAT_PARENT),
        subMenu("more", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            command("xxlarge", CalcCanvas.FONT_XXLARGE, CmdDesc.REPEAT_PARENT),
            command("sys.S", CalcCanvas.FONT_SYS_SML, CmdDesc.REPEAT_PARENT),
            command("sys.L", CalcCanvas.FONT_SYS_LRG, CmdDesc.REPEAT_PARENT),
            command("xxxlarge", CalcCanvas.FONT_XXXLARGE, CmdDesc.REPEAT_PARENT),
            command("sys.M", CalcCanvas.FONT_SYS_MED, CmdDesc.REPEAT_PARENT)
        )
    );

    static Menu sysFontMenu = subMenu(null,
        command("medium", CalcCanvas.FONT_SYS_MED, CmdDesc.REPEAT_PARENT),
        command("small", CalcCanvas.FONT_SYS_SML, CmdDesc.REPEAT_PARENT),
        command("large", CalcCanvas.FONT_SYS_LRG, CmdDesc.REPEAT_PARENT)
    );

    static Menu intMenu = subMenu("int",
        command(CalcEngine.ROUND),
        command(CalcEngine.CEIL),
        command(CalcEngine.FLOOR),
        command(CalcEngine.TRUNC),
        command(CalcEngine.FRAC)
    );

    static Menu math = subMenu("math",
        subMenu("simple", CmdDesc.TITLE_SKIP,
            command(CalcEngine.SQRT),
            command(CalcEngine.SQR),
            command(CalcEngine.RECIP),
            command(CalcEngine.PERCENT_CHG),
            command(CalcEngine.PERCENT)
        ),
        subMenu("pow",
            command(CalcEngine.EXP),
            command(CalcEngine.YPOWX),
            command(CalcEngine.LN),
            command(CalcEngine.XRTY),
            subMenu("pow¸10,2", CmdDesc.TITLE_SKIP,
                command(CalcEngine.EXP2),
                command(CalcEngine.EXP10),
                command(CalcEngine.LOG2),
                command(CalcEngine.LOG10)
            )
        ),
        subMenu("misc",
            command(CalcEngine.RANDOM),
            command(CalcEngine.MOD),
            command(CalcEngine.DIVF),
            command(CalcEngine.FACTORIZE),
            intMenu
        ),
        subMenu("matrix",
            subMenu("create", CmdDesc.TITLE_SKIP,
                command(CalcEngine.MATRIX_NEW),
                command(CalcEngine.TO_ROW),
                command(CalcEngine.TO_COL),
                command(CalcEngine.TO_MATRIX)
            ),
            subMenu("parts", CmdDesc.TITLE_SKIP,
                command(CalcEngine.MATRIX_SPLIT),
                command(CalcEngine.MATRIX_ROW),
                command(CalcEngine.MATRIX_COL),
                command(CalcEngine.BREAK_MATRIX),
                command(CalcEngine.MATRIX_AIJ)
            ),
            subMenu("math", CmdDesc.TITLE_SKIP,
                command(CalcEngine.DETERM),
                command(CalcEngine.TRANSP),
                command(CalcEngine.TRACE),
                command(CalcEngine.TRANSP_CONJ),
                subMenu("more", CmdDesc.TITLE_SKIP,
                    command(CalcEngine.ABS),
                    command(CalcEngine.MATRIX_MAX),
                    command(CalcEngine.MATRIX_MIN),
                    command(CalcEngine.MATRIX_SIZE)
                )
            ),
            subMenu("combine", CmdDesc.TITLE_SKIP,
                command(CalcEngine.MATRIX_CONCAT),
                null,
                null,
                command(CalcEngine.MATRIX_STACK)
            )
        ),
        subMenu("prob",
            command(CalcEngine.PYX),
            command(CalcEngine.CYX),
            command(CalcEngine.FACT),
            command(CalcEngine.GAMMA),
            subMenu("more", CmdDesc.TITLE_SKIP,
                command(CalcEngine.INVERFC),
                command(CalcEngine.ERFC),
                command(CalcEngine.PHI),
                command(CalcEngine.INVPHI)
            )
        )
    );

    static Menu trig = subMenu("trig",
        subMenu("normal", CmdDesc.TITLE_SKIP,
            command(CalcEngine.SIN),
            command(CalcEngine.COS),
            command(CalcEngine.TAN)
        ),
        subMenu("arc", CmdDesc.TITLE_SKIP,
            command(CalcEngine.ASIN),
            command(CalcEngine.ACOS),
            command(CalcEngine.ATAN)
        ),
        subMenu("hyp", CmdDesc.TITLE_SKIP,
            command(CalcEngine.SINH),
            command(CalcEngine.COSH),
            command(CalcEngine.TANH)
        ),
        subMenu("archyp", CmdDesc.TITLE_SKIP,
            command(CalcEngine.ASINH),
            command(CalcEngine.ACOSH),
            command(CalcEngine.ATANH)
        ),
        subMenu("more", CmdDesc.TITLE_SKIP,
            command(CalcEngine.TRIG_DEGRAD),
            command(CalcEngine.TO_RAD),
            command(CalcEngine.TO_DEG),
            null, // coord or cplx
            command(CalcEngine.PI)
        )
    );

    static Menu coordMenu = subMenu("coord",
        command(CalcEngine.HYPOT),
        command(CalcEngine.RP),
        command(CalcEngine.PR),
        command(CalcEngine.ATAN2),
        command(CalcEngine.TO_CPLX)
    );

    static Menu cplxMenu = subMenu("cplx",
        command(CalcEngine.CPLX_SPLIT),
        command(CalcEngine.ABS),
        command(CalcEngine.CPLX_ARG),
        command(CalcEngine.CPLX_CONJ)
    );

    static Menu bitOp = subMenu("bitop",
        command(CalcEngine.AND),
        command(CalcEngine.OR),
        command(CalcEngine.BIC),
        command(CalcEngine.XOR)
    );

    static Menu bitMath = subMenu("bitop¸2",
        command(CalcEngine.NOT),
        command(CalcEngine.YUPX),
        command(CalcEngine.YDNX),
        intMenu
    );

    static Menu prog1 = subMenu("prog",
        command(CalcEngine.PROG_NEW),
        command(CalcEngine.PROG_RUN),
        subMenu("draw", CmdDesc.NO_PROG,
            command(CalcEngine.PROG_DRAW),
            command(CalcEngine.PROG_DRAWPOL),
            command(CalcEngine.PROG_DRAWPARM),
            command(CalcEngine.PROG_DRAWZZ)
        ),
        command(CalcEngine.PROG_APPEND),
        subMenu("more", CmdDesc.TITLE_SKIP,
            command(CalcEngine.PROG_INTEGR),
            command(CalcEngine.PROG_DIFF),
            command(CalcEngine.PROG_SOLVE),
            command(CalcEngine.PROG_MINMAX),
            command(CalcEngine.PROG_CLEAR)
        )
    );

    static Menu prog2 = subMenu("prog",
        command(CalcEngine.PROG_FINISH),
        subMenu("flow",
            subMenu("x?y", CmdDesc.TITLE_SKIP,
                command(CalcEngine.IF_EQUAL),
                command(CalcEngine.IF_NEQUAL),
                command(CalcEngine.IF_LESS),
                command(CalcEngine.IF_LEQUAL),
                command(CalcEngine.IF_GREATER)
            ),
            subMenu("label", CmdDesc.TITLE_SKIP,
                command(CalcEngine.LBL),
                command(CalcEngine.GTO),
                command(CalcEngine.GSB),
                command(CalcEngine.RTN)
            ),
            subMenu("loop", CmdDesc.TITLE_SKIP,
                command(CalcEngine.ISG),
                null,
                null,
                command(CalcEngine.DSE)
            ),
            subMenu("x?0", CmdDesc.TITLE_SKIP,
                command(CalcEngine.IF_EQUAL_Z),
                command(CalcEngine.IF_NEQUAL_Z),
                command(CalcEngine.IF_LESS_Z),
                command(CalcEngine.IF_LEQUAL_Z),
                command(CalcEngine.IF_GREATER_Z)
            ),
            command(CalcEngine.STOP)
        ),
        subMenu("util",
            command(CalcEngine.ABS),
            command(CalcEngine.MAX),
            command(CalcEngine.MIN),
            command(CalcEngine.SELECT),
            command(CalcEngine.SGN)
        ),
        command(CalcEngine.PROG_PURGE),
        subMenu("mem",
            command(CalcEngine.RCL_X),
            command(CalcEngine.STO_X),
            null,
            command(CalcEngine.STP_X)
        )
    );
    
    static Menu rowSizeMenu = subMenu(null,
        subMenu("[2-4]", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            null,
            command("[1x2]", CalcCanvas.NUMBER_2, CmdDesc.REPEAT_PARENT),
            command("[1x3]", CalcCanvas.NUMBER_3, CmdDesc.REPEAT_PARENT),
            command("[1x4]", CalcCanvas.NUMBER_4, CmdDesc.REPEAT_PARENT)
        ),
        subMenu("[5-8]", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            command("[1x5]", CalcCanvas.NUMBER_5, CmdDesc.REPEAT_PARENT),
            command("[1x6]", CalcCanvas.NUMBER_6, CmdDesc.REPEAT_PARENT),
            command("[1x7]", CalcCanvas.NUMBER_7, CmdDesc.REPEAT_PARENT),
            command("[1x8]", CalcCanvas.NUMBER_8, CmdDesc.REPEAT_PARENT)
        ),
        subMenu("[9-12]", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            command("[1x9]", CalcCanvas.NUMBER_9, CmdDesc.REPEAT_PARENT),
            command("[1x10]", CalcCanvas.NUMBER_10, CmdDesc.REPEAT_PARENT),
            command("[1x11]", CalcCanvas.NUMBER_11, CmdDesc.REPEAT_PARENT),
            command("[1x12]", CalcCanvas.NUMBER_12, CmdDesc.REPEAT_PARENT)
        ),
        subMenu("[13-16]", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            command("[1x13]", CalcCanvas.NUMBER_13, CmdDesc.REPEAT_PARENT),
            command("[1x14]", CalcCanvas.NUMBER_14, CmdDesc.REPEAT_PARENT),
            command("[1x15]", CalcCanvas.NUMBER_15, CmdDesc.REPEAT_PARENT),
            command("[1x16]", CalcCanvas.NUMBER_0+16, CmdDesc.REPEAT_PARENT)
        )
    );

    static Menu colSizeMenu = subMenu(null,
        subMenu("[2-4]", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            null,
            command("[2x1]", CalcCanvas.NUMBER_2, CmdDesc.REPEAT_PARENT),
            command("[3x1]", CalcCanvas.NUMBER_3, CmdDesc.REPEAT_PARENT),
            command("[4x1]", CalcCanvas.NUMBER_4, CmdDesc.REPEAT_PARENT)
        ),
        subMenu("[5-8]", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            command("[5x1]", CalcCanvas.NUMBER_5, CmdDesc.REPEAT_PARENT),
            command("[6x1]", CalcCanvas.NUMBER_6, CmdDesc.REPEAT_PARENT),
            command("[7x1]", CalcCanvas.NUMBER_7, CmdDesc.REPEAT_PARENT),
            command("[8x1]", CalcCanvas.NUMBER_8, CmdDesc.REPEAT_PARENT)
        ),
        subMenu("[9-12]", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            command("[9x1]", CalcCanvas.NUMBER_9, CmdDesc.REPEAT_PARENT),
            command("[10x1]", CalcCanvas.NUMBER_10, CmdDesc.REPEAT_PARENT),
            command("[11x1]", CalcCanvas.NUMBER_11, CmdDesc.REPEAT_PARENT),
            command("[12x1]", CalcCanvas.NUMBER_12, CmdDesc.REPEAT_PARENT)
        ),
        subMenu("[13-16]", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            command("[13x1]", CalcCanvas.NUMBER_13, CmdDesc.REPEAT_PARENT),
            command("[14x1]", CalcCanvas.NUMBER_14, CmdDesc.REPEAT_PARENT),
            command("[15x1]", CalcCanvas.NUMBER_15, CmdDesc.REPEAT_PARENT),
            command("[16x1]", CalcCanvas.NUMBER_0+16, CmdDesc.REPEAT_PARENT)
        )
    );

    static Menu matrixSizeMenu = subMenu(null,
        subMenu("[2-3]", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            command("[2x2]", CalcCanvas.NUMBER_0, CmdDesc.REPEAT_PARENT),
            command("[2x3]", CalcCanvas.NUMBER_1, CmdDesc.REPEAT_PARENT),
            command("[3x2]", CalcCanvas.NUMBER_2, CmdDesc.REPEAT_PARENT),
            command("[3x3]", CalcCanvas.NUMBER_3, CmdDesc.REPEAT_PARENT)
        ),
        subMenu("[4]", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            command("[2x4]", CalcCanvas.NUMBER_4, CmdDesc.REPEAT_PARENT),
            command("[3x4]", CalcCanvas.NUMBER_5, CmdDesc.REPEAT_PARENT),
            command("[4x3]", CalcCanvas.NUMBER_6, CmdDesc.REPEAT_PARENT),
            command("[4x2]", CalcCanvas.NUMBER_7, CmdDesc.REPEAT_PARENT),
            command("[4x4]", CalcCanvas.NUMBER_8, CmdDesc.REPEAT_PARENT)
        ),
        subMenu("[5]", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            command("[2x5]", CalcCanvas.NUMBER_9, CmdDesc.REPEAT_PARENT),
            command("[3x5]", CalcCanvas.NUMBER_10, CmdDesc.REPEAT_PARENT),
            command("[5x3]", CalcCanvas.NUMBER_11, CmdDesc.REPEAT_PARENT),
            command("[5x2]", CalcCanvas.NUMBER_12, CmdDesc.REPEAT_PARENT)
        ),
        subMenu("[6-7]", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            command("[2x6]", CalcCanvas.NUMBER_13, CmdDesc.REPEAT_PARENT),
            command("[2x7]", CalcCanvas.NUMBER_14, CmdDesc.REPEAT_PARENT),
            command("[7x2]", CalcCanvas.NUMBER_15, CmdDesc.REPEAT_PARENT),
            command("[6x2]", CalcCanvas.NUMBER_0+16, CmdDesc.REPEAT_PARENT)
        ),
        subMenu("[8]", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            command("[2x8]", CalcCanvas.NUMBER_0+17, CmdDesc.REPEAT_PARENT),
            null,
            null,
            command("[8x2]", CalcCanvas.NUMBER_0+18, CmdDesc.REPEAT_PARENT)
        )
    );

    static Menu progMenu = subMenu(null,
        command("", CalcCanvas.NUMBER_0, CmdDesc.REPEAT_PARENT),
        command("", CalcCanvas.NUMBER_1, CmdDesc.REPEAT_PARENT),
        command("", CalcCanvas.NUMBER_2, CmdDesc.REPEAT_PARENT),
        command("", CalcCanvas.NUMBER_3, CmdDesc.REPEAT_PARENT),
        subMenu("more", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            command("", CalcCanvas.NUMBER_4, CmdDesc.REPEAT_PARENT),
            command("", CalcCanvas.NUMBER_5, CmdDesc.REPEAT_PARENT),
            command("", CalcCanvas.NUMBER_6, CmdDesc.REPEAT_PARENT),
            command("", CalcCanvas.NUMBER_7, CmdDesc.REPEAT_PARENT),
            command("", CalcCanvas.NUMBER_8, CmdDesc.REPEAT_PARENT)
            // Remember to set CalcEngine.NUM_PROGS accordingly
        )
    );

    static Menu spatialUnitMenu = subMenu("spatial", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
        subMenu("length", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            unit("m"),
            subMenu("metric", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
                unit("mm"),
                unit("cm"),
                unit("km"),
                unit("Å")
            ),
            subMenu("US/Imp", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
                unit("in"),
                unit("ft"),
                unit("yd"),
                unit("mi")
            ),
            subMenu("other", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
                unit("NM"),
                unit("AU"),
                unit("ly"),
                unit("pc")
            )
        ),
        subMenu("area", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            unit("m²"),
            unit("a"),
            unit("da"),
            unit("ha"),
            subMenu("US/Imp", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
                unit("acre"),
                unit("in²"),
                unit("ft²"),
                unit("yd²"),
                unit("mi²")
            )
        ),
        subMenu("speed", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            unit("m/s"),
            unit("km/h"),
            unit("mph"),
            unit("knot"),
            unit("c")
        ),
        subMenu("volume", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            subMenu("metric", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
                unit("ml"),
                unit("cl"),
                unit("dl"),
                unit("l"),
                unit("m³")
            ),
            subMenu("U.S.", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
                unit("fl.oz"),
                unit("cup"),
                unit("pt"),
                unit("gal"),
                unit("in³")
            ),
            subMenu("Imp.", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
                unit("`fl.oz`"),
                unit("`cup`"),
                unit("`pt`"),
                unit("`gal`")
            )
        ),
        subMenu("accel", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            unit("m/s²"),
            unit("in/s²"),
            unit("ft/s²"),
            unit("mi/s²"),
            unit("`g`")
        )
    );

    static Menu otherUnitMenu = subMenu("other", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
        subMenu("time", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            unit("s"),
            unit("min"),
            unit("h"),
            unit("d"),
            unit("y")
        ),
        subMenu("temp", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            unit("°C"),
            unit("ð°C"),
            unit("ð°F"),
            unit("°F"),
            unit("K")
        ),
        subMenu("chem", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            unit("mol"),
            unit("e"),
            unit("eV")
        ),
        subMenu("electric", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            unit("V"),
            unit("C"),
            unit("A"),
            unit("Ø"),
            subMenu("more", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
                unit("F"),
                unit("H"),
                unit("T"),
                unit("Wb")
            )
        )
    );

    static Menu firmUnitMenu = subMenu("firm", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
        subMenu("mass", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            unit("kg"),
            subMenu("metric", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
                unit("g"),
                unit("t"),
                unit("u")
            ),
            subMenu("US/Imp", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
                unit("gr"),
                unit("oz"),
                unit("lb"),
                unit("st")
            ),
            subMenu("US/Imp", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
                unit("ton"),
                unit("`ton`")
            )
        ),
        subMenu("force", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            unit("N"),
            unit("pdl"),
            unit("kgf"),
            unit("lbf")
        ),
        subMenu("power", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            unit("W"),
            unit("kW"),
            unit("MW"),
            unit("hp")
        ),
        subMenu("pressure", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            unit("Pa"),
            unit("psi"),
            unit("bar"),
            unit("atm"),
            unit("mmHg")
        ),
        subMenu("energy", CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,
            unit("J"),
            unit("kJ"),
            unit("cal"),
            unit("kcal"),
            unit("Btu")
        )
    );

    static Menu unitMenu = subMenu(null,
        spatialUnitMenu,
        otherUnitMenu,
        firmUnitMenu
    );

    static Menu unitConvertMenu = subMenu(null,
        spatialUnitMenu,
        otherUnitMenu,
        firmUnitMenu,
        unit("US/Imp"),
        unit("SI")
    );
}