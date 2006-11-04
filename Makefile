SHELL = /bin/sh
VERSION = 3.03
TARGETS = midp1/Calc.jad \
          midp1/target/Calc.jar \
          midp2/CalcMIDP2.jad \
          midp2/target/CalcMIDP2.jar \
          nokia/CalcNokia.jad \
          nokia/target/CalcNokia.jar \
          applet/target/CalcApplet.jar

BASEURL = http://midp-calc.sf.net

WTK_1 = ../../WTK104
WTK_2 = ../../WTK2.2
WTK_N = ../../WTK104
BOOTCLASSPATH_1 = $(WTK_1)/lib/midpapi.zip
BOOTCLASSPATH_2 = $(WTK_2)/lib/midpapi20.jar:$(WTK_2)/lib/cldcapi10.jar
BOOTCLASSPATH_N = $(WTK_N)/lib/classes.zip:$(WTK_1)/lib/midpapi.zip
# antenna and proguard must be installed in $(WTK_1)/lib
# ANTENNA_PATH paths are relative to the midp1/midp2/nokia subdirectories
ANTENNA_PATH= ../$(WTK_1)/lib/antenna-bin-0.9.14.jar:../$(WTK_1)/lib/proguard-3.6.jar

JFLAGS = -encoding "ISO8859-1" -O
JFLAGS_1 = -bootclasspath $(BOOTCLASSPATH_1) -d . $(JFLAGS)
JFLAGS_2 = -bootclasspath $(BOOTCLASSPATH_2) -d . $(JFLAGS)
JFLAGS_N = -bootclasspath $(BOOTCLASSPATH_N) -d . $(JFLAGS)

JAVAC = javac -source 1.3 -target 1.3

JAVAFILES =  src/midpcalc/Calc.java \
             src/midpcalc/CalcCanvas.java \
             src/midpcalc/CalcEngine.java \
             src/midpcalc/GraphCanvas.java \
             src/midpcalc/Real.java \
             src/midpcalc/Complex.java \
             src/midpcalc/Matrix.java \
             src/midpcalc/Guess.java \
             src/midpcalc/GFont.java \
             src/midpcalc/GFontBase.java \
             src/midpcalc/DataStore.java \
             src/midpcalc/SetupCanvas.java \
             src/midpcalc/CmdDesc.java

MIDPFILES =  applet/src/javax/microedition/midlet/MIDlet.java \
             applet/src/javax/microedition/lcdui/Display.java \
             applet/src/javax/microedition/lcdui/Displayable.java \
             applet/src/javax/microedition/lcdui/Command.java \
             applet/src/javax/microedition/lcdui/CommandListener.java \
             applet/src/javax/microedition/lcdui/Canvas.java \
             applet/src/javax/microedition/lcdui/Form.java \
             applet/src/javax/microedition/lcdui/TextBox.java \
             applet/src/javax/microedition/lcdui/TextField.java \
             applet/src/javax/microedition/lcdui/Graphics.java \
             applet/src/javax/microedition/lcdui/Image.java \
             applet/src/javax/microedition/lcdui/Font.java \
             applet/src/javax/microedition/rms/RecordStore.java \
             applet/src/javax/microedition/rms/RecordEnumeration.java

HTMLFILES =  doc/Calc.html \
             doc/Calc-log.html \
             doc/Calc-prog.html \
             applet/CalcApplet.html

IMAGES =     doc/images/Calc.jpg \
             doc/images/Calc_menu0.gif \
             doc/images/Calc_menu1.gif \
             doc/images/Calc_menu2.gif \
             doc/images/Calc_menu3.gif \
             doc/images/Calc_menu4.gif \
             doc/images/Calc_menu5.gif \
             doc/images/Calc_menu6.gif \
             doc/images/Calc_menu7.gif

FONTS =      fonts/small.pgm \
             fonts/medium.pgm \
             fonts/large.pgm \
             fonts/xlarge.pgm

default: $(TARGETS)

fonts/pgm2java: fonts/pgm2java.c
	gcc -o $@ $< -Wall -O2

src/midpcalc/Real.java: ../real-java/Real.jpp Makefile
	cpp -C -P -DDO_INLINE -DPACKAGE=midpcalc -o $@ $<

