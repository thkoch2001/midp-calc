package ral;

public final class Matrix
{
  public Real[][] D;
  public int rows,cols;
  public int refCount; // Used for garbage collection

  public static boolean isInvalid(Matrix A) {
    return A == null || A.D == null || A.rows <= 0 || A.cols <= 0;
  }
  
  private void makeInvalid() {
    D = null;
    rows = cols = 0;
    refCount = 0;
  }
  
  private void alloc(int _rows, int _cols) {
    makeInvalid();
    if (_rows <= 0 || _cols <= 0)
      return;
    D = new Real[_cols][_rows];
    for (int c=0; c<_cols; c++)
      for (int r=0; r<_rows; r++)
        D[c][r] = new Real();
    rows = _rows;
    cols = _cols;
  }

  public Matrix() {
    makeInvalid();
  }

  public Matrix(int rows, int cols) {
    alloc(rows,cols);
  }

  public Matrix(int size) {
    alloc(size,size);
  }

  public Matrix(final Matrix A) {
    if (isInvalid(A)) {
      makeInvalid();
      return;
    }
    alloc(A.rows,A.cols);
    for (int c=0; c<cols; c++)
      for (int r=0; r<rows; r++)
        D[c][r].assign(A.D[c][r]);
  }

  Matrix subMatrix(int row, int col, int nRows, int nCols) {
    if (row<0 || col<0 || nRows<=0 || nCols<=0 ||
        rows < row+nRows || cols < col+nCols)
      return null;

    Matrix M = new Matrix(nRows,nCols);
    for (int c=0; c<nCols; c++)
      for (int r=0; r<nRows; r++)
        M.D[c][r].assign(D[c+col][r+row]);
    return M;
  }

  public static Matrix add(final Matrix A, final Matrix B) {
    if (isInvalid(A) || isInvalid(B) || A.rows != B.rows || A.cols != B.cols)
      return null;

    Matrix M = new Matrix(A);
    for (int c=0; c<M.cols; c++)
      for (int r=0; r<M.rows; r++)
        M.D[c][r].add(B.D[c][r]);
    return M;
  }

  public static Matrix sub(final Matrix A, final Matrix B) {
    if (isInvalid(A) || isInvalid(B) || A.rows != B.rows || A.cols != B.cols)
      return null;

    Matrix M = new Matrix(A);
    for (int c=0; c<M.cols; c++)
      for (int r=0; r<M.rows; r++)
        M.D[c][r].sub(B.D[c][r]);
    return M;
  }

  public static Matrix mul(final Matrix A, final Matrix B) {
    if (isInvalid(A) || isInvalid(B) || A.cols != B.rows)
      return null;

    Matrix M = new Matrix(A.rows,B.cols);
    Real tmp = new Real();
    for (int c=0; c<B.cols; c++)
      for (int r=0; r<A.rows; r++)
        for (int k=0; k<A.cols; k++) {
          tmp.assign(A.D[k][r]);
          tmp.mul(B.D[c][k]);
          M.D[c][r].add(tmp);
        }
    return M;
  }

  public static Matrix mul(final Matrix A, final Real b) {
    if (isInvalid(A) || b == null || !b.isFinite())
      return null;

    Matrix M = new Matrix(A);
    for (int c=0; c<M.cols; c++)
      for (int r=0; r<M.rows; r++)
        M.D[c][r].mul(b);
    return M;
  }

  public static Matrix div(final Matrix A, final Matrix B) {
    if (isInvalid(A) || isInvalid(B) || A.cols != B.cols)
      return null;
    return mul(A,invert(B));
  }

  public static Matrix div(final Matrix A, final Real b) {
    if (isInvalid(A) || b == null || b.isNan() || b.isZero())
      return null;
    Real tmp = new Real(b);
    tmp.recip();
    return mul(A,tmp);
  }

  public static Matrix div(final Real a, final Matrix B) {
    if (a == null || !a.isFinite() || isInvalid(B))
      return null;
    return mul(invert(B),a);
  }

  public static Matrix neg(final Matrix A) {
    if (isInvalid(A))
      return null;

    Matrix M = new Matrix(A);
    for (int c=0; c<M.cols; c++)
      for (int r=0; r<M.rows; r++)
        M.D[c][r].neg();
    return M;
  }

  public static boolean equals(final Matrix A, final Matrix B) {
    if (isInvalid(A) || isInvalid(B) || A.rows != B.rows || A.cols != B.cols)
      return false;

    for (int c=0; c<A.cols; c++)
      for (int r=0; r<A.rows; r++)
        if (A.D[c][r].equalTo(B.D[c][r]))
          return false;
    return true;
  }

  public static boolean notEquals(final Matrix A, final Matrix B) {
    if (isInvalid(A) || isInvalid(B) || A.rows != B.rows || A.cols != B.cols)
      return false;

    return !equals(A,B);
  }
  
  public static Matrix transp(final Matrix A) {
    if (isInvalid(A))
      return null;

    Matrix M = new Matrix(A.cols,A.rows);
    for (int c=0; c<M.cols; c++)
      for (int r=0; r<M.rows; r++)
        M.D[c][r].assign(A.D[r][c]);
    return M;
  }

