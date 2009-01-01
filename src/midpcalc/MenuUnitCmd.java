package midpcalc;

public class MenuUnitCmd extends Menu {

    private String label;

    public MenuUnitCmd(String l) {
        label = l;
        findUnit(label); // Fail early
    }

    public String getLabel() {
        return label;
    }

    public short getCommand() {
        return (short)CalcCanvas.UNIT;
    }

    public short getFlags() {
        return CmdDesc.REPEAT_PARENT;
    }

    public short getParam() {
        return findUnit(label);
    }
    
    static short findUnit(String name) {
        for (int unitType=0; unitType<Unit.allUnits.length; unitType++)
            for (int unit=0; unit<Unit.allUnits[unitType].length; unit++)
                if (name.equals(Unit.allUnits[unitType][unit].name))
                    return (short)((unit<<8) + unitType);
        throw new IllegalStateException("Could not find unit");
    }
}
