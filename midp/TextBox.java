package javax.microedition.lcdui;

public class TextBox extends Displayable
{
  String text;
  
  TextBox(String title, String t, int maxSize, int constraints) {
    text = t;
    // ...
  }

  void setString(String t) {
    text = t;
  }

  String getString() {
    return text;
  }

  public void paint(java.awt.Graphics g) {
    // ...
  }
}
