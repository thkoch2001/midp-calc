VERSION = 2.01
TARGETS = Calc.jar Calc.jad

WTK_HOME = /home/roarl/ant/WTK104
JFLAGS = --bootclasspath=$(WTK_HOME)/lib/midpapi.zip -Wall -C -d . -O2
#JFLAGS = -bootclasspath $(WTK_HOME)/lib/midpapi.zip -d . -O

JAVAFILES  = Calc.java \
             CalcCanvas.java \
             CalcEngine.java \
             GraphCanvas.java \
             GFont.java \
             GFontBase.java \
             DataStore.java \
             SetupCanvas.java \
             Real.java

default: $(TARGETS)

pgm2java: pgm2java.c
	gcc -o $@ $< -Wall -O2

Real.java: Real.jpp Makefile
	cpp -C -P -DDO_INLINE -o $@ $<

GFontBase.java: pgm2java large.pgm medium.pgm small.pgm Makefile
	echo "package ral;"                          >  $@
	echo "abstract class GFontBase {"            >> $@
	pgm2java small.pgm small_                    >> $@
	pgm2java medium.pgm medium_                  >> $@
	pgm2java large.pgm large_                    >> $@
	echo "}"                                     >> $@

Calc.jad: Makefile
	echo "MIDlet-Name: Calc"                     >  $@
	echo "MIDlet-Vendor: Roar Lauritzsen"        >> $@
	echo "MIDlet-Version: $(VERSION)"            >> $@
	echo "MIDlet-Description: Scientific RPN Calculator" >> $@
	echo "MIDlet-Icon: /ral/Calc.png"            >> $@
	echo "MIDlet-Info-URL: http://gridbug.ods.org/Calc.html" >> $@
	echo "MIDlet-Data-Size: 2048"                >> $@
	echo "MIDlet-Jar-URL: http://gridbug.ods.org/Calc.jar" >> $@
	echo "MIDlet-Jar-Size: 0"                    >> $@
	echo "MicroEdition-Profile: MIDP-1.0"        >> $@
	echo "MicroEdition-Configuration: CLDC-1.0"  >> $@
	echo "MIDlet-1: Calc, /ral/Calc.png, ral.Calc" >> $@

Calc.jar: $(JAVAFILES) Calc.jad Calc.png
	rm -rf ral
	gcj $(JFLAGS) $(JAVAFILES)
#	javac $(JFLAGS) $(JAVAFILES)
	cp Calc.png ral/
#	jar cf Calc.jar ral/*
	ant -buildfile build.xml -Dwtk.home=${WTK_HOME} make-jar

clean:
	rm -rf $(TARGETS) ral Real.java GFontBase.java pgm2java *~ .\#*

publish: Calc.jad Calc.jar Real.java
	scp Calc.jad Calc.jar Calc.html Calc-log.html Calc-prog.html Real.html Real.java Real.jpp gridbug:public_html
