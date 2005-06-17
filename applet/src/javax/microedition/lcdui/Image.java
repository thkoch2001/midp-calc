package javax.microedition.lcdui;

public class Image
{
  public java.awt.Image image;
  int width,height;

  private Image(java.awt.Image i, int w, int h) {
    image = i;
    width = w;
    height = h;
  }

  public static Image createImage(int width, int height) {
    return new Image(new java.awt.image.BufferedImage(width,height,
      java.awt.image.BufferedImage.TYPE_INT_RGB),width,height);
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public Graphics getGraphics() {
    return new Graphics(image.getGraphics(), null);
  }
}
