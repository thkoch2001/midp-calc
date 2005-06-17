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
//  private Image doubleBuffer;

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
    //try {
    //  setFullScreenMode(c.fullScreen);
    //} catch (Throwable e) {
    //  // In case of MIDP 1.0
    //}

//    sizeChanged(getWidth(),getHeight());
  }

//   protected void sizeChanged(int w, int h) {
//     if (doubleBuffer==null ||
//         doubleBuffer.getWidth() != w || doubleBuffer.getHeight() != h)
//     {
//       try {
//         doubleBuffer = Image.createImage(w,h);
//       } catch (Throwable e) {
//         doubleBuffer = null;
//       }
//     }
//   }

  public void init(int gx, int gy, int gw, int gh) {
    this.gx = gx;
    this.gy = gy;
    this.gw = gw;
    this.gh = gh;
    internalRepaint = false;
  }

  public void paint(Graphics g /*gr*/) {
    try {
//    Graphics g = doubleBuffer != null ? doubleBuffer.getGraphics() : gr;

    if (!internalRepaint) {
      // Clear screen
      cc.drawModeIndicators(g, false);
      g.setColor(0);
      g.fillRect(gx,gy,gw,gh);
      cc.calc.startGraph(g,gx,gy,gw,gh,midlet.bgrDisplay);
      midlet.display.callSerially(this); // Calls back run() below later
//      if (doubleBuffer != null)
//        gr.drawImage(doubleBuffer,0,0,Graphics.TOP|Graphics.LEFT);
      return;
    }
//    if (doubleBuffer == null)
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

//    if (doubleBuffer != null)
//      gr.drawImage(doubleBuffer,0,0,Graphics.TOP|Graphics.LEFT);
    } catch (OutOfMemoryError e) {
      midlet.outOfMemory();
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
