/*
 * @test
 * @bug 1234567
 * @summary SWIG-style comments are ignored
 * @author Werner Dietl
 * @compile Test02.java
 */
class Test02 {
  /*@SWIG:some/file,140,MY_CLASS@*/
  void foo() {
  }
  /*@SWIG@*/
}
