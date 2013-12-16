/*
 * @test
 * @bug 1234567
 * @summary Annotations in comments are recognized
 * @author Werner Dietl
 * @compile Test05.java
 */

// Unrecognized comment, newlines are not allowed
/*@Unknown("HiThere!")
*/
class Test05 {}
