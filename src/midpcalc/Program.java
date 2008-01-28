package midpcalc;

public class Program implements Monitorable {

    public static final String emptyProg = "< >";
    public static final int progStartLength = 10;

    private final CalcEngine engine;

    private String[] progLabels;
    private short[][] prog;
    private int progSize;
    private int currentProg;
    private int numProgSteps;
    private short[] progStepAddr; // Always numProgSteps+1 valid entries

    private Real.NumberFormat format;
    private MonitorCache cache = new MonitorCache();
    private String caption;
    private Real rTmp = new Real();
    
    public Program(int numProgs, CalcEngine engine, Real.NumberFormat format) {
        this.engine = engine;
        setFormat(format);

        prog = new short[numProgs][];
        progLabels = new String[numProgs];
        for (int i=0; i<numProgs; i++)
            progLabels[i] = emptyProg;
    }
    
    public short[] getProg(int i) {
        return prog[i];
    }
    
    public void setProg(int i, short[] prog, String progLabel) {
        this.prog[i] = prog;
        this.progLabels[i] = progLabel;
    }
    
    public String getProgLabel(int i) {
        return progLabels[i];
    }
    
    public void clear(int i) {
        prog[i] = null;
        progLabels[i] = emptyProg;
    }

    public boolean isEmpty() {
        return prog[currentProg] == null;
    }
    
    public boolean isEmpty(int i) {
        return prog[i] == null;
    }
    
    public void newProgram(int i, String progLabel) {
        prog[i] = null;
        progLabels[i] = progLabel;
    }

    public void prepareProg(int progNo, boolean forEdit) {
        currentProg = progNo;
        caption = "Prog: " + progLabels[currentProg];
        cache.clearElementsFromRow(0);
        if (prog[currentProg] == null || prog[currentProg].length == 0) {
            if (forEdit) {
                prog[currentProg] = new short[progStartLength];
            }
            progSize = 0;
        } else {
            progSize = prog[currentProg].length;
        }
        progStepAddr = new short[progSize+1]; // Will be enough
        numProgSteps = 0;
        for (int i=0; i<progSize; i++, numProgSteps++) {
            progStepAddr[numProgSteps] = (short)i;
            if ((prog[currentProg][i] & 0x8000) != 0) 
                i += 5;
        }
        progStepAddr[numProgSteps] = (short)progSize;
    }
    
    public void purge() {
        progSize = 0;                   // delete program
        numProgSteps = 0;
        cache.clearElementsFromRow(0);
    }
    
    public int currentProg() {
        return currentProg;
    }
    
    public int numProgSteps() {
        return numProgSteps;
    }
    
    public void trim() {
        short [] prog2 = new short[progSize];
        System.arraycopy(prog[currentProg],0,prog2,0,progSize);
        prog[currentProg] = prog2;
    }
    
    public void cleanup() {
        currentProg = -1;
        progStepAddr = null;
    }

    private void progInsert(int step, int stepSize) {
        if (progSize+stepSize > prog[currentProg].length) {
            int newProgLength = Math.max(prog[currentProg].length*2, progSize+stepSize+progStartLength);
            short[] prog2 = new short[newProgLength];
            System.arraycopy(prog[currentProg],0,prog2,0,progSize);
            prog[currentProg] = prog2;
        }
        
        if (numProgSteps+1+1 > progStepAddr.length) {
            int newProgStepAddrLength = Math.max(progStepAddr.length*2, numProgSteps+1+1);
            short[] progStepAddr2 = new short[newProgStepAddrLength];
            System.arraycopy(progStepAddr,0,progStepAddr2,0,numProgSteps+1);
            progStepAddr = progStepAddr2;
        }
        
        int progAddr = progStepAddr[step];
        System.arraycopy(prog[currentProg], progAddr, prog[currentProg],
                         progAddr+stepSize, progSize-progAddr);
        for (int i=numProgSteps+1; i>step; i--) {
            progStepAddr[i] = (short)(progStepAddr[i-1]+stepSize);
        }
        // ...and progStepAddr[step] = progAddr
        numProgSteps ++;
        progSize += stepSize;

        cache.clearElementsFromRow(step);
    }
    
