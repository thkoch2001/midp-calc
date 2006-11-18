package midpcalc;

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
    static final int XXLARGE = 5;
    static final int XXXLARGE = 6;
    static final int BGR_ORDER = 8;
    static final int SIZE_MASK = 7;

    private byte [] char_bits;
    private int char_width;
    private int char_height;
    private String char_set;
    private int style;
    private Font systemFont;
    private boolean bgr;
    private boolean sizeX2;
    private boolean monospaced;
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
    private static GFont xxlargeFont;
    private static GFont xxxlargeFont;
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
            } else if ((style & SIZE_MASK) == XXLARGE) {
                if (xxlargeFont == null) {
                    xxlargeFont = new GFont(style, largeCache, canvas);
                }
                font = xxlargeFont;
            } else if ((style & SIZE_MASK) == XXXLARGE) {
                if (xxxlargeFont == null) {
                    xxxlargeFont = new GFont(style, largeCache, canvas);
                }
                font = xxxlargeFont;
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
        sizeX2 = false;
        style &= SIZE_MASK;
        this.style = style;
        if (style == SMALL) {
            systemFont = null;
            char_width = small_char_width;
            char_height = small_char_height;
            char_bits_resource = small_char_bits_resource;
            char_set = small_char_set;
        } else if (style == MEDIUM) {
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
        } else if (style == XLARGE) {
            systemFont = null;
            char_width = xlarge_char_width;
            char_height = xlarge_char_height;
            char_bits_resource = xlarge_char_bits_resource;
            char_set = xlarge_char_set;
        } else if (style == XXLARGE) {
            systemFont = null;
            char_width = large_char_width;
            char_height = large_char_height;
            char_bits_resource = large_char_bits_resource;
            char_set = large_char_set;
            sizeX2 = true;
        } else if (style == XXXLARGE) {
            systemFont = null;
            char_width = xlarge_char_width;
            char_height = xlarge_char_height;
            char_bits_resource = xlarge_char_bits_resource;
            char_set = xlarge_char_set;
            sizeX2 = true;
        } else if (style == SYSTEM) {
            systemFont = Font.getFont(Font.FACE_PROPORTIONAL,
                                      Font.SIZE_MEDIUM,
                                      Font.STYLE_PLAIN);
            char_width = Math.max(systemFont.charWidth('8'),
                                  systemFont.charWidth('6'));
            char_height = systemFont.getHeight();
            char_set = null;
        }
        if (char_bits_resource != null) {
            InputStream in= getClass().getResourceAsStream(char_bits_resource);
            bits = new byte[char_set.length()*
                            ((char_height*char_width*2*2+7)/8)];
            for (int pos=0; pos<bits.length;)
                pos += in.read(bits, pos, bits.length-pos);
            in.close();
            if (sizeX2) {
                char_width *= 2;
                char_height *= 2;
            }
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

    public void setMonospaced(boolean monospaced) {
        this.monospaced = monospaced;
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

    int clipX1, clipX2, clipY1, clipY2;

    private void getClip(Graphics g) {
        clipX1 = g.getClipX();
        clipY1 = g.getClipY();
        clipX2 = clipX1 + g.getClipWidth();
        clipY2 = clipY1 + g.getClipHeight();
    }

    private void renderChar(int cacheX, int cacheY, int resIndex) {
        Graphics g = char_cache_graphics;
        g.setColor(0);
        g.fillRect(cacheX,cacheY,char_width,char_height);
        if (sizeX2) {
            for (int y2=0; y2<char_height; y2+=2) {
                int bitPos1,bitPos2;
                bitPos1 = (resIndex*char_height+y2)*char_width-2;
                bitPos2 = (resIndex*char_height+y2+2)*char_width-2;
                for (int x2=0; x2<char_width; x2++) {
                    int gray1,gray2,gray3,gray4,gray5,gray6;
                    if (x2>0) {
                        gray1 = ((char_bits[bitPos1/8]>>(bitPos1&7))&3)*85;
                        if (y2+2<char_height)
                            gray2 = ((char_bits[bitPos2/8]>>(bitPos2&7))&3)*85;
                        else
                            gray2 = 0;
                    } else {
                        gray1 = gray2 = 0;
                    }
                    bitPos1 += 2;
                    bitPos2 += 2;
                    gray3 = ((char_bits[bitPos1/8]>>(bitPos1&7))&3)*85;
                    if (y2+2<char_height)
                        gray4 = ((char_bits[bitPos2/8]>>(bitPos2&7))&3)*85;
                    else
                        gray4 = 0;
                    if (x2+1<char_width) {
                        bitPos1 += 2;
                        bitPos2 += 2;
                        gray5 = ((char_bits[bitPos1/8]>>(bitPos1&7))&3)*85;
                        if (y2+2<char_height)
                            gray6 = ((char_bits[bitPos2/8]>>(bitPos2&7))&3)*85;
                        else
                            gray6 = 0;
                    } else {
                        gray5 = gray6 = 0;
                    }
                    if (gray1+gray2+gray3+gray4+gray5+gray6 != 0) {
                        int red,green,blue,tmp;
                        tmp = gray3;
                        gray3 = gray1+tmp*3;
                        gray1 = gray1*3+tmp;
                        gray5 = tmp*3+gray5;
                        tmp = gray4;
                        gray4 = gray2+tmp*3;
                        gray2 = gray2*3+tmp;
                        gray6 = tmp*3+gray6;

                        red   = (gray1*3 + gray2)/8 - 128;
                        green = (gray3*3 + gray4)/8 - 128;
                        blue  = (gray5*3 + gray6)/8 - 128;
                        if (red  <0) red  =0; if (red  >255) red  =255;
                        if (green<0) green=0; if (green>255) green=255;
                        if (blue <0) blue =0; if (blue >255) blue =255;
                        if (bgr)
                            g.setColor(blue,green,red);
                        else
                            g.setColor(red,green,blue);
                        g.fillRect(cacheX+x2,cacheY+y2,1,1);

                        red   = (gray1 + gray2*3)/8 - 128;
                        green = (gray3 + gray4*3)/8 - 128;
                        blue  = (gray5 + gray6*3)/8 - 128;
                        if (red  <0) red  =0; if (red  >255) red  =255;
                        if (green<0) green=0; if (green>255) green=255;
                        if (blue <0) blue =0; if (blue >255) blue =255;
                        if (bgr)
                            g.setColor(blue,green,red);
                        else
                            g.setColor(red,green,blue);
                        g.fillRect(cacheX+x2,cacheY+y2+1,1,1);
                    }
                    bitPos1 -= 2;
                    bitPos2 -= 2;
                }
            }
        } else {
            for (int y2=0; y2<char_height; y2++) {
                for (int x2=0; x2<char_width; x2++) {
                    int red,green,blue,bitPos;
                    bitPos = ((resIndex*char_height+y2)*char_width+x2)*4-2;
                    if (x2>0)
                        red = ((char_bits[bitPos/8]>>(bitPos&7))&3)*85;
                    else
                        red = 0;
                    bitPos += 2;
                    green = ((char_bits[bitPos/8]>>(bitPos&7))&3)*70;
                    bitPos += 2;
                    blue = ((char_bits[bitPos/8]>>(bitPos&7))&3)*85;
                    if (red+green+blue != 0) {
                        if (bgr)
                            g.setColor(blue,green,red);
                        else
                            g.setColor(red,green,blue);
                        g.fillRect(cacheX+x2,cacheY+y2,1,1);
                    }
                }
            }
        }
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
            renderChar(cacheX, cacheY, resIndex);
        }
        int cx1 = clipX1 > x ? clipX1 : x;
        int cx2 = clipX2 < x+char_width ? clipX2 : x+char_width;
        int cy1 = clipY1 > y ? clipY1 : y;
        int cy2 = clipY2 < y+char_height ? clipY2 : y+char_height;
        if (cx1<cx2 && cy1<cy2) {
            g.setClip(cx1, cy1, cx2-cx1, cy2-cy1);
            g.drawImage(char_cache, x-cacheX, y-cacheY,
                        Graphics.TOP|Graphics.LEFT);
        }
    }

    public int getHeight() {
        return char_height;
    }
  
    public int charWidth() {
        return char_width;
    }

    public int stringWidth(String string) {
        if (systemFont != null && !monospaced)
            return systemFont.stringWidth(string);
        return char_width*string.length();
    }

    public int drawString(Graphics g, int x, int y, String string)
    {
        int length = string.length();
        if (systemFont != null) {
            g.setFont(systemFont);
            g.setColor(bg);
            g.fillRect(x,y,char_width*length,char_height);
            g.setColor(fg);
            if (monospaced) {
                x += char_width/2;
                for (int i=0; i<length; i++) {
                    g.drawChar(string.charAt(i), x, y,
                               Graphics.TOP|Graphics.HCENTER);
                    x += char_width;
                }
            } else {
                g.drawString(string,x,y,Graphics.TOP|Graphics.LEFT);
            }
            return x + systemFont.stringWidth(string);
        }
        getClip(g);
        for (int i=0; i<length; i++) {
            drawChar(g,x,y,string.charAt(i));
            x += char_width;
        }
        g.setClip(clipX1, clipY1, clipX2-clipX1, clipY2-clipY1);
        return x;
    }

    public int drawString(Graphics g,int x,int y,StringBuffer string,int start)
    {
        if (systemFont != null) {
            String s = string.toString();
            g.setColor(bg);
            g.fillRect(x,y,char_width*s.length()-start,char_height);
            g.setColor(fg);
            g.setFont(systemFont);
            g.drawSubstring(s, start, s.length()-start, x, y,
                            Graphics.TOP|Graphics.LEFT);
            return x + systemFont.substringWidth(s,start,s.length()-start);
        }
        int length;
        length = string.length();
        getClip(g);
        for (int i=start; i<length; i++) {
            drawChar(g,x,y,string.charAt(i));
            x += char_width;
        }
        g.setClip(clipX1, clipY1, clipX2-clipX1, clipY2-clipY1);
        return x;
    }
}
