public final class Calc 
{
  public static void main(String[] args) {
    Real a = new Real();
    a.assign(Real.MAX);
    //System.out.println(a);
    a.recip();
    System.out.println(a);
    //System.out.println(a.toString(16));
    a.assign(Real.MIN);
    //System.out.println(a.toString(16));
    a.recip();
    //System.out.println(a);
  }
}
