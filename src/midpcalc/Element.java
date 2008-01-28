package midpcalc;

class Element {

    final Real r = new Real();
    String str   = null;
    String label = null;

    void clearStrings() {
        str     = null;
    }
    
    public void formatUpdated() {
        str = null;
    }

    void clear() {
        r.makeZero();
        clearStrings();
    }

    final void makeNan() {
        clear();
        r.makeNan();
    }
    
    final void makeEncodedNan(int i) {
        clear();
        r.makeNan();
        r.mantissa += i+1;
    }
    
    final boolean isEncodedNan() {
        return !isMatrix() && !isComplex() && r.isNan() && (int)r.mantissa != 0;
    }
    
    final int decodeNan() {
        return (int)r.mantissa - 1;
    }
    
    void copy(Element e) {
        r.assign(e.r);
        str = e.str;
    }
    
    final void copy(Element e, boolean omitStr) {
        copy(e);
        str = null;
    }
    
    final void set(Real r) {
        clear();
        this.r.assign(r);
    }

    final Real getReal() {
        return r;
    }
    
    final String getLabel() {
        return label;
    }
    
    Real getImag() {
        return null;
    }

    Matrix getMatrix() {
        return null;
    }
    
    long getUnit() {
        return 0;
    }
    
    String getUnitStr() {
        return null;
    }
    
    final String getStr(Real.NumberFormat format) {
        if (str == null)
            str = toString(format);
        return str;
    }
    
    String toString(Real.NumberFormat format) {
        return r.toString(format);
    }
    
    boolean isZero() {
        return r.isZero();
    }
    
    boolean containsNan() {
        return r.isNan();
    }
    
    boolean isComplex() {
        return false;
    }
    
    boolean isMatrix() {
        return false;
    }
    
    boolean hasUnit() {
        return false;
    }
    
    boolean isAbnormal() {
        return !r.isFinite();
    }
    
    boolean isAbnormalOrComplex() {
        return !r.isFinite();
    }
}