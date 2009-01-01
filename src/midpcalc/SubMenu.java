package midpcalc;

public class SubMenu extends Menu {
    private String label;
    private short flags;
    private Menu subMenu1, subMenu2, subMenu3, subMenu4, subMenu5;

    public SubMenu(String l, int f, Menu m1, Menu m2, Menu m3, Menu m4, Menu m5) {
        label = l;
        flags = (short)f;
        subMenu1 = m1;
        subMenu2 = m2;
        subMenu3 = m3;
        subMenu4 = m4;
        subMenu5 = m5;
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

    public boolean hasSubMenu() {
        return subMenu1 != null || subMenu2 != null || subMenu3 != null || subMenu4 != null || subMenu5 != null;
    }

    public void setSubMenu(int i, Menu m) {
        switch (i) {
            case 0: subMenu1 = m; break;
            case 1: subMenu2 = m; break;
            case 2: subMenu3 = m; break;
            case 3: subMenu4 = m; break;
            case 4: subMenu5 = m; break;
        }
    }

    public Menu getSubMenu(int i) {
        switch (i) {
            case 0: return subMenu1;
            case 1: return subMenu2;
            case 2: return subMenu3;
            case 3: return subMenu4;
            case 4: return subMenu5;
        }
        return null;
    }
}
