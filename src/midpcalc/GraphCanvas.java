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
  private boolean internalRepaint;
  private boolean drawAxesFlag;
  private boolean fullAxes;
  private boolean stopped;
//  private Image doubleBuffer;

  private final int nDiv=10;
  private int zoomWidth,xOff,yOff;
  private int zoomX1,zoomX2,zoomY1,zoomY2;
  private boolean zooming;
  private boolean unknownKeyPressed;

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
    drawAxesFlag = false;
    fullAxes = false;
    stopped = false;
    setFullScreenMode(cc.fullScreen);
  }

  public void paint(Graphics g /*gr*/) {
    try {
//    Graphics g = doubleBuffer != null ? doubleBuffer.getGraphics() : gr;

    if (!internalRepaint) {
      // Clear screen
      cc.drawModeIndicators(g, false, stopped);
      g.setColor(0);
      g.fillRect(gx,gy,gw,gh);
      cc.calc.startGraph(g,gx,gy,gw,gh,midlet.bgrDisplay);
      midlet.display.callSerially(this); // Calls back run() below later
//      if (doubleBuffer != null)
//        gr.drawImage(doubleBuffer,0,0,Graphics.TOP|Graphics.LEFT);
      return;
    } else if (drawAxesFlag) {
      if (!stopped)
        fullAxes = false;
      cc.calc.drawAxes(g,gx,gy,gw,gh,midlet.bgrDisplay,!fullAxes);
      if (stopped)
        fullAxes = true;
      drawAxesFlag = false;
    }
//    if (doubleBuffer == null)
      internalRepaint = false;

    if (stopped && (zoomWidth!=nDiv || xOff!=0 || yOff!=0 || zooming)) {
      // Remove old frame (should be XOR, but we don't have that)
      g.setClip(gx,gy,gw,gh);
      g.setColor(0,0,0);
      g.drawRect(zoomX1,zoomY1,zoomX2-zoomX1,zoomY2-zoomY1);
      // Draw zoom frame
      boolean zoomOut = false;
      int zW = zoomWidth;
      if (zoomWidth > nDiv) {
        zoomOut = true;
        zW = 2*nDiv-zW;
        g.setColor(255,0,0);
      } else {
        g.setColor(255,255,255);
      }
      zoomX1 = gx+2+((gw-4-1)* xOff    +nDiv/2)/nDiv;
      zoomX2 = gx+2+((gw-4-1)*(xOff+zW)+nDiv/2)/nDiv;
      zoomY1 = gy+2+((gh-4-1)* yOff    +nDiv/2)/nDiv;
      zoomY2 = gy+2+((gh-4-1)*(yOff+zW)+nDiv/2)/nDiv;
      g.drawRect(zoomX1,zoomY1,zoomX2-zoomX1,zoomY2-zoomY1);
      zooming = true;
    }
    if (cc.calc.progRunning) {
      cc.drawModeIndicators(g, true, stopped);
      if (!stopped) {
        cc.calc.continueGraph(g,gx,gy,gw,gh);
        if (cc.calc.progRunning)
          midlet.display.callSerially(this); // Calls back run() below later
        else
          // If "continueGraph()" halts, go back to main screen
          midlet.displayScreen();
      }
    } else {
      stopped = true;
      cc.drawModeIndicators(g, false, stopped);
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

  private void direction(int direction) {
    if (!stopped)
      return;
    switch (direction) {
      case UP:    if (yOff>-nDiv) yOff--; break;
      case DOWN:  if (yOff< nDiv) yOff++; break;
      case LEFT:  if (xOff>-nDiv) xOff--; break;
      case RIGHT: if (xOff< nDiv) xOff++; break;
      case FIRE:
        if (zoomWidth>2) {
          zoomWidth-=2;
          if (zoomWidth < nDiv) {
            xOff++;
            yOff++;
          } else {
            xOff--;
            yOff--;
          }
        }
        break;
      case '1':
        if (zoomWidth<2*nDiv-2) {
          zoomWidth+=2;
          if (zoomWidth > nDiv) {
            xOff++;
            yOff++;
          } else {
            xOff--;
            yOff--;
          }
        }
        break;
    }
    internalRepaint = true;
    repaint();
  }

  public void keyPressed(int key) {
    switch (key) {
      case '3': case '7': case '9':
        // As long as there are *some* that can be used to "turn lights on"
        break;
      case '1':
        direction(key);
        break;
      case '0':
        drawAxesFlag = true;
        internalRepaint = true;
        repaint();
        break;
      case '*':
        internalRepaint = true;
        if (stopped) {
          stopped = false;
          if (zoomWidth!=nDiv || xOff!=0 || yOff!=0 || zooming) {
            // Do zoom
            boolean zoomOut = false;
            if (zoomWidth > nDiv) {
              zoomOut = true;
              zoomWidth = 2*nDiv-zoomWidth;
            }
            Real factor = new Real(zoomWidth);
            Real xo = new Real(xOff);
            Real yo = new Real(nDiv-zoomWidth-yOff);
            factor.div(nDiv);
            xo.div(nDiv);
            yo.div(nDiv);
            if (zoomOut) {
              factor.recip();
              xo.neg();
              xo.mul(factor);
              yo.neg();
              yo.mul(factor);
            }
            cc.calc.zoomGraph(factor,xo,yo);
            internalRepaint = false; // clearscreen
          }
        } else {
          stopped = true;
          zoomWidth = nDiv;
          xOff=0;
          yOff=0;
          zoomX1 = zoomY1 = -1;
          zoomX2 = getWidth();
          zoomY2 = getHeight();
          zooming = false;
        }
        repaint();
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
          case UP:
          case DOWN:
          case LEFT:
          case RIGHT:
          case FIRE:
            direction(getGameAction(key));
            break;
          default:
            // Nokia and other direct key mappings
            switch (key) {
              case '2':
              case KEY_UP_ARROW:
                direction(UP);
                break;
              case '8':
              case KEY_DOWN_ARROW:
                direction(DOWN);
                break;
              case '4':
              case KEY_LEFT_ARROW:
                direction(LEFT);
                break;
              case '6':
              case KEY_RIGHT_ARROW:
                direction(RIGHT);
                break;
              case '5':
              case KEY_SOFTKEY3:
                direction(FIRE);
                break;
              default:
                if (midlet.doubleKeyEvents)
                  // We don't yet know if we can treat this as "clear"
                  unknownKeyPressed = true;
                else
                  commandAction(brk, this);
                break;
            }
        }
        break;
    }
  }

  public void keyReleased(int key) {
    if (unknownKeyPressed) {
      // It's a "delayed clear key"
      unknownKeyPressed = false;
      commandAction(brk, this);
    }
  }

  public void commandAction(Command c, Displayable d) {
    // If an unknown key has beed pressed but not released, ignore it
    unknownKeyPressed = false;

    cc.calc.progRunning = false;
    internalRepaint = false;
    midlet.displayScreen();
  }
}
