package midpcalc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import junit.framework.TestCase;
import static midpcalc.CalcEngine.*;

public class CalcEngineTest extends TestCase {
    
    CalcEngine calc;
    int numDigits;
    boolean stackPreserved;
    int leftoverStackElements;
    
    class TestCanvas implements CanvasAccess {
        public int getHeight() { return 128; }
        public int getWidth() { return 128; }
        public void prepareGraph(int graphCommand, int graphParam) {}
    }

    protected void setUp() throws Exception {
        super.setUp();
        calc = new CalcEngine(new TestCanvas());
        numDigits = 30;
        calc.setDisplayProperties(numDigits, 5);
        
        enter(1.1);
        enter(2.2, 2.2);
        enter(3.3, "m");
        enter(new double[][] {{4,4},{4,4}});
        stackPreserved = true;
        leftoverStackElements = 1;
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        try {
            if (stackPreserved) {
                clx(leftoverStackElements);
                calc.command(BASE_DEC, 0);
                calc.command(NORM, 0);
                calc.command(POINT_DOT, 0);
                calc.command(POINT_REMOVE, 0);
                assertX(new double[][] {{4,4},{4,4}});
                clx();
                assertX(3.3, "m");
                clx();
                assertX(2.2, 2.2);
                clx();
                assertX(1.1);
                clx();
                assertSame(ComplexMatrixElement.empty, calc.getStack().element(0, 0, numDigits));
            }
        } finally {
            calc = null;
        }
    }
    
    private void cmd(int cmd) {
        calc.command(cmd, 0);
    }
    
    private void cmd(int cmd, int param) {
        calc.command(cmd, param);
    }
    
    private void cmd(int cmd, int param1, int param2) {
        calc.command(cmd, (param1<<16)+param2);
    }
    
    private void enter() {
        cmd(ENTER);
    }

    private void clx() {
        cmd(CLEAR);
    }

    private void clx(int n) {
        for (int i=0; i<n; i++)
            clx();
    }

    private String doubleToString(double n) {
        String s = String.valueOf(n);
        s = s.replace('E', 'e');
        s = s.replaceFirst(".0(?=$|e)", "");
        if (s.equals("-0"))
            return "0";
        return s;
    }

    private String complexToString(double re, double im) {
        String s1 = doubleToString(re);
        String s2 = (im == 0 ? "" : (im < 0 ? "" : "+") + doubleToString(im) + "i");
        return s1+s2;
    }

    private void type(String n) {
        assertFalse(calc.inputInProgress);
        for (int i=0; i<n.length(); i++) {
            char c = n.charAt(i);
            if (c == '-' || c == 'e')
                cmd(SIGN_E);
            else if (c == '.')
                cmd(DEC_POINT);
            else {
                int cmd = Real.hexChar.indexOf(c)+DIGIT_0;
                assertTrue(cmd >= DIGIT_0 && cmd <= DIGIT_F);
                cmd(cmd);
            }
            assertTrue(calc.inputInProgress);
        }
        assertEquals(n, calc.inputBuf.toString());
    }

    private void type(double n) {
        type(doubleToString(n));
    }

    private void enter(String n) {
        type(n);
        enter();
        assertX(n);
    }

    private void enter(double n) {
        type(n);
        enter();
        assertX(n);
    }

    private void enter(double n, String unit) {
        type(n);
        cmd(UNIT_SET, findUnit(unit));
        assertX(n, unit);
    }

    private int findUnit(String unit) {
        int tmp = MenuUnitCmd.findUnit(unit);
        return ((tmp & 0xff00) << 8) + (tmp & 0xff);
    }

    private void enter(double re, double im) {
        enter(im);
        type(re);
        cmd(TO_CPLX);
        assertX(re, im);
    }

    // Complex numbers with units exist (e.g. 2+3i V), so it must be tested
    private void enter(double re, double im, String unit) {
        enter(re, im);
        cmd(UNIT_SET, findUnit(unit));
        assertX(re, im, unit);
    }

    private void enterRow(double [] row) {
        enter(row[0]);
        for (int col=1; col<row.length; col++) {
            type(row[col]);
            cmd(MATRIX_CONCAT);
        }
    }
    
    private void enter(double [][] m) {
        enterRow(m[0]);
        for (int row=1; row<m.length; row++) {
            enterRow(m[row]);
            cmd(MATRIX_STACK);
        }
        assertX("M:["+m.length+"x"+m[0].length+"]");
    }
    
    private void enterRow(double [][] row) {
        enter(row[0][0],row[0][1]);
        for (int col=1; col<row.length; col++) {
            enter(row[col][0],row[col][1]);
            cmd(MATRIX_CONCAT);
        }
    }
    
    private void enter(double [][][] m) {
        enterRow(m[0]);
        for (int row=1; row<m.length; row++) {
            enterRow(m[row]);
            cmd(MATRIX_STACK);
        }
        assertX("M:["+m.length+"x"+m[0].length+"]");
    }
    
    private void assertStackElement(int i, String s) {
        assertFalse(calc.inputInProgress);
        Monitorable st = calc.getStack();
        String e = st.element(i, 0, numDigits);
        String unit = st.elementSuffix(i, 0);
        assertEquals(s, (e.equals("-0")?"0":e)+(unit!=null?unit:""));
    }
    
    private void assertStackElement(int i, double n) {
        assertStackElement(i, doubleToString(n));
    }

    private void assertStackElement(int i, double re, double im) {
        assertStackElement(i, complexToString(re,im));
    }
    
    private void assertStackElement(int i, double n, String unit) {
        assertStackElement(i, doubleToString(n)+" "+unit);
    }
    
    private void assertStackElement(int i, double re, double im, String unit) {
        assertStackElement(i, complexToString(re,im)+" "+unit);
    }
    
    private void assertStackElement(int i, double [][] m) {
        for (int a=0; a<i; a++)
            cmd(ROLLDN);
        assertX("M:["+m.length+"x"+m[0].length+"]");
        for (int row=0; row<m.length; row++) {
            type(row+1);
            cmd(MATRIX_ROW);
            if (m[row].length == 1) {
                assertX(m[row][0]);
            } else {
                for (int col=0; col<m[row].length; col++) {
                    type(col+1);
                    cmd(MATRIX_COL);
                    assertX(m[row][col]);
                    clx();
                }
            }
            clx();
        }
        for (int a=0; a<i; a++)
            cmd(ROLLUP);
    }

    private void assertStackElement(int i, double [][][] m) {
        for (int a=0; a<i; a++)
            cmd(ROLLDN);
        assertX("M:["+m.length+"x"+m[0].length+"]");
        for (int row=0; row<m.length; row++) {
            type(row+1);
            cmd(MATRIX_ROW);
            if (m[row].length == 1) {
                assertX(m[row][0][0],m[row][0][1]);
            } else {
                for (int col=0; col<m[row].length; col++) {
                    type(col+1);
                    cmd(MATRIX_COL);
                    assertX(m[row][col][0],m[row][col][1]);
                    clx();
                }
            }
            clx();
        }
        for (int a=0; a<i; a++)
            cmd(ROLLUP);
    }

    private void assertX(String s) {
        assertStackElement(0, s);
    }
    
    private void assertX(double n) {
        assertStackElement(0, n);
    }
    
    private void assertX(double re, double im) {
        assertStackElement(0, re, im);
    }
    
    private void assertX(double n, String unit) {
        assertStackElement(0, n, unit);
    }
    
    private void assertX(double re, double im, String unit) {
        assertStackElement(0, re, im, unit);
    }
    
    private void assertX(double [][] m) {
        assertStackElement(0, m);
    }
    
    private void assertX(double [][][] m) {
        assertStackElement(0, m);
    }
    
    private void assertY(String s) {
        assertStackElement(1, s);
    }
    
    private void assertY(double n) {
        assertStackElement(1, n);
    }
    
    private void assertY(double re, double im) {
        assertStackElement(1, re, im);
    }
    
    private void assertY(double n, String unit) {
        assertStackElement(1, n, unit);
    }
    
    private void assertY(double [][] m) {
        assertStackElement(1, m);
    }
    
    private void assertZ(double n) {
        assertStackElement(2, n);
    }
    
    private void assertZ(double [][] m) {
        assertStackElement(2, m);
    }
    
    private void assertXLessThan(double n) {
        double x = Double.valueOf(calc.getStack().element(0, 0, numDigits));
        assertTrue(x<n);
    }
    
    private void assertXGreaterThan(double n) {
        double x = Double.valueOf(calc.getStack().element(0, 0, numDigits));
        assertTrue(x>n);
    }
    
    private void testDigit(int cmd, boolean bin, boolean oct, boolean dec) {
        char digit = Real.hexChar.charAt(cmd-DIGIT_0);
        if (!bin) {
            cmd(BASE_BIN);
            cmd(cmd);
            assertFalse(calc.inputInProgress);
        }
        if (!oct) {
            cmd(BASE_OCT);
            cmd(cmd);
            assertFalse(calc.inputInProgress);
        }
        if (!dec) {
            cmd(BASE_DEC);
            cmd(cmd);
            assertFalse(calc.inputInProgress);
        }
        cmd(BASE_HEX);
        if (cmd>DIGIT_9)
            cmd(DIGIT_0); // Hex digits replace a normal digit when typed
        cmd(cmd);
        assertTrue(calc.inputInProgress);
        assertEquals(String.valueOf(digit), calc.inputBuf.toString());

        enter();
    }

    public void test_DIGIT_0() {
        testDigit(DIGIT_0, true, true, true);
    }

    public void test_DIGIT_1() {
        assertEquals(DIGIT_0+1, DIGIT_1);
        testDigit(DIGIT_1, true, true, true);
    }

    public void test_DIGIT_2() {
        assertEquals(DIGIT_0+2, DIGIT_2);
        testDigit(DIGIT_2, false, true, true);
    }

    public void test_DIGIT_3() {
        assertEquals(DIGIT_0+3, DIGIT_3);
        testDigit(DIGIT_3, false, true, true);
    }

    public void test_DIGIT_4() {
        assertEquals(DIGIT_0+4, DIGIT_4);
        testDigit(DIGIT_4, false, true, true);
    }

    public void test_DIGIT_5() {
        assertEquals(DIGIT_0+5, DIGIT_5);
        testDigit(DIGIT_5, false, true, true);
    }

    public void test_DIGIT_6() {
        assertEquals(DIGIT_0+6, DIGIT_6);
        testDigit(DIGIT_6, false, true, true);
    }

    public void test_DIGIT_7() {
        assertEquals(DIGIT_0+7, DIGIT_7);
        testDigit(DIGIT_7, false, true, true);
    }

    public void test_DIGIT_8() {
        assertEquals(DIGIT_0+8, DIGIT_8);
        testDigit(DIGIT_8, false, false, true);
    }

    public void test_DIGIT_9() {
        assertEquals(DIGIT_0+9, DIGIT_9);
        testDigit(DIGIT_9, false, false, true);
    }

    public void test_DIGIT_A() {
        assertEquals(DIGIT_0+0xA, DIGIT_A);
        testDigit(DIGIT_A, false, false, false);
    }

    public void test_DIGIT_B() {
        assertEquals(DIGIT_0+0xB, DIGIT_B);
        testDigit(DIGIT_B, false, false, false);
    }

    public void test_DIGIT_C() {
        assertEquals(DIGIT_0+0xC, DIGIT_C);
        testDigit(DIGIT_C, false, false, false);
    }

    public void test_DIGIT_D() {
        assertEquals(DIGIT_0+0xD, DIGIT_D);
        testDigit(DIGIT_D, false, false, false);
    }

    public void test_DIGIT_E() {
        assertEquals(DIGIT_0+0xE, DIGIT_E);
        testDigit(DIGIT_E, false, false, false);
    }

    public void test_DIGIT_F() {
        assertEquals(DIGIT_0+0xF, DIGIT_F);
        testDigit(DIGIT_F, false, false, false);
    }

    public void test_SIGN_E() {
        cmd(SIGN_E);
        assertTrue(calc.inputInProgress);
        assertEquals("-", calc.inputBuf.toString());
        cmd(SIGN_E);
        assertTrue(calc.inputInProgress);
        assertEquals("", calc.inputBuf.toString());
        cmd(SIGN_E);
        cmd(DIGIT_0);
        assertEquals("-0", calc.inputBuf.toString());
        cmd(SIGN_E);
        assertEquals("-0e", calc.inputBuf.toString());
        cmd(SIGN_E);
        assertEquals("-0e-", calc.inputBuf.toString());
        cmd(SIGN_E);
        assertEquals("-0e", calc.inputBuf.toString());
        cmd(SIGN_E);
        cmd(DIGIT_0);
        assertEquals("-0e-0", calc.inputBuf.toString());
        cmd(SIGN_E);
        assertEquals("-0e-0", calc.inputBuf.toString());

        enter();
    }

    public void test_SIGN_E_binary() {
        cmd(BASE_BIN);
        cmd(SIGN_E);
        assertTrue(calc.inputInProgress);
        assertEquals("/", calc.inputBuf.toString());
        cmd(SIGN_E);
        assertEquals("-", calc.inputBuf.toString());
        cmd(SIGN_E);
        assertTrue(calc.inputInProgress);
        assertEquals("", calc.inputBuf.toString());
        cmd(SIGN_E);
        cmd(SIGN_E);
        cmd(DIGIT_0);
        assertEquals("-0", calc.inputBuf.toString());
        assertEquals("-0", calc.inputBuf.toString());
        cmd(SIGN_E);
        assertEquals("-0e", calc.inputBuf.toString());
        cmd(SIGN_E);
        assertEquals("-0e-", calc.inputBuf.toString());
        cmd(SIGN_E);
        assertEquals("-0e", calc.inputBuf.toString());
        cmd(SIGN_E);
        cmd(DIGIT_0);
        assertEquals("-0e-0", calc.inputBuf.toString());
        cmd(SIGN_E);
        assertEquals("-0e-0", calc.inputBuf.toString());

        enter();
    }

    public void test_DEC_POINT() {
        cmd(DEC_POINT);
        assertTrue(calc.inputInProgress);
        assertEquals("0.", calc.inputBuf.toString());
        cmd(DEC_POINT);
        assertEquals("0", calc.inputBuf.toString());
        cmd(DEC_POINT);
        cmd(DIGIT_0);
        assertEquals("0.0", calc.inputBuf.toString());
        cmd(DEC_POINT);
        assertEquals("0.0", calc.inputBuf.toString());
        clx();
        cmd(DEC_POINT);
        assertEquals("0", calc.inputBuf.toString());
        cmd(SIGN_E);
        cmd(DEC_POINT);
        assertEquals("0e", calc.inputBuf.toString());

        enter();
    }

    public void test_DEC_POINT_hex() {
        cmd(BASE_HEX);
        cmd(SIGN_E);
        assertTrue(calc.inputInProgress);
        assertEquals("/", calc.inputBuf.toString());
        cmd(DEC_POINT);
        assertEquals("/F.", calc.inputBuf.toString());

        enter();
    }

    public void test_SIGN_POINT_E() {
        cmd(SIGN_POINT_E);
        assertTrue(calc.inputInProgress);
        assertEquals("-", calc.inputBuf.toString());
        cmd(SIGN_POINT_E);
        assertTrue(calc.inputInProgress);
        assertEquals("", calc.inputBuf.toString());
        cmd(SIGN_POINT_E);
        cmd(DIGIT_0);
        assertEquals("-0", calc.inputBuf.toString());
        cmd(SIGN_POINT_E);
        assertEquals("-0.", calc.inputBuf.toString());
        cmd(SIGN_POINT_E);
        assertEquals("-0e", calc.inputBuf.toString());
        cmd(SIGN_POINT_E);
        assertEquals("-0e-", calc.inputBuf.toString());
        cmd(SIGN_POINT_E);
        assertEquals("-0e", calc.inputBuf.toString());
        cmd(SIGN_POINT_E);
        cmd(DIGIT_0);
        assertEquals("-0e-0", calc.inputBuf.toString());
        cmd(SIGN_POINT_E);
        assertEquals("-0e-0", calc.inputBuf.toString());

        enter();
    }
    
