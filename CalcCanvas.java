package ral;

import javax.microedition.lcdui.*;

public class CalcCanvas
    extends Canvas
    implements CommandListener
{
  private GFont mediumFont,smallFont,largeFont;
  private GFont currentFont;
  private CalcEngine calc;

  private final Command add;
  private final Command enter;

  private final Calc midlet;

  private int numRepaintLines = 0;
  private boolean repeating = false;
  private boolean internalRepaint = false;
  private int offX, offY, width, height, charWidth, charHeight;

  public CalcCanvas(Calc m) {
    midlet = m;

    mediumFont = new GFont(GFont.MEDIUM);
    smallFont  = new GFont(GFont.SMALL);
    largeFont  = new GFont(GFont.LARGE);
    currentFont = mediumFont;

    enter = new Command("ENTER" , Command.OK  , 1);
    add   = new Command("+"     , Command.ITEM, 1);
    addCommand(enter);
    addCommand(add);
    setCommandListener(this);

    charWidth = currentFont.charWidth();
    width = getWidth()/charWidth;
    offX = (getWidth()-width*charWidth)/2;
    charHeight = currentFont.getHeight();
    height = getHeight()/charHeight;
    offY = (getHeight()-height*charHeight)/2;

    calc = new CalcEngine();
    calc.setMaxWidth(width);
    numRepaintLines = 100;
  }

  public void paint(Graphics g) {
    boolean cleared = false;
    if (numRepaintLines == 100 || !internalRepaint) {
      g.setColor(0);
      g.fillRect(0,0,getWidth(),getHeight());
      cleared = true;
      numRepaintLines = 100;
    }
    internalRepaint = false;

    if (numRepaintLines > 0) {
      if (numRepaintLines > height)
        numRepaintLines = height;

      for (int i=0; i<numRepaintLines; i++) {
        if (i==0 && calc.inputIsInProgress()) {
          StringBuffer tmp = calc.getInputBuffer();
          tmp.append('_');
          currentFont.drawString(g,offX,offY+(height-1)*charHeight,tmp);
          if (!cleared) {
            g.setColor(0);
            g.fillRect(offX+tmp.length()*charWidth,offY+(height-1)*charHeight,
                       (width-tmp.length())*charWidth,charHeight);
          }
          tmp.setLength(tmp.length()-1);
        } else {
          String tmp = calc.getStackElement(i);
          currentFont.drawString(
            g,offX+(width-tmp.length())*charWidth,
            offY+(height-1-i)*charHeight,tmp);
          if (!cleared) {
            g.setColor(0);
            g.fillRect(offX,offY+(height-1-i)*charHeight,
                       (width-tmp.length())*charWidth,charHeight);
          }
        }
      }
    }
    numRepaintLines = 0;
  }

  private void checkRepaint() {
    int repaintLines = calc.numRepaintLines();
    if (repaintLines > numRepaintLines)
      numRepaintLines = repaintLines;
    if (numRepaintLines > 0) {
      internalRepaint = true;
      repaint();
    }
  }

  protected void keyPressed(int key) {
    repeating = false;
    switch (key) {
      case '0': case '1': case '2': case '3': case '4':
      case '5': case '6': case '7': case '8': case '9':
        calc.command(CalcEngine.DIGIT_0+key-'0',0);
        break;
      case '#':
        calc.command(CalcEngine.SIGN_E,0);
        break;
      case '*':
        calc.command(CalcEngine.DEC_POINT,0);
        break;
      case -8:
        calc.command(CalcEngine.CLEAR,0);
        break;
      case -1: // UP
      case -2: // DOWN
      case -3: // LEFT
      case -4: // RIGHT
      case -5: // PUSH
        // Menu-stuff
        break;
    }
    checkRepaint();
  }

  protected void keyRepeated(int key) {
    switch (key) {
      case '1': case '2': case '3': case '4': case '5': case '6':
        if (repeating)
          return;
        calc.command(CalcEngine.DIGIT_A+key-'1',0);
        break;
      case -8:
        calc.command(CalcEngine.CLEAR,0);
        break;
    }
    repeating = true;
    checkRepaint();
  }

  public void commandAction(Command c, Displayable d)
  {
    if (c == enter)
      calc.command(CalcEngine.ENTER,0);
    if (c == add)
      calc.command(CalcEngine.ADD,0);
    checkRepaint();
  }

}
