package ral;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public final class Calc
    extends MIDlet
    implements CommandListener
{
  public CalcCanvas screen;
  public SetupCanvas setup;
  public Display display;
  public PropertyStore propertyStore;

  public TextBox newProgram;
  public Command okCommand;
  public Command cancelCommand;
  public int whichProgram;

  public GraphCanvas graph;

  // Setup items
  public boolean hasClearKey = true;
  public byte commandArrangement = 0;
  public boolean bgrDisplay = false;
  
  public static final byte PROPERTY_SETUP = 0;

  public Calc() {
    display = Display.getDisplay(this);
    propertyStore = PropertyStore.open("CalcData");

    newProgram = new TextBox("New program", "", CalcEngine.PROGLABEL_SIZE,
                             TextField.ANY);
    okCommand = new Command("Ok", Command.OK, 1);
    cancelCommand = new Command("Cancel", Command.CANCEL, 1);
    newProgram.addCommand(okCommand);
    newProgram.addCommand(cancelCommand);
    newProgram.setCommandListener(this);
  }
  
  public void startApp() {
    restoreSetup();
    displayScreen();
  }

  public void displayScreen() {
    if (screen == null)
      screen = new CalcCanvas(this);
    display.setCurrent(screen);
  }

  public void askNewProgram(String name, int n) {
    newProgram.setString(name);
    whichProgram = n;
    display.setCurrent(newProgram);
  }

  public void displayGraph(int gx, int gy, int gw, int gh) {
    if (graph == null)
      graph = new GraphCanvas(this,screen);
    graph.init(gx,gy,gw,gh);
    display.setCurrent(graph);
  }
  
  public void commandAction(Command c, Displayable d) {
    if (c == okCommand) {
      screen.calc.progLabels[whichProgram] = newProgram.getString();
      screen.calc.command(CalcEngine.PROG_NEW, whichProgram);
    }
    displayScreen();
  }
    
  public void pauseApp() {
  }

  public void destroyApp(boolean unconditional) {
    if (propertyStore != null) {
      
      saveSetup();
      screen.quit();
      propertyStore.close();
    }
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

  public void restoreSetup() {
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
  }

}
