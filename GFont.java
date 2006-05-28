package ral;

import java.io.*;
import javax.microedition.lcdui.*;

final class GFont
    extends GFontBase
{
  static final int SMALL  = 0;
  static final int MEDIUM = 1;
  static final int LARGE  = 2;
  static final int SYSTEM = 3;
  static final int XLARGE = 4;
  static final int BGR_ORDER = 8;
  static final int SIZE_MASK = 7;

  private byte [] char_bits;
  private int char_width;
  private int char_height;
  private String char_set;
  private int style;
  private Font systemFont;
  private boolean bgr;
  private int fg,bg;

  private Image char_cache;
  private Graphics char_cache_graphics;
  private boolean largeCache;
  private int cacheWidth,cacheHeight,cacheSize;
  private int bucketSize;
  private short [] cacheHash;
  private short [] cacheTime;
  private short time;
  private int refCount;

  private static GFont smallFont;
  private static GFont mediumFont;
  private static GFont largeFont;
  private static GFont xlargeFont;
  private static GFont systemFt;

  public static GFont getFont(int style, boolean largeCache, Canvas canvas)
  {
    GFont font = null;
    try {
      if ((style & SIZE_MASK) == SMALL) {
        if (smallFont == null) {
          smallFont = new GFont(style, largeCache, canvas);
        }
        font = smallFont;
      } else if ((style & SIZE_MASK) == MEDIUM) {
        if (mediumFont == null) {
          mediumFont = new GFont(style, largeCache, canvas);
        }
        font = mediumFont;
      } else if ((style & SIZE_MASK) == LARGE) {
        if (largeFont == null) {
          largeFont = new GFont(style, largeCache, canvas);
        }
        font = largeFont;
      } else if ((style & SIZE_MASK) == XLARGE) {
        if (xlargeFont == null) {
          xlargeFont = new GFont(style, largeCache, canvas);
        }
        font = xlargeFont;
      } else /*if ((style & SIZE_MASK) == SYSTEM)*/ {
        if (systemFt == null) {
          systemFt = new GFont(style, largeCache, canvas);
        }
        return systemFt;
      }
      if (largeCache && !font.hasLargeCache()) {
        font.setCacheSize(largeCache, canvas);
      }
      font.refCount++;
      return font;
    } catch (IOException e) {
      if (font != null)
        font.close();
      throw new IllegalStateException("Cannot initialize font");
    } catch (OutOfMemoryError e) {
      if (font != null)
        font.close();
      throw e;
    }
  }

  public void close() {
    refCount--;
    if (refCount <= 0) {
      char_bits = null;
      char_set = null;
      systemFont = null;
      char_cache = null;
      char_cache_graphics = null;
      cacheHash = null;
      cacheTime = null;
      if (this == smallFont) {
        smallFont = null;
      } else if (this == mediumFont) {
        mediumFont = null;
      } else if (this == largeFont) {
        largeFont = null;
      } else if (this == systemFt) {
        systemFt = null;
      }
    }
  }

  private GFont(int style, boolean largeCache, Canvas canvas)
    throws IOException
  {
    String char_bits_resource = null;
    byte [] bits = null;
    fg = 0xffffff;
    bg = 0x000000;
    refCount = 0;

    bgr = (style & BGR_ORDER) != 0;
    style &= SIZE_MASK;
    this.style = style;
    if (style == MEDIUM) {
      systemFont = null;
      char_width = medium_char_width;
      char_height = medium_char_height;
      char_bits_resource = medium_char_bits_resource;
      char_set = medium_char_set;
    } else if (style == LARGE) {
      systemFont = null;
      char_width = large_char_width;
      char_height = large_char_height;
      char_bits_resource = large_char_bits_resource;
      char_set = large_char_set;
    } else if (style == SMALL) {
      systemFont = null;
      char_width = small_char_width;
      char_height = small_char_height;
      char_bits_resource = small_char_bits_resource;
      char_set = small_char_set;
    } else if (style == XLARGE) {
      systemFont = null;
      char_width = xlarge_char_width;
      char_height = xlarge_char_height;
      char_bits_resource = xlarge_char_bits_resource;
      char_set = xlarge_char_set;
    } else if (style == SYSTEM) {
      systemFont = Font.getFont(Font.FACE_MONOSPACE,
                                Font.SIZE_MEDIUM,
                                Font.STYLE_PLAIN);
      char_width = systemFont.charWidth('8');
      char_height = systemFont.getHeight();
      char_set = null;
    }
    if (char_bits_resource != null) {
      InputStream in = getClass().getResourceAsStream(char_bits_resource);
      bits = new byte[char_set.length()*((char_height*char_width*2*2+7)/8)];
      for (int pos=0; pos<bits.length;)
        pos += in.read(bits, pos, bits.length-pos);
      in.close();
      setCacheSize(largeCache, canvas);
    }
    char_bits = bits;
  }

  public int getStyle() {
    return style;
  }

  public boolean hasLargeCache() {
    return largeCache;
  }

  private void setCacheSize(boolean largeCache, Canvas canvas) {
    cacheHash = null;
    cacheTime = null;
    char_cache = null;
    char_cache_graphics = null;
    this.largeCache = largeCache;
    cacheWidth = largeCache ? 16 : 10;
    if (cacheWidth*char_width > canvas.getWidth()) {
      cacheWidth = canvas.getWidth()/char_width;
    }
    cacheHeight = (largeCache ? 128 : 40) / cacheWidth;
    if (cacheHeight*char_height > canvas.getHeight()) {
      cacheHeight = canvas.getHeight()/char_height;
    }    
    cacheSize = cacheWidth*cacheHeight;
    bucketSize = 4;
    cacheHash = new short[cacheSize];
    cacheTime = new short[cacheSize];
    time = 0;
    char_cache = Image.createImage(char_width*cacheWidth,
                                   char_height*cacheHeight);
    char_cache_graphics = char_cache.getGraphics();
  }

  public void setColor(int fg, int bg) {
    this.fg = fg;
    this.bg = bg;
  }

  private int getIndex(char ch) {
      int i,i2,j;
      if (++time < 0) { // Ooops, wraparound after 32768 gets
        for (i=0; i<cacheSize; i++)
          cacheTime[i] = 0; // As an easy way out, all get same priority
        time = 1;
      }
      // Calculated hashes are assumed distinct and nonzero
      short hash = (short)(((ch*131+fg)*137+bg)%65537 /*129169*/);
      int bucketStart = (hash&0xffff)%cacheSize;

      // Search bucket for the correct hash
      for (i=i2=bucketStart; i<bucketStart+bucketSize; i++,i2++) {
        if (i2 >= cacheSize) i2 -= cacheSize;
        if (cacheHash[i2] == hash) {
          cacheTime[i2] = time;
          //System.out.println("Cache hit  for '"+ch+"': "+i2);
          return i2; // Cache hit
        }
      }

      // Cache miss. Find LRU for this bucket
      j = bucketStart;
      for (i=i2=bucketStart+1; i<bucketStart+bucketSize; i++,i2++) {
        if (i2 >= cacheSize) i2 -= cacheSize;
        if (cacheTime[i2] < cacheTime[j])
          j = i2;
      }
      //if (cacheTime[j] == 0) {
      //  System.out.println("Cache miss for '"+ch+"': "+j);
      //} else {
      //  System.out.println("Cache collision for '"+ch+"': "+j);
      //}
      cacheHash[j] = hash;
      cacheTime[j] = time;
      return -j-1; // Signaling cache miss
  }

  private void drawChar(Graphics g, int x, int y, char ch) {
    int index = getIndex(ch);
    boolean cacheMiss = false;
    if (index<0) {
      index = -index-1;
      cacheMiss = true;
    }
    int cacheX = char_width * (index % cacheWidth);
    int cacheY = char_height * (index / cacheWidth);
    if (cacheMiss) {
      int resIndex = char_set.indexOf(ch);
      if (resIndex < 0)
        resIndex = char_set.indexOf('?');
      Graphics g2 = char_cache_graphics;
      g2.setColor(0);
      g2.fillRect(cacheX,cacheY,char_width,char_height);
      for (int y2=0; y2<char_height; y2++) {
        for (int x2=0; x2<char_width; x2++) {
          int red,green,blue;
          int bitPos = (resIndex*char_width*char_height+y2*char_width+x2)*4-2;
          if (x2>0)
            red = ((char_bits[bitPos/8]>>(bitPos&7))&3)*85;
          else
            red = 0;
          bitPos += 2;
          green = ((char_bits[bitPos/8]>>(bitPos&7))&3)*70;
          bitPos += 2;
          blue = ((char_bits[bitPos/8]>>(bitPos&7))&3)*85;
          if (red != 0 || green != 0 || blue != 0) {
            if (bgr)
              g2.setColor(blue,green,red);
            else
              g2.setColor(red,green,blue);
            g2.fillRect(cacheX+x2,cacheY+y2,1,1);
          }
        }
      }
    }
    int clipX = g.getClipX();
    int clipY = g.getClipY();
    int clipWidth = g.getClipWidth();
    int clipHeight = g.getClipHeight();
    g.clipRect(x, y, char_width, char_height);
    g.drawImage(char_cache, x-cacheX, y-cacheY, Graphics.TOP|Graphics.LEFT);
    g.setClip(clipX, clipY, clipWidth, clipHeight);
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
      g.setColor(bg);
      g.fillRect(x,y,char_width*string.length(),char_height);
      g.setColor(fg);
      g.drawString(string,x,y,g.TOP|g.LEFT);
      return x + systemFont.stringWidth(string);
    }
    int length;
    length = string.length();
    for (int i=0; i<length; i++) {
      drawChar(g,x,y,string.charAt(i));
      x += char_width;
    }
    return x;
  }

  public int drawString(Graphics g, int x, int y,StringBuffer string,int start)
  {
    if (systemFont != null) {
      String s = string.toString();
      g.setColor(bg);
      g.fillRect(x,y,char_width*s.length()-start,char_height);
      g.setColor(fg);
      g.setFont(systemFont);
      g.drawSubstring(s,start,s.length()-start,x,y,g.TOP|g.LEFT);
      return x + systemFont.substringWidth(s,start,s.length()-start);
    }
    int length;
    length = string.length();
    for (int i=start; i<length; i++) {
      drawChar(g,x,y,string.charAt(i));
      x += char_width;
    }
    return x;
  }
}
