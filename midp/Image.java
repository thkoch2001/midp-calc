package javax.microedition.lcdui;

public class Image
{
  public java.awt.Image image;

  private Image(java.awt.Image i) {
    image = i;
  }

  public static Image createImage(int width, int height) {
    return new Image(new java.awt.image.BufferedImage(width,height,
      java.awt.image.BufferedImage.TYPE_INT_RGB));
  }

  public Graphics getGraphics() {
    return new Graphics(image.getGraphics(), null);
  }
}
