package ral;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.io.*;

public final class Calc
    extends MIDlet
    implements CommandListener
{
  public CalcCanvas screen;
  public SetupCanvas setup;
  public Display display;
  public DataStore dataStore;

  public TextBox newProgram;
  public Command okCommand;
  public Command cancelCommand;
  public int whichProgram;

  public GraphCanvas graph;

  // Setup items
  public boolean hasClearKey = true;
  public byte commandArrangement = 0;
  public boolean bgrDisplay = false;
  
  public Calc() {
    display = Display.getDisplay(this);
    dataStore = DataStore.open("CalcData");
  }
  
  public void startApp() {
    DataInputStream in = null;
    if (dataStore != null)
      in = dataStore.startReading();
    if (in != null) {
      try {
        restoreSetup(in);
        screen = new CalcCanvas(this,in);
      } catch (IOException ioe) {
      }
    } else {
      setup = new SetupCanvas(this);
      if (!setup.isFinished()) {
        display.setCurrent(setup);
        return;
      }
    }
    displayScreen();
  }

  public void displayScreen() {
    if (screen == null)
      screen = new CalcCanvas(this,null);
    display.setCurrent(screen);
  }

  public void displayGraph(int gx, int gy, int gw, int gh) {
    if (graph == null)
      graph = new GraphCanvas(this,screen);
    graph.init(gx,gy,gw,gh);
    display.setCurrent(graph);
  }
  
  public void askNewProgram(String name, int n) {
    if (newProgram == null) {
      newProgram = new TextBox("New program", "", CalcEngine.PROGLABEL_SIZE,
                               TextField.ANY);
      okCommand = new Command("Ok", Command.OK, 1);
      cancelCommand = new Command("Cancel", Command.CANCEL, 1);
      newProgram.addCommand(okCommand);
      newProgram.addCommand(cancelCommand);
      newProgram.setCommandListener(this);
    }
    newProgram.setString(name);
    whichProgram = n;
    display.setCurrent(newProgram);
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
    if (dataStore != null) {
      DataOutputStream out = dataStore.startWriting();
      if (out != null) {
        try {
          saveSetup(out);
          screen.saveState(out);
        } catch (IOException ioe) {
        }
      }
      dataStore.close();
    }
  }
    
  public void exitRequested() {
    destroyApp(false);
    notifyDestroyed();
  }

  public void saveSetup(DataOutputStream out) throws IOException {
    out.writeShort(3);
    out.writeBoolean(hasClearKey);
    out.writeByte(commandArrangement);
    out.writeBoolean(bgrDisplay);
  }

  public void restoreSetup(DataInputStream in) throws IOException {
    short length = in.readShort();
    if (length >= 3) {
      hasClearKey = in.readBoolean();
      commandArrangement = in.readByte();
      bgrDisplay = in.readBoolean();
      length -= 3;
    }
    in.skip(length);
  }

}
