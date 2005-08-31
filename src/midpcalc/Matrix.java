package ral;

public final class Matrix
{
  public Real[][] D;
  public Real[][] DI;
  public int rows,cols;
  public int refCount; // Used for garbage collection

  public static boolean isInvalid(Matrix A) {
    return A == null || A.D == null || A.rows <= 0 || A.cols <= 0;
  }

  private void makeInvalid() {
    D = DI = null;
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

  private void allocImag() {
    if (DI != null)
      return;
    DI = new Real[cols][rows];
    for (int c=0; c<cols; c++)
      for (int r=0; r<rows; r++)
        DI[c][r] = new Real();
  }

  public Matrix() {
    makeInvalid();
  }

  public Matrix(int rows, int cols, boolean complex) {
    alloc(rows,cols);
    if (complex)
      allocImag();
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
    alloc(A.rows, A.cols);
    if (A.DI!=null)
      allocImag();
    for (int c=0; c<cols; c++)
      for (int r=0; r<rows; r++) {
        D[c][r].assign(A.D[c][r]);
        if (A.DI != null)
          DI[c][r].assign(A.DI[c][r]);
      }
  }

  void setElement(int row, int col, Real a, Real aI) {
    if (row<0 || row>=rows || col<0 || col>=cols)
      return;
    if (a != null)
      D[col][row].assign(a);
    if (aI != null && !aI.isZero()) {
      allocImag();
      DI[col][row].assign(aI);
    }
  }

  void getElement(int row, int col, Real a, Real aI) {
    if (row<0 || row>=rows || col<0 || col>=cols) {
      if (a  != null) a.makeNan();
      if (aI != null) aI.makeZero();
      return;
    }
    if (a != null)
      a.assign(D[col][row]);
    if (aI != null)
      aI.assign(DI!=null ? DI[col][row] : Real.ZERO);
  }

  void setSubMatrix(int row, int col, Matrix A) {
    if (isInvalid(A) || row<0 || col<0 ||
        rows < row+A.rows || cols < col+A.cols)
      return;

    if (A.DI != null)
      allocImag();
    for (int c=0; c<A.cols; c++)
      for (int r=0; r<A.rows; r++) {
        D[c+col][r+row].assign(A.D[c][r]);
        if (A.DI != null)
          DI[c+col][r+row].assign(A.DI[c][r]);
      }
  }

  Matrix subMatrix(int row, int col, int nRows, int nCols) {
    if (row<0 || col<0 || nRows<=0 || nCols<=0 ||
        rows < row+nRows || cols < col+nCols)
      return null;

    Matrix M = new Matrix(nRows, nCols, DI!=null);
    for (int c=0; c<nCols; c++)
      for (int r=0; r<nRows; r++) {
        M.D[c][r].assign(D[c+col][r+row]);
        if (DI != null)
          M.DI[c][r].assign(DI[c+col][r+row]);
      }
    return M;
  }

  void normalize() {
    // Change results of type nan+2i, 2+nani, nan+nani to nan (+0i)
    // Change complex matrix to normal matrix if all imaginary parts are 0
    if (DI == null)
      return;

    boolean complex = false;
    for (int c=0; c<cols; c++)
      for (int r=0; r<rows; r++) {
        if (D[c][r].isNan() || DI[c][r].isNan()) {
          D[c][r].makeNan();
          DI[c][r].makeZero();
        }
        if (!DI[c][r].isZero())
          complex = true;
      }
    if (!complex)
      DI = null;
  }

  public static Matrix add(Matrix A, Matrix B) {
    if (isInvalid(A) || isInvalid(B) || A.rows != B.rows || A.cols != B.cols)
      return null;

    Matrix M = new Matrix(A);
    if (B.DI != null)
      M.allocImag();
    for (int c=0; c<M.cols; c++)
      for (int r=0; r<M.rows; r++) {
        M.D[c][r].add(B.D[c][r]);
        if (B.DI != null)
          M.DI[c][r].add(B.DI[c][r]);
      }
    M.normalize();
    return M;
  }

  public static Matrix sub(Matrix A, Matrix B) {
    if (isInvalid(A) || isInvalid(B) || A.rows != B.rows || A.cols != B.cols)
      return null;

    Matrix M = new Matrix(A);
    if (B.DI != null)
      M.allocImag();
    for (int c=0; c<M.cols; c++)
      for (int r=0; r<M.rows; r++) {
        M.D[c][r].sub(B.D[c][r]);
        if (B.DI != null)
          M.DI[c][r].sub(B.DI[c][r]);
      }
    M.normalize();
    return M;
  }

  public static Matrix mul(Matrix A, Matrix B) {
    if (isInvalid(A) || isInvalid(B) || A.cols != B.rows)
      return null;

    Matrix M = new Matrix(A.rows, B.cols);
    Real tmp = new Real();
    if (A.DI == null && B.DI == null) {
      for (int c=0; c<B.cols; c++)
        for (int r=0; r<A.rows; r++)
          for (int k=0; k<A.cols; k++) {
            tmp.assign(A.D[k][r]);
            tmp.mul(B.D[c][k]);
            M.D[c][r].add(tmp);
          }
    } else {
      M.allocImag();
      Real tmpI = new Real();
      for (int c=0; c<B.cols; c++)
        for (int r=0; r<A.rows; r++)
          for (int k=0; k<A.cols; k++) {
            tmp.assign(A.D[k][r]);
            tmpI.assign(A.DI!=null ? A.DI[k][r] : Real.ZERO);
            Complex.mul(tmp,tmpI,
                        B.D[c][k], B.DI!=null ? B.DI[c][k] : Real.ZERO);
            M.D[c][r].add(tmp);
            M.DI[c][r].add(tmpI);
          }
    }
    M.normalize();
    return M;
  }

  public static Matrix mul(Matrix A, Real b, Real bI) {
    if (isInvalid(A) || !b.isFinite() || !bI.isFinite())
      return null;

    Matrix M = new Matrix(A);
    if (M.DI == null && bI.isZero()) {
      for (int c=0; c<M.cols; c++)
        for (int r=0; r<M.rows; r++)
          M.D[c][r].mul(b);
    } else {
      M.allocImag();
      for (int c=0; c<M.cols; c++)
        for (int r=0; r<M.rows; r++)
          Complex.mul(M.D[c][r],M.DI[c][r], b,bI);
    }
    M.normalize();
    return M;
  }

  public static Matrix div(Matrix A, Matrix B) {
    if (isInvalid(A) || isInvalid(B) || A.cols != B.cols)
      return null;
    return mul(A,invert(B));
  }

  public static Matrix div(Matrix A, Real b, Real bI) {
    if (isInvalid(A) || b.isNan() || (b.isZero() && bI.isZero()))
      return null;
    Real tmp = new Real(b);
    if (bI.isZero()) {
      tmp.recip();
      return mul(A,tmp,bI);
    } else {
      Real tmpI = new Real(bI);
      Complex.recip(tmp,tmpI);
      return mul(A,tmp,tmpI);
    }
  }

  public static Matrix div(Real a, Real aI, Matrix B) {
    if (!a.isFinite() || !aI.isFinite() || isInvalid(B))
      return null;
    return mul(invert(B),a,aI);
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
      for (int r=0; r<M.rows; r++) {
        M.D[c][r].neg();
        if (M.DI != null)
          M.DI[c][r].neg();
      }
    return M;
  }

  public static boolean equals(Matrix A, Matrix B) {
    if (isInvalid(A) || isInvalid(B) || A.rows != B.rows || A.cols != B.cols)
      return false;

    for (int c=0; c<A.cols; c++)
      for (int r=0; r<A.rows; r++)
        if (!A.D[c][r].equalTo(B.D[c][r]) ||
            (A.DI!=null && B.DI==null && !A.DI[c][r].isZero()) ||
            (A.DI==null && B.DI!=null && !B.DI[c][r].isZero()) ||
            (A.DI!=null && B.DI!=null && !A.DI[c][r].equalTo(B.DI[c][r])))
          return false;
    return true;
  }

  public static boolean notEquals(Matrix A, Matrix B) {
    if (isInvalid(A) || isInvalid(B) || A.rows != B.rows || A.cols != B.cols)
      return false;

    return !equals(A,B);
  }

  public static Matrix conj(Matrix A) {
    if (isInvalid(A))
      return null;
    if (A.DI == null)
      return A;

    Matrix M = new Matrix(A);
    for (int c=0; c<M.cols; c++)
      for (int r=0; r<M.rows; r++)
        M.DI[c][r].neg();
    return M;
  }

  public static Matrix transp(Matrix A, boolean conj) {
    if (isInvalid(A))
      return null;

    Matrix M = new Matrix(A.cols, A.rows, A.DI!=null);
    for (int c=0; c<M.cols; c++)
      for (int r=0; r<M.rows; r++) {
        M.D[c][r].assign(A.D[r][c]);
        if (M.DI != null) {
          M.DI[c][r].assign(A.DI[r][c]);
          if (conj)
            M.DI[c][r].neg();
        }
      }
    return M;
  }

  public static Matrix invert(Matrix A) {
    return invert(A,null,null);
  }

  public static Matrix invert(Matrix A, Real det, Real detI) {
    if (isInvalid(A))
      return null;

    if (A.rows > A.cols)
    {
      // System is overspecified, calculate the least squares solution
      // by pseudoinversion:    T    -1   T
      //                      (A * A)  * A
      if (det != null) {
        det.makeNan();
        detI.makeZero();
        return null; // Assuming, if det!=null, you don't want the inverse
      }
      Matrix T = transp(A,true);
      return mul(invert(mul(T,A)),T);
    }
    else if (A.rows < A.cols)
    {
      // System is underspecified, calculate the least squares solution
      // by pseudoinversion:   T        T -1
      //                      A * (A * A )
      if (det != null) {
        det.makeNan();
        detI.makeZero();
        return null; // Assuming, if det!=null, you don't want the inverse
      }
      Matrix T = transp(A,true);
      return mul(T,invert(mul(A,T)));
    }

    Matrix T = new Matrix(A);
    Matrix M = new Matrix(A.rows);
    Real[] t;
    Real tmp = new Real();
    int i,j,k;
    if (det != null) {
      det.assign(Real.ONE);
      detI.makeZero();
    }
    for (i=0; i<M.rows; i++)
      M.D[i][i].assign(Real.ONE);

    if (A.DI == null) {
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
    } else {
      M.allocImag();
      Real tmpI = new Real();
      Real best = new Real();
      for (i=0; i<M.rows; i++) {
        best.makeInfinity(1); // -inf
        for (k=j=i; j<M.rows; j++) {
          tmp.assign(T.D[j][i]);
          tmp.sqr();
          tmpI.assign(T.DI[j][i]);
          tmpI.sqr();
          tmp.add(tmpI);
          if (tmp.greaterThan(best)) {
            best.assign(tmp);
            k = j;
          }
        }
        if (k!=i) {
          t = T.D[i];  T.D[i]  = T.D[k];  T.D[k]  = t;
          t = T.DI[i]; T.DI[i] = T.DI[k]; T.DI[k] = t;
          t = M.D[i];  M.D[i]  = M.D[k];  M.D[k]  = t;
          t = M.DI[i]; M.DI[i] = M.DI[k]; M.DI[k] = t;
          if (det != null) {
            det.neg();
            detI.neg();
          }
        }
        if (det != null)
          Complex.mul(det,detI, T.D[i][i],T.DI[i][i]);
        Complex.recip(T.D[i][i],T.DI[i][i]);
        if (!T.D[i][i].isFinite() || !T.DI[i][i].isFinite()) {
          if (det != null) {
            det.makeZero(); // Just in case
            detI.makeZero();
          }
          return null;
        }
        for (j=0; j<M.rows; j++) {
          Complex.mul(M.D[i][j],M.DI[i][j], T.D[i][i],T.DI[i][i]);
          if (j > i)
            Complex.mul(T.D[i][j],T.DI[i][j], T.D[i][i],T.DI[i][i]);
          for (k=0; k<M.rows; k++)
            if (k != i) {
              tmp.assign(T.D[k][i]);
              tmpI.assign(T.DI[k][i]);
              Complex.mul(tmp,tmpI, M.D[i][j],M.DI[i][j]);
              M.D[k][j].sub(tmp);
              M.DI[k][j].sub(tmpI);
              if (j>i) {
                tmp.assign(T.D[k][i]);
                tmpI.assign(T.DI[k][i]);
                Complex.mul(tmp,tmpI, T.D[i][j],T.DI[i][j]);
                T.D[k][j].sub(tmp);
                T.DI[k][j].sub(tmpI);
              }
            }
        }
      }
      M.normalize();
    }
    return M;
  }

  public void det(Real det, Real detI) {
    invert(this,det,detI);
  }

  public void trace(Real Tr, Real TrI) {
    Tr.makeZero();
    TrI.makeZero();
    for (int i=0; i<cols && i<rows; i++) {
      Tr.add(D[i][i]);
      if (DI != null)
        TrI.add(D[i][i]);
    }
  }

  public void norm_F(Real norm2, Real norm2I) {
    Real tmp = new Real();
    norm2.makeZero();
    norm2I.makeZero();
    if (DI == null) {
      for (int c=0; c<cols; c++)
        for (int r=0; r<rows; r++) {
          tmp.assign(D[c][r]);
          tmp.sqr();
          norm2.add(tmp);
        }
      norm2.sqrt();
    } else {
      Real tmpI = new Real();
      for (int c=0; c<cols; c++)
        for (int r=0; r<rows; r++) {
          tmp.assign(D[c][r]);
          tmpI.assign(DI[c][r]);
          Complex.sqr(tmp,tmpI);
          norm2.add(tmp);
          norm2I.add(tmpI);
        }
      Complex.sqrt(norm2,norm2I);
    }
  }

  public static Matrix concat(Matrix A, Matrix B) {
    if (isInvalid(A) || isInvalid(B) || A.rows != B.rows)
      return null;
    Matrix M = new Matrix(A.rows,A.cols+B.cols);
    M.setSubMatrix(0,0,A);
    M.setSubMatrix(0,A.cols,B);
    return M;
  }

  public static Matrix concat(Matrix A, Real b, Real bI) {
    if (isInvalid(A) || A.rows != 1)
      return null;
    Matrix M = new Matrix(1,A.cols+1);
    M.setSubMatrix(0,0,A);
    M.setElement(0,A.cols,b,bI);
    return M;
  }

  public static Matrix concat(Real a, Real aI, Matrix B) {
    if (isInvalid(B) || B.rows != 1)
      return null;
    Matrix M = new Matrix(1,B.cols+1);
    M.setElement(0,0,a,aI);
    M.setSubMatrix(0,1,B);
    return M;
  }

  public static Matrix concat(Real a, Real aI, Real b, Real bI) {
    Matrix M = new Matrix(1,2);
    M.setElement(0,0,a,aI);
    M.setElement(0,1,b,bI);
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

  public static Matrix stack(Matrix A, Real b, Real bI) {
    if (isInvalid(A) || A.cols != 1)
      return null;
    Matrix M = new Matrix(A.rows+1,1);
    M.setSubMatrix(0,0,A);
    M.setElement(A.rows,0,b,bI);
    return M;
  }

  public static Matrix stack(Real a, Real aI, Matrix B) {
    if (isInvalid(B) || B.cols != 1)
      return null;
    Matrix M = new Matrix(B.rows+1,1);
    M.setElement(0,0,a,aI);
    M.setSubMatrix(1,0,B);
    return M;
  }

  public static Matrix stack(Real a, Real aI, Real b, Real bI) {
    Matrix M = new Matrix(2,1);
    M.setElement(0,0,a,aI);
    M.setElement(1,0,b,bI);
    return M;
  }
}
