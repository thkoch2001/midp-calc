package midpcalc;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Graphics;

import com.nokia.mid.ui.FullCanvas;

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
    protected void paintCommands(Graphics g, UniFont f, String middle) {
        g.setColor(Colors.c[Colors.FOREGROUND]);
        g.fillRect(0,getHeight()-f.getHeight(),getWidth(),f.getHeight());
        g.setColor(Colors.c[Colors.BACKGROUND]);
        g.fillRect(0,getHeight()-f.getHeight()-1,getWidth(),1);
        f.setColor(Colors.BACKGROUND, Colors.FOREGROUND);
        f.setEmphasized(true);
        if (a != null)
            f.drawString(g, 5, getHeight()-f.getHeight(), a.getLabel());
        if (b != null)
            f.drawString(g, getWidth()-5-f.stringWidth(b.getLabel()),
                    getHeight()-f.getHeight(), b.getLabel());
        if (middle != null &&
            (a == null ||
             f.stringWidth(a.getLabel())+
             f.stringWidth(middle)*5/6<getWidth()/2)&&
            (b == null ||
             f.stringWidth(b.getLabel())+
             f.stringWidth(middle)*5/6<getWidth()))
        {
            f.setEmphasized(false);
            f.drawString(g, getWidth()/2-f.stringWidth(middle)/2,
                    getHeight()-f.getHeight(), middle);
        }
    }
}
