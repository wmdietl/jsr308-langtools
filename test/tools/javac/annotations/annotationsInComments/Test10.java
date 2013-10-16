/*
 * @test
 * @bug 1234567
 * @summary Fully-qualified annotations in comments are recognized
 * @author Werner Dietl
 * @compile/fail/ref=Test10.out -XDrawDiagnostics Test10.java
 */

// Recognized comment, unknown annotation
/*@my.fully.qualified.Annotation*/
class Test10 {}