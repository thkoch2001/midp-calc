package javax.microedition.midlet;

public abstract class MIDlet 
{
  public Display display = new Display();

  public abstract void startApp();
  public abstract void pauseApp();
  public abstract void destroyApp(boolean unconditional);
  public void notifyDestroyed() { }
  public void notifyPaused() { }
  public void resumeRequest() { }
  public String getAppProperty(String key) {
    return System.getProperty(key);
  }
}