    /* 
     * Command packing:
     *
     * Normal command:
     * +-+---------+-+-----------------+
     * |0|p p p p p|0|c c c c c c c c c|     p = param, c = cmd
     * +-+---------+-+-----------------+
     * 15        10 9                 0
     * 
     * Matrix STO, RCL:
     * +-+-+-------+-+-+---+-----------+
     * |0|0|r r r r|1|x|r r|c c c c c c|     r = row, c = col, x = STO, RCL
     * +-+-+-------+-+-+---+-----------+
     * 15 14     10 9 8   6           0
     * 
     * Unit *, /, -> :
     * +-+-+-------+-+---+-+-----------+
     * |0|1|u u u u|1|x x|u|t t t t t t|     u = unit, t = type, x = *, /, ->
     * +-+-+-------+-+---+-+-----------+
     * 15 14     10 9   7 6           0
     * 
     * Real number n:
     * +-+-----------------------------+     +-------------------------------+
     * |1|n n n n n n n n n n n n n n n| +5* |n n n n n n n n n n n n n n n n|
     * +-+-----------------------------+     +-------------------------------+
     * 15                             0      15                             0
     * 
     */
    
    static class DecodedCommand {
        int cmd;
        int param;             // For normal commands
        int row, col;          // For Matrix commands
        int unitType, unit;    // For Unit commands
    }
    private DecodedCommand decoded = new DecodedCommand();
    
    private static int encodeCommand(int cmd, int param) {
        return cmd + (param<<10);
    }
    
    private static void decodeCommand(int cmd, DecodedCommand decoded) {
        decoded.cmd = cmd & 0x3ff;
        decoded.param = cmd>>>10;
    }

    private static int encodeMatrixCmd(int cmd, int row, int col) {
        return cmd + ((row&0x3)<<6) + ((row&0x3c)<<8) + col;
    }
    
    private static void decodeMatrixCmd(int cmd, DecodedCommand decoded) {
        decoded.cmd = cmd & (CalcEngine.MATRIX_STO|CalcEngine.MATRIX_RCL);
        decoded.row = ((cmd>>6)&0x3) + ((cmd>>8)&0x3c);
        decoded.col = cmd & 0x3f;
    }

    private static int encodeUnitCmd(int cmd, int unitType, int unit) {
        return cmd + unitType + ((unit&0x1)<<6) + ((unit&0x1e)<<9);
    }

    private static void decodeUnitCmd(int cmd, DecodedCommand decoded) {
        decoded.cmd = cmd & (CalcEngine.UNIT_SET|CalcEngine.UNIT_SET_INV|CalcEngine.UNIT_CONVERT);
        decoded.unitType = cmd & 0x3f;
        decoded.unit = ((cmd>>6)&0x1) + ((cmd>>9)&0x1e);
        // Precautions in case of old program
        if (decoded.unitType > Unit.allUnits.length)
            decoded.unitType = 0;
        if (decoded.unit > Unit.allUnits[decoded.unitType].length)
            decoded.unit = 0;
    }

    private void encodeProgReal(Real r, int addr) {
        prog[currentProg][addr++] = (short)(r.mantissa >> 47);
        prog[currentProg][addr++] = (short)(r.mantissa >> 31);
        prog[currentProg][addr++] = (short)(r.mantissa >> 15);
        prog[currentProg][addr++] = (short)((r.mantissa << 1)+r.sign);
        prog[currentProg][addr++] = (short)(r.exponent >> 16);
        prog[currentProg][addr  ] = (short)(r.exponent);
    }

    private void decodeProgReal(Real r, int addr) {
        if (addr+5 >= prog[currentProg].length) { // Just a precaution
            r.makeZero();
            return;
        }
        r.mantissa = (((long)(prog[currentProg][addr  ] & 0xffff) << 47)+
                      ((long)(prog[currentProg][addr+1] & 0xffff) << 31)+
                      ((long)(prog[currentProg][addr+2] & 0xffff) << 15)+
                      ((long)(prog[currentProg][addr+3] & 0xffff) >> 1));
        r.sign     = (byte)(prog[currentProg][addr+3] & 1);
        r.exponent = (((prog[currentProg][addr+4] & 0xffff) << 16)+
                      ((prog[currentProg][addr+5] & 0xffff)));
    }

