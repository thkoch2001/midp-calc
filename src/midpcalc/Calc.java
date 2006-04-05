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
  public Form resetConfirmation;
  public Form messageScreen;
  public Command okCommand;
  public Command cancelCommand;
  public int whichProgram;

  public GraphCanvas graph;

  // Setup items
  public boolean hasClearKey = true;
  public byte commandArrangement = 0;
  public boolean bgrDisplay = false;
  public boolean doubleKeyEvents = false;
  
  public Calc() {
    display = Display.getDisplay(this);
    dataStore = DataStore.open("CalcData");
  }
  
  public void startApp() {
    if (screen == null) {
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
  
  public void askNewProgram(String name, int n, boolean newprog) {
    // always create because of varying title -- setTitle requires MIDP2
    newProgram = new TextBox(newprog ? "New program" : "Edit program",
                               "", CalcEngine.PROGLABEL_SIZE, TextField.ANY);
    if (okCommand == null) {
      okCommand = new Command("Ok", Command.OK, 1);
      cancelCommand = new Command("Cancel", Command.CANCEL, 1);
    }
    newProgram.addCommand(okCommand);
    newProgram.addCommand(cancelCommand);
    newProgram.setCommandListener(this);
    
    newProgram.setString(name);
    whichProgram = n;
    display.setCurrent(newProgram);
  }

  public void resetRequested() {
    if (resetConfirmation == null) {
      resetConfirmation = new Form("Reset?");
      resetConfirmation.append("Are you sure you want to reset and exit, erasing all data?");
      if (okCommand == null) {
        okCommand = new Command("Ok", Command.OK, 1);
        cancelCommand = new Command("Cancel", Command.CANCEL, 1);
      }
      resetConfirmation.addCommand(okCommand);
      resetConfirmation.addCommand(cancelCommand);
      resetConfirmation.setCommandListener(this);
    }
    display.setCurrent(resetConfirmation);
  }

  public void displayMessage(String caption, String message) {
    messageScreen = new Form(caption);
    messageScreen.append(message);
    if (okCommand == null) {
      okCommand = new Command("Ok", Command.OK, 1);
      cancelCommand = new Command("Cancel", Command.CANCEL, 1);
    }
    messageScreen.addCommand(okCommand);
    messageScreen.setCommandListener(this);
    display.setCurrent(messageScreen);
  }

  public void outOfMemory() {
    displayMessage("Error", "Out of memory. You may be able to continue using the application, but to be safe you should exit now.");
  }

  public void commandAction(Command c, Displayable d) {
    if (d == newProgram) {
      if (c == okCommand) {
        screen.calc.progLabels[whichProgram] = newProgram.getString();
        screen.calc.command(CalcEngine.PROG_EDIT, whichProgram);
      }
      displayScreen();
    } else if (d == resetConfirmation) {
      if (c == okCommand) {
        if (dataStore != null)
          dataStore.destroy();
        notifyDestroyed();
      } else {
        displayScreen();
      }
    } else if (d == messageScreen) {
      displayScreen();
    }
  }
    
  public void pauseApp() {
  }

  public void destroyApp(boolean unconditional) {
    if (dataStore != null) {
      DataOutputStream out = dataStore.startWriting();
      if (out != null && screen != null) {
        try {
          saveSetup(out);
          screen.saveState(out);
        } catch (IOException ioe) {
        }
      }
      dataStore.close();
      dataStore = null;
    }
  }
    
  public void exitRequested() {
    destroyApp(false);
    notifyDestroyed();
  }

  public void saveSetup(DataOutputStream out) throws IOException {
    out.writeShort(4);
    out.writeBoolean(hasClearKey);
    out.writeByte(commandArrangement);
    out.writeBoolean(bgrDisplay);
    out.writeBoolean(doubleKeyEvents);
  }

  public void restoreSetup(DataInputStream in) throws IOException {
    short length = in.readShort();
    if (length >= 3) {
      hasClearKey = in.readBoolean();
      commandArrangement = in.readByte();
      bgrDisplay = in.readBoolean();
      length -= 3;
    }
    if (length >= 1) {
      doubleKeyEvents = in.readBoolean();
      length --;
    }
    in.skip(length);
  }

}
