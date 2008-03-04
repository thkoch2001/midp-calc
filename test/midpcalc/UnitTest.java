package midpcalc;

import junit.framework.TestCase;

public class UnitTest extends TestCase {

    private static Unit findUnit(String name) {
        for (int unitType=0; unitType<Unit.allUnits.length; unitType++)
            for (int unit=0; unit<Unit.allUnits[unitType].length; unit++)
                if (name.equals(Unit.allUnits[unitType][unit].name))
                    return Unit.u().setUnit(unitType, unit);
        throw new IllegalStateException("Could not find unit "+name);
    }

    private static void check(Unit unit, String s) {
        Unit u = new Unit();
        u.unpack(unit.pack());
        String us = u.toString(); 
        if (!us.equals(s))
            throw new IllegalStateException("Unit "+unit+" toString="+us+", expected="+s);
    }

    private static void check(String a, String op, String b, String u) {
        check(a,op,b,"1",u);
    }

    private static void check(String u1, String amount, String u) {
        check(u1, "->", u, amount, u);
    }

    private static void check(String u1, String op, String u2, String amount, String u) {
        String amountStr = (amount.equals("1") && u.length()!=0 ? "" : amount+" ");
        if (op.equals("->") && u2.equals(u))
            System.out.println(u1+" = "+amountStr+u);
        else
            System.out.println(u1+" "+op+" "+u2+" = "+amountStr+u);

        Unit a = findUnit(u1);
        Unit b = findUnit(u2);
        Real f = new Real();
        Real o = new Real();
        if (op.equals("+")) {
            a.unpack(Unit.add(a.pack(), b.pack(), f, o));
        } else if (op.equals("-")) {
            a.unpack(Unit.sub(a.pack(), b.pack(), f, o));
        } else if (op.equals("·")) {
            a.unpack(Unit.mul(a.pack(), b.pack(), f));
        } else if (op.equals("/")) {
            a.unpack(Unit.div(a.pack(), b.pack(), f));
        } else if (op.equals("->")) {
            a.unpack(Unit.convertTo(a.pack(), b.pack(), f, o));
        }
        check(a,u);
        if (amount.indexOf('+') >= 0 || 
            (amount.indexOf('-') >= 0 && amount.indexOf('-') != amount.indexOf('e')+1)) {
            String aCheck = f.toString()+(o.isNegative() ? "" : "+")+o.toString();
            if (!aCheck.equals(amount))
                throw new IllegalStateException("Got "+aCheck+" "+u+", expected "+amount+" "+u);
        } else {
            Real f2 = new Real(amount);
            f2.sub(f);
            if (!o.isZero())
                throw new IllegalStateException("Got "+f+(o.isNegative() ? "" : "+")+o+" "+u+", expected "+amount+" "+u);
            if (!f2.isZero() && !f.toString().equals(amount))
                throw new IllegalStateException("Got "+f+" "+u+", expected "+amount+" "+u);
        }
    }

    public void testAllUnits() {
        for (int unitType=0; unitType<Unit.N_DERIVED_UNITS; unitType++) {
            for (int unit=0; unit<Unit.allUnits[unitType].length; unit++) {
                Unit aUnit = Unit.u().setUnit(unitType, unit);
                check(aUnit, Unit.allUnits[unitType][unit].name);
                System.out.print(Unit.toString(aUnit.pack()));
                if (!Unit.allUnits[unitType][unit].conversionFactor.equalTo(Real.ONE)) {
                    if (!Unit.allUnits[unitType][unit].conversionFactor.isNan())
                        System.out.print(" = "+Unit.allUnits[unitType][unit].conversionFactor+" "+
                                Unit.allUnits[unitType][unit].convertsTo);
                    else
                        System.out.print(" (nonlinear conversion)");
                } else if (Unit.allUnits[unitType][unit].convertsTo != null) {
                    System.out.print(" = "+Unit.allUnits[unitType][unit].convertsTo.toString(false, false));
                } else {
                    System.out.print(" (base SI unit)");
                }
                System.out.println();
            }
            System.out.println();
        }
        for (int unitType=0; unitType<Unit.N_PRIMITIVE_UNITS; unitType++) {
            int power = Math.min(1<<(Unit.bitsPerPower[unitType]-1),4);
            check(Unit.u().set(unitType,0,power), Unit.allUnits[unitType][0].name+Unit.DerivedUnit.powerStr[power]);
            check(Unit.u().set(unitType,0,-1), Unit.allUnits[unitType][0].name+"¹");
            check(Unit.u().set(unitType,0,-power), "/"+Unit.allUnits[unitType][0].name+Unit.DerivedUnit.powerStr[power]);
        }
    }
    
