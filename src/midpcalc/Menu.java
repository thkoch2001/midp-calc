package midpcalc;

final class Menu
{
    public String label;
    public short command;
    public short flags;
    public short param;
    public Menu [] subMenu;

    // get Details from CmdStr class
    Menu(int c) {
        label = CmdDesc.getStr(c, true);
        command = (short)c;
        flags = CmdDesc.getFlags(c);
    }

    Menu(String l, int c) {
        label = l;
        command = (short)c;
    }

    Menu(String l, int c, int f) {
        label = l;
        command = (short)c;
        if (c == CalcCanvas.UNIT)
            param = findUnit(l);
        flags = (short)f;
    }

    Menu(String l, Menu [] m) {
        label = l;
        subMenu = m;
    }

    Menu(String l, int f, Menu [] m) {
        label = l;
        flags = (short)f;
        subMenu = m;
    }

    static short findUnit(String name) {
        for (int unitType=0; unitType<Unit.allUnits.length; unitType++)
            for (int unit=0; unit<Unit.allUnits[unitType].length; unit++)
                if (name.equals(Unit.allUnits[unitType][unit].name))
                    return (short)((unit<<8) + unitType);
        throw new IllegalStateException("Could not find unit");
    }

    static Menu basicMenu = new Menu("basic",new Menu[] {
        new Menu(CalcEngine.SUB),
        new Menu(CalcEngine.MUL),
        new Menu(CalcEngine.DIV),
        new Menu(CalcEngine.NEG),
        null,
    });

    static Menu enterMonitor = new Menu(CalcEngine.MONITOR_ENTER);

    static Menu systemMenu = new Menu("sys",new Menu[] {
        new Menu("font",new Menu[] {
            new Menu("number",CalcCanvas.NUMBER_FONT,CmdDesc.FONT_REQUIRED),
            new Menu("menu",CalcCanvas.MENU_FONT,CmdDesc.FONT_REQUIRED),
            null,
            new Menu("monitor",CalcCanvas.MONITOR_FONT,CmdDesc.FONT_REQUIRED),
        }),
        new Menu("exit",CalcCanvas.EXIT,CmdDesc.NO_REPEAT),
        new Menu("reset",CalcCanvas.RESET,CmdDesc.NO_REPEAT),
        new Menu("fullscreen",CalcCanvas.FULLSCREEN,CmdDesc.NO_REPEAT),
        //new Menu("free",CalcEngine.FREE_MEM,CmdDesc.NO_REPEAT),
    });

