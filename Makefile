default: Calc.jar

pgm2java: pgm2java.c
	gcc -o $@ $< -Wall -O2

GFontBase.java: pgm2java large.pgm medium.pgm small.pgm
	echo "abstract class GFontBase {" > $@
	pgm2java small.pgm small_ >> $@
	pgm2java medium.pgm medium_ >> $@
	pgm2java large.pgm large_ >> $@
	echo "}" >> $@

Calc.jar: Calc.java CalcCanvas.java GFont.java GFontBase.java Real.java
	javac -classpath /home/roarl/s1studio/me/emulator/j2mewtk-1_0_4-linux/lib/midpapi.zip $+
	jar cf Calc.jar Calc.class CalcCanvas.class GFont.class GFontBase.class Real.class
