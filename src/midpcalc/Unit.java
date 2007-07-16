package midpcalc;

//Units:
//  Mass:               kg   g t gr oz lb st (t) ton u
//                                                 st = 14 lb
//  Length:             m    Å mm cm km in ft yd mi NM AU ly pc
//  Time:               s    h min d y             y = 365.25 d
//  Charge:             C    e                     C = 6241509479607717888 e
//  Temperature:        K    °C °F
//  Amount:             mol                        mol = 6.0221415e23
//Derived units:
//  Area:               m²   a da ha acre          acre = 43560 ft²
//  Volume:             m³   l dl cl ml gal pt cup fl.oz (gal pt cup fl.oz)
//  Speed:              m/s  km/h mph knot c       knot = NM/h, c = ly/y
//  Acceleration:       m/s² ips² fps² mps² g_n
//  Force:              N    pdl lbf kgf           N = kg·m/s², pdl = lb·fps², lbf = g_n·lb = 4.4 N
//  Pressure:           Pa   bar atm psi mmHg pdl/ft²
//                                                 Pa = N/m² = kg/m·s², bar = 100000 Pa, atm = 101325 Pa, psi = lbf/in²
//  Energy:             J    kJ cal kcal Btu eV ft·lbf hp·h kW·h
//                                                 J = N·m = kg·m²/s²
//  Effect:             W    kW hp                 W = J/s = kg·m²/s³
//  Current:            A                          A = C/s
//  Potential:          V                          V = W/A = kg·m²/s²·C
//  Capasitance:        F                          F = C/V = s²·C²/kg·m²
//  Resistance:         Ohm                        Ohm = V/A = kg·m²/s·C²
//  Flux:               Wb                         Wb = V·s = kg·m²/s·C
//  Flux density:       T                          T = Wb/m² = kg/s·C
//  Inductance:         H                          H = Wb/A = kg·m²/C²

public final class Unit {

    static final int N_PRIMITIVE_UNITS = 12;
    static final int N_DERIVED_UNITS = 27;
    int[] power = new int[N_PRIMITIVE_UNITS];
    int[] unit = new int[N_PRIMITIVE_UNITS];

    boolean error;
    boolean overflow;

    private static final int NONE  = 0;
    private static final int SI    = 1;
    private static final int IMP   = 2;  // US/British/Imperial
    private static final int BASE  = 4;
    
    static final class UnitDesc {
        final String name;
        final int system;
        final Real conversionFactor;
        Unit convertsTo;
        boolean isComposite;
        
        UnitDesc(String name) {
            this(name, SI);
        }
        UnitDesc(String name, int system) {
            this(name, system, Real.ONE);
        }
        UnitDesc(String name, int system, Real conversionFactor) {
            this.name = name;
            this.system = system;
            this.conversionFactor = conversionFactor;
            this.convertsTo = null;
            this.isComposite = false;
        }
        
        UnitDesc kg(int power) {
            return unit(0,0,power);
        }
        UnitDesc m(int power) {
            return unit(1,0,power);
        }
        UnitDesc s(int power) {
            return unit(2,0,power);
        }
        UnitDesc C(int power) {
            return unit(3,0,power);
        }
        UnitDesc unit(int unitType, int unit, int power) {
            if (power == 0)
                return this;
            if (convertsTo == null) {
                convertsTo = new Unit();
                isComposite = power != 1;
            } else {
                isComposite = true;
            }
            convertsTo.power[unitType] = power;
            convertsTo.unit[unitType] = unit;
            return this;
        }
    }
    
    // NOTE! When re-ordering units, parameters to UnitDesc.unit() refer
    // to the order specified here
    
