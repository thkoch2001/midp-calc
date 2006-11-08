package javax.microedition.lcdui;
import javax.microedition.midlet.*;

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
    return m.midletDisplay;
  }

  public boolean isColor() {
    return true;
  }

  public int numColors() {
    return 1<<24;
  }

  public void setCurrent(Displayable next) {
    current = next;
    midpcalc.CalcApplet.getCurrentApplet().repaint();
  }
}
