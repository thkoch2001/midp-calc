package midpcalc;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

final class SysFont extends UniFont {

    private Font systemFont;
    private Font systemEmFont; // Emphasized, i.e. bold

    public void close() {
        systemFont = null;
        systemEmFont = null;
        if (smallerFont != null) {
            UniFont f = smallerFont;
            smallerFont = null;
            f.close();
        }
    }

    SysFont(int style) {
        super(style);
        systemFont = null;

        int systemFontSize;
        switch (style & SIZE_MASK) {
            case SMALL: default:
                systemFontSize = Font.SIZE_SMALL;
                break;
            case MEDIUM: case SYSTEM:
                systemFontSize = Font.SIZE_MEDIUM;
                break;
            case LARGE: case XLARGE: case XXLARGE: case XXXLARGE:
                systemFontSize = Font.SIZE_LARGE;
                break;
        }
        systemFont = Font.getFont(Font.FACE_PROPORTIONAL,
                Font.STYLE_PLAIN, systemFontSize);
        systemEmFont = Font.getFont(Font.FACE_PROPORTIONAL,
                Font.STYLE_BOLD, systemFontSize);
        charMaxWidth = Math.max(systemFont.charWidth('8'), systemFont.charWidth('6'));
        charHeight = systemEmFont.getHeight();
        baselinePosition = systemFont.getBaselinePosition();
        if (baselinePosition < systemFont.getHeight() / 2) // Obviously wrong
            baselinePosition = systemFont.getHeight() * 19 / 22;
    }

    public int getStyle() {
        return size + SYSTEM_FONT;
    }


