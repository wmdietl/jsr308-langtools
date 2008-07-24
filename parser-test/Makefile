CHECKERS_JAR=../../checkers/checkers.jar
TREEDEBUG=javac -cp $(CHECKERS_JAR) -proc:only -processor checkers.util.debug.TreeDebug

.PHONY: all-tests basic array-elts

all-tests: basic array-elts

basic:
	javac BasicTest.java
	javac ArrayTest.java

array-elts:
	$(TREEDEBUG) ArrayTest.java > elts.out
	diff -q elts.expected elts.out
