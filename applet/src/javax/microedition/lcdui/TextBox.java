package javax.microedition.lcdui;

public class TextBox extends Displayable
{
  String text;
  
  public TextBox(String title, String t, int maxSize, int constraints) {
    text = t;
    // ...
  }

  public void setString(String t) {
    text = t;
  }

  public String getString() {
    return text;
  }

  public void paint(java.awt.Graphics g) {
    // ...
  }
}