    public void test_SIGN_POINT_E_binary() {
        cmd(BASE_BIN);
        cmd(SIGN_POINT_E);
        assertTrue(calc.inputInProgress);
        assertEquals("/", calc.inputBuf.toString());
        cmd(SIGN_E);
        assertEquals("-", calc.inputBuf.toString());
        cmd(SIGN_POINT_E);
        assertTrue(calc.inputInProgress);
        assertEquals("", calc.inputBuf.toString());
        cmd(SIGN_POINT_E);
        cmd(SIGN_POINT_E);
        cmd(DIGIT_0);
        assertEquals("-0", calc.inputBuf.toString());
        cmd(SIGN_POINT_E);
        assertEquals("-0.", calc.inputBuf.toString());
        cmd(SIGN_POINT_E);
        assertEquals("-0e", calc.inputBuf.toString());
        cmd(SIGN_POINT_E);
        assertEquals("-0e-", calc.inputBuf.toString());
        cmd(SIGN_POINT_E);
        assertEquals("-0e", calc.inputBuf.toString());
        cmd(SIGN_POINT_E);
        cmd(DIGIT_0);
        assertEquals("-0e-0", calc.inputBuf.toString());
        cmd(SIGN_POINT_E);
        assertEquals("-0e-0", calc.inputBuf.toString());

        enter();
    }
    
    public void test_ENTER() {
        enter(3.14);
    }

    public void test_CLEAR() {
        enter(2.5);
        type(3.14);
        clx();
        assertTrue(calc.inputInProgress);
        assertEquals("3.1", calc.inputBuf.toString());
        clx();
        assertEquals("3.", calc.inputBuf.toString());
        clx();
        assertEquals("3", calc.inputBuf.toString());
        clx();
        assertTrue(calc.inputInProgress);
        assertEquals("", calc.inputBuf.toString());
        clx();
        assertX(2.5);
        enter(1.41);
        clx();
        assertX(2.5);
    }

    public void test_ADD() {
        enter(2.5);
        type(1.5);
        cmd(ADD);
        assertX(2.5+1.5);
    }

    public void test_ADD_complex() {
        enter(1, 2);
        enter(2.5, 1.5);
        cmd(ADD);
        assertX(1+2.5, 2+1.5);
    }

    public void test_ADD_matrix() {
        enter(new double[][] {{1,2},{3,4}});
        enter(new double[][] {{2,3},{4,5}});
        cmd(ADD);
        assertX(new double[][] {{1+2,2+3},{3+4,4+5}});
        
        enter(new double[][] {{1,2},{3,4}});
        type(3);
        cmd(ADD);
        assertX("nan");

        leftoverStackElements = 2;
    }

    public void test_ADD_unit() {
        enter(2.5, "m");
        enter(1.5, "m");
        cmd(ADD);
        assertX(2.5+1.5, "m");
        
        enter(1, 2, "m");
        enter(2.5, 1.5, "cm");
        cmd(ADD);
        assertX(1*100+2.5, 2*100+1.5, "cm");

        leftoverStackElements = 2;
    }

    public void test_SUB() {
        enter(2.5);
        type(1.5);
        cmd(SUB);
        assertX(2.5-1.5);
    }

    public void test_SUB_complex() {
        enter(1, 2);
        enter(2.5, 2);
        cmd(SUB);
        assertX(1-2.5);
    }

    public void test_SUB_matrix() {
        enter(new double[][] {{1,2},{3,4}});
        enter(new double[][] {{2,3},{4,5}});
        cmd(SUB);
        assertX(new double[][] {{1-2,2-3},{3-4,4-5}});
    }

    public void test_SUB_unit() {
        enter(2.5, "m");
        enter(1.5, "m");
        cmd(SUB);
        assertX(2.5-1.5, "m");

        enter(1, 2, "m");
        enter(2.5, 2, "cm");
        cmd(SUB);
        assertX(1*100-2.5, 2*100-2, "cm");

        leftoverStackElements = 2;
    }

    public void test_MUL() {
        enter(2.5);
        type(1.5);
        cmd(MUL);
        assertX(2.5*1.5);
    }

    public void test_MUL_complex() {
        enter(1, 2);
        enter(2.5, 1.5);
        cmd(MUL);
        assertX(1*2.5-2*1.5, 1*1.5+2.5*2);
    }

    public void test_MUL_matrix() {
        enter(new double[][] {{1,2},{3,4}});
        enter(new double[][] {{2,3},{4,5}});
        cmd(MUL);
        assertX(new double[][] {{1*2+2*4,1*3+2*5},{3*2+4*4,3*3+4*5}});

        enter(new double[][] {{1,2},{3,4}});
        type(3);
        cmd(MUL);
        assertX(new double[][] {{1*3,2*3},{3*3,4*3}});

        enter(3);
        enter(new double[][] {{2,3},{4,5}});
        cmd(MUL);
        assertX(new double[][] {{3*2,3*3},{3*4,3*5}});

        leftoverStackElements = 3;
    }

    public void test_MUL_unit() {
        enter(2.5, "m");
        enter(1.5, "m");
        cmd(MUL);
        assertX(2.5*1.5, "m²");

        enter(1, 2, "m");
        enter(2.5, 1.5, "cm");
        cmd(MUL);
        assertX(1*100*2.5-2*100*1.5, 1*100*1.5+2.5*2*100, "cm²");

        leftoverStackElements = 2;
    }

    public void test_DIV() {
        enter(2.5);
        type(2);
        cmd(DIV);
        assertX(2.5/2);
    }

    public void test_DIV_complex() {
        enter(1*2.5-2*1.5, 1*1.5+2.5*2);
        enter(1, 2);
        cmd(DIV);
        assertX(2.5, 1.5);
    }

    public void test_DIV_matrix() {
        enter(new double[][] {{1*2+2*4,1*3+2*5},{3*2+4*4,3*3+4*5}});
        enter(new double[][] {{2,3},{4,5}});
        cmd(DIV);
        assertX(new double[][] {{1,2},{3,4}});

        enter(new double[][] {{1,2},{3,4}});
        type(2);
        cmd(DIV);
        assertX(new double[][] {{1.0/2,2.0/2},{3.0/2,4.0/2}});

        enter(3);
        enter(new double[][] {{0,1},{-1,0}});
        cmd(DIV);
        assertX(new double[][] {{3*0,3*-1},{3*1,3*0}});

        leftoverStackElements = 3;
    }

    public void test_DIV_unit() {
        enter(2.5, "m");
        enter(2, "m");
        cmd(DIV);
        assertX(2.5/2);

        enter(1*100*2.5-2*100*1.5, 1*100*1.5+2.5*2*100, "cm");
        enter(1, 2, "m");
        cmd(DIV);
        assertX(2.5, 1.5);

        leftoverStackElements = 2;
    }

    public void test_NEG() {
        type(-2.5);
        cmd(NEG);
        assertX(2.5);
    }

    public void test_NEG_complex() {
        enter(2.5, -1.5);
        cmd(NEG);
        assertX(-2.5, 1.5);
    }

    public void test_NEG_matrix() {
        enter(new double[][] {{1,2},{3,4}});
        cmd(NEG);
        assertX(new double[][] {{-1,-2},{-3,-4}});
    }

    public void test_NEG_unit() {
        enter(2.5, "m");
        cmd(NEG);
        assertX(-2.5, "m");
    }

    public void test_RECIP() {
        type(2);
        cmd(RECIP);
        assertX(1.0/2);
    }

    public void test_RECIP_complex() {
        enter(2, 2);
        cmd(RECIP);
        assertX(0.25,-0.25);
    }

    public void test_RECIP_matrix() {
        enter(new double[][] {{0,1},{-1,0}});
        cmd(RECIP);
        assertX(new double[][] {{0,-1},{1,0}});
    }

    public void test_RECIP_unit() {
        enter(2.5, "m");
        cmd(RECIP);
        assertX(1/2.5, "m¹"); // meaning: m^-1
    }

    public void test_SQR() {
        type(2);
        cmd(SQR);
        assertX(2*2);
    }

    public void test_SQR_complex() {
        enter(2.5, 1.5);
        cmd(SQR);
        assertX(2.5*2.5-1.5*1.5, 2*2.5*1.5);
    }

    public void test_SQR_matrix() {
        enter(new double[][] {{0,1},{-1,0}});
        cmd(SQR);
        assertX(new double[][] {{-1,0},{0,-1}});
    }

    public void test_SQR_unit() {
        enter(2.5, "m");
        cmd(SQR);
        assertX(2.5*2.5, "m²");
    }

    public void test_SQRT() {
        type(4);
        cmd(SQRT);
        assertX(2);
    }

    public void test_SQRT_complex() {
        enter(2.5*2.5-1.5*1.5, 2*2.5*1.5);
        cmd(SQRT);
        assertX(2.5, 1.5);
    }

    public void test_SQRT_matrix() {
        enter(new double[][] {{0,1},{-1,0}});
        cmd(SQRT);
        assertX("nan");
    }

    public void test_SQRT_unit() {
        enter(4, "m²");
        cmd(SQRT);
        assertX(2, "m");

        enter(2.5*2.5-1.5*1.5, 2*2.5*1.5, "m²");
        cmd(SQRT);
        assertX(2.5, 1.5, "m");

        leftoverStackElements = 2;
    }

    public void test_PERCENT() {
        enter(50);
        type(10);
        cmd(PERCENT);
        assertY(50);
        assertX(5);

        leftoverStackElements = 2;
    }

    public void test_PERCENT_complex() {
        enter(1, 2);
        enter(250, 150);
        cmd(PERCENT);
        assertY(1, 2);
        assertX(1*2.5-2*1.5, 1*1.5+2.5*2);

        leftoverStackElements = 2;
    }

    public void test_PERCENT_matrix() {
        enter(10);
        enter(new double[][] {{1,2},{3,4}});
        cmd(PERCENT);
        assertY(10);
        assertX("nan");

        enter(new double[][] {{1,2},{3,4}});
        type(10);
        cmd(PERCENT);
        assertY(new double[][] {{1,2},{3,4}});
        assertX(new double[][] {{0.1,0.2},{0.3,0.4}});

        leftoverStackElements = 4;
    }

    public void test_PERCENT_unit() {
        enter(2.5, "m");
        type(150);
        cmd(PERCENT);
        assertY(2.5, "m");
        assertX(2.5*1.5, "m");

        leftoverStackElements = 2;
    }

    public void test_PERCENT_CHG() {
        enter(5);
        type(10);
        cmd(PERCENT_CHG);
        assertX(100);
    }

    public void test_PERCENT_CHG_complex() {
        enter(1, 2);
        enter(250, 150);
        cmd(PERCENT_CHG);
        assertX("nan");
    }

    public void test_PERCENT_CHG_matrix() {
        enter(new double[][] {{2,4},{5,8}});
        enter(new double[][] {{1,2},{3,4}});
        cmd(PERCENT_CHG);
        assertX("nan");
    }

    public void test_PERCENT_CHG_unit() {
        enter(5, "m");
        enter(10, "m");
        cmd(PERCENT_CHG);
        assertX(100);

        enter(5, "m");
        enter(10, "s");
        cmd(PERCENT_CHG);
        assertX(100, Unit.ERR);

        leftoverStackElements = 2;
    }

    public void test_YPOWX() {
        enter(2);
        type(5);
        cmd(YPOWX);
        assertX(32);
    }

    public void test_YPOWX_complex() {
        enter(1, 1);
        type(4);
        cmd(YPOWX);
        assertX(-4, 0);

        enter(-1);
        enter(0.5);
        cmd(YPOWX);
        assertX(0, 1);

        leftoverStackElements = 2;
    }

    public void test_YPOWX_matrix() {
        enter(new double[][] {{0,1},{-1,0}});
        type(4);
        cmd(YPOWX);
        assertX(new double[][] {{1,0},{0,1}});

        enter(new double[][] {{0,1},{-1,0}});
        type(3.5);
        cmd(YPOWX);
        assertX("nan");

        enter(new double[][] {{0,1},{-1,0}});
        enter(4, 1);
        cmd(YPOWX);
        assertX("nan");

        enter(new double[][] {{0,1,2},{-1,0,2}});
        type(4);
        cmd(YPOWX);
        assertX("nan");

        leftoverStackElements = 4;
    }

    public void test_YPOWX_unit() {
        enter(10, "m");
        type(2);
        cmd(YPOWX);
        assertX(100, "m²");
    }

    public void test_XRTY() {
        enter(32);
        type(5);
        cmd(XRTY);
        assertX(2);
    }


    public void test_XRTY_complex() {
        enter(-4, 0);
        type(4);
        cmd(XRTY);
        assertX(1, 1);
    }

    public void test_XRTY_matrix() {
        enter(new double[][] {{0,1},{-1,0}});
        type(4);
        cmd(XRTY);
        assertX("nan");
    }

    public void test_XRTY_unit() {
        enter(1000, "m³");
        type(3);
        cmd(XRTY);
        assertX(10, "m");

        enter(-4, "m²");
        type(2);
        cmd(XRTY);
        assertX(0, 2, "m");

        leftoverStackElements = 2;
    }

    public void test_LN() {
        type(1);
        cmd(LN);
        assertX(0);
        
        type(2);
        cmd(LN);
        assertX("0.6931471805599453");

        leftoverStackElements = 2;
    }

    public void test_LN_complex() {
        type(-1);
        cmd(LN);
        assertX("0+3.141592653589793i");
        
        enter(1, 1);
        cmd(LN);
        assertX("0.34657359028+0.7853981633974i");

        leftoverStackElements = 2;
    }

    public void test_EXP() {
        type(0);
        cmd(EXP);
        assertX(1);
        
        type(1);
        cmd(EXP);
        assertX("2.718281828459045");

        leftoverStackElements = 2;
    }

    public void test_EXP_complex() {
        enter(1, 1);
        cmd(EXP);
        assertX("1.468693939916+2.287355287179i");
    }

    public void test_LOG10() {
        type(1);
        cmd(LOG10);
        assertX(0);
        
        type(100);
        cmd(LOG10);
        assertX(2);

        leftoverStackElements = 2;
    }

    public void test_LOG10_complex() {
        type(-1);
        cmd(LOG10);
        assertX("0+1.364376353841841i");
        
        enter(1, 1);
        cmd(LOG10);
        assertX("0.150514997832+0.34109408846i");

        leftoverStackElements = 2;
    }

    public void test_EXP10() {
        type(0);
        cmd(EXP10);
        assertX(1);
        
        type(2);
        cmd(EXP10);
        assertX(100);

        leftoverStackElements = 2;
    }

    public void test_EXP10_complex() {
        enter(1, 1);
        cmd(EXP10);
        assertX("-6.6820151019+7.4398033695749i");
    }

    public void test_LOG2() {
        type(1);
        cmd(LOG2);
        assertX(0);
        
        type(4);
        cmd(LOG2);
        assertX(2);

        leftoverStackElements = 2;
    }

    public void test_LOG2_complex() {
        type(-1);
        cmd(LOG2);
        assertX("0+4.532360141827194i");
        
        enter(1, 1);
        cmd(LOG2);
        assertX("0.5+1.133090035456798i");

        leftoverStackElements = 2;
    }

    public void test_EXP2() {
        type(0);
        cmd(EXP2);
        assertX(1);
        
        type(2);
        cmd(EXP2);
        assertX(4);

        leftoverStackElements = 2;
    }

    public void test_EXP2_complex() {
        enter(1, 1);
        cmd(EXP2);
        assertX("1.538477802728+1.277922552627i");
    }

    public void test_PYX() {
        enter(4);
        type(2);
        cmd(PYX);
        assertX(12);
        
        enter(-4);
        type(1);
        cmd(PYX);
        assertX(-4);

        leftoverStackElements = 2;
    }

    public void test_CYX() {
        enter(4);
        type(2);
        cmd(CYX);
        assertX(6);
        
        enter(-4);
        type(1);
        cmd(CYX);
        assertX(-4);

        leftoverStackElements = 2;
    }

