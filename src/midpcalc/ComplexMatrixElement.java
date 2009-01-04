package midpcalc;

final class ComplexMatrixElement extends Element {
    
    static final String empty = "";

    final Real i   = new Real();
    long unit      = 0;
    Matrix M       = null;
    String unitStr = null;

    void clearStrings() {
        super.clearStrings();
        unitStr = null;
    }
    
    public void formatUpdated() {
        if (!isEmpty())
            super.formatUpdated();
    }

    void clear() {
        super.clear();
        i.makeZero();
        unit = 0;
        M    = null;
    }

    void makeEmpty() {
        clear();
        str = empty;
    }
    
    void copy(Element e) {
        super.copy(e);
        if (e instanceof ComplexMatrixElement) {
            ComplexMatrixElement cme = (ComplexMatrixElement)e;
            i.assign(cme.i);
            unit    = cme.unit;
            M       = cme.M;
            unitStr = cme.unitStr;
        }
    }

    final void set(Real r, Real i) {
        set(r);
        if (i != null)
            this.i.assign(i);
        else
            this.i.makeZero();
    }
    
    final void set(Real r, Real i, long unit) {
        set(r, i);
        this.unit = unit;
    }
    
    final void set(Matrix M) {
        makeNan();
        if (Matrix.isInvalid(M)) {
            // Invalid matrix == nan
            return;
        }
        if (M.rows == 1 && M.cols == 1) {
            // 1x1 matrix == Real
            M.getElement(0,0,r,i);
            return;
        }
        this.M = M;
    }

    final Real getImag() {
        return i;
    }
    
    final Matrix getMatrix() {
        return M;
    }
    
    final long getUnit() {
        return unit;
    }
    
    String getUnitStr() {
        if (!hasUnit())
            return null;
        if (unitStr == null)
            unitStr = " "+Unit.toString(unit);
        return unitStr;
    }
    
    String toString(Real.NumberFormat format) {
        if (isMatrix()) {
            return "M:["+M.rows+"x"+M.cols+"]";
        }
        if (isComplex()) {
            int maxwidth = format.maxwidth;
            int imagSign = i.isNegative() && format.base==10 ? 0 : 1;
            format.maxwidth = maxwidth/2-imagSign;
            String imag = i.toString(format);
            format.maxwidth = maxwidth-1-imagSign-imag.length();
            String real = r.toString(format);
            if (real.length() < format.maxwidth) {
                format.maxwidth = maxwidth-1-imagSign-real.length();
                imag = i.toString(format);
            }
            format.maxwidth = maxwidth;
            return real+(imagSign!=0?"+":"")+imag+'i';
        }
        return super.toString(format);
    }

    boolean isEmpty() {
        return str == empty;
    }
    
    boolean isNothing() {
        return r.isZero() && i.isZero() && unit == 0 && M == null;
    }
    
    boolean isZero() {
        return r.isZero() && i.isZero() && M == null;
    }
    
    boolean containsNan() {
        return (r.isNan() || i.isNan()) && M == null;
    }
    
    boolean isComplex() {
        return M == null && !i.isZero();
    }
    
    boolean isMatrix() {
        return M != null;
    }
    
    boolean hasUnit() {
        return unit != 0;
    }
    
    boolean isAbnormal() {
        return !r.isFinite() || !i.isFinite() || M != null;
    }
    
    boolean isAbnormalOrComplex() {
        return !r.isFinite() || !i.isZero() || M != null;
    }
    
    void xchg(ComplexMatrixElement e, boolean keepStrings) {
        r.swap(e.r);
        i.swap(e.i);
        Matrix tmpM = M;
        M = e.M;
        e.M = tmpM;
        long tmpU = unit;
        unit = e.unit;
        e.unit = tmpU;
        String tmpS = unitStr;
        unitStr = e.unitStr;
        e.unitStr = tmpS;
        if (keepStrings) {
            tmpS = str;
            str = e.str;
            e.str = tmpS;
        } else {
            str = e.str = null;
        }
    }
    
    void postProcess(boolean complex, boolean complexOk,
                     boolean matrix, boolean matrixOk,
                     boolean hasUnit, boolean unitOk, long unit2) {
        if (matrix) {
            if (!matrixOk)
                M = null;
            set(M);
        } else {
            M = null;
            if ((complex && !complexOk) || containsNan())
                makeNan();
            if (r.isZero() && !i.isZero())
                r.abs(); // Remove annoying "-"
            if (hasUnit && !unitOk)
                unit = Unit.undefinedBinaryOperation(unit, unit2);
        }
        clearStrings();
    }
}