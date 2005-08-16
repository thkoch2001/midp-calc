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

  public Matrix(Matrix A) {
    if (isInvalid(A)) {
      makeInvalid();
      return;
    }
    alloc(A.rows,A.cols);
    for (int c=0; c<cols; c++)
      for (int r=0; r<rows; r++)
        D[c][r].assign(A.D[c][r]);
  }

  void setElement(int row, int col, Real a) {
    if (row<0 || row>=rows || col<0 || col>=cols || a==null)
      return;
    D[row][col].assign(a);
  }

  void getElement(int row, int col, Real a) {
    if (a==null)
      return;
    if (row<0 || row>=rows || col<0 || col>=cols) {
      a.makeNan();
      return;
    }
    a.assign(D[row][col]);
  }

  void setSubMatrix(int row, int col, Matrix A) {
    if (isInvalid(A) || row<0 || col<0 ||
        rows < row+A.rows || cols < col+A.cols)
      return;

    for (int c=0; c<A.cols; c++)
      for (int r=0; r<A.rows; r++)
        D[c+col][r+row].assign(A.D[c][r]);
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

  public static Matrix add(Matrix A, Matrix B) {
    if (isInvalid(A) || isInvalid(B) || A.rows != B.rows || A.cols != B.cols)
      return null;

    Matrix M = new Matrix(A);
    for (int c=0; c<M.cols; c++)
      for (int r=0; r<M.rows; r++)
        M.D[c][r].add(B.D[c][r]);
    return M;
  }

  public static Matrix sub(Matrix A, Matrix B) {
    if (isInvalid(A) || isInvalid(B) || A.rows != B.rows || A.cols != B.cols)
      return null;

    Matrix M = new Matrix(A);
    for (int c=0; c<M.cols; c++)
      for (int r=0; r<M.rows; r++)
        M.D[c][r].sub(B.D[c][r]);
    return M;
  }

  public static Matrix mul(Matrix A, Matrix B) {
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

  public static Matrix mul(Matrix A, Real b) {
    if (isInvalid(A) || b == null || !b.isFinite())
      return null;

    Matrix M = new Matrix(A);
    for (int c=0; c<M.cols; c++)
      for (int r=0; r<M.rows; r++)
        M.D[c][r].mul(b);
    return M;
  }

  public static Matrix div(Matrix A, Matrix B) {
    if (isInvalid(A) || isInvalid(B) || A.cols != B.cols)
      return null;
    return mul(A,invert(B));
  }

  public static Matrix div(Matrix A, Real b) {
    if (isInvalid(A) || b == null || b.isNan() || b.isZero())
      return null;
    Real tmp = new Real(b);
    tmp.recip();
    return mul(A,tmp);
  }

  public static Matrix div(Real a, Matrix B) {
    if (a == null || !a.isFinite() || isInvalid(B))
      return null;
    return mul(invert(B),a);
  }

  public static Matrix pow(Matrix A, int power) {
    // Calculate power of integer by successive squaring
    if (isInvalid(A) || A.cols != A.rows)
      return null;

    boolean inv=false;
    if (power < 0) {
      power = -power; // Also works for 0x80000000
      inv = true;
    }

    Matrix P = new Matrix(A.cols);
    for (int i=0; i<A.cols; i++)
      P.D[i][i].assign(Real.ONE);

    for (; power!=0; power>>>=1) {
      if ((power & 1) != 0)
        P = mul(P,A);
      A = mul(A,A);
    }

    if (inv)
      P = invert(P);
    return P;
  }

  public static Matrix neg(Matrix A) {
    if (isInvalid(A))
      return null;

    Matrix M = new Matrix(A);
    for (int c=0; c<M.cols; c++)
      for (int r=0; r<M.rows; r++)
        M.D[c][r].neg();
    return M;
  }

  public static boolean equals(Matrix A, Matrix B) {
    if (isInvalid(A) || isInvalid(B) || A.rows != B.rows || A.cols != B.cols)
      return false;

    for (int c=0; c<A.cols; c++)
      for (int r=0; r<A.rows; r++)
        if (!A.D[c][r].equalTo(B.D[c][r]))
          return false;
    return true;
  }

  public static boolean notEquals(Matrix A, Matrix B) {
    if (isInvalid(A) || isInvalid(B) || A.rows != B.rows || A.cols != B.cols)
      return false;

    return !equals(A,B);
  }
  
  public static Matrix transp(Matrix A) {
    if (isInvalid(A))
      return null;

    Matrix M = new Matrix(A.cols,A.rows);
    for (int c=0; c<M.cols; c++)
      for (int r=0; r<M.rows; r++)
        M.D[c][r].assign(A.D[r][c]);
    return M;
  }

  public static Matrix invert(Matrix A) {
    return invert(A,null);
  }
  
  public static Matrix invert(Matrix A, Real det) {
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

  public void det(Real det) {
    invert(this,det);
  }

  public void trace(Real Tr) {
    Tr.makeZero();
    for (int i=0; i<cols && i<rows; i++)
      Tr.add(D[i][i]);
  }

  public void norm_F(Real norm2) {
    Real tmp = new Real();
    norm2.makeZero();
    for (int c=0; c<cols; c++)
      for (int r=0; r<rows; r++) {
        tmp.assign(D[c][r]);
        tmp.sqr();
        norm2.add(tmp);
      }
    norm2.sqrt();
  }

  public static Matrix concat(Matrix A, Matrix B) {
    if (isInvalid(A) || isInvalid(B) || A.rows != B.rows)
      return null;
    Matrix M = new Matrix(A.rows,A.cols+B.cols);
    M.setSubMatrix(0,0,A);
    M.setSubMatrix(0,A.cols,B);
    return M;
  }

  public static Matrix concat(Matrix A, Real b) {
    if (isInvalid(A) || b==null || A.rows != 1)
      return null;
    Matrix M = new Matrix(1,A.cols+1);
    M.setSubMatrix(0,0,A);
    M.setElement(0,A.cols,b);
    return M;
  }

  public static Matrix concat(Real a, Matrix B) {
    if (a==null || isInvalid(B) || B.rows != 1)
      return null;
    Matrix M = new Matrix(1,B.cols+1);
    M.setElement(0,0,a);
    M.setSubMatrix(0,1,B);
    return M;
  }

  public static Matrix concat(Real a, Real b) {
    if (a==null || b==null)
      return null;
    Matrix M = new Matrix(1,2);
    M.setElement(0,0,a);
    M.setElement(0,1,b);
    return M;
  }

  public static Matrix stack(Matrix A, Matrix B) {
    if (isInvalid(A) || isInvalid(B) || A.cols != B.cols)
      return null;
    Matrix M = new Matrix(A.rows+B.rows,A.cols);
    M.setSubMatrix(0,0,A);
    M.setSubMatrix(A.rows,0,B);
    return M;
  }

  public static Matrix stack(Matrix A, Real b) {
    if (isInvalid(A) || b==null || A.cols != 1)
      return null;
    Matrix M = new Matrix(A.rows+1,1);
    M.setSubMatrix(0,0,A);
    M.setElement(A.rows,0,b);
    return M;
  }

  public static Matrix stack(Real a, Matrix B) {
    if (a==null || isInvalid(B) || B.cols != 1)
      return null;
    Matrix M = new Matrix(B.rows+1,1);
    M.setElement(0,0,a);
    M.setSubMatrix(1,0,B);
    return M;
  }

  public static Matrix stack(Real a, Real b) {
    if (a==null || b==null)
      return null;
    Matrix M = new Matrix(2,1);
    M.setElement(0,0,a);
    M.setElement(1,0,b);
    return M;
  }
}