    static Menu menu = new Menu("menu",new Menu[] {
        basicMenu,
        null, // math or binop
        null, // trig or binop2
        new Menu("special",new Menu [] {
            new Menu("stack",new Menu[] {
                new Menu(CalcEngine.LASTX),
                new Menu(CalcEngine.XCHG),
                new Menu(CalcEngine.UNDO),
                new Menu("more",CmdDesc.TITLE_SKIP,new Menu [] {
                    new Menu(CalcEngine.RCLST),
                    new Menu(CalcEngine.ROLLDN),
                    new Menu(CalcEngine.ROLLUP),
                    new Menu(CalcEngine.XCHGST),
                }),
                new Menu(CalcEngine.CLS),
            }),
            new Menu("mem",new Menu[] {
                new Menu(CalcEngine.STO),
                new Menu(CalcEngine.RCL),
                new Menu(CalcEngine.STP),
                new Menu(CalcEngine.XCHGMEM),
                new Menu(CalcEngine.CLMEM),
            }),
            new Menu("stat",new Menu[] {
                new Menu(CalcEngine.SUMPL),
                new Menu(CalcEngine.SUMMI),
                new Menu("result",CmdDesc.TITLE_SKIP,new Menu[] {
                    new Menu("average",new Menu [] {
                        new Menu(CalcEngine.AVG),
                        new Menu(CalcEngine.STDEV),
                        new Menu(CalcEngine.AVGXW),
                        new Menu(CalcEngine.PSTDEV),
                        new Menu(CalcEngine.AVG_DRAW),
                    }),
                    new Menu("ax+b",new Menu[] {
                        new Menu(CalcEngine.LIN_AB),
                        new Menu(CalcEngine.LIN_YEST),
                        new Menu(CalcEngine.LIN_XEST),
                        new Menu(CalcEngine.LIN_R),
                        new Menu(CalcEngine.LIN_DRAW),
                    }),
                    new Menu("a£x+b",new Menu[] {
                        new Menu(CalcEngine.LOG_AB),
                        new Menu(CalcEngine.LOG_YEST),
                        new Menu(CalcEngine.LOG_XEST),
                        new Menu(CalcEngine.LOG_R),
                        new Menu(CalcEngine.LOG_DRAW),
                    }),
                    new Menu("be^ax",new Menu[] {
                        new Menu(CalcEngine.EXP_AB),
                        new Menu(CalcEngine.EXP_YEST),
                        new Menu(CalcEngine.EXP_XEST),
                        new Menu(CalcEngine.EXP_R),
                        new Menu(CalcEngine.EXP_DRAW),
                    }),
                    new Menu("bx^a",new Menu[] {
                        new Menu(CalcEngine.POW_AB),
                        new Menu(CalcEngine.POW_YEST),
                        new Menu(CalcEngine.POW_XEST),
                        new Menu(CalcEngine.POW_R),
                        new Menu(CalcEngine.POW_DRAW),
                    }),
                }),
                new Menu("sums",new Menu[] {
                    new Menu(CalcEngine.N),
                    new Menu("x",CmdDesc.TITLE_SKIP,new Menu[] {
                        new Menu(CalcEngine.SUMX),
                        new Menu(CalcEngine.SUMXX),
                        new Menu(CalcEngine.SUMLNX),
                        new Menu(CalcEngine.SUMLN2X),
                    }),
                    new Menu("y",CmdDesc.TITLE_SKIP,new Menu[] {
                        new Menu(CalcEngine.SUMY),
                        new Menu(CalcEngine.SUMYY),
                        new Menu(CalcEngine.SUMLNY),
                        new Menu(CalcEngine.SUMLN2Y),
                    }),
                    new Menu("xy",CmdDesc.TITLE_SKIP,new Menu[] {
                        new Menu(CalcEngine.SUMXY),
                        new Menu(CalcEngine.SUMXLNY),
                        new Menu(CalcEngine.SUMYLNX),
                        new Menu(CalcEngine.SUMLNXLNY),
                    }),
                }),
                new Menu(CalcEngine.CLST),
            }),
            new Menu("finance",new Menu [] {
                new Menu(CalcEngine.FINANCE_STO),
                new Menu(CalcEngine.FINANCE_RCL),
                new Menu(CalcEngine.FINANCE_SOLVE),
                new Menu("more",CmdDesc.TITLE_SKIP,new Menu [] {
                    new Menu(CalcEngine.FINANCE_BGNEND),
                    new Menu(CalcEngine.FINANCE_MULINT),
                    new Menu(CalcEngine.FINANCE_DIVINT),
                }),
                new Menu(CalcEngine.FINANCE_CLEAR),
            }),
            new Menu("conv",new Menu [] {
                new Menu("time",new Menu[] {
                    new Menu(CalcEngine.TO_DHMS),
                    new Menu(CalcEngine.TO_H),
                    new Menu(CalcEngine.TIME_NOW),
                    new Menu(CalcEngine.DHMS_PLUS),
                    new Menu("more",CmdDesc.TITLE_SKIP,new Menu [] {
                        new Menu("unix",CmdDesc.TITLE_SKIP,new Menu [] {
                            new Menu(CalcEngine.DHMS_TO_UNIX),
                            null,
                            null,
                            new Menu(CalcEngine.UNIX_TO_DHMS),
                        }),
                        new Menu("JD",CmdDesc.TITLE_SKIP,new Menu [] {
                            new Menu(CalcEngine.DHMS_TO_JD),
                            null,
                            null,
                            new Menu(CalcEngine.JD_TO_DHMS),
                        }),
                        new Menu("MJD",CmdDesc.TITLE_SKIP,new Menu [] {
                            new Menu(CalcEngine.DHMS_TO_MJD),
                            null,
                            null,
                            new Menu(CalcEngine.MJD_TO_DHMS),
                        }),
                        new Menu(CalcEngine.TIME),
                        new Menu(CalcEngine.DATE),
                    }),
                }),
                new Menu("unit",new Menu [] {
                    new Menu(CalcEngine.UNIT_SET),
                    new Menu(CalcEngine.UNIT_SET_INV),
                    new Menu(CalcEngine.UNIT_CONVERT),
                    new Menu(CalcEngine.UNIT_DESCRIBE),
                    new Menu(CalcEngine.UNIT_CLEAR),
                }),
                new Menu("const",new Menu [] {
                    new Menu("univ",new Menu [] {
                        new Menu(CalcEngine.CONST_c),
                        new Menu(CalcEngine.CONST_h),
                        new Menu(CalcEngine.CONST_mu_0),
                        new Menu(CalcEngine.CONST_eps_0),
                    }),
                    new Menu("chem",new Menu [] {
                        new Menu(CalcEngine.CONST_NA),
                        new Menu(CalcEngine.CONST_R),
                        new Menu(CalcEngine.CONST_k),
                        new Menu(CalcEngine.CONST_F),
                    }),
                    new Menu("phys",new Menu [] {
                        new Menu(CalcEngine.CONST_alpha),
                        new Menu(CalcEngine.CONST_a_0),
                        new Menu(CalcEngine.CONST_R_inf),
                        new Menu(CalcEngine.CONST_mu_B),
                    }),
                    new Menu("atom",new Menu [] {
                        new Menu(CalcEngine.CONST_e),
                        new Menu(CalcEngine.CONST_m_e),
                        new Menu(CalcEngine.CONST_m_p),
                        new Menu(CalcEngine.CONST_m_n),
                        new Menu(CalcEngine.CONST_m_u),
                    }),
                    new Menu("astro",new Menu [] {
                        new Menu(CalcEngine.CONST_G),
                        new Menu(CalcEngine.CONST_g_n),
                        new Menu(CalcEngine.CONST_ly),
                        new Menu(CalcEngine.CONST_AU),
                        new Menu(CalcEngine.CONST_pc),
                    }),
                }),
                new Menu(CalcEngine.GUESS),
            }),
        }),
        new Menu("mode",new Menu[] {
            new Menu("number",new Menu[] {
                new Menu(CalcEngine.NORM),
                new Menu(CalcEngine.FIX),
                new Menu(CalcEngine.SCI),
                new Menu(CalcEngine.ENG),
                new Menu("sepr",new Menu[] {
                    new Menu("point",new Menu[] {
                        new Menu(CalcEngine.POINT_DOT),
                        new Menu(CalcEngine.POINT_COMMA),
                        new Menu(CalcEngine.POINT_KEEP),
                        new Menu(CalcEngine.POINT_REMOVE),
                    }),
                    null,
                    null,
                    new Menu("thousand",new Menu[] {
                        new Menu(CalcEngine.THOUSAND_DOT),
                        new Menu(CalcEngine.THOUSAND_SPACE),
                        new Menu(CalcEngine.THOUSAND_QUOTE),
                        new Menu(CalcEngine.THOUSAND_NONE),
                    }),
                }),
            }),
            null, // prog1 or prog2
            new Menu("base",new Menu[] {
                new Menu(CalcEngine.BASE_DEC),
                new Menu(CalcEngine.BASE_HEX),
                new Menu(CalcEngine.BASE_OCT),
                new Menu(CalcEngine.BASE_BIN),
            }),
            new Menu("monitor",new Menu[] {
                new Menu(CalcEngine.MONITOR_FINANCE),
                new Menu(CalcEngine.MONITOR_STAT),
                new Menu(CalcEngine.MONITOR_MEM),
                new Menu(CalcEngine.MONITOR_MATRIX),
                null, // off or prog
            }),
            systemMenu,
        }),
    });

