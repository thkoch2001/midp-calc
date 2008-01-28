package midpcalc;

public class MonitorableMatrix implements Monitorable {
    
    private Matrix M;
    private Real.NumberFormat format;
    private MonitorCache cache = new MonitorCache();
    private ComplexElement tmpElement = new ComplexElement();
    private String[] captions;
    
    public MonitorableMatrix(Real.NumberFormat format) {
        setFormat(format);
    }
    
    public void setMatrix(Matrix M) {
        if (this.M != M) {
            this.M = M;
            formatUpdated();
            if (M != null && (captions == null || captions.length < M.cols)) {
                captions = new String[M.cols];
                for (int i=0; i<captions.length; i++) {
                    captions[i] = "Col:"+(i+1);
                }
            }
        }
    }
    
    public Matrix matrix() {
        return M;
    }

    public int rows() {
        return M == null ? 0 : M.rows;
    }

    public int cols() {
        return M == null ? 1 : M.cols;
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
    
    public void matrixElementChanged(int row, int col) {
        if (cache.containsElement(row, col))
            cache.clearElement(row, col);
    }

    public String label(int row, int col) {
        if (!cache.containsLabel(row, col))
            cache.setLabel(row, col, "R"+(row+1));
        return cache.label(row, col);
    }

    public String lead(int row, int col, boolean currentPosition, boolean isInside) {
        return currentPosition && isInside ? "»": "=";
    }

    public String element(int row, int col, int maxWidth) {
        if (!cache.containsElement(row, col)) {
            String element = null;
            int tmp = format.maxwidth;
            format.maxwidth = maxWidth;
            tmpElement.set(M.D[col][row], M.DI==null ? null : M.DI[col][row]);
            element = tmpElement.getStr(format);
            format.maxwidth = tmp;
            cache.setElement(row, col, element);
        }
        return cache.element(row, col);
    }

    public String elementSuffix(int row, int col) {
        return null;
    }

    public boolean isLabelMonospaced(int row, int col) {
        return true;
    }

    public boolean hasCaption() {
        return true;
    }

    public String caption(int col) {
        return M == null ? "no matrix" : captions[col];
    }

}
