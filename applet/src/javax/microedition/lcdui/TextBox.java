package javax.microedition.lcdui;

public class TextBox extends Displayable
{
    String title;
    String text;
    int maxSize;
    Font headFont;
    Font textFont;
  
    public TextBox(String ti, String t, int m, int constraints) {
        title = ti;
        text = t;
        maxSize = m;
        headFont = Font.getFont(
            Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_LARGE);
        textFont = Font.getFont(
            Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_LARGE);
    }

    public void setString(String t) {
        text = t;
    }

    public String getString() {
        return text;
    }

    protected void keyPressed(int keyCode) {
        if (text.length()<maxSize &&
            ((keyCode >= 32 && keyCode <= 127) ||
             (keyCode >= 160 && keyCode <= 255))) {
            text += (char)keyCode;
            repaint();
        } else if (text.length()>0 && keyCode == '\b') {
            text = text.substring(0,text.length()-1);
            repaint();
        }
    }

    public void paint(java.awt.Graphics g) {
        g.setColor(java.awt.Color.WHITE);
        g.fillRect(0,0,getWidth(),getHeight());
        g.setColor(java.awt.Color.BLACK);
        g.setFont(headFont.font);
        g.drawString(title,3,textFont.getBaselinePosition());
        g.setFont(textFont.font);
        if (text == null)
            text = "";
        text += '_';
        g.drawString(text,3,
                     headFont.getHeight()+3+textFont.getBaselinePosition());
        text = text.substring(0,text.length()-1);
    }
}
