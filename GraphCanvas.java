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
    addCommand(new Command("Back", Command.BACK, 1));
    setCommandListener(this);
  }

  public void init(int gx, int gy, int gw, int gh) {
    this.gx = gx;
    this.gy = gy;
    this.gw = gw;
    this.gh = gh;
    internalRepaint = false;
  }

  public void paint(Graphics g) {
    cc.drawModeIndicators(g);
    if (!internalRepaint) {
      // Clear screen
      g.setColor(0);
      g.fillRect(gx,gy,gw,gh);
      cc.calc.startGraph(g,gx,gy,gw,gh);
      if (!cc.calc.progRunning) {
        // Make sure "RUN" indicator is turned off by yet another redraw
        midlet.display.callSerially(this); // Calls back run() below later
        return;
      }
    }
    internalRepaint = false;

    if (cc.calc.progRunning) {
      cc.calc.continueGraph(g,gx,gy,gw,gh);
      if (cc.calc.progRunning)
        midlet.display.callSerially(this); // Calls back run() below later
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
