package ral;

import javax.microedition.lcdui.*;

public class CalcCanvas
    extends Canvas
    implements CommandListener
{
  private GFont mediumFont,smallFont,largeFont;
  private GFont currentFont;

  private final Command backCommand;
  private final Command cancelCommand;
  private final Command exitCommand;
  private final Command helpCommand;
  private final Command itemCommand;
  private final Command okCommand;
  private final Command screenCommand;
  private final Command stopCommand;

  private final Calc calc;

  public CalcCanvas(Calc c) {
    calc = c;

    mediumFont = new GFont(GFont.MEDIUM);
    smallFont  = new GFont(GFont.SMALL);
    largeFont  = new GFont(GFont.LARGE);
    currentFont = mediumFont;

    backCommand   = new Command("back"  , Command.BACK  , 1);
    cancelCommand = new Command("cancel", Command.CANCEL, 1);
    exitCommand   = new Command("exit"  , Command.EXIT  , 1);
    helpCommand   = new Command("help"  , Command.HELP  , 1);
    itemCommand   = new Command("item"  , Command.ITEM  , 1);
    okCommand     = new Command("ok"    , Command.OK    , 1);
    screenCommand = new Command("screen", Command.SCREEN, 1);
    stopCommand   = new Command("stop"  , Command.STOP  , 1);

    addCommand(backCommand);
    addCommand(cancelCommand);
    addCommand(exitCommand);
    addCommand(helpCommand);
    addCommand(itemCommand);
    addCommand(okCommand);
    addCommand(screenCommand);
    addCommand(stopCommand);
    setCommandListener(this);
  }

  private String text = "";

  public void print(String a) {
    text = (a == null) ? a : "";
    repaint();
  }
  
  public void paint(Graphics g) {
    g.setColor(0);
    g.fillRect(0,0,getWidth(),getHeight());
    currentFont.drawString(g,0,10,text);
  }

  protected void keyPressed(int key) {
    print(Integer.toString(key));
  }

  protected void keyReleased(int key) {
    print("");
  }

  protected void pointerPressed(int x, int y) {
    print("pointer");
  }

  public void commandAction(Command c, Displayable d)
  {
    print(c.getLabel());
    if (c == exitCommand)
      calc.exitRequested();
  }

}
