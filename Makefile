VERSION = 0.99.5
TARGETS = Calc.jar Calc.jad

WTK_HOME = /home/roarl/ant/WTK104
JFLAGS = --bootclasspath=$(WTK_HOME)/lib/midpapi.zip -Wall -C -d . -O2

JAVAFILES  = Calc.java \
             CalcCanvas.java \
             CalcEngine.java \
             GFont.java \
             GFontBase.java \
             PropertyStore.java \
             Real.java

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

Calc.jad: Makefile
	echo "MIDlet-Name: Calc"                     >  $@
	echo "MIDlet-Version: $(VERSION)"            >> $@
	echo "MIDlet-Vendor: Roar Lauritzsen"        >> $@
	echo "MicroEdition-Profile: MIDP-1.0"        >> $@
	echo "MicroEdition-Configuration: CLDC-1.0"  >> $@
	echo "MIDlet-Jar-URL: Calc.jar"              >> $@
	echo "MIDlet-Jar-Size: 0"                    >> $@
	echo "MIDlet-1: Calc, , ral.Calc"            >> $@

Calc.jar: $(JAVAFILES) Calc.jad
	gcj $(JFLAGS) $(JAVAFILES)
	ant -buildfile build.xml -Dwtk.home=${WTK_HOME} make-jar

clean:
	rm -rf $(TARGETS) ral GFontBase.java pgm2java