    static Menu monitorOffMenu = new Menu(CalcEngine.MONITOR_NONE);

    static Menu monitorProgMenu = new Menu(CalcEngine.MONITOR_PROG);

    static Menu numberMenu = new Menu(null,new Menu[] {
        new Menu("<0-3>",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,new Menu[] {
            new Menu("<0>",CalcCanvas.NUMBER_0,CmdDesc.REPEAT_PARENT),
            new Menu("<1>",CalcCanvas.NUMBER_1,CmdDesc.REPEAT_PARENT),
            new Menu("<2>",CalcCanvas.NUMBER_2,CmdDesc.REPEAT_PARENT),
            new Menu("<3>",CalcCanvas.NUMBER_3,CmdDesc.REPEAT_PARENT),
        }),
        new Menu("<4-7>",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,new Menu[] {
            new Menu("<4>",CalcCanvas.NUMBER_4,CmdDesc.REPEAT_PARENT),
            new Menu("<5>",CalcCanvas.NUMBER_5,CmdDesc.REPEAT_PARENT),
            new Menu("<6>",CalcCanvas.NUMBER_6,CmdDesc.REPEAT_PARENT),
            new Menu("<7>",CalcCanvas.NUMBER_7,CmdDesc.REPEAT_PARENT),
        }),
        new Menu("<8-11>",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,new Menu[] {
            new Menu("<8>",CalcCanvas.NUMBER_8,CmdDesc.REPEAT_PARENT),
            new Menu("<9>",CalcCanvas.NUMBER_9,CmdDesc.REPEAT_PARENT),
            new Menu("<10>",CalcCanvas.NUMBER_10,CmdDesc.REPEAT_PARENT),
            new Menu("<11>",CalcCanvas.NUMBER_11,CmdDesc.REPEAT_PARENT),
        }),
        new Menu("<12-15>",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,new Menu[]{
            new Menu("<12>",CalcCanvas.NUMBER_12,CmdDesc.REPEAT_PARENT),
            new Menu("<13>",CalcCanvas.NUMBER_13,CmdDesc.REPEAT_PARENT),
            new Menu("<14>",CalcCanvas.NUMBER_14,CmdDesc.REPEAT_PARENT),
            new Menu("<15>",CalcCanvas.NUMBER_15,CmdDesc.REPEAT_PARENT),
        }),
    });

