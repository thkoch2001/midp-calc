package javax.microedition.lcdui;

public abstract class Displayable 
{
  Command a,b;
  CommandListener commandListener;
  Font commandFont;
  int width, height;

  Displayable() {
    commandFont = Font.getFont(
      Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_MEDIUM);
    width = ral.CalcApplet.getCurrentApplet().getWidth();
    height = ral.CalcApplet.getCurrentApplet().getHeight();
  }
  
  public void addCommand(Command cmd) {
    if (a == null)
      a = cmd;
    else
      b = cmd;
    ral.CalcApplet.getCurrentApplet().repaint();
  }

  public boolean isShown() {
    return true;
  }

  public void removeCommand(Command cmd) {
    if (cmd == a)
      a = null;
    else if (cmd == b)
      b = null;
    ral.CalcApplet.getCurrentApplet().repaint();
  }

  public void setCommandListener(CommandListener l) {
    commandListener = l;
  }

  protected void keyPressed(int keyCode) { }
  protected void keyRepeated(int keyCode) { }
  protected void pointerPressed(int x, int y) { }

  public void processKeyPress(int keyCode) {
    keyPressed(keyCode);
  }
  public void processKeyRepeat(int keyCode) {
    keyRepeated(keyCode);
  }
  public void processPointerPress(int x, int y) {
    if (y<height-commandFont.getHeight()-1)
      pointerPressed(x,y);
    else {
      if (x<width/2) {
        if (a != null && commandListener != null)
          commandListener.commandAction(a, this);
      } else {
        if (b != null && commandListener != null)
          commandListener.commandAction(b, this);
      }
    }
  }
  
  public int getHeight() {
    return height - commandFont.getHeight()-1;
  }

  public int getWidth() {
    return width;
  }

  public void repaint() {
    ral.CalcApplet.getCurrentApplet().repaint();
  }

  public void repaint(int x, int y, int width, int height) {
    ral.CalcApplet.getCurrentApplet().repaint(x,y,width,height);
  }

  public abstract void paint(java.awt.Graphics g);
  
  public void processRepaint(java.awt.Graphics g) {
    g.setColor(java.awt.Color.WHITE);
    g.fillRect(0,height-commandFont.getHeight(),width,commandFont.getHeight());
    g.setColor(java.awt.Color.LIGHT_GRAY);
    g.fillRect(0,height-commandFont.getHeight()-1,width,1);
    g.setColor(java.awt.Color.BLACK);
    g.setFont(commandFont.font);
    if (a != null) {
      g.drawString(a.getLabel(),3,height-commandFont.getHeight()+
                   commandFont.getBaselinePosition());
    }
    if (b != null) {
      g.drawString(b.getLabel(),width-3-commandFont.stringWidth(b.getLabel()),
                   height-commandFont.getHeight()+
                   commandFont.getBaselinePosition());
    }
    g.clipRect(0,0,width,height-commandFont.getHeight()-1);
    paint(g);
  }
}
