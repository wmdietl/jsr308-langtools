/*
 * @test
 * @bug 1234567
 * @summary Annotations in comments are recognized
 * @author Werner Dietl
 * @compile Test04.java
 */

// Unrecognized comment, ignored
/*@Unknown@*/
class Test04 {}
