package midpcalc;

public final class MonitorableElements implements Monitorable {
    
    private Element[] elements;
    private Real.NumberFormat format;
    private boolean monospacedLabel;
    
    public MonitorableElements(Real.NumberFormat format) {
        setFormat(format);
    }
    
    public MonitorableElements withElements(Element[] elements, boolean monospacedLabel) {
        this.elements = elements;
        this.monospacedLabel = monospacedLabel;
        formatUpdated();
        return this;
    }

    public int rows() {
        return elements.length;
    }

    public int cols() {
        return 1;
    }

    public void setDisplayedSize(int rows, int cols) {
    }

    public void setFormat(Real.NumberFormat format) {
        this.format = format;
    }

    public void formatUpdated() {
        if (elements != null)
            for (int i=0; i<elements.length; i++)
                elements[i].formatUpdated();
    }

    public String label(int row, int col) {
        return elements[row].getLabel();
    }

    public String lead(int row, int col, boolean currentPosition, boolean isInside) {
        return currentPosition && isInside ? "»": "=";
    }

    public String element(int row, int col, int maxWidth) {
        int tmp = format.maxwidth;
        format.maxwidth = maxWidth;
        String str = elements[row].getStr(format);
        format.maxwidth = tmp;
        return str;
    }

    public String elementSuffix(int row, int col) {
        return elements[row].getUnitStr();
    }

    public boolean isLabelMonospaced(int row, int col) {
        return monospacedLabel;
    }

    public boolean hasCaption() {
        return false;
    }

    public String caption(int col) {
        return null;
    }

}
