package ral;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public final class Calc
    extends MIDlet
{
  private CalcCanvas screen;
  public Display display;
  
  public Calc() {
    display = Display.getDisplay(this);
    screen = new CalcCanvas(this);
  }
  
  public void startApp() {
    display.setCurrent(screen);
  }

  public void pauseApp() {
  }

  public void destroyApp(boolean unconditional) {
    screen.quit();
  }
    
  public void exitRequested() {
    destroyApp(false);
    notifyDestroyed();
  }

}
