package midpcalc;

public interface Monitorable {
    int rows();
    int cols();
    void setDisplayedSize(int nRows, int nCols);
    void setFormat(Real.NumberFormat format);
    void formatUpdated();
    String label(int row, int col);
    String lead(int row, int col, boolean currentPosition, boolean isInside);
    String element(int row, int col, int maxWidth);
    String elementSuffix(int row, int col);
    boolean hasCaption();
    String caption(int col);
}
