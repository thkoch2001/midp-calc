VERSION = 2.03
TARGETS = Calc.jar Calc.jad CalcApplet.jar

WTK_HOME = ../../WTK104
JFLAGS = --bootclasspath=$(WTK_HOME)/lib/midpapi.zip --encoding="ISO 8859-1" -Wall -C -d . -O2
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

default: $(TARGETS)

pgm2java: pgm2java.c
	gcc -o $@ $< -Wall -O2

Real.java: ../real-java/Real.jpp Makefile
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
	echo "MIDletX-LG-Contents: G7100"            >> $@
	echo "MicroEdition-Profile: MIDP-1.0"        >> $@
	echo "MicroEdition-Configuration: CLDC-1.0"  >> $@
	echo "MIDlet-1: Calc, /ral/Calc.png, ral.Calc" >> $@

Calc.jar: $(JAVAFILES) Calc.jad Calc.png
	rm -rf ral
	gcj $(JFLAGS) $(JAVAFILES)
#	javac $(JFLAGS) $(JAVAFILES)
	cp Calc.png ral/
#	jar cf Calc.jar ral/*
	ant -buildfile build.xml -lib $(WTK_HOME)/lib -Dwtk.home=${WTK_HOME} make-jar
	touch Calc.jar

CalcApplet.jar: CalcApplet.java $(JAVAFILES) $(MIDPFILES)
	gcj --encoding="ISO 8859-1" -Wall -C -d midp -O2 CalcApplet.java $(JAVAFILES) $(MIDPFILES)
	cd midp && jar cf ../tmp.jar ral javax
	java -jar $(WTK_HOME)/lib/proguard-3.0.7.jar -injars tmp.jar -outjars CalcApplet.jar -libraryjars "<java.home>/lib/rt.jar" -keep public class ral.CalcApplet
	rm tmp.jar

clean:
	rm -rf $(TARGETS) midp/ral midp/javax Real.java GFontBase.java pgm2java *~ .\#* midp/*~ midp/.\#*

publish: Calc.jad Calc.jar CalcApplet.jar
	scp Calc.jad Calc.jar Calc.html Calc-log.html Calc-prog.html CalcApplet.jar CalcApplet.html gridbug:public_html
