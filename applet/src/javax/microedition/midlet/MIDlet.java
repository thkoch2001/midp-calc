package javax.microedition.midlet;
import javax.microedition.lcdui.*;
import netscape.javascript.JSObject;

public abstract class MIDlet 
{
    public Display midletDisplay = new Display();

    public abstract void startApp();
    public abstract void pauseApp();
    public abstract void destroyApp(boolean unconditional);
    public void notifyDestroyed() {
        try {
            JSObject.getWindow(midpcalc.CalcApplet.getCurrentApplet()).
                eval("window.close();");
        } catch (Throwable e) {
        }
    }
    public void notifyPaused() { }
    public void resumeRequest() { }
    public String getAppProperty(String key) {
        return System.getProperty(key);
    }
}
