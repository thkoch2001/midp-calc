VERSION = 0.92
TARGETS = Calc.jar Calc.jad

JFLAGS = --bootclasspath=/home/roarl/midpapi.jar -C -d . -O2

JAVAFILES  = Calc.java \
             CalcCanvas.java \
             CalcEngine.java \
             GFont.java \
             GFontBase.java \
             Real.java

CLASSFILES = ral/Calc.class \
             ral/CalcCanvas.class \
             ral/CalcEngine.class \
             ral/GFont.class \
             ral/GFontBase.class \
             ral/Real.class

SHELL = /usr/bin/csh

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
	echo "Manifest-Version: 1.0\r"               >  $@
	echo "MicroEdition-Configuration: CLDC-1.0\r">> $@
	echo "MIDlet-Name: Calc\r"                   >> $@
	echo "MIDlet-Vendor: Roar Lauritzsen\r"      >> $@
	echo "MIDlet-1: Calc, , ral.Calc\r"          >> $@
	echo "MIDlet-Version: $(VERSION)\r"          >> $@
	echo "MicroEdition-Profile: MIDP-1.0\r"      >> $@
	echo "\r"                                    >> $@

Calc.jar: $(JAVAFILES) calcManifest
	gcj $(JFLAGS) $(JAVAFILES)
#	javac -bootclasspath /home/roarl/midpapi.jar -d . $(JAVAFILES)
	jar cfm $@ calcManifest ral

Calc.jad: Calc.jar Makefile
	echo "MIDlet-Name: Calc"                     >  $@
	echo "MIDlet-Version: $(VERSION)"            >> $@
	echo "MIDlet-Vendor: Roar Lauritzsen"        >> $@
	echo "MicroEdition-Profile: MIDP-1.0"        >> $@
	echo "MicroEdition-Configuration: CLDC-1.0"  >> $@
	echo "MIDlet-Jar-URL: Calc.jar"              >> $@
	echo "MIDlet-Jar-Size:" `perl -e 'print -s "Calc.jar"'` >> $@
	echo "MIDlet-1: Calc, , ral.Calc"            >> $@
	echo ""                                      >> $@

clean:
	rm -rf $(TARGETS) ral GFontBase.java pgm2java calcManifest
