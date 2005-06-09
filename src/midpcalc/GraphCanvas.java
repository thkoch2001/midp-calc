package ral;

import javax.microedition.lcdui.*;

public final class GraphCanvas
    extends Canvas
    implements CommandListener, Runnable
{
  private CalcCanvas cc;
  private final Calc midlet;

  private int gx,gy,gw,gh;
  private boolean internalRepaint = false;

  public GraphCanvas(Calc m, CalcCanvas c) {
    midlet = m;
    cc = c;
    if ((m.commandArrangement & 0x80) != 0) {
      addCommand(new Command("Break",
        SetupCanvas.commandArrangement[(m.commandArrangement&0x7f)*2], 1));
    } else {
      addCommand(new Command("Break",
        SetupCanvas.commandArrangement[(m.commandArrangement&0x7f)*2+1], 1));
    }
    setCommandListener(this);
    try {
      //setFullScreenMode(c.fullScreen);
    } catch (Throwable e) {
      // In case of MIDP 1.0
    }
  }

  public void init(int gx, int gy, int gw, int gh) {
    this.gx = gx;
    this.gy = gy;
    this.gw = gw;
    this.gh = gh;
    internalRepaint = false;
  }

  public void paint(Graphics g) {
    if (!internalRepaint) {
      // Clear screen
      cc.drawModeIndicators(g, false);
      g.setColor(0);
      g.fillRect(gx,gy,gw,gh);
      cc.calc.startGraph(g,gx,gy,gw,gh);
      midlet.display.callSerially(this); // Calls back run() below later
      return;
    }
    internalRepaint = false;

    if (cc.calc.progRunning) {
      cc.drawModeIndicators(g, true);
      cc.calc.continueGraph(g,gx,gy,gw,gh);
      if (cc.calc.progRunning)
        midlet.display.callSerially(this); // Calls back run() below later
      else
        // If "continueGraph()" halts, go back to main screen
        midlet.displayScreen();
    } else {
      cc.drawModeIndicators(g, false);
    }
  }

  public void run() {
    if (isShown()) {
      internalRepaint = true;
      repaint();
    }
  }

  public void commandAction(Command c, Displayable d) {
    cc.calc.progRunning = false;
    internalRepaint = false;
    midlet.displayScreen();
  }
}
