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
    currentFont.drawString(g,0,30," !\"#$%&'()*+,-./");
    currentFont.drawString(g,0,30+h,"0123456789:;<=>?");
    currentFont.drawString(g,0,30+2*h,"@ABCDEFGHIJKLMNO");
    currentFont.drawString(g,0,30+3*h,"PQRSTUVWXYZ[\\]^_");
    currentFont.drawString(g,0,30+4*h,"`abcdefghijklmno");
    currentFont.drawString(g,0,30+5*h,"pqrstuvwxyz{|}~");
    System.out.println(text);
  }

  protected void keyPressed(int key) {
    print(Integer.toString(key)+" (down)");
    if (key=='1')
      currentFont = smallFont;
    if (key=='2')
      currentFont = mediumFont;
    if (key=='3')
      currentFont = largeFont;
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
