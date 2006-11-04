package midpcalc;

import javax.microedition.lcdui.*;

public class SetupCanvas
    extends MyCanvas
    implements CommandListener
{
  private final Calc midlet;

  private Command yes;
  private Command no;
  private Command ok;
  private Command left;
  private Command right;

  // The main queries consists of a heading and a text
  private String setupHeading;
  private String setupText;

  // I try to make the query texts as short as possible to make them fit on
  // really small screens such as Nokia 3510i
  private String commandQueryHeading = "Setup: keys";
  private String commandQueryText =
    "Press \"yes\" if you see \"no\" on the left key and \"yes\" "+
    "on the right key below";
  
  private String clearQueryHeading = "Setup: clear key";
  private String clearQueryText =
    "If you have a \"clear\" key, press it now, otherwise use #";

  private String bgrQueryHeading = "Setup: font";
  private String bgrQueryText =
    "What looks best, left or right numbers?";

  // Alert texts inform the user between the main queries
  private String alertHeading;
  private String alertText;

  // Setup states to keep track of progress
  private static final int COMMAND_QUERY = 0;
  private static final int CLEAR_QUERY = 1;
  private static final int BGR_QUERY = 2;
  private static final int QUERY_FINISHED = 3;
  private int query;

  private Font menuFont;
  private Font boldMenuFont;
  private boolean unknownKeyPressed = false;

  public static final int [] commandArrangement = {
    // Pairs of command types that lead to different command arrangements
    Command.OK,   Command.SCREEN,
    Command.ITEM, Command.BACK,
    Command.ITEM, Command.SCREEN,
    Command.OK,   Command.ITEM,
    Command.ITEM, Command.CANCEL,
    Command.ITEM, Command.STOP,
    Command.OK,   Command.BACK,
    Command.OK,   Command.CANCEL,
    Command.OK,   Command.STOP,
  };
  private int arrng = 0;

  public SetupCanvas(Calc m) {
    midlet = m;

    ok  = new Command("ok",  Command.OK, 1);
    setCommandListener(this);

    menuFont = Font.getFont(
      Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_MEDIUM);
    boldMenuFont = Font.getFont(
      Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_MEDIUM);

    query = COMMAND_QUERY;
    setupHeading = commandQueryHeading;
    setupText = commandQueryText;

    alertText =
      "To adapt the user interface, please complete the following setup.";
    alertHeading = "Setup";
    setupKeys();

    // Preset setup values for known devices
    String name = null;
    try {
      name = System.getProperty("microedition.platform");
    } catch (Exception e) {
    }
    if (name != null) {
      name = name.toLowerCase();
      if (name.startsWith("nokia")) {
        // Just start with the most likely command arrangement for Nokia
        arrng = 1;
      } else if (name.indexOf("t610")>0 || name.indexOf("z600")>0 ||
                 name.indexOf("z1010")>0 || name.indexOf("k700i")>0 ||
                 name.indexOf("x120")>0) {
        midlet.hasClearKey = true;
        midlet.commandArrangement = 0;
        midlet.bgrDisplay = false;
        query = QUERY_FINISHED;
        finish();
      } else if (name.indexOf("t630")>0) {
        midlet.hasClearKey = true;
        midlet.commandArrangement = 0;
        midlet.bgrDisplay = true;
        query = QUERY_FINISHED;
        finish();
      }
    }
    if (!automaticCommands() && query == COMMAND_QUERY) {
      // We must draw and handle commands ourself (Nokia Fullcanvas)
      query = CLEAR_QUERY;
      setupHeading = clearQueryHeading;
      setupText = clearQueryText;
    }
  }

  public boolean isFinished() {
    return query == QUERY_FINISHED;
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
    String text,heading;
    int height = getHeight();
    if (alertText != null) {
      g.setColor(216,156,156);
      text = alertText;
      heading = alertHeading;
    } else {
      g.setColor(156,216,216);
      text = setupText;
      heading = setupHeading;
    }
    g.fillRect(0,0,getWidth(),height);
    g.setColor(0);
    g.setFont(boldMenuFont);
    g.drawString(heading,2,0,g.TOP|g.LEFT);
    g.setFont(menuFont);
    drawWrapped(g,2,boldMenuFont.getHeight()+3,getWidth()-3,text);
    if (!automaticCommands()) {
      paintCommands(g,boldMenuFont,null,null);
      height -= boldMenuFont.getHeight()+1;
    }
    if (alertText == null && query == BGR_QUERY) {
      GFont font = GFont.getFont(GFont.MEDIUM, false, this);
      font.drawString(g,2,height-font.getHeight()-2," 567 ");
      font.close();
      font = GFont.getFont(GFont.MEDIUM|GFont.BGR_ORDER, false, this);
      font.drawString(g,getWidth()-font.charWidth()*5-2,
                      height-font.getHeight()-2," 567 ");
      font.close();
    }
  }

  private void setupKeys() {
    if (yes   != null) removeCommand(yes);
    if (no    != null) removeCommand(no);
    if (left  != null) removeCommand(left);
    if (right != null) removeCommand(right);
    removeCommand(ok);
    if (alertText != null) {
      addCommand(ok);
    } else if (query == COMMAND_QUERY) {
      if ((arrng & 0x80) == 0) {
        no  = new Command("no",  commandArrangement[2*(arrng & 0x7f)], 1);
        yes = new Command("yes", commandArrangement[2*(arrng & 0x7f)+1], 1);
        addCommand(no);
        addCommand(yes);
      } else {
        yes = new Command("yes", commandArrangement[2*(arrng & 0x7f)], 1);
        no  = new Command("no",  commandArrangement[2*(arrng & 0x7f)+1], 1);
        addCommand(yes);
        addCommand(no);
      }
    } else if (query == CLEAR_QUERY) {
      no  = new Command(" ", commandArrangement[2*(arrng & 0x7f)], 1);
      yes = new Command(" ", commandArrangement[2*(arrng & 0x7f)+1], 1);
      addCommand(no);
      addCommand(yes);
    } else if (query == BGR_QUERY) {
      if ((arrng & 0x80) == 0) {
        left = new Command("left", commandArrangement[2*(arrng & 0x7f)], 1);
        right= new Command("right",commandArrangement[2*(arrng & 0x7f)+1], 1);
        addCommand(left);
        addCommand(right);
      } else {
        right= new Command("right",commandArrangement[2*(arrng & 0x7f)], 1);
        left = new Command("left", commandArrangement[2*(arrng & 0x7f)+1], 1);
        addCommand(right);
        addCommand(left);
      }
    }
  }
  
  private void doRepaint() {
    setupKeys();
    repaint();
  }

  private void clearKeyPressed(boolean hasClearKey) {
    midlet.hasClearKey = hasClearKey;
    if (midlet.display.isColor()) {
      alertText = "Thank you - next setup item";
      alertHeading = "Setup";
      query = BGR_QUERY;
      setupHeading = bgrQueryHeading;
      setupText = bgrQueryText;
    } else {
      alertText = "Thank you - setup finished";
      alertHeading = "Setup";
      query = QUERY_FINISHED;
    }
    doRepaint();
  }

  private void clearKeyInUse() {
    alertText = "Sorry, that key is used for something else";
    alertHeading = setupHeading;
    doRepaint();
  }

  private void nextCommandArrangement() {
    if ((arrng & 0x80) == 0)
      arrng |= 0x80;
    else
      arrng = ((arrng & 0x7f)+1)%(commandArrangement.length/2);
    if (arrng == 0)
      alertText = "All arrangements tried, trying first again";
    else
      alertText = "Okay, trying next key arrangement ["+
        ((arrng & 0x7f)*2+((arrng & 0x80)>>7))+"]";
    alertHeading = setupHeading;
    doRepaint();
  }

  private void finish() {
    midlet.displayScreen();
  }

  protected void keyPressed(int key) {
    if (alertText != null) {
      if (!automaticCommands() && key == KEY_SOFTKEY1)
        commandAction(ok, this);
      return;
    }
    if (query == QUERY_FINISHED) {
      finish();
      return;
    } else if (query == COMMAND_QUERY) {
      // Wait and see, this could be a double keyPressed/commandAction event
      unknownKeyPressed = true;
      return;
    } else if (query == BGR_QUERY) {
      if (!automaticCommands()) {
        if (key == KEY_SOFTKEY1)
          commandAction(left, this);
        else if (key == KEY_SOFTKEY2)
          commandAction(right, this);
      }
      return;
    }
    // So, it's a CLEAR_QUERY
    switch (key) {
      case '0': case '1': case '2': case '3': case '4':
      case '5': case '6': case '7': case '8': case '9':
      case '*': case '.': case ',': case 65452:
      case '-': case 'e': case 'E':
      case '\n': case '\r': case '+':
      case KEY_SOFTKEY1:
      case KEY_SOFTKEY2:
        clearKeyInUse();
        break;
      case KEY_SEND:
      case KEY_END:
      case '\b': // BackSpace
        clearKeyPressed(true);
        break;
      case '#':
        clearKeyPressed(false);
        break;
      default:
        switch (getGameAction(key)) {
          case UP: case DOWN: case LEFT: case RIGHT: case FIRE:
            clearKeyInUse();
            break;
          case GAME_A: case GAME_B: case GAME_C: case GAME_D:
            clearKeyPressed(true);
            break;
          default:
            // Some keys are not mapped to game keys and must be
            // handled directly in this dirty fashion...
            switch (key) {
              case KEY_UP_ARROW:    // UP
              case KEY_DOWN_ARROW:  // DOWN
              case KEY_LEFT_ARROW:  // LEFT
              case KEY_RIGHT_ARROW: // RIGHT
              case KEY_SOFTKEY3:    // PUSH
                clearKeyInUse();
                break;
              case -8:  // SonyEricsson "c"
              case -23: // Motorola "menu"
                clearKeyPressed(true);
                break;
              default:
                if (midlet.doubleKeyEvents)
                  // We don't yet know if we can treat this as "clear"
                  unknownKeyPressed = true;
                else
                  clearKeyPressed(true);
                break;
            }
            break;
        }
        break;
    }
  }

  protected void keyReleased(int key) {
    if (unknownKeyPressed) {
      unknownKeyPressed = false;
      if (query == COMMAND_QUERY) {
        if (alertText == null)
          nextCommandArrangement();
      } else if (query == CLEAR_QUERY) {
        // Unknown key has been pressed and released, treat it as "clear"
        clearKeyPressed(true);
      }
    }
  }

  public void commandAction(Command c, Displayable d)
  {
    if (unknownKeyPressed) {
      // If a key has beed pressed but not released when we get the
      // commandAction, the phone has the "double key events" bug where a
      // commandAction is enclosed in an unknown keyPressed/keyReleased
      // event pair
      unknownKeyPressed = false;
      midlet.doubleKeyEvents = true;
    }

    if (query == QUERY_FINISHED) {
      finish();
      return;
    }
    if (alertText != null) {
      alertText = null;
      doRepaint();
      return;
    }
    if (query == CLEAR_QUERY) {
      clearKeyInUse();
      return;
    }
    if (c == yes) {
      alertText = "Thank you - next setup item";
      alertHeading = "Setup";
      midlet.commandArrangement = (byte)arrng;
      query = CLEAR_QUERY;
      setupHeading = clearQueryHeading;
      setupText = clearQueryText;
    } else if (c == no) {
      nextCommandArrangement();
    } else if (c == left || c == right) {
      alertText = "Thank you - setup finished";
      alertHeading = "Setup";
      query = QUERY_FINISHED;
      midlet.bgrDisplay = c == right;
    }
    doRepaint();
  }

}