    public void testUnitRelationships() {
        check("kg",/* = */ "1000","g");
        check("t","1000","kg");
        check("lb","7000","gr");
        check("lb","16","oz");
        check("lb","0.45359237","kg");
        check("st","14","lb");
        check("ton","2000","lb");
        check("`ton`","2240","lb");
        check("u","1.660538782e-27","kg");
        
        check("m","10000000000","Å");
        check("m","1000","mm");
        check("m","100","cm");
        check("km","1000","m");
        check("in","2.54","cm");
        check("ft","12","in");
        check("yd","3","ft");
        check("mi","1760","yd");
        check("NM","1852","m");
        check("AU","149597870691","m");
        check("ly","9460730472580800","m");
        check("pc","3.085677581305729e16","m");

        check("min","60","s");
        check("h","60","min");
        check("d","24","h");
        check("y","365.25","d");

        check("e","1.602176487e-19","C");

        check("K","1","ð°C");
        check("K","1.8","ð°F");
        check("ð°C","1.8","ð°F");

        check("a","100","m²");
        check("da","1000","m²");
        check("ha","10000","m²");
        check("acre","4840","yd²");

        check("m³","1000","l");
        check("l","1000","ml");
        check("l","100","cl");
        check("l","10","dl");
        check("gal","231","in³");
        check("gal","8","pt");
        check("pt","2","cup");
        check("pt","16","fl.oz");
        check("`gal`","4.54609","l");
        check("`gal`","8","`pt`");
        check("`cup`","8","`fl.oz`");
        check("`pt`","20","`fl.oz`");
        
        check("`g`","9.80665","m/s²");
        
        check("bar","100000","Pa");
        check("atm","101325","Pa");
        check("mmHg","133.322387415","Pa");

        check("kJ","1000","J");
        check("cal","4.1868","J");
        check("kcal","4186.8","J");
        check("Btu","1055.05585262","J");
        check("eV","1.602176487e-19","J");

        check("kW","1000","W");
        check("MW","1000000","W");
        check("hp","745.69987158227022","W");

        check("mi","/","h",/* = */ "mph");
        check("NM","/","h","knot");
        check("ly","/","y","c");
        check("in","/","s²","in/s²");
        check("ft","/","s²","ft/s²");
        check("mi","/","s²","mi/s²");
        check("kg","·","m/s²","N");
        check("lb","·","ft/s²","pdl");
        check("`g`","·","kg","kgf");
        check("`g`","·","lb","lbf");
        check("N","/","m²","Pa");
        check("lbf","/","in²","psi");
        check("N","·","m","J");
        check("J","/","s","W");
        check("C","/","s","A");
        check("W","/","A","V");
        check("C","/","V","F");
        check("V","/","A","Ø");
        check("V","·","s","Wb");
        check("Wb","/","m²","T");
        check("Wb","/","A","H");
    }
    
