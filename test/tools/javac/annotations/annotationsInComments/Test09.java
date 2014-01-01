/*
 * @test
 * @bug 1234567
 * @summary Annotations in comments are recognized
 * @author Werner Dietl
 * @compile -XDrawDiagnostics Test09.java
 */

// Unrecognized comment
/*  >>>
@Unknown("HiThere!")
*/
class Test07 {}
