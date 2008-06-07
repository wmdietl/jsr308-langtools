This directory contains simple regression tests for the JSR 308 parser
modifications. 

It uses the TreeDebug utility (from checkers.util) to dump the AST in a simple
text format and compares the result against an expected result using "diff";
future changes to the parser or TreeDebug may require updating the expected
result files. If it is possible for jtreg to test that the parser is correctly
building the AST, it may be better to use that in the long run.

To run the tests, run "make". (You may need to edit the Makefile to configure
the locations of the compiler and Checker Framework jar.)
