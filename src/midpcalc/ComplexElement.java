package midpcalc;

class ComplexElement extends Element {
    
    final Real i = new Real();

    void clear() {
        super.clear();
        i.makeZero();
    }

    void copy(Element e) {
        super.copy(e);
        if (e instanceof ComplexElement)
            i.assign(((ComplexElement)e).i);
    }
    
    final void set(Real r, Real i) {
        set(r);
        if (i != null)
            this.i.assign(i);
        else
            this.i.makeZero();
    }
    
    final Real getImag() {
        return i;
    }
    
    String toString(Real.NumberFormat format) {
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

    boolean isZero() {
        return r.isZero() && i.isZero();
    }
    
    boolean containsNan() {
        return r.isNan() || i.isNan();
    }
    
    boolean isComplex() {
        return !i.isZero();
    }
    
    boolean isMatrix() {
        return false;
    }
    
    boolean hasUnit() {
        return false;
    }
    
    boolean isAbnormal() {
        return !r.isFinite() || !i.isFinite();
    }
    
    boolean isAbnormalOrComplex() {
        return !r.isFinite() || !i.isZero();
    }
}