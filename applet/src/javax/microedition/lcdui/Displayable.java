package javax.microedition.lcdui;

public abstract class Displayable 
{
  Command a,b;
  CommandListener commandListener;
  java.applet.Applet applet;
  
  public void addCommand(Command cmd) {
    if (a == null)
      a = cmd;
    else
      b = cmd;
  }

  public boolean isShown() {
    return true;
  }

  public void removeCommand(Command cmd) {
    if (cmd == a)
      a = null;
    else if (cmd == b)
      b = null;
  }

  public void setCommandListener(CommandListener l) {
    commandListener = l;
  }

  public void setApplet(java.applet.Applet a) {
    applet = a;
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
    pointerPressed(x,y);
  }
  
  public abstract void paint(java.awt.Graphics g);
}