    // Primitive, simple (non-composite) units
    private static final UnitDesc[] massUnits = new UnitDesc[] {
        new UnitDesc("kg",  SI|BASE),
        new UnitDesc("g",   SI,       new Real(0, 0x3ffffff6, 0x4189374bc6a7ef9eL /*1e-3*/)).kg(1),
        new UnitDesc("t",   SI,       new Real(1000)).kg(1),
        new UnitDesc("gr",  IMP,      new Real(0, 0x3ffffff2, 0x43f253303203a99fL /*0.00006479891*/)).kg(1),
        new UnitDesc("oz",  IMP,      new Real(0, 0x3ffffffa, 0x741ea12add794261L /*0.028349523125*/)).kg(1),
        new UnitDesc("lb",  IMP|BASE, new Real(0, 0x3ffffffe, 0x741ea12add794261L /*0.45359237*/)).kg(1),
        new UnitDesc("st",  IMP,      new Real(0, 0x40000002, 0x659acd0581ca1a15L /*6.35029318*/)).kg(1),
        new UnitDesc("ton", IMP,      new Real(0, 0x40000009, 0x7165e963dc486ad3L /*907.18474*/)).kg(1),
        new UnitDesc("`t`", IMP,      new Real(0, 0x40000009, 0x7f018046e23ca09aL /*1016.0469088*/)).kg(1),
        new UnitDesc("u",   SI,       new Real(0, 0x3fffffa7, 0x41c7dd5a667f9950L /*1.66053886e-27*/)).kg(1),
    };
    private static final UnitDesc[] lengthUnits = new UnitDesc[] {
        new UnitDesc("m",   SI|BASE),
        new UnitDesc("Å",   SI,       new Real(0, 0x3fffffde, 0x6df37f675ef6eadfL /*1e-10*/)).m(1),
        new UnitDesc("mm",  SI,       new Real(0, 0x3ffffff6, 0x4189374bc6a7ef9eL /*1e-3*/)).m(1),
        new UnitDesc("cm",  SI,       Real.PERCENT).m(1),
        new UnitDesc("km",  SI,       new Real(1000)).m(1),
        new UnitDesc("in",  IMP,      new Real(0, 0x3ffffffa, 0x6809d495182a9931L /*0.0254*/)).m(1),
        new UnitDesc("ft",  IMP|BASE, new Real(0, 0x3ffffffe, 0x4e075f6fd21ff2e5L /*0.3048*/)).m(1),
        new UnitDesc("yd",  IMP,      new Real(0, 0x3fffffff, 0x750b0f27bb2fec57L /*0.9144*/)).m(1),
        new UnitDesc("mi",  IMP,      new Real(0, 0x4000000a, 0x6495810624dd2f1bL /*1609.344*/)).m(1),
        new UnitDesc("NM",  SI,       new Real(1852)).m(1),
        new UnitDesc("AU",  SI,       new Real(149597870691L)).m(1),
        new UnitDesc("ly",  SI,       new Real(9460730472580800L)).m(1),
        new UnitDesc("pc",  SI,       new Real(0, 0x40000036, 0x6da012f9404b0988L /*3.085677581305729e+16*/)).m(1),
    };
    private static final UnitDesc[] timeUnits = new UnitDesc[] {
        new UnitDesc("s",   SI|IMP|BASE),
        new UnitDesc("min", SI|IMP,   new Real(60)).s(1),
        new UnitDesc("h",   SI|IMP,   new Real(3600)).s(1),
        new UnitDesc("d",   SI|IMP,   new Real(86400)).s(1),
        new UnitDesc("y",   SI|IMP,   new Real(31557600)).s(1),
    };
    private static final UnitDesc[] chargeUnits = new UnitDesc[] {
        new UnitDesc("C",   SI|IMP|BASE),
        new UnitDesc("e",   SI|IMP,   new Real(0, 0x3fffffc1, 0x5e93683d3137633fL /*1.60217653e-19*/)).C(1),
    };
    private static final UnitDesc[] amountUnits = new UnitDesc[] {
        new UnitDesc("mol", SI|IMP|BASE),
    };
    private static final UnitDesc[] temperatureUnits = new UnitDesc[] {
        new UnitDesc("K",   SI|IMP|BASE),
        new UnitDesc("°C",  SI, Real.NAN).unit(5,0,1),
        new UnitDesc("°F",  IMP, Real.NAN).unit(5,0,1),
    };
    // Alternative primitive units (composite units with conversionFactor != 1)
    private static final UnitDesc[] alternativeAreaUnits = new UnitDesc[] {
        new UnitDesc("a",    SI,  Real.HUNDRED).m(2),
        new UnitDesc("da",   SI,  new Real(1000)).m(2),
        new UnitDesc("ha",   SI,  new Real(10000)).m(2),
        new UnitDesc("acre", IMP, new Real(0, 0x4000000b, 0x7e76d9f3fcbc7ea1L /*4046.8564224*/)).m(2),
    };
    private static final UnitDesc[] alternativeVolumeUnits = new UnitDesc[] {
        new UnitDesc("l",       SI,  new Real(0, 0x3ffffff6, 0x4189374bc6a7ef9eL /*1e-3*/)).m(3),
        new UnitDesc("ml",      SI,  new Real(0, 0x3fffffec, 0x431bde82d7b634dbL /*1e-6*/)).m(3),
        new UnitDesc("cl",      SI,  new Real(0, 0x3fffffef, 0x53e2d6238da3c212L /*1e-5*/)).m(3),
        new UnitDesc("dl",      SI,  new Real(0, 0x3ffffff2, 0x68db8bac710cb296L /*1e-4*/)).m(3),
        new UnitDesc("fl.oz",   IMP, new Real(0, 0x3ffffff0, 0x7c0a55e836d246a4L /*0.0000295735295625*/)).m(3),
        new UnitDesc("cup",     IMP, new Real(0, 0x3ffffff3, 0x7c0a55e836d246a4L /*0.0002365882365*/)).m(3),
        new UnitDesc("pt",      IMP, new Real(0, 0x3ffffff4, 0x7c0a55e836d246a4L /*0.000473176473*/)).m(3),
        new UnitDesc("gal",     IMP, new Real(0, 0x3ffffff7, 0x7c0a55e836d246a4L /*0.003785411784*/)).m(3),
        new UnitDesc("`fl.oz`", IMP, new Real(0, 0x3ffffff0, 0x772c4b265dd18634L /*0.0000284130625*/)).m(3),
        new UnitDesc("`cup`",   IMP, new Real(0, 0x3ffffff3, 0x772c4b265dd18634L /*0.0002273045*/)).m(3),
        new UnitDesc("`pt`",    IMP, new Real(0, 0x3ffffff5, 0x4a7baef7faa2f3e0L /*0.00056826125*/)).m(3),
        new UnitDesc("`gal`",   IMP, new Real(0, 0x3ffffff8, 0x4a7baef7faa2f3e0L /*0.00454609*/)).m(3),
    };
    private static final UnitDesc[] alternativeAccelerationUnits = new UnitDesc[] {
        new UnitDesc("`g`", NONE, new Real(0, 0x40000003, 0x4e7404ea4a8c154dL /*9.80665*/)).m(1).s(-2),
    };
    private static final UnitDesc[] alternativePressureUnits = new UnitDesc[] {
        new UnitDesc("bar",  SI, new Real(100000)).kg(1).m(-1).s(-2),
        new UnitDesc("atm",  SI, new Real(101325)).kg(1).m(-1).s(-2),
        new UnitDesc("mmHg", SI, new Real(0, 0x40000007, 0x42a943fda60892ccL /*133.322387415*/)).kg(1).m(-1).s(-2),
    };
    private static final UnitDesc[] alternativeEnergyUnits = new UnitDesc[] {
        new UnitDesc("kJ",   SI,  new Real(1000)).kg(1).m(2).s(-2),
        new UnitDesc("cal",  SI,  new Real(0, 0x40000002, 0x42fd21ff2e48e8a7L /*4.1868*/)).kg(1).m(2).s(-2),
        new UnitDesc("kcal", SI,  new Real(0, 0x4000000c, 0x416b333333333333L /*4186.8*/)).kg(1).m(2).s(-2),
        new UnitDesc("Btu",  IMP, new Real(0, 0x4000000a, 0x41f0e4c5b784bc12L /*1055.05585262*/)).kg(1).m(2).s(-2),
        new UnitDesc("eV",   SI,  new Real(0, 0x3fffffc1, 0x5e93683d3137633fL /*1.60217653e-19*/)).kg(1).m(2).s(-2),
    };
    private static final UnitDesc[] alternativePowerUnits = new UnitDesc[] {
        new UnitDesc("kW", SI,  new Real(1000)).kg(1).m(2).s(-3),
        new UnitDesc("MW", SI,  new Real(1000000)).kg(1).m(2).s(-3),
        new UnitDesc("GW", SI,  new Real(1000000000)).kg(1).m(2).s(-3),
        new UnitDesc("hp", IMP, new Real(0, 0x40000009, 0x5d36655916a80304L /*745.69987158227022*/)).kg(1).m(2).s(-3),
    };
    // Derived (non-primitive) units (composite units with conversionFactor == 1)
    private static final UnitDesc[] derivedSpeedUnits = new UnitDesc[] {
        new UnitDesc("mph",  IMP).unit(1,8,1).unit(2,2,-1),  // mi/h
        new UnitDesc("knot").unit(1,9,1).unit(2,2,-1),       // NM/h
        new UnitDesc("c").unit(1,11,1).unit(2,4,-1),         // ly/y
    };
    private static final UnitDesc[] derivedAccelerationUnits = new UnitDesc[] {
        new UnitDesc("ips²", IMP).unit(1,5,1).s(-2), // in/s²
        new UnitDesc("fps²", IMP).unit(1,6,1).s(-2), // ft/s²
        new UnitDesc("mps²", IMP).unit(1,8,1).s(-2), // mi/s²
    };
    private static final UnitDesc[] derivedForceUnits = new UnitDesc[] {
        new UnitDesc("N").kg(1).m(1).s(-2),
        new UnitDesc("pdl", IMP).unit(0,5,1).unit(1,6,1).s(-2), // lb·ft/s²
    };
    private static final UnitDesc[] derivedAlternativeForceUnits = new UnitDesc[] {
        new UnitDesc("kgf").kg(1).unit(8,0,1),            // kg·`g`
        new UnitDesc("lbf", IMP).unit(0,5,1).unit(8,0,1), // lb·`g`
    };
    private static final UnitDesc[] derivedPressureUnits = new UnitDesc[] {
        new UnitDesc("Pa").kg(1).m(-1).s(-2),
    };
    private static final UnitDesc[] derivedAlternativePressureUnits = new UnitDesc[] {
        new UnitDesc("psi", IMP).unit(0,5,1).unit(8,0,1).unit(1,5,-2), // lb·`g`/in²
    };
    private static final UnitDesc[] derivedEnergyUnits = new UnitDesc[] {
        new UnitDesc("J").kg(1).m(2).s(-2),
    };
    private static final UnitDesc[] derivedPowerUnits = new UnitDesc[] {
        new UnitDesc("W").kg(1).m(2).s(-3),
    };
    private static final UnitDesc[] derivedCurrentUnits = new UnitDesc[] {
        new UnitDesc("A").C(1).s(-1),
    };
    private static final UnitDesc[] derivedPotentialUnits = new UnitDesc[] {
        new UnitDesc("V").kg(1).m(2).s(-2).C(-1),
    };
    private static final UnitDesc[] derivedCapasitanceUnits = new UnitDesc[] {
        new UnitDesc("F").s(2).C(2).kg(-1).m(-2),
    };
    private static final UnitDesc[] derivedResistanceUnits = new UnitDesc[] {
        new UnitDesc("Ø"/*Ohm*/).kg(1).m(2).s(-1).C(-2),
    };
    private static final UnitDesc[] derivedFluxUnits = new UnitDesc[] {
        new UnitDesc("Wb").kg(1).m(2).s(-1).C(-1),
    };
    private static final UnitDesc[] derivedFluxDensityUnits = new UnitDesc[] {
        new UnitDesc("T").kg(1).s(-1).C(-1),
    };
    private static final UnitDesc[] derivedInductanceUnits = new UnitDesc[] {
        new UnitDesc("H").kg(1).m(2).C(-2),
    };
    private static final UnitDesc[] helperCompositeUnits = new UnitDesc[] {
        new UnitDesc("m²").m(2),
        new UnitDesc("m³").m(3),
        new UnitDesc("m/s").m(1).s(-1),
        new UnitDesc("km/h").unit(1,4,1).unit(2,2,-1),
        new UnitDesc("m/s²").m(1).s(-2),
        new UnitDesc("in²").unit(1,5,2),
        new UnitDesc("ft²").unit(1,6,2),
        new UnitDesc("yd²").unit(1,7,2),
        new UnitDesc("mi²").unit(1,8,2),
    };
    static final UnitDesc[][] allUnits = new UnitDesc[][] {
        massUnits,                       // 0
        lengthUnits,                     // 1
        timeUnits,                       // 2
        chargeUnits,                     // 3
        amountUnits,                     // 4
        temperatureUnits,                // 5
        alternativeAreaUnits,            // 6
        alternativeVolumeUnits,          // 7
        alternativeAccelerationUnits,    // 8
        alternativePressureUnits,        // 9
        alternativeEnergyUnits,          // 10
        alternativePowerUnits,           // 11
        // Derived
        derivedSpeedUnits,               // 12
        derivedAccelerationUnits,        // 13
        derivedForceUnits,               // 14
        derivedAlternativeForceUnits,    // 15
        derivedPressureUnits,            // 16
        derivedAlternativePressureUnits, // 17
        derivedEnergyUnits,              // 18
        derivedPowerUnits,               // 19
        derivedCurrentUnits,             // 20
        derivedPotentialUnits,           // 21
        derivedCapasitanceUnits,         // 22
        derivedResistanceUnits,          // 23
        derivedFluxUnits,                // 24
        derivedFluxDensityUnits,         // 25
        derivedInductanceUnits,          // 26
        helperCompositeUnits  // Used only indirectly
    };
    static final int simplePrimitiveUnits[] = new int [] {
        0,1,2,3,4,5
    };
    static final int compositePrimitiveUnits[] = new int [] {
        6,7,8,9,10,11
    };
    
