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
//  Acceleration:       m/s² in/s² ft/s² mi/s² g_n
//  Force:              N    pdl lbf kgf           N = kg·m/s², pdl = lb·ft/s², lbf = g_n·lb = 4.4 N
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

    static final int N_SIMPLE_PRIMITIVE_UNITS = 6;
    static final int N_PRIMITIVE_UNITS = 12;
    static final int N_DERIVED_UNITS = 26;
    static final int TEMP_TYPE = 5; // Temperature unit type index is 5

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
        final String nameNormalText;
        final int system;
        final Real conversionFactor;
        final Real conversionOffset;
        final Unit convertsTo;
        
        UnitDesc(String name, int system) {
            this(name, system, null);
        }
        UnitDesc(String name, Unit convertsTo) {
            this(name, SI, convertsTo);
        }
        UnitDesc(String name, int system, Unit convertsTo) {
            this(name, system, convertsTo, Real.ONE);
        }
        UnitDesc(String name, String nameNormalText, int system, Unit convertsTo) {
            this(name, nameNormalText, system, convertsTo, Real.ONE, Real.ZERO);
        }
        UnitDesc(String name, int system, Unit convertsTo, Real conversionFactor) {
            this(name, null, system, convertsTo, conversionFactor, Real.ZERO);
        }
        UnitDesc(String name, int system, Unit convertsTo,
                 Real conversionFactor, Real conversionOffset) {
            this(name, null, system, convertsTo, conversionFactor, conversionOffset);
        }
        UnitDesc(String name, String nameNormalText, int system,
                 Unit convertsTo, Real conversionFactor, Real conversionOffset) {
            this.name = name;
            this.nameNormalText = nameNormalText;
            this.system = system;
            this.conversionFactor = conversionFactor;
            this.conversionOffset = conversionOffset;
            this.convertsTo = convertsTo;
        }
    }

    // Predefined units
    private static final Unit kg   = u().kg(1);
    private static final Unit m    = u().m(1);
    private static final Unit s    = u().s(1);
    private static final Unit C    = u().C(1);
    private static final Unit K    = u().C(1);
    private static final Unit m2   = u().m(2);
    private static final Unit m3   = u().m(3);
    private static final Unit m_s2 = u().m(1).s(-2); // m/s²
    private static final Unit N    = u().kg(1).m(1).s(-2);
    private static final Unit Pa   = u().kg(1).m(-1).s(-2);
    private static final Unit J    = u().kg(1).m(2).s(-2);
    private static final Unit W    = u().kg(1).m(2).s(-3);
    
    // NOTE! When re-ordering units, parameters to Unit.set() refer
    // to the order specified here
    
    // Primitive, simple (non-composite) units
    private static final UnitDesc[] massUnits = new UnitDesc[] {
        new UnitDesc("kg",  SI|BASE),
        new UnitDesc("g",   SI,       kg, new Real(0, 0x3ffffff6, 0x4189374bc6a7ef9eL /*1e-3*/)),
        new UnitDesc("t",   SI,       kg, new Real(1000)),
        new UnitDesc("gr",  IMP,      kg, new Real(0, 0x3ffffff2, 0x43f253303203a99fL /*0.00006479891*/)),
        new UnitDesc("oz",  IMP,      kg, new Real(0, 0x3ffffffa, 0x741ea12add794261L /*0.028349523125*/)),
        new UnitDesc("lb",  IMP|BASE, kg, new Real(0, 0x3ffffffe, 0x741ea12add794261L /*0.45359237*/)),
        new UnitDesc("st",  IMP,      kg, new Real(0, 0x40000002, 0x659acd0581ca1a15L /*6.35029318*/)),
        new UnitDesc("ton", IMP,      kg, new Real(0, 0x40000009, 0x7165e963dc486ad3L /*907.18474*/)),
        new UnitDesc("`ton`",IMP,     kg, new Real(0, 0x40000009, 0x7f018046e23ca09aL /*1016.0469088*/)),
        new UnitDesc("u",   SI,       kg, new Real(0, 0x3fffffa7, 0x41c7dd5a667f9950L /*1.66053886e-27*/)),
    };
    private static final UnitDesc[] lengthUnits = new UnitDesc[] {
        new UnitDesc("m",   SI|BASE),
        new UnitDesc("Å",   SI,       m, new Real(0, 0x3fffffde, 0x6df37f675ef6eadfL /*1e-10*/)),
        new UnitDesc("mm",  SI,       m, new Real(0, 0x3ffffff6, 0x4189374bc6a7ef9eL /*1e-3*/)),
        new UnitDesc("cm",  SI,       m, Real.PERCENT),
        new UnitDesc("km",  SI,       m, new Real(1000)),
        new UnitDesc("in",  IMP,      m, new Real(0, 0x3ffffffa, 0x6809d495182a9931L /*0.0254*/)),
        new UnitDesc("ft",  IMP|BASE, m, new Real(0, 0x3ffffffe, 0x4e075f6fd21ff2e5L /*0.3048*/)),
        new UnitDesc("yd",  IMP,      m, new Real(0, 0x3fffffff, 0x750b0f27bb2fec57L /*0.9144*/)),
        new UnitDesc("mi",  IMP,      m, new Real(0, 0x4000000a, 0x6495810624dd2f1bL /*1609.344*/)),
        new UnitDesc("NM",  SI,       m, new Real(1852)),
        new UnitDesc("AU",  SI,       m, new Real(149597870691L)),
        new UnitDesc("ly",  SI,       m, new Real(9460730472580800L)),
        new UnitDesc("pc",  SI,       m, new Real(0, 0x40000036, 0x6da012f9404b0988L /*3.085677581305729e+16*/)),
    };
    private static final UnitDesc[] timeUnits = new UnitDesc[] {
        new UnitDesc("s",   SI|IMP|BASE),
        new UnitDesc("min", SI|IMP,   s, new Real(60)),
        new UnitDesc("h",   SI|IMP,   s, new Real(3600)),
        new UnitDesc("d",   SI|IMP,   s, new Real(86400)),
        new UnitDesc("y",   SI|IMP,   s, new Real(31557600)),
    };
    private static final UnitDesc[] chargeUnits = new UnitDesc[] {
        new UnitDesc("C",   SI|IMP|BASE),
        new UnitDesc("e",   SI|IMP,   C, new Real(0, 0x3fffffc1, 0x5e93683d3137633fL /*1.60217653e-19*/)),
    };
    private static final UnitDesc[] amountUnits = new UnitDesc[] {
        new UnitDesc("mol", SI|IMP|BASE),
    };
    private static final UnitDesc[] temperatureUnits = new UnitDesc[] {
        new UnitDesc("K",   SI|IMP|BASE),
        new UnitDesc("°C",  SI,       K, Real.ONE, new Real(0, 0x40000008, 0x444999999999999aL /* 273.15 */)),
        new UnitDesc("°F",  IMP,      K, new Real(0, 0x3fffffff, 0x471c71c71c71c71cL /* 1/1.8 */), new Real(0, 0x40000007, 0x7fafa4fa4fa4fa50L /* 273.15-32/1.8 */)),
        new UnitDesc("ð°C", "delta°C", SI,  K, Real.ONE, Real.ZERO),
        new UnitDesc("ð°F", "delta°F", IMP, K, new Real(0, 0x3fffffff, 0x471c71c71c71c71cL /* 1/1.8 */), Real.ZERO),
    };
    // Alternative primitive units (composite units with conversionFactor != 1)
    private static final UnitDesc[] alternativeAreaUnits = new UnitDesc[] {
        new UnitDesc("a",       SI,  m2, Real.HUNDRED),
        new UnitDesc("da",      SI,  m2, new Real(1000)),
        new UnitDesc("ha",      SI,  m2, new Real(10000)),
        new UnitDesc("acre",    IMP, m2, new Real(0, 0x4000000b, 0x7e76d9f3fcbc7ea1L /*4046.8564224*/)),
    };
    private static final UnitDesc[] alternativeVolumeUnits = new UnitDesc[] {
        new UnitDesc("l",       SI,  m3, new Real(0, 0x3ffffff6, 0x4189374bc6a7ef9eL /*1e-3*/)),
        new UnitDesc("ml",      SI,  m3, new Real(0, 0x3fffffec, 0x431bde82d7b634dbL /*1e-6*/)),
        new UnitDesc("cl",      SI,  m3, new Real(0, 0x3fffffef, 0x53e2d6238da3c212L /*1e-5*/)),
        new UnitDesc("dl",      SI,  m3, new Real(0, 0x3ffffff2, 0x68db8bac710cb296L /*1e-4*/)),
        new UnitDesc("fl.oz",   IMP, m3, new Real(0, 0x3ffffff0, 0x7c0a55e836d246a4L /*0.0000295735295625*/)),
        new UnitDesc("cup",     IMP, m3, new Real(0, 0x3ffffff3, 0x7c0a55e836d246a4L /*0.0002365882365*/)),
        new UnitDesc("pt",      IMP, m3, new Real(0, 0x3ffffff4, 0x7c0a55e836d246a4L /*0.000473176473*/)),
        new UnitDesc("gal",     IMP, m3, new Real(0, 0x3ffffff7, 0x7c0a55e836d246a4L /*0.003785411784*/)),
        new UnitDesc("`fl.oz`", IMP, m3, new Real(0, 0x3ffffff0, 0x772c4b265dd18634L /*0.0000284130625*/)),
        new UnitDesc("`cup`",   IMP, m3, new Real(0, 0x3ffffff3, 0x772c4b265dd18634L /*0.0002273045*/)),
        new UnitDesc("`pt`",    IMP, m3, new Real(0, 0x3ffffff5, 0x4a7baef7faa2f3e0L /*0.00056826125*/)),
        new UnitDesc("`gal`",   IMP, m3, new Real(0, 0x3ffffff8, 0x4a7baef7faa2f3e0L /*0.00454609*/)),
    };
    private static final UnitDesc[] alternativeAccelerationUnits = new UnitDesc[] {
        new UnitDesc("`g`", NONE, m_s2, new Real(0, 0x40000003, 0x4e7404ea4a8c154dL /*9.80665*/)),
    };
    private static final UnitDesc[] alternativePressureUnits = new UnitDesc[] {
        new UnitDesc("bar",  SI, Pa, new Real(100000)),
        new UnitDesc("atm",  SI, Pa, new Real(101325)),
        new UnitDesc("mmHg", SI, Pa, new Real(0, 0x40000007, 0x42a943fda60892ccL /*133.322387415*/)),
    };
    private static final UnitDesc[] alternativeEnergyUnits = new UnitDesc[] {
        new UnitDesc("kJ",   SI,  J, new Real(1000)),
        new UnitDesc("cal",  SI,  J, new Real(0, 0x40000002, 0x42fd21ff2e48e8a7L /*4.1868*/)),
        new UnitDesc("kcal", SI,  J, new Real(0, 0x4000000c, 0x416b333333333333L /*4186.8*/)),
        new UnitDesc("Btu",  IMP, J, new Real(0, 0x4000000a, 0x41f0e4c5b784bc12L /*1055.05585262*/)),
        new UnitDesc("eV",   SI,  J, new Real(0, 0x3fffffc1, 0x5e93683d3137633fL /*1.60217653e-19*/)),
    };
    private static final UnitDesc[] alternativePowerUnits = new UnitDesc[] {
        new UnitDesc("kW", SI,  W, new Real(1000)),
        new UnitDesc("MW", SI,  W, new Real(1000000)),
        new UnitDesc("hp", IMP, W, new Real(0, 0x40000009, 0x5d36655916a80304L /*745.69987158227022*/)),
    };
    // Derived (non-primitive) units (composite units with conversionFactor == 1)
    private static final UnitDesc[] derivedSpeedUnits = new UnitDesc[] {
        new UnitDesc("mph",  IMP, u().set(1,8,1).set(2,2,-1)),           // mi/h
        new UnitDesc("knot", u().set(1,9,1).set(2,2,-1)),                // NM/h
        new UnitDesc("c", u().set(1,11,1).set(2,4,-1)),                  // ly/y
    };
    private static final UnitDesc[] derivedForceUnits = new UnitDesc[] {
        new UnitDesc("N", N),
        new UnitDesc("pdl", IMP, u().set(0,5,1).set(1,6,1).s(-2)),       // lb·ft/s²
    };
    private static final UnitDesc[] derivedAlternativeForceUnits = new UnitDesc[] {
        new UnitDesc("kgf", u().kg(1).set(8,0,1)),                       // kg·`g`
        new UnitDesc("lbf", IMP, u().set(0,5,1).set(8,0,1)),             // lb·`g`
    };
    private static final UnitDesc[] derivedPressureUnits = new UnitDesc[] {
        new UnitDesc("Pa", Pa),
    };
    private static final UnitDesc[] derivedAlternativePressureUnits = new UnitDesc[] {
        new UnitDesc("psi", IMP, u().set(0,5,1).set(8,0,1).set(1,5,-2)), // lb·`g`/in²
    };
    private static final UnitDesc[] derivedEnergyUnits = new UnitDesc[] {
        new UnitDesc("J", J),
    };
    private static final UnitDesc[] derivedPowerUnits = new UnitDesc[] {
        new UnitDesc("W", W),
    };
    private static final UnitDesc[] derivedCurrentUnits = new UnitDesc[] {
        new UnitDesc("A", u().C(1).s(-1)),                               // C/s
    };
    private static final UnitDesc[] derivedPotentialUnits = new UnitDesc[] {
        new UnitDesc("V", u().kg(1).m(2).s(-2).C(-1)),                   // kg·m²/s²·C
    };
    private static final UnitDesc[] derivedCapasitanceUnits = new UnitDesc[] {
        new UnitDesc("F", u().s(2).C(2).kg(-1).m(-2)),                   // s²·C²/kg·m²
    };
    private static final UnitDesc[] derivedResistanceUnits = new UnitDesc[] {
        new UnitDesc("Ø", "Ohm", SI, u().kg(1).m(2).s(-1).C(-2)),        // kg·m²/s·C²
    };
    private static final UnitDesc[] derivedFluxUnits = new UnitDesc[] {
        new UnitDesc("Wb", u().kg(1).m(2).s(-1).C(-1)),                  // kg·m²/s·C
    };
    private static final UnitDesc[] derivedFluxDensityUnits = new UnitDesc[] {
        new UnitDesc("T", u().kg(1).s(-1).C(-1)),                        // kg/s·C
    };
    private static final UnitDesc[] derivedInductanceUnits = new UnitDesc[] {
        new UnitDesc("H", u().kg(1).m(2).C(-2)),                         // kg·m²/C²
    };
    // Additional composite units used in menus
    private static final UnitDesc[] helperCompositeUnits = new UnitDesc[] {
        new UnitDesc("m²",   m2),
        new UnitDesc("m³",   m3),
        new UnitDesc("m/s",  u().m(1).s(-1)),
        new UnitDesc("km/h", u().set(1,4,1).set(2,2,-1)),
        new UnitDesc("m/s²", m_s2),
        new UnitDesc("in/s²",u().set(1,5,1).s(-2)),
        new UnitDesc("ft/s²",u().set(1,6,1).s(-2)),
        new UnitDesc("mi/s²",u().set(1,8,1).s(-2)),
        new UnitDesc("in²",  u().set(1,5,2)),
        new UnitDesc("in³",  u().set(1,5,3)),
        new UnitDesc("ft²",  u().set(1,6,2)),
        new UnitDesc("yd²",  u().set(1,7,2)),
        new UnitDesc("mi²",  u().set(1,8,2)),
        new UnitDesc("s²",   u().s(2)),
        new UnitDesc("",     u()),
    };

    static final UnitDesc[][] allUnits = new UnitDesc[][] {
        // Primitive
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
        derivedForceUnits,               // 13
        derivedAlternativeForceUnits,    // 14
        derivedPressureUnits,            // 15
        derivedAlternativePressureUnits, // 16
        derivedEnergyUnits,              // 17
        derivedPowerUnits,               // 18
        derivedCurrentUnits,             // 19
        derivedPotentialUnits,           // 20
        derivedCapasitanceUnits,         // 21
        derivedResistanceUnits,          // 22
        derivedFluxUnits,                // 23
        derivedFluxDensityUnits,         // 24
        derivedInductanceUnits,          // 25
        // Additional
        helperCompositeUnits  // Used only indirectly
    };
    static final String OFL = "[ofl]";
    static final String ERR = "[err]";
    
    static final class DerivedUnit {
        int[] power = new int[N_DERIVED_UNITS];
        int[] unit = new int[N_DERIVED_UNITS];
        int complexity;
        
        // Order in which units appear in the output string
        private static final int unitTypeOrder[] = new int[] {
            //  N  N  A C  V  F Ohm Wb  H  J  J  W  W Pa Pa Pa kg m m² m³ m/s g s mol K  T
               13,14,19,3,20,21, 22,23,25,17,10,18,11,15,16, 9, 0,1, 6, 7, 12,8,2,  4,5,24
        };
        // Relative penalty for making use of each unit
        private static final int oddness[] = new int[] {
            // kg m s C mol K m² m³ g Pa J W m/s N N Pa Pa J W A V F Ohm Wb T H
                0,0,0,2,  0,0, 1, 1,2, 4,3,3,  2,3,3, 4, 4,3,3,1,3,4,  3, 4,3,4
        };
        // Dimensionality limit for "natural use" of each unit
        private static final int dimensionLimit[] = new int[] {
            // kg m s C mol K m² m³ g Pa J W m/s N N Pa Pa J W A V F Ohm Wb T H
                1,3,2,2,  1,1, 1, 1,1, 1,1,1,  2,1,1, 1, 1,1,1,2,2,1,  1, 1,2,1
        };
        static final String powerStr[] = new String[] {
            "", "", "²", "³", "¼"/*^4*/
        };
        static final String powerStrNormalText[] = new String[] {
            "", "", "²", "³", "^4", "^5", "^6", "^7", "^8"
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
            
            int maxFactor = -100;
            int minFactor = 100;
            for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
                if (reduceBy.power[unitType] != 0) {
                    int f = power[unitType]/reduceBy.power[unitType];
                    if (f > maxFactor) maxFactor = f;
                    if (f < minFactor) minFactor = f;
                }
            }
            int factor = minFactor > 0 ? minFactor : maxFactor < 0 ? maxFactor : 1;

            for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
                power[unitType] -= factor*reduceBy.power[unitType];
            }
            power[reduceByUnitType] += factor;
            unit[reduceByUnitType] = reduceByUnit;
            calcComplexity();
            if (complexity < oldComplexity)
                return factor;

            for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
                power[unitType] += 2*factor*reduceBy.power[unitType];
            }
            power[reduceByUnitType] -= 2*factor;
            calcComplexity();
            if (complexity < oldComplexity)
                return -factor;
            
            for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
                power[unitType] -= factor*reduceBy.power[unitType];
            }
            power[reduceByUnitType] += factor;
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
            return toString(false);
        }

        public String toString(boolean normalText) {
            String[] powerStr = normalText ? powerStrNormalText : DerivedUnit.powerStr;
            boolean allMinusOne = true;
            boolean someNegative = false;
            for (int i=0; i<N_DERIVED_UNITS; i++) {
                int unitType = unitTypeOrder[i];
                if (power[unitType] > 0 || power[unitType] < -1)
                    allMinusOne = false;
                if (power[unitType] < 0)
                    someNegative = true;
                if (Math.abs(power[unitType]) >= powerStr.length) {
                    return OFL;
                }
            }
            
            toStringBuf.setLength(0);
            boolean started = false;
            if (allMinusOne && !normalText) {
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
                if (!started && normalText)
                    toStringBuf.append("1");
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
    
    public static Unit u() {
        return new Unit();
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
    
    public Unit setUnit(int unitType, int unit) {
        if (unitType < N_PRIMITIVE_UNITS) {
            return unity().set(unitType, unit, 1);
        }
        return copy(allUnits[unitType][unit].convertsTo);
    }

    public Unit unity() {
        for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
            power[unitType] = 0;
        }
        error    = false;
        overflow = false;
        return this;
    }
    
    public Unit kg(int power) {
        return set(0,0,power);
    }
    public Unit m(int power) {
        return set(1,0,power);
    }
    public Unit s(int power) {
        return set(2,0,power);
    }
    public Unit C(int power) {
        return set(3,0,power);
    }
    public Unit mol(int power) {
        return set(4,0,power);
    }
    public Unit K(int power) {
        return set(5,0,power);
    }
    public Unit J(int power) {
        return kg(power).m(2*power).s(-2*power);
    }
    public Unit set(int unitType, int unit, int power) {
        this.power[unitType] += power;
        this.unit[unitType] = unit;
        return this;
    }

    static final int[] bitsPerPower = new int[] {
        2,4,4,3,2,2,2,2,2,2,2,2
    };
    private static final int[] bitsPerUnit = new int[] {
        4,4,3,2,1,3,3,4,1,3,3,2
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
        return error ? 
               ((error    ? 1L:0) << 62) +
               ((overflow ? 1L:0) << 63) :
               result;
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
                    nSI += Math.abs(power[unitType]);
                } 
                if ((system & IMP) != 0) {
                    nIMP += Math.abs(power[unitType]);
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
                              Unit targetUnit, Real conversionFactor) {
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
                if (power[unitType] == 0) {
                    if (targetUnit != null && targetUnit.power[unitType] != 0)
                        unit[unitType] = targetUnit.unit[unitType];
                    else
                        unit[unitType] = baseUnit(unitType, dominantSystem);
                }
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
                            Real conversionFactor, int requiredPower) {
        UnitDesc convertTo = allUnits[compositeType][compositeUnit];
        
        int factor = 0;
        if (requiredPower != 0) {
            factor = requiredPower - power[compositeType];
        } else {
            int maxFactor = -100;
            int minFactor = 100;
            for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
                if (convertTo.convertsTo.power[unitType] != 0) {
                    int f = power[unitType]/convertTo.convertsTo.power[unitType];
                    if (f > maxFactor) maxFactor = f;
                    if (f < minFactor) minFactor = f;
                }
            }
            if (minFactor > 0)
                factor = minFactor;
            if (maxFactor < 0)
                factor = maxFactor;
        }
        if (factor == 0)
            return false;

        for (int unitType=0; unitType<N_SIMPLE_PRIMITIVE_UNITS; unitType++) {
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
        return true;
    }
    
    private int fractionForFullConversion(Unit a) {
        // A unit is fully convertible to another if:
        // 1) Both units, when decomposed into simple primitive units, have all
        //    units in a fixed ratio relative to each other
        // 2) The all primitive units (simple and composite) in the unit converted to
        //    must be divisible by the denominator of the ratio

        int numerator = 0;
        int denominator = 0;
        for (int simple=0; simple<N_SIMPLE_PRIMITIVE_UNITS; simple++) {
            int d = power[simple];
            int n = a.power[simple];
            for (int composite=N_SIMPLE_PRIMITIVE_UNITS; composite<N_PRIMITIVE_UNITS; composite++) {
                d += allUnits[composite][0].convertsTo.power[simple] * power[composite];
                n += allUnits[composite][0].convertsTo.power[simple] * a.power[composite];
            }
            if ((n == 0) ^ (d == 0))
                return 0;
            if (numerator == 0) {
                numerator = n;
                denominator = d;
            } else {
                if (numerator*d != denominator*n)
                    return 0;
            }
        }
        // Reduce to simplest form
        if (numerator%2 == 0 && denominator%2 == 0) { numerator /= 2; denominator /= 2; }
        if (numerator%3 == 0 && denominator%3 == 0) { numerator /= 3; denominator /= 3; }
        if (numerator%5 == 0 && denominator%5 == 0) { numerator /= 5; denominator /= 5; }
        if (denominator < 0)                        { numerator *=-1; denominator *=-1; }
        for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
            if (a.power[unitType] % denominator != 0)
                return 0;
        }
        return (numerator<<16)+denominator;
    }

    private boolean hasAbsoluteTemperature() {
        return power[TEMP_TYPE] != 0 && (unit[TEMP_TYPE]==1 || unit[TEMP_TYPE]==2);
    }
    
    private boolean isPlainTemperature() {
        for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
            if (unitType != TEMP_TYPE && power[unitType] != 0)
                return false;
        }
        return power[TEMP_TYPE] == 1;
    }
    
    private boolean isPlainAbsoluteTemperature() {
        for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
            if (unitType != TEMP_TYPE && power[unitType] != 0)
                return false;
        }
        return power[TEMP_TYPE] == 1 && (unit[TEMP_TYPE]==0 || unit[TEMP_TYPE]==1 || unit[TEMP_TYPE]==2);
    }
    
    private boolean isPlainRelativeTemperature() {
        for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
            if (unitType != TEMP_TYPE && power[unitType] != 0)
                return false;
        }
        return power[TEMP_TYPE] == 1 && (unit[TEMP_TYPE]==0 || unit[TEMP_TYPE]==3 || unit[TEMP_TYPE]==4);
    }
    
    private Unit absoluteTemperature() {
        int u;
        switch (unit[TEMP_TYPE]) {
            case 0: default: u = 0; break;
            case 1: case 3:  u = 1; break;
            case 2: case 4:  u = 2; break;
        }
        return uTmp2.setUnit(TEMP_TYPE, u);
    }
    
    private Unit relativeTemperature() {
        int u;
        switch (unit[TEMP_TYPE]) {
            case 0: default: u = 0; break;
            case 1: case 3:  u = 3; break;
            case 2: case 4:  u = 4; break;
        }
        return uTmp2.setUnit(TEMP_TYPE, u);
    }
    
    private void makeAbsoluteTemperature() {
        switch (unit[TEMP_TYPE]) {
            case 3: unit[TEMP_TYPE] = 1; break;
            case 4: unit[TEMP_TYPE] = 2; break;
        }
    }
    
    private void makeRelativeTemperature() {
        switch (unit[TEMP_TYPE]) {
            case 1: unit[TEMP_TYPE] = 3; break;
            case 2: unit[TEMP_TYPE] = 4; break;
        }
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
    
    private void fullyConvertTo(Unit a, int fraction, Real conversionFactor) {
        int dominantSystem = a.dominantSystem();
        int numerator = fraction >> 16;
        int denominator = fraction & 0xffff;

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
        
        // Decompose all composites not present in a
        for (int composite=N_SIMPLE_PRIMITIVE_UNITS; composite<N_PRIMITIVE_UNITS; composite++) {
            if (power[composite] != 0 && a.power[composite] == 0) {
                decompose(composite, dominantSystem, a, conversionFactor);
            }
        }

        // Build up the specified fraction of all composites present in a
        for (int composite=N_SIMPLE_PRIMITIVE_UNITS; composite<N_PRIMITIVE_UNITS; composite++) {
            if (a.power[composite] != 0) {
                // It is already assured that a.power[composite] is divisible by denominator
                int requiredPower = a.power[composite]*numerator/denominator;
                if (requiredPower != power[composite])
                    compose(composite, a.unit[composite], conversionFactor, requiredPower);
            }
        }
    }
    
    public void convertTo(Unit a, Real conversionFactor, Real conversionOffset) {
        conversionFactor.assign(Real.ONE);
        if (conversionOffset != null)
            conversionOffset.assign(Real.ZERO);
        a = uTmp2.copy(a);
        if (handleErrorBinary(a))
            return;

        // Handle temperature specially
        if (hasAbsoluteTemperature() || a.hasAbsoluteTemperature()) {
            // Can only convert between absolute temperature and other plain temperatures
            if (!isPlainTemperature() || !a.isPlainTemperature() || conversionOffset == null) {
                error = true;
                return;
            }
            if (unit[TEMP_TYPE] != a.unit[TEMP_TYPE]) {
                conversionFactor.assign(allUnits[TEMP_TYPE][unit[TEMP_TYPE]].conversionFactor);
                conversionOffset.assign(allUnits[TEMP_TYPE][unit[TEMP_TYPE]].conversionOffset);
                conversionFactor.div(allUnits[TEMP_TYPE][a.unit[TEMP_TYPE]].conversionFactor);
                conversionOffset.sub(allUnits[TEMP_TYPE][a.unit[TEMP_TYPE]].conversionOffset);
                conversionOffset.div(allUnits[TEMP_TYPE][a.unit[TEMP_TYPE]].conversionFactor);
                unit[TEMP_TYPE] = a.unit[TEMP_TYPE];
            }
            return;
        }

        // Special case for "fully convertible" units
        int fraction = fractionForFullConversion(a);
        if (fraction != 0) {
            fullyConvertTo(a, fraction, conversionFactor);
            return;
        }

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
        for (int simple=0; simple<N_SIMPLE_PRIMITIVE_UNITS; simple++) {
            if (power[simple] == 0 && a.power[simple] != 0) {
                // Simple unit type only present in a
                for (int composite=N_SIMPLE_PRIMITIVE_UNITS; composite<N_PRIMITIVE_UNITS; composite++) {
                    if (power[composite] != 0 && a.power[composite] == 0) {
                        // Composite unit type only present in this
                        Unit composedOf = allUnits[composite][unit[composite]].convertsTo;
                        if (composedOf.power[simple] != 0) {
                            // Composite unit composed of simple unit
                            decompose(composite, dominantSystem, a, conversionFactor);
                        }
                    }
                }
            }
        }
        
        // For all composite unit types only present in a,
        // build up as many composites as possible from simple units not in a
        compositeUnits:
        for (int composite=N_SIMPLE_PRIMITIVE_UNITS; composite<N_PRIMITIVE_UNITS; composite++) {
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
                compose(composite, a.unit[composite], conversionFactor, 0);
            }
        }
        
        // Attempt to achieve the same balance of simple and composite
        // units as in a
        
        // ...later
    }

    public static long convertTo(long a, long b, Real convertA, Real offsetA) {
        uTmp1.unpack(a);
        uTmp2.unpack(b);
        uTmp1.convertTo(uTmp2, convertA, offsetA);
        return uTmp1.pack();
    }
    
    public void add(Unit a, Real conversionFactor, Real conversionOffset) {
        conversionFactor.assign(Real.ONE);
        if (conversionOffset != null)
            conversionOffset.assign(Real.ZERO);
        a = uTmp2.copy(a);
        if (handleErrorBinary(a))
            return;

        if (hasAbsoluteTemperature() || a.hasAbsoluteTemperature()) {
            if ((!isPlainAbsoluteTemperature() || !a.isPlainRelativeTemperature()) &&
                (!isPlainRelativeTemperature() || !a.isPlainAbsoluteTemperature())) {
                error = true;
                return;
            }
            if (!hasAbsoluteTemperature()) {
                convertTo(a.relativeTemperature(), conversionFactor, conversionOffset);
                makeAbsoluteTemperature();
            } else { // a is relative
                convertTo(a.absoluteTemperature(), conversionFactor, conversionOffset);
            }
            return;
        }

        if (!isSameUnit(a)) {
            convertTo(a, conversionFactor, conversionOffset);
            if (!isSameUnit(a)) {
                conversionFactor.assign(Real.ONE);
                if (conversionOffset != null)
                    conversionOffset.assign(Real.ZERO);
                error = true;
            }
        }
    }

    public static long add(long a, long b, Real convertA, Real offsetA) {
        uTmp1.unpack(a);
        uTmp2.unpack(b);
        uTmp1.add(uTmp2, convertA, offsetA);
        return uTmp1.pack();
    }
    
    public void sub(Unit a, Real conversionFactor, Real conversionOffset) {
        conversionFactor.assign(Real.ONE);
        if (conversionOffset != null)
            conversionOffset.assign(Real.ZERO);
        a = uTmp2.copy(a);
        if (handleErrorBinary(a))
            return;

        if (hasAbsoluteTemperature() || a.hasAbsoluteTemperature()) {
            if (!isPlainAbsoluteTemperature() || !a.isPlainTemperature()) {
                error = true;
                return;
            }
            if (a.hasAbsoluteTemperature()) {
                convertTo(a, conversionFactor, conversionOffset);
                makeRelativeTemperature();
            } else { // a is relative
                convertTo(a.absoluteTemperature(), conversionFactor, conversionOffset);
            }
            return;
        }

        if (!isSameUnit(a)) {
            convertTo(a, conversionFactor, conversionOffset);
            if (!isSameUnit(a)) {
                conversionFactor.assign(Real.ONE);
                if (conversionOffset != null)
                    conversionOffset.assign(Real.ZERO);
                error = true;
            }
        }
    }

    public static long sub(long a, long b, Real convertA, Real offsetA) {
        uTmp1.unpack(a);
        uTmp2.unpack(b);
        uTmp1.sub(uTmp2, convertA, offsetA);
        return uTmp1.pack();
    }
    
    public void mul(Unit a, Real conversionFactor) {
        conversionFactor.assign(Real.ONE);
        a = uTmp2.copy(a);
        if (handleErrorBinary(a))
            return;
        if (hasAbsoluteTemperature() || a.hasAbsoluteTemperature()) {
            if (!isUnity() || !a.isPlainAbsoluteTemperature()) {
                error = true;
                return;
            }
        } else {
            int fraction = fractionForFullConversion(a);
            if (fraction != 0) {
                fullyConvertTo(a, fraction, conversionFactor);
            } else {
                if (!isCompatibleWith(a)) {
                    convertTo(a, conversionFactor, null);
                }
            }
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
        if (hasAbsoluteTemperature() || a.hasAbsoluteTemperature()) {
            if (!isUnity() || !a.isPlainAbsoluteTemperature()) {
                error = true;
                return;
            }
        } else {
            int fraction = fractionForFullConversion(a);
            if (fraction != 0) {
                fullyConvertTo(a, fraction, conversionFactor);
            } else if (!isCompatibleWith(a)) {
                convertTo(a, conversionFactor, null);
            }
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
        if (hasAbsoluteTemperature()) {
            error = true;
            return;
        }
        for (int unitType=0; unitType<N_PRIMITIVE_UNITS; unitType++) {
            power[unitType] = -power[unitType];
        }
    }

    public static long recip(long a) {
        uTmp1.unpack(a);
        uTmp1.recip();
        return uTmp1.pack();
    }

    public void pow(int a) {
        if (handleErrorUnary())
            return;
        if (hasAbsoluteTemperature()) {
            error = true;
            return;
        }
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
        if (hasAbsoluteTemperature()) {
            error = true;
            return;
        }
        int dominantSystem = -1;
        for (int composite=N_SIMPLE_PRIMITIVE_UNITS; composite<N_PRIMITIVE_UNITS; composite++) {
            if ((power[composite] % a) != 0) {
                if (dominantSystem == -1)
                    dominantSystem = dominantSystem();
                decompose(composite, dominantSystem, null, conversionFactor);
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

    private static DerivedUnit reducedUnit = new DerivedUnit();
    private static DerivedUnit bestReducedUnit = new DerivedUnit();
    
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
        return toString(true, false);
    }

    public String toString(boolean reduce, boolean normalText) {
        if (overflow)
            return OFL;
        if (error)
            return ERR;
        if (isUnity())
            return "";
        reducedUnit.init(this);
        bestReducedUnit.copy(reducedUnit);
        if (reduce)
            reduce();
        return bestReducedUnit.toString(normalText);
    }
    
    public static String toString(long a) {
        return toString(a, true, false);
    }

    public static String toString(long a, boolean reduce, boolean normalText) {
        uTmp1.unpack(a);
        return uTmp1.toString(reduce, normalText);
    }
    
    public static String describe(long unit) {
        String unitDesc;
        if (unit == 0) {
            unitDesc = "The number is a dimensionless quantity, " +
                "without any physical units";
        } else {
            String u = Unit.toString(unit, false, true);
            String u_r = Unit.toString(unit, true, true);
            if (u.equals(Unit.OFL)) {
                unitDesc = "The unit is the result of an operation that " +
                    "overflowed, i.e. some physical quantity was raised to " +
                    "a power that was too high or too low";
            } else if (u.equals(Unit.ERR)) {
                unitDesc = "The unit is the result of an operation that is " +
                    "not permitted for units";
            } else if (u.equals(u_r)) {
                unitDesc = "The unit shown is the simplest form of the unit: " + u;
            } else if (u_r.equals(Unit.OFL)) {
                unitDesc = "The unit contains some physical quantity that " +
                    "cannot be displayed due to lack of symbols. The actual" +
                    "unit represented is: " + u;
            } else {
                unitDesc = "The unit shown is a reduced form composed of " +
                    "base units. The most primitive representation of this " +
                    "unit is: " + u;
            }
        }
        return unitDesc;
    }

}
