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

  java.awt.Image image;
  Graphics graphics;

  protected Canvas() {
    image = new java.awt.image.BufferedImage(
      getWidth(),getHeight(),java.awt.image.BufferedImage.TYPE_INT_RGB);
    graphics = new Graphics(image.getGraphics(),
			    ral.CalcApplet.getCurrentApplet());
  }

  public int getGameAction(int keyCode) {
    switch (keyCode) {
      case -1:
      case '8': return UP;
      case -3:
      case '4': return LEFT;
      case -4:
      case '6': return RIGHT;
      case -2:
      case '2': return DOWN;
      case -5:
      case '5': return FIRE;
    }
    return 0;
  }

  public int getHeight() {
    return ral.CalcApplet.getCurrentApplet().getHeight(); // ... -kanter
  }

  public int getWidth() {
    return ral.CalcApplet.getCurrentApplet().getWidth(); // ... -kanter
  }

  public boolean hasPointerEvents() {
    return true;
  }

  public void repaint() {
    ral.CalcApplet.getCurrentApplet().repaint();
  }

  public void repaint(int x, int y, int width, int height) {
    ral.CalcApplet.getCurrentApplet().repaint(x,y,width,height);
  }

  protected abstract void paint(Graphics g);

  public void paint(java.awt.Graphics g) {
    // ... draw commands
    // ... clipping
    paint(graphics);
    g.drawImage(image,0,0,ral.CalcApplet.getCurrentApplet());
  }
}
