/*
 * @test
 * @bug 1234567
 * @summary Annotations in comments are recognized
 * @author Werner Dietl
 * @compile/fail/ref=Test06.out -XDrawDiagnostics -XDTA:spacesincomments Test06.java
 */

// Recognized comment, unknown annotation
/* @Unknown("HiThere!") */
class Test06 {}
