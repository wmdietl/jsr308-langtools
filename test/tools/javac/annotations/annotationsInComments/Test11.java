/*
 * @test
 * @bug 1234567
 * @summary Ignore split comments; test for Checker Framework Issue 239
 * @author Werner Dietl
 * @compile/fail/ref=Test11.out -XDrawDiagnostics Test11.java
 */

// Unrecognized comment
/*@*//*Annotation(Test11a.class)*/
class Test11a {}

// Recognized comment, unknown annotation
/*@Annotation(Test11.class)*/
class Test11b {}
