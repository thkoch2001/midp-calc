package javax.microedition.lcdui;

public class Font
{
    public static final int FACE_MONOSPACE = 32;
    public static final int FACE_PROPORTIONAL = 64;
    public static final int FACE_SYSTEM = 0;
    public static final int SIZE_LARGE = 16;
    public static final int SIZE_MEDIUM = 0;
    public static final int SIZE_SMALL = 8;
    public static final int STYLE_BOLD = 1;
    public static final int STYLE_ITALIC = 2;
    public static final int STYLE_PLAIN = 0;
    public static final int STYLE_UNDERLINED = 4;

    int style;
    java.awt.Font font;
    java.awt.FontMetrics fontMetrics;

    private Font(int s) {
        style = s;
        font = new java.awt.Font(
            (s & FACE_MONOSPACE)!=0 ? "Monospaced" : "SansSerif",
            ((s & STYLE_BOLD)!=0 ? java.awt.Font.BOLD : 0)|
            ((s & STYLE_ITALIC)!=0 ? java.awt.Font.ITALIC : 0),
            12+
            ((s & SIZE_SMALL)!=0 ? -3 : 0)+
            ((s & SIZE_LARGE)!=0 ?  3 : 0));
        // Creating a temporary image to get a Graphcis to get FontMetrics
        java.awt.Image tmpI =
            new java.awt.image.BufferedImage(
                10,10,java.awt.image.BufferedImage.TYPE_INT_RGB);
        java.awt.Graphics tmpG = tmpI.getGraphics();
        fontMetrics = tmpG.getFontMetrics(font);
    }

    public int charWidth(char ch) {
        return fontMetrics.charWidth(ch);
    }

    public int getHeight() {
        return fontMetrics.getHeight();
    }

    public static Font getFont(int face, int style, int size) {
        return new Font(face|style|size);
    }

    public int getBaselinePosition() {
        return fontMetrics.getAscent();
    }

    public int stringWidth(String str) {
        return fontMetrics.stringWidth(str);
    }

    public int substringWidth(String str, int offset, int len) {
        return fontMetrics.stringWidth(str.substring(offset,offset+len));
    }
}