    private static final class DerivedUnit {
        int[] power = new int[N_DERIVED_UNITS];
        int[] unit = new int[N_DERIVED_UNITS];
        int complexity;
        
        // Order in which units appear in the output string
        private static final int unitTypeOrder[] = new int[] {
            //  N  N  A C  V  F Ohm Wb  H  J  J  W  W Pa Pa Pa kg m m² m³ m/s g  g s mol K  T
               14,15,20,3,21,22, 23,24,26,18,10,19,11,16,17, 9, 0,1, 6, 7, 12,8,13,2,  4,5,25
        };
        // Relative penalty for making use of each unit
        private static final int oddness[] = new int[] {
            // kg m s C mol K m² m³ g Pa J W m/s g N N Pa Pa J W A V F Ohm Wb T H
                0,0,0,2,  0,0, 1, 1,2, 4,3,3,  2,2,3,3, 4, 4,3,3,1,3,4,  3, 4,3,4
        };
        // Dimensionality limit for "natural use" of each unit
        private static final int dimensionLimit[] = new int[] {
            // kg m s A mol K m² m³ g Pa J W m/s g N N Pa Pa J W C V F Ohm Wb T H
                1,3,2,1,  1,1, 1, 1,1, 1,1,1,  1,1,1,1, 1, 1,1,1,1,1,1,  1, 1,1,1
        };
        static final String powerStr[] = new String[] {
            "", "", "²", "³", "¼"/*^4*/
        };

