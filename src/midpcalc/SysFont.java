package midpcalc;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

final class SysFont extends UniFont {

    private Font systemFont;
    private Font systemBoldFont;
    private Font systemItalicFont;
    private Font systemBoldItalicFont;

    public void close() {
        systemFont = null;
        systemBoldFont = null;
        systemItalicFont = null;
        systemBoldItalicFont = null;
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
        systemBoldFont = Font.getFont(Font.FACE_PROPORTIONAL,
                Font.STYLE_BOLD, systemFontSize);
        systemItalicFont = Font.getFont(Font.FACE_PROPORTIONAL,
                Font.STYLE_ITALIC, systemFontSize);
        systemBoldItalicFont = Font.getFont(Font.FACE_PROPORTIONAL,
                Font.STYLE_BOLD|Font.STYLE_ITALIC, systemFontSize);
        charMaxWidth = Math.max(systemFont.charWidth('8'), systemFont.charWidth('6'));
        charHeight = systemBoldFont.getHeight();
        baselinePosition = systemFont.getBaselinePosition();
        if (baselinePosition < systemFont.getHeight() / 2) // Obviously wrong
            baselinePosition = systemFont.getHeight() * 19 / 22;
    }

    public int getStyle() {
        return size + SYSTEM_FONT;
    }

    private Font currentFont() {
        return bold ? (italic ? systemBoldItalicFont : systemBoldFont) :
                      (italic ? systemItalicFont : systemFont);
    }
    
    private int drawSpecialChar(Graphics g, int x, int y, char ch) {
        
        if (processChar(ch, false))
            return 0;

        SysFont gFont = (subscript || superscript) ? (SysFont)smallerFont : this;

        Font font = gFont.currentFont();
        g.setFont(font);

        if (monospaced) {
            // NB! Assuming that special characters will not be drawn monospaced
            x += charMaxWidth / 2;
            g.drawChar(ch, x, y, Graphics.TOP | Graphics.HCENTER);
            return charMaxWidth;
        }

        if ("­«¿ß¡¶ÞãëÐ£»¹¼Øð".indexOf(ch)>=0) {
            int w = font.charWidth('O');
            int h = gFont.baselinePosition;
            switch (ch) {
                case '­': // Arrow ->
                    g.drawLine(x,y+h/2+1,x+w-2,y+h/2+1);
                    g.drawLine(x+w-2,y+h/2+1,x+w-2-2,y+h/2+1-2);
                    g.drawLine(x+w-2,y+h/2+1,x+w-2-2,y+h/2+1+2);
                    if (bold) {
                        g.drawLine(x,y+h/2+2,x+w-2,y+h/2+2);
                        g.drawLine(x+w-2,y+h/2+2,x+w-2-2,y+h/2+2-2);
                        g.drawLine(x+w-2,y+h/2+2,x+w-2-2,y+h/2+2+2);
                    }
                    break;
                case '«': // Arrows <->
                    g.drawLine(x,y+h/2,x+w-2,y+h/2);
                    g.drawLine(x+w-2,y+h/2,x+w-2-2,y+h/2-2);
                    g.drawLine(x,y+h/2+3,x+w-2,y+h/2+3);
                    g.drawLine(x,y+h/2+3,x+2,y+h/2+3+2);
                    if (bold) {
                        g.drawLine(x,y+h/2-1,x+w-2,y+h/2-1);
                        g.drawLine(x+w-2,y+h/2-1,x+w-2-2,y+h/2-1-2);
                        g.drawLine(x,y+h/2+2,x+w-2,y+h/2+2);
                        g.drawLine(x,y+h/2+2,x+2,y+h/2+2+2);
                    }
                    break;
                case '¿': // Sqrt
                    g.drawLine(x,y+h-3,x+3,y+h);
                    g.drawLine(x+3,y+h,x+3,y);
                    g.drawLine(x+3,y,x+w,y);
                    if (bold) {
                        g.drawLine(x+1,y+h-3,x+4,y+h);
                        g.drawLine(x+4,y+h,x+4,y+1);
                        g.drawLine(x+4,y+1,x+w,y+1);
                    }
                    overline = true;
                    break;
                case 'ß': // Sum
                    int b = (h&1)^1;
                    int s = (h-b-4)/2;
                    g.drawLine(x,y+b,x+w-2,y+b);
                    g.drawLine(x,y+b,x,y+b+2);
                    g.drawLine(x,y+b+2,x+s,y+b+2+s);
                    g.drawLine(x,y+h-3,x+s,y+b+2+s);
                    g.drawLine(x,y+h-3,x,y+h-1);
                    g.drawLine(x,y+h-1,x+w-2,y+h-1);
                    if (bold) {
                        g.drawLine(x+1,y+b+1,x+w-2,y+b+1);
                        g.drawLine(x+1,y+b+2,x+1+s,y+b+2+s);
                        g.drawLine(x+1,y+h-3,x+1+s,y+b+2+s);
                        g.drawLine(x+1,y+h-2,x+w-2,y+h-2);
                    }
                    break;
                case '¡': // Gamma
                    g.fillRect(x+1,y+1,bold ? 2 : 1,h-2);
                    g.fillRect(x,y+h-1,bold ? 4 : 3,1);
                    g.fillRect(x,y,w-1,bold ? 2 : 1);
                    g.fillRect(x+w-2,y+1,1,bold ? 3 : 2);
                    break;
                case '¶': // pi
                    g.drawLine(x,y+h/3,x+7,y+h/3);
                    g.fillRect(x+2,y+h/3,bold ? 2 : 1,h-h/3);
                    g.drawLine(x,y+h/3,x,y+h/3+1);
                    g.fillRect(x+5,y+h/3,bold ? 2 : 1,h-h/3);
                    w = 8;
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
                    w = h2/2+6+(h2<10?0:1);
                    int x2 = x+w-h2/2;
                    int y2 = y+h-h2;
                    g.drawLine(x+w-2,y2,x2,y+h-3);
                    g.fillRect(x2-1,y+h-2,1,1);
                    g.drawLine(x2-4,y+h-1,x2-2,y+h-1);
                    g.fillRect(x2-5,y+h-2,1,1);
                    g.drawLine(x,y+h2,x2-6,y2+2);
                    g.drawLine(x,y+h2-1,x2-6,y+h-3);
                    g.drawLine(x2-5,y2+1,x2-4,y2);
                    g.drawLine(x2-3,y2,x2-2,y2+1);
                    g.drawLine(x2-1,y2+2,x+w-3,y+h-1);
                    g.drawLine(x+w-2,y+h-1,x+w-1,y+h-2);
                    if (bold) {
                        g.drawLine(x+w-3,y2,x2-1,y+h-3);
                        g.fillRect(x2-2,y+h-3,1,1);
                        g.drawLine(x2-4,y+h-2,x2-2,y+h-2);
                        g.fillRect(x2-4,y+h-3,1,1);
                        g.drawLine(x+1,y+h2,x2-5,y2+2);
                        g.drawLine(x+1,y+h2-1,x2-5,y+h-3);
                        g.drawLine(x2-4,y2+1,x2-4,y2+2);
                        g.drawLine(x2-3,y2+1,x2-2,y2+2);
                        g.drawLine(x+w-2,y+h-2,x+w-1,y+h-3);
                    }
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
                    g.setColor(Colors.c[Colors.GREEN]);
                    int n=w-2;
                    if ((n-1)*2 > h-1)
                        n = (h-1)/2+1;
                    for (int i=0; i<n; i++) {
                        g.drawLine(x+i+1, y+h-1-i, x+i+1, y+h-1-(n-1)*2+i);
                    }
                    g.setColor(Colors.c[fg]);
                    break;
                case '¹':
                    g.setFont(((SysFont)smallerFont).currentFont());
                    g.drawString("-1", x, y-1, TOP_LEFT);
                    g.setFont(font);
                    w = smallerFont.stringWidth("-1");
                    break;
                case '¼':
                    g.setFont(((SysFont)smallerFont).currentFont());
                    g.drawString("4", x, y-1, TOP_LEFT);
                    g.setFont(font);
                    w = smallerFont.stringWidth("4");
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
                if (bold)
                    g.drawLine(x-1,y+1,x+font.charWidth(ch)-1,y+1);
            }
        }
        return font.charWidth(ch);
    }

