package javax.microedition.lcdui;

import java.awt.*;

public abstract class Canvas extends Displayable
{
  public static final int UP = 1;
  public static final int LEFT = 2;
  public static final int RIGHT = 5;
  public static final int DOWN = 6;
  public static final int FIRE = 8;
  public static final int GAME_A = 9;
  public static final int GAME_B = 10;
  public static final int GAME_C = 11;
  public static final int GAME_D = 12;

  protected Canvas() {
  }

  int getGameAction(int keyCode) {
    switch (keyCode) {
      case '8': return UP;
      case '4': return LEFT;
      case '6': return RIGHT;
      case '2': return DOWN;
      case '5': return FIRE;
    }
    return 0;
  }

  int getHeight() {
    return applet.getHeight(); // ... -kanter
  }

  int getWidth() {
    return applet.getWidth(); // ... -kanter
  }

  boolean hasPointerEvents() {
    return true;
  }

  void repaint() {
    applet.repaint();
  }

  void repaint(int x, int y, int width, int height) {
    applet.repaint(x,y,width,height);
  }

  protected abstract void paint(Graphics g);

  public void paint(java.awt.Graphics g) {
    // ... draw commands
    // ... clipping
    paint(new Graphics(g,applet));
  }
}
