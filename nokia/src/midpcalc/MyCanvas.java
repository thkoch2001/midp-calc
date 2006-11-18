package midpcalc;

import javax.microedition.lcdui.*;
import com.nokia.mid.ui.*;

public abstract class MyCanvas
    extends FullCanvas
{
    public boolean automaticCommands() {
        return false;
    }
    public boolean isFullScreen() {
        return true;
    }
    public boolean canToggleFullScreen() {
        return false;
    }
    public void setFullScreenMode(boolean mode) {
    }
    private Command a,b;
    public void addCommand(Command cmd) {
        if (a == null)
            a = cmd;
        else
            b = cmd;
    }
    public void removeCommand(Command cmd) {
        if (cmd == a)
            a = null;
        else if (cmd == b)
            b = null;
    }
    public void setCommandListener(CommandListener l) {
        // Not throwing exception
    }
    protected void paintCommands(Graphics g, Font f, String middle, Font f2) {
        g.setColor(0xffffff);
        g.fillRect(0,getHeight()-f.getHeight(),getWidth(),f.getHeight());
        g.setColor(0);
        g.fillRect(0,getHeight()-f.getHeight()-1,getWidth(),1);
        g.setFont(f);
        if (a != null)
            g.drawString(a.getLabel(), 5, getHeight()-f.getHeight(),
                         Graphics.TOP|Graphics.LEFT);
        if (b != null)
            g.drawString(b.getLabel(),getWidth()-5,getHeight()-f.getHeight(),
                         Graphics.TOP|Graphics.RIGHT);
        if (middle != null && f2 != null &&
            (a == null ||
             f.stringWidth(a.getLabel())+
             f2.stringWidth(middle)*5/6<getWidth()/2)&&
            (b == null ||
             f.stringWidth(b.getLabel())+
             f2.stringWidth(middle)*5/6<getWidth()))
        {
            g.setFont(f2);
            g.drawString(middle,getWidth()/2,getHeight()-f.getHeight(),
                         Graphics.TOP|Graphics.HCENTER);
        }
    }
}
