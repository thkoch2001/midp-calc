package ral;

import javax.microedition.lcdui.*;

public class CalcCanvas
    extends Canvas
    implements CommandListener
{
  private GFont mediumFont,smallFont,largeFont;
  private GFont currentFont;

  private final Command backCommand;
  private final Command itemCommand;
  private final Command okCommand;

  private final Calc calc;

  public CalcCanvas(Calc c) {
    calc = c;

    mediumFont = new GFont(GFont.MEDIUM);
    smallFont  = new GFont(GFont.SMALL);
    largeFont  = new GFont(GFont.LARGE);
    currentFont = mediumFont;

    backCommand   = new Command("back"  , Command.BACK  , 1);
    itemCommand   = new Command("ENTER" , Command.ITEM  , 1);
    okCommand     = new Command("+"     , Command.OK    , 1);

    addCommand(backCommand);
    addCommand(itemCommand);
    addCommand(okCommand);
    setCommandListener(this);
  }

  private String text = "";

  public void print(String a) {
    text = (a != null) ? a : "";
    repaint();
  }
  
  public void paint(Graphics g) {
    g.setColor(0);
    g.fillRect(0,0,getWidth(),getHeight());
    int h = currentFont.getHeight();
    currentFont.drawString(g,0,10,text);
    System.out.println(text);
  }

  protected void keyPressed(int key) {
    print(Integer.toString(key)+" (down)");
    if (key=='1') {
      Real a = new Real(Real.PI);
      text = a.toString();
    }
    if (key=='2') {
      Real a = new Real(Real.PI);
      Real b = new Real(Real.E);
      a.mul(b);
      text = a.toString();
    }
    if (key=='3') {
      Real a = new Real(Real.HALF);
      a.gamma();
      a.sqr();
      a.mul(new Real(4));
      text = a.toString();
    }
  }

  protected void keyReleased(int key) {
    print(Integer.toString(key)+" (up)");
  }

  public void commandAction(Command c, Displayable d)
  {
    print(c.getLabel());
    if (c == backCommand)
      calc.exitRequested();
  }

}
