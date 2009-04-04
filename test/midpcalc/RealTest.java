package midpcalc;

import junit.framework.TestCase;

public class RealTest extends TestCase {

    Real a;
    Real b;
    Real.NumberFormat format;

    protected void setUp() throws Exception {
        a = new Real();
        b = new Real();
        format = new Real.NumberFormat();
        format.precision = 100;
        format.maxwidth = 100;
    }
    
    private void assertReal(Real aReal, String aString) {
        assertEquals(aString, aReal.toString(format));
    }

    private void assertA(String aString) {
        assertReal(a, aString);
    }

    private void assertA(double aDouble) {
        assertReal(a, CalcEngineTest.doubleToString(aDouble));
    }

    private void assertB(String aString) {
        assertReal(b, aString);
    }

    private void assertB(double aDouble) {
        assertReal(b, CalcEngineTest.doubleToString(aDouble));
    }

    public void testToString() {
        a.assign(0);
        assertA(0);
        a.assign(1);
        assertA(1);
        a.assign("-0");
        assertA("-0");
        format.removePoint = false;
        assertA("-0.");
        a.makeInfinity(0);
        assertA("inf");
        a.makeInfinity(1);
        assertA("-inf");
        a.makeNan();
        assertA("nan");
        a.assign(Real.MAX);
        assertA("4.197157432934775e323228496");
        a.assign(Real.MIN);
        assertA("2.382564904887951e-323228497");
    }

    public void testToStringSpecial() {
        a.assign(0);
        format.removePoint = false;
        assertA("0.");
        format.removePoint = true;
        a.assign(1000000000);
        format.maxwidth = 10;
        assertA("1000000000");
        a.assign("0.0000001234567");
        assertA("0.00000012");
        a.assign(Real.MAX);
        assertA("4e323228496");
        a.assign("10000000");
        format.thousand = ' ';
        assertA("10 000 000");
        format.base = 16;
        assertA("0098 9680");
        format.maxwidth = 12;
        assertA("00 0098 9680");
    }

    public void testToStringAligned() {
        a.assign(0);
        format.maxwidth = 10;
        format.align = Real.NumberFormat.ALIGN_LEFT;
        assertA("0         ");
        format.align = Real.NumberFormat.ALIGN_RIGHT;
        assertA("         0");
        format.align = Real.NumberFormat.ALIGN_CENTER;
        assertA("    0     ");
    }

    public void testToStringHEX() {
        format.base = 16;
        format.maxwidth = 10;
        a.assign(0);
        assertA("0000000000");
        a.assign(1);
        assertA("0000000001");
        a.assign("-0");
        assertA("0000000000");
        a.assign("-1");
        assertA("FFFFFFFFFF");
    }

    public void testToStringOCT() {
        format.base = 8;
        format.maxwidth = 10;
        a.assign(0);
        assertA("0000000000");
        a.assign(255);
        assertA("0000000377");
    }

    public void testToStringBIN() {
        format.base = 2;
        format.maxwidth = 10;
        a.assign(0);
        assertA("0000000000");
        a.assign(3);
        assertA("0000000011");
    }

    public void testToStringFIX() {
        format.fse = Real.NumberFormat.FSE_FIX;
        format.precision = 4;
        a.assign(Real.PI);
        assertA("3.1416");
    }

    public void testToStringSCI() {
        format.fse = Real.NumberFormat.FSE_SCI;
        format.precision = 4;
        a.assign(10);
        assertA("1.0000e1");
    }

    public void testToStringENG() {
        format.fse = Real.NumberFormat.FSE_ENG;
        format.precision = 4;
        a.assign(10000);
        assertA("10.0000e3");
    }

    public void testAdd() {
        a.assign(2);
        assertA(2);
        b.assign(3);
        assertB(3);
        a.add(b);
        assertA(2+3);
    }

    public void testMagicRounding() {
        a.assign("4");
        b.assign("4.000000000000000006");
        assertB("4");
        format.base = 16;
        format.maxwidth = 18;
        assertB("04.000000000000007");
        format.base = 10;
        a.sub(b);
        assertA(0);

        a.assign("4");
        b.assign("3.999999999999999997");
        format.base = 16;
        assertB("03.FFFFFFFFFFFFFFD");
        format.base = 10;
        a.sub(b);
        assertA(0);

        a.assign("9223372036854775808");
        format.base = 16;
        assertA("008000000000000000");
        b.assign("9223372036854775801");
        assertB("007FFFFFFFFFFFFFF9");
        format.base = 10;
        a.sub(b);
        assertA(0);

        a.assign("9223372036854775814");
        format.base = 16;
        assertA("008000000000000006");
        b.assign("9223372036854775807");
        assertB("007FFFFFFFFFFFFFFF");
        format.base = 10;
        a.sub(b);
        assertA(0);
    }

