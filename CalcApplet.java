package ral;

import java.awt.*;
import java.awt.event.*;
import javax.microedition.midlet.*;

public class CalcApplet
  extends java.applet.Applet
  implements KeyListener, MouseListener
{
  private MIDlet calc;
  private static CalcApplet currentApplet;
  
  public void paint(Graphics g) {
    Display.getDisplay(calc).getCurrent().paint(g);
  }

  public void keyReleased(KeyEvent e) { }
  public void keyTyped(KeyEvent e) {
    int key = e.getKeyChar();
    System.out.println("keyTyped("+key+")");
    Display.getDisplay(calc).getCurrent().processKeyPress(key);
  }
  public void keyPressed(KeyEvent e) {
    System.out.println("keyPressed("+e.getKeyCode()+")");
    int key = 0;
    switch (e.getKeyCode()) {
      case KeyEvent.VK_UP:
      case KeyEvent.VK_KP_UP:
        key = -1;
        break;
      case KeyEvent.VK_DOWN:
      case KeyEvent.VK_KP_DOWN:
        key = -2;
        break;
      case KeyEvent.VK_LEFT:
      case KeyEvent.VK_KP_LEFT:
        key = -3;
        break;
      case KeyEvent.VK_RIGHT:
      case KeyEvent.VK_KP_RIGHT:
        key = -4;
        break;
      case KeyEvent.VK_HOME:
        key = -5;
        break;
    }
    if (key != 0)
      Display.getDisplay(calc).getCurrent().processKeyPress(key);
  }
  
  public void mouseClicked(MouseEvent e) { }
  public void mouseEntered(MouseEvent e) {
    requestFocus();
  }
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
    currentApplet = this;
    addKeyListener(this);
    addMouseListener(this);
    setBackground(Color.BLACK);
    calc = new Calc();
    calc.startApp();
  }

  public void stop() {
    calc.destroyApp(true);
  }

  public static CalcApplet getCurrentApplet() {
    return currentApplet;
  }
}