    static Menu financeMenu = new Menu(null,new Menu[] {
        new Menu("pv" ,CalcCanvas.NUMBER_0,CmdDesc.REPEAT_PARENT),
        new Menu("fv" ,CalcCanvas.NUMBER_1,CmdDesc.REPEAT_PARENT),
        new Menu("np" ,CalcCanvas.NUMBER_2,CmdDesc.REPEAT_PARENT),
        new Menu("pmt",CalcCanvas.NUMBER_3,CmdDesc.REPEAT_PARENT),
        new Menu("ir%",CalcCanvas.NUMBER_4,CmdDesc.REPEAT_PARENT),
    });

    static Menu fontMenu = new Menu(null,new Menu[] {
        new Menu("medium",CalcCanvas.FONT_MEDIUM,CmdDesc.REPEAT_PARENT),
        new Menu("small",CalcCanvas.FONT_SMALL,CmdDesc.REPEAT_PARENT),
        new Menu("large",CalcCanvas.FONT_LARGE,CmdDesc.REPEAT_PARENT),
        new Menu("xlarge",CalcCanvas.FONT_XLARGE,CmdDesc.REPEAT_PARENT),
        new Menu("more",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,new Menu [] {
            new Menu("xxlarge",CalcCanvas.FONT_XXLARGE,CmdDesc.REPEAT_PARENT),
            new Menu("sys.S",CalcCanvas.FONT_SYS_SML,CmdDesc.REPEAT_PARENT),
            new Menu("sys.L",CalcCanvas.FONT_SYS_LRG,CmdDesc.REPEAT_PARENT),
            new Menu("xxxlarge",CalcCanvas.FONT_XXXLARGE,CmdDesc.REPEAT_PARENT),
            new Menu("sys.M",CalcCanvas.FONT_SYS_MED,CmdDesc.REPEAT_PARENT),
        }),
    });

    static Menu sysFontMenu = new Menu(null,new Menu[] {
        new Menu("medium",CalcCanvas.FONT_SYS_MED,CmdDesc.REPEAT_PARENT),
        new Menu("small",CalcCanvas.FONT_SYS_SML,CmdDesc.REPEAT_PARENT),
        new Menu("large",CalcCanvas.FONT_SYS_LRG,CmdDesc.REPEAT_PARENT),
    });

    static Menu intMenu = new Menu("int",new Menu[] {
        new Menu(CalcEngine.ROUND),
        new Menu(CalcEngine.CEIL),
        new Menu(CalcEngine.FLOOR),
        new Menu(CalcEngine.TRUNC),
        new Menu(CalcEngine.FRAC),
    });