    public void test_FACT() {
        type(4);
        cmd(FACT);
        assertX(4*3*2*1);
    }

    public void test_FACT_complex() {
        enter(0, 4);
        cmd(FACT);
        assertX("nan");
    }

    public void test_FACT_unit() {
        enter(4, "m");
        cmd(FACT);
        assertX(4*3*2*1, Unit.ERR);
    }

    public void test_GAMMA() {
        type(4);
        cmd(GAMMA);
        assertX(3*2*1);
    }

    public void test_MOD() {
        enter(30);
        type(3);
        cmd(MOD);
        assertX(0);

        enter(-44e20);
        type(5e20);
        cmd(MOD);
        assertX(1e20);

        leftoverStackElements = 2;
    }

    public void test_DIVF() {
        enter(30);
        type(3);
        cmd(DIVF);
        assertX(10);

        enter(-44e20);
        type(5e20);
        cmd(DIVF);
        assertX(-9);

        leftoverStackElements = 2;
    }

    public void test_ERFC() {
        type(0);
        cmd(ERFC);
        assertX(1);

        type(-10);
        cmd(ERFC);
        assertX(2);

        type(1);
        cmd(ERFC);
        assertX("0.1572992070502851");

        leftoverStackElements = 3;
    }

    public void test_INVERFC() {
        type(1);
        cmd(INVERFC);
        assertX(0);

        type(2);
        cmd(INVERFC);
        assertX("-inf");

        type(0);
        cmd(INVERFC);
        assertX("inf");

        type(0.5);
        cmd(INVERFC);
        assertX("0.4769362762044699");

        leftoverStackElements = 4;
    }

    public void test_PHI() {
        type(0);
        cmd(PHI);
        assertX(0.5);

        type(10);
        cmd(PHI);
        assertX(1);

        type(1);
        cmd(PHI);
        assertX("0.8413447460685429");

        leftoverStackElements = 3;
    }

    public void test_INVPHI() {
        type(0.5);
        cmd(INVPHI);
        assertX(0);

        type(1);
        cmd(INVPHI);
        assertX("inf");

        type(0);
        cmd(INVPHI);
        assertX("-inf");

        type(0.75);
        cmd(INVPHI);
        assertX("0.6744897501960817");

        leftoverStackElements = 4;
    }

    public void test_GUESS() {
        type(3.14159292035); // 355/113 = 3.14159292035398
        cmd(GUESS);
        assertNotNull(calc.getMessage()); // I.e. the "explanation"
        assertY(3.14159292035);
        enter(355);
        type(113);
        cmd(DIV);
        cmd(SUB);
        assertX(0);

        leftoverStackElements = 2;
    }

    public void test_ATAN2() {
        enter(2);
        type(2);
        cmd(ATAN2);
        assertX("0.7853981633974483");
    }

    public void test_ATAN2_unit() {
        enter(2, "m");
        enter(200, "cm");
        cmd(ATAN2);
        assertX("0.7853981633974483");

        enter(2, "m");
        enter(2, "s");
        cmd(ATAN2);
        assertX("0.7853981633974483 "+Unit.ERR);

        leftoverStackElements = 2;
    }

    public void test_HYPOT() {
        enter(3);
        type(4);
        cmd(HYPOT);
        assertX(5);
    }

    public void test_HYPOT_unit() {
        enter(300, "cm");
        enter(4, "m");
        cmd(HYPOT);
        assertX(5, "m");
    }

    public void test_SIN() {
        type(0);
        cmd(SIN);
        assertX(0);

        cmd(PI);
        type(2);
        cmd(DIV);
        cmd(SIN);
        assertX(1);

        leftoverStackElements = 2;
    }

    public void test_SIN_complex() {
        enter(1, 1);
        cmd(SIN);
        assertX("1.298457581416+0.634963914785i");
    }

    public void test_COS() {
        type(0);
        cmd(COS);
        assertX(1);

        cmd(PI);
        type(2);
        cmd(DIV);
        cmd(COS);
        assertX(0);

        leftoverStackElements = 2;
    }

    public void test_COS_complex() {
        enter(1, 1);
        cmd(COS);
        assertX("0.833730025131-0.988897705763i");
    }

    public void test_TAN() {
        type(0);
        cmd(TAN);
        assertX(0);

        cmd(PI);
        type(4);
        cmd(DIV);
        cmd(TAN);
        assertX(1);

        leftoverStackElements = 2;
    }

    public void test_TAN_complex() {
        enter(1, 1);
        cmd(TAN);
        assertX("0.27175258532+1.0839233273387i");
    }

    public void test_ASIN() {
        type(0);
        cmd(ASIN);
        assertX(0);

        type(1);
        cmd(ASIN);
        assertX("1.570796326794897");

        leftoverStackElements = 2;
    }

    public void test_ASIN_complex() {
        enter(1, 1);
        cmd(ASIN);
        assertX("0.666239432493+1.061275061905i");

        enter(2);
        cmd(ASIN);
        assertX("1.570796326795+1.316957896925i");

        leftoverStackElements = 2;
    }

    public void test_ACOS() {
        type(1);
        cmd(ACOS);
        assertX(0);

        type(0);
        cmd(ACOS);
        assertX("1.570796326794897");

        leftoverStackElements = 2;
    }

    public void test_ACOS_complex() {
        enter(1, 1);
        cmd(ACOS);
        assertX("0.904556894302-1.061275061905i");

        type(2);
        cmd(ACOS);
        assertX("0-1.316957896924817i");

        leftoverStackElements = 2;
    }

    public void test_ATAN() {
        type(0);
        cmd(ATAN);
        assertX(0);

        type(1);
        cmd(ATAN);
        assertX("0.7853981633974483");

        leftoverStackElements = 2;
    }

    public void test_ATAN_complex() {
        enter(1, 1);
        cmd(ATAN);
        assertX("1.017221967898+0.402359478109i");
    }

    public void test_SINH() {
        type(0);
        cmd(SINH);
        assertX(0);

        type(1);
        cmd(SINH);
        assertX("1.175201193643801");

        leftoverStackElements = 2;
    }

    public void test_SINH_complex() {
        enter(1, 1);
        cmd(SINH);
        assertX("0.634963914785+1.298457581416i");
    }

    public void test_COSH() {
        type(0);
        cmd(COSH);
        assertX(1);

        type(1);
        cmd(COSH);
        assertX("1.543080634815244");

        leftoverStackElements = 2;
    }

    public void test_COSH_complex() {
        enter(1, 1);
        cmd(COSH);
        assertX("0.833730025131+0.988897705763i");
    }

    public void test_TANH() {
        type(0);
        cmd(TANH);
        assertX(0);

        type(1);
        cmd(TANH);
        assertX("0.7615941559557649");

        leftoverStackElements = 2;
    }

    public void test_TANH_complex() {
        enter(1, 1);
        cmd(TANH);
        assertX("1.0839233273387+0.27175258532i");
    }

    public void test_ASINH() {
        type(0);
        cmd(ASINH);
        assertX(0);

        type(1);
        cmd(ASINH);
        assertX("0.881373587019543");

        leftoverStackElements = 2;
    }

    public void test_ASINH_complex() {
        enter(1, 1);
        cmd(ASINH);
        assertX("1.061275061905+0.666239432493i");
    }

    public void test_ACOSH() {
        type(1);
        cmd(ACOSH);
        assertX(0);

        type(2);
        cmd(ACOSH);
        assertX("1.316957896924817");

        leftoverStackElements = 2;
    }

    public void test_ACOSH_complex() {
        enter(1, 1);
        cmd(ACOSH);
        assertX("1.061275061905+0.904556894302i");

        type(0);
        cmd(ACOSH);
        assertX("0+1.570796326794897i");

        leftoverStackElements = 2;
    }

    public void test_ATANH() {
        type(0);
        cmd(ATANH);
        assertX(0);

        type(0.5);
        cmd(ATANH);
        assertX("0.5493061443340548");

        leftoverStackElements = 2;
    }

    public void test_ATANH_complex() {
        enter(1, 1);
        cmd(ATANH);
        assertX("0.402359478109+1.017221967898i");

        type(2);
        cmd(ATANH);
        assertX("0.549306144334+1.570796326795i");

        leftoverStackElements = 2;
    }

    public void test_PI() {
        cmd(PI);
        assertX("3.141592653589793");
    }

    public void test_TRIG_DEGRAD() {
        assertFalse(calc.degrees);
        assertFalse(calc.grad);
        type(-1);
        cmd(ACOS);
        assertX("3.141592653589793");
        
        cmd(TRIG_DEGRAD);
        assertTrue(calc.degrees);
        assertFalse(calc.grad);
        type(-1);
        cmd(ACOS);
        assertX(180);

        cmd(TRIG_DEGRAD);
        assertFalse(calc.degrees);
        assertTrue(calc.grad);
        type(-1);
        cmd(ACOS);
        assertX(200);

        leftoverStackElements = 3;
    }

    public void test_TO_DEG() {
        cmd(PI);
        cmd(TO_DEG);
        assertX(180);
    }

    public void test_TO_RAD() {
        type(180);
        cmd(TO_RAD);
        assertX("3.141592653589793");
    }

    public void test_RP() {
        assertFalse(calc.degrees);
        assertFalse(calc.grad);
        enter(2);
        type(0);
        cmd(RP);
        assertY("1.570796326794897");
        assertX(2);
        
        cmd(TRIG_DEGRAD);
        assertTrue(calc.degrees);
        assertFalse(calc.grad);
        enter(2);
        type(0);
        cmd(RP);
        assertY(90);
        assertX(2);

        cmd(TRIG_DEGRAD);
        assertFalse(calc.degrees);
        assertTrue(calc.grad);
        enter(2);
        type(0);
        cmd(RP);
        assertY(100);
        assertX(2);

        leftoverStackElements = 6;
    }

    public void test_RP_complex() {
        enter(2, 2);
        type(0);
        cmd(RP);
        assertY("nan");
        assertX("nan");

        leftoverStackElements = 2;
    }

    public void test_RP_unit() {
        assertFalse(calc.degrees);
        assertFalse(calc.grad);
        enter(2, "m");
        enter(0, "m");
        cmd(RP);
        assertY("1.570796326794897");
        assertX(2, "m");

        leftoverStackElements = 2;
    }

    public void test_PR() {
        assertFalse(calc.degrees);
        assertFalse(calc.grad);
        cmd(PI);
        type(2);
        cmd(DIV);
        type(2);
        cmd(PR);
        assertY(2);
        assertX(0);
        
        cmd(TRIG_DEGRAD);
        assertTrue(calc.degrees);
        assertFalse(calc.grad);
        enter(90);
        type(2);
        cmd(PR);
        assertY(2);
        assertX(0);

        cmd(TRIG_DEGRAD);
        assertFalse(calc.degrees);
        assertTrue(calc.grad);
        enter(100);
        type(2);
        cmd(PR);
        assertY(2);
        assertX(0);

        leftoverStackElements = 6;
    }

    public void test_PR_unit() {
        assertFalse(calc.degrees);
        assertFalse(calc.grad);
        cmd(PI);
        type(2);
        cmd(DIV);
        enter(2, "m");
        cmd(PR);
        assertY(2, "m");
        assertX(0, "m");

        cmd(PI);
        type(2);
        cmd(DIV);
        cmd(UNIT_SET, findUnit("s"));
        enter(2, "m");
        cmd(PR);
        assertY(2, Unit.ERR);
        assertX(0, Unit.ERR);

        leftoverStackElements = 4;
    }

    public void test_ROUND() {
        type(3.5);
        cmd(ROUND);
        assertX(4);

        enter(3.5, -2.5);
        cmd(ROUND);
        assertX(4, -3);

        leftoverStackElements = 2;
    }

    public void test_CEIL() {
        type(3.4);
        cmd(CEIL);
        assertX(4);

        enter(3.4, -2.6);
        cmd(CEIL);
        assertX(4, -2);

        leftoverStackElements = 2;
    }

    public void test_FLOOR() {
        type(-3.6);
        cmd(FLOOR);
        assertX(-4);

        enter(3.6, -2.4);
        cmd(FLOOR);
        assertX(3, -3);

        leftoverStackElements = 2;
    }

    public void test_TRUNC() {
        type(-3.6);
        cmd(TRUNC);
        assertX(-3);

        enter(3.5, -2.5);
        cmd(TRUNC);
        assertX(3, -2);

        leftoverStackElements = 2;
    }

    public void test_FRAC() {
        type(-3.6);
        cmd(FRAC);
        assertX(-0.6);

        enter(3.5, -2.5);
        cmd(FRAC);
        assertX(0.5, -0.5);

        leftoverStackElements = 2;
    }

    public void test_AND() {
        enter(14);
        type(3);
        cmd(AND);
        assertX(2);
    }

    public void test_OR() {
        enter(14);
        type(3);
        cmd(OR);
        assertX(15);
    }

    public void test_XOR() {
        enter(14);
        type(3);
        cmd(XOR);
        assertX(13);
    }

    public void test_BIC() {
        enter(14);
        type(3);
        cmd(BIC);
        assertX(12);
    }

    public void test_NOT() {
        type(3);
        cmd(NOT);
        assertX(-4);
    }

    public void test_YUPX() {
        enter(3);
        type(2);
        cmd(YUPX);
        assertX(12);
    }

    public void test_YDNX() {
        enter(12);
        type(2);
        cmd(YDNX);
        assertX(3);
    }

    public void test_RANDOM() {
        cmd(RANDOM);
        assertXGreaterThan(0);
        assertXLessThan(1);
        cmd(RANDOM);
        assertXGreaterThan(0);
        assertXLessThan(1);
        cmd(SUB);
        assertXGreaterThan(-1);
        assertXLessThan(1);
    }

    public void test_TO_DHMS() {
        type(3.50);
        cmd(TO_DHMS);
        assertX(3.30);
    }

    public void test_TO_H() {
        type(3.30);
        cmd(TO_H);
        assertX(3.50);
    }

    public void test_DHMS_PLUS() {
        enter(3.5040);
        type(3.5050);
        cmd(DHMS_PLUS);
        assertX(7.4130);
    }

    public void test_TIME() {
        cmd(TIME);
        assertXGreaterThan(00.0000);
        assertXLessThan(23.5959);
    }

    public void test_DATE() {
        cmd(DATE);
        assertXGreaterThan(2008012500);
        assertXLessThan(2038011900);
    }

    public void test_TIME_NOW() {
        cmd(TIME_NOW);
        assertXGreaterThan(2008012616.5301);
        assertXLessThan(2038011903.1409);
    }

    public void test_DHMS_TO_UNIX() {
        type(1970010100);
        cmd(DHMS_TO_UNIX);
        assertX(0);

        type("1999123100");
        cmd(DHMS_TO_UNIX);
        assertX("946598400");

        leftoverStackElements = 2;
    }
    
    public void test_UNIX_TO_DHMS() {
        type(0);
        cmd(UNIX_TO_DHMS);
        assertX("1970010100");

        type("946598400");
        cmd(UNIX_TO_DHMS);
        assertX("1999123100");

        leftoverStackElements = 2;
    }

    public void test_DHMS_TO_JD() {
        type(1999123100);
        cmd(DHMS_TO_JD);
        assertX("2451543.5");
    }

    public void test_JD_TO_DHMS() {
        type(2451543.5);
        cmd(JD_TO_DHMS);
        assertX("1999123100");
    }

    public void test_DHMS_TO_MJD() {
        type(1858111700);
        cmd(DHMS_TO_MJD);
        assertX(0);

        type("1999123100");
        cmd(DHMS_TO_MJD);
        assertX("51543");

        leftoverStackElements = 2;
    }

    public void test_MJD_TO_DHMS() {
        type(0);
        cmd(MJD_TO_DHMS);
        assertX("1858111700");

        type(51543);
        cmd(MJD_TO_DHMS);
        assertX("1999123100");

        leftoverStackElements = 2;
    }

