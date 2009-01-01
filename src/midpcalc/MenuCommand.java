package midpcalc;

public class MenuCommand extends Menu {

    private short command;

    public MenuCommand(int c) {
        command = (short)c;
        // get Details from CmdDesc class
    }

    public String getLabel() {
        return CmdDesc.getStr(command, true);
    }

    public short getCommand() {
        return command;
    }

    public short getFlags() {
        return CmdDesc.getFlags(command);
    }

}
