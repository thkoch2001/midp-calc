package javax.microedition.lcdui;

public abstract class Canvas extends Displayable
{
    public static final int UP = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 5;
    public static final int DOWN = 6;
    public static final int FIRE = 8;
    public static final int GAME_A = 9;
    public static final int GAME_B = 10;
    public static final int GAME_C = 11;
    public static final int GAME_D = 12;

    java.awt.Image image;
    Graphics graphics;

    protected Canvas() {
        image = new java.awt.image.BufferedImage(
            getWidth(),getHeight(),java.awt.image.BufferedImage.TYPE_INT_RGB);
        graphics = new Graphics(image.getGraphics(),
                                midpcalc.CalcApplet.getCurrentApplet());
    }

    public int getGameAction(int keyCode) {
        switch (keyCode) {
            case -1:
            case '2': return UP;
            case -3:
            case '4': return LEFT;
            case -4:
            case '6': return RIGHT;
            case -2:
            case '8': return DOWN;
            case -5:
            case '5': return FIRE;
        }
        return 0;
    }

    public boolean hasPointerEvents() {
        return true;
    }

    public boolean hasRepeatEvents() {
        return false;
    }

    protected abstract void paint(Graphics g);

    public void paint(java.awt.Graphics g) {
        g.drawImage(image,0,0,midpcalc.CalcApplet.getCurrentApplet());
        graphics.g.setClip(0,0,getWidth(),getHeight());
        paint(graphics);
        g.drawImage(image,0,0,midpcalc.CalcApplet.getCurrentApplet());
    }

    public void setFullScreenMode(boolean mode) {
    }
}
