package javax.microedition.lcdui;

public class Graphics
{
  public static final int BASELINE = 64;
  public static final int BOTTOM = 32;
  public static final int HCENTER = 1;
  public static final int LEFT = 4;
  public static final int RIGHT = 8;
  public static final int TOP = 16;
  public static final int VCENTER = 2;

  java.awt.Graphics g;
  java.applet.Applet applet;
  Font font;

  public Graphics(java.awt.Graphics graphics, java.applet.Applet a) {
    g = graphics;
    applet = a;
  }

  public void setColor(int RGB) {
    g.setColor(new java.awt.Color(RGB));
  }
  
  public void setColor(int red, int green, int blue) {
    g.setColor(new java.awt.Color(red,green,blue));
  }
  
  public void fillRect(int x, int y, int width, int height) {
    g.fillRect(x,y,width,height);
  }

  public void fillArc(int x, int y, int width, int height,
               int startAngle, int arcAngle) {
    g.fillArc(x,y,width,height,startAngle,arcAngle);
  }

  public void setFont(Font f) {
    font = f;
    g.setFont(font.font);
  }

  public Font getFont() {
    return font;
  }

  public void setClip(int x, int y, int width, int height) {
    g.setClip(x,y,width,height);
  }

  public void drawString(String str, int x, int y, int anchor) {
    if ((anchor & HCENTER)!=0)
      x -= font.stringWidth(str)/2;
    else if ((anchor & RIGHT)!=0)
      x -= font.stringWidth(str);
    if ((anchor & TOP)!=0)
      y += font.getBaselinePosition();
    else if ((anchor & BOTTOM)!=0)
      y = y+font.getBaselinePosition()-font.getHeight();
    g.drawString(str,x,y);
  }
  
  public void drawSubstring(String str, int offset, int len,
			    int x, int y, int anchor) {
    drawString(str.substring(offset,offset+len),x,y,anchor);
  }
  
  public void drawChar(char ch, int x, int y, int anchor) {
    drawString(String.valueOf(ch),x,y,anchor);
  }
  
  public void drawLine(int x1, int y1, int x2, int y2) {
    g.drawLine(x1,y1,x2,y2);
  }
  
  public void drawImage(Image img, int x, int y, int anchor) {
    g.drawImage(img.image, x, y, applet);
  }
}
