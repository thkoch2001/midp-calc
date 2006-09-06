VERSION = 3.03
TARGETS = Calc.jar \
          Calc.jad \
          CalcMIDP2.jar \
          CalcMIDP2.jad \
          CalcNokia.jar \
          CalcNokia.jad \
          CalcApplet.jar

BASEURL = http://midp-calc.sf.net

# antenna and proguard must be installed in $(WTK_1)/lib
WTK_1 = ../../WTK104
WTK_2 = ../../WTK2.2
WTK_N = ../../WTK104
BOOTCLASSPATH_1 = $(WTK_1)/lib/midpapi.zip
BOOTCLASSPATH_2 = $(WTK_2)/lib/midpapi20.jar:$(WTK_2)/lib/cldcapi10.jar
BOOTCLASSPATH_N = $(WTK_N)/lib/classes.zip:$(WTK_1)/lib/midpapi.zip

JFLAGS = -encoding "ISO8859-1" -O
JFLAGS_1 = -bootclasspath $(BOOTCLASSPATH_1) -d . $(JFLAGS)
JFLAGS_2 = -bootclasspath $(BOOTCLASSPATH_2) -d . $(JFLAGS)
JFLAGS_N = -bootclasspath $(BOOTCLASSPATH_N) -d . $(JFLAGS)

JAVAC = javac -source 1.3 -target 1.3

JAVAFILES =  Calc.java \
             CalcCanvas.java \
             CalcEngine.java \
             GraphCanvas.java \
             Real.java \
             Complex.java \
             Matrix.java \
             Guess.java \
             GFont.java \
             GFontBase.java \
             DataStore.java \
             SetupCanvas.java \
             CmdDesc.java

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

IMAGES =     images/Calc.jpg \
             images/Calc_menu0.gif \
             images/Calc_menu1.gif \
             images/Calc_menu2.gif \
             images/Calc_menu3.gif \
             images/Calc_menu4.gif \
             images/Calc_menu5.gif \
             images/Calc_menu6.gif \
             images/Calc_menu7.gif

default: $(TARGETS)

pgm2java: pgm2java.c
	gcc -o $@ $< -Wall -O2

Real.java: ../real-java/Real.jpp Makefile
	cpp -C -P -DDO_INLINE -o $@ $<

GFontBase.java: pgm2java small.pgm medium.pgm large.pgm xlarge.pgm Makefile
	echo "package ral;"                          >  $@
	echo "abstract class GFontBase {"            >> $@
	./pgm2java small.pgm small_ a.dat            >> $@
	./pgm2java medium.pgm medium_ b.dat          >> $@
	./pgm2java large.pgm large_ c.dat            >> $@
	./pgm2java xlarge.pgm xlarge_ d.dat          >> $@
	echo "}"                                     >> $@

Calc.jad: Makefile
	echo "MIDlet-Name: Calc"                     >  $@
	echo "MIDlet-Vendor: Roar Lauritzsen"        >> $@
	echo "MIDlet-Version: $(VERSION)"            >> $@
	echo "MIDlet-Description: Scientific RPN Calculator" >> $@
	echo "MIDlet-Icon: /ral/Calc.png"            >> $@
	echo "MIDlet-Info-URL: $(BASEURL)/Calc.html" >> $@
	echo "MIDlet-Data-Size: 2048"                >> $@
	echo "MIDlet-Jar-URL: $(BASEURL)/Calc.jar"   >> $@
	echo "MIDlet-Jar-Size: 0"                    >> $@
	echo "MIDletX-LG-Contents: G7100"            >> $@
	echo "MicroEdition-Profile: MIDP-1.0"        >> $@
	echo "MicroEdition-Configuration: CLDC-1.0"  >> $@
	echo "MIDlet-1: Calc, /ral/Calc.png, ral.Calc" >> $@

CalcMIDP2.jad: Makefile
	echo "MIDlet-Name: Calc"                     >  $@
	echo "MIDlet-Vendor: Roar Lauritzsen"        >> $@
	echo "MIDlet-Version: $(VERSION)"            >> $@
	echo "MIDlet-Description: Scientific RPN Calculator" >> $@
	echo "MIDlet-Icon: /ral/Calc.png"            >> $@
	echo "MIDlet-Info-URL: $(BASEURL)/Calc.html" >> $@
	echo "MIDlet-Data-Size: 2048"                >> $@
	echo "MIDlet-Jar-URL: $(BASEURL)/CalcMIDP2.jar" >> $@
	echo "MIDlet-Jar-Size: 0"                    >> $@
	echo "MIDletX-LG-Contents: G7100"            >> $@
	echo "MicroEdition-Profile: MIDP-2.0"        >> $@
	echo "MicroEdition-Configuration: CLDC-1.0"  >> $@
	echo "MIDlet-1: Calc, /ral/Calc.png, ral.Calc" >> $@

