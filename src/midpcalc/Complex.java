package midpcalc;

public final class Complex
{
    public static boolean degrees, grad;

    private static Real tmp = new Real();
    private static Real tmp2 = new Real();
    private static Real tmp3 = new Real();
    private static Real tmp4 = new Real();
    private static Real tmp5 = new Real();
    private static Real tmp6 = new Real();

    public static void mul(Real y, Real yi, Real x, Real xi) {
        tmp.assign(yi);
        tmp.mul(xi);
        tmp2.assign(y);
        tmp2.mul(xi);
        y.mul(x);
        y.sub(tmp);
        yi.mul(x);
        yi.add(tmp2);
    }

    public static void div(Real y, Real yi, Real x, Real xi) {
        tmp.assign(yi);
        tmp.mul(xi);
        tmp2.assign(y);
        tmp2.mul(xi);
        y.mul(x);
        y.add(tmp);
        yi.mul(x);
        yi.sub(tmp2);
        x.sqr();
        xi.sqr();
        x.add(xi);
        y.div(x);
        yi.div(x);
    }

    public static void recip(Real x, Real xi) {
        tmp.assign(x);
        tmp.sqr();
        tmp2.assign(xi);
        tmp2.sqr();
        tmp.add(tmp2);
        x.div(tmp);
        xi.div(tmp);
        xi.neg();
    }

    public static void sqr(Real x, Real xi) {
        tmp.assign(xi);
        tmp.sqr();
        xi.mul(x);
        xi.scalbn(1);
        x.sqr();
        x.sub(tmp);
    }

    public static void sqrt(Real x, Real xi) {
        tmp.assign(x);
        tmp.hypot(xi);
        tmp2.assign(x);
        tmp2.abs();
        tmp.add(tmp2);
        tmp.scalbn(1);
        tmp.sqrt();
        if (!x.isNegative()) {
            x.assign(tmp);
            x.scalbn(-1);
            xi.div(tmp);
        } else {
            x.assign(xi);
            x.abs();
            x.div(tmp);
            tmp.copysign(xi);
            xi.assign(tmp);
            xi.scalbn(-1);
        }
    }

    public static void sinh(Real x, Real xi) {
        // "Alert" the user to the fact that we use strictly RAD
        degrees = grads = false;
        tmp.assign(x);
        tmp2.assign(xi);
        x.sinh();
        tmp2.cos();
        x.mul(tmp2);
        xi.sin();
        tmp.cosh();
        xi.mul(tmp);
    }

    public static void cosh(Real x, Real xi) {
        // "Alert" the user to the fact that we use strictly RAD
        degrees = grads = false;
        tmp.assign(x);
        tmp2.assign(xi);
        x.cosh();
        tmp2.cos();
        x.mul(tmp2);
        xi.sin();
        tmp.sinh();
        xi.mul(tmp);
    }

    public static void tanh(Real x, Real xi) {
        tmp3.assign(x);
        tmp4.assign(xi);
        sinh(x,xi);
        cosh(tmp3,tmp4);
        div(x,xi,tmp3,tmp4);
    }
  
    public static void asinh(Real x, Real xi) {
        tmp3.assign(x);
        tmp4.assign(xi);
        sqr(x,xi);
        x.add(Real.ONE);
        sqrt(x,xi);
        x.add(tmp3);
        xi.add(tmp4);
        ln(x,xi);
    }

    public static void acosh(Real x, Real xi) {
        tmp3.assign(x);
        tmp4.assign(xi);
        tmp3.add(Real.ONE);
        sqrt(tmp3,tmp4);
        tmp5.assign(x);
        tmp6.assign(xi);
        tmp5.sub(Real.ONE);
        sqrt(tmp5,tmp6);
        mul(tmp3,tmp4,tmp5,tmp6);
        x.add(tmp3);
        xi.add(tmp4);
        ln(x,xi);
    }

    public static void atanh(Real x, Real xi) {
        tmp3.assign(x);
        tmp4.assign(xi);
        x.add(Real.ONE);
        ln(x,xi);
        tmp3.neg();
        tmp4.neg();
        tmp3.add(Real.ONE);
        ln(tmp3,tmp4);
        x.sub(tmp3);
        xi.sub(tmp4);
        x.scalbn(-1);
        xi.scalbn(-1);
    }

    public static void exp(Real x, Real xi) {
        // "Alert" the user to the fact that we use strictly RAD
        degrees = grads = false;
        x.exp();
        tmp.assign(xi);
        xi.sin();
        xi.mul(x);
        tmp.cos();
        x.mul(tmp);
    }
  
    public static void ln(Real x, Real xi) {
        // "Alert" the user to the fact that we use strictly RAD
        degrees = grads = false;
        tmp.assign(x);
        if (!xi.isZero())
            x.hypot(xi);
        else
            x.abs();
        x.ln();
        xi.atan2(tmp);
    }
}