    private int drawSpecialChar(Graphics g, int x, int y, char ch) {
        
        if (processChar(ch, false))
            return 0;

        SysFont gFont = (subscript || superscript) ? (SysFont)smallerFont : this;

        Font font = emphasized ? gFont.systemEmFont : gFont.systemFont;
        g.setFont(font);
        g.setColor(green ? Colors.c[green()] : Colors.c[fg]);

        if (monospaced) {
            // NB! Assuming that special characters will not be drawn monospaced
            x += charMaxWidth / 2;
            g.drawChar(ch, x, y, Graphics.TOP | Graphics.HCENTER);
            return charMaxWidth;
        }

// Looks bad in monitor:
// sqrt,monospaced,>>
// Too bold: gamma,rarr,pi,sum,alpha
        
        if ("­«¿ß¡¶ÞãëÐ£»¹¼Øð".indexOf(ch)>=0) {
            int w = font.charWidth('O');
            int h = gFont.baselinePosition;
            switch (ch) {
                case '­': // Arrow ->
                    g.fillRect(x,y+h/2+1,w-1,2);
                    g.drawLine(x+w-2,y+h/2+1,x+w-2-2,y+h/2+1-2);
                    g.drawLine(x+w-3,y+h/2+1,x+w-3-1,y+h/2+1-1);
                    g.drawLine(x+w-2,y+h/2+2,x+w-2-2,y+h/2+2+2);
                    g.drawLine(x+w-3,y+h/2+2,x+w-3-1,y+h/2+2+1);
                    break;
                case '«': // Arrows <->
                    g.drawLine(x,y+h/2,x+w-2,y+h/2);
                    g.drawLine(x+w-2,y+h/2,x+w-2-2,y+h/2-2);
                    g.drawLine(x,y+h/2+3,x+w-2,y+h/2+3);
                    g.drawLine(x,y+h/2+3,x+2,y+h/2+3+2);
                    if (emphasized) {
                        g.drawLine(x,y+h/2-1,x+w-2,y+h/2-1);
                        g.drawLine(x+w-2,y+h/2-1,x+w-2-2,y+h/2-1-2);
                        g.drawLine(x,y+h/2+2,x+w-2,y+h/2+2);
                        g.drawLine(x,y+h/2+2,x+2,y+h/2+2+2);
                    }
                    break;
                case '¿': // Sqrt
                    g.drawLine(x,y+h-3,x+3,y+h);
                    g.drawLine(x+1,y+h-3,x+4,y+h);
                    g.fillRect(x+3,y,2,h+1);
                    g.fillRect(x+5,y,w-4,2);
                    overline = true;
                    break;
                case 'ß': // Sum
                    int b = (h&1)^1;
                    int s = (h-b-4)/2;
                    g.fillRect(x,y+b,w-1,2);
                    g.drawLine(x,y+b+2,x+s,y+b+2+s);
                    g.drawLine(x+1,y+b+2,x+1+s,y+b+2+s);
                    g.drawLine(x,y+h-3,x+s,y+b+2+s);
                    g.drawLine(x+1,y+h-3,x+1+s,y+b+2+s);
                    g.fillRect(x,y+h-2,w-1,2);
                    break;
                case '¡': // Gamma
                    g.fillRect(x+1,y+2,2,h-3);
                    g.drawLine(x,y+h-1,x+3,y+h-1);
                    g.fillRect(x,y,w-1,2);
                    g.drawLine(x+w-2,y+2,x+w-2,y+3);
                    break;
                case '¶': // pi
                    g.drawLine(x,y+h/3,x+7,y+h/3);
                    g.fillRect(x+2,y+h/3,2,h-h/3);
                    g.drawLine(x,y+h/3,x,y+h/3+1);
                    g.fillRect(x+5,y+h/3,2,h-h/3);
                    break;
                case 'Þ': // _infinity
                    g.drawChar('o',x,y+charHeight-
                               smallerFont.baselinePosition,TOP_LEFT);
                    g.drawChar('o',x+font.charWidth('o')*4/6,
                               y+charHeight-
                               smallerFont.baselinePosition,TOP_LEFT);
                    w = font.charWidth('o')*(6+4)/6;
                    break;
                case 'ã': // alpha
                    int h2 = (h*2/3+1)&~1;
                    w = h2/2+5+(h2<10?0:1);
                    int x2 = x+w-h2/2;
                    int y2 = y+h-h2;
                    g.drawLine(x+w-2,y2,x2,y+h-3);
                    g.drawLine(x+w-3,y2,x2-1,y+h-3);
                    g.drawLine(x2-2,y+h-3,x2-1,y+h-2);
                    g.fillRect(x2-4,y+h-2,3,2);
                    g.drawLine(x2-5,y+h-2,x2-4,y+h-3);
                    if (h2<10) {
                        g.fillRect(x2-6,y2+2,2,y+h-y2-4);
                    } else {
                        int h3 = (h2-1)/4;
                        g.fillRect(x2-6,y2+2,2,h3);
                        g.fillRect(x2-7,y2+2+h3,2,y+h-y2-4-2*h3);
                        g.fillRect(x2-6,y+h-2-h3,2,h3);
                    }
                    g.drawLine(x2-5,y2+1,x2-4,y2+2);
                    g.fillRect(x2-4,y2,2,2);
                    g.drawLine(x2-2,y2+2,x2-2,y2+1);
                    g.drawLine(x2-1,y2+2,x+w-3,y+h-1);
                    g.drawLine(x+w-2,y+h-1,x+w-2,y+h-2);
                    g.drawLine(x+w-1,y+h-2,x+w-1,y+h-3);
                    break;
                case 'ë': // epsilon
                    g.drawChar('e',x,y,TOP_LEFT);
                    int w1 = font.charWidth('e')*67/112;
                    g.setColor(Colors.c[bg]);
                    g.fillRect(x+w1,y,w-w1,h);
                    g.setColor(Colors.c[fg]);
                    w = w1+1;
                    break;
                case 'Ð': // theta
                    g.drawChar('O',x,y,TOP_LEFT);
                    g.drawChar('-',x,y-1,TOP_LEFT);
                    g.drawChar(
                        '-',x+font.charWidth('O')-font.charWidth('-'),y-1,
                        TOP_LEFT);
                    break;
                case '£': // ln
                    g.drawString("ln",x,y,TOP_LEFT);
                    w = font.stringWidth("ln");
                    break;
                case '»': // >
                    for (int i=0; i<w-1; i++) {
                        g.drawLine(x+i, y+h-1-i, x+i, y+h-1-(w-2)*2+i);
                    }
                    break;
                case '¹':
                    g.setFont(emphasized ? ((SysFont)smallerFont).systemEmFont :
                                           ((SysFont)smallerFont).systemFont);
                    g.drawString("-1", x, y-1, TOP_LEFT);
                    g.setFont(font);
                    break;
                case '¼':
                    g.setFont(emphasized ? ((SysFont)smallerFont).systemEmFont :
                                           ((SysFont)smallerFont).systemFont);
                    g.drawString("4", x, y-1, TOP_LEFT);
                    g.setFont(font);
                    break;
                case 'Ø':
                    g.drawChar('O',x,y,TOP_LEFT);
                    g.drawLine(x,y+h-1,x+w-1,y+h-1);
                    g.setColor(Colors.c[bg]);
                    g.fillRect(x+font.charWidth('O')/2-1,y+h/2,2,charHeight-h/2);
                    g.setColor(Colors.c[fg]);
                    break;
                case 'ð':
                    int w2 = w & ~1;
                    g.drawLine(x,y+h-1,x+w2/2-1,y+h-w2);
                    g.drawLine(x+w2/2-1,y+h-w2,x+w2-2,y+h-1);
                    g.drawLine(x+w2/2,y+h-w2,x+w2-1,y+h-1);
                    g.drawLine(x,y+h-1,x+w2-1,y+h-1);
                    break;
            }
            return w;
        }

        if (subscript) {
            g.drawChar(ch,x,y+charHeight-
                       smallerFont.baselinePosition,TOP_LEFT);
        } else if (superscript) {
            g.drawChar(ch,x,y-1,TOP_LEFT);
        } else {
            g.drawChar(ch,x,y,TOP_LEFT);
            if (overline) {
                g.drawLine(x-1,y,x+font.charWidth(ch)-1,y);
                if (emphasized)
                    g.drawLine(x-1,y+1,x+font.charWidth(ch)-1,y+1);
            }
        }
        return font.charWidth(ch);
    }

