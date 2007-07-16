package midpcalc;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

public abstract class UniFont {

    public static final int SMALL = 0;
    public static final int MEDIUM = 1;
    public static final int LARGE = 2;
    public static final int SYSTEM = 3; // deprecated
    public static final int XLARGE = 4;
    public static final int XXLARGE = 5;
    public static final int XXXLARGE = 6;
    public static final int SIZE_MASK = 7;
    public static final int BGR_ORDER = 8;
    public static final int SYSTEM_FONT = 16;

    protected static final int TOP_LEFT = Graphics.TOP | Graphics.LEFT;
    protected int charMaxWidth;
    protected int charHeight;
    protected int baselinePosition;
    protected int size;
    protected boolean monospaced;
    protected boolean emphasized;
    protected boolean green;
    protected boolean subscript;
    protected boolean superscript;
    protected boolean overline;
    protected int fg;
    protected int bg;
    public UniFont smallerFont;

    public static UniFont newFont(int style, boolean largeCache, boolean needSmallerFont, Canvas canvas) {
        UniFont font = null;
        int smallerStyle = style & ~SIZE_MASK;
        switch (style & SIZE_MASK) {
            case SMALL:
            case MEDIUM:   smallerStyle |= SMALL;   break;
            case LARGE:    smallerStyle |= MEDIUM;  break;
            case XLARGE:   smallerStyle |= LARGE;   break;
            case XXLARGE:  smallerStyle |= XLARGE;  break;
            case XXXLARGE: smallerStyle |= XXLARGE; break;
            case SYSTEM:
                smallerStyle |= SMALL;
                smallerStyle |= SYSTEM_FONT;
                break;
        }
        if ((style & SYSTEM_FONT) != 0 || (style & SIZE_MASK) == SYSTEM) {
            font = new SysFont(style);
        } else {
            font = new GFont(style, largeCache, canvas);
        }
        if (needSmallerFont) {
            font.smallerFont = newFont(smallerStyle, false, false, canvas);
        }
        return font;
    }

    public abstract void close();

    UniFont(int style) {
        fg = Colors.NUMBER;
        bg = Colors.BACKGROUND;

        size = style & SIZE_MASK;
    }

    public int getStyle() {
        return size;
    }
    
    public void setColor(int fg, int bg) {
        this.fg = fg;
        this.bg = bg;
        if (smallerFont != null && smallerFont != this)
            smallerFont.setColor(fg, bg);
    }
    
    protected int green() {
        return Colors.c[bg] == 0 ? Colors.GREEN : Colors.DARK_GREEN;
    }

    public void setMonospaced(boolean monospaced) {
        this.monospaced = monospaced;
        if (smallerFont != null && smallerFont != this)
            smallerFont.setMonospaced(monospaced);
    }

    public void setEmphasized(boolean emphasized) {
        this.emphasized = emphasized;
        if (smallerFont != null && smallerFont != this)
            smallerFont.setEmphasized(emphasized);
    }

    protected boolean processChar(char ch, boolean skip) {
        if (ch=='^' || ch=='¸') {
            boolean prevSubscript = subscript;
            boolean prevSuperscript = superscript;
            subscript = superscript = false;
            subscript   = ch=='¸' && !prevSubscript;
            superscript = ch=='^' && !prevSuperscript;
            return true;
        }
        
        if (ch=='~') {
            overline = !overline;
            return true;
        }
        
        if (ch=='`') {
            green = !green;
            return true;
        }
        
        if (ch=='¿' && skip) {
            overline = true;
        }
        
        return false;
    }

    public int getHeight() {
        return charHeight;
    }

    public int charWidth() {
        return charMaxWidth;
    }

    public int getBaselinePosition() {
        return baselinePosition;
    }

    public abstract int charWidth(char ch);

    public int stringWidth(String string) {
        return substringWidth(string,0,string.length());
    }

    public abstract int substringWidth(String string, int i, int j);

    public int drawString(Graphics g, int x, int y, String string) {
        return drawSubstring(g, x, y, string, 0, string.length());
    }

    public abstract int drawSubstring(Graphics g, int x, int y, String string, int i, int j);

    public abstract int drawString(Graphics g, int x, int y, StringBuffer string, int start);
}