package ral;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public final class Calc
    extends MIDlet
{
  private Display display;
  private CalcCanvas screen;
  
  //private PropertyStore propertyStore;
  //private static final byte PROPERTY_PERMANENT_ID = 10;
  //private static final byte PROPERTY_TEMPORARY_ID = 11;

  public Calc()
  {
    display = Display.getDisplay(this);
    screen = new CalcCanvas(this);

    //propertyStore = PropertyStore.open("CalcData");
    //if (propertyStore == null) {
    //  // Fatal error
    //  System.out.println("Cannot read application properties");
    //}
  }
  
  public void startApp()
  {
    display.setCurrent(screen);
  }

  public void pauseApp()
  {
  }

  public void destroyApp(boolean unconditional)
  {
  }
    
  public void exitRequested()
  {
    destroyApp(false);
    notifyDestroyed();
  }

}