        DerivedUnit() {
        }
        
        void init(Unit a) {
            for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
                power[unitType] = a.power[unitType];
                unit[unitType] = a.unit[unitType];
            }
            for (int unitType=N_PRIMITIVE_UNITS; unitType<N_DERIVED_UNITS; unitType++) {
                power[unitType] = unit[unitType] = 0;
            }
            calcComplexity();
        }
        
        void copy(DerivedUnit a) {
            for (int unitType=0; unitType<N_DERIVED_UNITS; unitType++) {
                power[unitType] = a.power[unitType];
                unit[unitType] = a.unit[unitType];
            }
            complexity = a.complexity;
        }
        
        void calcComplexity() {
            complexity = 0;
            boolean started = false;
            for (int unitType=0; unitType<N_DERIVED_UNITS; unitType++) {
                if (power[unitType] != 0) {
                    int absUnit = Math.abs(power[unitType]);
                    complexity +=
                        1 + absUnit + (started ? 1 : 0) + oddness[unitType] +
                        (absUnit > dimensionLimit[unitType] ? 3 : 0) +
                        (absUnit >= powerStr.length ? 50 : 0);
                    started = true;
                }
            }
        }
        
        int reduce(Unit reduceBy, int reduceByUnitType, int reduceByUnit) {
            int oldComplexity = complexity;
            
            if (power[reduceByUnitType] != 0 && unit[reduceByUnitType] != reduceByUnit)
                return 0;
            for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
                if (power[unitType] != 0 && reduceBy.power[unitType] != 0 && unit[unitType] != reduceBy.unit[unitType])
                    return 0;
            }

