VERSION = 2.08
TARGETS = Calc.jar Calc.jad CalcApplet.jar

WTK_HOME = ../../WTK104

## For use with gcj:
#JFLAGS = --bootclasspath=$(WTK_HOME)/lib/midpapi.zip --encoding="ISO 8859-1" -Wall -C -d . -O2

# For use with javac:
JFLAGS = -bootclasspath $(WTK_HOME)/lib/midpapi.zip -encoding "ISO8859-1" -d . -O

JAVAFILES =  Calc.java \
             CalcCanvas.java \
             CalcEngine.java \
             GraphCanvas.java \
             Complex.java \
             Matrix.java \
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
	pgm2java small.pgm small_ a.dat              >> $@
	pgm2java medium.pgm medium_ b.dat            >> $@
	pgm2java large.pgm large_ c.dat              >> $@
	pgm2java xlarge.pgm xlarge_ d.dat            >> $@
	echo "}"                                     >> $@

Calc.jad: Makefile
	echo "MIDlet-Name: Calc"                     >  $@
	echo "MIDlet-Vendor: Roar Lauritzsen"        >> $@
	echo "MIDlet-Version: $(VERSION)"            >> $@
	echo "MIDlet-Description: Scientific RPN Calculator" >> $@
	echo "MIDlet-Icon: /ral/Calc.png"            >> $@
	echo "MIDlet-Info-URL: http://midp-calc.sourceforge.net/Calc.html" >> $@
	echo "MIDlet-Data-Size: 2048"                >> $@
	echo "MIDlet-Jar-URL: http://midp-calc.sourceforge.net/Calc.jar" >> $@
	echo "MIDlet-Jar-Size: 0"                    >> $@
	echo "MIDletX-LG-Contents: G7100"            >> $@
	echo "MicroEdition-Profile: MIDP-1.0"        >> $@
	echo "MicroEdition-Configuration: CLDC-1.0"  >> $@
	echo "MIDlet-1: Calc, /ral/Calc.png, ral.Calc" >> $@

Calc.jar: $(JAVAFILES) Real.java Calc.jad Calc.png
	rm -rf ral
#	gcj $(JFLAGS) $(JAVAFILES) Real.java
	javac $(JFLAGS) $(JAVAFILES) Real.java
	cp Calc.png ral/
	ant -buildfile build.xml -lib $(WTK_HOME)/lib -Dwtk.home=${WTK_HOME} make-jar
	touch Calc.jar

CalcApplet.jar: CalcApplet.java $(JAVAFILES) Real.java $(MIDPFILES)
	gcj --encoding="ISO 8859-1" -Wall -C -d midp -O2 --classpath=$(JAVA_HOME)/jre/lib/plugin.jar CalcApplet.java $(JAVAFILES) Real.java $(MIDPFILES)
#	javac -encoding "ISO8859-1" -d midp -O -classpath $(JAVA_HOME)/jre/lib/javaplugin.jar CalcApplet.java $(JAVAFILES) Real.java $(MIDPFILES)
	cp [a-d].dat midp
	cd midp && jar cf ../CalcApplet.jar ral javax [a-d].dat

clean:
	rm -rf $(TARGETS) ral midp/ral midp/javax Real.java GFontBase.java pgm2java *.dat *~ .\#* midp/*~ midp/.\#* midp-*.tgz

derived.tgz: Real.java GFontBase.java Calc.jad
	tar czf derived.tgz Real.java GFontBase.java Calc.jad [a-d].dat

midp-calc-$(VERSION)-src.tgz: $(JAVAFILES) Real.java CalcApplet.java $(MIDPFILES) Calc.png pgm2java.c small.pgm medium.pgm large.pgm xlarge.pgm Makefile build.xml derived.tgz README
	tar czf $@ $^

publish: $(TARGETS) midp-calc-$(VERSION)-src.tgz
	cp Calc.jar Calc$(subst .,,$(VERSION)).jar
	scp $(TARGETS) Calc$(subst .,,$(VERSION)).jar $(HTMLFILES) shell.sf.net:/home/groups/m/mi/midp-calc/htdocs
	tar czf midp-calc-$(VERSION).tgz $(TARGETS) $(HTMLFILES)
	@echo ""
	@echo "***************************************************************"
	@echo "To make the release available on SourceForge, do the following:"
	@echo ""
	@echo "% ftp anonymous@upload.sourceforge.net"
	@echo "cd /incoming"
	@echo "bin"
	@echo "mput midp-calc-$(VERSION)*.tgz"
	@echo "bye"
	@echo ""
	@echo "% cvs update"
	@echo "% cvs commit -m \"Release $(VERSION)\""
	@echo "% cvs tag -R RELEASE_$(subst .,_,$(VERSION))"
	@echo ""
	@echo "Open http://sourceforge.net/projects/midp-calc/"
	@echo "Select Admin, File Releases"
	@echo "Select [Add Release] for the \"midp-calc\" package"
	@echo "New release name: $(VERSION)"
	@echo "Add notes, change log"
	@echo "Add the files midp-calc-$(VERSION)*.tgz"
	@echo "Set Processor: Platform-Independent, File Type Source .gz, .gz"
	@echo ""
