package javax.microedition.lcdui;

public abstract class Displayable 
{
  Command a,b;
  CommandListener commandListener;
  Font commandFont;
  int width, height;
  String [] label = {"0",".","-/e","1","2","3","4","5","6","7","8","9"};
  char [] key     = {'0','*','#','1','2','3','4','5','6','7','8','9'};

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
    if (y<height-(commandFont.getHeight()+1)*6)
      pointerPressed(x,y);
    else {
      if (y<height-(commandFont.getHeight()+1)*5) {
        if (x<width/2) {
          if (a != null && commandListener != null)
            commandListener.commandAction(a, this);
        } else {
          if (b != null && commandListener != null)
            commandListener.commandAction(b, this);
        }
      } else if (y<height-(commandFont.getHeight()+1)*4) {
        keyPressed('\b');
      } else {
        x = x*3/width;
        y = (height-y)/(commandFont.getHeight()+1);
        keyPressed(key[y*3+x]);
      }
    }
  }
  
  public int getHeight() {
    return height - (commandFont.getHeight()+1)*6;
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
    g.fillRect(0,height-(commandFont.getHeight()+1)*6,width,
               (commandFont.getHeight()+1)*6);
    g.setColor(java.awt.Color.LIGHT_GRAY);
    for (int i=1; i<=6; i++)
      g.fillRect(0,height-(commandFont.getHeight()+1)*i,width,1);
    g.fillRect(width/2,height-(commandFont.getHeight()+1)*6,
               1,commandFont.getHeight()+1);
    g.fillRect(width/3,height-(commandFont.getHeight()+1)*4,
               1,(commandFont.getHeight()+1)*4);
    g.fillRect(2*width/3,height-(commandFont.getHeight()+1)*4,
               1,(commandFont.getHeight()+1)*4);
    g.setColor(java.awt.Color.BLACK);
    g.setFont(commandFont.font);
    if (a != null) {
      g.drawString(a.getLabel(),3,height-(commandFont.getHeight()+1)*6+
                   commandFont.getBaselinePosition());
    }
    if (b != null) {
      g.drawString(b.getLabel(),width-3-commandFont.stringWidth(b.getLabel()),
                   height-(commandFont.getHeight()+1)*6+
                   commandFont.getBaselinePosition());
    }
    g.drawString("<-",3+width/2-commandFont.stringWidth("<-")/2,
                 height-(commandFont.getHeight()+1)*5+
                 commandFont.getBaselinePosition());
    for (int y=0; y<4; y++)
      for (int x=0; x<3; x++)
        g.drawString(label[y*3+x],
                     (x*2+1)*width/6-commandFont.stringWidth(label[y*3+x])/2,
                     height-(commandFont.getHeight()+1)*(y+1)+
                     commandFont.getBaselinePosition());

    g.clipRect(0,0,width,height-commandFont.getHeight()-1);
    paint(g);
  }
}