            for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
                power[unitType] -= reduceBy.power[unitType];
            }
            power[reduceByUnitType] ++;
            unit[reduceByUnitType] = reduceByUnit;
            calcComplexity();
            if (complexity < oldComplexity)
                return 1;

            for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
                power[unitType] += 2*reduceBy.power[unitType];
            }
            power[reduceByUnitType] -= 2;
            calcComplexity();
            if (complexity < oldComplexity)
                return -1;
            
            for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
                power[unitType] -= reduceBy.power[unitType];
            }
            power[reduceByUnitType] ++;
            complexity = oldComplexity;
            return 0;
        }
        
        void unReduce(Unit reduceBy, int reduceByUnitType, int reduceByUnit, int fac) {
            for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
                power[unitType] += reduceBy.power[unitType]*fac;
            }
            power[reduceByUnitType] -= fac;
            calcComplexity();
        }

        StringBuffer toStringBuf = new StringBuffer();

        public String toString() {
            boolean allMinusOne = true;
            boolean someNegative = false;
            for (int i=0; i<N_DERIVED_UNITS; i++) {
                int unitType = unitTypeOrder[i];
                if (power[unitType] > 0 || power[unitType] < -1)
                    allMinusOne = false;
                if (power[unitType] < 0)
                    someNegative = true;
                if (Math.abs(power[unitType]) >= powerStr.length) {
                    return "[ofl]";
                }
            }
            
            toStringBuf.setLength(0);
            boolean started = false;
            if (allMinusOne) {
                for (int i=0; i<N_DERIVED_UNITS; i++) {
                    int unitType = unitTypeOrder[i];
                    if (power[unitType] == -1) {
                        if (started)
                            toStringBuf.append('·');
                        started = true;
                        toStringBuf.append(allUnits[unitType][unit[unitType]].name);
                        toStringBuf.append('¹');
                    }
                }
                return toStringBuf.toString();
            }
            
            for (int i=0; i<N_DERIVED_UNITS; i++) {
                int unitType = unitTypeOrder[i];
                if (power[unitType] > 0) {
                    if (started)
                        toStringBuf.append('·');
                    started = true;
                    toStringBuf.append(allUnits[unitType][unit[unitType]].name);
                    toStringBuf.append(powerStr[power[unitType]]);
                }
            }
            if (someNegative) {
                //if (!started)
                //    toStringBuf.append("1");
                toStringBuf.append("/");
                started = false;
                for (int i=0; i<N_DERIVED_UNITS; i++) {
                    int unitType = unitTypeOrder[i];
                    if (power[unitType] < 0) {
                        if (started)
                            toStringBuf.append('·');
                        started = true;
                        toStringBuf.append(allUnits[unitType][unit[unitType]].name);
                        toStringBuf.append(powerStr[-power[unitType]]);
                    }
                }
            }
            return toStringBuf.toString();
        }
    }
    
    public Unit() {
    }
    
    public Unit(int unitType, int unit) {
        if (unitType < N_PRIMITIVE_UNITS) {
            this.power[unitType] = 1;
            this.unit[unitType] = unit;
        } else {
            copy(allUnits[unitType][unit].convertsTo);
        }
    }

    public Unit(Unit a) {
        copy(a);
    }

    public Unit(int mass, int length, int time, int charge, int amount, int temp) {
        power[0] = mass;
        power[1] = length;
        power[2] = time;
        power[3] = charge;
        power[4] = amount;
        power[5] = temp;
    }
    
    public Unit copy(Unit a) {
        for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
            power[unitType] = a.power[unitType];
            unit[unitType] = a.unit[unitType];
        }
        error = a.error;
        overflow = a.overflow;
        return this;
    }

    public void unity() {
        for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
            power[unitType] = 0;
        }
        error    = false;
        overflow = false;
    }
    
    Unit kg(int power) {
        return unit(0,0,power);
    }
    Unit m(int power) {
        return unit(1,0,power);
    }
    Unit s(int power) {
        return unit(2,0,power);
    }
    Unit C(int power) {
        return unit(3,0,power);
    }
    Unit mol(int power) {
        return unit(4,0,power);
    }
    Unit K(int power) {
        return unit(5,0,power);
    }
    Unit J(int power) {
        return kg(power).m(2*power).s(-2*power);
    }
    Unit unit(int unitType, int unit, int power) {
        this.power[unitType] += power;
        this.unit[unitType] = unit;
        return this;
    }

    private static final int[] bitsPerPower = new int[] {
        2,4,4,3,2,2,2,2,2,2,2,2
    };
    private static final int[] bitsPerUnit = new int[] {
        4,4,3,2,1,2,3,4,1,3,3,3
    };
    private static final int[] bitMask = new int[] {
        0,1,3,7,15,
    };

    public void unpack(long a) {
        for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
            int pBits = bitsPerPower[unitType];
            int uBits = bitsPerUnit[unitType];
            int p = (int)a & bitMask[pBits];
            a >>= pBits;
            int u = (int)a & bitMask[uBits];
            a >>= uBits;
            if (u == 0) {
                power[unitType] = unit[unitType] = 0;
            } else {
                unit[unitType] = u-1;
                p = (p<<(32-pBits))>>(32-pBits);
                if (p >= 0)
                    p++;
                power[unitType] = p;
            }
        }
        error = (a & 1) != 0;
        overflow = (a & 2) != 0;
    }
    
    public long pack() {
        long result = 0;
        int pos = 0;
        for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
            int pBits = bitsPerPower[unitType];
            int uBits = bitsPerUnit[unitType];
            int p = power[unitType];
            if (p != 0) {
                if (p > 0)
                    p--;
                int bits = p & bitMask[pBits];
                result += (long)bits << pos;
                if (p != (bits<<(32-pBits))>>(32-pBits))
                    error = overflow = true;
                result += (long)(unit[unitType]+1) << (pos+pBits);
            }
            pos += pBits + uBits;
        }
        return result +
               ((error    ? 1L:0) << 62) +
               ((overflow ? 1L:0) << 63);
    }

    public static long unitError =  1L<< 62;

    boolean isUnity() {
        if (error) 
            return false;
        for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
            if (power[unitType] != 0)
                return false;
        }
        return true;
    }
    
    private int dominantSystem() {
        int nSI=0;
        int nIMP=0;
        for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
            if (power[unitType] != 0) {
                int system = allUnits[unitType][unit[unitType]].system;
                if ((system & SI) != 0 ) {
                    nSI += power[unitType];
                } 
                if ((system & IMP) != 0) {
                    nIMP += power[unitType];
                }
            }
        }
        return nSI >= nIMP ? SI : IMP;
    }

    private int baseUnit(int unitType, int system) {
        if (system != SI) {
            // Find imperial base unit
            for (int unit=0; unit<allUnits[unitType].length; unit++) {
                if ((allUnits[unitType][unit].system & (IMP|BASE)) == (IMP|BASE))
                    return unit;
            }
        }
        return 0;
    }

    private boolean decompose(int compositeType, int dominantSystem,
                              Real conversionFactor) {
        UnitDesc convertFrom = allUnits[compositeType][unit[compositeType]];
        if (power[compositeType] == 0) {
            return false;
        }
        rTmp.assign(convertFrom.conversionFactor);
        if (power[compositeType] != 1)
            rTmp.pow(power[compositeType]);
        conversionFactor.mul(rTmp);
        for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
            if (convertFrom.convertsTo.power[unitType] != 0) {
                int pInc = convertFrom.convertsTo.power[unitType]*power[compositeType];
                if (power[unitType] == 0)
                    unit[unitType] = baseUnit(unitType, dominantSystem);
                if (unit[unitType] != 0) {
                    rTmp.assign(allUnits[unitType][unit[unitType]].conversionFactor);
                    if (pInc != 1)
                        rTmp.pow(pInc);
                    conversionFactor.div(rTmp);
                }
                power[unitType] += pInc;
            }
        }
        power[compositeType] = 0;
        return true;
    }
    
    private boolean compose(int compositeType, int compositeUnit,
                            Real conversionFactor) {
        UnitDesc convertTo = allUnits[compositeType][compositeUnit];
        int maxFactor = -100;
        int minFactor = 100;
        for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
            if (convertTo.convertsTo.power[unitType] != 0) {
                int factor = power[unitType]/convertTo.convertsTo.power[unitType];
                if (factor > maxFactor) maxFactor = factor;
                if (factor < minFactor) minFactor = factor;
            }
        }
        int factor = 0;
        if (minFactor > 0)
            factor = minFactor;
        if (maxFactor < 0)
            factor = maxFactor;
        if (factor == 0)
            return false;

        for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
            if (convertTo.convertsTo.power[unitType] != 0) {
                int pInc = convertTo.convertsTo.power[unitType]*factor;
                if (unit[unitType] != 0) {
                    rTmp.assign(allUnits[unitType][unit[unitType]].conversionFactor);
                    if (pInc != 1)
                        rTmp.pow(pInc);
                    conversionFactor.mul(rTmp);
                }
                power[unitType] -= pInc;
            }
        }

        rTmp.assign(convertTo.conversionFactor);
        if (factor != 1)
            rTmp.pow(factor);
        conversionFactor.div(rTmp);
        power[compositeType] += factor;
        unit[compositeType] = compositeUnit;
        return true;
    }
    
    private boolean isCompatibleWith(Unit a) {
        for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
            if (power[unitType] != 0 && a.power[unitType] != 0 && unit[unitType] != a.unit[unitType])
                return false;
        }
        return true;
    }
    
    private boolean isSameUnit(Unit a) {
        for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
            if (power[unitType] != a.power[unitType] ||
                (power[unitType] != 0 && unit[unitType] != a.unit[unitType]))
                return false;
        }
        return false;
    }
    
    private boolean handleErrorUnary() {
        if (error) {
            unity();
            return true;
        }
        return false;
    }
    
    private boolean handleErrorBinary(Unit a) {
        if ((error && a.isUnity()) || (isUnity() && a.error)) {
            unity();
            return true;
        }
        if (error || a.error) {
            error = true;
            return true;
        }
        return false;
    }
    
    public static long undefinedUnaryOperation(long a) {
        if (a == 0 || (a & unitError) != 0) {
            return 0;
        }
        return unitError;
    }

    public static long undefinedBinaryOperation(long a, long b) {
        if ((a == 0 || (a & unitError) != 0) && (b == 0 || (b & unitError) != 0)) {
            return 0;
        }
        return unitError;
    }

    private static Unit uTmp1 = new Unit();
    private static Unit uTmp2 = new Unit();
    private static Real rTmp = new Real();
    
    public void convertTo(Unit a, Real conversionFactor) {
        conversionFactor.assign(Real.ONE);
        a = uTmp2.copy(a);
        if (handleErrorBinary(a))
            return;

        int dominantSystem = a.dominantSystem();

        // For all unit types present in both this and a, convert to a's unit
        for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
            if (power[unitType] != 0 && a.power[unitType] != 0 && unit[unitType] != a.unit[unitType]) {
                // Unit type present in both this and a, but not same unit
                rTmp.assign(allUnits[unitType][unit[unitType]].conversionFactor);
                rTmp.div(allUnits[unitType][a.unit[unitType]].conversionFactor);
                if (power[unitType] != 1)
                    rTmp.pow(power[unitType]);
                conversionFactor.mul(rTmp);
                unit[unitType] = a.unit[unitType];
            }
        }
        
        // For all simple unit types only present in a, 
        // decompose all composites not present in a composed of this unit
        for (int i=0; i<simplePrimitiveUnits.length; i++) {
            int simple = simplePrimitiveUnits[i];
            if (power[simple] == 0 && a.power[simple] != 0) {
                // Simple unit type only present in a
                for (int j=0; j<compositePrimitiveUnits.length; j++) {
                    int composite = compositePrimitiveUnits[j]; 
                    if (power[composite] != 0 && a.power[composite] == 0) {
                        // Composite unit type only present in this
                        Unit composedOf = allUnits[composite][unit[composite]].convertsTo;
                        if (composedOf.power[simple] != 0) {
                            // Composite unit composed of simple unit
                            decompose(composite, dominantSystem, conversionFactor);
                        }
                    }
                }
            }
        }
        
        // For all composite unit types only present in a,
        // build up as many composites as possible from simple units not in a
        compositeUnits:
        for (int i=0; i<compositePrimitiveUnits.length; i++) {
            int composite = compositePrimitiveUnits[i];
            if (power[composite] == 0 && a.power[composite] != 0) {
                // Composite unit only present in a
                Unit composedOf = allUnits[composite][a.unit[composite]].convertsTo;
                for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
                    if (composedOf.power[unitType] != 0) {
                        if (power[unitType] == 0 || a.power[unitType] != 0)
                            continue compositeUnits;
                    }
                }
                // Composite unit type only present in this
                compose(composite, a.unit[composite], conversionFactor);
            }
        }
        
        // Attempt to achieve the same balance of simple and composite
        // units as in a
        
        // ...later
    }

    public static long convertTo(long a, long b, Real convertA) {
        uTmp1.unpack(a);
        uTmp2.unpack(b);
        uTmp1.convertTo(uTmp2, convertA);
        return uTmp1.pack();
    }
    
    public void add(Unit a, Real conversionFactor) {
        conversionFactor.assign(Real.ONE);
        a = uTmp2.copy(a);
        if (handleErrorBinary(a))
            return;
        if (!isSameUnit(a)) {
            convertTo(a, conversionFactor);
            if (!isSameUnit(a)) {
                conversionFactor.assign(Real.ONE);
                error = true;
            }
        }
    }

    public static long add(long a, long b, Real convertA) {
        uTmp1.unpack(a);
        uTmp2.unpack(b);
        uTmp1.add(uTmp2, convertA);
        return uTmp1.pack();
    }
    
    public void mul(Unit a, Real conversionFactor) {
        conversionFactor.assign(Real.ONE);
        a = uTmp2.copy(a);
        if (handleErrorBinary(a))
            return;
        if (!isCompatibleWith(a)) {
            convertTo(a, conversionFactor);
        }
        for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
            if (a.power[unitType] != 0) {
                power[unitType] += a.power[unitType];
                unit[unitType] = a.unit[unitType];
            }
        }
    }

    public static long mul(long a, long b, Real convertA) {
        uTmp1.unpack(a);
        uTmp2.unpack(b);
        uTmp1.mul(uTmp2, convertA);
        return uTmp1.pack();
    }
    
    public void div(Unit a, Real conversionFactor) {
        conversionFactor.assign(Real.ONE);
        a = uTmp2.copy(a);
        if (handleErrorBinary(a))
            return;
        if (!isCompatibleWith(a)) {
            convertTo(a, conversionFactor);
        }
        for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
            if (a.power[unitType] != 0) {
                power[unitType] -= a.power[unitType];
                unit[unitType] = a.unit[unitType];
            }
        }
    }
    
    public static long div(long a, long b, Real convertA) {
        uTmp1.unpack(a);
        uTmp2.unpack(b);
        uTmp1.div(uTmp2, convertA);
        return uTmp1.pack();
    }
    
    public void recip() {
        if (handleErrorUnary())
            return;
        for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
            power[unitType] = -power[unitType];
        }
    }

    public static long recip(long a) {
        uTmp1.unpack(a);
        uTmp1.recip();
        return uTmp1.pack();
    }

    public void sqrt(Real conversionFactor) {
        nroot(2,conversionFactor);
    }

    public static long sqrt(long a, Real convertA) {
        uTmp1.unpack(a);
        uTmp1.sqrt(convertA);
        return uTmp1.pack();
    }

    public void pow(int a) {
        if (handleErrorUnary())
            return;
        for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
            power[unitType] *= a;
        }
    }
    
    public static long pow(long a, int b) {
        uTmp1.unpack(a);
        uTmp1.pow(b);
        return uTmp1.pack();
    }

    public void nroot(int a, Real conversionFactor) {
        conversionFactor.assign(Real.ONE);
        if (handleErrorUnary())
            return;
        int dominantSystem = -1;
        for (int i=0; i<compositePrimitiveUnits.length; i++) {
            int unitType = compositePrimitiveUnits[i];
            if ((power[unitType] % a) != 0) {
                if (dominantSystem == -1)
                    dominantSystem = dominantSystem();
                decompose(unitType, dominantSystem, conversionFactor);
            }
        }
        for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
            if ((power[unitType] % a) != 0) {
                conversionFactor.assign(Real.ONE);
                error = true;
                return;
            }
            power[unitType] /= a;
        }
    }
    
    public static long nroot(long a, int b, Real convertA) {
        uTmp1.unpack(a);
        uTmp1.nroot(b, convertA);
        return uTmp1.pack();
    }

    private DerivedUnit reducedUnit = new DerivedUnit();
    private DerivedUnit bestReducedUnit = new DerivedUnit();
    
    private void reduce() {
        for (int unitType=N_PRIMITIVE_UNITS; unitType<N_DERIVED_UNITS; unitType++) {
            for (int unit=0; unit<allUnits[unitType].length; unit++) {
                int fac = reducedUnit.reduce(allUnits[unitType][unit].convertsTo, unitType,unit);
                if (fac != 0) {
                    if (reducedUnit.complexity < bestReducedUnit.complexity) {
                        bestReducedUnit.copy(reducedUnit);
                    }
                    reduce();
                    reducedUnit.unReduce(allUnits[unitType][unit].convertsTo, unitType,unit, fac);
                }
            }
        }
    }

    public String toString() {
        return toString(true);
    }

    public String toString(boolean reduce) {
        if (overflow)
            return "[ofl]";
        if (error)
            return "[err]";
        if (isUnity())
            return "";
        reducedUnit.init(this);
        bestReducedUnit.copy(reducedUnit);
        if (reduce)
            reduce();
        return bestReducedUnit.toString();
    }
    
    public static String toString(long a) {
        return toString(a,true);
    }

    public static String toString(long a, boolean reduce) {
        uTmp1.unpack(a);
        return uTmp1.toString(reduce);
    }
    
    private static void check(Unit unit, String s) {
        Unit u = new Unit();
        u.unpack(unit.pack());
        String us = u.toString(); 
        if (!us.equals(s))
            throw new IllegalStateException("Unit "+unit+" toString="+us+", expected="+s);
    }

    public static void main(String[] args) {
        for (int unitType=0; unitType<N_DERIVED_UNITS; unitType++) {
            for (int unit=0; unit<allUnits[unitType].length; unit++) {
                Unit aUnit = new Unit(unitType, unit);
                check(aUnit, allUnits[unitType][unit].name);
                System.out.print(toString(aUnit.pack()));
                if (!allUnits[unitType][unit].conversionFactor.equalTo(Real.ONE)) {
                    if (!allUnits[unitType][unit].conversionFactor.isNan())
                        System.out.print(" = "+allUnits[unitType][unit].conversionFactor+" "+
                                allUnits[unitType][unit].convertsTo);
                    else
                        System.out.print(" (nonlinear conversion)");
                } else if (allUnits[unitType][unit].convertsTo != null) {
                    System.out.print(" = "+allUnits[unitType][unit].convertsTo.toString(false));
                } else {
                    System.out.print(" (base SI unit)");
                }
                System.out.println();
            }
            System.out.println();
        }
        for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
            int power = Math.min(1<<(bitsPerPower[unitType]-1),4);
            check(new Unit().unit(unitType,0,power), allUnits[unitType][0].name+DerivedUnit.powerStr[power]);
            check(new Unit().unit(unitType,0,-1), allUnits[unitType][0].name+"¹");
            check(new Unit().unit(unitType,0,-power), "/"+allUnits[unitType][0].name+DerivedUnit.powerStr[power]);
        }
        check(new Unit().kg(1).s(-3), "W/m²");
    }
}
