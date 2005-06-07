package ral;

public final class Matrix
{
  public Real[][] D;
  public int rows,cols;
  public boolean inUse; // Used for garbage collection

  public static boolean isInvalid(Matrix A) {
    return A == null || A.D == null || A.rows <= 0 || A.cols <= 0;
  }
  
  private void makeInvalid() {
    D = null;
    rows = cols = 0;
    inUse = false;
  }
  
  private void alloc(int _rows, int _cols) {
    makeInvalid();
    if (_rows <= 0 || _cols <= 0)
      return;
    D = new Real[_rows][_cols];
    for (int i=0; i<_rows; i++)
      for (int j=0; j<_cols; j++)
        D[i][j] = new Real();
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
    for (int i=0; i<rows; i++)
      for (int j=0; j<cols; j++)
        D[i][j].assign(A.D[i][j]);
  }

  public Matrix(int rows, int cols, final Matrix A) {
    alloc(rows,cols);
    if (isInvalid(A))
      return;
    for (int i=0; i<rows && i<A.rows; i++)
      for (int j=0; j<cols && j<A.cols; j++)
        D[i][j].assign(A.D[i][j]);
  }

  Matrix subMatrix(int row, int col, int nRows, int nCols) {
    if (row<0 || col<0 || nRows<=0 || nCols<=0 ||
        rows < row+nRows || cols < col+nCols)
      return null;

    Matrix M = new Matrix(nRows,nCols);
    for (int i=0; i<nRows; i++)
      for (int j=0; j<nCols; j++)
        M.D[i][j].assign(D[i+row][j+col]);
    return M;
  }

  public static Matrix add(final Matrix A, final Matrix B) {
    if (isInvalid(A) || isInvalid(B) || A.rows != B.rows || A.cols != B.cols)
      return null;

    Matrix M = new Matrix(A);
    for (int i=0; i<M.rows; i++)
      for (int j=0; j<M.cols; j++)
        M.D[i][j].add(B.D[i][j]);
    return M;
  }

  public static Matrix sub(final Matrix A, final Matrix B) {
    if (isInvalid(A) || isInvalid(B) || A.rows != B.rows || A.cols != B.cols)
      return null;

    Matrix M = new Matrix(A);
    for (int i=0; i<M.rows; i++)
      for (int j=0; j<M.cols; j++)
        M.D[i][j].sub(B.D[i][j]);
    return M;
  }

  public static Matrix mul(final Matrix A, final Matrix B) {
    if (isInvalid(A) || isInvalid(B) || A.cols != B.rows)
      return null;

    Matrix M = new Matrix(A.rows,B.cols);
    Real tmp = new Real();
    for (int i=0; i<A.rows; i++)
      for (int j=0; j<B.cols; j++)
        for (int k=0; k<A.cols; k++) {
          tmp.assign(A.D[i][k]);
          tmp.mul(B.D[k][j]);
          M.D[i][j].add(tmp);
        }
    return M;
  }

  public static Matrix mul(final Matrix A, final Real b) {
    if (isInvalid(A) || b == null || !b.isFinite())
      return null;

    Matrix M = new Matrix(A);
    for (int i=0; i<M.rows; i++)
      for (int j=0; j<M.cols; j++)
        M.D[i][j].mul(b);
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
    for (int i=0; i<M.rows; i++)
      for (int j=0; j<M.cols; j++)
        M.D[i][j].neg();
    return M;
  }

  public static boolean equals(final Matrix A, final Matrix B) {
    if (isInvalid(A) || isInvalid(B) || A.rows != B.rows || A.cols != B.cols)
      return false;

    for (int i=0; i<A.rows; i++)
      for (int j=0; j<A.cols; j++)
        if (A.D[i][j].equalTo(B.D[i][j]))
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
    for (int i=0; i<M.rows; i++)
      for (int j=0; j<M.cols; j++)
        M.D[i][j].assign(A.D[j][i]);
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
      if (det != null)
        det.makeNan();
      Matrix T = transp(A);
      return mul(invert(mul(T,A)),T);
    }
    else if (A.rows < A.cols)
    {
      // System is underspecified, calculate the least squares solution
      // by pseudoinversion:   T        T -1
      //                      A * (A * A )
      if (det != null)
        det.makeNan();
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
    for (i=0; i<A.rows; i++) {
      for (k=i,j=i+1; j<A.rows; j++)
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
      for (j=0; j<A.rows; j++) {
        M.D[i][j].mul(T.D[i][i]);
        if (j > i) 
          T.D[i][j].mul(T.D[i][i]);
        for (k=0; k<A.rows; k++) 
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
    int i,j;
    for (i=0; i<M.rows; i++) {
      for (j=0; j<A.cols; j++)
        M.D[i][j].assign(A.D[i][j]);
      for (j=0; j<B.cols; j++)
        M.D[i][j+A.cols].assign(B.D[i][j]);
    }
    return M;
  }

  public static Matrix concat(final Matrix A, final Real b) {
    if (isInvalid(A) || b==null || A.rows != 1)
      return null;

    Matrix M = new Matrix(1,A.cols+1);
    for (int j=0; j<A.cols; j++)
      M.D[0][j].assign(A.D[0][j]);
    M.D[0][A.cols].assign(b);
    return M;
  }

  public static Matrix concat(final Real a, final Matrix B) {
    if (a==null || isInvalid(B) || B.rows != 1)
      return null;

    Matrix M = new Matrix(1,B.cols+1);
    M.D[0][0].assign(a);
    for (int j=0; j<B.cols; j++)
      M.D[0][j+1].assign(B.D[0][j]);
    return M;
  }

  public static Matrix stack(final Matrix A, final Matrix B) {
    if (isInvalid(A) || isInvalid(B) || A.cols != B.cols)
      return null;

    Matrix M = new Matrix(A.rows+B.rows,A.cols);
    int i,j;
    for (i=0; i<A.rows; i++)
      for (j=0; j<A.cols; j++)
        M.D[i][j].assign(A.D[i][j]);
    for (i=0; i<B.rows; i++)
      for (j=0; j<B.cols; j++)
        M.D[i+A.rows][j].assign(B.D[i][j]);
    return M;
  }

  public static Matrix stack(final Matrix A, final Real b) {
    if (isInvalid(A) || b==null || A.cols != 1)
      return null;

    Matrix M = new Matrix(A.rows+1,1);
    for (int j=0; j<A.rows; j++)
      M.D[j][0].assign(A.D[j][0]);
    M.D[A.rows][0].assign(b);
    return M;
  }

  public static Matrix stack(final Real a, final Matrix B) {
    if (a==null || isInvalid(B) || B.cols != 1)
      return null;

    Matrix M = new Matrix(B.rows+1,1);
    M.D[0][0].assign(a);
    for (int j=0; j<B.rows; j++)
      M.D[j+1][0].assign(B.D[j][0]);
    return M;
  }
}
