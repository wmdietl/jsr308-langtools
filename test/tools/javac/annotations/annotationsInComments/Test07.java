/*
 * @test
 * @bug 1234567
 * @summary Annotations in comments are recognized
 * @author Werner Dietl
 * @compile/fail/ref=Test07.out -XDrawDiagnostics Test07.java
 */

// Recognized comment, unknown annotation
/*>>>
@Unknown("HiThere!")
*/
class Test07 {}