    public void testTemperatureUnits() {
        check("°C","·","m",Unit.ERR);
        check("°C","+","m",Unit.ERR);
        check("°F","/","m",Unit.ERR);
        check("°C","->","m",Unit.ERR);
        check("ð°C","·","m","m·ð°C");
        check("ð°F","/","m","ð°F/m");
        check("","·","°C","°C");
        check("","·","°F","°F");
        check("","/","°C","°C¹");
        check("","/","°F","°F¹");

        check("°C","+","°C",Unit.ERR);
        check("°C","+","°F",Unit.ERR);
        check("°C","+","ð°C","°C");
        check("°C","+","ð°F","1.8+32","°F");
        check("°C","+","K","1+273.15","K");
        
        check("°C","-","°C","ð°C");
        check("°C","-","°F","1.8+32","ð°F");
        check("°C","-","ð°C","°C");
        check("°C","-","ð°F","1.8+32","°F");
        check("°C","-","K","1+273.15","K");
        
        check("°C","·","°C",Unit.ERR);
        check("°C","·","°F",Unit.ERR);
        check("°C","·","ð°C",Unit.ERR);
        check("°C","·","ð°F",Unit.ERR);
        check("°C","·","K",Unit.ERR);
        
        check("°C","/","°C",Unit.ERR);
        check("°C","/","°F",Unit.ERR);
        check("°C","/","ð°C",Unit.ERR);
        check("°C","/","ð°F",Unit.ERR);
        check("°C","/","K",Unit.ERR);
        
        check("°C","->","°C","°C");
        check("°C","->","°F","1.8+32","°F");
        check("°C","->","ð°C","1+273.15","ð°C");
        check("°C","->","ð°F","1.8+491.67","ð°F");
        check("°C","->","K","1+273.15","K");
        
        check("°F","+","°C",Unit.ERR);
        check("°F","+","°F",Unit.ERR);
        check("°F","+","ð°C","0.5555555555555556-17.77777777777778","°C");
        check("°F","+","ð°F","°F");
        check("°F","+","K","0.5555555555555556+255.3722222222222","K");
        
        check("°F","-","°C","0.5555555555555556-17.77777777777778","ð°C");
        check("°F","-","°F","ð°F");
        check("°F","-","ð°C","0.5555555555555556-17.77777777777778","°C");
        check("°F","-","ð°F","°F");
        check("°F","-","K","0.5555555555555556+255.3722222222222","K");

        check("°F","·","°C",Unit.ERR);
        check("°F","·","°F",Unit.ERR);
        check("°F","·","ð°C",Unit.ERR);
        check("°F","·","ð°F",Unit.ERR);
        check("°F","·","K",Unit.ERR);

        check("°F","/","°C",Unit.ERR);
        check("°F","/","°F",Unit.ERR);
        check("°F","/","ð°C",Unit.ERR);
        check("°F","/","ð°F",Unit.ERR);
        check("°F","/","K",Unit.ERR);

        check("°F","->","°C","0.5555555555555556-17.77777777777778","°C");
        check("°F","->","°F","°F");
        check("°F","->","ð°C","0.5555555555555556+255.3722222222222","ð°C");
        check("°F","->","ð°F","1+459.67","ð°F");
        check("°F","->","K","0.5555555555555556+255.3722222222222","K");

        check("ð°C","+","°C","°C");
        check("ð°C","+","°F","1.8","°F");
        check("ð°C","+","ð°C","ð°C");
        check("ð°C","+","ð°F","1.8","ð°F");
        check("ð°C","+","K","K");
        
        check("ð°C","-","°C",Unit.ERR);
        check("ð°C","-","°F",Unit.ERR);
        check("ð°C","-","ð°C","ð°C");
        check("ð°C","-","ð°F","1.8","ð°F");
        check("ð°C","-","K","K");
        
        check("ð°C","·","°C",Unit.ERR);
        check("ð°C","·","°F",Unit.ERR);
        check("ð°C","·","ð°C","ð°C²");
        check("ð°C","·","ð°F","1.8","ð°F²");
        check("ð°C","·","K","K²");
        
        check("ð°C","/","°C",Unit.ERR);
        check("ð°C","/","°F",Unit.ERR);
        check("ð°C","/","ð°C","");
        check("ð°C","/","ð°F","1.8","");
        check("ð°C","/","K","");
        
        check("ð°C","->","°C","1-273.15","°C");
        check("ð°C","->","°F","1.8-459.67","°F");
        check("ð°C","->","ð°C","ð°C");
        check("ð°C","->","ð°F","1.8","ð°F");
        check("ð°C","->","K","K");
        
        check("ð°F","+","°C","0.5555555555555556","°C");
        check("ð°F","+","°F","°F");
        check("ð°F","+","ð°F","ð°F");
        check("ð°F","+","ð°C","0.5555555555555556","ð°C");
        check("ð°F","+","K","0.5555555555555556","K");

        check("ð°F","-","°C",Unit.ERR);
        check("ð°F","-","°F",Unit.ERR);
        check("ð°F","-","ð°F","ð°F");
        check("ð°F","-","ð°C","0.5555555555555556","ð°C");
        check("ð°F","-","K","0.5555555555555556","K");

        check("ð°F","·","°C",Unit.ERR);
        check("ð°F","·","°F",Unit.ERR);
        check("ð°F","·","ð°F","ð°F²");
        check("ð°F","·","ð°C","0.5555555555555556","ð°C²");
        check("ð°F","·","K","0.5555555555555556","K²");

        check("ð°F","/","°C",Unit.ERR);
        check("ð°F","/","°F",Unit.ERR);
        check("ð°F","/","ð°F","");
        check("ð°F","/","ð°C","0.5555555555555556","");
        check("ð°F","/","K","0.5555555555555556","");

        check("ð°F","->","°C","0.5555555555555556-273.15","°C");
        check("ð°F","->","°F","1-459.67","°F");
        check("ð°F","->","ð°F","ð°F");
        check("ð°F","->","ð°C","0.5555555555555556","ð°C");
        check("ð°F","->","K","0.5555555555555556","K");

        check("K","+","°C","°C");
        check("K","+","°F","1.8","°F");
        check("K","+","ð°C","ð°C");
        check("K","+","ð°F","1.8","ð°F");
        check("K","+","K","K");

        check("K","-","°C","1-273.15","ð°C");
        check("K","-","°F","1.8-459.67","ð°F");
        check("K","-","ð°C","ð°C");
        check("K","-","ð°F","1.8","ð°F");
        check("K","-","K","K");

        check("K","·","°C",Unit.ERR);
        check("K","·","°F",Unit.ERR);
        check("K","·","ð°C","ð°C²");
        check("K","·","ð°F","1.8","ð°F²");
        check("K","·","K","K²");

        check("K","/","°C",Unit.ERR);
        check("K","/","°F",Unit.ERR);
        check("K","/","ð°C","");
        check("K","/","ð°F","1.8","");
        check("K","/","K","");

        check("K","->","°C","1-273.15","°C");
        check("K","->","°F","1.8-459.67","°F");
        check("K","->","ð°C","ð°C");
        check("K","->","ð°F","1.8","ð°F");
        check("K","->","K","K");
    }
    
