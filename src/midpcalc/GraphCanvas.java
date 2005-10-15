package ral;

import javax.microedition.lcdui.*;

public final class GraphCanvas
    extends MyCanvas
    implements CommandListener, Runnable
{
  private CalcCanvas cc;
  private final Calc midlet;
  private final Command brk;

  private int gx,gy,gw,gh;
  private boolean internalRepaint = false;
//  private Image doubleBuffer;

  public GraphCanvas(Calc m, CalcCanvas c) {
    midlet = m;
    cc = c;
    if ((m.commandArrangement & 0x80) != 0) {
      addCommand(brk = new Command("Break",
        SetupCanvas.commandArrangement[(m.commandArrangement&0x7f)*2], 1));
    } else {
      addCommand(brk = new Command("Break",
        SetupCanvas.commandArrangement[(m.commandArrangement&0x7f)*2+1], 1));
    }
    setCommandListener(this);
    setFullScreenMode(c.fullScreen);

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
    setFullScreenMode(cc.fullScreen);
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
    if (isShown() && !internalRepaint) {
      internalRepaint = true;
      repaint();
    }
  }

  public void keyPressed(int key) {
    if (!isFullScreen())
      return;

    // In full-screen mode, what normally works as "clear" should abort
    switch (key) {
      case '0': case '1': case '2': case '3': case '4':
      case '5': case '6': case '7': case '8': case '9': case '*':
        // As long as there are *some* that *don't* abort
        break;
      case KEY_SEND:
      case KEY_END:
      case '#':
      case '\b':
      case -8:
      case -23:
        commandAction(brk, this);
        break;
      default:
        switch (getGameAction(key)) {
          case GAME_A: case GAME_B: case GAME_C: case GAME_D:
            commandAction(brk, this);
            break;
        }
        break;
    }
  }

  public void commandAction(Command c, Displayable d) {
    cc.calc.progRunning = false;
    internalRepaint = false;
    midlet.displayScreen();
  }
}
