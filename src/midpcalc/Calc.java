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
  public boolean hasClearKey = true;
  public byte commandArrangement = 0;
  
  public static final byte PROPERTY_SETUP = 0;

  public Calc() {
    display = Display.getDisplay(this);
    propertyStore = PropertyStore.open("CalcData");
  }
  
  public void startApp() {
    if (propertyStore != null) {
      byte [] buf = new byte[3];
      buf[0] = PROPERTY_SETUP;
      int length = propertyStore.getProperty(buf);
      if (length >= 3) {
        hasClearKey = buf[1] != 0;
        commandArrangement = buf[2];
      } else {
        setup = new SetupCanvas(this);
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
      byte [] buf = new byte[3];
      buf[0] = PROPERTY_SETUP;
      buf[1] = (byte)(hasClearKey ? 1 : 0);
      buf[2] = commandArrangement;
      propertyStore.setProperty(buf,3);
    }
  }

}