    public void test_FACTORIZE() {
        type(17);
        cmd(FACTORIZE);
        assertY(17);
        assertX(1);
        type(25);
        cmd(FACTORIZE);
        assertY(5);
        assertX(5);
        type(49);
        cmd(FACTORIZE);
        assertY(7);
        assertX(7);
        type(46349);
        cmd(FACTORIZE);
        assertY(46349);
        assertX(1);
        clx(8);

        type(-1110.1);
        cmd(FACTORIZE);
        assertY(37);
        assertX(-30);
        cmd(FACTORIZE);
        assertY(5);
        assertX(-6);
        cmd(FACTORIZE);
        assertY(3);
        assertX(-2);
        cmd(FACTORIZE);
        assertY(2);
        assertX(-1);
        cmd(FACTORIZE);
        assertY(-1);
        assertX(1);

        leftoverStackElements = 6;
    }

    public void test_TO_CPLX() {
        enter(3);
        type(4);
        cmd(TO_CPLX);
        assertX(4, 3);
    }

    public void test_CPLX_SPLIT() {
        enter(4, 3);
        cmd(CPLX_SPLIT);
        assertY(3);
        assertX(4);

        leftoverStackElements = 2;
    }

    public void test_ABS() {
        enter(-4);
        cmd(ABS);
        assertX(4);
        
        enter(4, 3);
        cmd(ABS);
        assertX(5);

        leftoverStackElements = 2;
    }

    public void test_ABS_matrix() {
        enter(new double[][] {{3},{4}});
        cmd(ABS);
        assertX(5);
    }

    public void test_CPLX_ARG() {
        cmd(TRIG_DEGRAD);
        enter(4, 4);
        cmd(CPLX_ARG);
        assertX(45);

        enter(4);
        cmd(CPLX_ARG);
        assertX(0);

        leftoverStackElements = 2;
    }

    public void test_CPLX_CONJ() {
        enter(4, 3);
        cmd(CPLX_CONJ);
        assertX(4, -3);
    }

    public void test_CPLX_CONJ_matrix() {
        enter(new double[][][] {{{1,2},{3,4}}});
        cmd(CPLX_CONJ);
        assertX(new double[][][] {{{1,-2},{3,-4}}});
    }

    public void test_complex_matrix() {
        enter(new double[][] {{1,2},{3,4},{5,6}});
        enter(new double[][] {{6,5},{4,3},{2,1}});
        type(-1);
        cmd(SQRT);
        cmd(MUL);
        cmd(ADD);
        assertX(new double[][][] {{{1,6},{2,5}},{{3,4},{4,3}},{{5,2},{6,1}}});
    }

    public void test_MATRIX_NEW() {
        enter(4);
        type(4);
        cmd(MATRIX_NEW);
        assertX(new double[4][4]);

        enter(0,4);
        type(4);
        cmd(MATRIX_NEW);
        assertX("nan");

        enter(4);
        type(-4);
        cmd(MATRIX_NEW);
        assertX("nan");

        leftoverStackElements = 3;
    }

    public void test_MATRIX_CONCAT() {
        enter(new double[][] {{1},{3}});
        enter(new double[][] {{2},{4}});
        cmd(MATRIX_CONCAT);
        assertX(new double[][] {{1,2},{3,4}});

        enter(new double[][] {{1,2}});
        enter(new double[][] {{3},{4}});
        cmd(MATRIX_CONCAT);
        assertX(new double[][] {{1,2,3},{0,0,4}});

        enter(new double[][] {{1},{2}});
        enter(3,4);
        cmd(MATRIX_CONCAT);
        assertX(new double[][][] {{{1,0},{3,4}},{{2,0},{0,0}}});

        enter(3,4);
        enter(new double[][] {{1},{2}});
        cmd(MATRIX_CONCAT);
        assertX(new double[][][] {{{3,4},{1,0}},{{0,0},{2,0}}});

        leftoverStackElements = 4;
    }

    public void test_MATRIX_STACK() {
        enter(new double[][] {{1,2}});
        enter(new double[][] {{3,4}});
        cmd(MATRIX_STACK);
        assertX(new double[][] {{1,2},{3,4}});

        enter(2, "m");
        enter(3, "km");
        cmd(MATRIX_STACK);
        assertX(new double[][] {{2},{3}});

        enter(new double[][] {{1,2}});
        enter(new double[][] {{3},{4}});
        cmd(MATRIX_STACK);
        assertX(new double[][] {{1,2},{3,0},{4,0}});

        enter(new double[][] {{1,2}});
        enter(3,4);
        cmd(MATRIX_STACK);
        assertX(new double[][][] {{{1,0},{2,0}},{{3,4},{0,0}}});

        enter(3,4);
        enter(new double[][] {{1,2}});
        cmd(MATRIX_STACK);
        assertX(new double[][][] {{{3,4},{0,0}},{{1,0},{2,0}}});

        leftoverStackElements = 5;
    }

    public void test_MATRIX_SPLIT_horizontal() {
        enter(new double[][] {{1,2,3},{4,5,6},{7,8,9}});
        enter(2);
        cmd(MATRIX_SPLIT);
        assertY(new double[][] {{1,2,3},{4,5,6}});
        assertX(new double[][] {{7,8,9}});

        leftoverStackElements = 2;
    }

    public void test_MATRIX_SPLIT_vertical() {
        enter(new double[][] {{1,2,3},{4,5,6},{7,8,9}});
        enter(-2);
        cmd(MATRIX_SPLIT);
        assertY(new double[][] {{1,2},{4,5},{7,8}});
        assertX(new double[][] {{3},{6},{9}});

        leftoverStackElements = 2;
    }

    public void test_MATRIX_ROW() {
        enter(new double[][] {{1,2},{3,4}});
        type(2);
        cmd(MATRIX_ROW);
        assertX(new double[][] {{3,4}});
        enter(2, 2);
        cmd(MATRIX_ROW);
        assertX("nan");

        leftoverStackElements = 3;
    }

    public void test_MATRIX_COL() {
        enter(new double[][] {{1,2},{3,4}});
        type(2);
        cmd(MATRIX_COL);
        assertX(new double[][] {{2},{4}});
        enter(2, 2);
        cmd(MATRIX_COL);
        assertX("nan");

        leftoverStackElements = 3;
    }

    public void test_MATRIX_STO() {
        cmd(CLS);
        type(7);
        cmd(MATRIX_STO, 7, 7); // Should be ignored

        enter(new double[2][2]);
        enter();
        type(1);
        cmd(MATRIX_STO, 0, 0);
        type(2);
        cmd(MATRIX_STO, 0, 1);
        type(3);
        cmd(MATRIX_STO, 1, 0);
        type(4);
        cmd(MATRIX_STO, 1, 1);
        cmd(MATRIX_STO, 7, 1); // Should be ignored
        clx(4);
        assertY(new double[2][2]);
        assertX(new double[][] {{1,2},{3,4}});
        
        // A somewhat contrived case to provide code coverage
        enter();
        type(0);
        cmd(SELECT);
        cmd(STO, 4);
        cmd(MATRIX_STO, 0, 0); // Storing the matrix in itself; taken as nan
        cmd(MONITOR_MATRIX, 5);
        calc.getMonitor().setDisplayedSize(5, 1);
        assertEquals("nan", calc.getMonitor().element(0, 0, numDigits));
        // Matrixes in lasty and lastz should be unmodified
        cmd(UNDO); // This undoes the SELECT; MATRIX_STO is like a no-op for UNDO
        assertZ(new double[][] {{1,2},{3,4}});
        assertY(new double[][] {{1,2},{3,4}});
        assertX(0);

        // And another
        clx(2);
        enter();
        clx();
        cmd(MATRIX_STO, 1, 0); // Storing the matrix in itself; taken as nan
        assertEquals("nan", calc.getMonitor().element(1, 0, numDigits));
        // Matrixes in lastx and lasty should be unmodified
        cmd(UNDO); // This undoes the CLX; MATRIX_STO is like a no-op for UNDO
        assertY(new double[][] {{1,2},{3,4}});
        assertX(new double[][] {{1,2},{3,4}});

        stackPreserved = false;;
    }

    public void test_MATRIX_RCL() {
        enter(new double[][] {{1,2},{3,4}});
        cmd(MATRIX_RCL, 0, 0);
        assertX(1);
        cmd(MATRIX_RCL, 0, 1);
        assertX(2);
        cmd(MATRIX_RCL, 1, 0);
        assertX(3);
        cmd(MATRIX_RCL, 1, 1);
        assertX(4);

        leftoverStackElements = 5;
    }

    public void test_MATRIX_SIZE() {
        enter(new double[][] {{0,0},{0,0}});
        cmd(MATRIX_SIZE);
        assertY(2);
        assertX(2);
        
        leftoverStackElements = 3;
    }

    public void test_MATRIX_AIJ() {
        enter(new double[][] {{1,2},{3,4}});
        enter(1);
        enter(1);
        cmd(MATRIX_AIJ);
        assertX(1);

        enter(1);
        enter(2);
        cmd(MATRIX_AIJ);
        assertX(2);

        enter(2);
        enter(1);
        cmd(MATRIX_AIJ);
        assertX(3);

        enter(2);
        enter(2);
        cmd(MATRIX_AIJ);
        assertX(4);

        enter(0, 2);
        enter(2);
        cmd(MATRIX_AIJ);
        assertX("nan");

        leftoverStackElements = 6;
    }

    public void test_TRANSP() {
        enter(new double[][] {{1,2},{3,4},{5,6}});
        cmd(TRANSP);
        assertX(new double [][] {{1,3,5},{2,4,6}});
    }

    public void test_DETERM() {
        enter(new double[][] {{1,2},{3,4}});
        cmd(DETERM);
        assertX(1*4-2*3);
    }

    public void test_TRACE() {
        enter(new double[][] {{1,2},{3,4}});
        cmd(TRACE);
        assertX(1+4);
    }

    public void test_TRANSP_CONJ() {
        enter(new double[][] {{1,2},{3,4},{5,6}});
        cmd(TRANSP_CONJ);
        assertX(new double [][] {{1,3,5},{2,4,6}});

        enter(new double[][][] {{{1,6},{2,5}},{{3,4},{4,3}},{{5,2},{6,1}}});
        cmd(TRANSP_CONJ);
        assertX(new double [][][] {{{1,-6},{3,-4},{5,-2}},{{2,-5},{4,-3},{6,-1}}});

        leftoverStackElements = 2;
    }

    public void test_MATRIX_MIN() {
        enter(new double[][] {{1,2},{3,4}});
        cmd(MATRIX_MIN);
        assertX(1);

        leftoverStackElements = 2;
    }

    public void test_MATRIX_MAX() {
        enter(new double[][] {{1,2},{3,4}});
        cmd(MATRIX_MAX);
        assertX(4);

        leftoverStackElements = 2;
    }

    public void test_UNIT_SET() {
        type(3);
        cmd(UNIT_SET, findUnit("kg"));
        assertX(3, "kg");

        enter(3, 2);
        cmd(UNIT_SET, findUnit("kg"));
        assertX("3+2i kg");

        enter(new double[][] {{1,2},{3,4}});
        cmd(UNIT_SET, findUnit("kg"));
        assertX("nan");

        leftoverStackElements = 3;
    }

    public void test_UNIT_SET_INV() {
        type(3);
        cmd(UNIT_SET_INV, findUnit("m"));
        assertX(3, "m¹"); // meaning: m^-1
        
        cmd(UNIT_SET_INV, findUnit("m"));
        assertX(3, "/m²"); // meaning: m^-2
        cmd(UNIT_SET_INV, findUnit("m"));
        assertX(3, "/m³");
        cmd(UNIT_SET_INV, findUnit("m"));
        assertX(3, "/m¼"); // meaning: m^-4
        cmd(SQR);
        assertX(9, Unit.OFL); // can't display m^-8
        cmd(SQRT);
        assertX(3, "/m¼"); // everything ok
        cmd(UNIT_SET_INV, findUnit("s"));
        assertX(3, "/m¼·s");
        cmd(UNIT_SET, findUnit("m"));
        cmd(UNIT_SET, findUnit("m"));
        cmd(UNIT_SET, findUnit("m"));
        assertX(3, "m¹·s¹"); // meaning: m^-1*s^-1
        type(9);
        cmd(YPOWX);
        assertX(19683, Unit.OFL); // definitely overflow
    }

    public void test_UNIT_SET_INV_complex() {
        enter(3, 2);
        cmd(UNIT_SET_INV, findUnit("m"));
        assertX(3, 2, "m¹"); // meaning: m^-1
    }

    public void test_UNIT_CONVERT() {
        enter(3, "kg");
        cmd(UNIT_CONVERT, findUnit("g"));
        assertX(3000, "g");
    }

    public void test_UNIT_CONVERT_complex() {
        enter(3, 2, "kg");
        cmd(UNIT_CONVERT, findUnit("g"));
        assertX(3000, 2000, "g");
    }

    public void test_UNIT_CLEAR() {
        enter(2, "kg");
        cmd(UNIT_CLEAR);
        assertX(2);
    }

    public void test_UNIT_DESCRIBE() {
        enter(2, "kg");
        cmd(UNIT_DESCRIBE);
        assertEquals("The unit shown is the simplest form of the unit: kg", calc.getMessage());

        enter(2);
        cmd(UNIT_SET_INV, findUnit("m"));
        assertX(2, "m¹"); // meaning: m^-1
        cmd(UNIT_DESCRIBE);
        assertEquals("The unit shown is the simplest form of the unit: 1/m", calc.getMessage());

        leftoverStackElements = 2;
    }
    
    public void test_unit_error_handling() {
        enter(16, "m³");
        cmd(SQRT);
        assertX(4, Unit.ERR); // Can't sqrt m³
        cmd(ENTER); // Save the unit error

        cmd(SQRT);
        assertX(2); // Repeated unit errors "evaporate" 

        cmd(UNIT_SET, findUnit("m"));
        assertX(2, "m");
        cmd(ADD);
        assertX(6, Unit.ERR); // unit + err = err
        
        type(3);
        cmd(SUB);
        assertX(3); // no-unit + err = no-unit

        cmd(UNIT_SET, findUnit("m"));
        assertX(3, "m");
        cmd(FACT);
        assertX(3*2*1, Unit.ERR);
        cmd(FACT);
        assertX(6*5*4*3*2*1); // Repeated unit errors "evaporate"
    }

    public void test_XCHG() {
        enter(12);
        type(2);
        cmd(XCHG);
        assertY(2);
        assertX(12);

        leftoverStackElements = 2;
    }

    public void test_CLS() {
        enter(12);
        type(2);
        cmd(CLS);
        assertSame(ComplexMatrixElement.empty, calc.getStack().element(1, 0, numDigits));
        assertSame(ComplexMatrixElement.empty, calc.getStack().element(0, 0, numDigits));
        
        stackPreserved = false;
    }

    public void test_RCLST() {
        enter(12);
        type(2);
        cmd(RCLST, 1);
        assertZ(12);
        assertY(2);
        assertX(12);

        leftoverStackElements = 3;
    }

    public void test_LASTX() {
        enter(12);
        type(2);
        cmd(ADD);
        cmd(LASTX);
        assertY(14);
        assertX(2);
        cmd(LASTX);
        assertY(2);
        assertX(2);

        leftoverStackElements = 3;
    }

    public void test_UNDO() {
        enter(12);
        type(2);
        cmd(ADD);
        cmd(UNDO);
        assertY(12);
        assertX(2);

        leftoverStackElements = 2;
    }

    public void test_XCHGST() {
        enter(1);
        enter(2);
        type(3);
        cmd(XCHGST);
        assertZ(1);
        assertY(2);
        assertX(3);
        cmd(XCHGST, 1);
        assertZ(1);
        assertY(3);
        assertX(2);
        cmd(XCHGST, 2);
        assertZ(2);
        assertY(3);
        assertX(1);

        leftoverStackElements = 3;
    }

