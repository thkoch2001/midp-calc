package javax.microedition.lcdui;

public class Display 
{
  Displayable current;

  public void callSerially(Runnable r) {
    java.awt.EventQueue.invokeLater(r);
  }
  
  public Displayable getCurrent() {
    return current;
  }

  public static Display getDisplay(MIDlet m) {
    return m.display;
  }

  public boolean isColor() {
    return true;
  }

  public int numColors() {
    return 1<<24;
  }

  public void setCurrent(Displayable next) {
    current = next;
    ral.CalcApplet.getCurrentApplet().repaint();
  }
}
