VERSION = 0.2
TARGETS = Calc.jar Calc.jad

JFLAGS = -I/home/roarl/midpapi.jar -C -d . -O2

JAVAFILES  = Calc.java CalcCanvas.java GFont.java GFontBase.java
CLASSFILES = ral/Calc.class ral/CalcCanvas.class ral/GFont.class ral/GFontBase.class
#SHELL = /usr/bin/csh

default: $(TARGETS)

pgm2java: pgm2java.c
	gcc -o $@ $< -Wall -O2

GFontBase.java: pgm2java large.pgm medium.pgm small.pgm Makefile
	echo "package ral;"                          >  $@
	echo "abstract class GFontBase {"            >> $@
	pgm2java small.pgm small_                    >> $@
	pgm2java medium.pgm medium_                  >> $@
	pgm2java large.pgm large_                    >> $@
	echo "}"                                     >> $@

calcManifest: Makefile
	echo "Manifest-Version: 1.0"                 >  $@
	echo "MicroEdition-Configuration: CLDC-1.0"  >> $@
	echo "MIDlet-Name: Calc "                    >> $@
#	echo "Ant-Version: Apache Ant 1.5.4 "        >> $@
	echo "Created-By: 0.92-gcc"                  >> $@
	echo "MIDlet-Vendor: Roar Lauritzsen "       >> $@
	echo "MIDlet-1: Calc, , ral.Calc "           >> $@
	echo "MIDlet-Version: $(VERSION) "           >> $@
	echo "MicroEdition-Profile: MIDP-1.0"        >> $@
	echo ""                                      >> $@

Calc.jar: $(JAVAFILES) calcManifest
	gcj $(JFLAGS) $(JAVAFILES)
	jar cmf calcManifest $@ ral

Calc.jad: Calc.jar Makefile
	echo "MIDlet-Version: $(VERSION) "           >  $@
	echo "MIDlet-Vendor: Roar Lauritzsen "       >> $@
	echo "MIDlet-Jar-URL: http://gridbug.ods.org/~roarl/Calc.jar "             >> $@
	echo "MicroEdition-Configuration: CLDC-1.0 " >> $@
	echo "MIDlet-1: Calc, , ral.Calc "           >> $@
	echo "MicroEdition-Profile: MIDP-1.0 "       >> $@
	echo "MIDlet-Jar-Size:" `perl -e 'print -s "Calc.jar"'` >> $@
	echo "MIDlet-Name: Calc "                    >> $@

clean:
	rm -rf $(TARGETS) ral GFontBase.java pgm2java calcManifest