    public int charWidth(char ch) {
        return monospaced ? charMaxWidth :
               emphasized ? systemEmFont.charWidth(ch) : systemFont.charWidth(ch);
    }

    private static boolean plainString(String string) {
        for (int i = 0; i < string.length(); i++)
            if ("^~¸`­«¿ß¡¶ÞãëÐ£¹¼Ø»ð".indexOf(string.charAt(i)) >= 0)
                return false;
        return true;
    }

    private static boolean plainString(StringBuffer string, int start) {
        for (int i = start; i < string.length(); i++)
            if ("^~¸­`«¿ß¡¶ÞãëÐ£¹¼Ø»ð".indexOf(string.charAt(i)) >= 0)
                return false;
        return true;
    }

    public int substringWidth(String string, int start, int end) {
        if (monospaced) {
            // NB! Assuming that special characters will not be drawn monospaced
            return charMaxWidth * (end - start);
        }

        UniFont font = this;

        if (plainString(string)) {
            if (emphasized) {
                return systemEmFont.substringWidth(string, start, end-start);
            }
            return systemFont.substringWidth(string, start, end-start);
        }
        int width = 0;
        for (int i = 0; i < end && i < string.length(); i++) {
            char c = string.charAt(i);
            int charWidth = 0;
            if (c == '^' || c == '¸') {
                font = (font == this) ? smallerFont : this;
            } else if (c == '~' || c == '`') {
                ; // overline... no font change
            } else if ("­«¿ß¡¶Ð»Øð".indexOf(c) >= 0) {
                charWidth = font.charWidth('O');
            } else if (c == 'Þ') {
                charWidth = font.charWidth('o') * (6 + 4) / 6;
            } else if (c == 'ã') {
                int h2 = (font.baselinePosition * 2 / 3 + 1) & ~1;
                charWidth = h2 / 2 + 5 + (h2 < 10 ? 0 : 1);
            } else if (c == 'ë') {
                charWidth = font.charWidth('e') * 67 / 112 + 1;
            } else if (c == '£') {
                charWidth = font.stringWidth("ln");
            } else if (c == '¹') {
                charWidth = smallerFont.stringWidth("-1");
            } else if (c == '¼') {
                charWidth = smallerFont.stringWidth("4");
            } else {
                charWidth = font.charWidth(c);
            }
            if (i >= start)
                width += charWidth;
        }
        return width;
    }

    public int drawSubstring(Graphics g, int x, int y, String string, int start, int end) {
        if (end > string.length())
            end = string.length();

        if (!monospaced && plainString(string)) {
            Font font = emphasized ? systemEmFont : systemFont;
            g.setFont(font);
            g.setColor(Colors.c[bg]);
            g.fillRect(x, y, substringWidth(string, start, end), charHeight);
            g.setColor(Colors.c[fg]);
            g.drawSubstring(string, start, end-start, x, y, Graphics.TOP | Graphics.LEFT);
            return x + font.substringWidth(string, start, end-start);
        }
        
        subscript = superscript = overline = false;
        
        for (int i = 0; i < start; i++) {
            processChar(string.charAt(i), true);
        }
        for (int i = start; i < end; i++) {
            x += drawSpecialChar(g, x, y, string.charAt(i));
        }
        return x;
    }

    public int drawString(Graphics g, int x, int y, StringBuffer string, int start) {
        int end = string.length();
        
        if (!monospaced && plainString(string,start)) {
            String s = string.toString();
            Font font = emphasized ? systemEmFont : systemFont;
            g.setFont(font);
            g.setColor(Colors.c[bg]);
            g.fillRect(x, y, substringWidth(s, start, end), charHeight);
            g.setColor(Colors.c[fg]);
            g.drawSubstring(s, start, end - start, x, y, Graphics.TOP
                    | Graphics.LEFT);
            return x + font.substringWidth(s, start, end - start);
        }
        
        green = subscript = superscript = overline = false;
        
        for (int i = 0; i < start; i++) {
            processChar(string.charAt(i), true);
        }
        for (int i = start; i < end; i++) {
            x += drawSpecialChar(g, x, y, string.charAt(i));
        }
        return x;
    }
}
