package midpcalc;

public class MonitorCache {
    
    int startRow;
    int startCol;
    int nRows;
    int nCols;
    
    String[][] element;
    String[][] label;

    public void setMinimumSize(int rows, int cols) {
        if (rows > nRows || cols > nCols) {
            nRows = rows;
            nCols = cols;
            element = new String[nRows][];
            label = new String[nRows][];
            for (int i=0; i<nRows; i++) {
                element[i] = new String[nCols];
                label[i] = new String[nCols];
            }
        }
    }

    public void clearElementsFromRow(int fromRow) {
        if (element != null) {
            fromRow = Math.max(startRow, fromRow);
            for (int row=fromRow; row<startRow+nRows; row++) {
                for (int col=0; col<element[row % nRows].length; col++) {
                    element[row % nRows][col] = null;
                }
            }
        }
    }
    
    private boolean isInside(int row, int col) {
        return row >= startRow && row < startRow+nRows &&
               col >= startCol && col < startCol+nCols;
    }

    public boolean containsLabel(int row, int col) {
        return isInside(row, col) &&
               label[row % nRows][col % nCols] != null;
    }

    public boolean containsElement(int row, int col) {
        return isInside(row, col) &&
               element[row % nRows][col % nCols] != null;
    }

    public String label(int row, int col) {
        return label[row % nRows][col % nCols];
    }

    public String element(int row, int col) {
        return element[row % nRows][col % nCols];
    }

    public void setLabel(int row, int col, String label) {
        relocateCache(row, col);
        this.label[row % nRows][col % nCols] = label;
    }

    public void setElement(int row, int col, String element) {
        relocateCache(row, col);
        this.element[row % nRows][col % nCols] = element;
    }

    public void clearElement(int row, int col) {
        element[row % nRows][col % nCols] = null;
    }

    private void relocateCache(int row, int col) {
        int oldStartRow = startRow;
        int oldStartCol = startCol;
        if (row < startRow) {
            startRow = row;
        } else if (row >= startRow+nRows) {
            startRow = row-nRows+1;
        }
        if (col < startCol) {
            startCol = col;
        } else if (col >= startCol+nCols) {
            startCol = col-nCols+1;
        }
        if (startRow != oldStartRow || startCol != oldStartCol) {
            // Clear elements that are no longer in cache
            for (int i=oldStartRow; i<oldStartRow+nRows; i++)
                for (int j=oldStartCol; j<oldStartCol+nCols; j++)
                    if (!isInside(i,j)) {
                        label  [i % nRows][j % nCols] = null;
                        element[i % nRows][j % nCols] = null;
                    }
        }
    }

}
