package ral;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public final class Calc
    extends MIDlet
{
  public CalcCanvas screen;
  public SetupCanvas setup;
  public Display display;
  public PropertyStore propertyStore;

  // Setup items
  public boolean hasClearKey = true;
  public byte commandArrangement = 0;
  public boolean bgrDisplay = false;
  
  public static final byte PROPERTY_SETUP = 0;

  public Calc() {
    display = Display.getDisplay(this);
    propertyStore = PropertyStore.open("CalcData");
  }
  
  public void startApp() {
    if (propertyStore != null) {
      byte [] buf = new byte[4];
      buf[0] = PROPERTY_SETUP;
      int length = propertyStore.getProperty(buf);
      if (length >= 4) {
        hasClearKey = buf[1] != 0;
        commandArrangement = buf[2];
        bgrDisplay = buf[3] != 0;
      } else {
        setup = new SetupCanvas(this);
        if (!setup.isFinished())
          display.setCurrent(setup);
        return;
      }
    }
    displayScreen();
  }

  public void displayScreen() {
    if (screen == null)
      screen = new CalcCanvas(this);
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

  public void saveSetup() {
    if (propertyStore != null) {
      byte [] buf = new byte[4];
      buf[0] = PROPERTY_SETUP;
      buf[1] = (byte)(hasClearKey ? 1 : 0);
      buf[2] = commandArrangement;
      buf[3] = (byte)(bgrDisplay ? 1 : 0);
      propertyStore.setProperty(buf,4);
    }
  }

}