    public void test_ROLLDN() {
        cmd(CLS);
        cmd(ROLLDN);
        assertX("");
        enter(1);
        enter(2);
        type(3);
        cmd(ROLLDN);
        assertZ(3);
        assertY(1);
        assertX(2);
        cmd(ROLLDN);
        assertZ(2);
        assertY(3);
        assertX(1);
        cmd(ROLLDN);
        assertZ(1);
        assertY(2);
        assertX(3);

        stackPreserved = false;
    }

    public void test_ROLLUP() {
        cmd(CLS);
        cmd(ROLLUP);
        assertX("");
        enter(1);
        enter(2);
        type(3);
        cmd(ROLLUP);
        assertZ(2);
        assertY(3);
        assertX(1);
        cmd(ROLLUP);
        assertZ(3);
        assertY(1);
        assertX(2);
        cmd(ROLLUP);
        assertZ(1);
        assertY(2);
        assertX(3);

        stackPreserved = false;
    }

    public void test_STO() {
        type(-3.6);
        cmd(STO, 5);
        cmd(MONITOR_MEM, 10);
        assertEquals("-3.6", calc.getMonitor().element(5, 0, numDigits));
    }

    public void test_STP() {
        type(-3.6);
        cmd(STO, 5);
        cmd(STP, 5);
        cmd(MONITOR_MEM, 10);
        assertEquals("-7.2", calc.getMonitor().element(5, 0, numDigits));
    }

    public void test_RCL() {
        type(-3.6);
        cmd(STO, 5);
        clx();
        cmd(RCL, 5);
        assertX(-3.6);
    }

    public void test_XCHGMEM() {
        type(-3.6);
        cmd(STO, 5);
        clx();
        type(4);
        cmd(MONITOR_MEM, 10);
        cmd(XCHGMEM, 5);
        assertX(-3.6);
        assertNull(calc.getStack().label(0, 0));
        assertEquals("4", calc.getMonitor().element(5, 0, numDigits));
        assertEquals("M5", calc.getMonitor().label(5, 0));
    }

    public void test_RCL_X() {
        type(-3.6);
        cmd(STO, 5);
        clx();
        type(5);
        cmd(RCL_X);
        assertX(-3.6);
        type(55);
        cmd(RCL_X);
        assertX("nan");

        leftoverStackElements = 4;
    }

    public void test_STO_X() {
        enter(-3.6);
        type(5);
        cmd(STO_X);
        cmd(MONITOR_MEM, 10);
        type(55);
        cmd(STO_X); // Should do nothing
        assertEquals("-3.6", calc.getMonitor().element(5, 0, numDigits));

        leftoverStackElements = 3;
    }

    public void test_STP_X() {
        enter(-3.6);
        cmd(STO, 5);
        type(5);
        cmd(STP_X);
        cmd(MONITOR_MEM, 10);
        assertEquals("-7.2", calc.getMonitor().element(5, 0, numDigits));

        leftoverStackElements = 2;
    }

    public void test_CLMEM() {
        type(-3.6);
        cmd(STO, 5);
        cmd(CLMEM);
        cmd(RCL, 5);
        assertX(0);

        cmd(MONITOR_MEM, 10);
        type(-3.6);
        cmd(STO, 5);
        cmd(CLMEM);
        assertEquals("0", calc.getMonitor().element(5, 0, numDigits));

        leftoverStackElements = 3;
    }

    public void test_SUMPL() {
        enter(2);
        type(3);
        cmd(SUMPL);
        assertZ(2);
        assertY(3);
        assertX(1);
        cmd(SUMX);
        assertX(3);
        cmd(SUMY);
        assertX(2);

        leftoverStackElements = 5;
    }

    public void test_SUMMI() {
        enter(2);
        type(3);
        cmd(SUMPL);
        clx();
        cmd(SUMMI);
        assertZ(2);
        assertY(3);
        assertX(0);
        cmd(SUMX);
        assertX(0);
        cmd(SUMY);
        assertX(0);

        leftoverStackElements = 5;
    }

    private void enterSomeStatistics() {
        enter(3);
        type(2);
        cmd(SUMPL);
        enter(1);
        type(4);
        cmd(SUMPL);

        clx(6);
    }

    public void test_CLST() {
        enterSomeStatistics();
        cmd(CLST);
        cmd(SUMX);
        assertX(0);
        cmd(SUMY);
        assertX(0);

        cmd(MONITOR_STAT, 10);
        enterSomeStatistics();
        cmd(CLST);
        assertEquals("0", calc.getMonitor().element(5, 0, numDigits));

        leftoverStackElements = 2;
    }

    public void test_AVG() {
        enterSomeStatistics();
        cmd(AVG);
        assertY(2);
        assertX(3);

        leftoverStackElements = 2;
    }

    public void test_AVGXW() {
        enterSomeStatistics();
        cmd(AVGXW);
        assertX((3.0*2.0+1.0*4.0)/(1.0+3.0));
    }

    public void test_STDEV() {
        enterSomeStatistics();
        cmd(STDEV);
        assertY("1.414213562373095");
        assertX("1.414213562373095");

        leftoverStackElements = 2;
    }

    public void test_PSTDEV() {
        enterSomeStatistics();
        cmd(PSTDEV);
        assertY(1);
        assertX(1);

        leftoverStackElements = 2;
    }

    public void test_LIN_AB() {
        enterSomeStatistics();
        cmd(LIN_AB);
        assertY(5);
        assertX(-1);

        leftoverStackElements = 2;
    }

    public void test_LIN_YEST() {
        enterSomeStatistics();
        type(2);
        cmd(LIN_YEST);
        assertX(3);
        type(4);
        cmd(LIN_YEST);
        assertX(1);

        leftoverStackElements = 2;
    }

    public void test_LIN_XEST() {
        enterSomeStatistics();
        type(3);
        cmd(LIN_XEST);
        assertX(2);
        type(1);
        cmd(LIN_XEST);
        assertX(4);

        leftoverStackElements = 2;
    }

    public void test_LIN_R() {
        enterSomeStatistics();
        cmd(LIN_R);
        assertX(-1);
    }

    public void test_LOG_AB() {
        enterSomeStatistics();
        cmd(LOG_AB);
        assertY(5);
        assertX("-2.885390081777927");

        leftoverStackElements = 2;
    }

    public void test_LOG_YEST() {
        enterSomeStatistics();
        type(2);
        cmd(LOG_YEST);
        assertX(3);
        type(4);
        cmd(LOG_YEST);
        assertX(1);

        leftoverStackElements = 2;
    }

    public void test_LOG_XEST() {
        enterSomeStatistics();
        type(3);
        cmd(LOG_XEST);
        assertX(2);
        type(1);
        cmd(LOG_XEST);
        assertX(4);

        leftoverStackElements = 2;
    }

    public void test_LOG_R() {
        enterSomeStatistics();
        cmd(LOG_R);
        assertX(-1);
    }

    public void test_EXP_AB() {
        enterSomeStatistics();
        cmd(EXP_AB);
        assertY(9);
        assertX("-0.5493061443340548");

        leftoverStackElements = 2;
    }

    public void test_EXP_YEST() {
        enterSomeStatistics();
        type(2);
        cmd(EXP_YEST);
        assertX(3);
        type(4);
        cmd(EXP_YEST);
        assertX(1);

        leftoverStackElements = 2;
    }

    public void test_EXP_XEST() {
        enterSomeStatistics();
        type(3);
        cmd(EXP_XEST);
        assertX(2);
        type(1);
        cmd(EXP_XEST);
        assertX(4);

        leftoverStackElements = 2;
    }

    public void test_EXP_R() {
        enterSomeStatistics();
        cmd(EXP_R);
        assertX(-1);
    }

    public void test_POW_AB() {
        enterSomeStatistics();
        cmd(POW_AB);
        assertY(9);
        assertX("-1.584962500721156");

        leftoverStackElements = 2;
    }

    public void test_POW_YEST() {
        enterSomeStatistics();
        type(2);
        cmd(POW_YEST);
        assertX(3);
        type(4);
        cmd(POW_YEST);
        assertX(1);

        leftoverStackElements = 2;
    }

    public void test_POW_XEST() {
        enterSomeStatistics();
        type(3);
        cmd(POW_XEST);
        assertX(2);
        type(1);
        cmd(POW_XEST);
        assertX(4);

        leftoverStackElements = 2;
    }

    public void test_POW_R() {
        enterSomeStatistics();
        cmd(POW_R);
        assertX(-1);
    }

    public void test_N() {
        enterSomeStatistics();
        cmd(N);
        assertX(2);
    }

    public void test_SUMX() {
        enterSomeStatistics();
        cmd(SUMX);
        assertX(6);
    }

    public void test_SUMXX() {
        enterSomeStatistics();
        cmd(SUMXX);
        assertX(20);
    }

    public void test_SUMLNX() {
        enterSomeStatistics();
        cmd(SUMLNX);
        assertX("2.079441541679836");
    }

    public void test_SUMLN2X() {
        enterSomeStatistics();
        cmd(SUMLN2X);
        assertX("2.402265069591007");
    }

    public void test_SUMY() {
        enterSomeStatistics();
        cmd(SUMY);
        assertX(4);
    }

    public void test_SUMYY() {
        enterSomeStatistics();
        cmd(SUMYY);
        assertX(10);
    }

    public void test_SUMLNY() {
        enterSomeStatistics();
        cmd(SUMLNY);
        assertX("1.09861228866811");
    }

    public void test_SUMLN2Y() {
        enterSomeStatistics();
        cmd(SUMLN2Y);
        assertX("1.206948960812582");
    }

    public void test_SUMXY() {
        enterSomeStatistics();
        cmd(SUMXY);
        assertX(10);
    }

    public void test_SUMXLNY() {
        enterSomeStatistics();
        cmd(SUMXLNY);
        assertX("2.197224577336219");
    }

    public void test_SUMYLNX() {
        enterSomeStatistics();
        cmd(SUMYLNX);
        assertX("3.465735902799727");
    }

    public void test_SUMLNXLNY() {
        enterSomeStatistics();
        cmd(SUMLNXLNY);
        assertX("0.761500010418809");
    }

    public void test_STAT_RCL() {
        enterSomeStatistics();
        cmd(STAT_RCL, 2); // SUMXX
        assertX(20);
    }

    public void test_STAT_STO() {
        enterSomeStatistics();
        enter(33);
        cmd(STAT_STO, 2); // SUMXX
        clx();
        cmd(SUMXX);
        assertX(33);
    }

    public void test_NORM() {
        cmd(PI);
        cmd(FIX, 4);
        cmd(NORM);
        assertX("3.141592653589793");
    }

    public void test_FIX() {
        cmd(PI);
        cmd(FIX, 4);
        cmd(FIX, 4);
        assertX("3.1416");
    }

    public void test_SCI() {
        type(314159.265);
        cmd(SCI, 4);
        cmd(SCI, 4);
        assertX("3.1416e5");
    }

    public void test_ENG() {
        type(314159.265);
        cmd(ENG, 4);
        cmd(ENG, 4);
        assertX("314.1593e3");
    }

    public void test_POINT_DOT() {
        type(314159.265);
        cmd(THOUSAND_DOT);
        cmd(POINT_COMMA);
        cmd(POINT_DOT);
        assertX("314,159.265");
    }

    public void test_POINT_COMMA() {
        type(314159.265);
        cmd(THOUSAND_DOT);
        cmd(POINT_COMMA);
        assertX("314.159,265");
    }

    public void test_POINT_REMOVE() {
        type(2);
        cmd(POINT_KEEP);
        cmd(POINT_REMOVE);
        assertX(2);
    }

    public void test_POINT_KEEP() {
        type(2);
        cmd(POINT_KEEP);
        assertX("2.");
    }

    public void test_THOUSAND_DOT() {
        type(2000);
        cmd(THOUSAND_DOT);
        assertX("2,000");

        cmd(THOUSAND_NONE);
        cmd(POINT_COMMA);
        cmd(THOUSAND_DOT);
        assertX("2.000");
    }

    public void test_THOUSAND_SPACE() {
        type(2000);
        cmd(THOUSAND_SPACE);
        assertX("2 000");
    }

    public void test_THOUSAND_QUOTE() {
        type(2000);
        cmd(THOUSAND_QUOTE);
        assertX("2'000");
    }

    public void test_THOUSAND_NONE() {
        type(2000);
        cmd(THOUSAND_DOT);
        cmd(THOUSAND_NONE);
        assertX(2000);
    }

    public void test_BASE_BIN() {
        type(20);
        cmd(BASE_BIN);
        assertX("000000000000000000000000010100");
    }

    public void test_BASE_OCT() {
        type(20);
        cmd(BASE_OCT);
        assertX("000000000000000000000000000024");
    }

    public void test_BASE_DEC() {
        type(20);
        cmd(BASE_BIN);
        cmd(BASE_DEC);
        assertX(20);
    }

    public void test_BASE_HEX() {
        type(20);
        cmd(BASE_HEX);
        assertX("000000000000000000000000000014");
    }

    public void test_FINANCE_STO() {
        type(-3.6);
        cmd(FINANCE_STO, 3);
        cmd(MONITOR_FINANCE, 10);
        assertEquals("-3.6", calc.getMonitor().element(3, 0, numDigits));
    }

    public void test_FINANCE_RCL() {
        type(-3.6);
        cmd(FINANCE_STO, 3);
        clx();
        cmd(FINANCE_RCL, 3);
        assertX(-3.6);
    }

    public void test_FINANCE_SOLVE() {
        type(3);
        cmd(FINANCE_STO, 2);   // np
        type(50);
        cmd(FINANCE_STO, 3);   // pmt
        type(5);
        cmd(FINANCE_STO, 4);   // ir%
        cmd(FINANCE_SOLVE, 1); // fv
        assertX(-((50*1.05+50)*1.05+50));
        cmd(FINANCE_SOLVE, 0); // pv
        assertX(0);
        cmd(FINANCE_SOLVE, 2); // np
        assertX(3);
        cmd(FINANCE_SOLVE, 3); // pmt
        assertX(50);
        cmd(FINANCE_SOLVE, 4); // ir%
        assertX(5);

        leftoverStackElements = 8;
    }

    public void test_FINANCE_CLEAR() {
        type(-3.6);
        cmd(FINANCE_STO, 3);
        cmd(FINANCE_CLEAR, 3);
        cmd(FINANCE_RCL, 3);
        assertX(0);

        cmd(MONITOR_FINANCE);
        type(-3.6);
        cmd(FINANCE_STO, 3);
        cmd(FINANCE_CLEAR, 3);
        assertEquals("0", calc.getMonitor().element(3, 0, numDigits));

        leftoverStackElements = 3;
    }

    public void test_FINANCE_BGNEND() {
        cmd(FINANCE_BGNEND);
        assertTrue(calc.begin);
        type(3);
        cmd(FINANCE_STO, 2);   // np
        type(50);
        cmd(FINANCE_STO, 3);   // pmt
        type(5);
        cmd(FINANCE_STO, 4);   // ir%
        cmd(FINANCE_SOLVE, 1); // fv
        assertX(-(((50*1.05+50)*1.05+50))*1.05);

        leftoverStackElements = 4;
    }

    public void test_FINANCE_MULINT() {
        enter(5);
        type(3);
        cmd(FINANCE_MULINT);
        assertX(15.7625);
    }

    public void test_FINANCE_DIVINT() {
        enter(15.7625);
        type(3);
        cmd(FINANCE_DIVINT);
        assertX(5);
    }

    public void test_CONST_c() {
        cmd(CONST_c);
        assertX("299792458 m/s");
    }

    public void test_CONST_h() {
        cmd(CONST_h);
        assertX("6.62606896e-34 J·s");
    }

    public void test_CONST_mu_0() {
        cmd(CONST_mu_0);
        assertX("0.000001256637061435917 H/m");
    }

    public void test_CONST_eps_0() {
        cmd(CONST_eps_0);
        assertX("0.00000000000885418781762039 F/m");
    }

    public void test_CONST_NA() {
        cmd(CONST_NA);
        assertX("6.02214179e23 mol¹");
    }