  public static Matrix invert(final Matrix A) {
    return invert(A,null);
  }
  
  public static Matrix invert(final Matrix A, Real det) {
    if (isInvalid(A))
      return null;

    if (A.rows > A.cols)
    {
      // System is overspecified, calculate the least squares solution
      // by pseudoinversion:    T    -1   T
      //                      (A * A)  * A
      if (det != null) {
        det.makeNan();
        return null; // Assuming, if det!=null, you don't want the inverse
      }
      Matrix T = transp(A);
      return mul(invert(mul(T,A)),T);
    }
    else if (A.rows < A.cols)
    {
      // System is underspecified, calculate the least squares solution
      // by pseudoinversion:   T        T -1
      //                      A * (A * A )
      if (det != null) {
        det.makeNan();
        return null; // Assuming, if det!=null, you don't want the inverse
      }
      Matrix T = transp(A);
      return mul(T,invert(mul(A,T)));
    }

    Matrix T = new Matrix(A);
    Matrix M = new Matrix(A.rows);
    Real[] t;
    Real tmp = new Real();
    int i,j,k;
    if (det != null)
      det.assign(Real.ONE);

    for (i=0; i<M.rows; i++)
      M.D[i][i].assign(Real.ONE);
    for (i=0; i<M.rows; i++) {
      for (k=i,j=i+1; j<M.rows; j++)
        if (T.D[k][i].absLessThan(T.D[j][i]))
          k = j;
      if (k!=i) {
        t = T.D[i]; T.D[i] = T.D[k]; T.D[k] = t;
        t = M.D[i]; M.D[i] = M.D[k]; M.D[k] = t;
        if (det != null)
          det.neg();
      }
      if (det != null)
        det.mul(T.D[i][i]);
      T.D[i][i].recip();
      if (!T.D[i][i].isFinite()) {
        if (det != null)
          det.makeZero(); // Just in case
        return null;
      }
      for (j=0; j<M.rows; j++) {
        M.D[i][j].mul(T.D[i][i]);
        if (j > i) 
          T.D[i][j].mul(T.D[i][i]);
        for (k=0; k<M.rows; k++) 
          if (k != i) {
            tmp.assign(T.D[k][i]);
            tmp.mul(M.D[i][j]);
            M.D[k][j].sub(tmp);
            if (j>i) {
              tmp.assign(T.D[k][i]);
              tmp.mul(T.D[i][j]);
              T.D[k][j].sub(tmp);
            }
          }
      }
    }
    return M;
  }

  public static Matrix concat(final Matrix A, final Matrix B) {
    if (isInvalid(A) || isInvalid(B) || A.rows != B.rows)
      return null;

    Matrix M = new Matrix(A.rows,A.cols+B.cols);
    int r,c;
    for (c=0; c<A.cols; c++)
      for (r=0; r<M.rows; r++)
        M.D[c][r].assign(A.D[c][r]);
    for (c=0; c<B.cols; c++)
      for (r=0; r<M.rows; r++)
        M.D[c+A.cols][r].assign(B.D[c][r]);
    return M;
  }

  public static Matrix concat(final Matrix A, final Real b) {
    if (isInvalid(A) || b==null || A.rows != 1)
      return null;

    Matrix M = new Matrix(1,A.cols+1);
    for (int c=0; c<A.cols; c++)
      M.D[c][0].assign(A.D[c][0]);
    M.D[A.cols][0].assign(b);
    return M;
  }

  public static Matrix concat(final Real a, final Matrix B) {
    if (a==null || isInvalid(B) || B.rows != 1)
      return null;

    Matrix M = new Matrix(1,B.cols+1);
    M.D[0][0].assign(a);
    for (int c=0; c<B.cols; c++)
      M.D[c+1][0].assign(B.D[c][0]);
    return M;
  }

  public static Matrix stack(final Matrix A, final Matrix B) {
    if (isInvalid(A) || isInvalid(B) || A.cols != B.cols)
      return null;

    Matrix M = new Matrix(A.rows+B.rows,A.cols);
    int r,c;
    for (c=0; c<A.cols; c++) {
      for (r=0; r<A.rows; r++)
        M.D[c][r].assign(A.D[c][r]);
      for (r=0; r<B.rows; r++)
        M.D[c][r+A.rows].assign(B.D[c][r]);
    }
    return M;
  }

  public static Matrix stack(final Matrix A, final Real b) {
    if (isInvalid(A) || b==null || A.cols != 1)
      return null;

    Matrix M = new Matrix(A.rows+1,1);
    for (int r=0; r<A.rows; r++)
      M.D[0][r].assign(A.D[0][r]);
    M.D[0][A.rows].assign(b);
    return M;
  }

  public static Matrix stack(final Real a, final Matrix B) {
    if (a==null || isInvalid(B) || B.cols != 1)
      return null;

    Matrix M = new Matrix(B.rows+1,1);
    M.D[0][0].assign(a);
    for (int r=0; r<B.rows; r++)
      M.D[0][r+1].assign(B.D[0][r]);
    return M;
  }
}
