package ral;

import java.awt.*;
import java.awt.event.*;
import javax.microedition.midlet.*;

public class CalcApplet
  extends java.applet.Applet
  implements KeyListener, MouseListener
{
  private MIDlet calc;
  
  public void paint(Graphics g) {
    Display.getDisplay(calc).getCurrent().setApplet(this);
    Display.getDisplay(calc).getCurrent().paint(g);
  }

  public void keyReleased(KeyEvent e) { }
  public void keyTyped(KeyEvent e) {
    Display.getDisplay(calc).getCurrent().processKeyPress(e.getKeyChar());
  }
  public void keyPressed(KeyEvent e) { }
  
  public void mouseClicked(MouseEvent e) { }
  public void mouseEntered(MouseEvent e) { }
  public void mouseExited(MouseEvent e) { }
  public void mouseReleased(MouseEvent e) { }
  public void mousePressed(MouseEvent e)
  {
    Display.getDisplay(calc).getCurrent().
      processPointerPress(e.getX(),e.getY());
  }

  public String getAppletInfo() {
    return "Runs Calc in a simulated MIDP device";
  }

  public void start() {
    addKeyListener(this);
    addMouseListener(this);
    calc = new Calc();
    calc.startApp();
  }

  public void stop() {
    calc.destroyApp(true);
  }
}