    public void test_CONST_R() {
        cmd(CONST_R);
        assertX("8.314472 J/mol·K");
    }

    public void test_CONST_k() {
        numDigits = 16;
        cmd(CONST_k);
        assertX("1.3806504e-23 J/K");
    }

    public void test_CONST_F() {
        cmd(CONST_F);
        assertX("96485.3399 C/mol");
    }

    public void test_CONST_alpha() {
        cmd(CONST_alpha);
        assertX("0.0072973525376");
    }

    public void test_CONST_a_0() {
        cmd(CONST_a_0);
        assertX("0.000000000052917720859 m");
    }

    public void test_CONST_R_inf() {
        cmd(CONST_R_inf);
        assertX("10973731.568527 m¹");
    }

    public void test_CONST_mu_B() {
        numDigits = 16;
        cmd(CONST_mu_B);
        assertX("9.27400915e-24 A·m²");
    }

    public void test_CONST_e() {
        numDigits = 16;
        cmd(CONST_e);
        assertX("1.602176487e-19 C");
    }

    public void test_CONST_m_e() {
        cmd(CONST_m_e);
        assertX("9.10938215e-31 kg");
    }

    public void test_CONST_m_p() {
        numDigits = 16;
        cmd(CONST_m_p);
        assertX("1.672621637e-27 kg");
    }

    public void test_CONST_m_n() {
        numDigits = 16;
        cmd(CONST_m_n);
        assertX("1.674927211e-27 kg");
    }

    public void test_CONST_m_u() {
        numDigits = 16;
        cmd(CONST_m_u);
        assertX("1.660538782e-27 kg");
    }

    public void test_CONST_G() {
        cmd(CONST_G);
        assertX("0.0000000000667428 m³/kg·s²");
    }

    public void test_CONST_g_n() {
        cmd(CONST_g_n);
        assertX("9.80665 m/s²");
    }

    public void test_CONST_ly() {
        cmd(CONST_ly);
        assertX("9460730472580800 m");
    }

    public void test_CONST_AU() {
        cmd(CONST_AU);
        assertX("149597870691 m");
    }

    public void test_CONST_pc() {
        cmd(CONST_pc);
        assertX("3.085677581305729e16 m");
    }

    public void test_CONST_km_mi() {
        cmd(CONST_km_mi);
        assertX("1.609344");
    }

    public void test_CONST_m_ft() {
        cmd(CONST_m_ft);
        assertX("0.3048");
    }

    public void test_CONST_cm_in() {
        cmd(CONST_cm_in);
        assertX("2.54");
    }

    public void test_CONST_km_nm() {
        cmd(CONST_km_nm);
        assertX("1.852");
    }

    public void test_CONST_m_yd() {
        cmd(CONST_m_yd);
        assertX("0.9144");
    }

    public void test_CONST_g_oz() {
        cmd(CONST_g_oz);
        assertX("28.349523125");
    }

    public void test_CONST_kg_lb() {
        cmd(CONST_kg_lb);
        assertX("0.45359237");
    }

    public void test_CONST_mg_gr() {
        cmd(CONST_mg_gr);
        assertX("64.79891");
    }

    public void test_CONST_kg_ton() {
        cmd(CONST_kg_ton);
        assertX("907.18474");
    }

    public void test_CONST_J_cal() {
        cmd(CONST_J_cal);
        assertX("4.1868");
    }

    public void test_CONST_J_Btu() {
        cmd(CONST_J_Btu);
        assertX("1055.06");
    }

    public void test_CONST_W_hp() {
        cmd(CONST_W_hp);
        assertX("745.7");
    }

    public void test_CONST_l_pt() {
        cmd(CONST_l_pt);
        assertX("0.473176473");
    }

    public void test_CONST_l_cup() {
        cmd(CONST_l_cup);
        assertX("0.2365882365");
    }

    public void test_CONST_l_gal() {
        cmd(CONST_l_gal);
        assertX("3.785411784");
    }

    public void test_CONST_ml_floz() {
        cmd(CONST_ml_floz);
        assertX("29.5735295625");
    }

    public void test_CONST_K_C() {
        cmd(CONST_K_C);
        assertX("273.15");
    }

    public void test_CONV_C_F() {
        type(0);
        cmd(CONV_C_F);
        assertX(32);

        type(5);
        cmd(CONV_C_F);
        assertX(41);

        leftoverStackElements = 2;
    }

    public void test_CONV_F_C() {
        type(32);
        cmd(CONV_F_C);
        assertX(0);

        type(41);
        cmd(CONV_F_C);
        assertX(5);

        leftoverStackElements = 2;
    }

    public void test_MIN() {
        enter(2, "m");
        enter(4, "ft");
        cmd(MIN);
        assertX(4, "ft");

        enter(1000, "m");
        enter(2, "km");
        cmd(MIN);
        assertX(1000, "m");

        enter(-1);
        type(3);
        cmd(MIN);
        assertX(-1);

        enter(0);
        enter();
        cmd(DIV);
        type(3);
        cmd(MIN);
        assertX("nan");

        type(0);
        cmd(RECIP);
        cmd(UNIT_SET, findUnit("g"));
        type(0);
        cmd(RECIP);
        cmd(UNIT_SET, findUnit("kg"));
        assertX("inf kg");
        cmd(MIN);
        assertX("inf kg");

        leftoverStackElements = 5;
    }

    public void test_MAX() {
        enter(2);
        type(4);
        cmd(MAX);
        assertX(4);

        enter(-1);
        type(3);
        cmd(MAX);
        assertX(3);

        enter(1000, "m");
        enter(2, "kg");
        cmd(MAX);
        assertEquals("Comparison of numbers with incompatible units ignored", calc.getMessage());
        assertX("nan");

        enter(0);
        enter();
        cmd(DIV);
        type(3);
        cmd(MAX);
        assertX("nan");

        type(0);
        cmd(RECIP);
        cmd(UNIT_SET, findUnit("g"));
        type(0);
        cmd(RECIP);
        cmd(UNIT_SET, findUnit("kg"));
        assertX("inf kg");
        cmd(MAX);
        assertX("inf kg");

        leftoverStackElements = 5;
    }

    public void test_SELECT() {
        enter(2);
        enter(4);
        type(0.75);
        cmd(SELECT);
        assertX(3.5);
    }

    public void test_SGN() {
        type(5);
        cmd(SGN);
        assertX(1);

        type(-5);
        cmd(SGN);
        assertX(-1);

        type(0);
        cmd(SGN);
        assertX(1);

        leftoverStackElements = 3;
    }

    public void test_PUSH_INT() {
        cmd(PUSH_INT, 12);
        assertX(12);
    }

    public void test_PUSH_INT_N() {
        cmd(PUSH_INT_N, 12);
        assertX(-12);
    }

    public void test_PUSH_INF() {
        cmd(PUSH_INF);
        assertX("inf");
    }

    public void test_PUSH_INF_N() {
        cmd(PUSH_INF_N);
        assertX("-inf");
    }

    public void test_PROG_NEW() {
        calc.newProgramName = "tst";
        cmd(PROG_NEW, 3);
        assertTrue(calc.progRecording);
        assertEquals("tst", calc.progLabel(3));
        enter();
        cmd(MONITOR_PROG, 10);
        calc.getMonitor().setDisplayedSize(10, 1);
        assertEquals("ENTER", calc.getMonitor().elementSuffix(0, 0));
        assertEquals("[prg end]", calc.getMonitor().elementSuffix(1, 0));
    }

    public void test_PROG_FINISH() {
        test_PROG_NEW();
        cmd(PROG_FINISH);
        assertFalse(calc.progRecording);
        assertEquals("tst", calc.progLabel(3));
    }

    public void test_PROG_APPEND() {
        test_PROG_FINISH();
        cmd(PROG_APPEND, 3);
        assertTrue(calc.progRecording);
        cmd(CLEAR);
        assertEquals("clear", calc.getMonitor().elementSuffix(1, 0));
        assertEquals("[prg end]", calc.getMonitor().elementSuffix(2, 0));

        leftoverStackElements = 0;
    }

    public void test_PROG_PURGE() {
        test_PROG_NEW();
        cmd(PROG_PURGE);
        assertEquals("[prg end]", calc.getMonitor().elementSuffix(0, 0));
        cmd(CLEAR);
        assertEquals("clear", calc.getMonitor().elementSuffix(0, 0));
        assertEquals("[prg end]", calc.getMonitor().elementSuffix(1, 0));

        leftoverStackElements = 0;
    }

    public void test_PROG_CLEAR() {
        test_PROG_FINISH();
        cmd(PROG_CLEAR, 3);
        assertEquals(Program.emptyProg, calc.progLabel(3));
    }

    public void test_PROG_RUN() {
        test_PROG_FINISH();
        type(7.7);
        cmd(PROG_RUN, 3);
        assertY(7.7);
        assertX(7.7);

        leftoverStackElements = 3;
    }

    public void test_IF_EQUAL() {
        calc.newProgramName = "tst";
        cmd(PROG_NEW, 0);
        cmd(IF_EQUAL);
        type(100);
        cmd(PROG_FINISH);
        clx();
        
        enter(1);
        type(2);
        cmd(PROG_RUN, 0);
        assertX(2);
        clx(2);
        
        enter(3);
        type(3);
        cmd(PROG_RUN, 0);
        assertX(100);
        clx(3);
        
        enter(3, "kg");
        enter(3000, "g");
        cmd(PROG_RUN, 0);
        assertX(100);
        clx(3);
        
        enter(4, 4, "km");
        enter(4000, 4000, "m");
        cmd(PROG_RUN, 0);
        assertX(100);
        clx(3);

        type(0);
        cmd(RECIP);
        enter();
        cmd(TO_CPLX);
        assertX("inf+infi");
        enter();
        cmd(CPLX_CONJ);
        cmd(NEG);
        cmd(PROG_RUN, 0);
        assertX("-inf+infi");
        clx(2);

        enter(new double[][] {{1,2},{3,4}});
        enter(new double[][] {{1,2},{3,4}});
        cmd(PROG_RUN, 0);
        assertX(100);
        clx(3);

        enter(new double[][] {{1,2},{3,4}});
        type(2);
        assertNull(calc.getMessage());
        cmd(PROG_RUN, 0);
        assertEquals("Comparison of matrix and number ignored", calc.getMessage()); 
        assertX(2);
        clx(2);

        leftoverStackElements = 0;
    }

    public void test_IF_NEQUAL() {
        calc.newProgramName = "tst";
        cmd(PROG_NEW, 0);
        cmd(IF_NEQUAL);
        type(100);
        cmd(PROG_FINISH);
        
        enter(1);
        type(2);
        cmd(PROG_RUN, 0);
        assertX(100);
        
        enter(3);
        type(3);
        cmd(PROG_RUN, 0);
        assertX(3);
        
        type(0);
        cmd(RECIP);
        enter();
        cmd(TO_CPLX);
        assertX("inf+infi");
        enter();
        cmd(CPLX_CONJ);
        cmd(NEG);
        cmd(PROG_RUN, 0);
        assertX(100);
        clx(3);

        leftoverStackElements = 6;
    }

    public void test_IF_LESS() {
        calc.newProgramName = "tst";
        cmd(PROG_NEW, 0);
        cmd(IF_LESS);
        type(100);
        cmd(PROG_FINISH);
        
        enter(1);
        type(2);
        cmd(PROG_RUN, 0);
        assertX(2);
        
        enter(4);
        type(3);
        cmd(PROG_RUN, 0);
        assertX(100);
        
        enter(5,6);
        enter(7,8);
        cmd(PROG_RUN, 0);
        assertEquals("Magnitude comparison of complex numbers ignored", calc.getMessage()); 
        assertX(7,8);

        leftoverStackElements = 8;
    }

    public void test_IF_LEQUAL() {
        calc.newProgramName = "tst";
        cmd(PROG_NEW, 0);
        cmd(IF_LEQUAL);
        type(100);
        cmd(PROG_FINISH);
        
        enter(1);
        type(2);
        cmd(PROG_RUN, 0);
        assertX(2);
        
        enter(4);
        type(3);
        cmd(PROG_RUN, 0);
        assertX(100);
        
        enter(5);
        type(5);
        cmd(PROG_RUN, 0);
        assertX(100);
        
        leftoverStackElements = 9;
    }

    public void test_IF_GREATER() {
        calc.newProgramName = "tst";
        cmd(PROG_NEW, 0);
        cmd(IF_GREATER);
        type(100);
        cmd(PROG_FINISH);
        
        enter(1);
        type(2);
        cmd(PROG_RUN, 0);
        assertX(100);
        
        enter(3);
        type(3);
        cmd(PROG_RUN, 0);
        assertX(3);
        
        leftoverStackElements = 6;
    }

    public void test_IF_EQUAL_Z() {
        calc.newProgramName = "tst";
        cmd(PROG_NEW, 0);
        cmd(IF_EQUAL_Z);
        type(100);
        cmd(PROG_FINISH);
        
        type(2);
        cmd(PROG_RUN, 0);
        assertY(100);
        assertX(2);
        
        type(0);
        cmd(PROG_RUN, 0);
        assertX(100);
        
        leftoverStackElements = 4;
    }

    public void test_IF_NEQUAL_Z() {
        calc.newProgramName = "tst";
        cmd(PROG_NEW, 0);
        cmd(IF_NEQUAL_Z);
        type(100);
        cmd(PROG_FINISH);
        clx();
        
        type(2);
        cmd(PROG_RUN, 0);
        assertX(100);
        clx(2);
        
        type(0);
        cmd(PROG_RUN, 0);
        assertX(0);
        clx();
        
        enter(0, "kg");
        cmd(PROG_RUN, 0);
        assertX(0, "kg");
        clx();

        enter(new double[3][3]);
        cmd(PROG_RUN, 0);
        assertX(new double[3][3]);
        clx();

        enter(new double[][] {{1,2},{3,4}});
        cmd(PROG_RUN, 0);
        assertX(100);
        clx(2);

        leftoverStackElements = 0;
    }

    public void test_IF_LESS_Z() {
        calc.newProgramName = "tst";
        cmd(PROG_NEW, 0);
        cmd(IF_LESS_Z);
        type(100);
        cmd(PROG_FINISH);

        type(2);
        cmd(PROG_RUN, 0);
        assertX(2);
        
        type(0);
        cmd(PROG_RUN, 0);
        assertX(0);
        
        enter(new double[][] {{2,3}}); // A matrix here should not matter
        type(-1);
        cmd(PROG_RUN, 0);
        assertX(100);
        
        enter(new double[][] {{4,5}});
        cmd(PROG_RUN, 0);
        assertEquals("Magnitude comparison of matrices ignored", calc.getMessage()); 
        assertX(new double[][] {{4,5}});
        
        enter(6,7);
        cmd(PROG_RUN, 0);
        assertEquals("Magnitude comparison of complex numbers ignored", calc.getMessage()); 
        assertX(6,7);
        
        leftoverStackElements = 8;
    }

    public void test_IF_LEQUAL_Z() {
        calc.newProgramName = "tst";
        cmd(PROG_NEW, 0);
        cmd(IF_LEQUAL_Z);
        type(100);
        cmd(PROG_FINISH);
        
        type(2);
        cmd(PROG_RUN, 0);
        assertX(2);
        
        type(0);
        cmd(PROG_RUN, 0);
        assertX(100);
        
        type(-1);
        cmd(PROG_RUN, 0);
        assertX(100);
        
        leftoverStackElements = 6;
    }

    public void test_IF_GREATER_Z() {
        calc.newProgramName = "tst";
        cmd(PROG_NEW, 0);
        cmd(IF_GREATER_Z);
        type(100);
        cmd(PROG_FINISH);
        
        type(2);
        cmd(PROG_RUN, 0);
        assertX(100);
        
        type(0);
        cmd(PROG_RUN, 0);
        assertX(0);
        
        type(-1);
        cmd(PROG_RUN, 0);
        assertX(-1);

        leftoverStackElements = 5;
    }

