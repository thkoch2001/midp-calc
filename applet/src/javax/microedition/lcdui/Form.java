package javax.microedition.lcdui;

public class Form extends Displayable
{
  String title;
  String text;
  Font headFont;
  Font textFont;
  
  public Form(String t) {
    title = t;
    headFont = Font.getFont(
      Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_LARGE);
    textFont = Font.getFont(
      Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_MEDIUM);
  }

  public int append(String str) {
    text = str;
    return 0;
  }

  private void drawWrapped(java.awt.Graphics g, int x, int y, int w, String text) {
    int start = 0;
    int end;
    while (start < text.length()) {
      end = text.length();
      while (end>start && textFont.substringWidth(text,start,end-start)>w)
        end = text.lastIndexOf(' ',end-1);
      if (end <= start) {
        // Must chop word
        end = text.indexOf(' ',start);
        if (end <= start)
          end = text.length();
        while (end>start && textFont.substringWidth(text,start,end-start)>w)
          end--;
      }
      g.drawString(text.substring(start,end),x,y);
      y += textFont.getHeight();
      start = end;
      while (start < text.length() && text.charAt(start)==' ')
        start++;
    } 
  }

  public void paint(java.awt.Graphics g) {
    g.setColor(java.awt.Color.WHITE);
    g.fillRect(0,0,getWidth(),getHeight());
    g.setColor(java.awt.Color.BLACK);
    g.setFont(headFont.font);
    g.drawString(title,3,headFont.getBaselinePosition());
    g.setFont(textFont.font);
    if (text != null)
      drawWrapped(g,3,textFont.getHeight()+3+headFont.getBaselinePosition(),
                  getWidth()-6,text);
  }
}