    public boolean isLabel(int step, int labelNo) {
        int cmd = prog[currentProg][progStepAddr[step]];
        if ((cmd & (0x8000|CalcEngine.MATRIX_STO)) == 0) {
            decodeCommand(cmd, decoded);
            return decoded.cmd==CalcEngine.LBL && decoded.param==labelNo;
        }
        return false;
    }

    private boolean isNumber(int step) {
        if (step == numProgSteps)
            return false;
        int cmd = prog[currentProg][progStepAddr[step]];
        if ((cmd & 0x8000) != 0)
            return true;
        if ((cmd & CalcEngine.MATRIX_STO) != 0) // Also UNIT_SET
            return false;
        decodeCommand(cmd, decoded);
        return decoded.cmd == CalcEngine.PUSH_INT || decoded.cmd == CalcEngine.PUSH_INT_N;
    }

    public boolean record(int cmd, int param, int step) {
        if (prog[currentProg] == null ||
            (cmd >= CalcEngine.PROG_NEW    && cmd <= CalcEngine.PROG_DIFF) ||
            (cmd >= CalcEngine.AVG_DRAW    && cmd <= CalcEngine.POW_DRAW) ||
            (cmd >= CalcEngine.PROG_DRAW   && cmd <= CalcEngine.PROG_MINMAX) ||
            (cmd >= CalcEngine.MONITOR_PROG && cmd <= CalcEngine.PROG_APPEND))
            return false; // Such commands cannot be recorded

        if (cmd == CalcEngine.MATRIX_STO || cmd == CalcEngine.MATRIX_RCL) {
            int col = param & 0xffff;
            int row = (param>>16) & 0xffff;
            if (col>=64 || row>=64)
                return false; // Cannot program so large index (should we warn?)
            cmd = encodeMatrixCmd(cmd, row, col);
        } else if (cmd == CalcEngine.UNIT_SET || cmd == CalcEngine.UNIT_SET_INV ||
                   cmd == CalcEngine.UNIT_CONVERT) {
            int unitType = param & 0xffff;
            int unit = (param>>16) & 0xffff;
            if (unitType>=64 || unit>=32)
                return false; // There are no such units
            cmd = encodeUnitCmd(cmd, unitType, unit);
        } else {
            cmd = encodeCommand(cmd, param);
        }

        progInsert(step, 1);
        int inspos = progStepAddr[step];
        prog[currentProg][inspos] = (short)cmd;
        return true;
    }

    public void recordPush(Real x, int step) {
        int inspos = progStepAddr[step];
        int intVal = Math.abs(x.toInteger());

        if (x.isIntegral() && intVal<=31) {
            progInsert(step, 1);
            prog[currentProg][inspos] = (short)(CalcEngine.PUSH_INT + x.sign + (intVal<<10));
        } else if (x.isInfinity()) {
            progInsert(step, 1);
            prog[currentProg][inspos] = (short)(CalcEngine.PUSH_INF + x.sign);
        } else {
            progInsert(step, 6);
            encodeProgReal(x, inspos);
        }
    }
    
    public void deleteStep(int step) {
        if (step >= numProgSteps)
            return;
        int addr = progStepAddr[step];
        int stepSize = 1;
        if ((prog[currentProg][addr] & 0x8000) != 0)
            stepSize = 6;
        progSize -= stepSize;
        numProgSteps--;
        System.arraycopy(prog[currentProg],addr+stepSize,
                         prog[currentProg],addr,
                         progSize-addr);
        for (int i=step; i<numProgSteps+1; i++) {
            progStepAddr[i] = (short)(progStepAddr[i+1]-stepSize);
        }
        cache.clearElementsFromRow(step);
    }