    public void test_LBL() {
        calc.newProgramName = "tst";
        cmd(PROG_NEW, 0);
        cmd(LBL, 5);
        cmd(PROG_FINISH);

        leftoverStackElements = 0;
    }

    public void test_GTO() {
        calc.newProgramName = "tst";
        cmd(PROG_NEW, 0);
        cmd(IF_EQUAL_Z);
        cmd(GTO, 5);
        type(100);
        cmd(LBL, 5);
        cmd(PROG_FINISH);

        type(2);
        cmd(PROG_RUN, 0);
        assertX(100);
        
        type(0);
        cmd(PROG_RUN, 0);
        assertX(0);
        
        leftoverStackElements = 4;
    }

    public void test_STOP() {
        calc.newProgramName = "tst";
        cmd(PROG_NEW, 0);
        cmd(IF_EQUAL_Z);
        cmd(STOP);
        type(100);
        cmd(PROG_FINISH);

        type(2);
        cmd(PROG_RUN, 0);
        assertX(100);
        
        type(0);
        cmd(PROG_RUN, 0);
        assertX(0);
        
        leftoverStackElements = 4;
    }

    public void test_GSB() {
        calc.newProgramName = "tst";
        cmd(PROG_NEW, 0);
        cmd(IF_EQUAL_Z);
        cmd(GSB, 5);
        type(100);
        cmd(LBL, 5);
        cmd(PROG_FINISH);

        type(2);
        cmd(PROG_RUN, 0);
        assertX(100);
        
        type(0);
        cmd(PROG_RUN, 0);
        assertX(0);
        
        leftoverStackElements = 4;
    }

    public void test_RTN() {
        calc.newProgramName = "tst";
        cmd(PROG_NEW, 0);
        cmd(IF_EQUAL_Z);
        cmd(GSB, 5);
        type(200);
        cmd(STOP);
        cmd(LBL, 5);
        type(100);
        cmd(RTN);
        cmd(PROG_FINISH);

        type(2);
        cmd(PROG_RUN, 0);
        assertY(2);
        assertX(200);
        
        type(0);
        cmd(PROG_RUN, 0);
        assertZ(0);
        assertY(100);
        assertX(200);
        
        leftoverStackElements = 7;
    }

    public void test_DSE() {
        type(2); // Avoid messing up existing stack elements while programming

        calc.newProgramName = "tst";
        cmd(PROG_NEW, 0);
        cmd(LBL, 5);
        cmd(SQR);
        cmd(DSE, 2);
        cmd(GTO, 5);
        cmd(PROG_FINISH);

        type(1000.90099);
        cmd(STO, 2);
        type(2);
        cmd(PROG_RUN, 0);
        assertX(16);
        
        leftoverStackElements = 3;
    }

    public void test_ISG() {
        type(2); // Avoid messing up existing stack elements while programming

        calc.newProgramName = "tst";
        cmd(PROG_NEW, 0);
        cmd(LBL, 5);
        cmd(SQR);
        cmd(ISG, 2);
        cmd(GTO, 5);
        cmd(PROG_FINISH);

        type(80.16080);
        cmd(STO, 2);
        type(2);
        cmd(PROG_RUN, 0);
        assertX(16);
        
        leftoverStackElements = 3;
    }

    public void test_PROG_DIFF() {
        type(2); // Avoid messing up existing stack elements while programming

        calc.newProgramName = "tst";
        cmd(PROG_NEW, 0);
        cmd(SQR);
        cmd(PROG_FINISH);

        type(2);
        cmd(PROG_DIFF, 0);
        assertX(4);
        
        leftoverStackElements = 3;
    }

    public void test_MONITOR_NONE() {
        cmd(MONITOR_MEM, 5);
        cmd(MONITOR_NONE);
        assertNull(calc.getMonitor());

        leftoverStackElements = 0;
    }

    public void test_MONITOR_MEM() {
        cmd(MONITOR_MEM, 5);
        assertFalse(calc.getMonitor().hasCaption());
        assertEquals(1, calc.getMonitor().cols());
        assertEquals("M0", calc.getMonitor().label(0, 0));
        type(3);
        cmd(STO, 0);
        enter(4,4);
        cmd(STO, 1);
        enter(5, "J");
        cmd(STO, 2);
        enter(new double[][] {{6,6},{6,6}});
        cmd(STO, 3);
        assertEquals("3", calc.getMonitor().element(0, 0, numDigits));
        assertEquals("4+4i", calc.getMonitor().element(1, 0, numDigits));
        assertEquals("5", calc.getMonitor().element(2, 0, numDigits));
        assertEquals(" J", calc.getMonitor().elementSuffix(2, 0));
        assertEquals("M:[2x2]", calc.getMonitor().element(3, 0, numDigits));
        
        leftoverStackElements = 4;
    }

    public void test_MONITOR_STAT() {
        cmd(MONITOR_STAT, 5);
        assertFalse(calc.getMonitor().hasCaption());
        assertEquals(1, calc.getMonitor().cols());
        assertEquals("n", calc.getMonitor().label(0, 0));
        assertEquals("ßx²", calc.getMonitor().label(2, 0));
        type(3);
        cmd(STAT_STO, 0);
        enter(4,4);
        cmd(STAT_STO, 1);
        enter(5, "J");
        cmd(STAT_STO, 2);
        enter(new double[][] {{6,6},{6,6}});
        cmd(STAT_STO, 3);
        assertEquals("3", calc.getMonitor().element(0, 0, numDigits));
        assertEquals("4", calc.getMonitor().element(1, 0, numDigits));
        assertEquals("5", calc.getMonitor().element(2, 0, numDigits));
        assertNull(calc.getMonitor().elementSuffix(2, 0));
        assertEquals("nan", calc.getMonitor().element(3, 0, numDigits));
        
        leftoverStackElements = 4;
    }

    public void test_MONITOR_FINANCE() {
        cmd(MONITOR_FINANCE, 5);
        assertFalse(calc.getMonitor().hasCaption());
        assertEquals(1, calc.getMonitor().cols());
        assertEquals("pv", calc.getMonitor().label(0, 0));
        assertEquals("np", calc.getMonitor().label(2, 0));
        type(3);
        cmd(FINANCE_STO, 0);
        enter(4,4);
        cmd(FINANCE_STO, 1);
        enter(5, "J");
        cmd(FINANCE_STO, 2);
        enter(new double[][] {{6,6},{6,6}});
        cmd(FINANCE_STO, 3);
        assertEquals("3", calc.getMonitor().element(0, 0, numDigits));
        assertEquals("4", calc.getMonitor().element(1, 0, numDigits));
        assertEquals("5", calc.getMonitor().element(2, 0, numDigits));
        assertNull(calc.getMonitor().elementSuffix(2, 0));
        assertEquals("nan", calc.getMonitor().element(3, 0, numDigits));
        
        leftoverStackElements = 4;
    }

    public void test_MONITOR_MATRIX() {
        cmd(CLS);
        cmd(MONITOR_MATRIX, 5);
        calc.getMonitor().setDisplayedSize(5, 1);
        assertTrue(calc.getMonitor().hasCaption());
        assertEquals(0, calc.getMonitor().rows());
        assertEquals("no matrix", calc.getMonitor().caption(0));

        enter(new double[][] {{1,2},{3,4}});
        assertEquals("Col:1", calc.getMonitor().caption(0));
        assertEquals("R1", calc.getMonitor().label(0, 0));
        assertEquals("R3", calc.getMonitor().label(2, 0));
        assertEquals("1", calc.getMonitor().element(0, 0, numDigits));
        assertEquals("3", calc.getMonitor().element(1, 0, numDigits));

        stackPreserved = false;
    }

    public void test_MONITOR_PROG() {
        calc.newProgramName = "tst";
        cmd(PROG_NEW, 3);
        enter();
        cmd(MONITOR_PROG, 10);
        calc.getMonitor().setDisplayedSize(10, 1);
        assertTrue(calc.getMonitor().hasCaption());
        assertEquals(2, calc.getMonitor().rows());
        assertEquals("Prog: tst", calc.getMonitor().caption(0));
        assertNull(calc.getMonitor().element(0, 0, numDigits));
        assertEquals("ENTER", calc.getMonitor().elementSuffix(0, 0));
        assertNull(calc.getMonitor().element(0, 0, numDigits));
        assertEquals("[prg end]", calc.getMonitor().elementSuffix(1, 0));
        
        cmd(PROG_FINISH);
        assertNull(calc.getMonitor());
    }

    public void test_MONITOR_ENTER() {
        cmd(MONITOR_MEM, 5);
        assertFalse(calc.isInsideMonitor);
        cmd(MONITOR_ENTER);
        assertTrue(calc.isInsideMonitor);

        cmd(MONITOR_EXIT);
        leftoverStackElements = 0;
    }

    public void test_MONITOR_EXIT() {
        cmd(MONITOR_MEM, 5);
        cmd(MONITOR_ENTER);
        assertTrue(calc.isInsideMonitor);
        cmd(MONITOR_EXIT);
        assertFalse(calc.isInsideMonitor);

        leftoverStackElements = 0;
    }

    public void test_MONITOR_UP() {
        cmd(MONITOR_MEM, 5);
        cmd(MONITOR_ENTER);
        assertTrue(calc.isInsideMonitor);
        assertEquals(0, calc.monitorRow);
        cmd(MONITOR_UP);
        assertEquals(15, calc.monitorRow);

        cmd(MONITOR_EXIT);
        leftoverStackElements = 0;
    }

    public void test_MONITOR_UP_matrix() {
        enter(new double[2][2]);
        cmd(MONITOR_MATRIX, 5);
        cmd(MONITOR_ENTER);
        assertTrue(calc.isInsideMonitor);
        assertEquals(0, calc.monitorRow);
        assertEquals(0, calc.monitorCol);
        cmd(MONITOR_UP);
        assertEquals(1, calc.monitorRow);
        assertEquals(0, calc.monitorCol);

        cmd(MONITOR_EXIT);
    }

    public void test_MONITOR_DOWN() {
        cmd(MONITOR_MEM, 5);
        cmd(MONITOR_ENTER);
        assertTrue(calc.isInsideMonitor);
        assertEquals(0, calc.monitorRow);
        assertEquals(0, calc.monitorCol);
        cmd(MONITOR_DOWN);
        assertEquals(1, calc.monitorRow);
        for (int i=0; i<14; i++)
            cmd(MONITOR_DOWN);
        assertEquals(15, calc.monitorRow);
        cmd(MONITOR_DOWN);
        assertEquals(0, calc.monitorRow);
        assertEquals(0, calc.monitorCol);

        cmd(MONITOR_EXIT);
        leftoverStackElements = 0;
    }

    public void test_MONITOR_DOWN_matrix() {
        enter(new double[2][2]);
        cmd(MONITOR_MATRIX, 5);
        cmd(MONITOR_ENTER);
        assertTrue(calc.isInsideMonitor);
        assertEquals(0, calc.monitorRow);
        assertEquals(0, calc.monitorCol);
        assertEquals("Col:1", calc.getMonitor().caption(calc.monitorCol));
        cmd(MONITOR_DOWN);
        assertEquals(1, calc.monitorRow);
        assertEquals(0, calc.monitorCol);
        cmd(MONITOR_DOWN);
        assertEquals(0, calc.monitorRow);
        assertEquals(1, calc.monitorCol);
        assertEquals("Col:2", calc.getMonitor().caption(calc.monitorCol));
        cmd(MONITOR_DOWN);
        assertEquals(1, calc.monitorRow);
        assertEquals(1, calc.monitorCol);
        cmd(MONITOR_DOWN);
        assertEquals(0, calc.monitorRow);
        assertEquals(1, calc.monitorCol);

        cmd(MONITOR_EXIT);
    }

    public void test_MONITOR_LEFT() {
        enter(new double[2][2]);
        cmd(MONITOR_MATRIX, 5);
        cmd(MONITOR_ENTER);
        assertTrue(calc.isInsideMonitor);
        assertEquals(0, calc.monitorRow);
        assertEquals(0, calc.monitorCol);
        assertEquals("Col:1", calc.getMonitor().caption(calc.monitorCol));
        cmd(MONITOR_LEFT);
        assertEquals(0, calc.monitorRow);
        assertEquals(1, calc.monitorCol);
        assertEquals("Col:2", calc.getMonitor().caption(calc.monitorCol));

        cmd(MONITOR_EXIT);
    }

    public void test_MONITOR_RIGHT() {
        enter(new double[2][2]);
        cmd(MONITOR_MATRIX, 5);
        cmd(MONITOR_ENTER);
        assertTrue(calc.isInsideMonitor);
        assertEquals(0, calc.monitorRow);
        assertEquals(0, calc.monitorCol);
        assertEquals("Col:1", calc.getMonitor().caption(calc.monitorCol));
        cmd(MONITOR_RIGHT);
        assertEquals(0, calc.monitorRow);
        assertEquals(1, calc.monitorCol);
        assertEquals("Col:2", calc.getMonitor().caption(calc.monitorCol));
        cmd(MONITOR_RIGHT);
        assertEquals(1, calc.monitorRow);
        assertEquals(0, calc.monitorCol);
        cmd(MONITOR_RIGHT);
        assertEquals(1, calc.monitorRow);
        assertEquals(1, calc.monitorCol);
        cmd(MONITOR_RIGHT);
        assertEquals(1, calc.monitorRow);
        assertEquals(0, calc.monitorCol);

        cmd(MONITOR_EXIT);
    }

    public void test_MONITOR_GET() {
        cmd(MONITOR_MEM, 5);
        type(3);
        cmd(STO, 0);
        enter(4,4);
        cmd(STO, 1);
        clx(2);
        cmd(MONITOR_ENTER);
        assertEquals(0, calc.monitorRow);
        cmd(MONITOR_GET);
        assertTrue(calc.isInsideMonitor);
        assertEquals(1, calc.monitorRow);
        assertX(3);
        cmd(MONITOR_GET);
        assertEquals(2, calc.monitorRow);
        assertX(4,4);
        
        cmd(MONITOR_EXIT);
        leftoverStackElements = 2;
    }

    public void test_MONITOR_PUT() {
        cmd(MONITOR_MEM, 5);
        type(3);
        cmd(MONITOR_ENTER);
        assertEquals(0, calc.monitorRow);
        assertEquals("0", calc.getMonitor().element(0, 0, numDigits));
        cmd(MONITOR_PUT);
        assertTrue(calc.isInsideMonitor);
        assertEquals(1, calc.monitorRow);
        assertEquals("3", calc.getMonitor().element(0, 0, numDigits));
        assertEquals("0", calc.getMonitor().element(1, 0, numDigits));
        cmd(MONITOR_PUT);
        assertEquals(2, calc.monitorRow);
        assertEquals("3", calc.getMonitor().element(1, 0, numDigits));
        
        cmd(MONITOR_EXIT);
    }

    public void test_MONITOR_PUSH() {
        enter(0);
        cmd(MONITOR_MEM, 5);
        type(3);
        cmd(MONITOR_ENTER);
        assertEquals(0, calc.monitorRow);
        cmd(MONITOR_PUSH);
        assertFalse(calc.isInsideMonitor);
        assertEquals(1, calc.monitorRow);
        assertEquals("3", calc.getMonitor().element(0, 0, numDigits));
        assertX(0);
        type(4);
        cmd(MONITOR_ENTER);
        cmd(MONITOR_PUSH);
        assertEquals(2, calc.monitorRow);
        assertEquals("4", calc.getMonitor().element(1, 0, numDigits));
    }

    public void testHasComplexArgs() {
        enter(2);
        assertFalse(calc.hasComplexArgs());
        enter(3,4);
        assertTrue(calc.hasComplexArgs());
        enter(5);
        assertTrue(calc.hasComplexArgs());
        enter(6);
        assertFalse(calc.hasComplexArgs());

        leftoverStackElements = 4;
    }

