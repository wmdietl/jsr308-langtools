/*
 * @test
 * @bug 1234567
 * @summary Annotations in comments are recognized
 * @author Werner Dietl
 * @compile/fail/ref=Test03.out -XDrawDiagnostics Test03.java
 */

// Recognized comment, unknown annotation
/*@Unknown("HiThere) ")*/
class Test03 {}
