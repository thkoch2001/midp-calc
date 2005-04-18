VERSION = 2.04
TARGETS = Calc.jar Calc.jad CalcApplet.jar

WTK_HOME = ../../WTK104
JFLAGS = --bootclasspath=$(WTK_HOME)/lib/midpapi.zip --encoding="ISO 8859-1" -Wall -C -d . -O2
## For use with javac:
#JFLAGS = -bootclasspath $(WTK_HOME)/lib/midpapi.zip -encoding "ISO8859-1" -d . -O

JAVAFILES =  Calc.java \
             CalcCanvas.java \
             CalcEngine.java \
             GraphCanvas.java \
             GFont.java \
             GFontBase.java \
             DataStore.java \
             SetupCanvas.java

MIDPFILES =  midp/MIDlet.java \
             midp/Display.java \
             midp/Displayable.java \
             midp/Command.java \
             midp/CommandListener.java \
             midp/Canvas.java \
             midp/Form.java \
             midp/TextBox.java \
             midp/TextField.java \
             midp/Graphics.java \
             midp/Image.java \
             midp/Font.java \
             midp/RecordStore.java \
             midp/RecordEnumeration.java

HTMLFILES =  Calc.html \
             Calc-log.html \
             Calc-prog.html \
             CalcApplet.html

default: $(TARGETS)

pgm2java: pgm2java.c
	gcc -o $@ $< -Wall -O2

Real.java: ../real-java/Real.jpp Makefile
	cpp -C -P -DDO_INLINE -o $@ $<

GFontBase.java: pgm2java small.pgm medium.pgm large.pgm xlarge.pgm Makefile
	echo "package ral;"                          >  $@
	echo "abstract class GFontBase {"            >> $@
	pgm2java small.pgm small_                    >> $@
	pgm2java medium.pgm medium_                  >> $@
	pgm2java large.pgm large_                    >> $@
	pgm2java xlarge.pgm xlarge_                  >> $@
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
	echo "MIDletX-LG-Contents: G7100"            >> $@
	echo "MicroEdition-Profile: MIDP-1.0"        >> $@
	echo "MicroEdition-Configuration: CLDC-1.0"  >> $@
	echo "MIDlet-1: Calc, /ral/Calc.png, ral.Calc" >> $@

Calc.jar: $(JAVAFILES) Real.java Calc.jad Calc.png
	rm -rf ral
	gcj $(JFLAGS) $(JAVAFILES) Real.java
#	javac $(JFLAGS) $(JAVAFILES) Real.java
	cp Calc.png ral/
	ant -buildfile build.xml -lib $(WTK_HOME)/lib -Dwtk.home=${WTK_HOME} make-jar
	touch Calc.jar

CalcApplet.jar: CalcApplet.java $(JAVAFILES) Real.java $(MIDPFILES)
	gcj --encoding="ISO 8859-1" -Wall -C -d midp -O2 CalcApplet.java $(JAVAFILES) Real.java $(MIDPFILES)
	cd midp && jar cf ../tmp.jar ral javax
	java -jar $(WTK_HOME)/lib/proguard-3.1.jar -injars tmp.jar -outjars CalcApplet.jar -libraryjars "<java.home>/lib/rt.jar" -keep public class ral.CalcApplet
	rm tmp.jar

clean:
	rm -rf $(TARGETS) ral midp/ral midp/javax Real.java GFontBase.java pgm2java *~ .\#* midp/*~ midp/.\#* derived.tgz midp-*.tgz

derived.tgz: Real.java GFontBase.java Calc.jad
	tar czf derived.tgz Real.java GFontBase.java Calc.jad

midp-calc-source-$(VERSION).tgz: $(JAVAFILES) Real.java CalcApplet.java $(MIDPFILES) Calc.png pgm2java.c small.pgm medium.pgm large.pgm xlarge.pgm Makefile build.xml derived.tgz README
	tar czf $@ $^

publish: $(TARGETS) midp-calc-source-$(VERSION).tgz
	scp $(TARGETS) $(HTMLFILES) shell.sf.net:/home/groups/m/mi/midp-calc/htdocs
	tar czf midp-calc-$(VERSION).tgz $(TARGETS) $(HTMLFILES)