    private void fillState() {
        cmd(CLS);
        type(1);
        cmd(STO, 1);
        cmd(STAT_STO, 1);
        cmd(FINANCE_STO, 1);
        enter(2, 2);
        cmd(STO, 2);
        cmd(STAT_STO, 2);
        cmd(FINANCE_STO, 2);
        enter(3, "kg");
        cmd(STO, 3);
        cmd(STAT_STO, 3);
        cmd(FINANCE_STO, 3);
        enter(new double[][] {{1,2},{3,4}});
        cmd(STO, 4);
        cmd(STAT_STO, 4);
        cmd(FINANCE_STO, 4);
        enter(new double[][][] {{{1,1},{2,2}},{{3,3},{4,4}}});
        cmd(STO, 5);
        cmd(STAT_STO, 5);
        calc.newProgramName = "tst";
        cmd(PROG_NEW, 6);
        type(6);
        cmd(PROG_FINISH);

        enter(1.41, 3.14, "V");
        clx(); // Sets LASTx

        cmd(FINANCE_BGNEND);
        cmd(TRIG_DEGRAD);
        cmd(FIX, 5);
        cmd(BASE_HEX);
        cmd(POINT_COMMA);
        cmd(POINT_KEEP);
        cmd(THOUSAND_SPACE);
        cmd(MONITOR_MEM, 5);
    }

    private void verifyState(boolean hasUnits) {
        cmd(LASTX);
        type("14");
        enter();
        assertX("0000 0000 0000 0000 0014,00000"); // BASE_HEX, THOUSAND_SPACE, FIX 5, POINT_COMMA
        cmd(BASE_DEC);
        cmd(THOUSAND_NONE);
        cmd(NORM);
        cmd(POINT_DOT);
        assertX("20."); // POINT_KEEP
        cmd(POINT_REMOVE);
        assertTrue(calc.degrees);
        assertTrue(calc.begin);
        assertEquals(MONITOR_MEM, calc.monitorMode);
        assertEquals(5, calc.requestedMonitorSize);
        cmd(MONITOR_NONE);
        clx();

        if (hasUnits)
            assertX("1.41+3.14i V");
        else
            assertX(1.41, 3.14);
        clx();
        assertX(6);
        clx();
        assertX(new double[][][] {{{1,1},{2,2}},{{3,3},{4,4}}});
        clx();
        cmd(RCL, 5);
        assertX(new double[][][] {{{1,1},{2,2}},{{3,3},{4,4}}});
        clx();
        cmd(STAT_RCL, 5);
        assertX("nan");
        clx();
        assertX(new double[][] {{1,2},{3,4}});
        clx();
        cmd(RCL, 4);
        assertX(new double[][] {{1,2},{3,4}});
        clx();
        cmd(STAT_RCL, 4);
        assertX("nan");
        clx();
        cmd(FINANCE_RCL, 4);
        assertX("nan");
        clx();
        if (hasUnits)
            assertX(3, "kg");
        else
            assertX(3);
        clx();
        cmd(RCL, 3);
        if (hasUnits)
            assertX(3, "kg");
        else
            assertX(3);
        clx();
        cmd(STAT_RCL, 3);
        assertX(3);
        clx();
        cmd(FINANCE_RCL, 3);
        assertX(3);
        clx();
        assertX(2, 2);
        clx();
        cmd(RCL, 2);
        assertX(2, 2);
        clx();
        cmd(STAT_RCL, 2);
        assertX(2);
        clx();
        cmd(FINANCE_RCL, 2);
        assertX(2);
        clx();
        assertX(1);
        clx();
        cmd(RCL, 1);
        assertX(1);
        clx();
        cmd(STAT_RCL, 1);
        assertX(1);
        clx();
        cmd(FINANCE_RCL, 1);
        assertX(1);
        clx();
        cmd(RCL, 0);
        assertX(0);
        clx();
        cmd(STAT_RCL, 0);
        assertX(0);
        clx();
        cmd(FINANCE_RCL, 0);
        assertX(0);
        clx();

        for (int i=0; i<NUM_PROGS; i++)
            assertEquals(i == 6 ? "tst" : Program.emptyProg, calc.progLabel(i));
        cmd(PROG_APPEND, 6);
        cmd(MONITOR_PROG, 10);
        calc.getMonitor().setDisplayedSize(10, 1);
        assertEquals("6", calc.getMonitor().element(0, 0, numDigits));
        assertEquals("[prg end]", calc.getMonitor().elementSuffix(1, 0));
        cmd(PROG_FINISH);
    }
    
    private void verifyDefaults() {
        cmd(LASTX);
        type("18000.2"); cmd(DIGIT_F);
        enter();
        assertX("18000.2"); // BASE_DEC, THOUSAND_NONE, NORM, POINT_DOT
        type("20.");
        enter();
        assertX("20"); // POINT_REMOVE
        assertFalse(calc.degrees);
        assertFalse(calc.grad);
        assertFalse(calc.begin);
        assertEquals(MONITOR_NONE, calc.monitorMode);
        assertEquals(0, calc.requestedMonitorSize);
        clx(2);

        assertX(0);
        clx();
        assertX("");
        for (int i=0; i<MEM_SIZE; i++) {
            cmd(RCL, i);
            assertX(0);
        }
        for (int i=0; i<STAT_SIZE; i++) {
            cmd(STAT_RCL, i);
            assertX(0);
        }
        for (int i=0; i<FINANCE_SIZE; i++) {
            cmd(FINANCE_RCL, i);
            assertX(0);
        }
        for (int i=0; i<NUM_PROGS; i++)
            assertEquals(Program.emptyProg, calc.progLabel(i));
    }
    
    public void testState() {
        fillState();
        verifyState(true);

        stackPreserved = false;
    }

    public void testSaveAndRestoreState_defaults() throws IOException {
        calc = new CalcEngine(new TestCanvas());
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        calc.saveState(new DataOutputStream(s));
        calc = new CalcEngine(new TestCanvas());
        int defaultStateSize = s.size();
        calc.restoreState(new DataInputStream(new ByteArrayInputStream(s.toByteArray())));
        verifyDefaults();

        calc = new CalcEngine(new TestCanvas());
        // Allocate memory and finance module, verify that nothing is saved
        cmd(STO, 0);
        cmd(FINANCE_STO, 0);
        s = new ByteArrayOutputStream();
        calc.saveState(new DataOutputStream(s));
        calc = new CalcEngine(new TestCanvas());
        assertEquals(defaultStateSize, s.size());
        calc.restoreState(new DataInputStream(new ByteArrayInputStream(s.toByteArray())));
        verifyDefaults();

        stackPreserved = false;
    }
    
    public void testSaveAndRestoreState_full() throws IOException {
        fillState();
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        calc.saveState(new DataOutputStream(s));
        calc = new CalcEngine(new TestCanvas());
        calc.restoreState(new DataInputStream(new ByteArrayInputStream(s.toByteArray())));
        verifyState(true);

        stackPreserved = false;
    }
    
    public void testSaveAndRestoreState_monitors() throws IOException {
        int[] monitorModes = new int[] {MONITOR_MEM, MONITOR_STAT, MONITOR_FINANCE, MONITOR_MATRIX};

        for (int monitorMode: monitorModes) {
            calc = new CalcEngine(new TestCanvas());
            cmd(monitorMode, 5);

            ByteArrayOutputStream s = new ByteArrayOutputStream();
            calc.saveState(new DataOutputStream(s));
            calc = new CalcEngine(new TestCanvas());
            calc.restoreState(new DataInputStream(new ByteArrayInputStream(s.toByteArray())));

            assertEquals(monitorMode, calc.monitorMode);
            assertEquals(5, calc.requestedMonitorSize);
            cmd(MONITOR_NONE); // "restore" to expected value
            verifyDefaults();
        }

        stackPreserved = false;
    }
    
    public void testSaveAndRestoreState_variant() throws IOException {
        fillState();
        cmd(TRIG_DEGRAD);
        assertTrue(calc.grad);
        cmd(PROG_APPEND, 6);
        cmd(MONITOR_PROG, 10);

        ByteArrayOutputStream s = new ByteArrayOutputStream();
        calc.saveState(new DataOutputStream(s));
        calc = new CalcEngine(new TestCanvas());
        calc.restoreState(new DataInputStream(new ByteArrayInputStream(s.toByteArray())));

        assertTrue(calc.grad);
        cmd(TRIG_DEGRAD);
        cmd(TRIG_DEGRAD); // "restore" to expected state
        assertEquals(MONITOR_NONE, calc.monitorMode);
        cmd(MONITOR_MEM, 5); // "restore" to expected value
        verifyState(true);

        stackPreserved = false;
    }
    
    public void testRestoreState_v400() throws IOException {
        calc = new CalcEngine(new TestCanvas());
        byte[] s = new byte[] {
            1, -128, 64, 0, 0, 2, 96, 0, 0, 0, 0, 0, 0, 0, -128, 0, 0, 0, 64, 0,
            0, 0, 0, 0, 0, 7, -128, 0, 0, 0, 64, 0, 0, 0, 0, 0, 0, 4, 64, 0, 0,
            1, 96, 0, 0, 0, 0, 0, 0, 0, 64, 0, 0, 1, 64, 0, 0, 0, 0, 0, 0, 0,
            64, 0, 0, 0, 64, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 64, 0,
            0, 1, 64, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 1, -128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 64, 0,
            0, 0, 64, 0, 0, 0, 0, 0, 0, 0, 64, 0, 0, 1, 64, 0, 0, 0, 0, 0, 0, 0,
            64, 0, 0, 1, 96, 0, 0, 0, 0, 0, 0, 0, -128, 0, 0, 0, 64, 0, 0, 0, 0,
            0, 0, 3, -128, 0, 0, 0, 64, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            64, 0, 0, 1, 64, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0,  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 2, -98, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 64, 0, 0, 0, 64, 0, 0, 0, 0, 0, 0, 0, 64, 0, 0, 1, 64, 0, 0, 0,
            0, 0, 0, 0, 64, 0, 0, 1, 96, 0, 0, 0, 0, 0, 0, 0, -128, 0, 0, 0, 64,
            0, 0, 0, 0, 0, 0, 0, -128, 0, 0, 0, 64, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            60, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 64, 0, 0, 0, 64, 0, 0, 0, 0,
            0, 0, 0, 64, 0, 0, 1, 64, 0, 0, 0, 0, 0, 0, 0, 64, 0, 0, 1, 96, 0,
            0, 0, 0, 0, 0, 0, -128, 0, 0, 0, 64, 0, 0, 0, 0, 0, 0, 0, 0, 50, 6,
            3, 16, 16, 5, 1, 44, 0, 32, 37, -31, 10, 49, -31, -107, -94, -81,
            -85, -2, 41, 18, -41, -113, -15, -29, -78, 64, 0, 0, 0, 90, 61, 112,
            -93, -41, 10, 61, 113, 64, 0, 0, 1, 100, 122, -31, 71, -82, 20, 122,
            -31, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 12, 0, 116, 0, 115, 0,
            116, 0, 0, 0, 0, 24, -30, 0, 0, 0, 0, 0, 52, 0, 2, 0, 2, 64, 0, 0,
            0, 64, 0, 0, 0, 0, 0, 0, 0, 64, 0, 0, 1, 96, 0, 0, 0, 0, 0, 0, 0,
            64, 0, 0, 1, 64, 0, 0, 0, 0, 0, 0, 0, 64, 0, 0, 2, 64, 0, 0, 0, 0,
            0, 0, 0, 0, 52, 0, 2, 0, 2, 64, 0, 0, 0, 64, 0, 0, 0, 0, 0, 0, 0,
            64, 0, 0, 1, 96, 0, 0, 0, 0, 0, 0, 0, 64, 0, 0, 1, 64, 0, 0, 0, 0,
            0, 0, 0, 64, 0, 0, 2, 64, 0, 0, 0, 0, 0, 0, 0, 0, 52, 0, 2, 0, 2,
            64, 0, 0, 0, 64, 0, 0, 0, 0, 0, 0, 0, 64, 0, 0, 1, 96, 0, 0, 0, 0,
            0, 0, 0, 64, 0, 0, 1, 64, 0, 0, 0, 0, 0, 0, 0, 64, 0, 0, 2, 64, 0,
            0, 0, 0, 0, 0, 0, 0, 52, 0, 2, 0, 2, 64, 0, 0, 0, 64, 0, 0, 0, 0, 0,
            0, 0, 64, 0, 0, 1, 96, 0, 0, 0, 0, 0, 0, 0, 64, 0, 0, 1, 64, 0, 0,
            0, 0, 0, 0, 0, 64, 0, 0, 2, 64, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            100, 0, 2, 0, 2, 64, 0, 0, 0, 64, 0, 0, 0, 0, 0, 0, 0, 64, 0, 0, 1,
            96, 0, 0, 0, 0, 0, 0, 0, 64, 0, 0, 1, 64, 0, 0, 0, 0, 0, 0, 0, 64,
            0, 0, 2, 64, 0, 0, 0, 0, 0, 0, 0, 64, 0, 0, 0, 64, 0, 0, 0, 0, 0, 0,
            0, 64, 0, 0, 1, 96, 0, 0, 0, 0, 0, 0, 0, 64, 0, 0, 1, 64, 0, 0, 0,
            0, 0, 0, 0, 64, 0, 0, 2, 64, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0
        };
        calc.restoreState(new DataInputStream(new ByteArrayInputStream(s)));
        verifyState(false);

        stackPreserved = false;
    }

    // Specific bug tests
    
    public void test_HEX_05_thousand() {
        type(0.5);
        cmd(BASE_HEX);
        cmd(THOUSAND_QUOTE);
        // would display as "00'0000'0000'0000'0000'00000.8"
        assertX("000'0000'0000'0000'0000'0000.8");
    }
    
    public void test_matrix_y_clear() {
        enter(new double[][] {{1,2},{3,4}});
        enter(2);
        clx(); // Would change matrix to "nan"
        assertX(new double[][] {{1,2},{3,4}});
    }
    
    public void test_append_number_short_prog() {
        calc.newProgramName = "bug";
        cmd(PROG_NEW, 0);
        enter();
        cmd(PROG_FINISH);
        cmd(PROG_APPEND, 0);
        enter(1.1); // Would crash
        cmd(PROG_FINISH);

        leftoverStackElements = 2;
    }

    public void test_PROG_DIFF_bug() {
        type(2); // Avoid messing up existing stack elements while programming

        calc.newProgramName = "bug";
        cmd(PROG_NEW, 0);
        enter();
        cmd(YPOWX); // x^x
        cmd(PROG_FINISH);

        type(1);
        cmd(EXP);
        cmd(RECIP); // 1/e
        cmd(PROG_DIFF, 0);
        // Would return -9052.467079149503, but should be 0
        cmd(ABS);
        assertXLessThan(0.0000005);

        leftoverStackElements = 3;
    }

    public void test_equivalent_numbers() {
        enter("1999123100");
        type("1.9991231e9");
        Real.magicRounding = false;
        cmd(SUB);
        // Would return -0.0000000002328306436538696
        assertX(0);
        
        // Restore this global!
        Real.magicRounding = true;
    }

    public void test_PROG_with_UNIT() {
        type(2); // Avoid messing up existing stack elements while programming

        calc.newProgramName = "bug";
        cmd(PROG_NEW, 0);
        cmd(UNIT_SET, findUnit("m"));
        cmd(MONITOR_PROG, 10);
        calc.getMonitor().setDisplayedSize(10, 1);
        // Would look like "conv/unit* km"
        assertEquals("conv/unit* m", calc.getMonitor().elementSuffix(0, 0));
        cmd(PROG_FINISH);
        
        type(5);
        cmd(PROG_RUN, 0);
        assertX(5, "m");

        leftoverStackElements = 2;
    }
    
    public void test_setDisplayProperties() {
        cmd(PI);
        assertX("3.141592653589793");
        type(2);
        assertTrue(calc.inputInProgress);

        numDigits = 10;
        calc.setDisplayProperties(numDigits, 3);

        assertFalse(calc.inputInProgress);
        assertY("3.14159265");
        assertX(2);

        leftoverStackElements = 2;
    }
}
