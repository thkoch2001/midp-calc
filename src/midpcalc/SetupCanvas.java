package ral;

import javax.microedition.lcdui.*;

public class SetupCanvas
    extends Canvas
    implements CommandListener
{
  private Font menuFont;
  private Font boldMenuFont;

  private Command yes;
  private Command no;
  private Command ok;

  private final Calc midlet;

  private String setupHeading;
  private String setupText;
  private String commandQueryHeading = "Setup: keys";
  private String commandQueryText =
    "Press \"yes\" if you see \"no\" and \"yes\" mapped to the left and right keys below";
  private String clearQueryHeading = "Setup: clear key";
  private String clearQueryText =
    "If you have a \"clear\" key, press it now, otherwise use #";
  private String alertString;
  private String alertHeading;

  private boolean clearQuery = true;
  private boolean finished = false;

  public static final int [] commandArrangement = {
    Command.OK, Command.SCREEN,
    Command.ITEM, Command.BACK,
    Command.ITEM, Command.SCREEN,
    Command.OK, Command.ITEM,
    Command.ITEM, Command.CANCEL,
    Command.ITEM, Command.STOP,
    Command.OK, Command.BACK,
    Command.OK, Command.CANCEL,
    Command.OK, Command.STOP,
  };
  private int arrangement = 0;

  public SetupCanvas(Calc m) {
    midlet = m;

    ok  = new Command("ok",  Command.OK, 1);
    setCommandListener(this);

    menuFont = Font.getFont(
      Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_MEDIUM);
    boldMenuFont = Font.getFont(
      Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_MEDIUM);

    clearQuery = false;
    setupHeading = commandQueryHeading;
    setupText = commandQueryText;

    alertString =
      "To adapt the user interface, please complete the following setup.";
    alertHeading = "Setup";

    String name = System.getProperty("microedition.platform");
    if (name != null && name.startsWith("Nokia"))
      arrangement = 1;
  }

  private void drawWrapped(Graphics g, int x, int y, int w, String text) {
    int start = 0;
    int end;
    Font f = g.getFont();
    while (start < text.length()) {
      end = text.length();
      while (end>start && f.substringWidth(text,start,end-start)>w)
        end = text.lastIndexOf(' ',end-1);
      if (end <= start) {
        // Must chop word
        end = text.indexOf(' ',start);
        if (end <= start)
          end = text.length();
        while (end>start && f.substringWidth(text,start,end-start)>w)
          end--;
      }
      g.drawSubstring(text,start,end-start,x,y,g.TOP|g.LEFT);
      y += f.getHeight();
      start = end;
      while (start < text.length() && text.charAt(start)==' ')
        start++;
    } 
  }

  public void paint(Graphics g) {
    if (yes!=null) removeCommand(yes);
    if (no !=null) removeCommand(no);
    removeCommand(ok);
    if (alertString != null) {
      addCommand(ok);
    } else if (!clearQuery) {
      no  = new Command("no",  commandArrangement[2*arrangement], 1);
      yes = new Command("yes", commandArrangement[2*arrangement+1], 1);
      addCommand(no);
      addCommand(yes);
    }
    String text,heading;
    if (alertString != null) {
      g.setColor(216,156,156);
      text = alertString;
      heading = alertHeading;
    } else {
      g.setColor(156,216,216);
      text = setupText;
      heading = setupHeading;
    }
    g.fillRect(0,0,getWidth(),getHeight());
    g.setColor(0);
    g.setFont(boldMenuFont);
    g.drawString(heading,2,0,g.TOP|g.LEFT);
    g.setFont(menuFont);
    drawWrapped(g,2,boldMenuFont.getHeight()+3,getWidth()-3,text);
  }

  private void clearKeyPressed(boolean hasClearKey) {
    alertString = "Thank you - setup finished";
    alertHeading = "Setup";
    midlet.hasClearKey = hasClearKey;
    finished = true;
    repaint();
  }

  private void clearKeyInUse() {
    alertString = "Sorry, that key is used for something else";
    alertHeading = setupHeading;
    repaint();
  }

  private void nextCommandArrangement() {
    arrangement = (arrangement+1)%(commandArrangement.length/2);
    if (arrangement == 0)
      alertString = "All arrangements tried, trying first again";
    else
      alertString = "Okay, trying next key arrangement";
    alertHeading = setupHeading;
    repaint();
  }

  private void finish() {
    midlet.saveSetup();
    midlet.displayScreen();
  }

  protected void keyPressed(int key) {
    if (finished) {
      finish();
      return;
    }
    //if (alertString != null) {
    //  alertString = null;
    //  repaint();
    //  return;
    //}
    if (!clearQuery) {
      nextCommandArrangement();
      return;
    }
    switch (key) {
      case '0': case '1': case '2': case '3': case '4':
      case '5': case '6': case '7': case '8': case '9':
      case '*':
        clearKeyInUse();
        break;
      case '#':
        clearKeyPressed(false);
        break;
      default:
        switch (getGameAction(key)) {
          case UP:
          case DOWN:
          case LEFT:
          case RIGHT:
          case FIRE:
            clearKeyInUse();
            break;
          case GAME_A: case GAME_B: case GAME_C: case GAME_D:
            clearKeyPressed(true);
            break;
          default:
            // Some keys are not mapped to game keys and must be
            // handled directly in this dirty fashion...
            switch (key) {
              case -1: // UP
              case -2: // DOWN
              case -3: // LEFT
              case -4: // RIGHT
              case -5: // PUSH
                clearKeyInUse();
              default:
                clearKeyPressed(true);
                break;
            }
            break;
        }
        break;
    }
  }

  public void commandAction(Command c, Displayable d)
  {
    if (finished) {
      finish();
      return;
    }
    if (alertString != null) {
      alertString = null;
      repaint();
      return;
    }
    if (clearQuery) {
      clearKeyInUse();
      return;
    }
    if (c == yes) {
      alertString = "Thank you - next setup item";
      alertHeading = "Setup";
      midlet.commandArrangement = (byte)arrangement;
      clearQuery = true;
      setupHeading = clearQueryHeading;
      setupText = clearQueryText;
    } else {
      nextCommandArrangement();
    }
    repaint();
  }

}
