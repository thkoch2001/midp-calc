package ral;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public final class Calc
    extends MIDlet
{
  private CalcCanvas screen;
  
  public Calc() {
    screen = new CalcCanvas(this);
  }
  
  public void startApp() {
    Display.getDisplay(this).setCurrent(screen);
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
