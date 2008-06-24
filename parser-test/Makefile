CHECKERS_JAR=../../checkers/checkers.jar
TREEDEBUG=javac -cp $(CHECKERS_JAR) -proc:only -processor checkers.debug.TreeDebug

.PHONY: all-tests basic array-elts array-arrays

all-tests: basic array-elts array-arrays

basic:
	javac BasicTest.java
	javac ArrayTest.java

array-elts:
	$(TREEDEBUG) ArrayTest.java > elts.out
	diff -q elts.expected elts.out

array-arrays:
	jsr308_arrays="arrays" $(TREEDEBUG) ArrayTest.java > arrays.out
	diff -q arrays.expected arrays.out
