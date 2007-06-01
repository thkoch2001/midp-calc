package midpcalc;

public final class Colors {
    private static final int NUM_COLORS = 22;
    private static final int MENU_SIZE = 7;

    public static final int NUMBER = 0;
    public static final int BACKGROUND = 1;
    public static final int FOREGROUND = 2;
    public static final int EMPHASIZED = 3;
    public static final int BLACK = 4;
    public static final int GREEN = 5;
    public static final int PINK = 6;
    public static final int CYAN = 7;
    public static final int MENU = 8;
    public static final int MENU_DARK = MENU+MENU_SIZE;
    
    public static final int[] c = new int[NUM_COLORS];
    static {
        c[NUMBER]     = 0xffd2ff;
        c[BACKGROUND] = 0;
        c[FOREGROUND]    = 0xffffff;
        c[EMPHASIZED] = 0x0000c0;
        c[GREEN]      = c[NUMBER] & 0x00ff00;
        c[PINK]       = 0xd89c9c;
        c[CYAN]       = 0x9cd8d8;
        // Menu color components divisible by 4 for easy dimming
        c[MENU+0]     = 0x00e0e0;
        c[MENU+1]     = 0x00fc00;
        c[MENU+2]     = 0xe0e000;
        c[MENU+3]     = 0xfca800;
        c[MENU+4]     = 0xfc5400;
        c[MENU+5]     = 0xfc0000;
        c[MENU+6]     = 0xc00080;
        for (int i=0; i<MENU_SIZE; i++) {
            c[MENU_DARK+i] = c[MENU+i]/4;
        }
    }
}
