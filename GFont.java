package ral;

import javax.microedition.lcdui.*;

final class GFont
    extends GFontBase
{
  static final int SMALL  = 0;
  static final int MEDIUM = 1;
  static final int LARGE  = 2;
  static final int SYSTEM = 3;

  private final String char_bits;
  private final int char_width;
  private final int char_height;
  private final String char_set;
  private Image [] char_cache;
  private final int style;
  private final Font systemFont;
  private final byte [] char_index;

  public GFont(int style)
  {
    this.style = style;
    if (style == MEDIUM) {
      systemFont = null;
      char_width = medium_char_width;
      char_height = medium_char_height;
      char_bits = medium_char_bits;
      char_set = medium_char_set;
    } else if (style == LARGE) {
      systemFont = null;
      char_width = large_char_width;
      char_height = large_char_height;
      char_bits = large_char_bits;
      char_set = large_char_set;
    } else if (style == SMALL) {
      systemFont = null;
      char_width = small_char_width;
      char_height = small_char_height;
      char_bits = small_char_bits;
      char_set = small_char_set;
    } else { // SYSTEM font
      systemFont = Font.getFont(Font.FACE_MONOSPACE,
                                Font.SIZE_MEDIUM,
                                Font.STYLE_PLAIN);
      char_width = systemFont.charWidth('0');
      char_height = systemFont.getHeight();
      char_bits = null;
      char_set = null;
    }
    char_cache = new Image[32*3];
    char_index = new byte[96];
    for (char c=32; c<128; c++)
      char_index[c-32] = (byte)char_set.indexOf(c);
  }

  public int getStyle() {
    return style;
  }

  private int charToIndex(char c) {
    if (c>=32 && c<128)
      return (int)char_index[c-32];
    return -1;
  }

  private void drawChar(Graphics g, int x, int y, int index) {
    Image image = char_cache[index];
    if (image == null) {
      image = Image.createImage(char_width,char_height);
      Graphics g2 = image.getGraphics();
      g2.setColor(0);
      g2.fillRect(0,0,char_width,char_height);
      for (int y2=0; y2<char_height; y2++)
        for (int x2=0; x2<char_width; x2++) {
          int red,green,blue;
          int bitPos = (index*char_width*char_height+y2*char_width+x2)*4-2;
          if (x2>0)
            red = ((char_bits.charAt(bitPos/8)>>(bitPos&7))&3)*85;
          else
            red = 0;
          bitPos += 2;
          green = ((char_bits.charAt(bitPos/8)>>(bitPos&7))&3)*70;
          bitPos += 2;
          blue = ((char_bits.charAt(bitPos/8)>>(bitPos&7))&3)*85;
          if (red != 0 || green != 0 || blue != 0) {
            g2.setColor(red,green,blue);
            g2.fillRect(x2,y2,1,1);
          }
        }
      char_cache[index] = image;
    }
    g.drawImage(image,x,y,Graphics.TOP|Graphics.LEFT);
  }

  public int getHeight() {
    return char_height;
  }
  
  public int charWidth() {
    return char_width;
  }

  public int stringWidth(String string) {
    if (systemFont != null)
      return systemFont.stringWidth(string);
    return char_width*string.length();
  }

  public int drawString(Graphics g, int x, int y, String string)
  {
    if (systemFont != null) {
      g.setFont(systemFont);
      g.setColor(0);
      g.fillRect(x,y,char_width*string.length(),char_height);
      g.setColor(0xffffff);
      g.drawString(string,x,y,g.TOP|g.LEFT);
      return x + systemFont.stringWidth(string);
    }
    int length;
    length = string.length();
    for (int i=0; i<length; i++) {
      int index = charToIndex(string.charAt(i));
      if (index>=0) {
        drawChar(g,x,y,index);
        x += char_width;
      }
    }
    return x;
  }

  public int drawString(Graphics g, int x, int y,StringBuffer string,int start)
  {
    if (systemFont != null) {
      String s = string.toString();
      g.setColor(0);
      g.fillRect(x,y,char_width*s.length()-start,char_height);
      g.setColor(0xffffff);
      g.setFont(systemFont);
      g.drawSubstring(s,start,s.length()-start,x,y,g.TOP|g.LEFT);
      return x + systemFont.substringWidth(s,start,s.length()-start);
    }
    int length;
    length = string.length();
    for (int i=start; i<length; i++) {
      int index = charToIndex(string.charAt(i));
      if (index>=0) {
        drawChar(g,x,y,index);
        x += char_width;
      }
    }
    return x;
  }
}
