/*
 * @test
 * @bug 1234567
 * @summary Annotations in comments are recognized
 * @author Werner Dietl
 * @compile/fail/ref=Test08.out -XDrawDiagnostics -XDTA:spacesincomments Test08.java
 */

// Recognized comment, unknown annotation
/*  >>>
@Unknown("HiThere!")
*/
class Test07 {}