    public void testToStringLargeInteger() {
        a.assign("9223372036854775808");
        assertA("9.223372036854776e18");

        a.assign("9223372036854775807");
        assertA("9223372036854775807");

        a.assign("4611686018427387903");
        assertA("4611686018427387903");
        a.add(Real.HALF);
        assertA("4.611686018427388e18");
        a.mul(Real.TWO);
        assertA("9223372036854775807");

        a.assign("10000000000000000");
        assertA("10000000000000000");
        b.assign("0.001953125");
        a.add(b);
        assertA("1e16");

        a.assign("1000000000000000");
        assertA("1000000000000000");
        b.assign("0.4");
        a.add(b);
        assertA("1000000000000000");

        a.assign("999999999999999999");
        assertA("999999999999999999");

        a.assign("9999999999999999998");
        assertA("1e19");
    }

    public void testMagicRoundingLargeInteger() {
        // Probably more test cases than necessary, but brute force is easier
        // than actually thinking through all scenarios.

        a.assign("1FFFFFFFFFFFFFFF.C", 16);
        b.assign("1FFFFFFFFFFFFFFF.8", 16);
        a.sub(b);
        assertA(0);

        a.assign("3FFFFFFFFFFFFFFF.8", 16);
        b.assign("3FFFFFFFFFFFFFFF", 16);
        a.sub(b);
        assertA(0);

        a.assign("3FFFFFFFFFFFFFFF", 16);
        b.assign("3FFFFFFFFFFFFFFE.8", 16);
        a.sub(b);
        assertA(0);

        a.assign("9223372036854775807");
        b.assign("9223372036854775800");
        a.sub(b);
        assertA(7);

        a.assign("9223372036854775807");
        b.assign("9223372036854775806");
        a.sub(b);
        assertA(1);

        a.assign("1FFFFFFFFFFFFFFF", 16);
        b.assign("1FFFFFFFFFFFFFFE", 16);
        a.sub(b);
        assertA(1);

        a.assign("8000000000000006", 16);
        b.assign("7FFFFFFFFFFFFFFF", 16);
        a.sub(b);
        assertA(0);

        a.assign("8000000000000002", 16);
        b.assign("7FFFFFFFFFFFFFFB", 16);
        a.sub(b);
        assertA(0);

        a.assign("8000000000000000", 16);
        b.assign("7FFFFFFFFFFFFFFF", 16);
        a.sub(b);
        assertA(0);

        a.assign("75A5A5A5A5A5A5A7", 16);
        b.assign("75A5A5A5A5A5A5A0", 16);
        a.sub(b);
        assertA(7);

        a.assign("4000000000000002.0", 16);
        b.assign("3FFFFFFFFFFFFFFF.0", 16);
        a.sub(b);
        assertA(3);

        a.assign("4000000000000002.0", 16);
        b.assign("3FFFFFFFFFFFFFFF.8", 16);
        a.sub(b);
        assertA(0);

        a.assign("4000000000000001.0", 16);
        b.assign("3FFFFFFFFFFFFFFE.0", 16);
        a.sub(b);
        assertA(3);

        a.assign("4000000000000001.0", 16);
        b.assign("3FFFFFFFFFFFFFFE.8", 16);
        a.sub(b);
        assertA(0);

        a.assign("35A5A5A5A5A5A5AB.0", 16);
        b.assign("35A5A5A5A5A5A5A8.0", 16);
        a.sub(b);
        assertA(3);

        a.assign("35A5A5A5A5A5A5AB.0", 16);
        b.assign("35A5A5A5A5A5A5A8.8", 16);
        a.sub(b);
        assertA(0);

        a.assign("35A5A5A5A5A5A5A6.0", 16);
        b.assign("35A5A5A5A5A5A5A3.0", 16);
        a.sub(b);
        assertA(3);

        a.assign("35A5A5A5A5A5A5A6.0", 16);
        b.assign("35A5A5A5A5A5A5A3.8", 16);
        a.sub(b);
        assertA(0);

        a.assign("15A5A5A5A5A5A5AD.0", 16);
        b.assign("15A5A5A5A5A5A5AC.0", 16);
        a.sub(b);
        assertA(1);

        a.assign("15A5A5A5A5A5A5AD.0", 16);
        b.assign("15A5A5A5A5A5A5AC.C", 16);
        a.sub(b);
        assertA(0);

        a.assign("15A5A5A5A5A5A5A2.0", 16);
        b.assign("15A5A5A5A5A5A5A1.0", 16);
        a.sub(b);
        assertA(1);

        a.assign("15A5A5A5A5A5A5A2.0", 16);
        b.assign("15A5A5A5A5A5A5A1.C", 16);
        a.sub(b);
        assertA(0);
    }
    
    
    public void test_001_display_bug() {
        a.assign("0.01");
        // Would display 0
        assertA("0.01");
    }
}