src/midpcalc/GFontBase.java: fonts/pgm2java $(FONTS) Makefile
	echo "package midpcalc;"                                 >  $@
	echo "abstract class GFontBase {"                        >> $@
	fonts/pgm2java fonts/small.pgm  small_  resources/a.dat  >> $@
	fonts/pgm2java fonts/medium.pgm medium_ resources/b.dat  >> $@
	fonts/pgm2java fonts/large.pgm  large_  resources/c.dat  >> $@
	fonts/pgm2java fonts/xlarge.pgm xlarge_ resources/d.dat  >> $@
	echo "}"                                                 >> $@

midp1/Calc.jad: Makefile
	echo "MIDlet-Name: Calc"                                 >  $@
	echo "MIDlet-Vendor: Roar Lauritzsen"                    >> $@
	echo "MIDlet-Version: $(VERSION)"                        >> $@
	echo "MIDlet-Description: Scientific RPN Calculator"     >> $@
	echo "MIDlet-Icon: /Calc.png"                            >> $@
	echo "MIDlet-Info-URL: $(BASEURL)/Calc.html"             >> $@
	echo "MIDlet-Data-Size: 2048"                            >> $@
	echo "MIDlet-Jar-URL: $(BASEURL)/Calc.jar"               >> $@
	echo "MIDlet-Jar-Size: 0"                                >> $@
	echo "MIDletX-LG-Contents: G7100"                        >> $@
	echo "MicroEdition-Profile: MIDP-1.0"                    >> $@
	echo "MicroEdition-Configuration: CLDC-1.0"              >> $@
	echo "MIDlet-1: Calc, /Calc.png, midpcalc.Calc"          >> $@

midp2/CalcMIDP2.jad: Makefile
	echo "MIDlet-Name: Calc"                                 >  $@
	echo "MIDlet-Vendor: Roar Lauritzsen"                    >> $@
	echo "MIDlet-Version: $(VERSION)"                        >> $@
	echo "MIDlet-Description: Scientific RPN Calculator"     >> $@
	echo "MIDlet-Icon: /Calc.png"                            >> $@
	echo "MIDlet-Info-URL: $(BASEURL)/Calc.html"             >> $@
	echo "MIDlet-Data-Size: 2048"                            >> $@
	echo "MIDlet-Jar-URL: $(BASEURL)/CalcMIDP2.jar"          >> $@
	echo "MIDlet-Jar-Size: 0"                                >> $@
	echo "MIDletX-LG-Contents: G7100"                        >> $@
	echo "MicroEdition-Profile: MIDP-2.0"                    >> $@
	echo "MicroEdition-Configuration: CLDC-1.0"              >> $@
	echo "MIDlet-1: Calc, /Calc.png, midpcalc.Calc"          >> $@

nokia/CalcNokia.jad: Makefile
	echo "MIDlet-Name: Calc"                                 >  $@
	echo "MIDlet-Vendor: Roar Lauritzsen"                    >> $@
	echo "MIDlet-Version: $(VERSION)"                        >> $@
	echo "MIDlet-Description: Scientific RPN Calculator"     >> $@
	echo "MIDlet-Icon: /Calc.png"                            >> $@
	echo "MIDlet-Info-URL: $(BASEURL)/Calc.html"             >> $@
	echo "MIDlet-Data-Size: 2048"                            >> $@
	echo "MIDlet-Jar-URL: $(BASEURL)/CalcNokia.jar"          >> $@
	echo "MIDlet-Jar-Size: 0"                                >> $@
	echo "MIDletX-LG-Contents: G7100"                        >> $@
	echo "MicroEdition-Profile: MIDP-1.0"                    >> $@
	echo "MicroEdition-Configuration: CLDC-1.0"              >> $@
	echo "MIDlet-1: Calc, /Calc.png, midpcalc.Calc"          >> $@

midp1/target/Calc.jar: $(JAVAFILES) midp1/src/midpcalc/MyCanvas.java midp1/Calc.jad resources/Calc.png
	rm -rf midp1/target
	mkdir midp1/target
	$(JAVAC) $(JFLAGS_1) -d midp1/target $(JAVAFILES) midp1/src/midpcalc/MyCanvas.java
	cd midp1 && export CLASSPATH=$(ANTENNA_PATH) && ant -buildfile build.xml -Dwtk.home=../$(WTK_1) make-jar
	touch $@