    // execute step in program
    public void executeProgStep(int step) {
        if (step >= numProgSteps)
            return;
        int a = progStepAddr[step];
        int cmd = prog[currentProg][a];
        if ((cmd & 0x8000) != 0) {
            decodeProgReal(rTmp, a);
            engine.push(rTmp);
            return;
        }
        if ((cmd & CalcEngine.UNIT_SET) == CalcEngine.UNIT_SET) {
            decodeUnitCmd(cmd, decoded);
            engine.command(decoded.cmd, (decoded.unit<<16)+decoded.unitType);
            return;
        }
        if ((cmd & CalcEngine.MATRIX_STO) == CalcEngine.MATRIX_STO) {
            decodeMatrixCmd(cmd, decoded);
            engine.command(decoded.cmd, (decoded.row<<16)+decoded.col);
            return;
        }
        decodeCommand(cmd, decoded);
        engine.command(decoded.cmd, decoded.param);
    }

    // Monitorable methods

    public int rows() {
        return numProgSteps+1;
    }

    public int cols() {
        return 1;
    }

    public void setDisplayedSize(int rows, int cols) {
        cache.setMinimumSize(rows,cols);
    }

    public void setFormat(Real.NumberFormat format) {
        this.format = format;
    }

    public void formatUpdated() {
        cache.clearElementsFromRow(0);
    }

    public String label(int row, int col) {
        if (!cache.containsLabel(row, col))
            cache.setLabel(row, col, Integer.toString(row+1));
        return cache.label(row, col);
    }

    public String lead(int row, int col, boolean currentPosition, boolean isInside) {
        return currentPosition ? (isInside ? "»" : ">") : ":";
    }

    public String element(int row, int col, int maxWidth) {
        if (isNumber(row)) {
            if (!cache.containsElement(row, col))
                cache.setElement(row, col, makeNumber(row, maxWidth));
            return cache.element(row, col);
        }
        return null;
    }

    public String elementSuffix(int row, int col) {
        if (!isNumber(row)) {
            if (!cache.containsElement(row, col))
                cache.setElement(row, col, makeProgMonitorString(row));
            return cache.element(row, col);
        }
        return null;
    }

    public boolean isLabelMonospaced(int row, int col) {
        return true;
    }

    public boolean hasCaption() {
        return true;
    }

    public String caption(int col) {
        return caption;
    }

    private String makeNumber(int n, int maxWidth) {
        int cmd = prog[currentProg][progStepAddr[n]];
        if ((cmd & 0x8000) != 0) {
            decodeProgReal(rTmp, progStepAddr[n]);
            int tmp = format.maxwidth;
            format.maxwidth = maxWidth;
            String s = rTmp.toString(format);
            format.maxwidth = tmp;
            return s;
        }
        // PUSH_INT or PUSH_INT_N
        decodeCommand(cmd, decoded);
        return CmdDesc.getStr(decoded.cmd, false) + decoded.param;
    }

    private String makeProgMonitorString(int n) {
        if (n == numProgSteps) {
            return "[prg end]";
        }

        int cmd = prog[currentProg][progStepAddr[n]];

        if ((cmd & CalcEngine.UNIT_SET) == CalcEngine.UNIT_SET) {
            decodeUnitCmd(cmd, decoded);
            return CmdDesc.getStr(decoded.cmd, false) + " " +
                Unit.allUnits[decoded.unitType][decoded.unit].name;
        }
        if ((cmd & CalcEngine.MATRIX_STO) == CalcEngine.MATRIX_STO) {
            decodeMatrixCmd(cmd, decoded);
            return CmdDesc.getStr(decoded.cmd, false) + " M:["+decoded.row+","+decoded.col+"]";
        }

        decodeCommand(cmd, decoded);
        String cmdStr = CmdDesc.getStr(decoded.cmd, false);
        if ((CmdDesc.getFlags(decoded.cmd) & CmdDesc.NUMBER_REQUIRED) != 0) {
            return cmdStr + " " + decoded.param;
        } else if ((CmdDesc.getFlags(decoded.cmd) & CmdDesc.FINANCE_REQUIRED) != 0) {
            return cmdStr + " " + CalcEngine.financeLabels[decoded.param];
        } else if (decoded.cmd == CalcEngine.STAT_STO || decoded.cmd == CalcEngine.STAT_RCL) {
            return cmdStr + " " + CalcEngine.statLabels[decoded.param];
        } else if ((CmdDesc.getFlags(decoded.cmd) & CmdDesc.PROG_REQUIRED) != 0) {
            return cmdStr + " " + progLabels[decoded.param]; // for future extensions
        }
        return cmdStr;
    }
}