    static Menu math = new Menu("math",new Menu[] {
        new Menu("simple",CmdDesc.TITLE_SKIP,new Menu[] {
            new Menu(CalcEngine.SQRT),
            new Menu(CalcEngine.SQR),
            new Menu(CalcEngine.RECIP),
            new Menu(CalcEngine.PERCENT_CHG),
            new Menu(CalcEngine.PERCENT),
        }),
        new Menu("pow",new Menu[] {
            new Menu(CalcEngine.EXP),
            new Menu(CalcEngine.YPOWX),
            new Menu(CalcEngine.LN),
            new Menu(CalcEngine.XRTY),
            new Menu("pow¸10,2",CmdDesc.TITLE_SKIP,new Menu[] {
                new Menu(CalcEngine.EXP2),
                new Menu(CalcEngine.EXP10),
                new Menu(CalcEngine.LOG2),
                new Menu(CalcEngine.LOG10),
            }),
        }),
        new Menu("misc",new Menu[] {
            new Menu(CalcEngine.RANDOM),
            new Menu(CalcEngine.MOD),
            new Menu(CalcEngine.DIVF),
            new Menu(CalcEngine.FACTORIZE),
            intMenu,
        }),
        new Menu("matrix",new Menu[] {
            new Menu(CalcEngine.MATRIX_NEW),
            new Menu(CalcEngine.MATRIX_STACK),
            new Menu(CalcEngine.MATRIX_SPLIT),
            new Menu(CalcEngine.MATRIX_CONCAT),
            new Menu("more",CmdDesc.TITLE_SKIP,new Menu[] {
                new Menu(CalcEngine.DETERM),
                new Menu(CalcEngine.TRANSP),
                new Menu(CalcEngine.TRACE),
                new Menu(CalcEngine.TRANSP_CONJ),
                new Menu("more",CmdDesc.TITLE_SKIP,new Menu[] {
                    new Menu(CalcEngine.ABS),
                    new Menu(CalcEngine.MATRIX_SIZE),
                    new Menu(CalcEngine.MATRIX_ROW),
                    new Menu(CalcEngine.MATRIX_COL),
                    new Menu("more",CmdDesc.TITLE_SKIP,new Menu[] {
                        new Menu(CalcEngine.MATRIX_AIJ),
                        new Menu(CalcEngine.MATRIX_MAX),
                        new Menu(CalcEngine.MATRIX_MIN),
                    }),
                }),
            }),
        }),
        new Menu("prob",new Menu[] {
            new Menu(CalcEngine.PYX),
            new Menu(CalcEngine.CYX),
            new Menu(CalcEngine.FACT),
            new Menu(CalcEngine.GAMMA),
            new Menu("more",CmdDesc.TITLE_SKIP,new Menu[] {
                new Menu(CalcEngine.INVERFC),
                new Menu(CalcEngine.ERFC),
                new Menu(CalcEngine.PHI),
                new Menu(CalcEngine.INVPHI),
            }),
        }),
    });

    static Menu trig = new Menu("trig",new Menu[] {
        new Menu("normal",CmdDesc.TITLE_SKIP,new Menu[] {
            new Menu(CalcEngine.SIN),
            new Menu(CalcEngine.COS),
            new Menu(CalcEngine.TAN),
        }),
        new Menu("arc",CmdDesc.TITLE_SKIP,new Menu[] {
            new Menu(CalcEngine.ASIN),
            new Menu(CalcEngine.ACOS),
            new Menu(CalcEngine.ATAN),
        }),
        new Menu("hyp",CmdDesc.TITLE_SKIP,new Menu[] {
            new Menu(CalcEngine.SINH),
            new Menu(CalcEngine.COSH),
            new Menu(CalcEngine.TANH),
        }),
        new Menu("archyp",CmdDesc.TITLE_SKIP,new Menu[] {
            new Menu(CalcEngine.ASINH),
            new Menu(CalcEngine.ACOSH),
            new Menu(CalcEngine.ATANH),
        }),
        new Menu("more",CmdDesc.TITLE_SKIP,new Menu[] {
            new Menu(CalcEngine.TRIG_DEGRAD),
            new Menu(CalcEngine.TO_RAD),
            new Menu(CalcEngine.TO_DEG),
            null, // coord or cplx
            new Menu(CalcEngine.PI),
        }),
    });

    static Menu coordMenu = new Menu("coord",new Menu[] {
        new Menu(CalcEngine.HYPOT),
        new Menu(CalcEngine.RP),
        new Menu(CalcEngine.PR),
        new Menu(CalcEngine.ATAN2),
        new Menu(CalcEngine.TO_CPLX),
    });

