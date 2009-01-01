package midpcalc;

public class MenuGeneralCmd extends MenuCommand {

    private String label;
    private short flags;

    public MenuGeneralCmd(String l, int c, int f) {
        super(c);
        label = l;
        flags = (short)f;
    }

    public void setLabel(String l) {
        label = l;
    }

    public String getLabel() {
        return label;
    }

    public void setFlags(short f) {
        flags = f;
    }

    public short getFlags() {
        return flags;
    }

}