    public void testUnitCalculations() {
        check("W","/","m²","W/m²"); // Just checking that it does not come out kg/s³ 
        check("km","/","m","1000","");
        check("s","·","m","m·s");
        check("V","·","V","V²");
        check("c","·","c","c²");
        check("s","+","m",Unit.ERR);
        check("ft/s²","->","in","12","in/s²");
        check("J","->","g","1000","g·m²/s²");
        check("W","->","kJ","0.001","kJ/s");
        check("psi","6894.757293168361","Pa");
        check("Pa","0.0001450377377302092","psi");
        check("psi","0.06894757293168361","bar");
        check("bar","14.50377377302092","psi");
        check("psi","/","bar","0.06894757293168361","");
        check("bar","/","psi","14.50377377302092","");
        check("bar","+","psi","14.50377377302092","psi");
        check("a","->","m","100","m²");
        check("a","/","m²","100","");
        check("a","+","m²","100","m²");
        check("a","->","SI","100","m²");
        check("psi","->","SI","6894.757293168361","Pa");
        check("st","->","US/Imp","14","lb");
    }
    
    public void dont_testFurtherUnitCalculations() {
        // Still needs work: ... (if at all possible?)
        check("e","·","V","eV");
        check("V","·","e","eV");
        check("eV","/","V","e");
        check("eV","/","e","V");
    }

}