    static Menu cplxMenu = new Menu("cplx",new Menu[] {
        new Menu(CalcEngine.CPLX_SPLIT),
        new Menu(CalcEngine.ABS),
        new Menu(CalcEngine.CPLX_ARG),
        new Menu(CalcEngine.CPLX_CONJ),
    });

    static Menu bitOp = new Menu("bitop",new Menu[] {
        new Menu(CalcEngine.AND),
        new Menu(CalcEngine.OR),
        new Menu(CalcEngine.BIC),
        new Menu(CalcEngine.XOR),
    });

    static Menu bitMath = new Menu("bitop¸2",new Menu[] {
        new Menu(CalcEngine.NOT),
        new Menu(CalcEngine.YUPX),
        new Menu(CalcEngine.YDNX),
        intMenu,
    });

    static Menu prog1 = new Menu("prog",new Menu[] {
        new Menu(CalcEngine.PROG_NEW),
        new Menu(CalcEngine.PROG_RUN),
        new Menu("draw",CmdDesc.NO_PROG,new Menu[] {
            new Menu(CalcEngine.PROG_DRAW),
            new Menu(CalcEngine.PROG_DRAWPOL),
            new Menu(CalcEngine.PROG_DRAWPARM),
            new Menu(CalcEngine.PROG_DRAWZZ),
        }),
        new Menu(CalcEngine.PROG_APPEND),
        new Menu("more",CmdDesc.TITLE_SKIP,new Menu[] {
            new Menu(CalcEngine.PROG_INTEGR),
            new Menu(CalcEngine.PROG_DIFF),
            new Menu(CalcEngine.PROG_SOLVE),
            new Menu(CalcEngine.PROG_MINMAX),
            new Menu(CalcEngine.PROG_CLEAR),
        }),
    });

    static Menu prog2 = new Menu("prog",new Menu[] {
        new Menu(CalcEngine.PROG_FINISH),
        new Menu("flow",new Menu[] {
            new Menu("x?y",CmdDesc.TITLE_SKIP,new Menu[] {
                new Menu(CalcEngine.IF_EQUAL),
                new Menu(CalcEngine.IF_NEQUAL),
                new Menu(CalcEngine.IF_LESS),
                new Menu(CalcEngine.IF_LEQUAL),
                new Menu(CalcEngine.IF_GREATER),
            }),
            new Menu("label",CmdDesc.TITLE_SKIP,new Menu[] {
                new Menu(CalcEngine.LBL),
                new Menu(CalcEngine.GTO),
                new Menu(CalcEngine.GSB),
                new Menu(CalcEngine.RTN),
            }),
            new Menu("loop",CmdDesc.TITLE_SKIP,new Menu[] {
                new Menu(CalcEngine.ISG),
                null,
                null,
                new Menu(CalcEngine.DSE),
            }),
            new Menu("x?0",CmdDesc.TITLE_SKIP,new Menu[] {
                new Menu(CalcEngine.IF_EQUAL_Z),
                new Menu(CalcEngine.IF_NEQUAL_Z),
                new Menu(CalcEngine.IF_LESS_Z),
                new Menu(CalcEngine.IF_LEQUAL_Z),
                new Menu(CalcEngine.IF_GREATER_Z),
            }),
            new Menu(CalcEngine.STOP),
        }),
        new Menu("util",new Menu[] {
            new Menu(CalcEngine.ABS),
            new Menu(CalcEngine.MAX),
            new Menu(CalcEngine.MIN),
            new Menu(CalcEngine.SELECT),
            new Menu(CalcEngine.SGN),
        }),
        new Menu(CalcEngine.PROG_PURGE),
        new Menu("mem",new Menu[] {
            new Menu(CalcEngine.RCL_X),
            new Menu(CalcEngine.STO_X),
            null,
            new Menu(CalcEngine.STP_X),
        }),
    });

    static Menu progMenu = new Menu(null,new Menu[] {
        new Menu("",CalcCanvas.NUMBER_0,CmdDesc.REPEAT_PARENT),
        new Menu("",CalcCanvas.NUMBER_1,CmdDesc.REPEAT_PARENT),
        new Menu("",CalcCanvas.NUMBER_2,CmdDesc.REPEAT_PARENT),
        new Menu("",CalcCanvas.NUMBER_3,CmdDesc.REPEAT_PARENT),
        new Menu("more",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT,new Menu[] {
            new Menu("",CalcCanvas.NUMBER_4,CmdDesc.REPEAT_PARENT),
            new Menu("",CalcCanvas.NUMBER_5,CmdDesc.REPEAT_PARENT),
            new Menu("",CalcCanvas.NUMBER_6,CmdDesc.REPEAT_PARENT),
            new Menu("",CalcCanvas.NUMBER_7,CmdDesc.REPEAT_PARENT),
            new Menu("",CalcCanvas.NUMBER_8,CmdDesc.REPEAT_PARENT),
            // Remember to set CalcEngine.NUM_PROGS accordingly
        }),
    });