midp2/target/CalcMIDP2.jar: $(JAVAFILES) midp2/src/midpcalc/MyCanvas.java midp2/CalcMIDP2.jad resources/Calc.png
	rm -rf midp2/target
	mkdir midp2/target
	$(JAVAC) $(JFLAGS_2) -d midp2/target $(JAVAFILES) midp2/src/midpcalc/MyCanvas.java
	cd midp2 && export CLASSPATH=$(ANTENNA_PATH) && ant -buildfile build.xml -Dantennapath=$(ANTENNA_PATH) -Dwtk.home=../$(WTK_2) make-jar
	touch $@

nokia/target/CalcNokia.jar: $(JAVAFILES) nokia/src/midpcalc/MyCanvas.java nokia/CalcNokia.jad resources/Calc.png
	rm -rf nokia/target
	mkdir nokia/target
	$(JAVAC) $(JFLAGS_N) -d nokia/target $(JAVAFILES) nokia/src/midpcalc/MyCanvas.java
	cd nokia && export CLASSPATH=$(ANTENNA_PATH) && ant -buildfile build.xml -Dantennapath=$(ANTENNA_PATH) -Dwtk.home=../$(WTK_N) make-jar
	touch $@

applet/target/CalcApplet.jar: applet/src/midpcalc/CalcApplet.java $(JAVAFILES) midp1/src/midpcalc/MyCanvas.java $(MIDPFILES)
	rm -rf applet/target
	mkdir applet/target
	$(JAVAC) $(JFLAGS) -d applet/target -classpath applet/JSObject.jar applet/src/midpcalc/CalcApplet.java $(JAVAFILES) midp1/src/midpcalc/MyCanvas.java $(MIDPFILES)
	cd applet && ant -buildfile build.xml

clean:
	rm -rf $(TARGETS) midp1/target midp2/target nokia/target applet/target src/midpcalc/Real.java src/midpcalc/GFontBase.java fonts/pgm2java resources/*.dat *~ .\#* midp/*~ midp/.\#* midp-*.tgz

derived.tgz: src/midpcalc/Real.java src/midpcalc/GFontBase.java midp1/Calc.jad midp2/CalcMIDP2.jad nokia/CalcNokia.jad
	tar czf $@ $^ resources/[a-d].dat

midp-calc-$(VERSION)-src.tgz: $(JAVAFILES) $(MIDPFILES) $(FONTS) README Makefile midp1/src/midpcalc/MyCanvas.java midp1/build.xml midp1/.project midp1/.classpath midp1/.settings/org.eclipse.* midp2/src/midpcalc/MyCanvas.java midp2/build.xml midp2/.project midp2/.classpath midp2/.settings/org.eclipse.* nokia/src/midpcalc/MyCanvas.java nokia/build.xml nokia/.project nokia/.classpath nokia/.settings/org.eclipse.* applet/src/midpcalc/CalcApplet.java applet/build.xml applet/JSObject.jar applet/.project applet/.classpath applet/.settings/org.eclipse.* resources/Calc.png fonts/pgm2java.c derived.tgz
	tar czf $@ $^

publish: $(TARGETS) midp-calc-$(VERSION)-src.tgz
	cp midp1/target/Calc.jar midp1/target/Calc$(subst .,,$(VERSION)).jar
	cp midp2/target/CalcMIDP2.jar midp2/target/CalcMIDP2$(subst .,,$(VERSION)).jar
	cp nokia/target/CalcNokia.jar nokia/target/CalcNokia$(subst .,,$(VERSION)).jar
	scp $(TARGETS) midp1/target/Calc$(subst .,,$(VERSION)).jar midp2/target/CalcMIDP2$(subst .,,$(VERSION)).jar nokia/target/CalcNokia$(subst .,,$(VERSION)).jar $(HTMLFILES) doc/Calc.ico shell.sf.net:/home/groups/m/mi/midp-calc/htdocs
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