CalcNokia.jad: Makefile
	echo "MIDlet-Name: Calc"                     >  $@
	echo "MIDlet-Vendor: Roar Lauritzsen"        >> $@
	echo "MIDlet-Version: $(VERSION)"            >> $@
	echo "MIDlet-Description: Scientific RPN Calculator" >> $@
	echo "MIDlet-Icon: /ral/Calc.png"            >> $@
	echo "MIDlet-Info-URL: $(BASEURL)/Calc.html" >> $@
	echo "MIDlet-Data-Size: 2048"                >> $@
	echo "MIDlet-Jar-URL: $(BASEURL)/CalcNokia.jar" >> $@
	echo "MIDlet-Jar-Size: 0"                    >> $@
	echo "MIDletX-LG-Contents: G7100"            >> $@
	echo "MicroEdition-Profile: MIDP-1.0"        >> $@
	echo "MicroEdition-Configuration: CLDC-1.0"  >> $@
	echo "MIDlet-1: Calc, /ral/Calc.png, ral.Calc" >> $@

Calc.jar: $(JAVAFILES) midp1/MyCanvas.java Calc.jad Calc.png
	rm -rf ral
	$(JAVAC) $(JFLAGS_1) $(JAVAFILES) midp1/MyCanvas.java
	cp Calc.png ral/
	ant -buildfile build.xml -lib $(WTK_1)/lib -Dwtk.home=$(WTK_1) make-jar
	touch $@

CalcMIDP2.jar: $(JAVAFILES) midp2/MyCanvas.java CalcMIDP2.jad Calc.png
	rm -rf ral
	$(JAVAC) $(JFLAGS_2) $(JAVAFILES) midp2/MyCanvas.java
	cp Calc.png ral/
	ant -buildfile buildMIDP2.xml -lib $(WTK_1)/lib -Dwtk.home=$(WTK_2) -Dbootclasspath=$(BOOTCLASSPATH_2) make-jar
	touch $@

CalcNokia.jar: $(JAVAFILES) nokia/MyCanvas.java CalcNokia.jad Calc.png
	rm -rf ral
	$(JAVAC) $(JFLAGS_N) $(JAVAFILES) nokia/MyCanvas.java
	cp Calc.png ral/
	ant -buildfile buildNokia.xml -lib $(WTK_1)/lib -Dwtk.home=$(WTK_N) -Dbootclasspath=$(BOOTCLASSPATH_N) make-jar
	touch $@

CalcApplet.jar: CalcApplet.java $(JAVAFILES) midp1/MyCanvas.java $(MIDPFILES)
	$(JAVAC) -d midp $(JFLAGS) -classpath JSObject.jar CalcApplet.java $(JAVAFILES) midp1/MyCanvas.java $(MIDPFILES)
	cp [a-d].dat midp
	cd midp && jar cf ../CalcApplet.jar ral javax [a-d].dat

clean:
	rm -rf $(TARGETS) ral midp/ral midp/javax Real.java GFontBase.java pgm2java *.dat *~ .\#* midp/*~ midp/.\#* midp-*.tgz

derived.tgz: Real.java GFontBase.java Calc.jad
	tar czf derived.tgz $< [a-d].dat

midp-calc-$(VERSION)-src.tgz: $(JAVAFILES) midp1/MyCanvas.java midp2/MyCanvas.java nokia/MyCanvas.java CalcApplet.java $(MIDPFILES) Calc.png pgm2java.c small.pgm medium.pgm large.pgm xlarge.pgm Makefile build.xml derived.tgz JSObject.jar README
	tar czf $@ $^

publish: $(TARGETS) midp-calc-$(VERSION)-src.tgz
	cp Calc.jar Calc$(subst .,,$(VERSION)).jar
	cp CalcNokia.jar CalcNokia$(subst .,,$(VERSION)).jar
	cp CalcMIDP2.jar CalcMIDP2$(subst .,,$(VERSION)).jar
	scp $(TARGETS) Calc$(subst .,,$(VERSION)).jar CalcNokia$(subst .,,$(VERSION)).jar CalcMIDP2$(subst .,,$(VERSION)).jar $(HTMLFILES) Calc.ico shell.sf.net:/home/groups/m/mi/midp-calc/htdocs
	scp $(IMAGES) shell.sf.net:/home/groups/m/mi/midp-calc/htdocs/images
	tar czf midp-calc-$(VERSION).tgz $(TARGETS) $(HTMLFILES) $(IMAGES)
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
	@echo "% cvs tag -RF RELEASE_$(subst .,_,$(VERSION))"
	@echo ""
	@echo "Open http://sourceforge.net/projects/midp-calc/"
	@echo "Select Admin, File Releases"
	@echo "Select [Add Release] for the \"midp-calc\" package"
	@echo "New release name: $(VERSION)"
	@echo "Add notes, change log"
	@echo "Add the files midp-calc-$(VERSION)*.tgz"
	@echo "Set Processor: Platform-Independent, File Type Source .gz, .gz"
	@echo ""