    public int charWidth(char ch) {
        return monospaced ? charMaxWidth : currentFont().charWidth(ch);
    }

    private static boolean plainString(String string) {
        for (int i = 0; i < string.length(); i++)
            if ("^~¸`­«¿ß¡¶ÞãëÐ£¹¼Ø»ð".indexOf(string.charAt(i)) >= 0)
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
            return currentFont().substringWidth(string, start, end-start);
        }
        int width = 0;
        for (int i = 0; i < end && i < string.length(); i++) {
            char c = string.charAt(i);
            int charWidth = 0;
            if (c == '^' || c == '¸') {
                font = (font == this) ? smallerFont : this;
            } else if (c == '~' || c == '`') {
                ; // overline... no font change
            } else if ("­«¿ß¡Ð»Øð".indexOf(c) >= 0) {
                charWidth = font.charWidth('O');
            } else if (c == '¶') {
                charWidth = 8;
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

        g.setColor(Colors.c[bg]);
        g.fillRect(x, y, substringWidth(string, start, end), charHeight);
        g.setColor(Colors.c[fg]);

        if (!monospaced && plainString(string)) {
            Font font = currentFont();
            g.setFont(font);
            g.drawSubstring(string, start, end-start, x, y, Graphics.TOP | Graphics.LEFT);
            return x + font.substringWidth(string, start, end-start);
        }
        
        italic = subscript = superscript = overline = false;
        
        for (int i = 0; i < start; i++) {
            processChar(string.charAt(i), true);
        }
        for (int i = start; i < end; i++) {
            x += drawSpecialChar(g, x, y, string.charAt(i));
        }
        return x;
    }

    public int drawString(Graphics g, int x, int y, StringBuffer string, int start) {
        String s = string.toString();
        return drawSubstring(g, x, y, s, start, string.length());
    }
}
