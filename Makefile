VERSION = 1.15
TARGETS = Calc.jar Calc.jad

WTK_HOME = /home/roarl/ant/WTK104
JFLAGS = --bootclasspath=$(WTK_HOME)/lib/midpapi.zip -Wall -C -d . -O2
#JFLAGS = -bootclasspath $(WTK_HOME)/lib/midpapi.zip -d . -O

JAVAFILES  = Calc.java \
             CalcCanvas.java \
             CalcEngine.java \
             GFont.java \
             GFontBase.java \
             PropertyStore.java \
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
	echo "MIDlet-Version: $(VERSION)"            >> $@
	echo "MIDlet-Vendor: Roar Lauritzsen"        >> $@
	echo "MicroEdition-Profile: MIDP-1.0"        >> $@
	echo "MicroEdition-Configuration: CLDC-1.0"  >> $@
	echo "MIDlet-Jar-URL: http://gridbug.ods.org/~roarl/Calc.jar" >> $@
	echo "MIDlet-Jar-Size: 0"                    >> $@
	echo "MIDlet-1: Calc, , ral.Calc"            >> $@

Calc.jar: $(JAVAFILES) Calc.jad
	rm -rf ral
	gcj $(JFLAGS) $(JAVAFILES)
#	javac $(JFLAGS) $(JAVAFILES)
	ant -buildfile build.xml -Dwtk.home=${WTK_HOME} make-jar

clean:
	rm -rf $(TARGETS) ral Real.java GFontBase.java pgm2java *~

publish:
	scp Calc.jad Calc.jar Calc.html Calc-log.html Real.html Real.java Real.jpp gridbug:public_html