    static Menu spatialUnitMenu = new Menu("spatial",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT, new Menu[] {
        new Menu("length",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT, new Menu[] {
            new Menu("m",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("metric",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT, new Menu[] {
                new Menu("mm",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("cm",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("km",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("Å",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            }),
            new Menu("US/Imp",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT, new Menu[] {
                new Menu("in",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("ft",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("yd",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("mi",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            }),
            new Menu("other",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT, new Menu[] {
                new Menu("NM",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("AU",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("ly",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("pc",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            }),
        }),
        new Menu("area",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT, new Menu[] {
            new Menu("m²",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("a",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("da",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("ha",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("US/Imp",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT, new Menu[] {
                new Menu("acre",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("in²",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("ft²",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("yd²",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("mi²",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            }),
        }),
        new Menu("speed",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT, new Menu[] {
            new Menu("m/s",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("km/h",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("mph",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("knot",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("c",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
        }),
        new Menu("volume",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT, new Menu[] {
            new Menu("metric",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT, new Menu[] {
                new Menu("ml",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("cl",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("dl",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("l",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("m³",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            }),
            new Menu("U.S.",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT, new Menu[] {
                new Menu("fl.oz",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("cup",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("pt",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("gal",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("in³",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            }),
            new Menu("Imp.",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT, new Menu[] {
                new Menu("`fl.oz`",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("`cup`",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("`pt`",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("`gal`",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            }),
        }),
        new Menu("accel",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT, new Menu[] {
            new Menu("m/s²",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("in/s²",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("ft/s²",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("mi/s²",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("`g`",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
        }),
    });

    static Menu otherUnitMenu = new Menu("other",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT, new Menu[] {
        new Menu("time",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT, new Menu[] {
            new Menu("s",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("min",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("h",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("d",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("y",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
        }),
        new Menu("temp",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT, new Menu[] {
            new Menu("°C",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("ð°C",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("ð°F",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("°F",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("K",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
        }),
        new Menu("chem",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT, new Menu[] {
            new Menu("mol",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("e",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("eV",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
        }),
        new Menu("electric",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT, new Menu[] {
            new Menu("V",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("C",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("A",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("Ø",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("more",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT, new Menu[] {
                new Menu("F",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("H",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("T",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("Wb",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            }),
        }),
    });

    static Menu firmUnitMenu = new Menu("firm",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT, new Menu[] {
        new Menu("mass",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT, new Menu[] {
            new Menu("kg",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("metric",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT, new Menu[] {
                new Menu("g",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("t",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("u",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            }),
            new Menu("US/Imp",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT, new Menu[] {
                new Menu("gr",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("oz",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("lb",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("st",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            }),
            new Menu("US/Imp",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT, new Menu[] {
                new Menu("ton",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
                new Menu("`ton`",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            }),
        }),
        new Menu("force",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT, new Menu[] {
            new Menu("N",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("pdl",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("kgf",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("lbf",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
        }),
        new Menu("power",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT, new Menu[] {
            new Menu("W",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("kW",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("MW",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("hp",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
        }),
        new Menu("pressure",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT, new Menu[] {
            new Menu("Pa",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("psi",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("bar",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("atm",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("mmHg",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
        }),
        new Menu("energy",CmdDesc.TITLE_SKIP|CmdDesc.REPEAT_PARENT, new Menu[] {
            new Menu("J",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("kJ",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("cal",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("kcal",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
            new Menu("Btu",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
        }),
    });

    static Menu unitMenu = new Menu(null,new Menu[] {
        spatialUnitMenu,
        otherUnitMenu,
        firmUnitMenu,
    });

    static Menu unitConvertMenu = new Menu(null,new Menu[] {
        spatialUnitMenu,
        otherUnitMenu,
        firmUnitMenu,
        new Menu("US/Imp",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
        new Menu("SI",CalcCanvas.UNIT,CmdDesc.REPEAT_PARENT),
    });
}